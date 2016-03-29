package mesina.usbfiletransfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class PasteFragment extends Fragment {

    private static final int DEST1_CHOSEN = 0;
    private static final int DEST2_CHOSEN = 1;
    private static final int DEST3_CHOSEN = 2;
    private static final int RESET_DATA = 3;
    private static final String TAG = "thebluetooth";
    public static int PASTE_FRAGMENT = 0;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<selection> arrayList;
    ArrayList<String> dirfiles;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    int src;
    int count = 0;
    MainActivity main = (MainActivity) getActivity();
    String selectedFile, dest1, dest2, dest3, filename, d;
    private Handler h;

    public PasteFragment() {
        // Required empty public constructor
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
        //  if (destinations != null) {
        //    dest1 = main.dest1;
        // dest2 = main.dest2;
        // dest3 = main.dest3;
        // }
        selectedFile = main.selectedFile;
        if (selectedFile != " ") {
            filename = selectedFile.substring(selectedFile.lastIndexOf(":")+1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final MainActivity main = (MainActivity) getActivity();
        Bundle fromFragments = this.getArguments();
        // Number of destinations
        if (fromFragments != null) {
            count = fromFragments.getInt("count");
        }

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_paste, container, false);


        // Setup Selected Text Label
        final TextView selectLabel = (TextView) rootView.findViewById(R.id.selectedFile);
        //  Bundle select = this.getArguments();
        // Setup Destination Text Label
        final TextView destLabel1 = (TextView) rootView.findViewById(R.id.destLabel);
        final TextView destLabel2 = (TextView) rootView.findViewById(R.id.desttextView2);
        final TextView destLabel3 = (TextView) rootView.findViewById(R.id.desttextView3);

        if (main.selectedFile != " ") {
            selectLabel.setText(selectedFile);
        }

        destLabel1.setText("Choose destinations");
        final String[] finalDest = {new String()};

        // Setup Spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int source = parent.getSelectedItemPosition();
                switch (source) {
                    case 1:
                        main.src = 1;
                        dirfiles = main.getDirectory(main.src);
                        break;
                    case 2:
                        main.src = 2;
                        dirfiles = main.getDirectory(main.src);
                        break;
                    case 3:
                        main.src = 3;
                        dirfiles = main.getDirectory(main.src);
                        break;
                    case 4:
                        main.src = 4;
                        dirfiles = main.getDirectory(main.src);
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
                    if (main.src != 0) {
                        switch (main.src) {
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
                        args.putInt("src", src);
                        args.putInt("ope", PASTE_FRAGMENT);
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
        destButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                Log.d(TAG, "newcount: " + count);
                if (selectedFile == " ") {
                    Toast.makeText(getActivity(), "Choose a file", Toast.LENGTH_SHORT).show();
                } else {
                    if (main.dest3 == " ") {
                        Dialog destination = destinationDialog();
                        destination.show();
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

        // Setup Clear Button
        final Button clearButton = (Button) rootView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.mConnectedThread.write("k");
                int size = main.arrayList.size();
                main.arrayList.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
                main.arrayList.add(new selection("Add files to list", "Destinations"));
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
        addButton.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                if (finalSelectedFile == null || main.dest1 == " ") {
                    Toast.makeText(getActivity(), "List is empty! Add files", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), finalSelectedFile + " added!", Toast.LENGTH_SHORT).show();
                    main.mConnectedThread.write("A");
                    selection addtolist = new selection(finalSelectedFile, finalDest[0]);
                    arrayList.add(addtolist);
                    mAdapter.notifyItemInserted(arrayList.size());
                    selectLabel.setText("Select a file");
                    destLabel1.setText("Choose destinations");
                    destLabel2.setText(" ");
                    destLabel3.setText(" ");
                    h.obtainMessage(RESET_DATA).sendToTarget();
                }
            }
        });

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {

                switch (msg.what) {

                    case DEST1_CHOSEN:
                        destLabel1.setText(main.dest1);
                        finalDest[0] = main.dest1;
                        break;
                    case DEST2_CHOSEN:
                        destLabel2.setText(main.dest2);
                        finalDest[0] = main.dest1 + main.dest2;
                        break;
                    case DEST3_CHOSEN:
                        destLabel3.setText(main.dest3);
                        finalDest[0] = main.dest1 + main.dest2+ main.dest3;
                        break;
                    case RESET_DATA:
                        selectedFile = " ";
                        main.src = 0;
                        main.dest1 = " ";
                        main.dest2 = " ";
                        main.dest3 = " ";
                        count = 0;
                }

            }
        };

        // Setup Send Button
        Button sendButton = (Button) rootView.findViewById(R.id.sendButton);
        sendButton.setText("PASTE");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = arrayList.size();
                if (size <= 1) {
                    Toast.makeText(getActivity(), "Add files", Toast.LENGTH_SHORT).show();
                } else {
                    Dialog confirm = confirm();
                    confirm.show();
                }
            }
        });

        return rootView;
    }

    // Destination dialog

    public Dialog destinationDialog() {

        MainActivity main = (MainActivity) getActivity();
        Log.d(TAG, "source: "+ main.src);
        Log.d(TAG, "count: "+ count);
        final AlertDialog.Builder destination = new AlertDialog.Builder(getActivity());
        CharSequence[] usb1 = new CharSequence[]{"USB2", "USB3", "USB4"};
        CharSequence[] usb2 = new CharSequence[]{"USB1", "USB3", "USB4"};
        CharSequence[] usb3 = new CharSequence[]{"USB1", "USB2", "USB4"};
        CharSequence[] usb4 = new CharSequence[]{"USB1", "USB2", "USB3"};
        CharSequence[] destlist = new CharSequence[]{""};
        destination.setTitle("Choose a destination");
        switch (main.src) {
            case 1:
                destlist = usb1;
                break;
            case 2:
                destlist = usb2;
                break;
            case 3:
                destlist = usb3;
                break;
            case 4:
                destlist = usb4;
                break;
        }
        final CharSequence[] finalDestlist = destlist;
        destination.setSingleChoiceItems(finalDestlist, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                d = (String) finalDestlist[which];
                Log.d(TAG, "dest " + d + " " + String.valueOf(which));
            }
        });
        destination.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchDir(d);
                dialog.dismiss();
            }
        });
        destination.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return destination.create();
    }

    private void searchDir(String d) {

        MainActivity main = (MainActivity) getActivity();
        ArrayList<String> dir = main.checkSame(d);
        if (dir!=null) {
            for (int i=0; i<dir.size();i++) {

                if(dir.get(i).equals(filename) || dir.get(i).equals(filename.toLowerCase())) {
                    Dialog overwrite = overWrite();
                    overwrite.show();
                    return;
                }

            }
            addData();
        }

    }

    public Dialog overWrite() {

        AlertDialog.Builder overwrite = new AlertDialog.Builder(getActivity());
        overwrite.setTitle("Overwrite file");
        overwrite.setMessage("The filename already exists in this drive. Overwrite file?");
        overwrite.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addData();
                dialog.dismiss();
            }
        });
        overwrite.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return overwrite.create();
    }

    private void addData() {
        Log.d(TAG, "addData count: " + count);
        MainActivity main = (MainActivity) getActivity();
        switch (count) {
            case 0:
                main.dest1 = d;
                h.obtainMessage(DEST1_CHOSEN).sendToTarget();
                break;
            case 1:
                main.dest2 = d;
                h.obtainMessage(DEST2_CHOSEN).sendToTarget();
                break;
            case 2:
                main.dest3 = d;
                h.obtainMessage(DEST3_CHOSEN).sendToTarget();
                break;
            default:
                Toast.makeText(getActivity(), "Maximum of three destinations", Toast.LENGTH_SHORT).show();
                return;
        }
        switch (d) {
            case "USB1":
                main.mConnectedThread.write("m");
                break;
            case "USB2":
                main.mConnectedThread.write("n");
                break;
            case "USB3":
                main.mConnectedThread.write("o");
                break;
            case "USB4":
                main.mConnectedThread.write("p");
                break;
        }
        count++;
    }

    public Dialog confirm() {

        final MainActivity main = (MainActivity) getActivity();
        final AlertDialog.Builder proceed = new AlertDialog.Builder(getActivity());
        proceed.setTitle("Proceed?");
        proceed.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                main.mConnectedThread.write("i"); // start paste
                Toast.makeText(getActivity(), "Transfer Started", Toast.LENGTH_SHORT).show();
                //MainFragment home = new MainFragment();
                main.selectedFile = " ";
                main.showLoading();
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                main.arrayList.clear();
                main.arrayList.add(new selection("Add files to list", "Destinations"));
            }
        });
        proceed.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return proceed.create();
    }

}
