package com.example.saru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.saru.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity
{
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList=findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef,AdminOrders.class)
                        .build();
        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder>adapter=
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, final int i, @NonNull final AdminOrders adminOrders)
                    {
                     adminOrdersViewHolder.userName.setText("Name: "+adminOrders.getName());
                     adminOrdersViewHolder.userPhoneNumber.setText("Phone: "+adminOrders.getPhone());
                     adminOrdersViewHolder.userTotalPrice.setText("Total Amount: "+adminOrders.getTotalAmunt());
                     adminOrdersViewHolder.userDateTime.setText("Order at: "+adminOrders.getDate()+ " "+adminOrders.getTime());
                     adminOrdersViewHolder.userShippingAddress.setText("Address: "+adminOrders.getAddress()+","+adminOrders.getCity());
                     adminOrdersViewHolder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view)
                         {
                             String uID=getRef(i).getKey();
                             Intent intent=new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                             intent.putExtra("uid",uID);
                             startActivity(intent);
                         }
                     });
adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
     CharSequence options[]=new CharSequence[]
             {
             "Yes",
                     "No"
     };
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);
        builder.setTitle("Have you shipped this product?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
               if(i==0)
               {
                   String uID=getRef(i).getKey();
                   RemoveOrder(uID);
               }
               else {
                   finish();
               }
            }
        });
        builder.show();
    }
});

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                       return new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button ShowOrdersBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.order_user_name);
            userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address_city);
           ShowOrdersBtn=itemView.findViewById(R.id.show_all_products_btn);


        }
    }
    private void RemoveOrder(String uID)
    {
        ordersRef.child(uID).removeValue();
    }
}
