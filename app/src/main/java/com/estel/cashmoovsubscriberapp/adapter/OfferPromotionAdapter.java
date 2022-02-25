package com.estel.cashmoovsubscriberapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.activity.OfferPromotionDetailActivity;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.model.OfferPromotionModel;
import java.util.ArrayList;

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
        View listItem= layoutInflater.inflate(R.layout.item_offer_promotion_slider, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OfferPromotionModel offerPromotionModel = offerPromotionList.get(position);

        if(offerPromotionModel.getPromOfferTypeName().equalsIgnoreCase("Both")||
                offerPromotionModel.getPromOfferTypeName().equalsIgnoreCase("Image")) {
            holder.cardPromotionText.setVisibility(View.GONE);
            holder.cardPromotionImage.setVisibility(View.VISIBLE);
            holder.tvHeading.setText(offerPromotionModel.getHeading());
          //  holder.tvSubheading.setText(offerPromotionModel.getSubHeading());
           // holder.tvDescription.setText(offerPromotionModel.getDescription());
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.logo200x70b)
                            .error(R.drawable.logo200x70b))
                    .load(API.BASEURL + "ewallet/api/v1/promOfferTemplate/download/" + offerPromotionModel.getCode() + "/" + offerPromotionModel.getFileName())
                    .into(holder.img_offer_logo);

        }else {
            holder.cardPromotionText.setVisibility(View.VISIBLE);
            holder.cardPromotionImage.setVisibility(View.GONE);
            holder.tvHeadingText.setText(offerPromotionModel.getHeading());
           // holder.tvSubheading.setText(offerPromotionModel.getSubHeading());
            //holder.tvDescription.setText(offerPromotionModel.getDescription());
        }

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
        private CardView cardPromotionImage,cardPromotionText;
        private ImageView img_offer_logo;
        private TextView tvHeading,tvHeadingText,tvSubheading,tvDescription;
        public ViewHolder(View itemView) {
            super(itemView);
            cardPromotionImage = itemView.findViewById(R.id.cardPromotionImage);
            cardPromotionText = itemView.findViewById(R.id.cardPromotionText);
            img_offer_logo = itemView.findViewById(R.id.img_offer_logo);
            tvHeading = itemView.findViewById(R.id.tv_offer_name);
            tvHeadingText = itemView.findViewById(R.id.tv_offer_name_text);
           // tvSubheading = itemView.findViewById(R.id.tvSubheading);
            //tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}