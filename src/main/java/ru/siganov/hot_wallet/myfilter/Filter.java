package ru.siganov.hot_wallet.myfilter;

public interface Filter<T> {
    T apply(T o);
}
