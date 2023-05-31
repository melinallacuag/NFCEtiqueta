package com.example.nfcetiqueta.WebApiSVEN.Parameters;

import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;

import java.util.List;

public class GlobalInfo {

    public static List<TipoCliente>   gettipoclienteList10;
    public static List<TipoRango>     gettiporangoList10;
    public static List<TipoDescuento> gettipodescuentoList10;

    /**
     * Datos del Campo User - Usuario
     */
    public static String  getuserID10;
    public static String  getuserName10;
    public static String  getuserPass10;
    public static Boolean getuserLocked10;

    /**
     * URL de la APIService
     */
    public static final String BASE_URL = "http://192.168.1.2:8081/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
