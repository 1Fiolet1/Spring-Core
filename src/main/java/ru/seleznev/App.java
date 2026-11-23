package ru.seleznev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.seleznev.config.ApplicationConfiguration;
import ru.seleznev.console.OperationsConsoleListener;

public class App {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
            context.getBean(OperationsConsoleListener.class).runBank();
        }
    }
}
