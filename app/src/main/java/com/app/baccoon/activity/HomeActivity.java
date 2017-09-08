package com.app.baccoon.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.adapter.DrawerItemAdapter;
import com.app.baccoon.adapter.MessageAdapter;
import com.app.baccoon.adapter.NotificationListAdapter;
import com.app.baccoon.adapter.RecyclerItemClickListener;
import com.app.baccoon.bean.DrawerItem;
import com.app.baccoon.bean.FilterBean;
import com.app.baccoon.fragment.FavoriteFragment;
import com.app.baccoon.fragment.HomeFragment;
import com.app.baccoon.fragment.SearchFragment;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int REQUEST_CODE_FOR_CREATE_SELL = 16;
    private Button btnLogout;
    private SharedPreference sharedPreference;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    ActionBarDrawerToggle drawerToggle;
    View headerLayout;
    private RecyclerView recList;
    ArrayList<DrawerItem> drawerItems;
    String []itemsTitle;
    DrawerItemAdapter ad;
   public static String Lt,Ln;
    private ImageView iconFilter;
   public static boolean searchResult=false;
    private FilterBean filterBean;
    private SharedPreferences prefs;
    Place place;
// private ImageView cross;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Lt=getIntent().getStringExtra("LAT");
        Ln=getIntent().getStringExtra("LNG");
        Log.e("lat",""+Lt);
        Log.e("lng", "" + Ln);

        sharedPreference=SharedPreference.getInstance(this);
        sharedPreference.putString("FIRST_TIME", "False");
        filterBean=FilterBean.getInstance();

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);



      iconFilter = (ImageView) findViewById(R.id.filter);
        iconFilter.setOnClickListener(this);
        toolbar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">Baccoon</font>"));
        toolbar.setTitleTextColor(Color.WHITE);
        // Inflate the header view at runtime
       // headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);

        recList = (RecyclerView) findViewById(R.id.drawer_slidermenu);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        itemsTitle=getResources().getStringArray(R.array.drawer_items);




        drawerItems= new  ArrayList<DrawerItem>();
        for(int i=0;i<itemsTitle.length;i++){
            drawerItems.add(new DrawerItem(itemsTitle[i],false));
        }

    ad= new DrawerItemAdapter(drawerItems);
        recList.setAdapter(ad);
// We can now look up items within the header if needed
       // ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.);
        // Setup drawer view
        selectDrawerItem(0);
        setupDrawerContent();



   }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }


    private void setupDrawerContent() {

        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("pos", "" + position);
                        selectDrawerItem(position);
                    }
                })
        );

    }


    public void selectDrawerItem(int position) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        Fragment currentFragment=null;
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.flContent);

        switch (position) {
            case 0:

                 findViewById(R.id.filter).setVisibility(View.GONE);
              if(!(currentFragment instanceof HomeFragment)) {
                  fragment = new HomeFragment();
                  title = "Baccoon";
              }

              //  mToolbar.setTitle("GENERATE NEW ORDER");

                break;
            case 1:
              if(Common.isConnected(this))
              {

                  startActivity(new Intent(HomeActivity.this,MyBaccoonActivity.class));
              }
                break;
            case 2:

                startActivity(new Intent(HomeActivity.this, CreateSellActivity.class));

                break;
            case 3:
                if(!(currentFragment instanceof FavoriteFragment) && Common.isConnected(this))
                {
                    fragment = new FavoriteFragment();
                    title = "Favourites";
                }
                break;
            case 4:


                startActivity(new Intent(HomeActivity.this, MessageActivity.class));
                break;
            case 5:
                startActivity(new Intent(HomeActivity.this, MyProductActivity.class));
                // fragment = new FavouriteFragment();
                //title = getString(R.string.title_messages);
                break;

            case 6:
                startActivity(new Intent(HomeActivity.this, NotificationListActivity.class));

                break;
            case 7:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));

                break;
            case 8:
                startActivity(new Intent(HomeActivity.this, HelpActivity.class));

                break;
            case 9:
               if(Common.isConnected(this))
               {logout();}
                break;

        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment).addToBackStack("me");
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
            itemSelected(position);

        }

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item has been done by NavigationView
        // menuItem.setChecked(true);
       // setTitle(itemsTitle[position]);
        mDrawer.closeDrawers();

}


    private void logout() {


        sharedPreference.deletePreference();
        sharedPreference.putString("FIRST_TIME", "False");
        Intent intent =new Intent(HomeActivity.this, GetLocationActivity.class);
        startActivity(intent);
        finish();

    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000)
        {

            Fragment frg=getSupportFragmentManager().findFragmentById(R.id.flContent);
            if(resultCode==RESULT_OK) {
                Intent i = getIntent();
                Bundle bundle=new Bundle();

                if(data.hasExtra("defaultLocation") && data.getBooleanExtra("defaultLocation",false)) {
                    if (frg instanceof SearchFragment) {
                        getSupportFragmentManager().popBackStack();
                    }

                }else {


                        if (frg instanceof HomeFragment) {
                            Log.e("On activity result", "Activity Home calling sample Fragment.");
                            ((HomeFragment) frg).onActivityResult(requestCode, resultCode, data);
                        } else if (frg instanceof SearchFragment) {
                            Log.e("On activity result", "Activity Home calling Home Fragment.");
                            ((SearchFragment) frg).onActivityResult(requestCode, resultCode, data);
                        }

//                bundle.putString("Latitude", i.getStringExtra("Latitude"));
//                bundle.putString("Longitude",i.getStringExtra("Longitude"));
//                bundle.putString("Category", i.getStringExtra("Category"));
//                bundle.putString("sortBy",i.getStringExtra("sortBy"));
//                bundle.putString("Radius", i.getStringExtra("Radius"));
//                frg.setArguments(bundle);
                    }

//            else if(resultCode==RESULT_CANCELED)
//            {
//                if(data.hasExtra("defaultLocation") && data.getBooleanExtra("defaultLocation",false)) {
//                    if (frg instanceof SearchFragment) {
//                        getSupportFragmentManager().popBackStack();
//                    }
//                }
//
            }


        }
    }

    @Override
    public void onClick(View v) {
     //   if(v.getId()==iconFilter.getId())

        Fragment fr=getSupportFragmentManager().findFragmentById(R.id.flContent);
        {

            if( getSupportFragmentManager().findFragmentById(R.id.flContent)  instanceof HomeFragment) {
                Intent i = new Intent(this, FilterActivity.class);
                startActivityForResult(i, 1000);

            }
            else  if( getSupportFragmentManager().findFragmentById(R.id.flContent)  instanceof FavoriteFragment) {
                Intent i = new Intent(this, FilterActivity.class);
                startActivityForResult(i, 1000);
              //  findViewById(R.id.filter).setVisibility(View.GONE);
            }
            else
            {
                Intent i = new Intent(this, FilterActivity.class);
                startActivityForResult(i, 1000);
               // Log.e("awara","Lawaris");
            }
        }

    }


    public void itemSelected(int position)
    {
        for(int i =0;i<drawerItems.size();i++)
        {
            if(position==i)
            {
                drawerItems.get(i).setIsSelected(true);
            }else
            {
                drawerItems.get(i).setIsSelected(false);
            }

        }

        ad.notifyDataSetChanged();

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.flContent);

        if (fr instanceof SearchFragment) {
            getSupportFragmentManager().popBackStack();
            filterBean.setValues(0, 0, 0, "", "", sharedPreference.getString(SpKey.Place, ""), sharedPreference.getString(SpKey.Lat, ""), sharedPreference.getString(SpKey.Lng, ""));
            itemSelected(0);
        }
        else if (fr instanceof FavoriteFragment &&((FavoriteFragment)fr).isFiltered) {

              //  if(((FavoriteFragment)fr).isFiltered)
                {
                    ((FavoriteFragment)fr).clearFilter();
                }
              /*  else {
                    getSupportFragmentManager().popBackStack();
                    itemSelected(0);
                    getSupportActionBar().setTitle("Baccoon");
                    iconFilter.setImageResource(R.mipmap.filter);
                }*/
            }
            else {
                if (doubleBackToExitPressedOnce) {
                    finish();
                    //super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

        }
    }
}
