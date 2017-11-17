
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
        url: "/userList",
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

        $("#userList").html('');
        if(info.length <= pageSize){
            listCount = info.length;
        }else if((info.length-(currentPage - 1) * pageSize)>=10) {
            listCount = pageSize;
        }else{
            listCount =info.length-(currentPage - 1) * pageSize;
        }
            var userList="";
        for(var i=0 ; i<listCount;i++) {
            var j = i  + (currentPage - 1) * pageSize;
            if (j < info.length) {
                userList += "<tr>"
                    + "<td data-title='序号'><font size='2'>" + (j+1) + "</font></td>"
                    + "<td data-title='登录名'><font size='2'>" + info[j].username + "</font></td>"
                    + "<td data-title='用户昵称'><font size='2'>" + info[j].usernick + "</font></td>"
                    + "<td data-title='描述'><font size='2'>" + info[j].discription.substring(0,20) + "</font></td>"
                    + "<td data-title='邮箱地址'><font size='2'>" + info[j].email+ "</font></td>"
                    + "<td data-title='用户组'><font size='2'>" + info[j].groupname + "</font></td>"
                    + "<td data-title='头像'><font size='2'>" + info[j].photo + "</font></td>"
                    + "<td data-title='操作'><font size='2'><a href=\"/pages/addUser.jspa?eid=" + info[j].uid + "\" >编辑</a>&nbsp;&nbsp;<a href=\"/AddUserServlet?isdelete=yes&did=" + info[j].uid + "\" >删除</a></font></td></tr>";

            }

        }
        $("#userList").html(userList);

    }

    //清空数据
    function clearDate(){
        $("#userList").html("");
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