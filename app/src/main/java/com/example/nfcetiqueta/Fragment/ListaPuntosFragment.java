package com.example.nfcetiqueta.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcetiqueta.Adapter.LRegistroClienteAdapter;
import com.example.nfcetiqueta.Adapter.LRegistroPuntosAdapter;
import com.example.nfcetiqueta.NFCUtil;
import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientePuntos;
import com.example.nfcetiqueta.WebApiSVEN.Parameters.GlobalInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListaPuntosFragment extends Fragment {

    RecyclerView recyclerListaClientesPuntos;

    LRegistroPuntosAdapter lRegistroPuntosAdapter;

    SearchView BuscarRazonSocial;
    TextView campo_eliminar,campo_listacliente_puntos,campo_estado;

    Dialog modalClientePuntos,modalEliminarRegistro,modalEstadoPuntos;
    Button btnCancelarRProceso,btnEliminar,btnEliminarPuntos,btnDesactivarPuntos,btnActivarEstado,btnCancelarEstado,btnDesactivarEstado,btnCancelarPuntos;

    private APIService mAPIService;
    private NFCUtil nfcUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcUtil = new NFCUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_puntos, container, false);

        mAPIService  = GlobalInfo.getAPIService();
        BuscarRazonSocial   = view.findViewById(R.id.BuscarRazonSocial);

        /** Listado de Comprobantes  */
        recyclerListaClientesPuntos = view.findViewById(R.id.recyclerListaClientesPuntos);
        recyclerListaClientesPuntos.setLayoutManager(new LinearLayoutManager(getContext()));

        /** Mostrar Modal de Opciones Eliminar o Desactivar */
        modalClientePuntos = new Dialog(getContext());
        modalClientePuntos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalClientePuntos.setContentView(R.layout.modal_listacliente_puntos);
        modalClientePuntos.setCancelable(false);

        /** Mostrar Modal de Eliminar Cliente con Puntos  */
        modalEliminarRegistro = new Dialog(getContext());
        modalEliminarRegistro.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalEliminarRegistro.setContentView(R.layout.modal_eliminar_clientepunto);
        modalEliminarRegistro.setCancelable(false);

        /** Mostrar Modal de Estado Desactivado para Clientes con Puntos */
        modalEstadoPuntos = new Dialog(getContext());
        modalEstadoPuntos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalEstadoPuntos.setContentView(R.layout.modal_status_clientepuntos);
        modalEstadoPuntos.setCancelable(false);

        /** Datos para mostrar lista */
        findClientePuntos(GlobalInfo.getRFIDPuntos10 , GlobalInfo.getclienteId10 ,GlobalInfo.getGetIdCompany10);

        BuscarRazonSocial.setIconifiedByDefault(false);
        /** Buscador por Razon Social */
        BuscarRazonSocial.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                if (lRegistroPuntosAdapter != null) {
                    lRegistroPuntosAdapter.filtrado(userInput);
                }
                return true;
            }
        });

        return view;
    }

    /** API SERVICE - Card Consultar Venta */
    private void findClientePuntos(String nfcId,String ClienteID,Integer comapyId){

        Call<List<LClientePuntos>> call = mAPIService.findClienteArticulosPuntos(nfcId,ClienteID,comapyId);

        call.enqueue(new Callback<List<LClientePuntos>>() {
            @Override
            public void onResponse(Call<List<LClientePuntos>> call, Response<List<LClientePuntos>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.getlistaclientepuntosList10 = response.body();

                    lRegistroPuntosAdapter = new LRegistroPuntosAdapter(GlobalInfo.getlistaclientepuntosList10, getContext(), new LRegistroPuntosAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LClientePuntos item) {

                            moveToDescription(item);

                            modalClientePuntos.show();

                            btnEliminarPuntos         = modalClientePuntos.findViewById(R.id.btnEliminarPuntos);
                            btnDesactivarPuntos       = modalClientePuntos.findViewById(R.id.btnDesactivarPuntos);
                            btnCancelarPuntos         = modalClientePuntos.findViewById(R.id.btnCancelarPuntos);
                            campo_listacliente_puntos = modalClientePuntos.findViewById(R.id.campo_listacliente_puntos);

                            campo_listacliente_puntos.setText("Etiqueta NFC: " + GlobalInfo.getClienteRFIDPuntos10 + "\n" + "Razón Social: " + GlobalInfo.getClienteRZPuntos10);

                            if(GlobalInfo.getClienteStatusPuntos10){
                                btnDesactivarPuntos.setText("BLOQUEAR");
                            }else{
                                btnDesactivarPuntos.setText("ACTIVAR");
                            }

                            btnCancelarPuntos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    modalClientePuntos.dismiss();
                                }
                            });

                            btnEliminarPuntos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    modalEliminarRegistro.show();

                                    btnCancelarRProceso  = modalEliminarRegistro.findViewById(R.id.btnCancelarRProceso);
                                    btnEliminar          = modalEliminarRegistro.findViewById(R.id.btnEliminar);
                                    campo_eliminar       = modalEliminarRegistro.findViewById(R.id.campo_eliminar);

                                    campo_eliminar.setText("¿Estas seguro en eliminar tarjeta " + GlobalInfo.getClienteRFIDPuntos10 + " del cliente " + GlobalInfo.getClienteRZPuntos10  + "?" );

                                    btnCancelarRProceso.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modalEliminarRegistro.dismiss();
                                        }
                                    });

                                    btnEliminar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            postClientePuntosEliminado(GlobalInfo.getClienteRFIDPuntos10,GlobalInfo.getClienteIDPuntos10,GlobalInfo.getGetIdCompany10, Integer.valueOf(GlobalInfo.getuserID10));

                                            modalEliminarRegistro.dismiss();
                                            modalClientePuntos.dismiss();

                                            Toast.makeText(getContext(), "Se elimino Cliente Puntos", Toast.LENGTH_SHORT).show();

                                            /** Actualizar - Card Consultar Venta */
                                            findClientePuntos(GlobalInfo.getRFIDPuntos10 , GlobalInfo.getclienteId10 ,GlobalInfo.getGetIdCompany10);
                                        }
                                    });
                                }
                            });

                            btnDesactivarPuntos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    modalEstadoPuntos.show();

                                    btnCancelarEstado    = modalEstadoPuntos.findViewById(R.id.btnCancelarEstado);
                                    btnActivarEstado  = modalEstadoPuntos.findViewById(R.id.btnActivarEstado);
                                    btnDesactivarEstado  = modalEstadoPuntos.findViewById(R.id.btnDesactivarEstado);
                                    campo_estado         = modalEstadoPuntos.findViewById(R.id.campo_estado);

                                    btnActivarEstado.setVisibility(View.GONE);
                                    btnDesactivarEstado.setVisibility(View.GONE);

                                    btnCancelarEstado.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modalEstadoPuntos.dismiss();
                                        }
                                    });

                                    if(GlobalInfo.getClienteStatusPuntos10){

                                        btnDesactivarEstado.setVisibility(View.VISIBLE);

                                        campo_estado.setText("¿Estas seguro en desactivar tarjeta " + GlobalInfo.getClienteRFIDPuntos10 + " del cliente " + GlobalInfo.getClienteRZPuntos10  + "?" );

                                        btnDesactivarEstado.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                postClientePuntosUpdateStatus(GlobalInfo.getClienteRFIDPuntos10,GlobalInfo.getClienteIDPuntos10,GlobalInfo.getGetIdCompany10,GlobalInfo.getClienteStatusDesactivadoPuntos10);

                                                modalEstadoPuntos.dismiss();
                                                modalClientePuntos.dismiss();

                                                Toast.makeText(getContext(), "Se Desactivo Cliente Puntos", Toast.LENGTH_SHORT).show();

                                                /** Actualizar - Card Consultar Venta */
                                                findClientePuntos(GlobalInfo.getRFIDPuntos10 , GlobalInfo.getclienteId10 ,GlobalInfo.getGetIdCompany10);

                                            }
                                        });

                                    }else{

                                        btnActivarEstado.setVisibility(View.VISIBLE);

                                        campo_estado.setText("¿Estas seguro en activar tarjeta " + GlobalInfo.getClienteRFIDPuntos10 + " del cliente " + GlobalInfo.getClienteRZPuntos10  + "?" );

                                        btnActivarEstado.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                postClientePuntosUpdateStatus(GlobalInfo.getClienteRFIDPuntos10,GlobalInfo.getClienteIDPuntos10,GlobalInfo.getGetIdCompany10,GlobalInfo.getClienteStatusActivadoPuntos10);

                                                modalEstadoPuntos.dismiss();
                                                modalClientePuntos.dismiss();

                                                Toast.makeText(getContext(), "Se Activo Cliente Puntos", Toast.LENGTH_SHORT).show();

                                                /** Actualizar - Card Consultar Venta */
                                                findClientePuntos(GlobalInfo.getRFIDPuntos10 , GlobalInfo.getclienteId10 ,GlobalInfo.getGetIdCompany10);

                                            }
                                        });
                                    }

                                }
                            });



                        }
                    });

                    recyclerListaClientesPuntos.setAdapter(lRegistroPuntosAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LClientePuntos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Lista Cliente Puntos - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }
    /** Dato Id de la Etiqueta NFC */
    public void moveToDescription(LClientePuntos item) {
        GlobalInfo.getClienteRFIDPuntos10    = item.getRfid();
        GlobalInfo.getClienteIDPuntos10      = item.getClienteID();
        GlobalInfo.getClienteRZPuntos10      = item.getClienteRZ();
        GlobalInfo.getClienteStatusPuntos10  = item.getStatus();
    }

    /** API SERVICE - Eliminar Puntos*/
    private void postClientePuntosEliminado(String RFID,String ClienteID,Integer CompanyID,Integer UserID){

        Call<LClientePuntos> call = mAPIService.postClientePuntosEliminado(RFID,ClienteID,CompanyID,UserID);

        call.enqueue(new Callback<LClientePuntos>() {
            @Override
            public void onResponse(Call<LClientePuntos> call, Response<LClientePuntos> response) {

                if(!response.isSuccessful()){

                    Toast.makeText(getContext(), "Codigo de error Eliminar Cliente Puntos: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;

                }

            }

            @Override
            public void onFailure(Call<LClientePuntos> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Eliminar Cliente Puntos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /** API SERVICE - Actualizar Estado */
    private void postClientePuntosUpdateStatus(String RFID,String ClienteID,Integer CompanyID,Boolean Status){

        Call<LClientePuntos> call = mAPIService.postClientePuntosUpdateStatus(RFID,ClienteID,CompanyID,Status);

        call.enqueue(new Callback<LClientePuntos>() {
            @Override
            public void onResponse(Call<LClientePuntos> call, Response<LClientePuntos> response) {

                if(!response.isSuccessful()){

                    Toast.makeText(getContext(), "Codigo de error Eliminar Cliente Puntos: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;

                }

            }

            @Override
            public void onFailure(Call<LClientePuntos> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Eliminar Cliente Puntos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcUtil.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcUtil.onPause();
    }
}