package com.example.nfcetiqueta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.R;

import java.util.List;

public class LProductosAdapter  extends RecyclerView.Adapter<LProductosAdapter.ViewHolder>{

    public List<LProductos> lProductosList;
    private Context context;

    public interface  OnItemClickListener{
        void onItemClick(LProductos item);
    }

    final LProductosAdapter.OnItemClickListener listener;

    public LProductosAdapter(List<LProductos> lProductosList, Context context, LProductosAdapter.OnItemClickListener listener){
        this.lProductosList = lProductosList;
        this.context    = context;
        this.listener    = listener;

    }

    @NonNull
    @Override
    public LProductosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_producto,parent,false);
        return new LProductosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LProductosAdapter.ViewHolder holder, int position) {

        LProductos productos = lProductosList.get(position);
        holder.descripcion_prosucto.setText(lProductosList.get(position).getDescripcion());
        holder.codigo_producto.setText(lProductosList.get(position).getId());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(productos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lProductosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        private TextView descripcion_prosucto;
        private TextView codigo_producto;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView                = itemView.findViewById(R.id.cardProducto);
            descripcion_prosucto    = itemView.findViewById(R.id.descripcionProducto);
            codigo_producto         = itemView.findViewById(R.id.codigoProducto);
        }
    }
}
