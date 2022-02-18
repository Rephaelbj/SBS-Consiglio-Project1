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
    private int sprintDaysCount = 0;
    ArrayList<BurnupDay> burndays = new ArrayList<>();

    public BurnupChart(int xUpperBound, boolean isBurnDown) {
        super(new NumberAxis(lowerBound, xUpperBound, tickUnit), new NumberAxis(lowerBound, yUpperBound, tickUnit));
        sprintDaysCount = xUpperBound;
        getYAxis().setLabel("Points");
        getXAxis().setLabel("Days");
        setTitle("Burnup");

        initChart();
        this.isBurnDown = isBurnDown;
    }

    public void flipData()
    {
        changeChart();
        getData().get(0).getData().clear();
        getData().get(1).getData().clear();

        for(BurnupDay day : burndays)
        {
            getData().get(0).getData().add(makeDoneSeriesData(day));
            getData().get(1).getData().add(getIdealBurnDownLine(day));
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
                    getData().get(1).getData().add(getIdealBurnDownLine(burnupDay));
                }
            }
        });
    }

    private Data getIdealBurnDownLine(BurnupDay burnupDay) {
        double slope = ((double)burnupDay.getTotal() / 10);
        double day = burnupDay.getDay();
        int sprintStory;
        if(isBurnDown) {
            sprintStory = (int)((double)burnupDay.getTotal() - (double)slope*day);
        } else {
            sprintStory = (int)(slope*day);
        }
        return new Data(burnupDay.getDay(), sprintStory);
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
        getData().get(0).setName("Sprint Chart");
        getData().get(1).setName("Ideal Chart");
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
