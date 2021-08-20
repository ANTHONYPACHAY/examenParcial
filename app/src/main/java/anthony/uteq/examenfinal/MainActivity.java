package anthony.uteq.examenfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import anthony.uteq.examenfinal.utiles.Alerts;
import anthony.uteq.examenfinal.utiles.Methods;
import anthony.uteq.examenfinal.utiles.MyLogs;
import anthony.uteq.examenfinal.utiles.TableModel;

public class MainActivity extends AppCompatActivity {

    //0: respuesta a carga de imagen
    //1: respuesta solicitud de permiso a archivos
    private int[] CODES = {50, 100, 200, 250};
    private boolean FilesPermit = true;
    private ImageView img;
    ArrayList<String[]> lista;
    //objeto
    private Translator SpanishToEnglish = null;
    //objeto que ser enviado al siguiente activity
    private JsonObject JsonCountry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tratar de obtener permisos para archivos y la camara
        requestPermitStorage();
        //Inicia la descarga de los paquetes para traducir
        initTraslate();

        //establecer como variable global el contenedor de imagenes
        img = this.findViewById(R.id.imageView);
        //obtener referencia del botón
        Button button = (Button) findViewById(R.id.btnsearchpicture);
        //asignar evento al botón
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FilesPermit) {
                    //declarar el nuevo intent, el cual se encargará de abrir el visualizador de archivos
                    //para cargar una nueva imagen
                    Intent gallery = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    //ser establecen filtros de archivos que se podran obtener
                    gallery.setType("Image/*");
                    //se ejecuta el intent
                    startActivityForResult(gallery, CODES[0]);
                } else {
                    Alerts.MessageToast(MainActivity.this, "no tienes permiso");
                }
                //startActivity(gallery);
            }
        });
        Button buttonT = (Button) findViewById(R.id.btntakepicture);
        buttonT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePic.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePic, CODES[2]);
                    } else {
                        Alerts.MessageToast(MainActivity.this, "mal intent");
                    }
                } else {
                    Alerts.MessageToast(MainActivity.this, "no tienes permiso");
                }
            }
        });
        Button buttonOpenMap = (Button) findViewById(R.id.changeview);
        buttonOpenMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (JsonCountry != null) {
                    //llamar al metodo iniciar actividad con el mapita
                    strartMap(JsonCountry);
                }
            }
        });


    }

    private void requestPermitStorage() {
        Alerts.MessageToast(MainActivity.this, "No tiene permiso a archivos :c");
        //solicitar acceso a archivos del dispositivo
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, CODES[1]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //verifica si el resultado de la petición fue satisfactorio
        if (resultCode == Activity.RESULT_OK) {
            //verifica si se ha seleccionado una imagen
            if (requestCode == CODES[0]) {
                //obtener imageUri
                Uri imageUri = data.getData();
                if (img != null) {
                    try {
                        //ubicar imagen en contenedor ImageView
                        img.setImageURI(imageUri);
                        InputImage image = null;
                        try {
                            //obtiene el input imagen a partir de la uri
                            image = InputImage.fromFilePath(MainActivity.this, imageUri);
                        } catch (IOException e) {
                            MyLogs.error("IOimg: " + e.getMessage());
                        }
                        identifyLabels(image);
                    } catch (Exception ex) {
                        MyLogs.error("ImgSetUri: " + ex.getMessage());
                    }
                }
            } else if (requestCode == CODES[1]) {
                //obtiene respuesta de la imagen
                Alerts.MessageToast(MainActivity.this, "Permiso aceptado");
                FilesPermit = true;
            } else if (requestCode == CODES[2]) {
                //obtiene respuesta de la imagen
                Bundle extras = data.getExtras();
                Bitmap imgBitMap = (Bitmap) extras.get("data");
                if (img != null) {
                    InputImage image = null;
                    try {
                        //ubicar imagen en contenedor ImageView
                        img.setImageBitmap(imgBitMap);
                        //obtiene el input imagen a partir de un bitMap
                        image = InputImage.fromBitmap(imgBitMap, 0);
                    } catch (Exception ex) {
                        MyLogs.error("ImgSetUri: " + ex.getMessage());
                    }
                    identifyLabels(image);
                }
            }
        }
        MyLogs.error("resultCode: " + resultCode);
    }

    /*******************************************************************************************
     *                                  Etiquetado de imágenes                                 *
     *******************************************************************************************/
    private void tableAdapt(ArrayList<String[]> lista, boolean isTraslateAvalible) {
        //Primero y antes de hacer algo mas, investigar v':


        //obtener la referencia de la tabla en el activity
        TableLayout table = (TableLayout) findViewById(R.id.table);
        //declaramos el objeto que nos creará la tabla dinámica
        TableModel tbModel = new TableModel(MainActivity.this, table);
        //indicamos los encabezados de la tabla
        tbModel.setHeaders(new String[]{"Título", "Descripción"});
        //enviamos los datos del cuerpo de la tabla
        tbModel.setRows(lista);
        //configuramos la tabla, colores del encabezado y el cuerpo
        // tanto del texto como el fondo
        tbModel.setHeaderBackGroundColor(R.color.back_black);
        tbModel.setRowsBackGroundColor(R.color.back_white);

        tbModel.setHeadersForeGroundColor(R.color.back_white);
        tbModel.setRowsForeGroundColor(R.color.back_black);
        //Modifica la tabla a partir de los datos enviados y los parámetros enviados
        tbModel.makeTable();

        MyLogs.info(" FIN ");
    }

    /*******************************************************************************************
     *                                  Etiquetado de imágenes                                 *
     *******************************************************************************************/

    private void identifyLabels(InputImage image) {

        //verifica si se obtuvo el InputImage
        if (image != null) {
            // código del algoritmo
            try {
                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                //Procesar imagenes
                Task<Text> result =
                        recognizer.process(image)
                                .addOnSuccessListener(new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text visionText) {
                                        //4. Extract text from blocks of recognized text
                                        //https://developers.google.com/ml-kit/vision/text-recognition/android#4.-extract-text-from-blocks-of-recognized-text
                                        String resultText = visionText.getText();
                                        boolean flagTraslate = SpanishToEnglish != null;
                                        lista = new ArrayList<>();
                                        if (flagTraslate) {
                                            lista.add(new String[]{"Texto", resultText});
                                            lista.add(new String[]{"Country", ""});
                                            traslateCountryName(resultText, 1);
                                        } else {
                                            lista.add(new String[]{"Texto", resultText});
                                        }
                                        if (!flagTraslate) {
                                            tableAdapt(lista, flagTraslate);
                                        }
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                            }
                                        });
            } catch (Exception e) {
            }
        } else {
            Alerts.MessageToast(MainActivity.this, "Imagen no disponible");
        }
    }


    private void initTraslate() {
        // gif de carga
        Alerts.LoadingDialog(MainActivity.this);
        Alerts.showLoading();
        //descarga
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.SPANISH)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
        this.SpanishToEnglish = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        this.SpanishToEnglish.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                Alerts.closeLoading();
                                Alerts.MessageToast(MainActivity.this, "Paquete Descargado");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Alerts.closeLoading();
                                Alerts.MessageToast(MainActivity.this, "ErrPackTras: " + e.getMessage());
                            }
                        });
    }

    private void traslateCountryName(String text, int index) {
        if (this.SpanishToEnglish != null) {

            this.SpanishToEnglish.translate(text)
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String responseText) {
                                    //verifica la lista de elementos y que el indice buscado esté
                                    //entre los límites de la lista
                                    if (lista != null) {
                                        if (index >= 0 && index < lista.size()) {
                                            //obtiene los items
                                            String[] items = lista.get(index);
                                            if (items != null) {
                                                //agrega la traducción al vector
                                                items[1] = responseText;
                                            }
                                        }
                                        if (lista.size() - 1 == index) {
                                            //se envía la lista de datos a la tabla
                                            getDataVolley(responseText);
                                            //tableAdapt(lista, true);
                                        }
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Alerts.MessageToast(MainActivity.this, "ErrTrasWord: " + e.getMessage());
                                    if (lista.size() - 1 == index) {
                                        //se envía la lista de datos a la tabla
                                        tableAdapt(lista, true);
                                    }
                                }
                            });
        } else {
            Alerts.MessageToast(MainActivity.this, "Traductor no disponible");
        }
    }

    /*******************************************************************************************
     *                                  Etiquetado de imágenes                                 *
     *******************************************************************************************/
    private void getDataVolley(String param) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "http://www.geognos.com/api/en/countries/info/all.json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("-----------------------");
                        //MyLogs.info(response);
                        countryProcessRequest(response, param);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Alerts.MessageToast(MainActivity.this, "Error en Volley");
                        MyLogs.info(error.getMessage());
                        //cierra el modal una vez haya cargado la api
                        Alerts.closeLoading();
                    }
                }
        );
        queue.add(request);
    }

    private void countryProcessRequest(String response, String countryName) {
        //convertir respuesta de volley en Json de google
        JsonObject jso = Methods.stringToJSON(response);
        MyLogs.info("Estado: " + Methods.JsonToString(jso, "StatusMsg", ""));
        //verificamos el estado de la petición [parámetro que viene del json]
        if (Methods.JsonToInteger(jso, "StatusCode", 400) == 200) {
            //obtenemos los results, estas son las ciudades
            jso = Methods.JsonToSubJSON(jso, "Results");
            //validación de longitud de objetos captados
            if (jso.size() > 0) {
                //obtener todas las keys del objeto json
                Set<String> keys = jso.keySet();
                MyLogs.info(countryName + ": Entre :" + keys.size());
                //ciclar los objetos
                for (String key : keys) {
                    //objetner el objeto jsond e turno en base a la key
                    JsonObject minjso = Methods.JsonToSubJSON(jso, key);
                    MyLogs.info("key:" + key + " => " + Methods.JsonToString(minjso, "Name", ""));
                    //validar si lo que la foto capturó es un nombre encontrado en el json
                    if (Methods.JsonToString(minjso, "Name", "").toLowerCase().equals(countryName.toLowerCase())) {
                        //guardamos el objeto en el json del scope de función
                        JsonCountry = minjso;
                        MyLogs.error("aquí: " + key);
                    }
                }
            }
        }
        //si encontramos un objeto compatible con el nombre de una ciudad, vamos a ir al mapa
        MyLogs.info("-------------------------\n nuevo viaje: " + (JsonCountry != null));
        if (JsonCountry != null) {
            MyLogs.info(JsonCountry.toString());
            setDataInTable(JsonCountry, true);
        } else {
            Alerts.MessageToast(MainActivity.this, "Ciudad no encontrada");
        }
    }

    private void setDataInTable(JsonObject obj, boolean isRoot) {
        Set<String> keys = obj.keySet();
        for (String key : keys) {
            //MyLogs.info(key + ": " + key.equals("CountryInfo"));
            if (!key.equals("CountryInfo")) {
                JsonElement jse = Methods.securGetJSON(obj, key);
                if (!jse.isJsonObject()) {
                    String val = jse.toString();
                    lista.add(new String[]{key, val});
                } else {
                    JsonObject minjso = Methods.JsonElementToJSO(jse);
                    setDataInTable(minjso, false);
                }
            }else{
                String path = Methods.JsonToString(obj, "CountryInfo", "");
                if(path.length() > 0){
                    Button buttonOpenMap = (Button) findViewById(R.id.btnvisit);
                    buttonOpenMap.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //MyLogs.info(path);
                            Alerts.openBrowser(path, MainActivity.this);
                        }
                    });
                }
            }
        }
        if (isRoot) {
            tableAdapt(lista, false);
        }
    }

    private void strartMap(JsonObject countryObj) {
        //desplegar
        Intent intent = new Intent(MainActivity.this, mapview.class);
        Bundle b = new Bundle();
        b.putString("countryData", countryObj.toString());
        intent.putExtras(b);
        startActivity(intent);
    }

}