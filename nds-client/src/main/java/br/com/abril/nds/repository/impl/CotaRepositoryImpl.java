package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCCotaVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCCota;
import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.util.Intervalo;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class CotaRepositoryImpl extends AbstractRepositoryModel<Cota, Long>
		implements CotaRepository {

	@Value("#{queries.suspensaoCota}")
	protected String querySuspensaoCota;

	@Value("#{queries.countSuspensaoCota}")
	protected String queryCountSuspensaoCota;

	/**
	 * Construtor.
	 */
	public CotaRepositoryImpl() {

		super(Cota.class);
	}

	public Cota obterPorNumerDaCota(Integer numeroCota) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.add(Restrictions.eq("numeroCota", numeroCota));

		criteria.setMaxResults(1);

		return (Cota) criteria.uniqueResult();
	}
	
	public Cota obterPorNumerDaCotaAtiva(Integer numeroCota) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.add(Restrictions.eq("numeroCota", numeroCota));
		criteria.add(Restrictions.eq("situacaoCadastro", SituacaoCadastro.ATIVO));

		criteria.setMaxResults(1);

		return (Cota) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasPorNomePessoa(String nome) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.createAlias("pessoa", "pessoa");

		criteria.add(Restrictions.or(Restrictions.ilike("pessoa.nome", nome,
				MatchMode.ANYWHERE), Restrictions.ilike("pessoa.razaoSocial",
				nome, MatchMode.ANYWHERE)));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Cota> obterPorNome(String nome) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.createAlias("pessoa", "pessoa");

		criteria.add(Restrictions.or(Restrictions.eq("pessoa.nome", nome),
				Restrictions.eq("pessoa.razaoSocial", nome)));

		return criteria.list();
	}

	/**
	 * @see br.com.abril.nds.repository.CotaRepository#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(
				" select enderecoCota.id as id, enderecoCota.endereco as endereco, ")
				.append(" enderecoCota.principal as enderecoPrincipal, ")
				.append(" enderecoCota.tipoEndereco as tipoEndereco ")
				.append(" from EnderecoCota enderecoCota ")
				.append(" where enderecoCota.cota.id = :idCota ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				EnderecoAssociacaoDTO.class);

		query.setResultTransformer(resultTransformer);

		query.setParameter("idCota", idCota);

		return query.list();
	}
	
	
	@Override
	public TelefoneCota obterTelefonePorTelefoneCota(Long idTelefone, Long idCota){
		
		StringBuilder hql = new StringBuilder("select t from TelefoneCota t ");
		hql.append(" where t.telefone.id = :idTelefone ")
		   .append(" and   t.cota.id   = :idCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTelefone", idTelefone);
		query.setParameter("idCota", idCota);
		query.setMaxResults(1);
		
		return (TelefoneCota) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<CotaSuspensaoDTO> obterCotasSujeitasSuspensao(String sortOrder,
			String sortColumn, Integer inicio, Integer rp) {

		StringBuilder sql = new StringBuilder(querySuspensaoCota);

		sql.append(obterOrderByCotasSujeitasSuspensao(sortOrder, sortColumn));

		if (inicio != null && rp != null) {
			sql.append(" LIMIT :inicio,:qtdeResult");
		}

		Query query = getSession().createSQLQuery(sql.toString())
				.addScalar("idCota").addScalar("numCota")
				.addScalar("vlrConsignado").addScalar("vlrReparte")
				.addScalar("dividaAcumulada").addScalar("nome")
				.addScalar("razaoSocial").addScalar("dataAbertura");

		if (inicio != null && rp != null) {
			query.setInteger("inicio", inicio);
			query.setInteger("qtdeResult", rp);
		}

		query.setResultTransformer(Transformers
				.aliasToBean(CotaSuspensaoDTO.class));

		return query.list();
	}

	private String obterOrderByCotasSujeitasSuspensao(String sortOrder,
			String sortColumn) {
		String sql = "";

		if (sortColumn == null || sortOrder == null) {
			return sql;
		}

		sql += " ORDER BY ";

		if (sortColumn.equalsIgnoreCase("numCota")) {
			sql += "numCota";
		} else if (sortColumn.equalsIgnoreCase("nome")) {
			sql += "nome";
		} else if (sortColumn.equalsIgnoreCase("vlrConsignado")) {
			sql += "vlrConsignado";
		} else if (sortColumn.equalsIgnoreCase("vlrReparte")) {
			sql += "vlrReparte";
		} else if (sortColumn.equalsIgnoreCase("dividaAcumulada")) {
			sql += "dividaAcumulada";
		} else if (sortColumn.equalsIgnoreCase("diasAberto")) {
			sql += "dataAbertura";
		} else {
			return "";
		}

		sql += sortOrder.equalsIgnoreCase("asc") ? " ASC " : " DESC ";

		return sql;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoValorDTO> obterReparteDaCotaNoDia(Long idCota, Date date) {

		Criteria criteria = getSession().createCriteria(
				MovimentoEstoqueCota.class, "movimento");

		criteria.createAlias("movimento.produtoEdicao", "produtoEdicao");
		criteria.createAlias("movimento.tipoMovimento", "tipoMovimento");

		criteria.add(Restrictions.eq("data", date));
		criteria.add(Restrictions.eq("tipoMovimento.operacaoEstoque",
				OperacaoEstoque.ENTRADA));
		criteria.add(Restrictions.eq("cota.id", idCota));

		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.alias(Projections.property("qtde"),
				"quantidade"));
		projections.add(Projections.alias(
				Projections.property("produtoEdicao.precoVenda"), "preco"));

		criteria.setProjection(projections);

		criteria.setResultTransformer(Transformers
				.aliasToBean(ProdutoValorDTO.class));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoValorDTO> obterValorConsignadoDaCota(Long idCota) {

		Criteria criteria = getSession().createCriteria(
				EstoqueProdutoCota.class, "epCota");
		criteria.createAlias("epCota.produtoEdicao", "produtoEdicao");
		criteria.createAlias("epCota.cota", "cota");

		criteria.add(Restrictions.eq("cota.id", idCota));

		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.alias(
				Projections.property("epCota.qtdeRecebida"), "quantidade"));
		projections.add(Projections.alias(
				Projections.property("produtoEdicao.precoVenda"), "preco"));

		criteria.setProjection(projections);

		criteria.setResultTransformer(Transformers
				.aliasToBean(ProdutoValorDTO.class));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> obterDiasConcentracaoPagamentoCota(Long idCota) {
		StringBuilder hql = new StringBuilder("select cc.codigoDiaSemana ");
		hql.append(
				" from ConcentracaoCobrancaCota cc, Cota cota, ParametroCobrancaCota p, FormaCobranca fc ")
				.append(" where cota.id                     = p.cota.id ")
				.append(" and   fc.parametroCobrancaCota.id = p.id ")
				.append(" and   cc.formaCobranca.id = fc.id ")
				.append(" and   fc.principal = true ")
				.append(" and   cota.id                     = :idCota");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);

		return query.list();
	}

	@Override
	public Long obterTotalCotasSujeitasSuspensao() {

		StringBuilder sql = new StringBuilder(queryCountSuspensaoCota);

		Query query = getSession().createSQLQuery(sql.toString());

		Long qtde = ((BigInteger) query.uniqueResult()).longValue();

		return qtde == null ? 0 : qtde;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotaAssociadaFiador(Long idFiador) {
		Criteria criteria = this.getSession().createCriteria(Cota.class);
		criteria.add(Restrictions.eq("fiador.id", idFiador));

		return criteria.list();
	}

	@Override
	public Long obterQntCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT count ( cota.id ) ");

		hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}
		return (Long) query.uniqueResult();
	}

	@Override
	public BigInteger obterQntExemplaresCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");

		hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}

		query.setMaxResults(1);

		return (BigInteger) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChamadaAntecipadaEncalheDTO> obterCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ")
				.append(ChamadaAntecipadaEncalheDTO.class.getCanonicalName())
				.append(" ( box.codigo , cota.numeroCota, estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida, ")
				.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
				.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
				.append(" else null end ").append(" ) ");

		hql.append(getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(filtro));

		hql.append(getOrderByCotasSujeitasAntecipacoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasSujeitasAntecipacoEncalhe(filtro);

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}

		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao()
						.getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}

	/**
	 * Retorna os parametros da consulta de dividas.
	 * 
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String, Object> getParametrosCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("codigoProduto", filtro.getCodigoProduto());
		param.put("numeroEdicao", filtro.getNumeroEdicao());
		param.put("status", StatusLancamento.EXPEDIDO);
		param.put("dataAtual", new Date());
		param.put("tipoChamadaEncalhe", TipoChamadaEncalhe.ANTECIPADA);

		if (filtro.getNumeroCota() != null) {
			param.put("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getFornecedor() != null) {
			param.put("fornecedor", filtro.getFornecedor());
		}

		if (filtro.getBox() != null) {
			param.put("box", filtro.getBox());
		}
		
		if(filtro.getRota()!= null){
			param.put("rota",filtro.getRota());
		}
		
		if(filtro.getRoteiro()!= null){
			param.put("roteiro",filtro.getRoteiro());
		}

		return param;
	}

	private String getSqlFromEWhereCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM ").append(" Cota cota  ")
				.append(" JOIN cota.pessoa pessoa ")
				.append(" JOIN cota.box box ")
				.append(" JOIN cota.estoqueProdutoCotas estoqueProdutoCota ")
				.append(" JOIN cota.estudoCotas estudoCota ")
				.append(" JOIN estudoCota.estudo estudo ")
				.append(" JOIN estudo.produtoEdicao produtoEdicao  ")
				.append(" JOIN produtoEdicao.produto produto ")
				.append(" JOIN produtoEdicao.lancamentos lancamento ")
				.append(" JOIN cota.pdvs pdv ")
				.append(" LEFT JOIN pdv.roteirizacao roteirizacao ")
				.append(" LEFT JOIN roteirizacao.rota rota  ")
				.append(" LEFT JOIN rota.roteiro roteiro ");

		if (filtro.getFornecedor() != null) {
			hql.append(" JOIN produto.fornecedores fornecedor ");
		}

		hql.append(" WHERE ")
				.append("NOT EXISTS (")
				.append(" SELECT chamadaEncalheCota.cota FROM ChamadaEncalheCota chamadaEncalheCota ")
				.append(" JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
				.append(" WHERE chamadaEncalheCota.cota = cota ")
				.append(" AND chamadaEncalhe.produtoEdicao = produtoEdicao ")
				.append(" AND chamadaEncalhe.tipoChamadaEncalhe=:tipoChamadaEncalhe")
				.append(" ) ")
				.append(" AND estoqueProdutoCota.produtoEdicao.id = produtoEdicao.id ")
				.append(" AND lancamento.status =:status ")
				.append(" AND produto.codigo =:codigoProduto ")
				.append(" AND produtoEdicao.numeroEdicao =:numeroEdicao ")
				.append(" AND lancamento.dataRecolhimentoPrevista >:dataAtual ")
				.append(" AND (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) > 0 ")
				.append(" AND pdv.caracteristicas.pontoPrincipal = true ");

		if (filtro.getNumeroCota() != null) {

			hql.append(" AND cota.numeroCota =:numeroCota ");
		}

		if (filtro.getFornecedor() != null) {

			hql.append(" AND fornecedor.id =:fornecedor ");
		}

		if (filtro.getBox() != null) {

			hql.append(" AND box.id =:box ");
		}
		
		if(filtro.getRota()!= null){
			hql.append(" AND rota.id =:rota ");
		}
		
		if(filtro.getRoteiro()!= null ){
			hql.append(" AND roteiro.id =:roteiro ");
		}

		return hql.toString();
	}

	/**
	 * Retorna a instrução de ordenação para consulta
	 * obterCotasSujeitasAntecipacoEncalhe
	 * 
	 * @param filtro
	 *            - filtro de pesquisa com os parâmetros de ordenação.
	 * 
	 * @return String
	 */
	private String getOrderByCotasSujeitasAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return "";
		}

		StringBuilder hql = new StringBuilder();

		switch (filtro.getOrdenacaoColuna()) {
		case BOX:
			hql.append(" order by box.codigo ");
			break;
		case NOME_COTA:
			hql.append(" order by   ")
					.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
					.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
					.append(" else null end ");
			break;
		case NUMERO_COTA:
			hql.append(" order by cota.numeroCota ");
			break;
		case QNT_EXEMPLARES:
			hql.append(" order by  estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");
			break;
		default:
			hql.append(" order by  box.codigo ");
		}

		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}

	/**
	 * @see br.com.abril.nds.repository.CotaRepository#obterCotaPDVPorNumeroDaCota(java.lang.Integer)
	 */
	@Override
	public Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from Cota cota ").append(" left join fetch cota.pdvs ")
				.append(" where cota.numeroCota = :numeroCota ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);

		return (Cota) query.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.repository.CotaRepository#obterEnderecoPrincipal(long)
	 */
	@Override
	public EnderecoCota obterEnderecoPrincipal(long idCota) {
		Criteria criteria = getSession().createCriteria(EnderecoCota.class);
		criteria.add(Restrictions.eq("cota.id", idCota));

		criteria.add(Restrictions.eq("principal", true));
		criteria.setMaxResults(1);

		return (EnderecoCota) criteria.uniqueResult();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> obterCotas(FiltroCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(this.getSqlPesquisaCota(filtro, Boolean.FALSE));

		hql.append(this.ordenarConsultaCota(filtro));

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("principal", Boolean.TRUE);

		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getNumeroCpfCnpj() != null
				&& !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
			query.setParameter("numeroCpfCnpj", filtro.getNumeroCpfCnpj() + "%");
		}

		if (filtro.getNomeCota() != null
				&& !filtro.getNomeCota().trim().isEmpty()) {
			query.setParameter("nomeCota", filtro.getNomeCota() + "%" );
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				CotaDTO.class));
		
		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao()
						.getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}

	public Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(this.getSqlPesquisaCota(filtro, Boolean.TRUE));

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("principal", Boolean.TRUE);

		if (filtro.getNumeroCota() != null) {
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getNumeroCpfCnpj() != null
				&& !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
			query.setParameter("numeroCpfCnpj", filtro.getNumeroCpfCnpj() +  "%");
		}

		if (filtro.getNomeCota() != null
				&& !filtro.getNomeCota().trim().isEmpty()) {
			query.setParameter("nomeCota", filtro.getNomeCota() + "%");
		}

		return (Long) query.uniqueResult();
	}

	private String getSqlPesquisaCota(FiltroCotaDTO filtro, boolean isCount) {

		StringBuilder hql = new StringBuilder();

		if (isCount) {
			hql.append(" select count ( cota.numeroCota ) ");
		} else {

			hql.append(
					"SELECT cota.id as idCota, cota.numeroCota as numeroCota, ")
					.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
					.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
					.append(" else null end as nomePessoa, ")
					.append(" case when (pessoa.cpf is not null) then ( pessoa.cpf )")
					.append(" when (pessoa.cnpj is not null) then ( pessoa.cnpj )")
					.append(" else null end as numeroCpfCnpj, ")
					.append(" pdv.contato as contato ,")
					.append(" telefone.ddd || '-'|| telefone.numero as telefone ,")
					.append(" pessoa.email as email ,")
					.append(" cota.situacaoCadastro as status ");
		}

		hql.append(" FROM Cota cota ")
				.append(" join cota.pessoa pessoa ")
				.append(" left join cota.pdvs pdv ")
				.append(" left join cota.telefones telefonesCota ")
				.append(" left join telefonesCota.telefone telefone ")

				.append(" WHERE")
				.append(" ( telefonesCota.principal is null OR telefonesCota.principal=:principal ) ")
				.append(" AND (pdv.caracteristicas.pontoPrincipal is null OR pdv.caracteristicas.pontoPrincipal=:principal) ");

		if (filtro.getNumeroCota() != null) {
			hql.append(" AND cota.numeroCota =:numeroCota ");
		}

		if (filtro.getNumeroCpfCnpj() != null
				&& !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
			hql.append(" AND ( upper (pessoa.cpf) like(:numeroCpfCnpj) OR  upper(pessoa.cnpj) like upper (:numeroCpfCnpj) ) ");
		}

		if (filtro.getNomeCota() != null
				&& !filtro.getNomeCota().trim().isEmpty()) {

			hql.append(" AND ( upper(pessoa.nome) like upper(:nomeCota) OR  upper(pessoa.razaoSocial) like  upper(:nomeCota ) )");
		}

		return hql.toString();
	}

	/**
	 * Retorna string sql de ordenação da consulta de cotas
	 * 
	 * @param filtro
	 *            - filtro com opção de ordenação escolhida
	 * @return String
	 */
	private String ordenarConsultaCota(FiltroCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		if (filtro.getOrdemColuna() != null) {

			switch (filtro.getOrdemColuna()) {

			case NUMERO_COTA:
				hql.append(" ORDER BY numeroCota ");
				break;

			case NOME_PESSOA:
				hql.append(" ORDER BY nomePessoa ");
				break;

			case NUMERO_CPF_CNPJ:
				hql.append(" ORDER BY numeroCpfCnpj ");
				break;

			case CONTATO:
				hql.append(" ORDER BY contato ");
				break;

			case TELEFONE:
				hql.append(" ORDER BY telefone ");
				break;

			case EMAIL:
				hql.append(" ORDER BY email ");
				break;

			case STATUS:
				hql.append(" ORDER BY status ");
				break;

			default:
				hql.append(" ORDER BY numeroCota ");
			}

			if (filtro.getPaginacao() != null
					&& filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}

		}

		return hql.toString();
	}
	
	public Integer gerarSugestaoNumeroCota(){
		
		String hql = "select max(cota.numeroCota) from Cota cota where cota.id not in ( select h.pk.idCota from HistoricoNumeroCota h )";
		
		Integer numeroCota =  (Integer) getSession().createQuery(hql).uniqueResult();
		
		return (numeroCota == null ) ? 0 : numeroCota + 1;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.CotaRepository#obterCurvaABCCotaTotal(br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO)
	 */
	@Override
	public ResultadoCurvaABCCota obterCurvaABCCotaTotal(FiltroCurvaABCCotaDTO filtro){
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(ResultadoCurvaABCCota.class.getCanonicalName())
		.append(" ( (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ) ");

		hql.append(getWhereQueryObterCurvaABCCota(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCCota(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		return (ResultadoCurvaABCCota) query.list().get(0);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.CotaRepository#obterCurvaABCCota(br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCCotaVO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(RegistroCurvaABCCotaVO.class.getCanonicalName())
		.append(" ( estoqueProdutoCota.produtoEdicao.produto.codigo , ")
		.append("   estoqueProdutoCota.produtoEdicao.produto.nome , ")
		.append("   estoqueProdutoCota.produtoEdicao.numeroEdicao , ")
		.append("   (sum(movimentos.qtde)) , ")
		.append("   (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ) ");

		hql.append(getWhereQueryObterCurvaABCCota(filtro));
		hql.append(getGroupQueryObterCurvaABCCota(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCCota(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}

		return complementarCurvaABCCota((List<RegistroCurvaABCCotaVO>) query.list());

	}

	/**
	 * Retorna as tabelas, joins e filtros da Query de seleção do relatório de vendas
	 * @param filtro
	 * @return
	 */
	private String getWhereQueryObterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM EstoqueProdutoCota AS estoqueProdutoCota ")
		.append(" LEFT JOIN estoqueProdutoCota.movimentos AS movimentos ")
		.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao.produto.fornecedores AS fornecedores ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.enderecos AS enderecos ")
		.append(" LEFT JOIN enderecos.endereco AS endereco ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.pdvs AS pdv ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.pessoa AS pessoa ");

		hql.append("WHERE movimentos.data BETWEEN :dataDe AND :dataAte ");
		hql.append(" AND movimentos.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append(" AND estoqueProdutoCota.produtoEdicao.produto.codigo = :codigoProduto ");
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			hql.append(" AND estoqueProdutoCota.produtoEdicao.produto.nome = :nomeProduto ");
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedores.id = :codigoFornecedor ");
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.numeroEdicao = :edicaoProduto ");
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.produto.editor.codigo = :codigoEditor ");
		}

		if (filtro.getCodigoCota() != null && !filtro.getCodigoCota().isEmpty()) {
			hql.append("AND estoqueProdutoCota.cota.numeroCota = :codigoCota ");
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append("AND pessoa.nome = :nomeCota ");
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			hql.append("AND endereco.cidade = :municipio ");
		}

		return hql.toString();

	}

	/**
	 * Retorna o agrupamento das pesquisas do relatório de vendas
	 * @param filtro
	 * @return
	 */
	private String getGroupQueryObterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" GROUP BY estoqueProdutoCota.produtoEdicao.produto.codigo, ")
		   .append("   estoqueProdutoCota.produtoEdicao.produto.nome, ")
		   .append("   estoqueProdutoCota.produtoEdicao.numeroEdicao ");

		return hql.toString();
	}

	/**
	 * Popula os parametros da consulta do relatório de vendas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> getParametrosObterCurvaABCCota(FiltroCurvaABCCotaDTO filtro){

		HashMap<String,Object> param = new HashMap<String, Object>();

		param.put("dataDe",  filtro.getDataDe());
		param.put("dataAte", filtro.getDataAte());

		param.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		if (filtro.getCodigoCota() != null && !filtro.getCodigoCota().isEmpty()) {
			param.put("codigoCota", Integer.parseInt(filtro.getCodigoCota().toString()));
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			param.put("nomeCota", filtro.getNomeCota());
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			param.put("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			param.put("codigoProduto", filtro.getCodigoProduto().toString());
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			param.put("nomeProduto", filtro.getNomeProduto());
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			param.put("edicaoProduto", filtro.getEdicaoProduto());
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			param.put("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			param.put("municipio", filtro.getMunicipio());
		}

		return param;
	}

	/**
	 * Insere os registros de participação e participação acumulada no resultado da consulta HQL
	 * @param lista
	 * @return
	 */
	private List<RegistroCurvaABCCotaVO> complementarCurvaABCCota(List<RegistroCurvaABCCotaVO> lista) {

		BigDecimal participacaoTotal = new BigDecimal(0);
		BigDecimal vendaTotal = new BigDecimal(0);

		// Soma todos os valores de participacao
		for (RegistroCurvaABCCotaVO registro : lista) {
			participacaoTotal.add(registro.getFaturamento());
			vendaTotal.add(registro.getVendaExemplares());
		}

		BigDecimal participacaoRegistro = new BigDecimal(0);
		BigDecimal participacaoAcumulada = new BigDecimal(0);
		BigDecimal porcentagemVendaRegistro = new BigDecimal(0);
		
		RegistroCurvaABCCotaVO registro = null;

		// Verifica o percentual dos valores em relação ao total de participacao
		for (int i=0; i<lista.size(); i++) {

			registro = (RegistroCurvaABCCotaVO) lista.get(i);

			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamento().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			registro.setParticipacao(participacaoRegistro);

			if (vendaTotal.doubleValue() != 0) {
				porcentagemVendaRegistro = new BigDecimal(registro.getVendaExemplares().doubleValue()*100/vendaTotal.doubleValue());
			}
			registro.setPorcentagemVenda(porcentagemVendaRegistro);
			
			participacaoAcumulada.add(participacaoRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);

			// Substitui o registro pelo registro atualizado (com participacao total)
			lista.set(i, registro);

		}

		return lista;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasPorIDS(List<Long> idsCotas) {
		
		try {
			
			if (idsCotas == null || idsCotas.isEmpty()) {
				return null;
			}
			 
			Criteria criteria = super.getSession().createCriteria(Cota.class);
			
			criteria.add(Restrictions.in("id", idsCotas));
			
			return criteria.list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro){
		
		StringBuilder hql = new StringBuilder("select count (cota.id) ");
		hql.append(" from Cota cota ");
		
		if (situacaoCadastro != null){
			
			hql.append(" where cota.situacaoCadastro = :situacao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (situacaoCadastro != null){
			
			query.setParameter("situacao", situacaoCadastro);
		}
		
		return (Long) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.CotaRepository#obterIdCotasEntre(br.com.abril.nds.util.Intervalo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<Long> obterIdCotasEntre(Intervalo<Integer> intervaloCota, Intervalo<Integer> intervaloBox, SituacaoCadastro situacao) {
		
		Set<Long> listaIdCotas = new HashSet<Long>();
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		criteria.createAlias("box", "box");
		criteria.setProjection(Projections.id());
		if (intervaloCota != null && intervaloCota.getDe() != null) {
			
			if (intervaloCota.getAte() != null) {
				criteria.add(Restrictions.between("numeroCota", intervaloCota.getDe(),
						intervaloCota.getAte()));
			} else {
				criteria.add(Restrictions.eq("numeroCota", intervaloCota.getDe()));
			}
		}
		
		if(intervaloBox != null && intervaloBox.getDe() != null &&  intervaloBox.getAte() != null){
			criteria.add(Restrictions.between("box.codigo", intervaloBox.getDe(),
					intervaloBox.getAte()));
		}
		if(situacao != null){
			criteria.add(Restrictions.eq("situacaoCadastro", situacao));
		}
		listaIdCotas.addAll(criteria.list());
		
		return listaIdCotas;
	}

}
