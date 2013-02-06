package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;

public class EstoqueProdutoCotaDAO {

	public List<EstoqueProdutoCota> getByCotaId(Long cotaId) {
		
		List<EstoqueProdutoCota> estoqueProdutoCotas = new ArrayList<EstoqueProdutoCota>();
		
		try {

			PreparedStatement psmt = Conexao
					.getConexao()
					.prepareStatement(
							"SELECT * FROM ESTOQUE_PRODUTO_COTA WHERE ESTOQUE_PRODUTO_COTA.COTA_ID = ? ORDER BY ID");
			psmt.setLong(1, cotaId);

			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
				estoqueProdutoCota.setId(rs.getLong("ID"));
				
				Cota cota = new Cota();
				cota.setId(rs.getLong("COTA_ID"));
				
				ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
				estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
				
				estoqueProdutoCota.setCota(cota);
				estoqueProdutoCota.setQuantidadeDevolvida(rs.getBigDecimal("QTDE_DEVOLVIDA"));
				estoqueProdutoCota.setQuantidadeRecebida(rs.getBigDecimal("QTDE_RECEBIDA"));
				estoqueProdutoCota.setVersao(rs.getInt("VERSAO"));
				
				estoqueProdutoCotas.add(estoqueProdutoCota);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return estoqueProdutoCotas;
	}
}
