package com.quantum.mq08.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quantum.mq08.R;
import com.quantum.mq08.entidades.Datos;
import com.quantum.mq08.mq08.VerActivity;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.DatosViewHolder> implements View.OnClickListener{

    ArrayList<Datos> listadatos;
    private View.OnClickListener listener;


    public Adaptador(ArrayList<Datos> listadatos) {
        this.listadatos = listadatos;
    }public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }
    //Aca se hace conexion con la vista en si
    @NonNull
    @Override
    public Adaptador.DatosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        view.setOnClickListener(this);
        return new DatosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.DatosViewHolder holder, int position) {
        final Datos datos = listadatos.get(position);
        holder.depositoAdaptador.setText(listadatos.get(position).getDeposito());
        holder.retencionAdaptador.setText(listadatos.get(position).getRetencion());
        holder.itemAdaptador.setText(listadatos.get(position).getItem());
        holder.loteAdaptador.setText(listadatos.get(position).getLote());
    }
//Aca recorre la lista
    @Override
    public int getItemCount() {
        return listadatos == null?0:listadatos.size();
    }

    //Aca se hacen las conexiones con la vista
    public class DatosViewHolder extends RecyclerView.ViewHolder {
        public TextView depositoAdaptador, retencionAdaptador,itemAdaptador, loteAdaptador;
        public View view;
        public DatosViewHolder(@NonNull View itemView) {
            super(itemView);

            depositoAdaptador = itemView.findViewById(R.id.depositoAdaptador);
            retencionAdaptador = itemView.findViewById(R.id.retencionAdaptador);
            itemAdaptador = itemView.findViewById(R.id.itemAdaptador);
            loteAdaptador = itemView.findViewById(R.id.loteAdaptador);

            this.view = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, VerActivity.class);
                    intent.putExtra("ID",listadatos.get(getAdapterPosition()).getId());

                    context.startActivity(intent);
                }
            });
        }
    }
}
