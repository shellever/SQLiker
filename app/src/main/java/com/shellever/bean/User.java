package com.shellever.bean;


public class User {
    private String name;
    private String gender;
    private int age;
    private float asset;

    public User() {
    }

    public User(String name, String gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public User(String name, String gender, int age, float asset) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.asset = asset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getAsset() {
        return asset;
    }

    public void setAsset(float asset) {
        this.asset = asset;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", asset=" + asset +
                '}';
    }
}
