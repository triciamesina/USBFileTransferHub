package mesina.usbfiletransfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class DirectoryFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> dirFiles = new ArrayList<String>();
    DirectoryLongClickAdapter mAdapter;
    int position;
    private static final String TAG = "thebluetooth";
    private static final int TAB_FRAGMENT = 4;
    int s=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_directory, container, false);
        MainActivity main = (MainActivity) getActivity();
        main.src = 0;
        Bundle extras = this.getArguments();
        s = extras.getInt("src");
        Log.d(TAG, "from args "+ s);
        Log.d(TAG, "source is "+ main.src);
        //   dirFiles = extras.getStringArrayList("directory");
        switch (s) {
            case 1:
                dirFiles = main.usb1List;
                break;
            case 2:
                dirFiles = main.usb2List;
                break;
            case 3:
                dirFiles = main.usb3List;
                break;
            default:
                dirFiles = main.usb4List;
                break;
        }
        //  Toast.makeText(getActivity(), "operation" + ope, Toast.LENGTH_SHORT).show();
        if (dirFiles != null) {

            // Get recyclerview id from layout
            recyclerView = (RecyclerView) rootView.findViewById(R.id.viewRecyclerView);
            registerForContextMenu(recyclerView);
            // Directory adapter
            mAdapter = new DirectoryLongClickAdapter(dirFiles);
            // Get recyclerview layout manager
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
            recyclerView.addItemDecoration(itemDecoration);
        }



        return rootView;
    }

    private void createAlert(final String oldName) {
        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder getNewName = new AlertDialog.Builder(getActivity());
        getNewName.setTitle("Rename");
        String oldfn = oldName.substring(oldName.lastIndexOf(":") + 1);
        getNewName.setMessage(oldfn + " selected. Enter the new name: (Maximum of 8 characters)");

        final EditText input = new EditText(getActivity());
        getNewName.setView(input);
        getNewName.setPositiveButton("OK", null);
        final AlertDialog alertDialog = getNewName.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = input.getEditableText().toString();
                if (newname.length() > 8) {
                    Toast.makeText(getActivity(), "Maximum of 8 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    main.mConnectedThread.write(newname);
                    main.mConnectedThread.write("-");
                    Toast.makeText(getActivity(), "File renamed to " + newname + "!", Toast.LENGTH_SHORT).show();
                    MainFragment home = new MainFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                    alertDialog.dismiss();
                }
            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Bundle pos = new Bundle();
        pos = this.getArguments();
        position = DirectoryLongClickAdapter.getPosition();
        MainActivity main = (MainActivity) getActivity();
        Log.d(TAG, "position is" + position);
        Log.d(TAG, "directory " + main.src);

        switch (main.src) {
            case 1:
                dirFiles = main.usb1List;
                break;
            case 2:
                dirFiles = main.usb2List;
                break;
            case 3:
                dirFiles = main.usb3List;
                break;
            case 4:
                dirFiles = main.usb4List;
                break;
            default:
                dirFiles = main.usb4List;
                break;
        }

        if (getUserVisibleHint()) {
            switch (item.getItemId()) {

                case (R.id.action_transfer):
                    main.mConnectedThread.write(String.valueOf(position));
                    main.mConnectedThread.write("-");
                    switch (main.src) {
                        case 1:
                            main.selectedFile = main.usb1List.get(position);
                            break;
                        case 2:
                            main.selectedFile = main.usb2List.get(position);
                            break;
                        case 3:
                            main.selectedFile = main.usb3List.get(position);
                            break;
                        case 4:
                            main.selectedFile = main.usb4List.get(position);
                            break;
                        default:
                            main.selectedFile = main.usb1List.get(position);
                            break;

                    }
                 //   main.selectedFile = dirFiles.get(position);
                 /*   PasteFragment paste = new PasteFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, paste).commit();*/
                    Log.d(TAG, "selected file " + main.selectedFile);
                    TransferFragment transfer = new TransferFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, transfer).commit();
                    Toast.makeText(getActivity(), "Choose a destination", Toast.LENGTH_SHORT).show();
                    break;

                case (R.id.action_delete):
                    main.mConnectedThread.write(String.valueOf(position));
                    main.mConnectedThread.write("-");
                    main.mConnectedThread.write("A");
                    switch (main.src) {
                        case 1:
                            main.selectedFile = main.usb1List.get(position);
                            break;
                        case 2:
                            main.selectedFile = main.usb2List.get(position);
                            break;
                        case 3:
                            main.selectedFile = main.usb3List.get(position);
                            break;
                        case 4:
                            main.selectedFile = main.usb4List.get(position);
                            break;
                        default:
                            main.selectedFile = main.usb1List.get(position);
                            break;

                    }
                    main.deleteList.add(main.selectedFile);
                   /* DeleteFragment delete = new DeleteFragment();
                    Bundle chosen = new Bundle();
                    chosen.putInt("file", position);
                    delete.setArguments(chosen);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, delete).commit();
                    Toast.makeText(getActivity(), "Add more files", Toast.LENGTH_SHORT).show(); */
                    Dialog confirm = confirm(main.selectedFile);
                    confirm.show();
                    break;

                case (R.id.action_rename):
                    main.mConnectedThread.write(String.valueOf(position));
                    main.mConnectedThread.write("-");
                    main.mConnectedThread.write("r");
                    switch (main.src) {
                        case 1:
                            main.selectedFile = main.usb1List.get(position);
                            break;
                        case 2:
                            main.selectedFile = main.usb2List.get(position);
                            break;
                        case 3:
                            main.selectedFile = main.usb3List.get(position);
                            break;
                        case 4:
                            main.selectedFile = main.usb4List.get(position);
                            break;
                        default:
                            main.selectedFile = main.usb1List.get(position);
                            break;

                    }
                    createAlert(main.selectedFile);
                    break;
            }
            return true;
        }
        return false;
    }


    public Dialog confirm(String selectedfile) {

        final MainActivity main = (MainActivity) getActivity();
        final AlertDialog.Builder proceed = new AlertDialog.Builder(getActivity());
        proceed.setTitle("Proceed?");
        proceed.setMessage("Delete this file: " + main.selectedFile + "?");
        proceed.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Delete Started", Toast.LENGTH_SHORT).show();
                main.mConnectedThread.write("q");
                main.selectedFile = " ";
                main.deleteList.clear();
                dialog.dismiss();
                MainFragment home = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
            }
        });
        proceed.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return proceed.create();
    }


}
