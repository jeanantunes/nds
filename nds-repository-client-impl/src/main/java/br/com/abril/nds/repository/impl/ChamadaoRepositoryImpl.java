package br.com.abril.nds.repository.impl;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChamadaoRepository;

/**
 * Classe de implementação referente ao acesso a dados
 * para as pesquisas de consignados do chamadão.
 * 
 * @author Discover Technology
 */
@Repository
public class ChamadaoRepositoryImpl extends AbstractRepositoryModel<Cota,Long> implements ChamadaoRepository {

	
	/**
	 * Construtor padrão
	 */
	public ChamadaoRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	public ResumoConsignadoCotaChamadaoDTO obterResumoConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder("select sum(consignadoCota.qtdExemplaresTotal) as qtdExemplaresTotal, sum(consignadoCota.valorTotal) as valorTotal from ( ");
		
		hql.append("SELECT ")
			.append(" sum(estoqueProdCota.QTDE_RECEBIDA ")
			.append(" - estoqueProdCota.QTDE_DEVOLVIDA) as qtdExemplaresTotal, ")
			.append(" sum((mec.PRECO_COM_DESCONTO) * (estoqueProdCota.QTDE_RECEBIDA - estoqueProdCota.QTDE_DEVOLVIDA)) as valorTotal ");
		
		hql.append(this.gerarQueryConsignados(filtro));
		
		hql.append(" ) as consignadoCota");
		
		Query query = this.getSession().createSQLQuery(hql.toString())
			.addScalar("qtdExemplaresTotal", StandardBasicTypes.BIG_INTEGER)
			.addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL);
		
		this.aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
			ResumoConsignadoCotaChamadaoDTO.class));
			
		return (ResumoConsignadoCotaChamadaoDTO) query.uniqueResult();
	}
	
	@Override
	public ResumoConsignadoCotaChamadaoDTO obterResumoConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ")
			.append(" sum(chamadaEncalheCota.qtdePrevista) as qtdExemplaresTotal, ")
			.append(" sum(mec.valoresAplicados.valorComDesconto * mec.qtde) as valorTotal ");
		
		hql.append(this.gerarQueryChamadasEncalhe(filtro));
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.aplicarParametrosParaPesquisaChamadaEncalhe(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
			ResumoConsignadoCotaChamadaoDTO.class));
			
		return (ResumoConsignadoCotaChamadaoDTO) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaChamadaoDTO> obterConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select ")
			.append("produto.CODIGO as codigoProduto, ")
			.append("produto.NOME as nomeProduto, ")
			.append("produtoEdicao.NUMERO_EDICAO as numeroEdicao, ")
			.append("produtoEdicao.PRECO_VENDA as precoVenda, ")
			.append("(mec.VALOR_DESCONTO) as desconto, ")
			.append("(mec.PRECO_COM_DESCONTO) as precoDesconto, ")
			.append("estoqueProdCota.QTDE_RECEBIDA - estoqueProdCota.QTDE_DEVOLVIDA as reparte, ")
			.append("(case ")
			.append("when (select count(produtoFor.FORNECEDORES_ID) from PRODUTO_FORNECEDOR produtoFor ")
			.append("where produtoFor.PRODUTO_ID = produto.ID) > 1 ")
			.append("then 'Diversos' ")
			.append("when (select count(produtoFor.FORNECEDORES_ID) from PRODUTO_FORNECEDOR produtoFor ")
			.append("where produtoFor.PRODUTO_ID = produto.ID) = 1 ")
			.append("then (select pessoa.RAZAO_SOCIAL ")
			.append("from PRODUTO_FORNECEDOR produtoFor, FORNECEDOR fornecedor, PESSOA pessoa ")
			.append("where fornecedor.ID = produtoFor.FORNECEDORES_ID ")
			.append("and fornecedor.JURIDICA_ID = pessoa.ID and produtoFor.PRODUTO_ID = produto.ID) ")
			.append("else null end) as nomeFornecedor, ")
			.append("lancamento.DATA_REC_PREVISTA as dataRecolhimento, ")
			.append("produtoEdicao.PRECO_VENDA * ")
			.append("(estoqueProdCota.QTDE_RECEBIDA - estoqueProdCota.QTDE_DEVOLVIDA) as valorTotal, ")
			.append("(mec.PRECO_COM_DESCONTO) * (estoqueProdCota.QTDE_RECEBIDA - estoqueProdCota.QTDE_DEVOLVIDA) as valorTotalDesconto, ")
			.append("lancamento.ID as idLancamento, ")
			.append("produtoEdicao.POSSUI_BRINDE as possuiBrinde ");
		
		hql.append(this.gerarQueryConsignados(filtro));
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
				
				case CODIGO_PRODUTO:
					hql.append(" order by codigoProduto ");
					break;
					
				case NOME_PRODUTO:
					hql.append(" order by nomeProduto ");
					break;
					
				case EDICAO:
					hql.append(" order by numeroEdicao ");
					break;
				
				case BRINDE:
					hql.append(" order by possuiBrinde ");
					break;
					
				case PRECO_VENDA:
					hql.append(" order by precoVenda ");
					break;
					
				case PRECO_DESCONTO:
					hql.append(" order by precoDesconto ");
					break;
					
				case REPARTE:
					hql.append(" order by reparte ");
					break;
					
				case FORNECEDOR:
					hql.append(" order by nomeFornecedor ");
					break;
					
				case RECOLHIMENTO:
					hql.append(" order by dataRecolhimento ");
					break;
				
				case VALOR_TOTAL:
					hql.append(" order by valorTotal ");
					break;
				
				case VALOR_TOTAL_DESCONTO:
					hql.append(" order by valorTotalDesconto ");
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		Query query = this.getSession().createSQLQuery(hql.toString())
			.addScalar("codigoProduto").addScalar("nomeProduto")
			.addScalar("numeroEdicao", StandardBasicTypes.LONG).addScalar("precoVenda")
			.addScalar("precoDesconto").addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
			.addScalar("nomeFornecedor").addScalar("dataRecolhimento")
			.addScalar("valorTotal").addScalar("valorTotalDesconto")
			.addScalar("idLancamento", StandardBasicTypes.LONG)
			.addScalar("possuiBrinde", StandardBasicTypes.BOOLEAN);
		
		this.aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsignadoCotaChamadaoDTO.class));
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaChamadaoDTO> obterConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select ")
			.append("produto.codigo as codigoProduto, ")
			.append("produto.nome as nomeProduto, ")
			.append("produtoEdicao.numeroEdicao as numeroEdicao, ")
			.append("mec.valoresAplicados.precoVenda as precoVenda, ")
			.append("(mec.valoresAplicados.precoComDesconto) as precoDesconto, ")
			.append("chamadaEncalheCota.qtdePrevista as reparte, ")
			.append("juridica.razaoSocial as nomeFornecedor, ")
			.append("chamadaEncalhe.dataRecolhimento as dataRecolhimento, ")
			.append("mec.valoresAplicados.precoVenda * mec.qtde as valorTotal, ")
			.append("(mec.valoresAplicados.valorComDesconto) * mec.qtde as valorTotalDesconto, ")
			.append("produtoEdicao.possuiBrinde as possuiBrinde ");
		
		hql.append(this.gerarQueryChamadasEncalhe(filtro));
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
				
				case CODIGO_PRODUTO:
					hql.append(" order by codigoProduto ");
					break;
					
				case NOME_PRODUTO:
					hql.append(" order by nomeProduto ");
					break;
					
				case EDICAO:
					hql.append(" order by numeroEdicao ");
					break;
				
				case BRINDE:
					hql.append(" order by possuiBrinde ");
					break;
					
				case PRECO_VENDA:
					hql.append(" order by precoVenda ");
					break;
					
				case PRECO_DESCONTO:
					hql.append(" order by precoDesconto ");
					break;
					
				case REPARTE:
					hql.append(" order by reparte ");
					break;
					
				case FORNECEDOR:
					hql.append(" order by nomeFornecedor ");
					break;
					
				case RECOLHIMENTO:
					hql.append(" order by dataRecolhimento ");
					break;
				
				case VALOR_TOTAL:
					hql.append(" order by valorTotal ");
					break;
				
				case VALOR_TOTAL_DESCONTO:
					hql.append(" order by valorTotalDesconto ");
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.aplicarParametrosParaPesquisaChamadaEncalhe(filtro, query);
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsignadoCotaChamadaoDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterTotalConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder("select count(consignadoCota.totalConsignados) as totalConsignados from ( ");
				
		hql.append("SELECT count(cota.ID) as totalConsignados ");
				
		hql.append(this.gerarQueryConsignados(filtro));
		
		hql.append(" ) as consignadoCota ");
		
		Query query = getSession().createSQLQuery(hql.toString())
			.addScalar("totalConsignados", StandardBasicTypes.LONG);
		
		this.aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterTotalConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
				
		hql.append("SELECT count(chamadaEncalhe.id) ");
				
		hql.append(this.gerarQueryChamadasEncalhe(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		this.aplicarParametrosParaPesquisaChamadaEncalhe(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	/**
	 * Gera a query de consignados da cota.
	 *   
	 * @param filtro - filtro da pesquisa
	 * 
	 * @return Query
	 */
	private StringBuilder gerarQueryConsignados(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("from COTA cota ")
	    	.append("inner join ESTOQUE_PRODUTO_COTA estoqueProdCota ")
	    	.append("on cota.ID = estoqueProdCota.COTA_ID ")
	        .append("inner join ESTUDO_COTA estudoCota ")
	        .append("on cota.ID = estudoCota.COTA_ID ")
	        .append("inner join ESTUDO estudo ")
	        .append("on estudoCota.ESTUDO_ID = estudo.ID ") 
	        .append("inner join PRODUTO_EDICAO produtoEdicao ") 
	        .append("on estudo.PRODUTO_EDICAO_ID = produtoEdicao.ID ") 
	        .append("inner join PRODUTO produto ") 
	        .append("on produtoEdicao.PRODUTO_ID = produto.ID ")
	        .append("inner join LANCAMENTO lancamento ") 
	        .append("on (produtoEdicao.ID = lancamento.PRODUTO_EDICAO_ID ")
	        .append("and estudo.DATA_LANCAMENTO = lancamento.DATA_LCTO_PREVISTA) ")
	        
	        .append("inner join MOVIMENTO_ESTOQUE_COTA mec ")
	        .append("on (mec.COTA_ID = cota.ID and mec.LANCAMENTO_ID = lancamento.ID) ")
	            
    		.append("inner join PRODUTO_FORNECEDOR produtoFornecedor ")
    		.append("on produtoFornecedor.PRODUTO_ID = produto.ID ")
	            
	    	.append("where estoqueProdCota.PRODUTO_EDICAO_ID = produtoEdicao.ID ") 
	    	
	    	.append("and (lancamento.STATUS = :statusLancamentoExpedido or lancamento.STATUS = :statusLancamentoEmBalanceamentoRec ) ")

//	        .append("and ( ")
//	        .append("(lancamento.STATUS = :statusLancamentoBalanceadoRec ")
//	        .append("and lancamento.DATA_REC_PREVISTA > :dataRecolhimento) ")
//	        .append("or (lancamento.STATUS = :statusLancamentoExpedido) ")
//	        .append("or (lancamento.STATUS = :statusLancamentoEmBalanceamentoRec) ")
//	        .append(") ")
// Comentado Por Eduardo "PunkRock" Castro em 09-11-2012 -> DD-MM-YYYY	
	    	
	        .append("and lancamento.DATA_REC_PREVISTA >= :dataRecolhimento ")

	        .append("and (estoqueProdCota.QTDE_RECEBIDA - estoqueProdCota.QTDE_DEVOLVIDA) > 0 ")
	        
	        .append("and not exists ( ")
	        .append("select chamadaEncalheCota.COTA_ID ")
	        .append("from CHAMADA_ENCALHE_COTA chamadaEncalheCota ") 
	        .append("inner join CHAMADA_ENCALHE chamadaEncalhe ")
	        .append("on chamadaEncalheCota.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID, COTA c ")
	        .append("where chamadaEncalheCota.COTA_ID = c.ID ")
	        .append("and chamadaEncalheCota.COTA_ID = cota.ID ")
	        .append("and chamadaEncalhe.PRODUTO_EDICAO_ID = produtoEdicao.ID ") 
	        .append("and chamadaEncalhe.TIPO_CHAMADA_ENCALHE in (:chamadaEncalheAntecipada, :chamadaEncalheChamadao) ")
            .append(")");
		
		if (filtro != null) {
		
			if (filtro.getNumeroCota() != null ) {
				
				hql.append("and cota.NUMERO_COTA = :numeroCota ");
			}

			if (filtro.getIdFornecedor() != null) {

				hql.append(" AND produtoFornecedor.FORNECEDORES_ID = :idFornecedor ");
			}

			if (filtro.getIdEditor() != null) {

				hql.append(" AND produto.EDITOR_ID = :idEditor ");
			}
		}
		
		hql.append(" group by lancamento.ID");
		
		return hql;
	}
	
	/**
	 * Gera a query de chamadas de encalhe da cota.
	 *   
	 * @param filtro - filtro da pesquisa
	 * 
	 * @return Query
	 */
	private StringBuilder gerarQueryChamadasEncalhe(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM ChamadaEncalheCota chamadaEncalheCota ")
			.append(" JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
			.append(" JOIN chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" JOIN produtoEdicao.produto produto ")
			.append(" JOIN produto.fornecedores fornecedores ")
			.append(" JOIN fornecedores.juridica juridica ")
			.append(" JOIN chamadaEncalheCota.cota cota ")
			.append(" JOIN cota.movimentoEstoqueCotas mec ")
			
			.append(" WHERE chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
			.append(" AND produto.tipoProduto.grupoProduto != :grupoProduto ")
			.append(" AND mec.tipoMovimento.grupoMovimento = :grupoMovimento ")
			.append(" AND mec.lancamento in (chamadaEncalheCota.chamadaEncalhe.lancamentos) ");
		
		if (filtro != null) {
		
			if (filtro.getDataChamadao() != null) {
				
				hql.append(" AND chamadaEncalhe.dataRecolhimento = :dataChamadao ");
			}
			
			if (filtro.getNumeroCota() != null ) {
				
				hql.append(" AND cota.numeroCota = :numeroCota ");
			}

			if (filtro.getIdFornecedor() != null) {

				hql.append(" AND fornecedores.id = :idFornecedor ");
			}

			if (filtro.getIdEditor() != null) {

				hql.append(" AND produto.editor.id = :idEditor ");
			}
		}
		
		return hql;
	}
	
	/**
	 * Aplica os parâmetros para a busca de cosignados da cota.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaConsignadosCota(FiltroChamadaoDTO filtro, 
													 	 	  Query query) {
		

//Comentado Por Eduardo "PunkRock" Castro em 09-11-2012 -> DD-MM-YYYY			
//		query.setParameter("statusLancamentoBalanceadoRec",
//						   StatusLancamento.BALANCEADO_RECOLHIMENTO.toString());
//		
		query.setParameter("statusLancamentoEmBalanceamentoRec",
				   StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.toString());
		
		query.setParameter("statusLancamentoExpedido", StatusLancamento.EXPEDIDO.toString());
		
		query.setParameter("chamadaEncalheAntecipada", TipoChamadaEncalhe.ANTECIPADA.toString());
		
		query.setParameter("chamadaEncalheChamadao", TipoChamadaEncalhe.CHAMADAO.toString());
		
		if (filtro == null) {
			
			return;
		}
		
		if (filtro.getDataChamadao() != null) {
			query.setParameter("dataRecolhimento", filtro.getDataChamadao());
		}
		
		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if (filtro.getIdEditor() != null) {
			query.setParameter("idEditor", filtro.getIdEditor());
		}
	}
	
	/**
	 * Aplica os parâmetros para a busca de chamada de encalhe.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaChamadaEncalhe(FiltroChamadaoDTO filtro, 
													 	 	 Query query) {
		
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.CHAMADAO);
		query.setParameter("grupoProduto", GrupoProduto.OUTROS);
		query.setParameter("grupoMovimento", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		if (filtro == null) {
			
			return;
		}
		
		if (filtro.getDataChamadao() != null) {
			query.setParameter("dataChamadao", filtro.getDataChamadao());
		}
		
		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if (filtro.getIdEditor() != null) {
			query.setParameter("idEditor", filtro.getIdEditor());
		}
	}

}
