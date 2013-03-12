package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;

@Repository
public class EstoqueProdutoCotaDAO {

    @Autowired
    private DataSource dataSource;

    public List<EstoqueProdutoCota> getByCotaIdProdutoEdicaoId(Cota cota, List<ProdutoEdicao> listProdutoEdicao) {

	List<EstoqueProdutoCota> estoqueProdutoCotas = new ArrayList<EstoqueProdutoCota>();

	try {

	    StringBuilder query = new StringBuilder(
		    " SELECT EPC.*, PE.NUMERO_EDICAO, P.NOME, P.ID AS ID_PRODUTO, TP.GRUPO_PRODUTO FROM ESTOQUE_PRODUTO_COTA EPC ");
	    query.append(" INNER JOIN PRODUTO_EDICAO PE ON (PE.ID = EPC.PRODUTO_EDICAO_ID) ");
	    query.append(" INNER JOIN PRODUTO P ON (P.ID = PE.PRODUTO_ID) ");
	    query.append(" INNER JOIN TIPO_PRODUTO TP ON (TP.ID = P.TIPO_PRODUTO_ID) ");
	    query.append(" INNER JOIN COTA C ON (C.ID = EPC.COTA_ID) ");
	    query.append(" WHERE C.ID = ? ");

	    if (listProdutoEdicao != null && !listProdutoEdicao.isEmpty()) {

		query.append(" AND EPC.PRODUTO_EDICAO_ID IN ( ");

		int i = 0;
		while (i < listProdutoEdicao.size()) {

		    query.append(listProdutoEdicao.get(i).getId());
		    query.append(",");
		    i++;
		}

		query.delete(query.length() - 1, query.length());

		query.append(" ) ");

	    }

	    query.append(" ORDER BY EPC.PRODUTO_EDICAO_ID ");

	    PreparedStatement psmt = dataSource.getConnection().prepareStatement(query.toString());

	    psmt.setLong(1, cota.getId());

	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {

		EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
		estoqueProdutoCota.setId(rs.getLong("ID"));

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdProduto(rs.getLong("ID_PRODUTO"));
		produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		produtoEdicao.setColecao(rs.getString("GRUPO_PRODUTO").equalsIgnoreCase("COLECIONAVEL"));
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
