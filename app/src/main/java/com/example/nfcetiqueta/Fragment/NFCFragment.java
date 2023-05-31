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
            descripcion_prodcuto,codigo_producto;

    Button btnagregar,btncancelar;

    Dialog modalProducto;
    LProductosAdapter lProductosAdapter;
    RecyclerView recyclerLProducto;

    TipoClienteAdapter tipoClienteAdapter;
    TipoRangoAdapter tipoRangoAdapter;
    TipoDescuentoAdapter tipoDescuentoAdapter;

    Spinner SpinnerTCliente,SpinnerTRango,SpinnerTDescuento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_n_f_c, container, false);

        btnagregar  = view.findViewById(R.id.btnAgregar);

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
        pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

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

                nfc          = inputNFC.getText().toString();
                placa        = inputPlaca.getText().toString();
                rucdni       = inputRucDni.getText().toString();
                razonsocial  = inputRazonSocial.getText().toString();
                codproducto  = input_CodProducto.getText().toString();
                descproducto = input_DescProducto.getText().toString();
                rgInicial    = input_RangoInicial.getText().toString();
                rgFinal      = input_RangoFinal.getText().toString();
                descuentoGl  = input_DescGalon.getText().toString();

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

                alertaNFC.setErrorEnabled(false);
                alertPlaca.setErrorEnabled(false);
                alertRucDni.setErrorEnabled(false);
                alertRazonSocial.setErrorEnabled(false);
                alertCodProducto.setErrorEnabled(false);
                alertDescProducto.setErrorEnabled(false);
                alertRangoInicial.setErrorEnabled(false);
                alertRangoFinal.setErrorEnabled(false);
                alertDescGalon.setErrorEnabled(false);

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

               TipoCliente tipoCliente = (TipoCliente) parent.getItemAtPosition(position);
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
                TipoRango tipoRango = (TipoRango) parent.getItemAtPosition(position);
                String descripcionRango = tipoRango.getDescripcion();
                if (tipoRango.getId().equals("GLN")) {
                    // Mostrar campo de entrada para "Contado"
                    input_DescTipoRango.setText(descripcionRango);

                } else if (tipoRango.getId().equals("MTO")) {
                    // Mostrar campo de entrada para "Descuento"
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
                TipoDescuento tipoDescuento = (TipoDescuento) parent.getItemAtPosition(position);
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