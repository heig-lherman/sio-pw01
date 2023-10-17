package sio.groupK.gui.model;

// att532 : 86729
// rat575 : 6773
// rl1889 : 316536
// u574   : 36905
// u1817  : 57201
// vm1748 : 336556

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

    public String path() {
        return path;
    }
}
