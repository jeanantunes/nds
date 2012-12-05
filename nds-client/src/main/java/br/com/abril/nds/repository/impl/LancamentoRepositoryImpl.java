package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoCanceladoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class LancamentoRepositoryImpl extends
		AbstractRepositoryModel<Lancamento, Long> implements LancamentoRepository {

	public LancamentoRepositoryImpl() {
		super(Lancamento.class);
	}

	private String getHQLDesconto() {
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.DESCONTO");
		hql.append(" from VIEW_DESCONTO view ")
		   .append(" where view.COTA_ID = cota.ID ")
		   .append(" and view.PRODUTO_EDICAO_ID = produtoEdicao.ID ")
		   .append(" and view.FORNECEDOR_ID = produtoFornecedor.FORNECEDORES_ID),0) ");
		
		return hql.toString();
	}
	
	@Override
	public SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data, List<Long> idsFornecedores) {
		StringBuilder hql = new StringBuilder("select count(lancamento) as totalLancamentos, " );
		hql.append("sum(lancamento.reparte * produtoEdicao.precoVenda) as valorTotalLancamentos ");
		hql.append("from Lancamento lancamento ");
		hql.append("join lancamento.produtoEdicao produtoEdicao ");
		hql.append("join produtoEdicao.produto produto ");
		hql.append("join produto.fornecedores fornecedor ");
		hql.append("where fornecedor.permiteBalanceamento = :permiteBalanceamento ");
		if (data != null) {
			hql.append("and lancamento.dataLancamentoPrevista = :data ");
		}
		boolean filtraFornecedores = idsFornecedores != null && !idsFornecedores.isEmpty();
		if (filtraFornecedores) {
			hql.append("and fornecedor.id in (:idsFornecedores) ");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("permiteBalanceamento", true);
		if (data != null) {
			query.setParameter("data", data);
		}
		if (filtraFornecedores) {
			query.setParameterList("idsFornecedores", idsFornecedores);
		}
		query.setResultTransformer(new AliasToBeanResultTransformer(SumarioLancamentosDTO.class));
		return (SumarioLancamentosDTO) query.uniqueResult();
	}

	@Override
	public void atualizarLancamento(Long idLancamento, Date novaDataLancamentoPrevista) {
		StringBuilder hql = new StringBuilder("update Lancamento set ");
		hql.append(" dataLancamentoPrevista = :novaDataLancamentoPrevista, ")
		   .append(" reparte = 0 ")
		   .append(" where id = :id");
		
		Query query = 
				this.getSession().createQuery(hql.toString());
		
		query.setParameter("novaDataLancamentoPrevista", novaDataLancamentoPrevista);
		query.setParameter("id", idLancamento);
		
		query.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ResumoPeriodoBalanceamentoDTO> buscarResumosPeriodo(
			List<Date> periodoDistribuicao, List<Long> fornecedores, GrupoProduto grupoCromo) {
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
			PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo) {
				
		Map<String, Object> parametros = new HashMap<String, Object>();
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento ");
		
		hql.append(gerarQueryProdutosNaoExpedidos(parametros, data, idFornecedor, estudo));	
		
		if( paginacaoVO != null ) {
			hql.append(gerarOrderByProdutosNaoExpedidos(
					LancamentoNaoExpedidoDTO.SortColumn.getByProperty(paginacaoVO.getSortColumn()),
					paginacaoVO.getOrdenacao()));
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		for (Entry<String, Object> entry: parametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		if( paginacaoVO != null ) {
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
		
		return (List<Lancamento>)query.list();
	}
	
	private String gerarOrderByProdutosNaoExpedidos(LancamentoNaoExpedidoDTO.SortColumn sortOrder, Ordenacao ascOrDesc) {

		String order;
		
		if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.DATA_ENTRADA)) {
			order = "itemRecebido.recebimentoFisico.dataRecebimento";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.CODIGO_PRODUTO)) {
			order = "produto.id";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.NOME_PRODUTO)) {
			order =  "produto.nome";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.EDICAO)) {
			order =  "produtoEdicao.numeroEdicao";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.CLASSIFICACAO_PRODUTO)) {
			order = "produto.tipoProduto.descricao";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.PRECO_PRODUTO)) {
			order =  "produtoEdicao.precoVenda";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.QTDE_PACOTE_PADRAO)) {
			order =  "produtoEdicao.pacotePadrao";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.QTDE_REPARTE)) {
			order =  "lancamento.reparte";
		} else if(sortOrder.equals(LancamentoNaoExpedidoDTO.SortColumn.DATA_CHAMADA)) {
			order =  "lancamento.dataRecolhimentoPrevista";
		} else {
			return "";
		}
		
		if ("".equals(order)) {
			
			return "";
		}
		
		if (ascOrDesc == null) {
			
			ascOrDesc = Ordenacao.ASC;
		}
		
		return " order by " + order + " " + ascOrDesc + " " ;
		
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
	private String gerarQueryProdutosNaoExpedidos(Map<String, Object> parametros, Date data, Long idFornecedor, Boolean estudo) {
		
		StringBuilder hql = new StringBuilder();	
		
		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		
		if(idFornecedor!=null) {
			hql.append(" join produto.fornecedores fornecedor ");
		}
		
		hql.append(" join lancamento.recebimentos itemRecebido ");
		
		boolean where = false;
		
		if (estudo != null && estudo == true ) {

			hql.append(" join lancamento.estudo estudo ");
			
			hql.append(" where estudo.status = :statusEstudo ");
			
			parametros.put("statusEstudo", StatusLancamento.ESTUDO_FECHADO);
			
			where = true;
		}
		
		if (!where) {
			
			hql.append(" where ");
			
		} else {
			
			hql.append(" and ");
		}
		
		//hql.append(" lancamento.status=:statusConfirmado ");
		hql.append(" lancamento.status=:statusBalanceado ");
		
		parametros.put("statusBalanceado", StatusLancamento.BALANCEADO);
		
		if (data != null) {
			
			hql.append(" AND lancamento.dataLancamentoDistribuidor = :data");
			
			parametros.put("data", data);
		}				
		
		if (idFornecedor != null) {
			hql.append(" AND fornecedor.id = :idFornecedor ");			
			parametros.put("idFornecedor", idFornecedor);
		}				
		
		
		
		return hql.toString();
	}
	
	public Boolean existeMatrizBalanceamentoConfirmado(Date data) {
		 
		StringBuilder jpql = new StringBuilder();
		Boolean existeLancamentoConfirmado = true;
		
		jpql = new StringBuilder();
		jpql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");	
		jpql.append(" FROM Lancamento lancamento ");
		jpql.append(" WHERE lancamento.dataLancamentoDistribuidor = :data ")
		    .append("   AND lancamento.status IN (:statusPlanejadoEConfirmado) ");
		
		// Implementado por Eduardo Punk Rock - A validação final será realizada utilizando apenas o estado BALANCEADO, que é o último estado liberado pela matriz de balanceamento
		//for (int i = 0; i < 2; i++) {
			
			Query query = getSession().createQuery(jpql.toString());
	
			List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();
			
			/*if(i == 0) {
				listaLancamentos.add(StatusLancamento.PLANEJADO);
				listaLancamentos.add(StatusLancamento.CONFIRMADO);
			} else {*/
				listaLancamentos.add(StatusLancamento.BALANCEADO);
			//}
			
			query.setParameterList("statusPlanejadoEConfirmado", listaLancamentos);
			query.setParameter("data", data);
			
			existeLancamentoConfirmado = existeLancamentoConfirmado && ((Boolean) query.uniqueResult());
			
		//}
		
		return existeLancamentoConfirmado;
		
	}
	
	public Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor, Boolean estudo) {
		
		Map<String, Object> parametros = new HashMap<String, Object>();
				 
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" select count(lancamento) ");	
		
		jpql.append(gerarQueryProdutosNaoExpedidos(parametros, data, idFornecedor, estudo));	
										
		Query query = getSession().createQuery(jpql.toString());
		
		for (Entry<String, Object> entry: parametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return (Long) query.uniqueResult();
	}
	
	public Lancamento obterLancamentoPorItensRecebimentoFisico(Date dataPrevista, TipoLancamento tipoLancamento, Long idProdutoEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Lancamento lancamento ");
		
		hql.append(" where lancamento.dataLancamentoPrevista = :dataPrevista ");
		
		if (tipoLancamento != null) {
			hql.append(" and lancamento.tipoLancamento = :tipoLancamento ");
		}
		
		hql.append(" and lancamento.produtoEdicao.id = :idProdutoEdicao");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setDate("dataPrevista", dataPrevista);
		
		if(tipoLancamento != null){	
			query.setParameter("tipoLancamento", tipoLancamento);
		}
		
		query.setLong("idProdutoEdicao", idProdutoEdicao);
		
		return (Lancamento) query.uniqueResult();
	}
	
	public Date obterDataRecolhimentoPrevista(String codigoProduto, Long numeroEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento.dataRecolhimentoPrevista  ")
			.append(" from Lancamento lancamento ")
			.append(" join lancamento.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")

			.append(" where produto.codigo = :codigoProduto ")
			.append(" and produtoEdicao.numeroEdicao =:numeroEdicao ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("codigoProduto", codigoProduto);
		query.setMaxResults(1);
		
		return (Date) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimento(Intervalo<Date> periodoRecolhimento,
																	   List<Long> fornecedores,
																	   GrupoProduto grupoCromo) {

		String sql = getConsultaBalanceamentoRecolhimentoAnalitico()
				   + " group by lancamento.ID "
				   + " order by dataRecolhimentoDistribuidor ";

		Query query = getQueryBalanceamentoRecolhimentoComParametros(periodoRecolhimento, fornecedores, grupoCromo, sql);

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimentoPorEditorData(Intervalo<Date> periodoRecolhimento, 
																					List<Long> fornecedores,
																					GrupoProduto grupoCromo) {

		String sql = getConsultaBalanceamentoRecolhimentoAnalitico() 
				   + " group by lancamento.ID "
				   + " order by idEditor, dataRecolhimentoDistribuidor ";

		Query query = getQueryBalanceamentoRecolhimentoComParametros(periodoRecolhimento, fornecedores, grupoCromo, sql);

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TreeMap<Date, BigDecimal> obterExpectativasEncalhePorData(Intervalo<Date> periodoRecolhimento, 
																 	 List<Long> fornecedores,
																 	 GrupoProduto grupoCromo) {

		String sql = this.getConsultaExpectativaEncalheData();
		
		Query query = getSession().createSQLQuery(sql); 

		List<String> statusParaBalanceamentoRecolhimento =
			this.getStatusParaBalanceamentoRecolhimento();
		
		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoInicial", periodoRecolhimento.getDe());
		query.setParameter("periodoFinal", periodoRecolhimento.getAte());
		query.setParameter("grupoCromo", grupoCromo);
		query.setParameter("tipoParcial", TipoLancamentoParcial.PARCIAL);
		
		query.setParameterList("statusParaBalanceamentoRecolhimento", statusParaBalanceamentoRecolhimento);

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
		
		String[] arrayStatusParaBalanceamentoRecolhimento =
			{StatusLancamento.EXPEDIDO.toString(),
			 StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.toString(),
			 StatusLancamento.BALANCEADO_RECOLHIMENTO.toString()};
		
		List<String> statusParaBalanceamentoRecolhimento =
			Arrays.asList(arrayStatusParaBalanceamentoRecolhimento);
		
		return statusParaBalanceamentoRecolhimento;
	}
	
	private String getConsultaBalanceamentoRecolhimentoAnalitico() {
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select ");
		sql.append(" fornecedor.ID as idFornecedor, ");
		sql.append(" pessoaFornecedor.RAZAO_SOCIAL as nomeFornecedor, ");
		sql.append(" periodoLancamentoParcial.TIPO as parcial, ");
		sql.append(" lancamento.STATUS as statusLancamento, ");
		sql.append(" lancamento.SEQUENCIA_MATRIZ as sequencia, ");
		sql.append(" lancamento.ID as idLancamento, ");
		sql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		sql.append(" lancamento.DATA_REC_PREVISTA as dataRecolhimentoPrevista, ");
		sql.append(" lancamento.DATA_REC_DISTRIB as dataRecolhimentoDistribuidor, ");
		sql.append(" lancamento.DATA_REC_DISTRIB as novaData, ");
		sql.append(" produto.EDITOR_ID as idEditor, ");
		sql.append(" pessoa.RAZAO_SOCIAL as nomeEditor, ");
				
		sql.append(" (select ");
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo and periodoLancamentoParcial.TIPO <> :tipoParcial then ");
		sql.append(" sum(((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO) ");
		sql.append(" else ");
		sql.append(" sum((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) ");
		sql.append(" end ");
		sql.append(" from COTA cota, ESTOQUE_PRODUTO_COTA epc, BOX box ");
		sql.append(" where epc.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		sql.append(" and cota.ID = epc.COTA_ID ");
		sql.append(" and cota.BOX_ID = box.ID and box.TIPO_BOX = :tipoBoxPostoAvancado) ");
		sql.append(" as expectativaEncalheAtendida, ");
		
		sql.append(" (select ");
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo and periodoLancamentoParcial.TIPO <> :tipoParcial then ");
		sql.append(" sum(((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO) ");
		sql.append(" else ");
		sql.append(" sum((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) ");
		sql.append(" end ");
		sql.append(" from COTA cota, ESTOQUE_PRODUTO_COTA epc, BOX box ");
		sql.append(" where epc.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		sql.append(" and cota.ID = epc.COTA_ID ");
		sql.append(" and cota.BOX_ID = box.ID and box.TIPO_BOX <> :tipoBoxPostoAvancado) ");
		sql.append(" as expectativaEncalheSede, ");
		
		sql.append(" (select ");
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo and periodoLancamentoParcial.TIPO <> :tipoParcial then ");
		sql.append(" sum(((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO) ");
		sql.append(" else ");
		sql.append(" sum((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) ");
		sql.append(" end ");
		sql.append(" from COTA cota, PDV pdv, ESTOQUE_PRODUTO_COTA epc ");
		sql.append(" where epc.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		sql.append(" and cota.ID = epc.COTA_ID and cota.ID = pdv.COTA_ID ");
		sql.append(" and pdv.PONTO_PRINCIPAL = :pontoPrincipal and pdv.TIPO_CARACTERISTICA_PDV = :tipoCaracteristicaPDV) ");
		sql.append(" as expectativaEncalheAlternativo, ");
		
		sql.append(" (select ");
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo and periodoLancamentoParcial.TIPO <> :tipoParcial then ");
		sql.append(" sum(((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO) ");
		sql.append(" else ");
		sql.append(" sum((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) ");
		sql.append(" end ");
		sql.append(" from COTA cota, ESTOQUE_PRODUTO_COTA epc ");
		sql.append(" where epc.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		sql.append(" and cota.ID = epc.COTA_ID) ");
		sql.append(" as expectativaEncalhe, ");
		
		sql.append(" (select ");
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo and periodoLancamentoParcial.TIPO <> :tipoParcial then ");
		sql.append(" sum((((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) / produtoEdicao.PACOTE_PADRAO) * (produtoEdicao.PRECO_VENDA - ( produtoEdicao.PRECO_VENDA * " + this.getHQLDesconto() + " / 100 ) )) ");
		sql.append(" else ");
		sql.append(" sum(((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) - ((epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) * (coalesce(produtoEdicao.EXPECTATIVA_VENDA, 0) / 100))) * (produtoEdicao.PRECO_VENDA - ( produtoEdicao.PRECO_VENDA * " + this.getHQLDesconto() + " / 100 ) )) ");
		sql.append(" end ");
		sql.append(" from COTA cota, ESTOQUE_PRODUTO_COTA epc ");
		sql.append(" where epc.PRODUTO_EDICAO_ID = produtoEdicao.ID ");
		sql.append(" and cota.ID = epc.COTA_ID) ");
		sql.append(" as valorTotal, ");
		
		sql.append(" case  ");
		sql.append(" when (chamadaEncalhe.ID is not null)  ");
		sql.append(" and chamadaEncalhe.TIPO_CHAMADA_ENCALHE <> :tipoChamadaEncalhe then true ");
		sql.append(" else false ");
		sql.append(" end as possuiChamada, ");
		sql.append(" produtoEdicao.ID as idProdutoEdicao, ");
		
		sql.append(" ( ");
		sql.append(getHQLDesconto());
		sql.append(" ) as desconto, ");
		
		
		sql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		sql.append(" produtoEdicao.PESO as peso, ");
		sql.append(" produtoEdicao.POSSUI_BRINDE as possuiBrinde, ");
		sql.append(" produtoEdicao.PRECO_VENDA as precoVenda, ");
		sql.append(" produto.codigo as codigoProduto, ");
		sql.append(" produto.nome as nomeProduto ");
		
		sql.append(" from ");
		sql.append(" ESTOQUE_PRODUTO_COTA estoqueProdutoCota  ");
		sql.append(" inner join ");
		sql.append(" PRODUTO_EDICAO produtoEdicao  ");
		sql.append(" on estoqueProdutoCota.PRODUTO_EDICAO_ID = produtoEdicao.ID  ");
		
		sql.append(" inner join ");
		sql.append(" COTA cota  ");
		sql.append(" on cota.ID=estoqueProdutoCota.COTA_ID ");
		
		sql.append(" inner join ");
		sql.append(" ESTUDO_COTA estudoCota  ");
		sql.append(" on cota.ID=estudoCota.COTA_ID ");

		sql.append(" inner join ");
		sql.append(" LANCAMENTO lancamento  ");
		sql.append(" on lancamento.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append(" inner join ");
		sql.append(" ESTUDO estudo ");
		sql.append(" on (estudo.ID = estudoCota.ESTUDO_ID ");
		sql.append(" 	 and estudo.PRODUTO_EDICAO_ID = lancamento.PRODUTO_EDICAO_ID ");
		sql.append(" 	 and estudo.DATA_LANCAMENTO = lancamento.DATA_LCTO_PREVISTA) ");
		
		sql.append(" inner join ");
		sql.append(" PRODUTO produto  ");
		sql.append(" on produtoEdicao.PRODUTO_ID=produto.ID  ");
		sql.append(" inner join ");
		sql.append(" PRODUTO_FORNECEDOR produtoFornecedor  ");
		sql.append(" on produto.ID=produtoFornecedor.PRODUTO_ID  ");
		sql.append(" left join ");
		sql.append(" CHAMADA_ENCALHE chamadaEncalhe  ");
		sql.append(" on chamadaEncalhe.PRODUTO_EDICAO_ID=produtoEdicao.ID  ");
		sql.append(" left join ");
		sql.append(" LANCAMENTO_PARCIAL lancamentoParcial  ");
		sql.append(" on lancamentoParcial.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append(" left join ");
		sql.append(" PERIODO_LANCAMENTO_PARCIAL periodoLancamentoParcial  ");
		sql.append(" on periodoLancamentoParcial.LANCAMENTO_PARCIAL_ID=lancamentoParcial.ID ");
		sql.append(" inner join ");
		sql.append(" FORNECEDOR fornecedor  ");
		sql.append(" on produtoFornecedor.fornecedores_ID=fornecedor.ID ");
		sql.append(" inner join ");
		sql.append(" EDITOR editor ");
		sql.append(" on produto.EDITOR_ID = editor.ID ");
		sql.append(" inner join ");
		sql.append(" PESSOA pessoa ");
		sql.append(" on editor.JURIDICA_ID = pessoa.ID, ");
		
		sql.append(" PESSOA pessoaFornecedor, ");
		sql.append(" TIPO_PRODUTO tipoProduto ");

		sql.append(" where ");
		sql.append(" fornecedor.JURIDICA_ID=pessoaFornecedor.ID ");
		sql.append(" and lancamento.STATUS in (:statusParaBalanceamentoRecolhimento) ");
		sql.append(" and produto.EDITOR_ID=editor.ID ");
		sql.append(" and produto.TIPO_PRODUTO_ID=tipoProduto.ID ");
		sql.append(" and ( ");
		sql.append(" lancamento.DATA_REC_DISTRIB between :periodoInicial and :periodoFinal ");
		sql.append(" ) ");

		sql.append(" and ( ");
		sql.append(" fornecedor.ID in (:idsFornecedores) ");
		sql.append(" )  ");
		sql.append(" and ( ");
		sql.append(" lancamento.DATA_REC_DISTRIB between :periodoInicial and :periodoFinal ");
		sql.append(" ) ");

		sql.append("and ( ");
		sql.append(" chamadaEncalhe.ID is null or ");
		sql.append(" chamadaEncalhe.DATA_RECOLHIMENTO between :periodoInicial and :periodoFinal ");  
		sql.append(" ) ");

		return sql.toString();
	}
	
	private String getConsultaExpectativaEncalheData() {
		
		String sql = " select analitica.dataRecolhimentoDistribuidor, "
				   + " sum(analitica.expectativaEncalhe) "
				   + " from "
				   + " ( "
				   + " select "
				   + " lancamento.DATA_REC_DISTRIB as dataRecolhimentoDistribuidor, "
				   + " case "
				   + " when tipoProduto.GRUPO_PRODUTO = :grupoCromo "
				   + " and periodoLancamentoParcial.TIPO <> :tipoParcial then "
				   + " case when produtoEdicao.EXPECTATIVA_VENDA is null or produtoEdicao.EXPECTATIVA_VENDA = 0 then "
				   + " sum((estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO) "
				   + " else "
				   + " sum(((estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA) - ((estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA) * (produtoEdicao.EXPECTATIVA_VENDA/100))) / produtoEdicao.PACOTE_PADRAO) "
				   + " end "
				   + " else "
				   + " case when produtoEdicao.EXPECTATIVA_VENDA is null or produtoEdicao.EXPECTATIVA_VENDA = 0 then "
				   + " sum(estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA) "
				   + " else "
				   + " sum((estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA) - ((estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA) * (produtoEdicao.EXPECTATIVA_VENDA/100))) "
				   + " end "
				   + " end as expectativaEncalhe ";
		
		String clausulaFrom = getConsultaBalanceamentoRecolhimentoAnalitico();
		
		clausulaFrom = clausulaFrom.substring(clausulaFrom.lastIndexOf(" from "));
		
		sql += clausulaFrom;
		sql += " group by lancamento.ID ";
		sql += " ) as analitica ";
		sql += " group by analitica.dataRecolhimentoDistribuidor ";
		
		return sql;
	}
	
	private Query getQueryBalanceamentoRecolhimentoComParametros(Intervalo<Date> periodoRecolhimento,
																 List<Long> fornecedores,
																 GrupoProduto grupoCromo,
																 String sql) {
		
		Query query = getSession().createSQLQuery(sql).addScalar("nomeFornecedor")
													  .addScalar("statusLancamento")
													  .addScalar("precoVenda")
													  .addScalar("codigoProduto")
													  .addScalar("nomeProduto")
													  .addScalar("nomeEditor")
													  .addScalar("dataLancamento")
													  .addScalar("dataRecolhimentoPrevista")
													  .addScalar("dataRecolhimentoDistribuidor")
													  .addScalar("expectativaEncalhe")
													  .addScalar("expectativaEncalheSede")
													  .addScalar("expectativaEncalheAtendida")
													  .addScalar("expectativaEncalheAlternativo")
													  .addScalar("valorTotal")
													  .addScalar("desconto")
													  .addScalar("parcial")
													  .addScalar("peso", StandardBasicTypes.LONG)
													  .addScalar("idEditor", StandardBasicTypes.LONG)
													  .addScalar("idLancamento", StandardBasicTypes.LONG)
													  .addScalar("numeroEdicao", StandardBasicTypes.LONG)
													  .addScalar("idFornecedor", StandardBasicTypes.LONG)
													  .addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
													  .addScalar("possuiBrinde", StandardBasicTypes.BOOLEAN)
													  .addScalar("possuiChamada", StandardBasicTypes.BOOLEAN)
													  .addScalar("sequencia", StandardBasicTypes.INTEGER)
													  .addScalar("novaData");													  

		List<String> statusParaBalanceamentoRecolhimento =
			this.getStatusParaBalanceamentoRecolhimento();
		
		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoInicial", periodoRecolhimento.getDe());
		query.setParameter("periodoFinal", periodoRecolhimento.getAte());
		query.setParameter("grupoCromo", grupoCromo);
		query.setParameter("tipoParcial", TipoLancamentoParcial.PARCIAL.toString());
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO.toString());
		query.setParameter("tipoBoxPostoAvancado", TipoBox.POSTO_AVANCADO.toString());
		query.setParameter("pontoPrincipal", true);
		query.setParameter("tipoCaracteristicaPDV", TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO.toString());
		
		
		query.setParameterList("statusParaBalanceamentoRecolhimento", statusParaBalanceamentoRecolhimento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoRecolhimentoDTO.class));
		
		return query;
	}

	@Override
	public Lancamento obterUltimoLancamentoDaEdicao(Long idProdutoEdicao) {
		

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
		   .append(" from Lancamento lancamento ")
		   .append(" where lancamento.dataLancamentoDistribuidor = ")
		   .append(" (select max(lancamentoMaxDate.dataLancamentoDistribuidor) ")
		   .append(" from Lancamento lancamentoMaxDate where lancamentoMaxDate.produtoEdicao.id=:idProdutoEdicao ) ")
		   .append(" and lancamento.produtoEdicao.id=:idProdutoEdicao ");
		
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		Object lancamento = query.uniqueResult();
		
		return (lancamento!=null) ? (Lancamento) lancamento : null ;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosPorIdOrdenados(Set<Long> idsLancamento) {

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
	 * @see br.com.abril.nds.repository.LancamentoRepository#obterLancamentoInformeRecolhimento(java.lang.Long, java.util.Calendar, java.util.Calendar, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(Long idFornecedor, Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento, String  orderBy, Ordenacao ordenacao, Integer initialResult, Integer maxResults){
	
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select ");
		
		hql.append(" lancamento.id as idLancamento, ");
		hql.append(" lancamento.produtoEdicao.id as idProdutoEdicao, 		  	");
		hql.append(" lancamento.sequenciaMatriz as sequenciaMatriz,			  	");
		hql.append(" produto.codigo as codigoProduto, 	");
		hql.append(" produto.nome as nomeProduto,		");
		hql.append(" periodoLancamentoParcial.tipo as tipoLancamentoParcial, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao,		");
		hql.append(" produtoEdicao.chamadaCapa as chamadaCapa,		");
		hql.append(" produtoEdicao.codigoDeBarras as codigoDeBarras, ");
		hql.append(" produtoEdicao.precoVenda as precoVenda, 		");
		
		hql.append(" coalesce(produto.desconto, 0) as desconto, 	");
		
		hql.append(" ( produtoEdicao.precoVenda -  ");
		
		hql.append(" ( produtoEdicao.precoVenda * ( coalesce(produto.desconto, 0) / 100 ) ) ) as precoDesconto, ");
		
		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, 		");
		
		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, 	");
		
		hql.append(" lancamentoParcial.recolhimentoFinal as dataRecolhimentoFinal, 	");
		
		hql.append(" editorPessoaJuridica.razaoSocial as nomeEditor		");

		hql.append(this.getHQLObtemLancamentoInformeRecolhimento(idFornecedor, dataInicioRecolhimento, dataFimRecolhimento));

		hql.append(" order by ");
		
		hql.append(orderBy);
		
		if(Ordenacao.ASC ==  ordenacao) {
			hql.append(" asc ");
		} else if(Ordenacao.DESC ==  ordenacao) {
			hql.append(" desc ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		if (idFornecedor != null) {
			query.setParameter("idFornecedor", idFornecedor);
		}
		
		query.setParameter("dataInicioRecolhimento", dataInicioRecolhimento.getTime());
		query.setParameter("dataFimRecolhimento", dataFimRecolhimento.getTime());
		query.setParameter("statusLancamento", StatusLancamento.BALANCEADO_RECOLHIMENTO);
		
		if (maxResults != null) {
			query.setMaxResults(maxResults);
		}
		if (initialResult !=null) {
			query.setFirstResult(initialResult);
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformeEncalheDTO.class));
		
		return query.list();
		
	}

	/**
	 * Obtém a clausula "from" que compõe do HQL para consulta de dados de lancamento.
	 * 
	 * @param idFornecedor
	 * @param dataInicioRecolhimento
	 * @param dataFimRecolhimento
	 * 
	 * @return String
	 */
	private String getHQLObtemLancamentoInformeRecolhimento(Long idFornecedor, Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento) {
		
		StringBuffer hql = new StringBuffer();

		hql.append(" from Lancamento lancamento ");
		
		hql.append(" inner join lancamento.produtoEdicao as produtoEdicao 	");
		hql.append(" inner join produtoEdicao.produto as produto 			");
		hql.append(" inner join produto.fornecedores as fornecedor 			");
		hql.append(" left join produto.editor as editor ");
		hql.append(" left join editor.pessoaJuridica as editorPessoaJuridica ");
		hql.append(" left join lancamento.periodoLancamentoParcial as periodoLancamentoParcial 	");
		hql.append(" left join periodoLancamentoParcial.lancamentoParcial as lancamentoParcial	");
		
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
		
		query.setParameter("dataInicioRecolhimento", dataInicioRecolhimento.getTime());
		
		query.setParameter("dataFimRecolhimento", dataFimRecolhimento.getTime());
		
		query.setParameter("statusLancamento", StatusLancamento.BALANCEADO_RECOLHIMENTO);

		return hql.toString();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.LancamentoRepository#quantidadeLancamentoInformeRecolhimento(java.lang.Long, java.util.Calendar, java.util.Calendar)
	 */
	@Override
	public Long quantidadeLancamentoInformeRecolhimento(Long idFornecedor, Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select count(lancamento.id) ");
		
		hql.append(this.getHQLObtemLancamentoInformeRecolhimento(idFornecedor, dataInicioRecolhimento, dataFimRecolhimento));
		
		Query query = getSession().createQuery(hql.toString());
		
		if (idFornecedor != null) {
			query.setParameter("idFornecedor", idFornecedor);
		}
		
		query.setParameter("dataInicioRecolhimento", dataInicioRecolhimento.getTime());
		
		query.setParameter("dataFimRecolhimento", dataFimRecolhimento.getTime());

		query.setParameter("statusLancamento", StatusLancamento.BALANCEADO_RECOLHIMENTO);
		
		return (Long) query.uniqueResult();
		
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.LancamentoRepository#obterDataUltimoLancamentoParcial(java.lang.Long, java.util.Date)
	 */
	public Date obterDataUltimoLancamentoParcial(Long idProdutoEdicao, Date dataOperacao) {
		
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

	public Date obterDataUltimoLancamento(Long idProdutoEdicao, Date dataOperacao) {
		
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
				dataInicioRecolhimento, dataFimRecolhimento, null,
				null, null, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoLancamentoDTO> obterBalanceamentoLancamento(Intervalo<Date> periodoDistribuicao,
																   List<Long> fornecedores) {

		String sql = this.montarConsultaBalanceamentoLancamentoAnalitico()
				   + " order by dataLancamentoDistribuidor ";
		
		Query query = this.getQueryBalanceamentoRecolhimento(periodoDistribuicao,
															 fornecedores,
															 sql);

		return query.list();
	}
	
	private String montarConsultaBalanceamentoLancamentoAnalitico() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" periodoLancamentoParcial.TIPO as parcial, ");
		sql.append(" lancamento.STATUS as statusLancamento, ");
		sql.append(" lancamento.ID as idLancamento, ");
		sql.append(" lancamento.DATA_LCTO_PREVISTA as dataLancamentoPrevista, ");
		sql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamentoDistribuidor, ");
		sql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as novaDataLancamento, ");
		sql.append(" lancamento.DATA_REC_PREVISTA as dataRecolhimentoPrevista, ");
		sql.append(" lancamento.ALTERADO_INTERFACE as alteradoInteface, ");

		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo then ");
		sql.append(" lancamento.REPARTE / produtoEdicao.PACOTE_PADRAO ");
		sql.append(" else ");
		sql.append(" lancamento.REPARTE ");
		sql.append(" end as repartePrevisto, ");
		
		sql.append(" lancamento.NUMERO_REPROGRAMACOES as numeroReprogramacoes, ");
		
		sql.append(" case when tipoProduto.GRUPO_PRODUTO = :grupoCromo then ");
		sql.append(" (lancamento.REPARTE / produtoEdicao.PACOTE_PADRAO) * (produtoEdicao.PRECO_VENDA - coalesce(produto.DESCONTO_ID, 0, (select VALOR from DESCONTO where ID = produto.DESCONTO_ID))) ");
		sql.append(" else ");
		sql.append(" lancamento.REPARTE * (produtoEdicao.PRECO_VENDA - coalesce(produto.DESCONTO_ID, 0, (select VALOR from DESCONTO where ID = produto.DESCONTO_ID))) ");
		sql.append(" end as valorTotal, ");
		
		sql.append(" produtoEdicao.ID as idProdutoEdicao, ");
		sql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		sql.append(" produtoEdicao.PESO as peso, ");
		sql.append(" produtoEdicao.PRECO_VENDA as precoVenda, ");
		sql.append(" produto.CODIGO as codigoProduto, ");
		sql.append(" produto.NOME as nomeProduto, ");
		sql.append(" produto.PERIODICIDADE as periodicidadeProduto, ");
		
		sql.append(" ( ");
		sql.append(" 	select sum(itemRecebFisico.QTDE_FISICO) ");
		sql.append(" 		from RECEBIMENTO_FISICO recebimentoFisico ");
		sql.append(" 		inner join ");
		sql.append(" 			ITEM_RECEB_FISICO itemRecebFisico ");
		sql.append(" 			on recebimentoFisico.ID = itemRecebFisico.RECEBIMENTO_FISICO_ID ");
		sql.append(" 		inner join ");
		sql.append(" 			LANCAMENTO_ITEM_RECEB_FISICO lancamentoItemRecebFisico ");
		sql.append(" 			on itemRecebFisico.ID = lancamentoItemRecebFisico.RECEBIMENTOS_ID ");
		sql.append(" 		where lancamentoItemRecebFisico.LANCAMENTO_ID = lancamento.ID ");
		sql.append(" ) as reparteFisico, ");
		
		sql.append(" case when ( ");
		sql.append(" 	select recebimentoFisico.ID ");
		sql.append(" 		from RECEBIMENTO_FISICO recebimentoFisico ");
		sql.append(" 		inner join ");
		sql.append(" 			ITEM_RECEB_FISICO itemRecebFisico ");
		sql.append(" 			on recebimentoFisico.ID = itemRecebFisico.RECEBIMENTO_FISICO_ID ");
		sql.append(" 		inner join ");
		sql.append(" 			LANCAMENTO_ITEM_RECEB_FISICO lancamentoItemRecebFisico ");
		sql.append(" 			on itemRecebFisico.ID = lancamentoItemRecebFisico.RECEBIMENTOS_ID ");
		sql.append(" 		where lancamentoItemRecebFisico.LANCAMENTO_ID = lancamento.ID ");
		sql.append(" 		limit 1 ");
		sql.append(" 	) is not null then ");
		sql.append(" 	true ");
		sql.append(" else ");
		sql.append(" 	false ");
		sql.append(" end as possuiRecebimentoFisico, ");
		
		sql.append(" case when ( ");
		sql.append(" 	select furoProduto.ID ");
		sql.append(" 		from FURO_PRODUTO furoProduto ");
		sql.append(" 		where furoProduto.LANCAMENTO_ID = lancamento.ID ");
		sql.append(" 		limit 1 ");
		sql.append(" 	) is not null then ");
		sql.append(" 	true ");
		sql.append(" else ");
		sql.append(" 	false ");
		sql.append(" end as possuiFuro ");
		
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
		sql.append(" inner join ");
		sql.append(" PRODUTO produto ");
		sql.append(" on produtoEdicao.PRODUTO_ID = produto.ID ");
		sql.append(" inner join ");
		sql.append(" TIPO_PRODUTO tipoProduto ");
		sql.append(" on tipoProduto.ID = produto.TIPO_PRODUTO_ID ");
		
		sql.append(" left join ");
		sql.append(" PERIODO_LANCAMENTO_PARCIAL periodoLancamentoParcial ");
		sql.append(" on periodoLancamentoParcial.LANCAMENTO_ID = lancamento.ID ");
		sql.append(" left join ");
		sql.append(" LANCAMENTO_PARCIAL lancamentoParcial ");
		sql.append(" on lancamentoParcial.ID = periodoLancamentoParcial.LANCAMENTO_PARCIAL_ID ");
		
		sql.append(" where ");
		sql.append("  UPPER(tipoProduto.GRUPO_PRODUTO) in ( :tiposProduto ) ");
		
		sql.append(" and ( ");
		sql.append(" 	select fornecedor.ID from PRODUTO_FORNECEDOR produtoFornecedor, FORNECEDOR fornecedor ");
		sql.append(" 		where produtoFornecedor.PRODUTO_ID = produto.ID ");
		sql.append(" 		and produtoFornecedor.FORNECEDORES_ID = fornecedor.ID ");
		sql.append(" 		and fornecedor.ID in ( :idsFornecedores ) ");
		sql.append(" 		limit 1 ");
		sql.append(" ) is not null ");
		
		sql.append(" and ( ");
		sql.append("	(lancamento.DATA_LCTO_DISTRIBUIDOR between :periodoInicial and :periodoFinal ");
		sql.append("		and  UPPER(lancamento.STATUS) in ( :statusLancamentoNoPeriodo )) ");
		sql.append(" 	or (lancamento.DATA_LCTO_DISTRIBUIDOR < :periodoInicial ");
		sql.append("		and UPPER(lancamento.STATUS) in ( :statusLancamentoDataMenorInicial )) "); 
		sql.append(" ) ");
		
		return sql.toString();
	}
	
	private Query getQueryBalanceamentoRecolhimento(Intervalo<Date> periodoDistribuicao,
											        List<Long> fornecedores,
											        String sql) {

		Query query = getSession().createSQLQuery(sql).addScalar("parcial")
			.addScalar("statusLancamento")
			.addScalar("idLancamento", StandardBasicTypes.LONG)
			.addScalar("dataLancamentoPrevista")
			.addScalar("dataLancamentoDistribuidor")
			.addScalar("novaDataLancamento")
			.addScalar("dataRecolhimentoPrevista")
			.addScalar("repartePrevisto")
			.addScalar("numeroReprogramacoes", StandardBasicTypes.INTEGER)
			.addScalar("reparteFisico")
			.addScalar("valorTotal")
			.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
			.addScalar("numeroEdicao", StandardBasicTypes.LONG)
			.addScalar("peso", StandardBasicTypes.LONG)
			.addScalar("precoVenda")
			.addScalar("codigoProduto")
			.addScalar("nomeProduto")
			.addScalar("periodicidadeProduto")
			.addScalar("possuiRecebimentoFisico", StandardBasicTypes.BOOLEAN)
			.addScalar("possuiFuro", StandardBasicTypes.BOOLEAN)
			.addScalar("alteradoInteface", StandardBasicTypes.BOOLEAN);		
		
		this.aplicarParametros(query, periodoDistribuicao, fornecedores);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoLancamentoDTO.class));

		return query;
	}
	
	private void aplicarParametros(Query query,
								   Intervalo<Date> periodoDistribuicao,
								   List<Long> fornecedores) {
		
		String[] arrayStatusLancamentoNoPeriodo = {StatusLancamento.PLANEJADO.name(),
												   StatusLancamento.CONFIRMADO.name(),
												   StatusLancamento.BALANCEADO.name(),
												   StatusLancamento.ESTUDO_FECHADO.name(),
												   StatusLancamento.FURO.toString()};
		
		String[] arrayStatusLancamentoDataMenorInicial = {StatusLancamento.PLANEJADO.name(),
				  										  StatusLancamento.CONFIRMADO.name()};
		
		String[] arrayTipoProduto = {GrupoProduto.OUTROS.name(),
									 GrupoProduto.REVISTA.name(),
									 GrupoProduto.CROMO.name(),
									 GrupoProduto.CARTELA.name(),
									 GrupoProduto.LIVRO.name(),
									 GrupoProduto.COLECIONAVEL.name()};
		
		List<String> statusLancamentoNoPeriodo = Arrays.asList(arrayStatusLancamentoNoPeriodo);
		
		List<String> statusLancamentoDataMenorInicial = Arrays.asList(arrayStatusLancamentoDataMenorInicial);
		
		List<String> tiposProduto = Arrays.asList(arrayTipoProduto);
		
		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoInicial", periodoDistribuicao.getDe());
		query.setParameter("periodoFinal", periodoDistribuicao.getAte());
		query.setParameterList("statusLancamentoNoPeriodo", statusLancamentoNoPeriodo);
		query.setParameterList("statusLancamentoDataMenorInicial", statusLancamentoDataMenorInicial);
		query.setParameterList("tiposProduto", tiposProduto);
		query.setParameter("grupoCromo", GrupoProduto.CROMO.toString());
	}

	@Override
	public Date buscarUltimoBalanceamentoLancamentoRealizadoDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("status", StatusLancamento.BALANCEADO));
		criteria.add(Restrictions.eq("dataLancamentoDistribuidor", dataOperacao));
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
	public Date buscarUltimoBalanceamentoRecolhimentoRealizadoDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("dataRecolhimentoDistribuidor", dataOperacao));
		criteria.add(Restrictions.eq("status", StatusLancamento.BALANCEADO_RECOLHIMENTO));
		criteria.setProjection(Projections.max("dataRecolhimentoDistribuidor"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Date buscarDiaUltimoBalanceamentoRecolhimentoRealizado() {
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("status", StatusLancamento.BALANCEADO_RECOLHIMENTO));
		criteria.setProjection(Projections.max("dataRecolhimentoDistribuidor"));
		return (Date) criteria.uniqueResult();
	}
	
	@Override
	public Lancamento obterLancamentoProdutoPorDataLancamentoOuDataRecolhimento(ProdutoEdicao produtoEdicao, Date dataLancamentoPrevista, Date dataRecolhimentoPrevista){
		
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
			query.setParameter("dataRecolhimentoPrevista", dataRecolhimentoPrevista);
		}
		
		return (Lancamento) query.uniqueResult();
	}
	
	@Override
	public Lancamento obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(ProdutoEdicao produtoEdicao, Date dataLancamentoPrevista, Date dataLancamentoDistribuidor){
		
		StringBuilder sql = new StringBuilder();
	
		sql.append(" select lancamento  from Lancamento lancamento ");
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
		
		query.setParameter("dataLancamentoDistribuidor", dataLancamentoDistribuidor);
		
		return (Lancamento) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeLancamentos(StatusLancamento statusLancamento){
		
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
	public BigDecimal obterConsignadoDia(StatusLancamento statusLancamento){
		
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
		   .append(" and lancamento.dataLancamentoPrevista between :periodoInicial and :periodoFinal ")
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

		criteria.add(Restrictions.eq("produtoEdicao.id", lancamentoAtual.getProdutoEdicao().getId()));

		criteria.add(Restrictions.gt("dataLancamentoPrevista", lancamentoAtual.getDataLancamentoPrevista()));

		criteria.addOrder(Order.asc("dataLancamentoPrevista"));
		
		criteria.setMaxResults(1);
		
		return (Lancamento) criteria.uniqueResult();
	}

	public Date obterDataMinimaProdutoEdicao(Long idProdutoEdicao, String propertyLancamentoDistribuidor) {
		
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		
		criteria.setProjection(Projections.min(propertyLancamentoDistribuidor));
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));

		criteria.add(Restrictions.ne("status", StatusLancamento.EXCLUIDO));
		
		criteria.add(Restrictions.ne("status", StatusLancamento.FURO));
		
		return (Date) criteria.uniqueResult();
	}

	public Date obterDataMaximaProdutoEdicao(Long idProdutoEdicao, String propertyLancamentoDistribuidor) {
		
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		
		criteria.setProjection(Projections.max(propertyLancamentoDistribuidor));
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		criteria.add(Restrictions.ne("status", StatusLancamento.EXCLUIDO));
		
		criteria.add(Restrictions.ne("status", StatusLancamento.FURO));
		
		return (Date) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoqueCota> buscarMovimentosEstoqueCotaParaFuro(Lancamento lancamento, TipoMovimentoEstoque tipoMovimentoEstoqueFuro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select mec ")
		   .append(" from Lancamento l ")
		   .append(" join l.movimentoEstoqueCotas mec ")
		   .append(" where l.id = :idLancamento ")
		   .append(" and mec.tipoMovimento != :tipoMovimentoEstoqueFuro ")
		   .append(" and mec.movimentoEstoqueCotaFuro is null ");
		
        Query query = super.getSession().createQuery(hql.toString());
        
        query.setParameter("idLancamento", lancamento.getId());
        query.setParameter("tipoMovimentoEstoqueFuro", tipoMovimentoEstoqueFuro);
		
    	return query.list();
	}
}
