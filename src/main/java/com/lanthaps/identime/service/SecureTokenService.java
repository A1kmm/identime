package com.lanthaps.identime.service;

/**
 * Generates short, human readable, secure random tokens. 
 * @author Andrew Miller
 */
public interface SecureTokenService {
  String makeToken();
}
