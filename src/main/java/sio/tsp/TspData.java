package sio.tsp;

import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * <p>Class storing data for an instance of the TSP.</p>
 *
 * <p>Each instance contains at least 3 cities.</p>
 */
public final class TspData {

  private final City[] cities;
  private final int[][] distanceMatrix;

  /**
   * Creates a new TspData.
   *
   * @param cities Array of cities.
   * @param distanceMatrix Matrix of distances between cities.
   */
  private TspData(final City[] cities, final int[][] distanceMatrix) {
    this.cities = cities;
    this.distanceMatrix = distanceMatrix;
  }

  /**
   * Creates a new TspData instance from a text file containing cities' data.
   *
   * @param filename name of the file to read from.
   * @throws FileNotFoundException If file can't be found.
   * @throws TspParsingException If file content does not conform to expected format.
   * @throws OutOfMemoryError    If the number of cities is too large.
   */
  public static TspData fromFile(final String filename) throws TspParsingException, FileNotFoundException {
    try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
      // Check that inputStream is open and not empty
      try {
        if (!scanner.hasNext()) {
          throw new TspParsingException("Invalid data. Empty data.");
        }
      } catch (IllegalStateException e) {
        throw new TspParsingException("Invalid data. Unable to read data.");
      }

      // Read the number of cities
      int numberOfCities;
      try {
        numberOfCities = scanner.nextInt();
      } catch (InputMismatchException e) {
        throw new TspParsingException("Invalid data value. Invalid number of cities in first line of data file.");
      } catch (NoSuchElementException e) {
        throw new TspParsingException("Invalid data format. Empty data file.");
      }
      if (numberOfCities < 3) {
        throw new TspParsingException("Invalid data value. Number of cities should be at least 3.");
      }


      // Allocate the array storing the XY coordinates of the cities
      City[] cities;
      try {
        cities = new City[numberOfCities];
      } catch (OutOfMemoryError e) {
        throw new OutOfMemoryError("Out of memory error. Number of cities is too large.");
      }

      // Read the coordinates of each city
      for (int cityReadCount = 0; cityReadCount < numberOfCities; cityReadCount++) {
        try {
          int cityNumber = scanner.nextInt();
          if (cityNumber != cityReadCount) {
            throw new TspParsingException(
                  String.format("Invalid city number: %s expected, %s read.", cityNumber, cityReadCount));
          }
          int x = scanner.nextInt();
          int y = scanner.nextInt();
          cities[cityNumber] = new City(x, y);
        } catch (InputMismatchException e) {
          throw new TspParsingException("Invalid data value. City numbers and coordinates should be non negative integers.");
        } catch (NoSuchElementException e) {
          throw new TspParsingException(
                "Incomplete line : should follow format \"<city number> <x> <y>\""
          );
        } catch (IllegalStateException e) {
          throw new TspParsingException("Invalid data. Unable to read data.");
        }
      }

      // Try to allocate the distance matrix between cities.
      // If not enough space is available, set distanceMatrix to null (distances will have to be recomputed
      // each time in getDistance(i,j)).
      int[][] distanceMatrix;
      try {
        distanceMatrix = new int[numberOfCities][numberOfCities];
      } catch (OutOfMemoryError e) {
        distanceMatrix = null;
      }

      if (distanceMatrix != null) {
        for (int i = 0; i < cities.length; i++) {
          distanceMatrix[i][i] = 0;
          for (int j = 0; j < i; j++) {
            distanceMatrix[i][j] = distanceMatrix[j][i] =
                  (int) Math.round(Math.hypot(cities[i].x - cities[j].x, cities[i].y - cities[j].y));
          }
        }
      }

      return new TspData(cities, distanceMatrix);
    }
  }

  /**
   * Returns the distance between two cities.
   *
   * @param i First city index.
   * @param j Second city index.
   * @return Distance between the two cities.
   * @throws IndexOutOfBoundsException If i or j are out of bounds.
   */
  public int getDistance(int i, int j) {
    // Check for out of bounds indices
    if (i < 0 || i >= cities.length || j < 0 || j >= cities.length) {
      throw new IndexOutOfBoundsException("City index out of bounds.");
    }

    // If distanceMatrix was not allocated, compute distance from i to j.
    if (distanceMatrix == null) {
      return (int) Math.round(Math.hypot(cities[i].x - cities[j].x, cities[i].y - cities[j].y));
    } else {
      return distanceMatrix[i][j];
    }
  }

  /**
   * Returns the number of cities of this problem instance.
   *
   * @return Number of cities.
   */
  public int getNumberOfCities() {
    return cities.length;
  }

  /**
   * Returns X coordinate of a city
   *
   * @param i City index
   * @return X coordinate of the city
   * @throws IndexOutOfBoundsException If i is out of bounds.
   */
  public int getXCoordinateForCity(int i) {
    // Check for out of bounds index
    if (i < 0 || i >= cities.length) {
      throw new IndexOutOfBoundsException("City index out of bounds.");
    }

    return cities[i].x;
  }

  /**
   * Returns Y coordinate of a city
   *
   * @param i City index
   * @return Y coordinate of the city
   * @throws IndexOutOfBoundsException If i is out of bounds.
   */
  public int getYCoordinateForCity(int i) {
    // Check for out of bounds index
    if (i < 0 || i >= cities.length) {
      throw new IndexOutOfBoundsException("City index out of bounds.");
    }

    return cities[i].y;
  }

  /**
   * Static nested class storing cities' XY coordinates
   */
  private record City(int x, int y) {
  }
}
