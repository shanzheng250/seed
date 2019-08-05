package com.shanz.api.connection;

import com.shanz.api.spi.SPI;

@SPI("cipher")
public interface ICipher {

    byte[] decrypt(byte[] data);

    byte[] encrypt(byte[] data);

}
