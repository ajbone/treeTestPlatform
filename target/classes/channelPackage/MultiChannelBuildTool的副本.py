#!/usr/bin/python
# coding=utf-8
import zipfile
import shutil
import os
import sys
import MySQLdb
import numpy as np
import json

conn = MySQLdb.connect(
    host='localhost',
    port=3306,
    user='root',
    passwd='',
    db='appPackage',
)

# 空文件 便于写入此空文件到apk包中作为channel文件
src_empty_file = 'info/imoney.txt'
# 创建一个空文件（不存在则创建）
f = open(src_empty_file, 'w')
f.close()

# 获取当前目录中所有的apk源包
src_apks = []
# python3 : os.listdir()即可，这里使用兼容Python2的os.listdir('.')
# for file in os.listdir('.'):
#     if os.path.isfile(file):
#         extension = os.path.splitext(file)[1][1:]
#         if extension in 'apk':
#             src_apks.append(file)
for file in os.listdir(sys.argv[2]):
    # print file
    # if os.path.isfile(file):
    fileName = os.path.splitext(file)[0]
    extension = os.path.splitext(file)[1][1:]
    if (extension == 'apk') & ('jiagu' in fileName):
        src_apks.append(file)

# 获取渠道列表

# channel_file = 'info/channel.txt'
# f = open(channel_file)
# lines = f.readlines()
# f.close()

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
    # 目录不存在则创建
    if not os.path.exists(output_dir):
        os.mkdir(output_dir)

    # 创建临时目录,与文件名相关
    # temp_dir = 'temp' + src_apk_name + '/'
    temp_dir = sys.argv[4]
    # 目录不存在则创建
    if not os.path.exists(temp_dir):
        os.mkdir(temp_dir)

    out_temp_apk = temp_dir + src_apk_name + 'temp' + src_apk_extension;
    # copy一份没有渠道信息的apk
    zin = zipfile.ZipFile(sys.argv[2]+src_apk, 'a', zipfile.ZIP_DEFLATED)
    zout = zipfile.ZipFile(out_temp_apk, 'w')
    for item in zin.infolist():
        buffer = zin.read(item.filename)
        if (item.filename[0:16:] != 'META-INF/channel'):
            zout.writestr(item, buffer)
    zout.close()
    zin.close()
    
    # 数据库中取渠道信息
    cur = conn.cursor(MySQLdb.cursors.DictCursor)
    cur.execute("select content from channelList where id =" + sys.argv[1])
    rows = cur.fetchmany()
    rows = np.array(rows)
    for row in rows:
        lines = json.loads(row["content"])

    # 遍历渠道号并创建对应渠道号的apk文件
    for line in lines:
        # 获取当前渠道号，因为从渠道文件中获得带有\n,所有strip一下

        # :号前是apk包名需要的 格式例如  xqd360:小期贷-360软件中心
        target_channel_info = line.strip().split(":")
        target_channel_name = target_channel_info[0].strip()
        target_channel = target_channel_info[1].strip() + "&" + target_channel_name
        # 拼接对应渠道号的apk
        target_apk = output_dir + "app-" + target_channel_name + "-release" + src_apk_extension
        # 拷贝建立新apk
        shutil.copy(out_temp_apk, target_apk)
        # zip获取新建立的apk文件
        zipped = zipfile.ZipFile(target_apk, 'a', zipfile.ZIP_DEFLATED)
        # 初始化渠道信息
        empty_channel_file = "META-INF/channel&{channel}".format(channel=target_channel)

        for item in zipped.infolist():
            if (item.filename[0:16:] == 'META-INF/channel'):
                print item.filename
        # 写入渠道信息

        zipped.write(src_empty_file, empty_channel_file)
        # 关闭zip流
        zipped.close()
