package com.example.nfcetiqueta.WebApiSVEN.Controllers;

import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientePuntos;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientes;
import com.example.nfcetiqueta.WebApiSVEN.Models.LCompany;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;
import com.example.nfcetiqueta.WebApiSVEN.Models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    /**
     * @User
     * @Conseguir el identificador "userID" - "05070608"
     */

    @GET("api/users/listado/{id}")
    Call<List<Users>> findUsers(@Path("id") String id);

    /**
     * @TipoProducto
     * @Listado de los productos
     */

    @GET("api/clientepreciocnf/listado/tproducto")
    Call<List<LProductos>> getLProductos();

    /**
     * @TipoCliente
     * @Seleccción de Tipos de Clientes
     */

    @GET("api/clientepreciocnf/listado/tcliente")
    Call<List<TipoCliente>> getTipoCliente();

    /**
     * @TipoRango
     * @Seleccción de Tipos de Rangos
     */

    @GET("api/clientepreciocnf/listado/trango")
    Call<List<TipoRango>> getTipoRango();

    /**
     * @TipoDescuento
     * @Seleccción de Tipos de Descuentos
     */

    @GET("api/clientepreciocnf/listado/tdescuento")
    Call<List<TipoDescuento>> getTipoDescuento();

    /**
     * @Cliente_DNI
     * @Buscar Cliente con DNI
     */

    @GET("api/cliente/listado/DNI/{id}")
    Call<List<LClientes>> findClienteDNI(@Path("id") String id);

    /**
     * @Cliente_RUC
     * @Buscar Cliente con RUC
     */

    @GET("api/cliente/listado/RUC/{id}")
    Call<List<LClientes>> findClienteRUC(@Path("id") String id);

    /**
     * @Company
     * @Listado de companias - Nombre
     */

    @GET("api/company/listado")
    Call<List<LCompany>> getCompany();

    /**
     * @ClientePrecio-Listado
     * @Listado de clientes afiliados - NFC && IDCOMPANY
     */

    @GET("api/clienteprecio/listado/{nfcId}/{comapyId}")
    Call<List<LClienteAfiliados>> findClienteAfiliado(@Path("nfcId") String nfcId,@Path("comapyId") Integer comapyId);

    /**
     * @ClientePrecio-Guardar
     * @Guardar clientes afiliados
     */

    @POST("api/clienteprecio/guardar")
    Call<LClienteAfiliados> postClienteAfiliados(@Body LClienteAfiliados lClienteAfiliados);

    /**
     * @ClientePrecio-Eliminar
     * @Eliminar clientes afiliados
     */

    @POST("api/ClientePrecio/Eliminar/{RFID}/{CompanyID}/{ArticuloID}")
    Call<LClienteAfiliados> postClienteAfiliadosEliminado(@Path("RFID") String RFID,@Path("CompanyID") Integer CompanyID,@Path("ArticuloID") String ArticuloID);

    /**
     * @ClientePuntos-Guardar
     * @GuardarClientesPuntos
     */
    @POST("api/ClientePuntos/Guardar")
    Call<LClientePuntos> postClientePuntos(@Body LClientePuntos lClientePuntos);

    /**
     * @ClientePuntos-Listado
     * @ListadoClientesPuntos
     */

    @GET("api/ClientePuntos/Listado/{RFID}/{CompanyId}")
    Call<List<LClientePuntos>> findClientePuntos(@Path("RFID") String RFID,@Path("CompanyId") Integer CompanyId);

    /**
     * @ClientePuntos-Eliminar
     * @EliminarClientesPuntos
     */

    @POST("api/ClientePuntos/Eliminar/{RFID}/{ClienteID}/{CompanyID}/{UserID}")
    Call<LClientePuntos> postClientePuntosEliminado(@Path("RFID") String RFID,@Path("ClienteID") String ClienteID,@Path("CompanyID") Integer CompanyID,@Path("UserID") Integer UserID);

    /**
     * @ClientePuntos-UpdateStatus
     * @EliminarClientesPuntos
     */

    @POST("api/ClientePuntos/UpdateStatus/{RFID}/{ClienteID}/{CompanyID}/{Status}")
    Call<LClientePuntos> postClientePuntosUpdateStatus(@Path("RFID") String RFID,@Path("ClienteID") String ClienteID,@Path("CompanyID") Integer CompanyID,@Path("Status") Boolean Status);


    /**
     * @ClientePuntos-Listado
     * @ListadoClientesPuntos
     */

    @GET("api/ClientePuntos/Listado2/{RFID}/{ClienteID}/{CompanyId}")
    Call<List<LClientePuntos>> findClienteArticulosPuntos(@Path("RFID") String RFID,@Path("ClienteID") String ClienteID,@Path("CompanyId") Integer CompanyId);

    /**
     * @ClientePuntos-Eliminar2
     * @EliminarClientesPuntos
     */

    @POST("api/ClientePuntos/Eliminar2/{RFID}/{ClienteID}/{CompanyID}/{UserID}")
    Call<LClientePuntos> postClientePuntosEliminado2(@Path("RFID") String RFID,@Path("ClienteID") String ClienteID,@Path("CompanyID") Integer CompanyID,@Path("UserID") Integer UserID);

}
