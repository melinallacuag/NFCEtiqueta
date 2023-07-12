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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nfcetiqueta.Adapter.LProductosAdapter;
import com.example.nfcetiqueta.Adapter.TipoClienteAdapter;
import com.example.nfcetiqueta.Adapter.TipoDescuentoAdapter;
import com.example.nfcetiqueta.Adapter.TipoRangoAdapter;
import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientes;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;
import com.example.nfcetiqueta.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NFCFragment extends Fragment implements NfcAdapter.ReaderCallback {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techLists;

    TextInputEditText inputNFC, inputPlaca, inputRucDni,inputRazonSocial,input_CodProducto,input_DescProducto,
                      input_DescTipoCliente, input_DescTipoRango,input_RangoInicial,input_RangoFinal,
                      input_DescTipoDescuento,input_DescGalon;

    TextInputLayout alertaNFC, alertPlaca,alertRucDni,alertRazonSocial,alertCodProducto,alertDescProducto,
                    alertRangoInicial,alertRangoFinal,alertDescGalon;

    String nfc, placa,rucdni,razonsocial,codproducto,descproducto,rgInicial,rgFinal,descuentoGl,
            descripcion_prodcuto,codigo_producto,
            tipoclienteID,tipoRangoID,tipoDescuentoID;

    Button btnagregar,btnconsultar,btncancelar;

    Dialog modalProducto;
    LProductosAdapter lProductosAdapter;
    RecyclerView recyclerLProducto;

    TipoClienteAdapter tipoClienteAdapter;
    TipoRangoAdapter tipoRangoAdapter;
    TipoDescuentoAdapter tipoDescuentoAdapter;

    TipoCliente tipoCliente;
    TipoRango tipoRango;
    TipoDescuento tipoDescuento;

    Spinner SpinnerTCliente,SpinnerTRango,SpinnerTDescuento;

    Double DoublergInicial,DoublergFinal,DoubledescuentoGl;

    private APIService mAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_n_f_c, container, false);

        mAPIService = GlobalInfo.getAPIService();

        btnagregar        = view.findViewById(R.id.btnAgregar);
        btnconsultar      = view.findViewById(R.id.buscarDatoCliente);

        alertaNFC         = view.findViewById(R.id.textNFC);
        alertPlaca        = view.findViewById(R.id.textPlaca);
        alertRucDni       = view.findViewById(R.id.text_RUC_DNI);
        alertRazonSocial  = view.findViewById(R.id.text_RazonSocial);
        alertCodProducto  = view.findViewById(R.id.text_CodProducto);
        alertDescProducto = view.findViewById(R.id.text_DescProducto);
        alertRangoInicial = view.findViewById(R.id.text_RangoInicial);
        alertRangoFinal   = view.findViewById(R.id.text_RangoFinal);
        alertDescGalon    = view.findViewById(R.id.text_DescGalon);

        inputNFC                = view.findViewById(R.id.input_EtiquetaNFC);
        inputPlaca              = view.findViewById(R.id.input_Placa);
        inputRucDni             = view.findViewById(R.id.input_ruc_dni);
        inputRazonSocial        = view.findViewById(R.id.input_RazonSocial);
        input_CodProducto       = view.findViewById(R.id.input_CodProducto);
        input_DescProducto      = view.findViewById(R.id.input_DescProducto);
        input_DescTipoCliente   = view.findViewById(R.id.input_DescTipoCliente);
        input_DescTipoRango     = view.findViewById(R.id.input_DescTipoRango);
        input_RangoInicial      = view.findViewById(R.id.input_RangoInicial);
        input_RangoFinal        = view.findViewById(R.id.input_RangoFinal);
        input_DescTipoDescuento = view.findViewById(R.id.input_DescTipoDescuento);
        input_DescGalon         = view.findViewById(R.id.input_DescGalon);

        /** Componentes de Seleccionar Datos */
        SpinnerTCliente   = view.findViewById(R.id.SpinnerTCliente);
        SpinnerTRango     = view.findViewById(R.id.SpinnerTRango);
        SpinnerTDescuento = view.findViewById(R.id.SpinnerTDescuento);

        /**
         *  Iniciar proceso de detector NFC
         */
        inputNFC.setKeyListener(null);

        nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        /** Create pending intent for reading NFC tag */
        Intent intent = new Intent(getContext(), getActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
       // pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
         pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        /** Create intent filters and tech lists for NFC tag reading */
        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFilters = new IntentFilter[]{tagIntentFilter};
        techLists = new String[][]{new String[]{NfcA.class.getName(), NfcB.class.getName(),
                NfcF.class.getName(), NfcV.class.getName(), IsoDep.class.getName(),
                MifareClassic.class.getName(), MifareUltralight.class.getName(),
                Ndef.class.getName()}};

        /**
         *  Proceso para Agregar un Nuevo Cliente Afiliado - Descuento
         */
        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nfc                = inputNFC.getText().toString();
                placa              = inputPlaca.getText().toString();
                rucdni             = inputRucDni.getText().toString();
                razonsocial        = inputRazonSocial.getText().toString();
                codproducto        = input_CodProducto.getText().toString();
                descproducto       = input_DescProducto.getText().toString();
                rgInicial          = input_RangoInicial.getText().toString();
                rgFinal            = input_RangoFinal.getText().toString();
                descuentoGl        = input_DescGalon.getText().toString();

                tipoclienteID      = tipoCliente.getId();
                tipoRangoID        = tipoRango.getId();
                tipoDescuentoID    = tipoDescuento.getId();

                if (verificarNFCRegistrado(nfc, rucdni)) {
                    alertRucDni.setError("El NFC ya está registrado con otro RUC/DNI");
                   return;
                }

                if(nfc.isEmpty()){
                    alertaNFC.setError("El campo NFC es obligatorio");
                    return;
                }if(placa.isEmpty()){
                    alertPlaca.setError("El campo Placa es obligatorio");
                    return;
                }else if(rucdni.isEmpty()){
                    alertRucDni.setError("El campo RUC o DNI es obligatorio");
                    return;
                } else if(razonsocial.isEmpty()){
                    alertRazonSocial.setError("El campo Razon Social es obligatorio");
                    return;
                }else if(codproducto.isEmpty()){
                    alertCodProducto.setError("El campo Cod. Producto es obligatorio");
                    return;
                }else if(descproducto.isEmpty()){
                    alertDescProducto.setError("El campo descripción es obligatorio");
                    return;
                }else if(rgInicial.isEmpty()){
                    alertRangoInicial.setError("El campo Rango Inicial es obligatorio");
                    return;
                }else if(rgFinal.isEmpty()){
                    alertRangoFinal.setError("El campo Rango Final es obligatorio");
                    return;
                }else if(descuentoGl.isEmpty()){
                    alertDescGalon.setError("El campo Descuento x Galon es obligatorio");
                    return;
                }
                /*if (!verificarRucDniClienteExistente(rucdni)) {
                    Toast.makeText(getContext(), "El RUC/DNI no coincide con el cliente existente", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                DoublergInicial    = Double.parseDouble(rgInicial);
                DoublergFinal      = Double.parseDouble(rgFinal);
                DoubledescuentoGl  = Double.parseDouble(descuentoGl);

                alertaNFC.setErrorEnabled(false);
                alertPlaca.setErrorEnabled(false);
                alertRucDni.setErrorEnabled(false);
                alertRazonSocial.setErrorEnabled(false);
                alertCodProducto.setErrorEnabled(false);
                alertDescProducto.setErrorEnabled(false);
                alertRangoInicial.setErrorEnabled(false);
                alertRangoFinal.setErrorEnabled(false);
                alertDescGalon.setErrorEnabled(false);

                guardar_clientesAfiliados(nfc,codproducto,rucdni,tipoclienteID,tipoRangoID,DoublergInicial,DoublergFinal,
                        razonsocial,placa,tipoDescuentoID,DoubledescuentoGl,GlobalInfo.getGetIdCompany10 ,GlobalInfo.getuserID10);

                Toast.makeText(getContext(), "Se guardo correctamente", Toast.LENGTH_SHORT).show();

                inputNFC.getText().clear();
                inputPlaca.setText("000-0000");
                inputRucDni.getText().clear();
                inputRazonSocial.getText().clear();
                input_CodProducto.getText().clear();
                input_DescProducto.getText().clear();
                input_RangoInicial.setText("0.000");
                input_RangoFinal.setText("0.000");
                input_DescGalon.setText("0.00");

                SpinnerTCliente.setSelection(0);
                SpinnerTRango.setSelection(0);
                SpinnerTDescuento.setSelection(0);

            }
        });

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
         *  Modal de Listado de Productos
         */
        modalProducto = new Dialog(getContext());
        modalProducto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalProducto.setContentView(R.layout.list_productos);
        modalProducto.setCancelable(false);

        /** Opción de Mostrar Modal de Productos */
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                btncancelar  = modalProducto.findViewById(R.id.btnCancelarLProductos);

                modalProducto.show();

                /** Mostrara los datos del Modal de Productos */
                recyclerLProducto = modalProducto.findViewById(R.id.recyclerLProducto);
                recyclerLProducto.setLayoutManager(new LinearLayoutManager(getContext()));

                lProductosAdapter = new LProductosAdapter(GlobalInfo.getproductosList10, getContext(), new LProductosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(LProductos item) {

                        /** Al seleccionar una opcion se insertara en el Campo ( Codigo - Descripcion ) */
                        descripcion_prodcuto = item.getDescripcion();
                        codigo_producto      = item.getId();

                        input_DescProducto.setText(descripcion_prodcuto);
                        input_CodProducto.setText(codigo_producto);

                        modalProducto.dismiss();
                    }
                });

                recyclerLProducto.setAdapter(lProductosAdapter);

                /** Cerrar Modal de Productos */
                btncancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalProducto.dismiss();
                    }
                });

                return true;
            }
        });

        /** Realizar Doble Click al campo CodProducto abrira el Modal de Producto */
        input_CodProducto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                if (!gestureDetector.onTouchEvent(event)) {

                    return false;
                }
                return true;
            }
        });

        /**
         *  Seleccionar Tipo de Cliente
         */

        Resources resTCliente = getResources();
        tipoClienteAdapter = new TipoClienteAdapter(getContext(), R.layout.itemcliente, (ArrayList<TipoCliente>) GlobalInfo.gettipoclienteList10, resTCliente);
        SpinnerTCliente.setAdapter(tipoClienteAdapter);

        SpinnerTCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipoCliente = (TipoCliente) parent.getItemAtPosition(position);
                String descripcionCliente = tipoCliente.getDescripcion();
                if (tipoCliente.getId().equals("CON")) {

                    input_DescTipoCliente.setText(descripcionCliente);

                } else if (tipoCliente.getId().equals("CRE")) {

                    input_DescTipoCliente.setText(descripcionCliente);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         *  Seleccionar Tipo de Rango
         */

        Resources resTRango = getResources();
        tipoRangoAdapter = new TipoRangoAdapter(getContext(), R.layout.itemrango, (ArrayList<TipoRango>) GlobalInfo.gettiporangoList10, resTRango);
        SpinnerTRango.setAdapter(tipoRangoAdapter);

        SpinnerTRango.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoRango = (TipoRango) parent.getItemAtPosition(position);
                String descripcionRango = tipoRango.getDescripcion();
                if (tipoRango.getId().equals("GLN")) {
                    input_DescTipoRango.setText(descripcionRango);

                } else if (tipoRango.getId().equals("MTO")) {
                    input_DescTipoRango.setText(descripcionRango);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         *  Seleccionar Tipo de Descuento
         */

        Resources resTDescuento = getResources();
        tipoDescuentoAdapter = new TipoDescuentoAdapter(getContext(), R.layout.itemdescuento, (ArrayList<TipoDescuento>) GlobalInfo.gettipodescuentoList10, resTDescuento);
        SpinnerTDescuento.setAdapter(tipoDescuentoAdapter);

        SpinnerTDescuento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoDescuento = (TipoDescuento) parent.getItemAtPosition(position);
                String descripcionDescuento = tipoDescuento.getDescripcion();
                if (tipoDescuento.getId().equals("DES")) {

                    input_DescTipoDescuento.setText(descripcionDescuento);

                } else if (tipoDescuento.getId().equals("PRE")) {

                    input_DescTipoDescuento.setText(descripcionDescuento);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private boolean verificarNFCRegistrado(String nfc, String rucdni) {
        if (GlobalInfo.getlistaclienteafiliadoList10 != null) {
            for (LClienteAfiliados cliente : GlobalInfo.getlistaclienteafiliadoList10) {
                if (cliente.getRfid().equals(nfc)) {
                    if (cliente.getClienteID().equals(rucdni)) {
                        return false; // El NFC ya está registrado con otro ID de RUC/DNI
                    } else {
                        return true; // El NFC está registrado con el mismo ID de RUC/DNI
                    }
                }
            }
        }
        return false; // El NFC no está registrado
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

                    inputRazonSocial.setText(GlobalInfo.getclienteRZ10);


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

    /** Guardar - GALONES */
    private void guardar_clientesAfiliados(String rfid,String articuloID,String clienteID,String tipoCliente,String tipoRango,
                                           Double rango1, Double rango2,String clienteRZ,String nroPlaca,String tipoDescuento,
                                           Double montoDescuento, Integer companyID,String userID){

        final LClienteAfiliados clienteAfiliados = new LClienteAfiliados(rfid,articuloID,clienteID,tipoCliente,tipoRango,rango1,rango2,clienteRZ,nroPlaca
                                                                    ,tipoDescuento,montoDescuento,companyID,userID);

        Call<LClienteAfiliados> call = mAPIService.postClienteAfiliados(clienteAfiliados);

        call.enqueue(new Callback<LClienteAfiliados>() {
            @Override
            public void onResponse(Call<LClienteAfiliados> call, Response<LClienteAfiliados> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<LClienteAfiliados> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE", Toast.LENGTH_SHORT).show();
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
}