package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
	 * @see br.com.abril.nds.repository.EditorRepository#obterHistoricoEditor(br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro) {
		
		StringBuilder hqlMargemCota = new StringBuilder();
		hqlMargemCota.append(" ((sum (( estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida )) * produtoEdicao.precoVenda)")
					 .append(" * ( movimentos.valoresAplicados.valorDesconto / 100))");
		
		StringBuilder hqlFaturamento = new StringBuilder(); 
		hqlFaturamento.append("   case when (lancamento.status in (:statusLancamentoRecolhido) ) then ( ") 
					  .append(" 		(sum ( (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida )) * produtoEdicao.precoVenda )")
					  .append("	) else 0 end ");
		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ")
			.append("   pessoaJuridica.razaoSocial as nomeEditor , ")
			.append("   produto.codigo as codigoProduto , ")
			.append("   produto.nome as nomeProduto , ")
			.append("   produtoEdicao.numeroEdicao as edicaoProduto , ")
			.append("   sum(estoqueProdutoCota.qtdeRecebida) as reparte,")
   		    .append("   case when (lancamento.status in (:statusLancamentoRecolhido) ) then ( ")
			.append("   	sum(estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) ")
			.append("	) else 0 end as vendaExemplares, ")
			.append(hqlFaturamento).append(" as faturamento ,")
			.append(hqlMargemCota).append("  as valorMargemCota, ")
			.append("((").append(hqlFaturamento)
			.append("- ").append(hqlMargemCota)
			.append(") * fornecedores.margemDistribuidor /100) as valorMargemDistribuidor ")
			.append(" FROM EstoqueProdutoCota  estoqueProdutoCota ")
			.append(" JOIN estoqueProdutoCota.movimentos  movimentos ")
			.append(" LEFT JOIN movimentos.lancamento as lancamento ")
			.append(" JOIN estoqueProdutoCota.produtoEdicao produtoEdicao")
			.append(" JOIN produtoEdicao.produto produto ")
			.append(" JOIN produto.fornecedores  fornecedores ")
			.append(" JOIN produto.editor editor ")
			.append(" JOIN editor.pessoaJuridica pessoaJuridica ")
			.append("WHERE movimentos.data BETWEEN :dataDe AND :dataAte ")
			.append(" AND editor.id = :codigoEditor ")
			.append(" AND movimentos.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ")
			.append(" group by pessoaJuridica.razaoSocial, produto.codigo, produtoEdicao.numeroEdicao ");
		
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", filtro.getDataDe());
		query.setParameter("dataAte", filtro.getDataAte());

		query.setParameter("codigoEditor", Long.parseLong(filtro.getNumeroEditor()));
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		
		query.setParameterList("statusLancamentoRecolhido", Arrays.asList(StatusLancamento.RECOLHIDO,StatusLancamento.FECHADO));
				
		query.setResultTransformer(new AliasToBeanResultTransformer(RegistroHistoricoEditorVO.class));
		
		return complementarHistoricoEditor(query.list(), filtro);
		
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Editor> obterEditoresDesc() {
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(Editor.class.getCanonicalName())
		   .append("(ed.id, p.razaoSocial) from Editor ed join ed.pessoaJuridica p ")
		   .append(" ORDER BY p.razaoSocial ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
}