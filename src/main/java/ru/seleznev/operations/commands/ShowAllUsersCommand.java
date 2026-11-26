package ru.seleznev.operations.commands;

import org.springframework.stereotype.Component;
import ru.seleznev.operations.ConsoleOperationType;
import ru.seleznev.operations.OperationCommand;
import ru.seleznev.user.UserService;

@Component
public class ShowAllUsersCommand implements OperationCommand {
    private final UserService userService;

    public ShowAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {
        String users = userService.showAllUsers();
        if (!users.isEmpty()) {
            System.out.println(users);
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
