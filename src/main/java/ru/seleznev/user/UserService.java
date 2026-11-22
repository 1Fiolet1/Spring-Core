package ru.seleznev.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Service;
import ru.seleznev.account.Account;
import ru.seleznev.account.AccountService;

import java.util.*;

@Service
public class UserService {
    private int lastId = 1;
    private final Map<Integer, User> userMap = new HashMap<>();
    private final Set<String> takenLogins = new HashSet<>();
    private final AccountService accountService;

    @Autowired
    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public User userCreate(String login) {
        if (takenLogins.contains(login)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже есть в системе");
        }
        Account account = accountService.createAccount(lastId);
        User user = new User(lastId, login, new ArrayList<>(List.of(account)));

        takenLogins.add(login);
        userMap.put(lastId, user);
        lastId++;

        return user;
    }

    public String showAllUsers() {
        String allUsersList = userMap.values().toString();
        System.out.println(allUsersList);
        return allUsersList;
    }

    public Account createAndLinkAccount(int userId) {
        User user = findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Не найден пользователь с id=%s".formatted(userId)));
        Account account = accountService.createAccount(userId);
        user.getAccountList().add(account);
        return account;
    }

    public void closeAndUnlinkAccount(int accountId) {
        Account account = accountService.findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Не найден аккаунт с id=%s".formatted(accountId)));

        User user = userMap.get(account.getUserId());

        accountService.closeAccount(accountId);
        if (user != null) {
            user.getAccountList().remove(account);
        }
    }

    public Optional<User> findUserById(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Id пользователя должно быть больше 0");
        }

        return Optional.ofNullable(userMap.get(userId));
    }
}
