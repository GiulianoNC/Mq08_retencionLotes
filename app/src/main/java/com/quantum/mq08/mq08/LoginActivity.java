package com.quantum.mq08.mq08;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quantum.mq08.R;
import com.quantum.mq08.conectividad.Conexion;
import com.quantum.mq08.database.DbHelper;
import com.quantum.mq08.parseo.Cuerpo2;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView user, contraseña, urls, qtm,hand;
    public static String usuarioGlobal = null;
    public static String contraseñaGlobal = null;

    Switch switcher;
    boolean nightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //actualizado  9/06/2023
    TableLayout logueo, config;
    private TextView direccion, sucursal, estado,rest,desple,CBD;

    public static String direc = null;
    public static String sucursalGlobal = null;
    public static String loteGlobal = null;
    public static String estadoGlobal = null;
    public static String restGlobal = "";
    public static String desplegableGlobal = null;
    public FloatingActionButton btnBaseDatos;
    public static boolean  checkGlobalLector = false;
    public static boolean  handHeldGlobal = false;
    public static String  visible = null;

    private CheckBox ckbxLector,handHeldLector;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ingreso
        user = findViewById(R.id.Usuario);
        contraseña = findViewById(R.id.contras);
        urls = findViewById(R.id.dir);

        //configuracion
        direccion = findViewById(R.id.direccion2);
        sucursal = findViewById(R.id.sucursal2);
        estado = findViewById(R.id.loteurl2);
        rest = findViewById(R.id.rest2);
        CBD= findViewById(R.id.cbd2);
        ckbxLector = findViewById(R.id.checkBoxLector2);
        hand = findViewById(R.id.handText);
        handHeldLector = findViewById(R.id.handHeld);

        //TableLayout
        logueo= findViewById(R.id.logueo);
        config= findViewById(R.id.configuracion);

        direc = direccion.getText().toString();
        sucursalGlobal = sucursal.getText().toString();
        estadoGlobal = estado.getText().toString();

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102, 45, 145));  //Define color

        //guardar
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        user.setText(preferences.getString("usuario", ""));
        contraseña.setText(preferences.getString("password", ""));
        hand.setText(preferences.getString("hand",""));


        SharedPreferences preferences2 = getSharedPreferences("dato", Context.MODE_PRIVATE);
        direccion.setText(preferences2.getString("direcciones", ""));
        sucursal.setText(preferences2.getString("sucursal", ""));
        estado.setText(preferences2.getString("estado", ""));
        rest.setText(preferences2.getString("rest", ""));
        CBD.setText(preferences2.getString("cbd",""));

        String direccion = getIntent().getStringExtra("direcciones");
        urls.setText(direccion);

        if(CBD.getText().toString().equals("0")){
            ckbxLector.setChecked(false);
        }else{
            ckbxLector.setChecked(true);
        }
        if(restGlobal.length() == 0){
            restGlobal = "1";
        }else{
            restGlobal = rest.getText().toString();
        }

        if(handHeldLector.isChecked()){
            Toast.makeText(this, "activado", Toast.LENGTH_SHORT).show();
            hand.setText("1");
        }else{
            hand.setText("0");
        }
        if(hand.getText().toString().equals( "1")){
            handHeldLector.setChecked(true);
            handHeldGlobal = true;
            Toast.makeText(this, "activado", Toast.LENGTH_SHORT).show();
        }else{
            handHeldLector.setChecked(false);
            handHeldGlobal = false;
        }
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

        if(user.length() == 0){
            config.setVisibility(View.INVISIBLE);
        }else{
            logueo.setVisibility(View.INVISIBLE);
        }
        if(visible == "1"){

            logueo.setVisibility(View.VISIBLE);
            config.setVisibility(View.INVISIBLE);

        }else{
            logueo.setVisibility(View.INVISIBLE);
            config.setVisibility(View.VISIBLE);

        }
    }

    public void globales (){
        if(ckbxLector.isChecked()){
            checkGlobalLector = true;
        }else{
            restGlobal = rest.getText().toString();
        }
        // handHeldLector.setChecked(false);

        if(handHeldLector.isChecked()){
            hand.setText("1");
        }else{
            hand.setText("0");
        }

        if(hand.getText().toString().equals( "1")){
            handHeldLector.setChecked(true);
            handHeldGlobal = true;
        }else{
            handHeldLector.setChecked(false);
            handHeldGlobal = false;
        }

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
                logueo.setVisibility(View.INVISIBLE);
                config.setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Login(View v) {
        String usuario = user.getText().toString();
        String contra = contraseña.getText().toString();
        String direccion2 = direccion.getText().toString();
        direc = direccion.getText().toString();
        sucursalGlobal = sucursal.getText().toString();
        estadoGlobal = estado.getText().toString();
        urls.setText(direccion2);

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
                        //user.setText("mensaje " + t);
                    }
                });

                SharedPreferences preferecias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferecias.edit();
                Obj_editor.putString("usuario", user.getText().toString());
                Obj_editor.putString("password", contraseña.getText().toString());
                Obj_editor.putString("hand", hand.getText().toString());

                Obj_editor.commit();
            }
        }
    }

    public void guardar(View view) {

        //guardado lo configurado
        SharedPreferences preferecias = getSharedPreferences("dato", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();
        Obj_editor.putString("direcciones", direccion.getText().toString());
        Obj_editor.putString("sucursal", sucursal.getText().toString());
        Obj_editor.putString("estado", estado.getText().toString());
        Obj_editor.putString("rest", rest.getText().toString());
        Obj_editor.putString("cbd", CBD.getText().toString());
        Obj_editor.commit();

        //base de datos
        DbHelper dbhelper = new DbHelper(LoginActivity.this);
        SQLiteDatabase sqLiteDatabase = dbhelper.getWritableDatabase();
        if (sqLiteDatabase != null) {
            Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(LoginActivity.this, "No fue creada la base de datos", Toast.LENGTH_LONG).show();
        }

        //condiciones de variables
        if(restGlobal.length() == 0){
            restGlobal = "1";
        }else{
            restGlobal = rest.getText().toString();
        }
        if (ckbxLector.isChecked()==true){
            CBD.setText("1");
        }else{
            CBD.setText("0");
        }
        if (CBD.getText().toString().equals("0")){
            checkGlobalLector = false;
            //   Toast.makeText(Configuracion.this,"0", Toast.LENGTH_LONG).show();
        } else if  (CBD.getText().toString().equals("1")){
            checkGlobalLector = true;
            //   Toast.makeText(Configuracion.this,"1", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(LoginActivity.this,"..", Toast.LENGTH_LONG).show();
        }
        usuarioGlobal = user.getText().toString();
        contraseñaGlobal = contraseña.getText().toString();
        direc = direccion.getText().toString();
        sucursalGlobal = sucursal.getText().toString();
        estadoGlobal = estado.getText().toString();

        //ingreso a la siguiente pantalla
        Intent siguiente = new Intent(LoginActivity.this, PrimeraPantalla.class);
        startActivity(siguiente);

        globales ();
    }
}