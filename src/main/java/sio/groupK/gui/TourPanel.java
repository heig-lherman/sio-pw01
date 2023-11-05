package sio.groupK.gui;

import java.awt.*;
import javax.swing.*;
import sio.tsp.TspTour;

import static java.util.Objects.isNull;

/**
 * Main panel for the application that can visualise a TSP tour.
 */
public class TourPanel extends JPanel {

    private static final int BORDER = 10;

    private TspTour tour;

    public TourPanel() {
        // set a white background
        setBackground(Color.WHITE);
    }

    public void setTour(TspTour tour) {
        // when we get a new tour, assign it and repaint.
        this.tour = tour;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isNull(tour)) {
            return;
        }

        // boilerplate code to enable anti-aliasing
        Graphics2D graph = (Graphics2D) g;
        graph.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // compute the scale factor to fit the tour in the panel (not to scale)
        ScaleFactor scaleFactor = computeScaleFactor();

        int[] order = tour.tour();
        // Draw cities first, since we might add path animations later on
        for (int i = 0; i < order.length; ++i) {
            int[] coords = getCityCoordinates(order[i], scaleFactor);
            graph.setColor(i == 0 ? Color.RED : Color.BLACK);
            graph.drawRect(coords[0] - 1, coords[1] - 1, 3, 3);
        }

        // Continue by drawing the computed tour as lines between the cities
        graph.setColor(new Color(0, 0, 0, 0.5f));
        for (int i = 0; i < order.length; ++i) {
            int[] coords1 = getCityCoordinates(order[i], scaleFactor);
            int[] coords2 = getCityCoordinates(order[(i + 1) % order.length], scaleFactor);
            graph.drawLine(coords1[0], coords1[1], coords2[0], coords2[1]);
        }

        graph.setColor(Color.BLACK);
    }

    /**
     * Compute the coordinates of a city in the panel.
     * @param city City index
     * @param scaleFactor Scale factor to apply
     * @return Coordinates of the city in the panel scaled to fit the panel
     */
    private int[] getCityCoordinates(int city, ScaleFactor scaleFactor) {
        return new int[]{
                (int) Math.floor(
                        (tour.data().getXCoordinateForCity(city) - scaleFactor.minX())
                                * scaleFactor.scaleX()
                                + BORDER
                ),
                (int) Math.floor(
                        (tour.data().getYCoordinateForCity(city) - scaleFactor.minY())
                                * scaleFactor.scaleY()
                                + BORDER
                ),
        };
    }

    /**
     * @return The scale factor to apply to the tour to fit it in the panel
     */
    private ScaleFactor computeScaleFactor() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < tour.data().getNumberOfCities(); ++i) {
            int x = tour.data().getXCoordinateForCity(i);
            int y = tour.data().getYCoordinateForCity(i);
            if (maxX < x) {
                maxX = x;
            }

            if (minX > x) {
                minX = x;
            }

            if (maxY < y) {
                maxY = y;
            }

            if (minY > y) {
                minY = y;
            }
        }

        return new ScaleFactor(
                minX,
                minY,
                (double) (getWidth() - 2 * BORDER) / (maxX - minX),
                (double) (getHeight() - 2 * BORDER) / (maxY - minY)
        );
    }

    /**
     * Represents a scale factor to apply to a tour to fit it in the panel.
     * @param minX Minimum X coordinate of the tour
     * @param minY Minimum Y coordinate of the tour
     * @param scaleX Scale factor to apply on the X axis
     * @param scaleY Scale factor to apply on the Y axis
     */
    private record ScaleFactor(int minX, int minY, double scaleX, double scaleY) {
    }
}
