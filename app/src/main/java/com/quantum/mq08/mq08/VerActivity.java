package com.quantum.mq08.mq08;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.mq08.R;
import com.quantum.mq08.database.DbDatos;
import com.quantum.mq08.entidades.Datos;

public class VerActivity extends AppCompatActivity {

TextView idV,depositoV,retencionV,itemV,loteV;
Button fabEliminar;
Datos datos;
int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        depositoV = findViewById(R.id.depoV);
        retencionV = findViewById(R.id.retenV);
        itemV = findViewById(R.id.itemV);
        loteV = findViewById(R.id.loteV);
        idV = findViewById(R.id.idV);

        fabEliminar = findViewById(R.id.fabEliminar);
        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        //hago una llamada a lo seleccionado  atra ves de la base de datos mediante  y el ID
        if(savedInstanceState == null){
            Bundle extras  = getIntent().getExtras();
            if(extras == null ){
                id = Integer.parseInt(null);
            }else{
                id= extras.getInt("ID");
                idV.setText(id +"" );
            }
        }else{
            id = (int ) savedInstanceState.getSerializable("ID");
        }
        final DbDatos dbDatos = new DbDatos( VerActivity.this);
        datos = DbDatos.mostrarUnidad(id);

        if(datos != null){
            depositoV.setText(datos.getDeposito());
            retencionV.setText(datos.getRetencion());
            itemV.setText(datos.getItem());
            loteV.setText(datos.getLote());

            depositoV.setInputType(InputType.TYPE_NULL);
            retencionV.setInputType(InputType.TYPE_NULL);
            itemV.setInputType(InputType.TYPE_NULL);
            loteV.setInputType(InputType.TYPE_NULL);
        }else{
            Toast.makeText(VerActivity.this,"no hay datos", Toast.LENGTH_LONG).show();
        }

        //elimino un registro
        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder( VerActivity.this);
                builder.setMessage("Desea eliminar este registro?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(dbDatos.eliminarDato(id)){
                                    lista();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(VerActivity.this,"no se eliminó regristro", Toast.LENGTH_LONG).show();
                            }
                        }).show();
            }
        });

    }

    public void Salir(View v){
        Intent intent = new Intent(VerActivity.this, PrimeraPantalla.class);
        startActivity(intent);
    }

    private void lista(){
        Intent intent = new Intent(this, PrimeraPantalla.class);
        startActivity(intent);
    }


}