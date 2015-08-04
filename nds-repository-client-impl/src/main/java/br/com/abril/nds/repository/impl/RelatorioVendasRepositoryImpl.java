package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.RegistroRankingSegmentoDTO;
import br.com.abril.nds.dto.TotalizadorRankingSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroRankingSegmentoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
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

		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.COTA));
		
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
		
		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.EDITOR));
		
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
		
		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.PRODUTO_EDICAO));
		
		sql.append(" GROUP BY consolidado.PRODUTO_EDICAO_ID ");
		
		sql.append(" ) as curvaABCCota ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		this.getFiltroCurvaABC(filtro, query);
		
		BigInteger qtde = (BigInteger) query.uniqueResult(); 
		
		return (qtde == null) ? 0 : qtde.intValue(); 	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		
		sql.append(" consolidado.CODIGO_PRODUTO AS codigoProduto, 		");
		sql.append(" consolidado.NOME_PRODUTO AS nomeProduto,			");
		sql.append(" consolidado.NUMERO_EDICAO AS edicaoProduto,		");
		sql.append(" consolidado.reparte AS reparte,					");
		sql.append(" consolidado.vendaExemplares AS vendaExemplares,	");
		sql.append(" consolidado.porcentagemVenda AS porcentagemVenda,	");
		
		sql.append(" consolidado.faturamentoCapa AS faturamento,		");
		sql.append(" consolidado.valorMargemCota AS valorMargemCota,	");
		sql.append(" consolidado.valorMargemDistribuidor AS valorMargemDistribuidor	");

		sql.append(" from ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.PRODUTO_EDICAO));
		
		sql.append(" ORDER BY faturamento desc ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("edicaoProduto", StandardBasicTypes.LONG);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("porcentagemVenda", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("faturamento", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorMargemCota", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);
		
		this.getFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroHistoricoEditorVO.class));
		
		return query.list();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro, TipoPesquisaRanking tipoPesquisa) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" consolidado.COTA_ID AS idCota, ");
		
		sql.append(" @valorAcumulado\\:=@valorAcumulado + consolidado.valor as participacaoAcumulada,	");
		
		if(TipoPesquisaRanking.RankingCota.equals(tipoPesquisa)){
			sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkCota, ");
		} else {
			sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkProduto, ");
		}
		
		sql.append(" consolidado.NUMERO_COTA AS numeroCota, 	");
		sql.append(" COALESCE(consolidado.NOME_COTA, consolidado.RAZAO_SOCIAL_COTA) AS nomeCota, ");
		sql.append(" consolidado.CIDADE_COTA AS municipio, 	");
		sql.append(" consolidado.valor as participacao,	");
		sql.append(" consolidado.vendaExemplares as vendaExemplares,	");
		sql.append(" consolidado.faturamentoCapa as faturamentoCapa		");
		
		sql.append("   from ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.COTA));
		
		sql.append(" ,(select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s ORDER BY faturamentoCapa desc, numeroCota ");

		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		query.addScalar("municipio", StandardBasicTypes.STRING);
		query.addScalar("participacao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("participacaoAcumulada", StandardBasicTypes.BIG_DECIMAL);
		
		if(TipoPesquisaRanking.RankingCota.equals(tipoPesquisa)){
			query.addScalar("rkCota", StandardBasicTypes.LONG);
		} else {
			query.addScalar("rkProduto", StandardBasicTypes.LONG);
		}
		
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		

		this.getFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCDistribuidorVO.class));
		
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" consolidado.EDITOR_ID AS codigoEditor, 			");
		sql.append(" consolidado.RAZAO_SOCIAL_EDITOR AS nomeEditor, 	");
		sql.append(" consolidado.faturamentoCapa AS faturamentoCapa,	");
		sql.append(" consolidado.valor AS participacao,						");
		sql.append(" @valorAcumulado\\:=@valorAcumulado + consolidado.valor as participacaoAcumulada, ");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkEditor,	");
		sql.append(" consolidado.vendaExemplares as vendaExemplares,	");
		sql.append(" consolidado.reparte as reparte,					");
		sql.append(" consolidado.valorMargemDistribuidor as valorMargemDistribuidor, 			");
		sql.append(" consolidado.porcentagemMargemDistribuidor as porcentagemMargemDistribuidor	");

		sql.append(" from ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.EDITOR));
		
		sql.append(" ,(select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s ORDER BY faturamentoCapa desc, codigoEditor ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("codigoEditor", StandardBasicTypes.LONG);
		query.addScalar("nomeEditor", StandardBasicTypes.STRING);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("participacaoAcumulada", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("rkEditor", StandardBasicTypes.LONG);
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("participacao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("valorMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("porcentagemMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);
		
		this.getFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCEditorVO.class));
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		
		sql.append(" consolidado.PRODUTO_EDICAO_ID AS idProdutoEdicao, 	");
		sql.append(" consolidado.CODIGO_PRODUTO AS codigoProduto, 		");
		sql.append(" consolidado.NOME_PRODUTO AS nomeProduto, 			");
		sql.append(" consolidado.NUMERO_EDICAO AS edicaoProduto, 		");
		
		sql.append(" @valorAcumulado\\:=@valorAcumulado + consolidado.valor as participacaoAcumulada,	");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkProduto, ");
		
		sql.append(" consolidado.valor as participacao,				");
		sql.append(" consolidado.vendaExemplares as vendaExemplares,	");
		sql.append(" consolidado.faturamentoCapa as faturamento,	");
		sql.append(" consolidado.reparte as reparte	");
		
		sql.append(" from ");
		
		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.PRODUTO_EDICAO));
		
		sql.append(" ,(select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s ORDER BY faturamento desc, nomeProduto   ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("edicaoProduto", StandardBasicTypes.LONG);

		query.addScalar("participacaoAcumulada", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("rkProduto", StandardBasicTypes.LONG);
		
		query.addScalar("participacao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("faturamento", StandardBasicTypes.BIG_DECIMAL);
		
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		
		this.getFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCCotaDTO.class));
		
		return query.list();
	}

	@Override
	public TotalizadorRankingSegmentoDTO obterQuantidadeRegistrosRankingSegmento(FiltroRankingSegmentoDTO filtro) {

		Query query = this.getSession().createSQLQuery(

			new StringBuilder().append(
				"select count(total.id) as quantidadeRegistros, sum(total.faturamentoCapa) as totalFaturamentoCapa from "
			).append(
				"(select c.ID as id, sum(case when tm.OPERACAO_ESTOQUE=:entradaEstoque then mec.QTDE else mec.QTDE*-1 end) * pe.PRECO_VENDA as faturamentoCapa " 
			).append(
				this.obterFromWhereRankingSegmento(filtro)
			).append(
				") as total"
			).toString()
		);

		query.setParameter("tipoSegmentoID", filtro.getIdTipoSegmento());
		query.setParameter("entradaEstoque", OperacaoEstoque.ENTRADA.name());
		
		if (filtro.getDe() != null && filtro.getAte() != null) {
			query.setParameter("dataDe", filtro.getDe());
			query.setParameter("dataAte", filtro.getAte());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(TotalizadorRankingSegmentoDTO.class));

		return (TotalizadorRankingSegmentoDTO) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroRankingSegmentoDTO> obterRankingSegmento(FiltroRankingSegmentoDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select @ranking\\:=@ranking+1 as ranking, @partAcum\\:=@partAcum+((consulta_segmento.faturamentoCapa*100)/:totalFaturamento) as participacaoAcumulada, ")
		   .append(" consulta_segmento.numeroCota as numeroCota, consulta_segmento.nomeCota as nomeCota, consulta_segmento.faturamentoCapa as faturamentoCapa,  ")
		   .append(" (consulta_segmento.faturamentoCapa*100)/:totalFaturamento as participacao ")
		   .append(" from ")
		   .append(" (select @ranking\\:=0, @partAcum\\:=0) r,")
		   .append(" ( select c.NUMERO_COTA as numeroCota, ") 
		   .append(" sum(mec.QTDE) as qtd, coalesce(pes.NOME, pes.RAZAO_SOCIAL) as nomeCota, ")
		   .append(" sum(case when tm.OPERACAO_ESTOQUE=:entradaEstoque then mec.QTDE else mec.QTDE*-1 end) * pe.PRECO_VENDA as faturamentoCapa ")
		   .append(this.obterFromWhereRankingSegmento(filtro))
	   	   .append(" order by faturamentoCapa desc, c.NUMERO_COTA asc ) as consulta_segmento");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("tipoSegmentoID", filtro.getIdTipoSegmento());
		query.setParameter("entradaEstoque", OperacaoEstoque.ENTRADA.name());
		query.setParameter("totalFaturamento", filtro.getTotalFaturamento());

		if (filtro.getDe() != null && filtro.getAte() != null) {
			query.setParameter("dataDe", filtro.getDe());
			query.setParameter("dataAte", filtro.getAte());
		}

		((SQLQuery) query).addScalar("ranking", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("participacaoAcumulada", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery) query).addScalar("numeroCota", StandardBasicTypes.INTEGER);
		((SQLQuery) query).addScalar("nomeCota");
		((SQLQuery) query).addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery) query).addScalar("participacao", StandardBasicTypes.BIG_DECIMAL);

		query.setResultTransformer(Transformers.aliasToBean(RegistroRankingSegmentoDTO.class));
		
		return query.list();
	}
	
	private String obterFromWhereRankingSegmento(FiltroRankingSegmentoDTO filtro) {
		
		StringBuilder from = new StringBuilder();
		
		from.append(" from ranking_segmento rs ")
			.append(" join produto p on p.TIPO_SEGMENTO_PRODUTO_ID=:tipoSegmentoID ")
			.append(" join produto_edicao pe on pe.PRODUTO_ID=p.ID ")
			.append(" join cota c on c.ID=rs.COTA_ID ")
			.append(" join pessoa pes on pes.ID=c.PESSOA_ID ")
			.append(" join movimento_estoque_cota mec on mec.COTA_ID=c.ID and mec.PRODUTO_EDICAO_ID=pe.ID ")
			.append(" join tipo_movimento tm on tm.ID=mec.TIPO_MOVIMENTO_ID ")
			.append(" where rs.TIPO_SEGMENTO_PRODUTO_ID=:tipoSegmentoID ")
			.append(" AND tm.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
		
		if (filtro.getDe() != null && filtro.getAte() != null) {
			from.append(" and mec.DATA between :dataDe and :dataAte ");
		}

		from.append(" group by c.ID ")
			.append(" having sum(rs.QTDE) > 0 ");

		return from.toString();
	}

	public StringBuilder obterFromWhereObterCurvaABC(FiltroCurvaABCDTO filtro, AgrupamentoCurvaABC agrupamento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" ( select ");
		
		sql.append(" movimento_estoque_cota.COTA_ID AS COTA_ID, "); 
		sql.append(" cota.NUMERO_COTA AS NUMERO_COTA,");
		sql.append(" pessoa.NOME AS NOME_COTA,");
		sql.append(" pessoa.RAZAO_SOCIAL AS RAZAO_SOCIAL_COTA,");
		sql.append(" endereco.CIDADE AS CIDADE_COTA, ");
		sql.append(" produto.EDITOR_ID AS EDITOR_ID, 			");
		sql.append(" pessoaeditor.RAZAO_SOCIAL as RAZAO_SOCIAL_EDITOR,");
		sql.append(" produto.CODIGO as CODIGO_PRODUTO,			");
		sql.append(" produto.nome as NOME_PRODUTO,				");
		sql.append(" movimento_estoque_cota.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID, ");
		sql.append(" produto_edicao.NUMERO_EDICAO AS NUMERO_EDICAO, ");
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
		sql.append(" ) / ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else 0 end");
		sql.append(" ) * 100 as porcentagemVenda, ");
		
		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * produto_edicao.PRECO_VENDA  ");
		sql.append(" ) as faturamentoCapa, ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else 0 end");
		sql.append(" ) as reparte, ");
		
		sql.append(" sum( ");
		sql.append(" (	");
		sql.append("	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append("	* (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(movimento_estoque_cota.VALOR_DESCONTO, 0)) / 100)) ");
		sql.append(" )	");
		
		sql.append(" - ");
		
		sql.append(" (	");
		sql.append("	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append("	* (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(descontologistica.PERCENTUAL_DESCONTO, descontologisticaproduto.PERCENTUAL_DESCONTO, 0)) / 100)) ");
		sql.append(" )	");
		sql.append(" ) as valorMargemDistribuidor, ");

		
		sql.append(" sum( ");
		sql.append(" (	");
		sql.append("	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append("	* produto_edicao.PRECO_VENDA ");
		sql.append(" )	");
		
		sql.append(" - ");
		
		sql.append(" (	");
		sql.append("	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append("	* (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(movimento_estoque_cota.VALOR_DESCONTO, 0)) / 100)) ");
		sql.append(" )	");
		sql.append(" ) as valorMargemCota, ");
		
		
		sql.append(" sum( ");
		sql.append(" (	");
		sql.append("	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append("	* (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(movimento_estoque_cota.VALOR_DESCONTO,0)) / 100)) ");
		sql.append(" )	");
		sql.append(" - ");
		sql.append(" (	");
		sql.append("	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append("	* (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(descontologistica.PERCENTUAL_DESCONTO, descontologisticaproduto.PERCENTUAL_DESCONTO, 0)) / 100)) ");
		sql.append(" )	");
		sql.append(" )  ");
		
		sql.append(" / ");
		
		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * produto_edicao.PRECO_VENDA  ");
		sql.append(" ) * 100 as porcentagemMargemDistribuidor ");
		
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
		
		sql.append("	left join desconto_logistica descontologistica on descontologistica.id = produto_edicao.desconto_logistica_id    		");
		sql.append("	left join desconto_logistica descontologisticaproduto on descontologisticaproduto.id = produto.desconto_logistica_id	");
		
		sql.append("	inner join lancamento on lancamento.produto_edicao_id = produto_edicao.id										 ");
		sql.append("	left join fechamento_encalhe fechamentoencalhe on                                                                ");
		sql.append("	(fechamentoencalhe.data_encalhe = lancamento.data_rec_distrib                                                ");
		sql.append("	and fechamentoencalhe.produto_edicao_id = produto_edicao.id)                                                 ");
		
		sql.append("	WHERE	");
		
		sql.append("	lancamento.status IN (:statusLancamento) 	   ");
		sql.append("	and tipomovimento.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'	   ");
		sql.append(" 	and movimento_estoque_cota.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
				
		sql.append(this.getFiltroCurvaABC(filtro, null));
		
		switch(agrupamento) {
		
			case COTA:
			sql.append("	group by movimento_estoque_cota.COTA_ID	");
			break;
	
			case PRODUTO_EDICAO:
			sql.append("	group by movimento_estoque_cota.PRODUTO_EDICAO_ID ");
			break;
			
			case EDITOR:
			sql.append("	group by editor.id ");
			break;
		
		}
		
		
		sql.append("	) as consolidado	");
		
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
			query.setParameterList("statusLancamento", 
			        Arrays.asList(
			                StatusLancamento.EM_RECOLHIMENTO.name(), 
			                StatusLancamento.RECOLHIDO.name(), 
			                StatusLancamento.FECHADO.name()));
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
				
				sql.append(" AND produto_edicao.NUMERO_EDICAO in (:edicaoProduto) ");
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
	
	
}
