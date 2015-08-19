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
import br.com.abril.nds.dto.TotalizadorRankingSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO.TipoConsultaCurvaABC;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroRankingSegmentoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RelatorioVendasRepository;
import br.com.abril.nds.vo.PaginacaoVO;

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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro, TipoPesquisaRanking tipoPesquisa) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT    ");
		sql.append("       consolidado.idCota as idCota, ");
		
		if(TipoPesquisaRanking.RankingCota.equals(tipoPesquisa)){
			sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkCota, ");
		} else {
			sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkProduto, ");
		}

		sql.append("       @valorAcumulado\\:=@valorAcumulado + consolidado.participacao as participacaoAcumulada, ");
		sql.append("       consolidado.numeroCota as numeroCota, ");
		sql.append("       consolidado.nomeCota AS nomeCota, ");
		sql.append("       consolidado.municipio AS municipio, ");
		sql.append("       consolidado.participacao as participacao, ");
		sql.append("       consolidado.vendaExemplares as vendaExemplares, ");
		sql.append("       consolidado.faturamentoCapa as faturamentoCapa  ");
		sql.append("   FROM ");
		
		sql.append("       (SELECT ");
		sql.append("         temp.cotaId as idCota, ");
		sql.append("         temp.numeroCota numeroCota, ");
		sql.append("         COALESCE(temp.NOME_COTA, temp.RAZAO_SOCIAL_COTA) AS nomeCota, ");
		sql.append("         temp.CIDADE_COTA AS municipio, ");
		sql.append("         sum(temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.valorDesconto,0)) / 100))) as participacao, ");
		sql.append("         temp.vendaSum as vendaExemplares, ");
		sql.append("         (temp.vendaSum * temp.PRECO_VENDA) as faturamentoCapa ");
		sql.append("  ");
		sql.append("         FROM ");
		sql.append("         ( ");
		sql.append("         Select  ");
		sql.append("           T.NUMERO_COTA as numeroCota, ");
		sql.append("           T.COTA_ID as cotaId, ");
		sql.append("           T.NOME_COTA, ");
		sql.append("           T.CIDADE_COTA, ");
		sql.append("           T.RAZAO_SOCIAL_COTA, ");
		sql.append("           SUM(T.reparte) reparteSum, ");
		sql.append("           SUM(T.venda) vendaSum, ");
		sql.append("           T.PRECO_VENDA, ");
		sql.append("           T.valorDesconto ");
		sql.append("            ");
		sql.append("          ");
		sql.append("         from ( ");
		sql.append("          ");
		sql.append("         SELECT ");
		sql.append("             mecReparte.COTA_ID AS COTA_ID, ");
		sql.append("             mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		sql.append("             c.NUMERO_COTA AS NUMERO_COTA, ");
		sql.append("             pess.NOME AS NOME_COTA, ");
		sql.append("             pess.RAZAO_SOCIAL AS RAZAO_SOCIAL_COTA, ");
		sql.append("             endereco.CIDADE AS CIDADE_COTA, ");
		sql.append("             pe.PRECO_VENDA AS PRECO_VENDA, ");
		sql.append("             cast(sum(case  ");
		sql.append("                 when tipo.OPERACAO_ESTOQUE = 'ENTRADA'  ");
		sql.append("                 THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) ");
		sql.append("                 ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, -mecReparte.QTDE, 0) ");
		sql.append("             end ) as unsigned int) AS reparte, ");
		sql.append("              ");
		sql.append("             (case  ");
		sql.append("                 when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO')      ");
		sql.append("                 then      cast(sum(        CASE             ");
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
		sql.append("                 LEFT JOIN ");
		sql.append("                     movimento_estoque_cota mecEncalhe  ");
		sql.append("                         on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
		sql.append("                 WHERE ");
		sql.append("                     lanc.id = l.id  ");
		sql.append("                     and cota.id = c.id) AS UNSIGNED INT)       ");
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
		
		sql.append("         WHERE ");
		
		sql.append("             l.status in (:statusLancamento) ");
		sql.append("             and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'   ");
		
		sql.append(this.getWhereFiltroCurvaABC(filtro, null, filtro.getTipoConsultaCurvaABC()));
		
		sql.append("         group by ");
		sql.append("             pe.numero_edicao, pe.id, ");
		sql.append("             mecReparte.cota_id, plp.numero_periodo  ");
		sql.append("         ORDER BY ");
		sql.append("             l.ID desc )T  ");
		sql.append("                         group by numeroCota ORDER BY vendaSum desc) temp  ");
		sql.append(" 							group by numeroCota                           ");
		sql.append(" 						        ORDER BY faturamentoCapa desc) consolidado,");
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
		
		this.getWhereFiltroCurvaABC(filtro, query, filtro.getTipoConsultaCurvaABC());
		
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
		
		this.configurarPaginacao(filtro, query);
		
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
		sql.append("       consolidado.porcentagemMargemDistribuidor as porcentagemMargemDistribuidor ");
		sql.append("        ");
		sql.append("       FROM  ");
		sql.append("       ( ");
		sql.append("         SELECT ");
		sql.append("          ");
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
		sql.append("          ");
		sql.append("         sum((temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.valorDesconto,0)) / 100))) - ");
		sql.append("             (temp.vendaSum * (temp.PRECO_VENDA - ((temp.PRECO_VENDA * coalesce(temp.percentualDescLog, ");
		sql.append("             temp.percentualDescProd,0))/100)))) / sum(((temp.vendaSum * temp.PRECO_VENDA) * 100)) ");
		sql.append("              as porcentagemMargemDistribuidor ");
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
		sql.append("             T.NUMERO_COTA as numeroCota ");
		sql.append("           FROM ");
		sql.append("             (SELECT ");
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
		sql.append("                         and cota.id = c.id) AS UNSIGNED INT)                         ");
		sql.append("                     else      null                     ");
		sql.append("                 end) as venda                         ");
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
		sql.append("             WHERE ");
		sql.append("                  l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')  ");
		sql.append(" 		             and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
		sql.append(              this.getWhereFiltroCurvaABC(filtro, null, TipoConsultaCurvaABC.DISTRIBUIDOR));
		sql.append("             group by ");
		sql.append("                 pe.numero_edicao, ");
		sql.append("                 pe.id, ");
		sql.append("                 mecReparte.cota_id, ");
		sql.append("                 plp.numero_periodo            ");
		sql.append("             ORDER BY ");
		sql.append("                 l.ID desc )T                            ");
		sql.append("             group by ");
		sql.append("                 NUMERO_COTA, ");
		sql.append("                 codigoEditor ");
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
		
		this.getWhereFiltroCurvaABC(filtro, query, TipoConsultaCurvaABC.DISTRIBUIDOR);
		
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
		
		this.configurarPaginacao(filtro, query);
		
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
		sql.append("         consolidado.NUMERO_EDICAO AS edicaoProduto, ");
		sql.append("         @valorAcumulado\\:=@valorAcumulado + consolidado.valor as participacaoAcumulada, ");
		sql.append("         @posicaoRanking\\:=@posicaoRanking + 1 as rkProduto, ");
		sql.append("         consolidado.valor as participacao, ");
		sql.append("         consolidado.vendaExemplares as vendaExemplares, ");
		sql.append("         consolidado.faturamentoCapa as faturamento, ");
		sql.append("         consolidado.reparte as reparte   ");
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
		sql.append("             sum(temp.reparteSum) as reparte ");
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
		sql.append("                 SUM(T.venda) as vendaSum ");
		sql.append("                  ");
		sql.append("             FROM ");
		sql.append("                 (SELECT ");
		sql.append("                     pe.id as idPe, ");
		sql.append("                     p.CODIGO as codPd, ");
		sql.append("                     p.NOME as nomeProduto, ");
		sql.append("                     pe.NUMERO_EDICAO as numEdicao, ");
		sql.append("                     mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		sql.append("                     c.NUMERO_COTA AS NUMERO_COTA, ");
		sql.append("                     pe.PRECO_VENDA AS PRECO_VENDA, ");
		sql.append("                     cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN                                ");
		sql.append("                                   if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0) ");
		sql.append("                                   ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)                                          ");
		sql.append("                              end ) as unsigned int) AS reparte, ");
		sql.append("                     (case when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO')then                                 ");
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
		sql.append("                         WHERE ");
		sql.append("                             lanc.id = l.id                                                  ");
		sql.append("                             and cota.id = c.id) AS UNSIGNED INT)                                               ");
		sql.append("                         else      null                                       ");
		sql.append("                     end) as venda                                       ");
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
		sql.append("                 WHERE ");
		sql.append("                     l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')                   ");
		sql.append("                     and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'   ");
//		sql.append("                     and l.DATA_REC_DISTRIB BETWEEN DATE_FORMAT('2015-08-01','%Y-%m-%d') AND DATE_FORMAT('2015-08-18','%Y-%m-%d') ");
//		sql.append("                     AND c.NUMERO_COTA = 8 ");
		sql.append(              this.getWhereFiltroCurvaABC(filtro, null, TipoConsultaCurvaABC.DISTRIBUIDOR));
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

//		sql.append(" select ");
//		
//		sql.append(" consolidado.PRODUTO_EDICAO_ID AS idProdutoEdicao, 	");
//		sql.append(" consolidado.CODIGO_PRODUTO AS codigoProduto, 		");
//		sql.append(" consolidado.NOME_PRODUTO AS nomeProduto, 			");
//		sql.append(" consolidado.NUMERO_EDICAO AS edicaoProduto, 		");
//		
//		sql.append(" @valorAcumulado\\:=@valorAcumulado + consolidado.valor as participacaoAcumulada,	");
//		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as rkProduto, ");
//		
//		sql.append(" consolidado.valor as participacao,				");
//		sql.append(" consolidado.vendaExemplares as vendaExemplares,	");
//		sql.append(" consolidado.faturamentoCapa as faturamento,	");
//		sql.append(" consolidado.reparte as reparte	");
//		
//		sql.append(" from ");
//		
//		sql.append(obterFromWhereObterCurvaABC(filtro, AgrupamentoCurvaABC.PRODUTO_EDICAO));
//		
//		sql.append(" ,(select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s ORDER BY faturamento desc, nomeProduto   ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		this.getWhereFiltroCurvaABC(filtro, query, TipoConsultaCurvaABC.DISTRIBUIDOR);
		
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
		
		this.configurarPaginacao(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroCurvaABCCotaDTO.class));
		
		return query.list();
	}

	@Override
	public TotalizadorRankingSegmentoDTO obterQuantidadeRegistrosRankingSegmento(FiltroRankingSegmentoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append("     consolidado.quantidadeRegistros as quantidadeRegistros, ");
		sql.append("     consolidado.totalFaturamentoCapa as totalFaturamentoCapa ");
		sql.append("   FROM ");
		sql.append("   ( ");
		
		sql.append(" SELECT  ");
		sql.append(" 	count(temp.NUMERO_COTA) as quantidadeRegistros,  ");
		sql.append("    sum(temp.faturamentoCapa) as totalFaturamentoCapa   ");
		
		sql.append(this.obterFromWhereRankingSegmento(filtro));
		
		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("tipoSegmentoID", filtro.getIdTipoSegmento());
		
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
		
		sql.append(" SELECT ");
		sql.append("     consolidado.ranking as ranking, ");
		sql.append("     consolidado.participacaoAcumulada as participacaoAcumulada, ");
		sql.append("     consolidado.numeroCota as numeroCota, ");
		sql.append("     consolidado.nomeCota as nomeCota, ");
		sql.append("     consolidado.faturamentoCapa as faturamentoCapa, ");
		sql.append("     consolidado.participacao as participacao ");
		sql.append("   FROM ");
		sql.append("   ( ");
		
		sql.append(" SELECT  ");
		
		sql.append("         @ranking\\:=@ranking+1 as ranking, ");
		sql.append("         @partAcum\\:=@partAcum+((temp.faturamentoCapa*100)/:totalFaturamento) as participacaoAcumulada, ");
		sql.append("         temp.NUMERO_COTA as numeroCota, ");
		sql.append("         temp.nomeCota as nomeCota, ");
		sql.append("         temp.faturamentoCapa as faturamentoCapa, ");
		sql.append("         (temp.faturamentoCapa*100)/:totalFaturamento as participacao ");
		
		sql.append(this.obterFromWhereRankingSegmento(filtro));
		
		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("tipoSegmentoID", filtro.getIdTipoSegmento());
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

		this.configurarPaginacao(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(RegistroRankingSegmentoDTO.class));
		
		return query.list();
	}
	
	private String obterFromWhereRankingSegmento(FiltroRankingSegmentoDTO filtro) {
		
		StringBuilder from = new StringBuilder();
		
		from.append(" FROM ");
		from.append("           (SELECT ");
		from.append("                 T.NUMERO_COTA as NUMERO_COTA, ");
		from.append("                 SUM(T.reparte) as reparteSum, ");
		from.append("                 SUM(T.venda) as vendaSum, ");
		from.append("                 SUM(T.venda * T.PRECO_VENDA) as faturamentoCapa, ");
		from.append("                 T.qtdRankingSegmento,  ");
		from.append("                 T.cotaId as CotaId, ");
		from.append("                 T.nomeCota as nomeCota ");
		from.append("             FROM ");
		from.append("                 (SELECT ");
		from.append("                     C.id as cotaId, ");
		from.append("                     coalesce(pess.NOME, pess.RAZAO_SOCIAL) as nomeCota, ");
		from.append("                     rs.QTDE as qtdRankingSegmento, ");
		from.append("                     mecReparte.VALOR_DESCONTO AS valorDesconto, ");
		from.append("                     c.NUMERO_COTA AS NUMERO_COTA, ");
		from.append("                     pe.PRECO_VENDA AS PRECO_VENDA, ");

		from.append("                     cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN                                ");
		from.append("                                   if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0) ");
		from.append("                                   ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)                                          ");
		from.append("                              end ) as unsigned int) AS reparte, ");

		from.append("                     (case when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO')then                                 ");
		from.append("                           cast(sum(CASE WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN                              ");
		from.append("                                       if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)  ");
		from.append("                                       ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)                                         ");
		from.append("                                    END) - (select sum(mecEncalhe.qtde)                                                     ");
		from.append("                         from ");
		from.append("                             lancamento lanc                                                     ");
		from.append("                         LEFT JOIN ");
		from.append("                             chamada_encalhe_lancamento cel                                                          ");
		from.append("                                 on cel.LANCAMENTO_ID = lanc.ID                                                     ");
		from.append("                         LEFT JOIN ");
		from.append("                             chamada_encalhe ce                                                          ");
		from.append("                                 on ce.id = cel.CHAMADA_ENCALHE_ID                                                     ");
		from.append("                         LEFT JOIN ");
		from.append("                             chamada_encalhe_cota cec                                                          ");
		from.append("                                 on cec.CHAMADA_ENCALHE_ID = ce.ID                                                     ");
		from.append("                         LEFT JOIN ");
		from.append("                             cota cota                                                          ");
		from.append("                                 on cota.id = cec.COTA_ID                                                     ");
		from.append("                         LEFT JOIN ");
		from.append("                             conferencia_encalhe confEnc                                                          ");
		from.append("                                 on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID                                                     ");
		from.append("                         LEFT JOIN ");
		from.append("                             movimento_estoque_cota mecEncalhe                                                          ");
		from.append("                                 on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID                                         ");
		from.append("                         WHERE ");
		from.append("                             lanc.id = l.id                                                  ");
		from.append("                             and cota.id = c.id) AS UNSIGNED INT)                                               ");
		from.append("                         else      null                                       ");
		from.append("                     end) as venda                                       ");
		from.append("                 FROM ");
		from.append("                     lancamento l                               ");
		from.append("                 JOIN ");
		from.append("                     produto_edicao pe                                          ");
		from.append("                         ON pe.id = l.produto_edicao_id                               ");
		from.append("                 LEFT JOIN ");
		from.append("                     periodo_lancamento_parcial plp                                          ");
		from.append("                         ON plp.id = l.periodo_lancamento_parcial_id                               ");
		from.append("                 JOIN ");
		from.append("                     produto p                                          ");
		from.append("                         ON p.id = pe.produto_id                                 ");
		from.append("                 LEFT JOIN ");
		from.append("                     movimento_estoque_cota mecReparte                                          ");
		from.append("                         on mecReparte.LANCAMENTO_ID = l.id                               ");
		from.append("                 LEFT JOIN ");
		from.append("                     tipo_movimento tipo                                          ");
		from.append("                         ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID                               ");
		from.append("                 JOIN ");
		from.append("                     cota c                                          ");
		from.append("                         ON mecReparte.COTA_ID = c.ID                         ");
		from.append("                 JOIN ");
		from.append("                     pessoa pess                                         ");
		from.append("                         ON c.PESSOA_ID = pess.ID                         ");
		from.append("                 inner join ");
		from.append("                     endereco_cota endCota                                          ");
		from.append("                         ON endCota.cota_id = c.id                                          ");
		from.append("                         and endCota.principal = true                                  ");
		from.append("                 inner join ");
		from.append("                     endereco endereco                                          ");
		from.append("                         ON endCota.endereco_id = endereco.id                    ");
		from.append("                 JOIN ");
		from.append("                     produto_fornecedor prodFornecedor                                         ");
		from.append("                         ON prodFornecedor.PRODUTO_ID = p.ID                         ");
		from.append("                 JOIN ");
		from.append("                     fornecedor                                          ");
		from.append("                         ON prodFornecedor.fornecedores_ID = fornecedor.ID                         ");
		from.append("                 JOIN ");
		from.append("                     editor                        ");
		from.append("                         ON editor.id = p.editor_id               ");
		from.append("                 JOIN ");
		from.append("                     pessoa pessoaeditor                        ");
		from.append("                         on editor.juridica_id = pessoaeditor.id         ");
		from.append("                 LEFT JOIN  ");
		from.append("                     tipo_segmento_produto tsp  ");
		from.append("                         ON p.TIPO_SEGMENTO_PRODUTO_ID = tsp.ID ");
		from.append("                 JOIN  ");
		from.append("                     ranking_segmento rs ");
		from.append("                     ON rs.TIPO_SEGMENTO_PRODUTO_ID = tsp.ID ");
		from.append("                        AND rs.COTA_ID = c.ID ");
		from.append("                 WHERE ");
		from.append("                     l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')                   ");
		
		if (filtro.getDe() != null && filtro.getAte() != null) {
			from.append(" 				  AND mecReparte.DATA BETWEEN DATE_FORMAT(:dataDe,'%Y-%m-%d') AND DATE_FORMAT(:dataAte,'%Y-%m-%d') ");
		}
		
		from.append("                     AND tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'   ");
		from.append("                     AND tsp.ID = :tipoSegmentoID ");
		from.append("                 group by ");
		from.append("                     pe.numero_edicao, ");
		from.append("                     pe.id, ");
		from.append("                     mecReparte.cota_id, ");
		from.append("                     plp.numero_periodo                          ");
		from.append("                 ORDER BY ");
		from.append("                     l.ID desc)T ");
		from.append("                 group by ");
		from.append("                     CotaId ");
		from.append("                   having ");
		from.append("                     sum(qtdRankingSegmento) > 0 ");
		from.append("                 ORDER BY ");
		from.append("                     faturamentoCapa desc) temp, ");
		from.append("                     (select ");
		from.append("                       @ranking\\:=0, ");
		from.append("                       @partAcum\\:=0) r) consolidado ");
		
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
	
	private String getWhereFiltroCurvaABC(FiltroCurvaABCDTO filtro, Query query, TipoConsultaCurvaABC tipoConsulta) {
		
		StringBuilder sql = new StringBuilder();
		
		if(tipoConsulta == TipoConsultaCurvaABC.DISTRIBUIDOR){
			if (query == null){
				sql = new StringBuilder();
				sql.append(" and l.DATA_REC_DISTRIB BETWEEN DATE_FORMAT(:dataDe,'%Y-%m-%d') AND DATE_FORMAT(:dataAte,'%Y-%m-%d') ");
			} else {
				query.setParameter("dataDe",  filtro.getDataDe());
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
		
		if (query == null){
			
			return sql.toString();
		}
		
		return null;
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {

		if(filtro.getPaginacao() == null){
			return;
		}
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}
	
}
