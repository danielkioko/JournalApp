package com.danielkioko.peachnotes.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.danielkioko.peachnotes.R;

public class CustomDialogClass extends Dialog implements
        View.OnClickListener {

    Activity a;
    Dialog dialog;
    Button cancel;

    public CustomDialogClass(Activity activity) {
        super(activity);
        this.a = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fingerprint_dialog_box);

        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_cancel:
                a.finish();
                break;
            default:
                break;
        }
        dismiss();
    }

}
