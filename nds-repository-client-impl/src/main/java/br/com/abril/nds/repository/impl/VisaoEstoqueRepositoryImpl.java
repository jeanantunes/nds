package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.util.StringUtil;

@Repository
public class VisaoEstoqueRepositoryImpl extends AbstractRepository implements
		VisaoEstoqueRepository {

	@Override
	public VisaoEstoqueDTO obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {

		String coluna = this.getColunaQtde(filtro.getTipoEstoque());

		StringBuilder hql = new StringBuilder();

		hql.append(
				" SELECT COALESCE(SUM(CASE WHEN (COALESCE(ep." + coluna
						+ ", 0) > 0) THEN 1 ELSE 0 END), 0) as produtos, ")
				.append("        COALESCE(SUM(ep." + coluna
						+ "), 0) as exemplares, ")
				.append("        COALESCE(SUM(pe.precoVenda * ep." + coluna
						+ "), 0) as valor  ")
				.append("   FROM EstoqueProduto as ep ")
				.append("   JOIN ep.produtoEdicao as pe ");
		if (filtro.getIdFornecedor() != -1) {
			hql.append("   JOIN pe.produto.fornecedores f ");
			hql.append("  WHERE f.id = :idFornecedor ");
		}

		Query query = getSession().createQuery(hql.toString());

		if (filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				VisaoEstoqueDTO.class));

		VisaoEstoqueDTO dto = (VisaoEstoqueDTO) query.uniqueResult();
		dto.setTipoEstoque(filtro.getTipoEstoque());
		dto.setEstoque(TipoEstoque.valueOf(filtro.getTipoEstoque())
				.getDescricao());

		return dto;
	}

	@Override
	public VisaoEstoqueDTO obterVisaoEstoqueHistorico(
			FiltroConsultaVisaoEstoque filtro) {

		String coluna = this.getColunaQtde(filtro.getTipoEstoque());

		StringBuilder hql = new StringBuilder();

		hql.append(
				" SELECT COALESCE(SUM(CASE WHEN (COALESCE(ep." + coluna
						+ ", 0) > 0) THEN 1 ELSE 0 END), 0) as produtos, ")
				.append("        COALESCE(SUM(ep." + coluna
						+ "), 0) as exemplares, ")
				.append("        COALESCE(SUM(pe.precoVenda * ep." + coluna
						+ "), 0) as valor  ")
				.append("   FROM HistoricoEstoqueProduto as ep ")
				.append("   JOIN ep.produtoEdicao as pe ");
		if (filtro.getIdFornecedor() != -1) {
			hql.append("   JOIN pe.produto.fornecedores f ");
		}
		hql.append("  WHERE ep.data =:data ");
		if (filtro.getIdFornecedor() != -1) {
			hql.append("    AND f.id = :idFornecedor ");
		}

		Query query = getSession().createQuery(hql.toString());

		query.setDate("data", filtro.getDataMovimentacao());
		if (filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				VisaoEstoqueDTO.class));

		VisaoEstoqueDTO dto = (VisaoEstoqueDTO) query.uniqueResult();
		dto.setTipoEstoque(filtro.getTipoEstoque());
		dto.setEstoque(TipoEstoque.valueOf(filtro.getTipoEstoque())
				.getDescricao());

		return dto;
	}

	@Override
	public VisaoEstoqueDTO obterVisaoEstoqueJuramentado(
			FiltroConsultaVisaoEstoque filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(
				" SELECT COALESCE(SUM(CASE WHEN (COALESCE(ep.qtde, 0) > 0) THEN 1 ELSE 0 END), 0) as produtos, ")
				.append("        COALESCE(SUM(ep.qtde), 0) as exemplares, ")
				.append("        COALESCE(SUM(pe.precoVenda * ep.qtde), 0) as valor  ")
				.append("   FROM EstoqueProdutoCotaJuramentado as ep ")
				.append("   JOIN ep.produtoEdicao as pe ");
		if (filtro.getIdFornecedor() != null
				&& !filtro.getIdFornecedor().equals(-1L)) {
			hql.append("   JOIN pe.produto.fornecedores f ");
		}
		hql.append("  WHERE ep.data = :data ");
		if (filtro.getIdFornecedor() != null
				&& !filtro.getIdFornecedor().equals(-1L)) {
			hql.append("    AND f.id = :idFornecedor ");
		}

		Query query = this.getSession().createQuery(hql.toString());

		query.setDate("data", filtro.getDataMovimentacao());
		if (filtro.getIdFornecedor() != null
				&& !filtro.getIdFornecedor().equals(-1L)) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				VisaoEstoqueDTO.class));

		VisaoEstoqueDTO dto = (VisaoEstoqueDTO) query.uniqueResult();
		dto.setTipoEstoque(filtro.getTipoEstoque());
		dto.setEstoque(TipoEstoque.valueOf(filtro.getTipoEstoque())
				.getDescricao());

		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(
			FiltroConsultaVisaoEstoque filtro) {

		String coluna = this.getColunaQtde(filtro.getTipoEstoque());

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe.id as produtoEdicaoId")
				.append("       ,pe.produto.codigo as codigo")

				.append("       , case when pe.nomeComercial is null or pe.nomeComercial = ''  ")

				.append("       then pe.produto.nome else pe.nomeComercial end as produto ")

				.append("       ,pe.numeroEdicao as edicao")
				.append("       ,pe.precoVenda as precoCapa")
				.append("       ,lan.dataLancamentoDistribuidor as lcto")
				.append("       ,lan.dataRecolhimentoDistribuidor as rclto")
				.append("		,(pe.precoVenda * ep." + coluna + ") as valor ")
				.append("       ,ep." + coluna + " as qtde")
				.append("   FROM EstoqueProduto as ep ")
				.append("   JOIN ep.produtoEdicao as pe ")
				.append("   JOIN pe.lancamentos as lan ");

		if (filtro.getIdFornecedor() != -1) {
			hql.append("   JOIN pe.produto.fornecedores f ");
		}

		hql.append("  WHERE ep." + coluna + " > 0 ");

		if (filtro.getIdFornecedor() != -1) {
			hql.append("    AND f.id = :idFornecedor ");
		}
		
		hql.append(" group by pe.id ");
		
		if (filtro.getPaginacao() != null && !StringUtil.isEmpty(filtro.getPaginacao().getSortColumn())) {
			hql.append("order by ")
					.append(filtro.getPaginacao().getSortColumn()).append(" ")
					.append(filtro.getPaginacao().getSortOrder());
		}

		Query query = this.getSession().createQuery(hql.toString());

		if (filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				VisaoEstoqueDetalheDTO.class));

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalheHistorico(
			FiltroConsultaVisaoEstoque filtro) {

		String coluna = this.getColunaQtde(filtro.getTipoEstoque());

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pe.id as produtoEdicaoId")
				.append("       ,pr.codigo as codigo")
				.append("       ,pe.nomeComercial as produto")
				.append("       ,pe.numeroEdicao as edicao")
				.append("       ,pe.precoVenda as precoCapa")
				.append("       ,lan.dataLancamentoDistribuidor as lcto")
				.append("       ,lan.dataRecolhimentoDistribuidor as rclto")
				.append("       ,ep." + coluna + " as qtde")
				.append("   FROM HistoricoEstoqueProduto as ep ")
				.append("   JOIN ep.produtoEdicao as pe ")
				.append("   JOIN pe.produto as pr ")
				.append("   JOIN pe.lancamentos as lan ");

		if (filtro.getIdFornecedor() != -1) {
			hql.append("   JOIN pr.fornecedores f ");
		}

		hql.append("  WHERE ep." + coluna + " > 0 ");
		hql.append("    AND ep.data = :data ");

		if (filtro.getIdFornecedor() != -1) {
			hql.append("    AND f.id = :idFornecedor ");
		}
		
		if (!StringUtil.isEmpty(filtro.getPaginacao().getSortColumn())) {
			hql.append("order by ")
					.append(filtro.getPaginacao().getSortColumn()).append(" ")
					.append(filtro.getPaginacao().getSortOrder());
		}

		Query query = this.getSession().createQuery(hql.toString());

		query.setDate("data", filtro.getDataMovimentacao());
		if (filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				VisaoEstoqueDetalheDTO.class));

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisaoEstoqueDetalheJuramentadoDTO> obterVisaoEstoqueDetalheJuramentado(
			FiltroConsultaVisaoEstoque filtro) {

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT co.numeroCota as cota")
				.append("       ,pess.nome as nome")
				.append("       ,pe.id as produtoEdicaoId")
				.append("       ,pr.codigo as codigo")
				.append("       ,pe.nomeComercial as produto")
				.append("       ,pe.numeroEdicao as edicao")
				.append("       ,pe.precoVenda as precoCapa")
				.append("       ,lan.dataLancamentoDistribuidor as lcto")
				.append("       ,lan.dataRecolhimentoDistribuidor as rclto")
				.append("       ,ep.qtde as qtde")
				.append("   FROM EstoqueProdutoCotaJuramentado as ep ")
				.append("   JOIN ep.cota as co ")
				.append("   JOIN co.pessoa as pess ")
				.append("   JOIN ep.produtoEdicao as pe ")
				.append("   JOIN pe.produto as pr ")
				.append("   JOIN pe.lancamentos as lan ");
		if (filtro.getIdFornecedor() != -1) {
			hql.append("   JOIN pr.fornecedores f ");
		}
		hql.append("  WHERE ep.qtde > 0 ");
		hql.append("    AND ep.data = :data ");
		if (filtro.getIdFornecedor() != -1) {
			hql.append("    AND f.id = :idFornecedor ");
		}

		if (!StringUtil.isEmpty(filtro.getPaginacao().getSortColumn())) {
			hql.append("order by ")
					.append(filtro.getPaginacao().getSortColumn()).append(" ")
					.append(filtro.getPaginacao().getSortOrder());
		}
		Query query = this.getSession().createQuery(hql.toString());

		query.setDate("data", filtro.getDataMovimentacao());
		if (filtro.getIdFornecedor() != -1) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(
				VisaoEstoqueDetalheJuramentadoDTO.class));

		return query.list();
	}

	private String getColunaQtde(String tipoEstoque) {

		if (tipoEstoque.equals(TipoEstoque.LANCAMENTO.toString())) {
			return "qtde";
		}
		if (tipoEstoque.equals(TipoEstoque.SUPLEMENTAR.toString())) {
			return "qtdeSuplementar";
		}
		if (tipoEstoque.equals(TipoEstoque.RECOLHIMENTO.toString())) {
			return "qtdeDevolucaoEncalhe";
		}
		if (tipoEstoque.equals(TipoEstoque.PRODUTOS_DANIFICADOS.toString())) {
			return "qtdeDanificado";
		}

		return null;
	}

	@Override
	public BigInteger obterQuantidadeEstoque(long idProdutoEdicao,
			String tipoEstoque) {
		String coluna = this.getColunaQtde(tipoEstoque);

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT DISTINCT ep." + coluna + " as qtde")
				.append("   FROM EstoqueProduto as ep ")
				.append("   JOIN ep.produtoEdicao as pe ")
				.append("   JOIN pe.lancamentos as lan ");

		hql.append("  WHERE pe.id = :idProdutoEdicao");

		Query query = this.getSession().createQuery(hql.toString());

		query.setLong("idProdutoEdicao", idProdutoEdicao);

		return (BigInteger) query.uniqueResult();
	}

	@Override
	public BigInteger obterQuantidadeEstoqueHistorico(long idProdutoEdicao,
			String tipoEstoque) {
		String coluna = this.getColunaQtde(tipoEstoque);

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ep." + coluna + " as qtde")
				.append("   FROM HistoricoEstoqueProduto as ep ")
				.append("   JOIN ep.produtoEdicao as pe ")
				.append("   JOIN pe.produto as pr ")
				.append("   JOIN pe.lancamentos as lan ");

		hql.append("  WHERE pe.id = :idProdutoEdicao");

		Query query = this.getSession().createQuery(hql.toString());

		query.setLong("idProdutoEdicao", idProdutoEdicao);

		return (BigInteger) query.uniqueResult();
	}

	@Override
	public BigInteger obterQuantidadeEstoqueJuramentado(long idProdutoEdicao) {

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ep.qtde as qtde")
				.append("   FROM EstoqueProdutoCotaJuramentado as ep ")
				.append("   JOIN ep.cota as co ")
				.append("   JOIN co.pessoa as pess ")
				.append("   JOIN ep.produtoEdicao as pe ")
				.append("   JOIN pe.produto as pr ")
				.append("   JOIN pe.lancamentos as lan ");

		hql.append("  WHERE pe.id = :idProdutoEdicao");

		Query query = this.getSession().createQuery(hql.toString());

		query.setLong("idProdutoEdicao", idProdutoEdicao);

		return (BigInteger) query.uniqueResult();
	}

}