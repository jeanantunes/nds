package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.util.DateUtil;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Feriado}
 * 
 * @author Discover Technology
 */
@Repository
public class FeriadoRepositoryImpl extends
		AbstractRepositoryModel<Feriado, Long> implements FeriadoRepository {

	/**
	 * Construtor padrão.
	 */
	public FeriadoRepositoryImpl() {
		super(Feriado.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Feriado> obterFeriados(Date data, TipoFeriado tipoFeriado,
			String uf, String localidade) {

		Criteria criteria = super.getSession().createCriteria(Feriado.class);

		if (data != null) {
			criteria.add(Restrictions.eq("data", data));
		}

		if (tipoFeriado != null) {
			criteria.add(Restrictions.eq("tipoFeriado", tipoFeriado));
		}

		if (uf != null) {
			criteria.add(Restrictions.eq("unidadeFederacao", uf));
		}

		if (localidade != null) {
			criteria.add(Restrictions.eq("localidade", localidade));
		}
		
		criteria.setCacheable(true);
		
		return criteria.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Feriado> obterFeriados(Date data, List<TipoFeriado> tiposFeriado, Boolean indOpera) {

		Criteria criteria = super.getSession().createCriteria(Feriado.class);

		if (data != null) {
			criteria.add(Restrictions.eq("data", data));
		}

		if (tiposFeriado != null) {
			criteria.add(Restrictions.in("tipoFeriado", tiposFeriado));
		}
		
		if (indOpera != null) {
			criteria.add(Restrictions.eq("indOpera", indOpera));
		}

		return criteria.list();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoMensal(
			int mes, int ano) {

		StringBuffer sql = new StringBuffer("");

		sql.append(" select ");
		
		sql.append(this.obterSelectCalendarioDTO());

		sql.append(" from ");

		sql.append(" Feriado f ");

		sql.append(" where ");

		sql.append(" month(f.data) = :mesFeriado and 	");

		sql.append(" year(f.data) = :anoFeriado 		");

		sql.append(" or ");

		sql.append(" ( ");

		sql.append(" f.indRepeteAnualmente = :indRepeteAnualmente and ");

		sql.append(" month(f.data) = :mesFeriado ");

		sql.append(" ) ");

		sql.append(" order by f.data asc ");

		Query query = getSession().createQuery(sql.toString())
				.setResultTransformer(
						new AliasToBeanResultTransformer(
								CalendarioFeriadoDTO.class));

		query.setParameter("mesFeriado", mes);

		query.setParameter("anoFeriado", ano);

		query.setParameter("indRepeteAnualmente", true);

		return query.list();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoDataEspecifica(
			Date dataFeriado) {

		StringBuffer sql = new StringBuffer("");

		sql.append(" select ");
		
		sql.append(this.obterSelectCalendarioDTO());
		
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

		Query query = getSession().createQuery(sql.toString())
				.setResultTransformer(
						new AliasToBeanResultTransformer(
								CalendarioFeriadoDTO.class));

		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(dataFeriado);

		query.setParameter("dataFeriado", dataFeriado);
		query.setParameter("diaFeriado", c.get(Calendar.DAY_OF_MONTH));
		query.setParameter("mesFeriado", (c.get(Calendar.MONTH) + 1));
		query.setParameter("indRepeteAnualmente", true);

		return query.list();

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoPeriodo(
			Date dataInicial, Date dataFinal) {

		StringBuffer sql = new StringBuffer("");

		sql.append(" select ");

		sql.append(this.obterSelectCalendarioDTO());
		
		sql.append(" from ");

		sql.append(" Feriado f ");

		sql.append(" where ");

		sql.append(" f.data between :dataInicial and :dataFinal ");

		sql.append(" or f.indRepeteAnualmente = :indRepeteAnualmente ");

		sql.append(" order by f.data asc ");

		Query query = getSession().createQuery(sql.toString())
				.setResultTransformer(
						new AliasToBeanResultTransformer(
								CalendarioFeriadoDTO.class));

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("indRepeteAnualmente", true);

		return query.list();

	}

	@Override
	public Long adicionar(Feriado entity) {
		verificarFeriadoAnualExistente(entity);
		return super.adicionar(entity);
	}
	
	@Override
	public void alterar(Feriado entity) {
		verificarFeriadoAnualExistente(entity);
		super.alterar(entity);
	}

	/**
	 * Verifica se já existe um feriado com repetição anual cadastrado com 
	 * os mesmo dia e mês e tipo do feriado recebido como parâmetro
	 * @param feriado feriado para verificação de feriado com repetição anual 
	 * já cadastrado com as caracteristicas do feriado recebido
	 */
	protected void verificarFeriadoAnualExistente(Feriado feriado) {
		Date data = feriado.getData();
		TipoFeriado tipoFeriado = feriado.getTipoFeriado();
		Feriado existente;
		
		if (tipoFeriado.equals(TipoFeriado.MUNICIPAL)) {
			existente = obterFeriadoAnualLocalidade(data, feriado.getLocalidade());
		
		} else {
			existente = obterFeriadoAnualTipo(data, tipoFeriado);
		}
		
		if (existente != null && !feriado.equals(existente)) {
			throw new DataIntegrityViolationException(
					"Feriado anual com o tipo " + tipoFeriado
							+ " já cadastrado para a data " + DateUtil.formatarDataPTBR(data));
		}
	}

	@Override
	public Feriado obterFeriadoAnualTipo(Date data, TipoFeriado tipo) {
		Validate.notNull(data,
				"Data para pesquisa do feriado não deve ser nula!");
		Validate.notNull(tipo, "Tipo do feriado não deve ser nulo!");

		StringBuilder hql = new StringBuilder(
				"from Feriado where tipoFeriado = :tipoFeriado ");
		hql.append(" and indRepeteAnualmente = :anual and day(data) = day(:dataPesquisa) ");
		hql.append(" and month(data) = month(:dataPesquisa)");

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("tipoFeriado", tipo);
		query.setParameter("anual", true);
		query.setParameter("dataPesquisa", data);
		
		query.setMaxResults(1);
		
		return (Feriado) query.uniqueResult();
	}
	
	
	private StringBuilder obterSelectCalendarioDTO() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" f.id as idFeriado, 				");
		sql.append(" f.data as dataFeriado, 			");
		sql.append(" f.tipoFeriado as tipoFeriado,  	");
		sql.append(" f.localidade as localidade,		");
		sql.append(" f.unidadeFederacao as ufSigla, 	");		
		sql.append(" f.indRepeteAnualmente as indRepeteAnualmente, 	");
		sql.append(" f.indOpera as indOpera,	 					");
		sql.append(" f.indEfetuaCobranca as indEfetuaCobranca, 		");
		sql.append(" f.descricao as descricaoFeriado 				");
		
		return sql;
	}

	@Override
	public Feriado obterFeriadoAnualLocalidade(Date data, String localidade) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT f FROM Feriado f WHERE f.tipoFeriado = :tipoFeriado ");
		hql.append(" AND f.localidade = :localidade			  ");
		hql.append(" AND f.indRepeteAnualmente = :anual       ");
		hql.append(" AND day(f.data)   = day(:dataPesquisa)   ");
		hql.append(" AND month(f.data) = month(:dataPesquisa) ");
		hql.append(" AND year(f.data)  = year(:dataPesquisa)  ");

		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("tipoFeriado", TipoFeriado.MUNICIPAL);
		query.setParameter("anual", true);
		query.setParameter("dataPesquisa", data);
		query.setParameter("localidade", localidade);
		
		query.setMaxResults(1);
		
		return (Feriado) query.uniqueResult();
		
		
	}
	
	@Override
	public boolean isFeriado(Date data){
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" count(f.id) from Feriado f ")
		   .append(" where f.data = :data");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", data);
		
		return (Long)query.uniqueResult() > 0;
	}
}
