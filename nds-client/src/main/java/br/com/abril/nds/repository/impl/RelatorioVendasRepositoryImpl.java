package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCDistribuidor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.RelatorioVendasRepository;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

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
		   .append(" ( ")
		   .append("   case when (lancamento.status in (:statusLancamentoRecolhido) ) then ( ")
		   .append("  			sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) ")
		   .append(" 		else 0 end, ")
		   .append("   case when (lancamento.status in (:statusLancamentoRecolhido) ) then ( ")
		   .append("   			sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (movimentos.valoresAplicados.precoComDesconto)) ) ")
		   .append(" 		else 0 end ")
		   .append(" ) ");

		hql.append(getWhereQueryObterCurvaABCDistribuidor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCDistribuidor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}

		query.setParameterList("statusLancamentoRecolhido", Arrays.asList(StatusLancamento.RECOLHIDO,StatusLancamento.FECHADO));
		
		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			query.setParameterList("edicaoProduto", (filtro.getEdicaoProduto()));
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
		
		hql.append("  SELECT cota.numeroCota as numeroCota, ")
		.append("   case when (pessoa.nome is not null) then ( pessoa.nome ) ")
		.append("     when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial ) ")
		.append("     else null end as nomeCota, ")
		.append("   case when pdv is null then 0 else count(distinct pdv.id) end as quantidadePdvs, " )
		.append("   endereco.cidade as municipio, " )
		.append("   case when (lancamento.status in (:statusLancamentoRecolhido) ) then ( ")
		.append(" 		   sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) ")
		.append(" 		   else 0 end, ")
		.append("   case when (lancamento.status in (:statusLancamentoRecolhido)) then ( ")
		.append("          sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (movimentos.valoresAplicados.precoComDesconto)) ) ")
		.append("          else 0 end as faturamentoCapa, ")
		.append("  estoqueProdutoCota.produtoEdicao.produto.id ,")
		.append("  estoqueProdutoCota.cota.id )");
		hql.append(getWhereQueryObterCurvaABCDistribuidor(filtro));
		hql.append(getGroupQueryObterCurvaABCDistribuidor(filtro));
		
		hql.append(" order by faturamentoCapa, numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCDistribuidor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setParameterList("statusLancamentoRecolhido", Arrays.asList(StatusLancamento.RECOLHIDO,StatusLancamento.FECHADO));
		
		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			query.setParameterList("edicaoProduto", (filtro.getEdicaoProduto()));
		}
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCDistribuidorVO.class));
		
		return complementarCurvaABCDistribuidor((List<RegistroCurvaABCDistribuidorVO>) query.list());

	}

	/**
	 * Retorna as tabelas, joins e filtros da Query de seleção do relatório de vendas
	 * @param filtro
	 * @return
	 */
	private String getWhereQueryObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM EstoqueProdutoCota estoqueProdutoCota ")
		.append(" JOIN estoqueProdutoCota.cota cota ")
		.append(" LEFT JOIN estoqueProdutoCota.movimentos movimentos ")
		.append(" LEFT JOIN movimentos.lancamento lancamento ")
		.append(" JOIN estoqueProdutoCota.produtoEdicao produtoEdicao ")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.editor editor ")
		.append(" JOIN produto.fornecedores fornecedores ")
		.append(" LEFT JOIN cota.enderecos enderecos ")
		.append(" LEFT JOIN enderecos.endereco endereco ")
		.append(" LEFT JOIN cota.pdvs pdv ")
		.append(" JOIN cota.pessoa pessoa ");

		hql.append("WHERE movimentos.data BETWEEN :dataDe AND :dataAte ");
		hql.append(" AND enderecos.principal IS TRUE ");

		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedores.id = :codigoFornecedor ");
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append("AND produto.codigo = :codigoProduto ");
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			hql.append("AND upper(produto.nome) like upper( :nomeProduto )");
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append("AND produtoEdicao.numeroEdicao in( :edicaoProduto )");
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			hql.append("AND editor.codigo = :codigoEditor ");
		}

		if (filtro.getCodigoCota() != null) {
			hql.append("AND cota.numeroCota = :codigoCota ");
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append("AND  upper(pessoa.nome) like upper (:nomeCota ) or upper(pessoa.razaoSocial) like upper ( :nomeCota ) ");
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

		hql.append(" GROUP BY cota.numeroCota, ")
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
			param.put("nomeProduto", filtro.getNomeProduto()+ "%");
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			param.put("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
		}

		if (filtro.getCodigoCota() != null ) {
			param.put("codigoCota", filtro.getCodigoCota());
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			param.put("nomeCota", filtro.getNomeCota()+ "%");
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

		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if (lista==null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		// Soma todos os valores de participacao
		for (RegistroCurvaABCDistribuidorVO registro : lista) {
			if (registro.getFaturamentoCapa()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamentoCapa());
			}
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		BigDecimal participacaoAcumulada = new BigDecimal(0);
		
		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroCurvaABCDistribuidorVO registro : lista) {
			
			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamentoCapa().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			
			participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
			
			registro.setParticipacao(participacaoRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);
		}

		return lista;
	}

}
