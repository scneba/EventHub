package com.alpha.team.eventhub.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.utils.CustomRecyclerOnClick;
import com.alpha.team.eventhub.utils.HomeViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by clasence on 29,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Recycler adapter for event recyclerview
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private ArrayList<Event> eventList;
    private Context context;
    private CustomRecyclerOnClick customRecyclerOnClick=null;

    public HomeRecyclerAdapter(ArrayList<Event> eventList, Context context, CustomRecyclerOnClick customRecyclerOnClick) {
        this.eventList = eventList;
        this.context = context;
        this.customRecyclerOnClick = customRecyclerOnClick;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_main_helper, viewGroup, false);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder,final int position) {
        final Event event = eventList.get(position);
        holder.tvEventName.setText(event.getEventName());
        holder.tvLocation.setText(event.getAddress());
        holder.tvDescription.setText(event.getDescription());
        holder.tvLike.setText(String.valueOf(event.getLikeCount()));



        if(event.getPicture()!=null &&event.getPicture().length()>10){

            String path = RetrofitClientInstance.BASE_URL + event.getPicture();
            Picasso.with(context).load(path).fit()
                    .error(android.R.drawable.ic_menu_slideshow)
                    .placeholder(android.R.drawable.ic_menu_slideshow)
                    .into(holder.thumbnail);

        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customRecyclerOnClick!=null){
                    customRecyclerOnClick.onCustomClick(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
