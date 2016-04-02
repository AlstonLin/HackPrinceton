package io.alstonlin.hackprinceton;

public class Food {
    private String name;
    private String id;
    private int calories;
    private int colesterol;
    private int fat;
    private int protien;
    private int carbs;
    private int sugar;
    private int sodium;

    public Food(String name, String id, int calories, int colesterol, int fat, int protien, int carbs, int sugar, int sodium) {
        this.name = name;
        this.id = id;
        this.calories = calories;
        this.colesterol = colesterol;
        this.fat = fat;
        this.protien = protien;
        this.carbs = carbs;
        this.sugar = sugar;
        this.sodium = sodium;
    }

    public Food(){

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getCalories() {
        return calories;
    }

    public int getColesterol() {
        return colesterol;
    }

    public int getFat() {
        return fat;
    }

    public int getProtien() {
        return protien;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getSugar() {
        return sugar;
    }

    public int getSodium() {
        return sodium;
    }
}
