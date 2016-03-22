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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DestinationFragment extends DialogFragment {

    // Add list of items
    CharSequence[] destlist;
    String destdrv;
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
        builder.setPositiveButton("OK", new PositiveButtonClickListener());
        builder.setTitle("Choose the destination");
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
            builder.setSingleChoiceItems(destlist, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    destdrv = (String) destlist[which];
                    Log.d("thebluetooth", "destdrv " +destdrv);
                }
            });
        }
        Toast.makeText(getActivity(), "chosen file" + fn, Toast.LENGTH_SHORT).show();
        return builder.create();
    }

    private class PositiveButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getActivity(), destdrv + " selected", Toast.LENGTH_SHORT).show();
            searchDir(destdrv);
            Log.d("thebluetooth", "destdrv " + destdrv);
        }
    }


/*
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
    }*/

    private void showAlert() {

        final AlertDialog.Builder overwrite = new AlertDialog.Builder(getActivity());
        overwrite.setTitle("Overwrite File!");
        overwrite.setMessage("The filename already exists in this drive. Overwrite file?");
        overwrite.setPositiveButton("YES", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addData(destdrv);
                Log.d("thebluetooth", "destdrv " + destdrv);
            }
        });
        overwrite.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                overwrite.create().dismiss();
            }
        });
        overwrite.create().show();
    }

    private void searchDir(String dest) {
        MainActivity main = (MainActivity) getActivity();
        ArrayList<String> dir = main.checkSame(dest);
        if (dir != null) {
            Log.d("thebluetooth", fn);
            for (int i = 0; i < dir.size(); i++) {
                Log.d("thebluetooth", dir.get(i));
                if (dir.get(i).equals(fn) || dir.get(i).equals(fn.toLowerCase())) {
                    Toast.makeText(getActivity(), "Same fn " + fn + " "+ dir.get(i), Toast.LENGTH_SHORT).show();
                    showAlert();
                }
            }
            addData(destdrv);
            Log.d("thebluetooth", "destdrv " + destdrv);
        }
    }

    public void addData(String dest) {
        Bundle dst = new Bundle();
        MainActivity main = (MainActivity) getActivity();
        if(extras != null) {
            count = extras.getInt("count");
            Log.d("thebluetooth", "destdrv " +destdrv);
            Log.d("thebluetooth", "dest " + dest);
            switch(count) {
                case 0:
                    dst.putString("dest1", dest);
                    destdrv = dest;
                    //main.dest1.equals(destdrv);
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
            switch (dest) {
                case "USB1":
                    main.mConnectedThread.write("m");
                    break;
                case "USB2":
                    main.mConnectedThread.write("n");
                    break;
                case "USB3":
                    main.mConnectedThread.write("o");
                    break;
                case "USB4":
                    main.mConnectedThread.write("p");
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
            destdrv = (String) destlist[which];
        }
    };

}


