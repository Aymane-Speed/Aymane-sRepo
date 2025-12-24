package com.example.ebank.service.impl;

import com.example.ebank.models.*;
import com.example.ebank.dto.agent.*;
import com.example.ebank.exception.BusinessException;
import com.example.ebank.repository.*;
import com.example.ebank.service.interfaces.AgentService;
import com.example.ebank.util.PasswordGenerator;
import com.example.ebank.util.RibValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;

@Service
public class AgentServiceImpl implements AgentService {

    private final ClientRepository clientRepo;
    private final AppUserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BankAccountRepository accountRepo;

    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder encoder;
    private final MailServiceImpl mailService;
    private final RibValidator ribValidator;

    public AgentServiceImpl(ClientRepository clientRepo,
                            AppUserRepository userRepo,
                            RoleRepository roleRepo,
                            BankAccountRepository accountRepo,
                            PasswordGenerator passwordGenerator,
                            PasswordEncoder encoder,
                            MailServiceImpl mailService,
                            RibValidator ribValidator) {
        this.clientRepo = clientRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.accountRepo = accountRepo;
        this.passwordGenerator = passwordGenerator;
        this.encoder = encoder;
        this.mailService = mailService;
        this.ribValidator = ribValidator;
    }

    @Transactional
    public CreateClientResponse createClient(CreateClientRequest req) {

        if (clientRepo.existsByNationalId(req.getNationalId())) {
            throw BusinessException.badRequest("Le numéro d’identité doit être unique");
        }
        if (clientRepo.existsByEmail(req.getEmail())) {
            throw BusinessException.badRequest("L’adresse mail doit être unique");
        }

        // 1) Create + save Client
        Client c = new Client();
        c.setFirstName(req.getFirstName().trim());
        c.setLastName(req.getLastName().trim());
        c.setNationalId(req.getNationalId().trim());
        c.setBirthDate(req.getBirthDate());
        c.setEmail(req.getEmail().trim().toLowerCase(Locale.ROOT));
        c.setPostalAddress(req.getPostalAddress().trim());

        clientRepo.save(c);

        // 2) Password: use provided one, else generate one
        boolean generated = (req.getPassword() == null || req.getPassword().trim().isEmpty());
        String rawPassword = generated ? passwordGenerator.generate(12) : req.getPassword().trim();

        // 3) Unique login + create AppUser
        String login = generateUniqueLogin(c.getFirstName(), c.getLastName());

        AppUser u = new AppUser();
        u.setUsername(login);
        u.setPasswordHash(encoder.encode(rawPassword)); // BCrypt recommended for hashing :contentReference[oaicite:2]{index=2}
        u.setEnabled(true);
        u.setClient(c);

        // 4) Role CLIENT
        // ⚠️ choisis UNE stratégie:
        // - si tu utilises hasRole('CLIENT'), Spring cherche ROLE_CLIENT par défaut :contentReference[oaicite:3]{index=3}
        // => en DB tu peux stocker "CLIENT" mais tes GrantedAuthorities doivent être "ROLE_" + name
        // ou tu stockes directement "CLIENT" et tu construis ROLE_ au moment du UserDetails.

        Role roleClient = roleRepo.findByName("CLIENT")
                .orElseGet(() -> roleRepo.save(new Role("CLIENT")));

        u.addRole(roleClient);

        userRepo.save(u);

        // 5) Email: tu peux envoyer seulement si password généré (recommandé)
        // sinon tu risques d'envoyer un mot de passe choisi par l’utilisateur (moins safe).
        if (generated) {
            mailService.sendCredentials(c.getEmail(), login, rawPassword);
        }

        return new CreateClientResponse(c.getId(), login);
    }


    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest req) {
        Client c = clientRepo.findByNationalId(req.getClientNationalId().trim())
                .orElseThrow(() -> BusinessException.badRequest("Le numéro d’identité doit exister au niveau de la base de données"));

        String rib = req.getRib().trim();
        if (!ribValidator.isValid(rib)) {
            throw BusinessException.badRequest("Le RIB doit être un RIB valide");
        }
        if (accountRepo.existsByRib(rib)) {
            throw BusinessException.badRequest("Ce RIB existe déjà");
        }

        BankAccount a = new BankAccount();
        a.setRib(rib);
        a.setOwner(c);
        a.setStatus(AccountStatus.OUVERT);
        accountRepo.save(a);

        return new CreateAccountResponse(a.getId(), a.getRib(), a.getStatus().name());
    }

    private String generateUniqueLogin(String firstName, String lastName) {
        String base = (normalize(firstName) + "." + normalize(lastName)).toLowerCase(Locale.ROOT);
        base = base.replaceAll("[^a-z0-9\\.]", "");
        if (base.length() < 3) base = "client";

        String candidate = base;
        int i = 1;
        while (userRepo.existsByUsername(candidate)) {
            candidate = base + i;
            i++;
        }
        return candidate;
    }

    private String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}", "");
    }
}
