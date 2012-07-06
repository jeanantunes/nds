package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.repository.FeriadoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Feriado}
 * 
 * @author Discover Technology
 */
@Repository
public class FeriadoRepositoryImpl extends AbstractRepositoryModel<Feriado, Long> 
										 implements FeriadoRepository {

	/**
	 * Construtor padrão.
	 */
	public FeriadoRepositoryImpl() {
		super(Feriado.class);
	}

	@Override
	public List<Feriado> obterFeriados(Date data, TipoFeriado tipoFeriado, String uf, Long idLocalidade) {

		Criteria criteria = super.getSession().createCriteria(Feriado.class);
		
		if(data!=null) {
			criteria.add(Restrictions.eq("data", data));
		}
		
		if(tipoFeriado!=null) {
			criteria.add(Restrictions.eq("tipoFeriado", tipoFeriado));
		}
		
		if(uf!=null) {
			criteria.add(Restrictions.eq("unidadeFederacao.sigla", uf));
		}
		
		if(idLocalidade!=null) {
			criteria.add(Restrictions.eq("localidade.id", idLocalidade));
		}
		
		
		return criteria.list();
	}
	
	@Override
	public Feriado obterFeriado(Date data, TipoFeriado tipoFeriado, String uf, Long idLocalidade) {

		Criteria criteria = super.getSession().createCriteria(Feriado.class);
		
		criteria.add(Restrictions.eq("data", data));
		criteria.add(Restrictions.eq("tipoFeriado", tipoFeriado));
		criteria.add(Restrictions.eq("unidadeFederacao.sigla", uf));
		criteria.add(Restrictions.eq("localidade.id", idLocalidade));
		
		return (Feriado) criteria.uniqueResult();
	}

	
	
	
}
