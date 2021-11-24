/*
*Title: Feedback 3.1 Grabadora de voz
* Author: Anas Nali
* Date: 11/11/2021
*/

package com.example.grabadora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    //Creamos el MediaPlayer que nos permitirá grabar y reproducir sonido
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;


    /*Con este booleano comprobamos si es true o false la presencia del microfono en el dispositivo,
     *que vamos a utilizar para la grabacion
     */

    private boolean isMicrophonePresent(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;

        } else {
            return false;
        }

    }


    /*
    Con esta instancia lo que hacemos es pedirle al usuario si nos da permiso de utilizar su microfono,
    anteriormente declarado en el Manifest
     */
    private void getMicrophonePermission (){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }

    /*
    * Tenemos que obtener la direccion donde se guardaran los archivos de sonido por lo que
    * creamos un nuevo directorio donde se guardaran los archivos de voz, y al final nos devuelve
    * en que ruta esta guardado
    * */

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecordingFile" + ".mp3");
        return file.getPath();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //En caso de que el es true el booleano del microfono solicita permiso de acceso.
        if (isMicrophonePresent()){
            getMicrophonePermission();
        }

    }



    public void btnRecordPressed(View view) {

        /*Cuando el boton de grabacion esta activado, crea una instancia MediaRecorder, que utiliza
        el microfono y a la vez va almacenando todo en un formato .3gp
        */
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this,"La grabacion ha comenzado", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public void btnStopPressed(View view) {

        //Al pulsar en el boton detener, basicamente detiene todo lo anteriormente ejecutado

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(this,"La grabacion se ha detenido", Toast.LENGTH_LONG).show();

    }

    public void btnPlayPressed(View view) {

        /*Al pulsar reproducir, estamos creando un MediaPlayer que nos va a permitir reproducir lo anteriormente grabado,
        la ruta del archivo, la ruta ya la obtuvimos con el getRecordingFilePath que creamos al principio.
        */

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this,"La grabacion se está reproduciendo", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

