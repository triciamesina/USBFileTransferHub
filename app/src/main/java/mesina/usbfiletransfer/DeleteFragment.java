package mesina.usbfiletransfer;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteFragment extends Fragment {

    private ArrayList<String> dirfiles;
    private ArrayList<String> deleteList;
    RecyclerView recyclerView;
    DirectoryAdapter mAdapter;
    int src = 0;
    public static int DELETE_FRAGMENT = 2;
//public Communicator comm1;

    public DeleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_delete, container, false);
        final MainActivity main = (MainActivity) getActivity();
        main.setActionBarTitle("Delete files");

        deleteList = main.getDeleteList();
        Bundle chosen = this.getArguments();
        if (chosen != null) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    main.mConnectedThread.write("A");
                }
            }, 500);
        }

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


        // Setup Choose Button
        final Button chooseButton = (Button) rootView.findViewById(R.id.chooseButton);
        assert chooseButton != null;
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                if (deleteList.size() < 15) {
                    Bundle args = new Bundle();
                    args.putStringArrayList("directory", dirfiles);
                    if (src != 0) {
                        if (args == null) {
                            Toast.makeText(getActivity(), "No drive detected", Toast.LENGTH_SHORT).show();
                        } else {
                                switch (src) {
                                    case 1:
                                        main.mConnectedThread.write("E"); // set usb1 as source
                                        break;
                                    case 2:
                                        main.mConnectedThread.write("F"); // set usb2 as source
                                        break;
                                    case 3:
                                        main.mConnectedThread.write("G"); // set usb3 as source
                                        break;
                                    default:
                                        main.mConnectedThread.write("H"); // set usb4 as source
                                        break;
                                }
                            Bundle source = new Bundle();
                            source.putInt("src", src);
                            args.putInt("src", src);
                            args.putInt("ope", DELETE_FRAGMENT);
                            main.saveData("source", source);
                            ChooseFragment directoryFragment = new ChooseFragment();
                            directoryFragment.setArguments(args);
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.fragment_container, directoryFragment).commit();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Choose a source drive", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Selection list is full!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Get recyclerview id from layout
        recyclerView = (RecyclerView) rootView.findViewById(R.id.deleteRecyclerView);
        // Directory adapter
        mAdapter = new DirectoryAdapter(deleteList);
        // Get recyclerview layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        // Setup Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Proceed?", Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int size = deleteList.size();
                        if (size < 1) {
                            Toast.makeText(getActivity(), "Add files", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Delete Started", Toast.LENGTH_SHORT).show();
                            main.mConnectedThread.write("q");
                            main.selectedFile = " ";
                            MainFragment home = new MainFragment();
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                            main.deleteList.clear();
                        }
                    }
                }).show();
            }
        });

        return rootView;
    }


}
