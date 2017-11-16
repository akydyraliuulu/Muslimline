package com.sistemosoft.muslimline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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

public class SaleActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<SaleImage> CustomListViewValuesArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a62252")));
        actionBar.setTitle("Акции");
        listView = (ListView) findViewById(R.id.sale_listView);
        if (isOnline()) {
            GetClients asyncT = new GetClients();
            asyncT.execute();
        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(SaleActivity.this);
            builder.setMessage("Ошибка!Пожалуйста, проверьте интернет-соединение.");
            builder.setPositiveButton("Ok ",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //Toast.makeText(getApplicationContext(),"Check the network connection",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
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
        private ProgressDialog progressDialog = new ProgressDialog(SaleActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Загрузка товаров...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    SaleActivity.GetClients.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://178.62.65.161/api/mobile/stocks/index");
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

            CustomListViewValuesArr = new ArrayList<SaleImage>();

            try {
                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    String image = jObject.getString("image_url");
//                    String name = jObject.getString("name");
//                    String color = jObject.getString("tints");
//                    color = (color == null) ? "" : color;
//                    String sizes = jObject.getString("size");
//                    String price = jObject.getString("price");
//                    String description = jObject.getString("description");
//                    String id = jObject.getString("id");
                    SaleImage saleImage = new SaleImage();
                    saleImage.setImage(image);
                    CustomListViewValuesArr.add(saleImage);
//                    listModel.setProductId(id);
//                    listModel.setProductName(name);
//                    listModel.setProductContent(description);
//                    listModel.setProductSize(sizes);
//                    listModel.setProductColor(color);
//                    listModel.setProductPrice(price);
//                    listModel.setTotalPrice(price);
//                    listModel.setCount("1");
//                    listModel.setImage(image);
                } // End Loop
                this.progressDialog.dismiss();

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
            listView.setAdapter(new ImageAdapter(SaleActivity.this, CustomListViewValuesArr));
        }
    }

    public class SaleImage {
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private ArrayList<SaleImage> data;
        SaleImage tempValues;
        private Activity activity;

        public ImageAdapter(SaleActivity saleActivity, ArrayList<SaleImage> data) {
            activity = saleActivity;
            this.data = data;
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
            public ImageView imageView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = inflater.inflate(R.layout.activity_sale_detail, parent, false);

                holder = new ViewHolder();
                holder.imageView = (ImageView) vi.findViewById(R.id.sale_image_id);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder)vi.getTag();
            }

            tempValues = data.get(position);
            Picasso.with(getApplicationContext()).load("http://178.62.65.161/" + tempValues.getImage()).fit().into(holder.imageView);
            return vi;
        }
    }
}
