package br.com.abril.nds.repository.impl;

import br.com.abril.nds.dto.*;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseParcialRepository;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AnaliseParcialRepositoryImpl extends AbstractRepositoryModel<EstudoCota, Long> implements AnaliseParcialRepository {

    public AnaliseParcialRepositoryImpl() {
        super(EstudoCota.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       c.numero_cota cota, ");
        sql.append("       c.classificacao_espectativa_faturamento classificacao, ");
        sql.append("       coalesce(pes.nome, pes.razao_social, pes.nome_fantasia, '') nome, ");
        sql.append("       pdv.quantidade npdv, ");
        sql.append("       round(ec.qtde_efetiva,0) reparteSugerido, ");
        sql.append("       ec.reparte reparteEstudo, ");
        sql.append("       ec.classificacao leg, ");
        sql.append("       ec.cota_nova cotaNova, ");
        sql.append("       0 juramento, ");
        sql.append("       ifnull((select round(epc.qtde_recebida, 0) ");
        sql.append("                 from estoque_produto_cota epc ");
        sql.append("                 join lancamento l on l.produto_edicao_id = epc.produto_edicao_id ");
        sql.append("                 join produto_edicao pe on pe.id = l.produto_edicao_id ");
        sql.append("                 join produto p on p.id = pe.produto_id ");
        sql.append("                where epc.cota_id = c.id ");
        sql.append("				  and p.id = p.id ");
        sql.append("                order by pe.numero_edicao desc ");
        sql.append("                limit 0, 1), 0) ultimoReparte ");
        sql.append("  from estudo_cota ec ");
        sql.append("  left join cota c on (c.id = ec.cota_id) ");
        sql.append("  left join pessoa pes on (c.pessoa_id = pes.id) ");
        sql.append("  left join estudo e on (e.id = ec.estudo_id) ");
        sql.append("  left join produto_edicao pe on (pe.id = e.produto_edicao_id) ");
        sql.append("  left join produto p on (p.id = pe.produto_id) ");
        sql.append("  left join (select cota_id cota_id, count(*) quantidade ");
        sql.append("               from pdv as pdvs ");
        sql.append("              group by cota_id) pdv on pdv.cota_id = c.id ");

        StringBuilder where = new StringBuilder();
        StringBuilder order = new StringBuilder();
        StringBuilder limit = new StringBuilder();

        where.append(" and ec.reparte is not null ");

        if (queryDTO.possuiOrdenacaoPlusFiltro()) {
            if (queryDTO.possuiOrdenacaoReparte()) {
                where.append(" and ec.reparte between ? and ? ");
                params.add(queryDTO.getFilterSortFrom());
                params.add(queryDTO.getFilterSortTo());
            }

            if (queryDTO.possuiOrdenacaoRanking()) {
                sql.append(" left join ranking_segmento ranking on ranking.cota_id = c.id ");
                order.append(" , ranking.qtde desc ");

                limit.append(" limit ");
                limit.append(queryDTO.getFilterSortFrom().intValue() - 1);
                limit.append(" , ");
                limit.append((queryDTO.getFilterSortTo().intValue() - queryDTO.getFilterSortFrom().intValue()));
            }

            if (queryDTO.possuiPercentualDeVenda()) {
                sql.append(" left join estoque_produto_cota epc on epc.cota_id = ec.cota_id and epc.produto_edicao_id = e.produto_edicao_id ");
                where.append(" and (((epc.qtde_recebida - epc.qtde_devolvida)*100)/epc.qtde_recebida) between ? and ?");
                params.add(queryDTO.getFilterSortFrom());
                params.add(queryDTO.getFilterSortTo());
            }
            if (queryDTO.possuiReducaoReparte()) {
                //Filtro feito diretamnete no JS.
            }
        }

        if (queryDTO.possuiElemento()) {
            if (queryDTO.elementoIsTipoPontoVenda()) {
                sql.append(" join pdv pdv2 on pdv2.cota_id = c.id and pdv2.tipo_ponto_pdv_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsGeradoorDeFluxo()) {
                sql.append(" join pdv pdv2 on pdv2.cota_id = c.id ");
                sql.append(" join gerador_fluxo_pdv gfp on gfp.pdv_id = pdv2.id and gfp.tipo_gerador_fluxo_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsBairro()) {
                sql.append(" join pdv pdv2 on pdv2.cota_id = c.id ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv2.id");
                sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.bairro = ? ");

                params.add("'".concat(queryDTO.getValorElemento().concat("'")));
            }
            if (queryDTO.elementoIsRegiao()) {
                sql.append(" join registro_cota_regiao as rcr on rcr.cota_id = c.id and rcr.regiao_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsAreaDeInfluencia()) {
                sql.append(" join pdv pdv2 on pdv2.cota_id = c.id and pdv2.area_influencia_pdv_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsDistrito()) {
                sql.append(" join pdv pdv2 on pdv2.cota_id = c.id ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv2.id ");
                sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.uf = '");
                sql.append(queryDTO.getValorElemento());
                sql.append("' ");
            }
            if (queryDTO.elementoIsCotasAVista()) {
                sql.append(" join parametro_cobranca_cota pcc on pcc.cota_id = c.id ");
                sql.append(" and pcc.tipo_cota = upper('");
                sql.append(queryDTO.getValorElemento().replaceAll("-","_"));
                sql.append("') ");
            }
            if (queryDTO.elementoIsCotasNovas()) {
                where.append(" and ec.cota_nova = ? ");
                params.add(queryDTO.getValorElemento());
            }
        }

        where.append(" and ec.ESTUDO_ID = ? ");
        params.add(queryDTO.getEstudoId());

        if (StringUtils.isNotEmpty(queryDTO.getNumeroCotaStr())) {
            where.append(" and c.numero_cota in (" + queryDTO.getNumeroCotaStr() + ") ");
        }

        if (queryDTO.getFaixaDe() != null) {
            where.append(" and ec.reparte >= ? ");
            params.add(queryDTO.getFaixaDe());
        }
        if (queryDTO.getFaixaAte() != null) {
            where.append(" and ec.reparte <= ? ");
            params.add(queryDTO.getFaixaAte());
        }

        sql.append(" where 1 = 1 ").append(where);

        if (queryDTO.possuiOrderBy()) {
            sql.append(" order by ").append(queryDTO.getSortName()).append(" ").append(queryDTO.getSortOrder()).append(order).append(limit);
        } else if (queryDTO.possuiOrdenacaoRanking()) {
            sql.append(" order by ").append(order).append(limit);
        }

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        populateQuery(query, params);
        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseParcialDTO.class));
        List list = query.list();
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       pe.id produtoEdicaoId, ");
        sql.append("       p.codigo codigoProduto, ");
        sql.append("       p.nome nomeProduto, ");
        sql.append("       pe.numero_edicao edicao ");
        sql.append("  from estudo_produto_edicao_base epe ");
        sql.append("  join produto_edicao pe on pe.id = epe.produto_edicao_id ");
        sql.append("  join produto p on p.id = pe.produto_id ");
        sql.append("  join lancamento l on l.produto_edicao_id = pe.id ");
        sql.append(" where epe.estudo_id = :estudoId ");
        sql.append("  order by l.data_lcto_distribuidor desc ");
        sql.append("  , pe.numero_edicao desc ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER);
        query.setParameter("estudoId", estudoId);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> getEdicoesBase(Long numeroCota, List<Long> listProdutoEdicaoId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("    epc.produto_edicao_id produtoEdicaoId, ");
        sql.append("    round(epc.qtde_recebida, 0) reparte, ");
        sql.append("    round(epc.qtde_recebida - epc.qtde_devolvida, 0) venda ");
        sql.append("  from estoque_produto_cota epc ");
        sql.append("  join cota c on c.id = epc.cota_id ");
        sql.append(" where c.numero_cota = :numeroCota ");
        sql.append(" and epc.produto_edicao_id in (:produtoEdicaoId) ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("reparte", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("venda", StandardBasicTypes.BIG_DECIMAL);

        query.setParameter("numeroCota", numeroCota);
        query.setParameterList("produtoEdicaoId", listProdutoEdicaoId);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EdicoesProdutosDTO> getEdicoesBaseParciais(Long numeroCota, Long numeroEdicao, String codigoProduto, Long periodo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pe.id produtoEdicaoId, ");
        sql.append("       p.codigo codigoProduto, ");
        sql.append("       p.nome nomeProduto, ");
        sql.append("       pe.numero_edicao edicao, ");
        sql.append("       plp.numero_periodo periodo, ");
        sql.append("       round(sum(case when mec.tipo_movimento_id = 21 then mec.qtde end),0) reparte, ");
        sql.append("       round(sum(case when mec.tipo_movimento_id = 21 then mec.qtde end) - ");
        sql.append("       sum(case when mec.tipo_movimento_id = 26 then mec.qtde end), 0) venda ");
        sql.append("  from lancamento l ");
        sql.append("  join produto_edicao pe on pe.id = l.produto_edicao_id and pe.numero_edicao = :numeroEdicao ");
        sql.append("  join produto p on p.id = pe.produto_id and p.codigo = :codigoProduto ");
        sql.append("  join periodo_lancamento_parcial plp on plp.lancamento_id = l.id ");
        sql.append("  join movimento_estoque_cota mec on mec.lancamento_id = l.id and mec.tipo_movimento_id in (21, 26) ");
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
                .addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
                .addScalar("venda", StandardBasicTypes.BIG_INTEGER);

        query.setParameter("numeroCota", numeroCota);
        query.setParameter("numeroEdicao", numeroEdicao);
        query.setParameter("codigoProduto", codigoProduto);
        query.setParameter("numeroPeriodo", periodo);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }

    private void populateQuery(SQLQuery query, List<Object> params) {
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnaliseEstudoDetalhesDTO> buscarDetalhesAnalise(ProdutoEdicao produtoEdicao) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * ");
        sql.append("  from (select pe.numero_edicao numeroEdicao, ");
        sql.append("               l.data_lcto_distribuidor dataLancamento, ");
        sql.append("               sum(epc.qtde_recebida) reparte, ");
        sql.append("               sum(epc.qtde_recebida - epc.qtde_devolvida) venda, ");
        sql.append("               sum(epc.qtde_devolvida) encalhe ");
        sql.append("		  from produto_edicao pe ");
        sql.append("          join lancamento l on l.produto_edicao_id = pe.id ");
        sql.append("		  join estoque_produto_cota epc on epc.produto_edicao_id = pe.id ");
        sql.append("         where pe.produto_id = :produtoId ");
        sql.append("		 group by pe.numero_edicao, l.data_lcto_distribuidor ");
        sql.append("		 order by pe.numero_edicao desc) t ");
        sql.append(" limit 9 ");

        Query query = getSession().createSQLQuery(sql.toString());
        query.setParameter("produtoId", produtoEdicao.getProduto().getId());
        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseEstudoDetalhesDTO.class));
        return query.list();
    }

    @Override
    @Transactional
    public void atualizaReparteCota(Long estudoId, Long numeroCota, Long reparteSubtraido) {

        StringBuilder sql = new StringBuilder();
        sql.append("update estudo_cota ec ");
        sql.append("  left join cota cota on cota.id = ec.cota_id ");
        sql.append("   set ec.qtde_efetiva = ec.qtde_efetiva + ? ");
        sql.append(" where ec.estudo_id = ? ");
        sql.append("   and cota.numero_cota = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteSubtraido);
        query.setLong(1, estudoId);
        query.setLong(2, numeroCota);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void atualizaReparteEstudo(Long estudoId, Long reparteSubtraido) {

        StringBuilder sql = new StringBuilder();
        sql.append("update estudo ");
        sql.append("   set reparte_distribuir = reparte_distribuir - ?, ");
        sql.append("       sobra = sobra + ? ");
        sql.append(" where id = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteSubtraido);
        query.setLong(1, reparteSubtraido);
        query.setLong(2, estudoId);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void liberar(Long id) {
        SQLQuery query = getSession().createSQLQuery("update estudo set liberado = 1 where id = ? ");
        query.setLong(0, id);
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pdv.id, ");
        sql.append("       t.descricao descricaoTipoPontoPDV, ");
        sql.append("       pdv.nome nomePDV,");
        sql.append("       pdv.porcentagem_faturamento porcentagemFaturamento,");
        sql.append("       ifnull(ep.principal, false) principal, ");
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

    @Override
    @Transactional(readOnly = true)
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cota.numero_cota numeroCota, ");
        sql.append("       coalesce(pe.nome, pe.razao_social, pe.nome_fantasia, '') nomeCota, ");
        sql.append("       ec.reparte quantidade, ");
        sql.append("       ec.classificacao motivo ");
        sql.append("  from estudo_cota ec ");
        sql.append("  join cota on cota.id = ec.cota_id ");
        sql.append("  join pessoa pe on pe.id = cota.pessoa_id ");
        sql.append("  left join ranking_segmento rks on rks.cota_id = cota.id ");
        sql.append("     and rks.tipo_segmento_produto_id = ? ");

        StringBuilder where = new StringBuilder();
        List<Object> params = new ArrayList<>();
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
                sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.bairro = ? ");

                params.add("'".concat(queryDTO.getValorElemento().concat("'")));
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
            if (queryDTO.elementoIsCotasNovas()) {
                where.append(" and ec.cota_nova = ? ");
                params.add(queryDTO.getValorElemento());
            }
        }
        where.append(" and ec.estudo_id = ? ");
        where.append("   and ( ec.reparte = 0 or ec.reparte is null ) ");
        params.add(queryDTO.getEstudo());

        if (queryDTO.possuiCota()) {
            sql.append(" and cota.numero_cota = ? ");
            params.add(queryDTO.getCota());
        }

        if (queryDTO.possuiNome()) {
            sql.append(" and (pe.nome like upper('").append(queryDTO.getNome()).append("') ");
            sql.append("  or pe.razao_social like upper('").append(queryDTO.getNome()).append("') ");
            sql.append("  or pe.nome_fantasia like upper('").append(queryDTO.getNome()).append("')) ");
        }

        if (queryDTO.getMotivo() != null && !queryDTO.getMotivo().equals("TODOS")) {
            sql.append(" and ec.classificacao = '");
            sql.append(queryDTO.getMotivo());
            sql.append("' ");
        }

        sql.append(" where 1 = 1 ").append(where);
        sql.append(" order by rks.qtde desc");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("numeroCota", StandardBasicTypes.LONG)
                .addScalar("nomeCota", StandardBasicTypes.STRING)
                .addScalar("quantidade", StandardBasicTypes.BIG_INTEGER)
                .addScalar("motivo", StandardBasicTypes.STRING);

        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }

        query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoEntrouNoEstudoDTO.class));

        return query.list();
    }

    @Override
    @Transactional
    public AnaliseEstudoDetalhesDTO historicoEdicaoBase(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pe.numero_edicao numeroEdicao, ");
        sql.append("        l.data_lcto_distribuidor dataLancamento, ");
        sql.append("        sum(epc.qtde_recebida) reparte, ");
        sql.append("        sum(epc.qtde_recebida - epc.qtde_devolvida) venda, ");
        sql.append("        sum(epc.qtde_devolvida) encalhe ");
        sql.append(" from produto_edicao pe ");
        sql.append("   join lancamento l on l.produto_edicao_id = pe.id ");
        sql.append("   join estoque_produto_cota epc on epc.produto_edicao_id = pe.id ");
        sql.append("   where pe.id = :id ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseEstudoDetalhesDTO.class));

        return (AnaliseEstudoDetalhesDTO) query.uniqueResult();
    }
}
