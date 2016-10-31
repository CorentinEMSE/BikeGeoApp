package corentinulysse.bikegeoapp;


/**
 * Created by Corentin on 17/10/2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * PagerAdapter correspondant au tabLayout de NavigationActivity
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    final int PAGE_COUNT=3;
    private String tabTitles[] = new String[] { "Liste", "Carte", "Info" };
    private Context context;
    private ListFragment tab1;
    private Map2Fragment tab2;

    /**
     * Constructeur
     * @param fm FragmentManager
     * @param NumOfTabs nombre d'onglets
     */
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Constructeur avec le contexte
     * @param fm FragmentManager
     * @param NumOfTabs nombre d'onglets
     * @param context contexte
     */
    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context=context;
    }

    /**
     * Création de chacun des fragments en fonction de leur position
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: //Premier fragment : listFragment
                 tab1 = ListFragment.newInstance(position+1);
                return tab1;
            case 1: //Deuxieme fragment : map2Fragment
                 tab2 = new Map2Fragment();
                //MapFragment mMapFragment= MapFragment.newInstance();
                return tab2;
            case 2: //Troisieme fragment : infoFragment
                InfoFragment tab3 = new InfoFragment();
                return tab3;
            default:
                return null;
        }
    }


    /**
     * Méthode permettant d'obtenir le nombre d'onglets
     * @return ce nombre
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    /**
     * Méthode permettant d'obtenir le titre de l'onglet correspondant à
     * @param position la position
     * @return retourne le nom de l'onglet
     */
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    /**
     * Méthode permettant de dire aux fragments que la requeteHTTP a été reçue par NavigationActivity
     */
    public void pagerAdapterHttpRequestReceived(){
        tab1.listfragmentOnHttpRequestReceived();
        tab2.map2fragmentOnHttpRequestReceived();
    }
}