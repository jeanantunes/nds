package br.com.abril.nds.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicaoBase;

@Repository
public class RankingSegmentoDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("#{query_estudo.queryCotasOrdenadasPorSegmentoSemEdicaoBase}")
    private String queryCotasOrdenadasPorSegmentoSemEdicaoBase;

    @Value("#{query_estudo.queryCotasOrdenadasMaiorMenor}")
    private String queryCotasOrdenadasMaiorMenor;

    /*
     * final String SQL_RETURN_ULTIMO_ID_RANKING_GERADO = "SELECT max(id) from ranking_segmento_gerados";
     * 
     * public Long getUltimoIDRankingSegmentoGerado(){
     * 
     * Long d = null; try { StringBuffer sb = new StringBuffer(SQL_RETURN_ULTIMO_ID_RANKING_GERADO);
     * 
     * PreparedStatement psmt = Conexao.getConexao().prepareStatement( sb.toString()); ResultSet rs = psmt.executeQuery(); while
     * (rs.next()) { d = rs.getLong(1); } } catch (Exception ex) { System.out.println("Ocorreu um erro ao tentar consultar");
     * ex.printStackTrace(); }
     * 
     * return d; }
     */
    private String sqlRankingSegmentoEdicoesBase(int qtde) {

	StringBuilder SQL_RANKING_SEGMENTO_EDICOES_BASE = new StringBuilder().append("(select cota_id,produto_edicao_id from ranking_segmento rs ")
		.append(" where rs.RANKING_SEGMENTO_GERADOS_ID=(select max(rsg.id) from ranking_segmento_gerados rsg) ")
		.append(" and rs.produto_edicao_id in (:IDS_BASE)) union ")
		.append("(select COTA_ID, produto_edicao_id from  movimento_estoque_cota mec where mec.tipo_movimento_id=21 ")
		.append(" and mec.produto_edicao_id in (:IDS_BASE) ");

	if (qtde >= 3) {
	    SQL_RANKING_SEGMENTO_EDICOES_BASE.append(" and mec.qtde >= ").append(qtde).append(")");

	} else {
	    SQL_RANKING_SEGMENTO_EDICOES_BASE.append(" and and mec.qtde = ").append(qtde).append(")");

	}
	return SQL_RANKING_SEGMENTO_EDICOES_BASE.toString();

    }

    public List<Long> getCotasOrdenadaPorSegmentoEdicaoAberta(List<Cota> cotaList, List<Long> idEdicoesBase) {

	StringBuilder sb = new StringBuilder();
	sb.append(" SELECT distinct COTA_ID FROM RANKING_SEGMENTO WHERE RANKING_SEGMENTO_GERADOS_ID = (select max(id) from ranking_segmento_gerados) and COTA_ID in (:COTA_IDS) ");

	List<Long> retorno = new ArrayList<>();
	List<Long> idList = new ArrayList<>();
	for (Cota c : cotaList) {
	    idList.add(c.getId());
	}

	try {
	    Map<String, Object> params = new HashMap<>();
	    params.put("COTA_IDS", idList);
	    SqlRowSet rs = jdbcTemplate.queryForRowSet(sb.toString(), params);

	    while (rs.next()) {
		retorno.add(rs.getLong(1));
	    }
	} catch (Exception ex) {
	    System.out.println("Ocorreu um erro ao tentar consultar as cotas");
	    ex.printStackTrace();
	}
	return retorno;
    }

    public List<Long> getCotasOrdenadaPorSegmentoSemEdicaoBase(List<Cota> cotaList, List<ProdutoEdicaoBase> edicoesBase) {

	List<Long> idList = new ArrayList<>();
	List<Long> idListEdicoesBase = new ArrayList<>();
	for (Cota c : cotaList) {
	    idList.add(c.getId());
	}
	for (ProdutoEdicaoBase c : edicoesBase) {
	    idListEdicoesBase.add(c.getId());
	}
	Map<String, Object> params = new HashMap<>();
	params.put("IDS_COTA", idList);
	params.put("IDS_PRODUTO_EDICAO", idListEdicoesBase);
	SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotasOrdenadasPorSegmentoSemEdicaoBase, params);

	List<Long> retorno = new ArrayList<>();
	while (rs.next()) {
	    retorno.add(rs.getLong(1));
	}
	return retorno;
    }

    public List<Long> getCotasOrdenadaPorSegmento(List<Cota> cList, List<ProdutoEdicaoBase> edicoesBase, int qtde) {

	List<Long> listaProdutoEdicao = new ArrayList<>();
	for (ProdutoEdicaoBase edicao : edicoesBase) {
	    listaProdutoEdicao.add(edicao.getId());
	}
	Map<String, Object> params = new HashMap<>();
	params.put("IDS_BASE", listaProdutoEdicao);

	SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlRankingSegmentoEdicoesBase(qtde), params);
	List<Long> retorno = new ArrayList<>();
	while (rs.next()) {
	    retorno.add(rs.getLong(1));
	}
	return retorno;
    }

    public List<Long> getCotasOrdenadasMaiorMenor(List<Cota> cotaList, ProdutoEdicaoBase produtoEdicaoBase) {
	List<Long> listaCotas = new ArrayList<>();
	for (Cota cota : cotaList) {
	    listaCotas.add(cota.getId());
	}

	Map<String, Object> params = new HashMap<>();
	params.put("PRODUTO_EDICAO_ID", produtoEdicaoBase.getId());
	params.put("IDS_COTA", listaCotas);
	SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotasOrdenadasMaiorMenor, params);

	List<Long> retorno = new ArrayList<>();
	while (rs.next()) {
	    retorno.add(rs.getLong(1));
	}
	return retorno;
    }
}
