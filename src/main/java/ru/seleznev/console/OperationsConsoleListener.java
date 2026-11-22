package ru.seleznev.console;

import org.springframework.stereotype.Component;
import ru.seleznev.account.exceptions.InsufficientFundsException;
import ru.seleznev.operations.ConsoleOperationType;
import ru.seleznev.operations.OperationCommand;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OperationsConsoleListener {
    private final Map<ConsoleOperationType, OperationCommand> commandMap;
    private final ConsoleInput consoleInput;

    public OperationsConsoleListener(List<OperationCommand> commands, ConsoleInput consoleInput) {
        this.commandMap = commands.stream()
                .collect(Collectors.toMap(OperationCommand::getOperationType, Function.identity()));
        this.consoleInput = consoleInput;
    }

    public void runBank() {
        System.out.println("MiniBank started. Type EXIT to stop.");
        consoleInput.printAvailableCommands();

        while (true) {
            try {
                ConsoleOperationType operationType = consoleInput.readOperationType();
                if (operationType == ConsoleOperationType.EXIT) {
                    break;
                }
                execute(operationType);
            } catch (EndOfInputException e) {
                break;
            }
        }

        System.out.println("MiniBank stopped.");
    }

    private void execute(ConsoleOperationType operationType) {
        OperationCommand command = commandMap.get(operationType);
        if (command == null) {
            System.out.println("Error: no command handler for " + operationType);
            return;
        }

        try {
            command.execute();
        } catch (IllegalArgumentException | IllegalStateException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
