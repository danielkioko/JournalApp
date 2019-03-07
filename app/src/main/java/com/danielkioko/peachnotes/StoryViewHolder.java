package com.danielkioko.peachnotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class StoryViewHolder extends RecyclerView.ViewHolder {

    View view;

    public StoryViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setHeading(String heading) {
        EditText editText = view.findViewById(R.id.et_heading);
        editText.setText(heading);
    }

    public void setStory(String story) {
        EditText editText = view.findViewById(R.id.et_story);
        editText.setText(story);
    }

    public void setImage(Context context, int image) {
        ImageView imageView = view.findViewById(R.id.img_story);
        Picasso.with(context).load(image).into(imageView);
    }

}
