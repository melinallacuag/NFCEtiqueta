package com.example.nfcetiqueta.WebApiSVEN.Parameters;

import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;

public class GlobalInfo {

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
