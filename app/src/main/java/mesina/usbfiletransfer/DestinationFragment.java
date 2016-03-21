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

import java.util.ArrayList;

public class DestinationFragment extends DialogFragment {

    // Add list of items
    CharSequence[] destlist;
    String dest;
    public static int DESTINATION_FRAGMENT = 2;
    Bundle extras;
    MainActivity main = (MainActivity) getActivity();
    String fn;
    int count = 1;

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
        switch (source) {
            case 1:
                destlist = usb1;
                break;
            case 2:
                destlist = usb2;
                break;
            case 3:
                destlist = usb3;
                break;
            case 4:
                destlist = usb4;
                break;
        }
        extras = getArguments();
        if (extras != null) {
            count = extras.getInt("count");
            fn = extras.getString("fn");
            builder.setSingleChoiceItems(destlist, -1, selectItemListener);
        }
        Toast.makeText(getActivity(), "chosen file" + fn, Toast.LENGTH_SHORT).show();
        return builder.create();
    }

    private class PositiveButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getActivity(), dest + "selected", Toast.LENGTH_SHORT).show();
                searchDir();
            }
        }




    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Warning");
        dialog.setMessage("The filename already exists in this drive. Overwrite file?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addData();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void searchDir() {
        MainActivity main = (MainActivity) getActivity();
        ArrayList<String> dir = main.checkSame(dest);
        if (dir != null) {
            for (int i = 0; i < dir.size(); i++) {
                if (dir.get(i).equals(fn)) {
                    showAlert();
                }
            }
            addData();
        }
    }

    public void addData() {
        Bundle dst = new Bundle();
        MainActivity main = (MainActivity) getActivity();
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
            Bundle send = new Bundle();
            int newcount = count+1;
            send.putInt("count", newcount);
            PasteFragment fragment = new PasteFragment();
            fragment.setArguments(send);
            getFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, fragment).commit();

        }
    }

    DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dest = (String) destlist[which];
        }
    };

}


