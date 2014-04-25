package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;

@Repository
public class DistribuicaoVendaMediaRepositoryImpl extends AbstractRepositoryModel<ProdutoEdicao, Long> implements DistribuicaoVendaMediaRepository {

    public DistribuicaoVendaMediaRepositoryImpl() {
	super(ProdutoEdicao.class);
    }

    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao) {
    	
    	return pesquisar(new FiltroEdicaoBaseDistribuicaoVendaMedia(codigoProduto, nomeProduto, edicao, idClassificacao));
    }

    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao, boolean usarICD) {
        return pesquisar(new FiltroEdicaoBaseDistribuicaoVendaMedia(codigoProduto, nomeProduto, edicao, idClassificacao), usarICD);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisarEdicoesParciais(String codigoProduto, Integer periodo, Long edicao) {
    	
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id, ");
		sql.append("       t.numero_edicao numeroEdicao, ");
		sql.append("       t.idProduto, ");
		sql.append("       t.codigo codigoProduto, ");
		sql.append("       t.nome nome, ");
		sql.append("       t.numero_periodo periodo, ");
		sql.append("       t.parcial parcial, ");
		sql.append("       t.dataLancamento, ");
		sql.append("       round(sum(t.reparte) - sum(t.encalhe)) / sum(t.reparte) * 100 percentualVenda, ");
		sql.append("       t.reparte, ");
		sql.append("       t.reparte - t.encalhe venda, ");
		sql.append("       t.status, ");
		sql.append("       t.classificacao ");
		sql.append("  from (select pe.id, ");
		sql.append("               pe.numero_edicao, ");
		sql.append("               p.codigo, ");
		sql.append("               p.id idProduto, ");
		sql.append("               p.nome, ");
		sql.append("               plp.numero_periodo, ");
		sql.append("               (case when plp.id is null then 0 else 1 end) parcial, ");
		sql.append("               l.data_lcto_prevista dataLancamento, ");
		sql.append("               sum(case when tm.grupo_movimento_estoque = 'ENVIO_JORNALEIRO' ");
		sql.append("                   then mec.qtde else 0 end) reparte, ");
		sql.append("               sum(case when tm.grupo_movimento_estoque = 'ENVIO_ENCALHE' ");
		sql.append("                   then mec.qtde else 0 end) encalhe, ");
		sql.append("               l.status status, ");
		sql.append("               tcp.descricao classificacao ");
		sql.append("          from movimento_estoque_cota mec ");
		sql.append("          join tipo_movimento tm ON tm.id = mec.tipo_movimento_id ");
		sql.append("          join lancamento l ON l.id = mec.lancamento_id ");
		sql.append("          join periodo_lancamento_parcial plp ON plp.lancamento_parcial_id = l.id ");
		sql.append("          join produto_edicao pe on pe.id = mec.produto_edicao_id ");
		sql.append("          join produto p on p.id = pe.produto_id ");
		sql.append("          join tipo_classificacao_produto tcp on tcp.id = pe.tipo_classificacao_produto_id ");
		sql.append("         where l.status in ('EXPEDIDO', 'EM BALANC RECOLHIMENTO', 'BALANCEADO RECOLHIMENTO', 'EM RECOLHIMENTO', 'FECHADO') ");
		
		if (edicao != null) {
		    sql.append("   and pe.numero_edicao = :numero_edicao ");
		}
		if (codigoProduto != null) {
		    sql.append("   and p.codigo = :codigo_produto ");
		}
		if (periodo != null) {
		    sql.append("   and plp.numero_periodo = :periodo ");
		}
		sql.append("         group by pe.numero_edicao, plp.numero_periodo) t group by t.id");
		
		Query query = getSession().createSQLQuery(sql.toString());
		if (edicao != null) {
		    query.setLong("numero_edicao", edicao);
		}
		if (codigoProduto != null) {
		    query.setString("codigo_produto", codigoProduto);
		}
		if (periodo != null) {
		    query.setInteger("periodo", periodo);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoVendaMediaDTO.class));
		return query.list();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, boolean usarICD) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("	select pe.id, ");
		sql.append("       pe.numero_edicao numeroEdicao, ");
		sql.append("       p.id idProduto, ");
		sql.append("       p.codigo codigoProduto, ");
		sql.append("       p.nome nome, ");
		sql.append("       plp.numero_periodo periodo, ");
		sql.append("       (case when plp.id is null then 0 else 1 end) parcial, ");
		sql.append("       l.data_lcto_prevista dataLancamento, ");
		
		sql.append(" CASE WHEN estqProd.QTDE IS NULL OR estqProd.QTDE=0 THEN                                                                                   ");
		sql.append("    CASE WHEN plp.NUMERO_PERIODO=1 THEN                                                                                                    ");
		sql.append("          ((SELECT reparte FROM lancamento WHERE id = l.id) - ifnull(l.REPARTE_PROMOCIONAL, 0))                                            ");
		sql.append("       WHEN plp.numero_periodo>1 THEN                                                                                                      ");
		sql.append("          (SELECT sum(lc.REPARTE) FROM lancamento lc WHERE lc.PRODUTO_EDICAO_ID = pe.id AND lc.PERIODO_LANCAMENTO_PARCIAL_ID = plp.ID)     ");
		sql.append("       ELSE                                                                                                                                ");
		sql.append("          (SELECT sum(lc.REPARTE) FROM lancamento lc WHERE lc.PRODUTO_EDICAO_ID = pe.id)                                                   ");
		sql.append("    END                                                                                                                                    ");
		sql.append(" ELSE                                                                                                                                      ");
		sql.append("   estqProd.QTDE                                                                                                                           ");
		sql.append(" END AS reparte, 																														   ");
		
		
		
//		sql.append(" (CASE   ");
//		sql.append("  	WHEN l.status = 'FECHADO' or l.status = 'RECOLHIDO' ");
//		sql.append("  THEN ");
//		sql.append("  	  round((SELECT sum(estqProdCota.qtde_recebida - estqProdCota.qtde_devolvida) ");
//		sql.append("          FROM ESTOQUE_PRODUTO_COTA estqProdCota ");
//		sql.append("               WHERE estqProdCota.PRODUTO_EDICAO_ID = pe.ID),0) ");
//		sql.append("  ELSE ");
//		sql.append("      round((SELECT sum(estqProdCota.qtde_recebida) ");
//		sql.append("          FROM ESTOQUE_PRODUTO_COTA estqProdCota ");
//		sql.append("          WHERE estqProdCota.PRODUTO_EDICAO_ID = pe.ID),0) ");
//		sql.append("  END) venda, ");
		
		sql.append("   (CASE WHEN (pe.PARCIAL=true)                                                                                   ");
		sql.append("   THEN                                                                                                           ");
		sql.append("         (SELECT coalesce(sum(CASE tipomovime11_.OPERACAO_ESTOQUE                                                 ");
		sql.append("           WHEN 'ENTRADA' THEN movimentoe10_.QTDE                                                                 ");
		sql.append("           ELSE movimentoe10_.QTDE * -1 END),0)                                                                   ");
		sql.append("                                                                                                                  ");
		sql.append("         FROM MOVIMENTO_ESTOQUE_COTA movimentoe10_                                                                ");
		sql.append("                                                                                                                  ");
		sql.append("           INNER JOIN TIPO_MOVIMENTO tipomovime11_                                                                ");
		sql.append("               ON movimentoe10_.TIPO_MOVIMENTO_ID = tipomovime11_.ID                                              ");
		sql.append("           INNER JOIN LANCAMENTO lancamento12_                                                                    ");
		sql.append("               ON movimentoe10_.LANCAMENTO_ID = lancamento12_.ID                                                  ");
		sql.append("           INNER JOIN PERIODO_LANCAMENTO_PARCIAL periodolan13_                                                    ");
		sql.append("               ON lancamento12_.PERIODO_LANCAMENTO_PARCIAL_ID = periodolan13_.ID                                  ");
		sql.append("           INNER JOIN PRODUTO_EDICAO produtoedi14_                                                                ");
		sql.append("               ON lancamento12_.PRODUTO_EDICAO_ID = produtoedi14_.ID                                              ");
		sql.append("           LEFT OUTER JOIN MOVIMENTO_ESTOQUE_COTA movimentoe15_                                                   ");
		sql.append("               ON movimentoe10_.MOVIMENTO_ESTOQUE_COTA_FURO_ID = movimentoe15_.ID                                 ");
		sql.append("                                                                                                                  ");
		sql.append("         WHERE produtoedi14_.ID = pe.ID                                                                           ");
		sql.append("           AND periodolan13_.ID = plp.ID                                                                          ");
		sql.append("           AND lancamento12_.DATA_LCTO_DISTRIBUIDOR >= l.DATA_LCTO_DISTRIBUIDOR                                   ");
		sql.append("           AND lancamento12_.DATA_REC_DISTRIB <= l.DATA_REC_DISTRIB                                               ");
		sql.append("           AND (tipomovime11_.GRUPO_MOVIMENTO_ESTOQUE IN                                                          ");
		sql.append("                   ('ENVIO_JORNALEIRO', 'FALTA_DE', 'FALTA_EM', 'SOBRA_DE', 'SOBRA_EM',                           ");
		sql.append("                     'ESTORNO_COMPRA_ENCALHE', 'ESTORNO_COMPRA_SUPLEMENTAR', 'ESTORNO_ENVIO_REPARTE',             ");
		sql.append("                     'ESTORNO_REPARTE_COTA_AUSENTE', 'ESTORNO_VENDA_ENCALHE', 'ESTORNO_VENDA_ENCALHE_SUPLEMENTAR',");
		sql.append("                     'COMPRA_ENCALHE', 'COMPRA_SUPLEMENTAR', 'RECEBIMENTO_REPARTE', 'REPARTE_COTA_AUSENTE',       ");
		sql.append("                     'VENDA_ENCALHE', 'VENDA_ENCALHE_SUPLEMENTAR'))                                               ");
		sql.append("           AND (lancamento12_.TIPO_LANCAMENTO IN ('LANCAMENTO'))                                                  ");
		sql.append("           AND (movimentoe15_.ID IS NULL))                                                                        ");
		sql.append("   ELSE                                                                                                           ");
		sql.append("       CASE                                                                                                       ");
		sql.append("             WHEN l.status = 'FECHADO' OR l.status = 'RECOLHIDO'                                                  ");
		sql.append("             THEN                                                                                                 ");
		sql.append("                 round(                                                                                           ");
		sql.append("                   (SELECT sum(estqProdCota.qtde_recebida - estqProdCota.qtde_devolvida)                          ");
		sql.append("                       FROM ESTOQUE_PRODUTO_COTA estqProdCota                                                     ");
		sql.append("                       join lancamento lc on lc.PRODUTO_EDICAO_ID = estqProdCota.PRODUTO_EDICAO_ID                ");
		sql.append("                       left join periodo_lancamento_parcial plct on plct.ID = lc.PERIODO_LANCAMENTO_PARCIAL_ID    ");
		sql.append("                     WHERE estqProdCota.PRODUTO_EDICAO_ID = pe.ID and plct.NUMERO_PERIODO = plp.numero_periodo),  ");
		sql.append("                   0)                                                                                             ");
		sql.append("             ELSE                                                                                                 ");
		sql.append("                 round(                                                                                           ");
		sql.append("                   (SELECT sum(estqProdCota.qtde_recebida)                                                        ");
		sql.append("                       FROM ESTOQUE_PRODUTO_COTA estqProdCota                                                     ");
		sql.append("                       join lancamento lc on lc.PRODUTO_EDICAO_ID = estqProdCota.PRODUTO_EDICAO_ID                ");
		sql.append("                       left join periodo_lancamento_parcial plct on plct.ID = lc.PERIODO_LANCAMENTO_PARCIAL_ID    ");
		sql.append("                     WHERE estqProdCota.PRODUTO_EDICAO_ID = pe.ID and plct.NUMERO_PERIODO = plp.numero_periodo),  ");
		sql.append("                   0)                                                                                             ");
		sql.append("         END                                                                                                      ");
		sql.append("   END                                                                                                            ");
		sql.append("   		) venda,                                                                                                   ");
 
		sql.append("       (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then ");
		sql.append("           round(sum(epc.qtde_recebida) - sum(epc.qtde_devolvida)) / sum(epc.qtde_recebida) * 100 ");
		sql.append("       else 0 end) percentualVenda, ");
		sql.append("       l.status status, ");
        sql.append("       tcp.id idClassificacao, ");
        sql.append("       coalesce(tcp.descricao, '') classificacao ");
        
		sql.append("  from lancamento l ");
		
		sql.append("  join produto_edicao pe on pe.id = l.produto_edicao_id ");
		sql.append("  left join periodo_lancamento_parcial plp on plp.id = l.periodo_lancamento_parcial_id ");
		sql.append("  join produto p on p.id = pe.produto_id ");
		sql.append("  left join estoque_produto_cota epc on epc.produto_edicao_id = pe.id ");
		sql.append("  left join tipo_classificacao_produto tcp on tcp.id = pe.tipo_classificacao_produto_id ");
		sql.append("  LEFT JOIN estoque_produto estqProd ON estqProd.PRODUTO_EDICAO_ID = pe.ID ");

		sql.append(" where l.status in ('EXPEDIDO', 'EM_BALANCEAMENTO_RECOLHIMENTO', 'EM_RECOLHIMENTO', 'BALANCEADO_RECOLHIMENTO', 'EXCLUIDO_RECOLHIMENTO', 'FECHADO', 'RECOLHIDO') ");
		
		if (filtro.getEdicao() != null) {
		    sql.append("   and pe.numero_edicao = :numero_edicao ");
		}
		if (filtro.getCodigo() != null) {
            if (usarICD) {
                sql.append("   and p.codigo_icd = :codigo_produto ");
            } else {
                sql.append("   and p.codigo = :codigo_produto ");
            }
        }
        if (filtro.getClassificacao() != null) {
			sql.append("   and tcp.id = :classificacao ");
		}
		
		sql.append(" group by pe.numero_edicao, pe.id, plp.numero_periodo ");
		sql.append(ordenarConsulta(filtro));
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if (filtro.getEdicao()  != null) {
		    query.setLong("numero_edicao", filtro.getEdicao());
		}
		if (filtro.getCodigo() != null) {			
			query.setString("codigo_produto", filtro.getCodigo());
			
		}
		if (filtro.getClassificacao()  != null) {
		    query.setLong("classificacao", filtro.getClassificacao());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoVendaMediaDTO.class));
		return query.list();
    }

    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro) {
        return pesquisar(filtro, true); //por default usar o codigo ICD
    }

    private String ordenarConsulta(FiltroEdicaoBaseDistribuicaoVendaMedia filtro) {

		StringBuilder hql = new StringBuilder();

        if (filtro.getOrdemColuna() != null) {

            switch (filtro.getOrdemColuna()) {

                case CODIGO:
                    hql.append(" ORDER BY codigoProduto ");
                    break;

                case EDICAO:
                    hql.append(" ORDER BY numeroEdicao ");
                    break;

                case DATA_LANCAMENTO:
                    hql.append(" ORDER BY dataLancamento ");
                    break;

                case REPARTE:
                    hql.append(" ORDER BY reparte ");
                    break;

                case PERIODO:
                    hql.append(" ORDER BY periodo ");
                    break;

                case CLASSIFICACAO:
                    hql.append(" ORDER BY classificacao ");
                    break;

                case STATUS:
                    hql.append(" ORDER BY status ");
                    break;

                case VENDA:
                    hql.append("ORDER BY venda ");
                    break;

                default:
                    hql.append(" ORDER BY l.data_lcto_prevista desc ");
            }

            if (filtro.getPaginacao().getOrdenacao() != null) {
                hql.append(filtro.getPaginacao().getOrdenacao().toString());
            }

        } else {
            hql.append(" ORDER BY l.data_lcto_prevista desc ");
        }

		return hql.toString();
	}
    
}
