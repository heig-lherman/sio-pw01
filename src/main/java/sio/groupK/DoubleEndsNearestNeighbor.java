package sio.groupK;

import java.util.Arrays;
import java.util.Objects;
import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;
import sio.tsp.TspTour;

/**
 * Double ends nearest neighbor constructive heuristic for the TSP
 *
 * @author Loïc Herman
 */
public final class DoubleEndsNearestNeighbor implements TspConstructiveHeuristic {

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
        int s = startCityIndex;
        int t = startCityIndex;
        next[s] = s;

        // Loop through while there are still cities to visit
        while (true) {
            int nearest = -1;
            int end = -1;
            long nearestDistance = Long.MAX_VALUE;

            // iterate over every city to find the nearest one
            for (int i = 0; i < data.getNumberOfCities(); i++) {
                if (next[i] >= 0) {
                    // if the city has already been visited, skip it
                    continue;
                }

                // else, compute the distance between the current city
                // and both ends of the segment, and check if the distance
                // is the smallest one found so far (for both ends)
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

            // if the nearest city is still -1, it means that
            // there are no more cities to visit
            if (nearest < 0) {
                break;
            }

            // else, insert it into the correct end of the
            // chain and update the distance
            if (end == s) {
                // inserting at the start requires us to update
                // the next index of our new start to the previous
                // start and the next index of the current end to
                // our new start. Then, we update the start index.
                next[nearest] = s;
                next[t] = nearest;
                s = nearest;
            } else {
                // inserting at the end requires us to update
                // the next index of our previous end to the new
                // end and the next index of our new end to the
                // start. Then, we update the end index.
                next[t] = nearest;
                next[nearest] = s;
                t = nearest;
            }

            // update the distance with the one
            // from the nearest end we selected
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
