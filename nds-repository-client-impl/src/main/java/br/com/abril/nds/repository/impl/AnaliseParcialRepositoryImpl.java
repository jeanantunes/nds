package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseParcialRepository;

@Repository
public class AnaliseParcialRepositoryImpl extends
		AbstractRepositoryModel<EstudoCota, Long> implements
		AnaliseParcialRepository {

	public AnaliseParcialRepositoryImpl() {
		super(EstudoCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(
			AnaliseParcialQueryDTO queryDTO) {
		List<Object> params = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("select distinct ");
		sql.append("       cota.numero_cota cota, ");
		sql.append("       cota.classificacao_espectativa_faturamento classificacao, ");
		sql.append("       ifnull(pessoa.nome, '') nome, ");
		sql.append("       pdvs.quantidade npdv, ");
		sql.append("       ec.reparte reparteSugerido, ");
		sql.append("       cota.tipo_distribuicao_cota leg, ");
		sql.append("       0 juramento, ");
		sql.append("       ifnull((select round(avg(epe.venda), 1) ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id ");
		sql.append("                group by epe.cota_id), 0) mediaVenda, ");
		sql.append("       ifnull((select round(epc.qtde_recebida, 0) ");
		sql.append("                 from estoque_produto_cota epc ");
		sql.append("                 join lancamento l on l.produto_edicao_id = epc.produto_edicao_id ");
		sql.append("                 join produto_edicao pe on pe.id = l.produto_edicao_id ");
		sql.append("                 join produto p on p.id = pe.produto_id ");
		sql.append("                where epc.cota_id = cota.id ");
		sql.append("				  and p.id = produto.id ");
		sql.append("                order by pe.numero_edicao desc ");
		sql.append("                limit 0, 1), 0) ultimoReparte, ");
		sql.append("       ifnull((select reparte ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 0, 1), 0) as reparte1, ");
		sql.append("       ifnull((select epe.venda ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 0, 1), 0) as venda1, ");
		sql.append("       ifnull((select pe.numero_edicao ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                 join produto_edicao pe on pe.id = epe.produto_edicao_id ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 0, 1), 0) as numeroEdicao1, ");
		sql.append("       ifnull((select reparte ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 1, 1), 0) as reparte2, ");
		sql.append("       ifnull((select epe.venda ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 1, 1), 0) as venda2, ");
		sql.append("       ifnull((select pe.numero_edicao ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                 join produto_edicao pe on pe.id = epe.produto_edicao_id ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 1, 1), 0) as numeroEdicao2, ");
		sql.append("       ifnull((select reparte ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 2, 1), 0) as reparte3, ");
		sql.append("       ifnull((select epe.venda ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 2, 1), 0) as venda3, ");
		sql.append("       ifnull((select pe.numero_edicao ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                 join produto_edicao pe on pe.id = epe.produto_edicao_id ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 2, 1), 0) as numeroEdicao3, ");
		sql.append("       ifnull((select reparte ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 3, 1), 0) as reparte4, ");
		sql.append("       ifnull((select epe.venda ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 3, 1), 0) as venda4, ");
		sql.append("       ifnull((select pe.numero_edicao ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                 join produto_edicao pe on pe.id = epe.produto_edicao_id ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 3, 1), 0) as numeroEdicao4, ");
		sql.append("       ifnull((select reparte ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 4, 1), 0) as reparte5, ");
		sql.append("       ifnull((select epe.venda ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 4, 1), 0) as venda5, ");
		sql.append("       ifnull((select pe.numero_edicao ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                 join produto_edicao pe on pe.id = epe.produto_edicao_id ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 4, 1), 0) as numeroEdicao5, ");
		sql.append("       ifnull((select reparte ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 5, 1), 0) as reparte6, ");
		sql.append("       ifnull((select epe.venda ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 5, 1), 0) as venda6, ");
		sql.append("       ifnull((select pe.numero_edicao ");
		sql.append("                 from estudo_produto_edicao epe ");
		sql.append("                 join produto_edicao pe on pe.id = epe.produto_edicao_id ");
		sql.append("                where epe.cota_id = cota.id ");
		sql.append("                  and epe.estudo_id = ec.estudo_id limit 5, 1), 0) as numeroEdicao6 ");
		sql.append("  from estudo_cota ec ");
		sql.append("  left join cota cota on (cota.id = ec.cota_id) ");
		sql.append("  left join pessoa pessoa on (cota.pessoa_id = pessoa.id) ");
		sql.append("  left join estudo estudo on (estudo.id = ec.estudo_id) ");
		sql.append("  left join produto_edicao produto_edicao on (produto_edicao.id = estudo.produto_edicao_id) ");
		sql.append("  left join produto produto on (produto.id = produto_edicao.produto_id) ");
		sql.append("  left join (select cota_id cota_id, count(*) quantidade ");
		sql.append("               from pdv as pdvs ");
		sql.append("			  group by cota_id) pdvs on pdvs.cota_id = cota.id ");

		String where = "";

		if (queryDTO.possuiOrdenacaoPlusFiltro()) {
			if (queryDTO.possuiOrdenacaoReparte()) {
				where += " and ec.reparte between ? and ? ";
				params.add(queryDTO.getFilterSortFrom());
				params.add(queryDTO.getFilterSortTo());
			}

			if (queryDTO.possuiOrdenacaoRanking()) {
				sql.append(" left join ranking_segmento ranking on ranking.cota_id = cota.id ");
				where += " and ranking.qtde between ? and ? ";
				params.add(queryDTO.getFilterSortFrom());
				params.add(queryDTO.getFilterSortTo());
			}
			if (queryDTO.possuiPercentualDeVenda()) {
			    	sql.append(" left join estoque_produto_cota epc on estoque_produto_cota.cota_id=ec.cota_id and estoque_produto_cota.produto_edicao_id=estudo.produto_edicao_id ");
				where += " and (((epc.qtde_recebida-epc.qtde_devolvida)*100)/epc.qtde_recebida) between ? and ?";
				params.add(queryDTO.getFilterSortFrom());
				params.add(queryDTO.getFilterSortTo());
			}
			
			if (queryDTO.possuiReducaoReparte()) {

			}
		}

		if (queryDTO.possuiElemento()) {
			if (queryDTO.elementoIsTipoPontoVenda()) {
			    	sql.append(" left join pdv on pdv.cota_id= cota.id ");
			    	sql.append(" left join TIPO_PONTO_PDV tipo_pdv on tipo_pdv.id= pdv.TIPO_PONTO_PDV_ID ");

				where += " and tipo_pdv.id = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsGeradoorDeFluxo()) {
			    	sql.append(" left join pdv on pdv.cota_id= cota.id ");
			    	sql.append(" left join GERADOR_FLUXO_PDV gerador_fluxo_pdv on gerador_fluxo_pdv.PDV_ID= pdv.ID");

				where += " and gerador_fluxo_pdv.id = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsBairro()) {
			    	sql.append(" left join pdv on pdv.cota_id= cota.id ");
				sql.append(" left join ENDERECO_PDV endereco_pdv on endereco_pdv.PDV_ID= pdv.ID");
				sql.append(" left join ENDERECO endereco on endereco.ID= endereco_pdv.ENDERECO_ID");
				
				where += " and endereco.bairro = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsRegiao()) {
			    	sql.append(" left join REGISTRO_COTA_REGIAO as regiao_cota on regiao_cota.cota_id = cota.id ");
			    	sql.append(" left join REGIAO regiao on regiao.id= regiao_cota.regiao_id");

				where += " and regiao.nome_regiao = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsAreaDeInfluencia()) {
			    	sql.append(" left join pdv on pdv.cota_id= cota.id ");
				sql.append(" left join AREA_INFLUENCIA_PDV area on area.id= area.TIPO_CARACTERISTICA_PDV");

				where += " and area.desccricao = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsDistrito()) {
			    	sql.append(" left join pdv on pdv.cota_id= cota.id ");
			    	sql.append(" left join ENDERECO_PDV endereco_pdv on endereco_pdv.PDV_ID= pdv.ID");
			    	sql.append(" left join ENDERECO endereco on endereco.ID= endereco_pdv.ENDERECO_ID");

				where += " and endereco.uf = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsCotasAVista()) {

			}
			if (queryDTO.elementoIsCotasNovas()) {

			}
		}

		if(StringUtils.isNotEmpty(queryDTO.getNumeroCotaStr())){
			where += " and cota.numero_cota in ("+queryDTO.getNumeroCotaStr()+") ";
		}
		
		where += " and ec.ESTUDO_ID = ? ";
		params.add(queryDTO.getEstudoId());

		if (queryDTO.getFaixaDe() != null) {
		    where += " and ec.reparte >= ? ";
		    params.add(queryDTO.getFaixaDe());
		}
		if (queryDTO.getFaixaAte() != null) {
		    where += " and ec.reparte <= ? ";
		    params.add(queryDTO.getFaixaAte());
		}

		sql.append(" where 1=1 ").append(where);

		if (queryDTO.possuiOrderBy()) {
		    	sql.append(" order by ").append(queryDTO.getSortName()).append(" ").append(queryDTO.getSortOrder());
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		populateQuery(query, params);

		query.setResultTransformer(new AliasToBeanResultTransformer(
				AnaliseParcialDTO.class));
		query.setParameter(0, queryDTO.getEstudoId());
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
	public void atualizaReparte(Long estudoId, Long numeroCota, Double reparte) {
		SQLQuery query = getSession().createSQLQuery(
				"update estudo_cota ec "
						+ " left join cota cota on (cota.id=ec.cota_id) "
						+ "	set ec.reparte=? "
						+ " where ec.estudo_id=? and cota.numero_cota=?;");
		query.setDouble(0, reparte);
		query.setLong(1, estudoId);
		query.setLong(2, numeroCota);
		query.executeUpdate();

	}

	@Override
	public void liberar(Long id) {
		SQLQuery query = getSession().createSQLQuery(
				"update estudo set liberado = 1 where id = ?");
		query.setLong(0, id);
		query.executeUpdate();
	}

}
