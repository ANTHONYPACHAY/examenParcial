package anthony.uteq.examenfinal.utiles;

import android.util.Log;

public final class MyLogs {

    private static String label = "MyLogs";

    public MyLogs() {
    }

    public MyLogs(String label) {
        this.label = label;
    }

    public static String getLabel() {
        return label;
    }

    public static void setLabel(String label) {
        MyLogs.label = label;
    }

    public static void error(String text) {
        Log.e(label, text);
    }

    public static void warning(String text) {
        Log.w(label, text);
    }

    public static void info(String text) {
        Log.i(label, text);
    }

    public static void depuration(String text) {
        Log.d(label, text);
    }

    public static void detailedLog(String text) {
        Log.v(label, text);
    }
}
