package com.mla.israels.weshare.Utils;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mla.israels.weshare.DataObjects.Offer;
import com.mla.israels.weshare.DataObjects.Request;
import com.mla.israels.weshare.MainActivity;
import com.mla.israels.weshare.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by david on 26/07/2016.
 */
public class RecyclerAllRequestsAdapter extends RecyclerView.Adapter<RecyclerAllRequestsAdapter.RecyclerViewHolder>  implements View.OnClickListener{
    ArrayList<Integer> exPos = new ArrayList<>();
    List<Request> arrayList;
    Point point;
    private Context context;


    public RecyclerAllRequestsAdapter(Context current, List<Request> arrayList)
    {
        this.context = current;
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        recyclerViewHolder.itemView.setOnClickListener(this);
        recyclerViewHolder.itemView.setTag(recyclerViewHolder);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Request request = arrayList.get(position);
        holder.Title.setText(request.Title);
        holder.Summery.setText(request.Details);
        holder.Id = request.Id;



        if (request.UserId == ((MainActivity)context).currentUser.Id) {
            holder.btnDeleteRequest.setTag(R.id.request,request);
            holder.btnDeleteRequest.setTag(R.id.position ,holder.getPosition());
            holder.btnAddOffer.setVisibility(View.GONE);
            holder.btnDeleteRequest.setVisibility(View.VISIBLE);
        }
        else {
            boolean requestHasUserOffer = false;
            for (Offer offer:request.Offers) {
                if (offer.UserId == ((MainActivity) context).currentUser.Id) {
                    requestHasUserOffer = true;
                    break;
                }
            }

            if (requestHasUserOffer){
                holder.btnAddOffer.setEnabled(false);
                holder.btnAddOffer.setVisibility(View.VISIBLE);
                holder.btnDeleteRequest.setVisibility(View.GONE);
            }else {
                holder.btnAddOffer.setEnabled(true);
                holder.btnAddOffer.setTag(request.Id);
                holder.btnAddOffer.setVisibility(View.VISIBLE);
                holder.btnDeleteRequest.setVisibility(View.GONE);
            }
        }

        holder.Category.setText(context.getResources().getStringArray(R.array.JobsArray)[request.JobId - 1]);
        if (exPos.contains(position)){
            holder.FullDetails.setVisibility(View.VISIBLE);
            holder.Summery.setVisibility(View.GONE);
            holder.imageView.setImageResource(R.drawable.arrow_up);
            holder.FullSummery.setText(request.Details);
            holder.StartDate.setText(DateToString(request.StartDate));
            holder.EndDate.setText(DateToString(request.EndDate));


            try{
                int separateIndex = request.Location.lastIndexOf(";");
                String locationName = request.Location.substring(0, separateIndex);
                String latlong = request.Location.substring(separateIndex +1);
                String picUrl = "http://maps.googleapis.com/maps/api/staticmap?language=he&center=" + latlong + "&zoom=17&size=600x300&maptype=roadmap&format=png&markers=size:mid%7Ccolor:0x180df2%7Clabel:%7C"+ latlong;
                Picasso.with(context).load(picUrl)
                        .into(holder.LocationImg);

                holder.cvMap.setTag(R.id.lat_long, latlong);
                holder.cvMap.setTag(R.id.location_name, locationName);
                holder.LocationName.setText(locationName);
                holder.cvMap.setVisibility(View.VISIBLE);
            }catch (Exception e) {
                holder.cvMap.setVisibility(View.GONE);
            }
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

    public void requestAdded(){
        for (int index = 0; index < exPos.size(); ++index){
            exPos.set(index, exPos.get(index) + 1);
        }
    }

    public void collapseAllRequests(){
        exPos.clear();
    }

    public static String DateToString(String Original){
        //SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        //String dateFormatted = fmt.format()
        String year = Original.substring(0, 4);
        String month = Original.substring(5,7);
        String day = Original.substring(8,10);
        return day+"/"+month+"/"+year;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public static final String TITLE = "TITLE";
        public static final String SUMMERY = "SUMMERY";
        public static final String ID = "ID";

        int Id;
        TextView Title, Summery, FullSummery, LocationName, StartDate, EndDate;
        LinearLayout FullDetails;
        ImageView imageView;
        Button btnAddOffer;
        public TextView Category;
        LinearLayout btnDeleteRequest;
        ImageView LocationImg;
        CardView cvMap;

        public RecyclerViewHolder(View view)
        {
            super(view);
            Title = (TextView)view.findViewById(R.id.job_title);
            Summery = (TextView)view.findViewById(R.id.job_summary);
            FullSummery = (TextView)view.findViewById(R.id.full_job_summary);
            FullDetails = (LinearLayout)view.findViewById(R.id.flexible_area);
            imageView = (ImageView)view.findViewById(R.id.arrow);
            btnAddOffer = (Button) view.findViewById(R.id.btn_add_offer);
            btnDeleteRequest = (LinearLayout) view.findViewById(R.id.btn_delete_request);
            Category = (TextView) view.findViewById(R.id.category);
            LocationImg = (ImageView) view.findViewById(R.id.location_img);
            cvMap = (CardView) view.findViewById(R.id.map_card);
            LocationName = (TextView) view.findViewById(R.id.location_name);
            StartDate= (TextView) view.findViewById(R.id.start_date);
            EndDate= (TextView) view.findViewById(R.id.end_date);
        }
    }
}
