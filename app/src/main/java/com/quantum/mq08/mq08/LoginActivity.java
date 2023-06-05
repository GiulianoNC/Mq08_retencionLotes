package com.quantum.mq08.mq08;

import static com.quantum.mq08.mq08.Configuracion.direc;
import static com.quantum.mq08.mq08.Configuracion.estadoGlobal;
import static com.quantum.mq08.mq08.Configuracion.restGlobal;
import static com.quantum.mq08.mq08.Configuracion.sucursalGlobal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.mq08.R;
import com.quantum.mq08.conectividad.Conexion;
import com.quantum.mq08.parseo.Cuerpo2;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView user, contraseña, urls, qtm;

    public static String usuarioGlobal = null;
    public static String contraseñaGlobal = null;

    Switch switcher;
    boolean nightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = findViewById(R.id.Usuario);
        contraseña = findViewById(R.id.contras);

        //guardar

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        user.setText(preferences.getString("usuario", ""));
        contraseña.setText(preferences.getString("password", ""));
        urls = findViewById(R.id.dir);

        String direccion = getIntent().getStringExtra("direcciones");
        urls.setText(direccion);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102, 45, 145));  //Define color

        qtm = findViewById(R.id.QTMtitulo);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO");

        //Esto es el Day/Night Mode
        //Uso el SharedPreference para guardar el modo cuando salgo de la pagina
        switcher = findViewById(R.id.btnToggleDark);
        sharedPreferences = getSharedPreferences("MODE",Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night",false); //El modo luz es el default

        if (nightMODE){
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",true);
                }
                editor.apply();
            }
        });
    }
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //acciones de menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_privacidad:
                String url = "https://quantumconsulting.com.ar/politicas-de-privacidad/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.action_configuracion:
                Intent siguiente = new Intent(LoginActivity.this, Configuracion.class);
                startActivity(siguiente);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

  /*  public void ingresar (View v){

        String usuario = user.getText().toString();
        String contra = contraseña.getText().toString();

        String direccion = getIntent().getStringExtra("direcciones");
        urls.setText(direccion);

        if (user.length() == 0 && contraseña.length() == 0) {
            Toast.makeText(this, "Debes ingresar un usuario y contraseña", Toast.LENGTH_SHORT).show();
        }
        if (user.length() != 0 && contraseña.length() != 0) {

            if (urls.length() == 0)  {
                Intent siguiente = new Intent(LoginActivity.this, Configuracion.class);
                startActivity(siguiente);
            }else{

                Toast.makeText(LoginActivity.this, "Procesando", Toast.LENGTH_SHORT).show();

                usuarioGlobal = user.getText().toString();
                contraseñaGlobal = contraseña.getText().toString();

                if(restGlobal.equals("1") || restGlobal.equals("")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getUser(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }

                        }
                        //respuesta fallida

                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else  if(restGlobal.equals("2")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar3(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }

                        }
                        //respuesta fallida

                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else if(restGlobal.equals("3")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar3(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }

                        }
                        //respuesta fallida

                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                } else if(restGlobal.equals("4")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar4(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }

                        }
                        //respuesta fallida

                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else if(restGlobal.equals("5")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar5(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                        }
                        //respuesta fallida
                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else if(restGlobal.equals("6")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar6(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                        }
                        //respuesta fallida
                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                } else if(restGlobal.equals("7")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar7(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                        }
                        //respuesta fallida
                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else  if(restGlobal.equals("8")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar8(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                        }
                        //respuesta fallida
                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else if(restGlobal.equals("9")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar9(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                        }
                        //respuesta fallida
                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else if(restGlobal.equals("10")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                    Call<Cuerpo2> call = conexion.getEnviar10(login);
                    call.enqueue(new Callback<Cuerpo2>() {

                        //respuesta exitosa
                        @Override
                        public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                            if (response.isSuccessful()) {
                                int statusCode = response.code();

                                //llamada de la clase cuerpo y su respuesta de body
                                if (statusCode <= 200) {
                                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                    Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                    startActivity(siguiente);
                                } else {
                                    Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                        }
                        //respuesta fallida
                        @Override
                        public void onFailure(Call<Cuerpo2> call, Throwable t) {
                            //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            user.setText("mensaje " + t);
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Completar en configuración los datos "  , Toast.LENGTH_SHORT).show();
                }

            }
        }
        SharedPreferences preferecias =  getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();
        Obj_editor.putString("usuario", user.getText().toString());
        Obj_editor.putString("password", contraseña.getText().toString());
        Obj_editor.commit();

    }*/

    public void Login(View v) {
        String usuario = user.getText().toString();
        String contra = contraseña.getText().toString();
        String direccion = getIntent().getStringExtra("direcciones");

        urls.setText(direccion);

        if (usuario.length() == 0 && contra.length() == 0) {
            Toast.makeText(this, "Debes ingresar un usuario y contraseña", Toast.LENGTH_LONG).show();

            SharedPreferences preferecias = getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor Obj_editor = preferecias.edit();
            Obj_editor.putString("usuario", user.getText().toString());
            Obj_editor.putString("password", contraseña.getText().toString());

            Obj_editor.commit();
        }
        if (usuario.length() != 0 && contra.length() != 0) {


            if (urls.length() == 0) {

                Intent siguiente = new Intent(LoginActivity.this, Configuracion.class);
                startActivity(siguiente);

                SharedPreferences preferecias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferecias.edit();
                Obj_editor.putString("usuario", user.getText().toString());
                Obj_editor.putString("password", contraseña.getText().toString());
            } else {

                Toast.makeText(LoginActivity.this, "Procesando", Toast.LENGTH_LONG).show();

                usuarioGlobal = user.getText().toString();
                contraseñaGlobal = contraseña.getText().toString();

                //si llega a dar timeout
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(190, TimeUnit.SECONDS)
                        .connectTimeout(190, TimeUnit.SECONDS)
                        .build();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(direc)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();


                Conexion conexion = retrofit.create(Conexion.class);
                Cuerpo2 login = new Cuerpo2(usuario, contra, sucursalGlobal, "21602", estadoGlobal);

                Call<Cuerpo2> call = conexion.getUser(login);
                call.enqueue(new Callback<Cuerpo2>() {

                    //respuesta exitosa
                    @Override
                    public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {

                        if (response.isSuccessful()) {
                            int statusCode = response.code();

                            //llamada de la clase cuerpo y su respuesta de body
                            if (statusCode <= 200) {
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                                Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
                                startActivity(siguiente);
                            } else {
                                Toast.makeText(LoginActivity.this, "error ", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                        }

                    }
                    //respuesta fallida

                    @Override
                    public void onFailure(Call<Cuerpo2> call, Throwable t) {
                        //Toast.makeText(LoginActivity.this, "fallida", Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        user.setText("mensaje " + t);
                    }
                });

                SharedPreferences preferecias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferecias.edit();
                Obj_editor.putString("usuario", user.getText().toString());
                Obj_editor.putString("password", contraseña.getText().toString());
                Obj_editor.commit();
            }
        }
    }
}