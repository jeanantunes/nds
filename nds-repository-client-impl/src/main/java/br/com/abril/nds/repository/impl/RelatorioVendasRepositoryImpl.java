package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RelatorioVendasRepository;

@Repository
public class RelatorioVendasRepositoryImpl extends AbstractRepositoryModel<Distribuidor, Long> implements RelatorioVendasRepository {
	
	public RelatorioVendasRepositoryImpl() {
		super(Distribuidor.class);
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterCurvaABCDistribuidor(br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append("   cota.ID AS idCota, ");
		hql.append("   cota.NUMERO_COTA AS numeroCota, ");
		hql.append("   COALESCE(pessoa.NOME, pessoa.RAZAO_SOCIAL) AS nomeCota, ");
		  
		hql.append("   endereco.CIDADE AS municipio, ");
		hql.append("   (SELECT COUNT(*) FROM PDV pdv WHERE pdv.COTA_ID = cota.ID) AS quantidadePdvs, ");
		  
		hql.append("   SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   		END ");
		hql.append("   ) AS vendaExemplares, ");
		  
		hql.append("   SUM(CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 		END ");
		hql.append("   ) * produtoEdicao.PRECO_VENDA AS faturamentoCapa ");
		
		hql.append(this.getWhereQueryObterCurvaABCDistribuidor(filtro, param));
		hql.append(this.getGroupQueryObterCurvaABCDistribuidor(filtro));
		
		hql.append(" ORDER BY faturamentoCapa, numeroCota ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("numeroCota");
		query.addScalar("nomeCota");
		query.addScalar("municipio");
		query.addScalar("quantidadePdvs", StandardBasicTypes.INTEGER);
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);

		for(String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}
		
		query.setParameterList("grupoMovimentoEstoque", this.obterGruposMovimentoEstoqueCota());
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCDistribuidorVO.class));
		
		return query.list();
	}

	private List<String> obterGruposMovimentoEstoqueCota() {
		
		List<String> gruposMovimentoEstoque = new ArrayList<>();
		
		for (GrupoMovimentoEstoque grupoMovimentoEstoque : GrupoMovimentoEstoque.values()) {
			
			if (grupoMovimentoEstoque.getDominio().equals(Dominio.COTA)) {
				
				gruposMovimentoEstoque.add(grupoMovimentoEstoque.name());
			}
		}
		
		return gruposMovimentoEstoque;
	}

	/**
	 * Retorna as tabelas, joins e filtros da Query de seleção do relatório de vendas
	 * @param filtro
	 * @return
	 */
	private String getWhereQueryObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro, HashMap<String,Object> param) {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM ");
		hql.append("     MOVIMENTO_ESTOQUE_COTA movimentoEstoqueCota ");
		hql.append(" INNER JOIN ");
		hql.append(" 	  TIPO_MOVIMENTO tipoMovimento ");
		hql.append(" 			ON movimentoEstoqueCota.TIPO_MOVIMENTO_ID = tipoMovimento.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_EDICAO produtoEdicao ");          
		hql.append("         ON movimentoEstoqueCota.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO produto ");
		hql.append("         ON produtoEdicao.PRODUTO_ID = produto.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO_FORNECEDOR produtoFornecedor ");          
		hql.append("         ON produto.ID = produtoFornecedor.PRODUTO_ID ");  
		hql.append(" INNER JOIN ");
		hql.append("     FORNECEDOR fornecedor ");          
		hql.append("         ON produtoFornecedor.fornecedores_ID = fornecedor.ID ");
		hql.append(" INNER JOIN ");
		hql.append(" 	  COTA cota ");
		hql.append(" 	  		ON movimentoEstoqueCota.COTA_ID = cota.ID ");
		hql.append(" INNER JOIN ");
		hql.append(" 	  PESSOA pessoa ");
		hql.append(" 	  		ON pessoa.ID = cota.PESSOA_ID ");
		hql.append(" INNER JOIN ");
		hql.append(" 	  ENDERECO_COTA enderecoCota ");
		hql.append(" 	  		ON enderecoCota.COTA_ID = cota.ID AND enderecoCota.principal = true ");
		hql.append(" INNER JOIN ");
		hql.append(" 	  ENDERECO endereco ");
		hql.append(" 			ON enderecoCota.ENDERECO_ID = endereco.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     LANCAMENTO lancamento ");
		hql.append("         ON lancamento.ID = ( ");
		hql.append("         	CASE WHEN (produtoEdicao.PARCIAL) ");
		hql.append(" 					THEN (SELECT ID FROM LANCAMENTO WHERE PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		hql.append(" 								ORDER BY ID ASC LIMIT 1) ");
		hql.append(" 					ELSE (SELECT ID FROM LANCAMENTO WHERE PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		hql.append(" 								ORDER BY ID DESC LIMIT 1) ");
		hql.append(" 				END ");
		hql.append(" 	  		) ");
		hql.append("  LEFT JOIN ");
		hql.append("  	  FECHAMENTO_ENCALHE fechamentoEncalhe ");
		hql.append(" 			ON (fechamentoEncalhe.DATA_ENCALHE = lancamento.DATA_REC_DISTRIB ");
		hql.append(" 				AND fechamentoEncalhe.PRODUTO_EDICAO_ID = produtoEdicao.ID) ");
	    
		hql.append("  WHERE ");
		hql.append("     tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN ( ");
		hql.append(" 	  		:grupoMovimentoEstoque ");
		hql.append("     ) ");
		
		hql.append("     AND movimentoEstoqueCota.DATA BETWEEN :dataDe AND :dataAte ");

		param.put("dataDe",  filtro.getDataDe());
		param.put("dataAte", filtro.getDataAte());
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedor.ID = :codigoFornecedor ");
		
			param.put("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append(" AND produto.CODIGO = :codigoProduto ");
			
			param.put("codigoProduto", filtro.getCodigoProduto().toString());
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append(" AND produtoEdicao.NUMERO_EDICAO = :edicaoProduto ");
			
			param.put("edicaoProduto", filtro.getCodigoProduto().toString());
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			hql.append(" AND produto.EDITOR_ID = :codigoEditor ");
		
			param.put("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
		}

		if (filtro.getCodigoCota() != null) {
			hql.append(" AND cota.NUMERO_COTA = :codigoCota ");
			
			param.put("codigoCota", filtro.getCodigoCota());
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			hql.append("AND endereco.CIDADE = :municipio ");
		
			param.put("municipio", filtro.getMunicipio());
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

		hql.append(" GROUP BY cota.ID ");
 
		return hql.toString();
	}

}
