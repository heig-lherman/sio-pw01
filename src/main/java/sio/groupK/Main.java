package sio.groupK;

import javax.swing.JFrame;
import sio.groupK.gui.TourPanel;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;

// Longueurs optimales :
// att532 : 86729
// rat575 : 6773
// rl1889 : 316536
// u574   : 36905
// u1817  : 57201
// vm1748 : 336556

// TODO: tester chaque ville de départ sur chaque algorithme
//       pour toutes les données

public final class Main {
    public static void main(String[] args) throws Exception {
        // TODO
        //  - Renommage du package ;
        //  - Implémentation des classes NearestNeighbor et DoubleEndsNearestNeighbor ;
        //  - Affichage des statistiques dans la classe Main ;
        //  - Documentation abondante des classes comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        // Exemple de lecture d'un jeu de données :
        // TspData data = TspData.fromFile("data/att532.dat");

        TspData data = TspData.fromFile("data/vm1748.dat");
        TspConstructiveHeuristic denn = new DoubleEndsNearestNeighbor();
        TspConstructiveHeuristic nn = new NearestNeighbor();

        System.out.println(denn.computeTour(data, 2));
        System.out.println(nn.computeTour(data, 2));
    }
}
