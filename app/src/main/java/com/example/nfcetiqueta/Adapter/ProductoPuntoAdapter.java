package com.example.nfcetiqueta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductoPuntoAdapter extends RecyclerView.Adapter<ProductoPuntoAdapter.ViewHolder>{

    public List<LProductos> productosList;
    private static boolean[] checkedItems;
    private Context context;
    private static OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(LProductos item);
    }

    public ProductoPuntoAdapter(List<LProductos> productosList, boolean[] checkedItems, Context context , OnItemClickListener onItemClickListener) {
        this.productosList = productosList;
        this.checkedItems = checkedItems;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void clearSelections() {
        Arrays.fill(checkedItems, true);
        notifyDataSetChanged();
    }

    public void setSelections(List<String> selectedIds) {
        // Actualiza el estado de los ítems basado en selectedIds
        for (int i = 0; i < productosList.size(); i++) {
            checkedItems[i] = selectedIds.contains(productosList.get(i).getId());
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductoPuntoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_productopunto,parent,false);
        return new ProductoPuntoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoPuntoAdapter.ViewHolder holder, int position) {

        LProductos productos = productosList.get(position);

        holder.textView.setText(productosList.get(position).getDescripcion());
        holder.checkBox.setText(productosList.get(position).getId());
        holder.bind(productos, checkedItems[position]);

    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public List<LProductos> getSelectedProducts() {
        List<LProductos> selectedProducts = new ArrayList<>();
        for (int i = 0; i < productosList.size(); i++) {
            if (checkedItems[i]) {
                selectedProducts.add(productosList.get(i));
            }
        }
        return selectedProducts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(final LProductos producto, boolean isChecked) {
            textView.setText(producto.getDescripcion());
            checkBox.setChecked(isChecked);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedItems[getAdapterPosition()] = isChecked;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(producto);
                }
            });
        }
    }
}
