package com.example.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Data> {

    private Context context;
    private List<Data> data;

    public NotesAdapter ( Context context, List<Data> list){

        super(context,R.layout.rowlayout,list);
        this.context = context;
        this.data= list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.rowlayout,parent,false);

        TextView tvchar = convertView.findViewById(R.id.tvchar);
        TextView tvname = convertView.findViewById(R.id.tvname);
        TextView tvdate = convertView.findViewById(R.id.tvdate);

        tvchar.setText(data.get(position).getHeading().toUpperCase().charAt(0)+"");
        tvname.setText(data.get(position).getHeading());
        tvdate.setText("Created on " +data.get(position).getCreated().toString());

        return convertView;
    }


}
