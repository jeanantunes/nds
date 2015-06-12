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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.PercentualExcedenteEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import org.hibernate.Query;

import org.hibernate.Session;

import org.hibernate.SessionFactory;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;



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
    

    @Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			
		}
		
		return sessionFactory.openSession();
	}
    

    public void gravarEstudo(EstudoTransient estudo) {
    	
    	Long estudoId = null;
		
	    SqlParameterSource paramSource = new BeanPropertySqlParameterSource(estudo);
	    
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    
	    jdbcTemplate.update(insertEstudo, paramSource, keyHolder);
	    
	    estudoId = keyHolder.getKey().longValue();
	    
	    estudo.setId(estudoId);
	    
	    List<ProdutoEdicaoEstudo> listaProdutoEdicao = new ArrayList<>();
	    
	    for (CotaEstudo cota : estudo.getCotas()) {
	    	
			cota.setIdEstudo(estudoId);
			
			if (cota.getEdicoesRecebidas() != null) {
				
			    for (ProdutoEdicaoEstudo produto : cota.getEdicoesRecebidas()) {
			    	
					produto.setIdEstudo(estudoId);
					produto.setIdCota(cota.getId());
					 
					if(produto.getVenda() == null){
						produto.setVenda(produto.getReparte());
					}
					
					listaProdutoEdicao.add(produto);
			    }
			}
	    }
	    
	    gravarCotas(estudo.getCotas());

	    for (ProdutoEdicaoEstudo prod : estudo.getEdicoesBase()) {
	    	
	    	prod.setIdEstudo(estudoId.longValue());
	    }
	    
	    gravarProdutoEdicaoBase(estudo.getEdicoesBase());
	     gravarProdutoEdicao(listaProdutoEdicao);
    }
    
    public void gravarCotasor(final List<CotaEstudo> cotas) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cotas.toArray());
		jdbcTemplate.batchUpdate(insertEstudoCotas, batch);
    }
    
    
    public void gravarCotas(final List<CotaEstudo> cotas) {
        
    
    	StringBuffer querystr = new StringBuffer();
     	querystr.append("INSERT INTO estudo_cota_gerado (cota_id, estudo_id, qtde_prevista, qtde_efetiva, classificacao, reparte, venda_media, venda_media_nominal, ");
     	querystr.append("reparte_juramentado_a_faturar, quantidade_pdvs, reparte_minimo, reparte_maximo, venda_media_mais_n, indice_correcao_tendencia, indice_venda_crescente,");
     	querystr.append( "percentual_encalhe_maximo, mix, cota_nova, reparte_inicial) VALUES ");

     	String vals=
     			"(:id, :idEstudo, :reparteCalculado, :reparteCalculado, :classificacao.codigo, :reparteCalculado, :vendaMedia, "+
     					":vendaMediaNominal, :reparteJuramentadoAFaturar, :quantidadePDVs, :intervaloMinimo, :intervaloMaximo, :vendaMediaMaisN, "+
     					":indiceCorrecaoTendencia, :indiceVendaCrescente, :percentualEncalheMaximo, :mix, :nova, :reparteCalculado)";
    	boolean prim=true;
     	
	    
	 for(CotaEstudo ce:cotas) {
			
		 if (!prim)
	    	 querystr.append(",");
	     prim=false;
         querystr.append(
             vals
             .replace(":idEstudo",ce.getIdEstudo()==null?"null":ce.getIdEstudo().toString())
    		 .replace(":id",ce.getId()==null?"null":ce.getId().toString())
    		 
			 .replace(":reparteCalculado",ce.getReparteCalculado()==null?"null":ce.getReparteCalculado().toString())
			 .replace(":reparteCalculado",ce.getReparteCalculado()==null?"null":ce.getReparteCalculado().toString())
			 .replace(":classificacao.codigo",ce.getClassificacao()==null || ce.getClassificacao().getCodigo()==null?"null":"'"+ce.getClassificacao().getCodigo().toString()+"'")
			 .replace(":reparteCalculado",ce.getReparteCalculado()==null?"null":ce.getReparteCalculado().toString())
			 .replace(":vendaMediaNominal",ce.getVendaMediaNominal()==null?"null":ce.getVendaMediaNominal().toString())
			 .replace(":vendaMediaMaisN",ce.getVendaMediaMaisN()==null?"null":ce.getVendaMediaMaisN().toString())
			 .replace(":vendaMedia",ce.getVendaMedia()==null?"null":ce.getVendaMedia().toString())
			
			 .replace(":reparteJuramentadoAFaturar",ce.getReparteJuramentadoAFaturar()==null?"null":ce.getReparteJuramentadoAFaturar().toString())
			 .replace(":quantidadePDVs",ce.getQuantidadePDVs()==null?"null":ce.getQuantidadePDVs().toString())
			 .replace(":intervaloMinimo",ce.getIntervaloMinimo()==null?"null":ce.getIntervaloMinimo().toString())
			 .replace(":intervaloMaximo",ce.getIntervaloMaximo()==null?"null":ce.getIntervaloMaximo().toString())
			
			 .replace(":indiceCorrecaoTendencia",ce.getIndiceCorrecaoTendencia()==null?"null":ce.getIndiceCorrecaoTendencia().toString())
			 .replace(":indiceVendaCrescente",ce.getIndiceVendaCrescente()==null?"null":ce.getIndiceVendaCrescente().toString())
			 .replace(":percentualEncalheMaximo",ce.getPercentualEncalheMaximo()==null?"null":ce.getPercentualEncalheMaximo().toString())
			 .replace(":mix",""+ce.isMix())
			 .replace(":nova",""+ce.isNova())
 );

 

	 }
    	  Session session = this.getSession();
    	  Query query = session.createSQLQuery(querystr.toString());
    	  query.executeUpdate();
    	  session.close();
    }
    
    public void gravarProdutoEdicao(final List<ProdutoEdicaoEstudo> produtosEdicao) {
    
    	StringBuffer querystr = new StringBuffer();
     	querystr.append("INSERT INTO estudo_produto_edicao (estudo_id, cota_id, produto_edicao_id, reparte, venda, indice_correcao, venda_corrigida) VALUES "); 
     	String vals="(:idEstudo, :idCota, :id, :reparte, :venda, :indiceCorrecao, :vendaCorrigida )";
    	boolean prim=true;
     	for(ProdutoEdicaoEstudo pee:produtosEdicao) {
	     if (!prim)
	    	 querystr.append(",");
	     prim=false;
         querystr.append(
        	 vals.replace(":idEstudo",pee.getIdEstudo()==null?"null": pee.getIdEstudo().toString())   
            .replace(":idCota",pee.getIdCota()== null ?"null":pee.getIdCota().toString())
        	.replace(":id",pee.getId() == null ?"null": pee.getId().toString())
        	.replace(":reparte",pee.getReparte() == null ?"null":pee.getReparte().toString())
        	.replace(":vendaCorrigida",pee.getVendaCorrigida()==null?"null":pee.getVendaCorrigida().toPlainString())
        	.replace(":venda",pee.getVenda()== null ?"null":pee.getVenda().toString())
        	.replace(":indiceCorrecao",pee.getIndiceCorrecao()==null?"null":pee.getIndiceCorrecao().toString())
        	
        	
        	);
    		
    		 
    	
    	    }
    	  Session session = this.getSession();
    	  Query query = session.createSQLQuery(querystr.toString());
    	  query.executeUpdate();
    	  session.close();
    	
    }

    public void gravarProdutoEdicaoor(List<ProdutoEdicaoEstudo> produtosEdicao) {
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
