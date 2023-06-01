package com.example.nfcetiqueta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcetiqueta.Adapter.LCompanyAdapter;
import com.example.nfcetiqueta.Adapter.TipoClienteAdapter;
import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.LCompany;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;
import com.example.nfcetiqueta.WebApiSVEN.Models.Users;
import com.example.nfcetiqueta.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    List<Users> usersList;

    Button btnIniciarLogin;
    TextInputEditText inputUsuario, inputContraseña;
    TextInputLayout alertUser,alertPassword;
    String usuarioUser,contraseñaUser;

    LCompanyAdapter lCompanyAdapter;

    Spinner SpinnerCompany;

    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAPIService = GlobalInfo.getAPIService();

        btnIniciarLogin     = findViewById(R.id.btnlogin);
        inputUsuario        = findViewById(R.id.usuario);
        inputContraseña     = findViewById(R.id.contraseña);
        alertUser           = findViewById(R.id.textusuario);
        alertPassword       = findViewById(R.id.textcontraseña);

        SpinnerCompany      = findViewById(R.id.SpinnerCompany);

        btnIniciarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuarioUser    = inputUsuario.getText().toString();
                contraseñaUser = inputContraseña.getText().toString();

                if(usuarioUser.isEmpty()){
                    alertUser.setError("El campo usuario es obligatorio");
                    return;
                }else if(contraseñaUser.isEmpty()){
                    alertPassword.setError("El campo contraseña es obligatorio");
                    return;
                }

                alertUser.setErrorEnabled(false);
                alertPassword.setErrorEnabled(false);

                GlobalInfo.getuserID10 = "";
                GlobalInfo.getuserName10 = "";
                GlobalInfo.getuserPass10 = "";

                findUsers(inputUsuario.getText().toString());

            }
        });

        /** Mostrar el listado de Datos */
        getCompany();
        getListProductos();
        getTipoCliente();
        getTipoRango();
        getTipoDescuento();
    }

    /** API SERVICE - Empresa */
    private void getCompany(){

        Call<List<LCompany>> call = mAPIService.getCompany();

        call.enqueue(new Callback<List<LCompany>>() {
            @Override
            public void onResponse(Call<List<LCompany>> call, Response<List<LCompany>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.getlistacompanyList10 = response.body();

                    for(LCompany lCompany: GlobalInfo.getlistacompanyList10){

                        GlobalInfo.getIdCompany10     = Integer.valueOf(String.valueOf(lCompany.getCompanyID()));
                        GlobalInfo.getNameCompany10    = String.valueOf(lCompany.getNames());
                        GlobalInfo.getBranchCompany10  = String.valueOf(lCompany.getBranch());
                        GlobalInfo.getSloganCompany10  = String.valueOf(lCompany.getEslogan());
                    }

                    /**
                     *  Seleccionar Tipo de Company
                     */

                    Resources resTCompany = getResources();
                    lCompanyAdapter = new LCompanyAdapter(getApplicationContext(), R.layout.itemcompany, (ArrayList<LCompany>) GlobalInfo.getlistacompanyList10, resTCompany);
                    SpinnerCompany.setAdapter(lCompanyAdapter);

                    SpinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LCompany>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de conexión APICORE - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** API SERVICE - Users */
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

                        inputUsuario.setText(user.getUserID());
                        GlobalInfo.getuserID10 = user.getUserID();
                        GlobalInfo.getuserName10 = user.getNames();
                        GlobalInfo.getuserPass10  = user.getPassword();
                        GlobalInfo.getuserLocked10  = user.getLocked();

                    }

                    if (GlobalInfo.getuserLocked10 == false) {
                        Toast.makeText( getApplicationContext(), "El usuario se encuentra bloqueado", Toast.LENGTH_SHORT).show();
                    }else {

                        String getName = inputUsuario.getText().toString();
                        String getPass = checkpassword(inputContraseña.getText().toString());

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

    /** Listado de Productos */
    private void getListProductos(){

        Call<List<LProductos>> call = mAPIService.getLProductos();

        call.enqueue(new Callback<List<LProductos>>() {
            @Override
            public void onResponse(Call<List<LProductos>> call, Response<List<LProductos>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.getproductosList10 = response.body();

                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LProductos>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de conexión APICORE Tarjetas - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Spinner de Tipo de Cliente */
    private void getTipoCliente(){

        Call<List<TipoCliente>> call = mAPIService.getTipoCliente();

        call.enqueue(new Callback<List<TipoCliente>>() {
            @Override
            public void onResponse(Call<List<TipoCliente>> call, Response<List<TipoCliente>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.gettipoclienteList10 = response.body();

                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoCliente>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de conexión APICORE Tarjetas - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Spinner de Tipo de Rango */
    private void getTipoRango(){

        Call<List<TipoRango>> call = mAPIService.getTipoRango();

        call.enqueue(new Callback<List<TipoRango>>() {
            @Override
            public void onResponse(Call<List<TipoRango>> call, Response<List<TipoRango>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.gettiporangoList10 = response.body();

                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoRango>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de conexión APICORE Tarjetas - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Spinner de Tipo de Descuento */
    private void getTipoDescuento(){

        Call<List<TipoDescuento>> call = mAPIService.getTipoDescuento();

        call.enqueue(new Callback<List<TipoDescuento>>() {
            @Override
            public void onResponse(Call<List<TipoDescuento>> call, Response<List<TipoDescuento>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GlobalInfo.gettipodescuentoList10 = response.body();

                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoDescuento>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de conexión APICORE Tarjetas - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }
}