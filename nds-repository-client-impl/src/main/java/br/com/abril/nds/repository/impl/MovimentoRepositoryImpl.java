package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.movimentacao.Movimento}
 * 
 * @author Discover Technology
 */
@Repository
public class MovimentoRepositoryImpl extends AbstractRepositoryModel<Movimento, Long> 
												   implements MovimentoRepository {

	public MovimentoRepositoryImpl() {
		super(Movimento.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoAprovacaoDTO> obterMovimentosAprovacao(FiltroControleAprovacaoDTO filtro) {
		
		String hql = this.gerarQueryMovimentosAprovacao(filtro, false);
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case TIPO_MOVIMENTO:
					hql += "order by movimento.tipoMovimento.descricao ";
					break;
					
				case DATA_MOVIMENTO:
					hql += "order by movimento.dataCriacao ";
					break;
					
				case NUMERO_COTA:
					hql += "order by cota.numeroCota ";
					break;
				
				case NOME_COTA:
					hql += "order by (case when (pessoa.nome is not null) then ( pessoa.nome )"
						+  " when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )"
						+  " else null end)";
					break;
					
				case VALOR:
					hql += "order by movimento.valor ";
					break;
					
				case PARCELAS:
					hql += "order by movimento.parcelas ";
					break;
					
				case PRAZO:
					hql += "order by movimento.prazo ";
					break;
					
				case REQUERENTE:
					hql += "order by movimento.usuario.nome ";
					break;
				
				case STATUS:
					hql += "order by movimento.status ";
					break;
					
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql += filtro.getPaginacao().getOrdenacao().toString();
			}
		}
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaMovimentoAprovacao(filtro, query);
		
 		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MovimentoAprovacaoDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterTotalMovimentosAprovacao(FiltroControleAprovacaoDTO filtro) {
		
		String hql = this.gerarQueryMovimentosAprovacao(filtro, true);
		
		Query query = getSession().createQuery(hql);
		
		aplicarParametrosParaPesquisaMovimentoAprovacao(filtro, query);
		
		return (Long) query.uniqueResult();
	}
	
	/**
	 * Aplica os parâmetros para a busca de movimentos para aprovação.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param query - objeto query
	 */
	private void aplicarParametrosParaPesquisaMovimentoAprovacao(FiltroControleAprovacaoDTO filtro, 
													 	 		 Query query) {
		
		query.setParameter("aprovacaoAutomatica", false);
		
		if (filtro == null) {
			
			return;
		}
		
		if (filtro.getIdTipoMovimento() != null) {
			query.setParameter("idTipoMovimento", filtro.getIdTipoMovimento());
		}
		
		if (filtro.getDataMovimento() != null) {
			query.setParameter("dataMovimento", filtro.getDataMovimento());
		}
		
		if (filtro.getStatusAprovacao() != null) {
			query.setParameter("statusAprovacao", filtro.getStatusAprovacao());
		}
	}
	
	/**
	 * Gera a query de movimentos para aprovação.
	 *   
	 * @param filtro - filtro da pesquisa
	 * @param totalizar - flag para contagem de total
	 * 
	 * @return Query
	 */
	private String gerarQueryMovimentosAprovacao(FiltroControleAprovacaoDTO filtro, 
												 boolean totalizar) {
		
		String hql;
		
		if (totalizar) {
			
			hql = "select count(movimento) ";
			
		} else {
			
			hql = " select "
				+ " movimento.id as idMovimento, movimento.tipoMovimento.descricao as descricaoTipoMovimento,"
				+ " movimento.dataCriacao as dataCriacao, cota.numeroCota as numeroCota,"
				+ " (case when (pessoa.nome is not null) then ( pessoa.nome )"
				+ " when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )"
				+ " else null end) as nomeCota, movimento.valor as valor,"
				+ " movimento.parcelas as parcelas, movimento.prazo as prazo,"
				+ " movimento.usuario.nome as nomeUsuarioRequerente, movimento.status as statusMovimento,"
				+ " movimento.motivo as motivo";
		}
		
		hql += " from Movimento movimento "
			+ " left join movimento.cota cota "
			+ " left join cota.pessoa pessoa ";
		
		hql += " where movimento.tipoMovimento.aprovacaoAutomatica = :aprovacaoAutomatica ";
		
		if (filtro != null) {
		
			if (filtro.getIdTipoMovimento() != null) {
				
				hql += " and movimento.tipoMovimento.id = :idTipoMovimento ";
			}
			
			if (filtro.getDataMovimento() != null) {
				
				hql += "  and movimento.dataCriacao = :dataMovimento ";
			}
			
			if (filtro.getStatusAprovacao() != null) {
				
				hql += "  and movimento.status = :statusAprovacao ";
				
			}
		}
		
		return hql;
	}
	
	public boolean existeMovimentoEstoquePendente(List<GrupoMovimentoEstoque> gruposMovimento) {
		
		String hql = " select count(movimento) "
			+ " from Movimento movimento "
			+ " where movimento.tipoMovimento.grupoMovimentoEstoque in (:gruposMovimento)"
			+ " and status = :status ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameterList("gruposMovimento", gruposMovimento);
		
		query.setParameter("status", StatusAprovacao.PENDENTE);
		
		long resultado = (Long) query.uniqueResult();
		
		return resultado > 0;
	}
	
	public boolean existeMovimentoFinanceiroPendente(List<GrupoMovimentoFinaceiro> gruposMovimento) {
		
		String hql = " select count(movimento) "
			+ " from Movimento movimento "
			+ " where movimento.tipoMovimento.grupoMovimentoFinaceiro in (:gruposMovimento)"
			+ " and status = :status ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameterList("gruposMovimento", gruposMovimento);
		
		query.setParameter("status", StatusAprovacao.PENDENTE);
		
		long resultado = (Long) query.uniqueResult();
		
		return resultado > 0;
	}
	
	@Override
	public Movimento buscarPorId(Long id) {
		 
		String hql = " select movimento from Movimento movimento where movimento.id=:id ";
			
		Query query = getSession().createQuery(hql);
			
		query.setParameter("id", id);
			
		return (Movimento) query.uniqueResult();
	}
	
}
