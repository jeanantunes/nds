package br.com.abril.nds.repository.impl;

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
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroRankingSegmentoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RelatorioVendasRepository;

@Repository
public class RelatorioVendasRepositoryImpl extends AbstractRepositoryModel<Distribuidor, Long> implements RelatorioVendasRepository {
	
	public RelatorioVendasRepositoryImpl() {
		super(Distribuidor.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append("         consolidado.CODIGO_PRODUTO AS codigoProduto, ");
		sql.append("         consolidado.NOME_PRODUTO AS nomeProduto, ");
		sql.append("         consolidado.NUMERO_EDICAO AS edicaoProduto, ");
		sql.append("         consolidado.reparte AS reparte, ");
		sql.append("         consolidado.vendaExemplares AS vendaExemplares, ");
		sql.append("         consolidado.porcentagemVenda AS porcentagemVenda, ");
		sql.append("         consolidado.faturamentoCapa AS faturamento, ");
		sql.append("         consolidado.valorMargemCota AS valorMargemCota, ");
		sql.append("         consolidado.valorMargemDistribuidor AS valorMargemDistribuidor  ");
		sql.append("    ");
		sql.append("     FROM ");
		sql.append("         (SELECT ");
		sql.append("             temp.codProduto as CODIGO_PRODUTO, ");
		sql.append("             temp.nomeProduto as NOME_PRODUTO, ");
		sql.append("             temp.numEdicao AS NUMERO_EDICAO, ");
		sql.append("             sum(temp.reparteSum) as reparte, ");
		sql.append("             sum(temp.vendaSum) as vendaExemplares, ");
		sql.append("             (sum(temp.vendaSum)/sum(temp.reparteSum))*100 as porcentagemVenda, ");
		sql.append("             sum(temp.vendaSum * temp.PRECO_VENDA) as faturamentoCapa, ");
		sql.append("              ");
		sql.append("             sum((temp.vendaSum * temp.PRECO_VENDA)  -  (temp.vendaSum  * (temp.PRECO_VENDA - ((temp.PRECO_VENDA  ");
		sql.append("                   * coalesce(temp.valorDesconto,0)) / 100)))) as valorMargemCota,   ");
		sql.append("                ");
		sql.append("             sum((temp.vendaSum  * (temp.PRECO_VENDA - ((temp.PRECO_VENDA  ");
		sql.append("                   * coalesce(temp.valorDesconto,0)) / 100)))  ");
		sql.append("                     -  (temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA  ");
		sql.append("                         * coalesce(temp.percentualDescLog, temp.percentualDescProd,0))/ 100))) ");
		sql.append("                 ) as valorMargemDistribuidor ");
		sql.append("  ");
		sql.append("         FROM ");
		sql.append("             (Select ");
		sql.append("                 T.codProduto as codProduto, ");
		sql.append("                 T.nomeProduto as nomeProduto, ");
		sql.append("                 T.numEdicao as numEdicao, ");
		sql.append("                 T.COTA_ID as COTA_ID, ");
		sql.append("                 T.valorDesconto as valorDesconto, ");
		sql.append("                 SUM(T.reparte) reparteSum, ");
		sql.append("                 SUM(T.venda) vendaSum, ");
		sql.append("                 T.PRECO_VENDA as PRECO_VENDA, ");
		sql.append("                 T.lancID, ");
		sql.append("                 T.percentualDesc as percentualDescLog, ");
		sql.append("                 T.percentualDescProd as percentualDescProd ");
		sql.append("             from ");
		sql.append("                 (SELECT ");
		sql.append("                     p.CODIGO as codProduto, ");
		sql.append("                     p.NOME as nomeProduto, ");
		sql.append("                     pe.NUMERO_EDICAO as numEdicao, ");
		sql.append("                     mecReparte.COTA_ID AS COTA_ID, ");
		sql.append("                     mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		sql.append("                     pe.PRECO_VENDA AS PRECO_VENDA, ");
		sql.append("                     l.ID lancID, ");
		sql.append("                     descLogistica.PERCENTUAL_DESCONTO as percentualDesc, ");
		sql.append("                     descLogisticaProduto.PERCENTUAL_DESCONTO as percentualDescProd, ");
		sql.append("                      ");
		sql.append("                     cast(sum(case ");
		sql.append("                         when tipo.OPERACAO_ESTOQUE = 'ENTRADA' ");
		sql.append("                         THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0) ");
		sql.append("                         ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0) ");
		sql.append("                     end ) as unsigned int) AS reparte, ");
		sql.append("                     (case ");
		sql.append("                         when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO') ");
		sql.append("                             then cast(sum( ");
		sql.append("                                 CASE WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' ");
		sql.append("                                 THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0) ");
		sql.append("                             ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0) ");
		sql.append("                         END) ");
		sql.append("                         - (select ");
		sql.append("                             sum(mecEncalhe.qtde) ");
		sql.append("                         from ");
		sql.append("                             lancamento lanc ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe_lancamento cel ");
		sql.append("                                 on cel.LANCAMENTO_ID = lanc.ID ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe ce ");
		sql.append("                                 on ce.id = cel.CHAMADA_ENCALHE_ID ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe_cota cec ");
		sql.append("                                 on cec.CHAMADA_ENCALHE_ID = ce.ID ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             cota cota ");
		sql.append("                                 on cota.id = cec.COTA_ID ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             conferencia_encalhe confEnc ");
		sql.append("                                 on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             movimento_estoque_cota mecEncalhe ");
		sql.append("                                 on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
		sql.append("                         WHERE ");
		sql.append("                             lanc.id = l.id ");
		sql.append("                             and cota.id = c.id) AS SIGNED INT) ");
		sql.append("                         else      null ");
		sql.append("                     end) as venda ");
		sql.append("                 FROM ");
		sql.append("                     lancamento l ");
		sql.append("                 JOIN ");
		sql.append("                     produto_edicao pe ");
		sql.append("                         ON pe.id = l.produto_edicao_id ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     periodo_lancamento_parcial plp ");
		sql.append("                         ON plp.id = l.periodo_lancamento_parcial_id ");
		sql.append("                 JOIN ");
		sql.append("                     produto p ");
		sql.append("                         ON p.id = pe.produto_id ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     movimento_estoque_cota mecReparte ");
		sql.append("                         on mecReparte.LANCAMENTO_ID = l.id ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     tipo_movimento tipo ");
		sql.append("                         ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
		sql.append("                 JOIN ");
		sql.append("                     cota c ");
		sql.append("                         ON mecReparte.COTA_ID = c.ID ");
		sql.append("                 JOIN ");
		sql.append("                     pessoa pess ");
		sql.append("                         ON c.PESSOA_ID = pess.ID ");
		sql.append("                 inner join ");
		sql.append("                     endereco_cota endCota ");
		sql.append("                         ON endCota.cota_id = c.id ");
		sql.append("                         and endCota.principal = true ");
		sql.append("                 inner join ");
		sql.append("                     endereco endereco ");
		sql.append("                         ON endCota.endereco_id = endereco.id ");
		sql.append("                 inner join ");
		sql.append("                     editor  ");
		sql.append("                         on editor.id = p.editor_id ");
		sql.append("                 inner join ");
		sql.append("                     pessoa pessoaEditor  ");
		sql.append("                         on editor.juridica_id = pessoaEditor.id ");
		sql.append("                 left join ");
		sql.append("                     desconto_logistica descLogistica  ");
		sql.append("                         on descLogistica.id = pe.desconto_logistica_id ");
		sql.append("                 left join ");
		sql.append("                     desconto_logistica descLogisticaProduto  ");
		sql.append("                         on descLogisticaProduto.id = p.desconto_logistica_id ");
		sql.append("                 WHERE ");
		sql.append("                     l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')");
		sql.append("                     and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'     ");
		
		sql.append(this.getWhereFiltroCurvaABC(filtro, null));

		sql.append("                 group by ");
		sql.append("                     pe.numero_edicao, ");
		sql.append("                     pe.id, ");
		sql.append("                     mecReparte.cota_id, ");
		sql.append("                     plp.numero_periodo ");
		sql.append("                 ORDER BY ");
		sql.append("                     l.ID desc )T ");
		sql.append("                 group by ");
		sql.append("                     lancID ");
		sql.append("                 ORDER BY ");
		sql.append("                     vendaSum desc) temp ");
		sql.append("             group by ");
		sql.append("                 CODIGO_PRODUTO ");
		sql.append("             ORDER BY ");
		sql.append("                 faturamentoCapa desc) consolidado ");
		
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
		
		query.addScalar("venda", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("preco", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("reparte", StandardBasicTypes.BIG_DECIMAL);
		
		this.getWhereFiltroCurvaABC(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroHistoricoEditorVO.class));
		
		return query.list();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro, TipoPesquisaRanking tipoPesquisa) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT    ");
		sql.append("       consolidado_cota.idCota as idCota, ");
		
		if(TipoPesquisaRanking.RankingCota.equals(tipoPesquisa)){
			sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkCota, ");
		} else {
			sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkProduto, ");
		}

		sql.append("       @valorAcumulado\\:=@valorAcumulado + consolidado_cota.participacao as participacaoAcumulada, ");
		sql.append("       consolidado_cota.numeroCota as numeroCota, ");
		sql.append("       consolidado_cota.nomeCota AS nomeCota, ");
		sql.append("       consolidado_cota.municipio AS municipio, ");
		sql.append("       consolidado_cota.participacao as participacao, ");
		sql.append("       consolidado_cota.vendaExemplares as vendaExemplares, ");
		sql.append("       consolidado_cota.faturamentoCapa as faturamentoCapa,  ");
		sql.append("       consolidado_cota.segmentoDescricao as segmento, ");
		sql.append("       consolidado_cota.produtoNome as produtoNome, ");
		sql.append("       consolidado_cota.produtoCodigo as produtoCodigo, ");
		sql.append("       consolidado_cota.editorCodigo as editorCodigo, ");
		sql.append("       consolidado_cota.codigoBarra as codigoBarra, ");
		sql.append("       consolidado_cota.peb as peb, ");
		sql.append("       consolidado_cota.numeroEdicao as numeroEdicao, ");
		sql.append("       consolidado_cota.dataLancamento as dataLancamento, ");
		sql.append("       consolidado_cota.dataRecolhimento as dataRecolhimento, ");
		sql.append("       consolidado_cota.classificacao as classificacao, ");
		sql.append("       consolidado_cota.venda as venda, ");
		sql.append("       consolidado_cota.preco as preco, ");
		sql.append("       consolidado_cota.tipoPDV as tipoPDV, ");
		sql.append("       consolidado_cota.reparte as reparte ");
		sql.append("   FROM ");
		sql.append("       (SELECT ");
		sql.append("         sub02.cotaId as idCota, ");
		sql.append("         sub02.numeroCota numeroCota, ");
		sql.append("         COALESCE(sub02.NOME_COTA, sub02.RAZAO_SOCIAL_COTA) AS nomeCota, ");
		sql.append("         sub02.CIDADE_COTA AS municipio, ");
		sql.append("         sum(sub02.vendaSum * (sub02.PRECO_VENDA - ((sub02.PRECO_VENDA * coalesce(sub02.valorDesconto,0)) / 100))) as participacao, ");
		sql.append("         sum(sub02.vendaSum) as vendaExemplares, ");
		sql.append("         sum(sub02.vendaSum * sub02.PRECO_VENDA) as faturamentoCapa, ");
		sql.append("         sub02.segmentoDescricao as segmentoDescricao, ");
		sql.append("         sub02.produtoNome as produtoNome, ");
		sql.append("         sub02.produtoCodigo as produtoCodigo, ");
		sql.append("         sub02.editorCodigo as editorCodigo, ");
		sql.append("         sub02.codigoBarra as codigoBarra, ");
		sql.append("         sub02.peb as peb  , ");
		sql.append("         sub02.numeroEdicao as numeroEdicao, ");
		sql.append("         sub02.dataLancamento as dataLancamento, ");
		sql.append("         sub02.dataRecolhimento as dataRecolhimento, ");
		sql.append("         sub02.classificacao as classificacao, ");
		sql.append("         sub02.PRECO_VENDA as venda, ");
		sql.append("         sub02.preco as preco, ");
		sql.append("         sub02.tipoPDV as tipoPDV, ");
		sql.append("         sub02.reparteSum as reparte ");
		sql.append("  ");
		sql.append("         FROM ");
		sql.append("         ( ");
		sql.append("         Select  ");
		sql.append("           sub01.NUMERO_COTA as numeroCota, ");
		sql.append("           sub01.COTA_ID as cotaId, ");
		sql.append("           sub01.NOME_COTA, ");
		sql.append("           sub01.CIDADE_COTA, ");
		sql.append("           sub01.RAZAO_SOCIAL_COTA, ");
		sql.append("           SUM(sub01.reparte) reparteSum, ");
		sql.append("           SUM(sub01.venda) vendaSum, ");
		sql.append("           sub01.PRECO_VENDA as PRECO_VENDA, ");
		sql.append("           sub01.valorDesconto, ");
		sql.append("           sub01.lancId as lancId,  ");
		sql.append("           sub01.segmentoDescricao as segmentoDescricao,  ");
		sql.append("           sub01.produtoNome as produtoNome,  ");
		sql.append("           sub01.produtoCodigo as produtoCodigo,  ");
		sql.append("           sub01.editorCodigo as editorCodigo, ");
		sql.append("           sub01.codigoBarra as codigoBarra, ");
		sql.append("           sub01.peb as peb  , ");
		sql.append("           sub01.numeroEdicao as numeroEdicao, ");
		sql.append("           sub01.dataLancamento as dataLancamento, ");
		sql.append("           sub01.dataRecolhimento as dataRecolhimento, ");
		sql.append("           sub01.classificacao as classificacao, ");
		sql.append("           sub01.preco as preco, ");
		sql.append("           sub01.tipoPDV as tipoPDV ");
		
		sql.append("         from ( ");
		sql.append("          ");
		sql.append("         SELECT ");
		sql.append("             mecReparte.COTA_ID AS COTA_ID, ");
		sql.append("             mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		sql.append("             c.NUMERO_COTA AS NUMERO_COTA, ");
		sql.append("             if(pess.tipo = 'F',pess.NOME,pess.NOME_FANTASIA) AS NOME_COTA, ");
		sql.append("             pess.RAZAO_SOCIAL AS RAZAO_SOCIAL_COTA, ");
		sql.append("             endereco.CIDADE AS CIDADE_COTA, ");
		sql.append("             pe.PRECO_VENDA AS PRECO_VENDA, ");
		sql.append("             l.id as lancId, ");
		sql.append("             tsp.descricao as segmentoDescricao, ");
		sql.append("             p.nome as produtoNome, ");
		sql.append("             p.codigo as produtoCodigo, ");
		sql.append("             e.codigo as editorCodigo, ");
		sql.append("             pe.codigo_de_Barras as codigoBarra, ");
		sql.append("             p.peb as peb  , ");
		sql.append("             pe.numero_edicao as numeroEdicao, ");
		sql.append("             l.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		sql.append("             l.DATA_REC_DISTRIB as dataRecolhimento, ");
		sql.append("             tcp.DESCRICAO as classificacao, ");
		sql.append("             pe.PRECO_PREVISTO as preco, ");
		sql.append("             tpp.descricao AS tipoPDV, ");
		sql.append("             cast(sum(case  ");
		sql.append("                 when tipo.OPERACAO_ESTOQUE = 'ENTRADA'  ");
		sql.append("                 THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) ");
		sql.append("                 ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, -mecReparte.QTDE, 0) ");
		sql.append("             end ) as unsigned int) AS reparte, ");
		sql.append("              ");
		sql.append("             (case  ");
		sql.append("                 when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO')      ");
		sql.append("                 then      cast(sum(CASE             ");
		sql.append("                     WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA'             ");
		sql.append("                     THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) ");
		sql.append("                     ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, -mecReparte.QTDE, 0) ");
		sql.append("                 END)        - (select ");
		sql.append("                     sum(mecEncalhe.qtde)             ");
		sql.append("                 from ");
		sql.append("                     lancamento lanc             ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     chamada_encalhe_lancamento cel  ");
		sql.append("                         on cel.LANCAMENTO_ID = lanc.ID             ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     chamada_encalhe ce  ");
		sql.append("                         on ce.id = cel.CHAMADA_ENCALHE_ID             ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     chamada_encalhe_cota cec  ");
		sql.append("                         on cec.CHAMADA_ENCALHE_ID = ce.ID             ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     cota cota  ");
		sql.append("                         on cota.id = cec.COTA_ID             ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     conferencia_encalhe confEnc  ");
		sql.append("                         on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID             ");
		sql.append("                         and  ce.DATA_RECOLHIMENTO = lanc.data_rec_distrib       "); // chamado 4667129
		sql.append("                 LEFT JOIN ");
		sql.append("                     movimento_estoque_cota mecEncalhe  ");
		sql.append("                         on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
		sql.append("                 WHERE ");
		sql.append("                     lanc.id = l.id  ");
		sql.append("                     and cota.id = c.id) AS SIGNED INT)       ");
		sql.append("                 else      null       ");
		sql.append("             end) as venda ");
		sql.append("              ");
		sql.append("         FROM ");
		sql.append("             lancamento l       ");
		sql.append("         JOIN ");
		sql.append("             produto_edicao pe  ");
		sql.append("                 ON pe.id = l.produto_edicao_id       ");
		sql.append("         LEFT JOIN ");
		sql.append("             periodo_lancamento_parcial plp  ");
		sql.append("                 ON plp.id = l.periodo_lancamento_parcial_id       ");
		sql.append("         JOIN ");
		sql.append("             produto p  ");
		sql.append("                 ON p.id = pe.produto_id         ");
		sql.append("         LEFT JOIN ");
		sql.append("             movimento_estoque_cota mecReparte  ");
		sql.append("                 on mecReparte.LANCAMENTO_ID = l.id       ");
		sql.append("         LEFT JOIN ");
		sql.append("             tipo_movimento tipo  ");
		sql.append("                 ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID       ");
		sql.append("         JOIN ");
		sql.append("             cota c  ");
		sql.append("                 ON mecReparte.COTA_ID = c.ID ");
		sql.append("         JOIN  ");
		sql.append("             pessoa pess ");
		sql.append("                 ON c.PESSOA_ID = pess.ID ");
		sql.append("         inner join ");
		sql.append("             endereco_cota endCota  ");
		sql.append("                 ON endCota.cota_id = c.id  ");
		sql.append("                 and endCota.principal = true          ");
		sql.append("         inner join ");
		sql.append("             endereco endereco  ");
		sql.append("                 ON endCota.endereco_id = endereco.id ");
		sql.append(" 		 JOIN ");
		sql.append("             produto_fornecedor prodFornecedor ");
		sql.append("                 ON prodFornecedor.PRODUTO_ID = p.ID ");
		sql.append("         JOIN  ");
		sql.append("             fornecedor  ");
		sql.append("                 ON prodFornecedor.fornecedores_ID = fornecedor.ID ");
		sql.append("         JOIN  ");
		sql.append("             tipo_segmento_produto as tsp ");
		sql.append("                 ON tsp.ID = p.TIPO_SEGMENTO_PRODUTO_ID ");
		sql.append("         JOIN  ");
		sql.append("             editor as e ");
		sql.append("                 ON e.ID = p.EDITOR_ID ");
		sql.append("         JOIN  ");
		sql.append("             tipo_classificacao_produto as tcp ");
		sql.append("                 ON tcp.ID = pe.TIPO_CLASSIFICACAO_PRODUTO_ID ");
		sql.append("         LEFT JOIN  ");
		sql.append("             PDV as pdv   ");
		sql.append("                 on  pdv.cota_id = c.id ");
		sql.append("         LEFT JOIN  ");
		sql.append("             TIPO_PONTO_PDV tpp  ");
		sql.append("             	on tpp.id = pdv.TIPO_PONTO_PDV_id ");
		sql.append("         WHERE ");
		sql.append("             l.status in (:statusLancamento) ");
		sql.append("             and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' and pdv.ponto_principal = true  ");
		
		sql.append(this.getWhereFiltroCurvaABC(filtro, null));
		
		sql.append("         group by ");
		sql.append("             pe.numero_edicao, pe.id, ");
		sql.append("             mecReparte.cota_id, plp.numero_periodo  ");
		sql.append("         ORDER BY ");
		sql.append("             l.ID desc )sub01  ");
		sql.append("                         group by numeroCota, lancId ORDER BY vendaSum desc) sub02  ");
		sql.append(" 							group by numeroCota                           ");
		sql.append(" 						        ORDER BY faturamentoCapa desc) consolidado_cota,");
		sql.append(" 						            (select");
		sql.append(" 						                @valorAcumulado\\:=0,");
		sql.append(" 						                @posicaoRanking\\:=0) as s  ");
		
		if(TipoPesquisaRanking.RankingCota.equals(tipoPesquisa)){
			sql.append("                         ORDER BY rkCota ");
		} else {
			sql.append("                         ORDER BY rkProduto ");
		}
		
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameterList("statusLancamento", 
		        Arrays.asList(
		                StatusLancamento.EM_RECOLHIMENTO.name(), 
		                StatusLancamento.RECOLHIDO.name(), 
		                StatusLancamento.FECHADO.name()));
		
		this.getWhereFiltroCurvaABC(filtro, query);
		
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
			query.addScalar("tipoPDV", StandardBasicTypes.STRING);
			query.addScalar("reparte", StandardBasicTypes.BIG_DECIMAL);
		}

		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		
		query.addScalar("segmento", StandardBasicTypes.STRING);
		query.addScalar("produtoNome", StandardBasicTypes.STRING);
		query.addScalar("produtoCodigo", StandardBasicTypes.STRING);
		query.addScalar("editorCodigo", StandardBasicTypes.STRING);
		query.addScalar("codigoBarra", StandardBasicTypes.STRING);
		query.addScalar("peb", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.STRING);
		query.addScalar("dataLancamento", StandardBasicTypes.STRING);
		query.addScalar("dataRecolhimento", StandardBasicTypes.STRING);
		query.addScalar("classificacao", StandardBasicTypes.STRING);
		query.addScalar("preco", StandardBasicTypes.STRING);
		query.addScalar("venda", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCDistribuidorVO.class));
		
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("       consolidado.codigoEditor as codigoEditor, ");
		sql.append("       consolidado.nomeEditor as nomeEditor, ");
		sql.append("       consolidado.faturamentoCapa as faturamentoCapa, ");
		sql.append("       consolidado.participacao AS participacao, ");
		sql.append("         @valorAcumulado\\:=@valorAcumulado + consolidado.participacao as participacaoAcumulada, ");
		sql.append("         @posicaoRanking\\:=@posicaoRanking + 1 as rkEditor, ");
		sql.append("       consolidado.vendaExemplares as vendaExemplares, ");
		sql.append("       consolidado.reparte as reparte, ");
		sql.append("       consolidado.valorMargemDistribuidor as valorMargemDistribuidor, ");
		sql.append("       (consolidado.valorMargemDistribuidor/consolidado.faturamentoCapa*100) as porcentagemMargemDistribuidor,  ");
		sql.append("       consolidado.numeroCota as numeroCota, ");
		sql.append("       consolidado.nomeCota as nomeCota, ");
		sql.append("       consolidado.endereco as endereco, ");
		sql.append("       consolidado.tipoPDV as tipoPDV, ");
		sql.append("       consolidado.documentoPessoa as documentoPessoa, ");
		sql.append("       consolidado.situacaoAtivo as  situacaoAtivo,");
		sql.append("       consolidado.tipoGeradorFluxo as  tipoGeradorFluxo, ");
		sql.append("       consolidado.areaInfluenciaPDV as  areaInfluenciaPDV");
		sql.append("        ");
		sql.append("       FROM  ");
		sql.append("       ( ");
		sql.append("         SELECT ");
		sql.append("          ");
		sql.append("         temp.documentoPessoa as documentoPessoa, ");
		sql.append("         temp.tipoPDV as tipoPDV, ");
		sql.append("         temp.endereco as endereco, ");
		sql.append("         temp.nomeCota as nomeCota, ");
		sql.append("         temp.numeroCota as numeroCota, ");
		sql.append("         temp.codEditor as codigoEditor, ");
		sql.append("         temp.nomeEditor as nomeEditor, ");
		sql.append("         sum(temp.vendaSum * temp.PRECO_VENDA) as faturamentoCapa, ");
		sql.append("         sum(temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.valorDesconto,0)) / 100))) AS participacao, ");
		sql.append("         sum(temp.vendaSum) as vendaExemplares, ");
		sql.append("         sum(temp.reparteSum) as reparte, ");
		sql.append("          ");
		sql.append("         sum((temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.valorDesconto,0)) / 100))) - ");
		sql.append("              (temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.percentualDescLog, ");
		sql.append("                 temp.percentualDescProd,0))/100)) ");
		sql.append("               )) as valorMargemDistribuidor, ");
		sql.append("       temp.situacaoAtivo as  situacaoAtivo, ");
		sql.append("       temp.tipoGeradorFluxo as  tipoGeradorFluxo, ");
		sql.append("       temp.areaInfluenciaPDV as  areaInfluenciaPDV ");
		sql.append("  ");
		sql.append("     FROM ");
		sql.append("         (           ");
		sql.append("           Select ");
		sql.append("              ");
		sql.append("             T.codigoEditor as codEditor, ");
		sql.append("             T.razaoSocialEditor as nomeEditor, ");
		sql.append("             SUM(T.reparte) reparteSum, ");
		sql.append("             SUM(T.venda) vendaSum, ");
		sql.append("             T.PRECO_VENDA, ");
		sql.append("             T.valorDesconto, ");
		sql.append("             T.percentualDescLog as percentualDescLog, ");
		sql.append("             T.percentualDescProd as percentualDescProd, ");
		sql.append("             T.NUMERO_COTA as numeroCota, ");
		sql.append("             T.lancID, ");
		sql.append("             T.nomeCota as nomeCota, ");
		sql.append("             T.endereco as endereco, ");
		sql.append("             T.tipoPDV as tipoPDV, ");
		sql.append("             T.documentoPessoa as documentoPessoa, ");
		sql.append("             T.tipoGeradorFluxo as tipoGeradorFluxo, ");
		sql.append("             T.areaInfluenciaPDV as areaInfluenciaPDV, ");
		sql.append("       T.situacaoAtivo as  situacaoAtivo ");
		sql.append("           FROM ");
		sql.append("             (SELECT ");
		sql.append("                 tgf.descricao as  tipoGeradorFluxo, ");
		sql.append("                 aip.descricao as  areaInfluenciaPDV, ");
		sql.append("                 editor.ativo as  situacaoAtivo, ");
		sql.append("                 tpp.descricao as tipoPDV, ");
		sql.append("                 endereco.logradouro as endereco, ");
		sql.append("                 editor.ID as codigoEditor, ");
		sql.append("                 pessoaeditor.RAZAO_SOCIAL as razaoSocialEditor, ");
		sql.append("                 mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		sql.append("                 c.NUMERO_COTA AS NUMERO_COTA, ");
		sql.append("                 pe.PRECO_VENDA AS PRECO_VENDA, ");
		sql.append("                 descontologistica.PERCENTUAL_DESCONTO as percentualDescLog, ");
		sql.append("                 descontologisticaproduto.PERCENTUAL_DESCONTO as percentualDescProd, ");
		sql.append("                 cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN  ");
		sql.append("                             if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)                   ");
		sql.append("                             ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)               ");
		sql.append("                          end ) as unsigned int) AS reparte, ");
		sql.append("                 (case                    ");
		sql.append("                     when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO')then       ");
		sql.append("                         cast(sum(CASE WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN  ");
		sql.append("                           if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)                       ");
		sql.append("                           ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)                   ");
		sql.append("                     END) - (select ");
		sql.append("                         sum(mecEncalhe.qtde)                               ");
		sql.append("                     from ");
		sql.append("                         lancamento lanc                               ");
		sql.append("                     LEFT JOIN ");
		sql.append("                         chamada_encalhe_lancamento cel                            ");
		sql.append("                             on cel.LANCAMENTO_ID = lanc.ID                               ");
		sql.append("                     LEFT JOIN ");
		sql.append("                         chamada_encalhe ce                            ");
		sql.append("                             on ce.id = cel.CHAMADA_ENCALHE_ID                               ");
		sql.append("                     LEFT JOIN ");
		sql.append("                         chamada_encalhe_cota cec                            ");
		sql.append("                             on cec.CHAMADA_ENCALHE_ID = ce.ID                               ");
		sql.append("                     LEFT JOIN ");
		sql.append("                         cota cota                            ");
		sql.append("                             on cota.id = cec.COTA_ID                               ");
		sql.append("                     LEFT JOIN ");
		sql.append("                         conferencia_encalhe confEnc                            ");
		sql.append("                             on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID                               ");
		sql.append("                     LEFT JOIN ");
		sql.append("                         movimento_estoque_cota mecEncalhe                            ");
		sql.append("                             on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID                   ");
		sql.append("                     WHERE ");
		sql.append("                         lanc.id = l.id                        ");
		sql.append("                         and cota.id = c.id) AS SIGNED INT)                         ");
		sql.append("                     else      null                     ");
		sql.append("                 end) as venda, ");
		sql.append("                 l.id as lancID, ");
		sql.append("                     case when pess.tipo = 'J' then pess.razao_social ");
		sql.append("                     when  pess.tipo = 'F' then pess.nome ");
		sql.append("                     end as nomeCota, ");
		sql.append("                     case when pess.tipo = 'J' then pess.cnpj ");
		sql.append("                     when  pess.tipo = 'F' then pess.cpf ");
		sql.append("                     end as documentoPessoa ");
		sql.append("             FROM ");
		sql.append("                 lancamento l                 ");
		sql.append("             JOIN ");
		sql.append("                 produto_edicao pe                    ");
		sql.append("                     ON pe.id = l.produto_edicao_id                 ");
		sql.append("             LEFT JOIN ");
		sql.append("                 periodo_lancamento_parcial plp                    ");
		sql.append("                     ON plp.id = l.periodo_lancamento_parcial_id                 ");
		sql.append("             JOIN ");
		sql.append("                 produto p                    ");
		sql.append("                     ON p.id = pe.produto_id                   ");
		sql.append("             LEFT JOIN ");
		sql.append("                 movimento_estoque_cota mecReparte                    ");
		sql.append("                     on mecReparte.LANCAMENTO_ID = l.id                 ");
		sql.append("             LEFT JOIN ");
		sql.append("                 tipo_movimento tipo                    ");
		sql.append("                     ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID                 ");
		sql.append("             JOIN ");
		sql.append("                 cota c                    ");
		sql.append("                     ON mecReparte.COTA_ID = c.ID           ");
		sql.append("             JOIN ");
		sql.append("                 pessoa pess                   ");
		sql.append("                     ON c.PESSOA_ID = pess.ID           ");
		sql.append("             inner join ");
		sql.append("                 endereco_cota endCota                    ");
		sql.append("                     ON endCota.cota_id = c.id                    ");
		sql.append("                     and endCota.principal = true                    ");
		sql.append("             inner join ");
		sql.append("                 endereco endereco                    ");
		sql.append("                     ON endCota.endereco_id = endereco.id      ");
		sql.append("             JOIN ");
		sql.append("                 produto_fornecedor prodFornecedor                   ");
		sql.append("                     ON prodFornecedor.PRODUTO_ID = p.ID           ");
		sql.append("             JOIN ");
		sql.append("                 fornecedor                    ");
		sql.append("                     ON prodFornecedor.fornecedores_ID = fornecedor.ID           ");
		sql.append("             JOIN ");
		sql.append("                 editor  ");
		sql.append("                     ON editor.id = p.editor_id ");
		sql.append("             JOIN ");
		sql.append("                 pessoa pessoaeditor  ");
		sql.append("                     on editor.juridica_id = pessoaeditor.id ");
		sql.append("             left join  ");
		sql.append("                 desconto_logistica descontologistica  ");
		sql.append("                     on descontologistica.id = pe.desconto_logistica_id    	 ");
		sql.append(" 		        left join  ");
		sql.append("                 desconto_logistica descontologisticaproduto  ");
		sql.append("                     on descontologisticaproduto.id = p.desconto_logistica_id ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     PDV as pdv                        ");
		sql.append("                         on  pdv.cota_id = c.id");
		sql.append("                 LEFT JOIN ");
		sql.append("                     GERADOR_FLUXO_PDV as gfp                        ");
		sql.append("                         on gfp.pdv_id =pdv.id");
		sql.append("                 LEFT JOIN ");
		sql.append("                     TIPO_GERADOR_FLUXO_PDV as tgf                        ");
		sql.append("                         on tgf.id = gfp.TIPO_GERADOR_FLUXO_ID");
		sql.append("                 LEFT JOIN ");
		sql.append("                     AREA_INFLUENCIA_PDV as aip                        ");
		sql.append("                         on aip.id= pdv.AREA_INFLUENCIA_PDV_ID");
		sql.append("                 LEFT JOIN ");
		sql.append("                     TIPO_PONTO_PDV tpp                        ");
		sql.append("                         on tpp.id = pdv.TIPO_PONTO_PDV_id               ");
		sql.append("             WHERE ");
		sql.append("                  l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')  ");
		sql.append(" 		             and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' AND PDV.PONTO_PRINCIPAL = TRUE ");
		sql.append(              this.getWhereFiltroCurvaABC(filtro, null));
		sql.append("             group by ");
		sql.append("                 pe.numero_edicao, ");
		sql.append("                 pe.id, ");
		sql.append("                 mecReparte.cota_id, ");
		sql.append("                 plp.numero_periodo            ");
		sql.append("             ORDER BY ");
		sql.append("                 l.ID desc )T                            ");
		sql.append("             group by ");
		sql.append("                 NUMERO_COTA, ");
		sql.append("                 codigoEditor, ");
		sql.append("                 lancID ");
		sql.append("             ORDER BY ");
		sql.append("                 vendaSum desc) temp                          ");
		sql.append("         group by ");
		sql.append("             codEditor ");
		sql.append("         ORDER BY ");
		sql.append("             faturamentoCapa desc) consolidado, ");
		sql.append("             (select ");
		sql.append("                 @valorAcumulado\\:=0, ");
		sql.append("                 @posicaoRanking\\:=0) as s   ");
		sql.append("                  ");
		sql.append("                 ORDER BY rkEditor ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		this.getWhereFiltroCurvaABC(filtro, query);
		
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
		
		query.addScalar("numeroCota", StandardBasicTypes.STRING);
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		query.addScalar("documentoPessoa", StandardBasicTypes.STRING);
		query.addScalar("situacaoAtivo", StandardBasicTypes.STRING);
		query.addScalar("tipoGeradorFluxo", StandardBasicTypes.STRING);
		query.addScalar("areaInfluenciaPDV", StandardBasicTypes.STRING);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCEditorVO.class));
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("     SELECT ");
		sql.append("         consolidado.PRODUTO_EDICAO_ID AS idProdutoEdicao, ");
		sql.append("         consolidado.CODIGO_PRODUTO AS codigoProduto, ");
		sql.append("         consolidado.NOME_PRODUTO AS nomeProduto, ");
		sql.append("         consolidado.classificacaoProduto, ");
		sql.append("         consolidado.NUMERO_EDICAO AS edicaoProduto, ");
		sql.append("         @valorAcumulado\\:=@valorAcumulado + consolidado.valor as participacaoAcumulada, ");
		sql.append("         @posicaoRanking\\:=@posicaoRanking + 1 as rkProduto, ");
		sql.append("         consolidado.valor as participacao, ");
		sql.append("         consolidado.vendaExemplares as vendaExemplares, ");
		sql.append("         consolidado.faturamentoCapa as faturamento, ");
		sql.append("         consolidado.reparte as reparte,   ");
		sql.append("         consolidado.NUMERO_COTA as numeroCota,   ");
		sql.append("         consolidado.nomeCota as nomeCota, ");
		sql.append("         consolidado.tipoPDV as tipoPDV, ");
		sql.append("         consolidado.precoCapa, ");
		sql.append("         consolidado.nomeEditor, ");
		sql.append("         consolidado.descricaoSegmento ");
		sql.append("          ");
		sql.append("     FROM ");
		sql.append("         (           ");
		sql.append("         SELECT ");
		sql.append("             temp.prodEdicaoID as PRODUTO_EDICAO_ID, ");
		sql.append("             temp.codigoProduto as CODIGO_PRODUTO, ");
		sql.append("             temp.nomeProduto as NOME_PRODUTO, ");
		sql.append("             temp.numeroEdicao as NUMERO_EDICAO, ");
		sql.append("             sum(temp.vendaSum * temp.PRECO_VENDA) as faturamentoCapa, ");
		sql.append("             sum(temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.valorDesconto,0)) / 100))) AS valor, ");
		sql.append("             sum(temp.vendaSum) as vendaExemplares, ");
		sql.append("             sum(temp.reparteSum) as reparte, ");
		sql.append("             temp.NUMERO_COTA as NUMERO_COTA, ");
		sql.append("             temp.nomeCota as nomeCota, ");
		sql.append("             temp.tipoPDV as tipoPDV, ");
		sql.append("             temp.precoCapa, ");
		sql.append("             temp.nomeEditor, ");
		sql.append("         	 temp.classificacaoProduto, ");
		sql.append("             temp.descricaoSegmento ");
		sql.append("         FROM ");
		sql.append("             (                       ");
		sql.append("             SELECT ");
		sql.append("                 T.idPe as prodEdicaoID, ");
		sql.append("                 T.codPd as codigoProduto, ");
		sql.append("                 T.nomeProduto as nomeProduto, ");
		sql.append("                 T.numEdicao as numeroEdicao, ");
		sql.append("                 T.valorDesconto as valorDesconto, ");
		sql.append("                 T.NUMERO_COTA as NUMERO_COTA, ");
		sql.append("                 T.PRECO_VENDA as PRECO_VENDA, ");
		sql.append("                 SUM(T.reparte) as reparteSum, ");
		sql.append("                 SUM(T.venda) as vendaSum, ");
		sql.append("                 T.nomeCota as nomeCota, ");
		sql.append("                 T.tipoPDV as tipoPDV, ");
		sql.append("                 T.precoCapa, ");
		sql.append("                 T.nomeEditor, ");
		sql.append("         		 T.classificacaoProduto, ");
		sql.append("                 T.descricaoSegmento ");
		sql.append("             FROM ");
		sql.append("                 (SELECT ");
		sql.append("                     pe.id as idPe, ");
		sql.append("                     p.CODIGO as codPd, ");
		sql.append("                     p.NOME as nomeProduto, ");
		sql.append("                     pe.NUMERO_EDICAO as numEdicao, ");
		sql.append("                     mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		sql.append("                     c.NUMERO_COTA AS NUMERO_COTA, ");
		sql.append("                     pe.PRECO_VENDA AS PRECO_VENDA, ");
		sql.append("                     tpp.descricao AS tipoPDV, ");
		sql.append("                     cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN                                ");
		sql.append("                                   if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0) ");
		sql.append("                                   ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)                                          ");
		sql.append("                              end ) as unsigned int) AS reparte, ");
		sql.append("                    coalesce( (case when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO')then                                 ");
		sql.append("                           cast(sum(CASE WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN                              ");
		sql.append("                                       if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)  ");
		sql.append("                                       ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)                                         ");
		sql.append("                                    END) - (select sum(mecEncalhe.qtde)                                                     ");
		sql.append("                         from ");
		sql.append("                             lancamento lanc                                                     ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe_lancamento cel                                                          ");
		sql.append("                                 on cel.LANCAMENTO_ID = lanc.ID                                                     ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe ce                                                          ");
		sql.append("                                 on ce.id = cel.CHAMADA_ENCALHE_ID                                                     ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe_cota cec                                                          ");
		sql.append("                                 on cec.CHAMADA_ENCALHE_ID = ce.ID                                                     ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             cota cota                                                          ");
		sql.append("                                 on cota.id = cec.COTA_ID                                                     ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             conferencia_encalhe confEnc                                                          ");
		sql.append("                                 on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID                                                     ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             movimento_estoque_cota mecEncalhe                                                          ");
		sql.append("                                 on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID                                         ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             pessoa as p");
		sql.append("                                 on p.id = cota.pessoa_ID                                         ");
		sql.append("                         WHERE ");
		sql.append("                             lanc.id = l.id                                                  ");
		sql.append("                             and cota.id = c.id) AS SIGNED INT)                                               ");
		sql.append("                         else      null                                       ");
		sql.append("                     end),0) as venda,                                       ");
		sql.append("                     case when pess.tipo = 'J' then pess.razao_social ");
		sql.append("                    	  when  pess.tipo = 'F' then pess.nome ");
		sql.append("                     end as nomeCota, ");
		sql.append("                     case when pessoaeditor.tipo = 'J' then pessoaeditor.razao_social ");
		sql.append("                     	  when pessoaeditor.tipo = 'F' then pessoaeditor.nome ");
		sql.append("                     end as nomeEditor, ");
		sql.append("                     tsp.descricao as descricaoSegmento, ");
		sql.append("         	 		 tcp.descricao as classificacaoProduto, ");
		sql.append("                     COALESCE(PE.PRECO_VENDA, 0) as precoCapa ");
		sql.append("                 FROM ");
		sql.append("                     lancamento l                               ");
		sql.append("                 JOIN ");
		sql.append("                     produto_edicao pe                                          ");
		sql.append("                         ON pe.id = l.produto_edicao_id                               ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     periodo_lancamento_parcial plp                                          ");
		sql.append("                         ON plp.id = l.periodo_lancamento_parcial_id                               ");
		sql.append("                 JOIN ");
		sql.append("                     produto p                                          ");
		sql.append("                         ON p.id = pe.produto_id                                 ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     movimento_estoque_cota mecReparte                                          ");
		sql.append("                         on mecReparte.LANCAMENTO_ID = l.id                               ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     tipo_movimento tipo                                          ");
		sql.append("                         ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID                               ");
		sql.append("                 JOIN ");
		sql.append("                     cota c                                          ");
		sql.append("                         ON mecReparte.COTA_ID = c.ID                         ");
		sql.append("                 JOIN ");
		sql.append("                     pessoa pess                                         ");
		sql.append("                         ON c.PESSOA_ID = pess.ID                         ");
		sql.append("                 inner join ");
		sql.append("                     endereco_cota endCota                                          ");
		sql.append("                         ON endCota.cota_id = c.id                                          ");
		sql.append("                         and endCota.principal = true                                  ");
		sql.append("                 inner join ");
		sql.append("                     endereco endereco                                          ");
		sql.append("                         ON endCota.endereco_id = endereco.id                    ");
		sql.append("                 JOIN ");
		sql.append("                     produto_fornecedor prodFornecedor                                         ");
		sql.append("                         ON prodFornecedor.PRODUTO_ID = p.ID                         ");
		sql.append("                 JOIN ");
		sql.append("                     fornecedor                                          ");
		sql.append("                         ON prodFornecedor.fornecedores_ID = fornecedor.ID                         ");
		sql.append("                 JOIN ");
		sql.append("                     editor                        ");
		sql.append("                         ON editor.id = p.editor_id               ");
		sql.append("                 JOIN ");
		sql.append("                     pessoa pessoaeditor                        ");
		sql.append("                         on editor.juridica_id = pessoaeditor.id               ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     PDV as pdv                        ");
		sql.append("                         on  pdv.cota_id = c.id");
		sql.append("                 LEFT JOIN ");
		sql.append("                     TIPO_PONTO_PDV tpp                        ");
		sql.append("                         on tpp.id = pdv.TIPO_PONTO_PDV_id               ");
		sql.append("                 JOIN ");
		sql.append("                     tipo_segmento_produto tsp                         ");
		sql.append("                         on tsp.id = p.TIPO_SEGMENTO_PRODUTO_ID               ");
		sql.append("                 JOIN ");
		sql.append("                     tipo_classificacao_produto tcp                          ");
		sql.append("                         on tcp.id = pe.tipo_classificacao_produto_id              ");
		sql.append("                 WHERE ");
		sql.append("                     l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')                   ");
		sql.append("                     and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' AND PDV.ponto_principal = true");
		sql.append(              this.getWhereFiltroCurvaABC(filtro, null));
		sql.append("                 group by ");
		sql.append("                     pe.numero_edicao, ");
		sql.append("                     pe.id, ");
		sql.append("                     mecReparte.cota_id, ");
		sql.append("                     plp.numero_periodo                          ");
		sql.append("                 ORDER BY ");
		sql.append("                     l.ID desc )T                                          ");
		sql.append("                 group by ");
		sql.append("                     NUMERO_COTA, codigoProduto, numeroEdicao ");
		sql.append("                 ORDER BY ");
		sql.append("                     vendaSum desc) temp                                    ");
		sql.append("             group by ");
		sql.append("                 prodEdicaoID           ");
		sql.append("             ORDER BY ");
		sql.append("                 faturamentoCapa desc) consolidado, ");
		sql.append("                 (select ");
		sql.append("                     @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s ");
		sql.append("             ORDER BY ");
		sql.append("                 rkProduto ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		this.getWhereFiltroCurvaABC(filtro, query);
		
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
		query.addScalar("numeroCota", StandardBasicTypes.STRING);
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		
		query.addScalar("tipoPDV", StandardBasicTypes.STRING);
		query.addScalar("classificacaoProduto", StandardBasicTypes.STRING);
		
		query.addScalar("nomeEditor", StandardBasicTypes.STRING);
		query.addScalar("descricaoSegmento", StandardBasicTypes.STRING);
		query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCCotaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroRankingSegmentoDTO> obterRankingSegmento(FiltroRankingSegmentoDTO filtro) {

		StringBuilder sql = new StringBuilder();
		
		getQueryRankingSegmento(filtro, sql);
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("tipoSegmentoID", filtro.getIdTipoSegmento());
		
		this.getWhereFiltroCurvaABC(filtro, query);

		query.addScalar("ranking", StandardBasicTypes.LONG);
		query.addScalar("participacaoAcumulada", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeCota");
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("participacao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("segmentoDescricao", StandardBasicTypes.STRING);
		query.addScalar("reparteSum", StandardBasicTypes.STRING);
		query.addScalar("reparte", StandardBasicTypes.STRING);
		query.addScalar("venda", StandardBasicTypes.STRING);
		

		query.setResultTransformer(Transformers.aliasToBean(RegistroRankingSegmentoDTO.class));
		
		return query.list();
	}

	private void getQueryRankingSegmento(FiltroRankingSegmentoDTO filtro, StringBuilder sql) {
		sql.append("      SELECT ");
		sql.append("         @posicaoRanking\\:=@posicaoRanking + 1 as ranking, ");
		sql.append("         @valorAcumulado\\:=@valorAcumulado + consolidado.faturamentoCapa as participacaoAcumulada, ");
		sql.append("         consolidado.numeroCota as numeroCota, ");
		sql.append("         consolidado.nomeCota AS nomeCota, ");
		sql.append("         consolidado.faturamentoCapa as faturamentoCapa, ");
		sql.append("         consolidado.participacao as participacao, ");
		sql.append("         consolidado.segmentoDescricao as segmentoDescricao, ");
		sql.append("         consolidado.reparteSum as reparteSum, ");
		sql.append("         consolidado.reparte as reparte, ");
		sql.append("         consolidado.venda as venda ");
		sql.append("     FROM ");
		sql.append("         (SELECT ");
		sql.append("             temp.cotaId as idCota, ");
		sql.append("             temp.numeroCota numeroCota, ");
		sql.append("             COALESCE(temp.NOME_COTA,temp.RAZAO_SOCIAL_COTA) AS nomeCota, ");
		sql.append("             temp.CIDADE_COTA AS municipio, ");
		sql.append("             sum(temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.valorDesconto,0)) / 100))) as participacao, ");
		sql.append("             sum(temp.vendaSum) as vendaExemplares, ");
		sql.append("             sum(temp.vendaSum * temp.PRECO_VENDA) as faturamentoCapa,             ");
		sql.append("             temp.segmentoDescricao as segmentoDescricao,             ");
		sql.append("             temp.reparteSum as reparteSum,             ");
		sql.append("             temp.reparte as reparte, ");
		sql.append("             temp.vendaSum as venda ");
		sql.append("         FROM ");
		sql.append("             (Select ");
		sql.append("                 T.NUMERO_COTA as numeroCota, ");
		sql.append("                 T.COTA_ID as cotaId, ");
		sql.append("                 T.NOME_COTA, ");
		sql.append("                 T.CIDADE_COTA, ");
		sql.append("                 T.RAZAO_SOCIAL_COTA, ");
		sql.append("                 SUM(T.reparte) reparteSum, ");
		sql.append("                 SUM(T.venda) vendaSum, ");
		sql.append("                 T.PRECO_VENDA, ");
		sql.append("                 T.valorDesconto, ");
		sql.append("                 T.lancId as lancId,                      ");
		sql.append("                 T.segmentoDescricao as segmentoDescricao,                      ");
		sql.append("                 T.reparte as reparte                      ");
		sql.append("             from ");
		sql.append("                 ( SELECT ");
		sql.append("                     mecReparte.COTA_ID AS COTA_ID, ");
		sql.append("                     mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		sql.append("                     c.NUMERO_COTA AS NUMERO_COTA, ");
		sql.append("                     IF(pess.tipo='F',pess.NOME,pess.nome_fantasia) AS NOME_COTA, ");
		sql.append("                     if(pess.tipo='F',pess.nome,pess.RAZAO_SOCIAL) AS RAZAO_SOCIAL_COTA, ");
		sql.append("                     endereco.CIDADE AS CIDADE_COTA, ");
		sql.append("                     pe.PRECO_VENDA AS PRECO_VENDA, ");
		sql.append("                     l.id as lancId, ");
		sql.append("                     tsp.descricao as segmentoDescricao, ");
		sql.append("                     cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN  ");
		sql.append("                           if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)                   ");
		sql.append("                         ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)               ");
		sql.append("                     end ) as unsigned int) AS reparte, ");
		sql.append("                     (case                    ");
		sql.append("                         when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO') then       ");
		sql.append("                             cast(sum(CASE WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN  ");
		sql.append("                                 if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)                       ");
		sql.append("                                 ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)                   ");
		sql.append("                                       END)         ");
		sql.append("                         - (select ");
		sql.append("                             sum(mecEncalhe.qtde)                               ");
		sql.append("                         from ");
		sql.append("                             lancamento lanc                               ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe_lancamento cel                            ");
		sql.append("                                 on cel.LANCAMENTO_ID = lanc.ID                               ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe ce                            ");
		sql.append("                                 on ce.id = cel.CHAMADA_ENCALHE_ID                               ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             chamada_encalhe_cota cec                            ");
		sql.append("                                 on cec.CHAMADA_ENCALHE_ID = ce.ID                               ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             cota cota                            ");
		sql.append("                                 on cota.id = cec.COTA_ID                               ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             conferencia_encalhe confEnc                            ");
		sql.append("                                 on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID                               ");
		sql.append("                         LEFT JOIN ");
		sql.append("                             movimento_estoque_cota mecEncalhe                            ");
		sql.append("                                 on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID                   ");
		sql.append("                         WHERE ");
		sql.append("                             lanc.id = l.id                        ");
		sql.append("                             and cota.id = c.id) AS SIGNED INT)                         ");
		sql.append("                         else      null                     ");
		sql.append("                     end) as venda                         ");
		sql.append("                 FROM ");
		sql.append("                     lancamento l                 ");
		sql.append("                 JOIN ");
		sql.append("                     produto_edicao pe                    ");
		sql.append("                         ON pe.id = l.produto_edicao_id                 ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     periodo_lancamento_parcial plp                    ");
		sql.append("                         ON plp.id = l.periodo_lancamento_parcial_id                 ");
		sql.append("                 JOIN ");
		sql.append("                     produto p                    ");
		sql.append("                         ON p.id = pe.produto_id                   ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     movimento_estoque_cota mecReparte                    ");
		sql.append("                         on mecReparte.LANCAMENTO_ID = l.id                 ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     tipo_movimento tipo                    ");
		sql.append("                         ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID                 ");
		sql.append("                 JOIN ");
		sql.append("                     cota c                    ");
		sql.append("                         ON mecReparte.COTA_ID = c.ID           ");
		sql.append("                 JOIN ");
		sql.append("                     pessoa pess                   ");
		sql.append("                         ON c.PESSOA_ID = pess.ID           ");
		sql.append("                 inner join ");
		sql.append("                     endereco_cota endCota                    ");
		sql.append("                         ON endCota.cota_id = c.id                    ");
		sql.append("                         and endCota.principal = true                    ");
		sql.append("                 inner join ");
		sql.append("                     endereco endereco                    ");
		sql.append("                         ON endCota.endereco_id = endereco.id      ");
		sql.append("                 JOIN ");
		sql.append("                     produto_fornecedor prodFornecedor                   ");
		sql.append("                         ON prodFornecedor.PRODUTO_ID = p.ID           ");
		sql.append("                 JOIN ");
		sql.append("                     fornecedor                    ");
		sql.append("                         ON prodFornecedor.fornecedores_ID = fornecedor.ID ");
		sql.append("                          ");
		sql.append("                 JOIN  ");
		sql.append("                     tipo_segmento_produto tsp ");
		sql.append("                     ON tsp.ID = p.TIPO_SEGMENTO_PRODUTO_ID ");
		sql.append("                    ");
		sql.append("                 WHERE ");
		sql.append("                     l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')               ");
		sql.append("                     AND tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'     ");
		sql.append("                     AND tsp.ID = :tipoSegmentoID ");
					
		sql.append(this.getWhereFiltroCurvaABC(filtro, null));
		
		sql.append("                 group by ");
		sql.append("                     pe.numero_edicao, ");
		sql.append("                     pe.id, ");
		sql.append("                     mecReparte.cota_id, ");
		sql.append("                     plp.numero_periodo            ");
		sql.append("                 ORDER BY ");
		sql.append("                     l.ID desc )T                            ");
		sql.append("                 group by ");
		sql.append("                     numeroCota, ");
		sql.append("                     lancId  ");
		sql.append("                 ORDER BY ");
		sql.append("                     vendaSum desc) temp           ");
		sql.append("             group by ");
		sql.append("                 numeroCota ");
		sql.append("             ORDER BY ");
		sql.append("                 faturamentoCapa desc) consolidado, ");
		sql.append("                 (select ");
		sql.append("                     @valorAcumulado\\:=0, ");
		sql.append("                     @posicaoRanking\\:=0) as s                            ");
		sql.append("             ORDER BY ");
		sql.append("                 ranking ");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroRankingSegmentoDTO> obterRelatorioVendaSegmentoPorCota(FiltroRankingSegmentoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select  ");
		sql.append("    T2.ranking, ");
		sql.append("    T2.segmentoDescricao, ");
		sql.append("    T2.faturamentoCapa, ");
		sql.append("    T2.participacao, ");
		sql.append("    T2.participacaoAcumulada, ");
		sql.append("    T2.numeroCota, ");
		sql.append("    T2.nomeCota, ");
		sql.append("    T2.reparteSum, ");
		sql.append("    T2.reparte, ");
		sql.append("    T2.venda ");
		
		sql.append(" FROM  ");
		sql.append(" 	(  ");

		getQueryRankingSegmento(filtro, sql);
		
		sql.append(" ) T2 where T2.numeroCota = :numeroCota  ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("tipoSegmentoID", filtro.getIdTipoSegmento());
		query.setParameter("numeroCota", filtro.getNumeroCota());
		
		this.getWhereFiltroCurvaABC(filtro, query);

		query.addScalar("ranking", StandardBasicTypes.LONG);
		query.addScalar("participacaoAcumulada", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeCota");
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("participacao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("segmentoDescricao", StandardBasicTypes.STRING);
		query.addScalar("reparteSum", StandardBasicTypes.STRING);
		query.addScalar("reparte", StandardBasicTypes.STRING);
		query.addScalar("venda", StandardBasicTypes.STRING);
		

		query.setResultTransformer(Transformers.aliasToBean(RegistroRankingSegmentoDTO.class));
		
		return query.list();
	}
	
	private String getWhereFiltroCurvaABC(FiltroCurvaABCDTO filtro, Query query) {
		
		StringBuilder sql = new StringBuilder();
		
		if (filtro.getDataDe() != null && filtro.getDataAte() != null) {

			if (query == null){
				sql.append(" AND l.DATA_REC_DISTRIB BETWEEN DATE_FORMAT(:dataDe,'%Y-%m-%d') AND DATE_FORMAT(:dataAte,'%Y-%m-%d') ");
			} else {
				query.setParameter("dataDe", filtro.getDataDe());
				query.setParameter("dataAte", filtro.getDataAte());
			}
			
		}
		
		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			
			if (query == null){
				
				sql.append(" AND p.CODIGO = :codigoProduto ");
			} else {
				
				query.setParameter("codigoProduto", filtro.getCodigoProduto());
			}
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			
			if (query == null){
				
				sql.append("AND fornecedor.ID = :codigoFornecedor ");
			} else {
				
				query.setParameter("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
			}
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			
			if (query == null){
				
				sql.append(" AND pe.numero_edicao in (:edicaoProduto) ");

			} else {
				
				query.setParameterList("edicaoProduto", filtro.getEdicaoProduto());
			}
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			
			if (query == null){
				
				sql.append(" AND p.EDITOR_ID = :codigoEditor ");
			} else {
				
				query.setParameter("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
			}
		}

		if (filtro.getCodigoCota() != null) {
			
			if (query == null){
				
				sql.append(" AND c.NUMERO_COTA = :codigoCota ");
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
		
		if(filtro.getRegiaoID() != null && !filtro.getNumCotasDentroDaRegiao().isEmpty()){
			
			if (query == null){
				sql.append(" AND c.NUMERO_COTA in (:listCota) ");
			} else {
				query.setParameterList("listCota", filtro.getNumCotasDentroDaRegiao());
			}
		}
		
		if (query == null){
			
			return sql.toString();
		}
		
		return null;
	}
	
}
