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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ResultForm extends Stage {

    //Declaration and initialisation of variables
    private File rfile = new File("./data", "answers.txt");
    private File qfile = new File("./data", "inputdata.txt");
    private int totQues = 25;
    ComboBox<String> comboBox;
    Label labResultLabel = new Label("R E S U L T S ");
    private Label labName, labGrade;
    private Label labResult, labPercentage;
    private AnalysisForm winAnalysis;
    private char cAns;
    private char ansSheet[] = new char[totQues];
    private char finalAns[] = new char[totQues];
    List<String> listNames = new ArrayList<String>();

    public ResultForm() {
        this.setTitle("Results");
        labName = new Label("Choose the result of participant that you want to view");

        // Show "Results"
        labResult = new Label();

        // Show Percentage
        Label percentage = new Label("PERCENTAGE: ");

        labPercentage = new Label();

        labGrade = new Label();

        Label selected = new Label();

        // DropDown List
        comboBox = new ComboBox<String>(FXCollections.observableList(listNames));

        // Button Proceed to analysis
        Button btnExit = new Button("Proceed to Analysis");
        btnExit.setOnAction(e -> {
            this.hide();
            winAnalysis = new AnalysisForm();
            winAnalysis.showStage();
        });

        // Button Display Results
        Button btnDisplay = new Button("Display Results");
        btnDisplay.setOnAction(e -> {
            selected.setText("Displaying result of " + comboBox.getValue());
            compareAns(rfile, qfile);
        });

        // Adding the components into the scene
        VBox myVbox = new VBox(labName, comboBox, btnDisplay, selected, labResultLabel, labResult, percentage,
                labPercentage, labGrade, btnExit);
        myVbox.setSpacing(10);
        myVbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(myVbox, 700, 700);
        this.setScene(scene);
    }

    //Adding names into the dropDown List
    public void readName() {
        Scanner rFile;
        String name;
        try {
            rFile = new Scanner(rfile);
            while (rFile.hasNextLine()) {
                String aLine = rFile.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(":");
                while (sline.hasNext()) {
                    for (int i = 0; i < totQues; i++) {
                        cAns = sline.next().charAt(0);
                    }
                    name = sline.next();
                    listNames.add(name);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Compare Answers
    public void compareAns(File rf, File qf) {
        Scanner sQueFile;
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
        String name = "";
        String result = "";
        double percentageResults = 0;
        double correctAns = 0;
        try {
            sfile = new Scanner(rf);
            while (sfile.hasNextLine()) {

                String aLine = sfile.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(":");

                while (sline.hasNext()) {
                    for (int i = 0; i < totQues; i++) {
                        cAns = sline.next().charAt(0);
                        finalAns[i] = cAns;
                    }

                    if (sline.hasNext()) {
                        name = sline.next();
                    } else {
                        name = "";
                    }

                    if (name.equals(comboBox.getValue())) {
                        for (int k = 0; k < finalAns.length; k++) {
                            result += k + 1 + ".\t" + finalAns[k] + " \t";
                            if (k == 4 | k == 9 | k == 14 | k == 19 | k == 24) {
                                result += "\n";
                            }
                        }
                        for (int k = 0; k < ansSheet.length; k++) {
                            if (ansSheet[k] == finalAns[k]) {
                                correctAns++;
                            }
                        }
                        percentageResults = (correctAns / totQues) * 100;
                        percentageResults = Math.round(percentageResults * 100.0) / 100.0;
                        labPercentage.setText(Double.toString(percentageResults) + " % ");

                        //Give grade(passed or failed)
                        if (percentageResults >= 50) {
                            labGrade.setText("PASSED. CONGRATULATIONS TO YOU");
                        } else if (percentageResults < 50) {
                            labGrade.setText("FAILED. BETTER LUCK NEXT TIME :)");
                        } else {
                            labGrade.setText("ERROR DISPLAYING GRADE");
                        }
                    }
                }
            }
            if (comboBox.getValue() == null) {
                result += "NOTHING TO DISPLAY";
            }
            labResult.setText(result);
            sfile.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showStage() {
        this.show();
    }
}
