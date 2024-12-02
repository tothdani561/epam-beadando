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

    @ShellMethod(key = "describe account", value = "describe account")
    public String describeAccount() {
        return userService.describe()
                .map(userDto -> "Signed in with " + (userDto.role() == User.Role.ADMIN ? "privileged" : "standard")
                        + " account '" + userDto.username() + "'")
                .orElse("You are not signed in");
    }

    @ShellMethod(key = "sign in privileged", value = "sign in privileged")
    public String signIn(String username, String password) {
        userService.logout();
        return userService.login(username, password)
                .filter(userDto -> userDto.role() == User.Role.ADMIN) // Csak ADMIN role engedélyezett
                .map(userDto -> "Admin user " + userDto.username() + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign out", value = "sign out")
    public String signOut() {
        return userService.logout()
                .map(userDto -> userDto.username() + " is logged out!")
                .orElse("You need to login first!");
    }

    @ShellMethod(key = "admin task", value = "admin task")
    public String performAdminTask() {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }
        return "Admin task performed successfully!";
    }


}
