package com.example.nfcetiqueta.WebApiSVEN.Parameters;

import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientes;
import com.example.nfcetiqueta.WebApiSVEN.Models.LCompany;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;

import java.util.List;

public class GlobalInfo {

    public static List<TipoCliente>   gettipoclienteList10;
    public static List<TipoRango>     gettiporangoList10;
    public static List<TipoDescuento> gettipodescuentoList10;
    public static List<LProductos>    getproductosList10;
    public static List<LClientes>     getlistaclienteList10;
    public static List<LCompany>      getlistacompanyList10;
    public static List<LClienteAfiliados>      getlistaclienteafiliadoList10;

    /**
     * Datos de la Empresa
     */
    public static Integer getIdCompany10;
    public static String getNameCompany10;
    public static String getBranchCompany10;
    public static String getSloganCompany10;

    public static String getnfcIdLClienteAfiliado10;

    /**
     * Datos del Campo User - Usuario
     */
    public static String  getuserID10;
    public static String  getuserName10;
    public static String  getuserPass10;
    public static Boolean getuserLocked10;

    /**
     * Datos del Cliente RUC-DNI
     */
    public static String  getclienteRZ10;

    /**
     * URL de la APIService
     */
    public static final String BASE_URL = "http://192.168.1.2:8081/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
