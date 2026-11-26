package ru.seleznev.operations;

public interface OperationCommand {
    void execute();

    ConsoleOperationType getOperationType();
}
