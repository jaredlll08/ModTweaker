package modtweaker2.helpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {
    
    public static <T> T getInstance(Constructor<T> constructor, Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (Exception e) { LogHelper.logError("Exception creating instance of " + constructor.getClass().getName(), e); }

        return null;
    }
    
    public static <T> T getInstance(Class<T> clazz, Object... arguments) {
        Class[] classArray = new Class[arguments.length];
        
        for(int i = 0; i < arguments.length; i++) {
            classArray[i] = arguments[i].getClass();
        }
        
        Constructor<T> constructor = getConstructor(clazz, classArray);
        
        return getInstance(constructor, arguments);
    }
    
    public static Constructor getConstructor(String string, Class... types) {
        try {
            Class clazz = Class.forName(string);
            Constructor constructor = clazz.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception ex) { LogHelper.logError("Exception creating instance of " + string, ex); }

        return null;
    }
    
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class... types) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception ex) { LogHelper.logError("Exception getting constructore of " + clazz.getName(), ex); }
        
        return null;
    }

    public static <T> T getObject(Object o, String... fieldName) {
        Class cls = o.getClass();
        for (String field : fieldName) {
            try {
                Field result = cls.getDeclaredField(field);
                result.setAccessible(true);
                return (T) result.get(o);
            } catch (Exception ex) {}
        }

        return null;
    }

    public static <T> T getFinalObject(Object o, String... fieldName) {
        Class cls = o.getClass();
        for (String field : fieldName) {
            try {
                Field result = cls.getDeclaredField(field);
                result.setAccessible(true);
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(result, result.getModifiers() & ~Modifier.FINAL);
                return (T) result.get(o);
            } catch (Exception ex) {}
        }

        return null;
    }

    public static <T> T getStaticObject(Class cls, String... fieldName) {
        for (String field : fieldName) {
            try {
                Field result = cls.getDeclaredField(field);
                result.setAccessible(true);
                return (T) result.get(null);
            } catch (Exception e) {}
        }

        return null;
    }

    public static void setPrivateValue(Class cls, String field, int var) {
        try {
            Field f = cls.getDeclaredField(field);
            f.setAccessible(true);
            f.setInt(null, var);
        } catch (Exception e) {}
    }

    public static void setPrivateValue(Class cls, Object o, String field, Object var) {
        cpw.mods.fml.relauncher.ReflectionHelper.setPrivateValue(cls, o, var, field);
    }
}
