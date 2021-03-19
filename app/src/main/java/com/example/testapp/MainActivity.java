package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private TextView text1,textView2;
    private EditText EditText1;
    private int[] min = new int[34];
    private double[] obmen = new double[34];
    private double summ, naminaldouble = 0;
    private int i = 0;
    private String[] Id = new String[34];
    private String naminalString;
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //String quere = "https://www.cbr-xml-daily.ru/daily_json.js";
        //HttpURLConnection connection = null;

        /*try{
            connection = (HttpURLConnection) new URL(quere).openConnection();

            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);

            connection.connect();

            StringBuilder sb = new StringBuilder();

            if(HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;

                while ((line = in.readLine()) != null){
                    sb.append(line);
                    sb.append("\n");
                }
                System.out.println(sb.toString());
            } else {
                System.out.println("Fail" + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable cause){
            cause.printStackTrace();
        } finally{
            if (connection != null){
                connection.disconnect();
            }
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = findViewById(R.id.Text1);
        textView2 = findViewById(R.id.textView2);
        System.out.println("*********************************");
        text1.setText("SEKV");
        button1 = findViewById(R.id.button2);
        RequestQueue Qrecuest = Volley.newRequestQueue(this);
        String url = "https://www.cbr-xml-daily.ru/daily_json.js";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                text1.setText("");
                try {
                    JSONObject ValuteObj = response.getJSONObject("Valute");
                    Iterator keys = ValuteObj.keys();

                    while (keys.hasNext())
                    {
                        String currentKey = keys.next().toString();
                        JSONObject val = ValuteObj.getJSONObject(currentKey);

                        min[i] = val.getInt("Nominal");
                        obmen[i] = val.getDouble("Value");
                        Id[i] = val.getString("CharCode");
                        i++;
                        text1.append(val.getString("Name") + "\n" + val.getString("Nominal") + " " + val.getString("CharCode") + " / " + val.getString("Value") + " Руб" + "\n" + "\n");
                    }
                } catch (JSONException e) {
                    text1.setText("ERR");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Qrecuest.add(request);

        ArrayAdapter<String> IdAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Id);
        IdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(IdAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(obmen[(int)id]), Toast.LENGTH_SHORT);
               //toast.show();
               //obmen[(int)id];
                try {

                EditText1 = findViewById(R.id.EditText1);
                naminalString = EditText1.getText().toString();
                naminaldouble = Double.parseDouble(naminalString);
                summ = naminaldouble / (obmen[(int)id] / min[(int)id]) ;
                naminalString = String.valueOf(summ);

                textView2.setText(naminalString);
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                Qrecuest.add(request);
            }
        }));
    }
}