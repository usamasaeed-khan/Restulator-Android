package com.example.restulator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restulator.Models.Table;

public class TablesScreenAdapter extends RecyclerView.Adapter<TablesScreenAdapter.TablesViewHolder> {

    private Table[] mDataset;
    private Context context;

    TablesScreenAdapter(Context context, Table[] myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public TablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TablesViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TablesViewHolder holder, int position) {
        final Table table = mDataset[position];

        TextView capacityText = holder.itemView.findViewById(R.id.capacity_text);
        TextView tableNoText = holder.itemView.findViewById(R.id.table_number_text);
        ImageView tableImage = holder.itemView.findViewById(R.id.table_image);

        // Set capacity text
        String capacityString = "Capacity: " + table.getCapacity();
        capacityText.setText(capacityString);

        // Set table number
        String tableNoString = "Table No: " + table.getId();
        tableNoText.setText(tableNoString);

        switch(table.getStatusEnum()){

            // if the status is active, load reserved image.
            case ACTIVE:
                tableImage.setImageResource(R.drawable.reserved);
                break;

            // if the status is in-active, load unreserved image.
            case IN_ACTIVE:
                tableImage.setImageResource(R.drawable.unreserved);
                break;

            // else load default image.
            default:
                tableImage.setImageResource(R.drawable.ic_launcher_background);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context, "Table No: " + table.getId(),Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Status: " + table.getStatus(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, OrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("table_id", table.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    static class TablesViewHolder extends RecyclerView.ViewHolder{

        TablesViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

}


