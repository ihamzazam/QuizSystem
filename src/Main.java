/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hamza
 */

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.stage.Stage;

public class Main extends Application {

	// declaring and initializing the variables 
	private File myf = new File("./data", "questions.txt");
	private File myf2 = new File("./data", "result.txt");
	private int totQues = 0;
	private int activeQ = 1;
	private Label labQuesNo, labQues, labName, labCountry, labID;
	private ImageView imgQues;
	private ImageView imgAnsA, imgAnsB, imgAnsC, imgAnsD;
	private Label labA, labB, labC, labD;
	private RadioButton radChoice1, radChoice2, radChoice3, radChoice4;
	private ToggleGroup grpChoices;
	private Button btnPrev, btnNext, btnSubmit;
	private Pane mainPane;
	private Pane paneC;
	private Scene mainScene;
	private ContestantEntryForm winEntryForm;
	private ResultForm winResult;
	private static final Integer STARTTIME = 300;
	private Timeline timeline;
	private Label timeText,timerLabel, timerLabelR;
	private Integer timeSeconds = STARTTIME;
	private MediaPlayer mdPlayer;
	private Rectangle rightPanel;
	private LinkedList<TestForm> quesList = new LinkedList<TestForm>();

	public void start(Stage mainStage) {
		mainStage.setTitle("Miss Universe Knowledge Test");

		//rightPanel
		rightPanel = new Rectangle();
		rightPanel.setWidth(10);
		rightPanel.setHeight(900);
		rightPanel.setX(990);
		rightPanel.setY(0);
		rightPanel.setFill(Color.BLACK);		
		
		// Layouts of components
		labName = new Label("");
		labName.setLayoutX(1050);
		labName.setLayoutY(120);
		labName.setStyle("-fx-font-size: 20pt;-fx-pref-width: 200px;-fx-font-weight:bold;");

		labCountry = new Label("");
		labCountry.setLayoutX(1050);
		labCountry.setLayoutY(505);
		labCountry.setStyle("-fx-font-size: 20pt;-fx-pref-width: 200px;-fx-font-weight:bold;");

		labID = new Label("");
		labID.setLayoutX(1240);
		labID.setLayoutY(120);
		labID.setStyle("-fx-font-size: 20pt;-fx-font-weight:bold;");

		labQuesNo = new Label("");
		labQuesNo.setLayoutX(50);
		labQuesNo.setLayoutY(50);
		labQuesNo.setStyle("-fx-font-size: 15pt;-fx-font-family:serif;-fx-text-fill:#0000ff;");

		labQues = new Label("");
		labQues.setLayoutX(50);
		labQues.setLayoutY(75);
		labQues.setStyle("-fx-font-size: 20pt;-fx-font-weight:bold;");

		imgQues = new ImageView();
		imgQues.setLayoutX(50);
		imgQues.setLayoutY(75);
		imgQues.setFitHeight(175);
		imgQues.setFitWidth(175);

		imgAnsA = new ImageView();
		imgAnsA.setLayoutX(75);
		imgAnsA.setLayoutY(75);
		imgAnsA.setFitHeight(200);
		imgAnsA.setFitWidth(200);

		imgAnsB = new ImageView();
		imgAnsB.setLayoutX(75);
		imgAnsB.setLayoutY(300);
		imgAnsB.setFitHeight(200);
		imgAnsB.setFitWidth(200);

		imgAnsC = new ImageView();
		imgAnsC.setLayoutX(500);
		imgAnsC.setLayoutY(75);
		imgAnsC.setFitHeight(200);
		imgAnsC.setFitWidth(200);

		imgAnsD = new ImageView();
		imgAnsD.setLayoutX(500);
		imgAnsD.setLayoutY(300);
		imgAnsD.setFitHeight(200);
		imgAnsD.setFitWidth(200);

		labA = new Label("A");
		labA.setLayoutX(25);
		radChoice1 = new RadioButton("");
		radChoice1.setLayoutX(50);

		labB = new Label("B");
		labB.setLayoutX(25);
		radChoice2 = new RadioButton("");
		radChoice2.setLayoutX(50);

		labC = new Label("C");
		labC.setLayoutX(25);
		radChoice3 = new RadioButton("");
		radChoice3.setLayoutX(50);

		labD = new Label("D");
		labD.setLayoutX(25);
		radChoice4 = new RadioButton("");
		radChoice4.setLayoutX(50);

		grpChoices = new ToggleGroup();

		radChoice1.setToggleGroup(grpChoices);
		radChoice2.setToggleGroup(grpChoices);
		radChoice3.setToggleGroup(grpChoices);
		radChoice4.setToggleGroup(grpChoices);

		timeText = new Label("Time Remaining");
		timeText.setTextFill(Color.BLACK);
		timeText.setStyle("-fx-font-size: 15pt;-fx-font-family:sans-serif;-fx-font-weight:BOLD;");
		timeText.setLayoutX(1120);
		timeText.setLayoutY(25);
				
		timerLabel = new Label();
		timerLabel.setTextFill(Color.BLUE);
		timerLabel.setStyle("-fx-font-size: 20pt;-fx-font-weight:BOLD;");
		timerLabel.setLayoutX(1120);
		timerLabel.setLayoutY(50);

		timerLabelR = new Label();
		timerLabelR.setTextFill(Color.RED);
		timerLabelR.setStyle("-fx-font-size: 20pt;-fx-font-weight:BOLD;");
		timerLabelR.setLayoutX(1120);
		timerLabelR.setLayoutY(50);

		// Adding the components
		paneC = new Pane();
		paneC.setLayoutX(25);
		paneC.setLayoutY(75);
		paneC.getChildren().addAll(imgQues,imgAnsA,imgAnsB,imgAnsC,imgAnsD,labA,radChoice1,labB,radChoice2,labC,radChoice3,labD,radChoice4);

		// Declaration and layouts of buttons(NEXT, END, PREV)
		btnPrev = new Button("Prev");
		btnPrev.setLayoutX(350);
		btnPrev.setLayoutY(725);
		btnPrev.setStyle("-fx-pref-width: 100px;-fx-pref-height: 50px;");
		btnPrev.setDisable(true);

		btnNext = new Button("Next");
		btnNext.setLayoutX(550);
		btnNext.setLayoutY(725);
		btnNext.setStyle("-fx-pref-width: 100px;-fx-pref-height: 50px;");

		btnSubmit = new Button("Submit");
		btnSubmit.setLayoutX(1160);
		btnSubmit.setLayoutY(800);
		btnSubmit.setStyle("-fx-pref-width: 75px;");
		
		// call to read from file method to read questions from the txt file
		readFromFile();
		radChoice1.setOnAction(e -> {
			quesList.get(activeQ - 1).setSelected(0, true);
			quesList.get(activeQ - 1).setSelected(1, false);
			quesList.get(activeQ - 1).setSelected(2, false);
			quesList.get(activeQ - 1).setSelected(3, false);
		});
		radChoice2.setOnAction(e -> {
			quesList.get(activeQ - 1).setSelected(0, false);
			quesList.get(activeQ - 1).setSelected(1, true);
			quesList.get(activeQ - 1).setSelected(2, false);
			quesList.get(activeQ - 1).setSelected(3, false);
		});
		radChoice3.setOnAction(e -> {
			quesList.get(activeQ - 1).setSelected(0, false);
			quesList.get(activeQ - 1).setSelected(1, false);
			quesList.get(activeQ - 1).setSelected(2, true);
			quesList.get(activeQ - 1).setSelected(3, false);
		});
		radChoice4.setOnAction(e -> {
			quesList.get(activeQ - 1).setSelected(0, false);
			quesList.get(activeQ - 1).setSelected(1, false);
			quesList.get(activeQ - 1).setSelected(2, false);
			quesList.get(activeQ - 1).setSelected(3, true);
		});
		if (totQues == 1) {
			btnNext.setDisable(true);
		}
		btnNext.setOnAction(e -> {
			activeQ++;
			btnPrev.setDisable(false);
			if (activeQ == totQues) {
				btnNext.setDisable(true);
			}
			reloadQues();
		});
		btnPrev.setOnAction(e -> {
			activeQ--;
			btnNext.setDisable(false);
			if (activeQ == 1) {
				btnPrev.setDisable(true);
			}
			reloadQues();
		});
		btnSubmit.setOnAction(e -> {
			saveToFile();
			winResult.readName();
			mainStage.hide();
			winResult.showStage();
                        mdPlayer.dispose();
		});

		// Adding components to the scene
		mainPane = new Pane();
		mainPane.getChildren().addAll(labName,labCountry,labID,labQuesNo,labQues,paneC,btnNext,btnPrev,btnSubmit,timeText,timerLabel,timerLabelR,rightPanel);

		mainScene = new Scene(mainPane, 1400, 900); // reszie
		mainStage.setScene(mainScene);
		reloadQues();
		winEntryForm = new ContestantEntryForm();
		winEntryForm.setOnHiding(e -> {
			labName.setText(winEntryForm.getName());
			labCountry.setText(winEntryForm.getCountry());
			labID.setText(winEntryForm.getPass());
			mainStage.show();
			startTiming(mainStage);
			flag();
			CandidatePhoto();
		});
		winResult = new ResultForm();

		// Playing Siren Sound when time is less than 10 seconds
		String musicFile = "./data/Sound/sirene.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		mdPlayer = new MediaPlayer(sound);

	}

	private void CandidatePhoto() {
		FileInputStream input1;
		try {
			input1 = new FileInputStream("./data/Cand_Pics/" + winEntryForm.getName() + ".jpg");
			Image image1 = new Image(input1);
			ImageView imageCand = new ImageView(image1);
			imageCand.setLayoutX(1050);
			imageCand.setLayoutY(180);
			imageCand.setFitHeight(300);
			imageCand.setFitWidth(300);
			mainPane.getChildren().add(imageCand);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
	}

	// Display of Country flag
	public void flag() {
		FileInputStream input = null;
		try {
			input = new FileInputStream("./data/Flags/" + winEntryForm.getCountry() + ".jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image image = new Image(input);
		ImageView imageFlag = new ImageView(image);
		imageFlag.setLayoutX(1050);
		imageFlag.setLayoutY(555);
		imageFlag.setFitHeight(200);
		imageFlag.setFitWidth(300);
		mainPane.getChildren().add(imageFlag);
	}

	// Method for timer
	public void startTiming(Stage mainStage) {
		if (timeline != null) {
			timeline.stop();
		} else {
			timeSeconds = STARTTIME;
			timerLabel.setText(timeSeconds.toString() + " seconds");
			timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
				timeSeconds--;
				timerLabel.setText(timeSeconds.toString() + " seconds");
				if (timeSeconds <= 10) {
					timerLabelR.setText(timeSeconds.toString() + " seconds");
					mdPlayer.play();
				}
				if (timeSeconds <= 0) {
					mdPlayer.pause();
					timeline.stop();
					mainStage.hide();
					saveToFile();
					winResult.readName();
					winResult.showStage();
				}
			}));
			timeline.playFromStart();
		}
	}

	// Reloading of questions
	public void reloadQues() {
		labQuesNo.setText("Question " + Integer.toString(activeQ) + " of " + totQues);
		labQues.setText(quesList.get(activeQ - 1).getTheQues());
		radChoice1.setText(quesList.get(activeQ - 1).getChoice(0));
		radChoice2.setText(quesList.get(activeQ - 1).getChoice(1));
		radChoice3.setText(quesList.get(activeQ - 1).getChoice(2));
		radChoice4.setText(quesList.get(activeQ - 1).getChoice(3));
		imgQues.setImage(null);
		imgAnsA.setImage(null);
		imgAnsB.setImage(null);
		imgAnsC.setImage(null);
		imgAnsD.setImage(null);

		if (quesList.get(activeQ - 1).getType() == 1) {
			labA.setLayoutX(25);
			labA.setLayoutY(75);
			radChoice1.setLayoutX(50);
			radChoice1.setLayoutY(75);
			labB.setLayoutX(25);
			labB.setLayoutY(125);
			radChoice2.setLayoutX(50);
			radChoice2.setLayoutY(125);
			labC.setLayoutX(25);
			labC.setLayoutY(175);
			radChoice3.setLayoutX(50);
			radChoice3.setLayoutY(175);
			labD.setLayoutX(25);
			labD.setLayoutY(225);
			radChoice4.setLayoutX(50);
			radChoice4.setLayoutY(225);
		}
		if (quesList.get(activeQ - 1).getType() == 2) {
			File pFile = new File("data/QuestionsPics/" + quesList.get(activeQ - 1).getQuesPic());
			Image img = new Image(pFile.toURI().toString());
			imgQues.setImage(img);
			imgAnsA.setImage(null);
			imgAnsB.setImage(null);
			imgAnsC.setImage(null);
			imgAnsD.setImage(null);
			labA.setLayoutX(25);
			labA.setLayoutY(275);
			radChoice1.setLayoutX(50);
			radChoice1.setLayoutY(275);
			labB.setLayoutX(25);
			labB.setLayoutY(325);
			radChoice2.setLayoutX(50);
			radChoice2.setLayoutY(325);
			labC.setLayoutX(25);
			labC.setLayoutY(375);
			radChoice3.setLayoutX(50);
			radChoice3.setLayoutY(375);
			labD.setLayoutX(25);
			labD.setLayoutY(425);
			radChoice4.setLayoutX(50);
			radChoice4.setLayoutY(425);
		}
		radChoice1.setSelected(quesList.get(activeQ - 1).getSelected(0));
		radChoice2.setSelected(quesList.get(activeQ - 1).getSelected(1));
		radChoice3.setSelected(quesList.get(activeQ - 1).getSelected(2));
		radChoice4.setSelected(quesList.get(activeQ - 1).getSelected(3));

		if (quesList.get(activeQ - 1).getType() == 3) {
			File pFile1 = new File("data/QuestionsPics/" + quesList.get(activeQ - 1).getAnsPicA());
			Image imgA = new Image(pFile1.toURI().toString());
			File pFile2 = new File("data/QuestionsPics/" + quesList.get(activeQ - 1).getAnsPicB());
			Image imgB = new Image(pFile2.toURI().toString());
			File pFile3 = new File("data/QuestionsPics/" + quesList.get(activeQ - 1).getAnsPicC());
			Image imgC = new Image(pFile3.toURI().toString());
			File pFile4 = new File("data/QuestionsPics/" + quesList.get(activeQ - 1).getAnsPicD());
			Image imgD = new Image(pFile4.toURI().toString());
			imgQues.setImage(null);
			imgAnsA.setImage(imgA);
			imgAnsB.setImage(imgB);
			imgAnsC.setImage(imgC);
			imgAnsD.setImage(imgD);

			labA.setLayoutY(150);
			radChoice1.setLayoutY(150);
			labB.setLayoutY(350);
			radChoice2.setLayoutY(350);
			labC.setLayoutX(450);
			labC.setLayoutY(150);
			radChoice3.setLayoutX(475);
			radChoice3.setLayoutY(150);
			labD.setLayoutX(450);
			labD.setLayoutY(350);
			radChoice4.setLayoutX(475);
			radChoice4.setLayoutY(350);
		}
		radChoice1.setSelected(quesList.get(activeQ - 1).getSelected(0));
		radChoice2.setSelected(quesList.get(activeQ - 1).getSelected(1));
		radChoice3.setSelected(quesList.get(activeQ - 1).getSelected(2));
		radChoice4.setSelected(quesList.get(activeQ - 1).getSelected(3));
	}

	// Save Contestant's Answer to result.txt
	public void saveToFile() {
		try {
			String selected = "";
			PrintWriter pw = new PrintWriter(new FileWriter(myf2, true));
			for (TestForm i : quesList) {
				if (i.getSelected(0)) {
					selected = "A";
				} else if (i.getSelected(1)) {
					selected = "B";
				} else if (i.getSelected(2)) {
					selected = "C";
				} else if (i.getSelected(3)) {
					selected = "D";
				} else {
					selected = "N";
				}
				pw.print(selected + ":");
			}
			pw.print(labName.getText());
			pw.println();
			pw.close();
		} catch (IOException e) {

		}
	}

	// Read Questions from questions.txt
	public void readFromFile() {
		Scanner sfile;
		int type;
		char answer;
		String theQues;
		String choices[] = new String[4];
		String quesPic = "";
		String ansPic1 = "";
		String ansPic2 = "";
		String ansPic3 = "";
		String ansPic4 = "";
		TestForm ques;
		try {
			sfile = new Scanner(myf);
			String aLine = sfile.nextLine();
			Scanner sline = new Scanner(aLine);
			totQues = Integer.parseInt(sline.next());
			for (int k = 1; k <= totQues; k++) {
				aLine = sfile.nextLine();
				sline = new Scanner(aLine);
				sline.useDelimiter(":");
				type = Integer.parseInt(sline.next());
				answer = sline.next().charAt(0);
				theQues = sline.next();
				choices[0] = "";
				choices[1] = "";
				choices[2] = "";
				choices[3] = "";
				if (type == 2) {
					quesPic = sline.next();
				}
				if (type == 3) {
					ansPic1 = sline.next();
					ansPic2 = sline.next();
					ansPic3 = sline.next();
					ansPic4 = sline.next();
				}
				if (type == 1 || type == 2) {
					choices[0] = sline.next();
					choices[1] = sline.next();
					choices[2] = sline.next();
					choices[3] = sline.next();
				}

				sline.close();
				ques = new TestForm(type, answer, theQues, choices, quesPic, ansPic1, ansPic2, ansPic3, ansPic4);
				quesList.add(ques);
			}
			sfile.close();
		} catch (FileNotFoundException e) {
			System.out.println("File to read " + myf + " not found!");
		}
	}

	public static void main(String args[]) {
		Application.launch(args);
	}
}