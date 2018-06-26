package emcorp.studio.spkmotor.Adapter;

/**
 * Created by ASUS on 21/09/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import emcorp.studio.spkmotor.Fragment.KerusakanFragment;
import emcorp.studio.spkmotor.Fragment.PenyebabFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                KerusakanFragment tab1 = new KerusakanFragment();
                return tab1;
            case 1:
                PenyebabFragment tab2 = new PenyebabFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
