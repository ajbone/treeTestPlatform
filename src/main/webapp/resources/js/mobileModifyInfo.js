
var pageSize = "10";//每页行数
var currentPage = "1";//当前页
var totalPage = "0";//总页数
$(document).ready(function() {
    queryForPages();
    function queryForPages(){
    $.ajax({
        type: "post",
        dataType: "json",
        // contentType: "application/json;charset=utf-8",
        data:{'mid':$("#mid").val()},
        url: "/machineinfolist",
        success: function callbackFun(result) {
            var info = eval("(" + result + ")");
            clearDate();
            fillTable(info);

        }
        });
    }
    

    function fillTable(info){
        totalPage = Math.ceil((info.length)/pageSize);
        listCount="";

        $("#mobileModifyInfo").html('');
        if(info.length <= pageSize){
            listCount = info.length;
        }else if((info.length-(currentPage - 1) * pageSize)>=10) {
            listCount = pageSize;
        }else{
            listCount =info.length-(currentPage - 1) * pageSize;
        }
            var mobileModifyInfo="";
        for(var i=0 ; i<listCount;i++) {
            var j = i  + (currentPage - 1) * pageSize;
            if (j < info.length) {
                mobileModifyInfo += "<tr>"
                    + "<td data-title='序号' >" + (j+1) + "</td>"
                    + "<td data-title='更改时间'>" + info[j].modifytime + "</td>"
                    + "<td data-title='更改信息'>" + info[j].info + "</td>"
            }

        }
        console.log(info.length);
        if(info.length==0){
            mobileModifyInfo ="暂无信息";

        }
        $("#mobileModifyInfo").html(mobileModifyInfo);

    }

    //清空数据
    function clearDate(){
        $("#mobileModifyInfo").html("");
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