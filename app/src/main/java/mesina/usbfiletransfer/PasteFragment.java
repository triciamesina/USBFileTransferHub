package mesina.usbfiletransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class PasteFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
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
/*
        // Setup Recycler View
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.selectionRecyclerView);
        mAdapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
*/

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_paste, container, false);

        // Setup Spinner
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int source = parent.getSelectedItemPosition();
                switch(source) {
                    case 0:
                        dirfiles = filesList;
                        break;
                    case 1:
                        dirfiles = filesList2;
                        break;
                    default:
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
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                if (arrayList.size() < 10) {
                    Bundle args = new Bundle();
                    args.putStringArray("directory", dirfiles);
                    if (args == null) {
                        Toast.makeText(getActivity(), "No drive detected", Toast.LENGTH_SHORT).show();
                    } else {
                        ChooseFragment directoryFragment = new ChooseFragment();
                        directoryFragment.setArguments(args);
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.fragment_container, directoryFragment).commit();
                    }
                } else {
                    Toast.makeText(getActivity(), "Selection list is full!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        TextView selectLabel = (TextView) rootView.findViewById(R.id.selectedFile);
        Bundle select = this.getArguments();
        if (select == null) {
            selectLabel.setText("Select a file");
        } else {
            String selectedFile = select.getString("selected");
            selectLabel.setText(selectedFile);
        }

        // Setup Destination Text Label
        TextView destLabel = (TextView) rootView.findViewById(R.id.destTextView);
        if (select == null) {
            destLabel.setText("none selected");
        } else {
            String dest = select.getString("destination");
            destLabel.setText(dest);
        }

        return rootView;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
