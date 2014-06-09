package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliseNormalDTO;
import br.com.abril.nds.dto.filtro.AnaliseNormalQueryDTO;
import br.com.abril.nds.model.cadastro.AnaliseNormalProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseNormalRepository;

@Repository
public class AnaliseNormalRepositoryImpl extends
		AbstractRepositoryModel<EstudoGerado, Long> implements
		AnaliseNormalRepository {

	public AnaliseNormalRepositoryImpl() {
		super(EstudoGerado.class);
	}

	@Override
	public List<AnaliseNormalDTO> buscaAnaliseNormalPorEstudo(AnaliseNormalQueryDTO queryDTO) {

		final List<BigInteger> edicoes = new ArrayList<>(6);

		int[] edicoesScreen = queryDTO.edicoes();

		if (edicoesScreen != null && queryDTO.edicoes().length > 0) {

			for (int i = 0; i < queryDTO.edicoes().length; i++) {
				edicoes.add(BigInteger.valueOf(edicoesScreen[i]));
			}

		} else {

			edicoes.addAll(edicoesAnteriores(queryDTO.getEstudoId()));
		}

		List<Object> params = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select distinct ");
		sql.append("       cota.numero_cota as cota, ");
		sql.append("       cota.classificacao_espectativa_faturamento classificacao, ");
		sql.append("       p.nome nome, ");
		sql.append("       ec.quantidade_pdvs npdvs, ");
		sql.append("       ec.reparte reparteSugerido, ");
		sql.append("       cota.tipo_distribuicao_cota tipoDistribuicaoCota ");

		for (int i = 0; i < edicoes.size(); i++) {
			
			BigInteger numeroEdicao = edicoes.get(i);
			params.add(numeroEdicao);
		}
		
		String where = " where ec.estudo_id=?";
		
		params.add(queryDTO.getEstudoId());

		if (queryDTO.isRankingFilteredSorted()) {
			sql.append(" ,rs.qtde as filtroRanking");
		}
		if (queryDTO.isPorcentagemVendaFilteredSorted()) {
			sql.append(" ,(((epc.qtde_recebida-epc.qtde_devolvida)*100)/epc.qtde_recebida) as filtroPercVenda");
		}
		sql.append(" from estudo_cota_gerado ec "
				+ "	left join cota cota on cota.id=ec.cota_id "
				+ "	left join pessoa p on cota.pessoa_id=p.id  "
				+ "	left join estudo_gerado e on e.id=ec.estudo_id  "
				+ "	left join produto_edicao pe on pe.id=e.produto_edicao_id "
				+ "	left join produto prod on prod.id=pe.produto_id ");
		if (queryDTO.isRankingFilteredSorted()) {
			sql.append("	left join ranking_segmento rs on rs.cota_id=ec.cota_id ");
		}
		if (queryDTO.isPorcentagemVendaFilteredSorted()) {
			sql.append(" left join estoque_produto_cota epc on epc.cota_id=ec.cota_id and epc.produto_edicao_id=e.produto_edicao_id ");
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

		if (queryDTO.isRankingFilteredSorted()) {
			Double from = queryDTO.from();
			if (from != null) {
				where += " and rs.qtde >= ?";
				params.add(from.longValue());
			}
			Double to = queryDTO.to();
			if (to != null) {
				where += " and rs.qtde <= ?";
				params.add(to.longValue());
			}
		}
		if (queryDTO.isReparteFilteredSorted()) {
			Double from = queryDTO.from();
			if (from != null) {
				where += " and ec.reparte >= ?";
				params.add(from.longValue());
			}
			Double to = queryDTO.to();
			if (to != null) {
				where += " and ec.reparte <= ?";
				params.add(to.longValue());
			}
		}
		if (queryDTO.isPorcentagemVendaFilteredSorted()) {
			Double from = queryDTO.from();
			if (from != null) {
				where += " and (((epc.qtde_recebida-epc.qtde_devolvida)*100)/epc.qtde_recebida) >= ?";
				params.add(from.longValue());
			}
			Double to = queryDTO.to();
			if (to != null) {
				where += " and (((epc.qtde_recebida-epc.qtde_devolvida)*100)/epc.qtde_recebida) <= ?";
				params.add(to.longValue());
			}
		}
		String orderBy = "";
		if (queryDTO.hasGridSort()) {
			
			orderBy += " order by " + queryDTO.getSortName() + " "
					+ queryDTO.getSortOrder();
		}

		SQLQuery query = getSession().createSQLQuery(sql + where + orderBy);

		for (int i = 0; i < params.size(); i++) {
			populateQuery(query, params);
		}

		List list = query.setResultTransformer(new ResultTransformer() {
			
			public Object transformTuple(Object[] tuple, String[] aliases) {
				
				AnaliseNormalDTO dto = new AnaliseNormalDTO();
				dto.setCota(toInt(tuple[0]));
				dto.setClassificacao((ClassificacaoEspectativaFaturamento) toEnum(
						tuple[1], ClassificacaoEspectativaFaturamento.class));
				dto.setNome((String) tuple[2]);
				dto.setNpdv(toInt(tuple[3]));
				dto.setReparteSugerido(toBigInteger(tuple[4]));
				dto.setLeg((TipoDistribuicaoCota) tuple[5]);

				dto.setEdicoes(edicoes);

				if (edicoes.size() > 0) {
					dto.setUltimoReparte(toDouble(tuple[6]));
				}

				dto.setRepartes(tuple, 6);

				dto.popularMedia();

				return dto;
			}

			private Enum toEnum(Object o, Class e) {
				return o == null ? null : Enum.valueOf(e, o.toString());
			}

			private double toDouble(Object o) {
				return o == null ? 0 : o instanceof Number ? ((Number) o)
						.doubleValue() : Double.parseDouble(o.toString());
			}

			@Override
			public List transformList(List collection) {
				return collection;
			}

			private BigInteger toBigInteger(Object object) {
				return object == null ? BigInteger.ZERO
						: object instanceof BigInteger ? (BigInteger) object
								: new BigInteger(object.toString());
			}

			private int toInt(Object o) {
				return o == null ? 0 : o instanceof Number ? ((Number) o)
						.intValue() : Integer.parseInt(o.toString());
			}
			
		}).list();
		
		return list;
	}

	private List<BigInteger> edicoesAnteriores(Long id) {
		SQLQuery numerosQuery = getSession()
				.createSQLQuery(
						"select pe.numero_edicao from produto_edicao pe left join (select pe.produto_id,pe.numero_edicao from produto_edicao pe left join estudo_gerado e on e.produto_edicao_id=pe.id where e.id=?) as tupla on pe.produto_id=tupla.produto_id where pe.produto_id=tupla.produto_id and pe.numero_edicao <= tupla.numero_edicao order by pe.numero_edicao desc limit 6;");
		numerosQuery.setLong(0, id);
		final List<BigInteger> list2 = numerosQuery.list();
		return list2;
	}

	private void populateQuery(SQLQuery query, List<Object> params) {
		for (int i = 0; i < params.size(); i++) {
			Object object = params.get(i);
			if (object instanceof Long) {
				query.setLong(i, (Long) object);
			} else if (object instanceof BigInteger) {
				query.setBigInteger(i, (BigInteger) object);
			} else if (object instanceof String) {
				query.setString(i, object.toString());
			} else {
				throw new RuntimeException(
						"Unrecognized type of paramenter, probably you must implement it search for the method AnaliseNormalRepositoryImpl.populateQuery");
			}
		}
	}

	@Override
	public void atualizaReparte(Long estudoId, Long numeroCota, Long reparte) {
		SQLQuery query = getSession()
				.createSQLQuery(
						"update estudo_cota_gerado ec left join cota cota on cota.id=ec.cota_id set ec.reparte=? where ec.estudo_id=? and cota.numero_cota=?;");
		query.setLong(0, reparte);
		query.setLong(1, estudoId);
		query.setLong(2, numeroCota);
		query.executeUpdate();
	}

	@Override
	public List<AnaliseNormalProdutoEdicaoDTO> buscaProdutoParaGrid(Long id) {
		String sql = "select "
				+ "	distinct(pe.numero_edicao) as edicao, "
				+ "	tupla.data_lancamento as dataLancamento,"
				+ "	epe.reparte as reparte,"
				+ "	epe.venda as venda, "
				+ "	pe.tipo_lancamento as status,"
				+ "	pe.CHAMADA_CAPA as capa "
				+ "from produto_edicao pe "
				+ "	left join "
				+ "		(select pe.produto_id,"
				+ "				pe.numero_edicao, "
				+ "				e.data_lancamento "
				+ "		from produto_edicao pe "
				+ "			left join estudo_gerado e on e.produto_edicao_id=pe.id "
				+ "		where e.id=?) as tupla on pe.produto_id=tupla.produto_id "
				+ "	left join produto p on p.id=tupla.produto_id "
				+ "	left join estudo_produto_edicao as epe on epe.produto_edicao_id=pe.id "
				+ "where pe.produto_id=tupla.produto_id "
				+ "order by pe.numero_edicao desc;";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setLong(0, id);
		query.setResultTransformer(Transformers
				.aliasToBean(AnaliseNormalProdutoEdicaoDTO.class));

		return query.list();
	}

	@Override
	public void liberarEstudo(Long id) {
		SQLQuery query = getSession().createSQLQuery(
				"update estudo_gerado set liberado = 1 where id = ?");
		query.setLong(0, id);
		query.executeUpdate();
	}

}
