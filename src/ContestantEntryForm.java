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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ContestantEntryForm extends Stage {

    // declaring and initializing the variables 
    private File contProfile = new File("./data", "contestants.txt");
    private File rfile = new File("./data", "answers.txt");
    private TextField fieldName, fieldPass;
    private Boolean verified = false;
    private ChoiceBox<String> choicebox = new ChoiceBox<>();
    private VBox contestantVbox;
    private HBox contestantHbox;
    private Scene contestantScene;
    private Label labTitle,labCountry,labPass,labPassVerify,contName,space;
    private Button proceedBtn, btnResult;
    private FileInputStream inputHamza, inputKish, inputCherng, inputSara, inputFirdaus, inputHamza2, inputKish2, inputCherng2, inputSara2, inputFirdaus2, inputHamza3, inputKish3, inputCherng3, inputSara3, inputFirdaus3, inputBlank;
    private Image imageHamza, imageKish, imageCherng, imageSara, imageFirdaus,imageHamza2, imageKish2, imageCherng2, imageSara2, imageFirdaus2, imageHamza3, imageKish3, imageCherng3, imageSara3, imageFirdaus3, imageBlank;
    private ImageView imageCont, imageCont2, imageCont3; // imageCont = imageContestant
    private ResultForm winResult;
    ComboBox<String> comboBoxName;
    List<String> listNames = new ArrayList<String>();
    private IntegerProperty maxLength = new SimpleIntegerProperty(this, "maxLength", 7);
    private StringProperty restrict = new SimpleStringProperty(this, "restrict");
    
	public ContestantEntryForm() {
		profilePhoto();
		
		this.setTitle("Contestant Form");

		// Setting the label for the title MUKT
		labTitle = new Label("Miss Universe Knowledge Test");
		labTitle.setStyle("-fx-font-size: 25pt;-fx-font-weight: bold;-fx-font-family:serif;-fx-text-fill:#000000;");

		// Setting the label for select country option
		labCountry = new Label("Select your Country");
		labCountry.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#0000ff;");

		// Setting the label for entering the password
		labPass = new Label("Please enter your Password");
		labPass.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#0000ff;");

		// Setting the label for entering the username
		contName = new Label("Please enter your Name");
		contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#0000ff;");
		
		space = new Label("");

		// Textfield to input the name
		fieldName = new TextField();
                fieldName.setMaxWidth(325);

		// Textfield to input the password
		fieldPass = new TextField();
                fieldPass.setMaxWidth(325);
                fieldPass.setPromptText("03xxxxx");
                               
		
        fieldPass.textProperty().addListener(new ChangeListener<String>() {
        	private boolean ignore;
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fieldPass.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (ignore || newValue == null)
                    return;
                if (maxLength.get() > -1 && newValue.length() > maxLength.get()) {
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
		choicebox.getItems().addAll("Mozambique","Namibia","Montenegro","Morocco","Myanmar");
		choicebox.setValue("Mozambique");
                choicebox.setMaxWidth(325);

		// Button to start the test 
		proceedBtn = new Button("PROCEED TO TEST");
		proceedBtn.setOnAction(e -> {
			checkNames();
                        verified = verification();
                        if (listNames.contains(comboBoxName.getValue())) {
                            comboBoxName.setValue(" ");
                            contName.setText("This user has taken the test, please select another user");
                            contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
                        } 
                        else if (verified == false) {
                            contName.setText("Incorrect Password");
                            contName.setStyle("-fx-font-size: 14pt;-fx-font-family:serif;-fx-text-fill:#ff0000;");
                        }
                        else {
                              this.hide();                       
                        }
		});

                // Button to view the results
                btnResult = new Button("PROCEED TO RESULT");
		btnResult.setOnAction(e -> {
                        winResult = new ResultForm();
                        winResult.readName();
                        winResult.showStage();
                });
		
		//ComboBox for Name
		comboBoxName = new ComboBox<>();
                comboBoxName.setMaxWidth(325);
		comboBoxName.getItems().addAll("Hamza","Kishendran","Yii Cherng","Sara","Firdaus");
		comboBoxName.setEditable(true);
		comboBoxName.setPromptText("Name");
		
		// Check the name selected by user
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
		imageCont.setFitHeight(200);
		imageCont.setFitWidth(200);
                
                imageCont2 = new ImageView(imageBlank);
		imageCont2.setFitHeight(200);
		imageCont2.setFitWidth(200);
                
                imageCont3 = new ImageView(imageBlank);
		imageCont3.setFitHeight(200);
		imageCont3.setFitWidth(200);
                
                // Hbox for the 3 images to make the appear next to each other
                contestantHbox = new HBox(imageCont, imageCont2, imageCont3);
                contestantHbox.setSpacing(20);
                contestantHbox.setAlignment(Pos.CENTER);
		
                // Add contestant Vbox and Scene
                contestantVbox = new VBox(12);
                contestantVbox.setAlignment(Pos.CENTER);
                contestantVbox.getChildren().addAll(labTitle,contName, contestantHbox,comboBoxName,labPass,fieldPass,labCountry,choicebox,space,proceedBtn,btnResult);
                contestantScene = new Scene(contestantVbox, 900, 750);
                this.setScene(contestantScene);
                this.show();
	}
        
	// fetch and load profile pictures from the file
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
	
        public boolean verification() {
            boolean v = false;
            try {
                Scanner sfile = new Scanner(contProfile);
                int totalCont = Integer.parseInt(sfile.nextLine());
                for (int i = 0; i < totalCont; i++) {
                    String aline = sfile.nextLine();
                    Scanner sline = new Scanner(aline);
                    sline.useDelimiter(",");
                    String n = sline.next();
                    if (comboBoxName.getValue().equals(n)) {
                        String p = sline.next();
                        if (fieldPass.getText().equals(p)){
                            v = true;
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
        char cAns;
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
	
	// Method to get the country selected from choiceBox
	public void getChoice(ChoiceBox<String> choicebox) {
		String country = choicebox.getValue();
		System.out.println(country);
	}

	// Method to get the country selected
	public String getCountry() {
		return choicebox.getValue();
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