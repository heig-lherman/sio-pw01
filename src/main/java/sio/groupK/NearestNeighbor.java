package sio.groupK;

import java.util.Arrays;
import java.util.Objects;
import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

/*
1: procedure NN(S, d, s)
    2: Déterminer la ville t la plus proche de s et créer une tournée avec s et t
    3: Poser s = t
    4: tant que toutes les villes n’ont pas été ajoutées à la tournée faire
        5: Déterminer la ville t, hors tournée, la plus proche de s et l’ajouter après s
        6: Poser s = t
    7: fin tant que
    8: Retourner la tournée construite
9: fin procedure
 */

public final class NearestNeighbor implements TspConstructiveHeuristic {

    @Override
    public TspTour computeTour(TspData data, int startCityIndex) {
        Objects.requireNonNull(data, "data must not be null");
        if (startCityIndex < 0 || startCityIndex > data.getNumberOfCities()) {
            throw new IllegalArgumentException("start city index out of bounds");
        }

        int[] next = new int[data.getNumberOfCities()];
        Arrays.fill(next, -1);
        long distance = 0;

        // Find first nearest city from start city
        var t = startCityIndex;
        next[startCityIndex] = startCityIndex;

        while (true) {
            int nearest = -1;
            long nearestDistance = Long.MAX_VALUE;

            for (int i = 0; i < data.getNumberOfCities(); i++) {
                if (next[i] >= 0) {
                    continue;
                }

                var d = data.getDistance(t, i);

                if (d < nearestDistance) {
                    nearest = i;
                    nearestDistance = d;
                }
            }

            if (nearest < 0) {
                break;
            }

            next[t] = nearest;
            next[nearest] = startCityIndex;
            t = nearest;

            distance += nearestDistance;
        }

        return new TspTour(data, computeTour(next, startCityIndex), distance);
    }

    private int[] computeTour(int[] next, int s) {
        var res = new int[next.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = s;
            s = next[s];
        }

        return res;
    }
}