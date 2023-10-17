package sio.groupK.gui;

import javax.swing.*;
import sio.groupK.gui.model.TspDataSource;
import sio.groupK.gui.model.TspHeuristic;
import sio.tsp.TspData;

public class TspApplication extends JFrame {
    private JPanel contentPane;
    private JComboBox<TspDataSource> data;
    private JComboBox<TspHeuristic> heuristic;
    private JSlider startIndex;
    private JLabel startIndexLabel;
    private JLabel tourLengthLabel;
    private TourPanel tourPanel;

    public TspApplication() {
        super("TSP Visualiser");
        setContentPane(contentPane);

        data.setModel(new DefaultComboBoxModel<>(TspDataSource.values()));
        heuristic.setModel(new DefaultComboBoxModel<>(TspHeuristic.values()));

        data.addActionListener(e -> computeTour());
        heuristic.addActionListener(e -> computeTour());
        startIndex.addChangeListener(e -> {
            startIndexLabel.setText(String.valueOf(startIndex.getValue()));
            computeTour();
        });

        computeTour();
    }

    private void computeTour() {
        try {
            var tourData = TspData.fromFile(((TspDataSource) data.getSelectedItem()).path());
            startIndex.setMaximum(tourData.getNumberOfCities() - 1);

            var tour = ((TspHeuristic) heuristic.getSelectedItem())
                    .getHeuristicInstance()
                    .computeTour(
                            tourData,
                            startIndex.getValue()
                    );
            tourPanel.setTour(tour);
            tourLengthLabel.setText("Tour length: %d".formatted(tour.length()));
        } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "An error occurred while loading data",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createUIComponents() {
        tourPanel = new TourPanel();
    }

    public static void main(String[] args) {
        TspApplication app = new TspApplication();
        app.pack();
        app.setSize(1200, 600);
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
