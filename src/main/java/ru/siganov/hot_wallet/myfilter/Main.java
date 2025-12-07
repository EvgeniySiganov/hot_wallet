package ru.siganov.hot_wallet.myfilter;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayFilter<String> arrayFilter = new ArrayFilter<>();
        String[] filter = arrayFilter.filter(new String[]{"one", "two"},
                String::toUpperCase);
        System.out.println(Arrays.toString(filter));
    }
}
