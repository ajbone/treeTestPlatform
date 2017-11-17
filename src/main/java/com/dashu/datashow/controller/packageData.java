package com.dashu.datashow.controller;



import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by shenzhaohua on 16/7/20.
 */
public class packageData {
    static packageDB db1 = null;
    static ResultSet ret = null;


    public void  executePackage (String sql){


     //连接数据库查数据
        db1 = new packageDB(sql);//创建packageDB对象

        try {
            db1.pst.execute();//执行语句，得到结果集
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //执行select语句
    public int selectPackage (String sql){
        int count =0;
        db1 = new packageDB(sql);//创建packageDB对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                count = ret.getInt(1);
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;

    }
    //执行select语句
    public String selectStatus (String sql){
        String jobstatus ="";
        db1 = new packageDB(sql);//创建packageDB对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                jobstatus = ret.getString(1);
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobstatus;

    }

    //执行machine单个信息
    public String selectMachineInfo (String sql){
        String machineInfo ="";
        db1 = new packageDB(sql);//创建packageDB对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                machineInfo = ret.getString(1)+"|"+ret.getString(2)+"|"+ret.getString(3)+"|"+ret.getString(4)+"|"+ret.getString(5)+"|"+ret.getString(6)+"|"+ret.getString(7)+"|"+ret.getString(8);
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return machineInfo;

    }

    //自定义结果数据个数
    public String selectResult (String sql,int count){
        String resultInfo ="";
        db1 = new packageDB(sql);//创建packageDB对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                for(int i=1;i<=count;i++) {
                    if (i == 1) {
                        resultInfo = ret.getString(i);
                    } else {
                        resultInfo = resultInfo + "|" + ret.getString(i);
                    }
                }
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInfo;

    }

    //查询project信息
    public String selectProjectInfo (String sql){
        String ProjectInfo ="";
        db1 = new packageDB(sql);//创建packageDB对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                ProjectInfo = ret.getString(1)+"|"+ret.getString(2)+"|"+ret.getString(3)+"|"+ret.getString(4)+"|"+ret.getString(5)+"|"+ret.getString(6)+"|"+ret.getString(7)+"|"+ret.getString(8)+"|"+ret.getString(9);
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ProjectInfo;

    }


    public int getBuildNumber(String curl) throws  IOException {

        String ls_str;
        int buildNumber = 0;
        Process ls_proc = Runtime.getRuntime().exec(curl);
        DataInputStream ls_in = new DataInputStream(
                ls_proc.getInputStream());

        try {
            while ((ls_str = ls_in.readLine()) != null) {
                if(ls_str.equals("<html>")){
                    ls_str ="0";
                    break;
                }
                buildNumber = Integer.parseInt(ls_str);
            }
        } catch (IOException e) {
        }
        return buildNumber;
    }

    public int getBuildStatus(String curl) throws  IOException {

        String ls_str;
        int buildstatus = 0;
        Process ls_proc = Runtime.getRuntime().exec(curl);
        DataInputStream ls_in = new DataInputStream(
                ls_proc.getInputStream());

        try {
            while ((ls_str = ls_in.readLine()) != null) {
                if(ls_str.equals("<html>")){
                    ls_str ="0";
                    break;
                }
                if (ls_str.contains("SUCCESS")) {
                    buildstatus =1;
                }else if (ls_str.contains("FAILURE")) {
                    buildstatus =2;
                }else if (ls_str.contains("ABORTED")){
                    buildstatus =3;
                }
            }
        } catch (IOException e) {
        }
        return buildstatus;
    }

    //获取apk名字
    public  static  String getApkName(String apkPath,String keyword) throws  IOException {
        String apkName ="";
        try {
            File f = new File(apkPath);
            for(File temp : f.listFiles()) {
                int ind=temp.getName().indexOf(keyword);
                if(ind !=-1)
                {
                    apkName = apkName+","+temp.getName();

                }
            }
        } catch (Exception e) {
        }
        if(apkName !=""){
            apkName =apkName.substring(1);
        }
        return apkName;
    }

    //获取包含关键字的文件名
    public  static  String getFileName(String Path,String keyword) throws  IOException {
        String fileName ="";
        try {
            File f = new File(Path);
            for(File temp : f.listFiles()) {
                if (temp.getName().contains("jiagu_temp")) {
                    continue;
                }
                int ind=temp.getName().indexOf(keyword);
                if(ind !=-1)
                {
                    fileName = temp.getName();

                }
            }
        } catch (Exception e) {
        }
        return fileName;
    }
    //获取文件的最后编辑时间
    public static  String getFileModifyTime(String path){
        File f = new File(path);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(f.lastModified());
        return sdf.format(cal.getTime());
    }


    //创建文件夹
    public boolean createDirectory(String directory) {
        boolean createok = false;
        File file =new File(directory);
        //如果文件夹不存在则创建
        if  (!file .exists()  && !file .isDirectory())
        {
            file .mkdir();
            file.setWritable(true,false);
            file.setReadable(true,false);
            file.setExecutable(true,false);
            createok =true;
        } else
        {
            System.out.println(directory+"目录已存在");
        }
        return createok;

    }

    //创建文件
    public static void createFile(String fileName,String content) throws  IOException{
        File file =new File(fileName);
        //如果文件夹不存在则创建,存在就删掉 在创建
        if(!file.exists()){
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        file.setWritable(true,false);
        file.setReadable(true,false);
        file.setExecutable(true,false);
        FileWriter fw=new FileWriter(fileName);
        fw.write(content);
        fw.flush();
        fw.close();

    }

    //读取文件内容
    public String readFileContent(String path) throws  IOException {
        String  Content ="";
        try {
            File f = new File(path);
            if(f.isFile()&&f.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
                BufferedReader in = new BufferedReader(read);
                String line = null;
                String str = "";
                while ((line = in.readLine()) != null) {
                    str += line;
                }
                Content = str;
                read.close();
            }
        } catch (IOException e) {
        }
        return Content;
    }

    /***
     * 方法：
     * @Title: replaceContentToFile
     * @Description: TODO
     * @param @param path 文件
     * @param @param str 开始删除的字符
     * @param @param con  追加的文本
     * @return void    返回类型
     * @throws
     */
    public static void replaceContentToFile(String path, String outPath ,String str, String str02,String con){
        try {
            FileReader read = new FileReader(path);
            BufferedReader br = new BufferedReader(read);
            StringBuilder content = new StringBuilder();
            String part ="";

            while(br.ready() != false){
                content.append(br.readLine());
                content.append("\r\n");
            }
            int dex = content.indexOf(str);
            int dex02 = content.indexOf(str02);
            if( dex != -1){
                part =content.substring(dex+str.length(), content.length());
                content.delete(dex, content.length());
                content.append(con);
                content.append(part);
            }else if( dex02 != -1){
                part =content.substring(dex02+str.length(), content.length());
                content.delete(dex02, content.length());
                content.append(con);
                content.append(part);
            }
            br.close();
            read.close();
            FileOutputStream fs = new FileOutputStream(outPath);
            fs.write(content.toString().getBytes());
            fs.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e){
            e.printStackTrace();

        }
    }

    //取配置文件中的版本号
    public static  String getIosVersion(String path ,String str, String str02){
        String part ="";
        String version ="";
        try {
            FileReader read = new FileReader(path);
            BufferedReader br = new BufferedReader(read);
            StringBuilder content = new StringBuilder();

            while(br.ready() != false){
                content.append(br.readLine());
                content.append("\r\n");
            }
            int dex = content.indexOf(str);
            if( dex != -1){
                part =content.substring(dex+str.length(), content.length());
            }
            int dex02 = part.indexOf(str02);
            if( dex02 != -1){
                version =part.substring(0,dex02);
            }
            br.close();
            read.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e){
            e.printStackTrace();

        }
        return version;

    }

    public LinkedHashMap<String,List> selectList(String sql,int count) {
        LinkedHashMap<String,List> coll = new LinkedHashMap<String,List>();

        db1 = new packageDB(sql);//创建packageDB对象

        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                List<String> list = new ArrayList<String>();
                for(int i=2;i<=count;i++){
                    list.add(ret.getString(i));
                }
                coll.put(ret.getString(1),list);
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coll;
    }

    //搜索结果存入map,Map<String,String>
    public LinkedHashMap<String,String> selectMapList(String sql) {
        LinkedHashMap<String,String> coll = new LinkedHashMap<String,String>();

        db1 = new packageDB(sql);//创建packageDB对象

        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                coll.put(ret.getString(1),ret.getString(2));
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coll;
    }


    public String configFile(String svn,String gitbranches,String WEB_ROOT,String JENKINS_APP_PATH,String APK_DIRECTORY,String packagetype,String platform,String alias,String email,String path,String apkPath,String credentials){
        String content ="";
        String target ="";
        String svnSplit ="";
        String ios_content_02 = "";
        String kaixindaiConfig ="";
        String content_01 ="<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<project>\n" +
                "  <actions/>\n" +
                "  <description>hello android</description>\n" +
                "  <keepDependencies>false</keepDependencies>\n" +
                "  <properties/>\n" +
                "  <scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.2.0\">\n" +
                "    <configVersion>2</configVersion>\n" +
                "    <userRemoteConfigs>\n" +
                "      <hudson.plugins.git.UserRemoteConfig>\n" +
                "        <url>";
        String content_02 =svn+"</url>\n" +
                "        <credentialsId>"+credentials+"</credentialsId>\n" +
//                "        <credentialsId>641268f1-09ae-4dd4-ab59-a6ad05f2f605</credentialsId>\n" +
//                "        <credentialsId>2b22e99e-1531-453c-b179-21d71cbdae67</credentialsId>\n" +
                "      </hudson.plugins.git.UserRemoteConfig>\n" +
                "    </userRemoteConfigs>\n" +
                "    <branches>\n" +
                "      <hudson.plugins.git.BranchSpec>\n" +
                "        <name>*/"+gitbranches+"</name>\n" +
                "      </hudson.plugins.git.BranchSpec>\n" +
                "    </branches>\n" +
                "    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
                "    <submoduleCfg class=\"list\"/>\n" +
                "    <extensions/>\n" +
                "  </scm>\n" +
                "  <canRoam>true</canRoam>\n" +
                "  <disabled>false</disabled>\n" +
                "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n" +
                "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n" +
                "  <authToken>12345</authToken>\n" +
                "  <triggers/>\n" +
                "  <concurrentBuild>false</concurrentBuild>\n" +
                "  <builders>\n" +
                "    <hudson.tasks.Shell>\n" +
                "      <command>#!/bin/bash\n"+
                "git log -10 | grep -v  &quot;Merge branch &apos;"+gitbranches+"&apos;&quot; &gt;&gt;"+APK_DIRECTORY+"/${BUILD_NUMBER}/gitlog.txt</command>\n" +
                "    </hudson.tasks.Shell>\n" +
                "    <hudson.plugins.gradle.Gradle plugin=\"gradle@1.26\">\n" +
                "      <switches>clean</switches>\n" +
                "      <tasks>";
        String content_03=packagetype+"</tasks>\n" +
                "      <rootBuildScriptDir>${WORKSPACE}</rootBuildScriptDir>\n" +
                "      <buildFile>${WORKSPACE}/"+apkPath+"/build.gradle</buildFile>\n" +
                "      <gradleName>Gradle-3.3</gradleName>\n" +
                "      <useWrapper>false</useWrapper>\n" +
                "      <makeExecutable>false</makeExecutable>\n" +
                "      <useWorkspaceAsHome>false</useWorkspaceAsHome>\n" +
                "      <wrapperLocation>${WORKSPACE}</wrapperLocation>\n" +
                "      <passAsProperties>false</passAsProperties>\n" +
                "    </hudson.plugins.gradle.Gradle>\n" +
                "    <hudson.tasks.Shell>\n" +
                "      <command>" +
                "mv "+JENKINS_APP_PATH+"/"+apkPath+"/build/outputs/apk/* "+APK_DIRECTORY+"/${BUILD_NUMBER}/apk/\n" +
//                "cd "+APK_DIRECTORY+"/${BUILD_NUMBER}/apk/\n" +
//                "echo 'Txy30330'|sudo -S chmod -R 777 *\n" +
                "</command>\n" +
                " </hudson.tasks.Shell>\n" +
                "  </builders>\n" +
                "  <publishers>\n"+
                "    <hudson.plugins.emailext.ExtendedEmailPublisher plugin=\"email-ext@2.60\">\n" +
                "      <recipientList>"+email+"</recipientList>\n" +
                "      <configuredTriggers>\n" +
                "        <hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>\n" +
                "          <email>\n" +
                "            <recipientList>"+email+"</recipientList>\n" +
                "            <subject>$PROJECT_DEFAULT_SUBJECT</subject>\n" +
                "            <body>$PROJECT_DEFAULT_CONTENT</body>\n" +
                "            <recipientProviders>\n" +
                "              <hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider/>\n" +
                "              <hudson.plugins.emailext.plugins.recipients.ListRecipientProvider/>\n" +
                "            </recipientProviders>\n" +
                "            <attachmentsPattern></attachmentsPattern>\n" +
                "            <attachBuildLog>false</attachBuildLog>\n" +
                "            <compressBuildLog>false</compressBuildLog>\n" +
                "            <replyTo>$PROJECT_DEFAULT_REPLYTO</replyTo>\n" +
                "            <contentType>text/html</contentType>\n" +
                "          </email>\n" +
                "        </hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>\n" +
                "      </configuredTriggers>\n" +
                "      <contentType>text/html</contentType>\n" +
                "      <defaultSubject>$PROJECT_NAME - 打包状态 - $BUILD_STATUS</defaultSubject>\n" +
                "      <defaultContent>&lt;b&gt;&lt;font color = &quot;#CD5C5C&quot;&gt;(本邮件是程序自动下发的，请勿回复！)&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建通知:$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;项目名称：$PROJECT_NAME&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;git版本号：${GIT_REVISION}&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建状态：$BUILD_STATUS&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;触发原因：${CAUSE}&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建日志地址：&lt;a href=&quot;${BUILD_URL}console&quot;&gt;${BUILD_URL}console&lt;/a&gt;&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "变更集:${JELLY_SCRIPT,template=&quot;html&quot;}&lt;br/&gt;&lt;hr/&gt;</defaultContent>\n" +
                "      <attachmentsPattern></attachmentsPattern>\n" +
                "      <presendScript>$DEFAULT_PRESEND_SCRIPT</presendScript>\n" +
                "      <postsendScript>$DEFAULT_POSTSEND_SCRIPT</postsendScript>\n" +
                "      <attachBuildLog>false</attachBuildLog>\n" +
                "      <compressBuildLog>false</compressBuildLog>\n" +
                "      <replyTo>$DEFAULT_REPLYTO</replyTo>\n" +
                "      <saveOutput>false</saveOutput>\n" +
                "      <disabled>false</disabled>\n" +
                "    </hudson.plugins.emailext.ExtendedEmailPublisher>\n" +
                "</publishers>\n" +
                "  <buildWrappers/>\n" +
                "</project>";

        if (platform.equals("iOS")){
            svnSplit=this.getProjectName(svn);
            if (svnSplit.contains("gongfudai")){
                target ="gongfudai";
            }else if(svnSplit.contains("xiaoqidai")){
                target ="xiaoqidai";
            }else if(svnSplit.contains("kaixindai")){
                target ="kaixindai";
                kaixindaiConfig ="pod install\n";
            }else {
                target =svnSplit;
            }

            String ios_content_01 = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<project>\n" +
                    "  <actions/>\n" +
                    "  <description>hello ios</description>\n" +
                    "  <keepDependencies>false</keepDependencies>\n" +
                    "  <properties/>\n" +
                    "  <scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.2.0\">\n" +
                    "    <configVersion>2</configVersion>\n" +
                    "    <userRemoteConfigs>\n" +
                    "      <hudson.plugins.git.UserRemoteConfig>\n" +
                    "        <url>"+svn+"</url>\n" +
                    "        <credentialsId>"+credentials+"</credentialsId>\n" +
//                    "        <credentialsId>641268f1-09ae-4dd4-ab59-a6ad05f2f605</credentialsId>\n" +
//                    "        <credentialsId>2b22e99e-1531-453c-b179-21d71cbdae67</credentialsId>\n" +
                    "      </hudson.plugins.git.UserRemoteConfig>\n" +
                    "    </userRemoteConfigs>\n" +
                    "    <branches>\n" +
                    "      <hudson.plugins.git.BranchSpec>\n" +
                    "        <name>*/"+gitbranches+"</name>\n" +
                    "      </hudson.plugins.git.BranchSpec>\n" +
                    "    </branches>\n" +
                    "    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
                    "    <submoduleCfg class=\"list\"/>\n" +
                    "    <extensions/>\n" +
                    "  </scm>\n" +
                    "  <canRoam>true</canRoam>\n" +
                    "  <disabled>false</disabled>\n" +
                    "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n" +
                    "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n" +
                    "  <authToken>12345</authToken>\n" +
                    "  <triggers/>\n" +
                    "  <concurrentBuild>false</concurrentBuild>\n" +
                    "  <builders>\n";
            if (svnSplit.contains("sdk-ios")){
                ios_content_02="<hudson.tasks.Shell>\n" +
                        "      <command>#!/bin/bash\n" +
                        "echo 'Txy30330'|sudo -S chmod -R 777 *\n" +
                        "cd Example\n"+
                        "export LC_ALL=\"en_US.UTF-8\"\n" +
                        "echo 'Txy30330'|sudo -S chmod -R 777 *\n" +
                        kaixindaiConfig+
//                        "pod update --verbose\n" +
                        "echo 'Txy30330'|sudo -S chmod -R 777 *\n"+
                        "git log -10| grep -v  &quot;Merge branch &apos;" + gitbranches + "&apos;&quot; &gt;&gt;" + APK_DIRECTORY + "/${BUILD_NUMBER}/gitlog.txt\n" +

                        "export WORKSPACENAME=TreefinanceService\n" +
                        "export SCHEME=TreefinanceService-Example\n" +
                        "export CONFIGURATION=Debug\n" +
                        "export EXPORTOPTIONPATH="+APK_DIRECTORY+"/ExportOptions.plist\n" +
                        "export MYFILES="+APK_DIRECTORY+"/${BUILD_NUMBER}/apk/TreefinanceService-Example.ipa\n" +
                        "export XCARCHIVE=${WORKSPACE}/build/${SCHEME}.xcarchive\n" +
                        "\n" +
                        "# clean\n" +
                        "echo \"================= Clean一下 =================\"\n" +
                        "if [  -e \"${XCARCHIVE}\" ]; then\n"+
                        "rm -rf ${XCARCHIVE}\n"+
                        "fi\n"+
                        "xcodebuild clean  -workspace  ${WORKSPACE}/Example/${WORKSPACENAME}.xcworkspace \\\n" +
                        "                  -scheme \"${SCHEME}\" \\\n" +
                        "                  -configuration ${CONFIGURATION} \n" +
                        "#-alltargets\n" +
                        "echo \"================= Clean一下 Over =================\"\n" +
                        "\n" +
                        "# archive\n" +
                        "echo \"============== archive ==================\"\n" +
                        "security -v unlock-keychain -p Txy30330 /Users/Shared/Jenkins/Library/Keychains/login.keychain-db\n"+
                        "security -v unlock-keychain -p Txy30330 /Users/shenzhaohua/Library/Keychains/login.keychain-db\n"+
                        "xcodebuild archive  -workspace ${WORKSPACE}/Example/${WORKSPACENAME}.xcworkspace \\\n" +
                        "                    -scheme ${SCHEME} \\\n" +
                        "                    -configuration ${CONFIGURATION} \\\n" +
                        "                    -destination generic/platform=ios \\\n" +
                        "                    -archivePath ${WORKSPACE}/build/${SCHEME}.xcarchive \n" +
                        "\n" +
                        "# export ipa\n" +
                        "echo \"+++++++++++++++++ exportArchive +++++++++++++++++\"\n" +
                        "xcodebuild -exportArchive -archivePath ${WORKSPACE}/build/${SCHEME}.xcarchive \\\n" +
                        "                          -exportPath ${WORKSPACE}/build/${SCHEME}\\\n" +
                        "                          -destination generic/platform=ios\\\n" +
                        "                          -exportOptionsPlist ${EXPORTOPTIONPATH}\\\n" +
                        "                          -allowProvisioningUpdates\n"+



//                        "</command>\n" +
//                        "    </hudson.tasks.Shell>\n" +
//                        "<au.com.rayh.XCodeBuilder plugin=\"xcode-plugin@2.0.0\">\n" +
//                        "      <cleanBeforeBuild>false</cleanBeforeBuild>\n" +
//                        "      <cleanTestReports>true</cleanTestReports>\n" +
//                        "      <configuration>Release</configuration>\n" +
//                        "      <target>demo</target>\n" +
//                        "      <sdk></sdk>\n" +
//                        "      <symRoot></symRoot>\n" +
//                        "      <configurationBuildDir>${WORKSPACE}</configurationBuildDir>\n" +
//                        "      <xcodeProjectPath></xcodeProjectPath>\n" +
//                        "      <xcodeProjectFile></xcodeProjectFile>\n" +
//                        "      <xcodebuildArguments></xcodebuildArguments>\n" +
//                        "      <xcodeSchema>TreefinanceService-Example</xcodeSchema>\n" +
//                        "      <xcodeWorkspaceFile>${WORKSPACE}/Example/TreefinanceService</xcodeWorkspaceFile>\n" +
//                        "      <embeddedProfileFile></embeddedProfileFile>\n" +
//                        "      <cfBundleVersionValue></cfBundleVersionValue>\n" +
//                        "      <cfBundleShortVersionStringValue></cfBundleShortVersionStringValue>\n" +
//                        "      <buildIpa>true</buildIpa>\n" +
//                        "      <generateArchive>false</generateArchive>\n" +
//                        "      <unlockKeychain>true</unlockKeychain>\n" +
//                        "      <keychainName>none (specify one below)</keychainName>\n" +
//                        "      <keychainPath>/Users/Shared/Jenkins/Library/Keychains/login.keychain</keychainPath>\n" +
//                        "      <keychainPwd>Txy30330</keychainPwd>\n" +
//                        "      <codeSigningIdentity></codeSigningIdentity>\n" +
//                        "      <allowFailingBuildResults>false</allowFailingBuildResults>\n" +
//                        "      <ipaName>app_" + alias + "</ipaName>\n" +
//                        "      <ipaOutputDirectory>${WORKSPACE}</ipaOutputDirectory>\n" +
//                        "      <provideApplicationVersion>false</provideApplicationVersion>\n" +
//                        "      <changeBundleID>false</changeBundleID>\n" +
//                        "      <bundleID></bundleID>\n" +
//                        "      <bundleIDInfoPlistPath></bundleIDInfoPlistPath>\n" +
//                        "      <interpretTargetAsRegEx>false</interpretTargetAsRegEx>\n" +
//                        "      <ipaManifestPlistUrl></ipaManifestPlistUrl>\n" +
//                        "      <signIpaOnXcrun>true</signIpaOnXcrun>\n" +
//                        "    </au.com.rayh.XCodeBuilder>\n"+
//                        "<hudson.tasks.Shell>\n" +
                        " mv "+JENKINS_APP_PATH+"/build/${SCHEME}/*.ipa "+APK_DIRECTORY+"/${BUILD_NUMBER}/apk/\n" +
                        "if [ ! -e \"${MYFILES}\" ]; then\n"+
                        " exit 1\n"+
                        "fi\n"+
                "</command>\n" +
                        "    </hudson.tasks.Shell>\n" +
                        "  </builders>\n" ;

            }else {
                ios_content_02 = "<hudson.tasks.Shell>\n" +
                        "      <command>#!/bin/bash\n" +
                        "export LC_ALL=\"en_US.UTF-8\"\n" +
                        "echo 'Txy30330'|sudo -S chmod -R 777 *\n" +
                        kaixindaiConfig+
//                        "pod update --verbose\n" +
                        "echo 'Txy30330'|sudo -S chmod -R 777 *\n"+
                        "git log -10| grep -v  &quot;Merge branch &apos;" + gitbranches + "&apos;&quot; &gt;&gt;" + APK_DIRECTORY + "/${BUILD_NUMBER}/gitlog.txt\n" +
                        "cd " + JENKINS_APP_PATH + "/" + target + "/" + path + "/\n" +
                        "chmod 777 *onfig*\n" +
                        "cp "+JENKINS_APP_PATH + "/" + target + "/" + path + "/config.h "+APK_DIRECTORY+"/${BUILD_NUMBER}/\n"+
                        "cp " + APK_DIRECTORY + "/config.h " + JENKINS_APP_PATH + "/" + target + "/" + path + "/\n" +
                        "export WORKSPACENAME="+svnSplit+"\n" +
                        "export SCHEME="+svnSplit+"\n" +
                        "export CONFIGURATION=Debug\n" +
                        "export EXPORTOPTIONPATH="+APK_DIRECTORY+"/ExportOptions.plist\n" +
                        "export MYFILES="+APK_DIRECTORY+"/${BUILD_NUMBER}/apk/"+svnSplit+".ipa\n" +
                        "export XCARCHIVE=${WORKSPACE}/build/${WORKSPACENAME}.xcarchive\n" +
                        "\n" +
                        "# clean\n" +
                        "echo \"================= Clean一下 =================\"\n" +
                        "if [  -e \"${XCARCHIVE}\" ]; then\n"+
                        "rm -rf ${XCARCHIVE}\n"+
                        "fi\n"+
                        "xcodebuild clean  -workspace  ${WORKSPACE}/${WORKSPACENAME}.xcworkspace \\\n" +
                        "                  -scheme ${SCHEME} \\\n" +
                        "                  -configuration ${CONFIGURATION} \\\n" +
                        "#-alltargets\n" +
                        "echo \"================= Clean一下 Over =================\"\n" +
                        "\n" +
                        "# archive\n" +
                        "echo \"============== archive ==================\"\n" +
                        "security -v unlock-keychain -p Txy30330 /Users/Shared/Jenkins/Library/Keychains/login.keychain-db\n"+
                        "security -v unlock-keychain -p Txy30330 /Users/shenzhaohua/Library/Keychains/login.keychain-db\n"+
                        "xcodebuild archive  -workspace ${WORKSPACE}/${WORKSPACENAME}.xcworkspace \\\n" +
                        "                    -scheme ${SCHEME} \\\n" +
                        "                    -configuration ${CONFIGURATION} \\\n" +
                        "                    -destination generic/platform=ios \\\n" +
                        "                    -archivePath ${WORKSPACE}/build/${WORKSPACENAME}.xcarchive \n" +
                        "\n" +
                        "# export ipa\n" +
                        "echo \"+++++++++++++++++ exportArchive +++++++++++++++++\"\n" +
                        "xcodebuild -exportArchive -archivePath ${WORKSPACE}/build/${WORKSPACENAME}.xcarchive \\\n" +
                        "                          -exportPath ${WORKSPACE}/build/${WORKSPACENAME}\\\n" +
                        "                          -destination generic/platform=ios\\\n" +
                        "                          -exportOptionsPlist ${EXPORTOPTIONPATH}\\\n" +
                        "                          -allowProvisioningUpdates\n"+


//                        "</command>\n" +
//                        "    </hudson.tasks.Shell>\n" +
//                        "<au.com.rayh.XCodeBuilder plugin=\"xcode-plugin@2.0.0\">\n" +
//                        "      <cleanBeforeBuild>false</cleanBeforeBuild>\n" +
//                        "      <cleanTestReports>true</cleanTestReports>\n" +
//                        "      <configuration>Release</configuration>\n" +
//                        "      <target>" + target + "</target>\n" +
//                        "      <sdk></sdk>\n" +
//                        "      <symRoot></symRoot>\n" +
//                        "      <configurationBuildDir>${WORKSPACE}</configurationBuildDir>\n" +
//                        "      <xcodeProjectPath></xcodeProjectPath>\n" +
//                        "      <xcodeProjectFile></xcodeProjectFile>\n" +
//                        "      <xcodebuildArguments></xcodebuildArguments>\n" +
//                        "      <xcodeSchema>" + svnSplit + "</xcodeSchema>\n" +
//                        "      <xcodeWorkspaceFile>${WORKSPACE}/" + svnSplit + "</xcodeWorkspaceFile>\n" +
//                        "      <embeddedProfileFile></embeddedProfileFile>\n" +
//                        "      <cfBundleVersionValue></cfBundleVersionValue>\n" +
//                        "      <cfBundleShortVersionStringValue></cfBundleShortVersionStringValue>\n" +
//                        "      <buildIpa>true</buildIpa>\n" +
//                        "      <generateArchive>false</generateArchive>\n" +
//                        "      <unlockKeychain>true</unlockKeychain>\n" +
//                        "      <keychainName>none (specify one below)</keychainName>\n" +
//                        "      <keychainPath>/Users/Shared/Jenkins/Library/Keychains/login.keychain</keychainPath>\n" +
//                        "      <keychainPwd>Txy30330</keychainPwd>\n" +
//                        "      <codeSigningIdentity></codeSigningIdentity>\n" +
//                        "      <allowFailingBuildResults>false</allowFailingBuildResults>\n" +
//                        "      <ipaName>app_" + alias + "</ipaName>\n" +
//                        "      <ipaOutputDirectory>${WORKSPACE}</ipaOutputDirectory>\n" +
//                        "      <provideApplicationVersion>false</provideApplicationVersion>\n" +
//                        "      <changeBundleID>false</changeBundleID>\n" +
//                        "      <bundleID></bundleID>\n" +
//                        "      <bundleIDInfoPlistPath></bundleIDInfoPlistPath>\n" +
//                        "      <interpretTargetAsRegEx>false</interpretTargetAsRegEx>\n" +
//                        "      <ipaManifestPlistUrl></ipaManifestPlistUrl>\n" +
//                        "    </au.com.rayh.XCodeBuilder>\n"+
                        "mv "+JENKINS_APP_PATH+"/build/${WORKSPACENAME}/*.ipa "+APK_DIRECTORY+"/${BUILD_NUMBER}/apk/\n" +
                        "cp "+APK_DIRECTORY+"/${BUILD_NUMBER}/config.h "+ APK_DIRECTORY + "/\n"+
                        "if [ ! -e \"${MYFILES}\" ]; then\n"+
                        " exit 1\n"+
                        "fi\n"+
                        "</command>\n" +
                        "    </hudson.tasks.Shell>\n" +
                        "  </builders>\n" ;
            }
            String ios_content_03 = "  <publishers>\n"+
                    "  <hudson.plugins.emailext.ExtendedEmailPublisher plugin=\"email-ext@2.60\"> \n" +
                    "      <recipientList>"+email+"</recipientList>\n" +
                    "      <configuredTriggers>\n" +
                    "        <hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>\n" +
                    "          <email>\n" +
                    "            <recipientList>"+email+"</recipientList>\n" +
                    "            <subject>$PROJECT_DEFAULT_SUBJECT</subject>\n" +
                    "            <body>$PROJECT_DEFAULT_CONTENT</body>\n" +
                    "            <recipientProviders>\n" +
                    "              <hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider/>\n" +
                    "              <hudson.plugins.emailext.plugins.recipients.ListRecipientProvider/>\n" +
                    "            </recipientProviders>\n" +
                    "            <attachmentsPattern></attachmentsPattern>\n" +
                    "            <attachBuildLog>false</attachBuildLog>\n" +
                    "            <compressBuildLog>false</compressBuildLog>\n" +
                    "            <replyTo>$PROJECT_DEFAULT_REPLYTO</replyTo>\n" +
                    "            <contentType>text/html</contentType>\n" +
                    "          </email>\n" +
                    "        </hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>\n" +
                    "      </configuredTriggers>\n" +
                    "      <contentType>text/html</contentType>\n" +
                    "      <defaultSubject>$PROJECT_NAME - 打包状态 - $BUILD_STATUS</defaultSubject>\n" +
                    "      <defaultContent>&lt;b&gt;&lt;font color = &quot;#CD5C5C&quot;&gt;(本邮件是程序自动下发的，请勿回复！)&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                    "\n" +
                    "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建通知:$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                    "\n" +
                    "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;项目名称：$PROJECT_NAME&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                    "\n" +
                    "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;git版本号：${GIT_REVISION}&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                    "\n" +
                    "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建状态：$BUILD_STATUS&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                    "\n" +
                    "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;触发原因：${CAUSE}&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                    "\n" +
                    "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建日志地址：&lt;a href=&quot;${BUILD_URL}console&quot;&gt;${BUILD_URL}console&lt;/a&gt;&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                    "\n" +
                    "变更集:${JELLY_SCRIPT,template=&quot;html&quot;}&lt;br/&gt;&lt;hr/&gt;</defaultContent>\n" +
                    "      <attachmentsPattern></attachmentsPattern>\n" +
                    "      <presendScript>$DEFAULT_PRESEND_SCRIPT</presendScript>\n" +
                    "      <postsendScript>$DEFAULT_POSTSEND_SCRIPT</postsendScript>\n" +
                    "      <attachBuildLog>false</attachBuildLog>\n" +
                    "      <compressBuildLog>false</compressBuildLog>\n" +
                    "      <replyTo>$DEFAULT_REPLYTO</replyTo>\n" +
                    "      <saveOutput>false</saveOutput>\n" +
                    "      <disabled>false</disabled>\n" +
                    "    </hudson.plugins.emailext.ExtendedEmailPublisher>\n" +
                    "</publishers>\n" +
                    "  <buildWrappers/>\n" +
                    "</project>";
            content = ios_content_01+ios_content_02+ios_content_03;
        }else {
            content = content_01 + content_02 + content_03;
        }
        return content;
    }

    //创建ios只拉代码的jenkins的xml
    public String iosConfigFile(String svn,String gitbranches,String credentials){
            String content = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<project>\n" +
                    "  <actions/>\n" +
                    "  <description>hello task</description>\n" +
                    "  <keepDependencies>false</keepDependencies>\n" +
                    "  <properties/>\n" +
                    "  <scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.2.0\">\n" +
                    "    <configVersion>2</configVersion>\n" +
                    "    <userRemoteConfigs>\n" +
                    "      <hudson.plugins.git.UserRemoteConfig>\n" +
                    "        <url>"+svn+"</url>\n" +
                    "        <credentialsId>"+credentials+"</credentialsId>\n" +
//                    "        <credentialsId>641268f1-09ae-4dd4-ab59-a6ad05f2f605</credentialsId>\n" +
//                    "        <credentialsId>2b22e99e-1531-453c-b179-21d71cbdae67</credentialsId>\n" +
                    "      </hudson.plugins.git.UserRemoteConfig>\n" +
                    "    </userRemoteConfigs>\n" +
                    "    <branches>\n" +
                    "      <hudson.plugins.git.BranchSpec>\n" +
                    "        <name>*/"+gitbranches+"</name>\n" +
                    "      </hudson.plugins.git.BranchSpec>\n" +
                    "    </branches>\n" +
                    "    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
                    "    <submoduleCfg class=\"list\"/>\n" +
                    "    <extensions/>\n" +
                    "  </scm>\n" +
                    "  <canRoam>true</canRoam>\n" +
                    "  <disabled>false</disabled>\n" +
                    "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n" +
                    "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n" +
                    "  <authToken>12345</authToken>\n" +
                    "  <triggers/>\n" +
                    "  <concurrentBuild>false</concurrentBuild>\n" +
                    "<builders>"+
                    "<hudson.tasks.Shell> \n" +
                    "      <command>#!/bin/bash\n" +
                    "export LC_ALL=\"en_US.UTF-8\"\n" +
                    "echo 'Txy30330'|sudo -S chmod -R 777 *\n" +
                    "pod update \n" +
                    "echo 'Txy30330'|sudo -S chmod -R 777 *\n"+
                    "</command>\n" +
                    "    </hudson.tasks.Shell>\n" +
                    "  </builders>\n" +
                    "  <publishers/>\n" +
                    "  <buildWrappers/>\n" +
                    "</project>";
        return content;
    }


    //task的config文件
    public String taskConfigFile(String git,String gitbranches,String action,String addressInput){
        String content_01 ="<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<project>\n" +
                "  <actions/>\n" +
                "  <description>hello ios</description>\n" +
                "  <keepDependencies>false</keepDependencies>\n" +
                "  <properties/>\n" +
                "  <scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.2.0\">\n" +
                "    <configVersion>2</configVersion>\n" +
                "    <userRemoteConfigs>\n" +
                "      <hudson.plugins.git.UserRemoteConfig>\n" +
                "        <url>";
        String content_02 =git+"</url>\n" +
                "        <credentialsId>641268f1-09ae-4dd4-ab59-a6ad05f2f605</credentialsId>\n" +
//                "        <credentialsId>2b22e99e-1531-453c-b179-21d71cbdae67</credentialsId>\n" +
                "      </hudson.plugins.git.UserRemoteConfig>\n" +
                "    </userRemoteConfigs>\n" +
                "    <branches>\n" +
                "      <hudson.plugins.git.BranchSpec>\n" +
                "        <name>*/"+gitbranches+"</name>\n" +
                "      </hudson.plugins.git.BranchSpec>\n" +
                "    </branches>\n" +
                "    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
                "    <submoduleCfg class=\"list\"/>\n" +
                "    <extensions/>\n" +
                "  </scm>\n" +
                "  <canRoam>true</canRoam>\n" +
                "  <disabled>false</disabled>\n" +
                "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n" +
                "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n" +
                "  <authToken>12345</authToken>\n" +
                "  <triggers/>\n" +
                "  <concurrentBuild>false</concurrentBuild>\n" +
                "  <builders>\n" +
                "    <hudson.tasks.Shell>\n" +
                "      <command>#!/bin/bash\n" +
                ""+action+"\n" +
                "</command>\n" +
                "    </hudson.tasks.Shell>\n";
        String content_03 ="<hudson.plugins.emailext.ExtendedEmailPublisher plugin=\"email-ext@2.60\"> \n" +
                "      <recipientList>"+addressInput+"</recipientList>\n" +
                "      <configuredTriggers>\n" +
                "        <hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>\n" +
                "          <email>\n" +
                "            <recipientList>"+addressInput+"</recipientList>\n" +
                "            <subject>$PROJECT_DEFAULT_SUBJECT</subject>\n" +
                "            <body>$PROJECT_DEFAULT_CONTENT</body>\n" +
                "            <recipientProviders>\n" +
                "              <hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider/>\n" +
                "              <hudson.plugins.emailext.plugins.recipients.ListRecipientProvider/>\n" +
                "            </recipientProviders>\n" +
                "            <attachmentsPattern></attachmentsPattern>\n" +
                "            <attachBuildLog>false</attachBuildLog>\n" +
                "            <compressBuildLog>false</compressBuildLog>\n" +
                "            <replyTo>$PROJECT_DEFAULT_REPLYTO</replyTo>\n" +
                "            <contentType>text/html</contentType>\n" +
                "          </email>\n" +
                "        </hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>\n" +
                "      </configuredTriggers>\n" +
                "      <contentType>text/html</contentType>\n" +
                "      <defaultSubject>$PROJECT_NAME - 执行状态 - $BUILD_STATUS</defaultSubject>\n" +
                "      <defaultContent>&lt;b&gt;&lt;font color = &quot;#CD5C5C&quot;&gt;(本邮件是程序自动下发的，请勿回复！)&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建通知:$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;项目名称：$PROJECT_NAME&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;git版本号：${GIT_REVISION}&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建状态：$BUILD_STATUS&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;触发原因：${CAUSE}&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "&lt;b&gt;&lt;font color = &quot;#2F4F4F&quot;&gt;构建日志地址：&lt;a href=&quot;${BUILD_URL}console&quot;&gt;${BUILD_URL}console&lt;/a&gt;&lt;/font&gt;&lt;/b&gt;&lt;br/&gt;&lt;br/&gt;\n" +
                "\n" +
                "变更集:${JELLY_SCRIPT,template=&quot;html&quot;}&lt;br/&gt;&lt;hr/&gt;</defaultContent>\n" +
                "      <attachmentsPattern></attachmentsPattern>\n" +
                "      <presendScript>$DEFAULT_PRESEND_SCRIPT</presendScript>\n" +
                "      <postsendScript>$DEFAULT_POSTSEND_SCRIPT</postsendScript>\n" +
                "      <attachBuildLog>false</attachBuildLog>\n" +
                "      <compressBuildLog>false</compressBuildLog>\n" +
                "      <replyTo>$DEFAULT_REPLYTO</replyTo>\n" +
                "      <saveOutput>false</saveOutput>\n" +
                "      <disabled>false</disabled>\n" +
                "    </hudson.plugins.emailext.ExtendedEmailPublisher>\n" +
                "</builders>\n"+
                "  <publishers/>\n" +
                "  <buildWrappers/>\n" +
                "</project>";
        String content = content_01 + content_02 + content_03;
        return content;

    }

    //取git名称
    public static String getProjectName(String path)  {
        String[] pathSplit =path.split("/");
        int count = pathSplit.length;
        return pathSplit[count-1].replace(".git","");
    }

    //取配置文件中的版本号
    public static Map<String, String> readProperties(String propertiesPath,String sdk_version) {
        Properties prop = new Properties();
        Map<String, String> map = new HashMap<String, String>();
        try {
            //读取属性文件properties
            InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath));
            prop.load(in);
            map.put("version", prop.getProperty(sdk_version));
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

//读出parameter.Properties数据
    public static Properties getParameterProperties() {
        Properties prop = new Properties();
        try {
            //读取属性文件properties
            String propertiesPath =System.getProperty("user.dir")+"/src/main/resources/parameter.properties";
            InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath));
            prop.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;

    }

    /**
     * 创建ZIP文件
     * @param sourcePath 文件或文件夹路径
     * @param zipPath 生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String zipPath) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
//            zos.setEncoding("gbk");//此处修改字节码方式。
            //createXmlFile(sourcePath,"293.xml");
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
//            log.error("创建ZIP文件失败",e);
            System.out.println("创建ZIP文件失败");

        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
//                log.error("创建ZIP文件失败",e);
                System.out.println("创建ZIP文件失败");

            }

        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if(file.exists()){
            if(file.isDirectory()){//处理文件夹
                parentPath+=file.getName()+File.separator;
                File [] files=file.listFiles();
                if(files.length != 0)
                {
                    for(File f:files){
                        writeZip(f, parentPath, zos);
                    }
                }
                else
                {       //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else{
                FileInputStream fis=null;
                try {
                    fis=new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte [] content=new byte[1024];
                    int len;
                    while((len=fis.read(content))!=-1){
                        zos.write(content,0,len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("创建ZIP文件失败");
//                    log.error("创建ZIP文件失败",e);
                } catch (IOException e) {
                    System.out.println("创建ZIP文件失败");
//                    log.error("创建ZIP文件失败",e);
                }finally{
                    try {
                        if(fis!=null){
                            fis.close();
                        }
                    }catch(IOException e){
//                        log.error("创建ZIP文件失败",e);
                        System.out.println("创建ZIP文件失败");

                    }
                }
            }
        }
    }

    //等待几次后退出循环体
    public static String  waitJiagu(String path,String keywords) throws IOException {
        String fileExist ="";
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 10 * 60 * 1000;
        while(System.currentTimeMillis() <= endTime){
            fileExist =  getFileName(path,keywords);
            if (fileExist!="") {
                //等待3秒
//                try
//                {
//                    Thread.sleep(3000);
//                }
//                catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
                break;
            }
        }
        return fileExist;
    }

    /**
     * @param filePath
     */
    public boolean renameApk(String filePath,String keyword, String newName) throws IOException {
        String jiaguFile = getFileName(filePath,keyword);
        File f=new File(filePath+File.separator+jiaguFile);
        String c=f.getParent();
        File mm=new File(c+File.separator+newName);
        if(f.renameTo(mm))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    //用以模糊删除头部为str的文件
    public static boolean delFilesByPath(String path,String str){
        //参数说明---------path:要删除的文件的文件夹的路径---------str:要匹配的字符串
        boolean b=false;
        File file = new File(path);
        File[] tempFile = file.listFiles();
        for(int i = 0; i < tempFile.length; i++){
            if(tempFile[i].getName().indexOf(str) !=-1){
                tempFile[i].delete();
                b=true;
            }
        }
        return b;
    }



    //取项目列表
    public LinkedHashMap<String,List> projectlist(){
        return selectList("select id,platform,projectname from project order by platform,id DESC",3);
    }


}
