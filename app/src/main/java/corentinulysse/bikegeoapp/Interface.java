package corentinulysse.bikegeoapp;

import java.util.List;

/**
 * Created by Ulysse on 29/10/2016.
 */

/**
 * Interface permettant de transferer des informations entre l'ativité NavigationActivity et les fragments de celle-ci, notamment la liste de station issue de la requête http ou la possibilité de faire une requête depuis un fragment
 */
public interface Interface {

    List<StationsVelib> getStationList();

    void sendHttpRequestFromFragment();

}
