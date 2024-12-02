package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.persistence.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class UserCommand {

    private final UserService userService;

    @ShellMethod(key = "sign in privileged", value = "Sign in as privileged user")
    public String signInPrivileged(String username, String password) {
        userService.logout();
        return userService.login(username, password)
                .filter(userDto -> userDto.role() == User.Role.ADMIN) // Csak ADMIN role engedélyezett
                .map(userDto -> "Admin user " + userDto.username() + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "admin task", value = "Perform an admin-only task")
    public String performAdminTask() {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }
        return "Admin task performed successfully!";
    }


}
