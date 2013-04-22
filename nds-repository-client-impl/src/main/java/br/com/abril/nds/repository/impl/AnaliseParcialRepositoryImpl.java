package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

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

		String sql = "select "
				+ "	distinct(cota.NUMERO_COTA) as cota,"
				+ "	cota.CLASSIFICACAO_ESPECTATIVA_FATURAMENTO as classificacao,"
				+ "	ifnull(pessoa.NOME,'') as nome,"
				+ "	pdvs.quantidade as npdv,"
				+ "	estudo_cota.reparte as reparteSugerido,"
				+ "	cota.TIPO_DISTRIBUICAO_COTA as leg,"
				+ "	1000 as juramento,"
				+ "	ifnull((select ((sum(epc.QTDE_RECEBIDA) - sum(epc.QTDE_DEVOLVIDA)) /sum(epc.QTDE_RECEBIDA)) * 100 from ESTOQUE_PRODUTO_COTA as epc where epc.COTA_ID = cota.id GROUP BY epc.COTA_ID),0) as mediaVenda,"
				+ "	120 as ultimoReparte,"
				+ "	ifnull((select reparte from estudo_cota as ec inner join cota as c on (ec.cota_id = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -1),0) as reparte1,"
				+ "	ifnull((select sum(epc.QTDE_RECEBIDA) - sum(epc.QTDE_DEVOLVIDA)  from ESTOQUE_PRODUTO_COTA as epc  inner join cota as c on (epc.COTA_ID = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -1),0) as venda1,"
				+ "	ifnull((select reparte from estudo_cota as ec inner join cota as c on (ec.cota_id = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -2),0) as reparte2,"
				+ "	ifnull((select sum(epc.QTDE_RECEBIDA) - sum(epc.QTDE_DEVOLVIDA)  from ESTOQUE_PRODUTO_COTA as epc  inner join cota as c on (epc.COTA_ID = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -2),0) as venda2,"
				+ "	ifnull((select reparte from estudo_cota as ec inner join cota as c on (ec.cota_id = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -3),0) as reparte3,"
				+ "	ifnull((select sum(epc.QTDE_RECEBIDA) - sum(epc.QTDE_DEVOLVIDA)  from ESTOQUE_PRODUTO_COTA as epc  inner join cota as c on (epc.COTA_ID = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -3),0) as venda3,"
				+ "	ifnull((select reparte from estudo_cota as ec inner join cota as c on (ec.cota_id = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -4),0) as reparte4,"
				+ "	ifnull((select sum(epc.QTDE_RECEBIDA) - sum(epc.QTDE_DEVOLVIDA)  from ESTOQUE_PRODUTO_COTA as epc  inner join cota as c on (epc.COTA_ID = c.id) where c.id = cota.id and c.numero_cota = cota.numero_cota -4),0) as venda4"
				+ " from"
				+ "	estudo_cota as estudo_cota"
				+ "	left join cota as cota on (cota.id=estudo_cota.cota_id)"
				+ "	left join pessoa as pessoa on (cota.pessoa_id = pessoa.id)"
				+ "	left join estudo as estudo on (estudo.id = estudo_cota.estudo_id)"
				+ "	left join produto_edicao as produto_edicao on (produto_edicao.id=estudo.produto_edicao_id)"
				+ "	left join produto as produto on (produto.id=produto_edicao.produto_id)"
				+ "	left join ("
				+ "		select COTA_ID as cota_id, count(*) as quantidade from PDV as pdvs group by COTA_ID"
				+ "	) as pdvs on (pdvs.cota_id = cota.id)";

		String where = "";

		if (queryDTO.possuiOrdenacaoPlusFiltro()) {
			if (queryDTO.possuiOrdenacaoReparte()) {
				where += " and estudo_cota.reparte between ? and ? ";
				params.add(queryDTO.getFilterSortFrom());
				params.add(queryDTO.getFilterSortTo());
			}

			if (queryDTO.possuiOrdenacaoRanking()) {
				sql += " left join ranking_segmento ranking on ranking.cota_id = cota.id ";
				where += " and ranking.qtde between ? and ? ";
				params.add(queryDTO.getFilterSortFrom());
				params.add(queryDTO.getFilterSortTo());
			}
			if (queryDTO.possuiPercentualDeVenda()) {
				sql += " left join estoque_produto_cota epc on estoque_produto_cota.cota_id=estudo_cota.cota_id and estoque_produto_cota.produto_edicao_id=estudo.produto_edicao_id ";
				where += " and (((epc.qtde_recebida-epc.qtde_devolvida)*100)/epc.qtde_recebida) between ? and ?";
				params.add(queryDTO.getFilterSortFrom());
				params.add(queryDTO.getFilterSortTo());
			}
			if (queryDTO.possuiReducaoReparte()) {

			}
		}

		if (queryDTO.possuiElemento()) {
			if (queryDTO.elementoIsTipoPontoVenda()) {
				sql += " left join pdv on pdv.cota_id= cota.id ";
				sql += " left join TIPO_PONTO_PDV tipo_pdv on tipo_pdv.id= pdv.TIPO_PONTO_PDV_ID ";

				where += " and tipo_pdv.id = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsGeradoorDeFluxo()) {
				sql += " left join pdv on pdv.cota_id= cota.id ";
				sql += " left join GERADOR_FLUXO_PDV gerador_fluxo_pdv on gerador_fluxo_pdv.PDV_ID= pdv.ID";

				where += " and gerador_fluxo_pdv.id = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsBairro()) {
				sql += " left join pdv on pdv.cota_id= cota.id ";
				sql += " left join ENDERECO_PDV endereco_pdv on endereco_pdv.PDV_ID= pdv.ID";
				sql += " left join ENDERECO endereco on endereco.ID= endereco_pdv.ENDERECO_ID";
				
				where += " and endereco.bairro = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsRegiao()) {
				sql += " left join REGISTRO_COTA_REGIAO as regiao_cota on regiao_cota.cota_id = cota.id ";
				sql += " left join REGIAO regiao on regiao.id= regiao_cota.regiao_id";

				where += " and regiao.nome_regiao = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsAreaDeInfluencia()) {
				sql += " left join pdv on pdv.cota_id= cota.id ";
				sql += " left join AREA_INFLUENCIA_PDV area on area.id= area.TIPO_CARACTERISTICA_PDV";

				where += " and area.desccricao = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsDistrito()) {
				sql += " left join pdv on pdv.cota_id= cota.id ";
				sql += " left join ENDERECO_PDV endereco_pdv on endereco_pdv.PDV_ID= pdv.ID";
				sql += " left join ENDERECO endereco on endereco.ID= endereco_pdv.ENDERECO_ID";

				where += " and endereco.uf = ?";
				params.add(queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsCotasAVista()) {

			}
			if (queryDTO.elementoIsCotasNovas()) {

			}
		}

		where += " and estudo_cota.ESTUDO_ID = ? ";
		params.add(queryDTO.getEstudoId());

		if (queryDTO.getFaixaDe() != null) {
		    where += " and estudo_cota.reparte >= ? ";
		    params.add(queryDTO.getFaixaDe());
		}
		if (queryDTO.getFaixaAte() != null) {
		    where += " and estudo_cota.reparte <= ? ";
		    params.add(queryDTO.getFaixaAte());
		}

		sql += " where 1=1 " + where;

		if (queryDTO.possuiOrderBy()) {
			sql += " order by " + queryDTO.getSortName() + " "
					+ queryDTO.getSortOrder();
		}

		SQLQuery query = getSession().createSQLQuery(sql);

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
