package ru.siganov.hot_wallet.mycounter;

import java.util.HashMap;
import java.util.Map;

public class ElementCounter<T> {

    public Map<T, Integer> count(T[] elements) {
        Map<T, Integer> counterMap = new HashMap<>();
        for (T element : elements) {
            counterMap.merge(element, 1, Integer::sum);
        }
        return counterMap;
    }
}
