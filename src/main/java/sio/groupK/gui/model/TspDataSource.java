package sio.groupK.gui.model;

/**
 * Data sources for the TSP used in the visualiser.
 */
public enum TspDataSource {
    ATT532("data/att532.dat"),
    RAT575("data/rat575.dat"),
    RL1889("data/rl1889.dat"),
    U574("data/u574.dat"),
    U1817("data/u1817.dat"),
    VM1748("data/vm1748.dat");

    private final String path;

    TspDataSource(String path) {
        this.path = path;
    }

    /**
     * @return Path to the data file
     */
    public String path() {
        return path;
    }
}
