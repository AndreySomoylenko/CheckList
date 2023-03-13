package ru.samsung.case2022.db;

public class Money {
    private int rubles;
    private int cents;

    public Money(int rubles, int cents) {
        this.rubles = rubles;
        this.cents = cents;
    }

    public Money plus (Money money2) {
        int rubles = this.getRubles() + money2.getRubles();
        int cents = this.getCents() + money2.getCents();
        rubles += cents / 100;
        cents = cents % 100;
        return new Money(rubles, cents);
    }

    public Money minus (Money money2) {
        int rubles = this.getRubles() - money2.getRubles();
        int cents = Math.abs(this.getCents() - money2.getCents());
        if (this.getCents() < money2.getCents()) {
            rubles--;
        }
        return new Money(rubles, cents);
    }

    public int getRubles() {
        return rubles;
    }

    public int getCents() {
        return cents;
    }
}
