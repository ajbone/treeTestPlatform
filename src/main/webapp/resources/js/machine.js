
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
        url: "/machinelist",
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

        $("#machinelist").html('');
        if(info.length <= pageSize){
            listCount = info.length;
        }else if((info.length-(currentPage - 1) * pageSize)>=10) {
            listCount = pageSize;
        }else{
            listCount =info.length-(currentPage - 1) * pageSize;
        }
            var machinelist="";
        for(var i=0 ; i<listCount;i++) {
            var j = i  + (currentPage - 1) * pageSize;
            if (j < info.length) {
                machinelist += "<tr>"
                    + "<td data-title='序号'><font size='2'>" + (j+1) + "</font></td>"
                    + "<td data-title='创建时间'><font size='2'>" + info[j].createtime + "</font></td>"
                    + "<td data-title='平台'><font size='2'>" + info[j].platform + "</font></td>"
                    + "<td data-title='编号'><font size='2'>" + info[j].nub + "</font></td>"
                    + "<td data-title='手机名称'><font size='2'>" + info[j].model + "</font></td>"
                    + "<td data-title='手机版本'><font size='2'>" + info[j].os + "</font></td>"
                    + "<td data-title='借用人'><font size='2'>" + info[j].user + "</font></td>"
                    + "<td data-title='借用时间'><font size='2'>"+ info[j].modify + "</font></td>"
                    + "<td data-title='操作'><font size='2'><a href=\"/pages/editMobile.jspa?edit=" + info[j].id + "\" >编辑</a>&nbsp&nbsp<a href=\"/pages/mobileModifyInfo.jspa?mid=" + info[j].id + "\" >记录</a></font></td></tr>";

            }

        }
        $("#machinelist").html(machinelist);

    }

    //清空数据
    function clearDate(){
        $("#machinelist").html("");
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