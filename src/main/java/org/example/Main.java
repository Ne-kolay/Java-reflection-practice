package org.example;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Person obj = new Person();


        Serializer.deserializeFromXML(Serializer.serializeToXML(obj));




    }
}
        