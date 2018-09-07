package it.chiarani.meteotrentinoapp.api;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.database.entity.LocalityEntity;
import it.chiarani.meteotrentinoapp.models.Locality;
import it.chiarani.meteotrentinoapp.repositories.LocalityRepository;

public class API_locality extends AsyncTask<String, Integer, Integer>{

  // #REGION PRIVATE FIELDS
  private final static String API_LOCALITY_TAG = "API_LOCALITY";
  private final static String URL_API = "https://www.meteotrentino.it/protcivtn-meteo/api/front/localitaOpenData";
  Context mContext;
  AlertDialog builder;
  AlertDialog.Builder alert;
  Application _app;
  public API_locality_response delegate = null;
  // #END REGION

  /**
   * Main constructor
   * @param mContext app context
   * @param res callback interface for get content async
   */
  public API_locality(Application app, Context mContext, API_locality_response res) {
    this._app = app;
    this.mContext = mContext;
    this.delegate = res;
  }

  /**
   * Before execute the task create a dialog
   */
  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    alert = new AlertDialog.Builder(mContext);
    alert.setMessage(mContext.getResources().getString(R.string.API_locality_alert)).create();
    alert.setCancelable(false);
    builder = alert.show();
  }

  /**
   * After execute the task we call the interface callback
   * and send the data
   */
  @Override
  protected void onPostExecute(Integer integer) {
    builder.dismiss();
    delegate.processFinish();
  }

  /**
   * Execute in bakground the task:
   * Create a GET and call a REST API for get all locality and parse the JSON
   */
  @Override
  protected Integer doInBackground(String... s) {
    LocalityRepository repository = new LocalityRepository(_app);
    HttpURLConnection connection = null;
    BufferedReader reader = null;

    try {

      URL url = new URL(URL_API);
      connection = (HttpURLConnection) url.openConnection();
      connection.connect();

      InputStream stream = connection.getInputStream();

      reader = new BufferedReader(new InputStreamReader(stream));

      StringBuffer buffer = new StringBuffer();
      String line = "";
      publishProgress(20);
      while ((line = reader.readLine()) != null) {
        buffer.append(line + "\n");
      }

      String data = buffer.toString();
      JSONObject ob = new JSONObject(data);
      JSONArray arr = ob.getJSONArray("localita");

      for (int i = 0; i < arr.length(); i++) {
        publishProgress(i);
        String locality = arr.getJSONObject(i).optString("localita");
        if(locality.toLowerCase().equals("rovereto")) {
          int x = 1;
        }
        String place = arr.getJSONObject(i).optString("comune");
        int quota = Integer.parseInt(arr.getJSONObject(i).optString("quota"));
        String latitudine = arr.getJSONObject(i).optString("latitudine");
        String longitudine = arr.getJSONObject(i).optString("longitudine");

        repository.insert(new LocalityEntity(locality, place, quota, latitudine, longitudine));
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
      Log.e(API_LOCALITY_TAG, "Errore MalformedURLException: "+  e.toString());
    } catch (IOException e) {
      e.printStackTrace();
      Log.e(API_LOCALITY_TAG, "Errore IOException: "+  e.toString());
    } catch (Exception e) {
      e.printStackTrace();
      Log.e(API_LOCALITY_TAG, "Errore Exception: "+  e.toString());
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
        Log.e(API_LOCALITY_TAG, "Errore IOException1: "+  e.toString());
      }
    }
    Log.d(API_LOCALITY_TAG, "DATI locality Correttamente scaricati");

    return 1;
  }

  @Override
  protected void onProgressUpdate(Integer... values) {
    TextView messageView = (TextView)builder.findViewById(android.R.id.message);
    messageView.setText("Ottengo le località.. "+ values[0] +"/539");
  }
}
