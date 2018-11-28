package com.example.kanav.Heroku;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Heroku extends AppCompatActivity {
RecyclerView mRecyclerView;
// for showing users in a recyclerview
ArrayList<user> users=new ArrayList<>();
// users arraylist will be used for accessing users from json
    private int firstVisibleItem;
    //firstVisibleItem will point to first visible item of recyclerview
    private int visibleItemCount;
    //visibleItemCount is the visible items on recyclerview
    private int totalItemCount;
    private int itemCount = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private final int visibleThreshold = 1;
    int offset1=0;
    int limit=10;
    Heroku_adapter adapter;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroku);
        mRecyclerView = findViewById(R.id.users);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        // DisplayMetrics is used to get width of the screen
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        adapter=new Heroku_adapter(users,getApplicationContext(),width);

        fetchUsers(offset1,limit);
        // method to fetch data from heroku API
        GridLayoutManager llm = new GridLayoutManager(getApplicationContext(),1);
        mRecyclerView.setLayoutManager(llm);
        addOnScrollListener(llm);






    }


/// fetchUsers is a method used to fetch details from heroku API
    public void fetchUsers( final int offset, final int limit){


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Downloading Users data");
        dialog.show();

        queue = Volley.newRequestQueue(this);

        String url = "http://sd2-hiring.herokuapp.com/api/users?+offset="+offset+"&limit="+limit;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    dialog.dismiss();
                    // very important to dismiss dialog as it will cause memory leak
                    JSONObject data=response.getJSONObject("data");
                    // will fetch data object from json
                    JSONArray u=data.getJSONArray("users");
                    // will fetch users array object from json

                    for(int i=0;i< u.length();i++)
                    // fot iterating through users list to get items
                    {
                        JSONObject us=u.getJSONObject(i);
                        ArrayList<String> listdata = new ArrayList<String>();
                        JSONArray jArray = us.getJSONArray("items");
                        if (jArray != null) {
                            for (int j=0;j<jArray.length();j++){
                                listdata.add(jArray.getString(j));
                            }
                        }
                        users.add(new user(us.getString("name"),us.getString("image"),listdata));
                        //adapter.addAll(users);

                        //Log.i("SIZE",users.size()+" ");
                        adapter.notifyDataSetChanged();

                    }
                    offset1=offset+10;
                    // will increase the offset count for accessing users while scrolling
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("v",error.getMessage()+"");

            }
        });

        queue.add(jsonObjectRequest);

        mRecyclerView.setAdapter(adapter);



    }


    private void addOnScrollListener(final GridLayoutManager gridLayoutManager) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mRecyclerView.getChildCount();

                totalItemCount = gridLayoutManager.getItemCount();

                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();


                if(loading) {



                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                  fetchUsers(offset1,limit);

                    loading = true;
                }
            }
        });

    }

}
