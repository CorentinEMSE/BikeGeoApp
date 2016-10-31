package corentinulysse.bikegeoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment donnant des informations sur l'application à l'utilisateur
 */
public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
        Initialisations
         */
        View mView = inflater.inflate(R.layout.fragment_info, container, false);

        /*
        On déclare les composants du layout que l'on voudra modifier
         */
        TextView tDesc = (TextView) mView.findViewById(R.id.f_i_tdesc);
        TextView desc = (TextView) mView.findViewById(R.id.f_i_desc);
        TextView tConc = (TextView) mView.findViewById(R.id.f_i_tconc);
        TextView conc = (TextView) mView.findViewById(R.id.f_i_conc);

        /*
        Modification de ces éléments
         */

        tDesc.setText("A quoi sert BikeGeoApp ?");
        desc.setText("Cette application liste toutes les stations Velib' pour afficher la disponibilités en temps réels des Velib' et des places.\nSi une station vous plait dans la liste ou sur la carte, cliquez dessus puis, dans la nouvelle fenêtre, cliquez sur le petit coeur dans la toolbar, elle sera sauvegardée dans vos favoris !\nVous pouvez accéder à vos favoris depuis n'importe quelle page via les options supplémentaires de la Toolbar.");
        tConc.setText("Les concepteurs");
        conc.setText("Elle a été réalisée par deux étudiants en informatique de l'Ecole des Mines de Saint-Etienne : Corentin MARCHAND et Ulysse PETIT.");

        return mView;
    }


}
