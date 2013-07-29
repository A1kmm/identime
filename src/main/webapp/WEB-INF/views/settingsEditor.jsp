<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/identime" prefix="local" %>
<local:site-theme title="Site Settings Editor" customBase="..">
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
              <%-- We use a relative URI here so a broken base URI doesn't lock the admin out. --%>
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
          settingRow.append($("<td/>").append(settingInput));
          $('#settings').append(settingRow);
        })();
      }
    });
  </script>
  <form><local:csrfProtect/></form>
  <div class="alert alert-info" id="status"></div>
  <table class="table table-striped table-hover table-bordered">
    <thead><tr><th style="width: 40%">Setting</th><th>Value</th></tr></thead>
    <tbody id="settings">
    </tbody>
  </table>
</body>
</html>
</local:site-theme>