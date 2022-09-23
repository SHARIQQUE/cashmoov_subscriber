package com.estel.cashmoovsubscriberapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    @NonNull
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

        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("100000")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashin);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("100001")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashout);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("101441")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("106449")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("104591")){
            holder.imgLogo.setImageResource(R.drawable.ic_lewallet);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("101777")){
            holder.imgLogo.setImageResource(R.drawable.ic_airtime);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("100005")){
            holder.imgLogo.setImageResource(R.drawable.ic_baseline_payment_24);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("106443")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashpickup24);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("105068")){
            holder.imgLogo.setImageResource(R.drawable.icon_merchant);
        }


        holder.tvTransType.setText(miniStatementTrans.getTransactionTypeName());

        if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
            holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));
            holder.tvAmount.setText(MyApplication.addDecimal(""+miniStatementTrans.getFromAmount())+" "+miniStatementTrans.getFromCurrencySymbol());
            holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
        }
        if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
            holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
            holder.tvAmount.setText(MyApplication.addDecimal(""+miniStatementTrans.getToAmount())+" "+miniStatementTrans.getToCurrencySymbol());
            holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
        }

        if(holder.tvMsisdn.getText().toString().isEmpty()){
            holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerName());
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

                    //: Cash To Wallet
                    if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("100000")
                            ||miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("101667")
                            ||miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("106449")){



                        if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                            miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                    miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getFromCurrencySymbol(),
                                    miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
                                    miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getComReceiveAmount(),
                                    miniStatementTrans.getTaxAsJson(),
                                    miniStatementTrans.getDestPostBalance(),miniStatementTrans.getFee());

                        }
                        if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                            miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                    miniStatementTrans.getFromWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
                                    miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
                                    miniStatementTrans.getCreationDate(),
                                    miniStatementTrans.getStatus(),
                                    miniStatementTrans.getComReceiveAmount(),
                                    miniStatementTrans.getTaxAsJson(),
                                    miniStatementTrans.getDestPostBalance(),miniStatementTrans.getFee());
                        }
                         return;
                    }



                //101441 Case Reversal and Wallet To Cash InterNational //101667 salry Apyment

                if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("101441")
                ){

                    Double PostBalance=0.00;
                    if(miniStatementTrans.isReverse()){
                        PostBalance=miniStatementTrans.getDestPostBalance();
                    }else{
                        PostBalance=miniStatementTrans.getSrcPostBalance();
                    }

                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                        miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getFromCurrencySymbol(),
                                miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
                                miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getComReceiveAmount(),
                                miniStatementTrans.getTaxAsJson(),PostBalance,miniStatementTrans.getFee());

                    }
                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                        miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                miniStatementTrans.getFromWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
                                miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
                                miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),
                                miniStatementTrans.getComReceiveAmount(),
                                miniStatementTrans.getTaxAsJson(),PostBalance,miniStatementTrans.getFee());
                    }
                    return;
                }




                if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                    miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                            miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getFromCurrencySymbol(),
                            miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),


                            miniStatementTrans.getCreationDate(),
                            miniStatementTrans.getStatus(),miniStatementTrans.getComReceiveAmount()
                            ,miniStatementTrans.getTaxAsJson(),miniStatementTrans.getSrcPostBalance()
                            ,miniStatementTrans.getFee());

                }
                if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                    miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                            miniStatementTrans.getFromWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
                            miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
                            miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),
                            miniStatementTrans.getComReceiveAmount(),miniStatementTrans.getTaxAsJson(),
                            miniStatementTrans.getSrcPostBalance(),miniStatementTrans.getFee());
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