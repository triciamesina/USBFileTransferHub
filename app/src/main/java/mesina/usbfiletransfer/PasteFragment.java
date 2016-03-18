package mesina.usbfiletransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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

    private Spinner spinner;
    View.OnClickListener mOnClickListener;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<selection> arrayList = new ArrayList<>();
    SharedPreferences sharedPref;
    String[] dirfiles;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    final String[] filesList = {"pdf", "png", "doc", "ppt", "txt"};
    final String[] filesList2 = {"pdf1", "png1", "doc1", "ppt1", "txt1"};
    final String[] usb2 = {"1:testpdf.pdf", "1:testppt.ppt", "1:testdoc.doc", "1:testtxt.txt", "1:testpng.png"};
    int src;
    String data;
    private static Bundle mRecyclerViewState;

    public PasteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Data inside list view
        String[] filesList = {"pdf", "png", "doc", "ppt", "txt"};
        String[] filesList2 = {"pdf1", "png1", "doc1", "ppt1", "txt1"};
        String[] usb2 = {"1:testpdf.pdf", "1:testppt.ppt", "1:testdoc.doc", "1:testtxt.txt", "1:testpng.png"};

        selection selectionList = new selection("Selected Files", "Destination");
        arrayList.add(selectionList);
        ((MainActivity) getActivity()).setActionBarTitle("Paste Files");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String selectedFile = null, dest = null;
        int count;
        MainActivity main = (MainActivity) getActivity();
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_paste, container, false);

        // Setup Spinner
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int source = parent.getSelectedItemPosition();
                switch (source) {
                    case 0:
                        src = 0;
                        dirfiles = null;
                        break;
                    case 1:
                        src = 1;
                        dirfiles = filesList;
                        break;
                    case 2:
                        src = 2;
                        dirfiles = filesList2;
                        break;
                    default:
                        src = 3;
                        dirfiles = usb2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Setup Choose Button
        final Button chooseButton = (Button) rootView.findViewById(R.id.chooseButton);
        String selected = null;
        assert chooseButton != null;
        Bundle args = new Bundle();
        args = this.getArguments();
        if (args != null) {
            chooseButton.setClickable(false);
        } else {
            chooseButton.setOnClickListener(new View.OnClickListener() {
                @Nullable
                @Override
                public void onClick(View v) {
                    if (arrayList.size() < 15) {
                        Bundle args = new Bundle();
                        args.putStringArray("directory", dirfiles);
                        if (src != 0) {
                            if (args == null) {
                                Toast.makeText(getActivity(), "No drive detected", Toast.LENGTH_SHORT).show();
                            } else {
                                ChooseFragment directoryFragment = new ChooseFragment();
                                directoryFragment.setArguments(args);
                                FragmentManager fm = getFragmentManager();
                                fm.beginTransaction().replace(R.id.fragment_container, directoryFragment).commit();
                                chooseButton.setClickable(false);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Choose a source drive", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Selection list is full!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Setup Destination Button
        final Button destButton = (Button) rootView.findViewById(R.id.addDestButton);
        assert destButton != null;
        destButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                DestinationFragment chooseDest = new DestinationFragment();
                chooseDest.show(getFragmentManager(), "destinationDialog");
            }
        });

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
        final TextView destLabel = (TextView) rootView.findViewById(R.id.destLabel);
        Bundle select = main.getSavedData();
        selectedFile = select.getString("selected");
        dest = select.getString("destination");
        if (selectedFile != null) {
            selectLabel.setText(selectedFile);

            destLabel.setText("Choose destinations");
        }

        if (dest == null) {
            destLabel.setText("Choose destinations");
        } else if (dest != null) {
            destLabel.setText(dest);
        }

        // Setup Add Button
        final Button addButton = (Button) rootView.findViewById(R.id.addButton);
        assert destButton != null;
        final String finalSelectedFile = selectedFile;
        final String finalDest = dest;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                if (finalSelectedFile == null || finalDest == null) {
                    Toast.makeText(getActivity(), "Choose a file", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), finalSelectedFile + " added!", Toast.LENGTH_SHORT).show();
                    arrayList.add(new selection(finalSelectedFile, finalDest));
                    mAdapter.notifyItemInserted(arrayList.size());
                    selectLabel.setText("Select a file");
                    destLabel.setText("Choose destinations");
                    chooseButton.setClickable(true);
                    addButton.setClickable(false);
                }
            }
        });

        return rootView;
    }

}
