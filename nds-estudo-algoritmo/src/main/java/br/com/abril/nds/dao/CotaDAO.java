package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.Cota;

public class CotaDAO {

	public List<Cota> getCotas() {
		List<Cota> cotas = new ArrayList<Cota>();
		try {
			PreparedStatement psmt = Conexao.getConexao().prepareStatement("SELECT ID FROM COTA");
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				Cota cota = new Cota();
				cota.setId(rs.getLong("ID"));
				
				cotas.add(cota);
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as cotas");
			ex.printStackTrace();
		}
		return cotas;
	}
}
