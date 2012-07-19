package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCDistribuidor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.repository.RelatorioVendasRepository;

@Repository
public class RelatorioVendasRepositoryImpl extends AbstractRepositoryModel<Distribuidor, Long> implements RelatorioVendasRepository {

	public RelatorioVendasRepositoryImpl() {
		super(Distribuidor.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterCurvaABCDistribuidorTotal(br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO)
	 */
	@Override
	public ResultadoCurvaABCDistribuidor obterCurvaABCDistribuidorTotal(FiltroCurvaABCDistribuidorDTO filtro) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(ResultadoCurvaABCDistribuidor.class.getCanonicalName())
		.append(" ( (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ) ");

		hql.append(getWhereQueryObterCurvaABCDistribuidor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCDistribuidor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		return (ResultadoCurvaABCDistribuidor) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterCurvaABCDistribuidor(br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO)
	 */
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

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCDistribuidor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}

		return complementarCurvaABCDistribuidor((List<RegistroCurvaABCDistribuidorVO>) query.list());

	}

	/**
	 * Retorna as tabelas, joins e filtros da Query de seleção do relatório de vendas
	 * @param filtro
	 * @return
	 */
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

	/**
	 * Retorna o agrupamento das pesquisas do relatório de vendas
	 * @param filtro
	 * @return
	 */
	private String getGroupQueryObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" GROUP BY estoqueProdutoCota.cota.numeroCota, ")
			.append("   CASE WHEN (pessoa.nome is not null) THEN ( pessoa.nome ) ")
			.append("     WHEN (pessoa.razaoSocial is not null) THEN ( pessoa.razaoSocial ) ")
			.append("     ELSE null END ");
 
		return hql.toString();
	}

	/**
	 * Popula os parametros do relatório.
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

	/**
	 * Insere os registros de participação e participação acumulada no resultado da consulta HQL
	 * @param lista
	 * @return
	 */
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

}
