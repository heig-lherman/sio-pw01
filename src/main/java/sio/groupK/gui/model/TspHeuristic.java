package sio.groupK.gui.model;

import sio.groupK.DoubleEndsNearestNeighbor;
import sio.groupK.NearestNeighbor;
import sio.tsp.TspConstructiveHeuristic;

public enum TspHeuristic {
    NN {
        @Override
        public TspConstructiveHeuristic getHeuristicInstance() {
            return new NearestNeighbor();
        }
    },
    DENN {
        @Override
        public TspConstructiveHeuristic getHeuristicInstance() {
            return new DoubleEndsNearestNeighbor();
        }
    };

    public abstract TspConstructiveHeuristic getHeuristicInstance();
}
