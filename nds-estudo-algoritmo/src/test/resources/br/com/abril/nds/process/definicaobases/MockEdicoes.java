package br.com.abril.nds.process.definicaobases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dao.Conexao;
import br.com.abril.nds.model.ProdutoEdicao;

public class MockEdicoes {

    public static List<ProdutoEdicao> getEdicoesRandom() {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	try {
	    PreparedStatement psmt = Conexao
		    .getConexao()
		    .prepareStatement(
			    "select PRODUTO_EDICAO_ID from ( "
				    + "select PRODUTO_EDICAO_ID, count(*) as qtd, rand() as rnd from estoque_produto_cota "
				    + "group by PRODUTO_EDICAO_ID "
				    + "having count(*) > 100 "
				    + "order by rnd desc " 
				    + "limit 10 "
				    + ") as subquery");
	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		edicoes.add(edicao);
	    }

	} catch (ClassNotFoundException | SQLException e) {
	    e.printStackTrace();
	}

	return edicoes;
    }

}
