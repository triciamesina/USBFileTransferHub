package mesina.usbfiletransfer;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingDialog extends DialogFragment {


    public LoadingDialog() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ProgressDialog loading = new ProgressDialog(getActivity());
        this.setStyle(STYLE_NO_TITLE, getTheme());
        loading.setMessage("Transfer in progress");
        loading.setCancelable(true);
        return loading;
    }
}