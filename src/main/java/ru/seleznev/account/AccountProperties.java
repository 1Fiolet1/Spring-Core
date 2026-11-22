package ru.seleznev.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountProperties {
    private final int amount;
    private final double commission;

    @Autowired
    public AccountProperties(@Value("${account.default-amount}") int amount,
                             @Value("${account.transfer-commission}") double commission)
    {
        if (amount < 0) {
            throw new IllegalArgumentException("Начальный баланс должен быть больше нуля");
        }
        if (!Double.isFinite(commission) || commission < 0 || commission > 1) {
            throw new IllegalArgumentException("Комиссия должна быть в диапазоне от 0 до 1");
        }
        this.amount = amount;
        this.commission = commission;
    }

    public int getAmount() {
        return amount;
    }

    public double getCommission() {
        return commission;
    }
}
