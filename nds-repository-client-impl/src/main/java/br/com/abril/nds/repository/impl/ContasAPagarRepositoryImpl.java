package br.com.abril.nds.repository.impl;

import static org.apache.commons.lang.StringUtils.leftPad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ContasAPagarConsultaProdutoVO;
import br.com.abril.nds.client.vo.ContasApagarConsultaPorDistribuidorVO;
import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ContasAPagarRepositoryImpl extends AbstractRepository implements ContasAPagarRepository{

	@Override
	public Integer pesquisarPorDistribuidorCount(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(true, false, false, filtro));
		
		this.setarParametrosQueryContasAPagar(query, filtro, true, false);
		query.setParameter("grupoRecebFis", GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
		return query.list().size();
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasApagarConsultaPorDistribuidorVO> pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(false, false, true, filtro));
		
		this.setarParametrosQueryContasAPagar(query, filtro, false, true);
		query.setParameter("grupoRecebFis", GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		if (paginacaoVO != null){
			
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
			query.setFirstResult(paginacaoVO.getQtdResultadosPorPagina() * (paginacaoVO.getPaginaAtual() - 1));
		}
		
		return query.list();
	}
	
	@Override
	public BigDecimal buscarTotalPesquisarPorDistribuidor(FiltroContasAPagarDTO filtro, boolean desconto){
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(false, true, desconto, filtro));
		
		this.setarParametrosQueryContasAPagar(query, filtro, true, desconto);
		query.setParameter("grupoRecebFis", GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
		return (BigDecimal) query.uniqueResult();
	}
	
	private String montarQueryPorDistribuidor(boolean count, boolean totais, boolean desconto, FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if (count){
			
			hql.append("select (l.dataRecolhimentoDistribuidor) ");
		} else {
			
			if (totais){
				
				hql.append("select ");
			} else {
				
				hql.append("select new ")
				   .append(ContasApagarConsultaPorDistribuidorVO.class.getCanonicalName());
				
				hql.append("( l.dataRecolhimentoDistribuidor as dataMovimento, ");
			}
			
			hql.append(" coalesce(");
			
			if (desconto){
				
				hql.append(" sum((prEd.precoVenda - (prEd.precoVenda * (")
				   .append(" case when prEd.origem = :origemInterface ")
				   .append(" then (coalesce(descLogPrEd.percentualDesconto, descLogPr.percentualDesconto, 0) / 100) ")
				   .append(" else (coalesce(prEd.desconto, pr.desconto, 0) / 100) ")
				   .append(" end) ")
				   .append(" ) ) * me.qtde) ");
			} else {
				
				hql.append(" sum((prEd.precoVenda * me.qtde)) ");
			}
			
			hql.append(",0) ");
			
			if (!totais){
			   
				hql.append(" as consignado, ");
    			//encalhe
    			hql.append(" coalesce((select sum( ");
    			
    			final String qtdsEncalhe = 
    			        "(coalesce(estProd.qtdeDevolucaoEncalhe,0) + coalesce(estProd.qtdeJuramentado,0) + " +
    			        " coalesce(estProd.qtdeDanificado,0) - coalesce(estProd.qtdePerda,0) + coalesce(estProd.qtdeGanho,0))";
    			
    			if (desconto){
                    
                    hql.append(" (").append(qtdsEncalhe).append(" * (prEd2.precoVenda - (prEd2.precoVenda * ( ")
                       .append(" case when prEd2.origem = :origemInterface ")
                       .append(" then (coalesce(descLogPrEd2.percentualDesconto, descLogPr2.percentualDesconto, 0) / 100) ")
                       .append(" else (coalesce(prEd2.desconto, pr2.desconto, 0) /100) ")
                       .append(" end) ")
                       .append(" ))) ");
                } else {
                    
                    hql.append(" (").append(qtdsEncalhe).append(" * prEd2.precoVenda) ");
                }
                
                hql.append(") ")
                   .append(" from EstoqueProduto estProd ")
                   .append(" join estProd.produtoEdicao prEd2 ")
                   .append(" join prEd2.lancamentos lanc ")
                   .append(" left join prEd2.descontoLogistica descLogPrEd2")
                   .append(" join prEd2.produto pr2 ")
                   .append(" left join pr2.descontoLogistica descLogPr2 ")
                   .append(" left join pr2.fornecedores fornecedor2 ")
                   .append(" where lanc.dataRecolhimentoDistribuidor = l.dataRecolhimentoDistribuidor ")
                   .append(" and lanc.status in (:statusLancamento) ");
                   
                if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
                    
                    hql.append(" and fornecedor2.id in (:fornecedores) ");
                }
    			
    		    hql.append(" ) ,0) ")
    		       .append(" as encalhe, ")
    			
    			//estoque
    		       .append(" coalesce((select sum( ");
    			
    			if (desconto){
    				
    			    hql.append(" ((estProd.qtde + coalesce(estProd.qtdeSuplementar,0))* (prEd2.precoVenda - (prEd2.precoVenda * ( ")
    				   .append(" case when prEd2.origem = :origemInterface ")
    				   .append(" then (coalesce(descLogPrEd2.percentualDesconto, descLogPr2.percentualDesconto, 0) / 100) ")
                       .append(" else (coalesce(prEd2.desconto, pr2.desconto, 0) /100) ")
                       .append(" end) ")
                       .append(" ))) ");
    			} else {
    				
    				hql.append(" ((estProd.qtde + coalesce(estProd.qtdeSuplementar,0)) * prEd2.precoVenda) ");
    			}
    			
    			hql.append(") ")
    			   .append(" from EstoqueProduto estProd ")
    			   .append(" join estProd.produtoEdicao prEd2 ")
    			   .append(" join prEd2.lancamentos lanc ")
    			   .append(" left join prEd2.descontoLogistica descLogPrEd2")
    			   .append(" join prEd2.produto pr2 ")
    			   .append(" left join pr2.descontoLogistica descLogPr2 ")
    			   .append(" left join pr2.fornecedores fornecedor2 ")
    			   .append(" where lanc.dataRecolhimentoDistribuidor = l.dataRecolhimentoDistribuidor ");
    			   
    			if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
    			    
    			    hql.append(" and fornecedor2.id in (:fornecedores) ");
    			}
    			
    			hql.append(") ,0) ")
    		   	   .append(" as estoque, ");
    			
    			//FaltasSobras
    			hql.append("sum(coalesce((select sum( ");
    			
    			if (desconto){
    				
    				hql.append(" (estProd.qtde * (prEd2.precoVenda - (prEd2.precoVenda * ( ")
    				   .append(" case when prEd2.origem = :origemInterface ")
    				   .append(" then (coalesce(descLogPrEd2.percentualDesconto, descLogPr2.percentualDesconto, 0) / 100) ")
    				   .append(" else (coalesce(prEd2.desconto, pr2.desconto, 0) /100) ")
    				   .append(" end) ")
    				   .append(" ))) ");
    			} else {
    				
    				hql.append(" (estProd.qtde * prEd2.precoVenda) ");
    			}
    			
    			hql.append(") ")
    			   .append(" from EstoqueProduto estProd ")
                   .append(" join estProd.produtoEdicao prEd2 ")
                   .append(" join prEd2.lancamentos lanc ")
                   .append(" left join prEd2.descontoLogistica descLogPrEd2")
                   .append(" join prEd2.produto pr2 ")
                   .append(" left join pr2.descontoLogistica descLogPr2 ")
                   .append(" left join pr2.fornecedores fornecedor2 ")
                   .append(" join estProd.movimentos me ")
                   .append(" where lanc.dataRecolhimentoDistribuidor = l.dataRecolhimentoDistribuidor ")
                   .append(" and lanc.status in (:statusLancamento) ");
    			   
    			if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
    			    
    			    hql.append(" and fornecedor2.id in (:fornecedores) ");
    			}
    			
    			hql.append(" and (me.tipoMovimento.grupoMovimentoEstoque = :tipoDiferencaSobraEm or me.tipoMovimento.grupoMovimentoEstoque = :tipoDiferencaSobraDe)")
    		       .append(" ) ,0) - ")
    		       
    		       .append("coalesce((select sum( ");
    			
    			if (desconto){
    			    
    				hql.append(" (estProd.qtde * (prEd2.precoVenda - (prEd2.precoVenda * ( ")
    				   .append(" case when prEd2.origem = :origemInterface ")
    				   .append(" then (coalesce(descLogPrEd2.percentualDesconto, descLogPr2.percentualDesconto, 0) / 100) ")
    				   .append(" else (coalesce(prEd2.desconto, pr2.desconto, 0) /100) ")
    				   .append(" end) ")
    				   .append(" ))) ");
    			} else {
    				
    				hql.append(" (estProd.qtde * prEd2.precoVenda) ");
    			}
    			
    			hql.append(") ")
                   .append(" from EstoqueProduto estProd ")
                   .append(" join estProd.produtoEdicao prEd2 ")
                   .append(" join prEd2.lancamentos lanc ")
                   .append(" left join prEd2.descontoLogistica descLogPrEd2")
                   .append(" join prEd2.produto pr2 ")
                   .append(" left join pr2.descontoLogistica descLogPr2 ")
                   .append(" left join pr2.fornecedores fornecedor2 ")
                   .append(" join estProd.movimentos me ")
                   .append(" where lanc.dataRecolhimentoDistribuidor = l.dataRecolhimentoDistribuidor ")
                   .append(" and lanc.status in (:statusLancamento) ");
    			
    			if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
    			    
    			    hql.append(" and fornecedor2.id in (:fornecedores) ");
    			}
    			
    			hql.append(" and (me.tipoMovimento.grupoMovimentoEstoque = :tipoDiferencaFaltaEm or me.tipoMovimento.grupoMovimentoEstoque = :tipoDiferencaFaltaDe)")
    		       .append(" ) ,0)) ");
    			
    			hql.append(" as faltasSobras, ");
    			
    			//PerdasGanhos
    			hql.append("coalesce((select sum( ");
    			
    			if (desconto){
    				
    			    hql.append(" (dif.qtde * (prEd6.precoVenda - (prEd6.precoVenda * ( ")
    			       .append(" case when prEd6.origem = :origemInterface ")
    			       .append(" then (coalesce(descLogPrEd6.percentualDesconto, descLogPr6.percentualDesconto, 0) / 100) ")
    			       .append(" else (coalesce(prEd6.desconto, pr6.desconto, 0) /100) ")
    			       .append(" end) ")
    			       .append(" ))) ");
    			} else {
    				
    				hql.append(" (dif.qtde * prEd6.precoVenda) ");
    			}
    			
    			hql.append(") ")
    			   .append(" from LancamentoDiferenca ld3 ")
    			   .append(" join ld3.diferenca dif ")
    			   .append(" join dif.produtoEdicao prEd6 ")
    			   .append(" left join prEd6.descontoLogistica descLogPrEd6 ")
    			   .append(" join prEd6.produto pr6 ")
    			   .append(" left join pr6.descontoLogistica descLogPr6 ")
    			   .append(" left join pr6.fornecedores fornecedor6 ")
    			   .append(" where ld3.dataProcessamento = l.dataRecolhimentoDistribuidor ");
    			
    			if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
    			    
    			    hql.append(" and fornecedor6.id in (:fornecedores) ");
    			}
    			
    			hql.append(" and dif.qtde is not null ")
    		   	   .append(" and prEd6.precoVenda is not null ")
    		   	   .append(" and ld3.status = :statusPerda ")
    		   	   .append(" group by dif.tipoDiferenca),0) + ")
    		   	   
    		   	   .append("coalesce((select sum( ");
    			
    			if (desconto){
    				
    			    hql.append(" (dif.qtde * (prEd7.precoVenda - (prEd7.precoVenda * ( ")
    			       .append(" case when prEd7.origem = :origemInterface ")
    			       .append(" then (coalesce(descLogPrEd7.percentualDesconto, descLogPr7.percentualDesconto, 0) / 100) ")
    			       .append(" else (coalesce(prEd7.desconto, pr7.desconto, 0) /100) ")
    			       .append(" end) ")
    			       .append(" ))) ");
    			} else {
    				
    				hql.append(" (dif.qtde * prEd7.precoVenda) ");
    			}
    			
    			hql.append(") ")
    			   .append(" from LancamentoDiferenca ld4 ")
    			   .append(" join ld4.diferenca dif ")
    			   .append(" join dif.produtoEdicao prEd7 ")
    			   .append(" left join prEd7.descontoLogistica descLogPrEd7 ")
    			   .append(" join prEd7.produto pr7 ")
    			   .append(" left join pr7.descontoLogistica descLogPr7 ")
    			   .append(" left join pr7.fornecedores fornecedor7 ")
    			   .append(" where ld4.dataProcessamento = l.dataRecolhimentoDistribuidor ");
    			
    			if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
    			    
    			    hql.append(" and fornecedor7.id in (:fornecedores) ");
    			}
    			
    			hql.append(" and ld4.diferenca.qtde is not null ")
    		   	   .append(" and prEd.precoVenda is not null ")
    		   	   .append(" and ld4.status = :statusGanho ")
    		   	   .append(" group by dif.tipoDiferenca),0) ");
    			
    			hql.append(" as perdasGanhos) ");
		    }
		}
		
		hql.append(" from Lancamento l ")
		   .append(" join l.produtoEdicao prEd ")
		   .append(" left join prEd.movimentoEstoques me ")
		   .append(" left join me.tipoMovimento tpMov ")
		   .append(" left join prEd.descontoLogistica descLogPrEd")
		   .append(" join prEd.produto pr ")
		   .append(" left join pr.descontoLogistica descLogPr ")
		   .append(" left join pr.fornecedores f ");      
		
		hql.append(" where l.dataRecolhimentoDistribuidor between :inicio and :fim ")
		   .append(" and l.status in (:statusLancamento) ")
		   .append(" and (tpMov.grupoMovimentoEstoque = :grupoRecebFis or tpMov.grupoMovimentoEstoque is null) ");
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
		    
		    hql.append(" and f.id in (:fornecedores) ");
		}
		
		if (!totais){
			
			hql.append(" group by l.dataRecolhimentoDistribuidor ")
			   .append(" order by l.dataRecolhimentoDistribuidor desc ");
		}
		
		if (!count && !totais){
		
			PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
			
			if (paginacaoVO != null && !"data".equals(paginacaoVO.getSortColumn()) &&
					paginacaoVO.getSortColumn() != null){
				
				hql.append(", ")
				   .append(paginacaoVO.getSortColumn())
				   .append(" ")
				   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
			}
		}
		
		return hql.toString();
	}
	
	private void setarParametrosQueryContasAPagar(Query query, FiltroContasAPagarDTO filtro, 
	        boolean totais, boolean desconto){
		
		query.setParameter("inicio", filtro.getDataDe());
		query.setParameter("fim", filtro.getDataAte());
		
		query.setParameterList("statusLancamento",
		        Arrays.asList(
		                StatusLancamento.RECOLHIDO,
		                StatusLancamento.EM_RECOLHIMENTO,
		                StatusLancamento.FECHADO
		        )
		);
		
		if (!totais){
		    
            query.setParameter("tipoDiferencaFaltaEm", GrupoMovimentoEstoque.FALTA_EM);
            query.setParameter("tipoDiferencaFaltaDe", GrupoMovimentoEstoque.FALTA_DE);
            query.setParameter("tipoDiferencaSobraEm", GrupoMovimentoEstoque.SOBRA_EM);
            query.setParameter("tipoDiferencaSobraDe", GrupoMovimentoEstoque.SOBRA_DE);
            
            query.setParameter("statusPerda", StatusAprovacao.PERDA);
            query.setParameter("statusGanho", StatusAprovacao.GANHO);
		}
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			query.setParameterList("fornecedores", filtro.getIdsFornecedores());
		}
		
        if (filtro.getProdutoEdicaoIDs() != null && !filtro.getProdutoEdicaoIDs().isEmpty()){
			
			query.setParameterList("idsProdutoEdicao", filtro.getProdutoEdicaoIDs());
		}
        
        if (desconto){
            query.setParameter("origemInterface", Origem.INTERFACE);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContasApagarConsultaPorProdutoDTO> pesquisarPorProduto(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(this.montarQueryPorProduto(filtro));

		this.setarParametrosQueryContasAPagar(query, filtro, false, false);
		
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		if (paginacaoVO != null){
			
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
			query.setFirstResult(paginacaoVO.getQtdResultadosPorPagina() * (paginacaoVO.getPaginaAtual() - 1));
		}
		
		return query.list();
	}
	
	@Override
	public ContasAPagarGridPrincipalProdutoDTO pesquisarTotaisPorProduto(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(this.montarQueryTotaisPorProduto(filtro));

		this.setarParametrosQueryContasAPagar(query, filtro, true, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarGridPrincipalProdutoDTO.class));
		
		return (ContasAPagarGridPrincipalProdutoDTO) query.uniqueResult();
	}
	
	@Override
	public Long pesquisarCountPorProduto(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(this.montarQueryQuantidadePorProduto(filtro));

		this.setarParametrosQueryContasAPagar(query, filtro, true, false);
		
		return (Long) query.uniqueResult();
	}
	
	private String montarQueryPorProduto(FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();

		hql.append("select new ")
		   .append(ContasApagarConsultaPorProdutoDTO.class.getCanonicalName())
		   .append("(")
		   .append("       produtoEdicao.id as produtoEdicaoId, ")
		   .append("       l.dataRecolhimentoDistribuidor as rctl,")
		   .append("       produto.codigo as codigo, ")
		   .append("       produto.nome as produto, ")
		   .append("       produtoEdicao.numeroEdicao as edicao, ")
		   .append("       produtoEdicao.parcial as tipo, ")	   
		   .append("       l.reparte as reparte, ")
		   .append("       f.juridica.nomeFantasia as fornecedor,")
		   
		   .append(this.queryPorProduto(filtro))
		
		   .append(")")
		
		   .append(this.queryPorProdutoFrom(filtro))
         
		   .append(" group by produtoEdicao.id ");
	    
        PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		if (paginacaoVO != null){
		
			hql.append(" order by ")
		       .append(paginacaoVO.getSortColumn()!=null && !paginacaoVO.getSortColumn().equals("")?paginacaoVO.getSortColumn():" rctl ")
		       .append(" ")
		       .append(paginacaoVO.getSortOrder()!=null && !paginacaoVO.getSortOrder().equals("")?paginacaoVO.getSortOrder():" desc ");
		}
		
	    return hql.toString();
	}
	
	private String montarQueryQuantidadePorProduto(FiltroContasAPagarDTO filtro){    	
	    	
    	StringBuilder hql = new StringBuilder();

    	hql.append(" Select count(distinct produtoEdicao.id) ")
    	
	       .append(this.queryPorProdutoFrom(filtro));

		return hql.toString();
    }
	
	private String montarQueryTotaisPorProduto(FiltroContasAPagarDTO filtro){    	
    	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ")
		   .append("       COALESCE(sum(produtoEdicao.precoVenda * l.reparte),0) as totalPagto, ")     
		   .append("       COALESCE(sum( COALESCE((produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0) * l.reparte ),0) as totalDesconto, ")
		   .append("       COALESCE(sum( ( produtoEdicao.precoVenda - COALESCE((produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0) )  * l.reparte ),0) as valorLiquido ")

	       .append(this.queryPorProdutoFrom(filtro));

		return hql.toString();
    }
	
    private String queryPorProduto(FiltroContasAPagarDTO filtro){

		final StringBuilder hql = new StringBuilder();
		
		final String parametroData = " l.dataRecolhimentoDistribuidor ";
			
		hql.append(" (select  ")
		   .append(" sum(estProd.qtde + coalesce(estProd.qtdeSuplementar,0)) ")
		   .append(" from EstoqueProduto estProd ")
           .append(" join estProd.produtoEdicao prEd2 ")
           .append(" join prEd2.lancamentos lanc ")
           .append(" left join prEd2.descontoLogistica descLogPrEd2")
           .append(" join prEd2.produto pr2 ")
           .append(" left join pr2.descontoLogistica descLogPr2 ")
           .append(" left join pr2.fornecedores fornecedor2 ")
           .append(" where lanc.dataRecolhimentoDistribuidor = ").append(parametroData)
           .append(" and prEd2.id = produtoEdicao.id ")
           .append(" ) as suplementacao, ")
           
		   .append(" (select sum((coalesce(estProd.qtdeDevolucaoEncalhe,0) + coalesce(estProd.qtdeJuramentado,0) + ")
           .append(" coalesce(estProd.qtdeDanificado,0) - coalesce(estProd.qtdePerda,0) + coalesce(estProd.qtdeGanho,0))) ")
           .append(" from EstoqueProduto estProd ")
           .append(" join estProd.produtoEdicao prEd2 ")
           .append(" join prEd2.lancamentos lanc ")
           .append(" left join prEd2.descontoLogistica descLogPrEd2")
           .append(" join prEd2.produto pr2 ")
           .append(" left join pr2.descontoLogistica descLogPr2 ")
           .append(" left join pr2.fornecedores fornecedor2 ")
           .append(" where lanc.dataRecolhimentoDistribuidor = ").append(parametroData)
           .append(" and prEd2.id = produtoEdicao.id ")
           .append(" and lanc.status in (:statusLancamento)) as encalhe, ")
           
           .append("	   (COALESCE((select sum(ld2.diferenca.qtde) ")
		   .append("                  from LancamentoDiferenca ld2 ")	   
		   .append(" 		 		  where ld2.dataProcessamento = ").append(parametroData)
		   .append(" 		          and ld2.diferenca.qtde is not null ")
		   .append(" 		          and ld2.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" 		          and (ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraEm or ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraDe)")
		   .append(" 		          and ld2.diferenca.produtoEdicao.id = produtoEdicao.id ")	    
		   .append(" 		          group by ld2.diferenca.tipoDiferenca) ")
		   .append("        ,0) - ")		   
		   .append("	    COALESCE((select sum(ld.diferenca.qtde) ")
		   .append(" 		          from LancamentoDiferenca ld ")		   
		   .append(" 		 		  where ld.dataProcessamento = ").append(parametroData)	   
		   .append(" 		          and ld.diferenca.qtde is not null ")
		   .append(" 		          and ld.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" 		          and (ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaEm or ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaDe)")
		   .append(" 		          and ld.diferenca.produtoEdicao.id = produtoEdicao.id ")	    
		   .append(" 		          group by ld.diferenca.tipoDiferenca) ")
		   .append("        ,0) ")
	       .append("       ) as faltasSobras, ")
		
		   .append("       (COALESCE((select sum(ld3.diferenca.qtde) ")
		   .append("  		          from LancamentoDiferenca ld3 ")		   
		   .append(" 		 		  where ld3.dataProcessamento = ").append(parametroData)		   
		   .append("                  and ld3.diferenca.qtde is not null ")
		   .append("                  and ld3.diferenca.produtoEdicao.precoVenda is not null ")	  
		   .append("                  and ld3.diferenca.produtoEdicao.id = produtoEdicao.id ")	   
		   .append("                  and ld3.status = :statusPerda ")
	       .append("                  group by ld3.diferenca.tipoDiferenca) ")
	       .append("        ,0) + ")		   
		   .append("        COALESCE((select sum(ld4.diferenca.qtde) ")
		   .append("                  from LancamentoDiferenca ld4 ")
		   .append(" 		 		  where ld4.dataProcessamento = ").append(parametroData)
		   .append("                  and ld4.diferenca.qtde is not null ")
		   .append("                  and ld4.diferenca.produtoEdicao.precoVenda is not null ")	    
		   .append("                  and ld4.diferenca.produtoEdicao.id = produtoEdicao.id ")	
		   .append("                  and ld4.status = :statusGanho ")
		   .append("                  group by ld4.diferenca.tipoDiferenca) ")
		   .append("        ,0)")
		   .append("       ) as debitosCreditos, ")
		   		   
		   .append("       ( COALESCE(l.reparte, 0) - ")
		   .append("         COALESCE((select sum(movimento.qtde) from ConferenciaEncalhe conferencia ")
		   .append("                   join conferencia.movimentoEstoqueCota movimento ")
		   .append("                   join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		   .append("        	       join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")		   		   
		   .append(" 		 		   where chamadaEncalhe.dataRecolhimento = ").append(parametroData)		   		   
		   .append(" 			       and movimento.produtoEdicao.id = produtoEdicao.id ")	    
		   .append("         ),0)")
		   .append("       ) as venda, ")		   
		   
		   .append("       ( ")
		   .append("             COALESCE(sum( (produtoEdicao.precoVenda - COALESCE((produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0)) * l.reparte ),0) - ")
		   
		   .append("             COALESCE((select sum(movimento.qtde * (conferencia.produtoEdicao.precoVenda - COALESCE((conferencia.produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0))) ")
		   .append("                       from ConferenciaEncalhe conferencia ")
		   .append("                       join conferencia.movimentoEstoqueCota movimento ")
		   .append("        		       join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		   .append("        		       join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		   .append(" 		 		       where chamadaEncalhe.dataRecolhimento = ").append(parametroData)
		   .append(" 				       and movimento.produtoEdicao.id = produtoEdicao.id ")	    
		   .append("             ),0) - ")

		   .append("	         (COALESCE((select sum(ld2.diferenca.qtde * (ld2.diferenca.produtoEdicao.precoVenda - COALESCE((ld2.diferenca.produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0))) ")
		   .append("                        from LancamentoDiferenca ld2 ")		   
		   .append(" 		 		        where ld2.dataProcessamento = ").append(parametroData)		   
		   .append(" 		                and ld2.diferenca.qtde is not null ")
		   .append(" 		                and ld2.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" 		                and (ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraEm or ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraDe)")
		   .append(" 		                and ld2.diferenca.produtoEdicao.id = produtoEdicao.id ")	    
		   .append(" 		                group by ld2.diferenca.tipoDiferenca) ")
		   .append("              ,0) - ")		   
		   .append("	          COALESCE((select sum(ld.diferenca.qtde * (ld.diferenca.produtoEdicao.precoVenda - COALESCE((ld.diferenca.produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0))) ")
		   .append(" 		                from LancamentoDiferenca ld ")
		   .append(" 		 		        where ld.dataProcessamento = ").append(parametroData)	
		   .append(" 		                and ld.diferenca.qtde is not null ")
		   .append(" 		                and ld.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" 		                and (ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaEm or ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaDe)")
		   .append(" 		                and ld.diferenca.produtoEdicao.id = produtoEdicao.id ")	    
		   .append(" 		                group by ld.diferenca.tipoDiferenca) ")
		   .append("              ,0) ")
	       .append("             ) - ")
		
		   .append("             (COALESCE((select sum(ld3.diferenca.qtde * (ld3.diferenca.produtoEdicao.precoVenda - COALESCE((ld3.diferenca.produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0))) ")
		   .append("  		                from LancamentoDiferenca ld3 ")
		   .append(" 		 		        where ld3.dataProcessamento = ").append(parametroData)
		   .append("                        and ld3.diferenca.qtde is not null ")
		   .append("                        and ld3.diferenca.produtoEdicao.precoVenda is not null ")	  
		   .append("                        and ld3.diferenca.produtoEdicao.id = produtoEdicao.id ")	   
		   .append("                        and ld3.status = :statusPerda ")
	       .append("                        group by ld3.diferenca.tipoDiferenca) ")
	       .append("              ,0) + ")		   
		   .append("              COALESCE((select sum(ld4.diferenca.qtde * (ld4.diferenca.produtoEdicao.precoVenda - COALESCE((ld4.diferenca.produtoEdicao.precoVenda / 100) * f.margemDistribuidor,0))) ")
		   .append("                        from LancamentoDiferenca ld4 ")		   
		   .append(" 		 		        where ld4.dataProcessamento = ").append(parametroData)		   
		   .append("                        and ld4.diferenca.qtde is not null ")
		   .append("                        and ld4.diferenca.produtoEdicao.precoVenda is not null ")	    
		   .append("                        and ld4.diferenca.produtoEdicao.id = produtoEdicao.id ")	
		   .append("                        and ld4.status = :statusGanho ")
		   .append("                        group by ld4.diferenca.tipoDiferenca) ")
		   .append("              ,0)")
		   .append("             ) ")
	       .append("       ) as saldoAPagar ");
           
		return hql.toString();
	}

    private String queryPorProdutoFrom(FiltroContasAPagarDTO filtro){    	
    	
    	final StringBuilder hql = new StringBuilder(" from Lancamento l ")
	       .append(" join l.produtoEdicao produtoEdicao ")
	       .append(" join produtoEdicao.produto produto ")
	       .append(" join produto.fornecedores f ")
	       .append(" where l.dataRecolhimentoDistribuidor between :inicio and :fim ")
	       .append(" and l.status in (:statusLancamento) ")
	       .append(" and l.reparte is not null ")
	       .append(" and produtoEdicao.precoVenda is not null ");	      
    	
    	if (filtro.getProdutoEdicaoIDs() != null && !filtro.getProdutoEdicaoIDs().isEmpty()){
		    hql.append(" and produtoEdicao.id in (:idsProdutoEdicao) ");
    	}   
   
		return hql.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarConsignadoDTO> pesquisarDetalheConsignado(FiltroContasAPagarDTO filtro) {
	    
		StringBuilder hql = new StringBuilder();
		hql.append("select ")
		   .append("    produto2.codigo as codigo, ")
		   .append("    produto2.nomeComercial as produto, ")
		   .append("    produtoEdicao.numeroEdicao as edicao, ")
		   .append(" 	coalesce(produtoEdicao.precoVenda,0) as precoCapa, ")
		   .append(" 	coalesce(produtoEdicao.precoVenda,0) - (coalesce(produtoEdicao.precoVenda,0) * ( ")
		   .append("      case when produtoEdicao.origem = :origemInterface ")
		   .append("      then (coalesce(descLogPrEd.percentualDesconto, descLogPr.percentualDesconto, 0) / 100) ")
		   .append("      else (coalesce(produtoEdicao.desconto, produto2.desconto, 0) / 100) ")
		   .append("      end)) as precoComDesconto, ")
		   .append(" 	me.qtde as reparteSugerido, ")
		   .append(" 	me.qtde - (select sum(coalesce(dif.qtde,0)) from Diferenca dif join dif.produtoEdicao pe where pe.id = produtoEdicao.id) as reparteFinal, ")
		   .append(" 	(select sum(coalesce(dif.qtde,0)) from Diferenca dif join dif.produtoEdicao pe where pe.id = produtoEdicao.id) as diferenca, ")
		   .append(" 	coalesce(pessoa.razaoSocial, '') as fornecedor, ")
		   .append(" 	coalesce(produtoEdicao.precoVenda,0) * (me.qtde) as valor, ")

           .append(" coalesce(");
		    hql.append(" sum((produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (")
			   .append(" case when produtoEdicao.origem = :origemInterface ")
			   .append(" then (coalesce(descLogPrEd.percentualDesconto, descLogPr.percentualDesconto, 0) / 100) ")
			   .append(" else (coalesce(produtoEdicao.desconto, produto2.desconto, 0) / 100) ")
			   .append(" end) ")
			   .append(" ) ) * me.qtde) ");
		    hql.append(",0) as valorComDesconto ")

		   .append(" from ")
		   .append(" 	Lancamento l ")
		   .append(" 	join l.produtoEdicao produtoEdicao ")
		   .append("    left join produtoEdicao.movimentoEstoques me ")
		   .append("    left join me.tipoMovimento tpMov ")
		   .append("    left join produtoEdicao.descontoLogistica descLogPrEd ")
		   .append("	join produtoEdicao.produto produto2 ")
		   .append("    left join produto2.descontoLogistica descLogPr ")
		   .append("	left join produto2.fornecedores fo ")
		   .append("	left join fo.juridica pessoa ")
		   .append(" where l.dataRecolhimentoDistribuidor = :data ")
		   .append(" 	and l.status in (:statusLancamento) ")
		   .append("    and (tpMov.grupoMovimentoEstoque = :grupoRecebFis or tpMov.grupoMovimentoEstoque is null) ");
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			hql.append(" and produto2.nomeComercial = :nomeProduto ");
		}
		
		if (filtro.getEdicao() != null){
			
			hql.append(" and produtoEdicao.numeroEdicao = :numeroEdicao ");
		}
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
		    
		    hql.append(" and fo.id in (:fornecedores) ");
		}
		
		hql.append(" group by produtoEdicao.id ")
		   .append(" order by ")
		   .append("	pessoa.razaoSocial ");
		
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		if (paginacaoVO != null && paginacaoVO.getSortColumn() != null){
			
			hql.append(", ")
			   .append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", filtro.getDataDetalhe());
		
		query.setParameterList("statusLancamento", 
		        Arrays.asList(
		                StatusLancamento.RECOLHIDO,
		                StatusLancamento.EM_RECOLHIMENTO,
		                StatusLancamento.FECHADO
		        )
		);
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			query.setParameter("nomeProduto", filtro.getProduto());
		}
		
		if (filtro.getEdicao() != null){
			
			query.setParameter("numeroEdicao", filtro.getEdicao());
		}
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
		    
		    query.setParameterList("fornecedores", filtro.getIdsFornecedores());
		}
		
		query.setParameter("origemInterface", Origem.INTERFACE);
		
		query.setParameter("grupoRecebFis", GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarConsignadoDTO.class));
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro){
		
	    final String qtdsEncalhe = 
                " coalesce(estProd.qtdeDevolucaoEncalhe,0) + coalesce(estProd.qtdeJuramentado,0) + " +
                " coalesce(estProd.qtdeDanificado,0) - coalesce(estProd.qtdePerda,0) + coalesce(estProd.qtdeGanho,0)";
	    
		StringBuilder hql = new StringBuilder();
		hql.append("select ")
		   .append("    produto2.codigo as codigo, ")
		   .append("    produto2.nomeComercial as produto, ")
		   .append("    produtoEdicao.numeroEdicao as edicao, ")
		   .append(" 	coalesce(produtoEdicao.precoVenda,0) as precoCapa, ")
		   .append("    coalesce(produtoEdicao.precoVenda,0) - (coalesce(produtoEdicao.precoVenda,0) * ( ")
           .append("      case when produtoEdicao.origem = :origemInterface ")
           .append("      then (coalesce(descLogPrEd2.percentualDesconto, descLogPr2.percentualDesconto, 0) / 100) ")
           .append("      else (coalesce(produtoEdicao.desconto, produto2.desconto, 0) / 100) ")
           .append("      end)) as precoComDesconto, ")
		   .append(" 	(").append(qtdsEncalhe).append(") as encalhe, ")
		   .append(" 	coalesce(pessoa.razaoSocial, '') as fornecedor, ")
		   .append("    (coalesce(produtoEdicao.precoVenda,0) - (coalesce(produtoEdicao.precoVenda,0) * ( ")
           .append("      case when produtoEdicao.origem = :origemInterface ")
           .append("      then (coalesce(descLogPrEd2.percentualDesconto, descLogPr2.percentualDesconto, 0) / 100) ")
           .append("      else (coalesce(produtoEdicao.desconto, produto2.desconto, 0) / 100) ")
           .append("      end))) * (").append(qtdsEncalhe).append(") as valor ")
		   .append(" from EstoqueProduto estProd ")
           .append(" join estProd.produtoEdicao produtoEdicao ")
           
           .append(" left join produtoEdicao.movimentoEstoques me ")
           .append(" left join me.tipoMovimento tpMov ")
           
           .append(" join produtoEdicao.lancamentos lanc ")
           .append(" left join produtoEdicao.descontoLogistica descLogPrEd2")
           .append(" join produtoEdicao.produto produto2 ")
           .append(" left join produto2.descontoLogistica descLogPr2 ")
           .append(" left join produto2.fornecedores fornecedor2 ")
           .append(" join fornecedor2.juridica pessoa ")
           .append(" where lanc.dataRecolhimentoDistribuidor = :data ")
           .append(" and lanc.status in (:statusLancamento) ")
           .append(" and (tpMov.grupoMovimentoEstoque = :grupoRecebFis or tpMov.grupoMovimentoEstoque is null) ");
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			hql.append(" and produto2.nomeComercial = :nomeProduto ");
		}
		
		if (filtro.getEdicao() != null){
			
			hql.append(" and produtoEdicao.numeroEdicao = :numeroEdicao ");
		}
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
		    
		    hql.append(" and fornecedor2.id in (:fornecedores) ");
		}
		
		hql.append(" order by ")
		   .append("	pessoa.razaoSocial ");
		
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		if (paginacaoVO != null && paginacaoVO.getSortColumn() != null){
			
			hql.append(", ")
			   .append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarEncalheDTO.class));
		
		query.setParameter("data", filtro.getDataDetalhe());
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			query.setParameter("nomeProduto", filtro.getProduto());
		}
		
		if (filtro.getEdicao() != null){
			
			query.setParameter("numeroEdicao", filtro.getEdicao());
		}
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
            
            query.setParameterList("fornecedores", filtro.getIdsFornecedores());
        }
		
		query.setParameter("origemInterface", Origem.INTERFACE);
		
		query.setParameterList("statusLancamento",
                Arrays.asList(
                        StatusLancamento.RECOLHIDO,
                        StatusLancamento.EM_RECOLHIMENTO,
                        StatusLancamento.FECHADO
                )
        );
        
        query.setParameter("grupoRecebFis", GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro){
		
	    StringBuilder hql = new StringBuilder();
		hql.append("select ")
		   .append("    produto2.codigo as codigo, ")
		   .append("    produto2.nomeComercial as produto, ")
		   .append("    produtoEdicao.numeroEdicao as edicao, ")
		   .append(" 	produtoEdicao.precoVenda as precoCapa, ")
		   .append(" 	produtoEdicao.precoVenda - (produtoEdicao.precoVenda * ")
		   .append("    case when produtoEdicao.origem = :origemInterface ")
		   .append("    then (coalesce(descLogPrEd.percentualDesconto, descLogPr.percentualDesconto, 0) / 100) ")
		   .append("    else (coalesce(produtoEdicao.desconto, produto2.desconto, 0) /100) end ")
		   .append("    ) as precoComDesconto,")
		   .append("	estProd.qtde as exemplares, ")
		   .append(" 	coalesce(pessoa.razaoSocial, '') as fornecedor, ")
		   .append("    estProd.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * ")
           .append("    case when produtoEdicao.origem = :origemInterface ")
           .append("    then (coalesce(descLogPrEd.percentualDesconto, descLogPr.percentualDesconto, 0) / 100) ")
           .append("    else (coalesce(produtoEdicao.desconto, produto2.desconto, 0) /100) end) ")
           .append("    ) as valor ")		   
		   
		   .append(" from EstoqueProduto estProd ")
           .append(" join estProd.produtoEdicao produtoEdicao ")
           .append(" join produtoEdicao.lancamentos lanc ")
           .append(" left join produtoEdicao.descontoLogistica descLogPrEd ")
           .append(" join produtoEdicao.produto produto2 ")
           .append(" left join produto2.descontoLogistica descLogPr ")
           .append(" left join produto2.fornecedores fo ")
           .append(" left join fo.juridica pessoa ")
           .append(" left join estProd.movimentos me ")
           .append(" left join me.tipoMovimento tpMov ")
           .append(" where lanc.dataRecolhimentoDistribuidor = :data ")
           .append(" and lanc.status in (:statusLancamento) ")
           .append(" and tpMov.grupoMovimentoEstoque in (:gruposMov) ");
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			hql.append(" and produto2.nomeComercial = :nomeProduto ");
		}
		
		if (filtro.getEdicao() != null){
			
			hql.append(" and produtoEdicao.numeroEdicao = :numeroEdicao ");
		}
		
		hql.append(" order by ")
		   .append("	pessoa.razaoSocial ");
		
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		if (paginacaoVO != null && paginacaoVO.getSortColumn() != null){
			
			hql.append(", ")
			   .append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", filtro.getDataDetalhe());
		query.setParameter("origemInterface", Origem.INTERFACE);
		
		query.setParameterList("statusLancamento",
                Arrays.asList(
                        StatusLancamento.RECOLHIDO,
                        StatusLancamento.EM_RECOLHIMENTO,
                        StatusLancamento.FECHADO
                )
        );
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			query.setParameter("nomeProduto", filtro.getProduto());
		}
		
		if (filtro.getEdicao() != null){
			
			query.setParameter("numeroEdicao", filtro.getEdicao());
		}
		
		query.setParameterList("gruposMov", 
		        Arrays.asList(
		                GrupoMovimentoEstoque.FALTA_EM, 
		                GrupoMovimentoEstoque.FALTA_DE, 
		                GrupoMovimentoEstoque.SOBRA_EM, 
		                GrupoMovimentoEstoque.SOBRA_DE
		        )
		);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarFaltasSobrasDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarConsultaProdutoVO> obterProdutos(final FiltroContasAPagarDTO filtro) {
		StringBuilder sql = new StringBuilder();


        sql.append(" SELECT  p.codigo as codigo, p.nome as produto, ");
        sql.append("	 pe.numeroEdicao as edicao, pe.precoVenda as precoCapa, pe.id as produtoEdicaoID, ");
        sql.append(" 	 fornec.nomeFantasia as fornecedor, edi.razaoSocial as editor ");
        sql.append(" FROM ProdutoEdicao pe ");
        sql.append(" JOIN  pe.produto p");
        sql.append(" JOIN  p.editor e");
        sql.append(" JOIN  p.fornecedores f");
        sql.append(" JOIN  f.juridica fornec");
        sql.append(" JOIN  e.pessoaJuridica edi");
        sql.append(" JOIN  pe.lancamentos lan ");
        
        if(	filtro.getProduto()!=null || filtro.getEdicao()!=null ){
        	sql.append(" WHERE ");
	        
        	if(filtro.getProduto() != null){
        	    sql.append("  (p.codigo = :codigoProduto OR p.codigoICD = :codigoICD OR p.codigo = :codigoProdin) " );
	        }
	        
	        if(filtro.getEdicao() != null){
	        	sql.append(" AND    pe.numeroEdicao = :numeroEdicao ");
	        }
        }
        
        sql.append(" group by pe.id ");
        
        if (filtro.getPaginacaoVO() != null && filtro.getPaginacaoVO().getSortColumn() != null){
            
            sql.append(" order by ");
            
            if ("edicao".equals(filtro.getPaginacaoVO().getSortColumn())){
                
                sql.append("codigo, edicao ");
            } else {
                
                sql.append(filtro.getPaginacaoVO().getSortColumn());
            }
            
            if (filtro.getPaginacaoVO().getOrdenacao() != null){
                
                sql.append(" ").append(filtro.getPaginacaoVO().getOrdenacao());
            }
        } else {
            
            sql.append(" order by lan.dataRecolhimentoDistribuidor desc, p.codigo, pe.numeroEdicao");
        }
        
        Query query = getSession().createQuery(sql.toString());
        if(filtro.getProduto() != null){
            query.setParameter("codigoProduto", leftPad(filtro.getProduto(), 8, "0"));
            query.setParameter("codigoICD", filtro.getProduto());
            query.setParameter("codigoProdin", leftPad(filtro.getProduto().concat("01"), 8, "0"));
        }
        
        if(filtro.getEdicao()!=null){
        	query.setParameter("numeroEdicao", filtro.getEdicao());            
        }
        query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarConsultaProdutoVO.class));
        return query.list();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro){
		
		Query query = this.getSession().createQuery(this.obterQueryPesquisaParcial(filtro, false));
		query.setParameter("data", filtro.getDataDetalhe());
		
		//movimentos de grupo de estoque suplementar de entrada
		List<GrupoMovimentoEstoque> movimentosSuplementar = new ArrayList<GrupoMovimentoEstoque>();
		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		movimentosSuplementar.add(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO);
		movimentosSuplementar.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR);
		movimentosSuplementar.add(GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		movimentosSuplementar.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		query.setParameterList("movimentosSuplementarEntrada", movimentosSuplementar);
		
		//movimentos de grupo de estoque suplementar de saida
		movimentosSuplementar = new ArrayList<GrupoMovimentoEstoque>();
		movimentosSuplementar.add(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
		movimentosSuplementar.add(GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR);
		movimentosSuplementar.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		query.setParameterList("movimentosSuplementarSaida", movimentosSuplementar);
		
		query.setParameter("data", filtro.getDataDetalhe());
		query.setParameter("statusLancamento", StatusLancamento.CONFIRMADO);
		query.setParameter("codigoProduto", filtro.getProduto());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarParcialDTO.class));
		
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		if (paginacaoVO != null){
			
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
			query.setFirstResult(paginacaoVO.getQtdResultadosPorPagina() * (paginacaoVO.getPaginaAtual() - 1));
		}
		
		return query.list();
	}
	
	@Override
	public Long countPesquisarParcial(FiltroContasAPagarDTO filtro){
		
		Query query = this.getSession().createQuery(this.obterQueryPesquisaParcial(filtro, true));
		query.setParameter("data", filtro.getDataDetalhe());
		query.setParameter("codigoProduto", filtro.getProduto());
		
		return (Long) query.uniqueResult();
	}
	
	private String obterQueryPesquisaParcial(FiltroContasAPagarDTO filtro, boolean count){
		
		StringBuilder hql = new StringBuilder("select ");
		
		if (count){
			
			hql.append("count (lp.id) ");
		} else {
			
			//tudo que está relacionado a parcias no sistema atualmente faz menção a um intervalo,
			//porém, para a tela de contas a pagar não existe documentação que explique que intervalo seja esse.
			//A query abaixo vai considerar TUDO que estiver lançado antes da data informada no filtro
			
			hql.append(" 	l.dataLancamentoDistribuidor as lcto, ")
			   .append(" 	l.dataRecolhimentoDistribuidor as rclt, ")
			   .append(" 	l.reparte as reparte, ")
			   .append("	l.id as idLancamento, ")
			   //suplementacao
			   .append("	((select ")
			   .append("		sum(m.qtde) ")
			   .append(" 	 from MovimentoEstoque m ")
			   .append("		  join m.estoqueProduto esp ")
			   .append("		  join esp.produtoEdicao ped ")
			   .append("		  join ped.produto prod1 ")
			   .append(" 	 where m.data = :data ")
		   	   .append(" 		   and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarEntrada) ")
		   	   .append("		   and prod1.codigo = :codigoProduto) - ")
		   	   .append("	 (select ")
		   	   .append(" 	 	sum(m2.qtde) ")
		   	   .append(" 	 from MovimentoEstoque m2 ")
		   	   .append("		  join m2.estoqueProduto esp2 ")
			   .append("		  join esp2.produtoEdicao ped2 ")
			   .append("		  join ped2.produto prod2 ")
		   	   .append(" 	 where m2.data = :data ")
		       .append(" 		   and m2.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarSaida) ")
		       .append("		   and prod2.codigo = :codigoProduto)) as suplementacao, ")
//		       //encalhe
			   .append(" 	(select ")
			   .append("		sum(conferencia.qtde) ")
			   .append(" 	 from ConferenciaEncalhe conferencia ")
		       .append(" 		  join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		       .append(" 		  join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		       .append("          join conferencia.movimentoEstoqueCota movimento ")
		       .append("          join movimento.produtoEdicao pe ")
		       .append("		  join pe.produto pr ")
		       .append(" 	 where chamadaEncalhe.dataRecolhimento = :data ")
		       .append("		   and pr.codigo = :codigoProduto) as encalhe, ")
			   //venda
		       .append(" 	(l.reparte - ")
			   .append(" 	(select ")
			   .append("		sum(conferencia2.qtde) ")
			   .append(" 	 from ConferenciaEncalhe conferencia2 ")
		       .append(" 		  join conferencia2.chamadaEncalheCota chamadaEncalheCota2 ")
		       .append(" 		  join chamadaEncalheCota2.chamadaEncalhe chamadaEncalhe2 ")
		       .append("          join conferencia2.movimentoEstoqueCota movimento2 ")
		       .append("          join movimento2.produtoEdicao pe2 ")
		       .append("		  join pe2.produto pr2 ")
		       .append(" 	 where chamadaEncalhe2.dataRecolhimento = :data ")
		       .append("		   and pr2.codigo = :codigoProduto)) as venda, ")
		       //% venda
		       
		       //venda total
		       .append(" 	((select sum(l2.reparte) ")
		       .append("	  from ")
		       .append("    	LancamentoParcial lp2 ")
		       .append("		join lp2.periodos plp2 ")
		       .append("		join plp2.lancamentos l2 ")
		       .append("	  where ")
		       .append("		lp2.recolhimentoFinal = :data ")
		       .append("		and l2.status = :statusLancamento) * ")
			   //venda do produto
		       .append(" 	(select ")
			   .append("		sum(conferencia2.qtde) ")
			   .append(" 	 from ConferenciaEncalhe conferencia2 ")
		       .append(" 		  join conferencia2.chamadaEncalheCota chamadaEncalheCota2 ")
		       .append(" 		  join chamadaEncalheCota2.chamadaEncalhe chamadaEncalhe2 ")
		       .append("          join conferencia2.movimentoEstoqueCota movimento2 ")
		       .append("          join movimento2.produtoEdicao pe2 ")
		       .append("		  join pe2.produto pr2 ")
		       .append(" 	 where chamadaEncalhe2.dataRecolhimento = :data ")
		       .append("		   and pr2.codigo = :codigoProduto)) / 100 as pctVenda, ")
		       .append("	(select ")
		       .append("		sum(fech.quantidade) ")
		       .append("	 from FechamentoEncalhe fech ")
		       .append("	 where fech.fechamentoEncalhePK.produtoEdicao.produto.codigo = :codigoProduto ")
		       .append("		   and fech.fechamentoEncalhePK.dataEncalhe = :data ")
		       .append("	) as vendaCe, ")
		       //reparte acumulado
		       .append("	(select ")
		       .append("		sum(movCota.qtde) ")
		       .append("	from Lancamento lancamentoSupl")
		       .append("		 left join lancamentoSupl.movimentoEstoqueCotas movCota ")
		       .append("		 join lancamentoSupl.produtoEdicao.produto pe ")
		       .append("	where pe.codigo = :codigoProduto ")
		       .append("		  and lancamentoSupl.dataLancamentoDistribuidor <= :data) as reparteAcum, ")
		       //venda acumulada
		       .append("	((select ")
		       .append("		sum(liMCota.qtde) ")
		       .append("	from Lancamento lancamentoInicial ")
		       .append("		 left join lancamentoInicial.movimentoEstoqueCotas liMCota ")
		       .append("	where lancamentoInicial.dataLancamentoDistribuidor = :data ")
		       .append("		  and lancamentoInicial.produtoEdicao.produto.codigo = :codigoProduto) - ")
		       .append("	(select ")
		       .append("		sum(movimento.qtde) ")
		       .append("	from ConferenciaEncalhe conferencia")
		       .append("		 join conferencia.movimentoEstoqueCota movimento ")
		       .append("		 join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		       .append("		 join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		       .append("	where chamadaEncalhe.dataRecolhimento <= :data ")
		       .append("		  and chamadaEncalhe.produtoEdicao.produto.codigo = :codigoProduto)) as vendaAcum, ")
		       //% venda acumulada
		       
		       //venda total
		       .append(" 	((select sum(l2.reparte) ")
		       .append("	  from ")
		       .append("    	LancamentoParcial lp2 ")
		       .append("		join lp2.periodos plp2 ")
		       .append("		join plp2.lancamentos l2 ")
		       .append("	  where ")
		       .append("		lp2.recolhimentoFinal = :data ")
		       .append("		and l2.status = :statusLancamento) * ")
		       //venda acumulada do produto
		       .append("	(select ")
		       .append("		sum(liMCota.qtde) ")
		       .append("	from Lancamento lancamentoInicial ")
		       .append("		 left join lancamentoInicial.movimentoEstoqueCotas liMCota ")
		       .append("	where lancamentoInicial.dataLancamentoDistribuidor = :data ")
		       .append("		  and lancamentoInicial.produtoEdicao.produto.codigo = :codigoProduto) * ")
		       .append("	(select ")
		       .append("		sum(movimento.qtde) ")
		       .append("	from ConferenciaEncalhe conferencia")
		       .append("		 join conferencia.movimentoEstoqueCota movimento ")
		       .append("		 join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		       .append("		 join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		       .append("	where chamadaEncalhe.dataRecolhimento <= :data ")
		       .append("		  and chamadaEncalhe.produtoEdicao.produto.codigo = :codigoProduto) / 100) as pctVendaAcum ");
		}
		
		hql.append(" from ")
		   .append("    PeriodoLancamentoParcial plp ")
		   .append("    join plp.lancamentoParcial lp ")
		   .append("	join plp.lancamentos l ")
		   .append("    join l.produtoEdicao pe ")
		   .append("    join pe.produto prod ")
		   .append(" where ")
		   .append("	(l.dataRecolhimentoDistribuidor = :data)")
		   .append("	and prod.codigo = :codigoProduto ");
		
		if (filtro.getPaginacaoVO() != null && !count && filtro.getPaginacaoVO().getSortColumn() != null && filtro.getPaginacaoVO().getSortOrder() != null){
			
			hql.append(" order by ")
			   .append(filtro.getPaginacaoVO().getSortColumn())
			   .append(" ")
			   .append(filtro.getPaginacaoVO().getSortOrder() != null ? filtro.getPaginacaoVO().getSortOrder() : "");
		}
		
		return hql.toString();
	}
}
