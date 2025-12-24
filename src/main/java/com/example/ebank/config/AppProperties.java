package com.example.ebank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Jwt jwt = new Jwt();
    private Rib rib = new Rib();
    private Mail mail = new Mail();
    private boolean seed = true;

    public Jwt getJwt() { return jwt; }
    public Rib getRib() { return rib; }
    public Mail getMail() { return mail; }
    public boolean isSeed() { return seed; }
    public void setSeed(boolean seed) { this.seed = seed; }

    public static class Jwt {
        private String secretB64;
        private long ttlSeconds = 3600;

        public String getSecretB64() { return secretB64; }
        public void setSecretB64(String secretB64) { this.secretB64 = secretB64; }
        public long getTtlSeconds() { return ttlSeconds; }
        public void setTtlSeconds(long ttlSeconds) { this.ttlSeconds = ttlSeconds; }
    }

    public static class Rib {
        private String regex = "^[0-9]{24}$";
        public String getRegex() { return regex; }
        public void setRegex(String regex) { this.regex = regex; }
    }

    public static class Mail {
        private boolean enabled = false;
        private String from = "no-reply@ebank.local";
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
    }
}
