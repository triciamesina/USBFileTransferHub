package mesina.usbfiletransfer;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class RenameFragment extends Fragment {

    private ArrayList<String> dirfiles;
    private ArrayList<String> directoryShow = new ArrayList<String>();
    RecyclerView recyclerView;
    RenameAdapter mAdapter;
    int src = 0;
    public static int RENAME_FRAGMENT = 2;
    String oldName;
    int pos;

    public RenameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rename, container, false);

        final MainActivity main = (MainActivity) getActivity();
        main.setActionBarTitle("Rename files");

        // Setup Spinner
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int source = parent.getSelectedItemPosition();
                switch (source) {
                    case 1:
                        src = 1;
                        dirfiles = main.getDirectory(src);
                        break;
                    case 2:
                        src = 2;
                        dirfiles = main.getDirectory(src);
                        break;
                    case 3:
                        src = 3;
                        dirfiles = main.getDirectory(src);
                        break;
                    case 4:
                        src = 4;
                        dirfiles = main.getDirectory(src);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get recyclerview id from layout
        recyclerView = (RecyclerView) rootView.findViewById(R.id.renameRecyclerView);

        // Setup Choose Button
        final Button chooseButton = (Button) rootView.findViewById(R.id.chooseButton);
        assert chooseButton != null;
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                    if (src != 0) {
                        switch (src) {
                            case 1:
                                main.mConnectedThread.write("E");
                                break;
                            default:
                                main.mConnectedThread.write("F");
                                break;
                        }
                        directoryShow = dirfiles;
                        // Directory adapter
                        mAdapter = new RenameAdapter(directoryShow);
                        // Get recyclerview layout manager
                        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(mAdapter);
                        RecyclerView.ItemDecoration itemDecoration = new
                                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
                        recyclerView.addItemDecoration(itemDecoration);
                    } else {
                        Toast.makeText(getActivity(), "Choose a source drive", Toast.LENGTH_SHORT).show();
                    }
                }
        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                oldName = directoryShow.get(position);
                pos = position;
                main.mConnectedThread.write(Integer.toString(position));
                Toast.makeText(getActivity(), oldName + " selected", Toast.LENGTH_SHORT).show();
                Bundle extras = new Bundle();
                MainActivity main = (MainActivity) getActivity();
                extras.putString("oldname", oldName);
                main.saveData("oldname", extras);
                createAlert(oldName);
            }
        }));

        return rootView;
    }

    private void createAlert(final String oldName) {
        final MainActivity main = (MainActivity) getActivity();
        final String root = oldName.substring(0, oldName.lastIndexOf(":")+1);
        final String ext = oldName.substring(oldName.lastIndexOf("."));

        AlertDialog.Builder getNewName = new AlertDialog.Builder(getActivity());
        getNewName.setTitle("Rename");
        String oldfn = oldName.substring(oldName.lastIndexOf(":")+1);
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
                    Toast.makeText(getActivity(), "File renamed to " + newname +"!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    switch (src){
                        case 1:
                            main.usb1List.set(main.usb1List.indexOf(oldName), root.concat(newname).concat(ext));
                            mAdapter.notifyItemChanged(main.usb1List.indexOf(oldName));
                            break;
                        case 2:
                            main.usb2List.set(main.usb2List.indexOf(oldName), root.concat(newname).concat(ext));
                            mAdapter.notifyItemChanged(main.usb2List.indexOf(oldName));
                            break;
                        case 3:
                            main.usb3List.set(main.usb3List.indexOf(oldName), root.concat(newname).concat(ext));
                            mAdapter.notifyItemChanged(main.usb3List.indexOf(oldName));
                            break;
                        default:
                            main.usb4List.set(main.usb4List.indexOf(oldName), root.concat(newname).concat(ext));
                            mAdapter.notifyItemChanged(main.usb4List.indexOf(oldName));
                            break;
                    }
                    MainFragment home = new MainFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                }
            }
        });

    }

}
