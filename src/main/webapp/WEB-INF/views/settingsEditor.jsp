<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/identime" prefix="local" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Settings Editor</title>
  <script type="text/javascript" src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
  <script type="text/javascript">
    "use strict";
    var settingsData = [${settingsJSON}];
    $(function() {
      for (var i in settingsData) {
        (function () {
    	  var setting = settingsData[i];
    	  var settingRow = $("<tr/>");
    	  settingRow.append($("<td/>").append(setting.description));
          var settingInput = $("<input class='setting-value' type='text'/>");
          settingInput.val(setting.value);
          function ensureSaved() {
        	  if (settingInput.pendingSave == settingInput.val())
        		  return;
              if (settingInput.pendingSave == null && settingInput.val() == setting.value)
            	  return;
              settingInput.pendingSave = settingInput.val();
              $("#status").text("Save in progress...");
              <%// We use a relative URI here so a broken base URI doesn't lock the admin out. %>
              $.ajax({ url: "saveSetting", data: { type: setting.type, setting: setting.name,
                value: settingInput.pendingSave, csrfToken: $("#csrf").val() },
            	    error: function() { $("#status").text("Temporary problem saving setting"); },
            	    dataType: "json",
            	    success: function(ret) {
            	    	if (ret.error != null) {
            	    		$("#status").text("Invalid value: " + ret.error)
            	    		settingInput.addClass("invalid-value");
            	    	} else {
            	    		settingInput.removeClass("invalid-value");
            	    		$("#status").text("Changes saved");
            	    	}
            	    }
            	     });
          };
          settingInput.keypress(function() {
        	  $("#status").text("Settings changed");
        	  settingInput.removeClass("invalid-value");
              if (settingInput.changeTimer) window.clearTimeout(settingInput.changeTimer);
              settingInput.changeTimer = window.setTimeout(ensureSaved, 1000);
          	  });
          settingInput.blur(ensureSaved);
          settingRow.append($("<tr/>").append(settingInput));
          $('#settings').append(settingRow);
        })();
      }
    });
  </script>
</head>
<body>
  <form><local:csrfProtect/></form>
  <h2>Change your settings</h2>
  <p id="status"></p>
  <table border="1" id="settings">
    <tr><th>Setting</th><th>Value</th></tr>
  </table>
</body>
</html>