package io.alstonlin.hackprinceton;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * The Fragment for the image feed in AppActivity
 */
public class HistoryFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";
    private MainActivity activity;
    private HistoryAdapter adapter;

    /**
     * Use this Factory method to create the Fragment instead of the constructor.
     * @param activity The Activity this Fragment will be attached to
     * @return The new Fragment instance
     */
    public static HistoryFragment newInstance(MainActivity activity) {
        final HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    private void setupAdapter(View v){
        ListView list = (ListView) v.findViewById(R.id.list);
        HistoryAdapter adapter = new HistoryAdapter(activity, this);
        list.setAdapter(adapter);
        this.adapter = adapter;
        DAO.getInstance().getFood(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        setupAdapter(v);
        return v;
    }

    public void setupStats(){
        if (getView() == null) return;
        TextView caloriesView = (TextView) getView().findViewById(R.id.calories);
        TextView colesterolView = (TextView) getView().findViewById(R.id.colesterol);
        TextView fatView = (TextView) getView().findViewById(R.id.fat);
        TextView protienView = (TextView) getView().findViewById(R.id.protien);
        TextView carbsView = (TextView) getView().findViewById(R.id.carbs);
        TextView sugarView = (TextView) getView().findViewById(R.id.sugar);
        TextView sodiumView = (TextView) getView().findViewById(R.id.sodium);
        int calories = 0, colesterol = 0, fat = 0, protien = 0, carbs = 0, sugar = 0, sodium = 0;
        for (Food f : activity.getFoods()){
            if (DateUtils.isToday(f.getCreatedAt().getTime())){
                calories += f.getCalories();
                colesterol += f.getColesterol();
                fat += f.getFat();
                protien += f.getProtien();
                carbs += f.getCarbs();
                sugar += f.getSugar();
                sodium += f.getSodium();
            }
        }
        long caloriesPercent = Math.round(calories * 100.0 / Food.HEALTHY_CALORIES),
                colesterolPercent = Math.round(colesterol * 100.0 / Food.HEALTHY_COLESTEROL),
                fatPercent = Math.round(fat * 100.0 / Food.HEALTHY_FAT),
                protienPercent = Math.round(protien * 100.0 / Food.HEALTHY_PROTIEN),
                carbsPercent = Math.round(carbs * 100.0 / Food.HEALTHY_CARBS),
                sugarPercent = Math.round(sugar * 100.0 / Food.HEALTHY_SUGAR),
                sodiumPercent = Math.round(sodium * 100.0 / Food.HEALTHY_SODIUM);

        caloriesView.setText("Calories\n" + calories + "kcal (" + caloriesPercent + "%)");
        colesterolView.setText("Colesterol\n" + colesterol + "mg (" + colesterolPercent + "%)");
        fatView.setText("Fat\n" + fat + "mg (" + fatPercent + "%)");
        protienView.setText("Protien\n" + protien + "mg (" + protienPercent + "%)");
        carbsView.setText("Carbs\n" + carbs + "mg (" + carbsPercent + "%)");
        sugarView.setText("Sugar\n" + sugar + "mg (" + sugarPercent + "%)");
        sodiumView.setText("Sodium\n" + sodium + "mg (" + sodiumPercent + "%)");

        // Changes color
        if (caloriesPercent >= 100){
            caloriesView.setBackgroundColor(Color.parseColor("#EF5350"));
        }
        if (colesterolPercent >= 100){
            colesterolView.setBackgroundColor(Color.parseColor("#EF5350"));
        }
        if (fatPercent >= 100){
            fatView.setBackgroundColor(Color.parseColor("#EF5350"));
        }
        if (protienPercent >= 100){
            protienView.setBackgroundColor(Color.parseColor("#EF5350"));
        }
        if (carbsPercent >= 100){
            carbsView.setBackgroundColor(Color.parseColor("#EF5350"));
        }
        if (sugarPercent >= 100){
            sugarView.setBackgroundColor(Color.parseColor("#EF5350"));
        }
        if (sodiumPercent >= 100){
            sodiumView.setBackgroundColor(Color.parseColor("#EF5350"));
        }
    }
}