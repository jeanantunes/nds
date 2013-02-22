package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaEnvioRepository;

@Repository
public class NotaEnvioRepositoryImpl  extends AbstractRepositoryModel<NotaEnvio, Long> implements NotaEnvioRepository{

	public NotaEnvioRepositoryImpl() {
		super(NotaEnvio.class);
	}

	@Override
	public boolean verificarExistenciaEmissaoDeNotasPeloEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(*) from EstudoCota estudoCota")
		   .append(" where estudoCota.estudo.id = :idEstudo") 
		   .append(" and   estudoCota.itemNotaEnvio = null");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("idEstudo", idEstudo);
		
		
		Integer count = (Integer)query.uniqueResult();
		
		return count == 0;
	}
}
