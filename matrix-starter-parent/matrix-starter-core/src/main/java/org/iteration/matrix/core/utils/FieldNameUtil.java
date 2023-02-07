package org.iteration.matrix.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 驼峰法-下划线互转
 */
public class FieldNameUtil {

    private static final Pattern UNDERLINE_TO_CAMEL_PATTERN = Pattern.compile("([A-Za-z\\d]+)(_)?");
    private static final Pattern CAMEL_TO_UNDERLINE_PATTERN = Pattern.compile("[A-Z]([a-z\\d]+)?");

    private FieldNameUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean smallCamel) {
        if (StringUtils.isEmpty(line)) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        Pattern pattern = UNDERLINE_TO_CAMEL_PATTERN;
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0))
                    : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line      源字符串
     * @param upperCase 是否大写
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line, boolean upperCase) {
        if (StringUtils.isEmpty(line)) {
            return StringUtils.EMPTY;
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Pattern pattern = CAMEL_TO_UNDERLINE_PATTERN;
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(upperCase ? word.toUpperCase() : word.toLowerCase());
            sb.append(matcher.end() == line.length() ? StringUtils.EMPTY : "_");
        }
        return sb.toString();
    }

}
