package com.pebblesifter.android;

import java.util.UUID;

public class Constants {
  public static final UUID PEBBLE_SIFTER_UUID = UUID.fromString("ACA3B3D0-BF4A-4777-9238-FF95F07AA221");

  public static final int SIFTER_PEBBLE_NAME = 0x0;
  public static final int SIFTER_TEXT = 0x1;
  public static final int SIFTER_FULL_NAME = 0x2;
  public static final int SIFTER_PEBBLE_MENU_NAME = 0x3;

  public static final int HANDSHAKE_INIT = 0x4;
  public static final int HANDSHAKE_SUCCESS = 0x5;
  public static final int HANDSHAKE_FAIL = 0x6;
}
