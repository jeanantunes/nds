package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.repository.RelatorioTiposProdutosRepository;

@Repository
public class RelatorioTiposProdutosRepositoryImpl extends AbstractRepository implements RelatorioTiposProdutosRepository {

	@Override
	public List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro) {
		
		boolean hasTipoProduto = filtro.getTipoProduto() != null && filtro.getTipoProduto().longValue() != -1L;
		boolean hasLancamentoDe = filtro.getDataLancamentoDe() != null;
		boolean hasLancamentoAte = filtro.getDataLancamentoAte() != null;
		boolean hasRecolhimentoDe = filtro.getDataRecolhimentoDe() != null;
		boolean hasRecolhimentoAte = filtro.getDataRecolhimentoAte() != null;
		
		boolean hasFilter = hasTipoProduto || hasLancamentoDe || hasLancamentoAte || hasRecolhimentoDe || hasRecolhimentoAte;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT p.id AS codigo, " +
				"p.descricao AS produto, " +
				"pe.numeroEdicao AS edicao, " +
				"pe.precoVenda AS precoCapa, " +
				"( " +
				" (" +
				"	(select sum(mec.qtde) from MovimentoEstoqueCota mec where mec.tipoMovimento.operacaoEstoque = 'SAIDA' and mec.lancamento.id = l.id)" +
				" - " +
				"   (select sum(mec.qtde) from MovimentoEstoqueCota mec where mec.tipoMovimento.operacaoEstoque = 'ENTRADA' and mec.lancamento.id = l.id)" +
				" )" +
				" * pe.precoVenda" +
				") AS faturamento, " +
				"t.descricao AS tipoProduto, " +
				"l.dataRecolhimentoDistribuidor AS recolhimento, " +
				"l.dataLancamentoDistribuidor AS lancamento " +
				"FROM ProdutoEdicao pe " +
				"JOIN pe.produto p " +
				"JOIN p.tipoProduto t " +
				"JOIN pe.lancamentos l");
		
		if(hasFilter) {
			sb.append(" where true = true");
			
			if(hasTipoProduto) {
				sb.append(" and t.id = :idTipoProduto");
			}
			if(hasLancamentoDe) {
				sb.append(" and l.dataLancamentoDistribuidor >= :dataLancamentoDe");
			}
			if(hasLancamentoAte) {
				sb.append(" and l.dataLancamentoDistribuidor <= :dataLancamentoAte");
			}
			if(hasRecolhimentoDe) {
				sb.append(" and l.dataRecolhimentoDistribuidor >= :dataRecolhimentoDe");
			}
			if(hasRecolhimentoAte) {
				sb.append(" and l.dataRecolhimentoDistribuidor <= :dataRecolhimentoAte");
			}
		}
		
		sb.append(" order by " + filtro.getPaginacaoVO().getSortColumn() + " " + filtro.getPaginacaoVO().getSortOrder());
		
		
		Query query = getSession().createQuery(sb.toString());
		
		if(hasFilter) {
			sb.append(" where true = true");
			
			if(hasTipoProduto) {
				query.setParameter("idTipoProduto", filtro.getTipoProduto());
			}
			if(hasLancamentoDe) {
				query.setParameter("dataLancamentoDe", filtro.getDataLancamentoDe());
			}
			if(hasLancamentoAte) {
				query.setParameter("dataLancamentoAte", filtro.getDataLancamentoAte());
			}
			if(hasRecolhimentoDe) {
				query.setParameter("dataRecolhimentoDe", filtro.getDataRecolhimentoDe());
			}
			if(hasRecolhimentoAte) {
				query.setParameter("dataRecolhimentoAte", filtro.getDataRecolhimentoAte());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioTiposProdutosDTO.class));

		int total = query.list().size();
		filtro.getPaginacaoVO().setQtdResultadosTotal(total);
		
		Integer paginaAtual = filtro.getPaginacaoVO().getPaginaAtual();
		Integer qtdResultadosPorPagina = filtro.getPaginacaoVO().getQtdResultadosPorPagina();
		
		query.setFirstResult((paginaAtual - 1) * qtdResultadosPorPagina);
		query.setMaxResults(qtdResultadosPorPagina);
		
		
		List result = query.list();
		
		return result;
	}
}
