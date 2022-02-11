package se.bettercode;

import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.SmallBacklog;
import se.bettercode.scrum.Sprint;
import se.bettercode.scrum.team.CobraTeam;
import se.bettercode.scrum.team.Team;

import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting command line app.");
        Team team = new CobraTeam();
        Backlog backlog = new SmallBacklog();
        Sprint sprint = new Sprint("First sprint", 10, team, backlog);
        sprint.runSprint();
        System.out.println("Finished command line app.");
    }
}
