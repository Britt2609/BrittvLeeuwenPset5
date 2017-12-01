package com.example.britt.brittvleeuwenpset5;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by britt on 27-11-2017.
 */

public class RestoAdapter extends ResourceCursorAdapter {


    public RestoAdapter(Context context, Cursor cursor) {
        super(context, R.layout.order, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dish = view.findViewById(R.id.dish);
        TextView amount = view.findViewById(R.id.amount);
        TextView price = view.findViewById(R.id.price);

        Integer index_item = cursor.getColumnIndex("name");
        Integer index_price = cursor.getColumnIndex("price");
        Integer index_amount = cursor.getColumnIndex("amount");

        String value_name = cursor.getString(index_item);
        Integer value_amount = cursor.getInt(index_amount);
        Float value_price = cursor.getFloat(index_price);

        dish.setText(value_name);
        amount.setText(value_amount + "x");
        price.setText("$" + value_price*value_amount);
    }
}
