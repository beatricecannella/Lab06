package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Citta> getAllCitta(){
		String sql = "SELECT DISTINCT Localita "
				+ "FROM situazione";
		

		List<Citta> allCitta = new ArrayList<Citta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String localita = rs.getString("Localita");
				Citta c = new Citta(localita);
				 c.setRilevamentiPerCitta(this.getRilevamentiCitta(c.getNome()));
				allCitta.add(c);
			}

			conn.close();
			return allCitta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getRilevamentiCitta(String localita){
		
		String sql = "SELECT  Localita, Data, Umidita "
				+ "FROM situazione "
				+ "WHERE localita = ? "
				+ "ORDER BY data ASC";
		

		List<Rilevamento> rilevamentiPerCitta = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			ResultSet rs = st.executeQuery();
			

			while (rs.next()) {
				
				String citta = rs.getString("Localita");
				LocalDate data = rs.getDate("Data").toLocalDate();
				int umidita = rs.getInt("Umidita");
				
				Rilevamento r = new Rilevamento (citta, data, umidita);
				rilevamentiPerCitta.add(r);
			}

			conn.close();
			return rilevamentiPerCitta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
	
	
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		String sql = "SELECT  Localita, Data, Umidita "
				+ "FROM situazione "
				+ "WHERE localita = ?  AND MONTH(Data)=? "
				+ "ORDER BY data ASC";
		

		List<Rilevamento> rilevamentiPerCitta = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();
			

			while (rs.next()) {
				
				String citta = rs.getString("Localita");
				LocalDate data = rs.getDate("Data").toLocalDate();
				int umidita = rs.getInt("Umidita");
				
				Rilevamento r = new Rilevamento (citta, data, umidita);
				rilevamentiPerCitta.add(r);
			}

			conn.close();
			return rilevamentiPerCitta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
