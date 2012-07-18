package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
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

	
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriado(Date dataFeriado) {
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" select ");
		
		sql.append(" f.data as dataFeriado, 			");
		sql.append(" f.tipoFeriado as tipoFeriado,  	");
		sql.append(" localidade.id as idLocalidade,		");							
		sql.append(" uf.sigla as ufSigla, 				");							
		sql.append(" localidade.nome as nomeCidade, 	");							
		sql.append(" f.indRepeteAnualmente as indRepeteAnualmente, 	");							
		sql.append(" f.indOpera as indOpera,	 					");							
		sql.append(" f.indEfetuaCobranca as indEfetuaCobranca, 		");
		sql.append(" f.descricao as descricaoFeriado 				");
		
		sql.append(" from ");
		
		sql.append(" Feriado f ");
		
		sql.append(" where ");
			
		sql.append(" f.data = :dataFeriado ");
		
		sql.append(" or ");
		
		sql.append(" ( ");

		sql.append(" f.indRepeteAnualmente = :indRepeteAnualmente and ");
		
		sql.append(" day(f.data) = :diaFeriado and ");

		sql.append(" month(f.data) = :mesFeriado ");
		
		sql.append(" ) ");
		
		Query query = getSession().createQuery(sql.toString()).setResultTransformer(
				new AliasToBeanResultTransformer(CalendarioFeriadoDTO.class));
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFeriado);
		
		query.setParameter("dataFeriado",  dataFeriado);
		query.setParameter("diaFeriado", c.get(Calendar.DAY_OF_MONTH));
		query.setParameter("mesFeriado", c.get(Calendar.MONTH));
		query.setParameter("indRepeteAnualmente", true);
	
		return query.list();
		
	}

	public List<CalendarioFeriadoDTO> obterListaDataFeriado(Date dataInicial, Date dataFinal) {
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" select ");
		
		sql.append(" f.data, f.descricaoFeriado	");
		
		sql.append(" from ");
		
		sql.append(" Feriado f ");
		
		sql.append(" where ");
			
		sql.append(" f.data between :dataInicial and :dataFinal ");
		
		sql.append(" or f.indRepeteAnualmente = :indRepeteAnualmente ");
		
		Query query = getSession().createQuery(sql.toString()).setResultTransformer(new AliasToBeanResultTransformer(CalendarioFeriadoDTO.class));
		
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("indRepeteAnualmente", true);
	
		return query.list();
		
	}
	
}
