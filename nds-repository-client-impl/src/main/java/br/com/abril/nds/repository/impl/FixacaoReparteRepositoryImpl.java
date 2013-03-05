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
	
	@SuppressWarnings("unchecked")
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

		sql.append(" select ");
		
		sql.append(" f.id as id, " +
				" f.qtdeExemplares as qtdeExemplares," +
				" f.qtdeEdicoes as qtdeEdicoes," +
				" f.dataHora as dataHora," +
				" f.edicaoInicial as edicaoInicial," +
				" f.edicaoFinal as edicaoFinal," +
				" f.edicaoFinal - f.edicaoInicial as edicoesAtendidas," +
				" f.cotaFixada.numeroCota as cotaFixada," +
				" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '')  as nomeCota," +
				" f.produtoFixado.tipoClassificacaoProduto.descricao as classificacaoProduto," +
				" f.usuario.login as usuario," +
				" produto.codigo as codigoProduto," +
				" count(pdv.id) as qtdPdv");

		sql.append(" from ");

		sql.append(" FixacaoReparte f ");
		
		sql.append(" left join f.cotaFixada.pessoa as pessoa ");

		sql.append(" left join f.cotaFixada.pdvs as pdv ");
		
		sql.append(" inner join f.produtoFixado as produto ");
		
		sql.append(" where UPPER(produto.nome) like  UPPER(:nomeProduto) ");
		
		sql.append(" and f.cotaFixada.tipoDistribuicaoCota = :tipoCota ");
		
		if(isClassificacaoPreenchida){
			sql.append(" and upper(f.produtoFixado.tipoClassificacaoProduto.descricao) = upper(:classificacaoProduto)");
		}
		
		if(isCodigoProdutoPreenchido){
			sql.append(	" and UPPER(produto.codigo) = UPPER(:codigoProduto) ");
		}

		sql.append(" GROUP BY f.id ");
		
		sql.append(" order by f.dataHora asc ");
		
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

		sql.append(" select ");
		
		sql.append(" f.id as id, " +
				" f.qtdeExemplares as qtdeExemplares," +
				" f.qtdeEdicoes as qtdeEdicoes," +
				" f.edicaoInicial as edicaoInicial," +
				" f.edicaoFinal as edicaoFinal," +
				" f.dataHora as dataHora ," +
				" f.edicaoFinal - f.edicaoInicial as edicoesAtendidas," +
				" f.cotaFixada.numeroCota as cotaFixada," +
				" f.produtoFixado.codigo as produtoFixado," +
				" f.produtoFixado.nome as nomeProduto, " +
				" f.produtoFixado.tipoClassificacaoProduto.descricao as classificacaoProduto," +
				" f.usuario.login as usuario," +
				" count(pdv.id) as qtdPdv");

		sql.append(" from ");

		sql.append(" FixacaoReparte f ");

		sql.append(" inner join f.cotaFixada as cota ");
		sql.append(" inner join cota.pdvs as pdv ");
		
		sql.append(" where cota.pessoa.nome like  :nomeCota ");
		sql.append(" and f.cotaFixada.tipoDistribuicaoCota = :tipoCota ");
		
		if(isNumeroCotaPreenchido ){
			sql.append(	" and cota.numeroCota = :numeroCota ");
		}

		sql.append(" GROUP BY f.id ");
		
		sql.append(" order by f.dataHora asc ");
		
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
		
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" from ");

		sql.append(" FixacaoReparte f ");
		
		sql.append(" where f.cotaFixada = :cotaSelecionada ");
		
		sql.append(" and f.produtoFixado = :produtoSelecionado  ");
		
		Query query  = getSession().createQuery(sql.toString());
		query.setParameter("cotaSelecionada",  cota);
		query.setParameter("produtoSelecionado", produto);
		
		return (FixacaoReparte)query.uniqueResult();
	}
	
	
	
	
	
	
	
}

