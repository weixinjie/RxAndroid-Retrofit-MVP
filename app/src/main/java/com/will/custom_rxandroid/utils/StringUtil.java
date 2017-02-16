package com.will.custom_rxandroid.utils;

import android.database.CursorJoiner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String工具类
 *
 * @author hongjinqun
 * @date 2012-10-30
 */
public class StringUtil {

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null) return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 判断一个字符窜是否为空
     *
     * @param str
     * @return
     */
    public static boolean isValid(String str) {
        boolean isValid = false;
        if (null == str || str.length() == 0) {
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 把多个收件人/抄送人组装成一个字符串，为了插入数据库
     *
     * @param receiverList
     * @return
     */
    public static String buildReceiver(List<String> receiverList) {
        StringBuilder sb = new StringBuilder();
        if (null != receiverList) {
            for (String receiver : receiverList) {
                sb.append(receiver);
                sb.append(";");
            }
        }
        return sb.toString();
    }

    /**
     * 验证邮箱地址合法性
     *
     * @param addr
     * @return
     */
    public static boolean isEmailAddrValid(String addr) {
        if (isValid(addr)) {
            String emailAddressPattern = "[a-zA-Z0-9][a-zA-Z0-9._-]{2,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
            return addr.matches(emailAddressPattern);
        } else {
            return false;
        }
    }

    /**
     * 用当前时间生成一个随机的id
     *
     * @return
     */
    public static String createId() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHMMSS");
        return sdf.format(date);
    }

    /**
     * 把一个输入流根据传人的编码方式读成字符串
     *
     * @param is
     * @param encoding
     * @return
     */
    public static String streamToString(InputStream is, String encoding) {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is, encoding);
            char[] b = new char[4096];
            for (int n; (n = isr.read(b)) != -1; ) {
                sb.append(new String(b, 0, n));
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 截取在start与end之间的字符窜
     *
     * @param start
     * @param end
     * @param oldStr
     * @return
     */
    public static String getValueBetweenStartEnd(String start, String end,
                                                 String oldStr) {
        String value = "";
        if (isValid(oldStr)) {
            Matcher m = Pattern.compile(start + "([\\d\\D]+?)" + end).matcher(
                    oldStr);
            while (m.find()) {
                value = m.group(1);
            }
        }
        return value;
    }

    /**
     * 根据label获取以label开头的内容并放入集合
     *
     * @param string
     * @param label
     * @return
     */
    public static ArrayList<String> getLabelContentList(String string,
                                                        String label) {
        Matcher m = Pattern.compile("<" + label + "([\\d\\D]+?)>",
                Pattern.CASE_INSENSITIVE).matcher(string);
        ArrayList<String> list = new ArrayList<String>();
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    /**
     * 根据分号分隔字符串
     *
     * @param oldStr
     * @return
     */
    public static ArrayList<String> getSubstringBySemicolon(String oldStr) {
        ArrayList<String> list = new ArrayList<String>();
        if (isValid(oldStr)) {
            String[] tempList = oldStr.split(";");
            list.addAll(Arrays.asList(tempList));
        }
        return list;
    }

    /**
     * 根据一个HashMap<String,String>组装一个字符串，形如Content_ID&&file:///EmailAttach.
     * getLocalPath();...
     *
     * @param htmlImgSrcMap
     * @return
     */
    public static String getHtmlImgPath(HashMap<String, String> htmlImgSrcMap) {
        StringBuilder sb = new StringBuilder();
        if (null != htmlImgSrcMap) {
            Set<Entry<String, String>> sets = htmlImgSrcMap.entrySet();
            Iterator<Entry<String, String>> its = sets.iterator();
            while (its.hasNext()) {
                Entry<String, String> entry = its.next();
                sb.append(entry.getKey());
                sb.append("&&");
                sb.append(entry.getValue());
                sb.append(";");
            }
        }
        return sb.toString();
    }

    /**
     * 根据一个String分割组装一个HashMap（key-->Content-ID,value-->本地路径）
     *
     * @param htmlImg 形如: cid:_Foxmail.1@BD1CCEF1-E764-4E07-8FF0-8BB0A6DFB4A5
     *                &&file:////mnt/sdcard/shanEmail/attachment/datu1_1.jpg;
     *                cid:_Foxmail.0@B67203DB-A9F7-4371-B494-39293A3DF40E
     *                &&file:////mnt/sdcard/shanEmail/attachment/12.gif;
     * @return
     */
    public static HashMap<String, String> getHtmlPathMap(String htmlImg) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        if (isValid(htmlImg)) {
            // 先根据分号(";")，分出每个html里的图片信息，再根据"&&"分割出contentId和本地存储路径
            ArrayList<String> tempList = getSubstringBySemicolon(htmlImg);
            if (null != tempList) {
                for (int i = 0; i < tempList.size(); i++) {
                    // 这个就是每个图片的content-ID&&localPath
                    String s = tempList.get(i);
                    String[] temp = s.split("&&");
                    if (null != temp && temp.length > 1) {
                        // Content-ID
                        String key = temp[0];
                        // 本地存储路径
                        String value = temp[1];
                        tempMap.put(key, value);
                    }
                }
            }

        }
        return tempMap;
    }

    public static boolean isNumber(String str) {// 判断整型
        return null != str && str.matches("[\\d]+");
    }

    /**
     * 判断字符串是否是yyyy-MM-dd HH:mm:ss的格式
     *
     * @param str
     * @return
     */
    public static boolean isDateTime(String str) {
        String match = "^(?:(?!0000)[0-9]{4}-" + "(?:(?:0[1-9]|1[0-2])-"
                + "(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-"
                + "(?:29|30)|(?:0[13578]|1[02])-31)|"
                + "(?:[0-9]{2}(?:0[48]|[2468][048]|"
                + "[13579][26])|(?:0[48]|[2468][048]|"
                + "[13579][26])00)-02-29)\\s+([01][0-9]|2[0-3])"
                + ":[0-5][0-9]:[0-5][0-9]$";
        return null != str && str.matches(match);
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 去掉字符串里面的空格
     *
     * @param input
     * @return
     */
    public static String clearString(String input) {
        if (input == null || input.equals("")) {
            return null;
        }
        String result = input.replaceAll("\\s", "");
        return result;
    }

    /**
     * 判断输入的字符串是否是手机号
     *
     * @param input
     * @return
     */
    public static boolean isMobile(String input) {
        if (input == null) {
            return false;
        } else if (input.length() == 11) {
            return false;
        }
        return true;
    }

    /**
     * 根据value值遍历map获得key
     *
     * @param map
     * @param value
     * @return
     */
    public static int getKeyByValue(Map<Integer, String> map, String value) {
        if (map == null || map.size() == 0)
            return -1;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            int key = (int) entry.getKey();
            String val = (String) entry.getValue();
            if (val.equals(value)) {
                return key;
            }
        }
        return -1;
    }

    /**
     * list 去除重复元素
     *
     * @param arlList
     */
    public static void removeDuplicate(List arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

    /**
     * 得到textview显示的数字
     *
     * @param tv
     * @return
     */
    public static int getCount(TextView tv) {
        int count = 1;
        try {
            String countString = tv.getText().toString().trim();
            count = Integer.parseInt(countString);
        } catch (Exception e) {

        }
        return count;
    }
}
