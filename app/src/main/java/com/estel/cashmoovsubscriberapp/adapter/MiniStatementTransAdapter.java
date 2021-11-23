package com.estel.cashmoovsubscriberapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.listners.MiniStatemetListners;
import com.estel.cashmoovsubscriberapp.model.MiniStatementTrans;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MiniStatementTransAdapter extends RecyclerView.Adapter<MiniStatementTransAdapter.ViewHolder>{
    private Context context;
    private List<MiniStatementTrans> miniStatementTransList = new ArrayList<>();
    private MiniStatemetListners miniStatemetListners;

    public MiniStatementTransAdapter(Context context, List<MiniStatementTrans> miniStatementTransList) {
        this.context = context;
        this.miniStatementTransList = miniStatementTransList;
        miniStatemetListners = (MiniStatemetListners) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_wallet, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        final MiniStatementTrans miniStatementTrans = miniStatementTransList.get(position);
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Cash-in")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashin);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Cash-out")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashout);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Remit Send")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Wallet Transfer")){
            holder.imgLogo.setImageResource(R.drawable.ic_lewallet);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Airtime Purchase")){
            holder.imgLogo.setImageResource(R.drawable.ic_rechargement);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Remit Send Reversal")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Recharge & Payment")){
            holder.imgLogo.setImageResource(R.drawable.ic_baseline_payment_24);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Cash PickUp")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashpickup24);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Pay")){
            holder.imgLogo.setImageResource(R.drawable.ic_paimentfacture);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Withdrawal")){
            holder.imgLogo.setImageResource(R.drawable.ic_paymane24);
        }

        holder.tvTransType.setText(miniStatementTrans.getTransactionTypeName());
        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
        if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
            holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));
            holder.tvAmount.setText(df.format(miniStatementTrans.getFromAmount())+" "+miniStatementTrans.getFromCurrencySymbol());
        }
        if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
            holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
            holder.tvAmount.setText(df.format(miniStatementTrans.getToAmount())+" "+miniStatementTrans.getToCurrencySymbol());

        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            date = inputFormat.parse(miniStatementTrans.getCreationDate());
            String formattedDate = outputFormat.format(date);
            holder.tvCreationDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.linItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(miniStatementTrans.getCode()!=null)


                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                        miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getFromCurrencySymbol(),
                                miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
                                miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus());
                    }
                if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                    miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                            miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToCurrencySymbol(),
                            miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
                            miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus());
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return miniStatementTransList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linItem;
        private ImageView imgLogo;
        private TextView tvTransType,tvMsisdn,tvAmount,tvCreationDate;
        public ViewHolder(View itemView) {
            super(itemView);
            linItem = itemView.findViewById(R.id.linItem);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            tvTransType = itemView.findViewById(R.id.tvTransType);
            tvMsisdn = itemView.findViewById(R.id.tvMsisdn);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCreationDate = itemView.findViewById(R.id.tvCreationDate);
        }
    }
}