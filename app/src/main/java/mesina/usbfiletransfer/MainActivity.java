package mesina.usbfiletransfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //bluetooth

    private static final String TAG = "thebluetooth";
    private static final int PASTE_FRAGMENT = 0;
    private static final int MOVE_FRAGMENT = 1;
    private static final int DELETE_FRAGMENT = 2;
    private static final int RENAME_FRAGMENT = 3;
    private static final int TAB_FRAGMENT = 4;
    Handler h;
    int open;

    final int RECIEVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    public ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "00:14:03:04:08:59";

    //HC-05 20:15:04:16:06:79
    //BM9600 00:14:03:04:08:59


    String selectedFile = " ";
    String dest1 = " ";
    String dest2 = " ";
    String dest3 = " ";
    int src = 0;
    ArrayList<selection> arrayList = new ArrayList<>();
    ArrayList<String> deleteList = new ArrayList<>();
    String usb1files = "";
    String usb2files = "";
    String usb3files = "";
    String usb4files = "";
    //String usb1files = "0:PDF120KB.PDF 0:PNG120KB.PNG 0:DOC120KB.DOC";
    //String usb2files = "1:PNG50KB3.PNG 1:D50KB4.DOC 1:PDF50KB4.PDF 1:PNG50KB4.PNG 1:PNG50KB1.PNG";
    //String usb3files = "2:D50KB3.DOC 2:PDF50KB3.PDF 2:PNG50KB3.PNG 2:PNG50KB1.PNG";
    //String usb4files = "3:PDF50KB3.PDF 3:PDF120KB.PDF 3:D50KB2.DOC 3:PDF50KB2.PDF 3:PNG50KB2.PNG 3:PNG50KB1.PNG";
    ArrayList<String> usb1List, usb2List, usb3List, usb4List;
    String oldName = " ";
    MainFragment home = new MainFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // if receive massage
                      /*  byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("/");                            // determine the end-of-line
                        if (endOfLineIndex > 0) {// if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex); */// extract string
                        if (msg.arg1 > 0) {
                            String sbprint = (String) msg.obj;
                        Log.d(TAG, "Received data: " + sbprint + "...");
                        if (sbprint.charAt(0) == '0') {
                            String directory = sbprint;
                            splitDirs(directory);
                            updateDirs();
                            dirDone();
                        } else if (sbprint.charAt(0) == '*') {
                            String time = sbprint.substring(1);
                            doneLoading();
                            showDone(time);
                        }

                            break;
                }
            }
                    //   break;
              //  }
            }
        };


        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, home).commit();
        selection initial = new selection("Add files to list", "Destinations");
        arrayList.add(initial);
        if (usb1files != null) {
            usb1List = new ArrayList<String>(Arrays.asList(usb1files.split(" ")));
        }
        if (usb2files != null) {
            usb2List = new ArrayList<String>(Arrays.asList(usb2files.split(" ")));
        }
        if (usb3files != null) {
            usb3List = new ArrayList<String>(Arrays.asList(usb3files.split(" ")));
        }
        if (usb4files != null) {
            usb4List = new ArrayList<String>(Arrays.asList(usb4files.split(" ")));
        }
    }

    private void updateDirs() {

        if (usb1files != null) {
            usb1List = new ArrayList<String>(Arrays.asList(usb1files.split(" ")));
        }
        if (usb2files != null) {
            usb2List = new ArrayList<String>(Arrays.asList(usb2files.split(" ")));
        }
        if (usb3files != null) {
            usb3List = new ArrayList<String>(Arrays.asList(usb3files.split(" ")));
        }
        if (usb4files != null) {
            usb4List = new ArrayList<String>(Arrays.asList(usb4files.split(" ")));
        }
    }

    private void splitDirs(String directory) {
        ArrayList<String> dirarray = new ArrayList<> (Arrays.asList(directory.split(",")));
        if (dirarray.size() >= 0) {
            usb1files = dirarray.get(0);
            Log.d(TAG, "usb1files" + usb1files);
        }
        if (dirarray.size() > 1) {
            usb2files = dirarray.get(1);
            Log.d(TAG, "usb2files" + usb2files);
        }
        if (dirarray.size() > 2) {
            usb3files = dirarray.get(2);
        }
        if (dirarray.size() > 3) {
            usb4files = dirarray.get(3);
        }

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                 return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection", e);
                return (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getBaseContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
    //    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
        if(!btSocket.isConnected()) {
            try {
                btSocket.connect();
                Log.d(TAG, "....Connection ok...");
                Toast.makeText(getBaseContext(), "Connected to Hub", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                try {
                    btSocket.close();
                    Toast.makeText(getBaseContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                } catch (IOException e2) {
                    errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                }
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
        mConnectedThread.write("l");
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getBaseContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "...In onPause()...");

        try {
            btSocket.close();

        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");

        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");

        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    public class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                   // String rcvd = new String(buffer, 0, bytes);
                  //  Log.d(TAG, "Receive: " + buffer);
                    String strIncom = new String(buffer, 0, bytes); // create string from bytes array
                    buffer[bytes] = '\0';
                    sb.append(strIncom);                                                // append string
                    int endOfLineIndex = sb.indexOf("/");                            // determine the end-of-line
                    if (endOfLineIndex > 0) {// if end-of-line,
                        String newstring = "";
                        newstring = sb.substring(0, endOfLineIndex);// extract string
                        sb.delete(0, sb.length());
                        Log.d(TAG, "Receive: " + newstring);
                       // h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget(); //Send to message queue Handler
                        h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, newstring).sendToTarget();
                        newstring = "";
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        mConnectedThread.write("k");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        } else if (id == R.id.action_back) {
            mConnectedThread.write("k");
            resetData();
            arrayList.clear();
            arrayList.add(new selection("Add files to list", "Destinations"));
            MainFragment home = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
            return true;
        } else if (id == R.id.action_rate) {
            Intent rate = new Intent(android.content.Intent.ACTION_VIEW);
            rate.setData(Uri.parse("https://docs.google.com/forms/d/11ggmce9-PLmPTjgdTKCBvIMXdfiQ9eU_Ia-Q3YyXo0g/viewform"));
            startActivity(rate);
            return true;
        } else if (id == R.id.action_connect) {
            if (!btSocket.isConnected()) {
                Log.d(TAG, "Connect button pressed");
                Toast.makeText(MainActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
                // Set up a pointer to the remote node using it's address.
                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                // Two things are needed to make a connection:
                //   A MAC address, which we got above.
                //   A Service ID or UUID.  In this case we are using the
                //     UUID for SPP.
                try {
                    btSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                // Discovery is resource intensive.  Make sure it isn't going on
                // when you attempt to connect and pass your message.
                btAdapter.cancelDiscovery();

                // Establish the connection.  This will block until it connects.
                Log.d(TAG, "...Connecting...");
                //    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
                if (!btSocket.isConnected()) {
                    try {
                        btSocket.connect();
                        Log.d(TAG, "....Connection ok...");
                        Toast.makeText(getBaseContext(), "Connected to Hub", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        try {
                            btSocket.close();
                            Toast.makeText(getBaseContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                        } catch (IOException e2) {
                            errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                        }
                    }
                }

                // Create a data stream so we can talk to server.
                Log.d(TAG, "...Create Socket...");

                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();
                mConnectedThread.write("l");
            } else {
                Toast.makeText(MainActivity.this, "Disconnecting", Toast.LENGTH_SHORT).show();
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    // Select new item reinitialize values
    public void resetData() {
        selectedFile = " ";
        dest1 = " ";
        dest2 = " ";
        dest3 = " ";
    }

    // Add item to selection list
    public void addToList(selection added) {
        arrayList.add(added);
    }

    public void saveData(String id, Bundle data) {
        // based on the id you'll know which fragment is trying to save data(see below)
        // the Bundle will hold the data

        if(id == "selected") { // selected file
            selectedFile = data.getString("selected");
        } else if (id == "source") {
            src = data.getInt("src");
        } else if (id == "dest1") { // from destination fragment
            dest1 = data.getString("dest1");
        } else if (id == "dest2") {
            dest2 = data.getString("dest2");
        } else if (id == "dest3") {
            dest3 = data.getString("dest3");
        } else if (id == "list") {
            arrayList = data.getParcelableArrayList("list");
        } else if (id == "reset") {

            selectedFile = " ";
            dest1 = " ";
            dest2 = " ";
            dest3 = " ";

        } else if (id == "delete") {
            deleteList.add(data.getString("delete"));
        } else if (id == "oldame") {
            oldName = data.getString("oldname");
        }
    }

    public Bundle getSavedData() {
        // here you'll save the data previously retrieved from the fragments and
        // return it in a Bundle
        Bundle data = new Bundle();
        data.putParcelableArrayList("list", arrayList);
        return data;
    }

    public ArrayList<String> getDeleteList () {
        return deleteList;
    }

    public String getSelectedFile() {
        String selected = selectedFile;
        return selected;
    }

    public int getSource() {
        return src;
    }

    public Bundle getDestinations() {

        Bundle data = new Bundle();
        data.putString("dest1", dest1);
        data.putString("dest2", dest2);
        data.putString("dest3", dest3);
        return data;
    }

    public ArrayList<String> getDirectory(int source) {
        switch (source){
            case 1:
                if (usb1files != null) {
                    usb1List = new ArrayList<String>(Arrays.asList(usb1files.split(" ")));
                }
                Log.d(TAG, usb1files);
                return usb1List;
            case 2:
                if (usb2files != null) {
                    usb2List = new ArrayList<String>(Arrays.asList(usb2files.split(" ")));
                }
                Log.d(TAG, usb2files);
                return usb2List;
            case 3:
                if (usb3files != null) {
                    usb3List = new ArrayList<String>(Arrays.asList(usb3files.split(" ")));
                }
                return usb3List;
            default:
                if (usb4files != null) {
                    usb4List = new ArrayList<String>(Arrays.asList(usb4files.split(" ")));
                }
                return usb4List;
        }
    }

    public ArrayList<String> checkSame(String d) {
        ArrayList<String> filenames = new ArrayList<String>();
        ArrayList<String> arr = new ArrayList<>();
        switch (d){
            case "USB1":
                arr = usb1List;
                break;
            case "USB2":
                arr = usb2List;
                Log.d(TAG, "d=USB2" + usb2files);
                break;
            case "USB3":
                arr = usb3List;
                break;
            case "USB4":
                arr = usb4List;
                break;
        }
        Log.d(TAG + "/chek/dest", d);
        for (int i = 0; i < arr.size(); i++) {
            String s = arr.get(i);
            String f = s.substring(s.lastIndexOf(":")+1);
            filenames.add(f);
            Log.d(TAG + "/chek/s", s);
            Log.d(TAG+"/chek/fn", f);
        }
        return filenames;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            btSocket.close();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public void showLoading() {

        LoadingDialog loading = (LoadingDialog) getSupportFragmentManager().findFragmentByTag(LoadingDialog.FRAGMENT_TAG);
        if (loading == null) {
            loading = new LoadingDialog();
            loading.setCancelable(false);
            Bundle type = new Bundle();
            type.putInt("type", 0);
            loading.setArguments(type);
            getSupportFragmentManager().beginTransaction().add(loading, LoadingDialog.FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }

    public void doneLoading() {

        LoadingDialog loading = (LoadingDialog) getSupportFragmentManager().findFragmentByTag(LoadingDialog.FRAGMENT_TAG);
        if (loading != null) {
            getSupportFragmentManager().beginTransaction().remove(loading).commitAllowingStateLoss();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
        }

    }

    public void loadDir() {

        LoadingDialog loading = (LoadingDialog) getSupportFragmentManager().findFragmentByTag(LoadingDialog.FRAGMENT_TAG);
        if (loading == null) {
            loading = new LoadingDialog();
            loading.setCancelable(false);
            getSupportFragmentManager().beginTransaction().add(loading, LoadingDialog.FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }


    public void dirDone() {

        LoadingDialog loading = (LoadingDialog) getSupportFragmentManager().findFragmentByTag(LoadingDialog.FRAGMENT_TAG);
        if (loading != null) {
            TabFragment view = new TabFragment();
            PasteFragment paste = new PasteFragment();
            MoveFragment move = new MoveFragment();
            DeleteFragment delete = new DeleteFragment();
            RenameFragment rename = new RenameFragment();
            getSupportFragmentManager().beginTransaction().remove(loading).commitAllowingStateLoss();
            switch (open) {
                case PASTE_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, paste).addToBackStack("main").commit();
                    break;
                case MOVE_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, move).addToBackStack("main").commit();
                    break;
                case DELETE_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, delete).addToBackStack("main").commit();
                    break;
                case RENAME_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rename).addToBackStack("main").commit();
                    break;
                case TAB_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, view).addToBackStack("main").commit();
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, view).addToBackStack("main").commit();
                    break;
            }
        }

    }

    public void noDetect() {

        LoadingDialog loading = (LoadingDialog) getSupportFragmentManager().findFragmentByTag(LoadingDialog.FRAGMENT_TAG);
        TabFragment view = new TabFragment();
        PasteFragment paste = new PasteFragment();
        MoveFragment move = new MoveFragment();
        DeleteFragment delete = new DeleteFragment();
        RenameFragment rename = new RenameFragment();
        if (loading != null) {
            getSupportFragmentManager().beginTransaction().remove(loading).commitAllowingStateLoss();
            switch (open) {
                case PASTE_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, paste).addToBackStack("main").commit();
                    break;
                case MOVE_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, move).addToBackStack("main").commit();
                    break;
                case DELETE_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, delete).addToBackStack("main").commit();
                    break;
                case RENAME_FRAGMENT:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rename).addToBackStack("main").commit();
                    break;
                case TAB_FRAGMENT:
                   /*MODIFY!!!!!!!!!!!!!!!!!*/ // MODIFY FOR TESTING!!!
                   getSupportFragmentManager().beginTransaction().remove(loading).commitAllowingStateLoss();
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, view).addToBackStack("main").commit();
                   // Toast.makeText(MainActivity.this, "No drives detected", Toast.LENGTH_SHORT).show();
                 //   Toast.makeText(MainActivity.this, "No drives detected", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "No drives detected", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void showDone(String time) {

        AlertDialog.Builder done = new AlertDialog.Builder(this);
        done.setTitle("Transfer done!");
        done.setMessage("Done in " + time + " ms.");
        done.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
            }
        });
        done.create();
        done.show();

    }

}