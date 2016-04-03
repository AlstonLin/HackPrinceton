package io.alstonlin.hackprinceton;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter{
    private MainActivity activity;

    public HistoryAdapter(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return activity.getFoods().size();
    }

    @Override
    public Object getItem(int position) {
        return activity.getFoods().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        Food food = activity.getFoods().get(position);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView calories = (TextView) convertView.findViewById(R.id.calories);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        name.setText(food.getName());
        calories.setText(food.getCalories() + " calories");
        time.setText(DateUtils.getRelativeTimeSpanString(food.getCreatedAt().getTime()));
        return convertView;
    }

    public void setDataSet(ArrayList<Food> foods){
        this.activity.setFoods(foods);
        notifyDataSetChanged();
    }
}
