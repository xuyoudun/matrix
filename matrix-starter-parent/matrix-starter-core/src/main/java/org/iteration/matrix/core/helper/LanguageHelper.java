package org.iteration.matrix.core.helper;

import org.apache.commons.lang3.LocaleUtils;

import java.util.Locale;

public class LanguageHelper {

    private LanguageHelper() {
    }

    /**
     * 根据当前登陆用户获取语言信息
     *
     * @return String
     */
    public static String language() {
        return "zh_CN";
    }

    public static Locale locale() {
        return LocaleUtils.toLocale(LanguageHelper.language());
    }
}
