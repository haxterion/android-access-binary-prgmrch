package com.blogspot.prgrmrch.pjr01c;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button btn;
    String URL="http://192.168.42.102/android/RCH.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //memanggil thread DownloadImageAsyncTask untuk
                //mendownload gambar dari server
                new DownloadImageAsyncTask().execute(URL);
            }
        });
    }

    //method untuk membuka kkoneksi dengan server
    public static InputStream openHttpGetConnection(String url){
        InputStream input = null;
        try{

            //menggunakan class HttpCLient agar aplikasi dapat terhubung ke
            //server menggunakan protokol HTTP
            HttpClient httpClient = new DefaultHttpClient();

            //HttpResponse berfungsi untuk mengetahui respon dari percobaan
            //koneksi menggunakan protokol http, apakah ada permasalahan atau tidak
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            //mendapatkan data biner dari server yang dituju
            input = httpResponse.getEntity().getContent();
        }catch (Exception e){
            Log.d("InputStream",e.getLocalizedMessage());
        }return input;
    }

    //method downloadImage untuk mendownload/convert data biner ke
    //data method ini tidak bisa dipanggil langsung, harus melalui thread
    //turunan class AsyncTask
    private Bitmap downloadImage(String url){
        Bitmap bitmap = null;
        InputStream input = null;
        try{
            input = openHttpGetConnection(url);

            //mendecode data biner menjadi obyek bitmap
            bitmap = BitmapFactory.decodeStream(input);
            input.close();
        }catch (Exception e){
            Log.d("DownloadImage", e.getLocalizedMessage());
        }return bitmap;
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params){
            return downloadImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imV = (ImageView)findViewById(R.id.imageView1);
            imV.setImageBitmap(result);
        }
    }
}
