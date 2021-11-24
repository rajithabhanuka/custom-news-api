package org.comppress.customnewsapi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomStringUtils {

    public static String getImgLink(String xmlInput){
        int index = xmlInput.indexOf("src=\"");
        if(index == -1) return null;
        String substr = xmlInput.substring(index + 5);
        int endIndex = substr.indexOf("\"");
        if(endIndex == -1) return null;
        String imgUrl = substr.substring(0, endIndex);
        return imgUrl;
    }

    public static String getImgLinkWithMatcher(String xmlInput){
        Pattern pattern = Pattern.compile("(<img .*?>)");
        Matcher matcher = pattern.matcher(xmlInput);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
