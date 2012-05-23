package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.util.comparators.CurvaABCParticipacaoAcumuladaComparator;
import br.com.abril.nds.client.util.comparators.CurvaABCParticipacaoComparator;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABC;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.EditorRepository;

@Repository
public class EditorRepositoryImpl extends AbstractRepository<Editor, Long> implements EditorRepository {

	public EditorRepositoryImpl() {
		super(Editor.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Editor> obterEditores() {
		String hql = "from Editor ed ORDER BY ed.nome";
		Query query = getSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Editor> editores = query.list();
		return editores;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultadoCurvaABC obterCurvaABCEditorTotal(FiltroCurvaABCEditorDTO filtro){
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(ResultadoCurvaABC.class.getCanonicalName())
		.append(" ( (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ) ");

		hql.append(getWhereQueryObterCurvaABCEditor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCEditor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		return (ResultadoCurvaABC) query.list().get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(RegistroCurvaABCEditorVO.class.getCanonicalName())
		.append(" ( editor.codigo , ")
		.append("   editor.nome , ")
		.append("   (sum(movimentos.qtde)) , ")
		.append("   (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ) ");

		hql.append(getWhereQueryObterCurvaABCEditor(filtro));
		hql.append(getGroupQueryObterCurvaABCEditor(filtro));
		hql.append(getOrderQueryObterCurvaABCEditor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCEditor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}

		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return getOrderObterCurvaABCEditor(complementarCurvaABCEditor((List<RegistroCurvaABCEditorVO>) query.list()), filtro);

	}

	private String getWhereQueryObterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" FROM EstoqueProdutoCota AS estoqueProdutoCota ")
		.append(" LEFT JOIN estoqueProdutoCota.movimentos AS movimentos ")
		.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao.produto.fornecedores AS fornecedores ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.enderecos AS enderecos ")
		.append(" LEFT JOIN enderecos.endereco AS endereco ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.pdvs AS pdv ")
		.append(" LEFT JOIN estoqueProdutoCota.cota.pessoa AS pessoa ")
		.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao.produto.editor editor ");

		hql.append("WHERE movimentos.data BETWEEN :dataDe AND :dataAte ");
		hql.append(" AND movimentos.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append(" AND estoqueProdutoCota.produtoEdicao.produto.codigo = :codigoProduto ");
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			hql.append(" AND estoqueProdutoCota.produtoEdicao.produto.nome = :nomeProduto ");
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedores.id = :codigoFornecedor ");
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.numeroEdicao = :edicaoProduto ");
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			hql.append("AND editor.codigo = :codigoEditor ");
		}

		if (filtro.getCodigoCota() != null && !filtro.getCodigoCota().isEmpty()) {
			hql.append("AND estoqueProdutoCota.cota.numeroCota = :codigoCota ");
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append("AND pessoa.nome = :nomeCota ");
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			hql.append("AND endereco.cidade = :municipio ");
		}

		return hql.toString();

	}

	private String getGroupQueryObterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" GROUP BY editor.codigo, ")
		   .append("   editor.nome ");

		return hql.toString();
	}

	private String getOrderQueryObterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
				case CODIGO_EDITOR:
					hql.append(" order by editor.codigo ");
					break;
				case NOME_EDITOR:
					hql.append(" order by editor.nome ");
					break;
				case REPARTE:
					hql.append(" order by (sum(movimentos.qtde)) ");
					break;
				case VENDA_EXEMPLARES:
					hql.append(" order by (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) ");
					break;
				case FATURAMENTO_CAPA:
					hql.append(" order by ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - estoqueProdutoCota.produtoEdicao.desconto)) ) ");
					break;
				default:
					hql.append(" order by editor.codigo ");
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}

		return hql.toString();
	}

	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> getParametrosObterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro){

		HashMap<String,Object> param = new HashMap<String, Object>();

		param.put("dataDe",  filtro.getDataDe());
		param.put("dataAte", filtro.getDataAte());

		param.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		if (filtro.getCodigoCota() != null && !filtro.getCodigoCota().isEmpty()) {
			param.put("codigoCota", Integer.parseInt(filtro.getCodigoCota().toString()));
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			param.put("nomeCota", filtro.getNomeCota());
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			param.put("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			param.put("codigoProduto", filtro.getCodigoProduto().toString());
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			param.put("nomeProduto", filtro.getNomeProduto());
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			param.put("edicaoProduto", filtro.getEdicaoProduto());
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			param.put("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			param.put("municipio", filtro.getMunicipio());
		}

		return param;
	}

	private List<RegistroCurvaABCEditorVO> complementarCurvaABCEditor(List<RegistroCurvaABCEditorVO> lista) {

		BigDecimal participacaoTotal = new BigDecimal(0);
		BigDecimal vendaTotal = new BigDecimal(0);

		// Soma todos os valores de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {
			participacaoTotal.add(registro.getFaturamentoCapa());
			vendaTotal.add(registro.getVendaExemplares());
		}

		BigDecimal participacaoRegistro = new BigDecimal(0);
		BigDecimal participacaoAcumulada = new BigDecimal(0);
		BigDecimal porcentagemVendaRegistro = new BigDecimal(0);
		
		RegistroCurvaABCEditorVO registro = null;

		// Verifica o percentual dos valores em relação ao total de participacao
		for (int i=0; i<lista.size(); i++) {

			registro = (RegistroCurvaABCEditorVO) lista.get(i);

			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamentoCapa().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			registro.setParticipacao(participacaoRegistro);

			if (vendaTotal.doubleValue() != 0) {
				porcentagemVendaRegistro = new BigDecimal(registro.getVendaExemplares().doubleValue()*100/vendaTotal.doubleValue());
			}
			registro.setPorcentagemVendaExemplares(porcentagemVendaRegistro);
			
			participacaoAcumulada.add(participacaoRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);

			// Substitui o registro pelo registro atualizado (com participacao total)
			lista.set(i, registro);

		}

		return lista;
	}

	@SuppressWarnings("unchecked")
	private List<RegistroCurvaABCEditorVO> getOrderObterCurvaABCEditor(List<RegistroCurvaABCEditorVO> lista, FiltroCurvaABCEditorDTO filtro) {

		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case PARTICIPACAO:
					Collections.sort(lista, new CurvaABCParticipacaoComparator());
				case PARTICIPACAO_ACUMULADA:
					Collections.sort(lista, new CurvaABCParticipacaoAcumuladaComparator());
				case PORCENTAGEM_VENDA_EXEMPLARES:
					Collections.sort(lista, new Comparator() {

						@Override
						public int compare(Object o1, Object o2) {
							RegistroCurvaABCEditorVO registro1 = (RegistroCurvaABCEditorVO) o1;
							RegistroCurvaABCEditorVO registro2 = (RegistroCurvaABCEditorVO) o2;
							return registro1.getPorcentagemVendaExemplares().compareTo(registro2.getPorcentagemVendaExemplares());
						}
						
					});
				default:
					break;
			}
		}
		return lista;
	}

	@Override
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(
			FiltroPesquisarHistoricoEditorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(RegistroHistoricoEditorVO.class.getCanonicalName())
			.append(" ( editor.nome, ")
			.append("   estoqueProdutoCota.produtoEdicao.produto.codigo, ")
			.append("   estoqueProdutoCota.produtoEdicao.produto.nome, ")
			.append("   estoqueProdutoCota.produtoEdicao.numeroEdicao, ")
			.append("   (sum(movimentos.qtde)) , ")
			.append("   (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) ) ");
		
		hql.append(" FROM EstoqueProdutoCota AS estoqueProdutoCota ")
		.append(" LEFT JOIN estoqueProdutoCota.movimentos AS movimentos ")
		.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao.produto.editor editor ");

		hql.append("WHERE movimentos.data BETWEEN :dataDe AND :dataAte ");
		hql.append(" AND editor.codigo = :codigoEditor ");
		hql.append(" AND movimentos.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");

		if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
				case CODIGO_PRODUTO:
					hql.append(" order by estoqueProdutoCota.produtoEdicao.produto.codigo ");
					break;
				case NOME_PRODUTO:
					hql.append(" order by estoqueProdutoCota.produtoEdicao.produto.nome ");
					break;
				case EDICAO_PRODUTO:
					hql.append(" order by estoqueProdutoCota.produtoEdicao.numeroEdicao ");
					break;
				case REPARTE:
					hql.append(" order by (sum(movimentos.qtde)) ");
					break;
				case VENDA_EXEMPLARES:
					hql.append(" order by (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)) ");
					break;
				default:
					hql.append(" order by estoqueProdutoCota.produtoEdicao.produto.codigo ");
					break;
			}
		}

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", filtro.getDataDe());
		query.setParameter("dataAte", filtro.getDataAte());
		query.setParameter("codigoEditor", Long.parseLong(filtro.getNumeroEditor()));
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);

		return complementarHistoricoEditor(query.list(), filtro);
		
	}

	private List<RegistroHistoricoEditorVO> complementarHistoricoEditor(List<RegistroHistoricoEditorVO> lista, FiltroPesquisarHistoricoEditorDTO filtro) {

		BigDecimal vendaTotal = new BigDecimal(0);

		// Soma todos os valores de participacao
		for (RegistroHistoricoEditorVO registro : lista) {
			vendaTotal.add(registro.getVendaExemplares());
		}

		BigDecimal porcentagemVendaRegistro = new BigDecimal(0);
		
		RegistroHistoricoEditorVO registro = null;

		// Verifica o percentual dos valores em relação ao total de participacao
		for (int i=0; i<lista.size(); i++) {

			registro = (RegistroHistoricoEditorVO) lista.get(i);

			if (vendaTotal.doubleValue() != 0) {
				porcentagemVendaRegistro = new BigDecimal(registro.getVendaExemplares().doubleValue()*100/vendaTotal.doubleValue());
			}
			registro.setPorcentagemVenda(porcentagemVendaRegistro);
			
			// Substitui o registro pelo registro atualizado (com participacao total)
			lista.set(i, registro);

		}		
		
		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case PORCENTAGEM_VENDA_EXEMPLARES:
					Collections.sort(lista, new Comparator() {

						@Override
						public int compare(Object o1, Object o2) {
							RegistroHistoricoEditorVO registro1 = (RegistroHistoricoEditorVO) o1;
							RegistroHistoricoEditorVO registro2 = (RegistroHistoricoEditorVO) o2;
							return registro1.getPorcentagemVenda().compareTo(registro2.getPorcentagemVenda());
						}
						
					});
				default:
					break;
			}
		}
		return lista;	
	}
	
}
