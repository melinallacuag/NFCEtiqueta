package com.example.nfcetiqueta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.Users;
import com.example.nfcetiqueta.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    Button btn;
    TextInputEditText usuario, contraseña;
    TextInputLayout alertuser,alertpassword;
    String usuarioUser,contraseñaUser;
    TextView imeii;
    List<Users> usersList;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAPIService = GlobalInfo.getAPIService();

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

                GlobalInfo.getuserID10 = "";
                GlobalInfo.getuserName10 = "";
                GlobalInfo.getuserPass10 = "";

                findUsers(usuario.getText().toString());

            }
        });
    }

    /** API SERVICE - Usuarios */
    private void findUsers(String id){

        Call<List<Users>> call = mAPIService.findUsers(id);

        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText( getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    usersList = response.body();

                    for(Users user: usersList) {

                        usuario.setText(user.getUserID());
                        GlobalInfo.getuserID10 = user.getUserID();
                        GlobalInfo.getuserName10 = user.getNames();
                        GlobalInfo.getuserPass10  = user.getPassword();
                        GlobalInfo.getuserLocked10  = user.getLocked();

                    }

                    if (GlobalInfo.getuserLocked10 == false) {
                        Toast.makeText( getApplicationContext(), "El usuario se encuentra bloqueado", Toast.LENGTH_SHORT).show();
                    }else {

                        String getName = usuario.getText().toString();
                        String getPass = checkpassword(contraseña.getText().toString());

                        if( getName.equals(GlobalInfo.getuserName10)  || getPass.equals(GlobalInfo.getuserPass10)){

                            Toast.makeText( getApplicationContext(), "Bienvenido al Sistema SVEN", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent( getApplicationContext(),Menu.class));

                        }
                        else {
                            Toast.makeText( getApplicationContext(), "El usuario o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                        }

                    }

                }catch (Exception ex){
                    Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText( getApplicationContext(), "Error de conexión APICORE Users - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /** Hash de la Contraseña */
    private String checkpassword(String clave){

        String lResult = "";
        String lasc1 = "";

        int lValor = 0;
        int lTam = 0;
        int lCar = 0;
        int lasc2 = 0;

        lTam = clave.length();

        for(int lcont = 1 ; lcont <= lTam; lcont += 1){

            switch (lcont){
                case 1:
                    lCar = 1;
                    lasc1 = clave.substring(0,1);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 2:
                    lCar = 3;
                    lasc1 = clave.substring(1,2);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 3:
                    lCar = 5;
                    lasc1 = clave.substring(2,3);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 4:
                    lCar = 7;
                    lasc1 = clave.substring(3,4);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 5:
                    lCar = 9;
                    lasc1 = clave.substring(4,5);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 6:
                    lCar = 11;
                    lasc1 = clave.substring(5,6);
                    lasc2 = lasc1.charAt(0);
                    break;
            }

            lValor = lValor + lasc2 * lCar;

        }

        lResult = String.valueOf(lValor);

        return lResult;
    }

}