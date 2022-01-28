package com.example.trackcovid19;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class StateDetails extends AppCompatActivity {

    TextView nameState, casesState, deathsState, recoveredState, activeState ;
    ListView list;
    List<DistrictModel> arrayList;
    ProgressDialog progressDialog;
    private String state_name;
    private DistrictAdapter districtAdapter;
    PieChartView pieChartView;
    Toolbar toolbar;
    ArrayList<DistrictModel> districtArrayList;

    private static final String stateUrl = "https://data.covid19india.org/data.json";
    private static final String districtUrl = "https://data.covid19india.org/state_district_wise.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);

        Intent intent = getIntent();
        state_name = intent.getStringExtra("state_name");

        nameState = findViewById(R.id.state);
        casesState = findViewById(R.id.txtStateCases);
        deathsState = findViewById(R.id.txtStateDeaths);
        recoveredState = findViewById(R.id.txtStateRecovered);
        activeState = findViewById(R.id.txtStateActive);
        pieChartView = findViewById(R.id.pieChartStates);

        toolbar = (Toolbar) findViewById(R.id.toolbar_state);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select a District");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StateDetails.this, MainActivity.class));
            }
        });
        arrayList = new ArrayList<>();;

        getStateData();

        list = (ListView) findViewById(R.id.listViewDistrict);
        districtArrayList = new ArrayList<DistrictModel>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        getDistrictListData();

    }

    public void getStateData() {
        RequestQueue requestQueue = Volley.newRequestQueue(StateDetails.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, stateUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("statewise");
                            for (int i = 0; i < jsonArray.length(); i ++ ) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String state = jsonObject.getString("state");

                                if(state.equals(state_name)) {
                                    nameState.setText(state_name);
                                    // Confirm Cases
                                    String total_confirm_cases = jsonObject.getString("confirmed");
                                    casesState.setText(total_confirm_cases);
                                    // Active Cases
                                    String total_active_cases = jsonObject.getString("active");
                                    activeState.setText(total_active_cases);
                                    // Recover Cases
                                    String total_recover_cases = jsonObject.getString("recovered");
                                    recoveredState.setText(total_recover_cases);
                                    // Death Cases
                                    String total_death_cases = jsonObject.getString("deaths");
                                    deathsState.setText(total_death_cases);

                                    List<SliceValue> pieData = new ArrayList<>();
                                    pieData.add(new SliceValue(Integer.parseInt(casesState.getText().toString()), Color.parseColor("#D39F03")));
                                    pieData.add(new SliceValue(Integer.parseInt(recoveredState.getText().toString()), Color.parseColor("#FF4CAF50")));
                                    pieData.add(new SliceValue(Integer.parseInt(deathsState.getText().toString()), Color.parseColor("#f55c47")));
                                    pieData.add(new SliceValue(Integer.parseInt(activeState.getText().toString()), Color.parseColor("#38ACDD")));
                                    PieChartData pieChartData = new PieChartData(pieData);
                                    pieChartView.setPieChartData(pieChartData);
//
//                                    pieChart.addPieSlice(new PieModel("cases",Integer.parseInt(casesState.getText().toString()), Color.parseColor("#D39F03")));
//                                    pieChart.addPieSlice(new PieModel("recovered",Integer.parseInt(recoveredState.getText().toString()), Color.parseColor("#FF4CAF50")));
//                                    pieChart.addPieSlice(new PieModel("deaths",Integer.parseInt(deathsState.getText().toString()), Color.parseColor("#f55c47")));
//                                    pieChart.addPieSlice(new PieModel("active",Integer.parseInt(activeState.getText().toString()), Color.parseColor("#38ACDD")));
//                                    pieChart.startAnimation();
                                }
                            }
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StateDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
        // Request
        requestQueue.add(jsonObjectRequest);
    }


    public void getDistrictListData() {
        RequestQueue requestQueue = Volley.newRequestQueue(StateDetails.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, districtUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(state_name);
                            JSONObject distObject = jsonObject.getJSONObject("districtData");

                            JSONArray key = distObject.names();
                            for (int i = 0; i < key.length(); i++) {
                                String name = key.getString(i);
                                JSONObject jsonObjectDist = distObject.getJSONObject(name);
                                String confirm_cases = jsonObjectDist.getString("confirmed");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("state_name", state_name);
                                map.put("confirm_cases", confirm_cases);
                                map.put("dist_name", name);

                                DistrictModel distModel = new DistrictModel(name, state_name, confirm_cases);
                                districtArrayList.add(distModel);
                            }
                            districtAdapter = new DistrictAdapter(getApplicationContext(), districtArrayList);
                            list.setAdapter(districtAdapter);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StateDetails.this, e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StateDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
        );requestQueue.add(jsonObjectRequest);
    }

}