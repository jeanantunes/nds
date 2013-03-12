package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RegiaoRepository;

@Repository
public class RegiaoRepositoryImpl extends AbstractRepositoryModel<Regiao, Long> implements RegiaoRepository {
	
	public RegiaoRepositoryImpl( ) {
		super(Regiao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegiaoDTO> buscarRegiao() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT");
		hql.append(" regiao.id as idRegiao,");
		hql.append(" regiao.nomeRegiao as nomeRegiao, ");
		hql.append(" regiao.regiaoIsFixa as isFixa, ");
		hql.append(" regiao.dataRegiao as dataAlteracao, ");
		hql.append(" usuario.nome as nomeUsuario ");
		
		hql.append(" FROM Regiao as regiao ");
		hql.append(" JOIN regiao.idUsuario as usuario");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RegiaoDTO.class));
		
		return query.list();
	}

	@Override
	public void execucaoQuartz() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" delete from regiao WHERE datediff(now(),data_regiao)>90 and REGIAO_IS_FIXA = 0; ");
		SQLQuery createSQLQuery = this.getSession().createSQLQuery(hql.toString());
		
		createSQLQuery.executeUpdate();
	}
	
}
