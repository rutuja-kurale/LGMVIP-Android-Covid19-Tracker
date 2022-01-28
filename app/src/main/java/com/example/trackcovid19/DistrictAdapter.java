package com.example.trackcovid19;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.List;

public class DistrictAdapter extends ArrayAdapter<DistrictModel> {

    private Context context;
    private List<DistrictModel> districtModels;
    private List<DistrictModel> fullArrayList;

    public DistrictAdapter(Context context,  List<DistrictModel> districtModels) {
        super(context, R.layout.district_list, districtModels);

        this.context = context;
        this.districtModels = districtModels;
        this.fullArrayList = districtModels;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.district_list,null,true);

        TextView districtName = (TextView) view1.findViewById(R.id.districtName);
        CardView cardView = view1.findViewById(R.id.card_district);

        districtName.setText(districtModels.get(position).getDist_name());

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),StateDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("dist_name", districtName.getText());
                context.startActivity(intent);
            }
        });

        final String state_name = fullArrayList.get(position).getState_name();
        final String confirm_cases = fullArrayList.get(position).getConfirm_cases();
        final String dist_name = fullArrayList.get(position).getDist_name();
        final String deaths = fullArrayList.get(position).getDeaths();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Districts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("state_name", state_name);
                intent.putExtra("dist_name", dist_name);
                intent.putExtra("confirm_cases", confirm_cases);
                intent.putExtra("deaths", deaths);

                context.startActivity(intent);

            }
        });

        return view1;
    }
    @Override
    public int getCount() {
        return districtModels.size();
    }

    @Override
    public DistrictModel getItem(int position) {
        return districtModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
