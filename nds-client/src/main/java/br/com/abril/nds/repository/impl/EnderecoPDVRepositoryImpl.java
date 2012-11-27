package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.repository.EnderecoPDVRepository;

@Repository
public class EnderecoPDVRepositoryImpl extends AbstractRepositoryModel<EnderecoPDV, Long> implements EnderecoPDVRepository {

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
		
		StringBuilder hql = new StringBuilder("select c.cota.pessoa.enderecos ");
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemDTO<Integer, String>> buscarMunicipioPdvPrincipal() {
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select endereco.codigoCidadeIBGE as key, endereco.cidade as value ")
		   .append(" from EnderecoCota enderecoCota ")
		   .append(" join enderecoCota.endereco endereco ")
		   .append(" group by endereco.cidade");

		/*.append(" from PDV pdv join pdv.enderecos enderecoPDV join enderecoPDV.endereco endereco ")
			.append(" where pdv.caracteristicas.pontoPrincipal = true ")
			.append(" group by endereco.codigoCidadeIBGE, endereco.cidade ")
			.append(" order by endereco.cidade ");*/
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		
		return query.list();
	}
	
	@Override
	public Endereco buscarMunicipioPdvPrincipal(Integer codigoCidadeIBGE) {
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select endereco from ")
			.append(" PDV pdv join pdv.enderecos enderecoPDV join enderecoPDV.endereco endereco ")
			.append(" where pdv.caracteristicas.pontoPrincipal = true ")
			.append(" and endereco.codigoCidadeIBGE =:codigoCidadeIBGE ")
			.append(" group by endereco.cidade ")
			.append(" order by endereco.cidade ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("codigoCidadeIBGE", codigoCidadeIBGE);
		query.setMaxResults(1);
		
		return (Endereco) query.uniqueResult();
	}
	
	@Override
	public Endereco buscarEnderecoPrincipal(Long idPdv){
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select endereco from ")
			.append(" PDV pdv join pdv.enderecos enderecoPDV join enderecoPDV.endereco endereco ")
			.append(" where pdv.caracteristicas.pontoPrincipal = true ")
			.append(" and pdv.id = :idPdv ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idPdv", idPdv);
		query.setMaxResults(1);
		
		return (Endereco) query.uniqueResult();
	}
}
