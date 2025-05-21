package org.example;

public class Person {
    public int age = 20;
    public String name = "Nikolay";
    public boolean isEmployed = false;

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", isEmployed=" + isEmployed +
                '}';
    }
}
