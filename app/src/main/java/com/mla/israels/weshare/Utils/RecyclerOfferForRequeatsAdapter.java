package com.mla.israels.weshare.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mla.israels.weshare.DataObjects.Offer;
import com.mla.israels.weshare.DataObjects.User;
import com.mla.israels.weshare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by david on 26/07/2016.
 */
public class RecyclerOfferForRequeatsAdapter extends RecyclerView.Adapter<RecyclerOfferForRequeatsAdapter.RecyclerViewHolder>{
    List<Offer> arrayList;
    private Context context;


    public RecyclerOfferForRequeatsAdapter(List<Offer> arrayList)
    {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_in_user_request_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Offer offer = arrayList.get(position);
        holder.Id = offer.Id;
        holder.OfferName.setText(offer.User.Name);
        //holder.OfferName.setTag(offer.User);
        holder.UserEmail.setText(offer.User.Email);
        holder.Comment.setText(offer.Comment);
        Picasso.with(context).load(offer.User.PictureUrl)
                .into(holder.pic);
        holder.pic.setTag(offer.User.LinkdinUserProfileUrl);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        int Id;
        TextView OfferName, Comment ,UserEmail;
        ImageView pic;

        public RecyclerViewHolder(View view)
        {
            super(view);
            OfferName = (TextView) view.findViewById(R.id.offer_name);
            Comment = (TextView) view.findViewById(R.id.comment);
            UserEmail = (TextView) view.findViewById(R.id.offer_email);
            pic = (ImageView) view.findViewById(R.id.user_pic);
        }
    }
}
