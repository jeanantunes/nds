package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.LancamentoRepository;

@Repository
public class LancamentoRepositoryImpl extends
		AbstractRepository<Lancamento, Long> implements LancamentoRepository {

	public LancamentoRepositoryImpl() {
		super(Lancamento.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosBalanceamentoMartriz(Date inicio, Date fim,
			Long... idsFornecedores) {
		StringBuilder hql = new StringBuilder("select lancamento from Lancamento lancamento ");
		hql.append("join fetch lancamento.produtoEdicao produtoEdicao ");
		hql.append("join fetch produtoEdicao.produto produto ");
		hql.append("join fetch produto.fornecedores fornecedor ");
		hql.append("where lancamento.dataLancamentoPrevista between :inicio and :fim ");
		hql.append("and fornecedor.permiteBalanceamento = :permiteBalanceamento ");
	
		boolean filtraFornecedores = idsFornecedores.length > 0;
		if (filtraFornecedores) {
			hql.append("and fornecedor.id in (:idsFornecedores) ");
		}
		hql.append("order by produto.periodicidade asc");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		query.setParameter("permiteBalanceamento", true);
		if (filtraFornecedores) {
			query.setParameterList("idsFornecedores", idsFornecedores);
		}

		return query.list();
	}

	@Override
	public void atualizarLancamento(Long idLancamento, Date novaDataLancamentoPrevista) {
		StringBuilder hql = new StringBuilder("update Lancamento set ");
		hql.append(" dataLancamentoPrevista = :novaDataLancamentoPrevista, ")
		   .append(" reparte = 0 ")
		   .append(" where id = :id");
		
		Query query = 
				this.getSession().createQuery(hql.toString());
		
		query.setParameter("novaDataLancamentoPrevista", novaDataLancamentoPrevista);
		query.setParameter("id", idLancamento);
		
		query.executeUpdate();
	}

}
