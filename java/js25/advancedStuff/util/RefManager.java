package js25.advancedStuff.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefManager {

    public static Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> aClass, String name, Class<?>... parameterTypes) {
        try {
            return aClass.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(String className, String methodName, Class<?>... parameterTypes) {
        try {
            return getMethod(getClass(className), methodName, parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object callMethod(Method method, Object... input) {
        try {
            return method.invoke(null, input);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
