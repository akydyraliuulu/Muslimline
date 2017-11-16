package com.sistemosoft.muslimline;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static com.sistemosoft.muslimline.R.id.flag_image;

public class ContactActivity extends AppCompatActivity {

    ListView list;
    ContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a62252")));
        actionBar.setTitle("Контакты");

        list = (ListView) findViewById(R.id.contact_list);
        adapter = new ContentAdapter(getApplicationContext());
        list.setAdapter(adapter);

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

    public static class ViewHolder{

        public ImageView flag_image;
        public TextView contact;
        public TextView address;
    }

    public static class ContentAdapter extends BaseAdapter{
        private static final int LENGTH = 9;
        private Context mcontext;

        private final String[] contact;
        private final String[] address;
        private final Drawable[] flags;

        public ContentAdapter(Context context){
            mcontext = context;
            Resources resources = context.getResources();
            contact = resources.getStringArray(R.array.contact);
            address = resources.getStringArray(R.array.address);
            TypedArray array = resources.obtainTypedArray(R.array.flags);
            flags = new Drawable[array.length()];

            for (int i = 0; i < flags.length; i++){
                flags[i] = array.getDrawable(i);
            }
            array.recycle();
        }

        @Override
        public int getCount() {
            return LENGTH;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;

            if (convertView == null){

                LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_contact_detail, parent, false);
            holder = new ViewHolder();
            holder.contact = (TextView) view.findViewById(R.id.text_city);
            holder.address = (TextView) view.findViewById(R.id.text_address);
            holder.flag_image = (ImageView) view.findViewById(flag_image);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            holder.flag_image.setImageDrawable(flags[position % flags.length]);
            holder.contact.setText(contact[position % contact.length]);
            holder.address.setText(address[position % address.length]);

            return view;
        }



    }
}
