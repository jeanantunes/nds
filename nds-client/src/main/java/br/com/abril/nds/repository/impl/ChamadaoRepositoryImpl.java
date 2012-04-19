package br.com.abril.nds.repository.impl;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ChamadaoRepository;

@Repository
public class ChamadaoRepositoryImpl extends AbstractRepository<Cota,Long> implements ChamadaoRepository {

	
	/**
	 * Construtor padrão
	 */
	public ChamadaoRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	public ResumoConsignadoCotaChamadaoDTO obterResumoConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ")
			.append(" sum(estoqueProdutoCota.qtdeRecebida ")
			.append(" - estoqueProdutoCota.qtdeDevolvida) as qtdExemplaresTotal, ")
			.append(" sum(produtoEdicao.precoVenda * ")
			.append(" (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) as valorTotal ");
		
		hql.append(this.gerarQueryConsignados(filtro));
		
		Query query = this.getSession().createQuery(hql.toString());
		
		aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
			ResumoConsignadoCotaChamadaoDTO.class));
			
		return (ResumoConsignadoCotaChamadaoDTO) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaChamadaoDTO> obterConsignadosParaChamadao(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		//TODO: fornecedor
		hql.append("SELECT ")
			.append(" produto.codigo as codigoProduto, ")
			.append(" produto.nome as nomeProduto, ")
			.append(" produtoEdicao.numeroEdicao as numeroEdicao, ")
			.append(" produtoEdicao.precoVenda as precoCapa, ")
			.append(" produtoEdicao.desconto as precoComDesconto, ")
			.append(" (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) as reparte, ")
			.append(" juridica.razaoSocial as nomeFornecedor, ")
			.append(" lancamento.dataRecolhimentoPrevista as dataRecolhimento, ")
			.append(" (produtoEdicao.precoVenda * ")
			.append(" (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) as valorTotal ");
		
		hql.append(this.gerarQueryConsignados(filtro));
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				//TODO: acertar ordenações.
			
				case CODIGO_PRODUTO:
					hql.append(" order by produto.codigo ");
					break;
					
				case NOME_PRODUTO:
					hql.append(" order by produto.nome ");
					break;
					
				case EDICAO:
					hql.append(" order by produtoEdicao.numeroEdicao ");
					break;
				
				case PRECO_CAPA:
					hql.append(" order by produtoEdicao.precoVenda ");
					break;
					
				case VALOR_DESCONTO:
					hql.append(" order by produtoEdicao.desconto ");
					break;
					
				case REPARTE:
					hql.append(" order by (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) ");
					break;
					
				case FORNECEDOR:
					hql.append(" order by juridica.razaoSocial ");
					break;
					
				case RECOLHIMENTO:
					hql.append(" order by lancamento.dataRecolhimentoPrevista ");
					break;
				
				case VALOR_TOTAL:
					hql.append(" order by (produtoEdicao.precoVenda * ")
						.append(" (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) ");
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
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
		
		StringBuilder hql = new StringBuilder();
				
		hql.append("SELECT count(cota) ");
				
		hql.append(this.gerarQueryConsignados(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		aplicarParametrosParaPesquisaConsignadosCota(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	private StringBuilder gerarQueryConsignados(FiltroChamadaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM ")
			.append(" Cota cota ")
			.append(" JOIN cota.estoqueProdutoCotas estoqueProdutoCota ")
			.append(" JOIN cota.estudoCotas estudoCota ")
			.append(" JOIN estudoCota.estudo estudo ")
			.append(" JOIN estudo.produtoEdicao produtoEdicao ")
			.append(" JOIN produtoEdicao.produto produto ")
			.append(" JOIN produtoEdicao.lancamentos lancamento ")
			.append(" JOIN produto.fornecedores fornecedor ")
			.append(" JOIN fornecedor.juridica juridica ");
				
		hql.append(" WHERE lancamento.dataRecolhimentoPrevista > :dataAtual ")
			.append(" AND lancamento.status = :status ")
			.append(" AND (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) > 0 ");
				
		if (filtro != null) {
		
			if (filtro.getNumeroCota() != null ) {
				
				hql.append(" AND cota.numeroCota = :numeroCota ");
			}

			if (filtro.getIdFornecedor() != null) {
				
				hql.append(" AND fornecedor.id = :idFornecedor ");
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
		
		if (filtro == null) {
			
			return;
		}
		
		//TODO: new Date()?
		query.setParameter("dataAtual", new Date());
		query.setParameter("status", StatusLancamento.EXPEDIDO);
		
		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}
		
		if (filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
	}
	
	
}
