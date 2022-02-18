package se.bettercode.scrum.backlog;


import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class BacklogBurnChart {

    ListProperty<BurnupDay> burnupDays = new SimpleListProperty<BurnupDay>(FXCollections.observableList(new ArrayList<BurnupDay>()));
    ListProperty<BurnDownDay> burnDownDays = new SimpleListProperty<BurnDownDay>(FXCollections.observableList(new ArrayList<BurnDownDay>()));

    public ObservableList<BurnupDay> getBurnupDays() {
        return burnupDays.get();
    }

    public ListProperty<BurnupDay> burnupDaysProperty() {
        return burnupDays;
    }
    public ListProperty<BurnDownDay> burnDownDaysProperty() {
        return burnDownDays;
    }

    void addDay(BurnupDay burnupDay) {
        burnupDays.add(burnupDay);
    }

    void addBurnDownDay(BurnDownDay burnDownDay) {
        burnDownDays.add(burnDownDay);
    }

}
