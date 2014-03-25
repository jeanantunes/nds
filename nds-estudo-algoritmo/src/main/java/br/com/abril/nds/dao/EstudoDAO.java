package br.com.abril.nds.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.PercentualExcedenteEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class EstudoDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("#{query_estudo.insertEstudo}")
    private String insertEstudo;

    @Value("#{query_estudo.insertEstudoCotas}")
    private String insertEstudoCotas;

    @Value("#{query_estudo.insertProdutoEdicao}")
    private String insertProdutoEdicao;

    @Value("#{query_estudo.insertProdutoEdicaoBase}")
    private String insertProdutoEdicaoBase;

    @Value("#{query_estudo.queryParametrosDistribuidor}")
    private String queryParametrosDistribuidor;

    @Value("#{query_estudo.queryPercentuaisExcedentes}")
    private String queryPercentuaisExcedentes;
    
    @Value("#{query_estudo.queryIDEstudo}")
    private String queryIDEstudo;

    public void gravarEstudo(EstudoTransient estudo) {
    	
    	Long estudoId = this.obterProximoIDEstudo();
		
		estudo.setId(estudoId);
		
	    SqlParameterSource paramSource = new BeanPropertySqlParameterSource(estudo);
	    
	    jdbcTemplate.update(insertEstudo, paramSource);

	    List<ProdutoEdicaoEstudo> listaProdutoEdicao = new ArrayList<>();
	    
	    for (CotaEstudo cota : estudo.getCotas()) {
	    	
			cota.setIdEstudo(estudoId);
			
			if (cota.getEdicoesRecebidas() != null) {
				
			    for (ProdutoEdicaoEstudo produto : cota.getEdicoesRecebidas()) {
			    	
					produto.setIdEstudo(estudoId);
					produto.setIdCota(cota.getId());
					
					listaProdutoEdicao.add(produto);
			    }
			}
	    }
	    
	    gravarCotas(estudo.getCotas());

	    for (ProdutoEdicaoEstudo prod : estudo.getEdicoesBase()) {
	    	
	    	prod.setIdEstudo(estudoId);
	    }
	    
	    gravarProdutoEdicaoBase(estudo.getEdicoesBase());
	    gravarProdutoEdicao(listaProdutoEdicao);
    }
    
    private Long obterProximoIDEstudo() {
    	
    	SqlRowSet rs = jdbcTemplate.queryForRowSet(queryIDEstudo, new HashMap<String, Object>());
    	
    	rs.next();
    	
    	Long id = rs.getLong("ID");
    	
    	return (id == null || id.equals(0L) ? 1L : id);
    }

    public void gravarCotas(final List<CotaEstudo> cotas) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cotas.toArray());
	jdbcTemplate.batchUpdate(insertEstudoCotas, batch);
    }

    public void gravarProdutoEdicao(List<ProdutoEdicaoEstudo> produtosEdicao) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicao.toArray());
	jdbcTemplate.batchUpdate(insertProdutoEdicao, batch);
    }

    public void gravarProdutoEdicaoBase(List<ProdutoEdicaoEstudo> produtosEdicaoBase) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicaoBase.toArray());
	jdbcTemplate.batchUpdate(insertProdutoEdicaoBase, batch);
    }

    public void carregarPercentuaisExcedente(EstudoTransient estudo) {

	SqlRowSet rs = jdbcTemplate.queryForRowSet(queryPercentuaisExcedentes, new HashMap<String, Object>());

	Map<String, PercentualExcedenteEstudo> mapPercentualExcedente = new HashMap<>();

	while (rs.next()) {
	    PercentualExcedenteEstudo percentualExcedente = new PercentualExcedenteEstudo();
	    percentualExcedente.setEficiencia(rs.getString("EFICIENCIA"));
	    percentualExcedente.setPdv(rs.getBigDecimal("PDV"));
	    percentualExcedente.setVenda(rs.getBigDecimal("VENDA"));

	    mapPercentualExcedente.put(percentualExcedente.getEficiencia(), percentualExcedente);
	}

	estudo.setPercentualProporcaoExcedente(mapPercentualExcedente);
    }

    public void carregarParametrosDistribuidor(EstudoTransient estudo) {

	SqlRowSet rs = jdbcTemplate.queryForRowSet(queryParametrosDistribuidor, new HashMap<String, Object>());

	while(rs.next()) {
	    if (estudo.isComplementarAutomatico()) {
		estudo.setComplementarAutomatico(rs.getBoolean("COMPLEMENTAR_AUTOMATICO"));
	    }
	    estudo.setGeracaoAutomatica(rs.getBoolean("GERACAO_AUTOMATICA_ESTUDO"));
	    estudo.setPercentualMaximoFixacao(rs.getBigDecimal("PERCENTUAL_MAXIMO_FIXACAO"));
	    estudo.setPracaVeraneio(rs.getBoolean("PRACA_VERANEIO"));
	    estudo.setVendaMediaMais(rs.getBigDecimal("VENDA_MEDIA_MAIS").toBigInteger());

	}
    }
}
