package mesina.usbfiletransfer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String selectedFile, dest1, dest2, dest3;
    public static int CHOOSE_FRAGMENT = 1;
    public static int DESTINATION_FRAGMENT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainFragment fragment = new MainFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();


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

    public void saveData(int id, Bundle data) {
        // based on the id you'll know which fragment is trying to save data(see below)
        // the Bundle will hold the data

        if(id == CHOOSE_FRAGMENT) { // from choosefragment
            selectedFile = data.getString("selected");
        } else if (id == DESTINATION_FRAGMENT) { // from destination fragment
            dest1 = data.getString("destination");
        }
    }

    public Bundle getSavedData() {
        // here you'll save the data previously retrieved from the fragments and
        // return it in a Bundle
        Bundle data = new Bundle();
        data.putString("selected", selectedFile);
        data.putString("destination", dest1);
        return data;
    }

}