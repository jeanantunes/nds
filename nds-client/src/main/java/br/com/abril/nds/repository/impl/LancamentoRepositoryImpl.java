package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.PeriodoVO;

@Repository
public class LancamentoRepositoryImpl extends
		AbstractRepository<Lancamento, Long> implements LancamentoRepository {

	public LancamentoRepositoryImpl() {
		super(Lancamento.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterBalanceamentoMatrizLancamentos(FiltroLancamentoDTO filtro) {
		StringBuilder hql = new StringBuilder("select lancamento from Lancamento lancamento ");
		hql.append("join fetch lancamento.produtoEdicao produtoEdicao ");
		hql.append("join fetch produtoEdicao.produto produto ");
		hql.append("join fetch produto.fornecedores fornecedor ");
		hql.append("left join fetch lancamento.recebimentos recebimento ");
		hql.append("left join fetch lancamento.estudo estudo ");
		hql.append("where fornecedor.permiteBalanceamento = :permiteBalanceamento ");
		if (filtro.getData() != null) {
			hql.append("and lancamento.dataLancamentoPrevista = :data ");
		}
		if (filtro.filtraFornecedores()) {
			hql.append("and fornecedor.id in (:idsFornecedores) ");
		}
		PaginacaoVO paginacao = filtro.getPaginacao();
		ColunaOrdenacao colunaOrdenacao = filtro.getColunaOrdenacao();
		if (colunaOrdenacao != null) {
			if (ColunaOrdenacao.CODIGO_PRODUTO == colunaOrdenacao) {
				hql.append("order by produto.codigo ");
			} else if (ColunaOrdenacao.NOME_PRODUTO == colunaOrdenacao) {
				hql.append("order by produto.nome ");
			} else if (ColunaOrdenacao.NUMERO_EDICAO == colunaOrdenacao) {
				hql.append("order by produtoEdicao.numeroEdicao ");
			} else if (ColunaOrdenacao.PRECO == colunaOrdenacao) {
				hql.append("order by produtoEdicao.precoVenda ");
			} else if (ColunaOrdenacao.PACOTE_PADRAO == colunaOrdenacao) {
				hql.append("order by produtoEdicao.pacotePadrao ");
			} else if (ColunaOrdenacao.REPARTE == colunaOrdenacao) {
				hql.append("order by lancamento.reparte ");
			} else if (ColunaOrdenacao.FISICO == colunaOrdenacao) {
				hql.append("order by recebimento.qtdeFisico ");
			} else if (ColunaOrdenacao.ESTUDO_GERADO == colunaOrdenacao) {
					hql.append("order by estudo.qtdeReparte ");
			} else if (ColunaOrdenacao.LANCAMENTO == colunaOrdenacao) {
				hql.append("order by lancamento.tipoLancamento ");
			} else if (ColunaOrdenacao.RECOLHIMENTO == colunaOrdenacao) {
				hql.append("order by lancamento.dataRecolhimentoPrevista ");
			} else if (ColunaOrdenacao.FORNECEDOR == colunaOrdenacao) {
				hql.append("order by fornecedor.juridica.nomeFantasia ");
			} else if (ColunaOrdenacao.DATA_LANC_DISTRIB == colunaOrdenacao) {
				hql.append("order by lancamento.dataLancamentoDistribuidor ");
			} else if (ColunaOrdenacao.DATA_LANC_PREVISTO == colunaOrdenacao) {
				hql.append("order by lancamento.dataLancamentoPrevista ");
			} else if (ColunaOrdenacao.TOTAL == colunaOrdenacao) {
				hql.append("order by (lancamento.reparte * produtoEdicao.precoVenda) ");
			}
			String ordenacao = "asc";
			if (paginacao != null) {
				if (paginacao.getOrdenacao().equals(Ordenacao.DESC)) {
					ordenacao = "desc";
				}
			}
			hql.append(ordenacao);
		}

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("permiteBalanceamento", true);
		if (filtro.getData() != null) {
			query.setParameter("data", filtro.getData());
		}
		if (filtro.filtraFornecedores()) {
			query.setParameterList("idsFornecedores", filtro.getIdsFornecedores());
		}

		if (paginacao != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}
		return query.list();
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
				
		if (estudo != null && estudo == true ) {

			hql.append(" where lancamento.status=:statusEstudoFechado ");
			
			parametros.put("statusEstudoFechado", StatusLancamento.ESTUDO_FECHADO);
		} else {

			hql.append(" where (lancamento.status=:statusConfirmado ");
			hql.append(" or lancamento.status=:statusBalanceado ");
			hql.append(" or lancamento.status=:statusEstudoFechado) ");
			
			parametros.put("statusConfirmado", StatusLancamento.CONFIRMADO);
			parametros.put("statusBalanceado", StatusLancamento.BALANCEADO);
			parametros.put("statusEstudoFechado", StatusLancamento.ESTUDO_FECHADO);
		}
		
		
		if (data != null) {
			
			hql.append(" AND lancamento.dataLancamentoPrevista = :data");
			
			parametros.put("data", data);
		}				
		
		if (idFornecedor != null) {
			hql.append(" AND fornecedor.id = :idFornecedor ");			
			parametros.put("idFornecedor", idFornecedor);
		}				
		
		
		
		return hql.toString();
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
		
		return (Date) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimento(PeriodoVO periodoRecolhimento,
																	   List<Long> fornecedores,
																	   GrupoProduto grupoCromo) {

		String sql = getConsultaBalanceamentoRecolhimentoAnalitico() 
				   + " order by dataRecolhimentoDistribuidor ";

		Query query = getQueryBalanceamentoRecolhimentoComParametros(periodoRecolhimento, fornecedores, grupoCromo, sql);

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimentoPorEditorData(PeriodoVO periodoRecolhimento, 
																					List<Long> fornecedores,
																					GrupoProduto grupoCromo) {

		String sql = getConsultaBalanceamentoRecolhimentoAnalitico() 
				   + " order by idEditor, dataRecolhimentoDistribuidor ";

		Query query = getQueryBalanceamentoRecolhimentoComParametros(periodoRecolhimento, fornecedores, grupoCromo, sql);

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TreeMap<Date, BigDecimal> obterExpectativasEncalhePorData(PeriodoVO periodoRecolhimento, 
																 List<Long> fornecedores,
																 GrupoProduto grupoCromo) {

		String sql = getConsultaExpectativaEncalheData();
		
		Query query = getSession().createSQLQuery(sql); 

		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoInicial", periodoRecolhimento.getDataInicial());
		query.setParameter("periodoFinal", periodoRecolhimento.getDataFinal());
		query.setParameter("grupoCromo", grupoCromo);
		query.setParameter("tipoParcial", TipoLancamentoParcial.PARCIAL);
		query.setParameter("statusLancamentoExpedido", StatusLancamento.EXPEDIDO.toString());
		query.setParameter("statusLancamentoBalanceamentoRecolhimento", StatusLancamento.BALANCEADO_RECOLHIMENTO.toString());

		List<Object[]> expectativasEncalheDia = query.list();

		TreeMap<Date, BigDecimal> mapaExpectativaEncalheDia = new TreeMap<Date, BigDecimal>();

		for (Object[] expectativa : expectativasEncalheDia) {

			Date data = (Date) expectativa[0];

			BigDecimal expectativaEncalhe = (BigDecimal) expectativa[1];

			mapaExpectativaEncalheDia.put(data, expectativaEncalhe);
		}

		return mapaExpectativaEncalheDia;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verificarExistenciaChamadaEncalheMatrizRecolhimento(PeriodoVO periodo) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select count(chamadaEncalhe) ")
		   .append(" from ChamadaEncalhe chamadaEncalhe ")
		   .append(" where chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
		   .append(" and chamadaEncalhe.dataRecolhimento between :dataInicial and :dataFinal ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		query.setParameter("dataInicial", periodo.getDataInicial());
		query.setParameter("dataFinal", periodo.getDataFinal());
		
		Long quantidadeRegistrosEncontrados = (Long) query.uniqueResult();
		
		return quantidadeRegistrosEncontrados > 0;
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
		sql.append(" produto.EDITOR_ID as idEditor, ");
		sql.append(" editor.NOME as nomeEditor, ");

		sql.append(" case  ");
		sql.append(" when box.POSTO_AVANCADO = 1 then ");
		sql.append(" case  ");
		sql.append(" when tipoProduto.GRUPO_PRODUTO = :grupoCromo ");
		sql.append(" and periodoLancamentoParcial.TIPO<> :tipoParcial then ");
		sql.append(" (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" else estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" end ");
		sql.append(" end as expectativaEncalheAtendida, ");

		sql.append(" case  ");
		sql.append(" when box.POSTO_AVANCADO = 0 then ");
		sql.append(" case  ");
		sql.append(" when tipoProduto.GRUPO_PRODUTO = :grupoCromo ");
		sql.append(" and periodoLancamentoParcial.TIPO<> :tipoParcial ");
		sql.append(" then (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" else estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" end ");
		sql.append(" end as expectativaEncalheSede, ");

		sql.append(" case  ");
		sql.append(" when tipoProduto.GRUPO_PRODUTO = :grupoCromo ");
		sql.append(" and periodoLancamentoParcial.TIPO <> :tipoParcial then ");
		sql.append(" (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" else estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" end as expectativaEncalhe, ");
		
		sql.append(" case  ");
		sql.append(" when tipoProduto.GRUPO_PRODUTO = :grupoCromo ");
		sql.append(" and periodoLancamentoParcial.TIPO <> :tipoParcial then ");
		sql.append(" (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" * (produtoEdicao.PRECO_VENDA - produtoEdicao.DESCONTO) ");
		sql.append(" else estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA * (produtoEdicao.EXPECTATIVA_VENDA/100) ");
		sql.append(" * (produtoEdicao.PRECO_VENDA - produtoEdicao.DESCONTO) ");
		sql.append(" end as valorTotal, ");
		
		sql.append(" case  ");
		sql.append(" when (chamadaEncalhe.ID is not null)  ");
		sql.append(" and chamadaEncalhe.TIPO_CHAMADA_ENCALHE<> :tipoChamadaEncalhe then true ");
		sql.append(" else false ");
		sql.append(" end as possuiChamada, ");
		sql.append(" produtoEdicao.ID as idProdutoEdicao, ");
		sql.append(" produtoEdicao.DESCONTO as desconto, ");
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
		sql.append(" BOX box  ");
		sql.append(" on cota.BOX_ID=box.ID ");

		sql.append(" inner join ");
		sql.append(" LANCAMENTO lancamento  ");
		sql.append(" on lancamento.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append(" inner join ");
		sql.append(" PRODUTO produto  ");
		sql.append(" on produtoEdicao.PRODUTO_ID=produto.ID  ");
		sql.append(" inner join ");
		sql.append(" PRODUTO_FORNECEDOR produtoFornecedor  ");
		sql.append(" on produto.ID=produtoFornecedor.PRODUTO_ID  ");
		sql.append(" left join ");
		sql.append(" CHAMADA_ENCALHE chamadaEncalhe  ");
		sql.append(" on chamadaEncalhe.PRODUTO_EDICAO_ID=produtoEdicao.ID  ");
		sql.append(" inner join ");
		sql.append(" LANCAMENTO_PARCIAL lancamentoParcial  ");
		sql.append(" on lancamentoParcial.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		sql.append(" inner join ");
		sql.append(" PERIODO_LANCAMENTO_PARCIAL periodoLancamentoParcial  ");
		sql.append(" on periodoLancamentoParcial.LANCAMENTO_PARCIAL_ID=lancamentoParcial.ID ");
		sql.append(" inner join ");
		sql.append(" FORNECEDOR fornecedor  ");
		sql.append(" on produtoFornecedor.fornecedores_ID=fornecedor.ID, ");
		sql.append(" PESSOA pessoaFornecedor, ");
		sql.append(" EDITOR editor, ");
		sql.append(" TIPO_PRODUTO tipoProduto   ");

		sql.append(" where ");
		sql.append(" fornecedor.JURIDICA_ID=pessoaFornecedor.ID  ");
		sql.append(" and (lancamento.STATUS = :statusLancamentoExpedido or lancamento.STATUS = :statusLancamentoBalanceamentoRecolhimento) ");
		sql.append(" and produto.EDITOR_ID=editor.ID  ");
		sql.append(" and produto.TIPO_PRODUTO_ID=tipoProduto.ID  ");
		sql.append(" and ( ");
		sql.append(" lancamento.DATA_REC_DISTRIB between :periodoInicial and :periodoFinal ");
		sql.append(" )   ");

		sql.append(" and ( ");
		sql.append(" fornecedor.ID in (:idsFornecedores) ");
		sql.append(" )  ");
		sql.append(" and ( ");
		sql.append(" periodoLancamentoParcial.RECOLHIMENTO between :periodoInicial and :periodoFinal ");
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
				   + " case  "
				   + " when tipoProduto.GRUPO_PRODUTO = :grupoCromo "
				   + " and periodoLancamentoParcial.TIPO<> :tipoParcial then "
				   + " ((estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO * (produtoEdicao.EXPECTATIVA_VENDA/100))  "
				   + " else ((estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA) * (produtoEdicao.EXPECTATIVA_VENDA/100)) "
				   + " end as expectativaEncalhe ";

		String clausulaFrom = getConsultaBalanceamentoRecolhimentoAnalitico();
		
		clausulaFrom = clausulaFrom.substring(clausulaFrom.indexOf(" from "));
		
		sql += clausulaFrom;
		sql += " ) as analitica ";
		sql += " group by analitica.dataRecolhimentoDistribuidor ";
		
		return sql;
	}
	
	private Query getQueryBalanceamentoRecolhimentoComParametros(PeriodoVO periodoRecolhimento,
																	      List<Long> fornecedores,
																	      GrupoProduto grupoCromo,
																	      String sql) {
		
		Query query = getSession().createSQLQuery(sql).addScalar("nomeFornecedor")
													  .addScalar("precoVenda")
													  .addScalar("codigoProduto")
													  .addScalar("nomeProduto")
													  .addScalar("nomeEditor")
													  .addScalar("dataLancamento")
													  .addScalar("dataRecolhimentoPrevista")
													  .addScalar("dataRecolhimentoDistribuidor")
													  .addScalar("expectativaEncalhe")
													  .addScalar("valorTotal")
													  .addScalar("desconto")
													  .addScalar("parcial")
													  .addScalar("peso")
													  .addScalar("idEditor", StandardBasicTypes.LONG)
													  .addScalar("idLancamento", StandardBasicTypes.LONG)
													  .addScalar("numeroEdicao", StandardBasicTypes.LONG)
													  .addScalar("idFornecedor", StandardBasicTypes.LONG)
													  .addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
													  .addScalar("possuiBrinde", StandardBasicTypes.BOOLEAN)
													  .addScalar("possuiChamada", StandardBasicTypes.BOOLEAN);

		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoInicial", periodoRecolhimento.getDataInicial());
		query.setParameter("periodoFinal", periodoRecolhimento.getDataFinal());
		query.setParameter("grupoCromo", grupoCromo);
		query.setParameter("tipoParcial", TipoLancamentoParcial.PARCIAL);
		query.setParameter("statusLancamentoExpedido", StatusLancamento.EXPEDIDO.toString());
		query.setParameter("statusLancamentoBalanceamentoRecolhimento", StatusLancamento.BALANCEADO_RECOLHIMENTO.toString());
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
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
	public List<Lancamento> obterLancamentosPorId(Set<Long> idsLancamento) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
		   .append(" from Lancamento lancamento ")
		   .append(" where lancamento.id in (:idsLancamento) ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameterList("idsLancamento", idsLancamento);
		
		return query.list();
	}
	
	public Lancamento obterLancamentoPorDataRecolhimentoProdutoEdicao(Date dataRecolhimentoDistribuidor, Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Lancamento lancamento ");
		
		hql.append(" where lancamento.dataRecolhimentoDistribuidor = :dataRecolhimentoDistribuidor ");
		
		hql.append(" and lancamento.produtoEdicao.id = :idProdutoEdicao");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setDate("dataRecolhimentoDistribuidor", dataRecolhimentoDistribuidor);
		
		query.setLong("idProdutoEdicao", idProdutoEdicao);
		
		return (Lancamento) query.uniqueResult();
	}
	
}
