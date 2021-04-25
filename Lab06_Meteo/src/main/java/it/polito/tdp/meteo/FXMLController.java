/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.util.List;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import it.polito.tdp.meteo.model.Citta;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	Model model = new Model();
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	txtResult.clear();
    	String elenco = "";
    
    	if(this.boxMese.getValue()== null) {
    		this.txtResult.setText("ERRORE: Devi selezionare un mese!");
    	}
    	else {
    		int mese = this.boxMese.getValue();
    		List<Citta> s = this.model.trovaSequenza(mese);
    	for(Citta c: s) {
    		elenco += c + "\n";
    	}
    		this.txtResult.setText(elenco);
    	}
    		
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {

    	String elenco = "";
        txtResult.clear();
    	
    	
    	if(this.boxMese.getValue()== null) {
    		this.txtResult.setText("ERRORE: Devi selezionare un mese!");
    	}
    	else {
    		int mese = this.boxMese.getValue();
    		Map<Citta, Double> umiditaCitta = model.getUmiditaMedia(mese);
    	for (Citta c: umiditaCitta.keySet()) {
    		double umidita = c.getUmiditaMedia();
    		elenco += c.getNome() + " " + umidita + "\n";
    	
    	this.txtResult.setText(elenco);
    }}
    }
    
    public void setModel(Model model) {
    this.model= model;	
    this.boxMese.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
}

