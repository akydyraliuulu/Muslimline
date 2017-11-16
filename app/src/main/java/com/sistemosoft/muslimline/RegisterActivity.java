package com.sistemosoft.muslimline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
public class RegisterActivity extends AppCompatActivity {

    private static String surname = "";
    private static String email = "";
    private static String telephone = "";
    private static String region = "";
    private static String password = "";
    private static String password2 = "";
    private static Boolean optovik = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a62252")));
        actionBar.setTitle("Регистрация");

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_surname = (EditText) findViewById(R.id.et_surname);
                EditText et_name = (EditText) findViewById(R.id.et_email);
                EditText et_telephone = (EditText) findViewById(R.id.et_telephone);
                EditText et_region = (EditText) findViewById(R.id.et_address);
                EditText et_password = (EditText) findViewById(R.id.et_password);
                EditText et_password2 = (EditText) findViewById(R.id.et_password2);
                CheckBox et_optovik = (CheckBox) findViewById(R.id.aggrement);

                surname = et_surname.getText().toString();
                email = et_name.getText().toString();
                telephone = et_telephone.getText().toString();
                region = et_region.getText().toString();
                password = et_password.getText().toString();
                password2 = et_password2.getText().toString();
                optovik = et_optovik.isChecked();

                if (!surname.equals("") && !email.equals("") && !telephone.equals("") && !region.equals("") && !password.equals("") && !password2.equals("")) {
                    if (password.equals(password2)) {

                        SharedPreferences sp = getSharedPreferences("auth_token", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("surname", surname);
                        editor.putString("user_email", email);
                        editor.putString("telephone", telephone);
                        editor.putString("region", region);
                        editor.putString("password", password);
                        editor.putBoolean("wholesaler", optovik);
                        editor.commit();

                        if (isOnline()) {
                            AsyncT asyncT = new AsyncT();
                            asyncT.execute();
                        }else{
                            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Ошибка!Пожалуйста, проверьте интернет-соединение.");
                            builder.setPositiveButton("Ok ",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Пароль не совпадает!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
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

    class AsyncT extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Регистрация...");
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
                JSONObject json= (JSONObject) new JSONTokener(result).nextValue();
                Boolean success = json.getBoolean("success");
                if (success){
                    JSONObject json2 = json.getJSONObject("data");
                    String token = (String) json2.get("auth_token");

                    Log.d("Token", token);
                    SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.commit();

                    ArrayList<String> arrayList = new ArrayList<String>();
                    Intent intent = new Intent(RegisterActivity.this, ProductsActivity.class);
                    intent.putStringArrayListExtra("products", arrayList);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else{
                    this.progressDialog.dismiss();
                    JSONObject object = new JSONObject("data");
                    JSONArray errors = object.getJSONArray("errors");
                    Toast.makeText(getApplicationContext(),"Ошибка: " + errors.toString(),Toast.LENGTH_LONG).show();
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
                URL url = new URL(ApiUrl.REGISTRATION); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.setRequestProperty("Host", "android.schoolportal.gr");
                //httpURLConnection.connect();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", email);
                jsonObject.put("password", password);
                jsonObject.put("password_confirmation", password2);
                jsonObject.put("name", surname);
                jsonObject.put("telephone", telephone);
                jsonObject.put("address", region);
                jsonObject.put("wholesaler", optovik);

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("user", jsonObject);

                String str = jsonObject1.toString();
                //str = str.replaceFirst(":", "=>");

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


                /*DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(str);
                wr.flush();
                wr.close();*/

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
