package ru.samsung.case2022.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The BuysManager class
 * @author Philipp Schepnov ismail velidzhanov
 * This class is used to store list of buys for the whole project
 */

public class BuysManager {
    /**
     * List of products for the whole project
     * Creates ones at app startup
     */
    public static List<String> buys = new ArrayList<>();
    public static List<String> bag = new ArrayList<>();

    public static final Map<String, Money> prices = Map.of(
            "Печенье сладкое с маком", new Money(29, 90),
            "Капуста брокколи", new Money(249, 90),
            "Сыр полутвёрдый", new Money(359, 90),
            "Кофе растворимый с добавлением молотого", new Money(489, 90),
            "Творог мягкий 2%", new Money(144, 49),
            "Тесто замороженное дрожжевое", new Money(144, 0),
            "Молоко 3,2% пастеризованное", new Money(61, 99),
            "Блинчики с мясом", new Money(140, 80),
            "Сметана из топлёных сливок 15%", new Money(74, 90),
            "Чай чёрный листовой", new Money(506, 80)
        );

    public static Money sum;
}