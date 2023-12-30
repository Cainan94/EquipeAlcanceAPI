package com.cbt.EquipeAlcance.utils;

public interface StringUtils {
    static boolean isEmpty(String string){
        string = string.replace("\\s+", "");
        return string.isEmpty();
    }

    static boolean isNull(String string){
        return string == null;
    }

    static boolean isNullOrEmpty(String string){
        if(string!=null)
            string = string.replace("\\s+", "");
        return string==null || string.isEmpty();
    }
}
