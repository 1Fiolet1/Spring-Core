package ru.seleznev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.seleznev.account.AccountService;
import ru.seleznev.config.ApplicationConfiguration;
import ru.seleznev.user.UserService;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        UserService userService = context.getBean(UserService.class);
        AccountService accountService = context.getBean(AccountService.class);

        userService.userCreate("Fiolet");
        userService.showAllUsers();


    }
}
