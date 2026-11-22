package ru.seleznev.operations.commands;

import org.springframework.stereotype.Component;
import ru.seleznev.account.Account;
import ru.seleznev.account.AccountService;
import ru.seleznev.console.ConsoleInput;
import ru.seleznev.operations.ConsoleOperationType;
import ru.seleznev.operations.OperationCommand;

@Component
public class AccountWithdrawCommand implements OperationCommand {
    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    public AccountWithdrawCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readPositiveInt("Enter account id:", "account id");
        int amount = consoleInput.readPositiveInt("Enter amount:", "amount");
        Account account = accountService.withDraw(accountId, amount);
        System.out.println("Withdrew %d from account %d. New balance: %d"
                .formatted(amount, accountId, account.getMoneyAmount()));
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
