package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TelefoneRepository;

@Repository
public class TelefoneRepositoryImpl extends AbstractRepositoryModel<Telefone, Long> implements TelefoneRepository {

	public TelefoneRepositoryImpl() {
		super(Telefone.class);
	}

	@Override
	public void removerTelefones(Collection<Long> idsTelefones) {
		StringBuilder hql = new StringBuilder("delete from Telefone t ");
		hql.append(" where t.id in (:ids) ")
		   .append(" and t.id not in (select tc.telefone.id from TelefoneCota tc where tc.telefone.id in (:ids)) ")
		   .append(" and t.id not in (select tf.telefone.id from TelefoneFornecedor tf where tf.telefone.id in (:ids)) ")
		   .append(" and t.id not in (select te.telefone.id from TelefoneEntregador te where te.telefone.id in (:ids)) ")
		   .append(" and t.id not in (select tp.telefone.id from TelefonePDV tp where tp.telefone.id in (:ids)) ")
		   .append(" and t.id not in (select tt.telefone.id from TelefoneTransportador tt where tt.telefone.id in (:ids)) ")
		   .append(" and t.id not in (select td.telefone.id from TelefoneDistribuidor td where td.telefone.id in (:ids)) ")
		   .append(" and t.id not in (select tfi.telefone.id from TelefoneFiador tfi where tfi.telefone.id in (:ids)) ")
		   .append(" and t.id not in (select te.telefone.id from TelefoneEditor te where te.telefone.id in (:ids)) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", idsTelefones);
		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Telefone> buscarTelefonesPessoa(Long idPessoa, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select telefone ");
		hql.append(" from Telefone telefone ")
		   .append(" where telefone.pessoa.id = :idPessoa ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and telefone.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPessoa", idPessoa);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}
}