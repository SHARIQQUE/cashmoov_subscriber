package com.estel.cashmoovsubscriberapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.listners.ProductListeners;
import com.estel.cashmoovsubscriberapp.model.ProductModel;
import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context context;
    private List<ProductModel> productList = new ArrayList<>();
    private ProductListeners productListeners;

    public ProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        productListeners = (ProductListeners) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductModel productModel = productList.get(position);
        holder.tvProductName.setText(productModel.getName());
        if(productModel.getOperatorCode().equalsIgnoreCase("100055")){
            holder.ivProductLogo.setImageResource(R.drawable.canalplus);
        }
        if(productModel.getOperatorCode().equalsIgnoreCase("100046")){
            holder.ivProductLogo.setImageResource(R.drawable.startimes);
        }
        holder.cardProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productModel.getCode()!=null)
                 productListeners.onProductListItemClick(productModel.getCode(),productModel.getName());
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardProduct;
        private ImageView ivProductLogo;
        private TextView tvProductName;
        public ViewHolder(View itemView) {
            super(itemView);
            cardProduct = itemView.findViewById(R.id.cardProduct);
            ivProductLogo = itemView.findViewById(R.id.ivProductLogo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
        }
    }
}