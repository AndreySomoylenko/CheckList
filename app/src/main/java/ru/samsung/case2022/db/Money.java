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
        int cents;
        if (this.getCents() < money2.getCents()) {
            rubles--;
            cents = 100 - (money2.getCents() - this.getCents());
        } else {
            cents = this.getCents() - money2.getCents();
        }
        return new Money(rubles, cents);
    }

    public Money multiply(int times) {
        Money res = new Money(0, 0);
        for (int i = 0; i < times; i++) {
           res = res.plus(this);
        }
        return res;
    }

    public int getRubles() {
        return rubles;
    }

    public int getCents() {
        return cents;
    }

    public void makeZero() {
        rubles = 0;
        cents = 0;
    }
}
