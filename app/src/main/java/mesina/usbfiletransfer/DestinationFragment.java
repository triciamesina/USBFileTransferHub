package mesina.usbfiletransfer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DestinationFragment extends DialogFragment {

    // Add list of items
    final CharSequence usblist[] = new CharSequence[]{"USB1", "USB2", "USB3", "USB4"};
    String dest;
    public static int DESTINATION_FRAGMENT = 2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose the destination");
        builder.setPositiveButton("OK", new PositiveButtonClickListener());
        CharSequence[] usblist = new CharSequence[]{"USB1", "USB2", "USB3", "USB4"};
        builder.setSingleChoiceItems(usblist, -1, selectItemListener);
        return builder.create();

    }

    class PositiveButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dest!=null) {
                Toast.makeText(getActivity(), dest + "selected", Toast.LENGTH_SHORT).show();
                Bundle dst = new Bundle();
                dst.putString("destination", dest);
                MainActivity main = (MainActivity) getActivity();
                main.saveData(DESTINATION_FRAGMENT, dst);
                PasteFragment fragment = new PasteFragment();
                getFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, fragment).commit();
            } else {
                Toast.makeText(getActivity(), "Choose a destination", Toast.LENGTH_SHORT).show();
            }
        }
    }

    DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dest = (String) usblist[which];
        }
    };

}


