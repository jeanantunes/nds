package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.FormaCobrancaDefaultVO;
import br.com.abril.nds.dto.FormaCobrancaFornecedorDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
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
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @return FormaCobranca
	 */
	@Override
	public FormaCobranca obterFormaCobranca(Long idCota, Long idFornecedor, Integer diaDoMes, Integer diaDaSemana) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select f from Cota c ");		
		hql.append(" join c.parametroCobranca pcc ");
		hql.append(" join pcc.formasCobrancaCota f ");
		hql.append(" left join f.fornecedores fnc ");
		hql.append(" left join f.concentracaoCobrancaCota ccc ");
		hql.append(" where f.ativa = :indAtiva ");
		hql.append(" and c.id = :idCota ");
		
		if (idFornecedor != null) {
		
		    hql.append(" and fnc.id = :idFornecedor ");
		}
		
		hql.append(" and ( f.tipoFormaCobranca = :tipoFormaCobranca ");
			
	    hql.append("       or ( ( :diaMes IN ELEMENTS(f.diasDoMes) ) ");
				
		hql.append("             or ( ccc.codigoDiaSemana = :diaSemana ) ) )");
		

		Query query = super.getSession().createQuery(hql.toString());
		
        
        query.setParameter("indAtiva", true);
        
        query.setParameter("idCota", idCota);
        
        if (idFornecedor != null) {
        	
            query.setParameter("idFornecedor", idFornecedor);
        }
        
        query.setParameter("diaMes", diaDoMes);
        
        query.setParameter("diaSemana", diaDaSemana);
        
        query.setParameter("tipoFormaCobranca", TipoFormaCobranca.DIARIA);
        
        
        query.setMaxResults(1);
        
        
        return (FormaCobranca) query.uniqueResult();
	}

	/**
	 * Obtem FormaCobranca do Distribuidor
	 * @param idFornecedor
	 * @param data
	 * @param principal
	 * @return FormaCobranca
	 */
	@Override
	public FormaCobranca obterFormaCobranca(Long idFornecedor, Integer diaDoMes, Integer diaDaSemana, boolean principal) {
		
		
		StringBuilder hql = new StringBuilder();
		
		
		hql.append(" select f from PoliticaCobranca p ");		
		
		hql.append(" join p.formaCobranca f ");		
		
		hql.append(" left join f.fornecedores fnc ");
		
		hql.append(" left join f.concentracaoCobrancaCota ccc ");
		
		hql.append(" where p.ativo = :indAtivo ");

		if (idFornecedor!=null){
		    
			hql.append(" and fnc.id = :idFornecedor ");
		}
		
        hql.append(" and ( f.tipoFormaCobranca = :tipoFormaCobranca ");
		
        hql.append("       or ( :diaMes IN ELEMENTS(f.diasDoMes) ");
			
		hql.append("             or ccc.codigoDiaSemana = :diaSemana ) )");
		
		hql.append(" and p.principal = :principal ");
		

		Query query = super.getSession().createQuery(hql.toString());
		
        
        query.setParameter("indAtivo", true);
        
        if (idFornecedor!=null){
        	
            query.setParameter("idFornecedor", idFornecedor);
        }
        
        query.setParameter("diaMes", diaDoMes);
        
        query.setParameter("diaSemana", diaDaSemana);
        
        query.setParameter("tipoFormaCobranca", TipoFormaCobranca.DIARIA);
        
        query.setParameter("principal", principal);
        
        
        query.setMaxResults(1);
        
        
		return (FormaCobranca) query.uniqueResult();
	}

	/**
	 * Obtem FormaCobranca principal da Cota
	 * @param parametroCobrancaCota
	 * @return FormaCobranca
	 */
	@Override
	public FormaCobranca obterFormaCobranca(ParametroCobrancaCota parametroCobrancaCota) {
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" select f from FormaCobranca f ");		
		hql.append(" join f.parametroCobrancaCota pcc ");
		hql.append(" where f.ativa = :indAtiva ");
		hql.append(" and f.principal = :principal ");
		hql.append(" and pcc = :parametroCobrancaCota ");

		Query query = super.getSession().createQuery(hql.toString());
        
        query.setParameter("indAtiva", true);
        query.setParameter("principal", true);
        query.setParameter("parametroCobrancaCota", parametroCobrancaCota);
        
        //FIXME: Tirar esse lixo do código
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
	
	@SuppressWarnings("unchecked")
	public List<FormaCobrancaDefaultVO> obterFormaCobrancaDefault() {

		StringBuilder hql = new StringBuilder();

        hql.append(" select banco.id as idBanco, banco.nome as nomeBanco, f.tipoCobranca as tipoCobranca ");
        hql.append(" from FormaCobranca f ");	
		hql.append(" join f.politicaCobranca p ");
		hql.append(" left join f.banco banco ");		
		hql.append(" where p.ativo = :indtrue ");
		
		Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("indtrue", true);

        query.setResultTransformer(Transformers.aliasToBean(FormaCobrancaDefaultVO.class));        

        return query.list();
	}
	
	/**
     * Obtem FormaCobranca principal do Distribuidor com dados de fornecedor e concentração
     * @return FormaCobranca
     */
    @Override
    public FormaCobranca obterFormaCobrancaCompleto() {
        
        
        StringBuilder hql = new StringBuilder();
        
        
        hql.append(" select f from FormaCobranca f ");      
        
        hql.append(" join f.politicaCobranca p ");    
        
        hql.append(" left join fetch f.concentracaoCobrancaCota ccc ");    
        
        hql.append(" join fetch f.fornecedores forn ");    
        
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
	public List<FormaCobranca> obterFormasCobrancaCota(ParametroCobrancaCota parametroCobrancaCota) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select f from FormaCobranca f ");		
		hql.append(" where f.parametroCobrancaCota = :parametroCobrancaCota ");
		hql.append(" and f.ativa = true ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("parametroCobrancaCota", parametroCobrancaCota);
        return query.list();
	}
	
	/**
	 * Obtém lista de forma de cobranca da Cota
	 * @param Cota
	 * @return {@link List<FormaCobranca>}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobrancaDefaultVO> obterFormaCobrancaCotaDefault(Integer numeroCota) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select distinct banco.id as idBanco, banco.nome as nomeBanco, formaCobranca.tipoCobranca as tipoCobranca ");
		sql.append(" from Cota cota ");
		sql.append(" inner join cota.parametroCobranca parametroCobrancaCota ");
		sql.append(" inner join parametroCobrancaCota.formasCobrancaCota formaCobranca ");
		//certas formas de cobrança não tem banco, ex.: dinheiro
		sql.append(" left join formaCobranca.banco banco ");
		sql.append(" where cota.numeroCota = :numeroCota ");
		sql.append(" and formaCobranca.ativa = true ");

		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("numeroCota", numeroCota);
		
		query.setResultTransformer(Transformers.aliasToBean(FormaCobrancaDefaultVO.class));
		
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
		hql.append(" where f.parametroCobrancaCota = :parametroCobrancaCota ");
		hql.append(" and f.ativa = true ");
		
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("parametroCobrancaCota", cota.getParametroCobranca());
        
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
	 * Obtem lista de Formas de Cobrança por Cota
	 * @param parametroCobrancaCota
	 * @param idFormaCobranca
	 * @return List<formaCobranca>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobranca> obterPorCota(ParametroCobrancaCota parametroCobrancaCota, Long idFormaCobranca) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select f from FormaCobranca f");		
		hql.append(" where f.parametroCobrancaCota = :parametroCobrancaCota ");
		hql.append(" and f.ativa = :pAtiva ");
		
		if (idFormaCobranca != null) {
		    hql.append(" and f.id <> :pIdFormaCobranca ");
		}
		
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("parametroCobrancaCota", parametroCobrancaCota);
        query.setParameter("pAtiva", true);
        
        if (idFormaCobranca != null) {
        	query.setParameter("pIdFormaCobranca", idFormaCobranca);
        }
        
        return query.list();
	}
	
	/**
	 * Obtem lista de Formas de Cobrança
	 * @param idDistribuidor
	 * @param idFormaCobranca
	 * @return List<formaCobranca>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobranca> obterPorDistribuidor(Long idDistribuidor, Long idFormaCobranca) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select p.formaCobranca from PoliticaCobranca p");		
		hql.append(" where p.ativo = :pAtivo ");
		hql.append(" and p.distribuidor.id = :pIdDistribuidor ");
		
		if (idFormaCobranca!=null){
		    hql.append(" and p.formaCobranca.id <> :pIdFormaCobranca ");
		}
		
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("pAtivo", true);
        query.setParameter("pIdDistribuidor", idDistribuidor);

        if (idFormaCobranca!=null){
        	query.setParameter("pIdFormaCobranca", idFormaCobranca);
        }
        
        return query.list();
	}

	@Override
	public boolean obterFormasCobrancaAtivaCotas(Boolean ativa, Long idFormaCobranca) {
		
		Query query = super.getSession().createQuery(
				"select case when count(f.id) > 0 then true else false end " +
				"from FormaCobranca f " +
				"join f.parametroCobrancaCota pc " +
				"where f.ativa = :ativa " +
				"and f.id = :idFormaCobranca ");
        
		query.setParameter("ativa", ativa);
		query.setParameter("idFormaCobranca", idFormaCobranca);

		return (boolean) query.uniqueResult();
	}

	@Override
	public void removerFormasCobrancaCota(Integer numeroCota) {
		
		Query query = 
			this.getSession().createQuery(
				"delete from ParametroCobrancaCota c join c.cota cota where cota.numeroCota = :numeroCota");
		
		query.setParameter("numeroCota", numeroCota);
		
		query.executeUpdate();
	}
	
	/**
	 * Obtem lista de FormaCobranca ativa da cota ou do distribuidor
	 * Onde a concentração de pagamento é compatível com a data de operação atual
	 * 
	 * @param idFornecedor
	 * @param diaDoMes
	 * @param diaDaSemana
	 * @return List<FormaCobranca>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobranca> obterFormasCobrancaPorFornecedor(Long idFornecedor, Integer diaDoMes, Integer diaDaSemana) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select f from FormaCobranca f ");		
		
		hql.append(" left join f.fornecedores fnc ");
		
		hql.append(" left join f.concentracaoCobrancaCota ccc ");
		
		hql.append(" where f.ativa = :indAtiva ");
		
		hql.append(" and fnc.id = :idFornecedor ");

	    hql.append(" and ( f.tipoFormaCobranca = :tipoFormaCobranca ");
			
	    hql.append("       or ( :diaMes IN ELEMENTS(f.diasDoMes) ");
				
		hql.append("             or ccc.codigoDiaSemana = :diaSemana ) )");
			
		Query query = super.getSession().createQuery(hql.toString());
        
        query.setParameter("indAtiva", true);
        	
        query.setParameter("idFornecedor", idFornecedor);
        
        query.setParameter("diaMes", diaDoMes);
        
        query.setParameter("diaSemana", diaDaSemana);
        
        query.setParameter("tipoFormaCobranca", TipoFormaCobranca.DIARIA);
        
        return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobrancaFornecedorDTO> obterFormasCobrancaDistribuidorFornecedor() {
		
		StringBuilder sql = new StringBuilder().append("")                                       
		.append("select null as numeroCota, c.id as idCota, f.id as idFornecedor                              ")
		.append("from forma_cobranca fc                                                          ")
		.append("left join parametro_cobranca_cota pcc on pcc.id = fc.PARAMETRO_COBRANCA_COTA_ID ")
		.append("left join politica_cobranca pc on fc.id=pc.FORMA_COBRANCA_ID                    ")
		.append("inner join forma_cobranca_fornecedor fcf on fcf.FORMA_COBRANCA_ID = fc.id       ")
		.append("inner join fornecedor f on f.id = fcf.FORNECEDOR_ID                             ")
		.append("left outer join cota c on c.parametro_cobranca_id = pcc.ID                      ")
		.append("where 1=1                                                                       ")
		.append("and fc.ativa = :ativa                                                           ");
        
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());
		query.setParameter("ativa", true);
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("idFornecedor", StandardBasicTypes.LONG);
				
		query.setResultTransformer(Transformers.aliasToBean(FormaCobrancaFornecedorDTO.class));

		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormaCobrancaFornecedorDTO> obterFormasCobrancaCotaFornecedor() {
		
		StringBuilder sql = new StringBuilder().append("")                                       
		.append("select c.numero_cota as numeroCota, c.id as idCota, f.id as idFornecedor                              ")
		.append("from forma_cobranca fc                                                          ")
		.append("left join parametro_cobranca_cota pcc on pcc.id = fc.PARAMETRO_COBRANCA_COTA_ID ")
		.append("left join politica_cobranca pc on fc.id=pc.FORMA_COBRANCA_ID                    ")
		.append("left join cota c on c.parametro_cobranca_id = pcc.id                                          ")
		.append("inner join forma_cobranca_fornecedor fcf on fcf.FORMA_COBRANCA_ID = fc.id       ")
		.append("inner join fornecedor f on f.id = fcf.FORNECEDOR_ID                             ")
		.append("where 1=1                                                                       ")
		.append("and fc.ativa = :ativa                                                           ");
        
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());

		query.setParameter("ativa", true);
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.LONG);
		query.addScalar("idFornecedor", StandardBasicTypes.LONG);
				
		query.setResultTransformer(Transformers.aliasToBean(FormaCobrancaFornecedorDTO.class));

		return query.list();
	}
	
}