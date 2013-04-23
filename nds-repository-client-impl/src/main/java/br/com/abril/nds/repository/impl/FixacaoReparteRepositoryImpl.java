package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.vo.PaginacaoVO;
/**
 * Classe de implementação referente ao acesso a dados da entidade
 *
 * FixacaoReparte
 */

@Repository
public class FixacaoReparteRepositoryImpl extends  AbstractRepositoryModel<FixacaoReparte, Long> implements FixacaoReparteRepository {
 
	public FixacaoReparteRepositoryImpl() {
		super(FixacaoReparte.class);
	}
	
	@Override
	public FixacaoReparte buscarPorId(Long id){
		return super.buscarPorId(id);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO produto) {
	boolean isCodigoProdutoPreenchido = produto!= null && produto.getCodigoProduto() !=null;
	boolean isClassificacaoPreenchida = (produto.getClassificacaoProduto() != null ||produto.getClassificacaoProduto() !="");
		StringBuilder sql = new StringBuilder("");

		sql.append(" select ")
		.append(" f.id as id, ")
		.append(" f.qtdeExemplares as qtdeExemplares,")
		.append(" f.qtdeEdicoes as qtdeEdicoes,")
		.append(" f.dataHora as dataHora,")
		.append(" f.edicaoInicial as edicaoInicial,")
		.append(" f.edicaoFinal as edicaoFinal,")
		.append(" f.edicaoFinal - f.edicaoInicial as edicoesAtendidas,")
		.append(" f.cotaFixada.numeroCota as cotaFixada,")
		.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '')  as nomeCota,")
		.append(" f.produtoFixado.tipoClassificacaoProduto.descricao as classificacaoProduto,")
		.append(" f.usuario.login as usuario,")
		.append(" produto.codigo as codigoProduto,")
		.append(" count(pdv.id) as qtdPdv")

		.append(" from ")
		.append(" FixacaoReparte f ")
		.append(" left join f.cotaFixada.pessoa as pessoa ")
		.append(" left join f.cotaFixada.pdvs as pdv ")
		.append(" inner join f.produtoFixado as produto ")
		.append(" where ")
		.append( " UPPER(produto.nome) like  UPPER(:nomeProduto) ")
		.append(" and f.cotaFixada.tipoDistribuicaoCota = :tipoCota ");
		
		if(isClassificacaoPreenchida){
			sql.append(" and upper(f.produtoFixado.tipoClassificacaoProduto.descricao) = upper(:classificacaoProduto)");
		}
		
		if(isCodigoProdutoPreenchido){
			sql.append(	" and UPPER(produto.codigo) = UPPER(:codigoProduto) ");
		}

		sql.append(" GROUP BY f.id ")
		.append(" order by f.cotaFixada.numeroCota asc ");
		
		Query query = getSession().createQuery(sql.toString());
		
		
		query.setParameter("nomeProduto", produto.getNomeProduto() + "%");
		if(isCodigoProdutoPreenchido){
			query.setParameter("codigoProduto", produto.getCodigoProduto());
		}
		if(isClassificacaoPreenchida){
			query.setParameter("classificacaoProduto", produto.getClassificacaoProduto());
		}
		query.setParameter("tipoCota", TipoDistribuicaoCota.CONVENCIONAL);
		 query.setResultTransformer(new AliasToBeanResultTransformer(FixacaoReparteDTO.class));
		 configurarPaginacaoProduto(produto,query);
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorCota(FiltroConsultaFixacaoCotaDTO cota) {
		boolean isNumeroCotaPreenchido = cota!= null && cota.getCota() !=null;
		
		StringBuilder sql = new StringBuilder("");

		sql.append(" select ")
		.append(" f.id as id, ")
		.append(" f.qtdeExemplares as qtdeExemplares,")
		.append(" f.qtdeEdicoes as qtdeEdicoes,")
		.append(" f.edicaoInicial as edicaoInicial,")
		.append(" f.edicaoFinal as edicaoFinal,")
		.append(" f.dataHora as dataHora ,")
		.append(" f.edicaoFinal - f.edicaoInicial as edicoesAtendidas,")
		.append(" f.cotaFixada.numeroCota as cotaFixada,")
		.append(" f.produtoFixado.codigo as produtoFixado,")
		.append(" f.produtoFixado.nome as nomeProduto, ")
		.append(" f.produtoFixado.tipoClassificacaoProduto.descricao as classificacaoProduto,")
		.append(" f.usuario.login as usuario,")
		.append(" count(pdv.id) as qtdPdv")
		
		.append(" from ")
		.append(" FixacaoReparte f ")
		.append(" inner join f.cotaFixada as cota ")
		.append(" inner join cota.pdvs as pdv ")
		
		.append(" where ")
		.append(" upper(cota.pessoa.nome) like  upper(:nomeCota) ")
		.append(" or upper(cota.pessoa.nomeFantasia) like  upper(:nomeCota) ")
		.append(" or upper(cota.pessoa.razaoSocial) like  upper(:nomeCota) ")
		.append(" and f.cotaFixada.tipoDistribuicaoCota = :tipoCota ");
		
		if(isNumeroCotaPreenchido ){
			sql.append(	" and cota.numeroCota = :numeroCota ");
		}

		sql.append(" GROUP BY f.id ")
		.append(" order by f.produtoFixado.nome asc ");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("nomeCota", "%"+ cota.getNomeCota() +"%");
		if(isNumeroCotaPreenchido ){
			query.setParameter("numeroCota", new Integer(cota.getCota()));
		}
		 query.setParameter("tipoCota", TipoDistribuicaoCota.CONVENCIONAL);
		 query.setResultTransformer(new AliasToBeanResultTransformer(FixacaoReparteDTO.class));
		configurarPaginacao(cota, query);
		return query.list();
	}
	
	private void configurarPaginacao(FiltroConsultaFixacaoCotaDTO dto, Query query) {

		  PaginacaoVO paginacao = dto.getPaginacao();

		  if (paginacao.getQtdResultadosTotal().equals(0)) {
		   paginacao.setQtdResultadosTotal(query.list().size());
		  }

		  if(paginacao.getQtdResultadosPorPagina() != null) {
		   query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		  }

		  if (paginacao.getPosicaoInicial() != null) {
		   query.setFirstResult(paginacao.getPosicaoInicial());
		  }
	 }
	
	private void configurarPaginacaoProduto(FiltroConsultaFixacaoProdutoDTO dto, Query query) {

		  PaginacaoVO paginacao = dto.getPaginacao();

		  if (paginacao.getQtdResultadosTotal().equals(0)) {
		   paginacao.setQtdResultadosTotal(query.list().size());
		  }

		  if(paginacao.getQtdResultadosPorPagina() != null) {
		   query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		  }

		  if (paginacao.getPosicaoInicial() != null) {
		   query.setFirstResult(paginacao.getPosicaoInicial());
		  }
	 }

	@Override
	public FixacaoReparte buscarPorProdutoCota(Cota cota, Produto produto) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" from ");
		sql.append(" FixacaoReparte f ");
		sql.append(" where f.cotaFixada = :cotaSelecionada ");
		sql.append(" and f.produtoFixado = :produtoSelecionado  ");
		
		Query query  = getSession().createQuery(sql.toString());
		query.setParameter("cotaSelecionada",  cota);
		query.setParameter("produtoSelecionado", produto);
		
		return (FixacaoReparte)query.uniqueResult();
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<FixacaoReparte> buscarPorCota(Cota cota){
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" from ");
		sql.append(" FixacaoReparte f ");
		sql.append(" where f.cotaFixada = :cotaSelecionada ");
		
		Query query  = getSession().createQuery(sql.toString());
		query.setParameter("cotaSelecionada",  cota);
		
		
		return query.list();
	}
	
	
	public void removerPorCota(Cota cota){
		StringBuilder sql = new StringBuilder("");
		
		sql.append("delete from ");

		sql.append(" FixacaoReparte f ");
		
		sql.append(" where f.cotaFixada = :cotaSelecionada ");
		
		Query query  = getSession().createQuery(sql.toString());
		query.setParameter("cotaSelecionada",  cota);
		
	}

	@Override
	public boolean isFixacaoExistente(FixacaoReparteDTO fixacaoReparteDTO) {
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" from ")
		.append(" FixacaoReparte f ")
		.append(" where f.cotaFixada.numeroCota = :cotaSelecionada ")
		.append(" and f.produtoFixado.codigo = :produtoSelecionado  ");
		
		Query query  = getSession().createQuery(sql.toString());
		query.setParameter("cotaSelecionada",  fixacaoReparteDTO.getCotaFixada());
		query.setParameter("produtoSelecionado", fixacaoReparteDTO.getProdutoFixado());
		
		return query.list().size() > 0;
	}

	@Override
	public void execucaoQuartz() {
		StringBuilder hql = new StringBuilder("");

		hql.append("")
				.append(" delete from fixacao_reparte   ")
				.append(" where  fixacao_reparte.manter_fixa is false ")
				.append(" and fixacao_reparte.data_hora <= date_format(DATE_SUB(CURDATE(),INTERVAL 732 DAY),'%d/%m/%Y') ")
				.append(" and fixacao_reparte.ID_PRODUTO in  ")
				.append(" ( select distinct id_produto from fixacao_reparte ")
				.append(" join produto on produto.ID = fixacao_reparte.ID_PRODUTO  ")
				.append(" join produto_edicao on produto_edicao.PRODUTO_ID = produto.ID ")
				.append(" join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID ")
				.append(" where lancamento.DATA_CRIACAO <= date_format(DATE_SUB(CURDATE(),INTERVAL 732 DAY),'%d/%m/%Y')) ");
				
		Query query = getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
		
	}
	
}
