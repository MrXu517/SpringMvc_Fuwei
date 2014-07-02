package com.fuwei.util;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanyuPinyinUtil {
	public static String getFirstSpellByString(String string) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		char[] chars = string.toCharArray();
		StringBuffer stringBuffer = new StringBuffer();
		for (char c : chars) {
			try {
				String[] strings = PinyinHelper.toHanyuPinyinStringArray(c,
						format);
				if (strings != null && strings.length > 0) {
					stringBuffer.append(strings[0].charAt(0));
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}

}
