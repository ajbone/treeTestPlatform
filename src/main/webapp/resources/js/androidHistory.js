


$(document).ready(function() {
    $usernick = $("#usernick").val();
    function islogin() {
        if ($usernick == ""||$usernick==null) {
            window.location.href="/pages/login.jspa?redirect_url="+window.location.href;
        }
    }

    $("#historypackaging").click(function () {
        islogin();
        $.post("/pages/getPackage.jspa", {"pid": $("#pid").val(),"pagtype": $("#pagtype").val(), "historypackaging": true,"usernick": $("#usernick").val(),"platform": $("#platform").val()}, function (data) {
            setTimeout("window.location.reload()", 500);
        });
    });
    $("#podInstall").click(function () {
        $.post("/pages/getPackage.jspa", {"pid": $("#pid").val(),"pagtype": $("#pagtype").val(), "podInstall": true,"usernick": $("#usernick").val(),"platform": $("#platform").val()}, function (data) {
            setTimeout("window.location.reload()", 500);
        });
    });
    $("#doRelease").click(function () {
        islogin();
        $.post("/pages/getPackage.jspa", {"pid": $("#pid").val(),"pagtype": $("#pagtype").val(), "doRelease": true,"usernick": $("#usernick").val(),"platform": $("#platform").val()}, function (data) {
            setTimeout("window.location.reload()", 500);
        });
    });
    $("#channelPackage").click(function () {
        islogin();
        $.post("/pages/getPackage.jspa", {"pid": $("#pid").val(),"channelArraySelect": $("#channelArraySelect").val(), "channelPackage": true,"usernick": $("#usernick").val()}, function (data) {
            setTimeout("window.location.reload()", 500);
        });
    });
    $("#jiagu_360").click(function () {
        islogin();
        $.post("/pages/getPackage.jspa", {"pid": $("#pid").val(), "jiagu_360": true,"usernick": $("#usernick").val()}, function (data) {
            setTimeout("window.location.reload()", 500);
        });
    });

    //$("#uploadPackage").click(function () {
    //    islogin();
    //    //window.location.href="/pages/uploadPackage.jspa";
    //    $.post("/pages/uploadPackage.jspa", {"pid": $("#pid").val(),"channelArraySelect": $("#channelArraySelect").val(), "uploadPackage": true}, function (data) {
    //        window.open("/pages/uploadPackage.jspa");
    //    });
    //});

});