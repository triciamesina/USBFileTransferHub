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

    private String[] dirfiles;
    private ArrayList<String> directoryShow = new ArrayList<String>();
    RecyclerView recyclerView;
    RenameAdapter mAdapter;
    int src = 0;
    public static int RENAME_FRAGMENT = 2;


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
                        directoryShow = new ArrayList<String> (Arrays.asList(dirfiles));
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
                String selectedFile = directoryShow.get(position);
                Toast.makeText(getActivity(), selectedFile + " selected", Toast.LENGTH_SHORT).show();
                Bundle extras = new Bundle();
                MainActivity main = (MainActivity) getActivity();
            }
        }));


    // Setup Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder getNewName = new AlertDialog.Builder(getActivity());
                getNewName.setTitle("Rename");
                getNewName.setMessage("Enter the new name (Maximum of 8 characters)");

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
                            Toast.makeText(getActivity(), "File renamed!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            MainFragment home = new MainFragment();
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                        }
                    }
                });
            }
        });


        return rootView;
    }

}
