
$(document).ready(function() {

    $("#Packaging").click(function () {

         $.post("/pages/getSDK.jspa", { "Packaging": true,"usernick": $("#usernick").val()}, function (data) {
                setTimeout("window.location.reload()", 500);
          });
    });

});