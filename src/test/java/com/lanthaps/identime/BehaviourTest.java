package com.lanthaps.identime;

import junit.framework.Assert;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.jar.Manifest;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

public class BehaviourTest {

  @Before @SuppressWarnings("unchecked")
  public void setUp() throws Exception {
      
      WebAppContext ctx = new WebAppContext("src/main/webapp", "/");
      
      Server server = new Server(9000);
      
      ctx.setServer(server);
      server.setHandler(ctx);

      ctx.preConfigure();

      ctx.addOverrideDescriptor("src/main/webapp/WEB-INF/tests-web.xml");      
      
      // Replace classloader with a new classloader with all URLs in manifests 
      // from the parent loader bubbled up so Jasper looks at them.
      ClassLoader contextClassLoader = ctx.getClassLoader();
      ClassLoader parentLoader = contextClassLoader.getParent();
      if (contextClassLoader instanceof WebAppClassLoader &&
          parentLoader instanceof URLClassLoader) {
        LinkedList<URL> allURLs =
            new LinkedList<URL>(Arrays.asList(((URLClassLoader)parentLoader).getURLs()));
        for (URL url : ((LinkedList<URL>)allURLs.clone())) {
          try {
            URLConnection conn = new URL("jar:" + url.toString() + "!/").openConnection();
            if (!(conn instanceof JarURLConnection))
              continue;
            JarURLConnection jconn = (JarURLConnection)conn;
            Manifest jarManifest = jconn.getManifest();
            String[] classPath = ((String)jarManifest.getMainAttributes().getValue("Class-Path")).split(" ");

            for (String cpurl : classPath)
              allURLs.add(new URL(url, cpurl));
          } catch (IOException e) {} catch (NullPointerException e) {}
        }
        
        ctx.setClassLoader(
            new WebAppClassLoader(
                new URLClassLoader(allURLs.toArray(new URL[]{}), parentLoader),
                ((WebAppClassLoader)contextClassLoader).getContext()));
      }
      
      server.start();
  }
  
  @org.junit.Test public void testStories() throws Exception {
    String baseURL = "http://127.0.0.1:9000";
    WebDriver driver = new FirefoxDriver();
    // An administrator deploys a new site, and goes to the site...
    driver.get(baseURL + "/");
    // They are greeted with a login page.
    Assert.assertTrue(driver.getCurrentUrl().startsWith(baseURL + "/login"));
    // They click on 'Register' to set up the first account.
    WebElement el = driver.findElement(By.linkText("Register"));
    el.click();
    new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.name("password2")));
    
    // They fill in a new username and password...
    driver.findElement(By.name("username")).sendKeys("firstuser");
    driver.findElement(By.name("password")).sendKeys("mypassword");
    driver.findElement(By.name("password2")).sendKeys("mypassword");
    // and click Register on the form to create an account.
    driver.findElement(By.cssSelector(".btn-primary")).click();
    
    // They are now given the option to set up site settings:
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.presenceOfElementLocated(By.linkText("Configure OpenID settings")));
    driver.findElement(By.linkText("Configure OpenID settings")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Site Settings Editor"));
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[td[contains(text(), 'Base URL')]]")));
    // They set up the base URL for the site.
    el = driver.findElement(By.xpath("//tr[td[contains(text(),'')]]/td//input"));
    el.clear();
    el.sendKeys(baseURL);
    // The site automatically saves settings after a period of no input...
    Thread.sleep(3000);
    // Reload to check that the setting saved... 
    driver.navigate().refresh();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Site Settings Editor"));
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[td[contains(text(), 'Base URL')]]")));
    Assert.assertEquals(baseURL,
        driver.findElement(By.xpath("//tr[td[contains(text(),'Base URL')]]/td//input")).getAttribute("value"));
    
    // Try to change password, but get the current password wrong...
    driver.findElement(By.linkText("Account Settings")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Edit Account Settings"));    
    driver.findElement(By.name("currentPassword")).sendKeys("hello");
    driver.findElement(By.name("newPassword")).sendKeys("theworld");
    driver.findElement(By.name("newPassword2")).sendKeys("theworld");
    driver.findElement(By.className("btn-primary")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.textToBePresentInElement(By.id("currentPassword.errors"), "Bad credentials"));
    
    // Try again with the correct password...
    driver.findElement(By.name("currentPassword")).sendKeys("mypassword");
    el = driver.findElement(By.name("newPassword"));
    el.clear();
    el.sendKeys("theworld");
    el = driver.findElement(By.name("newPassword2"));
    el.clear();
    el.sendKeys("theworld");
    driver.findElement(By.className("btn-primary")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.textToBePresentInElement(By.className("alert-info"),
            "Account settings change processed"));

    // Logout...
    driver.findElement(By.linkText("Logout")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Login"));
    
    // Come back for another session...
    driver.findElement(By.name("j_username")).sendKeys("firstuser");
    driver.findElement(By.name("j_password")).sendKeys("mypassword");
    driver.findElement(By.className("btn-primary")).click();
    
    // Except that they changed their password...
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Login"));

    driver.findElement(By.name("j_username")).sendKeys("firstuser");
    driver.findElement(By.name("j_password")).sendKeys("theworld");
    driver.findElement(By.className("btn-primary")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Home"));
    
    // Logout...
    driver.findElement(By.linkText("Logout")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Login"));
   
    // Now our story describes another user, an ordinary site user...

    // They want to register...
    driver.findElement(By.linkText("Register")).click();
    new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.name("password2")));
    
    // They fill in a new username and password...
    driver.findElement(By.name("username")).sendKeys("seconduser");
    driver.findElement(By.name("password")).sendKeys("mypassword");
    driver.findElement(By.name("password2")).sendKeys("mypassword");
    // and click Register on the form to create an account.
    driver.findElement(By.cssSelector(".btn-primary")).click();
    
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Home"));

    // Someone gave them a site settings URL...
    driver.navigate().to(baseURL + "/admin/settings");
    // but they aren't allowed to go there...
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("denied"));
    
    /* This is disabled for now because it needs to run somewhere where we have
     * a public IP running on port 80 (stackoverflow doesn't like using OpenID on
     * other ports).
    // Instead, they decide to try logging in somewhere
    // using their new credentials.
    driver.navigate().to("http://stackoverflow.com/users/login");
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.presenceOfElementLocated(By.linkText("Show more login options …")));
    driver.findElement(By.linkText("Show more login options …")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.presenceOfElementLocated(By.id("openid_identifier")));    
    
    el = driver.findElement(By.id("openid_identifier"));
    el.clear();
    el.sendKeys(baseURL + "/u/seconduser");
    driver.findElement(By.id("submit-button")).click();
    
    // Since they are already logged in, it should take them straight to the 'Approve site' page.
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.presenceOfElementLocated(By.className("btn-primary")));
    driver.findElement(By.id("btn-primary")).click();
    new WebDriverWait(driver, 5).until(
        ExpectedConditions.titleContains("Confirm Your New Account"));
     */
    
    driver.quit();
  }
 
}
