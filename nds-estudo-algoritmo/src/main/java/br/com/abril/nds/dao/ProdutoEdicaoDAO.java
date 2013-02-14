package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class ProdutoEdicaoDAO {

	public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota) {
		List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
		try {
			PreparedStatement psmt = Conexao.getConexao().prepareStatement("" +
					"SELECT PE.NUMERO_EDICAO, EPC.QTDE_RECEBIDA, EPC.QTDE_DEVOLVIDA, PE.PARCIAL, PE.PACOTE_PADRAO, PE.PESO " +
					"  FROM ESTOQUE_PRODUTO_COTA EPC " +
					"  JOIN PRODUTO_EDICAO PE ON PE.ID = EPC.PRODUTO_EDICAO_ID " +
					" WHERE EPC.COTA_ID = ? ");
			psmt.setLong(1, cota.getId());
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				ProdutoEdicao edicao = new ProdutoEdicao();
				edicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
				edicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
				edicao.setVenda(edicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA")));
				edicao.setParcial(rs.getInt("PARCIAL") == 1);
				edicao.setPacotePadrao(rs.getInt("PACOTE_PADRAO"));
				edicao.setPeso(rs.getInt("PESO"));
				
				edicoes.add(edicao);
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota");
		}
		return edicoes;
	}
	
	public int getQtdeVezesReenviadas(Cota cota,ProdutoEdicao produtoEdicao) {
		try {
			PreparedStatement psmt = Conexao.getConexao().prepareStatement("" +
					"SELECT count(EPC.id) " +
					"  FROM ESTOQUE_PRODUTO_COTA EPC " +
					"  JOIN PRODUTO_EDICAO PE ON PE.ID = EPC.PRODUTO_EDICAO_ID " +
					" WHERE EPC.COTA_ID = ? AND " +
					" PE.ID = ?" +
					" and PE.parcial=1" +
					"");
			psmt.setLong(1, cota.getId());
			psmt.setLong(2, produtoEdicao.getId());
			
			ResultSet rs = psmt.executeQuery();
			rs.next();
			return rs.getInt(0);
			
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota");
		}
		
		return 0;
	}
}
