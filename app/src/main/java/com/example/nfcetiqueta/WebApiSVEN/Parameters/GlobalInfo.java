package com.example.nfcetiqueta.WebApiSVEN.Parameters;

import com.example.nfcetiqueta.WebApiSVEN.Controllers.APIService;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientePuntos;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientes;
import com.example.nfcetiqueta.WebApiSVEN.Models.LCompany;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;

import java.util.List;

public class GlobalInfo {

    /**
     * Clases de Listado
     */
    public static List<TipoCliente>   gettipoclienteList10;
    public static List<TipoRango>     gettiporangoList10;
    public static List<TipoDescuento> gettipodescuentoList10;
    public static List<LProductos>    getproductosList10;
    public static List<LClientes>     getlistaclienteList10;
    public static List<LCompany>      getlistacompanyList10;
    public static List<LClienteAfiliados>  getlistaclienteafiliadoList10;
    public static List<LClientePuntos>  getlistaclientepuntosList10;

    /**
     * Datos de la Empresa
     */
    public static Integer getGetIdCompany10;
    public static String  getNameCompany10;
    public static String  getBranchCompany10;
    public static String  getSloganCompany10;
    public static String  getAddressCompany10;

    /**
     * Datos del Campo User - Usuario
     */
    public static String  getuserID10;
    public static String  getuserName10;
    public static String  getuserPass10;
    public static Boolean getuserLocked10;
    public static Boolean getuserCancelar10;

    /**
     * Datos del Cliente RUC-DNI
     */
    public static String  getclienteRZ10;

    /**
     * Datos de la Empresa por Seleccion de Campo Descuentos
     */
    public static String getnfcId10 = String.valueOf(-1);
    public static String getRFiD10;
    public static String getArticuloID10;

    /**
     * Datos de la Empresa por Seleccion de Campo Puntos
     */
    public static String getRFIDPuntos10 = String.valueOf(-1);
    public static String getclienteId10 = String.valueOf(-1);
    public static String getClienteRFIDPuntos10;
    public static String getClienteIDPuntos10;
    public static String getClienteRZPuntos10;
    public static Boolean getClienteStatusPuntos10;
    public static Boolean getClienteStatusActivadoPuntos10 = true;
    public static Boolean getClienteStatusDesactivadoPuntos10 = false;

    /**
     * URL de la APIService
     */
    //public static final String BASE_URL = "http://4-fact.com:8081/";
    //public static final String BASE_URL = "http://192.168.1.227:8081/";
    //public static final String BASE_URL = "http://192.168.1.11:8081/";
    //public static final String BASE_URL = "http://192.168.1.14:8081/";
    public static final String BASE_URL = "http://192.168.1.245:8081/";
    //public static final String BASE_URL = "http://192.168.18.43:8081/";
    //public static final String BASE_URL = "http://192.168.18.33:8081/";
    //public static final String BASE_URL = "http://192.168.18.20:8081/";
    //public static final String BASE_URL = "http://192.168.1.245:8082/";
    //public static final String BASE_URL = "http://192.168.1.19:8081/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
