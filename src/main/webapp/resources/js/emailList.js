
var pageSize = "10";//每页行数
var currentPage = "1";//当前页
var totalPage = "0";//总页数
$(document).ready(function() {
    queryForPages();
    function queryForPages(){
    $.ajax({
        type: "get",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        url: "/emaillist",
        success: function callbackFun(result) {
            var info = eval("(" + result + ")");
            clearDate();
            fillTable(info);

        }
        });
    }

    // $("#packaging").click(function () {
    //     $.post("/package", {"packaging": true}, function (data) {
    //         setTimeout("window.location.reload()",500);
    //     });
    // });


    function fillTable(info){
        totalPage = Math.ceil((info.length)/pageSize);
        listCount="";

        $("#emaillist").html('');
        if(info.length <= pageSize){
            listCount = info.length;
        }else if((info.length-(currentPage - 1) * pageSize)>=10) {
            listCount = pageSize;
        }else{
            listCount =info.length-(currentPage - 1) * pageSize;
        }
            var emaillist="";
        for(var i=0 ; i<listCount;i++) {
            var j = i  + (currentPage - 1) * pageSize;
            if (j < info.length) {
                emaillist += "<tr>"
                    + "<td data-title='序号'><font size='2'>" + (j+1) + "</font></td>"
                    + "<td data-title='发件人'><font size='2'>" + info[j].createname + "</font></td>"
                    + "<td data-title='发送时间'><font size='2'>" + info[j].createtime + "</font></td>"
                    + "<td data-title='主题'><font size='2'>" + info[j].project.substring(0,70) + "</font></td>"
                    + "<td data-title='操作'><font size='2'><a href=\"/pages/reportDetail.jspa?eid=" + info[j].id + "\" >详情</a></font></td></tr>";

            }

        }
        $("#emaillist").html(emaillist);

    }

    //清空数据
    function clearDate(){
        $("#emaillist").html("");
    }
    //首页
    $("#firstPage").click(function(){
        currentPage="1";
        queryForPages();
    });
//上一页
    $("#previous").click(function(){
        if(currentPage>1){
            currentPage-- ;
        }
        queryForPages();
    });
//下一页
    $("#next").click(function(){
        if(currentPage<totalPage){
            currentPage++ ;
        }
        queryForPages();
    });
//尾页
    $("#last").click(function(){
        currentPage = totalPage;
        queryForPages();
    });

});