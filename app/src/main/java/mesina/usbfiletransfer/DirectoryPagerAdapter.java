package mesina.usbfiletransfer;

/**
 * Created by Tricia on 3/23/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DirectoryPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DirectoryPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        DirectoryFragment tab = new DirectoryFragment();
        Bundle source = new Bundle();
        switch (position) {
            case 0:
                source.putInt("src", 1);
                DirectoryFragment tab1 = new DirectoryFragment();
                tab1.setArguments(source);
                return tab1;
            case 1:
                source.putInt("src", 2);
                DirectoryFragment tab2 = new DirectoryFragment();
                tab2.setArguments(source);
                return tab2;
            case 2:
                source.putInt("src", 3);
                DirectoryFragment tab3 = new DirectoryFragment();
                tab3.setArguments(source);
                return tab3;
            case 3:
                source.putInt("src", 4);
                DirectoryFragment tab4 = new DirectoryFragment();
                tab4.setArguments(source);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}