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
    private File qf = new File("./data", "inputdata.txt");
    private File myf = new File("./data", "answers.txt");
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

        Scanner sQueFile;
        char ansSheet[] = new char[totQues];
        int type;
        char answer;
        int curAnsSheetIndex = 0;
        try {
            sQueFile = new Scanner(qf);

            totQues = Integer.parseInt(sQueFile.nextLine());
            ansSheet = new char[totQues];

            while (sQueFile.hasNextLine()) {
                String qLine = sQueFile.nextLine();
                Scanner qLineSplit = new Scanner(qLine);
                qLineSplit.useDelimiter(":");

                type = Integer.parseInt(qLineSplit.next());
                answer = qLineSplit.next().charAt(0);

                ansSheet[curAnsSheetIndex] = answer;
                curAnsSheetIndex++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File to read " + qf + " not found!");
        }

        Scanner sfile;
        String name;
        int correctAns = 0;
        int wrongAns = 0;
        int noAns = 0;
        int count = 0;
        int max = 0;
        int min = 0;
        char cAns;
        double median;
        int mode;
        List<Integer> cRes = new ArrayList<Integer>();
        double totCorrectAns = 0;
        double mean;
        double std;
        try {
            sfile = new Scanner(myf);
            while (sfile.hasNextLine()) {

                String aLine = sfile.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(":");

                while (sline.hasNext()) {

                    for (int i = 0; i < totQues; i++) {
                        cAns = sline.next().charAt(0);
                        if ("ABCD".indexOf(cAns) == -1) {
                            noAns++;
                        } else if (cAns == ansSheet[i]) {
                            correctAns++;
                        } else {
                            wrongAns++;
                        }
                    }

                    if (sline.hasNext()) {
                        name = sline.next();

                        series1.setName("Correct Answer(s)");
                        series1.getData().add(new XYChart.Data(name, correctAns));

                        series2.setName("Wrong Answer(s)");
                        series2.getData().add(new XYChart.Data(name, wrongAns));

                        series3.setName("Question did not answer");
                        series3.getData().add(new XYChart.Data(name, noAns));

                        count++;
                        totCorrectAns += correctAns;

                        cRes.add(correctAns);

                    } else {
                        System.out.println("No name is assigned for these results.");
                    }

                    correctAns = 0;
                    wrongAns = 0;
                    noAns = 0;
                }
                max = maximum(cRes, count);
                min = minimum(cRes, count);
                mean = totCorrectAns / count;
                mean = Math.round(mean * 100.0) / 100.0;
                mode = mode(cRes, count);
                median = median(cRes, count);
                std = stdDeviation(cRes, count, mean);
                std = Math.round(std * 100.0) / 100.0;

                labResult.setText("Statistical Analysis \n"
                        + "\n"
                        + "Highest Score" + "\t\t" + ": " + Integer.toString(max) + "\n"
                        + "Lowest Score" + "\t\t" + ": " + Integer.toString(min) + "\n"
                        + "Mean" + "\t\t\t" + ": " + Double.toString(mean) + "\n"
                        + "Mode" + "\t\t\t" + ": " + Integer.toString(mode) + "\n"
                        + "Median" + "\t\t\t" + ": " + Double.toString(median) + "\n"
                        + "Standard Deviation" + "\t" + ": " + Double.toString(std));
            }
            sfile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File to read " + myf + " not found!");
        }

        bc.getData().addAll(series1, series2, series3);

        Button btnExit = new Button("EXIT");
        btnExit.setOnAction(e -> {
            this.close();
        });

        VBox myVBox = new VBox(bc, labResult, space, btnExit);
        myVBox.setAlignment(Pos.TOP_CENTER);

        myVBox.getStylesheets().add("Chart.css");

        this.setScene(new Scene(myVBox, 1200, 725));
    }

    //Calculation of maximum score
    public int maximum(List<Integer> a, int n) {
        int maximum;
        maximum = a.get(0);
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) > maximum) {
                maximum = a.get(i);
            }
        }
        return maximum;
    }

    //Calculation of minimum score
    public int minimum(List<Integer> a, int n) {
        int minimum;
        minimum = a.get(0);
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) < minimum) {
                minimum = a.get(i);
            }
        }
        return minimum;
    }

    //Calculation of mode
    public int mode(List<Integer> a, int n) {
        int maxValue = 0, maxCount = 0, i, j;
        for (i = 0; i < n; ++i) {
            int count = 0;
            for (j = 0; j < n; ++j) {
                if (a.get(j) == a.get(i)) {
                    ++count;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = a.get(i);
            }
        }
        return maxValue;
    }

    //Calculation of median
    public int median(List<Integer> a, int n) {
        int m;
        Collections.sort(a);
        if (n % 2 == 1) {
            m = a.get((n + 1) / 2 - 1);
        } else {
            m = (a.get(n / 2 - 1) + a.get(n / 2)) / 2;
        }
        return m;
    }

    //Calculation of standard deviation
    public double stdDeviation(List<Integer> a, int n, double m) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.pow((a.get(i) - m), 2);
        }
        m = sum / n;
        double std = Math.sqrt(m);
        return std;
    }

    public void showStage() {
        this.show();
    }
}