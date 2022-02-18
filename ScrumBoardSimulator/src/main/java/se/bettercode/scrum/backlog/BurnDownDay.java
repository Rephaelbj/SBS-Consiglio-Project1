package se.bettercode.scrum.backlog;

import lombok.Data;

@Data
public class BurnDownDay {

    private int day, total, pending;

    public BurnDownDay(int day, int total, int pending) {
        if (pending > total) {
            throw new IllegalArgumentException("pending " + pending + " must not be larger than total " + total);
        }
        this.day = day;
        this.total = total;
        this.pending = pending;
    }
}
