package com.quantum.mq08.mq08;


import static com.quantum.mq08.database.DbDatos.actualizar;
import static com.quantum.mq08.mq08.LoginActivity.handHeldGlobal;
import static com.quantum.mq08.mq08.LoginActivity.visible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.quantum.mq08.R;
import com.quantum.mq08.adaptador.Adaptador;
import com.quantum.mq08.database.DbDatos;
import com.quantum.mq08.entidades.Datos;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PrimeraPantalla extends AppCompatActivity {

    Button limpieza;
    public RecyclerView datos;
    public ArrayList<Datos> listadatos;
    public Adaptador adaptador;
    public static int limpiezaGlobal = 0;
    public static String mostrarGlobal = "0";

    ArrayList<Datos> listaArrayContactos;
    Adaptador adapter;
    RecyclerView listaContactos;
    ProgressBar progresBar;
    int act = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera_pantalla);

        datos = findViewById(R.id.datos);
        datos.setLayoutManager(new LinearLayoutManager(this));
        progresBar = findViewById(R.id.progressBar);

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102, 45, 145));  //Define color

        //Se muestran los datos en Pantalla
        DbDatos dbdatos = new DbDatos(PrimeraPantalla.this);
        listadatos = new ArrayList<>();
        adaptador = new Adaptador(dbdatos.mostrarDatos());
        datos.setAdapter(adaptador);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (limpiezaGlobal == 1){
            limpiezaGlobal=0;
            mostrar();
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    actualizado();
                }
            });

        }else{
            mostrar();
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    actualizado();
                }
            });

        }

        //para limpieza
        limpieza = findViewById(R.id.limpieza);
        limpieza.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PrimeraPantalla.this);
            builder.setMessage("Desea eliminar los registros de la tabla? ")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbdatos.eliminarTodo();
                            mostrar();
                        }
                    }) .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Toast.makeText(PrimeraPantalla.this,"No se hizo la limpieza", Toast.LENGTH_LONG).show();
                        }
                    }).show();
        });
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //acciones de menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_sesion:

                Intent siguiente = new Intent(PrimeraPantalla.this, LoginActivity.class);
                startActivity(siguiente);

                visible = "1";
                break;

            case R.id.action_configuracion:
                Intent siguiente2 = new Intent(PrimeraPantalla.this, LoginActivity.class);
                visible = "2";
                startActivity(siguiente2);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void limpiezaAutomatica(){
        DbDatos dbContactos = new DbDatos(PrimeraPantalla.this);
        dbContactos.eliminarTodo();
        mostrar();
    }
    public void colectar(View view){
        Intent intent = new Intent(PrimeraPantalla.this,SegundaPantalla.class);
        startActivity(intent);
    }

   public void mostrar(){

          DbDatos dbContactos = new DbDatos(PrimeraPantalla.this);
          listaArrayContactos = new ArrayList<>();
          adapter = new Adaptador(dbContactos.mostrarDatos());
          datos.setAdapter(adapter);


       act =0;

   /*    if (limpiezaGlobal == 1){
           limpiezaAutomatica();
           limpiezaGlobal= 0;
       }*/

    }
    public void enviar (View v){

        new AlertDialog.Builder(this)
                //.setIcon(R.drawable.alacran)
                .setTitle("¿Desea enviar todos los registros?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbDatos dbContactos = new DbDatos(PrimeraPantalla.this);
                        listaArrayContactos = new ArrayList<>();
                        adapter = new Adaptador(dbContactos.enviarDatos());
                        datos.setAdapter(adapter);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Código a ejecutar después de 9 segundos
                                actualizado();
                            }
                        }, 9000);

                        Toast.makeText(PrimeraPantalla.this,"Enviando", Toast.LENGTH_LONG).show();
                    }
                }).show();


    }
    public void actualizado(){
        mostrar();
    }
}