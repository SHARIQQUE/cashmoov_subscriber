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
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH > {

    private Context context;
    private ArrayList<OfferPromotionModel> mSliderItems = new ArrayList<>();

    public SliderAdapterExample(Context context) {
        this.context = context;
    }

    public void renewItems(Context context,ArrayList<OfferPromotionModel> sliderItems) {
        this.context = context;
        this.mSliderItems = sliderItems;
        MyApplication.offerPromotionModelArrayList=sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(OfferPromotionModel sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer_promotion, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH holder, final int position) {

         final OfferPromotionModel offerPromotionModel = mSliderItems.get(position);

        if(offerPromotionModel.getPromOfferTypeName().equalsIgnoreCase("Both")||
                offerPromotionModel.getPromOfferTypeName().equalsIgnoreCase("Image")) {
            holder.tv_offer_name.setVisibility(View.GONE);
            holder.img_offer_logo.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.logo200x70b)
                            .error(R.drawable.logo200x70b))
                    .load(API.BASEURL + "ewallet/api/v1/promOfferTemplate/download/" + offerPromotionModel.getCode() + "/" + offerPromotionModel.getFileName())
                    .into(holder.img_offer_logo);

        }else {

            holder.img_offer_logo.setVisibility(View.GONE);
            holder.tv_offer_name.setVisibility(View.VISIBLE);
            holder.tv_offer_name.setText(offerPromotionModel.getServiceCategoryName());
            System.out.println("sjhjasj"+offerPromotionModel.getServiceCategoryName());
        }
        //holder.txt_title.setText(offerPromotionModel.getHeading());
        //holder.txt_time.setText(offerPromotionModel.getToDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.offerPromtionPos=position;
                Intent i = new Intent(context, OfferPromotionDetailActivity.class);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    public static class SliderAdapterVH  extends SliderViewAdapter.ViewHolder {
        private ImageView img_offer_logo;
        private TextView tv_offer_name,txt_title,txt_time;
        public SliderAdapterVH (View itemView) {
            super(itemView);
            img_offer_logo = itemView.findViewById(R.id.img_offer_logo);
            tv_offer_name = itemView.findViewById(R.id.tv_offer_name);
            // txt_title = itemView.findViewById(R.id.txt_title);
            // txt_time = itemView.findViewById(R.id.txt_time);
        }
    }

}