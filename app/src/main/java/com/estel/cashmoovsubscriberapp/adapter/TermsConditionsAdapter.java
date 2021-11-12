package com.estel.cashmoovsubscriberapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.model.TermsConditionsModel;

public class TermsConditionsAdapter extends RecyclerView.Adapter<TermsConditionsAdapter.ViewHolder>{
    private Context context;
    private TermsConditionsModel[]termsConditionsList;

    public TermsConditionsAdapter(Context context, TermsConditionsModel[]termsConditionsList) {
        this.context = context;
        this.termsConditionsList = termsConditionsList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_terms_conditions, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TermsConditionsModel termsConditionsModel = termsConditionsList[position];
            holder.txt_title.setText(termsConditionsModel.getTitle());
            holder.txt_description.setText(termsConditionsModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return termsConditionsList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title,txt_description;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_description = itemView.findViewById(R.id.txt_description);
        }
    }
}