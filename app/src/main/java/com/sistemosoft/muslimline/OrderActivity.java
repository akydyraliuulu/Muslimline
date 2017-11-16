package com.sistemosoft.muslimline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by appaz on 11/17/16.
 */
public class OrderActivity extends AppCompatActivity {

    private static String product_price = "";
    private static String category_name = "";
    private ArrayList<String> selected_products = new ArrayList<String>();

    private static String etd_surname = "";
    private static String etd_address = "";
    private static String etd_telephone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_order);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a62252")));
        actionBar.setTitle("Оформление Заказа");

        product_price = getIntent().getStringExtra("price");
        selected_products = getIntent().getStringArrayListExtra("products");

        SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
        String name = sharedPreferences.getString("name", "");
        String telephone = sharedPreferences.getString("telephone", "");
        String address = sharedPreferences.getString("address", "");

        final EditText et_name = (EditText) findViewById(R.id.et_name);
        final EditText et_telephone = (EditText) findViewById(R.id.et_telephone);
        final EditText et_region = (EditText) findViewById(R.id.et_address);
        final TextView et_price = (TextView) findViewById(R.id.et_price);

        int price = Integer.parseInt(product_price);
        et_name.setText(name);
        et_telephone.setText(telephone);
        et_region.setText(address);
        et_price.setText(Integer.toString(price));

        Button send = (Button) findViewById(R.id.send);
        send.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        // Your action here on button click
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        EditText et_surname = (EditText) findViewById(R.id.et_name);
                        EditText et_telephone = (EditText) findViewById(R.id.et_telephone);
                        EditText et_address = (EditText) findViewById(R.id.et_address);
                        etd_surname = et_surname.getText().toString();
                        etd_telephone = et_telephone.getText().toString();
                        etd_address = et_address.getText().toString();

                        if (isOnline()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
                            String token = sharedPreferences.getString("token", "");
                            if (!token.equals("")) {
                                AsyncT asyncT = new AsyncT();
                                asyncT.execute();
                            } else {
                                Async async = new Async();
                                async.execute();
                            }
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                            builder.setMessage("Ошибка!Пожалуйста, проверьте интернет-соединение.");
                            builder.setPositiveButton("Ok ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            //Toast.makeText(getApplicationContext(),"Check the network connection",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    }
                }
                return true;
            }
        });
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        // Your action here on button click
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        ArrayList<String> arrayList = new ArrayList<String>();
                        Intent intent = new Intent(OrderActivity.this, ProductsActivity.class);
                        intent.putStringArrayListExtra("products", arrayList);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    class AsyncT extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Оформление заказов...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    AsyncT.this.cancel(true);
                }
            });
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject json = (JSONObject) new JSONTokener(result).nextValue();
                Boolean success = json.getBoolean("success");
                if (success) {
                    JSONObject json2 = json.getJSONObject("data");
                    String message = (String) json2.get("message");
                    Log.d("Message", message);

                    Intent intent = new Intent(OrderActivity.this, SendActivity.class);
                    intent.putExtra("name", category_name);
                    startActivity(intent);
                } else {
                    JSONObject object = new JSONObject("data");
                    String errors = object.getString("message");
                    Toast.makeText(getApplicationContext(), "Ошибка: " + errors, Toast.LENGTH_LONG).show();
                }
                // End Loop
                this.progressDialog.dismiss();

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
                String token = sharedPreferences.getString("token", "");
                URL url = new URL(ApiUrl.CHECKOUT); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.setRequestProperty("Authorization", "Token token=" + token);
                // httpURLConnection.connect();

                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < selected_products.size(); i = i + 7) {
                    int total = Integer.parseInt(selected_products.get(i + 2)) * Integer.parseInt(selected_products.get(i + 1));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("product", selected_products.get(i + 4));
                    jsonObject.put("size", selected_products.get(i + 3));
                    jsonObject.put("color", selected_products.get(i + 5));
                    jsonObject.put("quantity", selected_products.get(i + 2));
                    jsonObject.put("price", selected_products.get(i + 1));
                    jsonObject.put("total", Integer.toString(total));
                    jsonObject.put("name", etd_surname);
                    jsonObject.put("telephone", etd_telephone);
                    jsonObject.put("address", etd_address);
                    jsonObject.put("review", selected_products.get(i + 6));
                    jsonArray.put(jsonObject);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", jsonArray);

                String str = jsonObject.toString();
                //str = str.replaceFirst(":","=>");

                Log.d("Json ", str);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(str);
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("Result ", result.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("Kata", "Kata boldu 1");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Kata", "Kata boldu 2");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Kata", "Kata boldu 3");
            }

            return result.toString();
        }
    }

    class Async extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Оформление заказов...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    Async.this.cancel(false);
                }
            });
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject json = (JSONObject) new JSONTokener(result).nextValue();
                Boolean success = json.getBoolean("success");
                if (success) {
                    JSONObject json2 = json.getJSONObject("data");
                    String message = (String) json2.get("message");
                    Log.d("Message", message);

                    Intent intent = new Intent(OrderActivity.this, SendActivity.class);
                    intent.putExtra("name", category_name);
                    startActivity(intent);
                } else {
                    JSONObject object = new JSONObject("data");
                    String errors = object.getString("message");
                    Toast.makeText(getApplicationContext(), "Ошибка: " + errors, Toast.LENGTH_LONG).show();
                }
                // End Loop
                this.progressDialog.dismiss();

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(ApiUrl.CHECKOUTV1); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                // httpURLConnection.connect();
                // httpURLConnection.setRequestProperty("Authorization", "Token token=" + token);


                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < selected_products.size(); i = i + 7) {
                    int total = Integer.parseInt(selected_products.get(i + 2)) * Integer.parseInt(selected_products.get(i + 1));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("product", selected_products.get(i + 4));
                    jsonObject.put("size", selected_products.get(i + 3));
                    jsonObject.put("color", selected_products.get(i + 5));
                    jsonObject.put("quantity", selected_products.get(i + 2));
                    jsonObject.put("price", selected_products.get(i + 1));
                    jsonObject.put("total", Integer.toString(total));
                    jsonObject.put("name", etd_surname);
                    jsonObject.put("telephone", etd_telephone);
                    jsonObject.put("address", etd_address);
                    jsonObject.put("review", selected_products.get(i + 6));
                    jsonArray.put(jsonObject);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", jsonArray);

                String str = jsonObject.toString();
                //str = str.replaceFirst(":","=>");

                Log.d("Json ", str);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(str);
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();

                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("Result ", result.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("Kata", "Kata boldu 1");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Kata", "Kata boldu 2");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Kata", "Kata boldu 3");
            }

            return result.toString();
        }
    }

}