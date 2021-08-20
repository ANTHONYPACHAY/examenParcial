package anthony.uteq.examenfinal.utiles;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tonyp * This java class contains the methods used within the back-end
 * of the application.
 */
public final class Methods {

    public static String getJsonMessage(String status, String information, String data) {
        return "{\"status\":" + status + ",\"information\":\"" + information + "\",\"data\":" + data + "}";
    }

    /**
     * This method is for the security application.
     *
     * @param email String type variable, contains the email.
     * @return a String, for the security request.
     */
    public static Boolean comprobeEmail(String email) {
        Pattern pat = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");//".*@uteq.edu.ec"
        Matcher mat = pat.matcher(email);
        if (mat.matches()) {
            return (email.length() <= 100);// length in database
        } else {
            return false;
        }
    }

    public static Boolean comprobePassword(String pass) {
        Pattern pat = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])\\w{6,}");///^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{6,}/
        Matcher mat = pat.matcher(pass);
        return mat.matches();// length in database
    }

    /**
     * Convert from string to json.
     *
     * @param json String type variable, contains the json to be converted.
     * @return a json.
     */
    public static JsonObject stringToJSON(String json) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject Jso = parser.parse(json).getAsJsonObject();
            return Jso;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new JsonObject();
        }
    }

    /**
     * Convert from string to json.
     *
     * @param json String type variable, contains the json to be converted.
     * @return a json.
     */
    public static JsonArray stringToJsonArray(String json) {
        try {
            JsonParser parser = new JsonParser();
            JsonArray jsa = parser.parse(json).getAsJsonArray();
            return jsa;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new JsonArray();
        }
    }

    /**
     * Convert from string to json.
     *
     * @param json String type variable, contains the json to be converted.
     * @return a json.
     */
    public static JsonElement stringToJSON2(String json) {
        try {
            JsonElement parser = new JsonPrimitive(json);
            System.out.println(parser.getAsString());
            //JsonObject Jso = new JsonObject();
            //Jso =  (JsonObject) parser.p(json);
            return parser;
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    /**
     * Get a part of the json.
     *
     * @param jso Variable type json, contains the information.
     * @param param String type variable, contains the name of the json
     * parameter to be divided.
     * @return a json, divided.
     */
    public static JsonElement securGetJSON(JsonObject jso, String param) {
        try {
            JsonElement res = jso.get(param);//request.getParameter(param);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method to divide a json.
     *
     * @param jso Variable type json, contains the information.
     * @param param String type variable, contains the name of the json
     * parameter to be divided.
     * @param defaulx String type variable, return variable
     * @return Return a String, with the json divided.
     */
    public static String JsonToSub(JsonObject jso, String param, String defaulx) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                return res.toString();
            } else {
                return defaulx;
            }
        } catch (Exception e) {
            return defaulx;
        }
    }

    /**
     * A sub json of a json.
     *
     * @param jso Variable type json, contains the information.
     * @param param String type variable, contains the name of the json
     * parameter to be divided.
     * @return a json.
     */
    public static JsonObject JsonToSubJSON(JsonObject jso, String param) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                return res.getAsJsonObject();
            } else {
                return new JsonObject();
            }
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    /**
     * From json to array.
     *
     * @param jso Variable type json, contains the information.
     * @param param String type variable, contains the name of the json
     * parameter to be divided.
     * @return a jsonArray, with data loaded
     */
    public static JsonArray JsonToArray(JsonObject jso, String param) {
        try {
            JsonArray jarr = jso.get(param).getAsJsonArray();
            if (jarr != null) {
                return jarr;
            } else {
                return new JsonArray();
            }
        } catch (Exception e) {
//            System.out.println("erro json a string");
            return new JsonArray();
        }
    }

    public static String[] JsonToStringVecttor(JsonObject jso, String param) {
        Gson gson = new Gson();

        try {
            String[] jarr = gson.fromJson(jso.get(param), String[].class);
            if (jarr != null) {
                return jarr;
            } else {
                return new String[]{};
            }
        } catch (Exception e) {
            return new String[]{};
        }
    }

    public static double[] JsonToDubleVector(JsonObject jso, String param) {
        Gson gson = new Gson();
        try {
            double[] jarr = gson.fromJson(jso.get(param), double[].class);
            if (jarr != null) {
                return jarr;
            } else {
                return new double[]{};
            }
        } catch (Exception e) {
            return new double[]{};
        }
    }

    /**
     * From json to String
     *
     * @param jso Variable type json, contains the information.
     * @param param String type variable, contains the name of the json
     * parameter to be divided.
     * @param defaulx String type variable, return variable
     * @return a String, with data loaded from the json.
     */
    public static String JsonToString(JsonObject jso, String param, String defaulx) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                String result = res.getAsString();
                result = result.trim().replace("\n", "\\n").replace("\t", "\\t").replace("'", "''");
                return result;
            } else {
                return defaulx;
            }
        } catch (Exception e) {
//            System.out.println("erro json a string");
            return defaulx;
        }
    }

    public static JsonObject objectToJson(Object jsonO) {
        try {
            return (JsonObject) jsonO;
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    public static JsonArray objectToJsonArray(Object jsonarrayO) {
        try {
            return (JsonArray) jsonarrayO;
        } catch (Exception e) {
            return new JsonArray();
        }
    }

    public static String JsonToStringWithFormat(JsonObject jso, String param, String defaulx) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                String result = res.getAsString();
                return result;
            } else {
                return defaulx;
            }
        } catch (Exception e) {
//            System.out.println("erro json a string");
            return defaulx;
        }
    }

    /**
     * Obtain an element from a Json, and store it in a String variable.
     *
     * @param jse The variable type JsonElement, contains the information.
     * @param defaulx String type variable, contains the element of the selected
     * json.
     * @return a variable of type String, selected element of the json.
     */
    public static String JsonElementToString(JsonElement jse, String defaulx) {
        try {
            if (jse != null) {
                return jse.getAsString();
            } else {
                return defaulx;
            }
        } catch (Exception e) {
            return defaulx;
        }
    }

    /**
     * from JsonElement to json.
     *
     * @param jse Variable type jsonElement, contains an element of another
     * json.
     * @return an object-type json
     */
    public static JsonObject JsonElementToJSO(JsonElement jse) {
        try {
            if (jse != null) {
                return jse.getAsJsonObject();
            } else {
                return new JsonObject();
            }
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    /**
     * from json to Integer.
     *
     * @param jso Variable type json, contains the information
     * @param param String type variable, contains the name of the json
     * parameter to be divided.
     * @param defaulx String type Integer, return variable
     * @return an integer, the variable is defaulx.
     */
    public static int JsonToInteger(JsonObject jso, String param, int defaulx) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                return res.getAsInt();
            } else {
                return defaulx;
            }
        } catch (Exception e) {
            return defaulx;
        }
    }

    /**
     * from json to boolean
     *
     * @param jso Variable type json, contains the information
     * @param param String type variable, contains the name of the json
     * parameter to be divided.
     * @param defaulx String type Boolean, return variable
     * @return an Boolean, the variable is defaulx.
     */
    public static Boolean JsonToBoolean(JsonObject jso, String param, boolean defaulx) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                return res.getAsBoolean();
            } else {
                return defaulx;
            }
        } catch (Exception e) {
            return defaulx;
        }
    }

    public static double JsonTodouble(JsonObject jso, String param, double defaulx) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                return res.getAsDouble();
            } else {
                return defaulx;
            }
        } catch (Exception e) {
            return defaulx;
        }
    }

    /**
     * Convert from string to integer, and then convert back to string.
     *
     * @param number String type variable, contains an integer to validate.
     * @return Returns a string, validating if it is integer.
     */
    public static String StringToIntegerString(String number) {
        int num;
        try {
            num = Integer.parseInt(number);
        } catch (Exception e) {
            num = -1;
        }
        return String.valueOf(num);
    }

    public static boolean isInteger(String number) {
        try {
            int num = Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean JsonValid(String json) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement jse = parser.parse(json);
            boolean flag1 = false, flag2 = false;
            try {
                jse.getAsJsonObject();
                flag1 = true;
            } catch (Exception e) {
                flag1 = false;
            }
            try {
                jse.getAsJsonArray();
                flag2 = true;
            } catch (Exception e) {
                flag2 = false;
            }
            return (flag1 || flag2);
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Boolean isValidCoordinates(String coordinatesValue) {
        //Longitud es coordenadas X, Latitud es coordeadas Y
        String twoDoublesRegularExpression = "-?[1-9][0-9]*(\\.[0-9]+)?,\\s*-?[1-9][0-9]*(\\.[0-9]+)?";
        return coordinatesValue.matches(twoDoublesRegularExpression);
    }

    public static Boolean testregex(String pattern, String text) {
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(text);
        return mat.matches();
    }

    public static Boolean verifyString(String text, String unEqString, int length) {
        if (!text.equals(unEqString)) {
            // -1 para indicar que no existe un maxlength
            if (length > -1) {
                return (text.length() <= length);
            } else {
                return true;
            }
        }
        return false;
    }

    public static Boolean verifyMaxLength(String text, int length) {
        return (text.length() <= length);
    }

    public static int wordsCount(String text, String spaces) {
        int contador = 1, pos;//**METODO PIRATEADO XD
        text = text.trim(); //eliminar los posibles espacios en blanco al principio y al final                              
        if (text.isEmpty()) { //si la cadena está vacía
            contador = 0;
        } else {
            pos = text.indexOf(spaces); //se busca el primer espacio en blanco
            while (pos != -1) {   //mientras que se encuentre un espacio en blanco
                contador++;    //se cuenta una palabra
                pos = text.indexOf(spaces, pos + spaces.length()); //se busca el siguiente espacio en blanco                       
            }                                    //a continuación del actual
        }
        return contador;
    }

    public static Boolean verifyMaxWords(String text, int length, String spaces) {
        int contador = wordsCount(text, spaces);
        return (contador > 0 && contador <= length);
    }

    public static Boolean verifyParraf(String text, int length, String spaces) {
        int contador = wordsCount(text, spaces);
        return (contador <= length);
    }

    public static JsonObject getIndixJarray(JsonArray jarr, int indice) {
        if (indice > -1 && indice < jarr.size()) {
            try {
                return jarr.get(indice).getAsJsonObject();
            } catch (Exception e) {
                return new JsonObject();
            }
        } else {
            return new JsonObject();
        }
    }

}
