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
    CharSequence[] usblist;
    String dest;
    public static int DESTINATION_FRAGMENT = 2;
    Bundle extras;
    MainActivity main = (MainActivity) getActivity();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MainActivity main = (MainActivity) getActivity();
        int source = main.getSource();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose the destination");
        builder.setPositiveButton("OK", new PositiveButtonClickListener());
        CharSequence[] usb1 = new CharSequence[]{"USB2", "USB3", "USB4"};
        CharSequence[] usb2 = new CharSequence[]{"USB1", "USB3", "USB4"};
        CharSequence[] usb3 = new CharSequence[]{"USB1", "USB2", "USB4"};
        CharSequence[] usb4 = new CharSequence[]{"USB1", "USB2", "USB3"};
        switch(source) {
            case 1:
                usblist = usb1;
                break;
            case 2:
                usblist = usb2;
                break;
            case 3:
                usblist = usb3;
                break;
            case 4:
                usblist = usb4;
                break;
        }
        builder.setSingleChoiceItems(usblist, -1, selectItemListener);
        return builder.create();

    }

    class PositiveButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            MainActivity main = (MainActivity) getActivity();
            if (dest!=null) {
                Toast.makeText(getActivity(), dest + "selected", Toast.LENGTH_SHORT).show();
                Bundle dst = new Bundle();
                int count = 1;
                extras = getArguments();
                if(extras != null) {
                    count = extras.getInt("count");
                    switch(count) {
                        case 0:
                            dst.putString("dest1", dest);
                            main.saveData("dest1", dst);
                            break;
                        case 1:
                            dst.putString("dest2", ", "+dest);
                            main.saveData("dest2", dst);
                            break;
                        case 2:
                            dst.putString("dest3", ", "+dest);
                            main.saveData("dest3", dst);
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Number of destinations exceeded", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                Bundle send = new Bundle();
                int newcount = count+1;
                send.putInt("count", newcount);
                PasteFragment fragment = new PasteFragment();
                fragment.setArguments(send);
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


