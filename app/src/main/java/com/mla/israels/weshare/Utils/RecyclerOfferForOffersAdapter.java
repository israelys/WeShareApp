package com.mla.israels.weshare.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mla.israels.weshare.DataObjects.Offer;
import com.mla.israels.weshare.R;

import java.util.List;

/**
 * Created by david on 26/07/2016.
 */
public class RecyclerOfferForOffersAdapter extends RecyclerView.Adapter<RecyclerOfferForOffersAdapter.RecyclerViewHolder>{
    List<Offer> arrayList;
    private Context context;


    public RecyclerOfferForOffersAdapter(List<Offer> arrayList)
    {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_in_user_offers_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Offer offer = arrayList.get(position);
        holder.Id = offer.Id;
        //holder.OfferName.setText(offer.User.Name);
        holder.Comment.setText(offer.Comment);
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
        TextView OfferName, Comment;

        public RecyclerViewHolder(View view)
        {
            super(view);
            OfferName = (TextView) view.findViewById(R.id.offer_name);
            Comment = (TextView) view.findViewById(R.id.comment);

        }
    }
}
