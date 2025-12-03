package ru.siganov.hot_wallet.mystringbuilder;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MyStringBuilder {
    private Snapshot snapshot;

    private int count = 0;

    private byte[] value;

    public MyStringBuilder() {
        this.value = new byte[0];
    }

    public MyStringBuilder(String s) {
        this.value = s.getBytes(UTF_8);
        this.count = s.length();
    }

    public MyStringBuilder append(String str) {
        if (str == null) {
            return this;
        }
        ensureCapacity(count + str.length());
        snapshot = new Snapshot(value.clone(), count);
        putBytesAt(count, str.getBytes(UTF_8));
        count+=str.length();
        return this;
    }

    public MyStringBuilder append(MyStringBuilder msb) {
        if (msb == null) {
            return this;
        }
        ensureCapacity(count + msb.length());
        snapshot = new Snapshot(value.clone(), count);
        putBytesAt(count, msb.value);
        count+=msb.length();
        return this;
    }

    public int length() {
        return count;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - value.length > 0) {
            value = Arrays.copyOf(value, minCapacity);
        }
    }

    private void putBytesAt(int offset, byte[] bytes) {
        System.arraycopy(bytes, 0, value, offset, bytes.length);
    }

    @Override
    public String toString() {
        return new String(value, UTF_8);
    }

    public void undo() {
        if (snapshot != null) {
            this.value = snapshot.bytes;
            this.count = snapshot.count;
        }
    }

    private record Snapshot(byte[] bytes, int count) {}
}
