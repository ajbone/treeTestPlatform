#!/usr/bin/python
# coding=utf-8
import os
import sys
import MySQLdb
import numpy as np
import json
import subprocess
import urllib2

reload(sys)
sys.setdefaultencoding('utf8')

conn = MySQLdb.connect(
        host='localhost',
        port=3306,
        user='root',
        passwd='',
        db='appPackage',
        charset='utf8'
)


parentDir = sys.argv[5]+"/"
apkDir = sys.argv[2]

# 获取当前目录中所有的apk源包
src_apks = []

for fileNm in os.listdir(apkDir):
    fileName = os.path.splitext(fileNm)[0]
    extension = os.path.splitext(fileNm)[1][1:]
    if (extension == 'apk') & ('jiagu_sign' in fileName):
        src_apks.append(fileNm)

# 获取渠道列表

for src_apk in src_apks:
    # file name (with extension)
    src_apk_file_name = os.path.basename(src_apk)
    # 分割文件名与后缀
    temp_list = os.path.splitext(src_apk_file_name)
    # name without extension
    src_apk_name = temp_list[0]
    # 后缀名，包含.   例如: ".apk "
    src_apk_extension = temp_list[1]

    # 创建生成目录,与文件名相关
    # output_dir = 'output_' + src_apk_name + '/'

    output_dir = sys.argv[3]
    # output_dir = parentDir + 'output_' + src_apk_name + '/'
    # 目录不存在则创建
    if not os.path.exists(output_dir):
        os.mkdir(output_dir)


    # 数据库中取渠道信息
    cur = conn.cursor(MySQLdb.cursors.DictCursor)
    # cur.execute("select content,appId from channelList where id =" + sys.argv[1])
    cur.execute("select content,appId from channelList where id ="+ sys.argv[1])
    rows = cur.fetchmany()
    rows = np.array(rows)
    for row in rows:
        lines = json.loads(row["content"])
        projectType = row["appId"]

        txtName = parentDir+projectType+".txt"
        f = file(txtName, "w")
        f.truncate()
        for line in lines:
            if projectType in 'xjcs':
                response = urllib2.urlopen('https://api.91xjcs.com/supermarket/app/license?appId=' + line.strip() + '&platform=android')
                hjson = json.loads(response.read())
                new_context = line.strip() + ":" + lines[line].strip() + ":"+ hjson['data'] +'\n'
            else:
                new_context = line.strip()+":"+lines[line].strip() + '\n'
            f.write(new_context)
        f.close()

    # 获取渠道列表
    channel_file = parentDir+projectType+".txt"
    f = open(channel_file)
    build_channel = f.readlines()
    f.close()


    #新渠道打包方式
    for line in build_channel:
        # # :号前是apk包名需要的 格式例如  xqd360:小期贷-360软件中心
        # target_channel_info = line.strip().split(":")
        # target_channel_name = target_channel_info[0].strip()

        # print "打包渠道：", line.strip()
        # 获取当前渠道号，因为从渠道文件中获得带有\n,所有strip一下

        # :号前是apk包名需要的 格式例如  xqd360:小期贷-360软件中心
        target_channel_info = line.strip().split(":")
        target_channel_name = target_channel_info[0].strip()
        target_channel = target_channel_info[1].strip()

        # 拼接对应渠道号的apk.
        if projectType in 'xjcs':
            target_channel_key = target_channel_info[2].strip()
            target_apk = output_dir + "supermarket-" + target_channel_name + "-release" + src_apk_extension
            excuteStr = "-jar " + parentDir + "walle-cli-all.jar put -c " + target_channel_name + " -e channel_code=" + target_channel_name + ",channel_name=" + target_channel + ",privateKey="+target_channel_key +" "+ apkDir + src_apk + " " + target_apk
        else:
            target_apk = output_dir + "app-" + target_channel_name + "-release" + src_apk_extension
            excuteStr = "-jar " + parentDir + "walle-cli-all.jar put -c " + target_channel_name + " -e channel_code=" + target_channel_name + ",channel_name=" + target_channel + " " + apkDir + src_apk + " " + target_apk

        # print "执行渠道打包命令：java" , excuteStr

        subprocess.check_output("java "+excuteStr,shell=True)

