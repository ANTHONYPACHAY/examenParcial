package anthony.uteq.examenfinal.utiles;

public class DataStatic {

    private static String urlDomain = "https://ciclerfff.herokuapp.com";
    //"https://aplicaciones.uteq.edu.ec/" ciclero_server;
    private static String webservices = "/webresources/";

    public static String gerUrlApi(String servicePath){
        //persona/logIn
        return urlDomain + webservices + servicePath;
    }

}
