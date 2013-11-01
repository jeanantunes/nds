package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RelatorioVendasRepository;

@Repository
public class RelatorioVendasRepositoryImpl extends AbstractRepositoryModel<Distribuidor, Long> implements RelatorioVendasRepository {
	
	public RelatorioVendasRepositoryImpl() {
		super(Distribuidor.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append("   produto.ID AS idProduto, ");
		hql.append("   cota.ID AS idCota, ");
		hql.append("   cota.NUMERO_COTA AS numeroCota, ");
		hql.append("   COALESCE(pessoa.NOME, pessoa.RAZAO_SOCIAL) AS nomeCota, ");
		  
		hql.append("   endereco.CIDADE AS municipio, ");
		
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   		END ");
		hql.append("   ) AS vendaExemplares, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 		END ");
		hql.append("   ) * produtoEdicao.PRECO_VENDA AS faturamentoCapa ");
		
		hql.append(this.getFromWhereObterCurvaABC());
		hql.append(this.getFiltroCurvaABCDistribuidor(filtro, null));
		
		hql.append(" GROUP BY movimentoEstoqueCota.ID ");
		
		hql.append(" ORDER BY cota.ID ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("idProduto", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		query.addScalar("municipio", StandardBasicTypes.STRING);
		
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);

		this.getFiltroCurvaABCDistribuidor(filtro, query);
		
		query.setParameterList("grupoMovimentoEstoque", this.obterGruposMovimentoEstoqueCota());
		query.setParameterList("tiposLancamento", this.obterTiposLancamentosRelatorioVenda());
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCDistribuidorVO.class));
		
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append("   editor.ID AS codigoEditor, ");
		hql.append("   pessoaEditor.RAZAO_SOCIAL AS nomeEditor, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN movimentoEstoqueCota.QTDE ");
		hql.append("   			ELSE 0 ");
		hql.append("   		END ");
		hql.append("   ) AS reparte, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   		END ");
		hql.append("   ) AS vendaExemplares, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   		END ");
		hql.append("   ) * 100 / ");
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN movimentoEstoqueCota.QTDE ");
		hql.append("   			ELSE 0 ");
		hql.append("   		END ");
		hql.append("   ) AS porcentagemVendaExemplares, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 		END ");
		hql.append("   ) * produtoEdicao.PRECO_VENDA AS faturamentoCapa, ");
		
		hql.append("   ( ");
		hql.append(" 	  (movimentoEstoqueCota.PRECO_COM_DESCONTO - (produtoEdicao.PRECO_VENDA ");
		hql.append(" 	  		- (produtoEdicao.PRECO_VENDA * COALESCE(descontoLogistica.PERCENTUAL_DESCONTO, 0) / 100) ) ");
		hql.append(" 			) ");
		hql.append(" 			* (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append(" 			  		THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 					ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 				END) ");
		hql.append("   ) AS valorMargemDistribuidor, ");
		  
		hql.append("   (( ");
		hql.append(" 	  (produtoEdicao.PRECO_VENDA ");
		hql.append(" 	  		- (produtoEdicao.PRECO_VENDA * COALESCE(descontoLogistica.PERCENTUAL_DESCONTO, 0) / 100) ");
		hql.append(" 			- movimentoEstoqueCota.PRECO_COM_DESCONTO) ");
		hql.append(" 			* (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append(" 			  		THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 					ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 				END) ");
		hql.append("   ) / ");
		hql.append("   ((CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 		END ");
		hql.append("   ) * produtoEdicao.PRECO_VENDA)) * 100 ");
		
		
		hql.append(" AS porcentagemMargemDistribuidor ");
		
		hql.append(this.getFromWhereObterCurvaABC());
		hql.append(this.getFiltroCurvaABCEditor(filtro, null));
		
		hql.append(" GROUP BY movimentoEstoqueCota.ID ");
		
		hql.append(" ORDER BY produto.EDITOR_ID ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.addScalar("codigoEditor", StandardBasicTypes.LONG);
		query.addScalar("nomeEditor", StandardBasicTypes.STRING);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("porcentagemVendaExemplares", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("porcentagemMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);

		this.getFiltroCurvaABCEditor(filtro, query);
		
		query.setParameterList("grupoMovimentoEstoque", this.obterGruposMovimentoEstoqueCota());
		query.setParameterList("tiposLancamento", this.obterTiposLancamentosRelatorioVenda());
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCEditorVO.class));
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append("   produtoEdicao.ID AS idProdutoEdicao, ");
		hql.append("   produto.CODIGO AS codigoProduto, ");
		hql.append("   produto.NOME AS nomeProduto, ");
		hql.append("   produtoEdicao.NUMERO_EDICAO AS edicaoProduto, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN movimentoEstoqueCota.QTDE ");
		hql.append("   			ELSE 0 ");
		hql.append("   		END ");
		hql.append("   ) AS reparte, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   		END ");
		hql.append("   ) AS vendaExemplares, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append("   		END "); 
		hql.append("   ) * 100 / ");
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN movimentoEstoqueCota.QTDE ");
		hql.append("   			ELSE 0 ");
		hql.append("   		END ");
		hql.append("   ) AS porcentagemVenda, ");
		  
		hql.append("   (CASE WHEN (tipoMovimento.OPERACAO_ESTOQUE = 'ENTRADA') ");
		hql.append("   			THEN (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 			ELSE (CASE WHEN (fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN - movimentoEstoqueCota.QTDE ELSE 0 END) ");
		hql.append(" 		END ");
		hql.append("   ) * produtoEdicao.PRECO_VENDA AS faturamento ");
		
		hql.append(this.getFromWhereObterCurvaABC());
		hql.append(this.getFiltroCurvaABCCota(filtro, null));
		
		hql.append(" GROUP BY movimentoEstoqueCota.ID ");
		
		hql.append(" ORDER BY cota.ID ");
		
		//hql.append(" ORDER BY faturamento ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("edicaoProduto", StandardBasicTypes.LONG);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("porcentagemVenda", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("faturamento", StandardBasicTypes.BIG_DECIMAL);

		this.getFiltroCurvaABCCota(filtro, query);
		
		query.setParameterList("grupoMovimentoEstoque", this.obterGruposMovimentoEstoqueCota());
		query.setParameterList("tiposLancamento", this.obterTiposLancamentosRelatorioVenda());
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCCotaDTO.class));
		
		return query.list();
	}
	
	private String getFromWhereObterCurvaABC() {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM ");
		hql.append("      MOVIMENTO_ESTOQUE_COTA movimentoEstoqueCota ");
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
		hql.append("   	  EDITOR editor ");
		hql.append("   	  		ON editor.ID = produto.EDITOR_id ");
		hql.append(" INNER JOIN ");
		hql.append("   	  PESSOA pessoaEditor ");
		hql.append("   	  		ON editor.JURIDICA_ID = pessoaEditor.ID ");
		hql.append(" LEFT JOIN ");
		hql.append("   	  DESCONTO_LOGISTICA descontoLogistica ");
		hql.append("   			ON descontoLogistica.ID = produtoEdicao.DESCONTO_LOGISTICA_ID ");
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
		hql.append("  AND ");
		hql.append("     lancamento.status IN ( ");
		hql.append("     		:tiposLancamento ");
		hql.append("     ) ");

		return hql.toString();
	}
	
	private String getFiltroCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro, Query query) {
		
		StringBuilder hql = null;
		
		if (query == null){
			hql = new StringBuilder();
			hql.append(" and lancamento.DATA_REC_DISTRIB BETWEEN :dataDe AND :dataAte ");
		} else {
			
			query.setParameter("dataDe",  filtro.getDataDe());
			query.setParameter("dataAte", filtro.getDataAte());
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			
			if (query == null){
				
				hql.append("AND fornecedor.ID = :codigoFornecedor ");
			} else {
				
				query.setParameter("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
			}
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produto.CODIGO = :codigoProduto ");
			} else {
				
				query.setParameter("codigoProduto", filtro.getCodigoProduto());
			}
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produtoEdicao.NUMERO_EDICAO in (:edicaoProduto) ");
			} else {
				
				query.setParameterList("edicaoProduto", filtro.getEdicaoProduto());
			}
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			
			if (query == null){
				
				hql.append(" AND produto.EDITOR_ID = :codigoEditor ");
			} else {
				
				query.setParameter("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
			}
		}

		if (filtro.getCodigoCota() != null) {
			
			if (query == null){
				
				hql.append(" AND cota.NUMERO_COTA = :codigoCota ");
			} else {
				
				query.setParameter("codigoCota", filtro.getCodigoCota());
			}
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			
			if (query == null){
				
				hql.append(" AND endereco.CIDADE = :municipio ");
			} else {
				
				query.setParameter("municipio", filtro.getMunicipio());
			}
		}
		
		if (query == null){
			
			return hql.toString();
		}
		
		return null;
	}
	
	private String getFiltroCurvaABCEditor(FiltroCurvaABCEditorDTO filtro, Query query) {
		
		StringBuilder hql = null;
		
		if (query == null){
			
			hql = new StringBuilder();
			hql.append(" AND lancamento.DATA_REC_DISTRIB BETWEEN :dataDe AND :dataAte ");
		} else {
			
			query.setParameter("dataDe",  filtro.getDataDe());
			query.setParameter("dataAte", filtro.getDataAte());
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			
			if (query == null){
				
				hql.append("AND fornecedor.ID = :codigoFornecedor ");
			} else {
				
				query.setParameter("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
			}
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produto.CODIGO = :codigoProduto ");
			} else {
				
				query.setParameter("codigoProduto", filtro.getCodigoProduto().toString());
			}
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produtoEdicao.NUMERO_EDICAO = :edicaoProduto ");
			} else {
				
				query.setParameter("edicaoProduto", filtro.getCodigoProduto().toString());
			}
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			
			if (query == null){
				
				hql.append(" AND produto.EDITOR_ID = :codigoEditor ");
			} else {
				
				query.setParameter("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
			}
		}

		if (filtro.getCodigoCota() != null) {
			
			if (query == null){
				
				hql.append(" AND cota.NUMERO_COTA = :codigoCota ");
			} else {
				
				query.setParameter("codigoCota", filtro.getCodigoCota());
			}
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			
			if (query == null){
				
				hql.append(" AND endereco.CIDADE = :municipio ");
			} else {
				
				query.setParameter("municipio", filtro.getMunicipio());
			}
		}

		if (query == null){
			
			return hql.toString();
		}
		
		return null;
	}
	
	private String getFiltroCurvaABCCota(FiltroCurvaABCCotaDTO filtro, Query query) {
		
		StringBuilder hql = null;
		
		if (query == null){
			
			hql = new StringBuilder();
			hql.append(" AND lancamento.DATA_REC_DISTRIB BETWEEN :dataDe AND :dataAte ");
		} else {
			
			query.setParameter("dataDe",  filtro.getDataDe());
			query.setParameter("dataAte", filtro.getDataAte());
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			
			if (query == null){
				
				hql.append("AND fornecedor.ID = :codigoFornecedor ");
			} else {
				
				query.setParameter("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
			}
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produto.CODIGO = :codigoProduto ");
			} else {
				
				query.setParameter("codigoProduto", filtro.getCodigoProduto());
			}
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produtoEdicao.NUMERO_EDICAO = :edicaoProduto ");
			} else {
				
				query.setParameter("edicaoProduto", filtro.getCodigoProduto());
			}
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			
			if (query == null){
				
				hql.append(" AND produto.EDITOR_ID = :codigoEditor ");
			} else {
				
				query.setParameter("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
			}
		}

		if (filtro.getCodigoCota() != null) {
			
			if (query == null){
				
				hql.append(" AND cota.NUMERO_COTA = :codigoCota ");
			} else {
				
				query.setParameter("codigoCota", filtro.getCodigoCota());
			}
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			
			if (query == null){
				
				hql.append(" AND endereco.CIDADE = :municipio ");
			} else {
				
				query.setParameter("municipio", filtro.getMunicipio());
			}
		}

		if (query == null){
			
			return hql.toString();
		}
		
		return null;
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
	
	private List<String> obterTiposLancamentosRelatorioVenda(){
		
		return Arrays.asList(StatusLancamento.EM_RECOLHIMENTO.name(),
				StatusLancamento.RECOLHIDO.name(), StatusLancamento.FECHADO.name());
	}
	
}
