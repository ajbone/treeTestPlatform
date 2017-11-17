package com.dashu.datashow.util;

import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by admin on 2017/2/21.
 */
public class SignUtil {

    private static ObjectMapper mapper    = new ObjectMapper();

    public static String          secret    = "ad)&2%=@B";

    protected static String       salt      = "carme9527";

    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"   };

    static {
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        //数据类型转双引号
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        //日期格式定义
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    static DataBase db1 =null;

    static ResultSet ret =null;

    public static String sign(String json) {
        try {
            HashMap<String, Object> paramMap = mapper.readValue(json, HashMap.class);
            TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
            convertTreeMap(paramMap, treeMap, 1);
            String targetJson = mapper.writeValueAsString(treeMap);
            return secondMd5(targetJson, secret, salt, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String GetMD5Code(String strObj, String encode) {
        String resultString = null;
        try {
            if (null == encode || "".equals(encode)) {
                encode = "UTF-8";
            }
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes(encode)));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return resultString;
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    public static String secondMd5(String str, String secret, String salt, String encode) {
        String firstMd5 = GetMD5Code(secret + str, encode);
        return GetMD5Code(firstMd5 + salt, encode);
    }

    public static void convertTreeMap(Map<String, Object> paramMap,
                                      TreeMap<String, Object> treeMap, int depth) {
        if (depth > 10) {
            return;
        }
        for (String key : paramMap.keySet()) {
            Object value = paramMap.get(key);
            if (isBaseObject(value)) {
                treeMap.put(key, value);
            } else if (value instanceof LinkedHashMap) {
                TreeMap<String, Object> childMap = new TreeMap<String, Object>();
                treeMap.put(key, childMap);
                depth++;
                convertTreeMap((LinkedHashMap) value, childMap, depth);
            } else if (value instanceof ArrayList) {
                List<Object> list = new ArrayList<Object>();
                treeMap.put(key, list);
                depth++;
                for (Object childValue : (ArrayList) value) {
                    if (isBaseObject(childValue)) {
                        list.add(childValue);
                    } else {
                        TreeMap<String, Object> childMap = new TreeMap<String, Object>();
                        list.add(childMap);
                        convertTreeMap((LinkedHashMap<String, Object>) childValue, childMap, depth);
                    }

                }
            }
        }
    }

    /**
     * 是否为Class对象
     *
     * @param object
     * @return
     */
    public static boolean isBaseObject(Object object) {
        if (object == null) {
            return false;
        }
        return isBaseObject(object.getClass());
    }

    /**
     * 是否为Class对象
     *
     * @return
     */
    public static boolean isBaseObject(Class<?> cla) {
        if (cla.equals(String.class) || cla.equals(Integer.class) || cla.equals(Long.class)
                || cla.equals(Float.class) || cla.equals(Double.class) || cla.equals(Byte.class)
                || cla.equals(Short.class) || cla.equals(Date.class)) {
            return true;
        }
        return false;
    }


    public String selectResult (String sql,String dbhost,String user,String password,String tableName){

        String selectResult ="";
        db1 = new DataBase(sql,dbhost,user,password,tableName);//创建packageDB对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                selectResult = ret.getString(1);
            }
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectResult;

    }

    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "\"" + e.getValue() + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    public static Map<Object, Object> jsonToMap(String jsonObj) {
        JSONObject jsonObject = JSONObject.parseObject(jsonObj);
        Map<Object, Object> map = (Map)jsonObject;
        return map;
    }
}
