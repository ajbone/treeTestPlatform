
$(document).ready(function() {

    $("#runTask").click(function () {

         $.post("/pages/taskRun.jspa", {"taskId": $("#taskId").val(), "runTask": true}, function (data) {
                setTimeout("window.location.reload()", 500);
          });
    });


});