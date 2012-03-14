package z.hol.android.mc.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import z.hol.android.mc.R;
import android.content.Context;
import android.content.res.Resources;

public class Utils {
	public static HashMap<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();
	
	/**
	 * 时间戳转换为日期(Date型)
	 * @param times 时间戳
	 * @return	Date型 “日期”
	 */
	public static Date times2Date(long times){
		
		return new Date(times);
	}
	
	/**
	 * 时间戳转换为日期(String 型)
	 * @param times
	 * @return String型"日期"("yyyy-MM-dd HH:mm:ss")
	 */
	public static String times2DateStr(long times){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s =sdf.format(times2Date(times));		
		return s;
	}
	
	public static String times2DateStr(long times, boolean isShort){
		Date date = times2Date(times);
		if (isShort){
			String month = String.format("%tb", date);
			String day = String.format("%te", date);
			String hour = String.format("%tI", date);
			String minute = String.format("%tM",date);
			String p = String.format("%tp",date).toUpperCase();
			//May 21  3:13PM
			return String.format("%s %s  %s:%s%s", month,day,hour,minute,p);
		}else{
			String year = String.format("%tY",date);
			String month = String.format("%tb", date);
			String day = String.format("%te", date);
			String hour = String.format("%tI", date);
			String minute = String.format("%tM",date);
			String p = String.format("%tp",date).toUpperCase();
			//2011 May 21  3:13PM
			return String.format("%s %s %s  %s:%s%s", year, month,day,hour,minute,p);
		}
	}
	
	public static String timestamp2DateStrNoTime(Context context, long times){
		Date date = times2Date(times);

		String year = String.format("%tY",date);
		String month = String.format("%tb", date);
		String day = String.format("%te", date);
		//String hour = String.format("%tI", date);
		//String minute = String.format("%tM", date);
		//String p = String.format("%tp", date).toUpperCase();
		// May 21
		String formatStr = context.getString(R.string.format_date_no_time);		
		//return String.format("%s %s", month, day);
		return String.format(formatStr, month, day);
		
	}
	
	/**
	 * 时间戳转换为时间(23:12)
	 * @param times
	 * @return
	 */
	public static String timesstamp2Time(long times){
		return timesstamp2Time(times, false);
	}
	
	/**
	 * 时间戳转换为时间(23:12)
	 * @param times
	 * @param hasSecend 是否要秒
	 * @return
	 */
	public static String timesstamp2Time(long times, boolean hasSecend){
		String format;
		if (hasSecend){
			format = "HH:mm:ss";
		}else{
			format = "HH:mm";
		}
		
		Date date = times2Date(times);	
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s =sdf.format(date);	
		
		// 23:12
		return s;
	}
	
	
	public static String timestamp2TimeNoYear(long times){
		
		String format = "MM-dd HH:mm";
		Date date = times2Date(times);	
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s =sdf.format(date);	
		
		// 03- 14 23:12
		return s;
	}
	
	public static String timesatmp2DataStrNoTimeWithYear(long times){
		Date date = times2Date(times);

		/*
		String year = String.format("%tY",date);
		String month = String.format("%tb", date);
		String day = String.format("%te", date);
		*/
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String s =sdf.format(date);	
		
		// 2011-01-02
		return s;
	}
	
	/**
	 * 日期转换为时间戳
	 * @param d 日期
	 * @return 时间戳
	 */
	public static long date2Times(Date d){		
		return (long) d.getTime()/1000;
	}
	/**
	 * 日期转换为时间戳
	 * @param dStr 日期
	 * @return 时间戳
	 * @throws ParseException
	 */
	public static long Date2Times(String dStr) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(dStr);		
		return date2Times(d);
	}
	
	
	public static final long SECOND = 1000;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;
	
	public static String timeCost(long startTime, Resources res){
		return timeCost(System.currentTimeMillis(), startTime, res);
	}
	
	public static String timeCost(long endTime, long startTime, Resources res){
		long cost = endTime - startTime;
		cost = Math.abs(cost);
		int count;
		String costStr = null;
		if (cost < MINUTE){
			//below 1 minter
			count = (int) (cost / SECOND);
			costStr = res.getQuantityString(R.plurals.secends, count, count);
			//costStr = String.format("%d sec", cost / SECOND);
		}else if (cost < HOUR){
			//below 1 hour
			count = (int) (cost / MINUTE);
			costStr = res.getQuantityString(R.plurals.minute, count, count);
			//costStr = String.format("%d min", cost / MINUTE);
		}else if (cost < DAY){
			count = (int) (cost / HOUR);
			costStr = res.getQuantityString(R.plurals.hours, count, count);
			//costStr = String.format("%d hour", cost / HOUR);
		}else {
			//costStr = times2DateStr(startTime, true);
			//costStr = timestamp2DateStrNoTime(startTime);
			costStr = timesstamp2Time(startTime);
		}
		
		return costStr;
	}
	
	public static long getDayStamp(long time){
		return time / DAY;
	}
	
	/**
	 * 根据默认格式来解析时间字符串.<br>
	 * 默认时间格式为 <b>EEE MMM dd HH:mm:ss Z yyyy</b>
	 * @param dateStr 时间字符串
	 * @return
	 */
	public static Date parseDateDefault(String dateStr){
		return parseDate(dateStr, "EEE MMM dd HH:mm:ss Z yyyy");
	}
	
	/**
	 * 根据时间格式，解析时间字符串
	 * @param dateStr 时间字符串, 比如: Mon Oct 18 10:06:57 +0800 2010
	 * @param formatStr 时间格式， 比如： EEE MMM dd HH:mm:ss Z yyyy
	 * @return
	 */
	public static Date parseDate(String dateStr, String formatStr){
        if(dateStr==null||"".equals(dateStr)){
        	return null;
        }
    	SimpleDateFormat sdf = formatMap.get(formatStr);
        if (null == sdf) {
            sdf = new SimpleDateFormat(formatStr, Locale.ENGLISH);
            //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.put(formatStr, sdf);
        }
        try {
        	Date date = null;
            synchronized(sdf){
                // SimpleDateFormat is not thread safe
            	date = sdf.parse(dateStr);
            }
            return date;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
		return null;
	}
	
	/**
	 * 获取一定长度的随机字符串
	 * @param length 字符串长度
	 * @return
	 */
	public static String getRandomStr(int length){
		int i = 0;
		char[] base = new char[]{
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
				'0','1','2','3','4','5','6','7','8','9'
		};
		StringBuilder sb = new StringBuilder("");
		for (; i<length ;i++){
			char r = base[(int) (Math.random()*61)];
			sb.append(r);
		}		
		return sb.toString();
	}
	
	/**
	 * 将byte数组转换为十六进制形式字符串
	 * @param bytes byte数组
	 * @return 十六进制形式字符串
	 */
	public static String hexArrayToString(byte [] bytes){
		StringBuilder sb = new StringBuilder("");		
		for (byte b : bytes){
			sb.append(Integer.toHexString(b));
			sb.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 将两个int组合为一个long
	 * @param lh 高位
	 * @param ll 低位
	 * @return long
	 */
	public static long mergeInt2Long(int lh, int ll){
		return ((long)lh << 32) + ll;
	}
	
	/**
	 * get values pair with key<br>
	 * values pair like " name= johann"
	 * @param valuesPair
	 * @param key
	 * @return
	 */
	public static String getValuePair(String valuesPair, String key){
		String values = null;
		String fullKey = key + "=";
		if (valuesPair.contains(fullKey)){
			int sp = valuesPair.indexOf(fullKey);
			sp = sp + fullKey.length();
			values = valuesPair.substring(sp);
		}
		
		return values;
	}
	
	public static String getRemainStr(String strHead, String base){
		String remain = null;
		int headLength = strHead.length();
		int headStartPos = base.indexOf(strHead);
		int headEndPos = headStartPos + headLength;
		remain = base.substring(headEndPos);
		
		return remain;
	}
	
	/**
	 * 将字符串转换为UTF-8编码
	 * @param oldStr 转换前的字符串
	 * @return UTF-8编码的字符串
	 */
	public static String toUtf8String(String oldStr){
		String newStr = null;
		try {
			newStr = new String(oldStr.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newStr;
	}
	
	/**
	 * 整形换布尔型
	 * @param i
	 * @return 如果是0则返回 false, 其它则为trues
	 */
	public static boolean intToBoolean(int i){
		return i == 0 ? false : true;
	}
	
	/**
	 * 布尔型转换为整形
	 * @param b
	 * @return false则返回0，true返回1
	 */
	public static int booleanToInt(boolean b){
		return b ? 1 : 0;
	}
	
	/**
	 * 字符数组大小写转换
	 * @param src 源数组
	 * @param lowerCase 为true转为小写，否则为大写
	 * @return 转换后的数组
	 */
	public static String[] stringArrayToCase(String[] src, boolean lowerCase){
		int count = src.length;
		String[] newStringArray = new String[count];
		for (int i = 0; i < count; i ++){
			if (lowerCase){
				newStringArray[i]  = src[i].toLowerCase();
			}else{
				newStringArray[i] = src[i].toUpperCase();
			}
		}
		return newStringArray;
	}
	
	public static String stringArrayToString(String[] strArray){
		if (strArray == null){
			return null;
		}
		int count = strArray.length;
		String str = null;
		if (count > 0){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < count; i ++){
				if (i > 0){
					sb.append(",");
				}
				sb.append(strArray[i]);
			}
			str = sb.toString();
		}
		return str;
	}
	
	/**
	 * 将String通过分隔符变为一个数组
	 * @param str
	 * @return
	 */
	public static String[] stringToStringArray(String str){
		if (str == null){
			return null;
		}
		String[] strArray = null;
		strArray = str.split(",");
		return strArray;
	}
	
	/**
	 * 判断字符是否是ascii码。<br />
	 * 可用在判断短信的字数等地方
	 * <p>
	 * 
	 * @param c 要判断的字符
	 * @return
	 */
	public static boolean isAscii(char c){
		if (0 <= c && c <= 255){
			return true;
		}else
			return false;
	}
	
	/**
	 * 检查List是为空。
	 * @param list
	 * @return 如果list为null或才size <= 0 返回false.
	 */
	public static boolean isListEmpty(List<?> list){
		if (list == null || list.isEmpty()){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 将一个字符串用数组来表示
	 * @param str
	 * @return
	 */
	public static String[] stringToArray(String str){
		return new String[]{str};
	}
	
	/**
	 * 字符串是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean sameString(String a, String b){
		/*
		if (a == null){
			if (b != null){
				return false;
			}else{
				return true;
			}
		}else{
			return a.equals(b);
		}
		*/
		return  (a == null) ? (b == null ? true : false ): a.equals(b);
	}
	
	/**
	 * 复制一份字符串数组
	 * @param strArray
	 * @return
	 */
	public static String[] copyStringArray(String[] strArray){
		String[] s = null;
		if (strArray != null){
			s = new String[strArray.length];
			for (int i = 0; i < strArray.length; i ++){
				s[i] = strArray[i];
			}
		}
		return s;
	}
	
	/**
	 * 复制一个List
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> copyList(List<T> list){
		List<T> l = null;
		if (list != null){
			l = new ArrayList<T>();
			for (int i = 0; i < list.size(); i ++){
				l.add(list.get(i));
			}
		}
		return l;
	}
	
	
    /**
     * Returns true if all the characters are meaningful as digits
     * in a phone number -- letters, digits, and a few punctuation marks.
     */
    public static boolean isPhoneNumber(CharSequence cons) {
        int len = cons.length();

        for (int i = 0; i < len; i++) {
            char c = cons.charAt(i);

            if ((c >= '0') && (c <= '9')) {
                continue;
            }
            if ((c == ' ') || (c == '-') || (c == '(') || (c == ')') || (c == '.') || (c == '+')
                    || (c == '#') || (c == '*')) {
                continue;
            }
            if ((c >= 'A') && (c <= 'Z')) {
                continue;
            }
            if ((c >= 'a') && (c <= 'z')) {
                continue;
            }

            return false;
        }

        return true;
    }
}
