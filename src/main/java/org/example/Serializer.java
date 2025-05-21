package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Serializer {

    public static String serializeToXML(Object o) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder(); //здесь собираем xml

        sb.append("<").append(o.getClass().getName()).append(">\n"); //достаём имя класса

        Field[] fields = o.getClass().getFields(); //получаем все поля
        for (Field field : fields) {
            field.setAccessible(true); //получаем доступ к полю на случай, если оно не public
            sb.append("    <").append(field.getName()).append(">")
                    .append(field.get(o)) //получаем значение поля в переданном объекте
                    .append("</").append(field.getName()).append(">\n");
        }

        sb.append("</").append(o.getClass().getName()).append(">\n");

        System.out.println("Object of type {" + o.getClass().getSimpleName() + "} " +
                "is " + "successfully serialised to XML: \n" + sb.toString());
        return sb.toString(); //отдаём собранный xml
    }


    public static Object deserializeFromXML(String xml) {
        try {
            String className = xml.substring(xml.indexOf("<") + 1, xml.indexOf(">"));
            Class<?> clazz = Class.forName(className);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                String openTag = "<" + field.getName() + ">";
                String closeTag = "</" + field.getName() + ">";
                int openIndex = xml.indexOf(openTag);
                int closeIndex = xml.indexOf(closeTag);

                //проверяем наличие открываеющего и закрывающего тега
                if (openIndex == -1 || closeIndex == -1) {
                    continue;
                }

                int start = openIndex + openTag.length();
                int end = closeIndex;

                String valueStr = xml.substring(start, end).trim();
                Class<?> fieldType = field.getType();
                Object value = parseStringToFieldType(valueStr, fieldType);
                field.set(instance, value);
            }
            System.out.println("XML is successfully deserialized to new object:\n" + instance.toString());
            return instance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Ошибка десериализации", e);
        }
    }

//метод для приведения строки к типу поля
    public static Object parseStringToFieldType(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else if (type == short.class || type == Short.class) {
            return Short.parseShort(value);
        } else if (type == byte.class || type == Byte.class) {
            return Byte.parseByte(value);
        } else if (type == char.class || type == Character.class) {
            return value.charAt(0); // предполагаем, что в строке один символ
        } else if (type == String.class) {
            return value;
        }

        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }



}

