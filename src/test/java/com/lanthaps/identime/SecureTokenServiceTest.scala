package com.lanthaps.identime

import org.junit.runners.JUnit4
import org.junit._
import com.lanthaps.identime.service.SecureTokenServiceImpl

class SecureTokenServiceTest {
  private val secureTokenService = new SecureTokenServiceImpl();
  
  @org.junit.Test
  def checkTokenLongEnough() : Unit =
    Assert.assertTrue(secureTokenService.makeToken().size > 8);
  
  @org.junit.Test
  def checkTokenUnique() : Unit =
    Assert.assertEquals(100,
        Range(0,100).map(_ => secureTokenService.makeToken())
        .distinct.size)
}
