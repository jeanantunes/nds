package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.util.comparators.CurvaABCParticipacaoAcumuladaComparator;
import br.com.abril.nds.client.util.comparators.CurvaABCParticipacaoComparator;
import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABC;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.repository.DistribuidorRepository;

@Repository
public class DistribuidorRepositoryImpl extends
		AbstractRepository<Distribuidor, Long> implements
		DistribuidorRepository {

	public DistribuidorRepositoryImpl() {
		super(Distribuidor.class);
	}

	@Override
	public Distribuidor obter() {
		String hql = "from Distribuidor";
		Query query = getSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Distribuidor> distribuidores = query.list();
		return distribuidores.isEmpty() ? null : distribuidores.get(0);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
															Collection<Long> idsForncedores,
															OperacaoDistribuidor operacaoDistribuidor) {

		StringBuilder hql =
			new StringBuilder("from DistribuicaoFornecedor where fornecedor.id in (:idsFornecedores) ");

		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("idsFornecedores", idsForncedores);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoDistribuidor> buscarDiasDistribuicaoDistribuidor(
															Long idDistruibuidor,
															OperacaoDistribuidor operacaoDistribuidor) {

		StringBuilder hql = new StringBuilder();

		hql.append("from DistribuicaoDistribuidor ");
		hql.append("where distribuidor.id = :idDistribuidor ");
		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idDistribuidor", idDistruibuidor);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterEnderecoPrincipal()
	 */
	@Override
	public EnderecoDistribuidor obterEnderecoPrincipal(){
		Criteria criteria=  getSession().createCriteria(EnderecoDistribuidor.class);
		criteria.add(Restrictions.eq("principal", true) );
		criteria.setMaxResults(1);

		return (EnderecoDistribuidor) criteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoGarantia> obtemTiposGarantiasAceitas() {
		Criteria criteria =  getSession().createCriteria(TipoGarantiaAceita.class);
		criteria.setProjection(Projections.property("tipoGarantia"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultadoCurvaABC obterCurvaABCDistribuidorTotal(FiltroCurvaABCDistribuidorDTO filtro){
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(ResultadoCurvaABC.class.getCanonicalName())
		.append(" ( (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ) ");

		hql.append(getWhereQueryObterCurvaABCDistribuidor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCDistribuidor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		return (ResultadoCurvaABC) query.list().get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(RegistroCurvaABCDistribuidorVO.class.getCanonicalName())
		.append(" ( estoqueProdutoCota.cota.numeroCota , ")
		.append("   case when (pessoa.nome is not null) then ( pessoa.nome ) ")
		.append("     when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial ) ")
		.append("     else null end , ")
		.append("   case when sum(pdv) is null then 0 else sum(pdv) end, " )
		.append("   endereco.cidade, " )
		.append("   (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ) ");

		hql.append(getWhereQueryObterCurvaABCDistribuidor(filtro));
		hql.append(getGroupQueryObterCurvaABCDistribuidor(filtro));
		hql.append(getOrderQueryObterCurvaABCDistribuidor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCDistribuidor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}

		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return getOrderObterCurvaABCDistribuidor(complementarCurvaABCDistribuidor((List<RegistroCurvaABCDistribuidorVO>) query.list()), filtro);

	}

	private String getWhereQueryObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM EstoqueProdutoCota AS estoqueProdutoCota ")
		.append(" LEFT JOIN estoqueProdutoCota.movimentos AS movimentos ")
		.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao.produto.fornecedores AS fornecedores ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.enderecos AS enderecos ")
		.append(" LEFT JOIN enderecos.endereco AS endereco ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.pdvs AS pdv ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.pessoa AS pessoa ");

		hql.append("WHERE movimentos.data BETWEEN :dataDe AND :dataAte ");
		hql.append(" AND enderecos.principal IS TRUE ");

		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedores.id = :codigoFornecedor ");
			//hql.append("and produtoEdicao.produto.fornecedores.id = :codigoFornecedor ");
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.produto.codigo = :codigoProduto ");
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.produto.nome = :nomeProduto ");
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

	private String getGroupQueryObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" GROUP BY estoqueProdutoCota.cota.numeroCota, ")
		.append("   CASE WHEN (pessoa.nome is not null) THEN ( pessoa.nome ) ")
		.append("     WHEN (pessoa.razaoSocial is not null) THEN ( pessoa.razaoSocial ) ")
		.append("     ELSE null END ");

		return hql.toString();
	}


	private String getOrderQueryObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case COTA:
					hql.append(" order by estoqueProdutoCota.cota.numeroCota ");
					break;
				case NOME:
					hql.append(" order by case when (pessoa.nome is not null) then ( pessoa.nome ) ")
					.append("     when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial ) ")
					.append("     else null end ");
					break;
				case MUNICIPIO:
					hql.append(" order by endereco.cidade ");
					break;
				case QTDEPDV:
					hql.append(" order by case when sum(pdv) is null then 0 else sum(pdv) end ");
					break;
				case VENDA_EXEMPLARES:
					hql.append(" order by (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) ");
					break;
				case FATURAMENTO:
					hql.append(" order by ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ");
					break;
				default:
					hql.append(" order by estoqueProdutoCota.cota.numeroCota ");
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}

		return hql.toString();
	}

	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> getParametrosObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro){

		HashMap<String,Object> param = new HashMap<String, Object>();

		param.put("dataDe",  filtro.getDataDe());
		param.put("dataAte", filtro.getDataAte());

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

		if (filtro.getCodigoCota() != null && !filtro.getCodigoCota().isEmpty()) {
			param.put("codigoCota", filtro.getCodigoCota().toString());
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			param.put("nomeCota", filtro.getNomeCota());
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			param.put("municipio", filtro.getMunicipio());
		}

		return param;
	}

	private List<RegistroCurvaABCDistribuidorVO> complementarCurvaABCDistribuidor(List<RegistroCurvaABCDistribuidorVO> lista) {

		BigDecimal participacaoTotal = new BigDecimal(0);

		// Soma todos os valores de participacao
		for (RegistroCurvaABCDistribuidorVO registro : lista) {
			participacaoTotal.add(registro.getFaturamentoCapa());
		}

		BigDecimal participacaoRegistro = new BigDecimal(0);
		BigDecimal participacaoAcumulada = new BigDecimal(0);

		RegistroCurvaABCDistribuidorVO registro = null;

		// Verifica o percentual dos valores em relação ao total de participacao
		for (int i=0; i<lista.size(); i++) {

			registro = (RegistroCurvaABCDistribuidorVO) lista.get(i);

			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamentoCapa().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			registro.setParticipacao(participacaoRegistro);

			participacaoAcumulada.add(participacaoRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);

			// Substitui o registro pelo registro atualizado (com participacao total)
			lista.set(i, registro);

		}

		return lista;
	}

	@SuppressWarnings("unchecked")
	private List<RegistroCurvaABCDistribuidorVO> getOrderObterCurvaABCDistribuidor(List<RegistroCurvaABCDistribuidorVO> lista, FiltroCurvaABCDistribuidorDTO filtro) {

		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case PARTICIPACAO:
					Collections.sort(lista, new CurvaABCParticipacaoComparator());
				case PARTICIPACAO_ACUMULADA:
					Collections.sort(lista, new CurvaABCParticipacaoAcumuladaComparator());
				default:
					break;
			}
		}
		return lista;
	}

}
