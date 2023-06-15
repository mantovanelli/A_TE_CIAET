package it.unimib.ciaet.ui.app;

import static it.unimib.ciaet.util.Constants.GRAPHS_API_BASE_URL;
import static it.unimib.ciaet.util.Constants.GRAPHS_API_KEY;
import static it.unimib.ciaet.util.Constants.GRAPHS_API_OTHER_URL;
import static it.unimib.ciaet.util.Constants.TOP_HEADLINES_ENDPOINT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.ciaet.adapter.GraphsArrayAdapter;
import it.unimib.ciaet.R;
import it.unimib.ciaet.model.CriptoCurrency;
import it.unimib.ciaet.repository.GraphsRepository;
import it.unimib.ciaet.repository.IGraphsRepository;
import it.unimib.ciaet.util.ResponseCallback;
import it.unimib.ciaet.util.SharedPreferencesUtil;

public class Fragment_Graphs extends Fragment implements ResponseCallback {

    private ArrayList<CriptoCurrency> currencyList;
    private IGraphsRepository iGraphsRepository;
    private GraphsArrayAdapter currencyRecyclerViewAdapter;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ProgressBar loadingPB;
    private EditText searchEdt;


    public Fragment_Graphs() {
        // Required empty public constructor
    }


    public static Fragment_Graphs newInstance() {return new Fragment_Graphs();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iGraphsRepository = new GraphsRepository(requireActivity().getApplication(), this);
        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
        currencyList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graphs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        loadingPB = view.findViewById(R.id.idPBLoading);

        RecyclerView recyclerViewCurrencies = view.findViewById(R.id.recycleview1);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);


        currencyRecyclerViewAdapter = new GraphsArrayAdapter(currencyList,
                requireActivity().getApplication());


        recyclerViewCurrencies.setLayoutManager(layoutManager);
        recyclerViewCurrencies.setAdapter(currencyRecyclerViewAdapter);


        loadingPB.setVisibility(View.VISIBLE);

        String lastUpdate = "0";

        iGraphsRepository.fetchCurrency(0,
                Long.parseLong(lastUpdate));


        getCurrencyData();




    }




    @Override
    public void onSuccess(List<CriptoCurrency> newsList, long lastUpdate) {


        if (newsList != null) {
            this.currencyList.clear();
            this.currencyList.addAll(newsList);
        }

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currencyRecyclerViewAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onFailure(String errorMessage) {
        loadingPB.setVisibility(View.GONE);
       /* Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_SHORT).show();*/
        Log.e("error in coloum",errorMessage);
    }





    private void filterCurrency(String currencyValue){
        ArrayList<CriptoCurrency> listaFiltrata = new ArrayList<>();
        for(CriptoCurrency item : currencyList){
            if(item.getSimbolo().toLowerCase().contains(currencyValue.toLowerCase())){
                listaFiltrata.add(item);
            }
        }
        if(listaFiltrata.isEmpty()){
            Snackbar.make(getView(), getString(R.string.error9), Snackbar.LENGTH_SHORT).show();
        }
        else
            currencyRecyclerViewAdapter.filterList(listaFiltrata);
    }



    private void getCurrencyData(){
        loadingPB.setVisibility(View.VISIBLE);
        String url = GRAPHS_API_BASE_URL+GRAPHS_API_OTHER_URL;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("data",response.toString());
                loadingPB.setVisibility(View.GONE);

                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String simbolo = dataObj.getString("symbol");
                        int id = dataObj.getInt("id");
                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double prezzo = USD.getDouble("price");
                        currencyList.add(new CriptoCurrency(simbolo, prezzo, id));
                    }
                    currencyRecyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                   // Snackbar.make(getView(), getString(R.string.error8), Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                error.printStackTrace();
                //Snackbar.make(getView(), getString(R.string.error7), Snackbar.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put(TOP_HEADLINES_ENDPOINT,GRAPHS_API_KEY);
                return  headers;
            }
        }
                ;
        requestQueue.add(jsonObjectRequest);
    }



}