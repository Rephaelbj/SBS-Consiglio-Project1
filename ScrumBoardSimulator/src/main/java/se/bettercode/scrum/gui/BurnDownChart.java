package se.bettercode.scrum.gui;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import se.bettercode.scrum.backlog.BurnDownDay;

public class BurnDownChart extends AreaChart<Number, Number>  {

    XYChart.Series totalSeries;
    XYChart.Series pendingSeries;
    static final double lowerBound = 0;
    static final double yUpperBound = 26;
    static final double tickUnit = 1;

    public BurnDownChart() {
        super(new NumberAxis(lowerBound, 10, tickUnit), new NumberAxis(lowerBound, yUpperBound, tickUnit));
        getYAxis().setLabel("Points");
        getXAxis().setLabel("Days");
        setTitle("BurnDown");

        initChart();
        totalSeries.setName("Total");
        pendingSeries.setName("Pending");
    }

    public void bindBurnDownDaysProperty(ListProperty<BurnDownDay> burnDownDays) {
        burnDownDays.get().addListener((ListChangeListener.Change<? extends BurnDownDay> c) -> addBurnDownData(c));
    }

    private void addBurnDownData(ListChangeListener.Change<? extends BurnDownDay> c) {
        Platform.runLater(() -> {
            while (c.next()) {
                for (BurnDownDay burnDownDays : c.getAddedSubList()) {
                    getData().get(0).getData().add(makePendingSeriesData(burnDownDays));
                    getData().get(1).getData().add(makeTotalSeriesData(burnDownDays));
                }
            }
        });
    }

    private Data makeTotalSeriesData(BurnDownDay burnDownDays) {
        return new Data(burnDownDays.getDay(), burnDownDays.getTotal());
    }

    private Data makePendingSeriesData(BurnDownDay burnDownDays) {
        return new Data(burnDownDays.getDay(), burnDownDays.getPending());
    }

    private void initChart() {
        getData().clear();
        totalSeries = new Series();
        pendingSeries = new Series();
        getData().addAll(pendingSeries, totalSeries);
    }

    public void removeAllData() {
        System.out.println("Removing all chart data...");
        initChart();
    }

    public BurnDownChart(Axis<Number> axis, Axis<Number> axis1) {
        super(axis, axis1);
    }

    public BurnDownChart(Axis<Number> axis, Axis<Number> axis1, ObservableList<Series<Number, Number>> observableList) {
        super(axis, axis1, observableList);
    }
}
