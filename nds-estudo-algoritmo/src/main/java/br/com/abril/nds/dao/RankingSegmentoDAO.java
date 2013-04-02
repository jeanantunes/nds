<<<<<<< HEAD
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

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class RankingSegmentoDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.queryCotasOrdenadasPorSegmentoSemEdicaoBase}")
	private String queryCotasOrdenadasPorSegmentoSemEdicaoBase;

	@Value("#{query_estudo.queryCotasOrdenadasMaiorMenor}")
	private String queryCotasOrdenadasMaiorMenor;

	@Value("#{query_estudo.queryRankingSegmentoEdicoesBase}")
	private String queryRankingSegmentoEdicoesBase;

	@Value("#{query_estudo.queryCotasOrdenadasPorSegmentoEdicaoAberta}")
	private String queryCotasOrdenadasPorSegmentoEdicaoAberta;

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

	public List<Long> getCotasOrdenadaPorSegmentoEdicaoAberta(List<CotaEstudo> cotaList, List<Long> idEdicoesBase) {

		List<Long> retorno = new ArrayList<>();
		List<Long> idList = new ArrayList<>();
		for (CotaEstudo c : cotaList) {
			idList.add(c.getId());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("COTA_IDS", idList);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotasOrdenadasPorSegmentoEdicaoAberta, params);

		while (rs.next()) {
			retorno.add(rs.getLong(1));
		}
		return retorno;
	}

	public List<Long> getCotasOrdenadaPorSegmentoSemEdicaoBase(List<CotaEstudo> cotaList, List<ProdutoEdicaoEstudo> edicoesBase) {

		List<Long> idList = new ArrayList<>();
		List<Long> idListEdicoesBase = new ArrayList<>();
		for (CotaEstudo c : cotaList) {
			idList.add(c.getId());
		}
		for (ProdutoEdicaoEstudo c : edicoesBase) {
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

	public List<Long> getCotasOrdenadaPorSegmento(List<CotaEstudo> cList, List<ProdutoEdicaoEstudo> edicoesBase, int qtde) {

		List<Long> listaProdutoEdicao = new ArrayList<>();
		for (ProdutoEdicaoEstudo edicao : edicoesBase) {
			listaProdutoEdicao.add(edicao.getId());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("IDS_BASE", listaProdutoEdicao);
		params.put("QTDE", qtde);

		SqlRowSet rs = jdbcTemplate.queryForRowSet(queryRankingSegmentoEdicoesBase.replace("#", qtde >= 3 ? ">=" : "="), params);
		List<Long> retorno = new ArrayList<>();
		while (rs.next()) {
			retorno.add(rs.getLong(1));
		}
		return retorno;
	}

	public List<Long> getCotasOrdenadasMaiorMenor(List<CotaEstudo> cotaList, ProdutoEdicaoEstudo produtoEdicaoBase) {
		List<Long> listaCotas = new ArrayList<>();
		for (CotaEstudo cota : cotaList) {
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
=======
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

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class RankingSegmentoDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.queryCotasOrdenadasPorSegmentoSemEdicaoBase}")
	private String queryCotasOrdenadasPorSegmentoSemEdicaoBase;

	@Value("#{query_estudo.queryCotasOrdenadasMaiorMenor}")
	private String queryCotasOrdenadasMaiorMenor;

	@Value("#{query_estudo.queryRankingSegmentoEdicoesBase}")
	private String queryRankingSegmentoEdicoesBase;

	@Value("#{query_estudo.queryCotasOrdenadasPorSegmentoEdicaoAberta}")
	private String queryCotasOrdenadasPorSegmentoEdicaoAberta;

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

	public List<Long> getCotasOrdenadaPorSegmentoEdicaoAberta(List<CotaEstudo> cotaList, List<Long> idEdicoesBase) {

		List<Long> retorno = new ArrayList<>();
		List<Long> idList = new ArrayList<>();
		for (CotaEstudo c : cotaList) {
			idList.add(c.getId());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("COTA_IDS", idList);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotasOrdenadasPorSegmentoEdicaoAberta, params);

		while (rs.next()) {
			retorno.add(rs.getLong(1));
		}
		return retorno;
	}

	public List<Long> getCotasOrdenadaPorSegmentoSemEdicaoBase(List<CotaEstudo> cotaList, List<ProdutoEdicaoEstudo> edicoesBase) {

		List<Long> idList = new ArrayList<>();
		List<Long> idListEdicoesBase = new ArrayList<>();
		for (CotaEstudo c : cotaList) {
			idList.add(c.getId());
		}
		for (ProdutoEdicaoEstudo c : edicoesBase) {
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

	public List<Long> getCotasOrdenadaPorSegmento(List<CotaEstudo> cList, List<ProdutoEdicaoEstudo> edicoesBase, int qtde) {

		List<Long> listaProdutoEdicao = new ArrayList<>();
		for (ProdutoEdicaoEstudo edicao : edicoesBase) {
			listaProdutoEdicao.add(edicao.getId());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("IDS_BASE", listaProdutoEdicao);
		params.put("QTDE", qtde);

		SqlRowSet rs = jdbcTemplate.queryForRowSet(queryRankingSegmentoEdicoesBase.replace("#", qtde >= 3 ? ">=" : "="), params);
		List<Long> retorno = new ArrayList<>();
		while (rs.next()) {
			retorno.add(rs.getLong(1));
		}
		return retorno;
	}

	public List<Long> getCotasOrdenadasMaiorMenor(List<CotaEstudo> cotaList, ProdutoEdicaoEstudo produtoEdicaoBase) {
		List<Long> listaCotas = new ArrayList<>();
		for (CotaEstudo cota : cotaList) {
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
>>>>>>> refs/remotes/DGBTi/fase2
