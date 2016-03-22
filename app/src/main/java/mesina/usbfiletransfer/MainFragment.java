package mesina.usbfiletransfer;


import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


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
        final MainActivity main = (MainActivity) getActivity();
        main.setActionBarTitle("USB File Transfer Hub");
        final Bundle title = new Bundle();
        final FragmentManager fm = getFragmentManager();

        // Fragment List
        final PasteFragment pasteFragment = new PasteFragment();
        final DeleteFragment deleteFragment = new DeleteFragment();
        final RenameFragment renameFragment = new RenameFragment();

        // Paste Button
        RelativeLayout pasteLayout = (RelativeLayout) rootView.findViewById(R.id.pasteLayout);
    //    FloatingActionButton pasteButton = (FloatingActionButton) rootView.findViewById(R.id.pasteButton);
      //  assert pasteButton != null;
        pasteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity) getActivity();
                main.mConnectedThread.write("D");
                title.putString("title", "Paste files");
                pasteFragment.setArguments(title);
                fm.beginTransaction().replace(R.id.fragment_container, pasteFragment).commit();
            }
        });

        // Move Button
        RelativeLayout moveLayout = (RelativeLayout) rootView.findViewById(R.id.moveLayout);
   //     FloatingActionButton moveButton = (FloatingActionButton) rootView.findViewById(R.id.moveButton);
     //   assert moveButton != null;
        moveLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main.mConnectedThread.write("D");
                title.putString("title", "Move files");
                pasteFragment.setArguments(title);
                fm.beginTransaction().replace(R.id.fragment_container, pasteFragment).commit();
            }
        });

        // Delete Button
        RelativeLayout deleteLayout = (RelativeLayout) rootView.findViewById(R.id.deleteLayout);
     //   FloatingActionButton deleteButton = (FloatingActionButton) rootView.findViewById(R.id.deleteButton);
     //   assert deleteButton != null;
        deleteLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main.mConnectedThread.write("D");
                title.putString("title", "Delete files");
                deleteFragment.setArguments(title);
                fm.beginTransaction().replace(R.id.fragment_container, deleteFragment).commit();
            }
        });

        // Rename Button
        RelativeLayout renameLayout = (RelativeLayout) rootView.findViewById(R.id.renameLayout);
     //   FloatingActionButton renameButton = (FloatingActionButton) rootView.findViewById(R.id.renameButton);
    //    assert renameButton != null;
        renameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main.mConnectedThread.write("D");
                main.mConnectedThread.write("r");
                title.putString("title", "Rename files");
                renameFragment.setArguments(title);
                fm.beginTransaction().replace(R.id.fragment_container, renameFragment).commit();
            }
        });


        return rootView;

    }

}
