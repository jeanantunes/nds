package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCEditor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.EditorRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * @author infoA2
 */
@Repository
public class EditorRepositoryImpl extends AbstractRepositoryModel<Editor, Long> implements EditorRepository {

	public EditorRepositoryImpl() {
		super(Editor.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EditorRepository#obterEditores()
	 */
	@Override
	public List<Editor> obterEditores() {
		String hql = "from Editor ed ORDER BY ed.nome";
		Query query = getSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Editor> editores = query.list();
		return editores;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EditorRepository#obterCurvaABCEditorTotal(br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO)
	 */
	@Override
	public ResultadoCurvaABCEditor obterCurvaABCEditorTotal(FiltroCurvaABCEditorDTO filtro){
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(ResultadoCurvaABCEditor.class.getCanonicalName())
		.append(" ( (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - (("+ this.getHQLDesconto() +") * estoqueProdutoCota.produtoEdicao.precoVenda / 100))) ) ) ");

		hql.append(getWhereQueryObterCurvaABCEditor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCEditor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		return (ResultadoCurvaABCEditor) query.list().get(0);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EditorRepository#obterCurvaABCEditor(br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT new ").append(RegistroCurvaABCEditorVO.class.getCanonicalName())
		.append(" ( editor.codigo , ")
		.append("   editor.nome , ")
		.append("   (sum(movimentos.qtde)) , ")
		.append("   (sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida)), ")
		.append("   ( sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * (estoqueProdutoCota.produtoEdicao.precoVenda - (("+ this.getHQLDesconto() +") * estoqueProdutoCota.produtoEdicao.precoVenda / 100))) ) ) ");

		hql.append(getWhereQueryObterCurvaABCEditor(filtro));
		hql.append(getGroupQueryObterCurvaABCEditor(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosObterCurvaABCEditor(filtro);

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}

		return complementarCurvaABCEditor((List<RegistroCurvaABCEditorVO>) query.list(), filtro);

	}

	/**
	 * Retorna as tabelas, joins e filtros da Query de seleção do relatório de vendas
	 * @param filtro
	 * @return
	 */
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

	/**
	 * Retorna o agrupamento das pesquisas do relatório de vendas
	 * @param filtro
	 * @return
	 */
	private String getGroupQueryObterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" GROUP BY editor.codigo, ")
		   .append("   editor.nome ");

		return hql.toString();
	}
	
	private String getHQLDesconto(){
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto");
		hql.append(" from ViewDesconto view ")
		   .append(" where view.cotaId = estoqueProdutoCota.cota.id ")
		   .append(" and view.produtoEdicaoId = estoqueProdutoCota.produtoEdicao.id ")
		   .append(" and view.fornecedorId = fornecedores.id),0) ");
		
		return hql.toString();
	}

	/**
	 * Popula os parametros do relatório.
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

	/**
	 * Insere os registros de participação e participação acumulada no resultado da consulta HQL
	 * @param lista
	 * @return
	 */
	private List<RegistroCurvaABCEditorVO> complementarCurvaABCEditor(List<RegistroCurvaABCEditorVO> lista, FiltroCurvaABCEditorDTO filtro) {

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
			
			registro.setDataDe(filtro.getDataDe());
			registro.setDataAte(filtro.getDataAte());

			// Substitui o registro pelo registro atualizado (com participacao total)
			lista.set(i, registro);

		}

		return lista;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EditorRepository#obterHistoricoEditor(br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO)
	 */
	@Override
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro) {

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

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", filtro.getDataDe());
		query.setParameter("dataAte", filtro.getDataAte());

		query.setParameter("codigoEditor", Long.parseLong(filtro.getNumeroEditor()));
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);

		return complementarHistoricoEditor(query.list(), filtro);
		
	}

	/**
	 * Complementa os dados do histórico do editor
	 * @param lista
	 * @param filtro
	 * @return
	 */
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
		
		return lista;	
	}

	/**
	 * Obtém editor por código
	 * @param codigo
	 * @return Editor
	 */
	@Override
	public Editor obterPorCodigo(Long codigo) {
        String hql = " from Editor e where e.codigo = :codigo";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigo", codigo);
		
		query.setMaxResults(1);
		
		return (Editor) query.uniqueResult();
	}
	
}
