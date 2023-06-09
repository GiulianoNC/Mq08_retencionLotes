package com.quantum.mq08.database;


import static com.quantum.mq08.mq08.LoginActivity.contraseñaGlobal;
import static com.quantum.mq08.mq08.LoginActivity.direc;
import static com.quantum.mq08.mq08.LoginActivity.estadoGlobal;
import static com.quantum.mq08.mq08.LoginActivity.restGlobal;
import static com.quantum.mq08.mq08.LoginActivity.sucursalGlobal;
import static com.quantum.mq08.mq08.LoginActivity.usuarioGlobal;
import static com.quantum.mq08.mq08.PrimeraPantalla.limpiezaGlobal;
import static com.quantum.mq08.mq08.PrimeraPantalla.mostrarGlobal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.quantum.mq08.conectividad.Conexion;
import com.quantum.mq08.entidades.Datos;
import com.quantum.mq08.mq08.VerActivity;
import com.quantum.mq08.parseo.Cuerpo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DbDatos extends DbHelper {
    public static int actualizar = 0;

    int id = 0;
    static Context context;

    public DbDatos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertaDatos(String deposito, String retencion, String item, String lote, String resultado) {
        long id = 0;
        try {
            DbHelper dbhelper = new DbHelper(context);
            SQLiteDatabase sqLiteDatabase = dbhelper.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put("deposito", deposito);
            values.put("retencion", retencion);
            values.put("item", item);
            values.put("lote", lote);
            values.put("resultado", resultado);


            id = sqLiteDatabase.insert(NOMBRE_TABLA, null, values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return id;
    }



    public ArrayList<Datos> mostrarDatos() {
        DbDatos dbDatos = new DbDatos(context);
        SQLiteDatabase sqLiteDatabase = dbDatos.getWritableDatabase();

        ArrayList<Datos> listaDatos = new ArrayList<>();
        Datos datos;
        Cursor cursorDatos;

        //Consultar los datos
        cursorDatos = sqLiteDatabase.rawQuery("SELECT * FROM " + NOMBRE_TABLA + " ORDER BY deposito ASC", null);

        if (cursorDatos.moveToFirst()) {
            do {
                datos = new Datos();
                datos.setId(cursorDatos.getInt(0));
                datos.setDeposito(cursorDatos.getString(1));
                datos.setRetencion(cursorDatos.getString(2));
                datos.setItem(cursorDatos.getString(3));
                datos.setLote(cursorDatos.getString(4));
                datos.setResultado(cursorDatos.getString(5));

                listaDatos.add(datos);
            } while (cursorDatos.moveToNext());
        }
        cursorDatos.close();
        return listaDatos;
    }

    public ArrayList<Datos> enviarDatos() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Datos> listaDatos = new ArrayList<>();
        Datos datos;
        Cursor cursorDatos;

        //Consultar los datos
        cursorDatos = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA + " ORDER BY deposito ASC", null);
        if (cursorDatos.moveToFirst()) {
            do {
                datos = new Datos();
                datos.setId(cursorDatos.getInt(0));
                datos.setDeposito(cursorDatos.getString(1));
                datos.setRetencion(cursorDatos.getString(2));
                datos.setItem(cursorDatos.getString(3));
                datos.setLote(cursorDatos.getString(4));
                datos.setResultado(cursorDatos.getString(5));

                listaDatos.add(datos);

                //llamo a retrofit
                //agregado
                //idGlobal = datos.setId(Integer.valueOf(cursorDatos.getString(0)));
                String DespositoString = datos.setDeposito(cursorDatos.getString(1));
                String RetencionString = datos.setRetencion(cursorDatos.getString(2));

                String ItemString = datos.setItem(cursorDatos.getString(3));
                String LoteString = datos.setLote(cursorDatos.getString(4));

                int idInt = datos.setId(cursorDatos.getInt(0));

                if(restGlobal.equals("1") || restGlobal.equals("")|| restGlobal.equals("2")){
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(500, TimeUnit.SECONDS)
                            .connectTimeout(500, TimeUnit.SECONDS)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                    Call<Cuerpo> call = conexion.getEnviar(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode == 200) {
                                Cuerpo cuerpo = response.body();
                                String estado =cuerpo.getJdeStatus();
                                editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                actualizar= 1;
                                eliminarDato(idInt);
                               // limpiezaGlobal = 1;
                                mostrarGlobal ="1";
                                mostrarDatos();

                                }else if (statusCode != 200) {
                                editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                mostrarDatos();
                                }else{
                                    Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else  if(restGlobal.equals("3") ){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                    Call<Cuerpo> call = conexion.getEnviar3(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                String estado = cuerpo.getJdeStatus();

                                if (estado.equals("SUCCESS")) {

                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    limpiezaGlobal = 1;
                                    mostrarGlobal ="1";
                                    mostrarDatos();
                                } else if (estado.equals("ERROR")){
                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                    Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                    mostrarDatos();
                                    limpiezaGlobal = 1;
                                    mostrarGlobal ="1";
                                }else{
                                    Toast.makeText(context, " error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
                } else  if(restGlobal.equals("4") ){
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(direc)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        Conexion conexion = retrofit.create(Conexion.class);

                        Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                        Call<Cuerpo> call = conexion.getEnviar4(login);
                        call.enqueue(new Callback<Cuerpo>() {
                            @Override
                            public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                                int statusCode = response.code();

                                if (statusCode <= 200) {
                                    Cuerpo cuerpo = response.body();

                                    boolean estado = Boolean.parseBoolean(cuerpo.getJdeStatus());

                                    if (estado == true) {

                                        editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                        Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                        limpiezaGlobal = 1;
                                        mostrarDatos();
                                    } else if (estado == false){
                                        editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                        Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                        mostrarDatos();
                                    }else{
                                        Toast.makeText(context, " error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (statusCode != 200) {
                                    Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                                    ;
                                }
                            }

                            @Override
                            public void onFailure(Call<Cuerpo> call, Throwable t) {
                                Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                                ;
                            }
                        });
                    }else  if(restGlobal.equals("5") ){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                    Call<Cuerpo> call = conexion.getEnviar5(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = Boolean.parseBoolean(cuerpo.getJdeStatus());

                                if (estado == true) {

                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    limpiezaGlobal = 1;
                                    mostrarDatos();
                                } else if (estado == false){
                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                    Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                    mostrarDatos();
                                }else{
                                    Toast.makeText(context, " error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
                }else  if(restGlobal.equals("6") ){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                    Call<Cuerpo> call = conexion.getEnviar6(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = Boolean.parseBoolean(cuerpo.getJdeStatus());

                                if (estado == true) {

                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    limpiezaGlobal = 1;
                                    mostrarDatos();
                                } else if (estado == false){
                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                    Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                    mostrarDatos();
                                }else{
                                    Toast.makeText(context, " error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
                }else  if(restGlobal.equals("7") ){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                    Call<Cuerpo> call = conexion.getEnviar7(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = Boolean.parseBoolean(cuerpo.getJdeStatus());

                                if (estado == true) {

                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    limpiezaGlobal = 1;
                                    mostrarDatos();
                                } else if (estado == false){
                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                    Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                    mostrarDatos();
                                }else{
                                    Toast.makeText(context, " error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
                }else  if(restGlobal.equals("8") ){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                    Call<Cuerpo> call = conexion.getEnviar8(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = Boolean.parseBoolean(cuerpo.getJdeStatus());

                                if (estado == true) {

                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    limpiezaGlobal = 1;
                                    mostrarDatos();
                                } else if (estado == false){
                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                    Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                    mostrarDatos();
                                }else{
                                    Toast.makeText(context, " error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
                }else  if(restGlobal.equals("9") ){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, sucursalGlobal, LoteString, estadoGlobal);

                    Call<Cuerpo> call = conexion.getEnviar9(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = Boolean.parseBoolean(cuerpo.getJdeStatus());

                                if (estado == true) {

                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    limpiezaGlobal = 1;
                                    mostrarDatos();
                                } else if (estado == false){
                                    editarContacto(idInt, DespositoString, RetencionString, ItemString, LoteString, " Error ");
                                    Toast.makeText(context, " Completado pero con errores", Toast.LENGTH_SHORT).show();
                                    mostrarDatos();
                                }else{
                                    Toast.makeText(context, " error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " error ", Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
                }else{
                    Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                }

            } while (cursorDatos.moveToNext());
            mostrarDatos();
        }
        cursorDatos.close();
        return listaDatos;
    }

    public boolean eliminarDato(int id) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            //validar por el ID
            db.execSQL("DELETE FROM " + NOMBRE_TABLA + " WHERE id = '" + id + "'");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            //cierra la conexion
            db.close();
        }
        return correcto;

    }

    public boolean eliminarTodo() {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {

            //validar por el ID
            db.execSQL("DELETE FROM " + NOMBRE_TABLA);
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            //cierra la conexion
            db.close();
        }
        return correcto;
    }

    public boolean editarContacto(int id, String deposito, String retencion, String item, String lote, String resultado) {


        boolean correcto = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {

            //validar por el ID
            db.execSQL("UPDATE " + NOMBRE_TABLA + " SET desposito = '" +
                    "', desposito = '" + deposito + "', retencion = '" + retencion + "', item = '" + item + "',lote = '" + lote + "', resultado = '" + resultado + "', id = '" + id + "'WHERE id='" + id + "' ");
            correcto = true;
            //progresBar.setVisibility(View.VISIBLE);

        } catch (Exception ex) {
            ex.toString();
            correcto = false;

        } finally {
            //cierra la conexion
            // progresBar.setVisibility(View.INVISIBLE);

            db.close();
        }
        return correcto;
    }

    public static Datos mostrarUnidad(int id){
        DbHelper dbDatos = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbDatos.getWritableDatabase();

        Datos datos = null;
        Cursor cursorDatos;

        //Consultar los datos
        cursorDatos = sqLiteDatabase.rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE id = " + id + " LIMIT 1", null);

        if(cursorDatos.moveToFirst()){
                datos = new Datos();
                datos.setId(cursorDatos.getInt(0));
                datos.setDeposito(cursorDatos.getString(1));
                datos.setRetencion(cursorDatos.getString(2));
                datos.setItem(cursorDatos.getString(3));
                datos.setLote(cursorDatos.getString(4));
                datos.setResultado(cursorDatos.getString(5));
        }
        cursorDatos.close();
        return datos;
    }

}
