package se.bettercode.scrum.gui;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import se.bettercode.scrum.backlog.BurnupDay;

import java.util.ArrayList;


public class BurnupChart extends AreaChart<Number, Number> {

    XYChart.Series totalSeries;
    XYChart.Series doneSeries;
    boolean isBurnDown = false;
    static final double lowerBound = 0;
    static final double yUpperBound = 40; //TODO: Make to backlog size in points?
    static final double tickUnit = 1;
    ArrayList<BurnupDay> burndays = new ArrayList<>();

    public BurnupChart(int xUpperBound, boolean isBurnDown) {
        super(new NumberAxis(lowerBound, xUpperBound, tickUnit), new NumberAxis(lowerBound, yUpperBound, tickUnit));
        getYAxis().setLabel("Points");
        getXAxis().setLabel("Days");
        setTitle("Burnup");

        initChart();
        totalSeries.setName("Total");
        doneSeries.setName("Done");
        this.isBurnDown =isBurnDown;
    }

    public void flipData()
    {
        changeChart();
        getData().get(0).getData().clear();
        getData().get(1).getData().clear();

        for(BurnupDay day : burndays)
        {
            getData().get(0).getData().add(makeDoneSeriesData(day));
            getData().get(1).getData().add(makeTotalSeriesData(day));
        }
    }
    public void changeChart()
    {
        isBurnDown = !isBurnDown;

        if(isBurnDown)
            setTitle("Burndown");
        else
            setTitle("Burnup");
    }


    public void bindBurnupDaysProperty(ListProperty<BurnupDay> burnupDays) {
        burnupDays.get().addListener((Change<? extends BurnupDay> c) -> addBurnupData(c));
    }

    private void addBurnupData(Change<? extends BurnupDay> c) {
        Platform.runLater(() -> {
            while (c.next()) {
                for (BurnupDay burnupDay : c.getAddedSubList()) {
                    burndays.add(burnupDay);
                    getData().get(0).getData().add(makeDoneSeriesData(burnupDay));
                    getData().get(1).getData().add(makeTotalSeriesData(burnupDay));
                }
            }
        });
    }

    public void removeAllData() {
        System.out.println("Removing all chart data...");
        initChart();
    }

    private void initChart() {
        getData().clear();
        totalSeries = new Series();
        doneSeries = new Series();
        getData().addAll(doneSeries, totalSeries);
    }

    private Data makeTotalSeriesData(BurnupDay burnupDay) {
        if(isBurnDown)
            return new Data(burnupDay.getDay(), 0);
        else
            return new Data(burnupDay.getDay(), burnupDay.getTotal());
    }

    private Data makeDoneSeriesData(BurnupDay burnupDay) {
        // Actually draws the chart
        if(isBurnDown)
            return new Data(burnupDay.getDay(), burnupDay.getTotal() - burnupDay.getDone());
        else
            return new Data(burnupDay.getDay(), burnupDay.getDone());
    }

}
