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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ContestantEntryForm extends Stage {

    // declaring and initializing the variables 
    private File contProfile = new File("./data", "contestants.txt");
    private File contAnsfile = new File("./data", "answers.txt");
    private PasswordField fieldPass;
    private Boolean verifiedPass = false, verifiedCountry = false;;
    private VBox contestantVbox;
    private HBox contestantHbox, contestantHbox2;
    private Scene contestantScene;
    private Label labTitle,labCountry,labPass,contName,space;
    private Button proceedBtn, resultBtn;
    private FileInputStream inputHamza, inputKish, inputCherng, inputSara, inputFirdaus, inputHamza2, inputKish2, inputCherng2, inputSara2, inputFirdaus2, inputHamza3, inputKish3, inputCherng3, inputSara3, inputFirdaus3, inputBlank;
    private Image imageHamza, imageKish, imageCherng, imageSara, imageFirdaus,imageHamza2, imageKish2, imageCherng2, imageSara2, imageFirdaus2, imageHamza3, imageKish3, imageCherng3, imageSara3, imageFirdaus3, imageBlank;
    private ImageView imageCont, imageCont2, imageCont3; // imageCont = imageContestant
    private ResultForm winResult;
    ComboBox<String> comboBoxName;
    ComboBox<String> comboBoxCountry;
    List<String> listNames = new ArrayList<String>();
    private IntegerProperty maxLength = new SimpleIntegerProperty(this, "maxLength", 7);
    private StringProperty restrict = new SimpleStringProperty(this, "restrict");
    
	public ContestantEntryForm() {
		profilePhoto(); //To get picture of contestant
		
		this.setTitle("MUKT - Contestant Entry Form");

		// Setting the label for the title MUKT
		labTitle = new Label("Miss Universe Knowledge Test");
		labTitle.setStyle("-fx-font-size: 25pt;-fx-font-weight: bold;-fx-font-family:serif;-fx-text-fill:#000000;");
                labTitle.setPadding(new Insets(0,20,10,10));

		// Setting the label for select country option
		labCountry = new Label("Select your Country");
		labCountry.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#000000;");

		// Setting the label for entering the password
		labPass = new Label("Please enter your Password");
		labPass.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#000000;");

		// Setting the label for entering the username
		contName = new Label("Please enter your Name");
		contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#000000;");
		
		space = new Label("");

		// Textfield to input the password
		fieldPass = new PasswordField();
                fieldPass.setMaxWidth(325);
                fieldPass.setPromptText("03xxxxx");
                               
		// To restrict the fieldPass to only have a maximum of 7 characters and can only input numbers
                fieldPass.textProperty().addListener(new ChangeListener<String>() {
                        private boolean ignore;
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, 
                                String newValue) {
                                if (!newValue.matches("\\d*")) { //To restrict to only numbers
                                        fieldPass.setText(newValue.replaceAll("[^\\d]", ""));
                                }
                                if (ignore || newValue == null)
                                        return;
                                if (maxLength.get() > -1 && newValue.length() > maxLength.get()) { //To restrict the length
                                        ignore = true;
                                        fieldPass.setText(newValue.substring(0, maxLength.get()));
                                        ignore = false;
                                }
                                if (restrict.get() != null && !restrict.get().equals("") && !newValue.matches(restrict.get() + "*")) {
                                        ignore = true;
                                        fieldPass.setText(oldValue);
                                        ignore = false;
                                }
                        }
                });
        
		// Dropdown option to select country
                comboBoxCountry = new ComboBox<>();
		comboBoxCountry.getItems().addAll("Mozambique","Namibia","Montenegro","Morocco","Myanmar");
		comboBoxCountry.setValue("Mozambique");
                comboBoxCountry.setMaxWidth(325);

		// Button to start the test 
		proceedBtn = new Button(" PROCEED TO TEST ");
		proceedBtn.setOnAction(e -> {
			checkNames();
                        
                        //If no name is chosen error message will prompt
                        if (comboBoxName.getValue() == null) {
                                contName.setText("Please select a user to proceed");
                                contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
                        }
                        else { //If contestant name is chosen
                                verifiedPass = verifyPass(); //To verify password
                                verifiedCountry = verifyCountry(); //To verify country

                                if (listNames.contains(comboBoxName.getValue())) { //To check if the user has already taken the test
                                        comboBoxName.setValue("");
                                        fieldPass.setText("");
                                        contName.setText("This user has taken the test, please select another user");
                                        contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
                                        imageCont.setImage(imageBlank);
                                        imageCont2.setImage(imageBlank);
                                        imageCont3.setImage(imageBlank);
                                } 
                                else if (verifiedPass == false) { //If the contestant input the wrong password, error message will promt
                                        contName.setText("Incorrect Password");
                                        contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
                                }
                                else if (verifiedCountry == false) { //If the contestant input the wrong country, error message will promt
                                        contName.setText("Incorrect Country");
                                        contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
                                }
                                else {
                                        this.hide();                   
                                }
                        }
		});

                // Button to view the results
                resultBtn = new Button(" PROCEED TO RESULT ");
		resultBtn.setOnAction(e -> {
                        winResult = new ResultForm();
                        winResult.readName();
                        winResult.showStage();
                });
		
		//ComboBox for Name
		comboBoxName = new ComboBox<>();
                comboBoxName.setMaxWidth(325);
		comboBoxName.getItems().addAll("Hamza","Kishendran","Yii Cherng","Sara","Firdaus");
		comboBoxName.setEditable(true);
		comboBoxName.setPromptText("Select Name");
                comboBoxName.setEditable(false);
		
		// Check the name selected by user, then will assign profile picture accordingly
		comboBoxName.setOnAction(e -> {
			if (comboBoxName.getValue() == "Hamza") {
				imageCont.setImage(imageHamza);
                                imageCont2.setImage(imageHamza2);
                                imageCont3.setImage(imageHamza3);
				
			} else if (comboBoxName.getValue() == "Kishendran") {
				imageCont.setImage(imageKish);
                                imageCont2.setImage(imageKish2);
                                imageCont3.setImage(imageKish3);
				
			} else if (comboBoxName.getValue() == "Yii Cherng") {
				imageCont.setImage(imageCherng);
                                imageCont2.setImage(imageCherng2);
                                imageCont3.setImage(imageCherng3);
				
			} else if (comboBoxName.getValue() == "Sara") {
				imageCont.setImage(imageSara);
                                imageCont2.setImage(imageSara2);
                                imageCont3.setImage(imageSara3);
				
			} else if (comboBoxName.getValue() == "Firdaus") {
				imageCont.setImage(imageFirdaus);
                                imageCont2.setImage(imageFirdaus2);
                                imageCont3.setImage(imageFirdaus3);
			} else {
				imageCont.setImage(imageBlank);
			}
		});

		// Default setup image
		imageCont = new ImageView(imageBlank);
		imageCont.setFitHeight(225);
		imageCont.setFitWidth(250);
                
                imageCont2 = new ImageView(imageBlank);
		imageCont2.setFitHeight(225);
		imageCont2.setFitWidth(250);
                
                imageCont3 = new ImageView(imageBlank);
		imageCont3.setFitHeight(225);
		imageCont3.setFitWidth(250);
                
                // Hbox for the 3 images to make the appear next to each other
                contestantHbox = new HBox(imageCont, imageCont2, imageCont3);
                contestantHbox.setSpacing(20);
                contestantHbox.setAlignment(Pos.CENTER);
                
                // Hbox for the two buttons (proceed and result) at the bottom of entry form
                contestantHbox2 = new HBox(proceedBtn, resultBtn);
		contestantHbox2.setPadding(new Insets(15, 12, 15, 12));
                contestantHbox2.setSpacing(10);
                contestantHbox2.setAlignment(Pos.CENTER);
                
                // Add contestant Vbox and Scene
                contestantVbox = new VBox(12);
                contestantVbox.setAlignment(Pos.CENTER);
                contestantVbox.getChildren().addAll(labTitle, contestantHbox,contName,comboBoxName,labPass,fieldPass,labCountry,comboBoxCountry,contestantHbox2,space);
                contestantScene = new Scene(contestantVbox, 900, 700);
                this.setScene(contestantScene);
                contestantScene.getStylesheets().add("style.css");
                this.show();
	}
        
	// Fetch and load profile pictures from the file
	public void profilePhoto() {
		try {
			inputBlank = new FileInputStream("./data/contestant_pics/blank.jpg");
			imageBlank = new Image(inputBlank);
			
			inputHamza = new FileInputStream("./data/contestant_pics/Hamza.jpg");
			imageHamza = new Image(inputHamza);
                        
                        inputHamza2 = new FileInputStream("./data/contestant_pics/Hamza2.jpg");
			imageHamza2 = new Image(inputHamza2);
                        
                        inputHamza3 = new FileInputStream("./data/contestant_pics/Hamza3.jpg");
			imageHamza3 = new Image(inputHamza3);
			
			inputKish = new FileInputStream("./data/contestant_pics/kishendran.jpg");
			imageKish = new Image(inputKish);
                        
                        inputKish2 = new FileInputStream("./data/contestant_pics/kish2.jpg");
			imageKish2 = new Image(inputKish2);
                        
                        inputKish3 = new FileInputStream("./data/contestant_pics/kish3.jpg");
			imageKish3 = new Image(inputKish3);
			
			inputCherng = new FileInputStream("./data/contestant_pics/yii cherng.jpg");
			imageCherng = new Image(inputCherng);
                        
                        inputCherng2 = new FileInputStream("./data/contestant_pics/cherng2.jpg");
			imageCherng2 = new Image(inputCherng2);
                        
                        inputCherng3 = new FileInputStream("./data/contestant_pics/cherng3.jpg");
			imageCherng3 = new Image(inputCherng3);
			
			inputSara = new FileInputStream("./data/contestant_pics/sara.jpg");
			imageSara = new Image(inputSara);	
                        
                        inputSara2 = new FileInputStream("./data/contestant_pics/sara2.jpg");
			imageSara2= new Image(inputSara2);
                        
                        inputSara3 = new FileInputStream("./data/contestant_pics/sara3.jpg");
			imageSara3 = new Image(inputSara3);
			
			inputFirdaus = new FileInputStream("./data/contestant_pics/firdaus.jpg");
			imageFirdaus = new Image(inputFirdaus);		
                        
                        inputFirdaus2 = new FileInputStream("./data/contestant_pics/firdaus2.jpg");
			imageFirdaus2 = new Image(inputFirdaus2);	
                        
                        inputFirdaus3 = new FileInputStream("./data/contestant_pics/firdaus3.jpg");
			imageFirdaus3 = new Image(inputFirdaus3);	
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}
	
        public boolean verifyCountry() { //To verify if the country entered by contestant is correct
                boolean c = false;
                try {
                    Scanner sfile = new Scanner(contProfile);
                    int totalCont = Integer.parseInt(sfile.nextLine()); //To get the total number of contestant 
                    for (int i = 0; i < totalCont; i++) { //To loop based on the total number of contestant
                        String aline = sfile.nextLine();
                        Scanner sline = new Scanner(aline);
                        sline.useDelimiter(",");
                        String n = sline.next();
                        if (comboBoxName.getValue().equals(n)) { //If the name selected is equal to the one in the file
                                sline.next(); 
                                String country = sline.next(); //To get the country
                                if (comboBoxCountry.getValue().equals(country)){ //If the country selected for the contestant is equal
                                        c = true; //c is set to true
                                }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("file not found");
                }
                return c;
        }
        
        public boolean verifyPass() { //To verify if the password entered by contestant is correct
            boolean v = false;
            try {
                Scanner sfile = new Scanner(contProfile);
                int totalCont = Integer.parseInt(sfile.nextLine()); //To get the total number of contestant 
                for (int i = 0; i < totalCont; i++) { //To loop based on the total number of contestant
                    String aline = sfile.nextLine();
                    Scanner sline = new Scanner(aline);
                    sline.useDelimiter(",");
                    String n = sline.next();
                    if (comboBoxName.getValue().equals(n)) { //If the name selected is equal to the one in the file
                        String p = sline.next(); //To get the password
                        if (fieldPass.getText().equals(p)){ //If the password entered for the contestant is equal
                            v = true; //v is set to true
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
            }
            return v;
        }
        
	// Scanning for names in results.txt file
	public void checkNames() {
		Scanner rFile;
                int totQues = 25;
                String name;
                try {
                rFile = new Scanner(contAnsfile);
                        while (rFile.hasNextLine()) {
                                String aLine = rFile.nextLine();
                                Scanner sline = new Scanner(aLine);
                                sline.useDelimiter(":");
                                while (sline.hasNext()) {
                                        for (int i = 0; i < totQues; i++) {
                                                sline.next().charAt(0); //To skip through the answer in result.txt
                                        }
                                        name = sline.next(); //To get name at the last delimeter
                                        listNames.add(name); //Add name to array
                                }
                        }
                } catch (Exception e) {
                        System.out.println(e);
                }
	}

	// Method to get the country selected
	public String getCountry() {
		return comboBoxCountry.getValue();
	}

	// Method to get the Name
	public String getName() {
		return comboBoxName.getValue();
	}

	// Method to get the password
	public String getPass() {
		return fieldPass.getText();
	}
}