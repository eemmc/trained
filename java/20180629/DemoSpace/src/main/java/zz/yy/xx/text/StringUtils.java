package zz.yy.xx.text;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtils {
	private StringUtils() {
		
	}
	
	
	
	public static String[] findString(String source, String regex) {
		
		LinkedList<String> result=new LinkedList<>();
        try {
        	Pattern pattern=Pattern.compile(regex);
            Matcher matcher = pattern.matcher(source);
            while(matcher.find()) {
            	result.add(matcher.group());
            }
        } catch (Exception e) {
            //do nothing;
        }

        return result.toArray(new String[result.size()]);
    }
	
	
	
	public static String sha1(String original) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            //使用指定的字节数组更新摘要。
            md.update(original.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte[] result = md.digest();
            @SuppressWarnings("resource")
			Formatter formatter = new Formatter(Locale.getDefault());
            for (byte bit : result) {
                formatter.format(Locale.getDefault(), "%02X", bit);
            }
            formatter.flush();
            return formatter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
