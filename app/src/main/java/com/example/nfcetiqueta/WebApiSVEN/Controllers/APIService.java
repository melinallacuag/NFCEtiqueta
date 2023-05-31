package com.example.nfcetiqueta.WebApiSVEN.Controllers;

import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;
import com.example.nfcetiqueta.WebApiSVEN.Models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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


}
