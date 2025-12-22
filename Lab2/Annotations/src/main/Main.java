package main;

import annotation.MyAnnotation;
import utils.Book;
import utils.OfficeWorker;

import java.lang.reflect.*;

public class Main {
    private static Object createValue(Class<?> type) throws ReflectiveOperationException {
        if (type.isPrimitive()) {
            return switch (type.getName()) {
                case "int" -> 1;
                case "double" -> 1.0;
                case "float" -> 1f;
                case "long" -> 1L;
                case "short" -> (short) 1;
                case "byte" -> (byte) 1;
                case "boolean" -> true;
                case "char" -> 'A';
                default -> null;
            };
        }

        if (type == String.class) {
            return "Тестовая строка";
        }

        return createObject(type);
    }

    private static Object createObject(Class<?> type) throws ReflectiveOperationException {
        try {
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
                constructor.setAccessible(true);
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    Class<?> p = paramTypes[i];
                    params[i] = createValue(p);
                }

                return constructor.newInstance(params);
            }
            throw new NoSuchMethodException("Нет подходящего конструктора для " + type.getName());
        }
    }

    private static Object[] generateParams(Class<?>[] types) throws ReflectiveOperationException {
        Object[] params = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            params[i] = createValue(types[i]);
        }
        return params;
    }

    private static void invokeAnnotatedMethods(Object obj) {
        Method[] methods = obj.getClass().getDeclaredMethods();

        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if (!(Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers))) {
                continue;
            }

            if (!method.isAnnotationPresent(MyAnnotation.class)) {
                continue;
            }

            int repeat = method.getAnnotation(MyAnnotation.class).repeat();
            for (int i = 0; i < repeat; i++) {
                try {
                    method.setAccessible(true);
                    Object[] generatedParams = generateParams(method.getParameterTypes());
                    if (Modifier.isStatic(modifiers)) {
                        method.invoke(null, generatedParams);
                    } else {
                        method.invoke(obj, generatedParams);
                    }
                } catch (InvocationTargetException e) {
                    System.out.println("Ошибка в методе: " + e.getTargetException().getMessage());

                } catch (ReflectiveOperationException e) {
                    System.out.println("Ошибка рефлексии: " + e.getMessage());

                } catch (RuntimeException e) {
                    System.out.println("Неожиданная ошибка: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        Book book = new Book();
        OfficeWorker worker = new OfficeWorker();

        System.out.println("Вызов методов Book:");
        invokeAnnotatedMethods(book);

        System.out.println("\nВызов методов OfficeWorker:");
        invokeAnnotatedMethods(worker);
    }
}
