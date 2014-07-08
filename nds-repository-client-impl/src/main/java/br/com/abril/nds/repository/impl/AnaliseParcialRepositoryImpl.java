package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseParcialRepository;

@Repository
public class AnaliseParcialRepositoryImpl extends AbstractRepositoryModel<EstudoCotaGerado, Long> implements AnaliseParcialRepository {

    public AnaliseParcialRepositoryImpl() {
        super(EstudoCotaGerado.class);
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
    public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       c.numero_cota cota, ");
        sql.append("       c.classificacao_espectativa_faturamento classificacao, ");
        sql.append("       coalesce(pes.nome, pes.razao_social, pes.nome_fantasia, '') nome, ");
        sql.append("       pdv_qtd.quantidade npdv, ");
        sql.append("       ec.qtde_efetiva reparteEstudo, ");
        sql.append("       (select coalesce(reparte, 0) ");
        sql.append("          from estudo_cota_gerado ");
        sql.append("         where estudo_id = e.estudo_origem_copia ");
        sql.append("           and cota_id = ec.cota_id) reparteEstudoOrigemCopia, ");
        sql.append("       coalesce(ec.reparte,0) reparteSugerido, ");
        sql.append("       ec.classificacao leg, ");
        sql.append("       ec.cota_nova cotaNova, ");
        sql.append("       (select coalesce(sum(mec.qtde), 0) ");
        sql.append("          from movimento_estoque_cota mec ");
        sql.append("         where mec.tipo_movimento_id = 32 ");
        sql.append("           and mec.cota_id = c.id ");
        sql.append("           and mec.produto_edicao_id = pe.id) juramento, ");
        sql.append("       ifnull((select round(epc.qtde_recebida, 0) ");
        sql.append("                 from estoque_produto_cota epc ");
        sql.append("                 join produto_edicao ped on epc.produto_edicao_id = ped.id ");
        sql.append("                where epc.cota_id = c.id ");
        sql.append("                  and ped.produto_id = p.id ");
        sql.append("                order by ped.numero_edicao desc ");
        sql.append("                limit 0, 1), 0) ultimoReparte, ");
        sql.append("       (coalesce(ec.reparte_inicial,0) <> coalesce(ec.reparte,0)) ajustado, ");
        sql.append("       (coalesce(ec.reparte_inicial,0) - coalesce(ec.reparte,0)) quantidadeAjuste ");
        sql.append("  from estudo_cota_gerado ec ");
        sql.append("  left join cota c on (c.id = ec.cota_id) ");
        sql.append("  left join pessoa pes on (c.pessoa_id = pes.id) ");
        sql.append("  left join estudo_gerado e on (e.id = ec.estudo_id) ");
        sql.append("  left join produto_edicao pe on (pe.id = e.produto_edicao_id) ");
        sql.append("  left join produto p on (p.id = pe.produto_id) ");
        sql.append("  left join (select cota_id cota_id, count(*) quantidade ");
        sql.append("               from pdv as pdvs ");
        sql.append("              group by cota_id) pdv_qtd on pdv_qtd.cota_id = c.id ");

        StringBuilder where = new StringBuilder();
        StringBuilder order = new StringBuilder();
        StringBuilder limit = new StringBuilder();
        List<Object> params = new ArrayList<>();
        List<Object> paramsWhere = new ArrayList<>();

        where.append(" and ( ( ec.reparte is not null and ec.reparte > 0 ) or ( ec.qtde_efetiva is not null and ec.qtde_efetiva > 0 ) or ec.classificacao = 'S' ) ");

        if (queryDTO.possuiOrdenacaoPlusFiltro()) {
            if (queryDTO.possuiOrdenacaoReparte()) {
                where.append(" and case when ec.classificacao = 'S' then coalesce(ec.reparte, 0) else ec.reparte end between ? and ? ");
                paramsWhere.add(queryDTO.getFilterSortFrom());
                paramsWhere.add(queryDTO.getFilterSortTo());
            }

            if (queryDTO.possuiOrdenacaoRanking()) {
                where.append(" and case when ec.classificacao = 'S' then coalesce(ec.reparte, 0) else ec.reparte end between ? and ? ");
                paramsWhere.add(queryDTO.getFilterSortFrom());
                paramsWhere.add(queryDTO.getFilterSortTo());

                sql.append(" left join ranking_segmento ranking on (ranking.cota_id = c.id and p.TIPO_SEGMENTO_PRODUTO_ID = ranking.TIPO_SEGMENTO_PRODUTO_ID) ");
                order.append(" ranking.qtde desc ");
            }

            if (queryDTO.possuiOrdenacaoNMaiores()) {
            	sql.append(" left join ranking_segmento ranking on (ranking.cota_id = c.id and p.TIPO_SEGMENTO_PRODUTO_ID = ranking.TIPO_SEGMENTO_PRODUTO_ID) ");
                order.append(" ranking.qtde desc ");

                limit.append(" limit ");
                limit.append(queryDTO.getFilterSortFrom().intValue() - 1);
                limit.append(" , ");
                limit.append((queryDTO.getFilterSortTo().intValue() - queryDTO.getFilterSortFrom().intValue()) + 1);
            }

            if (queryDTO.possuiPercentualDeVenda()) {
                sql.append(" left join estoque_produto_cota epc on (epc.cota_id = ec.cota_id and epc.produto_edicao_id = e.produto_edicao_id) ");
                where.append(" and (((epc.qtde_recebida - epc.qtde_devolvida)*100)/epc.qtde_recebida) between ? and ?");
                paramsWhere.add(queryDTO.getFilterSortFrom());
                paramsWhere.add(queryDTO.getFilterSortTo());
            }
//            if (queryDTO.possuiReducaoReparte()) {
//                //Filtro feito diretamnete no JS.
//            }
        }

        if (queryDTO.possuiElemento()) {
            if (queryDTO.elementoIsTipoPontoVenda()) {
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1 and pdv.tipo_ponto_pdv_id = ?) ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsGeradoorDeFluxo()) {
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1) ");
                sql.append(" join gerador_fluxo_pdv gfp on (gfp.pdv_id = pdv.id and gfp.tipo_gerador_fluxo_id = ?) ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsBairro()) {
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1) ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id");
                sql.append(" join endereco on (endereco.id = endereco_pdv.endereco_id and endereco.bairro = '");
                sql.append(queryDTO.getValorElemento());
                sql.append("') ");
            }
            if (queryDTO.elementoIsRegiao()) {
                sql.append(" join registro_cota_regiao as rcr on (rcr.cota_id = c.id and rcr.regiao_id = ?) ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsAreaDeInfluencia()) {
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1 and pdv.area_influencia_pdv_id = ?) ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsDistrito()) {
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1) ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id ");
                sql.append(" join endereco on (endereco.id = endereco_pdv.endereco_id and endereco.uf = '");
                sql.append(queryDTO.getValorElemento());
                sql.append("') ");
            }
            if (queryDTO.elementoIsCotasAVista()) {
                sql.append(" join parametro_cobranca_cota pcc on pcc.cota_id = c.id ");
                sql.append(" and pcc.tipo_cota = upper('");
                sql.append(queryDTO.getValorElemento().replaceAll("-","_"));
                sql.append("') ");
            }
            if (queryDTO.elementoIsCotasNovas()) {
                where.append(" and ec.classificacao "+ (queryDTO.getValorElemento().equals("1") ? "=" : "<>") +" 'S' ");
            }
            if (queryDTO.elementoTipoDistribuicaoCota()) {
            	where.append(" and c.TIPO_DISTRIBUICAO_COTA = ? ");
            	paramsWhere.add(queryDTO.getValorElemento().toUpperCase());
            }
        }

        where.append(" and ec.ESTUDO_ID = ? ");
        paramsWhere.add(queryDTO.getEstudoId());

        if (StringUtils.isNotEmpty(queryDTO.getNumeroCotaStr())) {
            where.append(" and c.numero_cota in (").append(queryDTO.getNumeroCotaStr()).append(") ");
        }

        if (queryDTO.getFaixaDe() != null) {
            where.append(" and ec.reparte >= ? ");
            paramsWhere.add(queryDTO.getFaixaDe());
        }
        if (queryDTO.getFaixaAte() != null) {
            where.append(" and ec.reparte <= ? ");
            paramsWhere.add(queryDTO.getFaixaAte());
        }

        sql.append(" where 1 = 1 ").append(where);

        if (queryDTO.possuiOrdenacaoNMaiores() || queryDTO.possuiOrdenacaoRanking()) {
            sql.append(" order by ").append(order).append(limit);
        } else if (queryDTO.possuiOrderBy()) {
            sql.append(" order by ").append(queryDTO.getSortName()).append(" ").append(queryDTO.getSortOrder());
        }

        Query query = getSession().createSQLQuery(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        int j = 0;
        for (int i = params.size(); i < params.size() + paramsWhere.size(); i++) {
            query.setParameter(i, paramsWhere.get(j++));
        }

        ((SQLQuery) query).addScalar("cota", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("classificacao", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("nome", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("npdv", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("reparteEstudo", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("reparteEstudoOrigemCopia", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("reparteSugerido", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("leg", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("cotaNova", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("juramento", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("ultimoReparte", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("ajustado", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("quantidadeAjuste", StandardBasicTypes.BIG_INTEGER);

        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseParcialDTO.class));
        return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId, Date date) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       pe.id produtoEdicaoId, ");
        sql.append("       p.codigo codigoProduto, ");
        sql.append("       p.nome nomeProduto, ");
        sql.append("       pe.numero_edicao edicao, ");
        sql.append("       epe.periodo_parcial periodo, ");
        sql.append("       tcp.id idTipoClassificacao, ");
        sql.append("       (case when l.tipo_lancamento = 'PARCIAL' then 1 else 0 end) parcial, ");
        sql.append("       (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then 0 else 1 end) edicaoAberta ");
        sql.append("  from estudo_produto_edicao_base epe ");
        sql.append("  join produto_edicao pe on pe.id = epe.produto_edicao_id ");
        sql.append("  join produto p on p.id = pe.produto_id ");
        sql.append("  join lancamento l on l.produto_edicao_id = pe.id ");
        sql.append("  left join tipo_classificacao_produto tcp on tcp.id = pe.tipo_classificacao_produto_id ");
        sql.append(" where epe.estudo_id = :estudoId ");
//        sql.append(" and l.DATA_LCTO_PREVISTA= :dataLancamento ");
        sql.append("  order by l.data_lcto_distribuidor desc ");
        sql.append("  , pe.numero_edicao desc, epe.periodo_parcial desc ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("idTipoClassificacao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("parcial", StandardBasicTypes.BOOLEAN)
                .addScalar("edicaoAberta", StandardBasicTypes.BOOLEAN);
        query.setParameter("estudoId", estudoId);
//        query.setParameter("dataLancamento", date);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudoParcial(Long estudoId, Integer numeroPeriodoBase) {

        StringBuilder sql = new StringBuilder();
        
        sql.append(" select pe.id produtoEdicaoId, ");
        sql.append("        p.codigo codigoProduto, ");
        sql.append("        p.nome nomeProduto, ");
        sql.append("        pe.numero_edicao edicao, ");
        sql.append("        plp.numero_periodo periodo, ");
        sql.append("        tcp.id idTipoClassificacao, ");
        sql.append("        (case when plp.id is not null then 1 else 0 end) parcial, ");
        
        sql.append("        coalesce( ");
        sql.append("            sum(mecReparte.QTDE) - ");
        sql.append("            (select sum(mecEncalhe.qtde) ");
        sql.append("                from lancamento lanc ");
        sql.append("                LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID ");
        sql.append("                LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID ");
        sql.append("                LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ");
        sql.append("                LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID ");
        sql.append("                LEFT JOIN movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
        sql.append("                WHERE lanc.id = l.id) ");
        sql.append("        , 0) as venda, ");
        
        sql.append("        coalesce(sum(mecReparte.QTDE), 0) as reparte, ");
        sql.append("        (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then 0 else 1 end) edicaoAberta, ");
        sql.append("        c.numero_cota numeroCota ");
        sql.append(" from estudo_gerado eg ");
        sql.append("   join estudo_cota_gerado ecg on ecg.estudo_id = eg.id ");
        sql.append("   join cota c on c.id = ecg.cota_id ");
        sql.append("   join produto_edicao pe on pe.id = eg.produto_edicao_id ");
        sql.append("   join produto p on p.id = pe.produto_id ");
        sql.append("   join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID ");
        sql.append("   join periodo_lancamento_parcial plp on plp.ID = l.PERIODO_LANCAMENTO_PARCIAL_ID ");
        sql.append("   left join tipo_classificacao_produto tcp on tcp.id = pe.tipo_classificacao_produto_id ");
        sql.append("   left join movimento_estoque_cota mecReparte on mecReparte.lancamento_id = l.id and mecReparte.COTA_ID = c.ID");
        sql.append(" where eg.id = :estudoId ");
        sql.append("   and plp.NUMERO_PERIODO < :numeroPeriodoBase ");
        sql.append(" group by plp.NUMERO_PERIODO, ecg.cota_id ");
        sql.append(" order by l.data_lcto_distribuidor desc, ");
        sql.append("   pe.numero_edicao desc, ");
        sql.append("   plp.numero_periodo desc ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("idTipoClassificacao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("parcial", StandardBasicTypes.BOOLEAN)
                .addScalar("edicaoAberta", StandardBasicTypes.BOOLEAN)
                .addScalar("venda", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("reparte", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("numeroCota", StandardBasicTypes.INTEGER);
        
        query.setParameter("estudoId", estudoId);
        query.setParameter("numeroPeriodoBase", numeroPeriodoBase);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> carregarPublicacaoDoEstudo(Long estudoId) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ")
        .append("       pe.id produtoEdicaoId, ")
        .append("       p.codigo codigoProduto, ")
        .append("       p.nome nomeProduto, ")
        .append("       pe.numero_edicao edicao, ")
        .append("       '' as periodo, ")
        .append("       (case when l.tipo_lancamento = 'PARCIAL' then 1 else 0 end) parcial, ")
        .append("       (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then 0 else 1 end) edicaoAberta ")
        .append("  from estudo_gerado e ")
        .append("  join produto_edicao pe on pe.id = e.produto_edicao_id ")
        .append("  join produto p on p.id = pe.produto_id ")
        .append("  join lancamento l on l.produto_edicao_id = pe.id ")
        .append(" where e.id = :estudoId ")
        .append("  order by l.data_lcto_distribuidor desc , pe.numero_edicao desc");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("parcial", StandardBasicTypes.BOOLEAN)
                .addScalar("edicaoAberta", StandardBasicTypes.BOOLEAN);
        query.setParameter("estudoId", estudoId);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }
    

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> buscaHistoricoDeVendaParaCota(Long numeroCota, List<Long> listProdutoEdicaoId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select epc.produto_edicao_id produtoEdicaoId, ");
        sql.append("       coalesce(epc.qtde_recebida, 0) reparte, ");
        sql.append("       coalesce(epc.qtde_recebida - epc.qtde_devolvida, 0) venda ");
        sql.append("  from estoque_produto_cota epc ");
        sql.append("  join cota c on c.id = epc.cota_id and c.numero_cota = :numeroCota ");
        sql.append(" where epc.produto_edicao_id in (:produtoEdicaoId) ");
        sql.append(" group by epc.produto_edicao_id ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("reparte", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("venda", StandardBasicTypes.BIG_DECIMAL);

        query.setParameter("numeroCota", numeroCota);
        query.setParameterList("produtoEdicaoId", listProdutoEdicaoId);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> getEdicoesBaseParciais(Long numeroCota, Long numeroEdicao, String codigoProduto, Long periodo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pe.id produtoEdicaoId, ");
        sql.append("       p.codigo codigoProduto, ");
        sql.append("       p.nome nomeProduto, ");
        sql.append("       pe.numero_edicao edicao, ");
        sql.append("       plp.numero_periodo periodo, ");
        sql.append("       sum(case when mec.tipo_movimento_id = 13 then mec.qtde end) reparte, ");
        sql.append("       (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then ");
        sql.append("           sum(case when mec.tipo_movimento_id = 13 then mec.qtde end) - ");
        sql.append("           sum(case when mec.tipo_movimento_id = 26 or mec.tipo_movimento_id = 32 then mec.qtde end) ");
        sql.append("       else 0 end) venda ");
        sql.append("  from lancamento l ");
        sql.append("  join produto_edicao pe on pe.id = l.produto_edicao_id and pe.numero_edicao = :numeroEdicao ");
        sql.append("  join produto p on p.id = pe.produto_id and p.codigo = :codigoProduto ");
        sql.append("  join periodo_lancamento_parcial plp on plp.lancamento_parcial_id = l.id ");
        sql.append("  join movimento_estoque_cota mec on mec.lancamento_id = l.id and mec.tipo_movimento_id in (13, 26, 32) ");
        sql.append("  join cota c on c.id = mec.cota_id and c.numero_cota = :numeroCota ");
        sql.append(" where l.tipo_lancamento = 'PARCIAL' ");
        sql.append("   and plp.numero_periodo = :numeroPeriodo ");
        sql.append(" group by pe.id, p.codigo, p.nome, pe.numero_edicao, plp.numero_periodo ");
        sql.append(" order by plp.numero_periodo desc ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("reparte", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("venda", StandardBasicTypes.BIG_DECIMAL);

        query.setParameter("numeroCota", numeroCota);
        query.setParameter("numeroEdicao", numeroEdicao);
        query.setParameter("codigoProduto", codigoProduto);
        query.setParameter("numeroPeriodo", periodo);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }

    @Override
    @Transactional
    public void atualizaReparteCota(Long estudoId, Long numeroCota, Long reparteSubtraido) {

        StringBuilder sql = new StringBuilder();
        sql.append("  update estudo_cota_gerado ec ");
        sql.append("  left join cota cota on cota.id = ec.cota_id ");
        sql.append("  set ec.reparte = coalesce(ec.reparte,0) + ?, ");
        sql.append("      ec.qtde_efetiva = coalesce(ec.qtde_efetiva,0) + ? ");
        sql.append("  where ec.estudo_id = ? ");
        sql.append("  and cota.numero_cota = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteSubtraido);
        query.setLong(1, reparteSubtraido);
        query.setLong(2, estudoId);
        query.setLong(3, numeroCota);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void atualizaClassificacaoCota(Long estudoId, Long numeroCota, String classificacaoCota) {

        StringBuilder sql = new StringBuilder();
        
        sql.append("update estudo_cota_gerado ec ");
        sql.append("  left join cota cota on cota.id = ec.cota_id ");
        sql.append("   set ec.classificacao = ? ");
        sql.append(" where ec.estudo_id = ? ");
        sql.append("   and cota.numero_cota = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setString(0, classificacaoCota);
        query.setLong(1, estudoId);
        query.setLong(2, numeroCota);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void atualizaReparteEstudo(Long estudoId, Long reparteSubtraido) {

        StringBuilder sql = new StringBuilder();
        sql.append("update estudo_gerado ");
        sql.append("   set sobra = coalesce(sobra, 0) - ? ");
        sql.append(" where id = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteSubtraido);
        query.setLong(1, estudoId);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void liberar(Long id) {
        SQLQuery query = getSession().createSQLQuery("update estudo_gerado set liberado = 1 where id = ? ");
        query.setLong(0, id);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
    public List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo) {
       
    	StringBuilder sql = new StringBuilder();
        sql.append("select pdv.id, ");
        sql.append("       t.descricao descricaoTipoPontoPDV, ");
        sql.append("       pdv.nome nomePDV,");
        sql.append("       pdv.porcentagem_faturamento porcentagemFaturamento,");
        sql.append("       ifnull(pdv.ponto_principal, false) principal, ");
        sql.append("       est.reparte,");
        sql.append("       concat(e.logradouro, ', ', e.numero, ' - ', e.bairro, ' - ', e.cep, ' - ', e.cidade, ' - ', e.uf) endereco ");
        sql.append("  from pdv ");
        sql.append("  left join cota c on c.id = pdv.cota_id ");
        sql.append("  left join endereco_pdv ep on ep.pdv_id = pdv.id and ep.principal = true");
        sql.append("  left join endereco e on e.id = ep.endereco_id ");
        sql.append("  left join tipo_ponto_pdv t on t.id = pdv.tipo_ponto_pdv_id ");
        sql.append("  left join estudo_pdv est on est.pdv_id = pdv.id and est.estudo_id = :idEstudo ");
        sql.append(" where c.numero_cota = :numeroCota ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("descricaoTipoPontoPDV", StandardBasicTypes.STRING)
                .addScalar("nomePDV", StandardBasicTypes.STRING)
                .addScalar("porcentagemFaturamento", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("principal", StandardBasicTypes.BOOLEAN)
                .addScalar("reparte", StandardBasicTypes.INTEGER)
                .addScalar("endereco", StandardBasicTypes.STRING);
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("idEstudo", idEstudo);
        query.setResultTransformer(new AliasToBeanResultTransformer(PdvDTO.class));

        return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = true)
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cota.numero_cota numeroCota, ");
        sql.append("       coalesce(pe.nome, pe.razao_social, pe.nome_fantasia, '') nomeCota, ");
        sql.append("       ec.reparte quantidade, ");
        sql.append("       ec.classificacao motivo ");
        sql.append("  from estudo_cota_gerado ec ");
        sql.append("  join cota on cota.id = ec.cota_id ");
        sql.append("  join pessoa pe on pe.id = cota.pessoa_id ");
        sql.append("  left join ranking_segmento rks on rks.cota_id = cota.id ");
        sql.append("     and rks.tipo_segmento_produto_id = ? ");

        StringBuilder where = new StringBuilder();
        List<Object> params = new ArrayList<>();
        List<Object> paramsWhere = new ArrayList<>();

        params.add(queryDTO.getTipoSegmentoProduto());

        if (queryDTO.possuiElemento()) {
            if (queryDTO.elementoIsTipoPontoVenda()) {
                sql.append(" join pdv on pdv.cota_id = cota.id and pdv.tipo_ponto_pdv_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsGeradoorDeFluxo()) {
                sql.append(" join pdv on pdv.cota_id = cota.id ");
                sql.append(" join gerador_fluxo_pdv gfp on gfp.pdv_id = pdv.id and gfp.tipo_gerador_fluxo_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsBairro()) {
                sql.append(" join pdv on pdv.cota_id = cota.id ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id");
                sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.bairro = '");
                sql.append(queryDTO.getValorElemento());
                sql.append("' ");
            }
            if (queryDTO.elementoIsRegiao()) {
                sql.append(" join registro_cota_regiao as rcr on rcr.cota_id = cota.id and rcr.regiao_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsAreaDeInfluencia()) {
                sql.append(" join pdv on pdv.cota_id = cota.id and pdv.area_influencia_pdv_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsDistrito()) {
                sql.append(" join pdv on pdv.cota_id = cota.id ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id ");
                sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.uf = '");
                sql.append(queryDTO.getValorElemento());
                sql.append("' ");
            }
            if (queryDTO.elementoIsCotasAVista()) {
                sql.append(" join parametro_cobranca_cota pcc on pcc.cota_id = cota.id ");
                sql.append(" and pcc.tipo_cota = upper('");
                sql.append(queryDTO.getValorElemento().replaceAll("-","_"));
                sql.append("') ");
            }
        }

        where.append(" and ec.estudo_id = ? ");
        where.append("   and (ec.reparte = 0 or ec.reparte is null) and (ec.qtde_efetiva is null or ec.qtde_efetiva = 0) ");
        where.append("   and ec.classificacao <> 'S' ");
        paramsWhere.add(queryDTO.getEstudo());

        where.append(" and (cota.situacao_cadastro = 'ATIVO' ");
        
		where.append(" or (cota.SITUACAO_CADASTRO = 'SUSPENSO' and "); 
		where.append(" 	(select max(h.DATA_INICIO_VALIDADE) from historico_situacao_cota h where h.COTA_ID=cota.ID) >= ");
		where.append(" 	date_sub((select data_operacao from distribuidor), interval 90 day) ");
		where.append(" )) ");
    
        if (queryDTO.possuiCota()) {
            where.append(" and cota.numero_cota = ? ");
            paramsWhere.add(queryDTO.getCota());
        }

        if (queryDTO.possuiNome()) {
            where.append(" and (pe.nome like upper('").append(queryDTO.getNome()).append("') ");
            where.append("  or pe.razao_social like upper('").append(queryDTO.getNome()).append("') ");
            where.append("  or pe.nome_fantasia like upper('").append(queryDTO.getNome()).append("')) ");
        }

        if (queryDTO.getMotivo() != null && !queryDTO.getMotivo().equals("TODOS")) {
            where.append(" and ec.classificacao = '");
            where.append(queryDTO.getMotivo());
            where.append("' ");
        }

        sql.append(" where 1 = 1 ").append(where);
        sql.append(" order by ");
        
        if(queryDTO.getPaginacao() != null && queryDTO.getPaginacao().getOrdenacao() != null) {
        
        	sql.append(queryDTO.getPaginacao().getOrderByClause());
        
        } else {
        	
        	sql.append(" numeroCota ");
        }

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("numeroCota", StandardBasicTypes.LONG)
                .addScalar("nomeCota", StandardBasicTypes.STRING)
                .addScalar("quantidade", StandardBasicTypes.BIG_INTEGER)
                .addScalar("motivo", StandardBasicTypes.STRING);

        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        int j = 0;
        for (int i = params.size(); i < params.size() + paramsWhere.size(); i++) {
            query.setParameter(i, paramsWhere.get(j++));
        }

        query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoEntrouNoEstudoDTO.class));

        return query.list();
    }
    
	@Override
	public AnaliseParcialDTO buscarReparteDoEstudo(Long estudoOrigem,Integer numeroCota) {
		String sql = "select estudo_cota.REPARTE as ultimoReparte from estudo_cota_gerado estudo_cota join cota ON estudo_cota.COTA_ID = cota.ID where estudo_id = :estudoID and cota.numero_cota= :numeroCota";
		
		  SQLQuery query = getSession().createSQLQuery(sql);
	        query.setParameter("estudoID", estudoOrigem);
	        query.setParameter("numeroCota", numeroCota);
	        query.addScalar("ultimoReparte", StandardBasicTypes.BIG_DECIMAL);
	        
	        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseParcialDTO.class));

	        return (AnaliseParcialDTO) query.uniqueResult();
		
	}
    @Override
    public Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo) {
        ScrollableResults results = getSession().createCriteria(Cota.class).add(Restrictions.eq("tipoDistribuicaoCota", tipo)).scroll();

        List<Integer> listNumeroCota = new ArrayList<>();
        while (results.next()) {
            Cota cota = (Cota) results.get()[0];
            listNumeroCota.add(cota.getNumeroCota());
        }
        return listNumeroCota.toArray(new Integer[listNumeroCota.size()-1]);
    }

    @Override
    @Transactional
    public void atualizaReparteTotalESaldo(Long idEstudo, Integer reparteTotal) {
        StringBuilder sql = new StringBuilder();
        sql.append("update estudo_gerado ");
        sql.append("   set sobra = sobra - (qtde_reparte - ?), ");
        sql.append("       qtde_reparte = ? ");
        sql.append(" where id = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteTotal);
        query.setLong(1, reparteTotal);
        query.setLong(2, idEstudo);
        query.executeUpdate();
    }
}
