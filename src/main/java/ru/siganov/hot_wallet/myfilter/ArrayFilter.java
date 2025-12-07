package ru.siganov.hot_wallet.myfilter;

import java.util.Arrays;

public class ArrayFilter<T> {
    public T[] filter(T[] array, Filter<T> filter) {
        T[] result = Arrays.copyOf(array, array.length);
        Arrays.setAll(result, i -> filter.apply(array[i]));
        return result;
    }
}
