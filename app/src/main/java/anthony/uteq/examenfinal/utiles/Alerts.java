package anthony.uteq.examenfinal.utiles;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import anthony.uteq.examenfinal.R;


public class Alerts {
    private static Dialog mLoadingDialog;

    public static void openBrowser(String url, Context context){
        Uri enlace = Uri.parse(url);
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(enlace);
        context.startActivity(in);
    }

    public static void MessageToast(Context ctx, String message){
        Toast toast= Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void LoadingDialog(Context context) {
        // Get the view

        mLoadingDialog = new Dialog(context);
        //mLoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.loading, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.container_loading);
        ImageView gifView = view.findViewById(R.id.git_loading);
        Glide.with(context).load(R.drawable.gatito2).into(gifView);

        mLoadingDialog.setContentView(view);

    }

    public static void showLoading(){
        if (mLoadingDialog!=null) {
            mLoadingDialog.show();
        }else{
            MyLogs.error("No se ha ininicalizado el swal");
        }
    }

    public static void closeLoading(){
        if (mLoadingDialog!=null) {
            mLoadingDialog.dismiss();
            mLoadingDialog=null;
        }
    }

    /*public static void loading(Activity ctx){
        new TTFancyGifDialog.Builder(ctx)
                .setTitle(String.valueOf(R.string.loading_tittle))
                .setMessage(String.valueOf(R.string.loading_description))
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#22b573")
                .setGifResource(R.drawable.gatito)      //pass your gif, png or jpg
                .isCancellable(true)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(ctx,"Ok",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }*/
}
