package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
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
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
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
	
	public Integer obterQtdRegistrosCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT count(*) FROM ( ");
		
		sql.append(" select consolidado.COTA_ID from ");

		sql.append(obterFromWhereObterCurvaABC(filtro));
		
		sql.append(" GROUP BY consolidado.COTA_ID ) AS curvaDistribuidor");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		this.getFiltroCurvaABC(filtro, query);
		
		BigInteger qtde = (BigInteger) query.uniqueResult(); 
		
		return (qtde == null) ? 0 : qtde.intValue(); 
		
	}

	public Integer obterQtdRegistrosCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT count(*) FROM ( ");
		
		sql.append(" SELECT consolidado.EDITOR_ID FROM ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro));
		
		sql.append(" GROUP BY consolidado.EDITOR_ID ) as curvaABCEditor ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		this.getFiltroCurvaABC(filtro, query);
		
		BigInteger qtde = (BigInteger) query.uniqueResult(); 
		
		return (qtde == null) ? 0 : qtde.intValue(); 	
	}

	public Integer obterQtdRegistrosCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT count(*) FROM ( ");
		
		sql.append(" SELECT consolidado.PRODUTO_EDICAO_ID FROM ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro));
		
		sql.append(" GROUP BY consolidado.PRODUTO_EDICAO_ID ");
		
		sql.append(" ) as curvaABCCota ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		this.getFiltroCurvaABC(filtro, query);
		
		BigInteger qtde = (BigInteger) query.uniqueResult(); 
		
		return (qtde == null) ? 0 : qtde.intValue(); 	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		
		sql.append("   consolidado.PRODUTO_ID AS idProduto, 	");
		sql.append("   consolidado.COTA_ID AS idCota, 			");
		sql.append("   consolidado.NUMERO_COTA AS numeroCota, 	");
		sql.append("   COALESCE(consolidado.NOME_COTA, consolidado.RAZAO_SOCIAL_COTA) AS nomeCota, ");
		sql.append("   consolidado.CIDADE_COTA AS municipio, 	");
		sql.append("   consolidado.valor as faturamentoCapa 	");
		
		sql.append("   from ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro));
		
		sql.append(" GROUP BY consolidado.COTA_ID ");

		sql.append(" ORDER BY consolidado.valor desc ");

		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("idProduto", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		query.addScalar("municipio", StandardBasicTypes.STRING);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);

		this.getFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCDistribuidorVO.class));
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}
		
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		
		sql.append(" consolidado.EDITOR_ID AS codigoEditor, 		");
		sql.append(" consolidado.RAZAO_SOCIAL_EDITOR AS nomeEditor, ");
		sql.append(" consolidado.valor AS faturamentoCapa			");

		sql.append(" from ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro));
		
		sql.append(" GROUP BY consolidado.EDITOR_ID ");
		
		sql.append(" ORDER BY consolidado.valor desc ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("codigoEditor", StandardBasicTypes.LONG);
		query.addScalar("nomeEditor", StandardBasicTypes.STRING);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);

		this.getFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCEditorVO.class));
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		
		sql.append(" consolidado.PRODUTO_EDICAO_ID AS idProdutoEdicao, 	");
		sql.append(" consolidado.CODIGO_PRODUTO AS codigoProduto, 		");
		sql.append(" consolidado.NOME_PRODUTO AS nomeProduto, 			");
		sql.append(" consolidado.NUMERO_EDICAO AS edicaoProduto, 		");
		sql.append(" consolidado.valor AS faturamento					");
		
		sql.append(" from ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro));
		
		sql.append(" GROUP BY consolidado.PRODUTO_EDICAO_ID ");
		
		sql.append(" ORDER BY consolidado.valor desc ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("edicaoProduto", StandardBasicTypes.LONG);
		query.addScalar("faturamento", StandardBasicTypes.BIG_DECIMAL);
		
		this.getFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCCotaDTO.class));
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public StringBuilder obterFromWhereObterCurvaABC(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" ( select ");
		
		sql.append(" movimento_estoque_cota.COTA_ID AS COTA_ID, "); 
		sql.append(" cota.NUMERO_COTA AS NUMERO_COTA,");
		sql.append(" pessoa.NOME AS NOME_COTA,");
		sql.append(" pessoa.RAZAO_SOCIAL AS RAZAO_SOCIAL_COTA,");
		sql.append(" endereco.CIDADE AS CIDADE_COTA, ");
		sql.append(" produto_edicao.PRODUTO_ID AS PRODUTO_ID, 	");
		sql.append(" produto.EDITOR_ID AS EDITOR_ID, 			");
		sql.append(" pessoaeditor.RAZAO_SOCIAL as RAZAO_SOCIAL_EDITOR,");
		sql.append(" produto.CODIGO as CODIGO_PRODUTO,			");
		sql.append(" produto.nome as NOME_PRODUTO,				");
		sql.append(" movimento_estoque_cota.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID, ");
		sql.append(" produto_edicao.NUMERO_EDICAO AS NUMERO_EDICAO, ");
		sql.append(" movimento_estoque_cota.DATA AS DATA_MOVIMENTO, ");
		sql.append(" produto_edicao.PRECO_VENDA AS PRECO_VENDA,		");
		
		//valor venda com desconto
		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(movimento_estoque_cota.VALOR_DESCONTO,0)) / 100)) ");
		sql.append(" ) as valor, ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" ) as vendaExemplares, ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * produto_edicao.PRECO_VENDA  ");
		sql.append(" ) as faturamentoCapa, ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else 0 end");
		sql.append(" ) as reparte, ");

		sql.append(" sum( ");
		sql.append(" 	( ");
		sql.append(" 		case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("		(movimento_estoque_cota.qtde*-1) end ");
		sql.append("  	) * 100 / ");
		
		sql.append(" 	( ");
		sql.append(" 		case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else 0 end ");
		sql.append(" 	)  	");
		sql.append(" ) as porcentagemVendaExemplares, ");
		
		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		
		sql.append(" 	 * (movimento_estoque_cota.PRECO_COM_DESCONTO - (produto_edicao.PRECO_VENDA ");
		sql.append(" 	  		- (produto_edicao.PRECO_VENDA * COALESCE(descontologistica.PERCENTUAL_DESCONTO, 0) / 100) ) ");
		sql.append(" 			) ");
		
		sql.append(" ) as valorMargemDistribuidor, ");

		sql.append(" sum( ");

		sql.append(" (	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		
		sql.append(" 	* (movimento_estoque_cota.PRECO_COM_DESCONTO - (produto_edicao.PRECO_VENDA ");
		sql.append(" 	  		- (produto_edicao.PRECO_VENDA * COALESCE(descontologistica.PERCENTUAL_DESCONTO, 0) / 100) ) ");
		sql.append(" 	  ) ");
		
		sql.append(" ) ");
		
		sql.append(" / ");
		
		sql.append(" (	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * produto_edicao.PRECO_VENDA )  ");
		
		sql.append(" ) as porcentagemMargemDistribuidor ");
		
		sql.append("	from   ");
		
		sql.append("	movimento_estoque_cota  ");
		
		sql.append("	inner join produto_edicao on movimento_estoque_cota.produto_edicao_id = produto_edicao.id                        ");
		sql.append("	inner join produto on (produto_edicao.produto_id = produto.id)                                                   ");
		sql.append("	inner join tipo_movimento tipomovimento on movimento_estoque_cota.tipo_movimento_id = tipomovimento.id           ");
		sql.append("	inner join produto_fornecedor produtofornecedor on produto.id = produtofornecedor.produto_id                     ");
		sql.append("	inner join fornecedor on produtofornecedor.fornecedores_id = fornecedor.id                                       ");
		sql.append("	inner join cota on movimento_estoque_cota.cota_id = cota.id                                                      ");
		sql.append("	inner join pessoa on pessoa.id = cota.pessoa_id                                                                  ");
		sql.append("	inner join endereco_cota enderecocota on enderecocota.cota_id = cota.id and enderecocota.principal = true        ");
		sql.append("	inner join endereco endereco on enderecocota.endereco_id = endereco.id                                           ");
		sql.append("	inner join editor on editor.id = produto.editor_id                                                               ");
		sql.append("	inner join pessoa pessoaeditor on editor.juridica_id = pessoaeditor.id                                           ");
		sql.append("	left join desconto_logistica descontologistica on descontologistica.id = produto_edicao.desconto_logistica_id    ");
		sql.append("	inner join lancamento on lancamento.id =                                                                         ");
		sql.append("	(                                                                                                                ");
		sql.append("		case when (produto_edicao.parcial)                                                                           ");
		sql.append("				then (select id from lancamento where produto_edicao_id = produto_edicao.id                          ");
		sql.append("							order by id asc limit 1)                                                                 ");
		sql.append("				else (select id from lancamento where produto_edicao_id = produto_edicao.id                          ");
		sql.append("							order by id desc limit 1)                                                                ");
		sql.append("			end                                                                                                      ");
		sql.append("	)                                                                                                                ");
	    sql.append("                                                                                                                     ");
		sql.append("	left join fechamento_encalhe fechamentoencalhe on                                                                ");
		sql.append("		(fechamentoencalhe.data_encalhe = lancamento.data_rec_distrib                                                ");
		sql.append("		and fechamentoencalhe.produto_edicao_id = produto_edicao.id)                                                 ");
		
		sql.append("	WHERE	");
		
		sql.append("	lancamento.status IN ( 'EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO' ) 	   ");
		
		sql.append(this.getFiltroCurvaABC(filtro, null));
		
		sql.append("		group by movimento_estoque_cota.PRODUTO_EDICAO_ID,   ");
		sql.append("		movimento_estoque_cota.COTA_ID, ");
		sql.append("		movimento_estoque_cota.DATA     ");
		
		sql.append("		order by movimento_estoque_cota.DATA desc ) as consolidado ");
		
		return sql;
	}
	
	private String getFiltroCurvaABC(FiltroCurvaABCDTO filtro, Query query) {
		
		StringBuilder sql = null;
		
		if (query == null){
			sql = new StringBuilder();
			sql.append(" and lancamento.DATA_REC_DISTRIB BETWEEN :dataDe AND :dataAte ");
		} else {
			
			query.setParameter("dataDe",  filtro.getDataDe());
			query.setParameter("dataAte", filtro.getDataAte());
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			
			if (query == null){
				
				sql.append("AND fornecedor.ID = :codigoFornecedor ");
			} else {
				
				query.setParameter("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
			}
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			
			if (query == null){
				
				sql.append(" AND produto.CODIGO = :codigoProduto ");
			} else {
				
				query.setParameter("codigoProduto", filtro.getCodigoProduto());
			}
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			
			if (query == null){
				
				sql.append(" AND produtoEdicao.NUMERO_EDICAO in (:edicaoProduto) ");
			} else {
				
				query.setParameterList("edicaoProduto", filtro.getEdicaoProduto());
			}
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			
			if (query == null){
				
				sql.append(" AND produto.EDITOR_ID = :codigoEditor ");
			} else {
				
				query.setParameter("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
			}
		}

		if (filtro.getCodigoCota() != null) {
			
			if (query == null){
				
				sql.append(" AND cota.NUMERO_COTA = :codigoCota ");
			} else {
				
				query.setParameter("codigoCota", filtro.getCodigoCota());
			}
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			
			if (query == null){
				
				sql.append(" AND endereco.CIDADE = :municipio ");
			} else {
				
				query.setParameter("municipio", filtro.getMunicipio());
			}
		}
		
		if (query == null){
			
			return sql.toString();
		}
		
		return null;
	}
	

	
	private List<String> obterTiposLancamentosRelatorioVenda(){
		
		return Arrays.asList(StatusLancamento.EM_RECOLHIMENTO.name(),
				StatusLancamento.RECOLHIDO.name(), StatusLancamento.FECHADO.name());
	}
	
}
