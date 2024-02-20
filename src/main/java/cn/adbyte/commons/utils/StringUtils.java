package cn.adbyte.commons.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

    /**
     * 转小写字母
     */
    public static String lowerCase(final String str) {
        return org.apache.commons.lang3.StringUtils.lowerCase(str);
    }

    /**
     * @author Adam 2023/12/1 14:42 说明: 判断两个字符串是否相等
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(str1, str2);
    }

    /**
     * 判断是否为空白字符
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(cs);
    }

    /**
     * 逗号分割转Set
     * a,b,c,d    ==>  [a,b,c,d]
     * a,,c,d     ==>  [a,c,d]
     * ,a,,c,d,    ==>  [a,c,d]
     */
    public static Set<String> commaDelimitedTrimEmptySet(String str) {
        Set<String> strings = org.springframework.util.StringUtils.commaDelimitedListToSet(str);
        return strings.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    /**
     * 逗号分割转Set
     * a,b,c,d    ==>  [a,b,c,d]
     * a,,c,d     ==>  [a,c,d]
     * ,a,,c,d,    ==>  [a,c,d]
     */
    public static LinkedHashSet<String> commaDelimitedTrimEmptyLinkedHashSet(String str) {

        String[] strings = org.springframework.util.StringUtils.commaDelimitedListToStringArray(str);
        List<String> collect = Arrays.stream(strings).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return new LinkedHashSet<>(collect);
    }

    /**
     * 逗号分割转List
     * 1,2,3,4    ==>  [1,2,3,4]
     * 1,,3,4     ==>  [1,3,4]
     * ,1,,3,4,   ==>  [1,3,4]
     */
    public static List<String> commaDelimitedTrimEmptyList(String str) {
        String[] strings = org.springframework.util.StringUtils.commaDelimitedListToStringArray(str);
        return Arrays.stream(strings).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    public static String[] split(String str, String delimiter) {
        return org.apache.commons.lang3.StringUtils.split(str, delimiter);
    }

    /**
     * 逗号分割转List<Integer>
     * 1,2,3,4    ==>  [1,2,3,4]
     * 1,,3,4     ==>  [1,3,4]
     * ,1,,3,4,   ==>  [1,3,4]
     * ,abc,      ==>  []
     */
    public static List<Integer> commaDelimitedTrimEmptyListInteger(String str) {

        if (RegexUtils.check(RegexUtils.Type.ONLY_NUMBER_OR_EMPTY, str)) {
            // 不为空时，必须为逗号分割的数字才能继续
            String id = commaDelimitedTrimEmptyString(str);
            if (StringUtils.isNotBlank(id)) {
                return Arrays.stream(id.split(","))
                        .mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 逗号分割转List<Long>
     * 1,2,3,4    ==>  [1,2,3,4]
     * 1,,3,4     ==>  [1,3,4]
     * ,1,,3,4,   ==>  [1,3,4]
     * abc,       ==>  []
     */
    public static List<Long> commaDelimitedTrimEmptyListLong(String str) {

        if (RegexUtils.check(RegexUtils.Type.ONLY_NUMBER_OR_EMPTY, str)) {
            // 不为空时，必须为逗号分割的数字才能继续
            String id = commaDelimitedTrimEmptyString(str);

            if (StringUtils.isNotBlank(id)) {
                return Arrays.stream(id.split(","))
                        .mapToLong(Long::valueOf).boxed().collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    /**
     * 逗号分割去空去重
     * <p>
     * ,           ==>  ""
     * a,b,c,d     ==>  a,b,c,d
     * a,,c,d      ==>  a,c,d
     * ,a,,c,d,    ==>  a,c,d
     * ,a,c,c,d,   ==>  a,c,d
     */
    public static String commaDelimitedTrimEmptyString(String str) {
        Set<String> strings = org.springframework.util.StringUtils.commaDelimitedListToSet(str);
        return collectionToCommaDistinctString(strings);
    }

    /**
     * 逗号分割，每个元素前拼接单引号
     * 无序
     * <p>
     * 1,2,3,4     ==>  '1','2','3','4'
     */
    public static String stringToCommaDistinctString(String str) {
        Set<String> strings = org.springframework.util.StringUtils.commaDelimitedListToSet(str);
        return org.springframework.util.StringUtils.collectionToDelimitedString(strings, ",", "'", "'");
    }

    /**
     * 逗号分割去空去重
     * 逗号分割，每个元素前拼接单引号
     * null        ==>  ''
     * ''          ==>  ''
     * 1           ==>  '1'
     * 1,ab        ==>  '1','ab'
     * 1,2,3,4,,   ==>  '1','2','3','4'
     *
     * @param str
     * @return
     */
    public static String toSqlInString(String str) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return "";
        }
        str = StringUtils.escapeSql(str);
        String str1 = StringUtils.commaDelimitedTrimEmptyString(str);
        return StringUtils.stringToCommaDistinctString(str1);
    }

    public static String escapeSql(String str) {
        return str == null ? null : org.apache.commons.lang3.StringUtils.replace(str, "'", "''");
    }

    /**
     * 集合字符串转去重后的文本
     * 乱序 去重
     * [1,2,3,4]     ==>  1,2,3,4
     * [1,,3,4]      ==>  1,3,4
     * [,1,,3,4,]    ==>  1,3,4
     * [,1,3,3,4,]   ==>  1,3,4
     */
    public static String collectionToCommaDistinctString(Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return "";
        }
        coll = coll.stream().filter(c -> {
            if (c instanceof CharSequence) {
                return StringUtils.isNotBlank((CharSequence) c);
            }
            return c != null;
        }).collect(Collectors.toSet());
        return org.springframework.util.StringUtils.collectionToCommaDelimitedString(coll);
    }

    /**
     * 集合字符串转文本
     * 有序 不去重
     * [1,2,3,4]     ==>  1,2,3,4
     * [1,,3,4]      ==>  1,3,4
     * [,1,,3,4,]    ==>  1,3,4
     * [,1,3,3,4,]   ==>  1,3,4
     */
    public static String collectionToCommaString(Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return "";
        }
        coll = coll.stream().filter(c -> {
            if (c instanceof CharSequence) {
                return StringUtils.isNotBlank((CharSequence) c);
            }
            return c != null;
        }).collect(Collectors.toList());
        return org.springframework.util.StringUtils.collectionToCommaDelimitedString(coll);
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return org.springframework.util.StringUtils.startsWithIgnoreCase(str, prefix);
    }

    public static boolean endsWithIgnoreCase(String str, String prefix) {
        return org.springframework.util.StringUtils.endsWithIgnoreCase(str, prefix);
    }

    /**
     * 在开头和结尾处附加逗号
     */
    public static String appendCommaAtStartAndEnd(String str) {
        if (str == null) {
            return ",";
        }
        boolean startsWith = StringUtils.startsWithIgnoreCase(str, ",");
        if (!startsWith) {
            str = ",".concat(str);
        }
        boolean endWith = StringUtils.endsWithIgnoreCase(str, ",");
        if (!endWith) {
            str = str.concat(",");
        }
        return str;
    }

    public static String strip(String str, final String stripChars) {
        return org.apache.commons.lang3.StringUtils.strip(str, stripChars);
    }

    /**
     * 分号分割转List<Long>
     * 1;2;3;4    ==>  [1,2,3,4]
     * 1;;3;4     ==>  [1,3,4]
     * ;1;;3,;4;   ==>  [1,3,4]
     * abc;       ==>  []
     */
    public static List<Long> semicolonDelimitedTrimEmptyListLong(String str) {

        if (StringUtils.isNotBlank(str)) {
            // 不为空时，必须为逗号分割的数字才能继续
            String id = commaDelimitedTrimEmptyString(str);

            if (StringUtils.isNotBlank(id)) {
                return Arrays.stream(id.split(";"))
                        .mapToLong(Long::valueOf).boxed().collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

}
