package io.alstonlin.hackprinceton;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Food implements Serializable{

    public static final int HEALTHY_CALORIES = 2500;
    public static final int HEALTHY_COLESTEROL = 300;
    public static final int HEALTHY_FAT = 70000;
    public static final int HEALTHY_PROTIEN = 56000;
    public static final int HEALTHY_CARBS = 400000;
    public static final int HEALTHY_SUGAR = 36000;
    public static final int HEALTHY_SODIUM = 23000;
    private String name;
    private String id;
    private String imageUrl;
    private Date createdAt;
    private Bitmap bitmap;
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

    public Food(JSONObject json) throws JSONException, ParseException {
        this.id = json.getString("_id");
        this.name = json.getString("name");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        TimeZone utc = TimeZone.getTimeZone("UTC");
        formatter.setTimeZone(utc);
        this.createdAt = formatter.parse(json.getString("createdAt"));
        this.calories = json.getInt("calories");
        this.sodium = json.getInt("sodium");
        this.sugar = json.getInt("sugar");
        this.carbs = json.getInt("carbs");
        this.protien = json.getInt("protien");
        this.fat = json.getInt("fat");
        this.colesterol = json.getInt("colesterol");
        this.imageUrl = DAO.SERVER_URL + "/" + json.getString("filename");
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
