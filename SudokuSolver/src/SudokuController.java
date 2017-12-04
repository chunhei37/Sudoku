import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javafx.stage.Stage;

public class SudokuController {
	
	private int[][] correctPuzzle;
    private int[][] gamePuzzle;
    private int[][] sandBoxPuzzle;
    private TextField[] cell;
    
    @FXML private Line line1;
    @FXML private Line line2;
    @FXML private Line line3;
    @FXML private Line line4;
    
    @FXML private GridPane mainGrid;
    
    @FXML private AnchorPane win_msg;
    @FXML private AnchorPane error_msg;
    
    @FXML private Button check_btn;
    @FXML private Button solveAll_btn;
    @FXML private Button clear_btn;
    
    
    @FXML
    public void initialize() {
    	PuzzleGenerator pg = new PuzzleGenerator();
        gamePuzzle = pg.copyArray(pg.gamePuzzle);
        sandBoxPuzzle = pg.copyArray(pg.gamePuzzle);
        correctPuzzle = pg.copyArray(pg.correctPuzzle);
    	cell = new TextField[pg.blanks];
    	System.out.println("PG count: " + pg.blanks);
    	createCell();
		System.out.println();
//		for(int i = 0; i < 9 ; i++) {
//			for(int y = 0; y < 9; y++) {
//				System.out.print(pg.correctPuzzle[i][y] + " ");
//				
//			}
//			System.out.println();
//		}
    }
    
    //Filling the gridpane with number and text field
    private void createCell() {

    	int count = 0;
    	for(int i = 0; i < gamePuzzle.length; i++) {
    		for(int y = 0; y < gamePuzzle.length; y++) {
    			if(gamePuzzle[i][y] != 0) {
        			Text num = new Text(gamePuzzle[i][y] + "");
        			num.setTextAlignment(TextAlignment.CENTER);
        			num.setFont(Font.font("Verdana", FontWeight.THIN, 30));
        			GridPane.setConstraints(num, y, i);
        			GridPane.setHalignment(num, HPos.CENTER);
        			GridPane.setValignment(num, VPos.CENTER);
            	    FadeTransition fade = new FadeTransition(Duration.millis(1500), num);
            	    fade.setFromValue(0);
            	    fade.setToValue(1);
            	    fade.play();
        	    	mainGrid.getChildren().addAll(num);
    			} else {
    				cell[count] = new TextField();
    				cell[count].setFont(Font.font("Verdana", FontWeight.THIN, 20));
    				GridPane.setConstraints(cell[count], y, i);
    				addTextLimiter(cell[count], 1);
    				mainGrid.getChildren().addAll(cell[count]);
    				count++;
    			}
    		}
    	}
    	for(TextField i : cell) {
    		i.toBack();
    	}
    	line1.toFront();
    	line2.toFront();
    	line3.toFront();
    	line4.toFront();
    }
    
    private void errorMsg() {
    	error_msg.toFront();
    	error_msg.setVisible(true);
    	PauseTransition pause = new PauseTransition(Duration.seconds(1));
    	pause.setOnFinished(event -> {
    		error_msg.toBack();
    		error_msg.setVisible(false);
    	}
    	);
    	pause.play();
    }

    //check answer
    public boolean checkPuzzle() {
    	boolean flag = true;
    	for(int i = 0; i < 9; i++) {
    		for(int y = 0; y < 9; y++) {
				if(sandBoxPuzzle[i][y] == 0) {
					errorMsg();
					return false;
				} else {
					if(correctPuzzle[i][y] != sandBoxPuzzle[i][y]) {
						flag = false;
					}
				}
    		}
    	}
    	if(!flag) {
        	Solver solver = new Solver();
    		if(!solver.checkSolved(sandBoxPuzzle)) {

    			return false;
    		}	
    	}
    	win_msg.toFront();
    	win_msg.setVisible(true);
    	PauseTransition pause = new PauseTransition(Duration.seconds(1));
    	pause.setOnFinished(event -> {
    		win_msg.toBack();
    		win_msg.setVisible(false);
    	}
    	);
    	pause.play();
    	return true;
    }
    
    //clear all inputed text field
    public void clear() {
    	for(int i = 0; i < cell.length; i++) {
    		cell[i].clear();
    	}
    }
    
    private void loadGame() {
    	solvePuzzle();
    	Node node = mainGrid.getChildren().get(0);
    	mainGrid.getChildren().removeAll(cell);
    	mainGrid.getChildren().clear();
    	mainGrid.getChildren().add(0, node);
    	createCell();
    	int count = 0;
    	for(int i = 0; i < 9; i++) {
    		for(int y = 0; y < 9; y++) {
    			if(gamePuzzle[i][y] == 0) {
    				if(sandBoxPuzzle[i][y] != 0) {
    					cell[count].setText(sandBoxPuzzle[i][y] + "");
    				}
    				count++;
    			}
    		}
    	}
    	solveAll_btn.setDisable(false);
    	clear_btn.setDisable(false);
    }

    //create a new puzzle
    public void newGame() {
    	solvePuzzle();
    	Node node = mainGrid.getChildren().get(0);
    	mainGrid.getChildren().clear();
    	mainGrid.getChildren().add(0, node);
    	initialize();
    	solveAll_btn.setDisable(false);
    	clear_btn.setDisable(false);
    	check_btn.setDisable(false);
    }
    
    //Limit user can only input 1 digit
    private void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
            	int row = GridPane.getRowIndex(tf);
            	int col = GridPane.getColumnIndex(tf);
            	System.out.println("Cell: [ "+ row + ", " + col +"]");
                try {
                	int num = Integer.parseInt(tf.getText());
                	sandBoxPuzzle[row][col] = num;
                	if(num == 0) {
                		tf.setText("");
                	}
                	//System.out.println("SandBoxPuzzle: [ "+ row + ", " + col +"] Updated!");
                } catch(NumberFormatException e) {
                   
                	tf.setText("");
                }
                if (tf.getText().length() > maxLength) {
                    tf.setText(tf.getText().substring(0, maxLength));
                }
                if(tf.getText().length() == 0) {
                    sandBoxPuzzle[row][col] = 0;
                    //System.out.println("SandBoxPuzzle: [ "+ row + ", " + col +"] Updated! Stored 0");
                }

                printArray(sandBoxPuzzle);
            }
        });
    }
    
    private void printArray(int[][] obj) {
    	System.out.println();
    	for(int i = 0; i < obj.length; i++) {
    		for(int y = 0; y < obj.length; y++) {
    			System.out.print(obj[i][y] + " ");
    		}
    		System.out.println();
    	}
    }
    
    //Solve the puzzle
    public void solvePuzzle() {
    	mainGrid.getChildren().removeAll(cell);
    	for(int i = 0; i < correctPuzzle.length; i++) {
    		for(int y = 0; y < correctPuzzle.length; y++) {
    			if(gamePuzzle[i][y] == 0) {
            		Text num = new Text(correctPuzzle[i][y] + "");
            		num.setTextAlignment(TextAlignment.CENTER);
            		num.setFont(Font.font("Verdana", FontWeight.THIN, 30));
            		num.setFill(Color.CHARTREUSE.darker().invert());
            		GridPane.setConstraints(num, y, i);
            		GridPane.setHalignment(num, HPos.CENTER);
            		GridPane.setValignment(num, VPos.CENTER);
            	    FadeTransition fade = new FadeTransition(Duration.millis(1500), num);
            	    fade.setFromValue(0);
            	    fade.setToValue(1);
            	    fade.play();
            		mainGrid.getChildren().addAll(num);
    			}
    		}
    	}
    	solveAll_btn.setDisable(true);
    	clear_btn.setDisable(true);
    	check_btn.setDisable(true);
    }
    
    //Save file inside game dir
    public void save() throws FileNotFoundException, UnsupportedEncodingException  {
    	String path = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()) + ".sdg";
    	saveFile(path);
    }
    
    //create save file
    private void saveFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
    	PrintWriter writer = new PrintWriter(path, "UTF-8");
		for(int i = 0; i < 9 ; i++) {
			for(int y = 0; y < 9; y++) {
				writer.write(gamePuzzle[i][y] + " ");
			}
			writer.println();
		}
		writer.println();
		for(int i = 0; i < 9 ; i++) {
			for(int y = 0; y < 9; y++) {
				writer.write(sandBoxPuzzle[i][y] + " ");
			}
			writer.println();
		}
    	writer.close();	
    }
    
    //allow user pick where to save 
    public void saveAs() {
    	FileChooser fileChooser = new FileChooser();
    	String filename = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()) + ".sdg";
    	fileChooser.setTitle("Save file");
    	fileChooser.setInitialFileName(filename);
    	fileChooser.getExtensionFilters().addAll(
    		    new FileChooser.ExtensionFilter("SDG", "*.sdg"));
    	File savedFile = fileChooser.showSaveDialog(null);
    	if (savedFile != null) {
    	    try {
    	    	saveFile(savedFile.toString());
    	    }
    	    catch(IOException e) {
    	        e.printStackTrace();
    	        return;
    	    }
    	}
    }
    
    public void open() throws IOException {
    	int count = 0;
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile = fileChooser.showOpenDialog(null);

    	if (selectedFile != null) {
    	    // pass the path to the file as a parameter
    	    File file = new File(selectedFile.toString());
    	    Scanner sc = new Scanner(file);
    	    for(int i = 0; i < 9; i++) {
    	    	for(int y = 0; y < 9; y++) {
    	    		int temp =  Integer.parseInt(sc.next());
    	    		if(temp == 0) {
    	    			count++;
    	    		}
    	    		gamePuzzle[i][y] = temp;
    	    	}	
    	    }
    	    for(int i = 0; i < 9; i++) {
    	    	for(int y = 0; y < 9; y++) {
    	    		sandBoxPuzzle[i][y] = Integer.parseInt(sc.next());
    	    	}	
    	    }
    	    cell = new TextField[count];
    	    System.out.println("Cell Count: " + count);
    	    sc.close();
    	    loadGame();
    	}
    }

    
    public void exit() {
        Platform.exit();
        System.exit(0);
    }
    
}
