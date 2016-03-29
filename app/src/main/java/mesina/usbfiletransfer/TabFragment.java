package mesina.usbfiletransfer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    public static int TAB_FRAGMENT = 4;
    public static final String TAG = "thebluetooth";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity main = (MainActivity) getActivity();
        main.setActionBarTitle("View Directories");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        final MainActivity main = (MainActivity) getActivity();
        main.mConnectedThread.write("E");

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("USB 1"));
        tabLayout.addTab(tabLayout.newTab().setText("USB 2"));
        tabLayout.addTab(tabLayout.newTab().setText("USB 3"));
        tabLayout.addTab(tabLayout.newTab().setText("USB 4"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        final DirectoryPagerAdapter adapter = new DirectoryPagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        main.src  = 1;
                        main.mConnectedThread.write("E");
                        break;
                    case 1:
                        main.src = 2;
                        main.mConnectedThread.write("F");
                        break;
                    case 2:
                        main.src = 3;
                        main.mConnectedThread.write("G");
                        break;
                    case 3:
                        main.src = 4;
                        main.mConnectedThread.write("H");
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }



}
