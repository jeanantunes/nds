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
	
	public Cota getAjustesReparteCota(Cota cota) {
		try {
			PreparedStatement psmt = Conexao.getConexao().prepareStatement("SELECT AJUSTE_APLICADO, FORMA_AJUSTE FROM AJUSTE_REPARTE WHERE COTA_ID = ?");
			psmt.setLong(1, cota.getId());
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("FORMA_AJUSTE").equals("vendaMedia")) {
					cota.setVendaMediaMaisN(rs.getBigDecimal("AJUSTE_APLICADO"));
				} else if (rs.getString("FORMA_AJUSTE").equals("encalheMaximo")) {
					cota.setPercentualEncalheMaximo(rs.getBigDecimal("AJUSTE_APLICADO"));
				}
				// TODO: ainda faltam carregar os parâmetros para ajuste de segmento e histórico (estarão gravados no banco respectivamente: 'segmento' e 'histórico')
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao consultar os parâmetros das cotas");
			ex.printStackTrace();
		}
		return cota;
	}
}
