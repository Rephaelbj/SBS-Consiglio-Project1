# SBS-Bieganski
This repository contains our team's work for the SER 516 semester project.

## Setup
To setup the project you need to download a few things first. 
1. Openjdk17
2. Java 17

Now we will use Intellij for the instructions however the steps in eclipse should be the same. 

1. Setup the maven project in the IDE 
2. Set the project compiler level to 17 and the ide to 17
3. Run the build using this configuration mvn clean package install and the dependencies should download upon build.
4. You can run the program using the command below however you need to add this to the VM arguments: --module-path="path/to/lib/folder" --add-modules=javafx.controls,javafx.fxml,javafx.media


## Run
java -jar target/ScrumBoardSimulator-1.0-SNAPSHOT-jar-with-dependencies.jar

## Contributors

### Original
Max Wenzin
Oscar Lantz

### New
David Bieganski
Dhruvi Jayeshkumar Modi
Bhavana Priya Kanumuri
Nabeel Khan

### Even newer
Gianni Consiglio
Rephael Jackson
