package com.example.myfirstapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class TodoFragment extends Fragment {
    View view;
    private RecyclerView cycleview;
    private EditText activity;
    private TextView timepicked,datepicked;
    private Button add;
    private Context context;
    public int id;
    private ArrayList<Activity> dataset=new ArrayList();

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_todo, container, false);
        activity=view.findViewById(R.id.activitytext);
        timepicked=view.findViewById(R.id.timepicked);
        datepicked=view.findViewById(R.id.datepicked);
        add=view.findViewById(R.id.add);
        cycleview=view.findViewById(R.id.recyclerView);
        MyDB db=new MyDB(getContext());
        dataset=db.fetching();
        this.id=dataset.size()+1;
        RecyclerViewAdapter ad = new RecyclerViewAdapter(getContext(),dataset);
        cycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        //Log.d("COUNT", cycleview.getLayerType()+"onCreate: ");
        cycleview.setAdapter(ad);
        timepicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerdialog(getContext());
            }
        });
        datepicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepickerdialog(getContext());
            }
        });
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(activity.getText().toString().equals("") || timepicked.getText().toString().equals("")||datepicked.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please enter the Activity and Time and date", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Activity is added", Toast.LENGTH_SHORT).show();
                    String tim=timepicked.getText().toString();
                    String act=activity.getText().toString();
                    String date=datepicked.getText().toString();
                    //ArrayList<Activity> dataset=db.fetching();
                    //int id=dataset.size()+1;
                    //dataset.add(new Activity(act,tim));
                    MyDB db=new MyDB(view.getContext());
                    db.insertdb(id,act,tim,date);
                    dataset = db.fetching();
                    RecyclerViewAdapter ad = new RecyclerViewAdapter(getContext(),dataset);
                    cycleview.setLayoutManager(new LinearLayoutManager(getContext()));
                    cycleview.setAdapter(ad);
                    ad.notifyItemChanged(dataset.size() - 1);
                    ad.notifyDataSetChanged();
                    cycleview.scrollToPosition(dataset.size() - 1);
                    timepicked.setText(null);
                    activity.setText(null);
                    datepicked.setText(null);
                    id=dataset.size()+1;
                }
            }
        });

        return view;
    }
    public void timepickerdialog(Context context){
        Calendar calendar=Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if(i<12)
                    timepicked.setText(i+":"+i1+"AM");
                else
                    timepicked.setText(i-12+":"+i1+"PM");
            }
        },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
     public void datepickerdialog(Context context){
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datepicked.setText(i2+"/"+i1+"/"+i);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        datePickerDialog.show();
    }
}