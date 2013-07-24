package com.lanthaps.identime.web;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanthaps.identime.service.CSRFTokenService;
import com.lanthaps.identime.service.SettingService;
import com.lanthaps.identime.model.SettingInformation;

/**
 * The SettingsController provides a user interface for changing the settings
 * on the service. Access to this controller should be limited to people trusted
 * to reconfigure the service.
 * @author Andrew Miller
 */
@Controller
public class SettingsController {
  public SettingsController() {}
  
  @Autowired private CSRFTokenService csrfTokenService;
  
  private SettingService settingService; 
  @Autowired void setSettingService(SettingService settingService) {
    this.settingService = settingService; 
  }
  
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/admin/settings", method = RequestMethod.GET)
  public String doSettings(Model model)
  {
    StringBuilder jsonBuilder = new StringBuilder();
    for (int i = 0; i < settingService.getAllSettings().length; i++) {
      if (i != 0)
        jsonBuilder.append(", ");
      SettingInformation<?> s = settingService.getAllSettings()[i];
      jsonBuilder.append("{\"name\": \"")
                 .append(JSONObject.escape(s.getName()))
                 .append("\", \"description\": \"")
                 .append(JSONObject.escape(s.getDescription()))
                 .append("\", \"value\": ");
      
      if (s.getType().equals(String.class))
        jsonBuilder.append('"').append(JSONObject.escape(
            settingService.loadStringSetting((SettingInformation<String>)s)))
            .append('"');
      else if (s.getType().equals(Boolean.class))
        if (settingService.loadBooleanSetting((SettingInformation<Boolean>)s))
          jsonBuilder.append("true");
        else
          jsonBuilder.append("false");
      else if (s.getType().equals(Integer.class))
        jsonBuilder.append(settingService.loadIntSetting((SettingInformation<Integer>)s));
      jsonBuilder.append("}");
    }
    model.addAttribute("settingsJSON", jsonBuilder.toString());
    return "settingsEditor";
  }
  
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/admin/saveSetting", method=RequestMethod.GET)
  public String saveSetting(HttpServletRequest req, Model model, @RequestParam("setting") String setting,
      @RequestParam("value") String value) {
    if (!csrfTokenService.checkToken(req))
      return "login";
    for (int i = 0; i < settingService.getAllSettings().length; i++) {
      SettingInformation<?> s = settingService.getAllSettings()[i];
      if (setting.equals(s.getName())) {
        if (s.getType().equals(String.class))
          settingService.saveStringSetting((SettingInformation<String>)s, value);
        else if (s.getType().equals(Boolean.class)) {
          boolean r;
          if (value == "true") {
            r = true;
          } else if (value == "false") {
            r = false;
          } else {
            model.addAttribute("rawText", "{\"error\": \"Invalid boolean value - expecting true or false\"}");
            return "raw";
          }
          settingService.saveBooleanSetting((SettingInformation<Boolean>)s, r);
        }
        else if (s.getType().equals(Integer.class)) {
          int r;
          try {
            r = Integer.parseInt(value);
          } catch (NumberFormatException e) {
            model.addAttribute("rawText", "{\"error\": \"Invalid integer value\"}");
            return "raw";
          }
          settingService.saveIntSetting((SettingInformation<Integer>)s, r);
        }
        model.addAttribute("rawText", "{}");
        return "raw";
      }
    }
    model.addAttribute("rawText", "{\"error\": \"Unknown setting\"}");
    return "raw";
  }
}
