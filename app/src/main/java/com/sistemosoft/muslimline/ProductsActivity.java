package com.sistemosoft.muslimline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

/**
 * Created by appaz on 11/17/16.
 */
public class ProductsActivity extends AppCompatActivity {

    private Menu menu;
    ArrayList<ModelClass> aList;
    ModelClass values;
    CustomAdapter customAdapter;
    Button cartBtn, contactBtn, accountBtn;
    private ArrayList<String> selected_products = new ArrayList<String>();
    private ArrayList<String> images4 = new ArrayList<String>();
    private ArrayList<String> images5 = new ArrayList<String>();
    private ArrayList<String> images6 = new ArrayList<String>();
    private ArrayList<String> images7 = new ArrayList<String>();
    private ArrayList<String> images8 = new ArrayList<String>();
    private ArrayList<String> images9 = new ArrayList<String>();
    private ArrayList<String> images10 = new ArrayList<String>();
    private ArrayList<String> images11 = new ArrayList<String>();
    private ArrayList<String> images12 = new ArrayList<String>();
    private ArrayList<String> images13 = new ArrayList<String>();
    private ArrayList<String> images14 = new ArrayList<String>();
    ArrayList<ModelClass> clientList = new ArrayList<ModelClass>();
    private static String category_name = "Платья";
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selected_products = getIntent().getExtras().getStringArrayList("products");
        Resources res = getResources();
        String[] products = res.getStringArray(R.array.category_array);
        lv = (ListView) findViewById(R.id.main_list);
        aList = new ArrayList<ModelClass>();
        for (String product : products) {
            ModelClass modelClass = new ModelClass();
            modelClass.setTitleToDisplay(product);
            aList.add(modelClass);
        }
        if (isOnline()) {
            new GetClients().execute();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Ошибка!Пожалуйста, проверьте интернет-соединение.");
            builder.setPositiveButton("Ok ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //Toast.makeText(getApplicationContext(),"Check the network connection",Toast.LENGTH_SHORT).show();
        }
        int length = selected_products.size() / 7;
        Button order = (Button) findViewById(R.id.order);
        order.setText("Корзина(" + length + ")");

        cartBtn = (Button) findViewById(R.id.order);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_products.size() == 0) {
                    Toast.makeText(ProductsActivity.this, "Cначала выберите товар!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ProductsActivity.this, CartActivity.class);
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
                Intent intent = new Intent(ProductsActivity.this, ContactActivity.class);
                intent.putStringArrayListExtra("products", selected_products);
                startActivity(intent);
            }
        });
        accountBtn = (Button) findViewById(R.id.account);
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, SaleActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onItemClick(int mPosition) {
        ModelClass value = (ModelClass) aList.get(mPosition);
        Intent intent = new Intent(ProductsActivity.this, CustomListViewAndroidExample.class);
        intent.putExtra("name", value.getTitleToDisplay());
        intent.putStringArrayListExtra("products", selected_products);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
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
                }else {
                    Intent intent1 = new Intent(ProductsActivity.this, SignInActivity.class);
                    startActivity(intent1);
                }
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

    private class GetClients extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;
        private ProgressDialog progressDialog = new ProgressDialog(ProductsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Пожалуйста, подождите...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    GetClients.this.cancel(false);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(ApiUrl.VIEWPAGER_DATA);
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
                    String id = jObject.getString("id");

                    JSONArray jsonArray = jObject.getJSONArray("products");
                    for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i1);
                        String urls = jsonObject.getString("image_url");

                        ModelClass modelClass = new ModelClass();
                        modelClass.setImageToDisplay(urls);
                        modelClass.setId(id);
                        clientList.add(modelClass);
                    }
                } // End Loop
                this.progressDialog.dismiss();
                images4 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("4")) {
                        images4.add(temp.getImageToDisplay());
                    }
                }
                images5 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("5")) {
                        images5.add(temp.getImageToDisplay());
                    }
                }
                images6 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("6")) {
                        images6.add(temp.getImageToDisplay());
                    }
                }
                images7 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("7")) {
                        images7.add(temp.getImageToDisplay());
                    }
                }
                images8 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("8")) {
                        images8.add(temp.getImageToDisplay());
                    }
                }
                images9 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("9")) {
                        images9.add(temp.getImageToDisplay());
                    }
                }
                images10 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("10")) {
                        images10.add(temp.getImageToDisplay());
                    }
                }
                images11 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("11")) {
                        images11.add(temp.getImageToDisplay());
                    }
                }
                images12 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("12")) {
                        images12.add(temp.getImageToDisplay());
                    }
                }
                images13 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("13")) {
                        images13.add(temp.getImageToDisplay());
                    }
                }
                images14 = new ArrayList<String>();
                for (int i = 0; i < clientList.size(); i++) {
                    ModelClass temp = clientList.get(i);
                    if (temp.getId().equals("14")) {
                        images14.add(temp.getImageToDisplay());
                    }
                }

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
            customAdapter = new CustomAdapter(ProductsActivity.this, aList, clientList);
            lv.setAdapter(customAdapter);
        }
    }

    public class CustomAdapter extends BaseAdapter {

        private Activity activity;
        private ArrayList<ModelClass> data;
        ArrayList<ModelClass> imageList;

        public CustomAdapter(Activity a, ArrayList<ModelClass> d, ArrayList<ModelClass> list) {
            activity = a;
            data = d;
            imageList = list;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {

            public Button bolshe;
            public TextView product_name;
            public ViewPager viewpager;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHolder holder;
            if (convertView == null) {

                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = inflater.inflate(R.layout.main_list_item, parent, false);

                holder = new ViewHolder();
                holder.product_name = (TextView) vi.findViewById(R.id.category_name1);
                holder.bolshe = (Button) vi.findViewById(R.id.more1);
                holder.viewpager = (ViewPager) vi.findViewById(R.id.pager1);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            values = data.get(position);
            holder.product_name.setText(values.getTitleToDisplay());
            if (position == 0) {
                ChildView childView = new ChildView(activity, position, images4);
                holder.viewpager.setAdapter(childView);
            } else if (position == 1) {
                ChildView childView = new ChildView(activity, position, images5);
                holder.viewpager.setAdapter(childView);
            } else if (position == 2) {
                ChildView childView = new ChildView(activity, position, images6);
                holder.viewpager.setAdapter(childView);
            } else if (position == 3) {
                ChildView childView = new ChildView(activity, position, images7);
                holder.viewpager.setAdapter(childView);
            } else if (position == 4) {
                ChildView childView = new ChildView(activity, position, images8);
                holder.viewpager.setAdapter(childView);
            } else if (position == 5) {
                ChildView childView = new ChildView(activity, position, images9);
                holder.viewpager.setAdapter(childView);
            } else if (position == 6) {
                ChildView childView = new ChildView(activity, position, images10);
                holder.viewpager.setAdapter(childView);
            } else if (position == 7) {
                ChildView childView = new ChildView(activity, position, images14);
                holder.viewpager.setAdapter(childView);
            } else if (position == 8) {
                ChildView childView = new ChildView(activity, position, images11);
                holder.viewpager.setAdapter(childView);
            } else if (position == 9) {
                ChildView childView = new ChildView(activity, position, images12);
                holder.viewpager.setAdapter(childView);
            } else if (position == 10) {
                ChildView childView = new ChildView(activity, position, images13);
                holder.viewpager.setAdapter(childView);
            }
            holder.bolshe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ModelClass value = new ModelClass();
                    value = data.get(position);
                    Intent intent = new Intent(ProductsActivity.this, CustomListViewAndroidExample.class);
                    intent.putExtra("name", value.getTitleToDisplay());
                    intent.putStringArrayListExtra("products", selected_products);
                    startActivity(intent);
                }
            });
//            holder.product_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Toast.makeText(getApplicationContext(), "pos : " + position, Toast.LENGTH_SHORT).show();
//                    ModelClass value = new ModelClass();
//                    value = (ModelClass) data.get(position);
//                    Intent intent = new Intent(ProductsActivity.this, CustomListViewAndroidExample.class);
//                    intent.putExtra("name", value.getTitleToDisplay());
//                    intent.putStringArrayListExtra("products", selected_products);
//                    startActivity(intent);
//                }
//            });
//            holder.viewpager.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ModelClass value = new ModelClass();
//                    value = (ModelClass) data.get(position);
//                    Intent intent = new Intent(ProductsActivity.this, CustomListViewAndroidExample.class);
//                    intent.putExtra("name", value.getTitleToDisplay());
//                    intent.putStringArrayListExtra("products", selected_products);
//                    startActivity(intent);
//                }
//            });

            vi.setOnClickListener(new OnItemClickListener(position));
            return vi;
        }

        private class OnItemClickListener implements View.OnClickListener {
            private int mPosition;

            OnItemClickListener(int position) {
                mPosition = position;
            }

            @Override
            public void onClick(View v) {
                ProductsActivity sct = (ProductsActivity) activity;
                /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
                sct.onItemClick(mPosition);
            }
        }
    }

    public class ChildView extends PagerAdapter {

        private ArrayList<String> GalImages;
        private Context context;
        int mposition;

        public ChildView(Activity activity, int position, ArrayList<String> imageList) {
            context = activity;
            mposition = position;
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

        @Override
        public float getPageWidth(int position) {
            return 0.4f;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.swipe_image, null);
            if (mposition == 0) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images4.get(position)).fit().into(img);
                    Log.d("POsition ", Integer.toString(position));
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 1) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images5.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 2) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images6.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 3) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images7.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 4) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images8.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 5) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images9.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 6) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images10.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 7) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images14.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 8) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images11.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 9) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images12.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else if (mposition == 10) {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(ApiUrl.Base_Url + images13.get(position)).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            } else {
                try {
                    final ImageView img = (ImageView) view.findViewById(R.id.imageView1);
                    ((ViewPager) container).addView(view);
                    Picasso.with(context).load(R.drawable.muslimline_logo).fit().into(img);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }
}
