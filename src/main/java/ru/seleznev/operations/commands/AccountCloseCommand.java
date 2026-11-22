package ru.seleznev.operations.commands;

import org.springframework.stereotype.Component;
import ru.seleznev.console.ConsoleInput;
import ru.seleznev.operations.ConsoleOperationType;
import ru.seleznev.operations.OperationCommand;
import ru.seleznev.user.UserService;

@Component
public class AccountCloseCommand implements OperationCommand {
    private final UserService userService;
    private final ConsoleInput consoleInput;

    public AccountCloseCommand(UserService userService, ConsoleInput consoleInput) {
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readPositiveInt("Enter account id to close:", "account id");
        userService.closeAndUnlinkAccount(accountId);
        System.out.println("Account " + accountId + " closed.");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
