package io.alstonlin.hackprinceton;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * The Singleton Data Access Object where all Networking will be handled.
 */
public class DAO {

    public static final String CLARIFAI_APP_ID = "fbVSSR7x8TQ8in4DUKaFeLYA7J1WqkLzneTZzRAp";
    public static final String CLARIFAI_APP_SECRET = "4cqsv2jVsMktcmsptdfoVnl8Hq-XpIp_16XCBBQV";
    // Food to recognize
    public static final List<String> foods = Arrays.asList("apple", "coffee", "chips", "banana", "cookie", "pretzel",
            "egg", "donut", "bagel", "pizza", "granola", "chocolate", "water", "lettuce", "tomato", "cheese", "cake",
            "olive oil");
    public static DAO instance;

    /**
     * Gets the Singleton DAO Object (lazy instantiation).
     * @return The Singleton
     */
    public static DAO getInstance(){
        if (instance == null) instance = new DAO();
        return instance;
    }

    private DAO(){};

    public void uploadImage(Bitmap image) {
        // Bitmap to bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        // Uploads to clarifai
        new ClarifaiTask().execute(bytes);
    }

    private class ClarifaiTask extends AsyncTask<byte[], Void, String[]> {
        @Override
        protected String[] doInBackground(byte[]... bytes) {
            ClarifaiClient clarifai = new ClarifaiClient(CLARIFAI_APP_ID, CLARIFAI_APP_SECRET);
            List<RecognitionResult> results = clarifai.recognize(new RecognitionRequest(bytes[0]));
            String match = matchFood(results);
            if (match == null) return null;
            int[] vals = parseValues(queryFood(match));
        }

        @Override
        protected void onPostExecute(String[] result) {
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
        final int NUM_ATTRIBUTES = 7;
        String[] values = new String[NUM_ATTRIBUTES];
        String[] keywords = {"total calories ", "cholesterol ", "total fat ", "protein ", "total carbohydrates ", "sugar ", "sodium "};
        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
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

    private int[] parseValues(String[] values){
        final String gram = "g";
        final String milligram = "mg";
        final String microgram = "Âµg";

    }
}