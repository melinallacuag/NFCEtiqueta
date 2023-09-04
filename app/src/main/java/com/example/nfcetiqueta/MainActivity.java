package com.example.nfcetiqueta;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnadelante;

    private NFCUtil nfcUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcUtil = new NFCUtil(this);

        btnadelante = findViewById(R.id.btn);

        btnadelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getApplicationContext(),Login.class));
            }
        });
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