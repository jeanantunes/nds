package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.ResultadoCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.Intervalo;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class CotaRepositoryImpl extends AbstractRepositoryModel<Cota, Long> implements CotaRepository {
	
    private static final Logger LOG = LoggerFactory.getLogger(CotaRepositoryImpl.class);

	@Value("#{queries.suspensaoCota}")
	public String querySuspensaoCota;

	@Value("#{queries.countSuspensaoCota}")
	public String queryCountSuspensaoCota;
	

	/**
	 * Construtor.
	 */
	public CotaRepositoryImpl() {

		super(Cota.class);
	}

	public Cota obterPorNumerDaCota(Integer numeroCota) {
		
		Query query = 
				this.getSession().createQuery(
						"select c from Cota c where c.numeroCota = :numeroCota");
		
		query.setParameter("numeroCota", numeroCota);
		
		return (Cota) query.uniqueResult();
	}
	
	public Cota obterPorNumerDaCotaAtiva(Integer numeroCota) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);

		criteria.add(Restrictions.eq("numeroCota", numeroCota));
		criteria.add(Restrictions.eq("situacaoCadastro", SituacaoCadastro.ATIVO));

		criteria.setMaxResults(1);

		return (Cota) criteria.uniqueResult();
	}
	
	public Cota obterPorPDV(Long idPDV) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.createAlias("pdvs", "pdv");
		
		criteria.add(Restrictions.eq("pdv.id", idPDV));
		
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

		criteria.add(Restrictions.or(Restrictions.like("pessoa.nome", nome, MatchMode.ANYWHERE),
				Restrictions.like("pessoa.razaoSocial", nome, MatchMode.ANYWHERE)));

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

		ResultTransformer resultTransformer = null; 
		
		try {
		    resultTransformer = new AliasToBeanConstructorResultTransformer(
		            EnderecoAssociacaoDTO.class.getConstructor(Long.class, Endereco.class, boolean.class, TipoEndereco.class));
		} catch (Exception e) {
            String message = "Erro criando result transformer para classe: "
                    + EnderecoAssociacaoDTO.class.getName();
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }

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
		hql.append(" from ConcentracaoCobrancaCota cc ")
		   .append(" join cc.formaCobranca fc ")
		   .append(" join fc.parametroCobrancaCota p ")
		   .append(" join p.cota cota ")
		   .append(" where cota.id = :idCota ");

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

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append("SELECT DISTINCT count ( cota.id ) ");

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

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append("SELECT DISTINCT new ")
				.append(ChamadaAntecipadaEncalheDTO.class.getCanonicalName())
				.append(" ( box.codigo, box.nome ,cota.numeroCota, estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida, ")
				.append(" lancamento.id ,")
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
		
		if(filtro.getCodMunicipio()!= null){
			param.put("codigoCidadeIBGE",filtro.getCodMunicipio());
		}
		
		if(filtro.getCodTipoPontoPDV()!= null){
			param.put("codigoTipoPontoPDV", filtro.getCodTipoPontoPDV());
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
				.append(" LEFT JOIN pdv.rotas rotaPdv  ")
				.append(" LEFT JOIN rotaPdv.rota rota  ")
				.append(" LEFT JOIN rota.roteiro roteiro ")
				.append(" LEFT JOIN roteiro.roteirizacao roteirizacao ");

		if (filtro.getFornecedor() != null) {
			hql.append(" JOIN produto.fornecedores fornecedor ");
		}
		
		if(filtro.getCodMunicipio()!= null){
			hql.append(" JOIN pdv.enderecos enderecoPDV ")
				.append(" JOIN enderecoPDV.endereco endereco ");
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
		
		if(filtro.getCodMunicipio()!= null){
			hql.append(" AND endereco.codigoCidadeIBGE =:codigoCidadeIBGE ");
		}
		
		if(filtro.getCodTipoPontoPDV()!= null){
			hql.append(" AND pdv.segmentacao.tipoPontoPDV.codigo =:codigoTipoPontoPDV ");
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
		
		query.setMaxResults(1);

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
		
		if (filtro.getLogradouro() != null
				&& !filtro.getLogradouro().trim().isEmpty()) {

			query.setParameter("logradouro", filtro.getLogradouro() + "%" );
		}
		
		if (filtro.getBairro() != null
				&& !filtro.getBairro().trim().isEmpty()) {

			query.setParameter("bairro", filtro.getBairro() + "%" );
		}
		
		if (filtro.getMunicipio() != null
				&& !filtro.getMunicipio().trim().isEmpty()) {

			query.setParameter("municipio", filtro.getMunicipio() + "%" );
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
		
		if (filtro.getLogradouro() != null
				&& !filtro.getLogradouro().trim().isEmpty()) {

			query.setParameter("logradouro", filtro.getLogradouro() + "%" );
		}
		
		if (filtro.getBairro() != null
				&& !filtro.getBairro().trim().isEmpty()) {

			query.setParameter("bairro", filtro.getBairro() + "%" );
		}
		
		if (filtro.getMunicipio() != null
				&& !filtro.getMunicipio().trim().isEmpty()) {

			query.setParameter("municipio", filtro.getMunicipio() + "%" );
		}

		return (Long) query.uniqueResult();
	}

	private String getSubSqlPesquisaTelefone() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ( select telefone.ddd || '-' || telefone.numero ");
		
		hql.append(" from  ");
		
		hql.append(" Telefone telefone where telefone.id = 	");
		
		hql.append(" ( select max(telefone.id) from TelefoneCota telefoneCota inner join telefoneCota.telefone as telefone");
		
		hql.append(" where telefoneCota.cota.id = cota.id and ");
		
		hql.append(" telefoneCota.principal = true ) ) as telefone, ");
		
		return hql.toString();
		
	}
	
	private String getSubSqlPesquisaContatoPDV() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ( select pdv.contato ");
		
		hql.append(" from  ");
		
		hql.append(" PDV pdv ");
		
		hql.append(" where pdv.id =  ");
		
		hql.append(" ( select max(pdv.id) from PDV pdv where pdv.cota.id = cota.id and pdv.caracteristicas.pontoPrincipal = true ) ) as contato,");
		
		return hql.toString();
		
	}
	
	private String getSqlPesquisaCota(FiltroCotaDTO filtro, boolean isCount) {

		StringBuilder hql = new StringBuilder();

		int colunaRazaoSocNomePessoa = 3;
		int colunaNumeroCpfCnpj 	 = 4;
		int colunaContato  = 5;
		int colunaTelefone = 6;
		
		if (isCount) {
			
			hql.append(" select count(distinct cota.numeroCota) ");
			
			
			
		} else {

			hql.append(
					"SELECT cota.id as idCota, cota.numeroCota as numeroCota, ")
					.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
					.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
					.append(" else null end as nomePessoa, ")
					.append(" case when (pessoa.cpf is not null) then ( pessoa.cpf )")
					.append(" when (pessoa.cnpj is not null) then ( pessoa.cnpj )")
					.append(" else null end as numeroCpfCnpj, ")
					
					
					.append(getSubSqlPesquisaContatoPDV())
					.append(getSubSqlPesquisaTelefone())
					
					.append(" pessoa.email as email ,")
					.append(" cota.situacaoCadastro as status, ")
					.append(" box.nome as descricaoBox ");
		}

		hql.append(" FROM Cota cota 								")
				.append(" join cota.pessoa pessoa 					")
				.append(" left join cota.enderecos enderecoCota 	")
				.append(" left join enderecoCota.endereco endereco 	")
		        .append(" left join cota.box box ");

		
		
		if(	filtro.getNumeroCota() != null ||
			(filtro.getNumeroCpfCnpj() != null && !filtro.getNumeroCpfCnpj().trim().isEmpty()) ||
			(filtro.getNomeCota() != null && !filtro.getNomeCota().trim().isEmpty()) ||
			(filtro.getLogradouro() != null && !filtro.getLogradouro().trim().isEmpty()) ||
			(filtro.getBairro() != null && !filtro.getBairro().trim().isEmpty()) ||
			(filtro.getMunicipio() != null && !filtro.getMunicipio().trim().isEmpty())) {
			
			hql.append(" WHERE ");
			
		}
		
		boolean indAnd = false;

		if (filtro.getNumeroCota() != null) {
			
			hql.append(" cota.numeroCota =:numeroCota ");
			
			indAnd = true;
		}

		if (filtro.getNumeroCpfCnpj() != null
				&& !filtro.getNumeroCpfCnpj().trim().isEmpty()) {
			
			if(indAnd) {
				hql.append(" AND ");
			}
			
			hql.append(" ( upper (pessoa.cpf) like(:numeroCpfCnpj) OR  upper(pessoa.cnpj) like upper (:numeroCpfCnpj) ) ");
			
			indAnd = true;
		}

		if (filtro.getNomeCota() != null
				&& !filtro.getNomeCota().trim().isEmpty()) {

			if(indAnd) {
				hql.append(" AND ");
			}
			
			hql.append(" ( upper(pessoa.nome) like upper(:nomeCota) OR  upper(pessoa.razaoSocial) like  upper(:nomeCota ) )");
			
			indAnd = true;
		}
		
		if (filtro.getLogradouro() != null
				&& !filtro.getLogradouro().trim().isEmpty()) {

			if(indAnd) {
				hql.append(" AND ");
			}
			
			hql.append(" ( upper(endereco.logradouro) like upper(:logradouro) )");
			
			indAnd = true;
		}
		
		if (filtro.getBairro() != null
				&& !filtro.getBairro().trim().isEmpty()) {

			if(indAnd) {
				hql.append(" AND ");
			}
			
			hql.append(" ( upper(endereco.bairro) like upper(:bairro) )");
			
			indAnd = true;
		}
		
		if (filtro.getMunicipio() != null
				&& !filtro.getMunicipio().trim().isEmpty()) {

			if(indAnd) {
				hql.append(" AND ");
			}
			
			hql.append(" ( upper(endereco.cidade) like upper(:municipio) )");
		}

		
		if (!isCount) {

			hql.append("group by ");
			hql.append("cota.id, ");
			hql.append("cota.numeroCota, ");
			hql.append(colunaRazaoSocNomePessoa + ", ");
			hql.append(colunaNumeroCpfCnpj 		+ ", ");
			hql.append(colunaContato 			+ ", ");
			hql.append(colunaTelefone 			+ ", ");
			hql.append("pessoa.email, ");
			hql.append("cota.situacaoCadastro, ");
			hql.append("box.nome  ");
			
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
			case BOX:
				hql.append("ORDER BY descricaoBox ");
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
	public ResultadoCurvaABCCotaDTO obterCurvaABCCotaTotal(FiltroCurvaABCCotaDTO filtro){
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(ResultadoCurvaABCCotaDTO.class.getCanonicalName())

		.append(" ( (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - ( movimentos.valoresAplicados.valorDesconto ))) ) ) ");

		hql.append(getWhereQueryObterCurvaABCCota(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCCota(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setParameter("statusLancamentoRecolhido", StatusLancamento.RECOLHIDO);
		
		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			query.setParameterList("edicaoProduto", (filtro.getEdicaoProduto()));
		}
		
		return (ResultadoCurvaABCCotaDTO) query.list().get(0);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.CotaRepository#obterCurvaABCCota(br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(RegistroCurvaABCCotaDTO.class.getCanonicalName())
		.append(" ( estoqueProdutoCota.produtoEdicao.produto.codigo , ")
		.append("   estoqueProdutoCota.produtoEdicao.produto.nome , ")
		.append("   estoqueProdutoCota.produtoEdicao.numeroEdicao , ")

		.append("   (sum(movimentos.qtde)) , ")
		.append("   (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - ( movimentos.valoresAplicados.valorDesconto ))) ) , ")
		.append("     estoqueProdutoCota.cota.id , estoqueProdutoCota.produtoEdicao.produto.id ) ");

		hql.append(getWhereQueryObterCurvaABCCota(filtro));
		hql.append(getGroupQueryObterCurvaABCCota(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCCota(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setParameter("statusLancamentoRecolhido", StatusLancamento.RECOLHIDO);
		
		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			query.setParameterList("edicaoProduto", (filtro.getEdicaoProduto()));
		}

		return complementarCurvaABCCota((List<RegistroCurvaABCCotaDTO>) query.list());

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
		.append(" LEFT JOIN movimentos.lancamento as lancamento ")
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
			hql.append(" AND upper(estoqueProdutoCota.produtoEdicao.produto.nome) like upper ( :nomeProduto ) ");
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedores.id = :codigoFornecedor ");
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.numeroEdicao in( :edicaoProduto ) ");
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.produto.editor.codigo = :codigoEditor ");
		}

		if (filtro.getCodigoCota() != null ) {
			hql.append("AND estoqueProdutoCota.cota.numeroCota = :codigoCota ");
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append("AND upper(pessoa.nome) like upper( :nomeCota) or upper(pessoa.razaoSocial) like upper(:nomeCota) ");
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
		
		if (filtro.getCodigoCota() != null) {
			param.put("codigoCota", filtro.getCodigoCota());
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			param.put("nomeCota", filtro.getNomeCota() +"%");
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			param.put("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			param.put("codigoProduto", filtro.getCodigoProduto().toString());
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			param.put("nomeProduto", filtro.getNomeProduto()+"%");
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
	private List<RegistroCurvaABCCotaDTO> complementarCurvaABCCota(List<RegistroCurvaABCCotaDTO> lista) {

		BigDecimal participacaoTotal = BigDecimal.ZERO;
		BigInteger vendaTotal = BigInteger.ZERO;

		// Soma todos os valores de participacao
		for (RegistroCurvaABCCotaDTO registro : lista) {
			if (registro.getFaturamento()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamento());
			} 
			vendaTotal = vendaTotal.add(registro.getVendaExemplares());
		}

		BigDecimal participacaoRegistro =BigDecimal.ZERO;
		BigDecimal participacaoAcumulada = BigDecimal.ZERO;
		BigDecimal porcentagemVendaRegistro = BigDecimal.ZERO;
		
	
		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroCurvaABCCotaDTO registro : lista) {

			if (registro.getFaturamento() != null) {
				// Partipacao do registro em relacao a participacao total no periodo
				if ( participacaoTotal.doubleValue() != 0 ) {
					participacaoRegistro = new BigDecimal((registro.getFaturamento().doubleValue()*100)/participacaoTotal.doubleValue());
				}
			} else {
				participacaoRegistro = BigDecimal.ZERO;
			}
			
			registro.setParticipacao(participacaoRegistro);
			
			if (vendaTotal.doubleValue() != 0) {
				porcentagemVendaRegistro = new BigDecimal(registro.getVendaExemplares().doubleValue()*100/vendaTotal.doubleValue());
			}
			
			participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
			
			registro.setPorcentagemVenda(porcentagemVendaRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);
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
	public Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro) {
		
		StringBuilder hql = new StringBuilder(" select count (cota.id) ");
		
		hql.append(" from Cota cota ");
		
		if (situacaoCadastro != null) {
			
			hql.append(" where cota.situacaoCadastro = :situacao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (situacaoCadastro != null) {
			
			query.setParameter("situacao", situacaoCadastro);
		}
		
		Object uniqueResult = query.uniqueResult();
		return (Long) uniqueResult;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterCotas(SituacaoCadastro situacaoCadastro) {
		
		StringBuilder hql = new StringBuilder(" from Cota cota ");

		if (situacaoCadastro != null) {
			
			hql.append(" where cota.situacaoCadastro = :situacao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (situacaoCadastro != null) {
			
			query.setParameter("situacao", situacaoCadastro);
		}
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasComInicioAtividadeEm(Date dataInicioAtividade) {
		
		StringBuilder hql = new StringBuilder(" from Cota cota ");

		if (dataInicioAtividade != null) {
			
			hql.append(" where cota.inicioAtividade = :dataInicioAtividade ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dataInicioAtividade != null) {
			
			query.setParameter("dataInicioAtividade", dataInicioAtividade);
		}
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte) {
		
		StringBuilder hql = new StringBuilder(" select cotaAusente.cota from CotaAusente cotaAusente ");
		
		hql.append(" where ativo = true");
		
		if (dataExpedicaoReparte != null) {
			
			hql.append(" and cotaAusente.data = :dataExpedicaoReparte ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dataExpedicaoReparte != null) {
			
			query.setParameter("dataExpedicaoReparte", dataExpedicaoReparte);
		}
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe) {
		
		StringBuilder hql = 
			new StringBuilder(" select chamadaEncalheCota.cota from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" where chamadaEncalheCota.cota.id not in ( ");
		hql.append(" select cota.id from ControleConferenciaEncalheCota controleConferenciaEncalheCota ");
		hql.append(" join controleConferenciaEncalheCota.cota cota ");
		hql.append(" where controleConferenciaEncalheCota.dataOperacao >= :dataRecolhimentoEncalhe ");
		hql.append(" and controleConferenciaEncalheCota.status = :statusControleConferenciaEncalhe) ");
		hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataRecolhimentoEncalhe ");
		hql.append(" group by chamadaEncalheCota.cota.id ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("dataRecolhimentoEncalhe", dataRecolhimentoEncalhe);
		query.setParameter("statusControleConferenciaEncalhe", StatusOperacao.CONCLUIDO);

		return query.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.CotaRepository#obterIdCotasEntre(br.com.abril.nds.util.Intervalo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterIdCotasEntre(Intervalo<Integer> intervaloCota, Intervalo<Integer> intervaloBox, List<SituacaoCadastro> situacoesCadastro
										, Long idRoteiro, Long idRota, String sortName, String sortOrder, Integer maxResults, Integer page) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		criteria.createAlias("box", "box");
		criteria.createAlias("pdvs", "pdvs");
		criteria.setProjection(Projections.distinct(Projections.id()));
		
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
		
		if(situacoesCadastro != null){
			criteria.add(Restrictions.in("situacaoCadastro", situacoesCadastro));
		}
		
	
		criteria.createAlias("pdvs.rotas", "rotaPdv");
	    criteria.createAlias("rotaPdv.rota", "rota");
		criteria.createAlias("rota.roteiro", "roteiro");
				
		
		if (idRoteiro != null){
			
			criteria.add(Restrictions.eq("roteiro.id", idRoteiro));
		}
		
		if (idRota != null){
			
			criteria.add(Restrictions.eq("rota.id", idRota));
		}
		
		criteria.addOrder(Order.asc("box.codigo"));
		criteria.addOrder(Order.asc("roteiro.ordem"));
		criteria.addOrder(Order.asc("roteiro.descricaoRoteiro"));
		criteria.addOrder(Order.asc("rota.ordem"));
		criteria.addOrder(Order.asc("rota.descricaoRota"));
		criteria.addOrder(Order.asc("numeroCota"));
		
		return criteria.list();
	}

	@Override
	public Integer obterDadosCotasComNotaEnvioEmitidasCount(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append( " select count(*) from ( ");
		
		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, true);
		
		sql.append(" ) rs1 ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 	 	
		
		montarParametrosFiltroNotasEnvio(filtro, query, true);	
		
		return ((BigInteger) query.uniqueResult()).intValue();
		
	}

	@Override
	public Integer obterDadosCotasComNotaEnvioAEmitirCount(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append( " select count(*) from ( ");
		
		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, true);
		
		sql.append(" ) rs1 ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 	 	
		
		montarParametrosFiltroNotasEnvio(filtro, query, true);	
		
		return ((BigInteger) query.uniqueResult()).intValue();
		
	}

	@Override
	public Integer obterDadosCotasComNotaEnvioEmitidasEAEmitirCount(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append( " select count(*) from ( ");
		
		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, true);
		sql.append(" union all ");
		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, true);
		
		sql.append(" ) rs1 ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 	 	
		
		montarParametrosFiltroNotasEnvio(filtro, query, true);	
		
		return ((BigInteger) query.uniqueResult()).intValue();
		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidas(FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();

		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, false);
		
		orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(),
				filtro.getPaginacaoVO().getSortOrder() == null? "":filtro.getPaginacaoVO().getSortOrder());
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		montarParametrosFiltroNotasEnvio(filtro, query, false);	
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
		
		return query.list();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioAEmitir(FiltroConsultaNotaEnvioDTO filtro) {
		
		StringBuilder sql = new StringBuilder();

		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, false);
		
		orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(),
				filtro.getPaginacaoVO().getSortOrder() == null? "":filtro.getPaginacaoVO().getSortOrder());
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		montarParametrosFiltroNotasEnvio(filtro, query, false);	
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNotaEnvioDTO> obterDadosCotasComNotaEnvioEmitidasEAEmitir(
			FiltroConsultaNotaEnvioDTO filtro) {

		StringBuilder sql = new StringBuilder();

		montarQueryCotasComNotasEnvioEmitidas(filtro, sql, false);
		sql.append( " union all ");
		montarQueryCotasComNotasEnvioNaoEmitidas(filtro, sql, false);
		
		orderByCotasComNotaEnvioEntre(sql, filtro.getPaginacaoVO().getSortColumn(),
				filtro.getPaginacaoVO().getSortOrder() == null? "":filtro.getPaginacaoVO().getSortOrder());
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		montarParametrosFiltroNotasEnvio(filtro, query, false);	
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaNotaEnvioDTO.class));
		
		return query.list();
		
	}
	
	private void montarQueryCotasComNotasEnvioEmitidas(FiltroConsultaNotaEnvioDTO filtro, StringBuilder sql, boolean isCount) {
		
		if(isCount) {
			sql.append( " select cota_.ID ");
		} else {
			sql.append( " select "
			+ "	        cota_.ID as idCota, "
			+ "	        cota_.NUMERO_COTA as numeroCota, "
			+ "	        coalesce(pessoa_cota_.nome,pessoa_cota_.razao_social) as nomeCota,  "
			+ "	        cota_.SITUACAO_CADASTRO as situacaoCadastro, "
			+ "	        SUM(coalesce(nei.reparte, 0)) as exemplares, "
			+ "	        SUM(coalesce(nei.reparte, 0) * pe_.PRECO_VENDA) as total, "
			+ "			case when count(nei.NOTA_ENVIO_ID)>0 then true else false end notaImpressa	");
		}
		sql.append( 
		  "	    from "
		+ "	        COTA cota_ " 
		+ "	    inner join "
		+ "	        BOX box1_  "
		+ "	            on cota_.BOX_ID=box1_.ID  "
		+ "	    inner join "
		+ "	        ESTUDO_COTA ec_  "
		+ "	            on cota_.ID=ec_.COTA_ID  "
		+ "	    inner join "
		+ "	        ESTUDO e_  "
		+ "	            on ec_.ESTUDO_ID=e_.ID  "
		+ "	    inner join "
		+ "	        LANCAMENTO lancamento_  "
		+ "	            on e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID  "
		+ "	            and e_.DATA_LANCAMENTO=lancamento_.DATA_LCTO_PREVISTA  "
		+ "	    inner join "
		+ "	        PRODUTO_EDICAO pe_  "
		+ "	            on e_.PRODUTO_EDICAO_ID=pe_.ID  "
		+ "	    inner join "
		+ "	        PRODUTO p_  "
		+ "	            on pe_.PRODUTO_ID=p_.ID  "
		+ "	    inner join "
		+ "	        PRODUTO_FORNECEDOR pf_  "
		+ "	            on p_.ID=pf_.PRODUTO_ID  "
		+ "	    inner join "
		+ "	        FORNECEDOR f_  "
		+ "	            on pf_.fornecedores_ID=f_.ID  "
		+ "	    inner join "
		+ "	        PDV pdv_  "
		+ "	            on cota_.ID=pdv_.COTA_ID  "
		+ "	     inner join "
		+ "        ROTA_PDV rota_pdv_  "
		+ "	            on pdv_.ID=rota_pdv_.PDV_ID    "  
		+ "	    inner join "
		+ "	        ROTA rota_  "
		+ "	            on rota_pdv_.rota_ID=rota_.ID  "
		+ "	    inner join "
		+ "	        ROTEIRO roteiro_  "
		+ "	            on rota_.ROTEIRO_ID=roteiro_.ID  "
		+ "	    inner join "
		+ "	        PESSOA pessoa_cota_  "
		+ "	            on cota_.PESSOA_ID=pessoa_cota_.ID  "
		+ "		inner join NOTA_ENVIO_ITEM nei " 
        + "    			on nei.ESTUDO_COTA_ID=ec_.ID "
		+ "	   where "
		+ "	        lancamento_.STATUS in (:status) ");
		
		if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
			sql.append(
			  "	        and ( "
			+ "	            f_.ID is null  "
			+ "	            or f_.ID in (:idFornecedores) "
			+ "	        )  ");
		}
		
		if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
			if (filtro.getIntervaloCota().getAte() != null) {
				
				sql.append("   and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
				
			} else {
				
				sql.append("   and cota_.NUMERO_COTA=:numeroCota ");
			}
	
		}
		
		if (filtro.getIntervaloBox() != null 
				&& filtro.getIntervaloBox().getDe() != null 
				&&  filtro.getIntervaloBox().getAte() != null) {
			sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
		}
		
		if (filtro.getIdRoteiro() != null){
			sql.append(" and roteiro_.ID=:idRoteiro  ");
		}
		
		if (filtro.getIdRota() != null){
			sql.append(" and rota_.ID=:idRota  ");
		}
		
		if (filtro.getIntervaloMovimento() != null 
				&& filtro.getIntervaloMovimento().getDe() != null 
				&& filtro.getIntervaloMovimento().getAte() != null) {
			sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
		}
		
		sql.append(
		  "	    group by cota_.ID ");
		
		
	}
		
	private void montarQueryCotasComNotasEnvioNaoEmitidas(FiltroConsultaNotaEnvioDTO filtro, StringBuilder sql, boolean isCount) {
		
		if(isCount) {
			sql.append( " select cota_.ID ");
		} else {
			sql.append( " select "
				+ "	        cota_.ID as idCota, "
				+ "	        cota_.NUMERO_COTA as numeroCota, "
				+ "	        coalesce(pessoa_cota_.nome,pessoa_cota_.razao_social) as nomeCota,  "
				+ "	        cota_.SITUACAO_CADASTRO as situacaoCadastro, "
				+ "	        SUM(ec_.QTDE_EFETIVA) as exemplares, "
				+ "	        SUM(ec_.QTDE_EFETIVA * pe_.PRECO_VENDA) as total, "
				+ "			case when count(nei.NOTA_ENVIO_ID)>0 then true else false end notaImpressa	"); 
		}
		sql.append( "   from "
				+ "	        COTA cota_ " 
				+ "	    inner join "
				+ "	        BOX box1_  "
				+ "	            on cota_.BOX_ID=box1_.ID  "
				+ "	    inner join "
				+ "	        ESTUDO_COTA ec_  "
				+ "	            on cota_.ID=ec_.COTA_ID  "
				+ "	    inner join "
				+ "	        ESTUDO e_  "
				+ "	            on ec_.ESTUDO_ID=e_.ID  "
				+ "	    inner join "
				+ "	        LANCAMENTO lancamento_  "
				+ "	            on e_.PRODUTO_EDICAO_ID=lancamento_.PRODUTO_EDICAO_ID  "
				+ "	            and e_.DATA_LANCAMENTO=lancamento_.DATA_LCTO_PREVISTA  "
				+ "	    inner join "
				+ "	        PRODUTO_EDICAO pe_  "
				+ "	            on e_.PRODUTO_EDICAO_ID=pe_.ID  "
				+ "	    inner join "
				+ "	        PRODUTO p_  "
				+ "	            on pe_.PRODUTO_ID=p_.ID  "
				+ "	    inner join "
				+ "	        PRODUTO_FORNECEDOR pf_  "
				+ "	            on p_.ID=pf_.PRODUTO_ID  "
				+ "	    inner join "
				+ "	        FORNECEDOR f_  "
				+ "	            on pf_.fornecedores_ID=f_.ID  "
				+ "	    inner join "
				+ "	        PDV pdv_  "
				+ "	            on cota_.ID=pdv_.COTA_ID  "
				+ "	     inner join "
				+ "        ROTA_PDV rota_pdv_  "
				+ "	            on pdv_.ID=rota_pdv_.PDV_ID    "  
				+ "	    inner join "
				+ "	        ROTA rota_  "
				+ "	            on rota_pdv_.rota_ID=rota_.ID  "
				+ "	    inner join "
				+ "	        ROTEIRO roteiro_  "
				+ "	            on rota_.ROTEIRO_ID=roteiro_.ID  "
				+ "	    inner join "
				+ "	        PESSOA pessoa_cota_  "
				+ "	            on cota_.PESSOA_ID=pessoa_cota_.ID  "
				+ "		left outer join NOTA_ENVIO_ITEM nei " 
		        + "    			on nei.ESTUDO_COTA_ID=ec_.ID "
				+ "	   where "
				+ "	        lancamento_.STATUS in (:status)  "
				+ "    and  nei.estudo_cota_id is null ");
				
				if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
					sql.append(
					  "	        and ( "
					+ "	            f_.ID is null  "
					+ "	            or f_.ID in (:idFornecedores) "
					+ "	        )  ");
				}
				
				if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
					if (filtro.getIntervaloCota().getAte() != null) {
						
						sql.append("   and cota_.NUMERO_COTA between :numeroCotaDe and :numeroCotaAte  ");
						
					} else {
						
						sql.append("   and cota_.NUMERO_COTA=:numeroCota ");
					}
			
				}
				
				if (filtro.getIntervaloBox() != null 
						&& filtro.getIntervaloBox().getDe() != null 
						&&  filtro.getIntervaloBox().getAte() != null) {
					sql.append(" and box1_.CODIGO between :boxDe and :boxAte  ");
				}
				
				if (filtro.getIdRoteiro() != null){
					sql.append(" and roteiro_.ID=:idRoteiro  ");
				}
				
				if (filtro.getIdRota() != null){
					sql.append(" and rota_.ID=:idRota  ");
				}
				
				if (filtro.getIntervaloMovimento() != null 
						&& filtro.getIntervaloMovimento().getDe() != null 
						&& filtro.getIntervaloMovimento().getAte() != null) {
					sql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR between :dataDe and :dataAte  ");
				}
				
				sql.append(
				  "	    group by cota_.ID ");
		
	}

	private void montarParametrosFiltroNotasEnvio(
			FiltroConsultaNotaEnvioDTO filtro, Query query, boolean isCount) {
		query.setParameterList("status", new String[]{StatusLancamento.BALANCEADO.name(), StatusLancamento.EXPEDIDO.name()});
		
		if (filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
			query.setParameterList("idFornecedores", filtro.getIdFornecedores());
		}
		
		
		if (filtro.getIntervaloCota() != null && filtro.getIntervaloCota().getDe() != null) {
			
			if (filtro.getIntervaloCota().getAte() != null) {
				query.setParameter("numeroCotaDe", filtro.getIntervaloCota().getDe());
				query.setParameter("numeroCotaAte", filtro.getIntervaloCota().getAte());
			} else {
				query.setParameter("numeroCota", filtro.getIntervaloCota().getDe());
			}
		}
		
		if (filtro.getIntervaloBox() != null 
				&& filtro.getIntervaloBox().getDe() != null 
				&&  filtro.getIntervaloBox().getAte() != null) {
			
			query.setParameter("boxDe", filtro.getIntervaloBox().getDe());
			query.setParameter("boxAte", filtro.getIntervaloBox().getAte());
		}
	
		if (filtro.getIdRoteiro() != null){
			
			query.setParameter("idRoteiro", filtro.getIdRoteiro());
		}
		
		if (filtro.getIdRota() != null){
			
			query.setParameter("idRota", filtro.getIdRota());
		}
		
		if (filtro.getIntervaloMovimento() != null 
				&& filtro.getIntervaloMovimento().getDe() != null 
				&& filtro.getIntervaloMovimento().getAte() != null) {
			query.setParameter("dataDe", filtro.getIntervaloMovimento().getDe());
			query.setParameter("dataAte",filtro.getIntervaloMovimento().getAte());
		}
		
		if(!isCount) {
			
			if (filtro.getPaginacaoVO().getPosicaoInicial()!= null){
				query.setFirstResult(filtro.getPaginacaoVO().getPosicaoInicial());
			}
			
			if (filtro.getPaginacaoVO().getQtdResultadosPorPagina()!= null){				
				query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
			}
			
		}
		
	}
	
	private void orderByCotasComNotaEnvioEntre(StringBuilder sql,
			String sortName, String sortOrder) {
		
		if("numeroCota".equals(sortName)) {
			sql.append(" order by numeroCota " + sortOrder +", notaImpressa ");
		}
		
		if("nomeCota".equals(sortName)) {
			sql.append(" order by  nomeCota " + sortOrder);
		}
				
		if("exemplares".equals(sortName)) {
			sql.append(" order by  exemplares " + sortOrder);
		}
		
		if("total".equals(sortName)) {
			sql.append(" order by  total " + sortOrder);
		}
		
		if("situacaoCadastro".equals(sortName)) {
			sql.append(" order by  cota_.SITUACAO_CADASTRO " + sortOrder);
		}
		
		if("notaImpressa".equals(sortName)) {
			sql.append(" order by  notaImpressa " + sortOrder);
		}
		
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<Cota> obterCotasPorFornecedor(Long idFornecedor) {
		
		String queryString = " select cota from Cota cota "
						   + " join fetch cota.fornecedores fornecedores "
						   + " where fornecedores.id = :idFornecedor ";
		
		Query query = this.getSession().createQuery(queryString);
		
		query.setParameter("idFornecedor", idFornecedor);
		
		return new HashSet<Cota>(query.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaTipoDTO> obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV tipoCota, Integer page, Integer rp, String sortname, String sortorder) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.id as idCota, ");
		hql.append(" cota.numeroCota as numCota, ");
		hql.append(" case pessoa.class ");
		hql.append(" when 'F' then pessoa.nome ");
		hql.append(" when 'J' then pessoa.razaoSocial end as nome,");
		hql.append(" endereco.cidade as municipio, ");
		hql.append(" endereco.logradouro || ', ' || endereco.numero || ' - ' || endereco.bairro || ' / ' || endereco.uf as endereco ");
		
		gerarWhereFromObterCotaPorTipo(hql);
		
		gerarOrderByObterCotaPorTipo(hql, sortname, sortorder);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoCota", tipoCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaTipoDTO.class));

		query.setFirstResult( (rp * page) - rp);
		
		query.setMaxResults(rp);
		
		return query.list();
	}
	
	private void gerarWhereFromObterCotaPorTipo(StringBuilder hql) {
		
		hql.append(" from Cota cota ");
		hql.append(" join cota.pessoa pessoa ");
		hql.append(" join cota.pdvs pdv ");
		hql.append(" join cota.enderecos enderecoCota ");
		hql.append(" join enderecoCota.endereco endereco ");
		
		hql.append(" where pdv.caracteristicas.pontoPrincipal=true ");
		hql.append(" and enderecoCota.principal=true ");		
		hql.append(" and pdv.segmentacao.tipoCaracteristica=:tipoCota");
				
	}

	private void gerarOrderByObterCotaPorTipo(StringBuilder hql,
			String sortname, String sortorder) {
		
		if(sortname == null || sortorder == null || sortname.isEmpty() || sortorder.isEmpty())
			return;
		
		if (sortname.equals("numCota")) {
			hql.append(" ORDER BY numCota ");
		} else if (sortname.equals("nome")) {
			hql.append(" ORDER BY nome ");
		} else if (sortname.equals("municipio")) {
			hql.append(" ORDER BY municipio ");
		} else if (sortname.equals("endereco")) {
			hql.append(" ORDER BY endereco ");
		}
		
		if(sortorder.equals("desc"))
			hql.append(" DESC ");
		else 
			hql.append(" ASC ");		
	}

	@Override
	public int obterCountCotaPorTipo(TipoCaracteristicaSegmentacaoPDV tipoCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cota.id) ");
		
		gerarWhereFromObterCotaPorTipo(hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoCota", tipoCota);
		
		return ((Long)query.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MunicipioDTO> obterQtdeCotaMunicipio(Integer page, Integer rp, String sortname, String sortorder) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select  ");
		hql.append(" 		endereco.cidade as municipio, ");
		hql.append(" 		count(cota.id) as qtde ");
				
		gerarWhereFromObterQtdeCotaMunicipio(hql);		
		
		hql.append(" group by endereco.cidade ");	
		
		gerarOrderByObterQtdeCotaMunicipio(hql, sortname, sortorder);
		
		Query query = this.getSession().createQuery(hql.toString());
				
		query.setResultTransformer(new AliasToBeanResultTransformer(MunicipioDTO.class));

		query.setFirstResult( (rp * page) - rp);
		
		query.setMaxResults(rp);
		
		return query.list();
	}

	private void gerarOrderByObterQtdeCotaMunicipio(StringBuilder hql,
			String sortname, String sortorder) {
		
		if(sortname == null || sortorder == null || sortname.isEmpty() || sortorder.isEmpty())
			return;
		
		if (sortname.equals("municipio")) {
			hql.append(" ORDER BY municipio ");
		} else if (sortname.equals("qtde")) {
			hql.append(" ORDER BY qtde ");
		} 
		
		if(sortorder.equals("desc"))
			hql.append(" DESC ");
		else 
			hql.append(" ASC ");	
		
	}
	
	private void gerarWhereFromObterQtdeCotaMunicipio(StringBuilder hql) {
		
		hql.append(" from Cota cota ");
		hql.append(" join cota.pessoa pessoa ");
		hql.append(" join cota.pdvs pdv ");
		hql.append(" join cota.enderecos enderecoCota ");
		hql.append(" join enderecoCota.endereco endereco ");
		
		hql.append(" where pdv.caracteristicas.pontoPrincipal=true ");
		hql.append(" and enderecoCota.principal=true ");
	}
	
	@Override
	public int obterCountQtdeCotaMunicipio() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(distinct endereco.cidade) ");
		
		gerarWhereFromObterQtdeCotaMunicipio(hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return ((Long)query.uniqueResult()).intValue();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCota obterHistoricoTitularidade(Long idCota,
            Long idHistorico) {
        Validate.notNull(idCota, "Identificador da cota não deve ser nulo!");
        Validate.notNull(idHistorico, "Identificador do histórico de titularidade não deve ser nulo!"); 
        String hql = "from HistoricoTitularidadeCota historico where historico.id = :idHistorico and historico.cota.id = :idCota";
        Query query = getSession().createQuery(hql);
        query.setParameter("idHistorico", idHistorico);
        query.setParameter("idCota", idCota);
        return (HistoricoTitularidadeCota) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCotaFormaPagamento obterFormaPagamentoHistoricoTitularidade(Long idFormaPagto) {
        Validate.notNull(idFormaPagto, "Identificador da forma de pagamento não deve ser nulo!");
        
        String hql = "from HistoricoTitularidadeCotaFormaPagamento where id = :id";
        Query query = getSession().createQuery(hql);
        query.setParameter("id", idFormaPagto);
        return (HistoricoTitularidadeCotaFormaPagamento) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCotaSocio obterSocioHistoricoTitularidade(Long idSocio) {
        Validate.notNull(idSocio, "Identificador do sócio não deve ser nulo!");
        
        String hql = "from HistoricoTitularidadeCotaSocio where id = :id";
        Query query = getSession().createQuery(hql);
        query.setParameter("id", idSocio);
        return (HistoricoTitularidadeCotaSocio) query.uniqueResult();
    }

    @Override
	public void ativarCota(Integer numeroCota) {
		
		Query query = 
				this.getSession().createQuery(
						"update Cota set situacaoCadastro = :status where numeroCota = :numeroCota");
		
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("status", SituacaoCadastro.ATIVO);
		
		query.executeUpdate();
	}	
    
    @Override
    public Cota buscarCotaPorID(Long idCota) {
    	
    	String queryString = " select cota from Cota cota "
				   + " join fetch cota.fornecedores fornecedores "
				   + " where cota.id = :idCota ";

		Query query = this.getSession().createQuery(queryString);
		
		query.setParameter("idCota", idCota);
		
		return (Cota) query.uniqueResult();
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasQuePossuemRangeReparte(BigInteger qtdReparteInicial, BigInteger qtdReparteFinal, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.lancamentos as lancamento ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and ");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
			// Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

			// Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		if (qtdReparteInicial != null && qtdReparteInicial.intValue() >= 0 && qtdReparteFinal != null && qtdReparteFinal.intValue() >= 0 ) {
			hql.append(" HAVING avg(lancamento.reparte) between :reparteInicial and :reparteFinal");
			parameters.put("reparteInicial", qtdReparteInicial.doubleValue());
			parameters.put("reparteFinal", qtdReparteFinal.doubleValue());
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	private void setParameters(Query query, Map<String, Object> parameters) {
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasQuePossuemRangeVenda(BigInteger qtdVendaInicial, BigInteger qtdVendaFinal,List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
			// Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

			// Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		if (qtdVendaInicial != null && qtdVendaInicial.intValue() >= 0 && qtdVendaFinal != null && qtdVendaFinal.intValue() >= 0 ) {
			hql.append(" HAVING avg(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) between :qtdVendaInicial and :qtdVendaFinal");
			parameters.put("qtdVendaInicial", qtdVendaInicial.doubleValue());
			parameters.put("qtdVendaFinal", qtdVendaFinal.doubleValue());
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasQuePossuemPercentualVendaSuperior(BigDecimal percentualVenda, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
			// Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

			// Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		if (percentualVenda != null && percentualVenda.doubleValue() >= 0) {
			hql.append(" HAVING (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) / sum(estoqueProdutoCota.qtdeRecebida))>= (:percentualVenda / 100)");
			parameters.put("percentualVenda", percentualVenda.intValue());
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasPorNomeOuNumero(CotaDTO cotaDto, List<ProdutoEdicaoDTO> listProdutoEdicaoDto, boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (cotaDto != null ) {
			if (cotaDto.getNumeroCota() != null && !cotaDto.getNumeroCota().equals(0)) {
				hql.append(" cota.numeroCota = :numeroCota and ");
				parameters.put("numeroCota", cotaDto.getNumeroCota());
			}
			else if (cotaDto.getNomePessoa() != null && !cotaDto.getNomePessoa().isEmpty()) {
				hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa and ");
				parameters.put("nomePessoa", cotaDto.getNomePessoa());
			}
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
			// Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

			// Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> buscarCotasPorComponentes(ComponentesPDV componente, String elemento, List<ProdutoEdicaoDTO> listProdutoEdicaoDto,	boolean cotasAtivas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		StringBuilder whereParameter = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		switch (componente) {
		case TipoPontodeVenda:
			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.segmentacao as segmentacao ");
			hql.append(" LEFT JOIN segmentacao.tipoPontoPDV tipoPontoPDV ");

			whereParameter.append(" tipoPontoPDV.id = :codigoTipoPontoPDV AND");
			parameters.put("codigoTipoPontoPDV", Long.parseLong(elemento));

			break;
		case Area_de_Influência:

			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.segmentacao as segmentacao ");
			hql.append(" LEFT JOIN segmentacao.areaInfluenciaPDV areaInfluenciaPDV ");

			whereParameter.append(" areaInfluenciaPDV.id = :codigoAreaInfluenciaPDV AND ");
			parameters.put("codigoAreaInfluenciaPDV", Long.parseLong(elemento));
			break;

		case Bairro:

			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.enderecos enderecosPdv ");
			hql.append(" LEFT JOIN enderecosPdv.endereco endereco ");
			
			whereParameter.append(" enderecosPdv.principal = true and endereco.bairro = :bairroPDV AND ");
			parameters.put("bairroPDV", elemento);

			break;
		case Distrito:
			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.enderecos enderecosPdv ");
			hql.append(" LEFT JOIN enderecosPdv.endereco endereco ");

			whereParameter.append(" enderecosPdv.principal = true and endereco.uf = :ufSigla AND");
			parameters.put("ufSigla", elemento);

			break;
		case GeradorDeFluxo:

			hql.append(" LEFT JOIN cota.pdvs pdvs ");
			hql.append(" LEFT JOIN pdvs.geradorFluxoPDV geradorFluxoPdvs ");
			hql.append(" INNER JOIN geradorFluxoPdvs.principal geradorPrincipal ");

			whereParameter.append(" geradorPrincipal.id = :idGeradorFluxoPDV AND ");
			parameters.put("idGeradorFluxoPDV",	Long.parseLong(elemento));

			break;
		case CotasAVista:
			hql.append(" LEFT JOIN cota.parametroCobranca as parametroCobranca ");
			
			whereParameter.append(" parametroCobranca.tipoCota = :tipoCota AND");
			parameters.put("tipoCota",TipoCota.A_VISTA);
			
			break;
		case CotasNovasRetivadas:
			whereParameter.append(" cota.id in (SELECT cotaBase.cota.id FROM CotaBase as cotaBase) AND ");
			
			break;
		case Região:
			whereParameter.append(" cota.id in (SELECT registro.cota.id FROM RegistroCotaRegiao as registro WHERE regiao.id = :regiaoId) AND ");
			parameters.put("regiaoId",Long.parseLong(elemento));
			
			break;
		default:
			break;
		}
		
		
		hql.append(" WHERE ");
		
		// adiciona os parâmetros feitos dentro so switch
		hql.append(whereParameter.toString());
		
		if (cotasAtivas) {
			hql.append(" cota.situacaoCadastro = :statusCota and");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
			// Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

			// Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnaliseHistoricoDTO> buscarHistoricoCotas(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, List<Cota> cotas) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" cota.situacaoCadastro as statusCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa, ");
		hql.append(" count(DISTINCT pdvs) as qtdPdv, ");
		hql.append(" avg(movimentos.qtde) as reparteMedio, ");
		hql.append(" avg(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) as vendaMedia ");
		
		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.movimentos as movimentos ");
		hql.append(" LEFT JOIN movimentos.tipoMovimento as tipoMovimento");
		hql.append(" LEFT JOIN produtoEdicao.produto as produto ");
		hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pdvs as pdvs ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		hql.append(" tipoMovimento.id = 21 and ");
		
		if (listProdutoEdicaoDto != null && listProdutoEdicaoDto.size() != 0) {
			
			// Populando o in ('','') do código produto
			hql.append(" produto.codigo in ( ");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append( "'" + listProdutoEdicaoDto.get(i).getCodigoProduto() + "'");
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");

			// Populando o in ('','') do numero Edição
			hql.append(" and produtoEdicao.numeroEdicao in (");
			for (int i = 0; i < listProdutoEdicaoDto.size(); i++) {
				
				hql.append(listProdutoEdicaoDto.get(i).getNumeroEdicao());
				
				if (listProdutoEdicaoDto.size() != i + 1) {
					hql.append(",");
				}
			}
			
			hql.append(")");
		}
		
		if (cotas != null && cotas.size() != 0) {
			
			// Populando o in ('','') do código produto
			hql.append(" and cota.numeroCota in ( ");
			for (int i = 0; i < cotas.size(); i++) {
				
				hql.append(cotas.get(i).getNumeroCota());
				
				if (cotas.size() != i + 1) {
					hql.append(","); 
				}
			}
			
			hql.append(" )");
		}
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseHistoricoDTO.class));
		
		return query.list();
	}

	@Override
	public HistoricoVendaPopUpCotaDto buscarCota(Integer numero) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" rankingFaturamento.id as rankId, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa, ");
		hql.append(" cota.tipoDistribuicaoCota as tipoDistribuicaoCota, ");
		hql.append(" rankingFaturamento.faturamento as faturamento, ");
		hql.append(" max(rankingFaturamentoGerado.dataGeracao) as  dataGeracao ");
		
		hql.append(" FROM RankingFaturamento rankingFaturamento ");
		hql.append(" INNER JOIN rankingFaturamento.rankingFaturamentoGerado as rankingFaturamentoGerado ");
		hql.append(" RIGHT JOIN rankingFaturamento.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		hql.append(" cota.numeroCota = ");
		hql.append(numero);
		
		hql.append(" GROUP BY rankingFaturamento.cota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HistoricoVendaPopUpCotaDto.class));
		
		return (HistoricoVendaPopUpCotaDto) query.uniqueResult();
	
	}

}
