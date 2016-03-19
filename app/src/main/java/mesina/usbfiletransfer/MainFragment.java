package mesina.usbfiletransfer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        // Paste Button
        Button pasteButton = (Button) rootView.findViewById(R.id.pasteButton);
        assert pasteButton != null;
        pasteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle title = new Bundle();
                title.putString("title", "Paste files");
                PasteFragment fragment = new PasteFragment();
                fragment.setArguments(title);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

        // Move Button
        Button moveButton = (Button) rootView.findViewById(R.id.moveButton);
        assert moveButton != null;
        moveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle title = new Bundle();
                title.putString("title", "Move files");
                PasteFragment fragment = new PasteFragment();
                fragment.setArguments(title);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

        // Delete Button
        Button deleteButton = (Button) rootView.findViewById(R.id.deleteButton);
        assert deleteButton != null;
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DeleteFragment fragment = new DeleteFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });


        return rootView;

    }

}
