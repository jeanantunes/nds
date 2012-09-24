package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;

@Repository
public class VisaoEstoqueRepositoryImpl extends AbstractRepository implements VisaoEstoqueRepository{
	
	public static final String LANCAMENTO = "Lançamento";
	public static final String LANCAMENTO_JURAMENTADO = "Lançamento Juramentado";
	public static final String SUPLEMENTAR = "Suplementar";
	public static final String RECOLHIMENTO = "Recolhimento";
	public static final String PRODUTOS_DANIFICADOS = "Produtos Danificados";
	
	@Override
	public VisaoEstoqueDTO obterLancamento(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO lancamento = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ")
		   .append(" SUM(IF(IFNULL(estoque_produto.qtde, 0) > 0, 1, 0) ) as produtos, ")
		   .append(" SUM(estoque_produto.qtde) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ");
		   if(filtro.getIdFornecedor() != -1){
			   sql.append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
			      .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		   }else{
			   sql.append(" WHERE movimento_estoque.data_criacao = :dataMovimentacao ");
		   }
	
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1)
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		lancamento.setEstoque(LANCAMENTO);
		lancamento.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		lancamento.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		lancamento.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return lancamento;
	}

	@Override
	public VisaoEstoqueDTO obterLancamentoJuramentado(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO lancamentoJuramentado = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" SUM(IF(IFNULL(estoque_produto.qtde_juramentado, 0) > 0, 1, 0) ) as produtos, ")
		   .append(" SUM(estoque_produto.qtde_juramentado) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_juramentado) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ");
		   if(filtro.getIdFornecedor() != -1){
			   sql.append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
			      .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		   }else{
			   sql.append(" WHERE movimento_estoque.data_criacao = :dataMovimentacao ");
		   }
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1)
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		lancamentoJuramentado.setEstoque(LANCAMENTO_JURAMENTADO);
		lancamentoJuramentado.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		lancamentoJuramentado.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		lancamentoJuramentado.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));

		return lancamentoJuramentado;
	}

	@Override
	public VisaoEstoqueDTO obterSuplementar(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO suplementar = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" SUM(IF(IFNULL(estoque_produto.qtde_suplementar, 0) > 0, 1, 0) ) as produtos, ")
		   .append(" SUM(estoque_produto.qtde_suplementar) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_suplementar) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ");
		   if(filtro.getIdFornecedor() != -1){
			   sql.append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
			      .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		   }else{
			   sql.append(" WHERE movimento_estoque.data_criacao = :dataMovimentacao ");
		   }
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1)
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		suplementar.setEstoque(SUPLEMENTAR);
		suplementar.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		suplementar.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		suplementar.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return suplementar;
	}

	@Override
	public VisaoEstoqueDTO obterRecolhimento(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO recolhimento = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" SUM(IF(IFNULL(estoque_produto.qtde_devolucao_encalhe, 0) > 0, 1, 0) ) as Produtos, ")
		   .append(" SUM(estoque_produto.qtde_devolucao_encalhe) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_devolucao_encalhe) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ");
		   if(filtro.getIdFornecedor() != -1){
			   sql.append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
			      .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		   }else{
			   sql.append(" WHERE movimento_estoque.data_criacao = :dataMovimentacao ");
		   }
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1)
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		recolhimento.setEstoque(RECOLHIMENTO);		
		recolhimento.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		recolhimento.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		recolhimento.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));

		return recolhimento;
	}

	@Override
	public VisaoEstoqueDTO obterProdutosDanificados(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO produtosDanificados = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" SUM(IF(IFNULL(estoque_produto.qtde_danificado, 0) > 0, 1, 0) ) as produtos, ")
		   .append(" SUM(estoque_produto.qtde_danificado) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_danificado) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ");
		   if(filtro.getIdFornecedor() != -1){
			   sql.append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
			      .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		   }else{
			   sql.append(" WHERE movimento_estoque.data_criacao = :dataMovimentacao ");
		   }
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdFornecedor() != -1)
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		produtosDanificados.setEstoque(PRODUTOS_DANIFICADOS);		
		produtosDanificados.setProdutos(Util.nvl((BigDecimal)result[0], BigDecimal.ZERO));
		produtosDanificados.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		produtosDanificados.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		
		return produtosDanificados;
	}

	
	//--------------------DETALHES-----------------//
	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheDTO> obterLancamentoDetalhe(FiltroConsultaVisaoEstoque filtro) {
		
		List<VisaoEstoqueDetalheDTO> lancamento = new ArrayList<VisaoEstoqueDetalheDTO>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ")
		   .append(" produto_edicao.codigo, ")
		   .append(" produto_edicao.nome_comercial, ")
		   .append(" produto_edicao.numero_edicao, ")
		   .append(" produto_edicao.preco_venda, ")
		   .append(" lancamento.data_lcto_distribuidor, ")
		   .append(" lancamento.data_rec_distrib, ")
		   .append(" estoque_produto.qtde,  ")
		   .append(" (produto_edicao.preco_venda * estoque_produto.qtde) ")
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
		
		List<Object[]> list = (List<Object[]>) query.list();
		
		for (int i = 0; i < list.size(); i++) {
			int j = 0;
			lancamento.add(new VisaoEstoqueDetalheDTO());
			lancamento.get(i).setCodigo((String) list.get(i)[j++]);
			lancamento.get(i).setProduto(Util.nvl((String)list.get(i)[j++], String.valueOf(BigDecimal.ZERO)));
			lancamento.get(i).setEdicao(Util.nvl((BigInteger)list.get(i)[j++], BigInteger.ZERO));
			lancamento.get(i).setPrecoCapa(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			lancamento.get(i).setLcto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			lancamento.get(i).setRclto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			lancamento.get(i).setQtde(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			lancamento.get(i).setValor(CurrencyUtil.formatarValor(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO)));	
		}
		
		
		return lancamento;
	}

	@Override
	public List<VisaoEstoqueDetalheJuramentadoDTO> obterLancamentoJuramentadoDetalhe(FiltroConsultaVisaoEstoque filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheDTO> obterSuplementarDetalhe(FiltroConsultaVisaoEstoque filtro) {
		List<VisaoEstoqueDetalheDTO> suplementar = new ArrayList<VisaoEstoqueDetalheDTO>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ")
		   .append(" produto_edicao.codigo, ")
		   .append(" produto_edicao.nome_comercial, ")
		   .append(" produto_edicao.numero_edicao, ")
		   .append(" produto_edicao.preco_venda, ")
		   .append(" lancamento.data_lcto_distribuidor, ")
		   .append(" lancamento.data_rec_distrib, ")
		   .append(" estoque_produto.qtde_suplementar,  ")
		   .append(" (produto_edicao.preco_venda * estoque_produto.qtde_suplementar) ")
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
		
		List<Object[]> list = (List<Object[]>) query.list();
		
		for (int i = 0; i < list.size(); i++) {
			int j = 0;
			suplementar.add(new VisaoEstoqueDetalheDTO());
			suplementar.get(i).setCodigo((String) list.get(i)[j++]);
			suplementar.get(i).setProduto(Util.nvl((String)list.get(i)[j++], String.valueOf(BigDecimal.ZERO)));
			suplementar.get(i).setEdicao(Util.nvl((BigInteger)list.get(i)[j++], BigInteger.ZERO));
			suplementar.get(i).setPrecoCapa(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			suplementar.get(i).setLcto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			suplementar.get(i).setRclto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			suplementar.get(i).setQtde(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			suplementar.get(i).setValor(CurrencyUtil.formatarValor(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO)));	
		}
		
		
		return suplementar;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheDTO> obterRecolhimentoDetalhe(FiltroConsultaVisaoEstoque filtro) {
List<VisaoEstoqueDetalheDTO> recolhimento = new ArrayList<VisaoEstoqueDetalheDTO>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ")
		   .append(" produto_edicao.codigo, ")
		   .append(" produto_edicao.nome_comercial, ")
		   .append(" produto_edicao.numero_edicao, ")
		   .append(" produto_edicao.preco_venda, ")
		   .append(" lancamento.data_lcto_distribuidor, ")
		   .append(" lancamento.data_rec_distrib, ")
		   .append(" estoque_produto.qtde_devolucao_encalhe,  ")
		   .append(" (produto_edicao.preco_venda * estoque_produto.qtde_devolucao_encalhe) ")
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
		
		List<Object[]> list = (List<Object[]>) query.list();
		
		for (int i = 0; i < list.size(); i++) {
			int j = 0;
			recolhimento.add(new VisaoEstoqueDetalheDTO());
			recolhimento.get(i).setCodigo((String) list.get(i)[j++]);
			recolhimento.get(i).setProduto(Util.nvl((String)list.get(i)[j++], String.valueOf(BigDecimal.ZERO)));
			recolhimento.get(i).setEdicao(Util.nvl((BigInteger)list.get(i)[j++], BigInteger.ZERO));
			recolhimento.get(i).setPrecoCapa(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			recolhimento.get(i).setLcto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			recolhimento.get(i).setRclto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			recolhimento.get(i).setQtde(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			recolhimento.get(i).setValor(CurrencyUtil.formatarValor(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO)));	
		}
		
		
		return recolhimento;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheDTO> obterProdutosDanificadosDetalhe(FiltroConsultaVisaoEstoque filtro) {
List<VisaoEstoqueDetalheDTO> produtosDanificados = new ArrayList<VisaoEstoqueDetalheDTO>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ")
		   .append(" produto_edicao.codigo, ")
		   .append(" produto_edicao.nome_comercial, ")
		   .append(" produto_edicao.numero_edicao, ")
		   .append(" produto_edicao.preco_venda, ")
		   .append(" lancamento.data_lcto_distribuidor, ")
		   .append(" lancamento.data_rec_distrib, ")
		   .append(" estoque_produto.qtde_danificado,  ")
		   .append(" (produto_edicao.preco_venda * estoque_produto.qtde_danificado) ")
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
		
		List<Object[]> list = (List<Object[]>) query.list();
		
		for (int i = 0; i < list.size(); i++) {
			int j = 0;
			produtosDanificados.add(new VisaoEstoqueDetalheDTO());
			produtosDanificados.get(i).setCodigo((String) list.get(i)[j++]);
			produtosDanificados.get(i).setProduto(Util.nvl((String)list.get(i)[j++], String.valueOf(BigDecimal.ZERO)));
			produtosDanificados.get(i).setEdicao(Util.nvl((BigInteger)list.get(i)[j++], BigInteger.ZERO));
			produtosDanificados.get(i).setPrecoCapa(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			produtosDanificados.get(i).setLcto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			produtosDanificados.get(i).setRclto(DateUtil.formatarDataPTBR((Date)list.get(i)[j++]));
			produtosDanificados.get(i).setQtde(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO));
			produtosDanificados.get(i).setValor(CurrencyUtil.formatarValor(Util.nvl((BigDecimal)list.get(i)[j++], BigDecimal.ZERO)));	
		}
		
		
		return produtosDanificados;
	}
	
}
