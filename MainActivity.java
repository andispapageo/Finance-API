package com.example.user.bdswiss_test.feature;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.liveDataListView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.BLUE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Papageorgiou Andreas IT Test", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutmanager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                getrequest();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }


public class ObjectData{
        public String symbol;
        public double bid;
        public double ask;
        public double price;
        public int color;
}
    private static final String symbol = "symbol";
    private static final String bid= "bid";
    private static final String ask = "ask";
    private static final String price = "price";
    List<ObjectData> apilist = new ArrayList<ObjectData>();
    List<ObjectData> saved_livedatalist = new ArrayList<ObjectData>();
    RecylerAdapter adaptor;
    public void getrequest()
    {

        if(apilist != null) {
            saved_livedatalist = apilist;
        }
        apilist = new ArrayList<ObjectData>();
        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                URL url;
                HttpsURLConnection urlConnection = null;
                 try {
                    url = new URL("https://forex.1forge.com/1.0.1/quotes?pairs=EURUSD,BTCUSD,GBPJPY,XAGUSD,GBPUSD,EURJPY,XRPUSD,AUDJPY,AUDUSD&api_key=B5X8nOnIJwKYbNwdMnepTcG7ZkRTDPaK");
                    urlConnection = (HttpsURLConnection) url
                            .openConnection();
                    urlConnection.setRequestProperty("User-Agent", "com.example.user.bdswiss_test.feature");
                    urlConnection.setReadTimeout(2000);
                    InputStream in = urlConnection.getInputStream();
                    Scanner scan = new Scanner(in);
                    InputStreamReader isw = new InputStreamReader(in);
                     String str = new String();

                     while(scan.hasNext())
                    {
                        str += scan.nextLine();
                    }
                     JSONArray jsonarray = new JSONArray(str);

                 for(int i =0; i<jsonarray.length(); i++)
                 {
                     JSONObject jsonobject = jsonarray.getJSONObject(i);
                     ObjectData s = new ObjectData();

                     s.symbol = jsonobject.getString(symbol);
                     s.bid = jsonobject.getDouble(bid);
                     s.ask = jsonobject.getDouble(ask);
                     s.price = jsonobject.getDouble(price);
                     s.color = Color.BLACK;
                     apilist.add(s);
                 }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                        UpdateResults();
                        UpdateStates();
                    }
                }
            }
        });
    }

   public void UpdateResults()
    {
         if(adaptor == null)
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (null != recyclerView && apilist.size() > 0) {

                        adaptor = new RecylerAdapter(apilist, MainActivity.this);
                        recyclerView.setAdapter(adaptor);
                    }
                }
            });
    }

    public static int compare (final double a, final double b) {
        return equals(a, b) ? 0 : (a < b) ? -1 : +1;
    }
    public static boolean equals (final double a, final double b) {
        return Math.abs(a - b) <  0.000001d; //EPSILON
    }
    int setColor;
    void UpdateStates()
    { if(adaptor != null) {
        if (apilist != null && saved_livedatalist != null) {
            if (saved_livedatalist.size() > 0) ;
            {
                if (apilist.size() > 0) {
                    for (ObjectData item : saved_livedatalist) {
                        for (int i = 0; i < apilist.size(); i++) {
                            double price = apilist.get(i).price;
                            double preprice = item.price;
                            if (item.symbol.equals((apilist.get(i).symbol))) {
                                if (item.price != apilist.get(i).price) {

                                    if (price < preprice)
                                        setColor = Color.RED;

                                    if (price > preprice)
                                        setColor = Color.GREEN;

                                    adaptor.updatePriceColor(apilist.get(i), apilist.get(i).price, setColor);
                                }
                            }
                        }
                    }
                }
            }
        }
      }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
