package sio.groupK;

import java.util.Arrays;
import java.util.Objects;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspTour;

/*
1: procedure DENN(S, d, s)
    2: Déterminer la ville t la plus proche de s et créer une tournée avec s et t
    3: tant que toutes les villes n’ont pas été ajoutées à la tournée faire
        4: Déterminer la ville v, hors tournée, la plus proche de s ou de t
        5: Ajouter v à la tournée et mettre à jour s ou t selon le cas
    6: fin tant que
    7: Retourner la tournée construite
8: fin procedure
 */

public final class DoubleEndsNearestNeighbor implements TspConstructiveHeuristic {

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
        var s = startCityIndex;
        var t = startCityIndex;
        next[s] = s;

        while (true) {
            int nearest = -1;
            int end = -1;
            long nearestDistance = Long.MAX_VALUE;

            for (int i = 0; i < data.getNumberOfCities(); i++) {
                if (next[i] >= 0) {
                    continue;
                }

                var d1 = data.getDistance(s, i);
                var d2 = data.getDistance(t, i);

                if (d1 < nearestDistance) {
                    nearest = i;
                    end = s;
                    nearestDistance = d1;
                }

                if (d2 < nearestDistance) {
                    nearest = i;
                    end = t;
                    nearestDistance = d2;
                }
            }

            if (nearest < 0) {
                break;
            }

            if (end == s) {
                next[nearest] = s;
                s = nearest;
            } else {
                next[t] = nearest;
                next[nearest] = s;
                t = nearest;
            }

            distance += nearestDistance;
        }

        return new TspTour(data, computeTour(next, s), distance);
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
