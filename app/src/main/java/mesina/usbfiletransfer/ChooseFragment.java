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
    public static int PASTE_FRAGMENT = 0;
    public static int MOVE_FRAGMENT = 1;
    public static int DELETE_FRAGMENT = 2;

    public ChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_choose, container, false);
        final MainActivity main = (MainActivity) getActivity();
        String[] filesList;
        final int ope; // operation chosen
        Bundle extras = this.getArguments();
        ope = extras.getInt("ope");
        int s = extras.getInt("src");
     //   dirFiles = extras.getStringArrayList("directory");
        switch(main.src) {
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

                    String selected = dirFiles.get(position);
                    main.selectedFile = selected;
                    main.mConnectedThread.write(String.valueOf(position));
                    main.mConnectedThread.write("-");
                    Toast.makeText(getActivity(), selected + " selected", Toast.LENGTH_SHORT).show();
                    Bundle extras = new Bundle();
                    MainActivity main = (MainActivity) getActivity();
                    switch (ope) {
                        case 0: // paste operation
                            PasteFragment pasteFragment = new PasteFragment();
                            // fragment.setArguments(extras);
                            getFragmentManager().beginTransaction().
                                    replace(R.id.fragment_container, pasteFragment).commit();
                            break;
                        case 1: // move operation
                            MoveFragment moveFragment = new MoveFragment();
                            // fragment.setArguments(extras);
                            getFragmentManager().beginTransaction().
                                    replace(R.id.fragment_container, moveFragment).commit();
                            break;
                        case 2: // delete operation
                            extras.putString("delete", selected);
                            main.saveData("delete", extras);
                            DeleteFragment deleteFragment = new DeleteFragment();
                            deleteFragment.setArguments(extras);
                            getFragmentManager().beginTransaction().
                                    replace(R.id.fragment_container, deleteFragment).commit();
                    }
                }
            }));
        }
        return rootView;
    }

}
