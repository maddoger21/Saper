package Victory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Victory {
    public static void showMessage(Context context, String message)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("ПОЗДРАВЛЯЮ");
        dlgAlert.setPositiveButton("УРА!", null);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("УРА!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dlgAlert.create().show();
    }

}
