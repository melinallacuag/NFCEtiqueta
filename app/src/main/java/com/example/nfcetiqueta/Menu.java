package com.example.nfcetiqueta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nfcetiqueta.Fragment.NFCFragment;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NFCFragment()).commit();
    }
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NFCFragment()).commit();
        }

    }
}