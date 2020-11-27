package Message;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Message {
    public static void showMessage(Context context, String message)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("ВЗРЫВ");
        dlgAlert.setPositiveButton(":(", null);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton(":(",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dlgAlert.create().show();
    }

}