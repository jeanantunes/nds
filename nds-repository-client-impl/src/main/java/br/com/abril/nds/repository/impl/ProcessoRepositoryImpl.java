package br.com.abril.nds.repository.impl;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ProcessoRepository;

@Repository
public class ProcessoRepositoryImpl extends AbstractRepositoryModel<Processo,Long> implements ProcessoRepository {

	/**
	 * Construtor padrão
	 */
	public ProcessoRepositoryImpl() {
		super(Processo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Processo> obterTodosProcessos() {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT proc FROM Processo proc ");
		
		Query query = this.getSession().createQuery(hql.toString());
		return query.list();
	}

	@Override
	public Processo buscarPeloNome(String nome) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT proc FROM Processo proc ");
		hql.append(" where proc.nome =: nome ");
		hql.append(" order by proc.codigo ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if(nome!= null && !nome.isEmpty()){
			query.setParameter("nome", nome);
		}
		
		return (Processo) query.uniqueResult();
	} 
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ItemDTO<String, String>> buscarProcessos(String parametros[]) {
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select proc.nome as key, proc.descricao as value ")
		   .append(" from Processo proc ")
		   .append(" Where ")
		   .append(" proc.nome in(:parametros)");

		Query query = getSession().createQuery(hql.toString());
		query.setParameterList("parametros", parametros);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		
		return query.setCacheable(true).list();
	}
	
}