package mesina.usbfiletransfer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class DirectoryFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> dirFiles = new ArrayList<String>();
    DirectoryAdapter mAdapter;

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
        Bundle extras = this.getArguments();
        int s = extras.getInt("src");
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
            // Directory adapter
            mAdapter = new DirectoryAdapter(dirFiles);
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
}
