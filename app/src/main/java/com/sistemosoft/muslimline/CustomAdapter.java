package com.sistemosoft.muslimline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by appaz on 11/17/16.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList<ListModel> data;
    private ListModel tempValues = null;
    int i = 0;

    public CustomAdapter(Activity a, ArrayList<ListModel> d) {
        activity = a;
        data = d;
    }

    @Override
    public int getCount() {

        if (data.size() <= 0) {
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView product_id;
        public TextView product_name;
        public TextView product_content;
        public TextView product_price;
        public TextView total_price;
        public TextView product_size;
        public TextView product_color;
        public ImageView product_image;
        public TextView sale_price;
        public Button addcart;
        public Button minus;
        public Button plus;
        public EditText count;
        public Spinner spinner;
        public Spinner spinner1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        final int mposition = position;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.activity_list_detail, parent, false);

            holder = new ViewHolder();
            holder.product_id = (TextView) vi.findViewById(R.id.product_id);
            holder.product_name = (TextView) vi.findViewById(R.id.product_name);
            holder.product_content = (TextView) vi.findViewById(R.id.product_content);
            holder.product_price = (TextView) vi.findViewById(R.id.product_price);
            holder.sale_price = (TextView) vi.findViewById(R.id.sale_price);
            holder.total_price = (TextView) vi.findViewById(R.id.total_price);
            holder.product_size = (TextView) vi.findViewById(R.id.product_size);
            holder.product_color = (TextView) vi.findViewById(R.id.product_color);
            holder.product_image = (ImageView) vi.findViewById(R.id.product_image);
            holder.spinner = (Spinner) vi.findViewById(R.id.spinner);
            holder.spinner1 = (Spinner) vi.findViewById(R.id.spinner1);
            holder.addcart = (Button) vi.findViewById(R.id.addcart);
            holder.minus = (Button) vi.findViewById(R.id.minus);
            holder.plus = (Button) vi.findViewById(R.id.plus);
            holder.count = (EditText) vi.findViewById(R.id.et_count);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        tempValues = null;
        tempValues = data.get(position);
        final int m_position = position;
        final int color_position = position;

        holder.product_id.setText(tempValues.getProductId());
        holder.product_name.setText(tempValues.getProductName());
        holder.product_price.setText("Цена: " + tempValues.getProductPrice() + " Руб");
        holder.count.setText(tempValues.getCount());
        if (!tempValues.getProductPrice().equals(tempValues.getTotalPrice())) {

            holder.total_price.setText("Цена: " + tempValues.getProductPrice() + " Руб");
            holder.sale_price.setText("Цена: " + tempValues.getTotalPrice() + " Руб");
            holder.sale_price.setPaintFlags(holder.sale_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.sale_price.setText("");
            holder.total_price.setText("Цена: " + tempValues.getProductPrice() + " Руб");
        }
        Picasso.with(activity).load(ApiUrl.Base_Url + tempValues.getImage()).into(holder.product_image);
        String sizes = tempValues.getProductSize();
        String colors = tempValues.getProductColor();
        String[] size = sizes.split(",");
        String[] color = colors.split(",");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, size);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter1);
        holder.spinner.setPrompt("Размер");
        holder.spinner.setSelection(0);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tempValues = data.get(m_position);
                tempValues.setProductsSize(holder.spinner.getSelectedItem().toString());
                data.set(m_position, tempValues);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, color);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner1.setAdapter(adapter2);
        holder.spinner1.setPrompt("Цвет");
        holder.spinner1.setSelection(0);
        holder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tempValues = data.get(color_position);
                tempValues.setProductsColor(holder.spinner1.getSelectedItem().toString());
                // Toast.makeText(activity, "size " + holder.spinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                data.set(color_position, tempValues);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /******** Set Item Click Listner for LayoutInflater for each row *******/

        holder.addcart.setOnTouchListener(new View.OnTouchListener() {

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
                        CustomListViewAndroidExample sct = (CustomListViewAndroidExample) activity;
                        sct.onAddcartClick(position, data);
                        break;
                    }
                }
                return true;
            }
        });

        holder.minus.setOnTouchListener(new View.OnTouchListener() {

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

                        tempValues = null;
                        tempValues = data.get(position);
                        int count1 = Integer.parseInt(tempValues.getCount());
                        int price = Integer.parseInt(tempValues.getProductPrice());
                        int total_price1 = 0;
                        if (count1 >= 2) {
                            count1 = count1 - 1;
                            total_price1 = count1 * price;
                            tempValues.setCount(Integer.toString(count1));
                            tempValues.setTotalPrice(Integer.toString(total_price1));
                            data.set(position, tempValues);

                            holder.count.setText(Integer.toString(count1));
                            holder.total_price.setText("Цена: " + Integer.toString(total_price1) + " Руб");
                        }
                        break;
                    }
                }
                return true;
            }
        });

        holder.plus.setOnTouchListener(new View.OnTouchListener() {

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

                        tempValues = null;
                        tempValues = data.get(position);
                        int count1 = Integer.parseInt(tempValues.getCount());
                        int price = Integer.parseInt(tempValues.getProductPrice());
                        int total_price1 = 0;
                        count1 = count1 + 1;
                        total_price1 = count1 * price;
                        tempValues.setCount(Integer.toString(count1));
                        tempValues.setTotalPrice(Integer.toString(total_price1));
                        data.set(position, tempValues);

                        holder.count.setText(Integer.toString(count1));
                        holder.total_price.setText("Цена: " + Integer.toString(total_price1) + " Руб");
                        break;
                    }
                }
                return true;
            }
        });

        vi.setOnClickListener(new OnItemClickListener(position));

        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            CustomListViewAndroidExample sct = (CustomListViewAndroidExample) activity;
            sct.onItemClick(mPosition);
        }
    }
}
