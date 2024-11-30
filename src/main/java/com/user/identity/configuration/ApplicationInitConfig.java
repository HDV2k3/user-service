package com.user.identity.configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.user.identity.constant.PredefinedRole;
import com.user.identity.repository.entity.Role;
import com.user.identity.repository.entity.User;
import com.user.identity.repository.RoleRepository;
import com.user.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "dacviethuynh@gmail.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.datasource",
            value = "driver-class-name",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            initDatabase(userRepository, roleRepository);
        };
    }

    @Transactional
    public void initDatabase(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application with PostgreSQL.....");

        if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            log.info("Initializing roles...");
            roleRepository.save(Role.builder()
                    .name(PredefinedRole.USER_ROLE)
                    .description("User role")
                    .build());

            Role adminRole = roleRepository.save(Role.builder()
                    .name(PredefinedRole.ADMIN_ROLE)
                    .description("Admin role")
                    .build());

            if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                log.info("Creating admin user...");
                var roles = new HashSet<Role>();
                Instant time = Instant.now();
                roles.add(adminRole);

                User user = User.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .firstName("Admin")
                        .lastName("Huynh")
                        .dayOfBirth(LocalDate.now())
                        .roles(roles)
                        .verificationToken(UUID.randomUUID().toString())
                        .enabled(true)
                        .build();
                user.setCreatedBy("System");
                user.setCreatedDate(time);
                user.setLastModifiedDate(time);
                user.setLastModifiedBy("System");
                userRepository.save(user);
                log.warn("Admin user has been created with default password: 'admin', please change it.");
            }
        }

        log.info("Application initialization with PostgreSQL completed.");
    }
}
