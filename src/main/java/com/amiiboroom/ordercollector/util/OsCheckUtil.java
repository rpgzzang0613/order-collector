package com.amiiboroom.ordercollector.util;

import com.amiiboroom.ordercollector.util.enums.OsType;
import org.springframework.stereotype.Component;

@Component
public class OsCheckUtil {

    public OsType getOsType() {

        OsType result;
        String os_name = System.getProperty("os.name").toLowerCase();

        if(os_name.contains("win")) {
            result = OsType.WINDOWS;
        }else if(os_name.contains("mac")) {
            result = OsType.MAC;
        }else if(os_name.contains("nix") || os_name.contains("nux") || os_name.indexOf("aix") > 0) {
            result = OsType.LINUX;
        }else {
            result = OsType.UNKNOWN;
        }

        return result;
    }

}
