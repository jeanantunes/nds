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
public class FormaCobrancaRepositoryImpl extends AbstractRepository<FormaCobranca,Long> implements FormaCobrancaRepository  {

	
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
	public FormaCobranca obterFormaCobrancaPrincipalCota(Cota cota) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from FormaCobranca f ");		
		hql.append(" where f.principal = :pPrincipal ");
		hql.append(" and f.parametroCobrancaCota.cota = :pCota ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pPrincipal", true);
        query.setParameter("pCota", cota);
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
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pCota", cota);
        return query.list().size();
	}

}
