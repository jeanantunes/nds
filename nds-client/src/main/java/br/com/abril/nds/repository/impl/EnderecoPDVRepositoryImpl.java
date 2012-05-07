package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.repository.EnderecoPDVRepository;

@Repository
public class EnderecoPDVRepositoryImpl extends AbstractRepository<EnderecoPDV, Long> implements EnderecoPDVRepository {

	public EnderecoPDVRepositoryImpl() {
		super(EnderecoPDV.class);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EnderecoAssociacaoDTO> buscaEnderecosPDV(Long idPDV, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(EnderecoAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.endereco, t.tipoEndereco, enderecoPessoa) ")
		   .append(" from EnderecoPDV t, Endereco enderecoPessoa ")
		   .append(" where t.pdv.id = :idPDV ")
		   .append("   and t.endereco.id = enderecoPessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.endereco.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoEndereco ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPDV", idPDV);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Endereco> buscarEnderecosPessoaPorPDV(Long idPDV) {
		
		StringBuilder hql = new StringBuilder("select c.pessoa.enderecos ");
		hql.append(" from PDV c ")
		   .append(" where c.id = :idPDV");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPDV", idPDV);
		
		return query.list();
	}
	
	@Override
	public EnderecoPDV buscarEnderecoPorEnderecoPDV(Long idEndereco, Long idPDV) {
		
		StringBuilder hql = new StringBuilder("select t from EnderecoPDV t ");
		hql.append(" where t.endereco.id = :idEndereco ")
		   .append(" and   t.pdv.id   = :idPDV");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idEndereco", idEndereco);
		query.setParameter("idPDV", idPDV);
		query.setMaxResults(1);
		
		return (EnderecoPDV) query.uniqueResult();
	}
	
	@Override
	public void excluirEnderecosPorIdPDV(Long idPDV){
		
		Query query = this.getSession().createQuery("delete from EnderecoPDV where pdv.id = :idPDV ");
		query.setParameter("idPDV", idPDV);
		
		query.executeUpdate();
	}
	
	@Override
	public void excluirEnderecosPDV(Collection<Long> idsEnderecoPDV){
		
		Query query = this.getSession().createQuery("delete from EnderecoPDV where id in (:idsEnderecoPDV) ");
		query.setParameterList("idsEnderecoPDV", idsEnderecoPDV);
		
		query.executeUpdate();
	}
}
