package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class FornecedorRepositoryImpl extends
		AbstractRepositoryModel<Fornecedor, Long> implements FornecedorRepository {

	/**
	 * Construtor padrão.
	 */
	public FornecedorRepositoryImpl() {
		super(Fornecedor.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresNaoReferenciadosComCota(Long idCota){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select fornecedor from Fornecedor fornecedor  ")
			.append(" where fornecedor.id not in ( ")
			
						.append(" select fornecedorF.id from Cota cota JOIN cota.fornecedores fornecedorF ")
						.append(" where cota.id = :idCota ) ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota",idCota);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresCota(Long idCota){
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" select fornecedor from Cota cota JOIN cota.fornecedores fornecedor ")
		.append(" where cota.id = :idCota ")
		.append(" and fornecedor.situacaoCadastro = :situacaoCadastro");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota",idCota);
		query.setParameter("situacaoCadastro",SituacaoCadastro.ATIVO);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresAtivos() {

		String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ " where fornecedor.situacaoCadastro = :situacaoCadastro";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("situacaoCadastro", SituacaoCadastro.ATIVO);

		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores() {

		String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica ";

		Query query = super.getSession().createQuery(hql);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores(String cnpj) {
		String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ "where fornecedor.juridica.cnpj like :cnpj ";

		Query query = getSession().createQuery(hql);
		query.setParameter("cnpj", cnpj+"%");

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores(boolean permiteBalanceamento,
			SituacaoCadastro... situacoes) {
		StringBuilder hql = new StringBuilder("from Fornecedor fornecedor ");
		hql.append("join fetch fornecedor.juridica juridica ");
		hql.append("where fornecedor.permiteBalanceamento = :permiteBalanceamento ");
		hql.append("and fornecedor.situacaoCadastro in (:situacoes) ");
		hql.append("order by juridica.nomeFantasia ");

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("permiteBalanceamento", permiteBalanceamento);
		query.setParameterList("situacoes", situacoes);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresDeProduto(String codigoProduto,
													   GrupoFornecedor grupoFornecedor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select fornecedores from Produto p ");
		hql.append(" join p.fornecedores fornecedores ");
		
		hql.append(" where fornecedores.situacaoCadastro = :situacaoCadastro ");
		
		if (codigoProduto != null && codigoProduto.length() > 0) {
			hql.append(" and p.codigo = :codigoProduto ");
		}
		
		if (grupoFornecedor != null) {
			hql.append(" and fornecedores.tipoFornecedor.grupoFornecedor = :grupoFornecedor ");
		}
		
		hql.append(" group by fornecedores ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("situacaoCadastro", SituacaoCadastro.ATIVO);
		
		if (grupoFornecedor != null) {
			query.setParameter("grupoFornecedor", grupoFornecedor);
		}		
		
		if (codigoProduto != null && codigoProduto.length() > 0) {
			query.setParameter("codigoProduto", codigoProduto);
		}
		
		return query.list();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<FornecedorDTO> obterFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		String hql = obterHQLConsultaFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		if (filtroConsultaFornecedor.getColunaOrdenacao() != null) {
		
			hql += " order by ";
			hql += filtroConsultaFornecedor.getColunaOrdenacao().getOrdenacao();
			hql += filtroConsultaFornecedor.getPaginacao() != null
			   		&& Ordenacao.DESC == filtroConsultaFornecedor.getPaginacao().getOrdenacao() ? 
			   				" desc " : " asc ";
		}

		Query query = obterQueryParametrizada(filtroConsultaFornecedor, hql);

	    if (filtroConsultaFornecedor.getPaginacao() != null 
				&& filtroConsultaFornecedor.getPaginacao().getPosicaoInicial() != null) {
		
			query.setFirstResult(filtroConsultaFornecedor.getPaginacao().getPosicaoInicial());
			
			query.setMaxResults(filtroConsultaFornecedor.getPaginacao().getQtdResultadosPorPagina());
	    }
		
		query.setResultTransformer(new AliasToBeanResultTransformer(FornecedorDTO.class));

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long obterContagemFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		String hql = obterHQLConsultaFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		hql = " select count(fornecedor) " 
			+ hql.substring(hql.indexOf(" from "));
		
		Query query = obterQueryParametrizada(filtroConsultaFornecedor, hql);
		
		return (Long) query.uniqueResult();
	}
	
	private String obterHQLConsultaFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ")
		   .append(" fornecedor.id as idFornecedor, ")
		   .append(" fornecedor.codigoInterface as codigoInterface, ")
		   .append(" fornecedor.origem as origem, ")
		   .append(" fornecedor.juridica.razaoSocial as razaoSocial, ")
		   .append(" fornecedor.juridica.cnpj as cnpj, ")
		   .append(" fornecedor.responsavel as responsavel, ")
		   .append(" '(' || telefone.ddd || ') ' || telefone.numero as telefone, ")
		   .append(" fornecedor.juridica.email as email, ")
		   .append(" fornecedor.emailNfe as emailNfe ")
		   .append(" from Fornecedor fornecedor ")
		   .append(" left join fornecedor.telefones telefoneFornecedor ")
		   .append(" left join telefoneFornecedor.telefone telefone ")
		   .append(" where telefoneFornecedor is null or telefoneFornecedor.principal = true ");
		
		if (filtroConsultaFornecedor.getCnpj() != null 
				&& !filtroConsultaFornecedor.getCnpj().isEmpty()) {
			
			hql.append(" and fornecedor.juridica.cnpj like :cnpj ");
		}
		
		if (filtroConsultaFornecedor.getNomeFantasia() != null 
				&& !filtroConsultaFornecedor.getNomeFantasia().isEmpty()) {
			
			hql.append(" and fornecedor.juridica.nomeFantasia like :nomeFantasia ");
		}

		if (filtroConsultaFornecedor.getRazaoSocial() != null 
				&& !filtroConsultaFornecedor.getRazaoSocial().isEmpty()) {
			
			hql.append(" and fornecedor.juridica.razaoSocial like :razaoSocial ");
		}
		
		return hql.toString();
	}
	
	/*
	 * Método que retorna o objeto Query com suas devidas parametrizações.
	 */
	private Query obterQueryParametrizada(FiltroConsultaFornecedorDTO filtroConsultaFornecedor, String hql) {
		
		Query query = getSession().createQuery(hql);
		
		if (filtroConsultaFornecedor.getCnpj() != null 
				&& !filtroConsultaFornecedor.getCnpj().isEmpty()) {
			
			query.setParameter("cnpj", "%" + filtroConsultaFornecedor.getCnpj() + "%");
		}
		
		if (filtroConsultaFornecedor.getNomeFantasia() != null 
				&& !filtroConsultaFornecedor.getNomeFantasia().isEmpty()) {
			
			query.setParameter("nomeFantasia", "%" + filtroConsultaFornecedor.getNomeFantasia() + "%");
		}

		if (filtroConsultaFornecedor.getRazaoSocial() != null 
				&& !filtroConsultaFornecedor.getRazaoSocial().isEmpty()) {
			
			query.setParameter("razaoSocial", "%" + filtroConsultaFornecedor.getRazaoSocial() + "%");
		}

		return query;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ItemDTO<Long, String>> obterFornecedoresIdNome(SituacaoCadastro situacao, Boolean inferface){
		Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.createAlias("juridica","juridica");
		if(situacao!=null){
			criteria.add(Restrictions.eq("situacaoCadastro", situacao));		
		}
		
		if(inferface!=null){
			if (inferface) {
				criteria.add(Restrictions.isNotNull("codigoInterface"));
			}else{
				criteria.add(Restrictions.isNull("codigoInterface"));
			}
		}
		
		criteria.setProjection(Projections.projectionList().add(Projections.id(), "key").add(Projections.property("juridica.razaoSocial"), "value"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		return  criteria.list();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer obterQuantidadeFornecedoresPorIdPessoa(Long idPessoa, Long idFornecedor) {

		Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.add(Restrictions.eq("juridica.id", idPessoa));
		
		if (idFornecedor != null) {
		
			criteria.add(Restrictions.ne("id", idFornecedor));
		}
		
		criteria.setProjection(Projections.rowCount());

		return ((Long) criteria.list().get(0)).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresPorIdPessoa(Long idPessoa) {

		Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.add(Restrictions.eq("juridica.id", idPessoa));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedorLikeNomeFantasia(String nomeFantasia) {
		
		try {
			
			Criteria criteria = super.getSession().createCriteria(Fornecedor.class);

			criteria.createAlias("juridica","juridica");
			criteria.setFetchMode("juridica", FetchMode.JOIN);
			criteria.add(Restrictions.ilike("juridica.nomeFantasia", nomeFantasia, MatchMode.ANYWHERE));
			
			return criteria.list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresPorId(List<Long> idsFornecedores) {
		
		String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ " where fornecedor.id in (:idsFornecedores) ";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameterList("idsFornecedores", idsFornecedores);
		
		return query.list();
	}

	/**
	 * Obtem Fornecedor por codigo
	 * @param codigo
	 * @return Fornecedor
	 */
	@Override
	public Fornecedor obterFornecedorPorCodigo(Integer codigo) {
        Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.add(Restrictions.eq("codigoInterface", codigo));
		
		criteria.setMaxResults(1);

		return (Fornecedor) criteria.uniqueResult();
	}
	
	@Override
	public Integer obterMinCodigoInterfaceDisponivel() {
		
		StringBuilder hql = new StringBuilder();
		
				hql.append(" select min(codInterface) from (											");
				hql.append(" select min(COD_INTERFACE + 1) as codInterface from FORNECEDOR           	");
				hql.append(" where (COD_INTERFACE + 1) not in (select COD_INTERFACE from FORNECEDOR) 	"); 
				hql.append(" UNION															  			");
				hql.append(" SELECT 1 AS codInteface from dual WHERE  1 not in				  			");
				hql.append(" ( select COD_INTERFACE from FORNECEDOR where COD_INTERFACE = 1 ) 			"); 
				hql.append(" ) as TBL_COD_INTEFACE 														");
		
		Query query = super.getSession().createSQLQuery(hql.toString());
		
		BigInteger codInterface = (BigInteger) query.uniqueResult();
		
		return codInterface.intValue();
		
	}
	
	@Override
	public Integer obterMaxCodigoInterface(){
		Criteria criteria = getSession().createCriteria(Fornecedor.class);		
		
		criteria.setProjection(Projections.max("codigoInterface"));
		return (Integer) criteria.uniqueResult();
	}
	
	
	@Override
	public EnderecoFornecedor obterEnderecoPrincipal(long idFornecedor) {
		Criteria criteria = getSession().createCriteria(EnderecoFornecedor.class);
		criteria.add(Restrictions.eq("fornecedor.id", idFornecedor));

		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);

		return (EnderecoFornecedor) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresPorDesconto(Long idDesconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select f ");
		hql.append("from Fornecedor f, HistoricoDescontoFornecedor hdf ");
		hql.append("where f.desconto.id = hdf.desconto.id ");
		hql.append("and f.id = hdf.fornecedor.id ");
		hql.append("and f.desconto.id = :idDesconto ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", idDesconto);
		
		return q.list();

	}
	
}
