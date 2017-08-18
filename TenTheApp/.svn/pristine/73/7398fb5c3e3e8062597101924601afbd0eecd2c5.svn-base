package com.nvcomputers.ten.utils;

/**
 * Created by rkumar4 on 5/9/2017.
 */

public class StringUtils {
    public static String trimWhiteSpaceAndNewLine(String str) {
        //return String. (str) ? str : str.trim ().trim ('\r').trim ('\n');
        return isNullOrEmpty(str) ? str : str.trim().replaceAll("\r","").replace("\n","");
    }

    /* public static String removeAllSpaces (String str)
     {
         return isNullOrEmpty (str) ? str : Regex.Replace (str, @"\s+", "");
     }*/
    public static String removeAllHashTags(String str) {
        return isNullOrEmpty(str) ? str : str.replace("#", "");
    }

    public static String removeLeadingSymbol(String str, String symbol) {

        if (str != null && str.substring(0, 1) == symbol) {
            str = str.substring(1);
        }

        return str;
    }

    public static boolean isNullOrEmpty(String string) {
        if (string == null || string.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isValidNumber(String str) {
        if (str.length() <= 4) {
            return false;
        }
        if (str.length() >= 11) {
            return false;
        }

        return true;
    }

    public static boolean IsValidOTP(String str) {
        if (str.length() < 4) {
            return false;
        }
        if (str.length() >= 5) {
            return false;
        }

        return true;
    }


    public static boolean IsValidPassword(String str) {
        if (str.length() <= 5) {
            return false;
        }
        //if (!Regex.IsMatch (str, "[A-Z]")) {
        //	return false;
        //}
        //if (!Regex.IsMatch (str, "[a-z]")) {
        //	return false;
        //}
        //if (str.IndexOfAny ("!@#$%^&+=".ToCharArray ()) == -1) {
        //	return false;
        //}

        return true;
    }
}
