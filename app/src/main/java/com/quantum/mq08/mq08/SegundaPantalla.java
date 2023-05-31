package com.quantum.mq08.mq08;

import static com.quantum.mq08.mq08.Configuracion.checkGlobalLector;
import static com.quantum.mq08.mq08.Configuracion.direc;
import static com.quantum.mq08.mq08.Configuracion.estadoGlobal;
import static com.quantum.mq08.mq08.Configuracion.loteGlobal;
import static com.quantum.mq08.mq08.Configuracion.sucursalGlobal;
import static com.quantum.mq08.mq08.LoginActivity.contraseñaGlobal;
import static com.quantum.mq08.mq08.LoginActivity.usuarioGlobal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.quantum.mq08.R;
import com.quantum.mq08.adaptador.AdaptadorDatos;
import com.quantum.mq08.conectividad.Conexion;
import com.quantum.mq08.database.DbDatos;
import com.quantum.mq08.parseo.Cuerpo2;
import com.quantum.mq08.parseo.Mq0801dDatareq;
import com.quantum.mq08.parseo.Mq0801lFromreq1;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SegundaPantalla extends AppCompatActivity {

    public TextView deposito, retencion,item,lote,qrInfo,colectadoQ,colectado;
    RecyclerView recycler, recycler2;
    ArrayList<String> listDatos,listDatos2;
    Button qr, ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_pantalla);
        deposito = findViewById(R.id.deposito);
        retencion = findViewById(R.id.retencion);
        item = findViewById(R.id.item);
        lote = findViewById(R.id.lote);
        item .requestFocus();
        qrInfo = findViewById(R.id.item);
        colectadoQ = findViewById(R.id.colectado);
        ok = findViewById(R.id.btOK);
        colectado = findViewById(R.id.colectado2);


        recycler= (RecyclerView) findViewById(R.id.recycerId8);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recycler2= (RecyclerView) findViewById(R.id.recycerId9);
        recycler2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102, 45, 145));  //Define color


        if(checkGlobalLector == false){
            colectadoQ.setVisibility(View.INVISIBLE);
            ok.setVisibility(View.INVISIBLE);
            lote .requestFocus();
        }else{
            colectadoQ .requestFocus();
            colectadoQ.setVisibility(View.INVISIBLE);
            ok.setVisibility(View.INVISIBLE);
        }

        if(sucursalGlobal.length() != 0){
            deposito.setText( sucursalGlobal);
        }else{
            sucursalGlobal = deposito.getText().toString();
        }

        if(estadoGlobal != null){
            retencion.setText( estadoGlobal);
        }else{
            estadoGlobal = retencion.getText().toString();
        }


        if(loteGlobal != null){
            lote.setText( loteGlobal);
        }else{
            loteGlobal = lote.getText().toString();
        }
            desplegable();
            desplegable2();

        //para agregar instantaneo
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                agregar3();
            }
        }, 0, 5000);
    }

    public void scan(View v){
        IntentIntegrator intentIntegrator = new IntentIntegrator(SegundaPantalla.this);
        //tipo de lector
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        //que va a decir el lector
        intentIntegrator.setPrompt("Lector - Código");
        //que camara usa, en este caso la 0 es la de atras
        intentIntegrator.setCameraId(0);
        //dispositivos, alertas de sonido
        intentIntegrator.setBeepEnabled(true);
        //para leer correctamente
        intentIntegrator.setBarcodeImageEnabled(true);
        //inicia el elemento de scaneo
        intentIntegrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //recibir el resultado de los parametros de arriba
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null  ){
            if(result.getContents() == null ){
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_SHORT).show();
            }else{
                if(checkGlobalLector == false){
                    if(lote.isFocused()){
                        lote.setText(result.getContents());
                    }else{
                        qrInfo.setText(result.getContents());
                    }
                }else{
                    colectadoQ.setText(result.getContents());

                    if (colectadoQ.length() != 0){
                        String qrString = colectadoQ.getText().toString();
                        String subcadena = qrString.substring(10, 23);
                        lote.setText(subcadena);
                        agregar2();
                    }
                }

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void ok(View v){
        String qrString = colectadoQ.getText().toString();
        String subcadena = qrString.substring(10, 23);
        lote.setText(subcadena);
        colectado.setText(subcadena);

    }

    public void desplegable(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(direc)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Conexion conexion = retrofit.create(Conexion.class);

        Cuerpo2 logerse = new Cuerpo2(usuarioGlobal, contraseñaGlobal);
        Call<Cuerpo2> call1 = conexion.getDesplegable(logerse);
        call1.enqueue(new Callback<Cuerpo2>() {
            @Override
            public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {

                    Cuerpo2 desplegable = (Cuerpo2) response.body();

                    listDatos =new ArrayList<>();

                    for(int e = 0; e<desplegable.getMq0801dDatareq().size(); e++){
                        ArrayList<Mq0801dDatareq> rowset1 = (ArrayList<Mq0801dDatareq>) desplegable.getMq0801dDatareq();
                        String depos = rowset1.get(e).getDesposito();
                        String dsp = rowset1.get(e).getDescripcion();

                        //saca los espacios en blanco
                        String strNew = depos.replace(" ", "");
                        listDatos.add( strNew  );
                        // String content = (dsp+ "\n\n" );

                        AdaptadorDatos adapter = new AdaptadorDatos(listDatos);
                        recycler.setAdapter(adapter);

                        adapter.setOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deposito.setText(listDatos.get(recycler.getChildAdapterPosition(view)));
                            }
                        });
                    }

                }
                else {
                    if (statusCode == 403) {
                        Toast.makeText(SegundaPantalla.this, "Usuario/Contraseña Incorrecto", Toast.LENGTH_LONG).show();
                    }
                    if (statusCode == 500) {
                        Toast.makeText(SegundaPantalla.this, "Error en el servidor", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Cuerpo2> call, Throwable t) {
                Toast.makeText(SegundaPantalla.this, "Sesión caducó  "  , Toast.LENGTH_LONG).show();

            }
        });
    }
    public void desplegable2(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(direc)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Conexion conexion = retrofit.create(Conexion.class);

        Cuerpo2 logerse = new Cuerpo2(usuarioGlobal, contraseñaGlobal);
        Call<Cuerpo2> call1 = conexion.getDesplegable2(logerse);
        call1.enqueue(new Callback<Cuerpo2>() {
            @Override
            public void onResponse(Call<Cuerpo2> call, Response<Cuerpo2> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {

                    Cuerpo2 desplegable = (Cuerpo2) response.body();

                    listDatos2 =new ArrayList<>();

                    for(int e = 0; e<desplegable.getMq0801lFromreq1().size(); e++){
                        ArrayList<Mq0801lFromreq1> rowset1 = (ArrayList<Mq0801lFromreq1>) desplegable.getMq0801lFromreq1();
                        String depos = rowset1.get(e).getCodigo();
                        String dsp = rowset1.get(e).getDescripcion();

                        //saca los espacios en blanco
                        String strNew = depos.replace(" ", "");
                        listDatos2.add( strNew  );
                        // String content = (dsp+ "\n\n" );

                        AdaptadorDatos adapter = new AdaptadorDatos(listDatos2);
                        recycler2.setAdapter(adapter);

                        adapter.setOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                retencion.setText(listDatos2.get(recycler.getChildAdapterPosition(view)));
                            }
                        });
                    }

                }
                else {
                    if (statusCode == 403) {
                        Toast.makeText(SegundaPantalla.this, "Usuario/Contraseña Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                    if (statusCode == 500) {
                        Toast.makeText(SegundaPantalla.this, "Error en el servidor", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Cuerpo2> call, Throwable t) {
                Toast.makeText(SegundaPantalla.this, "Sesión caducó  "  , Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void agregar(View view){
        //TextView a String
        if(deposito.length()==0 && retencion.length()==0 && item.length()==0 && lote.length()==0) {
            Toast.makeText(this,"Completar campos",Toast.LENGTH_LONG).show();
        } else if ( item.length()!=0 || lote.length()!=0) {
            String depositoString = deposito.getText().toString();
            String retencionString = retencion.getText().toString();

            String strNew = item.getText().toString();
            String itemString =  strNew.replace(" ", "");

            String strNew2 = lote.getText().toString();
            String loteString =  strNew2.replace(" ", "");

            DbDatos dbDatos = new DbDatos(SegundaPantalla.this);
            dbDatos.insertaDatos(depositoString, retencionString, itemString, loteString, "pendiente");

            Toast.makeText(SegundaPantalla.this, "Agregado", Toast.LENGTH_SHORT).show();
            limpiar();
        } else {
            Toast.makeText(this,"Intentelo denuevo",Toast.LENGTH_SHORT).show();
        }
    }

    public void agregar2(){
        //TextView a String
        if(deposito.length()==0 && retencion.length()==0 && item.length()==0 && lote.length()==0) {
            Toast.makeText(this,"Completar campos",Toast.LENGTH_LONG).show();
        } else if ( item.length()!=0 || lote.length()!=0) {
            String depositoString = deposito.getText().toString();
            String retencionString = retencion.getText().toString();
            String strNew = item.getText().toString();
            String itemString =  strNew.replace(" ", "");

            String strNew2 = lote.getText().toString();
            String loteString =  strNew2.replace(" ", "");

            DbDatos dbDatos = new DbDatos(SegundaPantalla.this);
            dbDatos.insertaDatos(depositoString, retencionString, itemString, loteString, "pendiente");

            Toast.makeText(SegundaPantalla.this, "Agregado", Toast.LENGTH_SHORT).show();
            limpiar();
        } else {
            Toast.makeText(this,"Intentelo denuevo",Toast.LENGTH_SHORT).show();
        }
    }

    public void agregar3(){
        //TextView a String

        if(lote.length() != 0) {
            String subcadena = lote.getText().toString();
            colectado.setText(subcadena);

            String depositoString = deposito.getText().toString();
            String retencionString = retencion.getText().toString();

            String strNew = item.getText().toString();
            String itemString =  strNew.replace(" ", "");

            String strNew2 = lote.getText().toString();
            String loteString =  strNew2.replace(" ", "");

            DbDatos dbDatos = new DbDatos(SegundaPantalla.this);
            dbDatos.insertaDatos(depositoString, retencionString, itemString, loteString, "pendiente");
            limpia2();
        }
    }

    public void salir(View view){
        Intent intent = new Intent(SegundaPantalla.this,PrimeraPantalla.class);
        startActivity(intent);
    }
    //limpia los textViews de item y serie
    private void limpiar (){
        //deposito.setText("");
        item.setText("");
        colectadoQ.setText("");
        lote.setText("");
    }

    private void limpia2 (){
        lote.setText("");
    }

}