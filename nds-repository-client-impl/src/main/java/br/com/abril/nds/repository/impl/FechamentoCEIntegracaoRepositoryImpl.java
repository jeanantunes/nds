package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoCEIntegracaoRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalhe, FechamentoEncalhePK> implements
		FechamentoCEIntegracaoRepository {
	
	@Autowired
	private ChamadaEncalheFornecedorRepository chamadaEncalheFornecedorRepository;
	
	public FechamentoCEIntegracaoRepositoryImpl() {
		super(FechamentoEncalhe.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemFechamentoCEIntegracaoDTO> buscarConferenciaEncalhe(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append("    PROD_EDICAO.ID AS idProdutoEdicao, ");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.NUEMRO_ITEM AS sequencial, ");
		
		hql.append("    PROD.CODIGO as codigoProduto, ");
		
		hql.append("    PROD.NOME as nomeProduto, ");
		
		hql.append("    PROD_EDICAO.NUMERO_EDICAO as numeroEdicao, ");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA as reparte, ");
		
		hql.append("    COALESCE((ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA - COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_FORNECEDOR,0) ),0) as venda, ");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO as precoCapa,");
		
		hql.append("    COALESCE((ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA - COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_FORNECEDOR,0) ) * PROD_EDICAO.PRECO_VENDA,0) as valorVenda,");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.REGIME_RECOLHIMENTO AS tipoFormatado, ");
		
		hql.append("	COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_FORNECEDOR,0) AS encalhe, ");
		
		hql.append("	ITEM_CH_ENC_FORNECEDOR.ID AS idItemCeIntegracao ");
		
		hql.append(this.obterHqlFrom(filtro));
		
		hql.append(obterOrdenacao(filtro));
		
		Query  query = getSession().createSQLQuery(hql.toString())
						.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
						.addScalar("sequencial", StandardBasicTypes.LONG)
						.addScalar("codigoProduto", StandardBasicTypes.STRING)
						.addScalar("nomeProduto", StandardBasicTypes.STRING)
						.addScalar("numeroEdicao", StandardBasicTypes.LONG)
						.addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
						.addScalar("tipoFormatado", StandardBasicTypes.STRING)
						.addScalar("venda", StandardBasicTypes.BIG_INTEGER)
						.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL)
						.addScalar("valorVenda", StandardBasicTypes.BIG_DECIMAL)
						.addScalar("encalhe", StandardBasicTypes.BIG_INTEGER)
						.addScalar("idItemCeIntegracao",StandardBasicTypes.LONG)
						.setResultTransformer(Transformers.aliasToBean(ItemFechamentoCEIntegracaoDTO.class));	
		
		this.aplicarParametros(filtro, query);
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
			
		return query.list();
	}
	
	public BigInteger countItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" COUNT(ITEM_CH_ENC_FORNECEDOR.ID) ");
		
		hql.append(this.obterHqlFrom(filtro));
		
		Query query = getSession().createSQLQuery(hql.toString());
		
		this.aplicarParametros(filtro, query);
		
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public FechamentoCEIntegracaoConsolidadoDTO buscarConferenciaEncalheTotal(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append("  sum(COALESCE(((ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA - COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_FORNECEDOR,0))  * PROD_EDICAO.PRECO_VENDA),0)) as totalBruto,");
		
		hql.append("  sum(COALESCE((((select desconto_logistica.PERCENTUAL_DESCONTO");
		hql.append("   from desconto_logistica where desconto_logistica.ID = PROD.DESCONTO_LOGISTICA_ID)");
		hql.append("  / 100)* PROD_EDICAO.PRECO_VENDA)*(ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA- COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_FORNECEDOR,0)),0)) as totalDesconto ");
		
		hql.append(this.obterHqlFrom(filtro));
		
		Query  query = getSession().createSQLQuery(hql.toString())
						.addScalar("totalBruto", StandardBasicTypes.BIG_DECIMAL)
						.addScalar("totalDesconto", StandardBasicTypes.BIG_DECIMAL)
						.setResultTransformer(Transformers.aliasToBean(FechamentoCEIntegracaoConsolidadoDTO.class));
		
		this.aplicarParametros(filtro, query);
		
		return (FechamentoCEIntegracaoConsolidadoDTO) query.uniqueResult();
	}
	
	private String obterHqlFrom(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM ");
	    
		hql.append(" ITEM_CHAMADA_ENCALHE_FORNECEDOR ITEM_CH_ENC_FORNECEDOR ");
		
		hql.append(" INNER JOIN  ");
		hql.append(" 		chamada_encalhe_fornecedor chmFornecedor ");         
		hql.append(" 			on ( ");
		hql.append("			 	chmFornecedor.ID = ITEM_CH_ENC_FORNECEDOR.CHAMADA_ENCALHE_FORNECEDOR_ID ");
		hql.append("			 )		");         
		
		hql.append(" INNER JOIN ");
		hql.append("    PRODUTO_EDICAO PROD_EDICAO ");
		hql.append("        ON ( ");
		hql.append("            ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append("        ) ");
		
		hql.append(" INNER JOIN ");
	    hql.append("    PRODUTO PROD ");
	    hql.append("        ON ( ");
		hql.append("            PROD_EDICAO.PRODUTO_ID = PROD.ID ");
		hql.append("        ) ");
		
		hql.append(" INNER JOIN ");
		hql.append("    PRODUTO_FORNECEDOR PROD_FORNEC ");
		hql.append("        ON ( ");
		hql.append("            PROD.ID = PROD_FORNEC.PRODUTO_ID ");
		hql.append("        ) ");
		
		hql.append(" INNER JOIN "); 
		hql.append(" 	 ESTOQUE_PRODUTO ESTOQUE_PROD ");
		hql.append(" 	 		ON ( ");
		hql.append("			  	ESTOQUE_PROD.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append("			  ) ");
		
		hql.append(" WHERE ");
		
		hql.append(" chmFornecedor.ANO_REFERENCIA =:anoReferencia  ");
		
		hql.append(" AND chmFornecedor.NUMERO_SEMANA =:numeroSemana ");
		
		if(filtro.getIdFornecedor()!= null){
			
			hql.append(" AND PROD_FORNEC.FORNECEDORES_ID = :idFornecedor ");
		}
		
		return hql.toString();
	}
	
	private String obterOrdenacao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColuna() != null) {

			hql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case SEQUENCIAL:
						orderByColumn = " sequencial ";
						break;
					case CODIGO_PRODUTO:
						orderByColumn = " codigoProduto ";
						break;
					case NOME_PRODUTO:
						orderByColumn = " nomeProduto  ";
						break;
					case ENCALHE:
						orderByColumn = " encalhe  ";
						break;
					case PRECO_CAPA:
						orderByColumn = " precoCapa ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = " numeroEdicao ";
						break;
					case REPARTE:
						orderByColumn = " reparte ";
						break;
					case TIPO:
						orderByColumn = " tipoFormatado ";
						break;
					case VALOR_VENDA:
						orderByColumn = " valorVenda ";
						break;
					case VENDA:
						orderByColumn = " venda ";
						break;
						
					default:
						break;
				}
			
			hql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());	
			}
		}
		
		return hql.toString();
	}
	
	private void aplicarParametros(FiltroFechamentoCEIntegracaoDTO filtro, Query query) {
		
		query.setParameter("anoReferencia",filtro.getAnoCorrente());
		
		query.setParameter("numeroSemana",filtro.getNumeroSemana());
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
	}
	
	@Override
	public boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro) {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cef FROM ChamadaEncalheFornecedor cef WHERE" +
				   " numeroSemana = :numeroSemana" +
				   " and anoReferencia= :anoReferencia"+
				   " and cef.statusCeNDS = :statusCeNDS");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("numeroSemana", filtro.getNumeroSemana().intValue());
		query.setParameter("anoReferencia", filtro.getAnoCorrente().intValue());
		query.setParameter("statusCeNDS", StatusCeNDS.FECHADO);
		
		return (query.uniqueResult() == null) ? false : true;
	}

}
