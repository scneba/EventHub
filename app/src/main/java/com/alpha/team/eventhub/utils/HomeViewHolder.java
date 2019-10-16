package com.alpha.team.eventhub.utils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alpha.team.eventhub.R;

/**
 * Created by clasence on 29,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */

public class HomeViewHolder extends RecyclerView.ViewHolder {

    public ImageView thumbnail;
    public View view;
    public TextView tvEventName;
    public TextView tvLocation;
    public TextView tvDescription;
    public TextView tvLike;
    public ConstraintLayout constraintLayout;

    public HomeViewHolder(View view) {
        super(view);
        this.view = view;
        tvEventName = view.findViewById(R.id.tv_eventname);
        tvLocation = view.findViewById(R.id.tv_location);
        tvDescription = view.findViewById(R.id.tv_description);
        tvLike = view.findViewById(R.id.tv_like);

        thumbnail = view.findViewById(R.id.imageView2);
        constraintLayout = view.findViewById(R.id.rlClick);

    }
}
