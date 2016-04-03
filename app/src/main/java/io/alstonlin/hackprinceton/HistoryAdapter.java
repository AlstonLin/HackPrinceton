package io.alstonlin.hackprinceton;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter{
    private MainActivity activity;
    private HistoryFragment fragment;

    public HistoryAdapter(MainActivity activity, HistoryFragment fragment){
        this.activity = activity;
        this.fragment = fragment;
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
        final Food food = activity.getFoods().get(position);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView calories = (TextView) convertView.findViewById(R.id.calories);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        name.setText(capitalize(food.getName()));
        calories.setText(food.getCalories() + " calories");
        time.setText(DateUtils.getRelativeTimeSpanString(food.getCreatedAt().getTime()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.food_dialog);
                dialog.show();
                new DownloadImageTask((ImageView) dialog.findViewById(R.id.image)).execute(food.getImageUrl());

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

            }
        });
        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(result, result.getWidth() / 2, result.getHeight() / 2, false);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            bmImage.setImageBitmap(rotatedBitmap);
        }
    }

    public void setDataSet(ArrayList<Food> foods){
        this.activity.setFoods(foods);
        notifyDataSetChanged();
        fragment.setupStats();
        VisualizeFragment.instance.notifyDataChanged();
    }

    private String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
