package mesina.usbfiletransfer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    String selectedFile = " ";
    String dest1 = " ";
    String dest2 = " ";
    String dest3 = " ";
    int src = 0;
    ArrayList<selection> arrayList = new ArrayList<>();
    ArrayList<String> deleteList = new ArrayList<>();
    public static int PASTE_FRAGMENT = 0;
    public static int CHOOSE_FRAGMENT = 1;
    public static int DESTINATION_FRAGMENT = 2;
    String usb1files = "0:testpdf.pdf 0:testppt.ppt 0:testdoc.doc 0:testtxt.txt 0:testpng.png";
    String usb2files = "1:testpdf.pdf 1:testppt.ppt 1:testdoc.doc 1:testtxt.txt 1:testpng.png";
    String usb3files = "2:testpdf.pdf 2:testppt.ppt 2:testdoc.doc 2:testtxt.txt 2:testpng.png";
    String usb4files = "2:testpdf.pdf 2:testppt.ppt 2:testdoc.doc 2:testtxt.txt 2:testpng.png";
    ArrayList<String> usb1List, usb2List, usb3List, usb4List;
    String oldName = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainFragment fragment = new MainFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        selection initial = new selection("Selected files", "Destinations");
        arrayList.add(initial);
        if (usb1files != null) {
            usb1List = new ArrayList<String>(Arrays.asList(usb1files.split(" ")));
        }
        if (usb2files != null) {
            usb2List = new ArrayList<String>(Arrays.asList(usb2files.split(" ")));
        }
        if (usb3files != null) {
            usb3List = new ArrayList<String>(Arrays.asList(usb4files.split(" ")));
        }
        if (usb4files != null) {
            usb4List = new ArrayList<String>(Arrays.asList(usb4files.split(" ")));
        }
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
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
            MainFragment home = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
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

        if(id.equals("selected")) { // selected file
            selectedFile = data.getString("selected");
        } else if (id.equals("source")) {
            src = data.getInt("src");
        } else if (id.equals("dest1")) { // from destination fragment
            dest1 = data.getString("dest1");
        } else if (id.equals("dest2")) {
            dest2 = data.getString("dest2");
        } else if (id.equals("dest3")) {
            dest3 = data.getString("dest3");
        } else if (id.equals("list")) {
            arrayList = data.getParcelableArrayList("list");
        } else if (id.equals("reset")) {

            selectedFile = " ";
            dest1 = " ";
            dest2 = " ";
            dest3 = " ";

        } else if (id.equals("delete")) {
            deleteList.add(data.getString("delete"));
        } else if (id.equals("oldame")) {
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
                return usb1List;
            case 2:
                return usb2List;
            case 3:
                return usb3List;
            default:
                return usb4List;
        }
    }

    public ArrayList<String> checkSame(String dest) {
        ArrayList<String> filenames = new ArrayList<String>();
        ArrayList<String> arr;
        switch (dest){
            case "USB1":
                arr = usb1List;
                break;
            case "USB2":
                arr = usb2List;
                break;
            case "USB3":
                arr = usb3List;
                break;
            default:
                arr = usb4List;
                break;
        }
        for (int i = 0; i < arr.size(); i++) {
            String s = arr.get(i);
            String f = s.substring(s.lastIndexOf(":")+1);
            filenames.add(f);
        }
        return filenames;
    }
}