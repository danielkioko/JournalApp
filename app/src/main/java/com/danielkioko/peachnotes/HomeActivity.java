package com.danielkioko.peachnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.danielkioko.peachnotes.DB.NDb;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NDb mydb;
    ImageButton btnadd;
    ListView mylist;
    Menu menu;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    Context context = this;
    CoordinatorLayout coordinatorLayout;
    SimpleCursorAdapter adapter;
    Snackbar snackbar;
    private ListView obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        mydb = new NDb(this);

        btnadd = findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(getApplicationContext(),
                        DisplayNote.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();
            }
        });

        Cursor c = mydb.fetchAll();
        String[] fieldNames = new String[]{NDb.name, NDb._id, NDb.dates, NDb.remark};
        int[] display = new int[]{R.id.txtnamerow, R.id.txtidrow,
                R.id.txtdate};
        adapter = new SimpleCursorAdapter(this, R.layout.list_template, c, fieldNames,
                display, 0);

        mylist = findViewById(R.id.noteLV);
        mylist.setAdapter(adapter);

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

//                ConstraintLayout constraintLayoutParent = (ConstraintLayout) arg1;
//                ConstraintLayout constraintLayoutChild = (ConstraintLayout) constraintLayoutParent
//                        .getChildAt(0);
////
////                LinearLayout linearLayoutParent = (LinearLayout) arg1;
////                LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent
////                        .getChildAt(0);
//
//               CardView cardView = (CardView) constraintLayoutParent.getChildAt(0);

                ConstraintLayout constraintLayoutParent = (ConstraintLayout) arg1;
                CardView cardView = (CardView) constraintLayoutParent.getChildAt(1);
                ConstraintLayout constraintLayoutChild = (ConstraintLayout)
                        cardView.getChildAt(0);

                TextView m = (TextView) constraintLayoutChild.getChildAt(1);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", Integer.parseInt(m.getText().toString()));
                Intent intent = new Intent(getApplicationContext(),
                        DisplayNote.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_profile) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            Context context = getBaseContext();
            if (context != HomeActivity.this) {
                startActivity(new Intent(this, HomeActivity.class));
            } else {
            }
        } else if (id == R.id.nav_data) {

        } else if (id == R.id.nav_security) {
            startActivity(new Intent(this, FingerprintAuthenticationActivity.class));
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
