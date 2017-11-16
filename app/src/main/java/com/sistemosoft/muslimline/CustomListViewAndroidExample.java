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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by appaz on 11/17/16.
 */
public class CustomListViewAndroidExample extends AppCompatActivity {

    private Menu menu;
    private static ListView list;
    private static int LISTVIEWPAGE = 1;
    private static boolean flag_loading = false;
    public CustomListViewAndroidExample CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
    private ArrayList<String> selected_products = new ArrayList<String>();
    private static String category_name = "";
    private static String category_id = "";
    Button categoryBtn, cartBtn, contactBtn, accountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        CustomListView = this;
        category_name = getIntent().getStringExtra("name");
        selected_products = getIntent().getStringArrayListExtra("products");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a62252")));
        actionBar.setTitle(category_name);

        list = (ListView) findViewById(R.id.detail_list);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (totalItemCount >= 6){
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        if (flag_loading == false) {
                            flag_loading = true;
                            LISTVIEWPAGE += 1;
                            new GetClientsMore().execute();
                        }
                    }
                }
            }
        });

        try {
            int length = selected_products.size() / 7;
            cartBtn = (Button) findViewById(R.id.order);
            cartBtn.setText("Корзина(" + length + ")");
        } catch (Exception e) {
            Toast.makeText(CustomListViewAndroidExample.this, "Cart calculating error", Toast.LENGTH_SHORT).show();
        }
        categoryBtn = (Button) findViewById(R.id.category);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomListViewAndroidExample.this, ProductsActivity.class);
                intent.putStringArrayListExtra("products", selected_products);
                startActivity(intent);
            }
        });
        cartBtn = (Button) findViewById(R.id.order);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_products.size() == 0) {
                    Toast.makeText(CustomListViewAndroidExample.this, "Cначала выберите товар!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CustomListViewAndroidExample.this, CartActivity.class);
                    intent.putExtra("name", category_name);
                    intent.putStringArrayListExtra("products", selected_products);
                    startActivity(intent);
                }
            }
        });
        contactBtn = (Button) findViewById(R.id.contact);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomListViewAndroidExample.this, ContactActivity.class);
                intent.putStringArrayListExtra("products", selected_products);
                startActivity(intent);
            }
        });
        accountBtn = (Button) findViewById(R.id.account);
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomListViewAndroidExample.this, SaleActivity.class);
                startActivity(intent);
            }
        });

        if (isOnline()) {
            LISTVIEWPAGE = 1;
            if (category_name.equals("Блузки, Кофты")) {
                category_id = "6";
                new GetClients().execute();
            } else if (category_name.equals("Головные Уборы")) {
                category_id = "7";
                new GetClients().execute();
            } else if (category_name.equals("Спортивные Костюмы")) {
                category_id = "8";
                new GetClients().execute();
            } else if (category_name.equals("Платья Для Никаха")) {
                category_id = "9";
                new GetClients().execute();
            } else if (category_name.equals("Аксессуары(Для Женшин)")) {
                category_id = "10";
                new GetClients().execute();
            } else if (category_name.equals("Жилет")) {
                category_id = "14";
                new GetClients().execute();
            } else if (category_name.equals("Юбки")) {
                category_id = "5";
                new GetClients().execute();
            } else if (category_name.equals("Платья")) {
                category_id = "4";
                new GetClients().execute();
            } else if (category_name.equals("Аксессуары(Для Мужчин)")) {
                category_id = "13";
                new GetClients().execute();
            }else if (category_name.equals("Спортивные Костюмы(Для Мужчин)")) {
                category_id = "11";
                new GetClients().execute();
            }  else if (category_name.equals("Брюки")) {
                category_id = "12";
                new GetClients().execute();
            }}else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Ошибка!Пожалуйста, проверьте интернет-соединение.");
            builder.setPositiveButton("Ok ",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setListData();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

//    public void setListData() {
//        if (isOnline()) {
//        if (category_name.equals("Блузки, Кофты")) {
//            category_id = "6";
//            new GetClients().execute();
//        } else if (category_name.equals("Головные Уборы")) {
//            category_id = "7";
//            new GetClients().execute();
//        } else if (category_name.equals("Спортивные Костюмы(Для Женшин)")) {
//            category_id = "8";
//            new GetClients().execute();
//        } else if (category_name.equals("Платья Для Никаха")) {
//            category_id = "9";
//            new GetClients().execute();
//        } else if (category_name.equals("Аксессуары(Для Женшин)")) {
//            category_id = "10";
//            new GetClients().execute();
//        } else if (category_name.equals("Аксессуары(Для Мужчин)")) {
//            category_id = "13";
//            new GetClients().execute();
//        }else if (category_name.equals("Спортивные Костюмы(Для Мужчин)")) {
//            category_id = "11";
//            new GetClients().execute();
//        }  else if (category_name.equals("Брюки")) {
//            category_id = "12";
//            new GetClients().execute();
//        }  else if (category_name.equals("Жилет")) {
//            category_id = "14";
//            new GetClients().execute();
//        } else if (category_name.equals("Юбки")) {
//            category_id = "5";
//            new GetClients().execute();
//        }else if (category_name.equals("Платья")) {
//            category_id = "4";
//            new GetClients().execute();
//        }}else{
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Ошибка!Пожалуйста, проверьте интернет-соединение.");
//            builder.setPositiveButton("Ok ",new DialogInterface.OnClickListener(){
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finishAffinity();
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//    }

    public void onItemClick(int mPosition) {
        ListModel tempValues = (ListModel) CustomListViewValuesArr.get(mPosition);
        Intent intent = new Intent(CustomListViewAndroidExample.this, ContentActivity.class);
        intent.putExtra("id", tempValues.getProductId());
        intent.putExtra("name", tempValues.getProductName());
        intent.putExtra("price", tempValues.getProductPrice());
        intent.putExtra("totalPrice", tempValues.getTotalPrice());
        intent.putExtra("size", tempValues.getProductSize());
        intent.putExtra("color", tempValues.getProductColor());
        intent.putExtra("description", tempValues.getProductContent());
        intent.putExtra("image_url", tempValues.getImage());
        intent.putExtra("category_name", category_name);
        intent.putStringArrayListExtra("products", selected_products);
        startActivity(intent);
    }

    public void onAddcartClick(int mPosition, ArrayList<ListModel> data) {
        CustomListViewValuesArr = data;
        CartCustomAdapter.ViewHolder holder = new CartCustomAdapter.ViewHolder();
        ListModel tempValues = (ListModel) CustomListViewValuesArr.get(mPosition);
        int isfind = 0;
        for (int i = 0; i < selected_products.size(); i = i + 7) {
            if (selected_products.get(i + 5).equals(tempValues.getProductId())) {
                isfind = 1;
                selected_products.set(i, tempValues.getProductName());
                selected_products.set(i + 1, tempValues.getProductPrice());
                selected_products.set(i + 2, tempValues.getCount());
                selected_products.set(i + 3, tempValues.getProductsSize());
                selected_products.set(i + 4, tempValues.getProductsColor());
                selected_products.set(i + 5, tempValues.getProductId());
                selected_products.set(i + 6, "");
                break;
            }
        }
        if (isfind == 0) {
            selected_products.add(tempValues.getProductName());
            selected_products.add(tempValues.getProductPrice());
            selected_products.add(tempValues.getCount());
            selected_products.add(tempValues.getProductsSize());
            selected_products.add(tempValues.getProductsColor());
            selected_products.add(tempValues.getProductId());
            selected_products.add("");

        }

        try {
            int length = selected_products.size() / 7;
            Button order = (Button) findViewById(R.id.order);
            order.setText("Корзина(" + length + ")");
        } catch (Exception e) {
            Toast.makeText(CustomListViewAndroidExample.this, "Cart calculating error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CustomListViewAndroidExample.this, ProductsActivity.class);
        intent.putStringArrayListExtra("products", selected_products);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;

        SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
        String token = sharedPreferences.getString("token", "");
        if (!token.equals("")){
            MenuItem menuItem = menu.findItem(R.id.login);
            menuItem.setTitle("Выйти");
        }else{
            MenuItem menuItem = menu.findItem(R.id.login);
            menuItem.setTitle("Войти");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                Intent intent = new Intent(CustomListViewAndroidExample.this, ProductsActivity.class);
                intent.putStringArrayListExtra("products", selected_products);
                startActivity(intent);
                return true;
            case R.id.login:
                // API 5+ solution
                SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
                String token = sharedPreferences.getString("token", "");
                if (!token.equals("")){
                    MenuItem menuItem = menu.findItem(R.id.login);
                    menuItem.setTitle("Войти");
                    SharedPreferences sharedPreferences1 = getSharedPreferences("auth_token", 0);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString("token", "");
                    editor.putBoolean("wholesaler", false);
                    editor.putString("name", "");
                    editor.putString("telephone", "");
                    editor.putString("address", "");
                    editor.commit();
                    selected_products = new ArrayList<String>();
                    Intent intent2 = new Intent(CustomListViewAndroidExample.this, ProductsActivity.class);
                    intent2.putStringArrayListExtra("products", selected_products);
                    startActivity(intent2);
                }else {
                    Intent intent1 = new Intent(CustomListViewAndroidExample.this, SignInActivity.class);
                    startActivity(intent1);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetClients extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;
        private ProgressDialog progressDialog = new ProgressDialog(CustomListViewAndroidExample.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Загрузка товаров...");
            //progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://www.muslimlineru.ru/api/mobile/products/index?id=" + category_id + "&page=" + LISTVIEWPAGE);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            CustomListViewValuesArr = new ArrayList<ListModel>();

            try {
                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String name = jObject.getString("name");
                    String color = jObject.getString("tints");
                    color = (color == null) ? "" : color;
                    String sizes = jObject.getString("size");
                    String price = jObject.getString("price");
                    String wholesaler_price = jObject.getString("wholesaler_price");
                    String image = jObject.getString("image_url");
                    String description = jObject.getString("description");
                    String discount = jObject.getString("discount");
                    discount = (discount.equals("null")) ? null : discount;
                    String id = jObject.getString("id");
                    String totalprice = price;

                    SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
                    Boolean wholesaler = sharedPreferences.getBoolean("wholesaler", false);
                    if (wholesaler){
                        discount = null;
                    }

                    if (discount != null) {
                        int totalPrice;
                        if (discount != null) {
                            totalPrice = Integer.parseInt(price);
                        } else {
                            totalPrice = 0;
                        }
                        int discount1 = Integer.parseInt(discount);
                        int salePrice = totalPrice - discount1 * totalPrice / 100;
                        price = Integer.toString(salePrice);
                    }else
                    {
                        price = wholesaler_price;
                        totalprice = wholesaler_price;
                    }

                    ListModel listModel = new ListModel();
                    listModel.setProductId(id);
                    listModel.setProductName(name);
                    listModel.setProductContent(description);
                    listModel.setProductSize(sizes);
                    listModel.setProductColor(color);
                    listModel.setProductPrice(price);
                    listModel.setTotalPrice(totalprice);
                    listModel.setCount("1");
                    listModel.setImage(image);
                    CustomListViewValuesArr.add(listModel);

                } // End Loop
                this.progressDialog.dismiss();

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)

            list.setAdapter(new CustomAdapter(CustomListViewAndroidExample.this, CustomListViewValuesArr));

        }

    }

    private class GetClientsMore extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;
        private ProgressDialog progressDialog = new ProgressDialog(CustomListViewAndroidExample.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Загрузка товаров...");
            //progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://www.muslimlineru.ru/api/mobile/products/index?id=" + category_id + "&page=" + LISTVIEWPAGE);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String name = jObject.getString("name");
                    String color = jObject.getString("tint");
                    color = (color == null) ? "" : color;
                    String size = jObject.getString("size");
                    String price = jObject.getString("price");
                    String description = jObject.getString("description");
                    String wholesaler_price = jObject.getString("wholesaler_price");
                    String image = jObject.getString("image_url");
                    String discount = jObject.getString("discount");
                    String id = jObject.getString("id");
                    String totalprice = price;

                    SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
                    Boolean wholesaler = sharedPreferences.getBoolean("wholesaler", true);
                    if (wholesaler){
                        discount = null;
                    }

                    if (discount != null) {
                        int totalPrice;
                        if (price != null) {
                            totalPrice = Integer.parseInt(price);
                        } else {
                            totalPrice = 0;
                        }
                        int discount1 = Integer.parseInt(discount);
                        int salePrice = totalPrice - discount1 * totalPrice / 100;
                        price = Integer.toString(salePrice);
                    }else
                    {
                        price = wholesaler_price;
                        totalprice = wholesaler_price;
                    }

                    ListModel listModel = new ListModel();
                    listModel.setProductId(id);
                    listModel.setProductName(name);
                    listModel.setProductContent(description);
                    listModel.setProductSize(size);
                    listModel.setProductColor(color);
                    listModel.setProductPrice(price);
                    listModel.setTotalPrice(totalprice);
                    listModel.setCount("1");
                    listModel.setImage(image);
                    CustomListViewValuesArr.add(listModel);

                } // End Loop

                this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)

            list.deferNotifyDataSetChanged();
            flag_loading = false;
        }

    }

}
