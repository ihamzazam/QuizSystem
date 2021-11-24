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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ResultForm extends Stage {

    //Declaration and initialisation of variables
    private File contAnsFile = new File("./data", "answers.txt");
    private File inputFile = new File("./data", "inputdata.txt");
    private int totQues = 25;
    ComboBox<String> comboBox;
    Label labResultLabel = new Label("RESULT");
    private Label labName;
    private Label labResult, labPercentage;
    private AnalysisForm winAnalysis;
    private char contAns;
    private char ansSheet[] = new char[totQues];
    private char finalAns[] = new char[totQues];
    List<String> listNames = new ArrayList<String>();

    //Decimal Format to format the decimal point
    DecimalFormat df0 = new DecimalFormat("#");

    public ResultForm() {
        this.setTitle("Results");
        labName = new Label("Select a contestant to view their results");
        labName.setStyle("-fx-font-size: 20pt;-fx-font-weight:BOLD;");

        // Show "Results"
        labResult = new Label();
        labResult.setStyle("-fx-font-size: 14pt;");
        labResultLabel.setStyle("-fx-font-size: 15pt;");

        // Show Percentage
        Label percentage = new Label("PERCENTAGE: ");
        percentage.setStyle("-fx-font-size: 15pt;");

        labPercentage = new Label();
        labPercentage.setStyle("-fx-font-size: 15pt;");

        Label selected = new Label();

        // DropDown List
        comboBox = new ComboBox<String>(FXCollections.observableList(listNames));

        // Button Proceed to analysis
        Button btnExit = new Button("Proceed to Analysis");
        btnExit.setOnAction(e -> { //When button "Proceed to Analysis" is clicked
            this.hide(); //Result form will hide
            winAnalysis = new AnalysisForm();
            winAnalysis.showStage(); //Analysis form will show
        });

        // Button Display Results
        Button btnDisplay = new Button("Display Results");
        btnDisplay.setOnAction(e -> { //When button "Display Results" is clicked, result of the selected contestant will display
            selected.setText("Displaying result of " + comboBox.getValue());
            selected.setStyle("-fx-font-size: 16pt;");
            compareAns(contAnsFile, inputFile); //To display the results
        });

        HBox myHbox = new HBox(comboBox, btnDisplay);
	    myHbox.setPadding(new Insets(15, 12, 15, 12));
        myHbox.setSpacing(10);
        myHbox.setAlignment(Pos.CENTER);
        
        HBox myHbox2 = new HBox(percentage,labPercentage);
        myHbox.setPadding(new Insets(15, 12, 15, 12));
        myHbox.setSpacing(10);
        myHbox2.setAlignment(Pos.CENTER);
            
        // Adding the components into the scene
        VBox myVbox = new VBox(labName, myHbox, selected, labResultLabel, labResult, myHbox2, btnExit);
        myVbox.setSpacing(10);
        myVbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(myVbox, 700, 700);
        this.setScene(scene);
        scene.getStylesheets().add("style.css");
    }

    //Adding names into the dropDown List of the result
    public void readName() {
        Scanner rFile;
        String name;
        try {
            rFile = new Scanner(contAnsFile);
            while (rFile.hasNextLine()) {
                String aLine = rFile.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(":");
                while (sline.hasNext()) {
                    for (int i = 0; i < totQues; i++) {
                        contAns = sline.next().charAt(0);
                    }
                    name = sline.next();
                    listNames.add(name);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Compare Answers and display the results
    public void compareAns(File rf, File qf) { //rf = ansFile   qf = inputFile
        Scanner sQueFile;
        char answer;
        int curAnsSheetIndex = 0;
        try {
            sQueFile = new Scanner(qf); //To read question file

            totQues = Integer.parseInt(sQueFile.nextLine()); //To get the total number of questions
            ansSheet = new char[totQues]; //To set the array limit of answer sheet to the total question

            while (sQueFile.hasNextLine()) { //If question file has next line
                String qLine = sQueFile.nextLine(); // qLine will start from the second line of sQueFile which is the question
                Scanner qLineSplit = new Scanner(qLine);
                qLineSplit.useDelimiter(":"); //To scan and split the line
                
                Integer.parseInt(qLineSplit.next());
                answer = qLineSplit.next().charAt(0); //To get the answer
                ansSheet[curAnsSheetIndex] = answer; //Add answer to array
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
            sfile = new Scanner(rf); //To read answer file
            while (sfile.hasNextLine()) { //While is answer file has next line
                String aLine = sfile.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(":");

                while (sline.hasNext()) { //while answer file has next line
                    for (int i = 0; i < totQues; i++) { //loop based on total question
                        contAns = sline.next().charAt(0); //To get the answer of the contestant
                        finalAns[i] = contAns; //Array to keep answer of contestant
                    }

                    if (sline.hasNext()) {
                        name = sline.next(); //Get name
                    } else {
                        name = "";
                    }

                    if (name.equals(comboBox.getValue())) { //The name from combobox is equal to the name of the answer file
                        for (int k = 0; k < ansSheet.length; k++) { //Loop to get the number of correct answer
                            if (ansSheet[k] == finalAns[k]) { //To compare and get the number of right answer
                                correctAns++; //To get the total number of correct answers
                            }
                        }

                        for (int k = 0; k < finalAns.length; k++) { //Loop to print the contestant's answer
                            result += k + 1 + ".\t" + finalAns[k] + " \t";
                            if (k == 4 | k == 9 | k == 14 | k == 19 | k == 24) {
                                result += "\n";
                            }
                        }

                        percentageResults = (correctAns / totQues) * 100; //Get the percentage of the result
                        percentageResults = Math.round(percentageResults * 100.0) / 100.0;
                        labPercentage.setText(df0.format(correctAns) + " / " + totQues + " ( " + df0.format(percentageResults) + " % )"); //To print the percentage
                    }
                }
            }
            if (comboBox.getValue() == null) { //If there is not any contestant selected and display result is clicked, "NOTHING TO DISPLAY" will print
                result += "NOTHING TO DISPLAY";
            }
            labResult.setText(result); //To write the results to labResult
            sfile.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showStage() {
        this.show();
    }
}
