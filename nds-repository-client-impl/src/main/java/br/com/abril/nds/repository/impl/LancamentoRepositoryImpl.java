package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.CotaOperacaoDiferenciadaDTO;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoCanceladoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FuroProdutoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class LancamentoRepositoryImpl extends
		AbstractRepositoryModel<Lancamento, Long> implements
		LancamentoRepository {

	@Autowired
	FuroProdutoRepository furoProdutoRepository;

	public LancamentoRepositoryImpl() {
		super(Lancamento.class);
	}

	@Override
	public SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(
			Date data, List<Long> idsFornecedores) {
		StringBuilder hql = new StringBuilder(
				"select count(lancamento) as totalLancamentos, ");
		hql.append("sum(lancamento.reparte * produtoEdicao.precoVenda) as valorTotalLancamentos ");
		hql.append("from Lancamento lancamento ");
		hql.append("join lancamento.produtoEdicao produtoEdicao ");
		hql.append("join produtoEdicao.produto produto ");
		hql.append("join produto.fornecedores fornecedor ");

		boolean hasWhere = false;

		if (data != null) {
			hql.append(" where lancamento.dataLancamentoPrevista = :data ");
			hasWhere = true;
		}
		boolean filtraFornecedores = idsFornecedores != null
				&& !idsFornecedores.isEmpty();
		if (filtraFornecedores) {
			hql.append(hasWhere ? " and " : " where ");
			hql.append(" fornecedor.id in (:idsFornecedores) ");
		}
		Query query = getSession().createQuery(hql.toString());
		if (data != null) {
			query.setParameter("data", data);
		}
		if (filtraFornecedores) {
			query.setParameterList("idsFornecedores", idsFornecedores);
		}
		query.setResultTransformer(new AliasToBeanResultTransformer(
				SumarioLancamentosDTO.class));
		return (SumarioLancamentosDTO) query.uniqueResult();
	}

	@Override
	public void atualizarLancamento(Long idLancamento,
			Date novaDataLancamentoPrevista) {
		StringBuilder hql = new StringBuilder("update Lancamento set ");
		hql.append(" dataLancamentoPrevista = :novaDataLancamentoPrevista, ")
				.append(" reparte = 0 ").append(" where id = :id");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("novaDataLancamentoPrevista",
				novaDataLancamentoPrevista);
		query.setParameter("id", idLancamento);

		query.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ResumoPeriodoBalanceamentoDTO> buscarResumosPeriodo(
			List<Date> periodoDistribuicao, List<Long> fornecedores,
			GrupoProduto grupoCromo) {
		StringBuilder hql = new StringBuilder(
				"select lancamento.dataLancamentoDistribuidor as data, ");
		hql.append("count(lancamento.produtoEdicao) as qtdeTitulos, ");
		hql.append("sum(case when lancamento.produtoEdicao.produto.tipoProduto.grupoProduto <> :grupoCromo then lancamento.reparte ");
		hql.append("else (lancamento.reparte / lancamento.produtoEdicao.pacotePadrao) end ) as qtdeExemplares, ");
		hql.append("sum((lancamento.reparte * lancamento.produtoEdicao.peso)) as pesoTotal, ");
		hql.append("sum((lancamento.reparte * lancamento.produtoEdicao.precoVenda)) as valorTotal ");
		hql.append("from Lancamento lancamento ");
		hql.append("join lancamento.produtoEdicao.produto.fornecedores as fornecedor ");
		hql.append("where lancamento.dataLancamentoDistribuidor in (:periodo) ");
		hql.append("and fornecedor.id in (:fornecedores) ");
		hql.append("group by lancamento.dataLancamentoDistribuidor ");
		hql.append("order by lancamento.dataLancamentoDistribuidor");
		Query query = getSession().createQuery(hql.toString());
		query.setParameterList("periodo", periodoDistribuicao);
		query.setParameterList("fornecedores", fornecedores);
		query.setParameter("grupoCromo", grupoCromo);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ResumoPeriodoBalanceamentoDTO.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosNaoExpedidos(
			PaginacaoVO paginacaoVO, Date data, Long idFornecedor,
			Boolean estudo) {

		Map<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ");

		hql.append(gerarQueryProdutosNaoExpedidos(parametros, data,
				idFornecedor, estudo, false));

		if (paginacaoVO != null) {
			hql.append(gerarOrderByProdutosNaoExpedidos(
					LancamentoNaoExpedidoDTO.SortColumn
							.getByProperty(paginacaoVO.getSortColumn()),
					paginacaoVO.getOrdenacao()));
		}

		Query query = getSession().createQuery(hql.toString());

		for (Entry<String, Object> entry : parametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		if (paginacaoVO != null) {
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}

		return (List<Lancamento>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterIdsLancamentosNaoExpedidos(PaginacaoVO paginacaoVO,
			Date data, Long idFornecedor, Boolean isSaldoInsuficiente) {

		Map<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento.id ");

		hql.append(gerarQueryProdutosNaoExpedidos(parametros, data,
				idFornecedor, true, isSaldoInsuficiente));

		if (paginacaoVO != null) {
			hql.append(gerarOrderByProdutosNaoExpedidos(
					LancamentoNaoExpedidoDTO.SortColumn
							.getByProperty(paginacaoVO.getSortColumn()),
					paginacaoVO.getOrdenacao()));
		}

		Query query = getSession().createQuery(hql.toString());

		for (Entry<String, Object> entry : parametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		if (paginacaoVO != null) {
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}

		return (List<Long>) query.list();
	}

	private String gerarOrderByProdutosNaoExpedidos(
			LancamentoNaoExpedidoDTO.SortColumn sortOrder, Ordenacao ascOrDesc) {

		String order;

		if (sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.DATA_ENTRADA)) {
			order = "lancamento.dataRecolhimentoPrevista";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.CODIGO_PRODUTO)) {
			order = "produto.id";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.NOME_PRODUTO)) {
			order = "produto.nome";
		} else if (sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.EDICAO)) {
			order = "produtoEdicao.numeroEdicao";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.CLASSIFICACAO_PRODUTO)) {
			order = "produto.tipoProduto.descricao";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.PRECO_PRODUTO)) {
			order = "produtoEdicao.precoVenda";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.QTDE_PACOTE_PADRAO)) {
			order = "produtoEdicao.pacotePadrao";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.QTDE_REPARTE)) {
			order = "lancamento.reparte";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.DATA_CHAMADA)) {
			order = "lancamento.dataRecolhimentoPrevista";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.QTDE_FISICO)) {
			order = "estoque.qtde";
		} else if (sortOrder
				.equals(LancamentoNaoExpedidoDTO.SortColumn.QTDE_ESTUDO)) {
			order = "estudo.qtdeReparte";
		} else {
			return "";
		}

		if ("".equals(order)) {

			return "";
		}

		if (ascOrDesc == null) {

			ascOrDesc = Ordenacao.ASC;
		}

		return " order by " + order + " " + ascOrDesc + " ";

	}

	/**
	 * Obtém query de Lancamentos a serem expedidos
	 * 
	 * @param parametros
	 * @param data
	 * @param idFornecedor
	 * @param estudo
	 * @param sortOrder
	 * @return
	 */
	private String gerarQueryProdutosNaoExpedidos(
			Map<String, Object> parametros, Date data, Long idFornecedor,
			Boolean estudo, Boolean isSaldoInsuficiente) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");

		if (idFornecedor != null) {
			hql.append(" join produto.fornecedores fornecedor ");
		}

		hql.append(" left join produtoEdicao.estoqueProduto estoque ");

		hql.append(" left join lancamento.estudo estudo ");

		boolean where = false;

		if (estudo != null && estudo == true) {

			hql.append(" where estudo.status = :statusEstudo ");

			parametros.put("statusEstudo", StatusLancamento.ESTUDO_FECHADO);

			where = true;
		}

		if (!where) {

			hql.append(" where ");

		} else {

			hql.append(" and ");
		}

		hql.append(" lancamento.status=:statusBalanceado ");

		// hql.append(" and ( (itemRecebido.id is null and produtoEdicao.parcial=true) or (itemRecebido.id is not null)) ");

		parametros.put("statusBalanceado", StatusLancamento.BALANCEADO);

		if (data != null) {

			hql.append(" AND lancamento.dataLancamentoDistribuidor = :data");

			parametros.put("data", data);
		}

		if (idFornecedor != null) {
			hql.append(" AND fornecedor.id = :idFornecedor ");
			parametros.put("idFornecedor", idFornecedor);
		}

		if (isSaldoInsuficiente) {
			hql.append(" and estoque.qtde>=estudo.qtdeReparte ");
		}

		hql.append(" group by lancamento ");

		return hql.toString();
	}

	public Boolean existeMatrizBalanceamentoConfirmado(Date data) {

		Boolean existeLancamentoConfirmado = true;

		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");
		jpql.append(" FROM Lancamento lancamento ");
		jpql.append(" WHERE lancamento.dataLancamentoDistribuidor = :data ")
				.append("   AND lancamento.status IN (:statusPlanejadoEConfirmado) ");

		Query query = getSession().createQuery(jpql.toString());

		List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();

		listaLancamentos.add(StatusLancamento.BALANCEADO);

		query.setParameterList("statusPlanejadoEConfirmado", listaLancamentos);
		query.setParameter("data", data);

		existeLancamentoConfirmado = existeLancamentoConfirmado
				&& ((Boolean) query.uniqueResult());

		return existeLancamentoConfirmado;

	}

	public Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor,
			Boolean estudo) {

		Map<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder jpql = new StringBuilder();

		jpql.append(" select count(lancamento) ");

		jpql.append(gerarQueryProdutosNaoExpedidos(parametros, data,
				idFornecedor, estudo, false));

		Query query = getSession().createQuery(jpql.toString());

		for (Entry<String, Object> entry : parametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return (long) query.list().size();
	}

	public Lancamento obterLancamentoPosteriorDataLancamento(
			Date dataLancamento, Long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from Lancamento lancamento ");

		hql.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao ");

		hql.append(" and lancamento.dataLancamentoDistribuidor >= :dataLancamento ");

		hql.append(" order by lancamento.dataLancamentoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("dataLancamento", dataLancamento);

		query.setMaxResults(1);

		return (Lancamento) query.uniqueResult();
	}

	public Lancamento obterLancamentoAnteriorDataLancamento(
			Date dataLancamento, Long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from Lancamento lancamento ");

		hql.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao ");

		hql.append(" and lancamento.dataLancamentoDistribuidor < :dataLancamento ");

		hql.append(" order by lancamento.dataLancamentoDistribuidor desc ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("dataLancamento", dataLancamento);

		query.setMaxResults(1);

		return (Lancamento) query.uniqueResult();
	}

	public Date obterDataRecolhimentoDistribuidor(String codigoProduto,
			Long numeroEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento.dataRecolhimentoDistribuidor  ")
				.append(" from Lancamento lancamento ")
				.append(" join lancamento.produtoEdicao produtoEdicao ")
				.append(" join produtoEdicao.produto produto ")

				.append(" where produto.codigo = :codigoProduto ")
				.append(" and produtoEdicao.numeroEdicao =:numeroEdicao ")
				.append(" and lancamento.status in (:statusLancamento) ")
				.append(" order by lancamento.dataLancamentoDistribuidor desc ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("codigoProduto", codigoProduto);
		
		query.setParameterList("statusLancamento", 
			Arrays.asList(StatusLancamento.EXPEDIDO, 
				StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO));
		
		query.setMaxResults(1);

		return (Date) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimento(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo) {

		String sql = getConsultaBalanceamentoRecolhimentoAnalitico()
				+ " group by lancamento.ID "
				+ " order by dataRecolhimentoDistribuidor,idFornecedor,peb ";

		Query query = getQueryBalanceamentoRecolhimentoComParametros(
				periodoRecolhimento, fornecedores, grupoCromo, sql);

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimentoPorEditorData(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo) {

		String sql = getConsultaBalanceamentoRecolhimentoAnalitico()
				+ " group by lancamento.ID "
				+ " order by idEditor, dataRecolhimentoDistribuidor ";

		Query query = getQueryBalanceamentoRecolhimentoComParametros(
				periodoRecolhimento, fornecedores, grupoCromo, sql);

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TreeMap<Date, BigDecimal> obterExpectativasEncalhePorData(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo) {

		String sql = this.getConsultaExpectativaEncalheData();

		Query query = getSession().createSQLQuery(sql);

		this.atribuirParametrosBalanceamentoRecolhimento(periodoRecolhimento, fornecedores, grupoCromo, query);

		List<Object[]> expectativasEncalheDia = query.list();

		TreeMap<Date, BigDecimal> mapaExpectativaEncalheDia = new TreeMap<Date, BigDecimal>();

		for (Object[] expectativa : expectativasEncalheDia) {

			Date data = (Date) expectativa[0];

			BigDecimal expectativaEncalhe = (BigDecimal) expectativa[1];

			mapaExpectativaEncalheDia.put(data, expectativaEncalhe);
		}

		return mapaExpectativaEncalheDia;
	}

	private List<String> getStatusParaBalanceamentoRecolhimento() {

		String[] arrayStatusParaBalanceamentoRecolhimento = {
				StatusLancamento.EXPEDIDO.toString(),
				StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.toString(),
				StatusLancamento.BALANCEADO_RECOLHIMENTO.toString(),
				StatusLancamento.EM_RECOLHIMENTO.toString(),
				StatusLancamento.RECOLHIDO.toString() };

		List<String> statusParaBalanceamentoRecolhimento = Arrays
				.asList(arrayStatusParaBalanceamentoRecolhimento);

		return statusParaBalanceamentoRecolhimento;
	}
	
	private String getConsultaBalanceamentoRecolhimentoAnalitico() {

		StringBuilder sql = new StringBuilder();

		sql.append(" select ");
		sql.append(" fornecedor.ID as idFornecedor, ");
		sql.append(" pessoaFornecedor.RAZAO_SOCIAL as nomeFornecedor, ");
		sql.append(" periodoLancamentoParcial.TIPO as parcial, ");
		sql.append(" lancamento.STATUS as statusLancamento, ");
		sql.append(" lancamento.ID as idLancamento, ");
		sql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		sql.append(" lancamento.DATA_REC_PREVISTA as dataRecolhimentoPrevista, ");
		sql.append(" lancamento.DATA_REC_DISTRIB as dataRecolhimentoDistribuidor, ");
		sql.append(" lancamento.DATA_REC_DISTRIB as novaData, ");
		sql.append(" produto.EDITOR_ID as idEditor, ");
		sql.append(" produtoEdicao.PEB as peb, "); 
		sql.append(" pessoaEditor.RAZAO_SOCIAL as nomeEditor, ");
	
		sql.append(" sum( ");
		sql.append("	case when (tipoProduto.GRUPO_PRODUTO = :grupoCromo and periodoLancamentoParcial.TIPO = :tipoParcial) ");
		sql.append("	then (	");		
		sql.append("		((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) - ((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO ");
		sql.append("		)" );
		sql.append("	else (	");		
		sql.append("		(if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) - ((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100)) ");
		sql.append("		) ");
		sql.append(" end ) as expectativaEncalhe, ");
		
		sql.append(" sum( ");
		sql.append("	((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) - ((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA,0) / 100))) * (produtoEdicao.PRECO_VENDA - ( produtoEdicao.PRECO_VENDA * (coalesce(descontoLogisticaProdutoEdicao.PERCENTUAL_DESCONTO / 100, descontoLogisticaProduto.PERCENTUAL_DESCONTO / 100, produtoEdicao.DESCONTO / 100 ,0)))) ");
		sql.append(" ) as valorTotal, ");

		sql.append(" produtoEdicao.ID as idProdutoEdicao, ");
		sql.append(" ((coalesce(descontoLogisticaProdutoEdicao.PERCENTUAL_DESCONTO, ");
		sql.append(" descontoLogisticaProduto.PERCENTUAL_DESCONTO, produtoEdicao.DESCONTO, ");
		sql.append(" 0))) as desconto, ");
		sql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		sql.append(" produtoEdicao.PESO as peso, ");
		sql.append(" produtoEdicao.POSSUI_BRINDE as possuiBrinde, ");
		sql.append(" produtoEdicao.PRECO_VENDA as precoVenda, ");
		sql.append(" produto.codigo as codigoProduto, ");
		sql.append(" produto.nome as nomeProduto ");

		sql.append(" from ");
		sql.append(" 	  LANCAMENTO lancamento ");
		sql.append(" inner join ");
		sql.append("     PRODUTO_EDICAO produtoEdicao ");
		sql.append("         on lancamento.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		sql.append(" inner join ");
		sql.append("     PRODUTO produto ");
		sql.append("         on produtoEdicao.PRODUTO_ID=produto.ID ");
		sql.append(" left join ");
		sql.append("     DESCONTO_LOGISTICA descontoLogisticaProdutoEdicao ");
		sql.append("         on descontoLogisticaProdutoEdicao.ID = produtoEdicao.DESCONTO_LOGISTICA_ID ");
		sql.append(" left join ");
		sql.append("     DESCONTO_LOGISTICA descontoLogisticaProduto ");
		sql.append("         on descontoLogisticaProduto.ID = produto.DESCONTO_LOGISTICA_ID ");
		sql.append(" inner join ");
		sql.append("     PRODUTO_FORNECEDOR produtoFornecedor ");
		sql.append("         on produto.ID=produtoFornecedor.PRODUTO_ID ");
		sql.append(" inner join ");
		sql.append("     FORNECEDOR fornecedor ");
		sql.append("         on produtoFornecedor.fornecedores_ID=fornecedor.ID ");
		sql.append(" inner join ");
		sql.append("     EDITOR editor ");
		sql.append("         on produto.EDITOR_ID = editor.ID ");
		sql.append(" inner join ");
		sql.append("     PESSOA pessoaEditor ");
		sql.append("         on editor.JURIDICA_ID = pessoaEditor.ID ");
		sql.append(" left join ");
		sql.append("     PERIODO_LANCAMENTO_PARCIAL periodoLancamentoParcial ");
		sql.append("         on periodoLancamentoParcial.ID = lancamento.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" left join ");
		sql.append("     LANCAMENTO_PARCIAL lancamentoParcial ");
		sql.append("         on periodoLancamentoParcial.LANCAMENTO_PARCIAL_ID = lancamentoParcial.ID ");
		sql.append(" inner join ");
		sql.append(" 	  PESSOA pessoaFornecedor ");
		sql.append(" 			on fornecedor.JURIDICA_ID = pessoaFornecedor.ID ");
		sql.append(" inner join ");
		sql.append(" 	  TIPO_PRODUTO tipoProduto ");
		sql.append(" 	  		on produto.TIPO_PRODUTO_ID=tipoProduto.ID ");
		sql.append(" inner join ");
		sql.append(" 	  MOVIMENTO_ESTOQUE_COTA movimentoEstoqueCota ");
		sql.append(" 	  		ON movimentoEstoqueCota.LANCAMENTO_ID = lancamento.ID ");
		sql.append(" inner join ");
		sql.append(" 	  TIPO_MOVIMENTO tipoMovimento ");
		sql.append(" 	  		on tipoMovimento.ID = movimentoEstoqueCota.TIPO_MOVIMENTO_ID ");

		sql.append(" where lancamento.STATUS in ( ");
		sql.append("         :statusParaBalanceamentoRecolhimento ");
		sql.append("     ) ");
		sql.append("     and ( ");
		sql.append("         lancamento.DATA_REC_DISTRIB between :periodoInicial and :periodoFinal ");
		sql.append("     ) ");
		sql.append("     and ( ");
		sql.append("         fornecedor.ID in ( ");
		sql.append("             :idsFornecedores ");
		sql.append("         ) ");
		sql.append("     ) ");
		sql.append("     and movimentoEstoqueCota.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");

		return sql.toString();
	}

	private String getConsultaExpectativaEncalheData() {
		
		/*+ " case "
		+ " when tipoProduto.GRUPO_PRODUTO = :grupoCromo and periodoLancamentoParcial.TIPO <> :tipoParcial then "
		+ " sum(((movimentoEstoque.QTDE) - ((movimentoEstoque.QTDE) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO) "
		+ " else "
		+ " sum((movimentoEstoque.QTDE) - ((movimentoEstoque.QTDE) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) "
		+ " end as expectativaEncalhe ";
*/
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select analitica.dataRecolhimentoDistribuidor, ");
		sql.append(" sum(analitica.expectativaEncalhe) " );
		sql.append(" from " );
		sql.append(" ( " );
		sql.append(" select " );
		sql.append(" lancamento.DATA_REC_DISTRIB as dataRecolhimentoDistribuidor, " );
		sql.append(" sum( ");
		sql.append("	case when (tipoProduto.GRUPO_PRODUTO =:grupoCromo and periodoLancamentoParcial.TIPO =:tipoParcial) ");
		sql.append("	then (	");		
		sql.append("		((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) - ((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO ");
		sql.append("		)" );
		sql.append("	else (	");		
		sql.append("		(if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) - ((if(tipoMovimento.OPERACAO_ESTOQUE = :grupoSaida, movimentoEstoqueCota.QTDE * -1, movimentoEstoqueCota.QTDE)) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100)) ");
		sql.append("		) ");
		sql.append(" end ) as expectativaEncalhe");
		
		String clausulaFrom = getConsultaBalanceamentoRecolhimentoAnalitico();

		clausulaFrom = clausulaFrom.substring(clausulaFrom.lastIndexOf(" from "));

		sql.append(clausulaFrom);
		sql.append(" group by lancamento.ID ");
		sql.append( " ) as analitica ");
		sql.append( " group by analitica.dataRecolhimentoDistribuidor ");

		return sql.toString();
	}

	private Query getQueryBalanceamentoRecolhimentoComParametros(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo, String sql) {

		Query query = getSession().createSQLQuery(sql)
				.addScalar("nomeFornecedor").addScalar("statusLancamento")
				.addScalar("precoVenda").addScalar("codigoProduto")
				.addScalar("nomeProduto").addScalar("nomeEditor")
				.addScalar("dataLancamento")
				.addScalar("dataRecolhimentoPrevista")
				.addScalar("dataRecolhimentoDistribuidor")
				.addScalar("expectativaEncalhe")
				.addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL)
				.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL)
				.addScalar("parcial")
				.addScalar("peso", StandardBasicTypes.LONG)
				.addScalar("idEditor", StandardBasicTypes.LONG)
				.addScalar("idLancamento", StandardBasicTypes.LONG)
				.addScalar("numeroEdicao", StandardBasicTypes.LONG)
				.addScalar("peb", StandardBasicTypes.LONG)
				.addScalar("idFornecedor", StandardBasicTypes.LONG)
				.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
				.addScalar("possuiBrinde", StandardBasicTypes.BOOLEAN)
				.addScalar("novaData");

		this.atribuirParametrosBalanceamentoRecolhimento(periodoRecolhimento,
				fornecedores, grupoCromo, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ProdutoRecolhimentoDTO.class));

		return query;
	}

	private void atribuirParametrosBalanceamentoRecolhimento(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo, Query query) {
		
		List<String> statusParaBalanceamentoRecolhimento = this
				.getStatusParaBalanceamentoRecolhimento();
		
		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoInicial", periodoRecolhimento.getDe());
		query.setParameter("periodoFinal", periodoRecolhimento.getAte());
		query.setParameter("grupoCromo", grupoCromo.toString());
		query.setParameter("tipoParcial",TipoLancamentoParcial.PARCIAL.toString());
		query.setParameter("grupoSaida", OperacaoEstoque.SAIDA.name());

		query.setParameterList("statusParaBalanceamentoRecolhimento",
				statusParaBalanceamentoRecolhimento);
	}

	@Override
	public Lancamento obterUltimoLancamentoDaEdicao(Long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" where lancamento.id = ")
				.append(" (select max(lancamentoMaxDate.id) ")
				.append(" from Lancamento lancamentoMaxDate where lancamentoMaxDate.produtoEdicao.id=:idProdutoEdicao ) ")
				.append(" and lancamento.produtoEdicao.id=:idProdutoEdicao ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setMaxResults(1);

		return (Lancamento) query.uniqueResult();
	}

	@Override
	public Lancamento obterUltimoLancamentoDaEdicaoParaCota(
			Long idProdutoEdicao, Long idCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from MovimentoEstoqueCota mec ")
				.append(" join mec.produtoEdicao.lancamentos lancamento ")
				.append(" join mec.cota cota ")
				.append(" where lancamento.dataLancamentoPrevista = ")
				.append(" (")
				.append("   select max(lancamentoMaxDate.dataLancamentoPrevista) ")
				.append("   from MovimentoEstoqueCota mecMaxDate ")
				.append("   join mecMaxDate.produtoEdicao.lancamentos lancamentoMaxDate ")
				.append("   join mecMaxDate.cota cotaMaxDate ")
				.append("   where lancamentoMaxDate.produtoEdicao.id = :idProdutoEdicao ")
				.append("   and cotaMaxDate.id = :idCota ").append(" ) ")
				.append(" and lancamento.produtoEdicao.id=:idProdutoEdicao ")
				.append(" and cota.id=:idCota ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		query.setParameter("idCota", idCota);
		
		query.setMaxResults(1);

		Object lancamento = query.uniqueResult();

		return (lancamento != null) ? (Lancamento) lancamento : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> obterLancamentosEdicao(Long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" left join lancamento.periodoLancamentoParcial periodoLancamentoParcial ")
				.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao ")
				.append(" order by periodoLancamentoParcial.numeroPeriodo, lancamento.numeroLancamento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		return (List<Lancamento>) query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosPorIdOrdenados(
			Set<Long> idsLancamento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" where lancamento.id in (:idsLancamento) ")
				.append(" order by lancamento.id ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("idsLancamento", idsLancamento);

		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.repository.LancamentoRepository#
	 * obterLancamentoInformeRecolhimento(java.lang.Long, java.util.Calendar,
	 * java.util.Calendar, java.lang.String,
	 * br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento, String orderBy, Ordenacao ordenacao,
			Integer initialResult, Integer maxResults) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select ");

		hql.append(" lancamento.id as idLancamento, ");
		hql.append(" lancamento.produtoEdicao.id as idProdutoEdicao, 		  	");
		hql.append(" chamadaEncalhe.sequencia as sequenciaMatriz,			  	");
		hql.append(" produto.codigo as codigoProduto, 	");
		hql.append(" produto.nome as nomeProduto,		");
		hql.append(" periodoLancamentoParcial.tipo as tipoLancamentoParcial, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao,		");
		hql.append(" produtoEdicao.chamadaCapa as chamadaCapa,		");
		hql.append(" produtoEdicao.codigoDeBarras as codigoDeBarras, ");
		hql.append(" produtoEdicao.precoVenda as precoVenda, 		");

		hql.append(" (CASE WHEN produtoEdicao.origem = :origemInterface ");
		hql.append(" THEN (coalesce(descLogProdEdicao.percentualDesconto, descLogProd.percentualDesconto, 0 ) /100 ) ");
		hql.append(" ELSE (coalesce(produtoEdicao.desconto, produto.desconto, 0) / 100) END ");
		hql.append(" ) as desconto, ");

		hql.append(" coalesce(produtoEdicao.precoVenda, 0) - (coalesce(produtoEdicao.precoVenda, 0) * ( ");
		hql.append(" CASE WHEN produtoEdicao.origem = :origemInterface ");
		hql.append(" THEN (coalesce(descLogProdEdicao.percentualDesconto, descLogProd.percentualDesconto, 0 ) /100 ) ");
		hql.append(" ELSE (coalesce(produtoEdicao.desconto, produto.desconto, 0) / 100) END ");
		hql.append(" )) as precoDesconto, ");

		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, 		");

		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, 	");

		hql.append(" lancamentoParcial.recolhimentoFinal as dataRecolhimentoFinal, 	");

		hql.append(" editorPessoaJuridica.razaoSocial as nomeEditor		");

		hql.append(this.getHQLObtemLancamentoInformeRecolhimento(idFornecedor,
				dataInicioRecolhimento, dataFimRecolhimento));

		hql.append(" order by ");

		hql.append(orderBy);

		if (Ordenacao.ASC == ordenacao) {
			hql.append(" asc ");
		} else if (Ordenacao.DESC == ordenacao) {
			hql.append(" desc ");
		}

		Query query = getSession().createQuery(hql.toString());

		if (idFornecedor != null) {
			query.setParameter("idFornecedor", idFornecedor);
		}

		query.setParameter("dataInicioRecolhimento",
				dataInicioRecolhimento.getTime());
		query.setParameter("dataFimRecolhimento", dataFimRecolhimento.getTime());
		query.setParameter("statusLancamento",
				StatusLancamento.BALANCEADO_RECOLHIMENTO);
		query.setParameter("origemInterface", Origem.INTERFACE);

		if (maxResults != null) {
			query.setMaxResults(maxResults);
		}
		if (initialResult != null) {
			query.setFirstResult(initialResult);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				InformeEncalheDTO.class));

		return query.list();

	}

	/**
	 * Obtém a clausula "from" que compõe do HQL para consulta de dados de
	 * lancamento.
	 * 
	 * @param idFornecedor
	 * @param dataInicioRecolhimento
	 * @param dataFimRecolhimento
	 * 
	 * @return String
	 */
	private String getHQLObtemLancamentoInformeRecolhimento(Long idFornecedor,
			Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento) {

		StringBuffer hql = new StringBuffer();

		hql.append(" from Lancamento lancamento ");

		hql.append(" inner join lancamento.produtoEdicao as produtoEdicao 	");
		hql.append(" inner join produtoEdicao.produto as produto 			");
		hql.append(" inner join produto.fornecedores as fornecedor 			");
		hql.append(" left join produto.editor as editor ");
		hql.append(" left join editor.pessoaJuridica as editorPessoaJuridica ");
		hql.append(" left join lancamento.periodoLancamentoParcial as periodoLancamentoParcial 	");
		hql.append(" left join periodoLancamentoParcial.lancamentoParcial as lancamentoParcial	");

		hql.append(" left join produtoEdicao.descontoLogistica as descLogProdEdicao ");
		hql.append(" left join produto.descontoLogistica as descLogProd ");

		hql.append(" join lancamento.chamadaEncalhe as chamadaEncalhe ");

		hql.append(" where ");

		hql.append(" lancamento.dataRecolhimentoDistribuidor between :dataInicioRecolhimento and :dataFimRecolhimento ");

		hql.append(" and lancamento.status = :statusLancamento ");

		if (idFornecedor != null) {
			hql.append(" and fornecedor.id = :idFornecedor ");
		}

		Query query = getSession().createQuery(hql.toString());

		if (idFornecedor != null) {
			query.setParameter("idFornecedor", idFornecedor);
		}

		query.setParameter("dataInicioRecolhimento",
				dataInicioRecolhimento.getTime());

		query.setParameter("dataFimRecolhimento", dataFimRecolhimento.getTime());

		query.setParameter("statusLancamento",
				StatusLancamento.BALANCEADO_RECOLHIMENTO);

		return hql.toString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.repository.LancamentoRepository#
	 * quantidadeLancamentoInformeRecolhimento(java.lang.Long,
	 * java.util.Calendar, java.util.Calendar)
	 */
	@Override
	public Long quantidadeLancamentoInformeRecolhimento(Long idFornecedor,
			Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select count(lancamento.id) ");

		hql.append(this.getHQLObtemLancamentoInformeRecolhimento(idFornecedor,
				dataInicioRecolhimento, dataFimRecolhimento));

		Query query = getSession().createQuery(hql.toString());

		if (idFornecedor != null) {
			query.setParameter("idFornecedor", idFornecedor);
		}

		query.setParameter("dataInicioRecolhimento",
				dataInicioRecolhimento.getTime());

		query.setParameter("dataFimRecolhimento", dataFimRecolhimento.getTime());

		query.setParameter("statusLancamento",
				StatusLancamento.BALANCEADO_RECOLHIMENTO);

		return (Long) query.uniqueResult();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.repository.LancamentoRepository#
	 * obterDataUltimoLancamentoParcial(java.lang.Long, java.util.Date)
	 */
	public Date obterDataUltimoLancamentoParcial(Long idProdutoEdicao,
			Date dataOperacao) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select max(lancamento.dataLancamentoDistribuidor)  ");

		hql.append(" from LancamentoParcial lancamentoParcial 			");

		hql.append(" inner join lancamentoParcial.periodos as periodo 	");

		hql.append(" inner join periodo.lancamento as lancamento 		");

		hql.append(" where ");

		hql.append(" lancamentoParcial.produtoEdicao.id = :idProdutoEdicao and  ");

		hql.append(" lancamentoParcial.lancamentoInicial <= :dataOperacao and 	");

		hql.append(" lancamentoParcial.recolhimentoFinal >= :dataOperacao and 	");

		hql.append(" lancamento.dataLancamentoDistribuidor < :dataOperacao 	    ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		query.setParameter("dataOperacao", dataOperacao);

		return (Date) query.uniqueResult();

	}

	public Date obterDataUltimoLancamento(Long idProdutoEdicao,
			Date dataOperacao) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select max(lancamento.dataLancamentoDistribuidor)  ");

		hql.append(" from Lancamento lancamento ");

		hql.append(" where ");

		hql.append(" lancamento.produtoEdicao.id = :idProdutoEdicao and ");

		hql.append(" lancamento.dataLancamentoDistribuidor < :dataOperacao 	    ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		query.setParameter("dataOperacao", dataOperacao);

		return (Date) query.uniqueResult();

	}

	@Override
	public List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento) {
		return obterLancamentoInformeRecolhimento(idFornecedor,
				dataInicioRecolhimento, dataFimRecolhimento, null, null, null,
				null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoLancamentoDTO> obterBalanceamentoLancamento(Date dataLancamento,
			Intervalo<Date> periodoDistribuicao, List<Long> fornecedores) {

		String sql = this.montarConsultaBalanceamentoLancamentoAnalitico()
				+ " order by dataLancamentoDistribuidor,nomeProduto ";

		Query query = this.getQueryBalanceamentoRecolhimento(
				 dataLancamento,periodoDistribuicao, fornecedores, sql);

		return query.list();
	}

	private String montarConsultaBalanceamentoLancamentoAnalitico() {

		StringBuilder sql = new StringBuilder();

		sql.append(" select distinct ");
		sql.append(" periodoLancamentoParcial.TIPO as parcial, ");
		sql.append(" lancamento.STATUS as statusLancamento, ");
		sql.append(" lancamento.ID as idLancamento, ");
		sql.append(" lancamento.DATA_LCTO_PREVISTA as dataLancamentoPrevista, ");
		sql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamentoDistribuidor, ");
		sql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as novaDataLancamento, ");
		sql.append(" lancamento.DATA_REC_PREVISTA as dataRecolhimentoPrevista, ");
		sql.append(" lancamento.DATA_REC_DISTRIB as dataRecolhimentoDistribuidor, ");
		sql.append(" lancamento.ALTERADO_INTERFACE as alteradoInteface, ");
		sql.append(" lancamento.TIPO_LANCAMENTO as tipoLancamento, ");

		sql.append(" coalesce( ");
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo then ");
		sql.append("   case when lancamento.REPARTE / produtoEdicao.PACOTE_PADRAO < 1 then 1 ");
		sql.append("   else round(lancamento.REPARTE / produtoEdicao.PACOTE_PADRAO) end ");
		sql.append(" else ");
		sql.append(" lancamento.REPARTE ");
		sql.append(" end, 0) as repartePrevisto, ");

		sql.append(" coalesce( ");
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo then ");
		sql.append(" (lancamento.REPARTE) * produtoEdicao.PRECO_VENDA ");
		sql.append(" else ");
		sql.append(" lancamento.REPARTE * produtoEdicao.PRECO_VENDA ");
		sql.append(" end, 0) as valorTotal, ");

		sql.append(" produtoEdicao.ID as idProdutoEdicao, ");
		sql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		sql.append(" produtoEdicao.PESO as peso, ");
		sql.append(" produtoEdicao.PRECO_VENDA as precoVenda, ");
		sql.append(" produto.CODIGO as codigoProduto, ");
		sql.append(" produto.NOME as nomeProduto, ");
		sql.append(" produto.PERIODICIDADE as periodicidadeProduto, ");

		sql.append(" (coalesce(estoqueProduto.QTDE,0)) as reparteFisico, ");

		sql.append(" case when ( ");
		sql.append(" 	select MAX(LANCAMENTO_ID) ");
		sql.append(" 		from FURO_PRODUTO furoProduto ");
		sql.append(" 		where furoProduto.LANCAMENTO_ID = lancamento.ID ");
		sql.append(" 	) is not null then ");
		sql.append(" 	true ");
		sql.append(" else ");
		sql.append(" 	false ");
		sql.append(" end as possuiFuro, ");

		sql.append(" estudo.QTDE_REPARTE as distribuicao, ");

		sql.append(" fornecedor.id as idFornecedor, ");
		sql.append(" pessoa.NOME_FANTASIA as nomeFantasia, ");

		sql.append(" produtoEdicao.peb as peb ");

		sql.append(montarClausulaFromConsultaBalanceamentoLancamento());

		return sql.toString();
	}

	private String montarClausulaFromConsultaBalanceamentoLancamento() {

		StringBuilder sql = new StringBuilder();

		sql.append(" from ");
		sql.append(" LANCAMENTO lancamento ");
		sql.append(" inner join ");
		sql.append(" PRODUTO_EDICAO produtoEdicao ");
		sql.append(" on lancamento.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		sql.append(" left join ESTOQUE_PRODUTO estoqueProduto ");
		sql.append(" on estoqueProduto.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append(" inner join ");
		sql.append(" PRODUTO produto ");
		sql.append(" on produtoEdicao.PRODUTO_ID = produto.ID ");

		sql.append(" inner join ");
		sql.append("     PRODUTO_FORNECEDOR produtoFornecedor ");
		sql.append("         on produto.ID=produtoFornecedor.PRODUTO_ID ");
		sql.append(" inner join ");
		sql.append("     FORNECEDOR fornecedor ");
		sql.append("         on produtoFornecedor.fornecedores_ID=fornecedor.ID ");
		
		sql.append(" inner join ");
		sql.append("     PESSOA pessoa ");
		sql.append("         on fornecedor.juridica_id=pessoa.ID ");

		sql.append(" inner join ");
		sql.append(" TIPO_PRODUTO tipoProduto ");
		sql.append(" on tipoProduto.ID = produto.TIPO_PRODUTO_ID ");

		sql.append(" left join ");
		sql.append(" ESTUDO estudo ");
		sql.append(" on estudo.LANCAMENTO_ID = lancamento.ID ");
		
		sql.append(" left join ");
		sql.append(" ESTUDO_GERADO ");
		sql.append(" estudoGerado  on estudoGerado.ID = estudo.ID AND estudoGerado.liberado = 1 ");
		
		sql.append(" left join ");
		sql.append(" PERIODO_LANCAMENTO_PARCIAL periodoLancamentoParcial ");
		sql.append(" on periodoLancamentoParcial.ID = lancamento.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" left join ");
		sql.append(" LANCAMENTO_PARCIAL lancamentoParcial ");
		sql.append(" on lancamentoParcial.ID = periodoLancamentoParcial.LANCAMENTO_PARCIAL_ID ");
		sql.append(" left join ");
		sql.append(" EXPEDICAO expedicao ");
		sql.append(" ON lancamento.EXPEDICAO_ID = expedicao.ID ");

		sql.append(" where ");

		sql.append(" fornecedor.ID in (:idsFornecedores) ");

		sql.append(" AND lancamento.DATA_LCTO_DISTRIBUIDOR <= :periodoFinal ");
		sql.append(" AND (lancamento.STATUS in (:statusLancamentoDataMenorFinal) ");
		sql.append(" OR (lancamento.STATUS in (:statusLancamentoDataMenorFinalExpedido)  ");
		sql.append(" and  lancamento.DATA_LCTO_DISTRIBUIDOR in ( ");
		sql.append(" select lb.DATA_LCTO_DISTRIBUIDOR ");
		sql.append(" from lancamento lb where lb.status in (:statusLancamentoDataMenorFinalExpedido) ");
		sql.append(" and lb.DATA_LCTO_DISTRIBUIDOR between :periodoInicial and :periodoFinal))) ");
		
		return sql.toString();
	}

	private Query getQueryBalanceamentoRecolhimento(
			Date dataPesquisada,Intervalo<Date> periodoDistribuicao, List<Long> fornecedores,
			String sql) {

		Query query = getSession().createSQLQuery(sql)
				.addScalar("tipoLancamento",StandardBasicTypes.STRING)
				.addScalar("parcial")
				.addScalar("statusLancamento")
				.addScalar("idLancamento", StandardBasicTypes.LONG)
				.addScalar("dataLancamentoPrevista")
				.addScalar("dataLancamentoDistribuidor")
				.addScalar("novaDataLancamento")
				.addScalar("dataRecolhimentoPrevista")
				.addScalar("dataRecolhimentoDistribuidor")
				.addScalar("repartePrevisto", StandardBasicTypes.BIG_INTEGER)
				.addScalar("reparteFisico", StandardBasicTypes.BIG_INTEGER)
				.addScalar("valorTotal")
				.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
				.addScalar("numeroEdicao", StandardBasicTypes.LONG)
				.addScalar("peso", StandardBasicTypes.LONG)
				.addScalar("precoVenda").addScalar("codigoProduto")
				.addScalar("nomeProduto").addScalar("periodicidadeProduto")
				.addScalar("possuiFuro", StandardBasicTypes.BOOLEAN)
				.addScalar("alteradoInteface", StandardBasicTypes.BOOLEAN)
				.addScalar("distribuicao", StandardBasicTypes.BIG_INTEGER)
				.addScalar("idFornecedor", StandardBasicTypes.LONG)
				.addScalar("peb", StandardBasicTypes.LONG)
				.addScalar("nomeFantasia");
					

		this.aplicarParametros(query,dataPesquisada, periodoDistribuicao, fornecedores);

		query.setResultTransformer(new AliasToBeanResultTransformer(
				ProdutoLancamentoDTO.class));

		return query;
	}

	private void aplicarParametros(Query query,Date dataPesquisada,
			Intervalo<Date> periodoDistribuicao, List<Long> fornecedores) {

		List<String> statusLancamentoDataMenorFinal = Arrays.asList(
				StatusLancamento.PLANEJADO.name(),
				StatusLancamento.CONFIRMADO.name(),
				StatusLancamento.FURO.name(),
				
				StatusLancamento.EM_BALANCEAMENTO.name(),
				StatusLancamento.BALANCEADO.name()//,
				//StatusLancamento.EXPEDIDO.name()
				);
		
		List<String> statusLancamentoDataMenorFinalExpedido = Arrays.asList(
				StatusLancamento.EXPEDIDO.name()
				);

		

		query.setParameterList("statusLancamentoDataMenorFinal",
				statusLancamentoDataMenorFinal);
		
		query.setParameterList("statusLancamentoDataMenorFinalExpedido",
				statusLancamentoDataMenorFinalExpedido);

		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoFinal", periodoDistribuicao.getAte());
		query.setParameter("periodoInicial", periodoDistribuicao.getDe());
		//query.setParameter("dataPesquisada", dataPesquisada);
		query.setParameter("grupoCromo", GrupoProduto.CROMO.toString());

	}

	@Override
	public Date buscarUltimoBalanceamentoLancamentoRealizadoDia(
			Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("status", StatusLancamento.BALANCEADO));
		criteria.add(Restrictions
				.eq("dataLancamentoDistribuidor", dataOperacao));
		criteria.setProjection(Projections.max("dataLancamentoDistribuidor"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Date buscarDiaUltimoBalanceamentoLancamentoRealizado() {
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("status", StatusLancamento.BALANCEADO));
		criteria.setProjection(Projections.max("dataLancamentoDistribuidor"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Date buscarUltimoBalanceamentoRecolhimentoRealizadoDia(
			Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("dataRecolhimentoDistribuidor",
				dataOperacao));
		criteria.add(Restrictions.eq("status",
				StatusLancamento.BALANCEADO_RECOLHIMENTO));
		criteria.setProjection(Projections.max("dataRecolhimentoDistribuidor"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Date buscarDiaUltimoBalanceamentoRecolhimentoRealizado() {
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("status",
				StatusLancamento.BALANCEADO_RECOLHIMENTO));
		criteria.setProjection(Projections.max("dataRecolhimentoDistribuidor"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Lancamento obterLancamentoProdutoPorDataLancamentoOuDataRecolhimento(
			ProdutoEdicao produtoEdicao, Date dataLancamentoPrevista,
			Date dataRecolhimentoPrevista) {

		StringBuilder sql = new StringBuilder();

		sql.append(" select lancamento  from Lancamento lancamento ");
		sql.append(" join lancamento.produtoEdicao produtoEdicao ");

		sql.append(" where produtoEdicao.id =:produtoEdicao ");

		if (dataLancamentoPrevista != null) {
			sql.append(" AND lancamento.dataLancamentoPrevista = :dataLancamentoPrevista ");
		}

		if (dataRecolhimentoPrevista != null) {
			sql.append(" AND lancamento.dataRecolhimentoPrevista = :dataRecolhimentoPrevista ");
		}

		Query query = getSession().createQuery(sql.toString());
		query.setMaxResults(1);
		query.setParameter("produtoEdicao", produtoEdicao.getId());

		if (dataLancamentoPrevista != null) {
			query.setParameter("dataLancamentoPrevista", dataLancamentoPrevista);
		}

		if (dataRecolhimentoPrevista != null) {
			query.setParameter("dataRecolhimentoPrevista",
					dataRecolhimentoPrevista);
		}

		return (Lancamento) query.uniqueResult();
	}

	@Override
	public Long obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
			ProdutoEdicao produtoEdicao, Date dataLancamentoPrevista,
			Date dataLancamentoDistribuidor) {

		StringBuilder sql = new StringBuilder();

		sql.append(" select lancamento.id ");
		sql.append(" from Lancamento lancamento ");
		sql.append(" join lancamento.produtoEdicao produtoEdicao ");

		sql.append(" where produtoEdicao.id =:produtoEdicao ");

		if (dataLancamentoPrevista != null) {
			sql.append(" AND lancamento.dataLancamentoPrevista = :dataLancamentoPrevista ");
		}

		sql.append(" AND lancamento.dataLancamentoDistribuidor = :dataLancamentoDistribuidor ");

		Query query = getSession().createQuery(sql.toString());
		query.setMaxResults(1);
		query.setParameter("produtoEdicao", produtoEdicao.getId());

		if (dataLancamentoPrevista != null) {
			query.setParameter("dataLancamentoPrevista", dataLancamentoPrevista);
		}

		query.setParameter("dataLancamentoDistribuidor",
				dataLancamentoDistribuidor);

		return (Long) query.uniqueResult();
	}

	@Override
	public Long obterQuantidadeLancamentos(StatusLancamento statusLancamento) {

		StringBuilder hql = new StringBuilder("select count(lanc.id) ");
		hql.append(" from Lancamento lanc ")
				.append(" where lanc.dataLancamentoPrevista = :hoje ")
				.append(" and lanc.status = :statusLancamento ");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("hoje", new Date());
		query.setParameter("statusLancamento", statusLancamento);

		return (Long) query.uniqueResult();
	}

	@Override
	public BigDecimal obterConsignadoDia(StatusLancamento statusLancamento) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append(" sum(lanc.produtoEdicao.precoVenda * lanc.reparte) ")
				.append(" from Lancamento lanc ")
				.append(" where lanc.reparte is not null ")
				.append(" and lanc.produtoEdicao.precoVenda is not null ")
				.append(" and lanc.dataLancamentoPrevista = :hoje ")
				.append(" and lanc.status = :statusLancamento ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("hoje", new Date());
		query.setParameter("statusLancamento", statusLancamento);

		return (BigDecimal) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoLancamentoCanceladoDTO> obterLancamentosCanceladosPor(
			Intervalo<Date> periodo, List<Long> idFornecedores) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select ")
				.append(" lancamento.produtoEdicao.produto.codigo as codigo, ")
				.append(" lancamento.produtoEdicao.produto.nome as produto, ")
				.append(" lancamento.produtoEdicao.numeroEdicao as numeroEdicao, ")
				.append(" lancamento.reparte as reparte, ")
				.append(" lancamento.dataLancamentoPrevista as dataLancamento")
				.append(" from Lancamento lancamento ")
				.append(" join lancamento.produtoEdicao produtoEdicao ")
				.append(" join lancamento.produtoEdicao.produto produto ")
				.append(" join lancamento.produtoEdicao.produto.fornecedores fornecedores ")
				.append(" where lancamento.status = :statusLancamento ")
				.append(" and lancamento.dataLancamentoDistribuidor between :periodoInicial and :periodoFinal ")
				.append(" and fornecedores.id in (:idFornecedores) ");

		Query query = this.getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				ProdutoLancamentoCanceladoDTO.class);

		query.setResultTransformer(resultTransformer);

		query.setParameter("statusLancamento", StatusLancamento.CANCELADO);
		query.setParameter("periodoInicial", periodo.getDe());
		query.setParameter("periodoFinal", periodo.getAte());
		query.setParameterList("idFornecedores", idFornecedores);

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Lancamento obterProximoLancamento(Lancamento lancamentoAtual) {

		Criteria criteria = getSession().createCriteria(Lancamento.class);

		criteria.add(Restrictions.eq("produtoEdicao.id", lancamentoAtual
				.getProdutoEdicao().getId()));

		criteria.add(Restrictions.gt("dataLancamentoPrevista",
				lancamentoAtual.getDataLancamentoPrevista()));

		criteria.addOrder(Order.asc("dataLancamentoPrevista"));

		criteria.setMaxResults(1);

		return (Lancamento) criteria.uniqueResult();
	}

	public Date obterDataMinimaProdutoEdicao(Long idProdutoEdicao,
			String propertyLancamentoDistribuidor) {

		Criteria criteria = getSession().createCriteria(Lancamento.class);

		criteria.setProjection(Projections.min(propertyLancamentoDistribuidor));

		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));

		// criteria.add(Restrictions.ne("status", StatusLancamento.EXCLUIDO));

		// criteria.add(Restrictions.ne("status", StatusLancamento.FURO));

		return (Date) criteria.uniqueResult();
	}

	public Date obterDataMaximaProdutoEdicao(Long idProdutoEdicao,
			String propertyLancamentoDistribuidor) {

		Criteria criteria = getSession().createCriteria(Lancamento.class);

		criteria.setProjection(Projections.max(propertyLancamentoDistribuidor));

		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));

		// criteria.add(Restrictions.ne("status", StatusLancamento.EXCLUIDO));

		// criteria.add(Restrictions.ne("status", StatusLancamento.FURO));

		return (Date) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoqueCota> buscarMovimentosEstoqueCotaParaFuro(
			Lancamento lancamento, TipoMovimentoEstoque tipoMovimentoEstoqueFuro) {
		StringBuilder hql = new StringBuilder();

		hql.append(" select mec ").append(" from Lancamento l ")
				.append(" join l.movimentoEstoqueCotas mec ")
				.append(" where l.id = :idLancamento ")
				.append(" and mec.tipoMovimento != :tipoMovimentoEstoqueFuro ")
				.append(" and mec.movimentoEstoqueCotaFuro is null ");

		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("idLancamento", lancamento.getId());
		query.setParameter("tipoMovimentoEstoqueFuro", tipoMovimentoEstoqueFuro);

		return query.list();
	}

	@Override
	public Boolean existeLancamentoNaoBalanceado(Date dataLancamento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.dataLancamentoDistribuidor = :dataLancamento ");
		hql.append(" AND lancamento.status IN (:statusLancamentoNaoBalanceado) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("statusLancamentoNaoBalanceado", Arrays.asList(
				StatusLancamento.PLANEJADO, StatusLancamento.CONFIRMADO,
				StatusLancamento.FURO));

		query.setParameter("dataLancamento", dataLancamento);

		return (Boolean) query.uniqueResult();

	}

	@Override
	public Boolean existeRecolhimentoBalanceado(Date dataRecolhimento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.dataRecolhimentoDistribuidor = :dataRecolhimento ");
		hql.append(" AND lancamento.status IN (:statusRecolhimentoNaoBalanceado) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("statusRecolhimentoNaoBalanceado",
			Arrays.asList(StatusLancamento.BALANCEADO_RECOLHIMENTO, StatusLancamento.EM_RECOLHIMENTO,
			              StatusLancamento.RECOLHIDO));

		query.setParameter("dataRecolhimento", dataRecolhimento);

		return (Boolean) query.uniqueResult();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosDistribuidorPorPeriodo(
			Intervalo<Date> periodoDistribuicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT lancamento ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.dataLancamentoDistribuidor ");
		hql.append(" BETWEEN :dataInicial AND :dataFinal ");
		hql.append(" AND lancamento.dataLancamentoPrevista ");
		hql.append(" NOT BETWEEN :dataInicial AND :dataFinal ");
		hql.append(" AND lancamento.status in (:statusLancamento) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataInicial", periodoDistribuicao.getDe());
		query.setParameter("dataFinal", periodoDistribuicao.getAte());

		query.setParameterList("statusLancamento",
				Arrays.asList(StatusLancamento.EM_BALANCEAMENTO));

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosPrevistosPorPeriodo(
			Intervalo<Date> periodoDistribuicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT lancamento ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.dataLancamentoPrevista ");
		hql.append(" BETWEEN :dataInicial AND :dataFinal ");
		hql.append(" AND lancamento.status in (:statusLancamento) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataInicial", periodoDistribuicao.getDe());
		query.setParameter("dataFinal", periodoDistribuicao.getAte());

		query.setParameterList("statusLancamento",
				Arrays.asList(StatusLancamento.EM_BALANCEAMENTO));

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosARecolherNaSemana(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento from Lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" join produto.fornecedores fornecedor ");

		hql.append(" where lancamento.dataRecolhimentoDistribuidor between :dataDe and :dataAte ");
		hql.append(" and fornecedor.id in (:idFornecedores) ");
		hql.append(" and lancamento.status in (:status) ");

		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("dataDe", periodoRecolhimento.getDe());
		query.setParameter("dataAte", periodoRecolhimento.getAte());
		query.setParameterList("idFornecedores", fornecedores);

		List<StatusLancamento> status = new ArrayList<StatusLancamento>();
		status.add(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);

		query.setParameterList("status", status);

		return query.list();
	}

	@Override
	public boolean existeCobrancaParaLancamento(Long idLancamento) {

		String hql = " select case when (count(lancamento) > 0) then true else false end "
				+ " from Lancamento lancamento "
				+ " join lancamento.movimentoEstoqueCotas movimentoEstoqueCota "
				+ " join movimentoEstoqueCota.movimentoFinanceiroCota movimentoFinanceiroCota "
				+ " join movimentoFinanceiroCota.consolidadoFinanceiroCota consolidadoFinanceiroCota "
				+ " where lancamento.id = :idLancamento ";

		Query query = this.getSession().createQuery(hql);

		query.setParameter("idLancamento", idLancamento);

		return (boolean) query.uniqueResult();
	}

	@Override
	public LancamentoDTO obterLancamentoPorID(Long idLancamento) {

		String hql = " select lancamento.id as id, "
				+ " produtoEdicao.id as idProdutoEdicao, "
				+ " produto.id as idProduto, "
				+ " lancamento.dataLancamentoPrevista as dataPrevista, "
				+ " lancamento.dataLancamentoDistribuidor as dataDistribuidor, "
				+ " lancamento.reparte as reparte, "
				+ " lancamento.tipoLancamento as tipoLancamento, "
				+ " periodoLancamentoParcial.numeroPeriodo as numeroPeriodo "
				+ " from Lancamento lancamento "
				+ " join lancamento.produtoEdicao produtoEdicao "
				+ " join produtoEdicao.produto produto "
				+ " left join lancamento.periodoLancamentoParcial periodoLancamentoParcial "
				+ " where lancamento.id = :idLancamento ";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("idLancamento", idLancamento);

		query.setResultTransformer(Transformers
				.aliasToBean(LancamentoDTO.class));

		return (LancamentoDTO) query.uniqueResult();
	}

	@Override
	public void alterarLancamento(Long idLancamento, Date dataStatus,
			StatusLancamento status, Expedicao expedicao) {

		String hql = " update Lancamento set dataStatus=:dataStatus, status=:status, expedicao=:expedicao where id=:idLancamento ";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("idLancamento", idLancamento);
		query.setParameter("dataStatus", dataStatus);
		query.setParameter("status", status);
		query.setParameter("expedicao", expedicao);

		query.executeUpdate();
	}

	@Override
	public BigInteger obterQtdLancamentoProdutoEdicaoCopiados(
			ProdutoDistribuicaoVO produtoDistribuicaoVO) {

		Lancamento lancamentoBase = buscarPorId(produtoDistribuicaoVO
				.getIdLancamento().longValue());

		StringBuilder sql = new StringBuilder();

		sql.append(" select count(*) from lancamento lanc ");
		sql.append(" join produto_edicao prodEdit ON prodEdit.ID = lanc.PRODUTO_EDICAO_ID ");
		sql.append(" join produto prod ON prod.ID =  prodEdit.produto_id ");
		sql.append(" where lanc.DATA_LCTO_PREVISTA  = :dataLctoPrevista");
		sql.append(" and   prodEdit.numero_edicao = :numeroEdicao");
		sql.append(" and   prod.CODIGO = :codigoProduto");

		Query query = super.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataLctoPrevista",
				lancamentoBase.getDataLancamentoPrevista());
		query.setParameter("numeroEdicao",
				produtoDistribuicaoVO.getNumeroEdicao());
		query.setParameter("codigoProduto",
				produtoDistribuicaoVO.getCodigoProduto());

		BigInteger count = (BigInteger) query.uniqueResult();

		return count;
	}

	@SuppressWarnings("unchecked")
	public Set<Date> obterDatasLancamentosExpedidos(Intervalo<Date> intervalo) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT lancamento.dataLancamentoDistribuidor ");
		hql.append(" FROM Lancamento lancamento join lancamento.expedicao expedicao ");
		hql.append(" WHERE lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("dataInicio", intervalo.getDe());
		query.setParameter("dataFim", intervalo.getAte());

		List<Date> retorno = query.list();

		return new TreeSet<Date>(retorno);
	}
	

	@SuppressWarnings("unchecked")
	public List<LancamentoDTO> obterDatasStatusAgrupados(FiltroLancamentoDTO filtro,Intervalo<Date> intervalo) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT DISTINCT lancamento.dataLancamentoDistribuidor as dataDistribuidor,lancamento.status as statusLancamento ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
		hql.append(" AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
		hql.append(" AND lancamento.status <> 'FECHADO' ");
		hql.append(" GROUP BY  lancamento.dataLancamentoDistribuidor, lancamento.status ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("dataInicio", intervalo.getDe());
		query.setParameter("dataFim", intervalo.getAte());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				LancamentoDTO.class));
		
		return (List<LancamentoDTO>) query.list();

	}

	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentoInStatus(Date dataLancamentoDistribuidor, List<StatusLancamento> status) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT DISTINCT lancamento.dataLancamentoDistribuidor,lancamento.status ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.dataLancamentoDistribuidor = :dataLancamentoDistribuidor ");
		hql.append(" AND lancamento.status IN (:status) ");
		hql.append(" GROUP BY lancamento.dataLancamentoDistribuidor, lancamento.status ");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("dataLancamentoDistribuidor",dataLancamentoDistribuidor);
		query.setParameterList("status", status);

		return (List<Lancamento>) query.list();

	}

	@SuppressWarnings("unchecked")
	public List<ProdutoLancamentoDTO> verificarDataConfirmada(
			ProdutoLancamentoDTO produtoLancamentoDTO) {
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT distinct lancamento.status as status");
		hql.append(" FROM Lancamento as lancamento");
		hql.append(" WHERE lancamento.dataLancamentoDistribuidor = :data ");

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("data", produtoLancamentoDTO.getNovaDataLancamento());

		query.setResultTransformer(new AliasToBeanResultTransformer(
				ProdutoLancamentoDTO.class));
		List<ProdutoLancamentoDTO> resultado = query.list();

		return resultado;
	}

	public void atualizarDataRecolhimentoDistribuidor(Date dataRecolhimento,
			Long... idLancamento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" UPDATE Lancamento lan set lan.dataRecolhimentoDistribuidor =:dataRecolhimento  ");
		hql.append(" WHERE lan.id IN (:idLancamento) ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("dataRecolhimento", dataRecolhimento);
		query.setParameterList("idLancamento", idLancamento);

		query.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosBalanceadosPorDataRecolhimentoDistrib(
			Date dataRecolhimentoDistribuidor) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" where lancamento.dataRecolhimentoDistribuidor <= :dataRecolhimentoDistribuidor ")
				.append(" and lancamento.status = :statusLancamentoBalanceamento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataRecolhimentoDistribuidor",
				dataRecolhimentoDistribuidor);
		query.setParameter("statusLancamentoBalanceamento",
				StatusLancamento.BALANCEADO_RECOLHIMENTO);

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosEmRecolhimentoVencidos(Date dataBase) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" where lancamento.dataRecolhimentoDistribuidor < :dataBase ")
				.append(" and lancamento.status = :statusEmRecolhimento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataBase", dataBase);
		query.setParameter("statusEmRecolhimento",
				StatusLancamento.EM_RECOLHIMENTO);

		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosEmRecolhimentoParaFechamento(Date dataBase) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select distinct lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" join lancamento.produtoEdicao produtoEdicao ")
				.append(" join produtoEdicao.movimentoEstoques movimento ")
				.append(" where lancamento.status = :statusRecolhido ")
				.append(" and movimento.tipoMovimento.grupoMovimentoEstoque =:grupoRecolhimentoEncalhe ")
				.append(" and movimento.data <= :dataBase ");
		

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataBase", dataBase);
		query.setParameter("statusRecolhido",StatusLancamento.RECOLHIDO);
		query.setParameter("grupoRecolhimentoEncalhe", GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosRecolhidosPorEdicoes(
			Set<Long> idsProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" join lancamento.produtoEdicao produtoEdicao ")
				.append(" where produtoEdicao.id in (:idsProdutoEdicao) ")
				.append(" and lancamento.status in (:status) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);
		query.setParameterList("status", new StatusLancamento[] {
				StatusLancamento.RECOLHIDO, StatusLancamento.EM_RECOLHIMENTO });

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CotaOperacaoDiferenciadaDTO> obterLancamentosEncalhesPorCota(
			Set<Long> idsCota, Set<Long> idsLancamento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento.id as idLancamento, ");
		hql.append(" cota.id as idCota, ");
		hql.append(" produtoEdicao.peso as peso, ");
		hql.append(" produtoEdicao.precoVenda as valorTotal, ");
		hql.append(" produtoEdicao.id as idProdutoEdicao, ");
		hql.append(" periodoLancamentoParcial.tipo as parcial, ");
		
		hql.append(" sum ( ");
		hql.append("  case when (tipoProduto.grupoProduto = :grupoCromo and periodoLancamentoParcial.tipo <> :tipoParcial) ");
		hql.append("		then ((( case when (tipoMovimento.grupoMovimentoEstoque.operacaoEstoque <>:operacaoEstoqueEntrada) then (movimentoEstoqueCota.qtde*-1) else (  movimentoEstoqueCota.qtde) end) - ((case when (tipoMovimento.grupoMovimentoEstoque.operacaoEstoque <>:operacaoEstoqueEntrada) then (movimentoEstoqueCota.qtde*-1) else (  movimentoEstoqueCota.qtde) end) * (coalesce(produtoEdicao.expectativaVenda, ");
		hql.append("      	0) / 100)) / produtoEdicao.pacotePadrao)) ");
		hql.append("   	else ((case when (tipoMovimento.grupoMovimentoEstoque.operacaoEstoque <>:operacaoEstoqueEntrada) then (movimentoEstoqueCota.qtde*-1) else (  movimentoEstoqueCota.qtde) end) - ((case when (tipoMovimento.grupoMovimentoEstoque.operacaoEstoque <>:operacaoEstoqueEntrada) then (movimentoEstoqueCota.qtde*-1) else (  movimentoEstoqueCota.qtde) end) * (coalesce(produtoEdicao.expectativaVenda, ");
		hql.append("   		0) / 100))) ");
		hql.append(" end ");
		hql.append(" ) as expectativaEncalhe, ");

		hql.append(" sum (( ");
		hql.append("  (case when (tipoMovimento.grupoMovimentoEstoque.operacaoEstoque <>:operacaoEstoqueEntrada) then (movimentoEstoqueCota.qtde*-1) else (  movimentoEstoqueCota.qtde) end) - ((case when (tipoMovimento.grupoMovimentoEstoque.operacaoEstoque <>:operacaoEstoqueEntrada) then (movimentoEstoqueCota.qtde*-1) else (  movimentoEstoqueCota.qtde) end) * (coalesce(produtoEdicao.expectativaVenda, ");
		hql.append("     0) / 100))) * (produtoEdicao.precoVenda - ( produtoEdicao.precoVenda * (coalesce(descontoLogisticaProdutoEdicao.percentualDesconto / 100, ");
		hql.append("     descontoLogisticaProduto.percentualDesconto / 100, ");
		hql.append("     0)) )  ");
		hql.append(" )) as valorTotal ");
		
		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" join produto.tipoProduto tipoProduto ");
		hql.append(" left join produtoEdicao.descontoLogistica descontoLogisticaProdutoEdicao ");
		hql.append(" left join produto.descontoLogistica descontoLogisticaProduto ");
		hql.append(" left join lancamento.periodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" join lancamento.estudo estudo, ");
		hql.append(" MovimentoEstoqueCota movimentoEstoqueCota join movimentoEstoqueCota.tipoMovimento tipoMovimento ");
		hql.append(" join movimentoEstoqueCota.cota cota ");
		hql.append(" join cota.box box ");
		
		hql.append(" where movimentoEstoqueCota.lancamento = lancamento ");
		hql.append(" and cota.id in (:idsCota) ");
		hql.append(" and lancamento.id in (:idsLancamento) ");
		hql.append(" and movimentoEstoqueCota.movimentoEstoqueCotaFuro is null ");
		hql.append(" group by lancamento.id, cota.id ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("idsCota", idsCota);
		query.setParameterList("idsLancamento", idsLancamento);
		query.setParameter("grupoCromo", GrupoProduto.CROMO);
		query.setParameter("tipoParcial", TipoLancamentoParcial.PARCIAL);
		query.setParameter("operacaoEstoqueEntrada", OperacaoEstoque.ENTRADA);
		
		query.setResultTransformer(Transformers
				.aliasToBean(CotaOperacaoDiferenciadaDTO.class));

		return query.list();
	}

	@Override
	public boolean existeMatrizRecolhimentoConfirmado(Date dataChamadao) {

		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");
		jpql.append(" FROM Lancamento lancamento ");
		jpql.append(" WHERE lancamento.dataRecolhimentoDistribuidor = :data ")
				.append("   AND lancamento.status IN (:statusConfirmado) ");

		Query query = getSession().createQuery(jpql.toString());

		List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();
		listaLancamentos.add(StatusLancamento.BALANCEADO_RECOLHIMENTO);
		listaLancamentos.add(StatusLancamento.EM_RECOLHIMENTO);
		listaLancamentos.add(StatusLancamento.RECOLHIDO);

		query.setParameterList("statusConfirmado", listaLancamentos);
		query.setParameter("data", dataChamadao);

		return (Boolean) query.uniqueResult();
	}

	public Integer obterProximaSequenciaMatrizPorData(Date dataLancamento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select max(lancamento.sequenciaMatriz) ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" where lancamento.dataLancamentoDistribuidor = :dataLancamento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataLancamento", dataLancamento);

		Integer sequenciaMatriz = (Integer) query.uniqueResult();

		sequenciaMatriz = sequenciaMatriz != null ? sequenciaMatriz : 0;

		return ++sequenciaMatriz;
	}

	public Lancamento obterLancamentoParcialFinal(Long idProdutoEdicao) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select lancamento  ");

		hql.append(" from LancamentoParcial lancamentoParcial                         ");

		hql.append(" inner join lancamentoParcial.periodos as periodo         ");

		hql.append(" inner join periodo.lancamentos as lancamento                 ");

		hql.append(" where ");

		hql.append(" lancamento.produtoEdicao.id = :idProdutoEdicao  ");

		hql.append(" and periodo.tipo =:tipoLancamento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		query.setParameter("tipoLancamento", TipoLancamentoParcial.FINAL);

		query.setMaxResults(1);

		return (Lancamento) query.uniqueResult();
	}

	@Override
	public Lancamento obterPrimeiroLancamentoDaEdicao(Long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
				.append(" from Lancamento lancamento ")
				.append(" where lancamento.id = ")
				.append(" (select min(lancamentoMin.id) ")
				.append(" from Lancamento lancamentoMin where lancamentoMin.produtoEdicao.id = :idProdutoEdicao) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setMaxResults(1);

		Lancamento lancamento = (Lancamento) query.uniqueResult();

		return lancamento;
	}

	@Override
	public Integer obterUltimoNumeroLancamento(Long idProdutoEdicao,
			Long idPeriodo) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select max(lancamento.numeroLancamento) ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" left join lancamento.periodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao ");

		if (idPeriodo != null) {
			hql.append(" and periodoLancamentoParcial.id = :idPeriodo ");
		}

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		if (idPeriodo != null) {
			query.setParameter("idPeriodo", idPeriodo);
		}

		return (Integer) query.uniqueResult();
	}

	@Override
	public Date getMaiorDataLancamentoPrevisto(Long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT max(lancamento.dataLancamentoPrevista) ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.produtoEdicao.id = :idProdutoEdicao ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		return (Date) query.uniqueResult();
	}

	@Override
	public Date getMaiorDataLancamentoDistribuidor(Long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT max(lancamento.dataLancamentoDistribuidor) ");
		hql.append(" FROM Lancamento lancamento ");
		hql.append(" WHERE lancamento.produtoEdicao.id = :idProdutoEdicao ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		return (Date) query.uniqueResult();
	}

	@Override
	public Lancamento obterLancamentoPorItemRecebimento(Long idItem) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.recebimentos item ");
		hql.append(" where item.id = :idItem");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idItem", idItem);

		return (Lancamento) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterPorEstudo(Estudo estudo) {

		StringBuilder sQuery = new StringBuilder();

		/*
		 * select * from Lancamento l
		 * 
		 * left join estudo on estudo.ID = 83914 where l.id = 90269;
		 */

		sQuery.append(" select l from Lancamento l,Estudo e  ");
		// sQuery.append(" inner join Estudo ");
		// sQuery.append(" on estudo.ID = :idEstudo ");
		sQuery.append(" where e.id= :idEstudo ");
		sQuery.append(" and e.produtoEdicao.id = l.produtoEdicao.id  ");
		// sQuery.append(" and e.dataLancamento = l.dataLancamentoPrevista  ");
		sQuery.append(" and e.lancamentoID = l.id  ");

		Query query = getSession().createQuery(sQuery.toString());

		// SQLQuery query =
		// getSession().createSQLQuery(sQuery.toString()).addEntity("lancamento",
		// Lancamento.class);

		// query.setParameter("idLancamento", estudo.getLancamentoID());
		query.setParameter("idEstudo", estudo.getId());

		return query.list();
	}

	@Override
	public Lancamento buscarPorIdSemEstudo(Long lancamentoId) {
		Criteria cri = getSession().createCriteria(Lancamento.class);
		// cri.setProjection(Projections.distinct(Projections.property("id")));
		cri.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		cri.add(Restrictions.eq("id", lancamentoId));
		return (Lancamento) cri.uniqueResult();
	}

	@Override
	public BigInteger obterUltimoRepartePorProduto(Long produtoId) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select round(sum(lc.reparte),0) ");
		sql.append(" from lancamento lc");
		sql.append(" where lc.produto_edicao_id in (");
		sql.append(" 		select produto_edicao.id from produto_edicao");
		sql.append(" 			where produto_id = :produtoId");
		sql.append(" 		)");
		sql.append(" and (lc.status = 'LANÇADA' or lc.status = 'CALCULADA')");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("produtoId", produtoId);

		return (BigInteger) query.uniqueResult();
	}

	@Override
	public Lancamento buscarPorDataLancamentoProdutoEdicao(Date dtLancamento,
			Long produtoEdicaoId) {
		StringBuilder sql = new StringBuilder();

		sql.append("from Lancamento l where l.dataLancamentoPrevista=:dtLancamento and l.produtoEdicao.id=:produtoEdicaoId");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("dtLancamento", dtLancamento);
		query.setParameter("produtoEdicaoId", produtoEdicaoId);

		return (Lancamento) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> obterRecolhimentosConfirmados(
			List<Date> datasConfirmadas) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" where lancamento.dataRecolhimentoDistribuidor in (:datasConfirmadas) ");
		hql.append(" and lancamento.status in (:statusLancamento) ");

		Query query = getSession().createQuery(hql.toString());

		List<StatusLancamento> statusLancamento = new ArrayList<>();

		statusLancamento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO);

		query.setParameterList("datasConfirmadas", datasConfirmadas);
		query.setParameterList("statusLancamento", statusLancamento);

		return query.list();
	}

	public Lancamento obterLancamentoParcialChamadaEncalhe(Long idChamdaEncalhe) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ");
		hql.append(" from ChamadaEncalhe chamdaEncalhe ");
		hql.append(" join chamdaEncalhe.lancamentos lancamento ");
		hql.append(" join lancamento.periodoLancamentoParcial periodoLancamento ");
		hql.append(" where chamdaEncalhe.id =:idChamdaEncalhe ");
		hql.append(" and lancamento.tipoLancamento =:tipoLancamento ");
		hql.append(" and periodoLancamento.tipo =:tipo ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		query.setParameter("tipo", TipoLancamentoParcial.PARCIAL);
		query.setParameter("idChamdaEncalhe", idChamdaEncalhe);

		query.setMaxResults(1);

		return (Lancamento) query.uniqueResult();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> obterLancamentosConfirmados(
			List<Date> datasConfirmadas) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" where lancamento.dataRecolhimentoDistribuidor in (:datasConfirmadas) ");
		hql.append(" and lancamento.status in (:statusLancamento) ");

		Query query = getSession().createQuery(hql.toString());

		List<StatusLancamento> statusLancamento = new ArrayList<>();

		statusLancamento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO);
		statusLancamento.add(StatusLancamento.EM_RECOLHIMENTO);
		statusLancamento.add(StatusLancamento.RECOLHIDO);

		query.setParameterList("datasConfirmadas", datasConfirmadas);
		query.setParameterList("statusLancamento", statusLancamento);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> obterMatrizLancamentosConfirmados(
			List<Date> datasConfirmadas) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" where lancamento.dataLancamentoDistribuidor in (:datasConfirmadas) ");
		hql.append(" and lancamento.status in (:statusLancamento) ");

		Query query = getSession().createQuery(hql.toString());

		List<StatusLancamento> statusLancamento = new ArrayList<>();

		statusLancamento.add(StatusLancamento.BALANCEADO);

		query.setParameterList("datasConfirmadas", datasConfirmadas);
		query.setParameterList("statusLancamento", statusLancamento);

		return query.list();
	}

	@Override
	public boolean existeConferenciaEncalheParaLancamento(Long idLancamento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select case when(count(lancamento.id) > 0) then true else false end ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.chamadaEncalhe chamadaEncalhe ");
		hql.append(" join chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCotas ");
		hql.append(" join chamadaEncalheCotas.conferenciasEncalhe conferenciasEncalhe ");
		hql.append(" where lancamento.id = :idLancamento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idLancamento", idLancamento);

		return (Boolean) query.uniqueResult();
	}

	public boolean existeConferenciaEncalheParaLancamento(Long idLancamento,
			TipoChamadaEncalhe tipoChamadaEncalhe) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select case when(count(lancamento.id) > 0) then true else false end ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.chamadaEncalhe chamadaEncalhe ");
		hql.append(" join chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCotas ");
		hql.append(" join chamadaEncalheCotas.conferenciasEncalhe conferenciasEncalhe ");
		hql.append(" where lancamento.id = :idLancamento ");
		hql.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe  ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idLancamento", idLancamento);
		query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);

		return (Boolean) query.uniqueResult();
	}
	
	public void desvincularEstudos(List<Long> idsEstudos) {
		
		this.getSession().createQuery(
			"update Lancamento lancamento set lancamento.estudo = null "
			+ "where lancamento.estudo.id in (:idsEstudos)")
			.setParameterList("idsEstudos", idsEstudos).executeUpdate();
	}

	@Override
	public List<Lancamento> obterMatrizLancamentosExpedidos(
			List<Date> datasConfirmadas) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean existeMatrizLancamentosExpedidos(
			List<Date> datasConfirmadas) {
				
		StringBuilder hql = new StringBuilder();

		hql.append(" select case when(count(lancamento.id) > 0) then true else false end ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" where lancamento.dataLancamentoDistribuidor in (:dataLancamento )");
		hql.append(" and lancamento.status = :status ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("dataLancamento", datasConfirmadas);
		query.setParameter("status", StatusLancamento.EXPEDIDO);
		
		return (Boolean) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> buscarDiasMatrizLancamentoAbertos(){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento.dataLancamentoDistribuidor,lancamento.status  from Lancamento lancamento ");
		hql.append(" where lancamento.dataLancamentoDistribuidor >= (select distribuidor.dataOperacao from Distribuidor distribuidor) ");
		hql.append(" group by lancamento.dataLancamentoDistribuidor , lancamento.status ");
		hql.append(" order by lancamento.dataLancamentoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());

		return  query.list();
		
	}

	public boolean existeProdutoEdicaoParaDia(ProdutoLancamentoDTO produtoLancamentoDTO,Date novaData){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento.id ");
		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" where lancamento.dataLancamentoDistribuidor =:dataLancamento ");
		hql.append(" and produto.codigo = :codProduto ");
		hql.append(" and produtoEdicao.id = :idProdutoEdicao ");
		hql.append(" and produtoEdicao.produto = produto.id ");
		hql.append(" and lancamento.id <> :idLancamento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataLancamento", novaData);
		query.setParameter("codProduto", produtoLancamentoDTO.getCodigoProduto());
		query.setParameter("idProdutoEdicao", produtoLancamentoDTO.getIdProdutoEdicao());
		query.setParameter("idLancamento", produtoLancamentoDTO.getIdLancamento());
		
		List lista =  query.list();
		
		if(lista==null || lista.isEmpty()){
			return false;
		}else {
			return true;
		}
		//return (Boolean) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<Lancamento> obterLancamentosRedistribuicoes() {

		StringBuilder hql = new StringBuilder();
		List<Lancamento> lista =new ArrayList<Lancamento>();
		LinkedList<Lancamento> listaAux =new LinkedList<Lancamento>();

		hql.append(" from Lancamento lancamento ");
		hql.append(" where (lancamento.tipoLancamento = :tipoLancamento ");
		hql.append(" or lancamento.periodoLancamentoParcial is not null )");
		//hql.append(" and lancamento.status <> :statusLancamento ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("tipoLancamento", TipoLancamento.REDISTRIBUICAO);
		//query.setParameter("statusLancamento", StatusLancamento.FECHADO);

		lista = query.list();
		//listaAux.addAll(lista);
		
		//
		for(Lancamento lancamento :lista){
		
			hql = new StringBuilder();
		
			hql.append(" from Lancamento lancamento");
			hql.append(" where ");
//					+ "lancamento.produtoEdicao.id = :idProdutoEdicao ");
//			hql.append(" and lancamento.produtoEdicao.produto.id = :idProduto ");
			hql.append(" lancamento.produtoEdicao.produto.codigo = :codigo ");
			hql.append(" and lancamento.produtoEdicao.numeroEdicao = :edicao ");
			hql.append(" order by lancamento.produtoEdicao.produto.codigo , ");
			hql.append("lancamento.produtoEdicao.numeroEdicao, ");
			hql.append("lancamento.periodoLancamentoParcial.numeroPeriodo, ");
			hql.append("lancamento.numeroLancamento ");


			query = getSession().createQuery(hql.toString());

			query.setParameter("codigo", lancamento.getProdutoEdicao().getProduto().getCodigo());
			
			query.setParameter("edicao", lancamento.getProdutoEdicao().getNumeroEdicao());
		
			listaAux.addAll(query.list());
		}
		
		
		return listaAux;
	}

	@SuppressWarnings("unchecked")
    public List<Lancamento> obterRedistribuicoes(Long idProdutoEdicao, Integer numeroPeriodo) {
        
        StringBuilder hql = new StringBuilder();

        hql.append(" select lancamento ");
        hql.append(" from Lancamento lancamento ");
        hql.append(" join lancamento.produtoEdicao produtoEdicao ");
        hql.append(" left join lancamento.periodoLancamentoParcial periodoLancamentoParcial "); 
        hql.append(" where produtoEdicao.id = :idProdutoEdicao ");
        hql.append(" and lancamento.tipoLancamento = :tipoLancamento ");
        
        if (numeroPeriodo != null) {
            
            hql.append(" and periodoLancamentoParcial.numeroPeriodo = :numeroPeriodo ");
        }
        
        Query query = getSession().createQuery(hql.toString());

        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameter("tipoLancamento", TipoLancamento.REDISTRIBUICAO);
        
        if (numeroPeriodo != null) {
            
            query.setParameter("numeroPeriodo", numeroPeriodo);
        }
        
        return  query.list();
    }
	
    public Lancamento obterLancamentoOriginalDaRedistribuicao(Long idProdutoEdicao, Integer numeroPeriodo) {
        
        StringBuilder hql = new StringBuilder();

        hql.append(" select lancamento ");
        hql.append(" from Lancamento lancamento ");
        hql.append(" join lancamento.produtoEdicao produtoEdicao ");
        hql.append(" left join lancamento.periodoLancamentoParcial periodoLancamentoParcial "); 
        hql.append(" where produtoEdicao.id = :idProdutoEdicao ");
        hql.append(" and lancamento.tipoLancamento = :tipoLancamento ");
        
        if (numeroPeriodo != null) {
            
            hql.append(" and periodoLancamentoParcial.numeroPeriodo = :numeroPeriodo ");
        }
        
        Query query = getSession().createQuery(hql.toString());

        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
        
        if (numeroPeriodo != null) {
            
            query.setParameter("numeroPeriodo", numeroPeriodo);
        }
        
        return  (Lancamento) query.uniqueResult();
    }
	
    @SuppressWarnings("unchecked")
    @Override
    public List<Lancamento> obterLancamentosDoPeriodoParcial(Long idPeriodo) {
        
        StringBuilder hql = new StringBuilder();
        
        hql.append(" select lancamento  ");
        hql.append(" from PeriodoLancamentoParcial periodoLancamentoParcial ");
        hql.append(" join periodoLancamentoParcial.lancamentos lancamento ");
        hql.append(" where periodoLancamentoParcial.id = :idPeriodo ");
        hql.append(" order by lancamento.dataLancamentoDistribuidor ");
        
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("idPeriodo", idPeriodo);
        
        return query.list();
    }
    

    public List<Date> obterDatasLancamentoValido(List<Long> idFornecedor) {

    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select dt from ( ");
    	hql.append(" select ADDDATE((select data_operacao from distribuidor),n-1) dt, DATE_FORMAT(ADDDATE((select data_operacao from distribuidor),n-1),'%w')+1 sm from ( ");
    	hql.append(" SELECT a.N + b.N * 10 + c.N * 100 + 1 n ");
    	hql.append(" FROM  ");
    	hql.append(" (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a ");
    	hql.append(" ,(SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b ");
    	hql.append(" ,(SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c ");
    	hql.append(" ORDER BY n) a ) b ");
    	hql.append(" where b.dt not in (select distinct data from feriado where tipo_feriado = 'FEDERAL' and ind_opera = 0) ");
    	hql.append(" and b.dt not in (select distinct data from feriado where tipo_feriado = 'ESTATUAL' and ind_opera = 0 and LOCALIDADE = (SELECT UPPER(e.uf) FROM endereco_distribuidor ed, endereco e where ed.endereco_id = e.id)) ");
    	hql.append(" and b.dt not in (select distinct data from feriado where tipo_feriado = 'MUNICIPAL' and ind_opera = 0 and LOCALIDADE = (SELECT UPPER(e.cidade) FROM endereco_distribuidor ed, endereco e where ed.endereco_id = e.id)) ");
    	hql.append(" and b.dt not in (select dtd from ( ");
    	hql.append(" select max(data_lcto_distribuidor) dtd , max(case when status in('CONFIRMADO','PLANEJADO') then 1 else 0 end ) st ");
    	hql.append(" from lancamento ");
    	hql.append(" where status in('BALANCEADO','CONFIRMADO','EM_BALANCEAMENTO','PLANEJADO') ");
    	hql.append(" group by data_lcto_distribuidor ) a ");
    	hql.append(" where st = 0) ");
    	hql.append(" and b.sm    in (select dia_semana  ");
    	hql.append(" from distribuicao_fornecedor  ");
    	hql.append(" where operacao_distribuidor = 'DISTRIBUICAO'  ");
    	hql.append(" and fornecedor_id in (:idFornecedor))  ");
    	hql.append(" order by dt  ");
    	
        Query query = getSession().createSQLQuery(hql.toString());
        
        query.setParameterList("idFornecedor", idFornecedor);

    	return query.list();
    }
    
    @Override
	public Lancamento obterParaAtualizar(Long id) {
		
		Query query = 
			this.getSession().createQuery("from Lancamento where id = :id ");
		
		query.setLockOptions(LockOptions.UPGRADE);
		
		query.setParameter("id", id);
		
		return (Lancamento) query.uniqueResult();
	}
    
}
