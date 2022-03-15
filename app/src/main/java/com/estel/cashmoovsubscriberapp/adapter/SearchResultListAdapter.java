package com.estel.cashmoovsubscriberapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.listners.LocationListLisners;
import com.estel.cashmoovsubscriberapp.model.LatLongModel;
import java.util.ArrayList;
import java.util.List;

public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.ViewHolder>{
    private Context context;
    private List<LatLongModel> locationArrayList = new ArrayList<>();
    private LocationListLisners loactionListLisners;

    public SearchResultListAdapter(Context context, List<LatLongModel> locationArrayList) {
        this.context = context;
        this.locationArrayList = locationArrayList;
        loactionListLisners = (LocationListLisners) context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_search_result, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final LatLongModel latLongModel = locationArrayList.get(position);

        holder.tvName.setText(latLongModel.getName());
        holder.tvOutletName.setText(latLongModel.getOutlateName());
        holder.tvMsisdn.setText(latLongModel.getMssisdn());
        holder.tvAddress.setText(latLongModel.getAddress());

        holder.linLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    loactionListLisners.onLocationListViewItemClick(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return locationArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linLocation;
        private TextView tvName,tvOutletName,tvMsisdn,tvAddress;
        public ViewHolder(View itemView) {
            super(itemView);
            linLocation = itemView.findViewById(R.id.linLocation);
            tvName = itemView.findViewById(R.id.tvName);
            tvOutletName = itemView.findViewById(R.id.tvOutletName);
            tvMsisdn = itemView.findViewById(R.id.tvMsisdn);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }
}