package com.danielkioko.peachnotes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ListTemplateClass extends AppCompatActivity{

    TextView noteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listtemplate);

        noteTitle =findViewById(R.id.txtnamerow);
        Typeface typefaceTitle = Typeface.createFromAsset(getAssets(), "JosefinSans-Bold.ttf");
        noteTitle.setTypeface(typefaceTitle);

    }
}
