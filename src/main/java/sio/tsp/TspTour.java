package sio.tsp;

import java.util.Arrays;

/**
 * Class storing a solution for an instance of the TSP.
 */
public record TspTour(TspData data, int[] tour, long length) {
  /**
   * @return a copy of the tour.
   */
  public int[] tour() {
    return Arrays.copyOf(tour, tour.length);
  }

  /**
   * @return String representation of the current tour
   */
  public String toString() {
    return "Length: " + length + ", "
          + "Tour: " + Arrays.toString(tour);
  }
}
