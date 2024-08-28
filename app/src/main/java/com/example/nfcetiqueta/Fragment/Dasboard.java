package com.example.nfcetiqueta.Fragment;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nfcetiqueta.Login;
import com.example.nfcetiqueta.NFCUtil;
import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;

public class Dasboard extends Fragment {

    TextView nombre_grifero,nombre_empresa,sucursal_empresa,slogan_empresa;
    CardView btn_Registrar,btn_Listado,btn_Salir,btn_RegistrarPuntos,btn_ListadoPuntos;
    Button btncancelarsalida,btnsalir;
    Dialog modalSalir;
    ShapeableImageView img_Logo;

    private NFCUtil nfcUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcUtil = new NFCUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dasboard, container, false);

        nombre_grifero    = view.findViewById(R.id.nombre_grifero);
        nombre_empresa    = view.findViewById(R.id.nombre_empresa);
        sucursal_empresa  = view.findViewById(R.id.sucursal_empresa);
        slogan_empresa    = view.findViewById(R.id.slogan_empresa);
        btn_Registrar     = view.findViewById(R.id.btn_Registrar);
        btn_RegistrarPuntos   = view.findViewById(R.id.btn_RegistrarPuntos);
        btn_Listado       = view.findViewById(R.id.btn_Listado);
        btn_Salir         = view.findViewById(R.id.btnSalir);
        img_Logo          = view.findViewById(R.id.logo_dashboard);
        btn_ListadoPuntos = view.findViewById(R.id.btn_ListadoPuntos);

        /** Nombre del usuario logeado */
        nombre_grifero.setText(GlobalInfo.getuserName10);

        /**
         * @MOSTRAR:LogoEmpresa_Dasboard
         */
        String rutaImagen = "/storage/emulated/0/appSven/";

        String nameRuta = "logo.jpg";

        if (!TextUtils.isEmpty(nameRuta)) {
            rutaImagen += nameRuta;
            File file = new File(rutaImagen);
            if (!file.exists()) {
                rutaImagen = "/storage/emulated/0/appSven/sinlogo.jpg";
            }
        } else {
            rutaImagen += "sinlogo.jpg";
        }

        Uri imagenProd = Uri.parse("file://" + rutaImagen);
        img_Logo.setImageURI(imagenProd);

        /**
         * @OBTENER:DatoGeneralCompania
         */
        String DirSucursal = "";
        if (GlobalInfo.getBranchCompany10 != null && !GlobalInfo.getBranchCompany10.isEmpty()) {
            DirSucursal = GlobalInfo.getBranchCompany10.replace("-", "");
            sucursal_empresa.setText(DirSucursal);
        } else {

            if (GlobalInfo.getAddressCompany10 != null && !GlobalInfo.getAddressCompany10.isEmpty()) {
                DirSucursal = GlobalInfo.getAddressCompany10.replace("-", "");
                sucursal_empresa.setText(DirSucursal);
            }else {
                sucursal_empresa.setText("");
            }

        }

        String NameCompany = "";
        if (GlobalInfo.getNameCompany10 != null && !GlobalInfo.getNameCompany10.isEmpty()) {
            NameCompany = GlobalInfo.getNameCompany10;
            nombre_empresa.setText(NameCompany);
        } else {
            nombre_empresa.setText("");
        }

        String SloganCompany = "";
        if (GlobalInfo.getSloganCompany10 != null && !GlobalInfo.getSloganCompany10.isEmpty()) {
            SloganCompany = GlobalInfo.getSloganCompany10;
            slogan_empresa.setText(SloganCompany);
        } else {
            slogan_empresa.setText("");
        }

        /** **/
        btn_RegistrarPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManagerRegistrarPuntos = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionRegistrarPuntos = fragmentManagerRegistrarPuntos.beginTransaction();
                int fragmentContainerRegistrar = R.id.fragment_container;
                PuntosFragment puntosFragment = new PuntosFragment();

                fragmentTransactionRegistrarPuntos.replace(fragmentContainerRegistrar, puntosFragment);
                fragmentTransactionRegistrarPuntos.addToBackStack(null);
                fragmentTransactionRegistrarPuntos.commit();

            }
        });

        /** Ir a la Pantalla - Registrar Clientes Afiliados */
        btn_Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManagerRegistrar = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionRegistrar = fragmentManagerRegistrar.beginTransaction();
                int fragmentContainerRegistrar = R.id.fragment_container;
                NFCFragment nfcFragment = new NFCFragment();

                fragmentTransactionRegistrar.replace(fragmentContainerRegistrar, nfcFragment);
                fragmentTransactionRegistrar.addToBackStack(null);
                fragmentTransactionRegistrar.commit();

            }
        });

        /** Ir a la Pantalla - Listado de Clientes Afiliados */
        btn_Listado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManagerListado = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionListado = fragmentManagerListado.beginTransaction();
                int fragmentContainerListado = R.id.fragment_container;
                ListadoRegistroFragment listadoRegistroFragment = new ListadoRegistroFragment();

                fragmentTransactionListado.replace(fragmentContainerListado, listadoRegistroFragment);
                fragmentTransactionListado.addToBackStack(null);
                fragmentTransactionListado.commit();

            }
        });

        btn_ListadoPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManagerListaPuntos = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionListaPunto = fragmentManagerListaPuntos.beginTransaction();
                int fragmentContainerListaPunto = R.id.fragment_container;
                ListaPuntosFragment listaPuntosFragment = new ListaPuntosFragment();

                fragmentTransactionListaPunto.replace(fragmentContainerListaPunto, listaPuntosFragment);
                fragmentTransactionListaPunto.addToBackStack(null);
                fragmentTransactionListaPunto.commit();
            }
        });

        /** Mostrar Modal - Salir */
        modalSalir = new Dialog(getContext());
        modalSalir.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalSalir.setContentView(R.layout.fragment_salir);
        modalSalir.setCancelable(false);

        btn_Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modalSalir.show();

                btncancelarsalida    = modalSalir.findViewById(R.id.btncancelarsalida);
                btnsalir     = modalSalir.findViewById(R.id.btnsalir);

                btncancelarsalida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalSalir.dismiss();
                    }
                });

                btnsalir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Intent intent = new Intent(getContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finalize();
                            Toast.makeText(getContext(), "SE CERRO SESIÓN", Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });

        return view;
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