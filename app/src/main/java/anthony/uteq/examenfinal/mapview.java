package anthony.uteq.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.JsonObject;

import anthony.uteq.examenfinal.utiles.Alerts;
import anthony.uteq.examenfinal.utiles.Methods;

public class mapview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        loadMap();
    }

    private void loadMap(){
        //loading
        Alerts.LoadingDialog(mapview.this);
        Alerts.showLoading();
        //obtener datos heredado del activity principal
        Bundle bundle = this.getIntent().getExtras();
        String data = bundle.getString("countryData");
        JsonObject countryData = Methods.stringToJSON(data);
        //preparar mapa

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mymap);

        if (mapFragment != null) {
            ImageView imgflag = (ImageView) this.findViewById(R.id.imgflag);
            MapManager mapManager = new MapManager(mapview.this, countryData, imgflag);
            mapFragment.getMapAsync(mapManager);
            //Spinner opcion =(Spinner)findViewById(R.id.typesMaps);
            //mapManager.setSpinner(opcion);

        } else {
            Alerts.MessageToast(mapview.this, "Esta nulo el Mapa");
        }
    }
}