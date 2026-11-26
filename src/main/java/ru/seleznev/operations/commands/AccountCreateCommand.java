package ru.seleznev.operations.commands;

import org.springframework.stereotype.Component;
import ru.seleznev.account.Account;
import ru.seleznev.console.ConsoleInput;
import ru.seleznev.operations.ConsoleOperationType;
import ru.seleznev.operations.OperationCommand;
import ru.seleznev.user.UserService;

@Component
public class AccountCreateCommand implements OperationCommand {
    private final UserService userService;
    private final ConsoleInput consoleInput;

    public AccountCreateCommand(UserService userService, ConsoleInput consoleInput) {
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int userId = consoleInput.readPositiveInt("Enter user id:", "user id");
        Account account = userService.createAndLinkAccount(userId);
        System.out.println("Account created: " + account);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
