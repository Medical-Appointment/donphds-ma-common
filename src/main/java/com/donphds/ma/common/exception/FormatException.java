package com.donphds.ma.common.exception;

public class FormatException extends RuntimeException {
  public FormatException(String msg) {
    super(msg);
  }

  public FormatException(Throwable e) {
    super(e);
  }
}
