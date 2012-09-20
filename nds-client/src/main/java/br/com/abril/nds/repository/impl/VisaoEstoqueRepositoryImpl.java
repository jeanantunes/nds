package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;

@Repository
public class VisaoEstoqueRepositoryImpl extends AbstractRepository implements VisaoEstoqueRepository {
	
	@Override
	public VisaoEstoqueDTO obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {

		String coluna = this.getColuna(filtro.getTipoEstoque());
		
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
		
		VisaoEstoqueDTO dto = new VisaoEstoqueDTO();
		dto.setEstoque(filtro.getTipoEstoque());
		dto.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		dto.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		dto.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return dto;
	}
	
	
	@Override
	public VisaoEstoqueDTO obterVisaoEstoqueHistorico(FiltroConsultaVisaoEstoque filtro) {
		
		String coluna = this.getColuna(filtro.getTipoEstoque());
		
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
		
		VisaoEstoqueDTO dto = new VisaoEstoqueDTO();
		dto.setEstoque(filtro.getTipoEstoque());
		dto.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		dto.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		dto.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return dto;
	}
	
	
	private String getColuna(String tipoEstoque) {
	
		if (tipoEstoque.equals(TipoEstoque.LANCAMENTO.getDescricao())) {
			return "qtde";
		}
		if (tipoEstoque.equals(TipoEstoque.LANCAMENTO_JURAMENTADO.getDescricao())) {
			return "qtde_juramentado";
		}
		if (tipoEstoque.equals(TipoEstoque.SUPLEMENTAR.getDescricao())) {
			return "qtde_suplementar";
		}
		if (tipoEstoque.equals(TipoEstoque.RECOLHIMENTO.getDescricao())) {
			return "qtde_devolucao_encalhe";
		}
		if (tipoEstoque.equals(TipoEstoque.PRODUTOS_DANIFICADOS.getDescricao())) {
			return "qtde_danificado";
		}
		
		return null;
	}
	
	
	
	
	public void obterDetalhe(FiltroConsultaVisaoEstoque filtro) {
		
		
	}
	
	
	
	
	
	
	//--------------------DETALHES-----------------//
	@Override
	public VisaoEstoqueDetalheDTO obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDetalheDTO lancamento = new VisaoEstoqueDetalheDTO();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ")
		   .append(" produto_edicao.codigo, ")
		   .append(" produto_edicao.nome_comercial, ")
		   .append(" produto_edicao.numero_edicao, ")
		   .append(" produto_edicao.preco_venda, ")
		   .append(" lancamento.data_lcto_distribuidor, ")
		   .append(" lancamento.data_rec_distrib, ")
		   .append(" estoque_produto.qtde,  ")
		   .append(" produto_edicao.preco_venda * estoque_produto.qtde ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" INNER JOIN estoque_produto ")
		   .append(" INNER JOIN lancamento ")
		   .append(" WHERE produto_fornecedor.produto_id = produto_edicao.id ")
		   .append(" AND produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" AND lancamento.produto_edicao_id = estoque_produto.produto_edicao_id ");
		   if(filtro.getIdFornecedor() != -1)
			   sql.append(" AND produto_fornecedor.fornecedores_id = :idFornecedor ");
		  
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1)
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		
		//query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		lancamento.setCodigo((String) result[0]);
		lancamento.setProduto(Util.nvl((String)result[1], String.valueOf(BigDecimal.ZERO)));
		lancamento.setEdicao(Util.nvl((BigInteger)result[2], BigInteger.ZERO));
		lancamento.setPrecoCapa(Util.nvl((BigDecimal)result[3], BigDecimal.ZERO));
		lancamento.setLcto(DateUtil.formatarDataPTBR((Date)result[4]));
		lancamento.setRclto(DateUtil.formatarDataPTBR((Date)result[5]));
		lancamento.setQtde(Util.nvl((BigDecimal)result[6], BigDecimal.ZERO));
		lancamento.setValor(Util.nvl((BigDecimal)result[7], BigDecimal.ZERO));
		
		return lancamento;
	}

}
