package com.quantum.mq08.conectividad;

import com.quantum.mq08.parseo.Cuerpo;
import com.quantum.mq08.parseo.Cuerpo2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Conexion {

    //logueo
    @POST("/jderest/v3/orchestrator/MQ0802A_ORCH")
    Call<Cuerpo2> getUser(@Body Cuerpo2 users );

    //DESPLEGABLE
    @POST("/jderest/v3/orchestrator/MQ0801D_ORCH")
    Call<Cuerpo2> getDesplegable(@Body Cuerpo2 users );

    //DESPLEGABLE2
    @POST("/jderest/v3/orchestrator/MQ0801L_ORCH")
    Call<Cuerpo2> getDesplegable2(@Body Cuerpo2 users );

    //enviar
    @POST("jderest/v3/orchestrator/MQ0802A_ORCH")
    Call<Cuerpo> getEnviar(@Body Cuerpo users );
    //enviar
    @POST("/jderest/v3/orchestrator/MQ0803A_ORCH")
    Call<Cuerpo> getEnviar3(@Body Cuerpo users );
    //enviar
    @POST("/jderest/v3/orchestrator/MQ0804A_ORCH")
    Call<Cuerpo> getEnviar4(@Body Cuerpo users );
    //enviar
    @POST("/jderest/v3/orchestrator/MQ0805A_ORCH")
    Call<Cuerpo> getEnviar5(@Body Cuerpo users );
    //enviar
    @POST("/jderest/v3/orchestrator/MQ0806A_ORCH")
    Call<Cuerpo> getEnviar6(@Body Cuerpo users );
    //enviar
    @POST("/jderest/v3/orchestrator/MQ0807A_ORCH")
    Call<Cuerpo> getEnviar7(@Body Cuerpo users );
    //enviar
    @POST("/jderest/v3/orchestrator/MQ0808A_ORCH")
    Call<Cuerpo> getEnviar8(@Body Cuerpo users );
    //enviar
    @POST("/jderest/v3/orchestrator/MQ0809A_ORCH")
    Call<Cuerpo> getEnviar9(@Body Cuerpo users );


}
