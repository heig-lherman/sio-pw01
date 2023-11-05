package sio.groupK;

import sio.tsp.TspConstructiveHeuristic;
import sio.tsp.TspData;

public final class Main {

    /**
     * List of datasets and their optimal length.
     */
    private static final DatasetPair[] datasets = {
        new DatasetPair("data/att532.dat", 86729),
        new DatasetPair("data/rat575.dat", 6773),
        new DatasetPair("data/rl1889.dat", 316536),
        new DatasetPair("data/u574.dat", 36905),
        new DatasetPair("data/u1817.dat", 57201),
        new DatasetPair("data/vm1748.dat", 336556),
    };

    /**
     * List of heuristics to test.
     */
    private static final TspConstructiveHeuristic[] heuristics = {
        new NearestNeighbor(),
        new DoubleEndsNearestNeighbor()
    };

    public static void main(String[] args) throws Exception {
        // We want to compute stats for every dataset and every heuristic.
        for (var dataset : datasets) {
            System.out.printf(
                    "%n%nDataset: %s (optimal length %d)%n",
                    dataset.filename,
                    dataset.optimalLength
            );
            // Load the data for the current dataset
            TspData data = TspData.fromFile(dataset.filename);

            // Iterate over the available heuristics and compute a tour starting from every city.
            for (var heuristic : heuristics) {
                long min = Long.MAX_VALUE;
                long max = 0;
                long avg = 0;
                long avgTime = 0;

                for (int i = 0; i < data.getNumberOfCities(); i++) {
                    var start = System.nanoTime();
                    var tour = heuristic.computeTour(data, i);
                    var end = System.nanoTime();

                    // Update our running average and min/max values if needed.
                    var length = tour.length();
                    min = Math.min(min, length);
                    max = Math.max(max, length);
                    avg += length;
                    avgTime += end - start;
                }

                // compute the average time of computation and average tour length
                avg /= data.getNumberOfCities();
                avgTime /= data.getNumberOfCities();

                // Log our results
                System.out.println("-".repeat(80));
                System.out.printf(
                        "Heuristic: %s (average tour compute time %.2f ms)%n",
                        heuristic.getClass().getSimpleName(),
                        avgTime / 1_000_000.0
                );
                System.out.printf("Min: %d (%.2f%% to optimal)%n", min, 100.0 * dataset.optimalLength / min);
                System.out.printf("Avg: %d (%.2f%% to optimal)%n", avg, 100.0 * dataset.optimalLength / avg);
                System.out.printf("Max: %d (%.2f%% to optimal)%n", max, 100.0 * dataset.optimalLength / max);
            }
        }
    }

    /**
     * Represents a dataset and its optimal length.
     * @param filename Filename of the dataset
     * @param optimalLength Optimal length of the dataset
     */
    private record DatasetPair(
            String filename,
            long optimalLength
    ) {}
}
