package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
					LancamentoNaoExpedidoDTO.SortColumn.getByProperty(paginacaoVO.getSortOrder()),
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

		Query query = getQueryBalanceamentoRecolhimentoAnaliticoComParametros(periodoRecolhimento, fornecedores, grupoCromo, sql);

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

		Query query = getQueryBalanceamentoRecolhimentoAnaliticoComParametros(periodoRecolhimento, fornecedores, grupoCromo, sql);

		return query.list();
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
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select ");
		hql.append(" fornecedor.ID as idFornecedor, ");
		hql.append(" pessoaFornecedor.RAZAO_SOCIAL as nomeFornecedor, ");
		hql.append(" periodoLancamentoParcial.TIPO as parcial, ");
		hql.append(" lancamento.ID as idLancamento, ");
		hql.append(" lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		hql.append(" lancamento.DATA_REC_PREVISTA as dataRecolhimentoPrevista, ");
		hql.append(" lancamento.DATA_REC_DISTRIB as dataRecolhimentoDistribuidor, ");
		hql.append(" produto.EDITOR_ID as idEditor, ");
		hql.append(" editor.NOME as nomeEditor, ");

		hql.append(" case  ");
		hql.append(" when box.POSTO_AVANCADO = 1 then ");
		hql.append(" case  ");
		hql.append(" when tipoProduto.GRUPO_PRODUTO = :grupoCromo ");
		hql.append(" and periodoLancamentoParcial.TIPO<> :tipoParcial then (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO  ");
		hql.append(" else estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA  ");
		hql.append(" end ");
		hql.append(" end as expectativaEncalheAtendida, ");

		hql.append(" case  ");
		hql.append(" when box.POSTO_AVANCADO = 0 then ");
		hql.append(" case  ");
		hql.append(" when tipoProduto.GRUPO_PRODUTO = :grupoCromo ");
		hql.append(" and periodoLancamentoParcial.TIPO<> :tipoParcial then (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO  ");
		hql.append(" else estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA  ");
		hql.append(" end ");
		hql.append(" end as expectativaEncalheSede, ");

		hql.append(" case  ");
		hql.append(" when tipoProduto.GRUPO_PRODUTO = :grupoCromo ");
		hql.append(" and periodoLancamentoParcial.TIPO<> :tipoParcial then (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)/produtoEdicao.PACOTE_PADRAO  ");
		hql.append(" else estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA  ");
		hql.append(" end as expectativaEncalhe, ");
		hql.append(" (estoqueProdutoCota.QTDE_RECEBIDA-estoqueProdutoCota.QTDE_DEVOLVIDA)*produtoEdicao.PRECO_VENDA as valorTotal, ");
		hql.append(" case  ");
		hql.append(" when (chamadaEncalhe.ID is not null)  ");
		hql.append(" and chamadaEncalhe.TIPO_CHAMADA_ENCALHE<> :tipoChamadaEncalhe then 1  ");
		hql.append(" else 0  ");
		hql.append(" end as possuiChamada, ");
		hql.append(" produtoEdicao.ID as idProdutoEdicao, ");
		hql.append(" produtoEdicao.DESCONTO as desconto, ");
		hql.append(" produtoEdicao.NUMERO_EDICAO as numeroEdicao, ");
		hql.append(" produtoEdicao.PESO as peso, ");
		hql.append(" produtoEdicao.POSSUI_BRINDE as possuiBrinde, ");
		hql.append(" produtoEdicao.PRECO_VENDA as precoVenda, ");
		hql.append(" produto.codigo as codigoProduto, ");
		hql.append(" produto.nome as nomeProduto ");
		hql.append(" from ");
		hql.append(" ESTOQUE_PRODUTO_COTA estoqueProdutoCota  ");
		hql.append(" inner join ");
		hql.append(" PRODUTO_EDICAO produtoEdicao  ");
		hql.append(" on estoqueProdutoCota.PRODUTO_EDICAO_ID = produtoEdicao.ID  ");
		
		hql.append(" inner join ");
		hql.append(" COTA cota  ");
		hql.append(" on cota.ID=estoqueProdutoCota.COTA_ID ");
		
		hql.append(" inner join ");
		hql.append(" ESTUDO_COTA estudoCota  ");
		hql.append(" on cota.ID=estudoCota.COTA_ID ");
		
		hql.append(" inner join ");
		hql.append(" BOX box  ");
		hql.append(" on cota.BOX_ID=box.ID ");
		
		hql.append(" inner join ");
		hql.append(" LANCAMENTO lancamento  ");
		hql.append(" on lancamento.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		hql.append(" inner join ");
		hql.append(" PRODUTO produto  ");
		hql.append(" on produtoEdicao.PRODUTO_ID=produto.ID  ");
		hql.append(" inner join ");
		hql.append(" PRODUTO_FORNECEDOR produtoFornecedor  ");
		hql.append(" on produto.ID=produtoFornecedor.PRODUTO_ID  ");
		hql.append(" left join ");
		hql.append(" CHAMADA_ENCALHE chamadaEncalhe  ");
		hql.append(" on chamadaEncalhe.PRODUTO_EDICAO_ID=produtoEdicao.ID  ");
		hql.append(" inner join ");
		hql.append(" LANCAMENTO_PARCIAL lancamentoParcial  ");
		hql.append(" on lancamentoParcial.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
		hql.append(" inner join ");
		hql.append(" PERIODO_LANCAMENTO_PARCIAL periodoLancamentoParcial  ");
		hql.append(" on periodoLancamentoParcial.LANCAMENTO_PARCIAL_ID=lancamentoParcial.ID ");
		hql.append(" inner join ");
		hql.append(" FORNECEDOR fornecedor  ");
		hql.append(" on produtoFornecedor.fornecedores_ID=fornecedor.ID, ");
		hql.append(" PESSOA pessoaFornecedor, ");
		hql.append(" EDITOR editor, ");
		hql.append(" TIPO_PRODUTO tipoProduto   ");

		hql.append(" where ");
		hql.append(" fornecedor.JURIDICA_ID=pessoaFornecedor.ID  ");
		hql.append(" and lancamento.STATUS = :statusLancamento ");
		hql.append(" and produto.EDITOR_ID=editor.ID  ");
		hql.append(" and produto.TIPO_PRODUTO_ID=tipoProduto.ID  ");
		hql.append(" and ( ");
		hql.append(" lancamento.DATA_REC_DISTRIB between :periodoInicial and :periodoFinal ");
		hql.append(" )   ");

		hql.append(" and ( ");
		hql.append(" fornecedor.ID in (:idsFornecedores) ");
		hql.append(" )  ");
		hql.append(" and ( ");
		hql.append(" periodoLancamentoParcial.RECOLHIMENTO between :periodoInicial and :periodoFinal ");
		hql.append(" ) ");
		
		return hql.toString();
	}
	
	private Query getQueryBalanceamentoRecolhimentoAnaliticoComParametros(PeriodoVO periodoRecolhimento,
																	      List<Long> fornecedores,
																	      GrupoProduto grupoCromo,
																	      String sql) {
		
		Query query = getSession().createSQLQuery(sql).addScalar("idFornecedor", StandardBasicTypes.LONG).addScalar("nomeFornecedor")
				  .addScalar("idEditor", StandardBasicTypes.LONG).addScalar("nomeEditor")
				  .addScalar("idLancamento", StandardBasicTypes.LONG).addScalar("dataLancamento")
				  .addScalar("dataRecolhimentoPrevista").addScalar("dataRecolhimentoDistribuidor")
				  .addScalar("expectativaEncalhe").addScalar("valorTotal")
				  .addScalar("parcial").addScalar("possuiChamada", StandardBasicTypes.BOOLEAN)
				  .addScalar("idProdutoEdicao", StandardBasicTypes.LONG).addScalar("desconto")
				  .addScalar("numeroEdicao", StandardBasicTypes.LONG).addScalar("peso")
				  .addScalar("possuiBrinde", StandardBasicTypes.BOOLEAN).addScalar("precoVenda")
				  .addScalar("codigoProduto").addScalar("nomeProduto");

		query.setParameterList("idsFornecedores", fornecedores);
		query.setParameter("periodoInicial", periodoRecolhimento.getDataInicial());
		query.setParameter("periodoFinal", periodoRecolhimento.getDataFinal());
		query.setParameter("grupoCromo", grupoCromo);
		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		query.setParameter("tipoParcial", TipoLancamentoParcial.PARCIAL);
		query.setParameter("statusLancamento", StatusLancamento.EXPEDIDO.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoRecolhimentoDTO.class));
		
		return query;
	}
		
}
