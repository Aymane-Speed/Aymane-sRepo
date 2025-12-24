package com.example.ebank.config;

import com.example.ebank.models.*;
import com.example.ebank.repository.*;
import org.slf4j.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class SeedDataRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedDataRunner.class);

    private final AppProperties props;
    private final RoleRepository roleRepo;
    private final AppUserRepository userRepo;
    private final ClientRepository clientRepo;
    private final BankAccountRepository accountRepo;
    private final OperationRepository operationRepo;
    private final PasswordEncoder encoder;

    public SeedDataRunner(AppProperties props,
                          RoleRepository roleRepo,
                          AppUserRepository userRepo,
                          ClientRepository clientRepo,
                          BankAccountRepository accountRepo,
                          OperationRepository operationRepo,
                          PasswordEncoder encoder) {
        this.props = props;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.clientRepo = clientRepo;
        this.accountRepo = accountRepo;
        this.operationRepo = operationRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!props.isSeed()) return;

        // Ensure base roles exist (as rows)
        Role roleClient = getOrCreateRole("CLIENT");
        Role roleAgent = getOrCreateRole("AGENT_GUICHET");

        // Agent
        if (!userRepo.existsByUsername("agent")) {
            AppUser agent = new AppUser("agent", encoder.encode("agent1234"));
            agent.addRole(roleAgent);
            userRepo.save(agent);
            log.info("Seeded AGENT_GUICHET user: agent/agent1234");
        }

        // Client + account
        if (!userRepo.existsByUsername("client1")) {
            Client c = new Client();
            c.setFirstName("Client");
            c.setLastName("Demo");
            c.setNationalId("ID-DEMO-001");
            c.setBirthDate(LocalDate.of(2000, 1, 1));
            c.setEmail("client1@demo.local");
            c.setPostalAddress("Adresse démo");
            clientRepo.save(c);

            AppUser u = new AppUser("client1", encoder.encode("client1234"));
            u.setClient(c);
            u.addRole(roleClient);
            userRepo.save(u);

            BankAccount a = new BankAccount();
            a.setRib("123456789012345678901234");
            a.setOwner(c);
            a.setStatus(AccountStatus.OUVERT);
            a.setBalance(new BigDecimal("10000.00"));
            accountRepo.save(a);

            String ref = UUID.randomUUID().toString().replace("-", "");
            operationRepo.save(new Operation(a, OperationType.CREDIT, "Versement initial", new BigDecimal("10000.00"), Instant.now(), ref));

            log.info("Seeded CLIENT user: client1/client1234 + account rib=123456789012345678901234");
        }
    }

    private Role getOrCreateRole(String name) {
        return roleRepo.findByName(name)
                .orElseGet(() -> roleRepo.save(new Role(name)));
    }
}
