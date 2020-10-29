package com.example.whatyouneed.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;


import com.example.whatyouneed.R;
import com.example.whatyouneed.data.DatabaseHandler;
import com.example.whatyouneed.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;
import java.util.zip.Inflater;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {

        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Item item = itemList.get(position);
        viewHolder.itemName.setText(MessageFormat.format("Item: {0}", item.getItemName()));
        viewHolder.itemColor.setText(MessageFormat.format("Color: {0}", item.getItemColor()));
        viewHolder.itemQuantity.setText(MessageFormat.format("Qty: {0}", String.valueOf(item.getItemQuantity())));
        viewHolder.itemSize.setText(MessageFormat.format("Size:  {0}", String.valueOf(item.getItemSize())));
        viewHolder.dateItemAdded.setText(MessageFormat.format("Added on: {0}", item.getDateItemAdded()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemColor;
        public TextView itemQuantity;
        public TextView itemSize;
        public TextView dateItemAdded;
        public int id;
        public Button editButton;
        public Button deleteButton;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemName = itemView.findViewById(R.id.item_name_lr);
            itemColor = itemView.findViewById(R.id.color_lr);
            itemQuantity = itemView.findViewById(R.id.quantity_lr);
            itemSize = itemView.findViewById(R.id.size_lr);
            dateItemAdded = itemView.findViewById(R.id.date_lr);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);


        }


        public void onClick(View v) {

            int position = getAdapterPosition();
            Item item = itemList.get(position);

            switch (v.getId()) {
                case R.id.editButton:
                    editItem(item);

                    break;

                case R.id.deleteButton:
                    deleteItem(item.getId());
                    break;
            }


        }


        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.confirmation_popup, null);

            Button noButton = view.findViewById(R.id.no_button);
            Button yesButton = view.findViewById(R.id.yes_button);

            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();

                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    db.close();

                    alertDialog.dismiss();

                }
            });



        }
        public void editItem(final Item newItem) {


            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.popup, null);

            Button saveButton;
            final EditText itemName;
            final EditText itemQuantity;
            final EditText itemColor;
            final EditText itemSize;
            TextView title;

            itemName = view.findViewById(R.id.addedItem);
            itemColor = view.findViewById(R.id.itemColor);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemSize = view.findViewById(R.id.itemSize);
//            title = view.findViewById(R.id.title_text);
            saveButton = view.findViewById(R.id.saveButton);

//            title.setText(R.string.edit_text);


            saveButton.setText(R.string.update_text);

            itemName.setText(newItem.getItemName());
            itemQuantity.setText(String.valueOf(newItem.getItemQuantity()));
            itemColor.setText(newItem.getItemColor());
            itemSize.setText(String.valueOf(newItem.getItemSize()));


            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    newItem.setItemName(itemName.getText().toString().trim());
                    newItem.setItemSize(Integer.parseInt(itemSize.getText().toString().trim()));
                    newItem.setItemColor(itemColor.getText().toString().trim());
                    newItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString().trim()));


                    if (!itemName.getText().toString().isEmpty()
                            && !itemColor.getText().toString().isEmpty()
                            && !itemSize.getText().toString().isEmpty()
                            && !itemQuantity.getText().toString().isEmpty()){

                        databaseHandler.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(), newItem);
                    }else{
                        Snackbar.make(v, "Fields are empty ",
                                Snackbar.LENGTH_SHORT);
                    }

                    alertDialog.dismiss();

                }
            });



        }




    }




}