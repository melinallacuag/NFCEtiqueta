package com.example.nfcetiqueta.Fragment;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcetiqueta.Adapter.LProductosAdapter;
import com.example.nfcetiqueta.Adapter.LRegistroClienteAdapter;
import com.example.nfcetiqueta.Adapter.LRegistroPuntosAdapter;
import com.example.nfcetiqueta.Adapter.ProductoPuntoAdapter;
import com.example.nfcetiqueta.Adapter.TipoClienteAdapter;
import com.example.nfcetiqueta.Adapter.TipoDescuentoAdapter;
import com.example.nfcetiqueta.Adapter.TipoRangoAdapter;
import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientePuntos;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientes;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;
import com.example.nfcetiqueta.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PuntosFragment extends Fragment implements NfcAdapter.ReaderCallback  {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techLists;

    TextInputEditText inputNFC, inputPlaca, inputRucDni,inputRazonSocial;

    TextInputLayout alertaNFC, alertPlaca,alertRucDni,alertRazonSocial;

    String nfc, placa,rucdni,razonsocial,productoId;

    Button btnconsultar;

    LinearLayout btnGuardar,btnLimpiarPunt;

    ProductoPuntoAdapter productoPuntoAdapter;
    LRegistroPuntosAdapter lRegistroPuntosAdapter;
    RecyclerView recyclerLProducto;

    List<LClientePuntos> lClientePuntosList;
    List<LClientePuntos> productosAsociados;

    Double ganados,canjeados,disponibles;
    Boolean estado;


    private APIService mAPIService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_puntos, container, false);

        mAPIService = GlobalInfo.getAPIService();

        btnGuardar        = view.findViewById(R.id.btnGuardar);
        btnconsultar      = view.findViewById(R.id.buscarDatoCliente);
        btnLimpiarPunt    = view.findViewById(R.id.btnLimpiarPunt);

        alertaNFC         = view.findViewById(R.id.textNFC);
        alertPlaca        = view.findViewById(R.id.textPlaca);
        alertRucDni       = view.findViewById(R.id.text_RUC_DNI);
        alertRazonSocial  = view.findViewById(R.id.text_RazonSocial);

        inputNFC                = view.findViewById(R.id.input_EtiquetaNFC);
        inputPlaca              = view.findViewById(R.id.input_Placa);
        inputRucDni             = view.findViewById(R.id.input_ruc_dni);
        inputRazonSocial        = view.findViewById(R.id.input_RazonSocial);

        btnGuardar.setEnabled(true);
        btnGuardar.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorSuccess));

        /**
         *
         */
        btnLimpiarPunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNFC.getText().clear();
                inputPlaca.setText("000-0000");
                inputRucDni.getText().clear();
                inputRazonSocial.getText().clear();

                inputRucDni.setEnabled(true);
                inputRazonSocial.setEnabled(true);

                alertRucDni.setBoxBackgroundColorResource(R.color.white);
                alertRazonSocial.setBoxBackgroundColorResource(R.color.white);

                productoPuntoAdapter.clearSelections();
            }
        });

        /**
         *  Iniciar proceso de detector NFC
         */
        inputNFC.setKeyListener(null);

        /*** Buscar nfc ***/
        inputNFC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nfc = s.toString();
                buscarClienteAfiliadoPorNFC(nfc);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        Intent intent = new Intent(getContext(), getActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFilters = new IntentFilter[]{tagIntentFilter};
        techLists = new String[][]{new String[]{NfcA.class.getName(), NfcB.class.getName(),
                NfcF.class.getName(), NfcV.class.getName(), IsoDep.class.getName(),
                MifareClassic.class.getName(), MifareUltralight.class.getName(),
                Ndef.class.getName()}};

        /**
         * Inicio Mostrara los datos del Modal de Productos
         */
        recyclerLProducto = view.findViewById(R.id.recyclerProductoPunto);
        recyclerLProducto.setLayoutManager(new LinearLayoutManager(getContext()));
        getListProductos();

        /**
         *  Consultar Dato del Cliente
         */
        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String campodatocliente = inputRucDni.getText().toString();

                if (campodatocliente.isEmpty() || campodatocliente == null) {
                    alertRucDni.setError("* El campo DNI / RUC es obligatorio");
                    return;
                }else if (campodatocliente.length() < 8 || 11 < campodatocliente.length() ){
                    alertRucDni.setError("* El DNI / RUC debe tener 8 o 11 dígitos");
                    return;
                }else if (campodatocliente.length() == 8 ){
                    findClienteDNI(campodatocliente);
                    return;
                }else if (campodatocliente.length() == 11 ){
                    findClienteRUC(campodatocliente);
                    return;
                }

                alertRucDni.setErrorEnabled(false);

                inputRazonSocial.getText().clear();

            }
        });

        /**
         *  Proceso para Agregar un Nuevo Cliente Afiliado - Descuento
         */
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nfc                = inputNFC.getText().toString();
                placa              = inputPlaca.getText().toString();
                rucdni             = inputRucDni.getText().toString();
                razonsocial        = inputRazonSocial.getText().toString();

                disponibles = 0.00;
                ganados  = 0.00;
                canjeados = 0.00;

                estado = true;

                if(nfc.isEmpty()){
                    alertaNFC.setError("El campo NFC es obligatorio");
                    return;
                }else if(placa.isEmpty()){
                    alertPlaca.setError("El campo Placa es obligatorio");
                    return;
                }else if(rucdni.isEmpty()){
                    alertRucDni.setError("El campo RUC o DNI es obligatorio");
                    return;
                } else if(razonsocial.isEmpty()){
                    alertRazonSocial.setError("El campo Razon Social es obligatorio");
                    return;
                }

                alertaNFC.setErrorEnabled(false);
                alertPlaca.setErrorEnabled(false);
                alertRucDni.setErrorEnabled(false);
                alertRazonSocial.setErrorEnabled(false);

                findClientePuntos(GlobalInfo.getRFIDPuntos10 , GlobalInfo.getclienteId10 ,GlobalInfo.getGetIdCompany10);

            }
        });

        findCliente(GlobalInfo.getRFIDPuntos10, GlobalInfo.getclienteId10, GlobalInfo.getGetIdCompany10);

        return view;
    }

    private void buscarClienteAfiliadoPorNFC(String nfc) {
        if (lClientePuntosList != null) {
            List<LClientePuntos> clientePuntosList = new ArrayList<>();
            for (LClientePuntos cliente : lClientePuntosList) {
                if (cliente.getRfid().equals(nfc)) {
                    clientePuntosList.add(cliente);
                }
            }
            if (!clientePuntosList.isEmpty()) {
                actualizarCampos(clientePuntosList);
                return;
            }
        }
        btnGuardar.setEnabled(true);
        btnGuardar.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorSuccess));
        if(GlobalInfo.getsettingDescuentoRFID10 != 0){
            limpiarCampos();
        }
    }


    private void limpiarCampos() {

        inputPlaca.setText("000-0000");
        inputRucDni.getText().clear();
        inputRazonSocial.getText().clear();

        inputRucDni.setEnabled(true);
        inputRazonSocial.setEnabled(true);

        alertRucDni.setBoxBackgroundColorResource(R.color.white);
        alertRazonSocial.setBoxBackgroundColorResource(R.color.white);

        productoPuntoAdapter.clearSelections();
    }

    private void actualizarCampos(List<LClientePuntos> clientePuntosList) {

        if (clientePuntosList == null || clientePuntosList.isEmpty()) return;

        LClientePuntos clientePuntos = clientePuntosList.get(0);

        if (!clientePuntos.getStatus()) {

            btnGuardar.setEnabled(false);
            btnGuardar.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colornew));
            Toast.makeText(getContext(), "El cliente está inactivo y no puede realizar operaciones.", Toast.LENGTH_SHORT).show();
            if(GlobalInfo.getsettingDescuentoRFID10 != 0){
                limpiarCampos();
            }
            return;

        } else {

            btnGuardar.setEnabled(true);
            btnGuardar.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorSuccess));

            inputPlaca.setText(clientePuntos.getNroPlaca());
            inputRucDni.setText(clientePuntos.getClienteID());
            inputRazonSocial.setText(clientePuntos.getClienteRZ());

            GlobalInfo.getClienteStatusPuntos10 = clientePuntos.getStatus();

            inputRucDni.setEnabled(false);
            inputRazonSocial.setEnabled(false);

            alertRucDni.setBoxBackgroundColorResource(R.color.colornew);
            alertRazonSocial.setBoxBackgroundColorResource(R.color.colornew);

            List<String> articuloIDsSeleccionados = new ArrayList<>();
            for (LClientePuntos puntos : clientePuntosList) {
                articuloIDsSeleccionados.add(puntos.getArticuloID());
            }

            if (productoPuntoAdapter != null) {
                productoPuntoAdapter.setSelections(articuloIDsSeleccionados);
            }
        }

    }

    private void findCliente(String nfcId,String ClienteID,Integer comapyId){

        Call<List<LClientePuntos>> call = mAPIService.findClienteArticulosPuntos(nfcId,ClienteID,comapyId);

            call.enqueue(new Callback<List<LClientePuntos>>() {
            @Override
            public void onResponse(Call<List<LClientePuntos>> call, Response<List<LClientePuntos>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                lClientePuntosList = response.body();

                if (lRegistroPuntosAdapter != null) {
                    lRegistroPuntosAdapter.updateData(lClientePuntosList);
                    lRegistroPuntosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<LClientePuntos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Lista Cliente Puntos- RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean verificarNFCRegistrado(String nfc, String rucdni) {
        if (lClientePuntosList != null) {
            for (LClientePuntos cliente : lClientePuntosList) {
                if (cliente.getRfid().equals(nfc)) {
                    if (!cliente.getClienteID().equals(rucdni)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


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

                    lClientePuntosList = response.body();

                    if (lRegistroPuntosAdapter != null) {
                        lRegistroPuntosAdapter.updateData(lClientePuntosList);
                        lRegistroPuntosAdapter.notifyDataSetChanged();
                    }

                    if (verificarNFCRegistrado(nfc, rucdni)) {
                        alertRucDni.setError("El NFC ya está registrado con otro RUC/DNI");
                        return;
                    }

                    if (lClientePuntosList != null && !lClientePuntosList.isEmpty()) {
                        postClientePuntosEliminado2(nfc, rucdni, GlobalInfo.getGetIdCompany10, Integer.valueOf(GlobalInfo.getuserID10));
                    } else {
                        guardarNuevoRegistro();
                    }

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

    /** Guardar - GALONES */
    private void guardar_clientesPuntos(String rfid,String articuloID,String clienteID,String clienteRZ,String nroPlaca,
                                        Double ganados, Double canjeados, Double disponibles, Boolean status, String companyID,String userID){

        final LClientePuntos clientePuntos = new LClientePuntos(rfid,articuloID,clienteID,clienteRZ,nroPlaca,
                ganados, canjeados,disponibles,status,companyID,userID);

        Call<LClientePuntos> call = mAPIService.postClientePuntos(clientePuntos);

        call.enqueue(new Callback<LClientePuntos>() {
            @Override
            public void onResponse(Call<LClientePuntos> call, Response<LClientePuntos> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo de error G Puntos: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<LClientePuntos> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Guardar Cliente Puntos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /** API SERVICE - Eliminar Puntos*/
    private void postClientePuntosEliminado2(String RFID,String ClienteID,Integer CompanyID,Integer UserID){

        Call<LClientePuntos> call = mAPIService.postClientePuntosEliminado2(RFID,ClienteID,CompanyID,UserID);

        call.enqueue(new Callback<LClientePuntos>() {
            @Override
            public void onResponse(Call<LClientePuntos> call, Response<LClientePuntos> response) {

                if(!response.isSuccessful()){

                    Toast.makeText(getContext(), "Codigo de error Eliminar Cliente Puntos: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;

                }

                guardarNuevoRegistro();

            }

            @Override
            public void onFailure(Call<LClientePuntos> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Eliminar Cliente Puntos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void guardarNuevoRegistro() {
        List<LProductos> selectedProducts = productoPuntoAdapter.getSelectedProducts();

        for (LProductos product : selectedProducts) {
            productoId = product.getId();
            guardar_clientesPuntos(nfc, productoId, rucdni, razonsocial, placa, ganados, canjeados, disponibles, estado, String.valueOf(GlobalInfo.getGetIdCompany10), GlobalInfo.getuserID10);
           // Toast.makeText(getContext(), "Se guardó correctamente " + productoId + rucdni, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();

        inputNFC.getText().clear();
        inputPlaca.setText("000-0000");
        inputRucDni.getText().clear();
        inputRazonSocial.getText().clear();

        alertRucDni.setBoxBackgroundColorResource(R.color.white);
        alertRazonSocial.setBoxBackgroundColorResource(R.color.white);

        inputRucDni.setEnabled(true);
        inputRazonSocial.setEnabled(true);

        productoPuntoAdapter.clearSelections();

        findCliente(GlobalInfo.getRFIDPuntos10, GlobalInfo.getclienteId10, GlobalInfo.getGetIdCompany10);
    }

    /** Listado de Productos */
    private void getListProductos(){

        Call<List<LProductos>> call = mAPIService.getLProductos();

        call.enqueue(new Callback<List<LProductos>>() {
            @Override
            public void onResponse(Call<List<LProductos>> call, Response<List<LProductos>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.getproductosList10 = response.body();

                    boolean[] checkedItems = new boolean[GlobalInfo.getproductosList10.size()];
                    Arrays.fill(checkedItems, true);

                    productoPuntoAdapter = new ProductoPuntoAdapter(GlobalInfo.getproductosList10,checkedItems, getContext(), new ProductoPuntoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LProductos item) {

                        }
                    });

                    recyclerLProducto.setAdapter(productoPuntoAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LProductos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Productos - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** API SERVICE - Buscar Cliente DNI */
    private  void findClienteDNI(String id){

        Call<List<LClientes>> call = mAPIService.findClienteDNI(id);

        call.enqueue(new Callback<List<LClientes>>() {
            @Override
            public void onResponse(Call<List<LClientes>> call, Response<List<LClientes>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.getlistaclienteList10 = response.body();

                    LClientes lClientes = GlobalInfo.getlistaclienteList10.get(0);

                    GlobalInfo.getclienteRZ10  = String.valueOf(lClientes.getClienteRZ());

                    inputRazonSocial.setText(GlobalInfo.getclienteRZ10);

                    if(GlobalInfo.getsettingDescuentoRFID10 == 0){
                        inputNFC.setText(id);
                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LClientes>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente DNI - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** API SERVICE - Buscar Cliente RUC */
    private  void findClienteRUC(String id){

        Call<List<LClientes>> call = mAPIService.findClienteRUC(id);

        call.enqueue(new Callback<List<LClientes>>() {
            @Override
            public void onResponse(Call<List<LClientes>> call, Response<List<LClientes>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.getlistaclienteList10 = response.body();

                    LClientes lClientes = GlobalInfo.getlistaclienteList10.get(0);

                    GlobalInfo.getclienteRZ10  = String.valueOf(lClientes.getClienteRZ());

                    GlobalInfo.getclienteRZ10 =  GlobalInfo.getclienteRZ10.replace("?","’");

                    inputRazonSocial.setText(GlobalInfo.getclienteRZ10);

                    if(GlobalInfo.getsettingDescuentoRFID10 == 0){
                        inputNFC.setText(id);
                    }


                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LClientes>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente RUC - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(getActivity(), this,
                    NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B |
                            NfcAdapter.FLAG_READER_NFC_F | NfcAdapter.FLAG_READER_NFC_V, null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(getActivity());
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        String nfcTag = ByteArrayToHexString(tag.getId());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inputNFC.setText(nfcTag);
            }
        });
    }

    private String ByteArrayToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(byteArray.length * 2);
        for (byte b : byteArray) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}