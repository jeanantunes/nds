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

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoAjusteReparte;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaDesenglobada;
import br.com.abril.nds.model.estudo.CotaEnglobada;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class CotaDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("#{query_estudo.queryCotaEquivalente}")
    private String queryCotaEquivalente;

    @Value("#{query_estudo.queryCotaWithEstoqueProdutoCota}")
    private String queryCotaWithEstoqueProdutoCota;

    @Value("#{query_estudo.queryCotasDesenglobadas}")
    private String queryCotasDesenglobadas;

    @Value("#{query_estudo.queryCotaRecebeuEdicaoAberta}")
    private String queryCotaRecebeuEdicaoAberta;

    @Value("#{query_estudo.queryHistoricoCota}")
    private String queryHistoricoCota;

    @Value("#{query_estudo.queryCotas}")
    private String queryCotas;

    @Value("#{query_estudo.queryHistoricoCotaParcial}")
    private String queryHistoricoCotaParcial;

    @Value("#{query_estudo.queryComponentesCota}")
    private String queryComponentesCota;

    public List<CotaEstudo> getIndiceAjusteCotaEquivalenteByCota(CotaEstudo cota) {

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
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return listEquivalente;
    }

    public List<CotaEstudo> getCotaWithEstoqueProdutoCota() {
	List<CotaEstudo> cotas = new ArrayList<CotaEstudo>();
	try {
	    SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotaWithEstoqueProdutoCota, new HashMap<String, Object>());
	    while (rs.next()) {
		CotaEstudo cota = new CotaEstudo();
		cota.setId(rs.getLong("ID"));
		cotas.add(cota);
	    }
	} catch (Exception ex) {
	    System.out.println("Ocorreu um erro ao tentar consultar as cotas");
	    ex.printStackTrace();
	}
	return cotas;
    }

    public List<Long> buscarCotasQueReceberamUltimaEdicaoAberta(String codigoProduto, List<Long> idsCotas, List<Long> numerosEdicao) {
	Map<String, Object> params = new HashMap<>();
	params.put("codigo_produto", codigoProduto);
	params.put("ids_cota", idsCotas);
	params.put("numeros_edicao", numerosEdicao);
	return jdbcTemplate.query(queryCotaRecebeuEdicaoAberta, params, new RowMapper<Long>() {

	    @Override
	    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
		return rs.getLong("COTA_ID");
	    }
	});
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
		cotaDesenglobada.setCotasEnglobadas(new ArrayList<>(Arrays.asList(cotaEnglobada)));
		return cotaDesenglobada;
	    }
	});

	return agrupaCotasDesenglobadas(new ArrayList<CotaDesenglobada>(cotasDesenglobadas));
    }

    private List<CotaDesenglobada> agrupaCotasDesenglobadas(ArrayList<CotaDesenglobada> cotasDesenglobadas) {
	if (cotasDesenglobadas.size() > 0) {
	    List<CotaDesenglobada> novaLista = new ArrayList<>();
	    CotaDesenglobada temp = cotasDesenglobadas.get(0);
	    for (int i = 1; i < cotasDesenglobadas.size(); i++) {
		if (temp.getId().equals(cotasDesenglobadas.get(i).getId())) {
		    if (temp.getCotasEnglobadas() == null) {
			temp.setCotasEnglobadas(new ArrayList<CotaEnglobada>());
		    }
		    temp.getCotasEnglobadas().addAll(cotasDesenglobadas.get(i).getCotasEnglobadas());
		} else {
		    novaLista.add(temp);
		    temp = cotasDesenglobadas.get(i);
		}
	    }
	    novaLista.add(temp);
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
	    cota.setClassificacao(ClassificacaoCota.Ajuste);
	}
	if (cota.getAjusteReparte() == null || cota.getAjusteReparte().compareTo(BigDecimal.ONE) == -1) {
	    cota.setIndiceAjusteCota(BigDecimal.ONE);
	} else {
	    cota.setIndiceAjusteCota(cota.getAjusteReparte());
	    cota.setClassificacao(ClassificacaoCota.Ajuste);
	}
    }

    public List<CotaEstudo> getCotas(final EstudoTransient estudo) {
	Map<String, Object> params = new HashMap<>();
	params.put("tipo_segmento_produto_id", estudo.getProdutoEdicaoEstudo().getTipoSegmentoProduto().getId());
	params.put("produto_id", estudo.getProdutoEdicaoEstudo().getProduto().getId());
	params.put("numero_edicao", estudo.getProdutoEdicaoEstudo().getNumeroEdicao());
	List<CotaEstudo> retorno = jdbcTemplate.query(queryCotas, params, new RowMapper<CotaEstudo>() {

	    @Override
	    public CotaEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
		CotaEstudo cota = new CotaEstudo();
		cota.setId(rs.getLong("COTA_ID"));
		cota.setNumeroCota(rs.getInt("NUMERO_COTA"));
		cota.setRecebeReparteComplementar(rs.getBoolean("RECEBE_COMPLEMENTAR"));
		cota.setQuantidadePDVs(rs.getBigDecimal("QTDE_PDVS"));
		cota.setSituacaoCadastro(SituacaoCadastro.valueOf(rs.getString("SITUACAO_CADASTRO")));
		cota.setMix(rs.getBoolean("MIX"));
		cota.setTipoDistribuicaoCota(TipoDistribuicaoCota.valueOf(rs.getString("TIPO_DISTRIBUICAO_COTA")));
		cota.setRecebeParcial(rs.getBoolean("RECEBE_RECOLHE_PARCIAIS"));
		cota.setExcecaoParcial(rs.getBoolean("COTA_EXCECAO_PARCIAL"));
		traduzAjusteReparte(rs, cota);
		if (rs.getBigDecimal("QTDE_RANKING_SEGMENTO") != null) {
		    cota.setQtdeRankingSegmento(rs.getBigDecimal("QTDE_RANKING_SEGMENTO").toBigInteger());
		}
		if (rs.getBigDecimal("QTDE_RANKING_FATURAMENTO") != null) {
		    cota.setQtdeRankingFaturamento(rs.getBigDecimal("QTDE_RANKING_FATURAMENTO"));
		}
		if (rs.getBigDecimal("REPARTE_MAX") != null) {
		    cota.setIntervaloMaximo(rs.getBigDecimal("REPARTE_MAX").toBigInteger());
		}
		if (rs.getBigDecimal("REPARTE_MIN") != null) {
		    cota.setIntervaloMinimo(rs.getBigDecimal("REPARTE_MIN").toBigInteger());
		}
		if (cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO) && rs.getBigDecimal("REPARTE_FIXADO") != null) {
		    cota.setReparteFixado(rs.getBigDecimal("REPARTE_FIXADO").toBigInteger());
		}
		if (cota.getTipoDistribuicao() != null && (!cota.getTipoDistribuicao().equals(TipoDistribuicaoCota.ALTERNATIVO) || cota.isMix())) {
		    if ((estudo.getProdutoEdicaoEstudo().getNumeroEdicao().compareTo(new Long(1)) == 0) || (!estudo.getProdutoEdicaoEstudo().isColecao())) {
			if (rs.getLong("COTA_BASE_ID") != 0) {
			    cota.setNova(true);
			    cota.setClassificacao(ClassificacaoCota.CotaNova);
			}
		    }
		}
		if (rs.getBoolean("COTA_NAO_RECEBE_SEGMENTO")) {
		    cota.setClassificacao(ClassificacaoCota.CotaNaoRecebeEsseSegmento);
		}
		if (rs.getBoolean("COTA_EXCECAO_SEGMENTO")) {
		    cota.setClassificacao(ClassificacaoCota.CotaExcecaoSegmento);
		}
		if (rs.getBoolean("COTA_NAO_RECEBE_CLASSIFICACAO")) {
		    cota.setClassificacao(ClassificacaoCota.BancaSemClassificacaoDaPublicacao);
		}
		if (cota.isMix()) {
		    cota.setClassificacao(ClassificacaoCota.CotaMix);
		}
		return cota;
	    }
	});
	return retorno;
    }
 
    public Map<Long, CotaEstudo> getHistoricoCota(final ProdutoEdicaoEstudo edicao) {
	Map<String, Object> params = new HashMap<>();
	params.put("produto_edicao_id", edicao.getId());
	if (edicao.isParcial()) {
	    params.put("numero_periodo", edicao.getPeriodo());
	}
	List<CotaEstudo> historicoCotas = jdbcTemplate.query(edicao.isParcial() ? queryHistoricoCotaParcial : queryHistoricoCota, params, new RowMapper<CotaEstudo>() {
	    @Override
	    public CotaEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
		CotaEstudo cota = new CotaEstudo();
		cota.setId(rs.getLong("cota_id"));
		cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicaoEstudo>());
		ProdutoEdicaoEstudo pe = new ProdutoEdicaoEstudo();
		pe.setVenda(rs.getBigDecimal("venda"));
		pe.setReparte(rs.getBigDecimal("reparte"));
		// copia dos atributos da edicao base
		pe.setId(edicao.getId());
		pe.setProduto(edicao.getProduto());
		pe.setEdicaoAberta(edicao.isEdicaoAberta());
		pe.setParcial(edicao.isParcial());
		pe.setColecao(edicao.isColecao());
		pe.setIndicePeso(edicao.getIndicePeso());
		pe.setNumeroEdicao(edicao.getNumeroEdicao());

		cota.getEdicoesRecebidas().add(pe);
		return cota;
	    }
	});
	Map<Long, CotaEstudo> retorno = new HashMap<>();
	for (CotaEstudo cota : historicoCotas) {
	    if (cota.getEdicoesRecebidas().size() > 0 && cota.getEdicoesRecebidas().get(0).getReparte().compareTo(BigDecimal.ZERO) > 0) {
		retorno.put(cota.getId(), cota);
	    }
	}
	return retorno;
    }

    public void getComponentesCota(List<CotaEstudo> cotasEstudo) {

	List<CotaEstudo> lista = jdbcTemplate.query(queryComponentesCota, new HashMap<String, Object>(), new RowMapper<CotaEstudo>() {

	    @Override
	    public CotaEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
		CotaEstudo cota = new CotaEstudo();
		cota.setId(rs.getLong("COTA_ID"));
		if (rs.getString("BAIRRO") != null) {
		    cota.getBairros().add(rs.getString("BAIRRO"));
		}
		if (rs.getString("TIPO_COTA") != null) {
		    cota.getTiposCota().add(rs.getString("TIPO_COTA"));
		}
		if (rs.getString("UF") != null) {
		    cota.getEstados().add(rs.getString("UF"));
		}
		if (rs.getInt("TIPO_PONTO_PDV_ID") != 0) { 
		    cota.getTiposPontoPdv().add(rs.getInt("TIPO_PONTO_PDV_ID"));
		}
		if (rs.getInt("TIPO_GERADOR_FLUXO_ID") != 0) {
		    cota.getTiposGeradorFluxo().add(rs.getInt("TIPO_GERADOR_FLUXO_ID"));
		}
		if (rs.getInt("REGIAO_ID") != 0) {
		    cota.getRegioes().add(rs.getInt("REGIAO_ID"));
		}
		if (rs.getInt("AREA_INFLUENCIA_PDV_ID") != 0) {
		    cota.getAreasInfluenciaPdv().add(rs.getInt("AREA_INFLUENCIA_PDV_ID"));
		}
		return cota;
	    }
	});

	List<CotaEstudo> retorno = agruparCotas(lista);

	Map<Long, CotaEstudo> mapCota = new HashMap<>();
	for (CotaEstudo cota : cotasEstudo) {
	    mapCota.put(cota.getId(), cota);
	}

	for (CotaEstudo cota : retorno) {
	    CotaEstudo temp = mapCota.get(cota.getId());
	    if (temp != null) {
		copySets(cota, temp);
	    }
	}
    }
    
    private void copySets(CotaEstudo origem, CotaEstudo destino) {
	destino.getTiposPontoPdv().addAll(origem.getTiposPontoPdv());
	destino.getTiposGeradorFluxo().addAll(origem.getTiposGeradorFluxo());
	destino.getBairros().addAll(origem.getBairros());
	destino.getRegioes().addAll(origem.getRegioes());
	destino.getTiposCota().addAll(origem.getTiposCota());
	destino.getAreasInfluenciaPdv().addAll(origem.getAreasInfluenciaPdv());
	destino.getEstados().addAll(origem.getEstados());
    }

    private List<CotaEstudo> agruparCotas(List<CotaEstudo> lista) {
	List<CotaEstudo> retorno = new ArrayList<>();
	if (lista.size() > 0) {
	    CotaEstudo previous = null;
	    for (CotaEstudo cota : lista) {
		if (previous == null) {
		    previous = cota;
		    continue;
		} else {
		    if (previous.getId().equals(cota.getId())) {
			copySets(cota, previous);
		    } else {
			retorno.add(previous);
			previous = cota;
		    }
		}
	    }
	    retorno.add(previous);
	}
	return retorno;
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
}
