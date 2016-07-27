package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Arrays;
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
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterIdFornecedores() {
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select f.id from Fornecedor f ");
		
		final Query query = getSession().createQuery(hql.toString());
		
		return ((List<Long>) query.list());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresNaoReferenciadosComCota(final Long idCota){
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select fornecedor from Fornecedor fornecedor  ")
			.append(" where fornecedor.situacaoCadastro ='ATIVO' AND  fornecedor.id not in ( ")
			
						.append(" select fornecedorF.id from Cota cota JOIN cota.fornecedores fornecedorF ")
						.append(" where cota.id = :idCota ) ");
		
		final Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota",idCota);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresCota(final Long idCota){
		
		final StringBuilder hql = new StringBuilder();
	
		hql.append(" select fornecedor from Cota cota JOIN cota.fornecedores fornecedor ")
		.append(" where cota.id = :idCota ")
		.append(" and fornecedor.situacaoCadastro = :situacaoCadastro");
		
		final Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota",idCota);
		query.setParameter("situacaoCadastro",SituacaoCadastro.ATIVO);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresAtivos() {

		final String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ " where fornecedor.situacaoCadastro = :situacaoCadastro";

		final Query query = super.getSession().createQuery(hql);

		query.setParameter("situacaoCadastro", SituacaoCadastro.ATIVO);

		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresPorSituacaoEOrigem(final SituacaoCadastro situacaoCadastro, 
																final Origem origem) {

		String hql = "select fornecedor from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica juridica "
				+ " where fornecedor.situacaoCadastro = :situacaoCadastro "
				+ " and fornecedor.fornecedorUnificador is null ";
	
		if(origem != null)
			hql += " and fornecedor.origem = :origem ";

		final Query query = super.getSession().createQuery(hql);

		query.setParameter("situacaoCadastro", situacaoCadastro);
		
		if(origem != null)
			query.setParameter("origem", origem);

		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores() {

		final String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica ";

		final Query query = super.getSession().createQuery(hql);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores(final String cnpj) {
		final String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ "where fornecedor.juridica.cnpj like :cnpj ";

		final Query query = getSession().createQuery(hql);
		query.setParameter("cnpj", cnpj+"%");

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores(final SituacaoCadastro... situacoes) {
		final StringBuilder hql = new StringBuilder("from Fornecedor fornecedor ");
		hql.append("join fetch fornecedor.juridica juridica ");
		hql.append("where fornecedor.situacaoCadastro in (:situacoes) ");
		hql.append("order by juridica.nomeFantasia ");

		final Query query = getSession().createQuery(hql.toString());
		query.setParameterList("situacoes", situacoes);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresDeProduto(final String codigoProduto,
													   final GrupoFornecedor grupoFornecedor) {
		
		final StringBuilder hql = new StringBuilder();
		
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
		
		final Query query = getSession().createQuery(hql.toString());
		
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
	public List<FornecedorDTO> obterFornecedoresPorFiltro(final FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		String hql = obterHQLConsultaFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		if (filtroConsultaFornecedor.getColunaOrdenacao() != null) {
		
			hql += " order by ";
			hql += filtroConsultaFornecedor.getColunaOrdenacao().getOrdenacao();
			hql += filtroConsultaFornecedor.getPaginacao() != null
			   		&& Ordenacao.DESC == filtroConsultaFornecedor.getPaginacao().getOrdenacao() ? 
			   				" desc " : " asc ";
		}

		final Query query = obterQueryParametrizada(filtroConsultaFornecedor, hql);

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
	public Long obterContagemFornecedoresPorFiltro(final FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		String hql = obterHQLConsultaFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		hql = " select count(fornecedor) " 
			+ hql.substring(hql.indexOf(" from "));
		
		final Query query = obterQueryParametrizada(filtroConsultaFornecedor, hql);
		
		return (Long) query.uniqueResult();
	}
	
	private String obterHQLConsultaFornecedoresPorFiltro(final FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select ")
		   .append(" fornecedor.id as idFornecedor, ")
		   .append(" fornecedor.codigoInterface as codigoInterface, ")
		   .append(" fornecedor.origem as origem, ")
		   .append(" fornecedor.juridica.razaoSocial as razaoSocial, ")
		   .append(" fornecedor.juridica.cnpj as cnpj, ")
		   .append(" fornecedor.responsavel as responsavel, ")
		   .append(" '(' || telefone.ddd || ') ' || telefone.numero as telefone, ")
		   .append(" fornecedor.juridica.email as email, ")
		   .append(" fornecedor.emailNfe as emailNfe, ")
		   .append(" fornecedor.integraGFS as integraGFS ")
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
			
			hql.append(" and lower( fornecedor.juridica.nomeFantasia ) like :nomeFantasia ");
		}

		if (filtroConsultaFornecedor.getRazaoSocial() != null 
				&& !filtroConsultaFornecedor.getRazaoSocial().isEmpty()) {
			
			hql.append(" and lower(fornecedor.juridica.razaoSocial ) like :razaoSocial ");
		}
		
		return hql.toString();
	}
	
	/*
	 * Método que retorna o objeto Query com suas devidas parametrizações.
	 */
	private Query obterQueryParametrizada(final FiltroConsultaFornecedorDTO filtroConsultaFornecedor, final String hql) {
		
		final Query query = getSession().createQuery(hql);
		
		if (filtroConsultaFornecedor.getCnpj() != null 
				&& !filtroConsultaFornecedor.getCnpj().isEmpty()) {
			
			query.setParameter("cnpj", "%" + filtroConsultaFornecedor.getCnpj().replaceAll("[./-]", "") + "%");
		}
		
		if (filtroConsultaFornecedor.getNomeFantasia() != null 
				&& !filtroConsultaFornecedor.getNomeFantasia().isEmpty()) {
			
			query.setParameter("nomeFantasia", "%" + filtroConsultaFornecedor.getNomeFantasia().toLowerCase().trim() + "%");
		}

		if (filtroConsultaFornecedor.getRazaoSocial() != null 
				&& !filtroConsultaFornecedor.getRazaoSocial().isEmpty()) {
			
			query.setParameter("razaoSocial", "%" + filtroConsultaFornecedor.getRazaoSocial().toLowerCase().trim() + "%");
		}

		return query;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ItemDTO<Long, String>> obterFornecedoresIdNome(final SituacaoCadastro situacao, final Boolean inferface){
		
		final Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
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
	
	@Override
	@SuppressWarnings("unchecked")
	public ItemDTO<Long, String> obterNome(final Long id){
		final Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.createAlias("juridica","juridica");
		
		criteria.add(Restrictions.idEq(id));
		
		
		criteria.setProjection(Projections.projectionList().add(Projections.id(), "key").add(Projections.property("juridica.razaoSocial"), "value"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		return  (ItemDTO<Long, String>) criteria.uniqueResult();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer obterQuantidadeFornecedoresPorIdPessoa(final Long idPessoa, final Long idFornecedor) {

		final Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.add(Restrictions.eq("juridica.id", idPessoa));
		
		if (idFornecedor != null) {
		
			criteria.add(Restrictions.ne("id", idFornecedor));
		}
		
		criteria.setProjection(Projections.rowCount());

		return ((Long) criteria.list().get(0)).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresPorIdPessoa(final Long idPessoa) {

		final Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.add(Restrictions.eq("juridica.id", idPessoa));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedorLikeNomeFantasia(final String nomeFantasia) {
		
		try {
			
			final Criteria criteria = super.getSession().createCriteria(Fornecedor.class);

			criteria.createAlias("juridica","juridica");
			criteria.setFetchMode("juridica", FetchMode.JOIN);
			criteria.add(Restrictions.ilike("juridica.nomeFantasia", nomeFantasia, MatchMode.ANYWHERE));
			
			return criteria.list();
			
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresPorId(final List<Long> idsFornecedores) {
		
		final String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ " where fornecedor.id in (:idsFornecedores) ";
		
		final Query query = super.getSession().createQuery(hql);
		
		query.setParameterList("idsFornecedores", idsFornecedores);
		
		return query.list();
	}

	/**
	 * Obtem Fornecedor por codigo
	 * @param codigo
	 * @return Fornecedor
	 */
	@Override
	public Fornecedor obterFornecedorPorCodigoInterface(final Integer codigo) {
        final Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.add(Restrictions.eq("codigoInterface", codigo));
		
		criteria.setMaxResults(1);

		return (Fornecedor) criteria.uniqueResult();
	}
	
	/**
     * Obtem Fornecedor por codigo
     * @param codigo
     * @return Fornecedor
     */
    @Override
    public Fornecedor obterFornecedorPorCodigoJoinJuridica(final Integer codigo) {
        final Criteria criteria = getSession().createCriteria(Fornecedor.class);
        
        criteria.createAlias("juridica", "juridica");
        
        criteria.add(Restrictions.eq("codigoInterface", codigo));
                
        criteria.setMaxResults(1);

        return (Fornecedor) criteria.uniqueResult();
    }
	
	@Override
	public Integer obterMinCodigoInterfaceDisponivel() {
		
		final StringBuilder hql = new StringBuilder();
		
				hql.append(" select min(codInterface) from (											");
				hql.append(" select min(COD_INTERFACE + 1) as codInterface from FORNECEDOR           	");
				hql.append(" where (COD_INTERFACE + 1) not in (select COD_INTERFACE from FORNECEDOR) 	"); 
				hql.append(" UNION															  			");
				hql.append(" SELECT 1 AS codInteface from dual WHERE  1 not in				  			");
				hql.append(" ( select COD_INTERFACE from FORNECEDOR where COD_INTERFACE = 1 ) 			"); 
				hql.append(" ) as TBL_COD_INTEFACE 														");
		
		final Query query = super.getSession().createSQLQuery(hql.toString());
		
		final BigInteger codInterface = (BigInteger) query.uniqueResult();
		
		return codInterface.intValue();
		
	}
	
	@Override
	public Integer obterMaxCodigoInterface(){
		final Criteria criteria = getSession().createCriteria(Fornecedor.class);		
		
		criteria.setProjection(Projections.max("codigoInterface"));
		return (Integer) criteria.uniqueResult();
	}
	
	
	@Override
	public EnderecoFornecedor obterEnderecoPrincipal(final long idFornecedor) {
		final Criteria criteria = getSession().createCriteria(EnderecoFornecedor.class);
		criteria.add(Restrictions.eq("fornecedor.id", idFornecedor));

		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);

		return (EnderecoFornecedor) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresPorDesconto(final Long idDesconto) {
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append("select f ");
		hql.append("from Fornecedor f, HistoricoDescontoFornecedor hdf ");
		hql.append("where f.desconto.id = hdf.desconto.id ");
		hql.append("and f.id = hdf.fornecedor.id ");
		hql.append("and f.desconto.id = :idDesconto ");
		
		final Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", idDesconto);
		
		return q.list();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> obterFornecedorPorNome(final String nomeFornecedor) {
		
		final String hql = "select pessoa from Fornecedor fornecedor "
				+ " join  fornecedor.juridica pessoa "
				+ " where lower(pessoa.razaoSocial) like :nomeFornecedor ";
		
		final Query query = super.getSession().createQuery(hql);
		
		query.setParameter("nomeFornecedor", "%" + nomeFornecedor.toLowerCase() + "%");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> obterFornecedorPorNome(final String nomeFornecedor, final Integer qtdMaxResult) {
		
		final String hql = "select pessoa from Fornecedor fornecedor "
				+ " join  fornecedor.juridica pessoa "
				+ " where lower(pessoa.razaoSocial) like :nomeFornecedor ";
		
		final Query query = super.getSession().createQuery(hql);
		
		query.setParameter("nomeFornecedor", "%" + nomeFornecedor.toLowerCase() + "%");
		query.setMaxResults(qtdMaxResult);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> obterFornecedorPorNomeFantasia(final String nomeFantasia) {
		
		final String hql = "select pessoa from Fornecedor fornecedor "
				+ " join  fornecedor.juridica pessoa "
				+ " where lower(pessoa.nomeFantasia) like :nomeFantasia ";
		
		final Query query = super.getSession().createQuery(hql);
		
		query.setParameter("nomeFantasia", "%" + nomeFantasia.toLowerCase() + "%");
		
		return query.list();
	}

	/**
	 * Obtem Fornecedor Padrao, utilizado para em Movimentos Financeiros sem definição de Distribuidor
	 * @return Fornecedor
	 */
	@Override
	public Fornecedor obterFornecedorPadrao() {
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select f from Fornecedor f ");

	    hql.append(" where f.padrao = true ");
		
		final Query query = getSession().createQuery(hql.toString());
		
		return (Fornecedor) query.uniqueResult();
	}
	
	@Override
	public Fornecedor obterFornecedorPorMovimentoEstoqueCota(final Long movimentoEstoqueCotaId) {
		
		final StringBuilder hql = new StringBuilder();
		
		hql.append(" select fornecedor from MovimentoEstoqueCota mec ");
		hql.append(" join mec.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" join produto.fornecedores fornecedor ");
		
		final Query query = getSession().createQuery(hql.toString());
		
		query.setMaxResults(1);
		
		return (Fornecedor) query.uniqueResult();
	}

	@Override
	public Origem obterOrigemCadastroFornecedor(final Long idFornecedor) {
		
		final Query query = this.getSession().createQuery("select origem from Fornecedor where id = :idFornecedor");
		query.setParameter("idFornecedor", idFornecedor);
		
		return (Origem) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresDesc() {
		
		final StringBuilder hql = new StringBuilder("select new ");
		hql.append(Fornecedor.class.getCanonicalName())
		   .append("(f.id, j.nomeFantasia) from Fornecedor f join f.juridica j ");
		
		final Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemDTO<Long, String>> obterFornecedoresDestinatarios(SituacaoCadastro situacao) {

		Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.createAlias("juridica", "juridica");
		criteria.add(Restrictions.isNull("fornecedorUnificador"));
		
		if(situacao != null) {
			criteria.add(Restrictions.eq("situacaoCadastro", situacao));		
		}
		
		criteria.setProjection(Projections.projectionList().add(Projections.id(), "key").add(Projections.property("juridica.razaoSocial"), "value"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		
		criteria.setCacheable(true);
		
		return  criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresUnificados() {

		final String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ " where fornecedor.situacaoCadastro = :situacaoCadastro"
				+ " and ( fornecedor.fornecedorUnificador is not null or fornecedor.fornecedoresUnificados is not empty )" ;

		final Query query = super.getSession().createQuery(hql);

		query.setParameter("situacaoCadastro", SituacaoCadastro.ATIVO);

		return query.list();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresFcDinap(String codigoDinap,
			String codigoFC) {
		
		final Integer codDinap = (codigoDinap!= null) ? Integer.parseInt(codigoDinap):null;
		
		final Integer codFC = (codigoFC!= null) ? Integer.parseInt(codigoFC):null;
		
		final Criteria criteria = getSession().createCriteria(Fornecedor.class);
		
		criteria.add(Restrictions.in("codigoInterface",Arrays.asList(codDinap,codFC)));
		
		return criteria.list();
	}
	
	@Override
	public Integer obterCodigoInterface(Long idFornecedor) {
		
		final Query query = super.getSession().createQuery(" select f.codigoInterface from Fornecedor f where f.id=:idFornecedor ");

		query.setParameter("idFornecedor", idFornecedor);

		return (Integer) query.uniqueResult();
	}

}
