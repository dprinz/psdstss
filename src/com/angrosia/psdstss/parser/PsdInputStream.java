package com.angrosia.psdstss.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PsdInputStream extends InputStream {
    private int position;
    private int positionMark;
    private final InputStream inputStream;

    public PsdInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.position = 0;
        this.positionMark = 0;
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
        position = positionMark;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }

    @Override
    public synchronized void mark(int limit) {
        inputStream.mark(limit);
        positionMark = position;
    }

    @Override
    public int read(byte[] b, int offset, int length) throws IOException {
        int res = inputStream.read(b, offset, length);
        if (res != -1) {
            position += res;
        }
        return res;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int res = inputStream.read(b);
        if (res != -1) {
            position += res;
        }
        return res;
    }

    @Override
    public int read() throws IOException {
        int res = inputStream.read();
        if (res != -1) {
            position++;
        }
        return res;
    }

    @Override
    public long skip(long n) throws IOException {
        long skip = inputStream.skip(n);
        position += skip;
        return skip;
    }

    public int getPosition() {
        return position;
    }

    public String readAsciiString(int length) throws IOException {
        byte[] bytes = new byte[length];
        //noinspection ResultOfMethodCallIgnored
        read(bytes);
        return new String(bytes, "ISO-8859-1");
    }

    public String readAsciiString() throws IOException {
        int size = readInt();
        if (size == 0) {
            return "";
        }
        return readAsciiString(size);
    }

    public String readEvenPadString() throws IOException {
        int size = readByte();
        if ((size & 0x01) == 0) {
            size++;
        }
        return readAsciiString(size);
    }

    public String readUnicodeString(int length) throws IOException {
        byte[] bytes = new byte[length * 2];
        //noinspection ResultOfMethodCallIgnored
        read(bytes);
        return new String(bytes, "UTF-16");
    }

    public String readUnicodeString() throws IOException {
        int size = readInt();
        if (size == 0) {
            return "";
        }
        return readUnicodeString(size);
    }

    public byte readByte() throws IOException {
        int ch = read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (byte) (ch);
    }

    public short readShort() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch1 << 8) + (ch2));
    }

    public int readInt() throws IOException {
        int b1 = read();
        int b2 = read();
        int b3 = read();
        int b4 = read();
        if ((b1 | b2 | b3 | b4) < 0) {
            throw new EOFException();
        }
        return ((b1 << 24) + (b2 << 16) + (b3 << 8) + (b4));
    }

    public boolean readBoolean() throws IOException {
        int ch = read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (ch != 0);
    }

    public int skipBytes(int n) throws IOException {
        int total = 0;
        int current;
        while ((total < n) && ((current = (int) skip(n - total)) > 0)) {
            total += current;
        }
        return total;
    }


}
