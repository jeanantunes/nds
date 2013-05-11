package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseParcialRepository;

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
	sql.append("       ec.reparte reparteSugerido, ");
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

	if (queryDTO.possuiOrdenacaoPlusFiltro()) {
	    if (queryDTO.possuiOrdenacaoReparte()) {
		where.append(" and ec.reparte between ? and ? ");
		params.add(queryDTO.getFilterSortFrom());
		params.add(queryDTO.getFilterSortTo());
	    }

	    if (queryDTO.possuiOrdenacaoRanking()) {
		sql.append(" left join ranking_segmento ranking on ranking.cota_id = cota.id ");
		where.append(" and ranking.qtde between ? and ? ");
		params.add(queryDTO.getFilterSortFrom());
		params.add(queryDTO.getFilterSortTo());
	    }
	    if (queryDTO.possuiPercentualDeVenda()) {
		sql.append(" left join estoque_produto_cota epc on estoque_produto_cota.cota_id=ec.cota_id and estoque_produto_cota.produto_edicao_id=estudo.produto_edicao_id ");
		where.append(" and (((epc.qtde_recebida-epc.qtde_devolvida)*100)/epc.qtde_recebida) between ? and ?");
		params.add(queryDTO.getFilterSortFrom());
		params.add(queryDTO.getFilterSortTo());
	    }
	    if (queryDTO.possuiReducaoReparte()) {

	    }
	}

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

		params.add(queryDTO.getValorElemento());
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
		sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.uf = ? ");

		params.add(queryDTO.getValorElemento());
	    }
	    if (queryDTO.elementoIsCotasAVista()) {

	    }
	    if (queryDTO.elementoIsCotasNovas()) {

	    }
	}

	where.append(" and ec.ESTUDO_ID = ? ");
	params.add(queryDTO.getEstudoId());

	if(StringUtils.isNotEmpty(queryDTO.getNumeroCotaStr())){
		where.append(" and c.numero_cota in ("+queryDTO.getNumeroCotaStr()+") ");
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
	    sql.append(" order by ").append(queryDTO.getSortName()).append(" ").append(queryDTO.getSortOrder());
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
	sql.append(" where epe.estudo_id = :estudoId ");

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
    public List<EdicoesProdutosDTO> getEdicoesBase(Long numeroCota, Long numeroEdicao, String codigoProduto) {
	StringBuilder sql = new StringBuilder();
	sql.append("select distinct ");
	sql.append("       pe.id produtoEdicaoId, ");
	sql.append("       p.codigo codigoProduto, ");
	sql.append("       p.nome nomeProduto, ");
	sql.append("       pe.numero_edicao edicao, ");
	sql.append("       epc.qtde_recebida reparte, ");
	sql.append("       epc.qtde_recebida - epc.qtde_devolvida venda ");
	sql.append("  from estoque_produto_cota epc ");
	sql.append("  join cota c on c.id = epc.cota_id ");
	sql.append("  join produto_edicao pe on pe.id = epc.produto_edicao_id and pe.numero_edicao = :numeroEdicao ");
	sql.append("  join produto p on p.id = pe.produto_id and p.codigo = :codigoProduto ");
	sql.append(" where c.numero_cota = :numeroCota ");

	Query query = getSession().createSQLQuery(sql.toString())
		.addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
		.addScalar("codigoProduto", StandardBasicTypes.STRING)
		.addScalar("nomeProduto", StandardBasicTypes.STRING)
		.addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
		.addScalar("reparte", StandardBasicTypes.BIG_DECIMAL)
		.addScalar("venda", StandardBasicTypes.BIG_DECIMAL);

	query.setParameter("numeroCota", numeroCota);
	query.setParameter("numeroEdicao", numeroEdicao);
	query.setParameter("codigoProduto", codigoProduto);
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
	sql.append("       pe.numero_edicao numeroEdicao, ");
	sql.append("       plp.numero_periodo periodo, ");
	sql.append("       sum(case when mec.tipo_movimento_id = 21 then mec.qtde end) reparte, ");
	sql.append("       (sum(case when mec.tipo_movimento_id = 21 then mec.qtde end) - ");
	sql.append("       sum(case when mec.tipo_movimento_id = 26 then mec.qtde end)) venda ");
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
		.addScalar("edicao", StandardBasicTypes.LONG)
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
	    Object object = params.get(i);
	    if (object instanceof Long) {
		query.setLong(i, (Long) object);
		continue;
	    }
	    if (object instanceof Double) {
		query.setDouble(i, (Double) object);
		continue;
	    }
	    if (object instanceof Integer) {
		query.setInteger(i, (Integer) object);
		continue;
	    }
	    if (object instanceof String) {
		query.setString(i, (String) object);
		continue;
	    }
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
	sql.append("   set ec.reparte = ec.reparte + ? ");
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
    public List<PdvDTO> carregarDetalhesCota(Long numeroCota) {
	StringBuilder sql = new StringBuilder();
	sql.append("select pdv.id, ");
	sql.append("       t.descricao descricaoTipoPontoPDV, ");
	sql.append("       pdv.porcentagem_faturamento porcentagemFaturamento, ");
	sql.append("       pdv.ponto_principal principal, ");
	sql.append("       concat(e.logradouro, ', ', e.numero, ' - ', e.bairro, ' - ', e.cep, ' - ', e.cidade, ' - ', e.uf) endereco ");
	sql.append("  from pdv ");
	sql.append("  join cota c on c.id = pdv.cota_id ");
	sql.append("  join endereco_pdv ep on ep.pdv_id = pdv.id ");
	sql.append("  join endereco e on e.id = ep.endereco_id ");
	sql.append("  join tipo_ponto_pdv t on t.id = pdv.tipo_ponto_pdv_id ");
	sql.append(" where c.numero_cota = :numeroCota ");

	Query query = getSession().createSQLQuery(sql.toString())
		.addScalar("id", StandardBasicTypes.LONG)
		.addScalar("descricaoTipoPontoPDV", StandardBasicTypes.STRING)
		.addScalar("porcentagemFaturamento", StandardBasicTypes.BIG_DECIMAL)
		.addScalar("principal", StandardBasicTypes.BOOLEAN)
		.addScalar("endereco", StandardBasicTypes.STRING);
	query.setParameter("numeroCota", numeroCota);
	query.setResultTransformer(new AliasToBeanResultTransformer(PdvDTO.class));

	return query.list();
    }

    @Override
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
	StringBuilder sql = new StringBuilder();
	sql.append("select cota.numero_cota numeroCota, ");
	sql.append("       pe.nome nomeCota, ");
	sql.append("       ec.reparte quantidade, ");
	sql.append("       ec.classificacao motivo ");
	sql.append("  from estudo_cota ec ");
	sql.append("  join cota on cota.id = ec.cota_id ");
	sql.append("  join pessoa pe on pe.id = cota.pessoa_id ");

	List<Object> params = new ArrayList<>();

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

		params.add(queryDTO.getValorElemento());
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
		sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.uf = ? ");

		params.add(queryDTO.getValorElemento());
	    }
	    if (queryDTO.elementoIsCotasAVista()) {

	    }
	    if (queryDTO.elementoIsCotasNovas()) {

	    }
	}
	sql.append(" where ec.estudo_id = ? ");
	sql.append("   and ec.reparte = 0 ");
	params.add(queryDTO.getEstudo());

	if (queryDTO.possuiCota()) {
	    sql.append(" and cota.numero_cota = ? ");
	    params.add(queryDTO.getCota());
	}

	if (queryDTO.possuiNome()) {
	    sql.append(" and pe.nome = ? ");
	    params.add(queryDTO.getNome());
	}

	Query query = getSession().createSQLQuery(sql.toString())
		.addScalar("numeroCota", StandardBasicTypes.LONG)
		.addScalar("nomeCota", StandardBasicTypes.STRING)
		.addScalar("quantidade", StandardBasicTypes.BIG_INTEGER)
		.addScalar("motivo", StandardBasicTypes.STRING);
	for (int i = 0; i < params.size(); i++) {
	    query.setParameter(i, params.get(i));
	}
	query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoEntrouNoEstudoDTO.class));
	List<CotaQueNaoEntrouNoEstudoDTO> lista = query.list();
	return lista;
    }
}
