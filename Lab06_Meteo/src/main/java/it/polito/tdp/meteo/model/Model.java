package it.polito.tdp.meteo.model;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	MeteoDAO meteoDao;
	Map<Citta, Double> umiditaPerCitta;
	//Citta c; 
	List<Citta> sequenza ;
	LinkedList<Citta> cittaAmmissibile = new LinkedList<Citta>();
	
	

	double costoT = 0.0;
	
	public Model() {
		 meteoDao = new MeteoDAO();
		 umiditaPerCitta = new HashMap<Citta, Double>();
		 
			
	}

	// of course you can change the String output with what you think works best
	public Map<Citta, Double> getUmiditaMedia(int mese) {
		List<Rilevamento> rilevamenti =	this.meteoDao.getAllRilevamenti();		
		
		for(Citta c: meteoDao.getAllCitta()) {
			int contatore = 0;
			double umidita = 0;
			double umiditaMedia = 0;
			for(Rilevamento r : c.getRilevamentiPerCitta()) {
			if(r.getData().getMonthValue() == mese) {
				contatore++;
				umidita += r.getUmidita();
			}
			}
			umiditaMedia = umidita / contatore;
			c.setUmiditaMedia(umiditaMedia);
			this.umiditaPerCitta.put(c, umiditaMedia);
		}
		
		return umiditaPerCitta;
	}
	
	
	
	// of course you can change the String output with what you think works best
public List<Citta> trovaSequenza(int mese) {
		 sequenza = null;
		 List<Citta> parziale = new ArrayList<Citta>(); //parziale
		 for(Citta c: this.meteoDao.getAllCitta()) {
				c.setRilevamenti(this.meteoDao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
			}
		 cerca(parziale, 0); //lancio la ricorsione
		 return sequenza;
	}
	
private void cerca(List<Citta> parziale, int livello) {
	
	if(livello == NUMERO_GIORNI_TOTALI) {
	
		costoT = this.calcolaCosto(parziale);
		
		if(sequenza == null || costoT<this.calcolaCosto(sequenza)) {
			sequenza = new ArrayList<Citta>(parziale);
		}
		
	return;
	}
	
	else {	
		

		for(Citta cittaProva: meteoDao.getAllCitta()) {
			if(sequenzaAmmissibile(cittaProva, parziale) == true) {
			parziale.add(cittaProva);
			this.cerca(parziale, livello+1);
			parziale.remove(parziale.size()-1);
			}
		
		}

		}
	}

	private boolean sequenzaAmmissibile(Citta c, List<Citta> parziale) {
	int conta =0; 
	
	for(Citta ultima: parziale) {
		if(ultima.equals(c)) {	
			conta++;
			}
		}
		if(parziale.size()==0) //ogni citta aggiunta va bene
			return true;
		
		if(conta >= NUMERO_GIORNI_CITTA_MAX) 
			return false;
		
		if(parziale.size()<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {

			if(parziale.get(parziale.size()-1).equals(c))
				return true;
			else
				return false;
			
		}
		
		
		
		
		
		 //se ho già aggiunto una citta in parziale, devo controllare che cittaProva sia uguale alla precedente
	/*	if(parziale.size()==1) { //ho solo un elemento in parziale
				if(parziale.get(parziale.size()-1).equals(c)) {
					 return true;
					}
		}
			
		if(parziale.size()==2) { //ho due citta in parziale
			if(parziale.get(parziale.size()-1).equals(c) && parziale.get(parziale.size()-2).equals(c)) {
			//	c.setGiorniConsecutivi(gCons);
				//c.increaseCounter();		
				return true;
				}
			}*/
		
		if(parziale.get(parziale.size()-1).equals(c)) { //se ho piu di 2 citta, posso rimanere altri giorni (tanto il controllo sui 
														// giorni massimi (6) è sopra)
			return true; 
			
		}
	
		//se voglio cambiare citta... devo controllare di essere stato 3 giorni nella stessa citta
		for(int i = 0; i< NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN-1; i++) {
			if(!parziale.get(parziale.size()-(i+1)).equals(parziale.get(parziale.size()-(i+2)))) {
				return false;
		}
		}
		return true;
	}
	
	
	
	
	private double calcolaCosto(List<Citta> parziale) {
		double costoTot = 0.0;
		for(int gg=1; gg<=NUMERO_GIORNI_TOTALI; gg++) {
			Citta cc = parziale.get(gg-1);
			double umidita = cc.getRilevamentiPerCitta().get(gg-1).getUmidita();
			costoTot += umidita; 
		}
		
		//costo +=100 se cambio citta
		
		for(int gg=2; gg<=NUMERO_GIORNI_TOTALI; gg++) {
			Citta c1 = parziale.get(gg-1);
			Citta c2 = parziale.get(gg-2);
			if(!c1.equals(c2)) {
			costoTot += COST;
			}
		}
		
		
		
		return costoTot;
	}
	
	
	public Map<Citta, List<Rilevamento>> elencoCitta(){
		Map<Citta, List<Rilevamento>> elenco = new HashMap<Citta, List<Rilevamento>>();
		
		for(Citta c: this.meteoDao.getAllCitta()) {
			elenco.put(c, c.getRilevamentiPerCitta());
			}
	return elenco;
}
}