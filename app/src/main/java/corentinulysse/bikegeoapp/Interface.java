package corentinulysse.bikegeoapp;

import java.util.List;

/**
 * Created by Ulysse on 29/10/2016.
 */

public interface Interface {

    List<StationsVelib> getStationList();

    void sendHttpRequestFromFragment();

//    void refreshFavorites();
}
