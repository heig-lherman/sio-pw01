package sio.groupK;

import java.util.Arrays;
import java.util.Objects;
import sio.tsp.TspData;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspTour;

/**
 * Nearest neighbor constructive heuristic for the TSP
 *
 * @author Loïc Herman
 */
public final class NearestNeighbor implements TspConstructiveHeuristic {

    /**
     * {@inheritDoc}
     * @param data Data of problem instance
     * @param startCityIndex Index of starting city, if needed by the implementation
     *
     * @return Solution found by the heuristic
     */
    @Override
    public TspTour computeTour(TspData data, int startCityIndex) {
        // fail-fast if data integrity isn't respected
        Objects.requireNonNull(data, "data must not be null");
        if (startCityIndex < 0 || startCityIndex > data.getNumberOfCities()) {
            throw new IllegalArgumentException("start city index out of bounds");
        }

        // allocate the array of next indices and initialize it to -1
        int[] next = new int[data.getNumberOfCities()];
        Arrays.fill(next, -1);
        long distance = 0;

        // Find first nearest city from start city
        int t = startCityIndex;
        next[startCityIndex] = startCityIndex;

        // Loop through while there are still cities to visit
        while (true) {
            int nearest = -1;
            long nearestDistance = Long.MAX_VALUE;

            // iterate over every city to find the nearest one
            for (int i = 0; i < data.getNumberOfCities(); i++) {
                if (next[i] >= 0) {
                    // if the city has already been visited, skip it
                    continue;
                }

                // else, compute the distance between the current city and the next one
                // and check if the distance is the smallest one found so far
                var d = data.getDistance(t, i);

                if (d < nearestDistance) {
                    nearest = i;
                    nearestDistance = d;
                }
            }

            // if the nearest city is still -1, it means that there are no more cities to visit
            if (nearest < 0) {
                break;
            }

            // else, insert it into the chain and update the distance
            next[t] = nearest;
            next[nearest] = startCityIndex;
            t = nearest;

            distance += nearestDistance;
        }

        // when we are done, return the tour
        return new TspTour(data, computeTour(next, startCityIndex), distance);
    }

    /**
     * Simple method to compute the tour from the array of next indices
     * @param next the array of next indices
     * @param s the starting city
     * @return an array of the city indexes, in order of visit
     */
    private int[] computeTour(int[] next, int s) {
        var res = new int[next.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = s;
            s = next[s];
        }

        return res;
    }
}
