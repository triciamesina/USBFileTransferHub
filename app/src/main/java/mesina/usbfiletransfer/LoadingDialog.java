package mesina.usbfiletransfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Tricia on 3/23/2016.
 */
public class LoadingDialog extends DialogFragment {

    public static final String FRAGMENT_TAG = "Loading";

    public static LoadingDialog newInstance() {

        LoadingDialog loading = new LoadingDialog();
        return loading;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle("Transfer started");
        dialog.setMessage("Please Wait");
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        return dialog;
    }
}
