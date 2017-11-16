package com.sistemosoft.muslimline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by appaz on 11/17/16.
 */
public class ContentActivity extends AppCompatActivity {

    private static String product_id = "";
    private static String product_name = "";
    private static String product_price = "";
    private static String category_name = "";
    private String product_image = "";


    ViewPager viewPager;
    Activity activity;
    Button categoryBtn, cartBtn, contactBtn, accountBtn;
    private ArrayList<String> selected_products = new ArrayList<String>();
    TextView tv_product_name, tv_product_price, tv_product_size, tv_product_color, tv_product_content, tv_sale_price;
    ArrayList<ImageModel> imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_content);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager1);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        selected_products = getIntent().getExtras().getStringArrayList("products");

        String product_size = getIntent().getStringExtra("size");
        String product_color = getIntent().getStringExtra("color");
        String product_totaPrice = getIntent().getStringExtra("totalPrice");
        String product_content = getIntent().getStringExtra("description");
        product_image = getIntent().getStringExtra("image_url");
        product_price = getIntent().getStringExtra("price");
        category_name = getIntent().getStringExtra("category_name");
        product_id = getIntent().getStringExtra("id");
        product_name = getIntent().getStringExtra("name");

        collapsingToolbar.setTitle(product_name);
        collapsingToolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a62252")));

        String[] size = product_size.split(",");
        String[] color = product_color.split(",");

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        tv_product_name = (TextView) findViewById(R.id.product_name);
        tv_product_price = (TextView) findViewById(R.id.product_price);
        tv_sale_price = (TextView) findViewById(R.id.sale_price);
        tv_product_size = (TextView) findViewById(R.id.product_size);
        tv_product_color = (TextView) findViewById(R.id.product_color);
        tv_product_content = (TextView) findViewById(R.id.product_content);
        tv_product_name.setText("Имя:  " + product_name.toUpperCase());

        if (!product_price.equals(product_totaPrice)) {

            tv_sale_price.setText("Цена: " + product_price + " Руб");
            tv_product_price.setText("Цена: " + product_totaPrice + " Руб");
            tv_product_price.setPaintFlags(tv_product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv_product_price.setText("");
            tv_sale_price.setText("Цена: " + product_price + " Руб");
        }
        tv_product_size.setText("Размер:  ");
        tv_product_color.setText("Цвет:  ");
        tv_product_content.setText("Описание:  " + product_content);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ContentActivity.this, android.R.layout.simple_spinner_item, size);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setPrompt("Размер");
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ContentActivity.this, android.R.layout.simple_spinner_item, color);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter2);
        spinner1.setPrompt("Цвет");
        spinner1.setSelection(0);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int length = selected_products.size() / 7;
        Button order = (Button) findViewById(R.id.order);
        order.setText("Корзина(" + length + ")");
        new GetClients().execute();

        order = (Button) findViewById(R.id.order1);
        order.setOnTouchListener(new View.OnTouchListener() {

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
                        Spinner spinner = (Spinner) findViewById(R.id.spinner);
                        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
                        int isfind = 0;
                        for (int i = 0; i < selected_products.size(); i = i + 7) {
                            if (selected_products.get(i + 5).equals(product_id)) {
                                isfind = 1;
                                selected_products.set(i, product_name);
                                selected_products.set(i + 1, product_price);
                                selected_products.set(i + 2, "1");
                                selected_products.set(i + 3, spinner.getSelectedItem().toString());
                                selected_products.set(i + 4, spinner1.getSelectedItem().toString());
                                selected_products.set(i + 5, product_id);
                                selected_products.set(i + 6, "");
                                break;
                            }
                        }
                        if (isfind == 0) {
                            selected_products.add(product_name);
                            selected_products.add(product_price);
                            selected_products.add("1");
                            selected_products.add(spinner.getSelectedItem().toString());
                            selected_products.add(spinner1.getSelectedItem().toString());
                            selected_products.add(product_id);
                            selected_products.add("");
                        }
                        Intent intent = new Intent(ContentActivity.this, CustomListViewAndroidExample.class);
                        intent.putStringArrayListExtra("products", selected_products);
                        intent.putExtra("name", category_name);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });

        categoryBtn = (Button) findViewById(R.id.category);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this, ProductsActivity.class);
                intent.putStringArrayListExtra("products", selected_products);
                startActivity(intent);
            }
        });
        cartBtn = (Button) findViewById(R.id.order);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_products.size() == 0) {
                    Toast.makeText(ContentActivity.this, "Cначала выберите товар!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ContentActivity.this, CartActivity.class);
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
                Intent intent = new Intent(ContentActivity.this, ContactActivity.class);
                intent.putStringArrayListExtra("products", selected_products);
                startActivity(intent);
            }
        });
        accountBtn = (Button) findViewById(R.id.account);
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this, SaleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     finish();
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

    private class GetClients extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(ApiUrl.PRODUCT_GALLERIES + product_id);
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
            imageList = new ArrayList<ImageModel>();
            ImageModel imageModel1 = new ImageModel();
            imageModel1.setImage(product_image);
            imageList.add(imageModel1);
            try {
                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    String image = jObject.getString("image_url");
                    String id = jObject.getString("id");
                    ImageModel imageModel = new ImageModel();
                    imageModel.setImage(image);
                    imageList.add(imageModel);

                } // End Loop


            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
            setImage();
        }

        private void setImage() {
            activity = ContentActivity.this;
            viewPager = (ViewPager) findViewById(R.id.pager1);
            ViewPager2 imageView = new ViewPager2(activity, imageList);
            viewPager.setAdapter(imageView);
        }

    }

    public class ImageModel {
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class ViewPager2 extends PagerAdapter {
        private ArrayList<ImageModel> GalImages;
        private Context context;

        public ViewPager2(Activity activity, ArrayList<ImageModel> imageList) {
            context = activity;
            GalImages = imageList;
        }

        @Override
        public int getCount() {
            return GalImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

//    @Override
//    public float getPageWidth(int position) {
//        return 0.9f;
//    }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.swipe_image1, null);
            ImageView img = (ImageView) view.findViewById(R.id.image_view);
            ImageModel imageModel;
            imageModel = GalImages.get(position);
            ((ViewPager) container).addView(view);
            Picasso.with(context).load(ApiUrl.Base_Url + imageModel.getImage()).fit().into(img);


            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

    }

}
