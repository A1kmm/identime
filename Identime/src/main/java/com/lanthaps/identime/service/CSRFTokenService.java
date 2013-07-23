package com.lanthaps.identime.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

/**
 * This service generates and checks tokens on forms to make sure that the form
 * is not a cross-site request forgery (CSRF). 
 * @author Andrew Miller
 */
public class CSRFTokenService {
  public static final String TOKEN_SESSION = "csrfToken";
  public static final String TOKEN_FORM = "csrfToken";
  
  private static Logger logger = LoggerFactory.getLogger(CSRFTokenService.class); 
  
  public static String getSessionToken(HttpSession session) {
    logger.info("Getting session token, session = " + ((session==null)?"null":session.toString()));
    String token = (String)session.getAttribute(TOKEN_SESSION);
    if (token != null)
      return token;
    synchronized(session) {
      token = (String)session.getAttribute(TOKEN_SESSION);
      if (token != null)
        return token;
      byte[] randomByte = new byte[16];
      new SecureRandom().nextBytes(randomByte);
      randomByte = Base64.encode(randomByte);
      try {
        String newToken = new String(randomByte, "UTF-8");
        session.setAttribute(TOKEN_SESSION, newToken);
        return newToken;
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Assertion failure: UTF-8 not supported");
      }
    }
  }

  public static boolean checkToken(HttpServletRequest req) {
    logger.info("Comparing tokens " + getSessionToken(req.getSession()) + " and " +
        req.getParameter(TOKEN_FORM));
    return getSessionToken(req.getSession()).equals(req.getParameter(TOKEN_FORM));
  }
}
