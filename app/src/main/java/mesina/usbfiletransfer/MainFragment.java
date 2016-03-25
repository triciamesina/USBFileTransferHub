package mesina.usbfiletransfer;


import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
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

    private static final int PASTE_FRAGMENT = 0;
    private static final int MOVE_FRAGMENT = 1;
    private static final int DELETE_FRAGMENT = 2;
    private static final int RENAME_FRAGMENT = 3;
    private static final int TAB_FRAGMENT = 4;

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
        final MoveFragment moveFragment = new MoveFragment();
        final TabFragment tabFragment = new TabFragment();

        // Paste Button
        RelativeLayout pasteLayout = (RelativeLayout) rootView.findViewById(R.id.pasteLayout);
    //    FloatingActionButton pasteButton = (FloatingActionButton) rootView.findViewById(R.id.pasteButton);
      //  assert pasteButton != null;
        pasteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.mConnectedThread.write("D");
                main.open = PASTE_FRAGMENT;
                main.loadDir();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        main.noDetect();
                    }
                }, 5000);
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
                main.open = MOVE_FRAGMENT;
                main.loadDir();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        main.noDetect();
                    }
                }, 5000);
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
                main.open = DELETE_FRAGMENT;
                main.loadDir();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        main.noDetect();
                    }
                }, 5000);
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
                                                main.open = RENAME_FRAGMENT;
                                                main.loadDir();
                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // Do something after 5s = 5000ms
                                                        main.noDetect();
                                                    }
                                                }, 5000);
                                            }
                                        });

                // View Button
                RelativeLayout viewLayout = (RelativeLayout) rootView.findViewById(R.id.viewLayout);
        //   FloatingActionButton renameButton = (FloatingActionButton) rootView.findViewById(R.id.renameButton);
        //    assert renameButton != null;
        viewLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                main.mConnectedThread.write("D");
                main.open = TAB_FRAGMENT;
                main.loadDir();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        main.noDetect();
                    }
                }, 5000);
            }
        });

        return rootView;

    }

    View.OnClickListener homelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final MainActivity main = (MainActivity) getActivity();
            main.mConnectedThread.write("D");
            main.loadDir();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    main.noDetect();
                }
            }, 2000);
        }
    };

}
