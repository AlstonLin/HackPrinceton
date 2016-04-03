package io.alstonlin.hackprinceton;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Serializable {
    private Dialog dialog;
    private PopupWindow window;
    private TabLayout tabLayout;
    private ArrayList<Food> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTabs();
        setupViewPager();
        getSupportActionBar().setTitle("NutriTrack");
        foods = new ArrayList<>();
        DAO.getInstance().setActivity(this);
    }


    private void setupTabs(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Track"));
        tabLayout.addTab(tabLayout.newTab().setText("Visualize"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager(){
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void onFoodAdded(Food food){
        long caloriesPercent = Math.round(food.getCalories() * 100.0 / Food.HEALTHY_CALORIES),
                colesterolPercent = Math.round(food.getColesterol() * 100.0 / Food.HEALTHY_COLESTEROL),
                fatPercent = Math.round(food.getFat() * 100.0 / Food.HEALTHY_FAT),
                protienPercent = Math.round(food.getProtien() * 100.0 / Food.HEALTHY_PROTIEN),
                carbsPercent = Math.round(food.getCarbs() * 100.0 / Food.HEALTHY_CARBS),
                sugarPercent = Math.round(food.getSugar() * 100.0 / Food.HEALTHY_SUGAR),
                sodiumPercent = Math.round(food.getSodium() * 100.0 / Food.HEALTHY_SODIUM);

        TextView caloriesView = (TextView) dialog.findViewById(R.id.calories);
        TextView colesterolView = (TextView) dialog.findViewById(R.id.colesterol);
        TextView fatView = (TextView) dialog.findViewById(R.id.fat);
        TextView protienView = (TextView) dialog.findViewById(R.id.protien);
        TextView carbsView = (TextView) dialog.findViewById(R.id.carbs);
        TextView sugarView = (TextView) dialog.findViewById(R.id.sugar);
        TextView sodiumView = (TextView) dialog.findViewById(R.id.sodium);

        ((TextView) dialog.findViewById(R.id.name)).setText(capitalize(food.getName()));
        caloriesView.setText("Calories\n" + food.getCalories() + "kcal (" + caloriesPercent + "%)");
        colesterolView.setText("Colesterol\n" + food.getColesterol() + "mg (" + colesterolPercent + "%)");
        fatView.setText("Fat\n" + food.getFat() + "mg (" + fatPercent + "%)");
        protienView.setText("Protien\n" + food.getProtien() + "mg (" + protienPercent + "%)");
        carbsView.setText("Carbs\n" + food.getCarbs() + "mg (" + carbsPercent + "%)");
        sugarView.setText("Sugar\n" + food.getSugar() + "mg (" + sugarPercent + "%)");
        sodiumView.setText("Sodium\n" + food.getSodium() + "mg (" + sodiumPercent + "%)");
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Changes color
        if (caloriesPercent >= 35){
            caloriesView.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }
        if (colesterolPercent >= 35){
            colesterolView.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }
        if (fatPercent >= 35){
            fatView.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }
        if (protienPercent >= 35){
            protienView.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }
        if (carbsPercent >= 35){
            carbsView.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }
        if (sugarPercent >= 35){
            sugarView.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }
        if (sodiumPercent >= 35){
            sodiumView.setBackgroundColor(Color.parseColor("#FFEB3B"));
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(food.getBitmap(), food.getBitmap().getWidth() / 2, food.getBitmap().getHeight() / 2, false);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        ((ImageView)dialog.findViewById(R.id.image)).setImageBitmap(rotatedBitmap);
    }

    public void closeDialog(View v){
        dialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    private String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}