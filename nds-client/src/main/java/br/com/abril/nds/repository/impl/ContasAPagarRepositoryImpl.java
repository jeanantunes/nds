package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ContasAPagarRepositoryImpl extends AbstractRepository implements ContasAPagarRepository{

	@Override
	public Integer pesquisarPorDistribuidorCount(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(true, false, false, filtro));
		
		this.setarParametrosQueryContasAPagar(query, filtro, true);
		
		return query.list().size();
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasApagarConsultaPorDistribuidorDTO> pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(false, false, false, filtro));
		
		this.setarParametrosQueryContasAPagar(query, filtro, false);
		
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
		
		this.setarParametrosQueryContasAPagar(query, filtro, false);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	private String montarQueryPorDistribuidor(boolean count, boolean totais, boolean desconto, FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if (count){
			
			if (filtro.getDataDe() == null){
			
				hql.append("select (l.dataRecolhimentoPrevista) ");
			} else {
				
				hql.append("select (l.dataCriacao) ");
			}
		} else {
			
			if (totais){
				
				hql.append("select ");
			} else {
				
				hql.append("select new ")
				   .append(ContasApagarConsultaPorDistribuidorDTO.class.getCanonicalName());
				
				if (filtro.getDataDe() == null){
					
					hql.append("( l.dataRecolhimentoPrevista as dataMovimento, ");
				} else {
					
					hql.append("( l.dataCriacao as dataMovimento, ");
				}
			}
			
			hql.append(" coalesce(");
			
			if (desconto){
				
				hql.append(" sum(((l.produtoEdicao.precoVenda * coalesce(f.margemDistribuidor, 0) / 100 ) * l.reparte)) ");
			} else {
				
				hql.append(" sum((l.produtoEdicao.precoVenda * l.reparte)) ");
			}
			
			hql.append(",0) ");
			
			if (totais){
			   
				hql.append(" - ( ");
			} else {
			   
				hql.append(" as consignado, ");
			}
		   
			//encalhe
			hql.append(" coalesce((select sum( ");
			
			if (desconto){
			   
				hql.append(" (movimento.qtde * (conferencia.produtoEdicao.precoVenda * coalesce(fornecedor1.margemDistribuidor, 0) / 100 )) ");
			} else {
				
				hql.append(" (movimento.qtde * conferencia.produtoEdicao.precoVenda) ");
			}
			
			hql.append(") ");
			
			hql.append(" from ConferenciaEncalhe conferencia ")
		       .append(" join conferencia.movimentoEstoqueCota movimento ")
		       .append(" join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		       .append(" join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
			
			if (desconto){
				
				hql.append(" join conferencia.produtoEdicao.produto.fornecedores fornecedor1 ");
			}
			
			if (filtro.getDataDe() == null){
				
				hql.append(" where chamadaEncalhe.dataRecolhimento = l.dataRecolhimentoPrevista) ,0) ");
			} else {
				
				hql.append(" where chamadaEncalhe.dataRecolhimento = l.dataCriacao) ,0) ");
			}
			
			if (totais){
				
				hql.append(") - (");
			} else {
				
				hql.append(" as encalhe, ");
			}
			
			//suplementar
			hql.append(" coalesce((select sum( ");
			
			if (desconto){
				
				hql.append(" (m.qtde * (m.produtoEdicao.precoVenda * coalesce(fornecedor2.margemDistribuidor, 0) / 100)) ");
			} else {
				
				hql.append(" (m.qtde * m.produtoEdicao.precoVenda) ");
			}
			
			hql.append(") ");
			
			hql.append(" from MovimentoEstoque m ");
			
			if (desconto){
				
				hql.append(" join m.produtoEdicao.produto.fornecedores fornecedor2 ");
			}
			
			if (filtro.getDataDe() == null){
				
				hql.append(" where m.data = l.dataRecolhimentoPrevista ");
			} else {
				
				hql.append(" where m.data = l.dataCriacao ");
			}
			
			hql.append(" and m.qtde is not null ")
		   	   .append(" and m.produtoEdicao.precoVenda is not null")
		   	   .append(" and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarEntrada)) ,0) - ")
		   	   
		      .append("coalesce((select sum( ");
			
			if (desconto){
				
				hql.append(" (m2.qtde * (m2.produtoEdicao.precoVenda * coalesce(fornecedor3.margemDistribuidor, 0) / 100)) ");
			} else {
				
				hql.append(" (m2.qtde * m2.produtoEdicao.precoVenda) ");
			}
			
			hql.append(") ");
			
			hql.append(" from MovimentoEstoque m2 ");
			
			if (desconto){
				
				hql.append(" join m2.produtoEdicao.produto.fornecedores fornecedor3 ");
			}
			
			if (filtro.getDataDe() == null){
				
				hql.append(" where m2.data = l.dataRecolhimentoPrevista ");
			} else {
				
				hql.append(" where m2.data = l.dataCriacao ");
			}
			
			hql.append(" and m2.qtde is not null ")
		       .append(" and m2.produtoEdicao.precoVenda is not null")
		       .append(" and m2.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarSaida)) ,0)");
			
			if (totais){
				
				hql.append(") - (");
			} else {
				
				hql.append(" as suplementacao, ");
			}
			
			//FaltasSobras
			hql.append("coalesce((select sum( ");
			
			if (desconto){
				
				hql.append(" (ld2.diferenca.qtde * (ld2.diferenca.produtoEdicao.precoVenda * coalesce(fornecedor4.margemDistribuidor, 0) / 100)) ");
			} else {
				
				hql.append(" (ld2.diferenca.qtde * ld2.diferenca.produtoEdicao.precoVenda) ");
			}
			
			hql.append(") ");
			
			hql.append(" from LancamentoDiferenca ld2 ");
			
			if (desconto){
				
				hql.append(" join ld2.diferenca.produtoEdicao.produto.fornecedores fornecedor4 ");
			}
			
			if (filtro.getDataDe() == null){
				
				hql.append(" where ld2.dataProcessamento = l.dataRecolhimentoPrevista ");
			} else {
				
				hql.append(" where ld2.dataProcessamento = l.dataCriacao ");
			}
			
			hql.append(" and ld2.diferenca.qtde is not null ")
		       .append(" and ld2.diferenca.produtoEdicao.precoVenda is not null")
		       .append(" and (ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraEm or ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraDe)")
		       .append(" group by ld2.diferenca.tipoDiferenca) ,0) - ")
		       
		       .append("coalesce((select sum( ");
			
			if (desconto){
				
				hql.append(" (ld.diferenca.qtde * (ld.diferenca.produtoEdicao.precoVenda * coalesce(fornecedor5.margemDistribuidor, 0) / 100)) ");
			} else {
				
				hql.append(" (ld.diferenca.qtde * ld.diferenca.produtoEdicao.precoVenda) ");
			}
			
			hql.append(") ");
			
			hql.append(" from LancamentoDiferenca ld ");
			
			if (desconto){
				
				hql.append(" join ld.diferenca.produtoEdicao.produto.fornecedores fornecedor5 ");
			}
			
			if (filtro.getDataDe() == null){
				
				hql.append(" where ld.dataProcessamento = l.dataRecolhimentoPrevista ");
			} else {
				
				hql.append(" where ld.dataProcessamento = l.dataCriacao ");
			}
			
			hql.append(" and ld.diferenca.qtde is not null ")
		       .append(" and ld.diferenca.produtoEdicao.precoVenda is not null")
		       .append(" and (ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaEm or ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaDe)")
		       .append(" group by ld.diferenca.tipoDiferenca) ,0) ");
			
			if (totais){
				
				hql.append(") - (");
			} else {
				
				hql.append(" as faltasSobras, ");
			}
			
			//PerdasGanhos
			hql.append("coalesce((select sum( ");
			
			if (desconto){
				
				hql.append(" (ld3.diferenca.qtde * (ld3.diferenca.produtoEdicao.precoVenda * coalesce(fornecedor6.margemDistribuidor, 0) / 100 )) ");
			} else {
				
				hql.append(" (ld3.diferenca.qtde * ld3.diferenca.produtoEdicao.precoVenda) ");
			}
			
			hql.append(") ");
			
			hql.append(" from LancamentoDiferenca ld3 ");
			
			if (desconto){
				
				hql.append(" join ld3.diferenca.produtoEdicao.produto.fornecedores fornecedor6 ");
			}
			
			if (filtro.getDataDe() == null){
				
				hql.append(" where ld3.dataProcessamento = l.dataRecolhimentoPrevista ");
			} else {
				
				hql.append(" where ld3.dataProcessamento = l.dataCriacao ");
			}
			
			hql.append(" and ld3.diferenca.qtde is not null ")
		   	   .append(" and ld3.diferenca.produtoEdicao.precoVenda is not null ")
		   	   .append(" and ld3.status = :statusPerda ")
		   	   .append(" group by ld3.diferenca.tipoDiferenca),0) + ")
		   	   
		   	   .append("coalesce((select sum( ");
			
			if (desconto){
				
				hql.append(" (ld4.diferenca.qtde * (ld4.diferenca.produtoEdicao.precoVenda * coalesce(fornecedor7.margemDistribuidor, 0) / 100 )) ");
			} else {
				
				hql.append(" (ld4.diferenca.qtde * ld4.diferenca.produtoEdicao.precoVenda) ");
			}
			
			hql.append(") ");
			
			hql.append(" from LancamentoDiferenca ld4 ");
			
			if (desconto){
				
				hql.append(" join ld4.diferenca.produtoEdicao.produto.fornecedores fornecedor7 ");
			}
			
			if (filtro.getDataDe() == null){
				
				hql.append(" where ld4.dataProcessamento = l.dataRecolhimentoPrevista ");
			} else {
				
				hql.append(" where ld4.dataProcessamento = l.dataCriacao ");
			}
			
			hql.append(" and ld4.diferenca.qtde is not null ")
		   	   .append(" and ld4.diferenca.produtoEdicao.precoVenda is not null ")
		   	   .append(" and ld4.status = :statusGanho ")
		   	   .append(" group by ld4.diferenca.tipoDiferenca),0) ");
			
			if (totais){
				
				hql.append(") ");
			} else {
				
				hql.append(" as perdasGanhos) ");
			}
		}
		
		hql.append(" from Lancamento l ");
		
		if ((filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()) ||
				desconto){
		
			hql.append(" join l.produtoEdicao.produto.fornecedores f ");
		}
		
		if (filtro.getDataDe() == null){
			
			hql.append(" where ld4.dataProcessamento = l.dataRecolhimentoPrevista ");
		} else {
			
			hql.append(" where l.dataCriacao between :inicio and :fim ");
		}
		
		hql.append(" and l.reparte is not null ")
		   .append(" and l.produtoEdicao.precoVenda is not null ")
		   .append(" and l.status = :statusLancamento ");
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			hql.append(" and f.id in (:idsFornecedores) ");
		}
		
		if (!totais){
			
			if (filtro.getDataDe() == null){
				
				hql.append(" group by l.dataRecolhimentoPrevista ")
				   .append(" order by l.dataRecolhimentoPrevista asc ");
			} else {
				
				hql.append(" group by l.dataCriacao ")
				   .append(" order by l.dataCriacao asc ");
			}
		}
		
		if (!count && !totais){
		
			PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
			
			if (paginacaoVO != null && !"data".equals(paginacaoVO.getSortColumn())){
				
				hql.append(", ")
				   .append(paginacaoVO.getSortColumn())
				   .append(" ")
				   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
			}
		}
		
		return hql.toString();
	}
	
	private void setarParametrosQueryContasAPagar(Query query, FiltroContasAPagarDTO filtro, boolean buscarDatas){
		
		if (filtro.getDataDe() != null){
			
			query.setParameter("inicio", filtro.getDataDe());
        }
        
        if (filtro.getDataAte() != null){
        	
        	query.setParameter("fim", filtro.getDataAte());
        }

		if (!buscarDatas){
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
			
			query.setParameter("tipoDiferencaFaltaEm", TipoDiferenca.FALTA_EM);
			query.setParameter("tipoDiferencaFaltaDe", TipoDiferenca.FALTA_DE);
			query.setParameter("tipoDiferencaSobraEm", TipoDiferenca.SOBRA_EM);
			query.setParameter("tipoDiferencaSobraDe", TipoDiferenca.SOBRA_DE);
			
			query.setParameter("statusPerda", StatusAprovacao.PERDA);
			query.setParameter("statusGanho", StatusAprovacao.GANHO);
		}
		
		query.setParameter("statusLancamento", StatusLancamento.CONFIRMADO);
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			query.setParameterList("idsFornecedores", filtro.getIdsFornecedores());
		}
		
        if (filtro.getProdutoEdicaoIDs() != null && !filtro.getProdutoEdicaoIDs().isEmpty()){
			
			query.setParameterList("idsProdutoEdicao", filtro.getProdutoEdicaoIDs());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContasApagarConsultaPorProdutoDTO> pesquisarPorProduto(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(this.montarQueryPorProduto(filtro));

		this.setarParametrosQueryContasAPagar(query, filtro, false);
		
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

		this.setarParametrosQueryContasAPagar(query, filtro, true);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarGridPrincipalProdutoDTO.class));
		
		return (ContasAPagarGridPrincipalProdutoDTO) query.uniqueResult();
	}
	
	@Override
	public Long pesquisarCountPorProduto(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(this.montarQueryQuantidadePorProduto(filtro));

		this.setarParametrosQueryContasAPagar(query, filtro, true);
		
		return (Long) query.uniqueResult();
	}
	
	private String montarQueryPorProduto(FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();

		String campoRctl = filtro.getDataDe() == null?" l.dataRecolhimentoPrevista as rctl, ":" l.dataCriacao as rctl, ";

		hql.append("select new ")
		   .append(ContasApagarConsultaPorProdutoDTO.class.getCanonicalName())
		   .append("(")
		   .append("       produtoEdicao.id as produtoEdicaoId, ")
		   .append(        campoRctl)
		   .append("       produto.codigo as codigo, ")
		   .append("       produto.nome as produto, ")
		   .append("       produtoEdicao.numeroEdicao as edicao, ")
		   .append("       produtoEdicao.parcial as tipo, ")	   
		   .append("       l.reparte as reparte ")
		
		   .append(",") 
		
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

		StringBuilder hql = new StringBuilder();
		
		String parametroData = filtro.getDataDe() == null?" l.dataRecolhimentoPrevista ":" l.dataCriacao ";
			
		hql.append("       (COALESCE((select sum(m.qtde) ")
		   .append("           		  from MovimentoEstoque m ")
		   .append(" 		 		  where m.data = ").append(parametroData)
		   .append(" 		 		  and m.qtde is not null ")
		   .append(" 		 		  and m.produtoEdicao.id = produtoEdicao.id ")
		   .append(" 		 		  and m.produtoEdicao.precoVenda is not null ")
	       .append(" 		 		  and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarEntrada)) ")
	       .append("        ,0) - ")			   
		   .append("	    COALESCE((select sum(m2.qtde) ")
		   .append(" 		          from MovimentoEstoque m2 ")
		   .append(" 		 		  where m2.data = ").append(parametroData)
		   .append(" 		 		  and m2.qtde is not null ")
		   .append(" 		 		  and m2.produtoEdicao.id = produtoEdicao.id ")	    
		   .append(" 		 		  and m2.produtoEdicao.precoVenda is not null")
		   .append(" 		 		  and m2.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarSaida)) ")
		   .append("        ,0) ")
		   .append("       ) as suplementacao, ")
			   
		   .append("       COALESCE((select sum(movimento.qtde) from ConferenciaEncalhe conferencia ")
		   .append("                 join conferencia.movimentoEstoqueCota movimento ")
		   .append("        		 join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		   .append("        		 join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")	   
		   .append(" 		 		 where chamadaEncalhe.dataRecolhimento = ").append(parametroData)	   
		   .append(" 				 and movimento.produtoEdicao.id = produtoEdicao.id ")	    
		   .append("       ),0) as encalhe, ")

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
    	
    	StringBuilder hql = new StringBuilder();
    	
    	String j = " WHERE ";

    	hql.append(" from Lancamento l ")
	       .append(" join l.produtoEdicao produtoEdicao ")
	       .append(" join produtoEdicao.produto produto ")
	       .append(" join produto.fornecedores f ");
	   
        if (filtro.getDataDe() != null){
            hql.append(j+" l.dataCriacao >= :inicio ");
            j = " AND ";
        }
        
        if (filtro.getDataAte() != null){
            hql.append(j+" l.dataCriacao <= :fim ");
            j = " AND ";
        }
	       
	    hql.append(j+" l.reparte is not null ")
	       .append(" and produtoEdicao.precoVenda is not null ")	      
	       .append(" and l.status = :statusLancamento ");
    	
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
		   .append("    produto2.codigo, ")
		   .append("    produto2.nomeComercial as produto, ")
		   .append("    produtoEdicao.numeroEdicao as edicao, ")
		   .append(" 	produtoEdicao.precoVenda as precoCapa, ")
		   .append(" 	produtoEdicao.precoVenda - (produtoEdicao.precoVenda * coalesce(fo.margemDistribuidor,0) / 100) as precoComDesconto, ")
		   .append(" 	l.reparte as reparteSugerido, ")
		   .append(" 	l.reparte - coalesce(dif.qtde,0) as reparteFinal, ")
		   .append(" 	l.reparte - (l.reparte - coalesce(dif.qtde,0)) as diferenca, ")
		   .append(" 	coalesce(dif.tipoDiferenca, '') as motivo, ")
		   .append(" 	coalesce(pessoa.razaoSocial, '') as fornecedor, ")
		   .append(" 	produtoEdicao.precoVenda * (l.reparte - coalesce(dif.qtde,0)) as valor, ")
		   .append(" 	(produtoEdicao.precoVenda - (produtoEdicao.precoVenda * coalesce(fo.margemDistribuidor,0) / 100)) * (l.reparte - coalesce(dif.qtde,0)) as valorDesconto ")
		   .append(" from ")
		   .append(" 	Lancamento l ")
		   .append(" 	join l.produtoEdicao produtoEdicao ")
		   .append("	join produtoEdicao.produto produto2 ")
		   .append("	join produto2.fornecedores fo ")
		   .append("	join fo.juridica pessoa ")
		   .append(" 	left join produtoEdicao.diferencas dif ")
		   .append("    left join dif.lancamentoDiferenca ld ")
		   .append(" where l.dataCriacao = :data ")
		   .append(" 	and l.reparte is not null ")
		   .append(" 	and produtoEdicao.precoVenda is not null ")
		   .append(" 	and l.status = :statusLancamento ")
		   .append("	and ld.status is not null and ld.status= :statusLancamentoDif ");
		
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
			
			hql.append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", filtro.getDataDetalhe());
		query.setParameter("statusLancamento", StatusLancamento.CONFIRMADO);
		query.setParameter("statusLancamentoDif", StatusAprovacao.APROVADO);
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			query.setParameter("nomeProduto", filtro.getProduto());
		}
		
		if (filtro.getEdicao() != null){
			
			query.setParameter("numeroEdicao", filtro.getEdicao());
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		hql.append("select ")
		   .append("    produto2.codigo, ")
		   .append("    produto2.nomeComercial as produto, ")
		   .append("    produtoEdicao.numeroEdicao as edicao, ")
		   .append(" 	produtoEdicao.precoVenda as precoCapa, ")
		   .append(" 	produtoEdicao.precoVenda - (produtoEdicao.precoVenda * coalesce(fo.margemDistribuidor,0) / 100) as precoComDesconto, ")
		   .append(" 	l.qtde as encalhe, ")
		   .append(" 	coalesce(pessoa.razaoSocial, '') as fornecedor, ")
		   .append(" 	(produtoEdicao.precoVenda - (produtoEdicao.precoVenda * coalesce(fo.margemDistribuidor,0) / 100)) as valor ")
		   .append(" from ")
		   .append(" 	ConferenciaEncalhe l ")
		   .append(" 	join l.produtoEdicao produtoEdicao ")
		   .append("	join produtoEdicao.produto produto2 ")
		   .append("	join produto2.fornecedores fo ")
		   .append("	join fo.juridica pessoa ")
		   .append(" 	join l.chamadaEncalheCota chamadaEncalheCota ")
		   .append(" 	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		   .append(" where chamadaEncalhe.dataRecolhimento = :data ")
		   .append(" 	and l.qtde is not null ")
		   .append(" 	and produtoEdicao.precoVenda is not null ");
		
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
			
			hql.append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", filtro.getDataDetalhe());
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			query.setParameter("nomeProduto", filtro.getProduto());
		}
		
		if (filtro.getEdicao() != null){
			
			query.setParameter("numeroEdicao", filtro.getEdicao());
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		hql.append("select ")
		   .append("    produto2.codigo, ")
		   .append("    produto2.nomeComercial as produto, ")
		   .append("    produtoEdicao.numeroEdicao as edicao, ")
		   .append(" 	produtoEdicao.precoVenda as precoCapa, ")
		   .append(" 	produtoEdicao.precoVenda - (produtoEdicao.precoVenda * coalesce(fo.margemDistribuidor,0) / 100) as precoComDesconto, ")
		   .append("	boxDetalhe.codigo as box, ")
		   .append("	diferenca.qtde as exemplares, ")
		   .append(" 	coalesce(pessoa.razaoSocial, '') as fornecedor, ")
		   .append(" 	(produtoEdicao.precoVenda - (produtoEdicao.precoVenda * coalesce(fo.margemDistribuidor,0) / 100)) as valor ")
		   .append(" from ")
		   .append(" 	LancamentoDiferenca l ")
		   .append(" 	join l.diferenca diferenca ")
		   .append("	join diferenca.produtoEdicao produtoEdicao ")
		   .append("	join diferenca.rateios rateioDiferenca ")
		   .append("	join rateioDiferenca.cota cota ")
		   .append("	join cota.box boxDetalhe ")
		   .append("	join produtoEdicao.produto produto2 ")
		   .append("	join produto2.fornecedores fo ")
		   .append("	join fo.juridica pessoa ")
		   .append(" where l.dataProcessamento = :data ")
		   .append(" 	and diferenca.qtde is not null ")
		   .append(" 	and produtoEdicao.precoVenda is not null ");
		
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
			
			hql.append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder() == null ? "" : paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", filtro.getDataDetalhe());
		
		if (filtro.getProduto() != null && !filtro.getProduto().isEmpty()){
			
			query.setParameter("nomeProduto", filtro.getProduto());
		}
		
		if (filtro.getEdicao() != null){
			
			query.setParameter("numeroEdicao", filtro.getEdicao());
		}
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarConsultaProdutoDTO> obterProdutos(FiltroContasAPagarDTO Filtro) {
		StringBuilder sql = new StringBuilder();


        sql.append(" SELECT  p.codigo as codigo, p.nome as produto, ");
        sql.append("	 pe.numeroEdicao as edicao, pe.precoVenda as precoCapa, pe.id as produtoEdicaoID, ");
        sql.append(" 	 fornec.nomeFantasia as fornecedor, edi.nomeFantasia as editor ");
        sql.append(" FROM ProdutoEdicao pe ");
        sql.append(" JOIN  pe.produto p");
        sql.append(" JOIN  p.editor e");
        sql.append(" JOIN  p.fornecedores f");
        sql.append(" JOIN  f.juridica fornec");
        sql.append(" JOIN  e.pessoaJuridica edi");
        
        if(	Filtro.getProduto()!=null || Filtro.getEdicao()!=null ){
        	sql.append(" WHERE ");
	        if(	Filtro.getProduto()!=null	)
	        	sql.append("        p.codigo = :codigoProduto ");
	        if(	Filtro.getEdicao()!=null	)
	        	sql.append(" AND    pe.numeroEdicao = :numeroEdicao ");
        }
        sql.append(" order by  codigo");
        Query query = getSession().createQuery(sql.toString());
        if(Filtro.getProduto() != null){
        	query.setParameter("codigoProduto", Filtro.getProduto());
        }
        
        if(Filtro.getEdicao()!=null){
        	query.setParameter("numeroEdicao", Filtro.getEdicao());            
        }
        query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarConsultaProdutoDTO.class));
        return query.list();

	}
}