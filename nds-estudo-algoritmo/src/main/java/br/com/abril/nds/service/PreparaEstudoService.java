package br.com.abril.nds.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.dao.Conexao;
import br.com.abril.nds.model.ProdutoEdicao;

public class PreparaEstudoService {
    
    private static final String SQL_COTA_VS_EDICAO = "select "
    + "    c.id as COTA_ID "
    + "    ,p.id as PRODUTO_ID "
    + "    ,pe.id as PRODUTO_EDICAO_ID "
    + "    ,pe.PARCIAL "
    + "    ,l.id as LANCAMENTO_ID "
    + "    ,l.STATUS "
    + "    ,tp.GRUPO_PRODUTO "
    + "    ,epc.QTDE_DEVOLVIDA "
    + "    ,epc.QTDE_RECEBIDA "
    + " from "
    + "    produto_edicao pe "
    + "    ,produto p "
    + "    ,lancamento l "
    + "    ,tipo_produto tp "
    + "    ,cota c "
    + "    ,estoque_produto_cota epc "
    + " where "
    + "    pe.id = ? "
    + "    and pe.PRODUTO_ID = p.id "
    + "    and pe.ID = l.PRODUTO_EDICAO_ID "
    + "    and tp.ID = p.TIPO_PRODUTO_ID "
    + "    and pe.ID = epc.PRODUTO_EDICAO_ID "
    + "    and c.ID = epc.COTA_ID ";

    private static final Logger log = LoggerFactory.getLogger(PreparaEstudoService.class);

    private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
    private static final String STATUS_FECHADO = "FECHADO";

    public List<ProdutoEdicao> listaEdicoesPorLancamento(ProdutoEdicao edicao) {
	List<ProdutoEdicao> edicoes = new ArrayList<>();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(
	     " select l.ID, l.PRODUTO_EDICAO_ID, l.STATUS, l.DATA_LCTO_PREVISTA, tp.GRUPO_PRODUTO, pe.PARCIAL " 
	   + " from lancamento l "
	   + " left join produto_edicao pe on pe.ID = l.PRODUTO_EDICAO_ID "
	   + " left outer join produto p on p.ID = pe.PRODUTO_ID "
	   + " left outer join tipo_produto tp on tp.ID = p.TIPO_PRODUTO_ID "
	   + " where pe.PRODUTO_ID = ( select PRODUTO_ID from produto_edicao where ID = ? ) "
//	   + " and l.DATA_LCTO_PREVISTA > date_sub(curdate(),INTERVAL 2 YEAR) "
	   + " order by l.DATA_LCTO_PREVISTA desc "
	   + " limit 16 "
	    );
	    
	    ps.setLong(1, edicao.getId());
	    ResultSet rs = ps.executeQuery();
	    while(rs.next()) {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdLancamento(rs.getLong("ID"));
		produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
		produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_PREVISTA"));
		produtoEdicao.setColecionavel(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
		produtoEdicao.setParcial(rs.getInt("PARCIAL") == 1);
		
		edicoes.add(produtoEdicao);
	    }
	} catch (ClassNotFoundException | SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return edicoes;
    }

    public List<ProdutoEdicao> getObjetoEdtudo(ProdutoEdicao edicao) {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_COTA_VS_EDICAO);
	    ps.setLong(1, edicao.getId());
	    ResultSet rs = ps.executeQuery();
	    while(rs.next()) {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdCota(rs.getLong("COTA_ID"));
		produtoEdicao.setIdProduto(rs.getLong("PRODUTO_ID"));
		produtoEdicao.setIdLancamento(rs.getLong("LANCAMENTO_ID"));
		produtoEdicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
		produtoEdicao.setVenda((produtoEdicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA"))));
		produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
		produtoEdicao.setColecionavel(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
		produtoEdicao.setParcial(rs.getInt("PARCIAL") == 1);
		
		edicoes.add(produtoEdicao);
	    }
	} catch (ClassNotFoundException | SQLException e) {
	    log.error("Error fetching objects from DB.", e);
	}
	return edicoes;
    }

    private boolean traduzColecionavel(String grupoProduto) {
	if(grupoProduto != null && grupoProduto.equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
	    return true;
	}
	return false;
    }

    private boolean traduzStatus(String status) {
	if(status != null && !status.equalsIgnoreCase(STATUS_FECHADO)) {
	    return true;
	}
	return false;
    }
}
