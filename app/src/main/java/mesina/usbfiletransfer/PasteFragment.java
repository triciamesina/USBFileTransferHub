package mesina.usbfiletransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class PasteFragment extends Fragment {


    public static int PASTE_FRAGMENT = 0;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<selection> arrayList;
    ArrayList<String> dirfiles;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    int src;
    String selectedFile, dest1, dest2, dest3, filename;
    public Communicator comm;
    public PasteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity main = (MainActivity) getActivity();
        Bundle fromActivity = main.getSavedData();
      //  Bundle fromHome = this.getArguments();
   //     String title = fromHome.getString("title");
        arrayList = fromActivity.getParcelableArrayList("list");
        Bundle destinations = main.getDestinations();
        main.setActionBarTitle("Paste files");
        if (destinations != null) {
            dest1 = destinations.getString("dest1");
            dest2 = destinations.getString("dest2");
            dest3 = destinations.getString("dest3");
        }
        selectedFile = main.getSelectedFile();
        if (selectedFile != " ") {
            filename = selectedFile.substring(selectedFile.lastIndexOf(":")+1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int count = 0;
        final MainActivity main = (MainActivity) getActivity();
        Bundle fromFragments = this.getArguments();
        // Number of destinations
        if (fromFragments != null) {
            count = fromFragments.getInt("count");
        }

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_paste, container, false);

        // Setup Spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
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

                    @Override
                    public void onClick(View v) {
                        if (arrayList.size() < 15) {
                            Bundle args = new Bundle();
                            args.putStringArrayList("directory", dirfiles);
                            if (src != 0) {
                                String i = "E";
                                    comm.respond(i);
                                    Bundle source = new Bundle();
                                    source.putInt("src", src);
                                    args.putInt("ope", PASTE_FRAGMENT);
                                    main.saveData("source", source);
                                    ChooseFragment directoryFragment = new ChooseFragment();
                                    directoryFragment.setArguments(args);
                                    FragmentManager fm = getFragmentManager();
                                    fm.beginTransaction().replace(R.id.fragment_container, directoryFragment).commit();
                                } else {
                                Toast.makeText(getActivity(), "Choose a source drive", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Selection list is full!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Setup Destination Button
        final Button destButton = (Button) rootView.findViewById(R.id.addDestButton);
        assert destButton != null;
        final int finalCount = count;
        destButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                if (selectedFile == " ") {
                    Toast.makeText(getActivity(), "Choose a file", Toast.LENGTH_SHORT).show();
                } else {
                    if (finalCount == 1 || finalCount == 2 || finalCount == 0) {
                        Bundle destCount = new Bundle();
                        destCount.putInt("count", finalCount);
                        destCount.putString("fn", filename);
                        DestinationFragment chooseDest = new DestinationFragment();
                        chooseDest.setArguments(destCount);
                        //   Toast.makeText(getActivity(), "sent "+finalCount, Toast.LENGTH_SHORT).show();
                        chooseDest.show(getFragmentManager(), "destinationDialog");
                    } else {
                        Toast.makeText(getActivity(), "Maximum number of destinations exceeded", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // Setup recycler view
        recyclerView = (RecyclerView) rootView.findViewById(R.id.selectionRecyclerView);
        mAdapter = new RecyclerAdapter(arrayList);
        if (recyclerView != null) {
            recyclerView.setAdapter(mAdapter);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
            recyclerView.addItemDecoration(itemDecoration);
        }

        // Setup Selected Text Label
        final TextView selectLabel = (TextView) rootView.findViewById(R.id.selectedFile);
        //  Bundle select = this.getArguments();
        // Setup Destination Text Label
        final TextView destLabel1 = (TextView) rootView.findViewById(R.id.destLabel);
        final TextView destLabel2 = (TextView) rootView.findViewById(R.id.desttextView2);
        final TextView destLabel3 = (TextView) rootView.findViewById(R.id.desttextView3);

        if (selectedFile != " ") {
            selectLabel.setText(selectedFile);
        }

        if (dest1 == " ") {
            destLabel1.setText("Choose destinations");
        } else if (dest1 != " ") {
            destLabel1.setText(dest1);
        }
        if (dest2 != " ") {
            destLabel2.setText(dest2);
        } else {
            dest2 = " ";
        }
        if (dest3 != " ") {
            destLabel3.setText(dest3);
        } else {
            dest3 = " ";
        }

        // Setup Clear Button
        final Button clearButton = (Button) rootView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = main.arrayList.size();
                main.arrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
                main.arrayList.add(new selection("Selected files", "Destinations"));
                mAdapter.notifyItemInserted(0);
                selectLabel.setText("Select a file");
                destLabel1.setText("Choose destinations");
                destLabel2.setText(" ");
                destLabel3.setText(" ");
                Bundle reset = new Bundle();
                reset.putString("selected", "");
                //  main.addToList(addtolist);
                main.resetData();
            }
        });

        // Setup Add Button
        final Button addButton = (Button) rootView.findViewById(R.id.addButton);
        assert destButton != null;
        final String finalSelectedFile = selectedFile;
        final String finalDest = dest1 + dest2 + dest3;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                if (finalSelectedFile == null || dest1 == " ") {
                    Toast.makeText(getActivity(), "Choose a file", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), finalSelectedFile + " added!", Toast.LENGTH_SHORT).show();
                    selection addtolist = new selection(finalSelectedFile, finalDest);
                    arrayList.add(addtolist);
                    mAdapter.notifyItemInserted(arrayList.size());
                    selectLabel.setText("Select a file");
                    destLabel1.setText("Choose destinations");
                    destLabel2.setText(" ");
                    destLabel3.setText(" ");
                    Bundle reset = new Bundle();
                    reset.putString("selected", "");
                  //  main.addToList(addtolist);
                    main.resetData();
                }
            }
        });

        // Setup Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Proceed?", Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int size = arrayList.size();
                        if (size <= 1) {
                            Toast.makeText(getActivity(), "Add files", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Transfer Started", Toast.LENGTH_SHORT).show();
                            MainFragment home = new MainFragment();
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                            main.arrayList.clear();
                            main.arrayList.add(new selection("Selected files", "Destinations"));
                        }
                    }
                }).show();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", arrayList);
        outState.putString("destination", dest1);
    }

  //  private Communicator comm = (Communicator) getActivity();

    public void setCommunicator(Communicator c)
    {
        comm = c;
    }
    public interface Communicator {

        void respond(String data);


    }
}
