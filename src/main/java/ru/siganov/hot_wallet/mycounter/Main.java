package ru.siganov.hot_wallet.mycounter;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ElementCounter<String> elementCounter = new ElementCounter<>();
        Map<String, Integer> count = elementCounter.count(new String[]{"one", "two", "three", "four", "five", "one"});
        System.out.println(count);
    }
}
