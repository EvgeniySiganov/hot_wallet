package ru.siganov.hot_wallet.mystringbuilder;

import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) {
        MyStringBuilder builder = new MyStringBuilder("start");
        builder.append(new MyStringBuilder("end"));
        System.out.println("builder: " + builder);
        builder.undo();
        System.out.println("builder: " + builder);
    }
}
