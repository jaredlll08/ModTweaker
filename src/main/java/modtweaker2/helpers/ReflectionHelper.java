package modtweaker2.helpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {
    
    /**
     * Creates a new instance with the provided constructor and arguments.
     * @param constructor The constructor of the class for which a new instance should be created.
     * @param arguments Array of objects to be passed as arguments to the constructor call.
     * @return A new object created by calling the constructor.
     */
    public static <T> T getInstance(Constructor<T> constructor, Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (Exception e) { LogHelper.logError("Exception creating instance of " + constructor.getClass().getName(), e); }

        return null;
    }
    
    /**
     * Obtains the constructor for the named class identified by the parameter types.
     * @param className The fully qualified name of the class.
     * @param argumentTypes Parameter types to identify the constructor.
     * @return Constructor object representing the declared constructor for the parameter types.
     */
    public static Constructor<?> getConstructor(final String className, final Class<?>... argumentTypes) {
        if(className == null || argumentTypes == null) {
            LogHelper.logError("The provided class name or arguments can't be null.");
            return null;
        }
        
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor(argumentTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception ex) { LogHelper.logError("Exception getting constructore of " + className, ex); }

        return null;
    }
    
    /**
     * Obtains the constructor for the class identified by the parameter types.
     * @param clazz The Class for which the constructor should be obtained.
     * @param argumentTypes Parameter types to identify the constructor.
     * @return Constructor object representing the declared constructor for the parameter types.
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... types) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception ex) { LogHelper.logError("Exception getting constructore of " + clazz.getName(), ex); }

        return null;
    }

    /**
     * Returns the value of a private field for an object instance.
     * @param object An object instance from which the field value is to be extracted.
     * @param fieldNames A list of field names for which the value should be extracted.
     * The functions returns value of the first field found.
     * @return The value of the provided field name.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObject(Object object, String... fieldNames) {
        Class<?> cls = object.getClass();
        for (String field : fieldNames) {
            try {
                Field result = cls.getDeclaredField(field);
                result.setAccessible(true);
                return (T) result.get(object);
            } catch (Exception ex) {}
        }

        return null;
    }

    /**
     * Returns the value of a private final field for an object instance and removes the final modifier.
     * @param object An object instance from which the field value is to be extracted.
     * @param fieldNames A list of field names for which the value should be extracted.
     * The functions returns value of the first field found.
     * @return The value of the provided field name.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFinalObject(Object object, String... fieldNames) {
        Class<?> cls = object.getClass();
        for (String field : fieldNames) {
            try {
                Field result = cls.getDeclaredField(field);
                result.setAccessible(true);
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(result, result.getModifiers() & ~Modifier.FINAL);
                return (T) result.get(object);
            } catch (Exception ex) {}
        }
        
        return null;
    }

    /**
     * Returns the value of a private static field for a class.
     * @param clazz The class for which the field value is to be extracted.
     * @param fieldNames A list of field names for which the value should be extracted.
     * @return The value of the provided field name.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getStaticObject(Class<?> clazz, String... fieldNames) {
        for (String field : fieldNames) {
            try {
                Field result = clazz.getDeclaredField(field);
                result.setAccessible(true);
                return (T) result.get(null);
            } catch (Exception e) {}
        }

        return null;
    }
    
    /**
     * Returns the value of a private static field for a class.
     * @param className The fully qualified name of the class.
     * @param fieldNames A list of field names for which the value should be extracted.
     * @return The value of the provided field name.
     */
    public static <T> T getStaticObject(String className, String... fieldNames) {
        try {
            Class<?> clazz = Class.forName(className);
            return getStaticObject(clazz, fieldNames);
        } catch (ClassNotFoundException e) { }
        
        return null;
    }

    public static void setPrivateValue(Class<?> cls, String field, int var) {
        try {
            Field f = cls.getDeclaredField(field);
            f.setAccessible(true);
            f.setInt(null, var);
        } catch (Exception e) {}
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void setPrivateValue( Class cls, Object o, String field, Object var) {
        cpw.mods.fml.relauncher.ReflectionHelper.setPrivateValue(cls, o, var, field);
    }
}
