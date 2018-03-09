package com.example.tanmoy.recyclerview_with_volley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycler_view;
    ArrayList<SetGet> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recycler_view.setLayoutManager(layoutManager);
        loaddatafromurl();
    }

    private void loaddatafromurl(){

        String url_client ="http://wptrafficanalyzer.in/p/demo1/first.php/countries/";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url_client, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rootobj = new JSONObject(response);
                    JSONArray jsonArray = rootobj.getJSONArray("countries");
                    arrayList = new ArrayList<SetGet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SetGet setGet = new SetGet();
                        JSONObject innerobj = jsonArray.getJSONObject(i);
                        setGet.setFlag(innerobj.getString("flag"));
                        setGet.setCountryname(innerobj.getString("countryname"));
                        setGet.setLanguage(innerobj.getString("language"));
                        setGet.setCapital(innerobj.getString("capital"));
                        JSONObject innerobj2 = innerobj.getJSONObject("currency");
                        setGet.setCurrencyname(innerobj2.getString("currencyname"));
                        arrayList.add(setGet);
                    }
                    recycler_view.setAdapter(new Custom_RecyclerAdapter());
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Volley Response Error".toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Volley Error : "+error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    //RecyclerView.Adapter represents the data that is to be shown with the ViewHoder.
    public class Custom_RecyclerAdapter extends RecyclerView.Adapter<Custom_RecyclerAdapter.MyViewHolder>{

        //This method returns a new instance of our ViewHolder.
        @Override
        public Custom_RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_layout, null);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        //This method binds the data to the view holder.
        @Override
        public void onBindViewHolder(Custom_RecyclerAdapter.MyViewHolder holder, int position) {
            Picasso.with(MainActivity.this).load(arrayList.get(position).getFlag()).into(holder.iv_flag);
            holder.tv_countryname.setText(arrayList.get(position).getCountryname());     //set variables in TextView's
            holder.tv_language.setText(arrayList.get(position).getLanguage());
            holder.tv_capital.setText(arrayList.get(position).getCapital());
            holder.tv_currencyname.setText(arrayList.get(position).getCurrencyname());
        }

        //This returns the size of the List.
        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        //RecyclerView.ViewHolder represents the views of our RecyclerView.
        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_flag;
            TextView tv_countryname,tv_language, tv_capital, tv_currencyname;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv_flag = (ImageView)itemView.findViewById(R.id.iv_flag);
                tv_countryname = (TextView)itemView.findViewById(R.id.tv_countryname);
                tv_language = (TextView)itemView.findViewById(R.id.tv_language);
                tv_capital = (TextView)itemView.findViewById(R.id.tv_capital);
                tv_currencyname = (TextView)itemView.findViewById(R.id.tv_currencyname);
            }
        }
    }
}
