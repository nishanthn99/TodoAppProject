package com.example.myfirstapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Activity> localDataSet;
    private final Context cont;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView,textViewtime,textViewdate;
        private Button edit,delete;
        private int pos;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            edit= view.findViewById(R.id.edit);
            delete=view.findViewById(R.id.delete);
            textView = view.findViewById(R.id.textView);
            textViewtime =  view.findViewById(R.id.textViewtime);
            textViewdate=view.findViewById(R.id.textViewdate);
        }
    }

    public RecyclerViewAdapter(Context applicationContext, ArrayList<Activity> dataSet) {
        localDataSet = dataSet;
        cont=applicationContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //MyDB db=new MyDB(viewHolder.itemView.getContext());
        //Activity obj=(Activity) localDataSet.get(position);
        //ArrayList<Activity> item=db.fetching();
        //for(int i=0;i<=localDataSet.size()-1;i++) {
            viewHolder.textView.setText(localDataSet.get(position).act);
            viewHolder.textViewtime.setText(localDataSet.get(position).time);
            viewHolder.textViewdate.setText(localDataSet.get(position).date);
        //}
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), getLayoutPosition()+" Edit button clicked", Toast.LENGTH_SHORT).show();
                Dialog dialog=new Dialog(view.getContext());
                dialog.setContentView(R.layout.update);
                Button reset,update;
                EditText upact;
                TextView uptime,updatedate;
                reset=dialog.findViewById(R.id.reset);
                //reset.setText("Reset");
                update=dialog.findViewById(R.id.update);
                //update.setText("Update");
                upact=dialog.findViewById(R.id.editText);
                updatedate=dialog.findViewById(R.id.editTextdate);
                upact.setText(((Activity) localDataSet.get(viewHolder.getLayoutPosition())).act);
                uptime=dialog.findViewById(R.id.editTextTime);
                //uptime.setText(((Activity) localDataSet.get(viewHolder.getAdapterPosition())).time);
                uptime.setText(((Activity) localDataSet.get(viewHolder.getLayoutPosition())).time);
                updatedate.setText(((Activity) localDataSet.get(viewHolder.getLayoutPosition())).date);
                uptime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar=Calendar.getInstance();
                        TimePickerDialog timePickerDialog=new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                if(i<12)
                                    uptime.setText(i+":"+i1+"AM");
                                else
                                    uptime.setText(i-12+":"+i1+"PM");
                            }
                        },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false);
                        timePickerDialog.show();
                    }
                });
                updatedate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar=Calendar.getInstance();
                        DatePickerDialog datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                updatedate.setText(i2+"/"+i1+"/"+i);
                            }
                        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                        datePickerDialog.show();
                    }
                });
                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upact.setText(null);
                        uptime.setText(null);
                        updatedate.setText(null);
                    }
                });
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (upact.getText().toString().equals("") || uptime.getText().toString().equals("")) {
                            Toast.makeText(cont, "Please enter the Activity and Time", Toast.LENGTH_SHORT).show();
                        } else {
                            //localDataSet.set(viewHolder.getAdapterPosition(), new Activity(upact.getText().toString(), uptime.getText().toString()));
                            MyDB db=new MyDB(view.getContext());
                            //localDataSet.set(viewHolder.getAdapterPosition(),db.updateact(new Activity(upact.getText().toString(), uptime.getText().toString())));
                            Activity obj=new Activity();
                            obj.id=viewHolder.getLayoutPosition()+1;
                            obj.act=upact.getText().toString();
                            obj.time=uptime.getText().toString();
                            obj.date=updatedate.getText().toString();
                            db.updateact(obj);
                            localDataSet.set(viewHolder.getAdapterPosition(),obj);
                            notifyItemChanged(viewHolder.getAdapterPosition());

                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), " Delete button clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert=new AlertDialog.Builder(view.getContext())
                        .setIcon(R.drawable.baseline_delete_forever_24)
                        .setTitle("Deleting the activity?")
                        .setMessage("Are you sure to delete the Activity")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //localDataSet.remove(viewHolder.getLayoutPosition());
                                MyDB db=new MyDB(view.getContext());
                                db.deleteact(viewHolder.getLayoutPosition()+1);
//                                for(int ii=viewHolder.getLayoutPosition()+1;ii<=localDataSet.size()-1;ii++){
//                                    Activity obj=new Activity();
//                                    obj.id=ii;
//                                    obj.act=localDataSet.get(ii).act;
//                                    obj.time=localDataSet.get(ii).time;
//                                    db.updatedel(obj);
//                                }
                                notifyItemRemoved(viewHolder.getLayoutPosition());
                                localDataSet.remove(viewHolder.getLayoutPosition());
                                //m2.id=localDataSet.size()-1;
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alert.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
