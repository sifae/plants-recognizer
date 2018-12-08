package com.example.plantsrecognizer.Utils;

import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import com.example.plantsrecognizer.Models.JsonModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonParseContent {

    private final AppCompatActivity activity;

    private ArrayList<HashMap<String, String>> arraylist;

    public JsonParseContent(AppCompatActivity activity) {
        this.activity = activity;
    }

    private String charArrayToString(char string[], int max_index) {
        StringBuilder new_string = new StringBuilder();
        for (int i = 0; i < max_index; i++) {
            new_string.append(string[i]);
        }
        return new_string.toString();
    }

    private String parse_extracts(char string[]) {
        StringBuilder new_string = new StringBuilder();
        int max_limit = 159;
        int tmp = max_limit;
        int counter = 0;

        for(int i = 0; i < string.length; i++){
            if(string[i] == '\n'){
                break;
            }
            else if(string[i] == '—' || string[i] == '-'){
                new_string = new StringBuilder();
                i++;
                if(string[i] == ' '){
                    i++;
                }
            }
            new_string.append(string[i]);
        }
        if (new_string.length() > max_limit) {
            string = new_string.toString().toCharArray();
            for (int i = max_limit; i >= 0; i--) {
                if (string[i] == '.') {
                    tmp = i;
                    break;
                }
            }
            if (tmp == max_limit) {
                for (int i = tmp; i >= 0; i--)
                    if (string[i] == ' ' || string[i] == ',') {
                        counter++;
                        if (counter == 2) {
                            tmp = i;
                            break;
                        }
                    }
                string[tmp] = '.';
                tmp++;
                string[tmp] = '.';
                tmp++;
                string[tmp] = '.';
                tmp++;
            }
            new_string = new StringBuilder(charArrayToString(string, tmp));
        }
        try {
            string = new_string.toString().toCharArray();
            string[0] = Character.toUpperCase(string[0]);
            new_string = new StringBuilder(charArrayToString(string, string.length));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new_string.toString();
    }

    public String getTitle(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject all_dataobj = jsonObject.getJSONObject("query").getJSONArray("pages").getJSONObject(0);
            return all_dataobj.getString(JsonConstants.Params.TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonModel getInfo(String response) {
        JsonModel jsonModel = new JsonModel();
        int limit = 20;
        try {
            JSONObject jsonObject = new JSONObject(response);
            arraylist = new ArrayList<>();

            JSONObject all_dataobj = jsonObject.getJSONObject("query").getJSONArray("pages").getJSONObject(0);

            try{
                JSONObject description_dataobj = all_dataobj.getJSONObject("terms");
                String description_row = description_dataobj.getString(JsonConstants.Params.DESCRIPTION);
                char[] description = description_row.substring(2, description_row.length() - 2).toCharArray();

                if (description.length <= limit) {      //Если очень маленькое описание.
                    throw new JSONException("Small Description");
                } else {
                    description[0] = Character.toUpperCase(description[0]);
                    jsonModel.setDescription(charArrayToString(description, description.length));
                }
            }catch (JSONException e){
                String extracts = all_dataobj.getString(JsonConstants.Params.EXTRACTS);
                extracts = Html.fromHtml(extracts).toString();
                String description = parse_extracts(extracts.toCharArray());
                //Log.v("WIKI_DATA_EXTRACTS",description);
                jsonModel.setDescription(description);
            }

            jsonModel.setTitle(all_dataobj.getString(JsonConstants.Params.TITLE));

            String source = all_dataobj.getJSONObject("thumbnail").getString(JsonConstants.Params.SOURCE);
            jsonModel.setSource(source);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonModel;
    }
}