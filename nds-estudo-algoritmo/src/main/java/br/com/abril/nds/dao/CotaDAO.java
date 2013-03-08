package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.model.TipoAjusteReparte;
import br.com.abril.nds.model.TipoSegmentoProduto;

@Repository
public class CotaDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("#{query_estudo.queryProdutoEdicaoPorCota}")
    private String queryProdutoEdicaoPorCota;

    @Value("#{query_estudo.queryAjusteReparte}")
    private String queryAjusteReparte;

    @Value("#{query_estudo.queryAjusteReparteTipoSegmento}")
    private String queryAjusteReparteTipoSegmento;

    @Value("#{query_estudo.queryAjusteReparteFormaReajuste}")
    private String queryAjusteReparteFormaReajuste;

    @Value("#{query_estudo.queryCotaEquivalente}")
    private String queryCotaEquivalente;
    
    @Value("#{query_estudo.queryCotaWithEstoqueProdutoCota}")
    private String queryCotaWithEstoqueProdutoCota;

    private Map<Long, Integer> idsPesos = new HashMap<Long, Integer>();

    public BigDecimal getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(Cota cota, TipoSegmentoProduto tipoSegmentoProduto,
	    TipoAjusteReparte[] tipoAjusteReparte) {

	BigDecimal ajusteAplicado = null;

	String query = null;
	try {
	    if (tipoSegmentoProduto != null) {
		query = queryAjusteReparteTipoSegmento;
	    } else {
		query = queryAjusteReparte;
	    }
	    if (tipoAjusteReparte != null && tipoAjusteReparte.length > 0) {
		
		for (int i = 0; i < tipoAjusteReparte.length; i++) {
//		    valores += "\"" + tipoAjusteReparte[i].toString() + "\",";
		}
		query = queryAjusteReparteFormaReajuste;
	    }
	    
	    Map<String, Object> params = new HashMap<>();
//	    params.put("", );

	    int i = 1;
//	    jdbcTemplate.query(query, params);
//	    PreparedStatement psmt = dataSource.getConnection().prepareStatement(queryAjusteReparte);
//	    if (tipoSegmentoProduto != null) {
//		psmt.setLong(i++, tipoSegmentoProduto.getId());
//	    }
//	    psmt.setLong(i++, cota.getId());
//	    psmt.setDate(i++, new java.sql.Date(new Date().getTime()));
//	    psmt.setDate(i++, new java.sql.Date(new Date().getTime()));
//
//	    ResultSet rs = psmt.executeQuery();
//	    if (rs.next()) {
//		ajusteAplicado = rs.getBigDecimal("AJUSTE_APLICADO");
//	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return ajusteAplicado;
    }

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
	params.put("IDS_PRODUTOS", idsPesos.keySet());

	returnListCota = jdbcTemplate.query(queryProdutoEdicaoPorCota, params, new RowMapper<Cota>() {

	    @Override
	    public Cota mapRow(ResultSet rs, int rowNum) throws SQLException {
		Cota cota = new Cota();
		cota.setId(rs.getLong("COTA_ID"));
		cota.setNumero(rs.getLong("NUMERO_COTA"));
		traduzFormaAjuste(rs, cota);
		cota.setEdicoesRecebidas(getEdicoes(rs, idsPesos, false));
		return cota;
	    }
	});

	returnListCota = agrupaCotas(returnListCota);
	return returnListCota;
    }

    private List<Cota> agrupaCotas(List<Cota> lista) {
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
    }

    private void traduzFormaAjuste(ResultSet rs, Cota cota) throws SQLException {
	// TODO: ainda faltam carregar os parâmetros para ajuste de segmento e histórico
	// (estarão gravados no banco respectivamente: 'segmento' e 'histórico')
	String formaAjuste = rs.getString("FORMA_AJUSTE");
	if ("vendaMedia".equals(formaAjuste)) {
	    cota.setVendaMediaMaisN(rs.getBigDecimal("AJUSTE_APLICADO"));
	} else if ("encalheMaximo".equals(formaAjuste)) {
	    cota.setPercentualEncalheMaximo(rs.getBigDecimal("AJUSTE_APLICADO"));
	}
    }

    private List<ProdutoEdicao> getEdicoes(ResultSet rs, Map<Long, Integer> idsPesos, boolean forceEdicaoFechada) throws SQLException {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();

	produtoEdicao.setIdProduto(rs.getLong("PRODUTO_ID"));
	produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
	produtoEdicao.setIdLancamento(rs.getLong("LANCAMENTO_ID"));

	// FIXME - gambeta loca remover!!!!!!!
	if (forceEdicaoFechada) {
	    produtoEdicao.setEdicaoAberta(false);
	} else {
	    produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
	}

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

    private static final String LANCAMENTO_PARCIAL = "PARCIAL";
    private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
    private static final String STATUS_FECHADO = "FECHADO";
}
