package com.lanthaps.identime

import org.junit.runners.JUnit4
import org.junit._
import javax.servlet.http._
import org.springframework.mock.web._
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.context.ServletContextAware
import com.lanthaps.identime.web._
import org.mockito._
import com.lanthaps.identime.service._

class CommonInterceptorTest {
  val interceptor : CommonModelInterceptor = new CommonModelInterceptor()
  
  @Before
  def setUp() : Unit = {
    
  }
  
  @org.junit.Test
  def postHandle() : Unit = {
		  val mv = new ModelAndView()
		  mv.setViewName("something");
		  var settingMock : SettingService = Mockito.mock(classOf[SettingService])
		  Mockito.when(settingMock.loadStringSetting(SettingServiceImpl.baseURL)).thenReturn("http://example.org/test/")
		  interceptor.setSettingService(settingMock)
		  interceptor.postHandle(new MockHttpServletRequest("GET", "http://example.org/test/"),
				  new MockHttpServletResponse(), new Object(),
				  mv)
		  Assert.assertEquals(mv.getModelMap().get("baseURL"), "http://example.org/test/")
  }
}