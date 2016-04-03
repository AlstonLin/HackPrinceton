package io.alstonlin.hackprinceton;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.food_dialog);
        dialog.show();
        ((TextView)dialog.findViewById(R.id.name)).setText(capitalize(food.getName()));
        ((TextView)dialog.findViewById(R.id.calories)).setText("Calories\n" + food.getCalories() + "cals");
        ((TextView)dialog.findViewById(R.id.colesterol)).setText("Colesterol\n" + food.getColesterol() + "mg");
        ((TextView)dialog.findViewById(R.id.fat)).setText("Fat\n" + food.getFat() + "mg");
        ((TextView)dialog.findViewById(R.id.protien)).setText("Protien\n" + food.getProtien() + "mg");
        ((TextView)dialog.findViewById(R.id.carbs)).setText("Carbs\n" + food.getCarbs() + "mg");
        ((TextView)dialog.findViewById(R.id.sugar)).setText("Sugar\n" + food.getSugar() + "mg");
        ((TextView)dialog.findViewById(R.id.sodium)).setText("Calories\n" + food.getSodium() + "mg");
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(food.getBitmap(), 0, 0, food.getBitmap().getWidth() / 2, food.getBitmap().getHeight() / 2, matrix, true);
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