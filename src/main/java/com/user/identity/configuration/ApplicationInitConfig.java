package com.user.identity.configuration;

import java.time.Instant;
import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.user.identity.constant.PredefinedRole;
import com.user.identity.entity.Role;
import com.user.identity.entity.User;
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
    static final String ADMIN_USER_NAME = "admin";

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

        if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
            log.info("Initializing roles...");
            roleRepository.save(Role.builder()
                    .name(PredefinedRole.USER_ROLE)
                    .description("User role")
                    .build());

            Role adminRole = roleRepository.save(Role.builder()
                    .name(PredefinedRole.ADMIN_ROLE)
                    .description("Admin role")
                    .build());

            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                log.info("Creating admin user...");
                var roles = new HashSet<Role>();
                Instant time = Instant.now();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .firstName("Admin")
                        .lastName("User")
                        .dateOfBirth(Instant.parse("2003-02-08T00:00:00Z"))
                        .roles(roles)
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
