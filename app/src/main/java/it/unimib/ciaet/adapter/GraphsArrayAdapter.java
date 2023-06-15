package it.unimib.ciaet.adapter;


import static it.unimib.ciaet.util.Constants.LOGO_GRAPHS_API_BASE_URL;
import static it.unimib.ciaet.util.Constants.LOGO_GRAPHS_FORMATO_API_BASE_URL;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;

import it.unimib.ciaet.R;
import it.unimib.ciaet.model.CriptoCurrency;

public class GraphsArrayAdapter extends RecyclerView.Adapter<GraphsArrayAdapter.ViewHolder>{

    private ArrayList<CriptoCurrency> currencyList;
    private Application application;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public GraphsArrayAdapter (ArrayList<CriptoCurrency> currencyList, Application application){
        this.currencyList = currencyList;
        this.application = application;
    }

    public void filterList(ArrayList<CriptoCurrency> arrayList){
        currencyList = arrayList;
        notifyDataSetChanged();
    }



    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.graphs_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(currencyList.get(position));

        CriptoCurrency currencyModel = currencyList.get(position);

//
//        holder.simbolo.setText(currencyModel.getSimbolo());
//        holder.prezzo.setText("$" + a);
//
//        holder.(LOGO_GRAPHS_API_BASE_URL + currencyModel.getId() + LOGO_GRAPHS_FORMATO_API_BASE_URL);

    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }






    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView simbolo, prezzo;
        private ImageView logo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            simbolo = itemView.findViewById(R.id.txt1);
            prezzo = itemView.findViewById(R.id.txt2);
            logo = itemView.findViewById(R.id.img1);
        }

        public void bind(CriptoCurrency currencies) {

            String a;
            if(df2.format(currencies.getPrezzo()).indexOf(",") != -1)
                a = String.format(df2.format(currencies.getPrezzo()));
            else
                a = String.format(df2.format(currencies.getPrezzo()) + ",00");

            simbolo.setText(currencies.getSimbolo());
            prezzo.setText("$" + a);
             logo = itemView.findViewById(R.id.img1);
             Glide.with(application).load(LOGO_GRAPHS_API_BASE_URL+currencies.getId()+".png").into(logo);
        }


    }




}


