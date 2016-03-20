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
    final String[] filesList = {"0:testpdf.pdf", "0:testppt.ppt", "0:testdoc.doc", "0:testtxt.txt", "0:testpng.png"};
    final String[] filesList2 = {"1:testpdf.pdf", "1:testppt.ppt", "1:testdoc.doc", "1:testtxt.txt", "1:testpng.png"};
    final String[] usb2 = {"2:testpdf.pdf", "2:testppt.ppt", "2:testdoc.doc", "2:testtxt.txt", "2:testpng.png"};


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

    public String[] getDirectory(int source) {
        switch (source){
            case 1:
                return filesList;
            case 2:
                return filesList2;
            case 3:
                return usb2;
            default:
                return usb2;
        }
    }

    public ArrayList<String> checkSame(String dest) {
        ArrayList<String> filenames = new ArrayList<String>();
        String[] arr;
        switch (dest){
            case "USB1":
                arr = filesList;
                break;
            case "USB2":
                arr = filesList2;
                break;
            case "USB3":
                arr = usb2;
                break;
            default:
                arr = usb2;
                break;
        }
        for (String s: arr) {
            String f = s.substring(s.lastIndexOf(":"));
            filenames.add(f);
        }
        return filenames;
    }

}