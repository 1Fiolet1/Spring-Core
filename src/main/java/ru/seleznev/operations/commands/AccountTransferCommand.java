package ru.seleznev.operations.commands;

import org.springframework.stereotype.Component;
import ru.seleznev.account.AccountService;
import ru.seleznev.console.ConsoleInput;
import ru.seleznev.operations.ConsoleOperationType;
import ru.seleznev.operations.OperationCommand;

@Component
public class AccountTransferCommand implements OperationCommand {
    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    public AccountTransferCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int fromAccountId = consoleInput.readPositiveInt("Enter source account id:", "source account id");
        int toAccountId = consoleInput.readPositiveInt("Enter target account id:", "target account id");
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("source and target account id must be different");
        }

        int amount = consoleInput.readPositiveInt("Enter amount:", "amount");

        accountService.transfer(fromAccountId, toAccountId, amount);
        System.out.println("Transfer completed from account " + fromAccountId + " to account " + toAccountId + ".");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
