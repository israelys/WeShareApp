package com.mla.israels.weshare.Utils;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mla.israels.weshare.DataObjects.Offer;
import com.mla.israels.weshare.DataObjects.Request;
import com.mla.israels.weshare.DataObjects.User;
import com.mla.israels.weshare.MainActivity;
import com.mla.israels.weshare.R;
import com.mla.israels.weshare.communication.RestService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by david on 26/07/2016.
 */
public class RecyclerUserRequeatsAdapter extends RecyclerView.Adapter<RecyclerUserRequeatsAdapter.RecyclerViewHolder>  implements View.OnClickListener{
    ArrayList<Integer> exPos = new ArrayList<>();
    List<Request> arrayList;
    Point point;
    private Context context;

    public RecyclerUserRequeatsAdapter(Context current, List<Request> arrayList)
    {
        this.context = current;
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_request_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        recyclerViewHolder.itemView.setOnClickListener(this);
        recyclerViewHolder.itemView.setTag(recyclerViewHolder);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final Request request = arrayList.get(position);
        holder.Title.setText(request.Title);
        holder.Summery.setText(request.Details);
        holder.FullSummery.setText(request.Details);
        holder.Id = request.Id;
        holder.btnDeleteRequest.setTag(R.id.request, request);
        holder.btnDeleteRequest.setTag(R.id.position, holder.getPosition());
        holder.Category.setText(context.getResources().getStringArray(R.array.JobsArray)[request.JobId - 1]);

        if (request.Offers != null && request.Offers.length > 0){
            holder.OffersTitle.setVisibility(View.VISIBLE);
            RecyclerOfferForRequeatsAdapter recycler = new RecyclerOfferForRequeatsAdapter(Arrays.asList(request.Offers));
            holder.recyclerOffersView.setAdapter(recycler);
            holder.recyclerOffersView.setVisibility(View.VISIBLE);
        }else {
            holder.OffersTitle.setVisibility(View.GONE);
            holder.recyclerOffersView.setVisibility(View.GONE);
        }

        if (exPos.contains(position)){
            holder.FullDetails.setVisibility(View.VISIBLE);
            holder.Summery.setVisibility(View.GONE);
            holder.imageView.setImageResource(R.drawable.arrow_up);
            //holder.imageView.animate().setDuration(500).rotation(90);
        }
        else {
            holder.FullDetails.setVisibility(View.GONE);
            holder.Summery.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(R.drawable.arrow_right);
        }

        Log.e("Debug onBind", position + " position...");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onClick(View view) {
        RecyclerViewHolder holder = (RecyclerViewHolder) view.getTag();
        // Check for an expanded view, collapse if you find one
        /*if (ExtendPosition >= 0) {
            int prev = ExtendPosition;
            notifyItemChanged(prev);
        }*/
        //((View)holder.imageView).animate().setDuration(10).setDuration(90);
        if (holder.FullDetails.getVisibility() == View.GONE){
            exPos.add(holder.getPosition());
        }else {
            int pos = exPos.lastIndexOf(holder.getPosition());
            if (pos != -1)
            exPos.remove(pos);
        }
        // Set the current position to "expanded"
        int ExtendPosition = holder.getPosition();
        notifyItemChanged(ExtendPosition);

    }

    public void dismissJob(int position) {
        arrayList.remove(position);
        this.notifyItemRemoved(position);
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    public void closeFlexible(int pos){
        exPos.remove(exPos.indexOf(pos));
        for (int index = 0; index < exPos.size(); ++index)
            if (exPos.get(index) > pos)
                exPos.set(index, exPos.get(index) - 1);
    }

    public void collapseAllRequests(){
        exPos.clear();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public static final String TITLE = "TITLE";
        public static final String SUMMERY = "SUMMERY";
        public static final String ID = "ID";

        int Id;
        TextView Title, Summery, FullSummery, OffersTitle;
        LinearLayout FullDetails;
        ImageView imageView;
        LinearLayout btnDeleteRequest;
        public TextView Category;
        RecyclerView recyclerOffersView;

        public RecyclerViewHolder(View view)
        {
            super(view);
            Title = (TextView)view.findViewById(R.id.job_title);
            Summery = (TextView)view.findViewById(R.id.job_summary);
            FullSummery = (TextView)view.findViewById(R.id.full_job_summary);
            FullDetails = (LinearLayout)view.findViewById(R.id.flexible_area);
            imageView = (ImageView)view.findViewById(R.id.arrow);
            btnDeleteRequest = (LinearLayout) view.findViewById(R.id.btn_delete_request);
            Category = (TextView) view.findViewById(R.id.category);
            OffersTitle = (TextView) view.findViewById(R.id.offers_title);

            recyclerOffersView = (RecyclerView) view.findViewById(R.id.recycler_offers_view);
            LinearLayoutManager llManager = new LinearLayoutManager(view.getContext());
            llManager.setAutoMeasureEnabled(true);
            recyclerOffersView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        }
    }
}
