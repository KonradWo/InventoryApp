package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.text_name);
        TextView priceTextView = view.findViewById(R.id.edit_text_product_price);
        TextView quantityTextView = view.findViewById(R.id.text_quantity);

        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        int id = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        final int quantity = cursor.getInt(quantityColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);

        nameTextView.setText(productName);
        priceTextView.setText(context.getResources().getString(R.string.product_price) + productPrice + context.getResources().getString(R.string.currency));
        quantityTextView.setText(context.getResources().getString(R.string.product_quantity)  + productQuantity);

        final Uri mCurrentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

        ImageButton sellButton = view.findViewById(R.id.button_sell);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues values = new ContentValues();
                if (quantity > 0) {
                    int newQuantity = quantity;
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, --newQuantity);
                    resolver.update(mCurrentProductUri, values, null, null);
                    context.getContentResolver().notifyChange(mCurrentProductUri, null);
                } else {
                    Toast.makeText(context, R.string.product_unavailable, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
