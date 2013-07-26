package com.lanthaps.identime

import com.lanthaps.identime.service.LocalUserServiceImpl
import org.mockito.Mockito
import com.lanthaps.identime.repository._
import com.lanthaps.identime.model._
import org.junit.Assert
import org.mockito._
import org.apache.velocity.app.VelocityEngine
import org.springframework.ui.velocity.VelocityEngineUtils
import org.springframework.ui.velocity.VelocityEngineFactoryBean
import org.springframework.web.context.support.ServletContextResource
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.mail.javamail.JavaMailSenderImpl
import com.lanthaps.identime.exception.TokenValidityException
import com.lanthaps.identime.service.SecureTokenServiceImpl
import java.util.Date
import java.text.DateFormat
import com.lanthaps.identime.service.SettingService
import com.lanthaps.identime.service.SettingServiceImpl
import com.lanthaps.identime.service.MailServerConfigurator

class LocalUserServiceTest {

  final val future = DateFormat.getInstance().parse("26/07/33 00:00:00")
  final val past = DateFormat.getInstance().parse("01/01/01 00:00:00")
  
  @org.junit.Test def changeUserEmail () : Unit = {
    val localUserServiceImpl = new LocalUserServiceImpl();
    val userEmailRepository = Mockito.mock(classOf[UserEmailRepository])
    val fooEmail = new UserEmail()
    fooEmail.setCurrentEmailResetToken("thetoken")
    fooEmail.setEmail("original@example.org")
    localUserServiceImpl.setUserEmailRepository(userEmailRepository)
    Mockito.when(userEmailRepository.findOne("foo")).thenReturn(fooEmail)
    localUserServiceImpl.setUserEmail("foo", "new@example.org")
    Assert.assertEquals("new@example.org", fooEmail.getEmail())
    Mockito.verify(userEmailRepository).save(fooEmail)
    Assert.assertNull(fooEmail.getCurrentEmailResetToken())
  }
  
  @org.junit.Test def addUserEmail () : Unit = {
    val localUserServiceImpl = new LocalUserServiceImpl();
    val userEmailRepository = Mockito.mock(classOf[UserEmailRepository])
    val localUserRepository = Mockito.mock(classOf[LocalUserRepository])
    localUserServiceImpl.setUserEmailRepository(userEmailRepository)
    localUserServiceImpl.setLocalUserRepository(localUserRepository)
    Mockito.when(userEmailRepository.findOne("foo")).thenReturn(null)
    localUserServiceImpl.setUserEmail("foo", "new@example.org")
    val newEmailCaptor : ArgumentCaptor[UserEmail] = new ArgumentCaptor()
    Mockito.verify(userEmailRepository).save(newEmailCaptor.capture())
    Assert.assertEquals("new@example.org", newEmailCaptor.getValue().getEmail())
  }
  
  @org.junit.Test def deleteUserEmail () : Unit = {
    val localUserServiceImpl = new LocalUserServiceImpl();
    val userEmailRepository = Mockito.mock(classOf[UserEmailRepository])
    localUserServiceImpl.setUserEmailRepository(userEmailRepository)
    val fooEmail = new UserEmail()
    fooEmail.setEmail("original@example.org")
    
    Mockito.when(userEmailRepository.findOne("foo")).thenReturn(fooEmail)
    localUserServiceImpl.setUserEmail("foo", null)
    
    Mockito.verify(userEmailRepository).delete(fooEmail)
  }
  
  @org.junit.Test def isFirstUserAccurate () : Unit = {
    val localUserServiceImpl = new LocalUserServiceImpl()
    val localUserRepository = Mockito.mock(classOf[LocalUserRepository])
    Mockito.when(localUserRepository.count()).thenReturn(0,1)
    localUserServiceImpl.setLocalUserRepository(localUserRepository)
    
    Assert.assertTrue(localUserServiceImpl.isNextUserFirst())
    Assert.assertFalse(localUserServiceImpl.isNextUserFirst())
  }
  
  @org.junit.Test def sendsPasswordReset () : Unit = {
    val localUserServiceImpl = new LocalUserServiceImpl()
    val userEmailRepository = Mockito.mock(classOf[UserEmailRepository])
    localUserServiceImpl.setUserEmailRepository(userEmailRepository)
    localUserServiceImpl.setMailServiceConfigurator(Mockito.mock(classOf[MailServerConfigurator]))
    
    val mailSender = Mockito.mock(classOf[JavaMailSenderImpl])
    localUserServiceImpl.setMailSender(mailSender)
    val velocityEngineFactory = new VelocityEngineFactoryBean()
    localUserServiceImpl.setSecureTokenService(new SecureTokenServiceImpl())
    localUserServiceImpl.setVelocityEngine(velocityEngineFactory.createVelocityEngine())
    val fooEmail = new UserEmail()
    fooEmail.setEmail("original@example.org")
    localUserServiceImpl.sendPasswordReset(fooEmail)
    Mockito.verify(mailSender).send(Matchers.any[MimeMessagePreparator]())
  }
  
  @org.junit.Test def tokenValidityCheck() : Unit = {
    val localUserServiceImpl = new LocalUserServiceImpl()
    val userEmailRepository = Mockito.mock(classOf[UserEmailRepository])
    val settingService = Mockito.mock(classOf[SettingService])
    localUserServiceImpl.setSettingService(settingService)
    localUserServiceImpl.setUserEmailRepository(userEmailRepository)
    Mockito.when(userEmailRepository.findByCurrentEmailResetToken("")).thenReturn(new UserEmail())
    Mockito.when(settingService.loadIntSetting(SettingServiceImpl.tokenExpiryTime)).thenReturn(1)
    val validTokenEmail = new UserEmail()
    validTokenEmail.setTokenIssued(future)
    val expiredTokenEmail = new UserEmail()
    expiredTokenEmail.setTokenIssued(past)
    Mockito.when(userEmailRepository.findByCurrentEmailResetToken("valid")).thenReturn(validTokenEmail)
    Mockito.when(userEmailRepository.findByCurrentEmailResetToken("expired")).thenReturn(expiredTokenEmail)
    Mockito.when(userEmailRepository.findByCurrentEmailResetToken("invalid")).thenReturn(null)
    try {
      localUserServiceImpl.checkTokenValidity(null)
      Assert.fail("Null token accepted")
    } catch { case e : TokenValidityException => {} }
    try {
      localUserServiceImpl.checkTokenValidity("")
      Assert.fail("Empty token accepted")
    } catch { case e : TokenValidityException => {} }
    try {
      localUserServiceImpl.checkTokenValidity("invalid")
      Assert.fail("Invalid token accepted")
    } catch { case e : TokenValidityException => {} }
    try {
      localUserServiceImpl.checkTokenValidity("expired")
      Assert.fail("Expired token accepted")
    } catch { case e : TokenValidityException => {} }
    Assert.assertEquals(validTokenEmail, localUserServiceImpl.checkTokenValidity("valid"))
  }
}
