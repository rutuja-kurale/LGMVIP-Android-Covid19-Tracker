package com.example.trackcovid19;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.StringRequest;
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

public class MainActivity extends AppCompatActivity {

    TextView txtCases, txtRecovered, txtDeaths, txtActive, totalCases, recovered, active, totalDeaths;
    ListView listView;
    public static List<StatesModel> statesModelList = new ArrayList<>();
    StatesModel statesModel;
    StateAdapter stateAdapter;
    ArrayList state;
    ProgressDialog progressDialog;
    PieChartView pieChartView;
    private ArrayList<HashMap<String, String>> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCases = findViewById(R.id.txtCases);
        txtRecovered = findViewById(R.id.txtRecovered);
        txtDeaths = findViewById(R.id.txtDeaths);
        txtActive = findViewById(R.id.txtActive);
        totalCases = findViewById(R.id.totalTxt);
        recovered = findViewById(R.id.recoveredTxt);
        active = findViewById(R.id.activeTxt);
        totalDeaths = findViewById(R.id.deathsTxt);
        listView = findViewById(R.id.listView);
        pieChartView = findViewById(R.id.piechart);


        fetchDataFromApi();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Select a State");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        arrayList = new ArrayList<>();
        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        fetchData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), StateDetails.class).putExtra("position", i));
                finish();
            }
        });

    }

    public void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String stateUrl = "https://data.covid19india.org/data.json";
//            simpleArcLoader.start();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, stateUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("statewise");
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String stateName = jsonObject.getString("state");
                                String stateCode = jsonObject.getString("active");

                                statesModel = new StatesModel(stateName, stateCode);
                                statesModelList.add(statesModel);
                            }
                            stateAdapter = new StateAdapter(MainActivity.this, statesModelList);
                            listView.setAdapter(stateAdapter);

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            progressDialog.dismiss();

//                                simpleArcLoader.stop();
//                                simpleArcLoader.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
        RequestQueue request = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    public void fetchDataFromApi() {
        String url = "https://disease.sh/v3/covid-19/countries/India";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Intent intent = getIntent();

                            txtCases.setText(jsonObject.getString("cases"));
                            txtRecovered.setText(jsonObject.getString("recovered"));
                            txtDeaths.setText(jsonObject.getString("deaths"));
                            txtActive.setText(jsonObject.getString("active"));
                            state = intent.getStringArrayListExtra("state");

                            List<SliceValue> pieData = new ArrayList<>();
                            pieData.add(new SliceValue(Integer.parseInt(txtCases.getText().toString()), Color.parseColor("#D39F03")));
                            pieData.add(new SliceValue(Integer.parseInt(txtRecovered.getText().toString()), Color.parseColor("#FF4CAF50")));
                            pieData.add(new SliceValue(Integer.parseInt(txtDeaths.getText().toString()), Color.parseColor("#f55c47")));
                            pieData.add(new SliceValue(Integer.parseInt(txtActive.getText().toString()), Color.parseColor("#38ACDD")));
                            PieChartData pieChartData = new PieChartData(pieData);
                            pieChartView.setPieChartData(pieChartData);

                            progressDialog.dismiss();

//                                pieChart.addPieSlice(new PieModel("cases",Integer.parseInt(txtCases.getText().toString()), Color.parseColor("#D39F03")));
//                                pieChart.addPieSlice(new PieModel("recovered",Integer.parseInt(txtRecovered.getText().toString()), Color.parseColor("#FF4CAF50")));
//                                pieChart.addPieSlice(new PieModel("deaths",Integer.parseInt(txtDeaths.getText().toString()), Color.parseColor("#f55c47")));
//                                pieChart.addPieSlice(new PieModel("active",Integer.parseInt(txtActive.getText().toString()), Color.parseColor("#38ACDD")));
//                                pieChart.startAnimation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}