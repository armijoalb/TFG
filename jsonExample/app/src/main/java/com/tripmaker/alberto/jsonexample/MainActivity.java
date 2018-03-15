package com.tripmaker.alberto.jsonexample;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.Buffer;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               processJSON();
            }
        });

    }

    private void processJSON() {
        String baseFolder = this.getFilesDir().getAbsolutePath();
        String file = new String(baseFolder + File.separator + "overpass_api.json");
        new jsonProcessor(this).execute(file);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openUrl() {

        // Código de la url que "abrimos" para obtener el archivoq ue tiene los nodos.
        String url_enconded = "https://overpass-api.de/api/interpreter?data=[out:json][timeout:100];" +
                "(node[\"place\"=\"city\"][\"name\"=\"Granada\"][\"is_in:province\"=\"Granada\"][\"is_in:country\"=\"Spain\"];)->.ciudad;" +
                "node[\"tourism\"=\"museum\"](around.ciudad:7000);out;>;out;" +
                "node[\"tourism\"=\"viewpoint\"](around.ciudad:7000);out;" +
                "node[\"tourism\"=\"hotel\"](around.ciudad:7000);out;node[\"tourism\"=\"hostel\"](around.ciudad:7000);out;" +
                "node[\"building\"=\"cathedral\"](around.ciudad:7000);out;";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_enconded));
        startActivity(browserIntent);

        Toast.makeText(this, "Iniciando la descarga", Toast.LENGTH_SHORT).show();
        // Utilizamos la nueva clase para descargar el contenido del fichero.
        DownloadFileFromURL mDownloader = new DownloadFileFromURL(this);
        String[] s = {Uri.parse(url_enconded).toString(), "overpass_api.json"};
        mDownloader.execute(s);


    }

    /**
     * Clase para encontrar todos los nodos dentro del archivo.
     */

    class jsonProcessor extends AsyncTask<String,Void,Void>{

        jsonParser mParser;
        private Context mContext;

        public jsonProcessor(Context theContext){
            this.mContext = theContext;
        }

        @Override
        protected Void doInBackground(String... file_path) {

            mParser = new jsonParser(file_path[0]);
            try {
                mParser.processJSON();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Toast.makeText(mContext,"El número de nodos es: " + mParser.getSize(),Toast.LENGTH_SHORT ).show();
        }
    }



    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private Context mContext;

        public DownloadFileFromURL(Context context) {
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                String baseFolder = mContext.getFilesDir().getAbsolutePath();
                File file = new File(baseFolder + File.separator + f_url[1]);
                file.getParentFile().mkdirs();
                OutputStream output = new FileOutputStream(file);

                Log.e("file_out: ", "File opened");
                Log.e("file_out:", "File saved in: " + mContext.getFilesDir().getAbsolutePath());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                Log.e("file_out:", "Finished writting output");

//                FileInputStream inputStream = openFileInput(f_url[1]);
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                int num_lines = 0;
//
//                while(bufferedReader.readLine() != null){
//                    num_lines ++;
//                }
//
//                Log.e("file readed: ", "num_lines " + num_lines);


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


    }
}