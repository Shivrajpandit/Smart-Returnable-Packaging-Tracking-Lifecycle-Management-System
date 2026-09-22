package com.packaging;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PackagingTrackingApplicationTests {

    @Test
    void testPasswordHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("admin123", "$2a$10$WxbaAPyL77tgE3Vs1Yibc./HpGPCzfpkga2nwOiF7BcD4FevhoP0q"));
        assertTrue(encoder.matches("staff123", "$2a$10$nSksTJ70QARhIjB7teqwWOUD1ALismu6pCQL3NzUKtZA3AyIvSZza"));
        assertTrue(encoder.matches("manager123", "$2a$10$gfjCkwy6myW7GY2wt6sC6e8FyzwzwGlqGCs04639d4ssOrO3H3dRi"));
    }
}


