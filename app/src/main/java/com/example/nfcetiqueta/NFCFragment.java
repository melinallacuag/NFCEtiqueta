package com.example.nfcetiqueta;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NFCFragment extends Fragment implements NfcAdapter.ReaderCallback {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techLists;
    private TextInputEditText textViewNFC;

    String nfc, placa,rucdni,razonsocial,direccion;

    Button btnagregar;
    TextInputEditText inputNFC, inputPlaca, inputRucDni,inputRazonSocial, inputDireccion;
    TextInputLayout alertaNFC, alertPlaca,alertRucDni,alertRazonSocial,alertDireccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_n_f_c, container, false);

        textViewNFC = view.findViewById(R.id.input_EtiquetaNFC);
        btnagregar  = view.findViewById(R.id.btnAgregar);

        inputNFC          = view.findViewById(R.id.input_EtiquetaNFC);
        inputPlaca        = view.findViewById(R.id.input_Placa);
        inputRucDni       = view.findViewById(R.id.input_ruc_dni);
        inputRazonSocial  = view.findViewById(R.id.input_RazonSocial);
        inputDireccion    = view.findViewById(R.id.input_Direccion);

        alertaNFC         = view.findViewById(R.id.textNFC);
        alertPlaca        = view.findViewById(R.id.textPlaca);
        alertRucDni       = view.findViewById(R.id.text_RUC_DNI);
        alertRazonSocial  = view.findViewById(R.id.text_RazonSocial);
        alertDireccion    = view.findViewById(R.id.text_Direccion);

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