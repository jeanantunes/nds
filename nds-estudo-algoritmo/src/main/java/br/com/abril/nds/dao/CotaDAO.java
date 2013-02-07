package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

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
	
	public List<ProdutoEdicao> getEdicaoBase(Cota cota) {
		List<ProdutoEdicao> edicoesBase = new ArrayList<ProdutoEdicao>();
		try {
			PreparedStatement psmt = Conexao.getConexao().prepareStatement("SELECT QTDE_RECEBIDA, QTDE_DEVOLVIDA,  FROM ESTOQUE_PRODUTO_COTA");
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				ProdutoEdicao edicaoBase = new ProdutoEdicao();
				edicaoBase.setId(rs.getLong("ID"));
				
				edicoesBase.add(edicaoBase);
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as edições base");
		}
		return edicoesBase;
	}
}
