package com.sistemosoft.muslimline;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by appaz on 11/17/16.
 */
public class SendActivity extends FragmentActivity {
    private static String category_name = "";
    private static Button star1, star2, star3, star4, star5, home, facebook, instagram, vkontakte, odnoklassnik;
    private int star_value = 0;

    private static String comments = "";
    private static String customer_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sended);

        category_name = getIntent().getStringExtra("name");

        home = (Button) findViewById(R.id.home);
        home.setOnTouchListener(new View.OnTouchListener() {

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
                        EditText et_comments = (EditText) findViewById(R.id.et_comments);
                        comments = et_comments.getText().toString();

                        Intent intent = new Intent(SendActivity.this, ProductsActivity.class);
                        intent.putStringArrayListExtra("products", new ArrayList<String>());
                        startActivity(intent);
                /*
                url = "http://172.33.166.71/ords/!data.saveComments?WSESSION_ID=" + null + "&WCUSTOMER_ID=" + customer_id + "&WCUSTOMER_COMMENT=" + comments + "&WRATING=" + star_value;
                Toast.makeText(SendActivity.this, "url: " + url, Toast.LENGTH_SHORT).show();
                new GetContacts().execute();
                */
                        break;
                    }
                }
                return true;
            }
        });
        facebook = (Button) findViewById(R.id.facebook);
        facebook.setOnTouchListener(new View.OnTouchListener() {

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
                        String url = "https://web.facebook.com/MuslimLine-muslim-clothers-1531522803770968/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    }
                }
                return true;
            }
        });

        instagram = (Button) findViewById(R.id.instagram);
        instagram.setOnTouchListener(new View.OnTouchListener() {

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
                        String url = "https://www.instagram.com/muslimline.official/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    }
                }
                return true;
            }
        });
        vkontakte = (Button) findViewById(R.id.vkontakte);
        vkontakte.setOnTouchListener(new View.OnTouchListener() {

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
                        String url = "https://vk.com/club77023514";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    }
                }
                return true;
            }
        });
        odnoklassnik = (Button) findViewById(R.id.odnoklassnik);
        odnoklassnik.setOnTouchListener(new View.OnTouchListener() {

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
                        String url = "https://ok.ru/profile/576538834081/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    }
                }
                return true;
            }
        });

        star1 = (Button) findViewById(R.id.star1);
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_active);
                star2.setBackgroundResource(R.drawable.star);
                star3.setBackgroundResource(R.drawable.star);
                star4.setBackgroundResource(R.drawable.star);
                star5.setBackgroundResource(R.drawable.star);
                star_value = 1;
            }
        });

        star2 = (Button) findViewById(R.id.star2);
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_active);
                star2.setBackgroundResource(R.drawable.star_active);
                star3.setBackgroundResource(R.drawable.star);
                star4.setBackgroundResource(R.drawable.star);
                star5.setBackgroundResource(R.drawable.star);
                star_value = 2;
            }
        });

        star3 = (Button) findViewById(R.id.star3);
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_active);
                star2.setBackgroundResource(R.drawable.star_active);
                star3.setBackgroundResource(R.drawable.star_active);
                star4.setBackgroundResource(R.drawable.star);
                star5.setBackgroundResource(R.drawable.star);
                star_value = 3;
            }
        });

        star4 = (Button) findViewById(R.id.star4);
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_active);
                star2.setBackgroundResource(R.drawable.star_active);
                star3.setBackgroundResource(R.drawable.star_active);
                star4.setBackgroundResource(R.drawable.star_active);
                star5.setBackgroundResource(R.drawable.star);
                star_value = 4;
            }
        });

        star5 = (Button) findViewById(R.id.star5);
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_active);
                star2.setBackgroundResource(R.drawable.star_active);
                star3.setBackgroundResource(R.drawable.star_active);
                star4.setBackgroundResource(R.drawable.star_active);
                star5.setBackgroundResource(R.drawable.star_active);
                star_value = 5;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EditText et_comments = (EditText) findViewById(R.id.et_comments);
        comments = et_comments.getText().toString();

        Intent intent = new Intent(SendActivity.this, ProductsActivity.class);
        intent.putStringArrayListExtra("products", new ArrayList<String>());
        startActivity(intent);
    }
}

