package com.sistemosoft.muslimline;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by appaz on 11/17/16.
 **/

public class CartCustomAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList<ListModel2> data;
    private static LayoutInflater inflater = null;
    private ListModel2 tempValues = null;
    public Resources res;
    int i = 0;

    public CartCustomAdapter(Activity a, ArrayList<ListModel2> d, Resources resLocal) {

        activity = a;
        data = d;
        res = resLocal;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        if (data.size() <= 0)
            return 1;
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

    public static class ViewHolder {

        public TextView product_id;
        public TextView product_name;
        public TextView product_price;
        public Button cancel;
        public Button minus;
        public Button plus;
        public TextView product_count;
        public EditText comments;
        private Button add_comment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.activity_list_cart, parent, false);

            holder = new ViewHolder();
            holder.product_id = (TextView) vi.findViewById(R.id.product_id);
            holder.product_name = (TextView) vi.findViewById(R.id.product_name);
            holder.product_price = (TextView) vi.findViewById(R.id.product_price);
            holder.product_count = (TextView) vi.findViewById(R.id.product_count);
            holder.comments = (EditText) vi.findViewById(R.id.et_comments);
            holder.add_comment = (Button) vi.findViewById(R.id.add_comment);
            holder.cancel = (Button) vi.findViewById(R.id.cancel);
            holder.minus = (Button) vi.findViewById(R.id.minus);
            holder.plus = (Button) vi.findViewById(R.id.plus);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        tempValues = null;
        tempValues = data.get(position);
        holder.product_id.setText(tempValues.getProductId());
        holder.product_name.setText(tempValues.getProductName() + "(" + tempValues.getProductsColor() + "," + tempValues.getProductSize() + ")");
        holder.product_count.setText(tempValues.getProductCount());
        holder.product_price.setText(tempValues.getProductPrice());
        holder.comments.setText(tempValues.getComments());

        holder.add_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77055555, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        // Your action here on button click
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        String comments = holder.comments.getText().toString();
                        tempValues = data.get(position);
                        tempValues.setComments(comments);
                        data.set(position, tempValues);
                        Toast.makeText(activity,"Комментарий добавлен!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });

        holder.cancel.setOnTouchListener(new View.OnTouchListener() {

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
                        data.remove(position);
                        try {
                            int totalPrice = 0;
                            int data_price = 0;
                            for (int i = 0; i < data.size(); i++) {
                                tempValues = null;
                                tempValues = data.get(i);
                                data_price = Integer.parseInt(tempValues.getProductPrice());
                                totalPrice = totalPrice + data_price;
                            }
                            CartActivity sct = (CartActivity) activity;
                            sct.setTotalPrice(totalPrice, data);
                            sct.updateListView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        int count1 = Integer.parseInt(tempValues.getProductCount());
                        int price = Integer.parseInt(tempValues.getProductPrice());
                        int single_price = price / count1;
                        int total_price1 = 0;
                        if (count1 >= 2) {
                            count1 = count1 - 1;
                            total_price1 = count1 * single_price;
                            tempValues.setProductCount(Integer.toString(count1));
                            tempValues.setProductPrice(Integer.toString(total_price1));
                            data.set(position, tempValues);

                            holder.product_count.setText(Integer.toString(count1));
                            holder.product_price.setText(Integer.toString(total_price1));

                            try {
                                int totalPrice = 0;
                                int data_price = 0;
                                for (int i = 0; i < data.size(); i++) {
                                    tempValues = null;
                                    tempValues = data.get(i);
                                    data_price = Integer.parseInt(tempValues.getProductPrice());
                                    totalPrice = totalPrice + data_price;
                                }
                                CartActivity sct = (CartActivity) activity;
                                sct.setTotalPrice(totalPrice, data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                        int count1 = Integer.parseInt(tempValues.getProductCount());
                        int price = Integer.parseInt(tempValues.getProductPrice());
                        int single_price = price / count1;
                        int total_price1 = 0;
                        count1 = count1 + 1;
                        total_price1 = count1 * single_price;
                        tempValues.setProductCount(Integer.toString(count1));
                        tempValues.setProductPrice(Integer.toString(total_price1));
                        data.set(position, tempValues);

                        holder.product_count.setText(Integer.toString(count1));
                        holder.product_price.setText(Integer.toString(total_price1));
                        try {
                            int totalPrice = 0;
                            int data_price = 0;
                            for (int i = 0; i < data.size(); i++) {
                                tempValues = null;
                                tempValues = data.get(i);
                                data_price = Integer.parseInt(tempValues.getProductPrice());
                                totalPrice = totalPrice + data_price;
                            }
                            CartActivity sct = (CartActivity) activity;
                            sct.setTotalPrice(totalPrice, data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

        }
    }
}

