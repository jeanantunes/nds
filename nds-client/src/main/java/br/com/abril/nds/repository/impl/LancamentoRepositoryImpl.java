package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
			Long[] idsFornecedores) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder("select lancamento from Lancamento lancamento ");
		hql.append("join fetch lancamento.produtoEdicao produtoEdicao ");
		hql.append("join fetch produtoEdicao.produto produto ");
		hql.append("join fetch produto.fornecedores fornecedor ");
		hql.append("where lancamento.dataLancamentoPrevista between :inicio and :fim ");
		hql.append("and fornecedor.permiteBalanceamento = :permiteBalanceamento ");
		params.put("inicio", inicio);
		params.put("fim", fim);
		params.put("permiteBalanceamento", true);
		if (idsFornecedores != null) {
			hql.append("and fornecedor.id in (:idsFornecedores) ");
			params.put("idsFornecedores", idsFornecedores);
		}
		hql.append("order by produto.periodicidade desc, lancamento.reparte asc");
		Query query = getSession().createQuery(hql.toString());
		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.list();
	}

}
