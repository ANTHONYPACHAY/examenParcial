package anthony.uteq.examenfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import anthony.uteq.examenfinal.utiles.Alerts;
import anthony.uteq.examenfinal.utiles.Methods;
import anthony.uteq.examenfinal.utiles.MyLogs;
import anthony.uteq.examenfinal.utiles.ObjectLocation;
import anthony.uteq.examenfinal.utiles.SuperItem;
import anthony.uteq.examenfinal.utiles.Alerts;
import anthony.uteq.examenfinal.utiles.SuperItem;

public class MapManager implements OnMapReadyCallback, GoogleMap.OnMapClickListener
        //GoogleMap.InfoWindowAdapter,
        //GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemSelectedListener
{

    private GoogleMap myMap = null;
    private Context context;

    private RequestQueue queue;
    private JsonObject ObjectLocation = null;
    private ImageView imgflag;

    public MapManager(Context ctx, JsonObject data, ImageView imgflag) {
        this.context = ctx;
        this.ObjectLocation = data;
        this.imgflag = imgflag;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            //cuando el mapa esté completamente cargado
            myMap = googleMap;
            // habilitar controles de zoom
            myMap.getUiSettings().setZoomControlsEnabled(true);
            myMap.getUiSettings().setMyLocationButtonEnabled(true);
            //esta clase tiene el método para el clic del mapa, por ello se le asigna this
            myMap.setOnMapClickListener(MapManager.this);
            //vista customizada
            //myMap.setOnInfoWindowClickListener(MapManager.this);

            //myMap.setInfoWindowAdapter(MapManager.this);
            myMap.setMapType(4);

            genericPoinst();
        } else {
            Alerts.MessageToast(context, "Esta nulo el Mapa");
            //cierra el modal una vez haya cargado la api
            Alerts.closeLoading();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //clic en el mapa
    }

    public GoogleMap getMyMap() {
        return myMap;
    }

    public void setMyMap(GoogleMap myMap) {
        this.myMap = myMap;
    }

    /*public void setSpinner(Spinner spinner) {

        ArrayList<SuperItem> sOpciones = new ArrayList<>();
        sOpciones.add(new SuperItem("1", "Mapa Normal"));
        sOpciones.add(new SuperItem("2", "Mapa Satelital"));
        sOpciones.add(new SuperItem("3", "Mapa Topográfica"));
        sOpciones.add(new SuperItem("4", "Mapa Híbrido"));


        ArrayAdapter<SuperItem> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, sOpciones);

        spinner.setAdapter(adapter);
        spinner.setPrompt(sOpciones.get(0).getDisplay());

        spinner.setOnItemSelectedListener(MapManager.this);
    }*/

    /**
     * Métodos del spinner
     **/
    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SuperItem tmp = (SuperItem) parent.getItemAtPosition(position);
        String city = "The city is " + tmp.getValue();
        Toast.makeText(context, city, Toast.LENGTH_LONG).show();

        myMap.setMapType(Integer.parseInt(tmp.getValue()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
    public void genericPoinst() {

        if (ObjectLocation != null) {
            String countryName = Methods.JsonToString(ObjectLocation, "Name", "None");
            JsonObject jsonCapital = Methods.JsonToSubJSON(ObjectLocation, "Capital");
            double[] points = Methods.JsonToDubleVector(jsonCapital, "GeoPt");
            if (points.length > 0) {
                LatLng location = new LatLng(points[0], points[1]);
                Marker mark = myMap.addMarker(new
                        MarkerOptions().position(location)
                        .title(Methods.JsonToString(jsonCapital, "Name", "Name"))
                        .snippet("Capital de: " + countryName));

                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.start);
                //rescalar icono
                bm = Bitmap.createScaledBitmap(bm, 100, 120, false);
                //asignar icono
                mark.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(location)
                        .zoom(3)
                        .bearing(0)
                        .build();
                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                myMap.animateCamera(camUpd3);
            }


            JsonObject GeoRectangle = Methods.JsonToSubJSON(ObjectLocation, "GeoRectangle");

            double West = Methods.JsonTodouble(GeoRectangle, "West", 0.0);
            double East = Methods.JsonTodouble(GeoRectangle, "East", 0.0);
            double North = Methods.JsonTodouble(GeoRectangle, "North", 0.0);
            double South = Methods.JsonTodouble(GeoRectangle, "South", 0.0);
            MyLogs.info(West + " :: " + East + " :: " + North + " :: " + South);
            PolylineOptions polyPais = new PolylineOptions()
                    .clickable(false)
                    .add(new LatLng(North, West),//0 0
                    new LatLng(North, East),//0 1
                    new LatLng(South, East),// 1 1
                    new LatLng(South, West),//1 0
                    new LatLng(North, West));//0 0
            polyPais.color(Color.CYAN);
            polyPais.width(5);

            myMap.addPolyline(polyPais);

            JsonObject CountryCodes = Methods.JsonToSubJSON(ObjectLocation, "CountryCodes");
            String flag = Methods.JsonToString(CountryCodes, "iso2", "");
            //MyLogs.info("http://www.geognos.com/api/en/countries/flag/" + flag + ".png");
            Picasso.get().load("http://www.geognos.com/api/en/countries/flag/" + flag + ".png")
                    .error(R.drawable.portada)
                    .into(imgflag);

            //mover camara
            double[] GeoPt = Methods.JsonToDubleVector(ObjectLocation, "GeoPt");

            if (GeoPt.length == 2) {
                LatLng location = new LatLng(GeoPt[0], GeoPt[1]);
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(location)
                        .zoom(3)
                        .bearing(0)
                        .build();
                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                myMap.animateCamera(camUpd3);
            }
        }
        Alerts.closeLoading();
    }
}
