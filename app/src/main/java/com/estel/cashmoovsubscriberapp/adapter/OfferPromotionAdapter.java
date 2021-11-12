package com.estel.cashmoovsubscriberapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.OfferPromotionDetailActivity;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.model.OfferPromotionModel;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class OfferPromotionAdapter extends RecyclerView.Adapter<OfferPromotionAdapter.ViewHolder>{
    private Context context;
    private ArrayList<OfferPromotionModel> offerPromotionList;

    public OfferPromotionAdapter(Context context, ArrayList<OfferPromotionModel>offerPromotionList) {
        this.context = context;
        this.offerPromotionList = offerPromotionList;
        MyApplication.offerPromotionModelArrayList=offerPromotionList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_offer_promotion, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OfferPromotionModel offerPromotionModel = offerPromotionList.get(position);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.banner_three)
                            .error(R.drawable.banner_three))
                    .load(API.BASEURL+"ewallet/api/v1/promOfferTemplate/download/"+offerPromotionModel.getCode()+"/"+offerPromotionModel.getFileName())
                    .into(holder.img_offer_logo);

            holder.tv_offer_name.setText(offerPromotionModel.getServiceCategoryName());
            //holder.txt_title.setText(offerPromotionModel.getHeading());
            //holder.txt_time.setText(offerPromotionModel.getToDate());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.offerPromtionPos=holder.getAdapterPosition();
                    Intent i = new Intent(context, OfferPromotionDetailActivity.class);
                    context.startActivity(i);
                }
            });
    }


    @Override
    public int getItemCount() {
        return offerPromotionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_offer_logo;
        private TextView tv_offer_name,txt_title,txt_time;
        public ViewHolder(View itemView) {
            super(itemView);
            img_offer_logo = itemView.findViewById(R.id.img_offer_logo);
            tv_offer_name = itemView.findViewById(R.id.tv_offer_name);
           // txt_title = itemView.findViewById(R.id.txt_title);
           // txt_time = itemView.findViewById(R.id.txt_time);
        }
    }
}