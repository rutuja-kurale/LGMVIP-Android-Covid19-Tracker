package com.example.trackcovid19;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Districts extends AppCompatActivity {

    private String state_name, dist_name, confirm_cases, deaths;
    Intent intent;
    PieChartView pieChartView;
    private TextView total_confirm_casesTv, name;
    private TextView total_active_casesTv;
    private TextView total_recover_casesTv;
    private TextView total_death_casesTv;
    ProgressDialog progressDialog;
    private static final String districtUrl = "https://data.covid19india.org/state_district_wise.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districts);

        intent = getIntent();
        state_name = intent.getStringExtra("state_name");
        dist_name = intent.getStringExtra("dist_name");
        confirm_cases = intent.getStringExtra("confirm_cases");
        deaths = intent.getStringExtra("deaths");

        total_confirm_casesTv = (TextView) findViewById(R.id.txtDistrictCases);
        total_active_casesTv = (TextView) findViewById(R.id.txtDistrictActive);
        total_recover_casesTv = (TextView) findViewById(R.id.txtDistrictRecovered);
        total_death_casesTv = (TextView) findViewById(R.id.txtDistrictDeaths);
        pieChartView = findViewById(R.id.pieChartDistrict);
        name = findViewById(R.id.nameDistrict);

        name.setText(dist_name);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        getDistDetails();

    }

    private void getDistDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(Districts.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, districtUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject state_dataObject = response.getJSONObject(state_name);
                    JSONObject distObject = state_dataObject.getJSONObject("districtData");
                    JSONObject particular_distData = distObject.getJSONObject(dist_name);


                    String total_confirm = particular_distData.getString("confirmed");
                    String total_active_cases = particular_distData.getString("active");
                    String total_recover = particular_distData.getString("recovered");
                    String total_death = particular_distData.getString("deceased");

                    total_confirm_casesTv.setText(total_confirm);
                    total_active_casesTv.setText(total_active_cases);
                    total_recover_casesTv.setText(total_recover);
                    total_death_casesTv.setText(total_death);

                    progressDialog.dismiss();

                    List<SliceValue> pieData = new ArrayList<>();
                    pieData.add(new SliceValue(Integer.parseInt(total_confirm_casesTv.getText().toString()), Color.parseColor("#D39F03")));
                    pieData.add(new SliceValue(Integer.parseInt(total_recover_casesTv.getText().toString()), Color.parseColor("#FF4CAF50")));
                    pieData.add(new SliceValue(Integer.parseInt(total_death_casesTv.getText().toString()), Color.parseColor("#f55c47")));
                    pieData.add(new SliceValue(Integer.parseInt(total_active_casesTv.getText().toString()), Color.parseColor("#38ACDD")));
                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartView.setPieChartData(pieChartData);

//                    pieChart.addPieSlice(new PieModel("cases",Integer.parseInt(total_confirm_casesTv.getText().toString()), Color.parseColor("#D39F03")));
//                    pieChart.addPieSlice(new PieModel("recovered",Integer.parseInt(total_recover_casesTv.getText().toString()), Color.parseColor("#FF4CAF50")));
//                    pieChart.addPieSlice(new PieModel("deaths",Integer.parseInt(total_death_casesTv.getText().toString()), Color.parseColor("#f55c47")));
//                    pieChart.addPieSlice(new PieModel("active",Integer.parseInt(total_active_casesTv.getText().toString()), Color.parseColor("#38ACDD")));
//                    pieChart.startAnimation();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Districts.this, e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Districts.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}