package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.estoque.Diferenca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class DiferencaEstoqueRepositoryImpl extends AbstractRepository<Diferenca, Long> implements DiferencaEstoqueRepository {

	/**
	 * Construtor.
	 */
	public DiferencaEstoqueRepositoryImpl() {
		
		super(Diferenca.class);
	}

	@SuppressWarnings("unchecked")
	public List<Diferenca> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencasLancamento(filtro, false);
		
		if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case CODIGO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.codigo ";
					break;
				case DESCRICAO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.produto.descricao ";
					break;
				case EXEMPLARES:
					hql += "order by diferenca.qtde ";
					break;
				case NUMERO_EDICAO:
					hql += "order by diferenca.produtoEdicao.numeroEdicao ";
					break;
				case PACOTE_PADRAO:
					hql += "order by diferenca.produtoEdicao.pacotePadrao ";
					break;
				case PRECO_PRODUTO:
					hql += "order by diferenca.produtoEdicao.precoVenda ";
					break;
				case TIPO_DIFERENCA:
					hql += "order by diferenca.tipoDiferenca ";
					break;
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				hql += filtro.getPaginacao().getOrdenacao().toString();
			}
		}
		
		Query query = super.getSession().createQuery(hql);
		
		if (filtro.getDataMovimento() != null) {
			
			query.setParameter("dataMovimento", filtro.getDataMovimento());
		}
		
		if (filtro.getTipoDiferenca() != null) {
		
			query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
		}
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	public Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencasLancamento(filtro, true);
		
		Query query = getSession().createQuery(hql);
		
		if (filtro.getDataMovimento() != null) {
			
			query.setParameter("dataMovimento", filtro.getDataMovimento());
		}
		
		if (filtro.getTipoDiferenca() != null) {
		
			query.setParameter("tipoDiferenca", filtro.getTipoDiferenca());
		}
		
		return (Long) query.uniqueResult();
	}
	
	/*
	 * Gera a query de busca de diferenças para lançamento.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param totalizar - flag para contagem de total
	 * 
	 * @return Query
	 */
	private String gerarQueryDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro, 
												  boolean totalizar) {
		
		String hql;
		
		if (totalizar) {
			
			hql = "select count(diferenca) ";
			
		} else {
			
			hql = " select diferenca ";
		}
					
		hql += " from Diferenca diferenca "
			+  " left join diferenca.movimentoEstoque movimentoEstoque ";
		
		boolean whereUtilizado = false;
		
		if (filtro.getDataMovimento() != null) {
			
			hql += (!whereUtilizado) ? " where " : " and ";
						
			hql += " movimentoEstoque.dataInclusao = :dataMovimento ";
			
			whereUtilizado = true;
		}
		
		if (filtro.getTipoDiferenca() != null) {
			
			hql += (!whereUtilizado) ? " where " : " and ";
			
			hql += " diferenca.tipoDiferenca = :tipoDiferenca ";
			
			whereUtilizado = true;
		}
		
		return hql;
	}

	@SuppressWarnings("unchecked")
	public List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		String hql = " select diferenca "
				   + " from Diferenca diferenca "
				   
				   + " left join diferenca.itemRecebimentoFisico itemRecebimentoFisico "
				   
				   + " where diferenca.movimentoEstoque is not null ";
				   
		/*if (filtro.getClass() != null) {
			hql += " and diferenca.produtoEdicao.produto.codigo = :codigoProduto ";
		}*/
				   
		Query query = getSession().createQuery(hql);
		
		return query.list();
	}
	
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		String hql = " select count(diferenca) "
				   + " from Diferenca diferenca "
				   
				   + " left join diferenca.itemRecebimentoFisico itemRecebimentoFisico "
				   
				   + " where diferenca.movimentoEstoque is not null ";
		
		Query query = getSession().createQuery(hql);
		
		return (Long) query.uniqueResult();
	}

}
