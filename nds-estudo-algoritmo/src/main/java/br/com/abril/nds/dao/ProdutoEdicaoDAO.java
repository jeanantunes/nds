package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class ProdutoEdicaoDAO {

	public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota) {
		return getEdicaoRecebidas(cota, null);
	}
	
	 public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota, ProdutoEdicao produto) {
		List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
		try {
			PreparedStatement psmt = Conexao.getConexao().prepareStatement("" +
					"SELECT P.NOME, PE.NUMERO_EDICAO, EPC.QTDE_RECEBIDA, EPC.QTDE_DEVOLVIDA, PE.PARCIAL, PE.PACOTE_PADRAO, PE.PESO \n" +
					"     , (CASE WHEN EXISTS(SELECT '.' \n" +
					"                           FROM TIPO_PRODUTO TP \n" +
					"                          WHERE TP.ID = P.TIPO_PRODUTO_ID AND TP.GRUPO_PRODUTO = 'COLECIONAVEL') THEN 1 ELSE 0 END) IS_COLECAO \n" +
					"  FROM ESTOQUE_PRODUTO_COTA EPC \n" +
					"  JOIN PRODUTO_EDICAO PE ON PE.ID = EPC.PRODUTO_EDICAO_ID \n" +
					"  JOIN PRODUTO P ON P.ID = PE.PRODUTO_ID \n" +
					" WHERE EPC.COTA_ID = ? " + produto == null ? "" : "AND PE.ID = ?");
			psmt.setLong(1, cota.getId());
			if (produto != null) {
				psmt.setLong(2, produto.getId());
			}
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				ProdutoEdicao edicao = new ProdutoEdicao();
				edicao.setNome(rs.getString("NOME"));
				edicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
				edicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
				edicao.setVenda(edicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA")));
				edicao.setParcial(rs.getInt("PARCIAL") == 1);
				edicao.setPacotePadrao(rs.getInt("PACOTE_PADRAO"));
				edicao.setPeso(rs.getInt("PESO"));
				edicao.setColecao(rs.getInt("IS_COLECAO") == 1);
				
				edicoes.add(edicao);
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota");
		}
		return edicoes;
	}
}
