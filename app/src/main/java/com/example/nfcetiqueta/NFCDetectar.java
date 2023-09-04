package com.example.nfcetiqueta;
import android.app.Activity;
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
import android.widget.Toast;

public class NFCDetectar implements NfcAdapter.ReaderCallback{

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techLists;
    private Activity activity;

    public NFCDetectar(Activity activity){
        this.activity = activity;

        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);

        Intent intent = new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFilters = new IntentFilter[]{tagIntentFilter};

        techLists = new String[][]{
                new String[]{NfcA.class.getName(), NfcB.class.getName(),
                        NfcF.class.getName(), NfcV.class.getName(), IsoDep.class.getName(),
                        MifareClassic.class.getName(), MifareUltralight.class.getName(),
                        Ndef.class.getName()}
        };
    }
    public void onResume() {
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(activity, this,
                    NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B |
                            NfcAdapter.FLAG_READER_NFC_F | NfcAdapter.FLAG_READER_NFC_V, null);
        }
    }

    public void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(activity);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        String nfcTag = ByteArrayToHexString(tag.getId());

        Toast.makeText(activity, "NFC"+ nfcTag, Toast.LENGTH_SHORT).show();

    }

    private String ByteArrayToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(byteArray.length * 2);
        for (byte b : byteArray) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
