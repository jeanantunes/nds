package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.FormaCobrancaRepository;

@Repository
public class FormaCobrancaRepositoryImpl extends AbstractRepositoryModel<FormaCobranca,Long> implements FormaCobrancaRepository  {

	
	/**
	 * Construtor padrão
	 */
	public FormaCobrancaRepositoryImpl() {
		super(FormaCobranca.class);
	}
	
	/**
	 * Obtém forma de cobranca para os parametros passados.
	 * @param Tipo de Cobrança
	 * @return {{@link br.com.abril.nds.model.cadastro.FormaCobranca}}
	 */
	@Override
	public FormaCobranca obterPorTipoEBanco(TipoCobranca tipo, Banco banco) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from FormaCobranca f ");		
		hql.append(" where f.tipoCobranca = :tipoCobranca  ");
		if (banco!=null){
		    hql.append(" and f.banco = :banco  ");
		}
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("tipoCobranca", tipo);
        if (banco!=null){
        	query.setParameter("banco", banco);
        }
        query.setMaxResults(1);
		return (FormaCobranca) query.uniqueResult();
	}

	
	/**
	 * Obtém uma lista de Bancos para os parametros passados.
	 * @param Tipo de Cobrança
	 * @return {@link List<Banco>}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Banco> obterBancosPorTipoDeCobranca(TipoCobranca tipo) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f.banco from FormaCobranca f ");		
		hql.append(" where f.tipoCobranca = :tipoCobranca  ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("tipoCobranca", tipo);
		return query.list();
	}

	
	/**
	 * Obtém a forma de cobranca Principal da Cota
	 * @param Cota
	 * @return FormaCobranca principal
	 */
	@Override
	public FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from FormaCobranca f ");		
		hql.append(" where f.principal = :principal ");
		hql.append(" and f.parametroCobrancaCota.cota.id = :idCota ");
		Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("principal", true);
        query.setParameter("idCota", idCota);
        query.setMaxResults(1);
        return (FormaCobranca) query.uniqueResult();
	}

	@Override
	public FormaCobranca obterFormaCobrancaPrincipal() {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from PoliticaCobranca p ");		
		hql.append(" join p.formaCobranca f ");		
		hql.append(" where p.principal = :principal ");
		Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("principal", true);
        query.setMaxResults(1);
		return (FormaCobranca) query.uniqueResult();
	}

	
	/**
	 * Obtém lista de forma de cobranca da Cota
	 * @param Cota
	 * @return {@link List<FormaCobranca>}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobranca> obterFormasCobrancaCota(Cota cota) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from FormaCobranca f ");		
		hql.append(" where f.parametroCobrancaCota.cota = :pCota ");
		hql.append(" and f.ativa = true ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pCota", cota);
        return query.list();
	}
	
	
	/**
	 * Obtém quantidade de forma de cobranca da Cota
	 * @param Cota
	 * @return long: Quantidade de formas de cobrança para a Cota
	 */
	@Override
	public int obterQuantidadeFormasCobrancaCota(Cota cota) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from FormaCobranca f ");		
		hql.append(" where f.parametroCobrancaCota.cota = :pCota ");
		hql.append(" and f.ativa = true ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pCota", cota);
        return query.list().size();
	}
	
	/**
	 * Desativa forma de cobrança
	 * @param idBanco
	 */
	@Override
	public void desativarFormaCobranca(long idFormaCobranca) {
		StringBuilder hql = new StringBuilder();
		hql.append(" update FormaCobranca f ");		
		hql.append(" set f.ativa = :ativa ");
		hql.append(" where f.id  = :idFormaCobranca ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("ativa", false);
        query.setParameter("idFormaCobranca", idFormaCobranca);
		query.executeUpdate();
	}

	/**
	 * Obtem lista de Formas de Cobrança por Cota e Tipo de Cobrança
	 * @param idCota
	 * @param tipoCobranca
	 * @return List<formaCobranca>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobranca> obterPorCotaETipoCobranca(Long idCota,TipoCobranca tipoCobranca, Long idFormaCobranca) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from FormaCobranca f");		
		hql.append(" where f.parametroCobrancaCota.cota.id = :pIdCota ");
		hql.append(" and f.tipoCobranca = :pTipoCobranca ");
		hql.append(" and f.ativa = :pAtiva ");
		if (idFormaCobranca!=null){
		    hql.append(" and f.id <> :pIdFormaCobranca ");
		}
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pIdCota", idCota);
        query.setParameter("pTipoCobranca", tipoCobranca);
        query.setParameter("pAtiva", true);
        if (idFormaCobranca!=null){
        	query.setParameter("pIdFormaCobranca", idFormaCobranca);
        }
        return query.list();
        
	}
	
	/**
	 * Obtem lista de Formas de Cobrança por Tipo de Cobrança
	 * @param tipoCobranca
	 * @return List<formaCobranca>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobranca> obterPorDistribuidorETipoCobranca(Long idDistribuidor, TipoCobranca tipoCobranca, Long idFormaCobranca) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select p.formaCobranca from PoliticaCobranca p");		
		hql.append(" where p.formaCobranca.tipoCobranca = :pTipoCobranca ");
		hql.append(" and p.formaCobranca.ativa = :pAtiva ");
		hql.append(" and p.distribuidor.id = :pIdDistribuidor ");
		if (idFormaCobranca!=null){
		    hql.append(" and p.formaCobranca.id <> :pIdFormaCobranca ");
		}
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pTipoCobranca", tipoCobranca);
        query.setParameter("pAtiva", true);
        query.setParameter("pIdDistribuidor", idDistribuidor);
        if (idFormaCobranca!=null){
        	query.setParameter("pIdFormaCobranca", idFormaCobranca);
        }
        return query.list();
        
	}

}
