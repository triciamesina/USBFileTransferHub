package mesina.usbfiletransfer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseFragment extends Fragment {

    private ArrayList<String> dirFiles = new ArrayList<>();
    private RecyclerView recyclerView;
    private DirectoryAdapter mAdapter;
    public static int CHOOSE_FRAGMENT = 1;

    public ChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_choose, container, false);

        String[] filesList;
        Bundle extras = this.getArguments();
        filesList = extras.getStringArray("directory");
        if (filesList != null) {
            final ArrayList<String> dirFiles = new ArrayList<String>(Arrays.asList(filesList));

            // Get recyclerview id from layout
            recyclerView = (RecyclerView) rootView.findViewById(R.id.dirRecyclerView);
            // Directory adapter
            mAdapter = new DirectoryAdapter(dirFiles);
            // Get recyclerview layout manager
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
            recyclerView.addItemDecoration(itemDecoration);

            recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    String selectedFile = dirFiles.get(position);
                    Toast.makeText(getActivity(), selectedFile + " selected", Toast.LENGTH_SHORT).show();
                    Bundle extras = new Bundle();
                    extras.putString("selected", selectedFile);
                    MainActivity main = (MainActivity) getActivity();
                    main.saveData("selected", extras);
                    PasteFragment fragment = new PasteFragment();
                   // fragment.setArguments(extras);
                    getFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, fragment).commit();
                }
            }));
        }
        return rootView;
    }

}
