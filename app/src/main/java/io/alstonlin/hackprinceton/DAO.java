package io.alstonlin.hackprinceton;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Singleton Data Access Object where all Networking will be handled.
 */
public class DAO {
    public static final String SERVER_URL = "http://ec2-52-23-228-170.compute-1.amazonaws.com:3000";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String SIGN_UP_ENDPOINT = "/newuser";
    public static final String NEW_FOOD_ENDPOINT = "/newFood";
    public static final String CLARIFAI_APP_ID = "fbVSSR7x8TQ8in4DUKaFeLYA7J1WqkLzneTZzRAp";
    public static final String CLARIFAI_APP_SECRET = "4cqsv2jVsMktcmsptdfoVnl8Hq-XpIp_16XCBBQV";
    // Food to recognize
    public static final List<String> foods = Arrays.asList("apple", "coffee", "chips", "banana", "cookie", "pretzel",
            "egg", "donut", "bagel", "pizza", "granola", "chocolate", "water", "lettuce", "tomato", "cheese", "cake",
            "olive oil");
    public static DAO instance;

    private String id = null;

    /**
     * Gets the Singleton DAO Object (lazy instantiation).
     * @return The Singleton
     */
    public static DAO getInstance(){
        if (instance == null) instance = new DAO();
        return instance;
    }

    private DAO(){};

    public void addFood(Bitmap image, MainActivity activity) {
        // Bitmap to bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        // Uploads to clarifai
        new AddFoodTask(activity).execute(bytes);
    }

    public void signup(String username, String password){
        new SignupTask().execute(username, password);
    }

    public void login(String username, String password, Activity activity){
        new LoginTask(activity).execute(username, password);
    }

    // Asynchronous stuff

    private class LoginTask extends AsyncTask<String, Void, String> {
        private Activity activity;

        public LoginTask(Activity activity){
            super();
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(SERVER_URL + LOGIN_ENDPOINT);
            List<NameValuePair> data = new ArrayList<>(2);
            data.add(new BasicNameValuePair("username", username));
            data.add(new BasicNameValuePair("password", password));
            try{
                post.setEntity(new UrlEncodedFormEntity(data));
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                JSONObject result = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                if (result.getBoolean("success")){
                    return result.getString("id");
                } else{
                    return null;
                }
            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null){
                Toast.makeText(activity, "Invalid login", Toast.LENGTH_SHORT).show();
            } else{
                id = s;
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        }
    }

    private class SignupTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(SERVER_URL + SIGN_UP_ENDPOINT);
            List<NameValuePair> data = new ArrayList<>(2);
            data.add(new BasicNameValuePair("username", username));
            data.add(new BasicNameValuePair("password", password));
            try{
                post.setEntity(new UrlEncodedFormEntity(data));
                client.execute(post);
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AddFoodTask extends AsyncTask<byte[], Void, Boolean> {
        private MainActivity activity;
        private Food food;

        public AddFoodTask(MainActivity activity){
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(byte[]... bytes) {
            // Clarifai API
            ClarifaiClient clarifai = new ClarifaiClient(CLARIFAI_APP_ID, CLARIFAI_APP_SECRET);
            List<RecognitionResult> results = clarifai.recognize(new RecognitionRequest(bytes[0]));
            // Wolfram Alpha API
            String match = matchFood(results);
            if (match == null) return false;
            food = parseValues(queryFood(match));
            // Server
            return addFood(food);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
                activity.onFoodAdded(food);
            } else {
                Toast.makeText(activity, "Could not detect the food", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String matchFood(List<RecognitionResult> results){
        String match = null;
        for (Tag tag : results.get(0).getTags()) {
            String name = tag.getName().toLowerCase();
            for (String food : foods){
                if (food.equals(name)){
                    match = food;
                }
            }
        }
        return match;
    }

    private String[] queryFood(String food) {
        // Creates a query
        WAQueryResult queryResult = null;
        WAEngine engine = new WAEngine();
        engine.setAppID("AJ5QRY-Y8GWWL4HGH");
        engine.addFormat("plaintext");
        WAQuery query = engine.createQuery();
        query.setInput(food + " food");

        try {
            queryResult = engine.performQuery(query);
            queryResult.getXML();
        } catch (WAException e) {
            e.printStackTrace();
        }

        String xmlString = queryResult.getXML().toString();
        char[] xml = queryResult.getXML().toString().toCharArray();

        // Parses result
        final int NUM_ATTRIBUTES = 8;
        String[] values = new String[NUM_ATTRIBUTES];
        String[] keywords = {"total calories ", "cholesterol ", "total fat ", "protein ", "total carbohydrates ", "sugar ", "sodium "};
        values[0] = food;
        for (int i = 1; i < keywords.length + 1; i++) {
            String keyword = keywords[i - 1];
            int index = xmlString.indexOf(keyword);
            values[i] = "";
            int spaceCount = 0;
            int j = index + keyword.length() + 1;
            while (j < xmlString.length() && spaceCount < 2) {
                if (xml[j] == ' '){
                    spaceCount++;
                }
                if (spaceCount < 2) {
                    values[i] += xml[j];
                }
                j++;
            }
        }
        return values;
    }

    /**
     * Makes every weight unit into mg.
     * @param values The strings with units
     * @return Integer values of all attributes in mg, if applicable
     */
    private Food parseValues(String[] values){
        final String gram = "g";
        final String microgram = "Âµg";
        int[] intVals = new int[values.length - 1];
        for (int i = 1; i < values.length; i++){
            String[] split = values[i].split(" ");
            int magnitude = Integer.parseInt(split[0]);
            String unit = split[1];
            if (unit.equals(gram)) intVals[i - 1] = magnitude * 1000;
            else if (unit.equals(microgram)) intVals[i - 1] = magnitude / 1000;
            else intVals[i - 1] = magnitude;
        }
        return new Food(values[0], null, intVals[0], intVals[1], intVals[2], intVals[3], intVals[4], intVals[5], intVals[6]);
    }

    private boolean addFood(Food food){
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(SERVER_URL + NEW_FOOD_ENDPOINT);
        List<NameValuePair> data = new ArrayList<>(8);
        data.add(new BasicNameValuePair("name", food.getName()));
        data.add(new BasicNameValuePair("calories", Integer.toString(food.getCalories())));
        data.add(new BasicNameValuePair("colesterol", Integer.toString(food.getCalories())));
        data.add(new BasicNameValuePair("fat", Integer.toString(food.getCalories())));
        data.add(new BasicNameValuePair("protien", Integer.toString(food.getCalories())));
        data.add(new BasicNameValuePair("carbs", Integer.toString(food.getCalories())));
        data.add(new BasicNameValuePair("sugar", Integer.toString(food.getCalories())));
        data.add(new BasicNameValuePair("sodium", Integer.toString(food.getCalories())));
        try{
            post.setEntity(new UrlEncodedFormEntity(data));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            JSONObject result = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
            return result.getBoolean("success");
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}