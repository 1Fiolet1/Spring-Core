package ru.seleznev.operations.commands;

import org.springframework.stereotype.Component;
import ru.seleznev.console.ConsoleInput;
import ru.seleznev.operations.ConsoleOperationType;
import ru.seleznev.operations.OperationCommand;
import ru.seleznev.user.User;
import ru.seleznev.user.UserService;

@Component
public class UserCreateCommand implements OperationCommand {
    private final UserService userService;
    private final ConsoleInput consoleInput;

    public UserCreateCommand(UserService userService, ConsoleInput consoleInput) {
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        String login = consoleInput.readRequiredString("Enter login:", "login");
        User user = userService.userCreate(login);
        System.out.println("User created: " + user);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
