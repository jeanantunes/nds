package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
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
	public List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append("    PROD_EDICAO.ID AS idProdutoEdicao, ");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.NUEMRO_ITEM AS sequencial, ");
		
		hql.append("    PROD.CODIGO AS codigoProduto, ");
		
		hql.append("    PROD.NOME AS nomeProduto, ");
		
		hql.append("    PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao, ");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA AS reparte, ");
		
		hql.append("	ITEM_CH_ENC_FORNECEDOR.ID AS idItemCeIntegracao, ");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.REGIME_RECOLHIMENTO AS tipoFormatado, ");
		
		hql.append("    ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO AS precoCapa,");
		
		hql.append("	case when ITEM_CH_ENC_FORNECEDOR.REGIME_RECOLHIMENTO = 'PARCIAL' ");
		hql.append("		then ");
		hql.append("			 COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_VENDA_INFORMADA, 0) ");
		hql.append("		else	");	
		hql.append("			(ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA ");
		hql.append("				- COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_DEVOLUCAO_INFORMADA,");
		hql.append("					  	COALESCE(ESTOQUE_PROD.QTDE_SUPLEMENTAR,0) ");
		hql.append("						 + COALESCE(ESTOQUE_PROD.QTDE,0) ");
		hql.append("					  	 + COALESCE(FCH_ENCALHE.QUANTIDADE,0)");		
		hql.append("				 )");
		hql.append("			)");
		hql.append("	end AS venda,");
		
		hql.append("	case when ITEM_CH_ENC_FORNECEDOR.REGIME_RECOLHIMENTO = 'PARCIAL'");
		hql.append("		then");
		hql.append("			COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_VENDA_INFORMADA, 0) * ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO"); 
		hql.append("		else ");     		
		hql.append("			(ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA ");
		hql.append("			- COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_DEVOLUCAO_INFORMADA,");
		hql.append("					COALESCE(ESTOQUE_PROD.QTDE_SUPLEMENTAR, 0)"); 
		hql.append("					+ COALESCE(ESTOQUE_PROD.QTDE, 0) ");
		hql.append("					+ COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_FORNECEDOR, 0) ");
		hql.append("					+ COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_ENCALHE, 0) ");
		hql.append("					+ COALESCE(ESTOQUE_PROD.QTDE_DANIFICADO, 0) ");
		hql.append("					+ COALESCE(FCH_ENCALHE.QUANTIDADE, 0)");		
		hql.append("			   )");
		hql.append("			) * ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO"); 
		hql.append("	end AS valorVenda,");
		
		hql.append("    case when ITEM_CH_ENC_FORNECEDOR.REGIME_RECOLHIMENTO = 'PARCIAL' "); 
		hql.append("    	then");
		hql.append("			COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_DEVOLUCAO_INFORMADA, 0) ");
		hql.append("		else ");
		hql.append("			COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_DEVOLUCAO_INFORMADA, ");
		hql.append("				COALESCE(ESTOQUE_PROD.QTDE_SUPLEMENTAR,0) "); 
		hql.append("				+ COALESCE(ESTOQUE_PROD.QTDE,0) ");
		hql.append("				+ COALESCE(FCH_ENCALHE.QUANTIDADE,0) ");		
		hql.append("			) ");
		hql.append("	end AS encalhe ");
		 
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
	public FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append("sum(COALESCE( case when ITEM_CH_ENC_FORNECEDOR.REGIME_RECOLHIMENTO = 'PARCIAL'");
		hql.append("		then");
		hql.append("			ITEM_CH_ENC_FORNECEDOR.QTDE_VENDA_INFORMADA * ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO ");
		hql.append("		else");
		hql.append("		(ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA ");
		hql.append("			- COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_DEVOLUCAO_INFORMADA,");
		hql.append("				COALESCE(ESTOQUE_PROD.QTDE_SUPLEMENTAR,0) ");
		hql.append("				+ COALESCE(ESTOQUE_PROD.QTDE,0) ");
		hql.append("				+ COALESCE(FCH_ENCALHE.QUANTIDADE,0)");		
		hql.append("			  )");
		hql.append("		) * ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO ");
	    
		hql.append("	end,0)) as totalBruto,");
		
		hql.append("sum(COALESCE( case when ITEM_CH_ENC_FORNECEDOR.REGIME_RECOLHIMENTO = 'PARCIAL'");
		hql.append("		then");
		hql.append("			(((COALESCE((select desconto_logistica.PERCENTUAL_DESCONTO  ");
		hql.append("				from desconto_logistica ");
		hql.append("				where desconto_logistica.ID = COALESCE(PROD_EDICAO.DESCONTO_LOGISTICA_ID,PROD.DESCONTO_LOGISTICA_ID)");
		hql.append("			),0)/100)*ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO)");
		hql.append("			* ITEM_CH_ENC_FORNECEDOR.QTDE_VENDA_INFORMADA)");
		hql.append("		else");
		hql.append("			(((COALESCE((select desconto_logistica.PERCENTUAL_DESCONTO "); 
		hql.append("				from desconto_logistica ");
		hql.append("				where desconto_logistica.ID = COALESCE(PROD_EDICAO.DESCONTO_LOGISTICA_ID,PROD.DESCONTO_LOGISTICA_ID)");
		hql.append("			),0)/100)*ITEM_CH_ENC_FORNECEDOR.PRECO_UNITARIO)");
		hql.append("			*(ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA ");
		hql.append("				- COALESCE(ITEM_CH_ENC_FORNECEDOR.QTDE_DEVOLUCAO_INFORMADA,");
		hql.append("					COALESCE(ESTOQUE_PROD.QTDE_SUPLEMENTAR,0) ");
		hql.append("					+ COALESCE(ESTOQUE_PROD.QTDE,0) ");
		hql.append("					+ COALESCE(FCH_ENCALHE.QUANTIDADE,0)");		
		hql.append("				)");
		hql.append("			))");
		hql.append("	end,0) ) as totalDesconto");

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
		
		hql.append(" LEFT OUTER JOIN "); 
		hql.append(" 	 ESTOQUE_PRODUTO ESTOQUE_PROD ");
		hql.append(" 	 		ON ( ");
		hql.append("			  	ESTOQUE_PROD.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append("			  ) ");
		
		hql.append(" LEFT OUTER JOIN fechamento_encalhe FCH_ENCALHE ");
		hql.append(" 		ON ( ");
		hql.append("  			FCH_ENCALHE.PRODUTO_EDICAO_ID = PROD_EDICAO.ID AND FCH_ENCALHE.DATA_ENCALHE BETWEEN :inicioSemana AND :fimSemana ");
		hql.append("  		)  ");
		
		if(filtro.getIdFornecedor()!= null){
			hql.append(" INNER JOIN ");
		    hql.append("    PRODUTO_FORNECEDOR PRODFORN ");
		    hql.append("        ON ( ");
			hql.append("            PRODFORN.PRODUTO_ID = PROD.ID ");
			hql.append("        ) ");
			hql.append(" INNER JOIN ");
		    hql.append("    FORNECEDOR FORNEC ");
		    hql.append("        ON ( ");
			hql.append("            PRODFORN.FORNECEDORES_ID = FORNEC.ID ");
			hql.append("        ) ");
		}
		
		hql.append(" WHERE ");
		
		hql.append(" chmFornecedor.ANO_REFERENCIA = :anoReferencia  ");
		
		hql.append(" AND chmFornecedor.NUMERO_SEMANA = :numeroSemana ");
		
		if(filtro.getIdFornecedor() != null){
			
			hql.append(" AND FORNEC.ID = :idFornecedor ");
		}
		
		if(filtro.getIdItemChamadaEncalheFornecedor() != null){
			
			hql.append(" AND ITEM_CH_ENC_FORNECEDOR.ID = :idItemChamadaEncalheFornecedor ");
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
		
		query.setParameter("anoReferencia",filtro.getAnoReferente());
		
		query.setParameter("numeroSemana",filtro.getNumeroSemana());
		
		query.setParameter("inicioSemana",filtro.getPeriodoRecolhimento().getDe());
		
		query.setParameter("fimSemana", filtro.getPeriodoRecolhimento().getAte());
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if(filtro.getIdItemChamadaEncalheFornecedor() != null) {
			query.setParameter("idItemChamadaEncalheFornecedor", filtro.getIdItemChamadaEncalheFornecedor());
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
		
		query.setParameter("numeroSemana", filtro.getNumeroSemana());
		query.setParameter("anoReferencia", filtro.getAnoReferente());
		query.setParameter("statusCeNDS", StatusCeNDS.FECHADO);
		
		return (query.uniqueResult() == null) ? false : true;
	}
	
	public FechamentoCEIntegracaoConsolidadoDTO obterConsolidadoCEIntegracao(Long idChamadaEncalheForncecdor){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" sum(COALESCE(ITEM_CHM_ENCALHE.QTDE_DEVOLUCAO_APURADA,0)) as totalVendaApurada,");
		
		hql.append(" sum(COALESCE(ITEM_CHM_ENCALHE.QTDE_VENDA_INFORMADA,0))   as totalVendaInformada,");
		
		hql.append(" sum(COALESCE(ITEM_CHM_ENCALHE.VALOR_VENDA_APURADO,0))    as totalCreditoApurado,"); 
		
		hql.append(" sum(COALESCE(ITEM_CHM_ENCALHE.VALOR_VENDA_INFORMADO,0))  as totalCreditoInformado,");
		
		hql.append(" sum(COALESCE(ITEM_CHM_ENCALHE.VALOR_MARGEM_APURADO,0))   as totalMargemInformado, ");
		
		hql.append(" sum(COALESCE(ITEM_CHM_ENCALHE.VALOR_MARGEM_INFORMADO,0)) as toatalMargemApurado"); 
		 		  
		hql.append(" from chamada_encalhe_fornecedor CHM_ENCALHE ");
		
		hql.append(" join item_chamada_encalhe_fornecedor ITEM_CHM_ENCALHE");
		
		hql.append(" on( ");
		
		hql.append("   ITEM_CHM_ENCALHE.CHAMADA_ENCALHE_FORNECEDOR_ID = CHM_ENCALHE.ID");
		
		hql.append("   )");
		
		hql.append(" where CHM_ENCALHE.ID = :idChamadaEncalheForncecdor ");
		
		Query  query = getSession().createSQLQuery(hql.toString())
									.addScalar("totalVendaApurada", StandardBasicTypes.BIG_DECIMAL)
									.addScalar("totalVendaInformada", StandardBasicTypes.BIG_DECIMAL)
									.addScalar("totalCreditoApurado", StandardBasicTypes.BIG_DECIMAL)
									.addScalar("totalCreditoInformado", StandardBasicTypes.BIG_DECIMAL)
									.addScalar("totalMargemInformado", StandardBasicTypes.BIG_DECIMAL)
									.addScalar("toatalMargemApurado", StandardBasicTypes.BIG_DECIMAL)
									.setResultTransformer(Transformers.aliasToBean(FechamentoCEIntegracaoConsolidadoDTO.class));		
		
		query.setParameter("idChamadaEncalheForncecdor", idChamadaEncalheForncecdor);
		
		return (FechamentoCEIntegracaoConsolidadoDTO) query.uniqueResult();
	}

}
