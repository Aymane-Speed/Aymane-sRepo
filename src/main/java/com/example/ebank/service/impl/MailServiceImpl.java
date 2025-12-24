package com.example.ebank.service.impl;

import com.example.ebank.config.AppProperties;
import com.example.ebank.service.interfaces.MailService;
import org.slf4j.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private final AppProperties props;
    private final JavaMailSender mailSender;

    public MailServiceImpl(AppProperties props, JavaMailSender mailSender) {
        this.props = props;
        this.mailSender = mailSender;
    }

    public void sendCredentials(String toEmail, String login, String rawPassword) {
        if (!props.getMail().isEnabled()) {
            log.info("[MAIL DISABLED] To={} | login={} | password={}", toEmail, login, rawPassword);
            return;
        }

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(props.getMail().getFrom());
            msg.setTo(toEmail);
            msg.setSubject("Vos accès eBank");
            msg.setText("Bonjour, Votre compte eBank a été créé. Login: " + login +
                    " Mot de passe: " + rawPassword +
                    " Veuillez changer votre mot de passe après connexion. Cordialement, eBank");

            mailSender.send(msg);
            log.info("[MAIL SENT] To={}", toEmail);
        } catch (Exception ex) {
            log.error("[MAIL FAILED] To={} | login={}", toEmail, login, ex);
            // ✅ on n'échoue PAS la création du client
        }
    }

}
