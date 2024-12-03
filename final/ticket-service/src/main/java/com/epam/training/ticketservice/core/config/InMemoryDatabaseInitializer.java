package com.epam.training.ticketservice.core.config;

import com.epam.training.ticketservice.core.user.persistence.User;
import com.epam.training.ticketservice.core.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InMemoryDatabaseInitializer {

    private final UserRepository userRepository;

    @PostConstruct
    public void initializeAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", "admin", User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created: admin / admin");
        }
    }
}
