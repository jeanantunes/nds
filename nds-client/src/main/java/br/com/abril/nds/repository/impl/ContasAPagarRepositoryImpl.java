package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

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
	
	public BigDecimal buscarTotalPesquisarPorDistribuidor(FiltroContasAPagarDTO filtro, boolean desconto){
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorDistribuidor(false, true, desconto, filtro));
		
		this.setarParametrosQueryporDistribuidor(query, filtro, false);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	private String montarQueryPorDistribuidor(boolean count, boolean totais, boolean desconto, FiltroContasAPagarDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if (count){
			
			hql.append("select (l.dataRecolhimentoDistribuidor) ");
		} else {
			
			hql.append("select new ")
			   .append(ContasApagarConsultaPorDistribuidorDTO.class.getCanonicalName())
			   .append("( l.dataRecolhimentoDistribuidor as dataMovimento, ")
			   .append(" sum(l.produtoEdicao.precoVenda * l.reparte) as consignado ")
			   
			   //encalhe
			   .append(",(select sum(movimento.qtde * conferencia.produtoEdicao.precoVenda) from ConferenciaEncalhe conferencia ")
			   .append(" join conferencia.movimentoEstoqueCota movimento ")
			   .append(" join conferencia.chamadaEncalheCota chamadaEncalheCota ")
			   .append(" join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
			   .append(" where chamadaEncalhe.dataRecolhimento = l.dataRecolhimentoDistribuidor) as encalhe ")
			   
			   //suplementar
			   .append(",(select sum(m.qtde * m.produtoEdicao.precoVenda) ")
			   .append(" from MovimentoEstoque m ")
			   .append(" where m.data = l.dataRecolhimentoDistribuidor ")
			   .append(" and m.qtde is not null ")
			   .append(" and m.produtoEdicao.precoVenda is not null")
			   .append(" and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarEntrada)) - ")
			   
			   .append("(select sum(m2.qtde * m2.produtoEdicao.precoVenda) ")
			   .append(" from MovimentoEstoque m2 ")
			   .append(" where m2.data = l.dataRecolhimentoDistribuidor ")
			   .append(" and m2.qtde is not null ")
			   .append(" and m2.produtoEdicao.precoVenda is not null")
			   .append(" and m2.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarSaida)) as suplementacao ")
			   
			   //FaltasSobras
			   .append(",(select sum(ld2.diferenca.qtde * ld2.diferenca.produtoEdicao.precoVenda) ")
			   .append(" from LancamentoDiferenca ld2 ")
			   .append(" where ld2.dataProcessamento = l.dataRecolhimentoDistribuidor ")
			   .append(" and ld2.diferenca.qtde is not null ")
			   .append(" and ld2.diferenca.produtoEdicao.precoVenda is not null")
			   .append(" and (ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraEm or ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraDe)")
			   .append(" group by ld2.diferenca.tipoDiferenca) - ")
			   
			   .append("(select sum(ld.diferenca.qtde * ld.diferenca.produtoEdicao.precoVenda) ")
			   .append(" from LancamentoDiferenca ld ")
			   .append(" where ld.dataProcessamento = l.dataRecolhimentoDistribuidor ")
			   .append(" and ld.diferenca.qtde is not null ")
			   .append(" and ld.diferenca.produtoEdicao.precoVenda is not null")
			   .append(" and (ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaEm or ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaDe)")
			   .append(" group by ld.diferenca.tipoDiferenca) as faltasSobras ")
			   
			   //PerdasGanhos
			   .append(",(select sum(ld3.diferenca.qtde * ld3.diferenca.produtoEdicao.precoVenda) ")
			   .append(" from LancamentoDiferenca ld3 ")
			   .append(" where ld3.dataProcessamento = l.dataRecolhimentoDistribuidor ")
			   .append(" and ld3.diferenca.qtde is not null ")
			   .append(" and ld3.diferenca.produtoEdicao.precoVenda is not null ")
			   .append(" and ld3.status = :statusPerda ")
			   .append(" group by ld3.diferenca.tipoDiferenca) + ")
			   
			   .append("(select sum(ld4.diferenca.qtde * ld4.diferenca.produtoEdicao.precoVenda) ")
			   .append(" from LancamentoDiferenca ld4 ")
			   .append(" where ld4.dataProcessamento = l.dataRecolhimentoDistribuidor ")
			   .append(" and ld4.diferenca.qtde is not null ")
			   .append(" and ld4.diferenca.produtoEdicao.precoVenda is not null ")
			   .append(" and ld4.status = :statusGanho ")
			   .append(" group by ld4.diferenca.tipoDiferenca) as perdasGanhos")
			   
			   .append(")");
		}
		
		hql.append(" from Lancamento l ");
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
		
			hql.append(" join l.produtoEdicao.produto.fornecedores f ");
		}
		
		hql.append(" where l.dataLancamentoDistribuidor between :inicio and :fim ")
		   .append(" and l.reparte is not null ")
		   .append(" and l.produtoEdicao.precoVenda is not null ")
		   .append(" and l.status = :statusLancamento ");
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			hql.append(" and f.id in (:idsFornecedores) ");
		}
		
		hql.append(" group by l.dataRecolhimentoDistribuidor ")
		   .append(" order by l.dataRecolhimentoDistribuidor asc ");
		
		if (!count){
		
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
			
		query.setParameter("inicio", filtro.getDataDe());
		query.setParameter("fim", filtro.getDataAte());
		
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
		
        if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			query.setParameterList("idsProdutoEdicao", filtro.getProdutoEdicaoIDs());
		}
	}
	
	
	
	
	
	
	
	

	
	
	private String obterHqlTotalizacoesContasAPagar(FiltroContasAPagarDTO filtro){
		
		boolean produto = (filtro.getProdutoEdicaoIDs()!=null && filtro.getProdutoEdicaoIDs().size()>0);
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(",      ((select sum(m.qtde * m.produtoEdicao.precoVenda) ")
		   .append("         from MovimentoEstoque m ")
		   .append(" 		 where m.data = l.dataRecolhimentoDistribuidor ")
		   .append(" 		 and m.qtde is not null ");
	    if (produto){
		   hql.append(" 		 and m.produtoEdicao.id in (:idsProdutoEdicao) ");
	    }
	    hql.append(" 		 and m.produtoEdicao.precoVenda is not null ")
	       .append(" 		 and m.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarEntrada)) - ")
			   
		   .append("	    (select sum(m2.qtde * m2.produtoEdicao.precoVenda) ")
		   .append(" 		 from MovimentoEstoque m2 ")
		   .append(" 		 where m2.data = l.dataRecolhimentoDistribuidor ")
		   .append(" 		 and m2.qtde is not null ");
	    if (produto){
		   hql.append(" 		 and m2.produtoEdicao.id in (:idsProdutoEdicao) ");
	    }
	    hql.append(" 		 and m2.produtoEdicao.precoVenda is not null")
		   .append(" 		 and m2.tipoMovimento.grupoMovimentoEstoque in (:movimentosSuplementarSaida)) ")
		   .append("       ) as suplementacao, ")
			   
		   
		   .append("       (select sum(movimento.qtde * conferencia.produtoEdicao.precoVenda) from ConferenciaEncalhe conferencia ")
		   .append("        join conferencia.movimentoEstoqueCota movimento ")
		   .append("        join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		   .append("        join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		   .append("        where chamadaEncalhe.dataRecolhimento = l.dataRecolhimentoDistribuidor ");
	    if (produto){
		    hql.append(" 		and movimento.produtoEdicao.id in (:idsProdutoEdicao) ");
	    }
	    hql.append("       ) as encalhe, ")
        
        
		   .append("       ((l.produtoEdicao.reparteDistribuido) - ")
		   .append("        (select sum(movimento.qtde * conferencia.produtoEdicao.precoVenda) from ConferenciaEncalhe conferencia ")
		   .append("         join conferencia.movimentoEstoqueCota movimento ")
		   .append("         join conferencia.chamadaEncalheCota chamadaEncalheCota ")
		   .append("         join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
		   .append("         where chamadaEncalhe.dataRecolhimento = l.dataRecolhimentoDistribuidor ");
	    if (produto){
		    hql.append(" 		 and movimento.produtoEdicao.id in (:idsProdutoEdicao) ) ");
	    }
	    hql.append("       ) as venda, ")

		   
		   .append("	   ((select sum(ld2.diferenca.qtde * ld2.diferenca.produtoEdicao.precoVenda) ")
		   .append("        from LancamentoDiferenca ld2 ")
		   .append(" 		 where ld2.dataProcessamento = l.dataRecolhimentoDistribuidor ")
		   .append(" 		 and ld2.diferenca.qtde is not null ")
		   .append(" 		 and ld2.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" 		 and (ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraEm or ld2.diferenca.tipoDiferenca = :tipoDiferencaSobraDe)");
	    if (produto){
		    hql.append(" 		 and ld2.diferenca.produtoEdicao.id in (:idsProdutoEdicao) ");
	    }
	    hql.append(" 		 group by ld2.diferenca.tipoDiferenca) - ")
		   
		   .append("	    (select sum(ld.diferenca.qtde * ld.diferenca.produtoEdicao.precoVenda) ")
		   .append(" 		 from LancamentoDiferenca ld ")
		   .append(" 		 where ld.dataProcessamento = l.dataRecolhimentoDistribuidor ")
		   .append(" 		 and ld.diferenca.qtde is not null ")
		   .append(" 		 and ld.diferenca.produtoEdicao.precoVenda is not null")
		   .append(" 		 and (ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaEm or ld.diferenca.tipoDiferenca = :tipoDiferencaFaltaDe)");
	    if (produto){ 
		    hql.append(" 		 and ld.diferenca.produtoEdicao.id in (:idsProdutoEdicao) ");
	    }
	    hql.append(" 		 group by ld.diferenca.tipoDiferenca) ")
	       .append("      ) as faltasSobras, ")
		
	   
		   .append("      ((select sum(ld3.diferenca.qtde * ld3.diferenca.produtoEdicao.precoVenda) ")
		   .append("  		 from LancamentoDiferenca ld3 ")
		   .append("        where ld3.dataProcessamento = l.dataRecolhimentoDistribuidor ")
		   .append("        and ld3.diferenca.qtde is not null ")
		   .append("        and ld3.diferenca.produtoEdicao.precoVenda is not null ");
	    if (produto){
		    hql.append("        and ld3.diferenca.produtoEdicao.id in (:idsProdutoEdicao) ");
	    }
	    hql.append("        and ld3.status = :statusPerda ")
	       .append("        group by ld3.diferenca.tipoDiferenca) + ")
		   
		   .append("       (select sum(ld4.diferenca.qtde * ld4.diferenca.produtoEdicao.precoVenda) ")
		   .append("        from LancamentoDiferenca ld4 ")
		   .append("        where ld4.dataProcessamento = l.dataRecolhimentoDistribuidor ")
		   .append("        and ld4.diferenca.qtde is not null ")
		   .append("        and ld4.diferenca.produtoEdicao.precoVenda is not null ");
	    if (produto){
		    hql.append("        and ld4.diferenca.produtoEdicao.id in (:idsProdutoEdicao) ");
	    }
	    hql.append("        and ld4.status = :statusGanho ")
		   .append("        group by ld4.diferenca.tipoDiferenca)")
		   .append("      )) as faltasSobras, ")
	   
	   
		   .append("       l.produtoEdicao.reparteDistribuido as saldoAPagar ")
	    
	    
	       .append(" from Lancamento l ")
		   .append(" join l.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produto.fornecedores f ")
		   
		   .append(" where l.dataLancamentoDistribuidor ")	
		   .append(" between :inicio and :fim ")
		   .append(" and l.reparte is not null ")
		   .append(" and produtoEdicao.precoVenda is not null ")
		   .append(" and l.status = :statusLancamento ");
		
		if (filtro.getIdsFornecedores() != null && !filtro.getIdsFornecedores().isEmpty()){
			
			hql.append(" and f.id in (:idsFornecedores) ");
		}
		
		if (produto){
			
			hql.append(" and produtoEdicao.id in (:idsProdutoEdicao) ");
		}
		
		hql.append(" group by l.dataRecolhimentoDistribuidor ")
		   .append(" order by l.dataRecolhimentoDistribuidor asc ");
		
		return hql.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContasApagarConsultaPorProdutoDTO> pesquisarPorProduto(FiltroContasAPagarDTO filtro) {
		
		Query query = this.getSession().createQuery(
				this.montarQueryPorProduto(false, filtro));
		
		this.setarParametrosQueryContasAPagar(query, filtro, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ContasApagarConsultaPorProdutoDTO.class));
		
		return query.list();
	}
	
	
    private String montarQueryPorProduto(boolean buscarDatas, FiltroContasAPagarDTO filtro){
    	

    	/*
    	•	Rclt – data de recolhimento, da mais recente pra mais antiga
    	•	codigo - Código do produto
    	•	Nome: nome do produto
    	•	Edição: edição do produto
    	•	Tipo: tipo de recolhimento (N – normal e P – parcial)
    	•	Reparte: reparte do produto
    	•	Suplementar: quantidade suplementar do produto
    	•	Encalhe: quantidade encalhe do produto
    	•	Venda: Reparte menos Encalhe
    	•	Faltas e Sobras: quantidade de faltas e sobras
    	•	Débitos/Créditos: quantidade de débitos e créditos do produto
    	•	Saldo a Pagar R$: valor total de saldo a pagar do produto (
    	*/

		
		StringBuilder hql = new StringBuilder();
			
		hql.append("select l.dataRecolhimentoDistribuidor as rctl, ")
		   .append("       l.produtoEdicao.poduto.codigo as codigo, ")
		   .append("       l.produtoEdicao.poduto.nome as produto, ")
		   .append("       l.produtoEdicao.numero as edicao, ")
		   .append("       l.produtoEdicao.parcial as tipo, ")	   
		   .append("       l.produtoEdicao.reparteDistribuido as reparte ")
		   
		   .append(this.obterHqlTotalizacoesContasAPagar(filtro));
		
		return hql.toString();
	}

}