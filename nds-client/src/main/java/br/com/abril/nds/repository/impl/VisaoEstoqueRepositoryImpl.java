package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.util.Util;

@Repository
public class VisaoEstoqueRepositoryImpl extends AbstractRepository implements VisaoEstoqueRepository {
	
	@Override
	public VisaoEstoqueDTO obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {

		String coluna = this.getColunaQtde(filtro.getTipoEstoque(), false);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" SUM(IF(IFNULL(estoque_produto." + coluna + ", 0) > 0, 1, 0) ) as produtos, ")
		   .append(" IFNULL(SUM(estoque_produto." + coluna + "), 0) as exemplares, ")
		   .append(" IFNULL(SUM(produto_edicao.preco_venda * estoque_produto." + coluna + "), 0) as valor  ")
		   .append(" FROM estoque_produto ")
		   .append(" INNER JOIN produto_edicao ")
		   .append("         ON estoque_produto.produto_edicao_id = produto_edicao.id ");
		
		if(filtro.getIdFornecedor() != -1) {
		   sql.append(" INNER JOIN produto_fornecedor ")
		      .append("         ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		      .append("        AND produto_fornecedor.fornecedores_id = :idFornecedor ");
		}
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		Object[] result = (Object[]) query.uniqueResult();
		
		TipoEstoque tipoEstoque = TipoEstoque.valueOf(filtro.getTipoEstoque());
		
		VisaoEstoqueDTO dto = new VisaoEstoqueDTO();
		dto.setTipoEstoque(filtro.getTipoEstoque());
		dto.setEstoque(tipoEstoque.getDescricao());
		dto.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		dto.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		dto.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return dto;
	}
	
	
	@Override
	public VisaoEstoqueDTO obterVisaoEstoqueHistorico(FiltroConsultaVisaoEstoque filtro) {
		
		String coluna = this.getColunaQtde(filtro.getTipoEstoque(), false);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" SUM(IF(IFNULL(estoque_produto." + coluna + ", 0) > 0, 1, 0) ) as produtos, ")
		   .append(" IFNULL(SUM(estoque_produto." + coluna + "), 0) as exemplares, ")
		   .append(" IFNULL(SUM(produto_edicao.preco_venda * estoque_produto." + coluna + "), 0) as valor  ")
		   .append(" FROM historico_estoque_produto ")
		   .append(" INNER JOIN produto_edicao ")
		   .append("         ON estoque_produto.produto_edicao_id = produto_edicao.id ");
		
		if(filtro.getIdFornecedor() != -1) {
		   sql.append(" INNER JOIN produto_fornecedor ")
		      .append("         ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		      .append("        AND produto_fornecedor.fornecedores_id = :idFornecedor ");
		}
		
		sql.append(" WHERE historico_estoque_produto.data = :data ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		query.setParameter("data", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		TipoEstoque tipoEstoque = TipoEstoque.valueOf(filtro.getTipoEstoque());
		
		VisaoEstoqueDTO dto = new VisaoEstoqueDTO();
		dto.setTipoEstoque(filtro.getTipoEstoque());
		dto.setEstoque(tipoEstoque.getDescricao());
		dto.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		dto.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		dto.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return dto;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro) {
		
		String coluna = this.getColunaQtde(filtro.getTipoEstoque(), true);
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe.codigo as codigo")
           .append("       ,pe.nomeComercial as produto")
		   .append("       ,pe.numeroEdicao as edicao")
		   .append("       ,pe.precoVenda as precoCapa")
		   .append("       ,lan.dataLancamentoDistribuidor as lcto")
		   .append("       ,lan.dataRecolhimentoDistribuidor as rclto")
		   .append("       ,ep." + coluna + " as qtde")
		   .append("   FROM EstoqueProduto as ep ")
		   .append("   JOIN ep.produtoEdicao as pe ")
		   .append("   JOIN pe.lancamentos as lan ");
		
		if(filtro.getIdFornecedor() != -1) {
			hql.append("   JOIN pe.produto.fornecedores f ");
		}
		   
		hql.append("  WHERE ep." + coluna + " > 0 ");
		
		if(filtro.getIdFornecedor() != -1) {
			hql.append("    AND f.id = :idFornecedor ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if(filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VisaoEstoqueDetalheDTO.class));

		return query.list();
	}
	
	
	private String getColunaQtde(String tipoEstoque, boolean hql) {
		
		if (tipoEstoque.equals(TipoEstoque.LANCAMENTO.toString())) {
			return "qtde";
		}
		if (tipoEstoque.equals(TipoEstoque.LANCAMENTO_JURAMENTADO.toString())) {
			return hql ? "qtdeJuramentado" : "qtde_juramentado";
		}
		if (tipoEstoque.equals(TipoEstoque.SUPLEMENTAR.toString())) {
			return hql ? "qtdeSuplementar" : "qtde_suplementar";
		}
		if (tipoEstoque.equals(TipoEstoque.RECOLHIMENTO.toString())) {
			return hql ? "qtdeDevolucaoEncalhe" : "qtde_devolucao_encalhe";
		}
		if (tipoEstoque.equals(TipoEstoque.PRODUTOS_DANIFICADOS.toString())) {
			return hql ? "qtdeDanificado" : "qtde_danificado";
		}
		
		return null;
	}
}
