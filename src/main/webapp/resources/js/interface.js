
$(document).ready(function() {

    window.onload=function(){
        if (location.href.indexOf("&refresh=")<0)
        {
            location.href=location.href+"&refresh="+Math.random();
        }
    }
    //var type =$verification;
    //alert(type);
    //if(type=="expired"){
    //    swal("验证码过期了,请重新生成!","3秒后自动跳转");
    //    }
    $("#postCapthCode").click(function () {
        if($("#loginName").val()==""){
            //alert("手机号码不能为空");
            swal('手机号码不能为空!');

        }else {
            swal("发送成功!", "点击下面按钮运行测试吧!", "success");
            $.post("/pages/interface.jspa", {
                "interfaceId": $("#interfaceId").val(),
                "loginName": $("#loginName").val(),
                "postCapthCode": true
            }, function (data) {
                sleep(3000);
                $("body").html(data);
            });
        }
    });

    $("#doInterface").click(function () {
    //if($("#verification").val()=="expired"){
    //    swal("验证码过期了,请重新生成!","3秒后自动跳转");
    //    }
        $.post("/pages/interface.jspa", {
            "interfaceId": $("#interfaceId").val(),
            //"doVerification": true,
        }, function (data) {
            sleep(3000);
            $("body").html(data);
        });
    });

    function sleep(numberMillis) {
        var now = new Date();
        var exitTime = now.getTime() + numberMillis;
        while (true) {
            now = new Date();
            if (now.getTime() > exitTime)
                return;
        }
    }
});