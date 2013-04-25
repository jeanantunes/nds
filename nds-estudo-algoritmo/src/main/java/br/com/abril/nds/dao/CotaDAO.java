package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoAjusteReparte;
import br.com.abril.nds.model.estudo.CotaDesenglobada;
import br.com.abril.nds.model.estudo.CotaEnglobada;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class CotaDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.queryProdutoEdicaoPorCota}")
	private String queryProdutoEdicaoPorCota;

	@Value("#{query_estudo.queryCotaEquivalente}")
	private String queryCotaEquivalente;

	@Value("#{query_estudo.queryCotaWithEstoqueProdutoCota}")
	private String queryCotaWithEstoqueProdutoCota;
	
	@Value("#{query_estudo.queryCotasDesenglobadas}")
	private String queryCotasDesenglobadas;

	@Value("#{query_estudo.queryCotasFixadas}")
	private String queryCotasFixadas;
	
	private static final String LANCAMENTO_PARCIAL = "PARCIAL";
	private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
	private static final String STATUS_FECHADO = "FECHADO";

	private Map<Long, BigDecimal> idsPesos = new HashMap<>();

	public CotaEstudo getIndiceAjusteCotaEquivalenteByCota(CotaEstudo cota) {

		List<CotaEstudo> listEquivalente = new ArrayList<CotaEstudo>();
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("COTA_ID", cota.getId());
			params.put("DATA", new java.sql.Date(new Date().getTime()));

			listEquivalente = jdbcTemplate.query(queryCotaEquivalente, params, new RowMapper<CotaEstudo>() {
				@Override
				public CotaEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CotaEstudo temp = new CotaEstudo();
					temp.setId(rs.getLong("ID"));
					temp.setNumeroCota(rs.getInt("NUMERO_COTA"));
					temp.setIndiceAjusteEquivalente(rs.getBigDecimal("INDICE_AJUSTE"));
					return temp;
				}
			});
			cota.setEquivalente(listEquivalente);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cota;
	}

	public List<CotaEstudo> getCotaWithEstoqueProdutoCota() {
		List<CotaEstudo> cotas = new ArrayList<CotaEstudo>();
		try {
			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotaWithEstoqueProdutoCota, new HashMap<String, Object>());
			while (rs.next()) {
				CotaEstudo cota = new CotaEstudo();
				cota.setId(rs.getLong("ID"));
				// cota.setNumero(rs.getLong("NUMERO_COTA"));
				// cota.setNomePessoa(rs.getString("NOME"));
				cotas.add(cota);
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as cotas");
			ex.printStackTrace();
		}
		return cotas;
	}

	public LinkedList<CotaEstudo> getCotasComEdicoesBase(EstudoTransient estudo) {
	    	LinkedList<CotaEstudo> returnListCota = new LinkedList<>();
		for (ProdutoEdicaoEstudo edicao : estudo.getEdicoesBase()) {
			idsPesos.put(edicao.getId(), edicao.getIndicePeso());
		}

		Map<String, Object> params = new HashMap<>();
		params.put("ID_PRODUTO", estudo.getProdutoEdicaoEstudo().getId());
		params.put("NUMERO_EDICAO", estudo.getProdutoEdicaoEstudo().getNumeroEdicao());
		params.put("TIPO_SEGMENTO", estudo.getProdutoEdicaoEstudo().getTipoSegmentoProduto());
		params.put("IDS_PRODUTOS", idsPesos.keySet());

		List<CotaEstudo> temp = jdbcTemplate.query(queryProdutoEdicaoPorCota, params, new RowMapper<CotaEstudo>() {
			@Override
			public CotaEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CotaEstudo cota = new CotaEstudo();
				cota.setId(rs.getLong("COTA_ID"));
				cota.setNumeroCota(rs.getInt("NUMERO_COTA"));
				cota.setQuantidadePDVs(rs.getBigDecimal("QTDE_PDVS"));
				cota.setRecebeReparteComplementar(rs.getBoolean("RECEBE_COMPLEMENTAR"));
				cota.setSituacaoCadastro(SituacaoCadastro.valueOf(rs.getString("SITUACAO_CADASTRO")));
				cota.setMix(rs.getBoolean("MIX"));
				cota.setRegiao(rs.getInt("REGIAO_ID"));
				traduzAjusteReparte(rs, cota);
				cota.setEdicoesRecebidas(getEdicoes(rs, idsPesos));
				return cota;
			}
		});
		for (CotaEstudo cota : temp) {
		    returnListCota.add(cota);
		}
		returnListCota = agrupaCotas(returnListCota);
		return returnListCota;
	}
	
	public List<CotaDesenglobada> buscarCotasDesenglobadas() {
		List<CotaDesenglobada> cotasDesenglobadas = jdbcTemplate.query(queryCotasDesenglobadas, new HashMap<String,String>(), new RowMapper<CotaDesenglobada>() {

			@Override
			public CotaDesenglobada mapRow(ResultSet rs, int rowNum) throws SQLException {
				CotaDesenglobada cotaDesenglobada = new CotaDesenglobada();
				cotaDesenglobada.setId(rs.getLong("COTA_ID_DESENGLOBADA"));
				CotaEnglobada cotaEnglobada = new CotaEnglobada();
				cotaEnglobada.setId(rs.getLong("COTA_ID_ENGLOBADA"));
				cotaEnglobada.setPorcentualEnglobacao(rs.getInt("PORCENTAGEM_COTA_ENGLOBADA"));
				cotaEnglobada.setDataInclusao(rs.getDate("DATA_ALTERACAO"));
				cotaDesenglobada.setCotasEnglobadas(Arrays.asList(cotaEnglobada));
				return cotaDesenglobada;
			}
		});
		
		return agrupaCotasDesenglobadas(cotasDesenglobadas);
	}

	private List<CotaDesenglobada> agrupaCotasDesenglobadas(List<CotaDesenglobada> cotasDesenglobadas) {
		if (cotasDesenglobadas.size() > 0) {
			List<CotaDesenglobada> novaLista = new ArrayList<>();
			CotaDesenglobada temp = cotasDesenglobadas.get(0);
			for (int i = 1; i < cotasDesenglobadas.size(); i++) {
				if (temp.equals(cotasDesenglobadas.get(i))) {
					temp.getCotasEnglobadas().addAll(cotasDesenglobadas.get(i).getCotasEnglobadas());
				} else {
					novaLista.add(temp);
					temp = cotasDesenglobadas.get(i);
				}
			}
			return novaLista;
		} else {
			return cotasDesenglobadas;
		}
	}

	private void traduzAjusteReparte(ResultSet rs, CotaEstudo cota) throws SQLException {
		String formaAjuste = rs.getString("FORMA_AJUSTE");
		if ((formaAjuste != null) && (!formaAjuste.isEmpty())) {
			if (formaAjuste.equals(TipoAjusteReparte.AJUSTE_VENDA_MEDIA)) {
				cota.setVendaMediaMaisN(rs.getBigDecimal("AJUSTE_APLICADO").toBigInteger());
			} else if (formaAjuste.equals(TipoAjusteReparte.AJUSTE_ENCALHE_MAX)) {
				cota.setPercentualEncalheMaximo(rs.getBigDecimal("AJUSTE_APLICADO"));
			} else {
				cota.setAjusteReparte(rs.getBigDecimal("AJUSTE_APLICADO"));
			}
		}
	}

	private LinkedList<CotaEstudo> agrupaCotas(LinkedList<CotaEstudo> lista) {
		if (lista.size() > 0) {
		    	LinkedList<CotaEstudo> novaLista = new LinkedList<CotaEstudo>();
			CotaEstudo temp = lista.get(0);
			for (int i = 1; i < lista.size(); i++) {
				if (temp.equals(lista.get(i))) {
					temp.getEdicoesRecebidas().addAll(lista.get(i).getEdicoesRecebidas());
				} else {
					novaLista.add(temp);
					temp = lista.get(i);
				}
			}
			return novaLista;
		} else {
			return lista;
		}
	}

	private List<ProdutoEdicaoEstudo> getEdicoes(ResultSet rs, Map<Long, BigDecimal> idsPesos) throws SQLException {
		List<ProdutoEdicaoEstudo> edicoes = new ArrayList<>();
		ProdutoEdicaoEstudo produtoEdicao = new ProdutoEdicaoEstudo();

		produtoEdicao.setProduto(new Produto());
		produtoEdicao.getProduto().setId(rs.getLong("PRODUTO_ID"));
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdLancamento(rs.getLong("LANCAMENTO_ID"));
		produtoEdicao.setEdicaoAberta(traduzStatus(rs.getString("STATUS")));
		produtoEdicao.setParcial(rs.getString("TIPO_LANCAMENTO").equalsIgnoreCase(LANCAMENTO_PARCIAL));
		produtoEdicao.setColecao(traduzColecionavel(rs.getString("GRUPO_PRODUTO")));
		produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
		produtoEdicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
		produtoEdicao.setVenda(rs.getBigDecimal("QTDE_VENDA"));

		produtoEdicao.setIndicePeso(idsPesos.get(produtoEdicao.getId()));

		produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		produtoEdicao.getProduto().setCodigo(rs.getString("CODIGO"));

		edicoes.add(produtoEdicao);
		return edicoes;
	}

	private boolean traduzStatus(String status) {
		if (status != null && !status.equalsIgnoreCase(STATUS_FECHADO)) {
			return true;
		}
		return false;
	}

	private boolean traduzColecionavel(String grupoProduto) {
		if (grupoProduto != null && grupoProduto.equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
			return true;
		}
		return false;
	}

	public CotaEstudo getCotaById(Long id) {
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("ID", id);
		
		return jdbcTemplate.queryForObject("select * from cota where id = :ID", paramMap, new RowMapper<CotaEstudo>() {

			@Override
			public CotaEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CotaEstudo cota = new CotaEstudo();
				cota.setId(rs.getLong("ID"));
				cota.setNumeroCota(rs.getInt("NUMERO_COTA"));
				return cota;
			}
		});
	}

	public List<CotaEstudo> getCotasBase() {
		List<CotaEstudo> cotaEstudos = new ArrayList<>();
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select COTA_ID from cota_base", new HashMap<String, Object>());
		while (rs.next()) {
			CotaEstudo cotaEstudo = new CotaEstudo();
			cotaEstudo.setId(rs.getLong("COTA_ID"));
		}
		return cotaEstudos;
	}
	
	public List<CotaEstudo> getCotasComFixacao(Long idProduto, Long numeroEdicaoEstudo) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("ID_PRODUTO", idProduto);
		paramMap.put("NUMERO_EDICAO_ESTUDO", numeroEdicaoEstudo);
		
		return jdbcTemplate.query(queryCotasFixadas, paramMap, new RowMapper<CotaEstudo>() {

			@Override
			public CotaEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CotaEstudo cota = new CotaEstudo();
				cota.setId(rs.getLong("ID_COTA"));
				cota.setReparteFixado(rs.getBigDecimal("QTDE_EXEMPLARES").toBigInteger());
				return cota;
			}
		});
	}
}
