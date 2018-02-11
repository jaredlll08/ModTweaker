package com.blamejared.compat.botania;

import net.minecraft.client.resources.*;

import java.lang.reflect.Field;
import java.util.Map;

public class LocalizationHelper {
    
    
    public static void setLocale(String unlocalized, String localized) {
        try {
            Field locale = I18n.class.getDeclaredField("i18nLocale");
            locale.setAccessible(true);
            Locale loc = (Locale) locale.get(null);
            Field propMap = loc.getClass().getDeclaredField("properties");
            propMap.setAccessible(true);
            Map<String, String> map = (Map<String, String>) propMap.get(loc);
            map.put(unlocalized, localized);
        } catch(Exception e) {
            //NO-OP people will complain if it stops translating, and this will get rid of errors on the server
        }
    }
    
}
