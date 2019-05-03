package com.danielkioko.peachnotes.Notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.danielkioko.peachnotes.DB.NDb;
import com.danielkioko.peachnotes.R;
import com.danielkioko.peachnotes.SettingsAndPreferences.SharedPref;

public class SearchListActivity extends AppCompatActivity {

    ListView listView;
    EditText etSearch;

    ListAdapter db;
    SimpleCursorAdapter adapter;

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_search_list);

        db = new ListAdapter(SearchListActivity.this, 1);
        db.open();

        listView = findViewById(R.id.searchListView);
        etSearch = findViewById(R.id.editTextListSearch);

        Cursor c = db.fetchAllData();

        String[] fieldNames = new String[]{
                NDb.name, NDb._id, NDb.dates, NDb.remark
        };

        int[] display = new int[]{
                R.id.txtnamerow, R.id.txtidrow,
                R.id.txtdate
        };

        adapter = new SimpleCursorAdapter(this, R.layout.search_list_template, c, fieldNames,
                display, 0);
        listView.setScrollBarSize(0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                ConstraintLayout constraintLayoutParent = (ConstraintLayout) arg1;
                TextView m = (TextView) constraintLayoutParent.getChildAt(0);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", Integer.parseInt(m.getText().toString()));
                Intent intent = new Intent(getApplicationContext(),
                        DisplayNote.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {
                adapter.getFilter().filter(stringVar.toString());
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return db.fetchdatabyfilter(constraint.toString(), "name");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
