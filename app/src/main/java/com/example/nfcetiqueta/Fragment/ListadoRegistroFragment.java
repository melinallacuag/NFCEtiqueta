package com.example.nfcetiqueta.Fragment;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nfcetiqueta.Adapter.LRegistroClienteAdapter;
import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Parameters.GlobalInfo;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoRegistroFragment extends Fragment {

    RecyclerView recyclerListaClientesAfiliados ;

    LRegistroClienteAdapter lRegistroClienteAdapter;

    List<LClienteAfiliados> lClienteAfiliadosList;

    SearchView BuscarRazonSocial;

    private APIService mAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listado_registro, container, false);

        mAPIService  = GlobalInfo.getAPIService();

        BuscarRazonSocial   = view.findViewById(R.id.BuscarRazonSocial);

        /** Listado de Comprobantes  */
        recyclerListaClientesAfiliados = view.findViewById(R.id.recyclerListaClientesAfiliados);
        recyclerListaClientesAfiliados.setLayoutManager(new LinearLayoutManager(getContext()));

        findClienteAfiliado(GlobalInfo.getnfcId10 ,GlobalInfo.getGetIdCompany10);

        /** Buscador por Razon Social */
        BuscarRazonSocial.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (lClienteAfiliadosList.isEmpty()) {

                    Toast.makeText(getContext(), "No se encontró el dato", Toast.LENGTH_SHORT).show();

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lRegistroClienteAdapter.filtrado(newText);
                return false;
            }
        });

        return view;
    }

    /** API SERVICE - Card Consultar Venta */
    private void findClienteAfiliado(String nfcId,Integer comapyId){

        Call<List<LClienteAfiliados>> call = mAPIService.findClienteAfiliado(nfcId,comapyId);

        call.enqueue(new Callback<List<LClienteAfiliados>>() {
            @Override
            public void onResponse(Call<List<LClienteAfiliados>> call, Response<List<LClienteAfiliados>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.getlistaclienteafiliadoList10 = response.body();

                    lRegistroClienteAdapter = new LRegistroClienteAdapter(GlobalInfo.getlistaclienteafiliadoList10, getContext());

                    recyclerListaClientesAfiliados.setAdapter(lRegistroClienteAdapter);


                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LClienteAfiliados>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Lista Cliente - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

}