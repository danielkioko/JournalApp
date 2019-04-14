package com.danielkioko.peachnotes.Notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.danielkioko.peachnotes.Backup;
import com.danielkioko.peachnotes.DB.NDb;
import com.danielkioko.peachnotes.R;
import com.danielkioko.peachnotes.SettingsAndPreferences.Setting;
import com.danielkioko.peachnotes.SettingsAndPreferences.SharedPref;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NDb mydb;
    FloatingActionButton btnadd;

    ListView mylist;
    CoordinatorLayout coordinatorLayout;
    SimpleCursorAdapter adapter;
    private int id_to_update = 0;

    SharedPref sharedPref;
    Backup backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        backup = new Backup();
        backup.exportDB();

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.paperPenToolbarTitle);
        AssetManager assetManager = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, String.format(Locale.US, "fonts/%s", "Pacifico.ttf"));
        textView.setTypeface(typeface);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

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
        mylist.setScrollBarSize(0);

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

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

        mylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                ConstraintLayout constraintLayoutParent = (ConstraintLayout) arg1;
                CardView cardView = (CardView) constraintLayoutParent.getChildAt(1);
                ConstraintLayout constraintLayoutChild = (ConstraintLayout)
                        cardView.getChildAt(0);

                final TextView m = (TextView) constraintLayoutChild.getChildAt(1);

                final Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", Integer.parseInt(m.getText().toString()));
                final Intent intent = getIntent();
                intent.putExtras(dataBundle);
                startActivity(intent);

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Options")
                        .setPositiveButton("DELETE",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        AlertDialog.Builder deletion_builder = new AlertDialog.Builder(HomeActivity.this);
                                        deletion_builder.setMessage("Delete Note?")
                                                .setPositiveButton("YES",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                int Value = dataBundle.getInt("id");
                                                                id_to_update = Value;
                                                                mydb.deleteNotes(id_to_update);
                                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                                finish();
                                                            }
                                                        });

                                        AlertDialog alertDialog = deletion_builder.create();
                                        alertDialog.setTitle("Are you sure");
                                        alertDialog.show();
                                    }
                                })

                        .setNegativeButton("EDIT",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dataBundle.putInt("id", Integer.parseInt(m.getText().toString()));
                                        Intent intent = new Intent(getApplicationContext(),
                                                DisplayNote.class);
                                        intent.putExtras(dataBundle);
                                        startActivity(intent);

                                    }
                                });

                AlertDialog d = builder.create();
                d.setTitle("");
                d.show();

                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
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

        if (id == R.id.app_bar_search) {
            startActivity(new Intent(getApplicationContext(), SearchListActivity.class));
        } else if (id == R.id.nav_notes) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), Setting.class));
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
