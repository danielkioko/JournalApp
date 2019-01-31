package com.danielkioko.peachnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toolbar;

import com.danielkioko.peachnotes.Accounts.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyNotes extends AppCompatActivity {
    private ListView obj;
    NDb mydb;
    ImageButton btnadd;

    Menu menu;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    Context context = this;
    CoordinatorLayout coordinatorLayout;
    SimpleCursorAdapter adapter;

    Snackbar snackbar;
    Toolbar toolbar;

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notedisplay);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        recyclerView = findViewById(R.id.noteRV);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Notes");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(MyNotes.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };

        btnadd = findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyNotes.this, DisplayNote.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Noter, NoteViewHolder> FBRA = new FirebaseRecyclerAdapter<Noter, NoteViewHolder>(
                Noter.class,
                R.layout.listtemplate,
                NoteViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(NoteViewHolder viewHolder, Noter model, int position) {

                final String noteID = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setTitle(model.getTime());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleActivity = new Intent(MyNotes.this, DisplayNote.class);
                        singleActivity.putExtra("noteID", noteID);
                        startActivity(singleActivity);
                    }
                });

            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        View view;

        public NoteViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title) {
            TextView noteTitle = view.findViewById(R.id.txtnamerow);
            noteTitle.setText(title);
        }

        public void setTime(String time) {
            TextView noteTime = view.findViewById(R.id.txtdate);
            noteTime.setText(time);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.profile:
                Intent toProfile = new Intent(getApplicationContext(),
                        ProfileSettings.class);
                startActivity(toProfile);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}