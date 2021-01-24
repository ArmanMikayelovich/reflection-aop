package com.mikayelovich.reflecthack.dtUtils;

import org.springframework.stereotype.Service;

@Service
public class MySqlStringEscape implements StringEscape {

    public  String escapeRestrictedChars(String o) {
        if(o != null && !o.isEmpty()) {
            return o.replaceAll("\\\\", "\\\\\\\\")
                    .replaceAll("%", "\\\\%")
                    .replaceAll("_", "\\\\_")
                    .replaceAll("\\\\'", "\\\\\\\\'")
                    .replaceAll("\"", "\\\\\"");
        }
        return o;
    }
}
