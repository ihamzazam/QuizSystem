/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hamza
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.lang.Math;

public class AnalysisForm extends Stage {

    //Declaration and initialisation of variables
    private File quesFile = new File("./data", "inputdata.txt");
    private File contAnsFile = new File("./data", "answers.txt");
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
    private int totQues = 25;
    XYChart.Series series1 = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();
    XYChart.Series series3 = new XYChart.Series();

    public AnalysisForm() {

        Label labResult = new Label();
        Label space = new Label();
        space = new Label("");
        this.setTitle("MUKT Analaysis");
        bc.setTitle("Result Summary");
        xAxis.setLabel("Participant(s)");
        xAxis.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
        yAxis.setLabel("Score");
        yAxis.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
        bc.setMinHeight(500);
        bc.setMinWidth(1000);
        labResult.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#000000;");

        Scanner sQuesFile;
        char ansSheet[] = new char[totQues];
        char answer;
        int curAnsSheetIndex = 0;
        try {
            sQuesFile = new Scanner(quesFile);

            totQues = Integer.parseInt(sQuesFile.nextLine()); //Get the total number of question
            ansSheet = new char[totQues];

            while (sQuesFile.hasNextLine()) { //while question file has next line
                String qLine = sQuesFile.nextLine();
                Scanner qLineSplit = new Scanner(qLine);
                qLineSplit.useDelimiter(":"); //split based on delimiter of ":"

                Integer.parseInt(qLineSplit.next());
                answer = qLineSplit.next().charAt(0); //Get the answer at char(0) by using the delimiter of ":"

                ansSheet[curAnsSheetIndex] = answer; //Insert answer into array of answer sheet
                curAnsSheetIndex++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File to read " + quesFile + " not found!");
        }

        Scanner sfile;
        String name;
        int correctAns = 0;
        int wrongAns = 0;
        int noAns = 0;
        int contNum = 0;
        int max = 0;
        int min = 0;
        char contAns;
        double median;
        int mode;
        List<Integer> contRes = new ArrayList<Integer>();
        double totCorrectAns = 0;
        double mean;
        double std;
        try {
            sfile = new Scanner(contAnsFile); //Scanner for contestant's answer file
            while (sfile.hasNextLine()) { //While file has next line
                String aLine = sfile.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(":");

                while (sline.hasNext()) {
                    for (int i = 0; i < totQues; i++) {
                        contAns = sline.next().charAt(0);
                        if ("ABCD".indexOf(contAns) == -1) { //If contestant did not answer the question
                            noAns++; //No answer increase
                        } else if (contAns == ansSheet[i]) { //If contestant's answer is same as the answersheet
                            correctAns++; //Correct Answer increase
                        } else { //If answer is wrong
                            wrongAns++; //Wrong answer increase
                        }
                    }

                    if (sline.hasNext()) { //To get name 
                        name = sline.next();

                        series1.setName("Correct Answer(s)");
                        series1.getData().add(new XYChart.Data(name, correctAns)); //Insert the value of the contestant's name and correct answers for barchart

                        series2.setName("Wrong Answer(s)");
                        series2.getData().add(new XYChart.Data(name, wrongAns)); //Insert the value of the contestant's name and wrong answers for barchart

                        series3.setName("Question did not answer");
                        series3.getData().add(new XYChart.Data(name, noAns)); //Insert the value of the contestant's name and no attempted answer for barchart

                        contNum++; //Get the total number of contestant
                        totCorrectAns += correctAns; //To get the total number of correct answer

                        contRes.add(correctAns); //Total number of correct answer is added into array

                    }
                    
                    //To reset the value
                    correctAns = 0;
                    wrongAns = 0;
                    noAns = 0;
                }
                max = maximum(contRes); //To get the maximum score of the quiz
                min = minimum(contRes); //To get the minimum score of the quiz
                mean = totCorrectAns / contNum; //To get the mean score of the quiz
                mean = Math.round(mean * 100.0) / 100.0;
                mode = mode(contRes, contNum); //To get the mode score of the quiz
                median = median(contRes, contNum); //To get the median score of the quiz
                std = stdDeviation(contRes, contNum, mean); //To get the standard deviation of the quiz
                std = Math.round(std * 100.0) / 100.0;

                labResult.setText("Statistical Analysis \n"
                    + "\n"
                    + "Highest Score" + "\t\t" + ": " + Integer.toString(max) + "\n"
                    + "Lowest Score" + "\t\t" + ": " + Integer.toString(min) + "\n"
                    + "Mean" + "\t\t\t" + ": " + Double.toString(mean) + "\n"
                    + "Mode" + "\t\t\t" + ": " + Integer.toString(mode) + "\n"
                    + "Median" + "\t\t\t" + ": " + Double.toString(median) + "\n"
                    + "Standard Deviation" + "\t" + ": " + Double.toString(std)
                );
            }
            sfile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File to read " + contAnsFile + " not found!");
        }

        bc.getData().addAll(series1, series2, series3); //Add all the values into bar chart

        Button btnExit = new Button("EXIT"); //Exit button to close the system
        btnExit.setOnAction(e -> {
            this.close();
        });

        VBox myVBox = new VBox(bc, labResult, space, btnExit);
        myVBox.setAlignment(Pos.TOP_CENTER);

        myVBox.getStylesheets().add("Chart.css");

        this.setScene(new Scene(myVBox, 1200, 725));
    }

    //Calculation of maximum score
    public int maximum(List<Integer> result) {
        int maximum;
        maximum = result.get(0); //To assign the first array as the maximum score
        for (int i = 0; i < result.size(); i++) { //To loop through the whole array of result
            if (result.get(i) > maximum) { //If there is a score lower than the maximum
                maximum = result.get(i); //That score is assigned as the maximum
            }
        }
        return maximum;
    }

    //Calculation of minimum score
    public int minimum(List<Integer> result) {
        int minimum;
        minimum = result.get(0); //To assign the first array as the minimum score
        for (int i = 0; i < result.size(); i++) { //To loop through the whole array of result
            if (result.get(i) < minimum) { //If there is a score lower than the minimum
                minimum = result.get(i); //That score is assigned as the minimum
            }
        }
        return minimum;
    }

    //Calculation of mode
    public int mode(List<Integer> result, int contNo) {
        int maxValue = 0, maxCount = 0, i, j;
        for (i = 0; i < contNo; ++i) { //Loop based on the number of contestant
            int count = 0;
            for (j = 0; j < contNo; ++j) { //Based on that contestant's result, a loop will occur to compare the other contestant to this contestant
                if (result.get(j) == result.get(i)) { //If the score is the same
                    ++count; //Count will increase
                }
            }
            if (count > maxCount) { //If the count is greater than the current maxCount
                maxCount = count; //maxCount will equals to the count
                maxValue = result.get(i); //maxValue will be assigned to that maxCount's result
            }
        }
        return maxValue;
    }

    //Calculation of median
    public int median(List<Integer> result, int contNo) {
        int m;
        Collections.sort(result); //To sort result to ascending order
        if (contNo % 2 == 1) { //If there are only 2 contestants
            m = result.get((contNo + 1) / 2 - 1);
        } else { //If there are more than 2 contestants
            m = (result.get(contNo / 2 - 1) + result.get(contNo / 2)) / 2;
        }
        return m;
    }

    //Calculation of standard deviation
    public double stdDeviation(List<Integer> result, int contNo, double m) {
        int sum = 0;
        double sq = 0.0;
        for (int i = 0; i < contNo; i++) {
            sum += Math.pow((result.get(i) - m), 2); //Using math pow to get the sum
        }
        sq = sum / contNo; //Sum will then be divided with number of contestant
        double std = Math.sqrt(sq); //Then it will be square rooted to get the standard deviation
        return std;
    }

    public void showStage() {
        this.show();
    }
}