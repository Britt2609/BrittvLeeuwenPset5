package com.example.britt.brittvleeuwenpset5;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment implements View.OnClickListener, ListView.OnItemLongClickListener {

    RestoDatabase restoDatabase;
    RestoAdapter restoAdapter;
    ListView list;
    TextView total;
    View v;
    Button canc;
    Button ord;
    RequestQueue queue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_order, container, false);
        list = v.findViewById(R.id.listView);
        total = v.findViewById(R.id.totalprice);
        restoDatabase = RestoDatabase.getInstance(getActivity().getApplicationContext());

        canc = (Button) v.findViewById(R.id.cancelbutton);
        canc.setOnClickListener(this);

        ord = (Button) v.findViewById(R.id.orderbutton);
        ord.setOnClickListener(this);

        list.setOnItemLongClickListener(this);


        return v;
    }



    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.cancelbutton: {
                restoDatabase.Clear();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.orderbutton: {
                Order();
                break;
            }
        }
    }


    public void Order() {
        queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest JsonRequestpost = new JsonObjectRequest(Request.Method.POST, "https://resto.mprog.nl/order", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(getActivity(), "Order time: "+ response.getString("preparation_time") + "minutes",
                                    Toast.LENGTH_SHORT).show();

                            restoDatabase.Clear();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "That didn't work!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "ERROR!", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(JsonRequestpost);
    }


    @Override
    public void onViewStateRestored(Bundle inState) {
        super.onViewStateRestored(inState);

        Cursor c = restoDatabase.selectAll();
        restoAdapter = new RestoAdapter(getActivity(), c);
        list.setAdapter(restoAdapter);

        String pricetotal = restoDatabase.totalPrice();

        total.setText(pricetotal);
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) list.getItemAtPosition(position);
        Integer index_item = cursor.getColumnIndex("name");

        String name = cursor.getString(index_item);

        Toast.makeText(this.getContext(), name + " deleted from your order!", Toast.LENGTH_LONG).show();
        restoDatabase.deleteItem(name);

        Cursor c = restoDatabase.selectAll();
        restoAdapter = new RestoAdapter(getActivity(), c);
        list.setAdapter(restoAdapter);
        return true;
    }
}
