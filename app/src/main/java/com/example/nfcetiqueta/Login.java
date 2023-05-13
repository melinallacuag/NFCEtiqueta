package com.example.nfcetiqueta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    Button btn;
    TextInputEditText usuario, contraseña;
    TextInputLayout alertuser,alertpassword;
    String usuarioUser,contraseñaUser;
    TextView imeii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn            = findViewById(R.id.btnlogin);
        usuario        = findViewById(R.id.usuario);
        contraseña     = findViewById(R.id.contraseña);
        alertuser      = findViewById(R.id.textusuario);
        alertpassword  = findViewById(R.id.textcontraseña);
        imeii          = findViewById(R.id.imei);

        imeii.setText(ObtenerIMEI.getDeviceId(getApplicationContext()));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuarioUser    = usuario.getText().toString();
                contraseñaUser = contraseña.getText().toString();

                if(usuarioUser.isEmpty()){
                    alertuser.setError("El campo usuario es obligatorio");
                    return;
                }else if(contraseñaUser.isEmpty()){
                    alertpassword.setError("El campo contraseña es obligatorio");
                    return;
                }

                alertuser.setErrorEnabled(false);
                alertpassword.setErrorEnabled(false);

                startActivity(new Intent( getApplicationContext(),Menu.class));

            }
        });
    }
}