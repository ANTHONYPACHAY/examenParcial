package anthony.uteq.examenfinal.utiles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class TableModel {

    private Context ctx;
    private Resources resources;
    private TableLayout table;

    private int headerBackGroundColor = 500038;//black
    private int rowsBackGroundColor = 500061;//white

    private int headersForeGroundColor = 500061;//white
    private int rowsForeGroundColor = 500038;//black

    private int fontSize = 14;

    private String[] headers;
    private ArrayList<String[]> rows;

    public TableModel(Context ctx, TableLayout table) {
        this.ctx = ctx;
        this.table = table;
        headers = new String[0];
        rows = new ArrayList<>();
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;

    }

    public ArrayList<String[]> getRows() {
        return this.rows;
    }

    public void setRows(ArrayList<String[]> rows) {
        this.rows = rows;

    }

    public void makeTable() {
        //limpia la tabla
        table.removeAllViews();
        //crea encabezados y el contenido
        createRow(this.headers, true);
        createTable(this.rows);
    }

    /* Config */
    public void setHeaderBackGroundColor(int headerBackGroundColor) {
        this.headerBackGroundColor = headerBackGroundColor;
    }

    public void setRowsBackGroundColor(int rowsBackGroundColor) {
        this.rowsBackGroundColor = rowsBackGroundColor;
    }

    public void setHeadersForeGroundColor(int headersForeGroundColor) {
        this.headersForeGroundColor = headersForeGroundColor;
    }

    public void setRowsForeGroundColor(int rowsForeGroundColor) {
        this.rowsForeGroundColor = rowsForeGroundColor;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /* Private Functions */
    private TableRow newRow() {
        //crea nueva columna
        TableRow tmpRow = new TableRow(this.ctx);
        return tmpRow;
    }

    private TextView newCell() {
        //crea celda
        TextView tmpCell = new TextView(this.ctx);
        //centra el contenido
        tmpCell.setGravity(Gravity.CENTER);
        //establece tamaño
        tmpCell.setTextSize(fontSize);
        //padding
        tmpCell.setPadding(15,5,15,5);
        return tmpCell;
    }

    private void createTable(ArrayList<String[]> data) {
        //cicla creando las filas
        for (int ind = 0; ind < data.size(); ind++) {
            createRow(data.get(ind), false);
        }
    }

    private void createRow(String[] value, boolean isHead) {
        //creo una nueva fila
        TableRow tmpRow = newRow();
        //ciclo creando las columnas
        for (int ind = 0; ind < value.length; ind++) {
            //agrega la columnas a la fila
            createCell(tmpRow, isHead, value[ind]);
        }
        //se establece el color a la fila
        if(isHead){
            tmpRow.setBackground(ContextCompat.getDrawable(ctx, headerBackGroundColor));
        }else{
            tmpRow.setBackground(ContextCompat.getDrawable(ctx, rowsBackGroundColor));
        }
        tmpRow.setGravity(Gravity.CENTER);
        tmpRow.setPaddingRelative(10, 0,0,0);
        //se ubica la fila de cabecera en la tabla
        table.addView(tmpRow);
    }

    private void createCell(TableRow row, boolean isHead, String value) {
        if (value == null) {
            value = "";
        }
        //crea una celda
        TextView tmpCell = newCell();
        //ubica contenido a celda
        tmpCell.setText(value.trim());
        //color del texto
        if(isHead){
            tmpCell.setTextColor(ContextCompat.getColor(ctx, headersForeGroundColor));
            tmpCell.setTypeface(tmpCell.getTypeface(), Typeface.BOLD);
        }else{
            tmpCell.setTextColor(ContextCompat.getColor(ctx, rowsForeGroundColor));
        }
        //agrega la celda a la fila
        row.addView(tmpCell, newTableRowParams());
    }

    private TableRow.LayoutParams newTableRowParams() {
        //parametros para espacio entre las filas
        TableRow.LayoutParams param = new TableRow.LayoutParams();
        param.setMargins(1, 1, 1, 1);
        param.weight = 1;
        return param;
    }
}
