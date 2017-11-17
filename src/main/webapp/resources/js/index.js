$(document).ready(function() {

    $("#indexBussnissBug").click(function () {
        $.post("/pages/index.jspa", {"startDate": $("#startDate").val(), "endDate": $("#endDate").val(),"project":$("#project").val() }, function (data) {
            //图表
            //console.log(data);
            //alert($("#startDate").val());
            //查询
            function loadDrugs() {
                $("body").html(data);
            }
            //载入图表
            loadDrugs();
        });
    });
    //$('.datepicker').pickadate({
    //    selectMonths: true,
    //    selectYears: 15
    //});

});