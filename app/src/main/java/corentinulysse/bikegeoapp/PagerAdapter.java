package corentinulysse.bikegeoapp;


/**
 * Created by Corentin on 17/10/2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    final int PAGE_COUNT=3;
    private String tabTitles[] = new String[] { "Liste", "Carte", "Info" };
    private Context context;


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ListFragment tab1 = ListFragment.newInstance(position+1);
                return tab1;
            case 1:
                MapFragment tab2 = new MapFragment();
                return tab2;
            case 2:
                InfoFragment tab3 = new InfoFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}