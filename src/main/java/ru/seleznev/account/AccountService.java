package ru.seleznev.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.seleznev.account.exceptions.InsufficientFundsException;

import java.util.*;

@Service
public class AccountService {
    private int lastId = 1;
    private final Map<Integer, Account> accountMap = new HashMap<>();
    private final AccountProperties accountProperties;

    @Autowired
    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
    }

    public Account createAccount(int userId) {
        Account account = new Account(lastId, userId, accountProperties.getAmount());
        accountMap.put(lastId, account);
        lastId++;
        return account;
    }

    public Account deposit(int accountId, int value) {
        if (value < 0) {
            throw new InsufficientFundsException("Значение для пополнения аккаунта должно быть больше 0");
        }

        Account account = findAccountById(accountId)
                .orElseThrow( () -> new IllegalArgumentException("Не найден аккаунт с id=%s".formatted(accountId)));

        int currentMoney = account.getMoneyAmount();
        account.setMoneyAmount(currentMoney + value);

        return account;
    }

    public Account withDraw(int accountId, int value) {
        if (value < 0) {
            throw new InsufficientFundsException("Значение для снятия суммы с аккаунта должно быть больше 0");
        }

        Account account = findAccountById(accountId)
                .orElseThrow( () -> new IllegalArgumentException("Не найден аккаунт с id=%s".formatted(accountId)));

        int currentMoney = account.getMoneyAmount();

        if (currentMoney < value) {
            throw new InsufficientFundsException("Недостаточно денег на балансе");
        }

        account.setMoneyAmount(currentMoney - value);

        return account;
    }

    public void transfer(int fromAccountId, int toAccountId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма перевода не может быть меньше 0");
        }

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Нельзя сделать трансфер со одного на тот же аккаунт");
        }

        Account fromAccount = findAccountById(fromAccountId)
                .orElseThrow( () -> new IllegalArgumentException("Не найден аккаунт с id=%s".formatted(fromAccountId)));

        Account toAccount = findAccountById(toAccountId)
                .orElseThrow( () -> new IllegalArgumentException("Не найден аккаунт с id=%s".formatted(toAccountId)));

        if (amount > fromAccount.getMoneyAmount()) {
            throw new InsufficientFundsException("Недостаточно средств");
        }

        if (fromAccount.getUserId() == toAccount.getUserId()) {
            fromAccount.setMoneyAmount(fromAccount.getMoneyAmount() - amount);
            toAccount.setMoneyAmount(toAccount.getMoneyAmount() + amount);
        } else {
            fromAccount.setMoneyAmount(fromAccount.getMoneyAmount() - amount);
            toAccount.setMoneyAmount(toAccount.getMoneyAmount() + (int) (amount * (1 - accountProperties.getCommission())));
        }
    }

    public void closeAccount(int accountId) {
        Account closingAccount = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Не найден аккаунт с id=%s".formatted(accountId)));

        Account remainingAccount = accountMap.values().stream()
                .filter(account -> account.getUserId() == closingAccount.getUserId())
                .filter(account -> account.getId() != accountId)
                .min(Comparator.comparingInt(Account::getId))
                .orElseThrow(() -> new IllegalStateException("Нельзя закрыть единственный счёт пользователя"));

        int newBalance = remainingAccount.getMoneyAmount() + closingAccount.getMoneyAmount();
        remainingAccount.setMoneyAmount(newBalance);
        closingAccount.setMoneyAmount(0);
        accountMap.remove(accountId);
    }

    public Optional<Account> findAccountById(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Id аккаунты должно быть больше 0");
        }

        return Optional.ofNullable(accountMap.get(accountId));
    }
}
