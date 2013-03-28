package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.envio.nota.NotaEnvio;
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
	
	@Override
	public Date obterMenorDataLancamentoPorNotaEnvio(Long numeroNotaEnvio) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select min(lancamento.dataLancamentoDistribuidor) ");
		hql.append(" from NotaEnvio notaEnvio ");
		hql.append(" join notaEnvio.listaItemNotaEnvio itemNotaEnvio ");
		hql.append(" join itemNotaEnvio.estudoCota estudoCota ");
		hql.append(" join estudoCota.estudo estudo ");
		hql.append(" join estudo.lancamentos lancamento ");
		hql.append(" where notaEnvio.id = :numeroNotaEnvio ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroNotaEnvio", numeroNotaEnvio);
		
		return (Date) query.uniqueResult();
	}
	
}
