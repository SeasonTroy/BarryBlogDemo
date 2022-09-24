package com.bootdang.shiro;

import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

public class MySimpleByteSource extends SimpleByteSource implements Serializable {

    private static final long serialVersionUID = -5590536205159769465L;

    public MySimpleByteSource (byte[] bytes) {
        super(bytes);
    }

    public MySimpleByteSource (char[] chars) {
        super(chars);
    }

    public MySimpleByteSource (String string) {
        super(string);
    }

    public MySimpleByteSource (ByteSource source) {
        super(source);
    }

    public MySimpleByteSource (File file) {
        super(file);
    }

    public MySimpleByteSource (InputStream stream) {
        super(stream);
    }

    @Override
    public byte[] getBytes () {
        return super.getBytes();
    }

    @Override
    public boolean isEmpty () {
        return super.isEmpty();
    }

    @Override
    public String toHex () {
        return super.toHex();
    }

    @Override
    public String toBase64 () {
        return super.toBase64();
    }

    @Override
    public String toString () {
        return super.toString();
    }

    @Override
    public int hashCode () {
        return super.hashCode();
    }

    @Override
    public boolean equals (Object o) {
        return super.equals(o);
    }
}
