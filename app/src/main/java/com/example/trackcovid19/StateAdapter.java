package com.example.trackcovid19;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class StateAdapter extends ArrayAdapter<StatesModel> {

    private Context context;
    private List<StatesModel> statesModels;

    public StateAdapter(Context context,  List<StatesModel> statesModels) {
        super(context, R.layout.list, statesModels);

        this.context = context;
        this.statesModels = statesModels;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list,null,true);

        TextView txtStateName = view.findViewById(R.id.stateName);
        TextView txtStateCode = view.findViewById(R.id.code);
        CardView cardView = view.findViewById(R.id.card);

        txtStateName.setText(statesModels.get(position).getState());
        txtStateCode.setText(statesModels.get(position).getStateCode());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),StateDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("state_name", txtStateName.getText());
                context.startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public int getCount() {
        return statesModels.size();
    }

    @Override
    public StatesModel getItem(int position) {
        return statesModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
