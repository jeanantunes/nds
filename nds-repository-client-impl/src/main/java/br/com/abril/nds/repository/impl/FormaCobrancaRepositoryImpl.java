package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
	 * Obtem FormaCobranca da Cota
	 * @param numeroCota
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	public FormaCobranca obterFormaCobranca(Integer numeroCota, List<Long> fornecedoresId, Integer diaDoMes, Integer diaDaSemana, BigDecimal valor) {
		
		
		StringBuilder hql = new StringBuilder();
		
		
		hql.append(" select f from FormaCobranca f ");		

		hql.append(" join f.parametroCobrancaCota pcc ");
		
		hql.append(" left join f.fornecedores fnc ");
		
		hql.append(" left join f.concentracaoCobrancaCota ccc ");
		
		hql.append(" where f.ativa = :indAtiva ");
		
		hql.append(" and pcc.cota.numeroCota = :numeroCota ");
		
		if (fornecedoresId!=null){
		
		    hql.append(" and fnc.id IN (:fornecedoresId) ");
		}
		
		hql.append(" and pcc.valorMininoCobranca <= :valor ");
		
		hql.append(" and ( f.tipoFormaCobranca = :tipoFormaCobranca ");
			
	    hql.append("       or ( ( :diaMes IN ELEMENTS(f.diasDoMes) ) ");
				
		hql.append("             or ( ccc.codigoDiaSemana = :diaSemana ) ) )");
		

		Query query = super.getSession().createQuery(hql.toString());
		
        
        query.setParameter("indAtiva", true);
        
        query.setParameter("numeroCota", numeroCota);
        
        if (fornecedoresId!=null){
        	
            query.setParameterList("fornecedoresId", fornecedoresId);
        }
        
        query.setParameter("valor", valor);
        
        query.setParameter("diaMes", diaDoMes);
        
        query.setParameter("diaSemana", diaDaSemana);
        
        query.setParameter("tipoFormaCobranca", TipoFormaCobranca.DIARIA);
        
        
        query.setMaxResults(1);
        
        
        return (FormaCobranca) query.uniqueResult();
	}

	/**
	 * Obtem FormaCobranca do Distribuidor
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	public FormaCobranca obterFormaCobranca(List<Long> fornecedoresId, Integer diaDoMes, Integer diaDaSemana, BigDecimal valor) {
		
		
		StringBuilder hql = new StringBuilder();
		
		
		hql.append(" select f from PoliticaCobranca p ");		
		
		hql.append(" join p.formaCobranca f ");		
		
		hql.append(" left join f.fornecedores fnc ");
		
		hql.append(" left join f.concentracaoCobrancaCota ccc ");
		
		hql.append(" where p.ativo = :indAtivo ");

		if (fornecedoresId!=null){
		    
			hql.append(" and fnc.id IN (:fornecedoresId) ");
		}
		
        hql.append(" and f.valorMinimoEmissao <= :valor ");
		
        hql.append(" and ( f.tipoFormaCobranca = :tipoFormaCobranca ");
		
        hql.append("       or ( :diaMes IN ELEMENTS(f.diasDoMes) ");
			
		hql.append("             or ccc.codigoDiaSemana = :diaSemana ) )");
		

		Query query = super.getSession().createQuery(hql.toString());
		
        
        query.setParameter("indAtivo", true);
        
        if (fornecedoresId!=null){
        	
            query.setParameterList("fornecedoresId", fornecedoresId);
        }
        
        query.setParameter("valor", valor);
        
        query.setParameter("diaMes", diaDoMes);
        
        query.setParameter("diaSemana", diaDaSemana);
        
        query.setParameter("tipoFormaCobranca", TipoFormaCobranca.DIARIA);
        
        
        query.setMaxResults(1);
        
        
		return (FormaCobranca) query.uniqueResult();
	}

	/**
	 * Obtem FormaCobranca principal da Cota
	 * @param idCota
	 * @return FormaCobranca
	 */
	@Override
	public FormaCobranca obterFormaCobranca(Long idCota) {
		
		
        StringBuilder hql = new StringBuilder();
		
		
		hql.append(" select f from FormaCobranca f ");		

		hql.append(" join f.parametroCobrancaCota pcc ");
		
		hql.append(" where f.ativa = :indAtiva ");
		
		hql.append(" and f.principal = :principal ");
		
		hql.append(" and pcc.cota.id = :idCota ");


		Query query = super.getSession().createQuery(hql.toString());
		
        
        query.setParameter("indAtiva", true);
        
        query.setParameter("principal", true);
        
        query.setParameter("idCota", idCota);
             
        
        query.setMaxResults(1);
        
        
        return (FormaCobranca) query.uniqueResult();
	}

	/**
	 * Obtem FormaCobranca principal do Distribuidor
	 * @return FormaCobranca
	 */
	@Override
	public FormaCobranca obterFormaCobranca() {
		
		
        StringBuilder hql = new StringBuilder();
		
		
        hql.append(" select f from FormaCobranca f ");		
		
		hql.append(" join f.politicaCobranca p ");		
		
		hql.append(" where p.ativo = :indAtivo ");
		
		hql.append(" and p.principal = :principal ");

		Query query = super.getSession().createQuery(hql.toString());
		
        
        query.setParameter("indAtivo", true);
        
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
	public List<FormaCobranca> obterPorDistribuidorETipoCobranca(TipoCobranca tipoCobranca, Long idFormaCobranca) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select p.formaCobranca from PoliticaCobranca p");		
		hql.append(" where p.formaCobranca.tipoCobranca = :pTipoCobranca ");
		hql.append(" and p.formaCobranca.ativa = :pAtiva ");
		hql.append(" and p.distribuidor.id is not null ");
		if (idFormaCobranca!=null){
		    hql.append(" and p.formaCobranca.id <> :pIdFormaCobranca ");
		}
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pTipoCobranca", tipoCobranca);
        query.setParameter("pAtiva", true);
        
        if (idFormaCobranca!=null){
        	query.setParameter("pIdFormaCobranca", idFormaCobranca);
        }
        return query.list();
        
	}

}
