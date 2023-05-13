package com.example.nfcetiqueta;
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

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class NFCFragment extends Fragment implements NfcAdapter.ReaderCallback {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techLists;
    private TextInputEditText textViewNFC;

    String nfc, placa,rucdni,razonsocial,direccion;

    Button btnagregar;
    TextInputEditText inputNFC, inputPlaca, inputRucDni,inputRazonSocial, inputDireccion,
            input_DescTipoCliente, input_DescTipoRango,input_DescTipoDescuento,input_CodProducto;
    TextInputLayout alertaNFC, alertPlaca,alertRucDni,alertRazonSocial,alertDireccion;

    Spinner SpinnerTCliente,SpinnerTRango,SpinnerTDescuento;

    Dialog modalCliente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_n_f_c, container, false);

        textViewNFC = view.findViewById(R.id.input_EtiquetaNFC);
        btnagregar  = view.findViewById(R.id.btnAgregar);

        inputNFC          = view.findViewById(R.id.input_EtiquetaNFC);
        inputPlaca        = view.findViewById(R.id.input_Placa);
        inputRucDni       = view.findViewById(R.id.input_ruc_dni);
        inputRazonSocial  = view.findViewById(R.id.input_RazonSocial);

        input_CodProducto = view.findViewById(R.id.input_CodProducto);


        alertaNFC         = view.findViewById(R.id.textNFC);
        alertPlaca        = view.findViewById(R.id.textPlaca);
        alertRucDni       = view.findViewById(R.id.text_RUC_DNI);
        alertRazonSocial  = view.findViewById(R.id.text_RazonSocial);


        /**
         * Spinner Cliente - Rango - Descuento
         */

        SpinnerTCliente   = view.findViewById(R.id.SpinnerTCliente);
        SpinnerTRango     = view.findViewById(R.id.SpinnerTRango);
        SpinnerTDescuento = view.findViewById(R.id.SpinnerTDescuento);

        input_DescTipoCliente  = view.findViewById(R.id.input_DescTipoCliente);
        input_DescTipoRango = view.findViewById(R.id.input_DescTipoRango);
        input_DescTipoDescuento = view.findViewById(R.id.input_DescTipoDescuento);

        textViewNFC.setKeyListener(null);

        // Initialize NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        // Create pending intent for reading NFC tag
        Intent intent = new Intent(getContext(), getActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        // Create intent filters and tech lists for NFC tag reading
        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFilters = new IntentFilter[]{tagIntentFilter};
        techLists = new String[][]{new String[]{NfcA.class.getName(), NfcB.class.getName(),
                NfcF.class.getName(), NfcV.class.getName(), IsoDep.class.getName(),
                MifareClassic.class.getName(), MifareUltralight.class.getName(),
                Ndef.class.getName()}};

        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nfc          = inputNFC.getText().toString();
                placa        = inputPlaca.getText().toString();
                rucdni       = inputRucDni.getText().toString();
                razonsocial  = inputRazonSocial.getText().toString();
                direccion    = inputDireccion.getText().toString();

                if(placa.isEmpty()){
                    alertPlaca.setError("El campo Placa es obligatorio");
                    return;
                }else if(rucdni.isEmpty()){
                    alertRucDni.setError("El campo RUC o DNI es obligatorio");
                    return;
                } else if(razonsocial.isEmpty()){
                    alertRazonSocial.setError("El campo Razon Social es obligatorio");
                    return;
                }else if(direccion.isEmpty()){
                    alertDireccion.setError("El campo Dirección es obligatorio");
                    return;
                }else if(nfc.isEmpty()){
                    alertaNFC.setError("El campo NFC es obligatorio");
                    return;
                }

                alertaNFC.setErrorEnabled(false);
                alertPlaca.setErrorEnabled(false);
                alertRucDni.setErrorEnabled(false);
                alertRazonSocial.setErrorEnabled(false);
                alertDireccion.setErrorEnabled(false);

                Toast.makeText(getContext(), "Se guardo correctamente", Toast.LENGTH_SHORT).show();

                inputNFC.getText().clear();
                inputPlaca.setText("000-0000");
                inputRucDni.getText().clear();
                inputRazonSocial.getText().clear();
                inputDireccion.getText().clear();

            }
        });

        modalCliente = new Dialog(getContext());
        modalCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalCliente.setContentView(R.layout.list_productos);
        modalCliente.setCancelable(false);

        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                modalCliente.show();


                return true;
            }
        });

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
         *  Tipo de Cliente
         */

        List<TipoCliente> tipoClienteList = new ArrayList<>();

        for (int i = 0; i < 1; i++){
            tipoClienteList.add(new TipoCliente("CON","FACTURA/BOLETA"));
            tipoClienteList.add(new TipoCliente("DES","FACTURA/NOTA DESPACHO"));
        }

        Resources resTCliente = getResources();
        TipoClienteAdapter tipoClienteAdapter = new TipoClienteAdapter(getContext(), R.layout.itemcliente, (ArrayList<TipoCliente>) tipoClienteList, resTCliente);
        SpinnerTCliente.setAdapter(tipoClienteAdapter);


        SpinnerTCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoCliente tipoCliente = (TipoCliente) parent.getItemAtPosition(position);
                String descripcionCliente = tipoCliente.getDescripcion();
                if (tipoCliente.getTipocliente().equals("CON")) {
                    // Mostrar campo de entrada para "Contado"
                    input_DescTipoCliente.setText(descripcionCliente);

                } else if (tipoCliente.getTipocliente().equals("DES")) {
                    // Mostrar campo de entrada para "Descuento"
                    input_DescTipoCliente.setText(descripcionCliente);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ningún elemento
            }
        });

        /**
         *  Tipo de Rango
         */

        List<TipoRango> tipoRangoList = new ArrayList<>();

        for (int i = 0; i < 1; i++){
            tipoRangoList.add(new TipoRango("GLN","RANGO POR GALONES"));
            tipoRangoList.add(new TipoRango("SOLES","RANGO"));
        }

        Resources resTRango = getResources();
        TipoRangoAdapter tipoRangoAdapter = new TipoRangoAdapter(getContext(), R.layout.itemrango, (ArrayList<TipoRango>) tipoRangoList, resTRango);
        SpinnerTRango.setAdapter(tipoRangoAdapter);

        SpinnerTRango.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoRango tipoRango = (TipoRango) parent.getItemAtPosition(position);
                String descripcionRango = tipoRango.getDescripcion();
                if (tipoRango.getTiporango().equals("GLN")) {
                    // Mostrar campo de entrada para "Contado"
                    input_DescTipoRango.setText(descripcionRango);

                } else if (tipoRango.getTiporango().equals("SOLES")) {
                    // Mostrar campo de entrada para "Descuento"
                    input_DescTipoRango.setText(descripcionRango);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ningún elemento
            }
        });

        /**
         *  Tipo de Descuento
         */

        List<TipoDescuento> tipoDescuentoList = new ArrayList<>();

        for (int i = 0; i < 1; i++){
            tipoDescuentoList.add(new TipoDescuento("DES","MONTO DESCUENTO"));
            tipoDescuentoList.add(new TipoDescuento("PFIJO","PAGO FIJO"));
        }

        Resources resTDescuento = getResources();
        TipoDescuentoAdapter tipoDescuentoAdapter = new TipoDescuentoAdapter(getContext(), R.layout.itemdescuento, (ArrayList<TipoDescuento>) tipoDescuentoList, resTDescuento);
        SpinnerTDescuento.setAdapter(tipoDescuentoAdapter);

        SpinnerTDescuento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoDescuento tipoDescuento = (TipoDescuento) parent.getItemAtPosition(position);
                String descripcionDescuento = tipoDescuento.getDescripcion();
                if (tipoDescuento.getTipodescuento().equals("DES")) {
                    // Mostrar campo de entrada para "Contado"
                    input_DescTipoDescuento.setText(descripcionDescuento);

                } else if (tipoDescuento.getTipodescuento().equals("PFIJO")) {
                    // Mostrar campo de entrada para "Descuento"
                    input_DescTipoDescuento.setText(descripcionDescuento);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ningún elemento
            }
        });

        return view;
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
                textViewNFC.setText(nfcTag);
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