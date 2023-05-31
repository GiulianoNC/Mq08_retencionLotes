package com.quantum.mq08.mq08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quantum.mq08.R;
import com.quantum.mq08.database.DbHelper;

public class Configuracion extends AppCompatActivity {
    private TextView direccion, sucursal, estado,rest,desple,CBD;

    public static String direc = null;
    public static String sucursalGlobal = null;
    public static String loteGlobal = null;
    public static String estadoGlobal = null;
    public static String restGlobal = "";
    public static String desplegableGlobal = null;
    public FloatingActionButton btnBaseDatos;
    public static boolean  checkGlobalLector = false;

    private CheckBox  ckbxLector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        direccion = findViewById(R.id.direccion);
        sucursal = findViewById(R.id.sucursal);
        estado = findViewById(R.id.loteurl);
        rest = findViewById(R.id.rest);
        CBD= findViewById(R.id.cbd);
        ckbxLector = findViewById(R.id.checkBoxLector);


        if(CBD.getText().toString().equals("0")){
            ckbxLector.setChecked(false);
        }else{
            ckbxLector.setChecked(true);
        }

        btnBaseDatos = findViewById(R.id.btnBaseDatos);

        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        direccion.setText(preferences.getString("direcciones", ""));

        sucursal.setText(preferences.getString("sucursal", ""));
        estado.setText(preferences.getString("estado", ""));
        rest.setText(preferences.getString("rest", ""));
        CBD.setText(preferences.getString("cbd",""));



        direc = direccion.getText().toString();
        sucursalGlobal = sucursal.getText().toString();
        estadoGlobal = estado.getText().toString();

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102, 45, 145));  //Define color

        //para crear base de datos
        btnBaseDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbhelper = new DbHelper(Configuracion.this);
                SQLiteDatabase sqLiteDatabase = dbhelper.getWritableDatabase();

                if (sqLiteDatabase != null) {
                    Toast.makeText(Configuracion.this, "Creada con Exito", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Configuracion.this, "No fue creada", Toast.LENGTH_LONG).show();

                }
            }
        });

        if(restGlobal.length() == 0){
            restGlobal = "1";
        }else{
            restGlobal = rest.getText().toString();
        }
    }

    public void guardar(View view) {
        SharedPreferences preferecias = getSharedPreferences("dato", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();

        Obj_editor.putString("direcciones", direccion.getText().toString());
        Obj_editor.putString("sucursal", sucursal.getText().toString());
        Obj_editor.putString("estado", estado.getText().toString());
        Obj_editor.putString("rest", rest.getText().toString());
        Obj_editor.putString("cbd", CBD.getText().toString());


        Obj_editor.commit();


        Intent siguiente = new Intent(Configuracion.this, LoginActivity.class);

        siguiente.putExtra("direcciones", direccion.getText().toString());
        siguiente.putExtra("sucursal", sucursal.getText().toString());
        siguiente.putExtra("estado", estado.getText().toString());

        startActivity(siguiente);
        DbHelper dbhelper = new DbHelper(Configuracion.this);
        SQLiteDatabase sqLiteDatabase = dbhelper.getWritableDatabase();
        if (sqLiteDatabase != null) {
            Toast.makeText(Configuracion.this, "Creada con Exito", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Configuracion.this, "No fue creada", Toast.LENGTH_LONG).show();

        }
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
            Toast.makeText(Configuracion.this,"..", Toast.LENGTH_LONG).show();
        }

    }
}