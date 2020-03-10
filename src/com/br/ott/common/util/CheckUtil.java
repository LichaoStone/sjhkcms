/*
 * 文 件 名:  CheckUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  pKF46373
 * 修改时间:  2011-10-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.br.ott.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
/**
 * 后台字符串格式验证 <功能详细描述>
 * 
 * @author pKF46373
 * @version [版本号, 2011-10-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class CheckUtil {
    private CheckUtil() {

    }

    /**

     * <判断字符串为空> <功能详细描述>
     * 

     * 实例化一个记录日志
     */
    private static Logger logger=Logger.getLogger(CheckUtil.class);
    /**
     * <判断字符串为空>
     * <功能详细描述>

     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isBlankOrNull(String str) {
        if (null != str.trim() && str.length() > 0) {
            return false;
        }
        return true;
    }

    /**
     * <判断字符串是否为空> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isRequired(String str) {
        if (isBlankOrNull(str)) {
            return false;
        }

        return true;
    }

    /**
     * <判断是不是字符> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isHalfChar(String str) {
        if (isBlankOrNull(str)) {
            return false;
        }
        if (str.matches("^[0-9a-zA-Z]*$")) {
            return true;
        }
        return false;
    }

    /**
     * <判断是否超过最大字节> <功能详细描述>
     * 
     * @param st
     * @param max
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isMaxBytes(String st, int max) {

        if (st == null) {
            return false;
        }

        int size = st.getBytes().length;
        if (max < size) {
            return false;
        }
        return true;
    }

    /**
     * <一句话功能简述> <功能详细描述>
     * 
     * @param st
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNotMinus(String st) {

        if (!isRequired(st)) {
            return false;
        }
        String value = st.replaceAll("\\,", "");
        double su = 0;
        try {
            su = Double.parseDouble(value);
        } catch (NumberFormatException ne) {
            return false;
        }

        if (su < 0) {
            return false;
        }
        return true;
    }

    /**
     * <> <功能详细描述>
     * 
     * @param st
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNotZero(String st) {

        if (!isRequired(st)) {
            return false;
        }
        String value = st.replaceAll("\\,", "");
        double su = 0;
        try {
            su = Double.parseDouble(value);
        } catch (NumberFormatException ne) {
            return false;
        }

        if (su == 0) {
            return false;
        }

        return true;
    }

    public static boolean isAlphanumericSign(String st) {

        if (isBlankOrNull(st)) {
            return false;
        }
        if (st.matches("^[!-~ ]*$")) {
            return true;
        }
        return false;
    }

    public static boolean isSingleByte(String st) {

        if (isBlankOrNull(st)) {
            return false;
        }
        if (st.getBytes().length != st.length()) {
            return false;
        } else if (st.matches("^[!-~.-� ]*$")) {
            return true;
        }
        return false;
    }

    public static boolean isEmSize(String st) {

        if (isBlankOrNull(st)) {
            return false;
        }
        if (st.getBytes().length == st.length() * 2) {
            return true;
        }
        return false;
    }

    public static boolean isWildCard(String st) {
        if (st != null) {
            int i = st.indexOf("*");
            if (i == -1 || i == st.length() - 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFromTo(String fromSt, String toSt) {

        if (fromSt.length() != toSt.length()) {
            return false;
        }
        if (fromSt.compareTo(toSt) <= 0) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * <判断两个数字是否相等> <功能详细描述>
     * 
     * @param num1
     * @param num2
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEqualNum(long num1, long num2) {

        if (num1 == num2) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * <判断字符串相等> <功能详细描述>
     * 
     * @param st1
     * @param st2
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEqualString(String st1, String st2) {
        return (st1.equals(st2));
    }

    /**
     * <判断是否为数字> <功能详细描述>
     * 
     * @param st
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNum(String st) {

        if (isBlankOrNull(st)) {
            return false;
        }
        if (st.matches("^[0-9]*$")) {
            return true;
        }
        return false;
    }

    /**
     * <判断是否为字母> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAlph(String str) {

        if (isBlankOrNull(str)) {
            return false;
        }
        if (str.matches("^[a-zA-Z]*$")) {
            return true;
        }
        return false;
    }

    /**
     * <判断字符串是否超出最大长度> <功能详细描述>
     * 
     * @param str
     * @param figure
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isOverLength(String str, int figure) {

        if (str == null) {
            return false;
        }
        if (str.length() > figure) {
            return false;
        }

        return true;
    }

    /**
     * <判断年份> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isYear(String str) {

        if (!isNum(str)) {
            return false;
        }

        int num = Integer.parseInt(str);

        if ((num < Constants.NUM1900) || (Constants.NUM2999 < num)) {
            return false;
        }

        return true;

    }

    /**
     * <判断是否为月份> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isMonth(String str) {

        if (!isNum(str)) {
            return false;
        }

        int num = Integer.parseInt(str);

        if ((num < Constants.NUM1) || (Constants.NUM12 < num)) {
            return false;
        }

        return true;

    }

    /**
     * <判断是否为天数> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isDay(String str) {

        if (!isNum(str)) {
            return false;
        }

        int num = Integer.parseInt(str);

        if ((num < 1) || (Constants.NUM31 < num)) {
            return false;
        }

        return true;

    }

    /**
     * <判断是否为电话号码> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isPhoneNumber(String str) {

        if (!isRequired(str)) {
            return false;
        }

        Pattern ptn = Pattern.compile("\\d{1,4}?-\\d{1,4}?-\\d{1,4}");
        Matcher mc = ptn.matcher(str);

        if (!mc.matches()) {
            return false;
        }

        return true;

    }

    /**
     * <判断是否为邮政编码> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isPostCode(String str) {
        if (!isRequired(str)) {
            return false;
        }
        Pattern ptn = Pattern.compile("\\d{3}-\\d{4}");
        Matcher mc = ptn.matcher(str);

        if (!mc.matches()) {
            return false;
        }
        return true;
    }

    /**
     * <电子邮箱格式判断> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEamil(String str) {
        if (!isRequired(str)) {
            return false;
        }
        Pattern ptn = Pattern
                .compile("([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}");
        Matcher mc = ptn.matcher(str);

        if (!mc.matches()) {
            return false;
        }
        return true;
    }

    /**
     * <联系方式格式判断> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isPhone(String str) {
        if (!isRequired(str)) {
            return false;
        }
        Pattern ptn = Pattern
                .compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
        Matcher mc = ptn.matcher(str);

        if (!mc.matches()) {
            return false;
        }
        return true;
    }

    /**
     * <账户名判断> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isOperid(String str) {
        if (!isRequired(str)) {
            return false;
        }
        Pattern ptn = Pattern.compile("[a-zA-Z]{1}[A-zA-Z0-9]{0,16}");
        Matcher mc = ptn.matcher(str);

        if (!mc.matches()) {
            return false;
        }
        return true;
    }

    /**
     * <mac地址判断> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isMac(String str) {
        if (!isRequired(str)) {
            return false;
        }
        Pattern ptn = Pattern.compile("[A-F0-9]+");
        Matcher mc = ptn.matcher(str);
        if (!mc.matches()) {
            return false;
        }
        return true;
    }
    /**
     * 验证MAC地址是否合法
     * 
     * @param mac
     * @return
     */
    public static boolean checkRegexMAC(String mac) {
        Pattern macPattern = Pattern.compile(Constants.MAC_REGEX);
        Matcher macMatcher = macPattern.matcher(mac);
        boolean flag = macMatcher.find();
        return flag;
    }
    /**
     * 验证IMEI地址是否合法
     * 
     * @param mac
     * @return
     */
    public static  boolean isIMEI(String imei) {
        Pattern macPattern = Pattern.compile(Constants.IMEI_REGEX);
        Matcher macMatcher = macPattern.matcher(imei);
        boolean flag = macMatcher.find();
        return flag;
    }
    /**
     * 验证密码格式
     * <功能详细描述>
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkPassword(String password){
        Pattern macPattern = Pattern.compile("^[0-9a-zA-Z]{8,16}$");
        Matcher macMatcher = macPattern.matcher(password);
        boolean flag = macMatcher.find();
        return flag;
    }
    /**
     * 判断时间是否起始时间小于终止时间 返回 boolean类型
     * 
     * @param starttime
     * @param endtime
     * @return
     * @throws ParseException [参数说明]
     * @return boolean true表示重复，false表示不重复
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public static  boolean isFormatDate(String starttime, String endtime) throws ParseException {
       SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag=false;
        if (starttime!=null && endtime!=null) {
            Date startDate = sdf.parse(starttime.replace("/","-"));
            Date endDate = sdf.parse(endtime.replace("/","-"));           
            flag = startDate.before(endDate);
        }
        return flag;
    }

    /**
     * <判断字符串长度> <功能详细描述>
     * 
     * @param str
     * @param length
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isLength(String str, int length) {
        if (!isRequired(str)) {
            return false;
        }
        if (str.length()>length) {
            return false;
        }
        return true;
    }

    /**
     * <验证日期格式为YYYY-MM-DD> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isdate(String str) {
        if (!isRequired(str)) {
            return false;
        }
        Pattern ptn = Pattern
                .compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        Matcher mc = ptn.matcher(str);
        if (!mc.matches()) {
            return false;
        }
        return true;
    }

    /**
     * <验证日期格式为DD/MM/YYYY> <功能详细描述>
     * 
     * @param str
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isdate1(String str) {
        if (!isRequired(str)) {
            return false;
        }
        Pattern ptn = Pattern
                .compile("(((0[1-9]|[12][0-9]|3[01])/((0[13578]|1[02]))|((0[1-9]|[12][0-9]|30)/(0[469]|11))|(0[1-9]|[1][0-9]|2[0-8])/(02))/([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}))|(29/02/(([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00)))");
        Matcher mc = ptn.matcher(str);
        if (!mc.matches()) {
            return false;
        }
        return true;
    }

    /**
     * <两个时间比较>
     * <功能详细描述>
     * @param startTime:开始时间
     * @param endTime:结束时间
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean iscompare(String startTime,String endTime){
        if(!isRequired(startTime) || !isRequired(endTime)){
            return false;
        }
        Date date=CheckUtil.parser(startTime);
        Date date1=CheckUtil.parser(endTime);
        long i=date1.getTime()-date.getTime();
        if(i>Constants.NUM0){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * <把字符串转换成时间>
     * <功能详细描述>
     * @param datetime:格式为yyyy-MM-dd的字符串
     * @return [参数说明]
     * 
     * @return Date [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static Date parser(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(datetime);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    /**
     * <把日期格式为yyyy-MM-dd转换成字符串>
     * <功能详细描述>
     * @param date:格式为yyyy-MM-dd日期
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String formatYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    
    /**
     * <把字符串转换成yyyy-MM-dd格式的字符串>
     * <功能详细描述>
     * @param str:字符串格式个yyyyyMMdd
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getDate(String str){
        StringBuffer datestr=new StringBuffer();
        datestr.append(str.substring(0,Constants.NUM4)+"-");
        datestr.append(str.substring(Constants.NUM4,Constants.NUM6)+"-");
        datestr.append(str.substring(Constants.NUM6,Constants.NUM8));
        logger.debug("datestr:"+datestr.toString());
        return datestr.toString();
    }
    
    public static void main(String[] args) {
        String startTime="2010-07-16";
//        String endTime="20110617";
//        String datestr=CheckUtil.getDate(endTime);
        //logger.debug("datestr : "+datestr);
        Date date=CheckUtil.parser(startTime);
        logger.debug("str:"+CheckUtil.formatYMD(date));
//        if(CheckUtil.iscompare(startTime, CheckUtil.formatYMD(date))){
//           logger.debug("boolean : true");
//        }else{
//            logger.debug("boolean : false");
//        }
    }
}
