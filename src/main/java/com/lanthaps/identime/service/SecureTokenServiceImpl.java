package com.lanthaps.identime.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

@Service
public class SecureTokenServiceImpl implements SecureTokenService {
  @Override
  public String makeToken() {
    byte[] randomByte = new byte[16];
    new SecureRandom().nextBytes(randomByte);
    randomByte = Base64.encode(randomByte);
    try {
      return new String(randomByte, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Assertion failure: UTF-8 not supported");
    }
  }

}
