package com.donphds.ma.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.nio.charset.Charset;

public class CryptoException extends RuntimeException{
  public CryptoException(String msg) {
    super(msg);
  }
  public CryptoException(Throwable e) {
    super(e);
  }
}
