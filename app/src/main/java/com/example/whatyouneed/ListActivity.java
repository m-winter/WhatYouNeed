package com.example.whatyouneed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.whatyouneed.data.DatabaseHandler;
import com.example.whatyouneed.model.Item;
import com.example.whatyouneed.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText enterItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);


        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        itemList = new ArrayList<>();


        itemList = databaseHandler.getAllItems();



        for (Item item : itemList){

            Log.d("added", "onCreate: " + item.getItemName());
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });

    }

    private void createPopDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null );

        enterItem = view.findViewById(R.id.addedItem);
        itemColor = view.findViewById(R.id.itemColor);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemSize = view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);


        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!enterItem.getText().toString().isEmpty()
                        && ! itemColor.getText().toString().isEmpty()
                        && ! itemQuantity.getText().toString().isEmpty()
                        && ! itemSize.getText().toString().isEmpty())
                {

                    saveItem(v);
                    Snackbar.make(v, "Item saved", Snackbar.LENGTH_SHORT).show();
                }

                else{
                    Snackbar.make(v, "Empty item", Snackbar.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void saveItem(View v) {

        Item item = new Item();

        String newItem = enterItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());


        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(quantity);
        item.setItemSize(size);


        databaseHandler.addItem(item);

        Snackbar.make(v, "Item Saved", Snackbar.LENGTH_SHORT).show();


        //go to next screen - close popup

        new Handler().postDelayed(new Runnable() {// delayed dismiss
            @Override
            public void run() {
                alertDialog.dismiss();

                startActivity(new Intent(ListActivity.this, ListActivity.class));

            }
        }, 1200);
    }


}