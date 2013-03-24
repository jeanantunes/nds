package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.CotaDesenglobada;
import br.com.abril.nds.model.CotaEnglobada;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.model.TipoAjusteReparte;

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

	private static final String LANCAMENTO_PARCIAL = "PARCIAL";
	private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
	private static final String STATUS_FECHADO = "FECHADO";

	private Map<Long, BigDecimal> idsPesos = new HashMap<>();

	public Cota getIndiceAjusteCotaEquivalenteByCota(Cota cota) {

		List<Cota> listEquivalente = new ArrayList<Cota>();
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("COTA_ID", cota.getId());
			params.put("DATA", new java.sql.Date(new Date().getTime()));

			listEquivalente = jdbcTemplate.query(queryCotaEquivalente, params, new RowMapper<Cota>() {
				@Override
				public Cota mapRow(ResultSet rs, int rowNum) throws SQLException {
					Cota temp = new Cota();
					temp.setId(rs.getLong("ID"));
					temp.setNumero(rs.getLong("NUMERO_COTA"));
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

	public List<Cota> getCotaWithEstoqueProdutoCota() {
		List<Cota> cotas = new ArrayList<Cota>();
		try {
			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotaWithEstoqueProdutoCota, new HashMap<String, Object>());
			while (rs.next()) {
				Cota cota = new Cota();
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

	public List<Cota> getCotasComEdicoesBase(Estudo estudo) {
		List<Cota> returnListCota = new ArrayList<>();
		for (ProdutoEdicaoBase edicao : estudo.getEdicoesBase()) {
			idsPesos.put(edicao.getId(), edicao.getPeso());
		}

		Map<String, Object> params = new HashMap<>();
		params.put("ID_PRODUTO", estudo.getProduto().getIdProduto());
		params.put("NUMERO_EDICAO", estudo.getProduto().getNumeroEdicao());
		params.put("TIPO_SEGMENTO", estudo.getProduto().getTipoSegmentoProduto());
		params.put("IDS_PRODUTOS", idsPesos.keySet());

		returnListCota = jdbcTemplate.query(queryProdutoEdicaoPorCota, params, new RowMapper<Cota>() {
			@Override
			public Cota mapRow(ResultSet rs, int rowNum) throws SQLException {
				Cota cota = new Cota();
				cota.setId(rs.getLong("COTA_ID"));
				cota.setNumero(rs.getLong("NUMERO_COTA"));
				cota.setQuantidadePDVs(rs.getBigDecimal("QTDE_PDVS"));
				cota.setMix(rs.getInt("MIX") == 1);
				traduzAjusteReparte(rs, cota);
				cota.setEdicoesRecebidas(getEdicoes(rs, idsPesos));
				return cota;
			}
		});

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

	private void traduzAjusteReparte(ResultSet rs, Cota cota) throws SQLException {
		String formaAjuste = rs.getString("FORMA_AJUSTE");
		if ((formaAjuste != null) && (!formaAjuste.isEmpty())) {
			if (formaAjuste.equals(TipoAjusteReparte.AJUSTE_VENDA_MEDIA)) {
				cota.setVendaMediaMaisN(rs.getBigDecimal("AJUSTE_APLICADO"));
			} else if (formaAjuste.equals(TipoAjusteReparte.AJUSTE_ENCALHE_MAX)) {
				cota.setPercentualEncalheMaximo(rs.getBigDecimal("AJUSTE_APLICADO"));
			} else {
				cota.setAjusteReparte(rs.getBigDecimal("AJUSTE_APLICADO"));
			}
		}
	}

	private List<Cota> agrupaCotas(List<Cota> lista) {
		if (lista.size() > 0) {
			List<Cota> novaLista = new ArrayList<Cota>();
			Cota temp = lista.get(0);
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

	private List<ProdutoEdicao> getEdicoes(ResultSet rs, Map<Long, BigDecimal> idsPesos) throws SQLException {
		List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();

		produtoEdicao.setIdProduto(rs.getLong("PRODUTO_ID"));
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdLancamento(rs.getLong("LANCAMENTO_ID"));
		produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
		produtoEdicao.setParcial(rs.getString("TIPO_LANCAMENTO").equalsIgnoreCase(LANCAMENTO_PARCIAL));
		produtoEdicao.setColecao(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
		produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
		produtoEdicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
		produtoEdicao.setVenda(rs.getBigDecimal("QTDE_VENDA"));

		produtoEdicao.setPeso(idsPesos.get(produtoEdicao.getId()));

		produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		produtoEdicao.setCodigoProduto(rs.getString("CODIGO"));

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

	public Cota getCotaById(Long id) {
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("ID", id);
		
		return jdbcTemplate.queryForObject("select * from cota where id = :ID", paramMap, new RowMapper<Cota>() {

			@Override
			public Cota mapRow(ResultSet rs, int rowNum) throws SQLException {
				Cota cota = new Cota();
				cota.setId(rs.getLong("ID"));
				cota.setNumero(rs.getLong("NUMERO_COTA"));
				return cota;
			}
		});
	}
}
