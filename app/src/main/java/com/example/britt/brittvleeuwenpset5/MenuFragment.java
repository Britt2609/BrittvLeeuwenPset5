package com.example.britt.brittvleeuwenpset5;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//android:list
//android.support.v4.app.ListFragment.


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {

    JsonObjectRequest JsonRequest;
    JSONArray items;
    RestoDatabase restoDatabase;
    PictureDatabase pictureDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restoDatabase = RestoDatabase.getInstance(getActivity().getApplicationContext());

        Bundle arguments = this.getArguments();
        final String category = arguments.getString("category");

        pictureDatabase = PictureDatabase.getInstance(getActivity().getApplicationContext());
        pictureDatabase.onDrop();

        Cursor c = pictureDatabase.selectAll();
        PictureAdapter pictureAdapter = new PictureAdapter(getContext(), c);
        this.setListAdapter(pictureAdapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://resto.mprog.nl/menu";

        // Request a string response from the provided URL.
        JsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            items = response.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {

                                JSONObject item = items.getJSONObject(i);
                                String dish = item.getString("category");

                                if (dish.equals(category)) {
                                    pictureDatabase.addItem(item.getString("name"),
                                            item.getString("description"),
                                            category, Float.valueOf(item.getString("price")), item.getString("image_url"));
                                }
                            }

                            Cursor c = pictureDatabase.selectAll();
                            PictureAdapter pictureAdapter = new PictureAdapter(getContext(), c);
                            setListAdapter(pictureAdapter);

                        } catch (JSONException e) {
                            Log.d("tag", e.toString());
                            System.out.println("That didn't work");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("That definently didn't work");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(JsonRequest);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onListItemClick(final ListView li, View v, final int position, long id) {
        super.onListItemClick(li, v, position, id);

        Cursor cursor = (Cursor) li.getItemAtPosition(position);
        Integer index_item = cursor.getColumnIndex("name");
        Integer index_price = cursor.getColumnIndex("price");

        String name = cursor.getString(index_item);
        Float price = cursor.getFloat(index_price);

        restoDatabase.addItem(name, price);
        Toast.makeText(this.getContext(), name + " added to your order!", Toast.LENGTH_LONG).show();
    }
}
