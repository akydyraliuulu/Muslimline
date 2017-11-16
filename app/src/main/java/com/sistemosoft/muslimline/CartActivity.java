package com.sistemosoft.muslimline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by appaz on 11/17/16.
 **/

public class CartActivity extends AppCompatActivity {
    private Menu menu;
    private static String category_name = "";
    private ArrayList<String> selected_products = new ArrayList<String>();
    public ArrayList<ListModel2> CartListViewArr = new ArrayList<ListModel2>();
    public CartActivity cartActivity = null;
    CartCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_cart);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a62252")));
        actionBar.setTitle("Корзина");

        //////////////////////////////////////////////////////////////////////
        try {
            category_name = getIntent().getStringExtra("name");
            selected_products = getIntent().getExtras().getStringArrayList("products");

            final ListView lv = (ListView) findViewById(R.id.detail_list);
            int total2 = 0;
            for (int i = 1; i < selected_products.size(); i = i + 7) {
                total2 = total2 + Integer.parseInt(selected_products.get(i)) * Integer.parseInt(selected_products.get(i + 1));
            }
            final TextView tv_total2 = (TextView) findViewById(R.id.total2);
            tv_total2.setText(Integer.toString(total2));

            Button send_order = (Button) findViewById(R.id.order);
            send_order.setOnTouchListener(new View.OnTouchListener() {

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

                            ListModel2 tempValues;
                            selected_products = new ArrayList<String>();
                            for (int i = 0; i < CartListViewArr.size(); i++) {
                                tempValues = CartListViewArr.get(i);
                                int productPrice = Integer.parseInt(tempValues.getProductPrice()) / Integer.parseInt(tempValues.getProductCount());
                                selected_products.add(tempValues.getProductName());
                                selected_products.add(Integer.toString(productPrice));
                                selected_products.add(tempValues.getProductCount());
                                selected_products.add(tempValues.getProductSize());
                                selected_products.add(tempValues.getProductId());
                                selected_products.add(tempValues.getProductsColor());
                                selected_products.add(tempValues.getComments());
                            }

                            final TextView tv_total2 = (TextView) findViewById(R.id.total2);
                            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                            intent.putExtra("price", tv_total2.getText());
                            intent.putStringArrayListExtra("products", selected_products);
                            startActivity(intent);
                            break;
                        }
                    }
                    return true;
                }
            });
            //////////////////////////////////
            cartActivity = this;
            Resources res = getResources();
            setListData();
            adapter = new CartCustomAdapter(cartActivity, CartListViewArr, res);
            lv.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(CartActivity.this, "Error! in the loading data to detail list.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setListData() {
        CartListViewArr = new ArrayList<ListModel2>();

        for (int i = 0; i < selected_products.size(); i = i + 7) {

            CartCustomAdapter.ViewHolder holder = new CartCustomAdapter.ViewHolder();
            final ListModel2 sched = new ListModel2();

            String name = selected_products.get(i);
            String price = selected_products.get(i + 1);
            String count = selected_products.get(i + 2);
            String size = selected_products.get(i + 3);
            String color = selected_products.get(i + 4);
            String comment = selected_products.get(i + 6);

            int intPrice = Integer.parseInt(price) * Integer.parseInt(count);
            sched.setProductId(selected_products.get(i + 5));
            sched.setProductName(name);
            sched.setProductCount(count);
            sched.setProductPrice(Integer.toString(intPrice));
            sched.setProductSize(size);
            sched.setProductsColor(color);
            sched.setComments(comment);
            /******** Take Model Object in ArrayList **********/
            CartListViewArr.add(sched);
        }
    }

    public void setTotalPrice(int totalPrice, ArrayList<ListModel2> data) {
        CartListViewArr = data;

        final TextView tv_total2 = (TextView) findViewById(R.id.total2);
        tv_total2.setText(Integer.toString(totalPrice));
    }

    public void setArrayData() {
        ListModel2 tempValues;
        selected_products = new ArrayList<String>();
        for (int i = 0; i < CartListViewArr.size(); i++) {
            tempValues = CartListViewArr.get(i);
            selected_products.add(tempValues.getProductName());
            selected_products.add(tempValues.getProductPrice());
            selected_products.add(tempValues.getProductCount());
            selected_products.add(tempValues.getProductSize());
            selected_products.add(tempValues.getProductsColor());
            selected_products.add(tempValues.getProductId());
            selected_products.add(tempValues.getComments());
        }
        Intent intent = new Intent(CartActivity.this, CustomListViewAndroidExample.class);
        intent.putExtra("name", category_name);
        intent.putStringArrayListExtra("products", selected_products);
        startActivity(intent);
    }

    public void updateListView() {
        if (CartListViewArr.size() > 0) {
            adapter.notifyDataSetChanged();
        } else {
            selected_products = new ArrayList<String>();
            Intent intent = new Intent(CartActivity.this, CustomListViewAndroidExample.class);
            intent.putExtra("name", category_name);
            intent.putStringArrayListExtra("products", selected_products);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;

        SharedPreferences sharedPreferences = getSharedPreferences("auth_token", 0);
        String token = sharedPreferences.getString("token", "");
        if (!token.equals("")) {
            MenuItem menuItem = menu.findItem(R.id.login);
            menuItem.setTitle("Выйти");
        } else {
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
                Intent intent = new Intent(CartActivity.this, CustomListViewAndroidExample.class);
                intent.putExtra("name", category_name);
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
                    Intent intent2 = new Intent(CartActivity.this, ProductsActivity.class);
                    intent2.putStringArrayListExtra("products", selected_products);
                    startActivity(intent2);
                }else {
                    Intent intent1 = new Intent(CartActivity.this, SignInActivity.class);
                    startActivity(intent1);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setArrayData();
    }
}
