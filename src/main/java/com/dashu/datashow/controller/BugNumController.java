package com.dashu.datashow.controller;

import com.dashu.datashow.util.TimeUtil;
import com.dashu.datashow.service.*;
import com.github.abel533.echarts.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by shenzhaohua on 4/18/16.
 */

@Controller
public class BugNumController {

    @Autowired
    private BugNumService bugNumService;

    @Autowired
    private TickNumTrendService tickNumTrendService;


    @Autowired
    private BugReportStatusService bugReportStatusService;

    @Autowired
    private BugResolutionStatusService bugResolutionStatusService;

    @Autowired
    private BugPlatformService bugPlatformService;
//
//    @Autowired
//    private TaskDisposeService taskDisposeService;


    @Autowired
    private BugCheckService bugCheckService;

    @Autowired
    private BugSeverityService bugSeverityService;

    @Autowired
    private BugFixedService bugFixedService;

    @Autowired
    private BugProjectService bugProjectService;

    @Autowired
    private BugDevService bugDevService;

    @RequestMapping(value = "/pages/index.jspa")
    public ModelAndView showBugPerProject(@RequestParam(value = "startDate" ,required = false)  String startDate,
                                          @RequestParam(value = "endDate",required = false)  String endDate,
                                          @RequestParam(value = "project",required = false)  String project,
                                          HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView("/pages/index");

//        Logger logger = Logger.getLogger(BugNumController.class);

        if(startDate==null || startDate.isEmpty() || startDate==""){
            startDate= TimeUtil.getAdjustTime(Calendar.DATE, -30).replace(" ","T");
        }

        if(endDate==null || endDate.isEmpty() || endDate==""){
            endDate = TimeUtil.getCurrentTime().replace(" ","T");
        }



        String projectName ="";

        if((project==null || project.isEmpty())){
            project = "2";

        }

//        logger.info(project);
//        logger.info(bugStatus.getBugResolutionStatus());

        //各业务线bug
        Option optionBar = bugNumService.selectBarPicture(startDate,endDate,project);
        String optBar = JSON.toJSONString(optionBar);

        //待验证BUG
        Option optionCheck=bugCheckService.selectBarPicture(startDate,endDate,project);
        String optCheck=JSON.toJSONString(optionCheck);

        //任务提交与解决数跟踪
        Option optionTrend=tickNumTrendService.selectLinePicture(startDate,endDate,project);
        String optTrend=JSON.toJSONString(optionTrend);

        //bug提交状态
        Option optionReport=bugReportStatusService.selectBarPicture(startDate,endDate,project);
        String optReport=JSON.toJSONString(optionReport);

        //bug状态分布
        Option optionPlatform=bugPlatformService.selectBarPicture(startDate,endDate,project);
        String optPlatform=JSON.toJSONString(optionPlatform);

         //每日bug
        Option optionResolution=bugResolutionStatusService.selectLinePicture(startDate,endDate,project);
        String optResolution=JSON.toJSONString(optionResolution);
//
//        //任务响应时间
//        Option optionDispose=taskDisposeService.selectBarPicture(startDate,endDate,project);
//        String optDispose=JSON.toJSONString(optionDispose);
//
        //BUG严重程度
        Option optionSeverity=bugSeverityService.selectBarPicture(startDate,endDate,project);
        String optSeverity=JSON.toJSONString(optionSeverity);

        //BUG解决天数
        Option optionFixed=bugFixedService.selectBarPicture(startDate,endDate,project);
        String optFixed=JSON.toJSONString(optionFixed);

        //哪个开发最辛苦
        Option optionDev=bugDevService.selectBarPicture(startDate,endDate,project);
        String optDev=JSON.toJSONString(optionDev);

        //项目列表
        HashMap<String, String>  optionProject=bugProjectService.selectBarPicture();

        projectName =optionProject.get(project);


//        logger.info(startDate);
//        logger.info(endDate);

        if(optCheck.equals("{}")){
            optCheck ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
        if(optBar.equals("{}")){
            optBar ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
        if(optTrend.equals("{}")){
            optTrend ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
        if(optReport.equals("{}")){
            optReport ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
        if(optPlatform.equals("{}")){
            optPlatform ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
        if(optResolution.equals("{}")){
            optResolution ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
//        if(optDispose.equals("{}")){
//            optDispose ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
//        }
        if(optSeverity.equals("{}")){
            optSeverity ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
        if(optFixed.equals("{}")){
            optFixed ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }
        if(optDev.equals("{}")){
            optDev ="{\"calculable\":true,\"legend\":{\"data\":[\"\"],\"x\":\"left\"},\"series\":[{\"data\":[0],\"itemStyle\":{\"没数据\":{\"color\":\"#1780C7\",\"label\":{\"show\":true,\"textStyle\":{\"color\":\"#DD002C\"}}}},\"markLine\":{\"data\":[{\"name\":\"平均值\",\"type\":\"average\"}]},\"markPoint\":{\"data\":[{\"name\":\"最大值\",\"type\":\"max\"},{\"name\":\"最小值\",\"type\":\"min\"}]},\"name\":\"BUG量\",\"type\":\"bar\"}],\"toolbox\":{\"feature\":{\"dataView\":{\"lang\":[\"数据视图\",\"关闭\",\"刷新\"],\"readOnly\":false,\"show\":true,\"title\":\"数据视图\"},\"magicType\":{\"show\":true,\"title\":{\"bar\":\"柱形图切换\",\"stack\":\"堆积\",\"tiled\":\"平铺\",\"line\":\"折线图切换\"},\"type\":[\"line\",\"bar\"]},\"saveAsImage\":{\"lang\":[\"点击保存\"],\"show\":true,\"title\":\"保存为图片\",\"type\":\"png\"}},\"show\":true},\"tooltip\":{\"trigger\":\"axis\"},\"xAxis\":[{\"axisLabel\":{\"interval\":0,\"rotate\":30},\"data\":[\"没数据\"],\"type\":\"category\"}],\"yAxis\":[{\"type\":\"value\"}]}";
        }

        //把所选项目和所选日期传到前端
        mv.addObject("projectName",projectName);
        mv.addObject("project",project);
        mv.addObject("startDate",startDate);
        mv.addObject("endDate",endDate);
        mv.addObject("optCheck", optCheck);
        mv.addObject("optionBar", optBar);
        mv.addObject("optTrend",optTrend);
        mv.addObject("optReport",optReport);
        mv.addObject("optPlatform",optPlatform);
        mv.addObject("optResolution",optResolution);
//        mv.addObject("optDispose",optDispose);
        mv.addObject("optSeverity",optSeverity);
        mv.addObject("optFixed",optFixed);
        mv.addObject("optProject",optionProject);
        mv.addObject("optDev",optDev);
        //取消饼图
        /*
        Option optionPie = bugNumService.selectPiePicture();
        String optPie = JSON.toJSONString(optionPie);
        mv.addObject("optionPie", optPie);*/

        //取项目名
        packageData doPackage =new packageData();
        LinkedHashMap<String,List> projectnew = new LinkedHashMap<String,List>();
        projectnew = doPackage.projectlist();
        request.setAttribute("projectlist", projectnew);
        mv.addObject("redirect_url", request.getRequestURL());
        return mv;
    }


}