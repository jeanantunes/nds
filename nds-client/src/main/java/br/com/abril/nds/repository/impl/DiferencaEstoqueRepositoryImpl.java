package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DiferencaDTO;
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
	public List<DiferencaDTO> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		String hql = this.gerarQueryDiferencasLancamento(filtro, false);
		
		if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case CODIGO_PRODUTO:
					hql += "order by produtoEdicao.produto.codigo ";
					break;
				case DESCRICAO_PRODUTO:
					hql += "order by produtoEdicao.produto.descricao ";
					break;
				case EXEMPLARES:
					hql += "order by movimentoEstoque.diferenca.qtde ";
					break;
				case NUMERO_EDICAO:
					hql += "order by produtoEdicao.numeroEdicao ";
					break;
				case PACOTE_PADRAO:
					hql += "order by produtoEdicao.pacotePadrao ";
					break;
				case PRECO_PRODUTO:
					hql += "order by produtoEdicao.precoCusto ";
					break;
				case TIPO_DIFERENCA:
					hql += "order by movimentoEstoque.diferenca.tipoDiferenca ";
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
	 * @param ehParaQuantidadeTotal - flag para contagem de total
	 * 
	 * @return Query
	 */
	private String gerarQueryDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro, 
												  boolean ehParaQuantidadeTotal) {
		
		String hql;
		
		if (ehParaQuantidadeTotal) {
			
			hql = "select count(movimentoEstoque) ";
			
		} else {
			
			hql = " select new " + DiferencaDTO.class.getCanonicalName() + "( "
				+ " produtoEdicao, movimentoEstoque) ";
		}
					
		hql += " from ProdutoEdicao produtoEdicao, MovimentoEstoque movimentoEstoque "
			+  " where movimentoEstoque.produtoEdicao.id = produtoEdicao.id ";
		
		if (filtro.getDataMovimento() != null) {
			
			hql += " and movimentoEstoque.dataInclusao = :dataMovimento ";
		}
		
		if (filtro.getTipoDiferenca() != null) {
			
			hql += " and movimentoEstoque.diferenca.tipoDiferenca = :tipoDiferenca ";
		}
		
		return hql;
	}

	public List<DiferencaDTO> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		return null;
	}
	
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		return null;
	}

}
