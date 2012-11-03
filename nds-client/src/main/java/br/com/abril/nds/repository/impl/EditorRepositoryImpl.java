package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCEditor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.util.MathUtil;

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
		String hql = "from Editor ed ORDER BY ed.pessoaJuridica.razaoSocial";
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
		
		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			query.setParameterList("edicaoProduto", (filtro.getEdicaoProduto()));
		}
		
		return (ResultadoCurvaABCEditor) query.list().get(0);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EditorRepository#obterCurvaABCEditor(br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro) {
			
		Query query = this.getSession().createSQLQuery(this.getSqlEditor(filtro).toString())
							.addScalar("codigoEditor", StandardBasicTypes.LONG)
							.addScalar("nomeEditor", StandardBasicTypes.STRING)
							.addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
							.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER)
							.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL)
							.addScalar("valorMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);
						
		HashMap<String, Object> param = getParametrosObterCurvaABCEditor(filtro);

		param.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.toString());
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			query.setParameterList("edicaoProduto", (filtro.getEdicaoProduto()));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RegistroCurvaABCEditorVO.class));
		
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
		.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao.produto.editor editor ")
		.append(" LEFT JOIN editor.pessoaJuridica pessoaEditor ");

		hql.append(" WHERE movimentos.data BETWEEN :dataDe AND :dataAte ");
		hql.append(" AND movimentos.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
		
		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append(" AND estoqueProdutoCota.produtoEdicao.produto.codigo = :codigoProduto ");
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			hql.append(" AND upper(estoqueProdutoCota.produtoEdicao.produto.nome) like upper( :nomeProduto ) ");
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedores.id = :codigoFornecedor ");
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append("AND estoqueProdutoCota.produtoEdicao.numeroEdicao in( :edicaoProduto ) ");
		}
		
		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			hql.append("AND editor.codigo = :codigoEditor ");
		}

		if (filtro.getCodigoCota() != null ) {
			hql.append("AND estoqueProdutoCota.cota.numeroCota = :codigoCota ");
		}
		
		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append("AND upper( pessoa.nome ) like upper( :nomeCota ) or upper(pessoa.razaoSocial) like upper (:nomeCota) ");
		}
		
		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			hql.append("AND endereco.cidade = :municipio ");
		}

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
		
		if (filtro.getCodigoCota() != null ) {
			param.put("codigoCota", filtro.getCodigoCota());
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			param.put("nomeCota", filtro.getNomeCota() + "%");
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			param.put("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			param.put("codigoProduto", filtro.getCodigoProduto().toString());
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			param.put("nomeProduto", filtro.getNomeProduto() +"%");
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

		BigDecimal participacaoTotal = BigDecimal.ZERO;
		BigInteger vendaTotal = BigInteger.ZERO;

		// Soma todos os valores de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {
			if (registro.getFaturamentoCapa()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamentoCapa());
			}
			vendaTotal = vendaTotal.add(registro.getVendaExemplares());
			
			registro.setPorcentagemMargemDistribuidor(MathUtil.divide(registro.getValorMargemDistribuidor(),registro.getFaturamentoCapa()));
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		BigDecimal participacaoAcumulada = BigDecimal.ZERO;
		BigDecimal porcentagemVendaRegistro = BigDecimal.ZERO;

		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {

			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamentoCapa().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			registro.setParticipacao(participacaoRegistro);

			if (vendaTotal.doubleValue() != 0) {
				porcentagemVendaRegistro = new BigDecimal(registro.getVendaExemplares().doubleValue()*100/vendaTotal.doubleValue());
			}
			
			participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
			
			registro.setPorcentagemVendaExemplares(porcentagemVendaRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);
			registro.setDataDe(filtro.getDataDe());
			registro.setDataAte(filtro.getDataAte());
		}

		return lista;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EditorRepository#obterHistoricoEditor(br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		StringBuilder hqlMargemCota = new StringBuilder();
		
		hqlMargemCota.append(" ")
		.append(" ((sum (( estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida )) * produtoEdicao.precoVenda)")
		.append(" * ( ").append(getHQLDesconto()).append(" / 100))");

		String hqlFaturamento = " ( sum ( (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida )) * produtoEdicao.precoVenda )";
		
		hql.append("SELECT ")
			
			.append("   pessoaJuridica.razaoSocial as nomeEditor , ")
			
			.append("   produto.codigo as codigoProduto , ")
			
			.append("   produto.nome as nomeProduto , ")
			
			.append("   produtoEdicao.numeroEdicao as edicaoProduto , ")
			
			.append("   sum(movimentos.qtde) as reparte , ")
			
			.append("   sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) as vendaExemplares ,")
			
			.append(hqlFaturamento).append(" as faturamento ,")
					
			.append(hqlMargemCota).append("  as valorMargemCota, ")
			
			.append("((").append(hqlFaturamento).append(" * (fornecedores.margemDistribuidor /100))")
			.append("- (").append(hqlMargemCota).append(" )) as valorMargemDistribuidor  ");	

		hql.append(" FROM EstoqueProdutoCota  estoqueProdutoCota ")
		.append(" JOIN estoqueProdutoCota.movimentos  movimentos ")
		.append(" JOIN estoqueProdutoCota.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores  fornecedores ")
		.append(" JOIN produto.editor editor ")
		.append(" JOIN editor.pessoaJuridica pessoaJuridica ");
		

		hql.append("WHERE movimentos.data BETWEEN :dataDe AND :dataAte ");
		hql.append(" AND editor.codigo = :codigoEditor ");
		hql.append(" AND movimentos.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
		hql.append(" group by pessoaJuridica.razaoSocial, produto.codigo, produtoEdicao.numeroEdicao ");
		
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", filtro.getDataDe());
		query.setParameter("dataAte", filtro.getDataAte());

		query.setParameter("codigoEditor", Long.parseLong(filtro.getNumeroEditor()));
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RegistroHistoricoEditorVO.class));
		
		return complementarHistoricoEditor(query.list(), filtro);
		
	}
	
	private String getSqlEditor(FiltroCurvaABCEditorDTO filtro){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ")
		    
			.append(" editor.CODIGO as codigoEditor,  ")
		    
		    .append(" pessoaEditor.RAZAO_SOCIAL as nomeEditor,  ")
		    
		    .append(" sum(movimentos.QTDE) as reparte,  ")
		    
		    .append(" sum(estoqueProduto.QTDE_RECEBIDA-estoqueProduto.QTDE_DEVOLVIDA) as vendaExemplares,  ")
		    
		    .append(" sum((estoqueProduto.QTDE_RECEBIDA-estoqueProduto.QTDE_DEVOLVIDA)*(produtoEdicao.PRECO_VENDA-(  ")
		    .append("	 coalesce((select viewDesconto.DESCONTO  ")
		    .append("    from  ")
		    .append("        VIEW_DESCONTO viewDesconto  ")
		    .append("    where  ")
		    .append("        viewDesconto.COTA_ID=estoqueProduto.COTA_ID  ")
		    .append("        and viewDesconto.PRODUTO_EDICAO_ID=estoqueProduto.PRODUTO_EDICAO_ID  ")
		    .append("        and viewDesconto.FORNECEDOR_ID=fornecedor.ID),  ")
		    .append("    0)*produtoEdicao.PRECO_VENDA/100))) as faturamentoCapa , ")
		    
		    .append( this.getSqlMargemDistribuidor()).append(" as valorMargemDistribuidor ")
		    
		    .append(" from  ")
		    .append("    ESTOQUE_PRODUTO_COTA as estoqueProduto  ")
		    .append(" left outer join  ")
		    .append("    MOVIMENTO_ESTOQUE_COTA as movimentos  ")
		    .append("        on estoqueProduto.ID=movimentos.ESTOQUE_PROD_COTA_ID  ")
		    .append(" left outer join  ")
		    .append("    PRODUTO_EDICAO as produtoEdicao  ")
		    .append("        on estoqueProduto.PRODUTO_EDICAO_ID=produtoEdicao.ID  ")
		    .append(" left outer join  ")
		    .append("    PRODUTO as produto  ")
		    .append("        on produtoEdicao.PRODUTO_ID=produto.ID  ")
		    .append(" left outer join  ")
		    .append("    PRODUTO_FORNECEDOR as produtoFornecedor  ")
		    .append("        on produto.ID=produtoFornecedor.PRODUTO_ID  ")
		    .append(" left outer join  ")
		    .append("    FORNECEDOR as fornecedor  ")
		    .append("        on produtoFornecedor.fornecedores_ID=fornecedor.ID  ")
		    .append(" left outer join  ")
		    .append("    EDITOR as editor  ")
		    .append("        on produto.EDITOR_ID=editor.ID  ")
		    .append(" left outer join  ")
		    .append("    PESSOA as pessoaEditor  ")
		    .append("        on editor.JURIDICA_ID=pessoaEditor.ID  ")
		    .append(" left outer join  ")
		    .append("    COTA as cota  ")
		    .append("        on estoqueProduto.COTA_ID=cota.ID  ")
		    .append(" left outer join  ")
		    .append("    ENDERECO_COTA as enderecoCota  ")
		    .append("        on cota.ID=enderecoCota.COTA_ID  ")
		    .append(" left outer join  ")
		    .append("    ENDERECO as endereco  ")
		    .append("        on enderecoCota.ENDERECO_ID=endereco.ID  ")
		    .append(" left outer join  ")
		    .append("    PDV as pdv  ")
		    .append("        on cota.ID=pdv.COTA_ID  ")
		    .append(" left outer join  ")
		    .append("    PESSOA as pessoaCota  ")
		    .append("        on cota.PESSOA_ID=pessoaCota.ID cross  ")
		    .append(" join  ")
		    .append("    TIPO_MOVIMENTO as tipoMovimento cross  ")
		    .append(" join  ")
		    .append("    PESSOA as pessoaJuridica  ")
		    
		    .append(this.getWhereEditor(filtro))
		    
		    .append(" group by  ")
		    .append("    editor.CODIGO ,  ")
		    .append("    pessoaJuridica.RAZAO_SOCIAL  ");
			
		return sql.toString();
	}
	
	private String getWhereEditor(FiltroCurvaABCEditorDTO filtro){
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" where  ")
	    	.append("	movimentos.TIPO_MOVIMENTO_ID=tipoMovimento.ID  ")
	    	.append("   and editor.JURIDICA_ID=pessoaJuridica.ID  ")
	    	.append("   and movimentos.DATA between :dataDe and :dataAte  ")
	    	.append("   and tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE= :grupoMovimentoEstoque  ");
		
		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append(" AND produto.CODIGO = :codigoProduto ");
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			hql.append(" AND upper(produto.NOME) like upper(:nomeProduto) ");
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			hql.append("AND fornecedor.ID = :codigoFornecedor ");
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append("AND produtoEdicao.NUMERO_EDICAO in( :edicaoProduto ) ");
		}
		
		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			hql.append("AND editor.CODIGO = :codigoEditor ");
		}

		if (filtro.getCodigoCota() != null ) {
			hql.append("AND cota.NUMERO_COTA  = :codigoCota ");
		}
		
		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append("AND upper(pessoaCota.NOME) like upper(:nomeCota) or upper(pessoaCota.RAZAO_SOCIAL) like upper(:nomeCota) ");
		}
		
		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			hql.append("AND endereco.CIDADE = :municipio ");
		}

		return hql.toString();
	
	}
	
	private String getSqlMargemDistribuidor(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("(select sum(sql_margem.margem) from (")
	
		.append("	 select ")
		.append("        ((sum(estoqueProdutoMargem.QTDE_RECEBIDA-estoqueProdutoMargem.QTDE_DEVOLVIDA)* produtoEdicaoMargem.PRECO_VENDA ) ")
		.append("		   * (fornecedorMargem.MARGEM_DISTRIBUIDOR/100))")
		.append("			- (sum(estoqueProdutoMargem.QTDE_RECEBIDA-estoqueProdutoMargem.QTDE_DEVOLVIDA)*produtoEdicaoMargem.PRECO_VENDA)* ")
		.append("				(coalesce((select viewDescontoMargem.DESCONTO ")
		.append("		        from ")
		.append("		            VIEW_DESCONTO viewDescontoMargem ")
		.append("        where ")
		.append("			viewDescontoMargem.COTA_ID=estoqueProdutoMargem.COTA_ID ")
		.append("		            and viewDescontoMargem.PRODUTO_EDICAO_ID=estoqueProdutoMargem.PRODUTO_EDICAO_ID ")
		.append("		            and viewDescontoMargem.FORNECEDOR_ID=fornecedorMargem.ID), ")
		.append("		        0)/100) ")
		.append("			as margem ")
		
		.append("    from ")
		.append("        ESTOQUE_PRODUTO_COTA estoqueProdutoMargem ")
		.append("    inner join ")
		.append("        MOVIMENTO_ESTOQUE_COTA movimentosMargem ")
		.append("            on estoqueProdutoMargem.ID=movimentosMargem.ESTOQUE_PROD_COTA_ID ")
		.append("    inner join ")
		.append("        PRODUTO_EDICAO produtoEdicaoMargem ")
		.append("            on estoqueProdutoMargem.PRODUTO_EDICAO_ID=produtoEdicaoMargem.ID ")
		.append("    inner join ")
		.append("        PRODUTO produtoMargem ")
		.append("            on produtoEdicaoMargem.PRODUTO_ID=produtoMargem.ID ")
		.append("    inner join ") 
		.append("        PRODUTO_FORNECEDOR produtoFornecedorMargem ")
		.append("            on produtoMargem.ID=produtoFornecedorMargem.PRODUTO_ID ")
		.append("    inner join ")
		.append("        FORNECEDOR fornecedorMargem ")
		.append("            on produtoFornecedorMargem.fornecedores_ID=fornecedorMargem.ID ")
		.append("    inner join ")
		.append("        EDITOR editorMargem ")
		.append("            on produtoMargem.EDITOR_ID=editorMargem.ID ")
		.append("    inner join ")
		.append("        PESSOA pessoaMargem ")
		.append("            on editorMargem.JURIDICA_ID=pessoaMargem.ID cross ")
		.append("    join ")
		.append("        TIPO_MOVIMENTO tipomovimeMargem ")
		
		.append("	  where ")
		.append("			movimentosMargem.TIPO_MOVIMENTO_ID=tipomovimeMargem.ID ")
		.append("			and editorMargem.JURIDICA_ID=pessoaMargem.ID ")
		.append("			and movimentosMargem.DATA between :dataDe and :dataAte ")
		.append("			and tipomovimeMargem.GRUPO_MOVIMENTO_ESTOQUE= :grupoMovimentoEstoque ")
		
		.append("    group by ")
		.append("        pessoaMargem.RAZAO_SOCIAL , ")
		.append("        produtoMargem.CODIGO , ")
		.append("        produtoEdicaoMargem.NUMERO_EDICAO) as sql_margem )");
        		
		return sql.toString();
	}
		
	/**
	 * Complementa os dados do histórico do editor
	 * @param lista
	 * @param filtro
	 * @return
	 */
	private List<RegistroHistoricoEditorVO> complementarHistoricoEditor(List<RegistroHistoricoEditorVO> lista, FiltroPesquisarHistoricoEditorDTO filtro) {

		BigInteger vendaTotal = BigInteger.ZERO;

		// Soma todos os valores de participacao
		for (RegistroHistoricoEditorVO registro : lista) {
			vendaTotal = vendaTotal.add(registro.getVendaExemplares());
		}

		BigDecimal porcentagemVendaRegistro = BigDecimal.ZERO;
		
		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroHistoricoEditorVO registro : lista) {

			if (vendaTotal.doubleValue() != 0) {
				porcentagemVendaRegistro = new BigDecimal(registro.getVendaExemplares().doubleValue()*100/vendaTotal.doubleValue());
			}
			registro.setPorcentagemVenda(porcentagemVendaRegistro);
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
