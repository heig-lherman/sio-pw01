package sio.tsp;

/**
 * Constructive heuristic for the TSP
 */
@FunctionalInterface
public interface TspConstructiveHeuristic {
	/**
	 * <p>Computes a tour for the travelling salesman problem on the given data.</p>
	 *
	 * <p>No guarantee is given as to the optimality of the resulting tour.</p>
	 *
	 * @param data Data of problem instance
	 * @param startCityIndex Index of starting city, if needed by the implementation
	 *
	 * @return Solution found by the heuristic
	 * @throws NullPointerException if {@code data} is null
	 * @throws IllegalArgumentException if {@code startCityIndex} is not usable by the implementation
	 */
	TspTour computeTour(TspData data, int startCityIndex);
}
