package com.example.worklist;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyviewHolder> {
    public static int a=10,b=1,noteid=1;
    public static String str="n";
    ArrayList<Model> dataholder;
    private onEditListener onEditorActionListener;
    private onDeleteListner onDeleteListner;


    public Adapter(ArrayList<Model> dataholder, onEditListener onEditorActionListener,onDeleteListner onDeleteListner) {
        this.dataholder = dataholder;
        this.onEditorActionListener = onEditorActionListener;
        this.onDeleteListner = onDeleteListner;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new MyviewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.dtitle.setText(dataholder.get(position).getTitle());
        holder.ddate.setText(dataholder.get(position).getDate());
        noteid=dataholder.get(position).getId();
        if (dataholder.get(position).getCheck() ==0)
        {
            holder.dbutton.setVisibility(View.GONE);
        }
        else{
            holder.dbutton.setVisibility(View.VISIBLE);
        }

        if (dataholder.get(position).getComplete() ==1){
            holder.chBox.setChecked(true);
            holder.dtitle.setPaintFlags(holder.dtitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.chBox.setChecked(false);
        }
        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=dataholder.get(position).getId();
                onEditorActionListener.oneditclick(dataholder.get(position));
                //Toast.makeText(view.getContext(), "Id ="+ dataholder.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.chBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Checked", Toast.LENGTH_SHORT).show();

                if (holder.chBox.isChecked()){
                    int b=dataholder.get(position).getId();
                    DatabaseManager dbManager = new DatabaseManager(view.getContext());
                    DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    String qry = "Update TASK SET Complete=1 where Id=" + b;
                    database.execSQL(qry);
                    ((MainActivity)view.getContext()).recreate();//recreate is used to recreate activity
//                    dbManager.updateCompleted(b,1);
                    //Toast.makeText(view.getContext(), "Id ="+ b +": Status updated to 1", Toast.LENGTH_SHORT).show();
                }
                else {
                    int b=dataholder.get(position).getId();
                    DatabaseManager dbManager = new DatabaseManager(view.getContext());
                    DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    String qry = "Update TASK SET Complete=0 where Id=" + b;
                    database.execSQL(qry);
                    ((MainActivity)view.getContext()).recreate();//recreate is used to recreate activity
                }


            }
        });
        holder.imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b=dataholder.get(position).getId();
                onDeleteListner.ondeleteclic(dataholder.get(position));
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder{
        TextView dtitle,ddate;
        ImageButton dbutton;
        ConstraintLayout cl;
        CheckBox chBox;
        RecyclerView recyclerView;
        ImageButton imageButton1;
        Button button,button2;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            dtitle = (TextView) itemView.findViewById(R.id.textView4);
            ddate = (TextView) itemView.findViewById(R.id.textView5);
            dbutton = (ImageButton) itemView.findViewById(R.id.imageButton2);
            cl=(ConstraintLayout) itemView.findViewById(R.id.constraintLayout);
            chBox = (CheckBox) itemView.findViewById(R.id.checkBox6);
            recyclerView =(RecyclerView) itemView.findViewById(R.id.recyclerView);
            imageButton1=(ImageButton) itemView.findViewById(R.id.imageButton);
        }
    }
    public interface onEditListener{
        void oneditclick(Model listCurrentData);
    }
    public interface onDeleteListner{
        void ondeleteclic(Model listCurrentData);
    }




}

