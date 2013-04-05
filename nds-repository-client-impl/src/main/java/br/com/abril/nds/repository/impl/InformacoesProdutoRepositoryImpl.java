package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.InformacoesAbrangenciaEMinimoProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.InformacoesProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class InformacoesProdutoRepositoryImpl extends AbstractRepositoryModel<InformacoesProdutoDTO, Long> implements InformacoesProdutoRepository {
	
	public InformacoesProdutoRepositoryImpl( ) {
		super(InformacoesProdutoDTO.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InformacoesProdutoDTO> buscarProdutos(FiltroInformacoesProdutoDTO filtro) {
//		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		
		hql.append(" produto.codigo AS codProduto, ");
		hql.append(" prodEdicao.numeroEdicao AS numeroEdicao, ");
		hql.append(" produto.nome AS nomeProduto, ");
		hql.append(" produto.periodicidade AS periodo, ");
		hql.append(" prodEdicao.precoVenda AS preco, ");
		hql.append(" lancamento.tipoLancamento AS status, ");
		hql.append(" prodEdicao.reparteDistribuido AS reparteDistribuido, ");
		hql.append(" produto.percentualAbrangencia AS percentualAbrangencia, ");
		hql.append(" lancamento.dataLancamentoPrevista AS dataLcto, ");
		hql.append(" lancamento.dataRecolhimentoPrevista AS dataRcto, ");

//		hql.append(" prodEdicao.venda AS venda, ");
		hql.append(" prodEdicao.reparteDistribuido AS reparteMinimo, "); // DADO INCONSISTENTE...
		
		hql.append(" algortm.descricao AS algoritmo, "); 
		hql.append(" estudoG.dataAlteracao AS dataAlteracao, ");
		hql.append(" estudoG.id AS estudo, "); 
		hql.append(" usuarioEstudo.nome AS nomeUsuario ");

		hql.append(" FROM ProdutoEdicao AS prodEdicao ");
		
		hql.append(" left join prodEdicao.produto AS produto ");
		hql.append(" left join prodEdicao.lancamentos AS lancamento ");
		hql.append(" left join produto.algoritmo AS algortm ");
		hql.append(" INNER join lancamento.estudo AS estudoG ");
		hql.append(" left join estudoG.usuario as usuarioEstudo ");
		
		hql.append(" WHERE produto.codigo = :COD_PRODUTO ");
		hql.append(" AND produto.nome = :NOME_PRODUTO ");
		hql.append(this.getSqlWhereBuscarProdutos(filtro));
		
		hql.append(" ORDER BY numeroEdicao ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("COD_PRODUTO", filtro.getCodProduto());
		query.setParameter("NOME_PRODUTO", filtro.getNomeProduto());
		this.paramsDinamicosBuscarProdutos(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesProdutoDTO.class));
		
		if (filtro != null){
			configurarPaginacao(filtro, query);
		}
		return query.list();
	}
		
	
//	StringBuilder sql = new StringBuilder();
//	
//				 sql.append(" select ")
//				.append(" 	produto.CODIGO as codProduto,") 
//				.append(" 	produtoEdicao.NUMERO_EDICAO as numeroEdicao,")	    
//				.append("   produto.NOME as nomeProduto,") 
//				.append("   produto.PERIODICIDADE as periodo,")
//				.append("   produtoEdicao.PRECO_VENDA as preco,")
//				.append("   lancamento.TIPO_LANCAMENTO as status,")
//				.append("   produtoEdicao.REPARTE_DISTRIBUIDO as reparteDistribuido,")
//				.append("   produto.PERCENTUAl_ABRANGENCIA as percentualAbrangencia,")
//				.append("   lancamento.DATA_LCTO_PREVISTA as dataLcto,")
//				.append("   lancamento.DATA_REC_PREVISTA as dataRcto,")
//				.append("   estrateg.REPARTE_MINIMO as reparteMinimoGhoma,")
//				.append("   algoritmo.DESCRICAO as algoritmo,")
//				.append("   estudo.ID as estudo,")
//				.append("   usua.NOME as nomeUsuario,")
//				.append("   estudo.DATA_ALTERACAO as dataAlteracao ")
//				.append("   from		")
//				.append("   PRODUTO_EDICAO produtoEdicao") 
//				.append("   left outer join")
//				.append("   	PRODUTO produto") 
//				.append("       	on produtoEdicao.PRODUTO_ID=produto.ID") 
//				.append("   left outer join")
//				.append("       ALGORITMO algoritmo") 
//				.append("           on produto.ALGORITMO_ID=algoritmo.ID") 
//				.append("   left outer join")
//				.append("       LANCAMENTO lancamento") 
//				.append("           on produtoEdicao.ID=lancamento.PRODUTO_EDICAO_ID") 
//				.append("   inner join")
//				.append("       ESTUDO estudo") 
//				.append("           on lancamento.PRODUTO_EDICAO_ID=estudo.PRODUTO_EDICAO_ID") 
//				.append("           and lancamento.DATA_LCTO_PREVISTA=estudo.DATA_LANCAMENTO")
//				.append("   left join usuario usua ")
//				.append("         on usua.ID = estudo.USUARIO_ID")
//				.append("   left join estrategia estrateg")
//				.append("         on estrateg.PRODUTO_EDICAO_ID = produtoEdicao.ID")
//				.append("   where		")
//				.append(" 		produto.codigo = :COD_PRODUTO ")
//				.append(" 		AND produto.nome = :NOME_PRODUTO ")
//				.append(this.getSqlWhereBuscarProdutos(filtro));
//				 
////				.append(" ORDER BY numeroEdicao ");
//				 
//		SQLQuery query = getSession().createSQLQuery(sql.toString());
//		
//		query.setParameter("COD_PRODUTO", filtro.getCodProduto());
//		query.setParameter("NOME_PRODUTO", filtro.getNomeProduto());
//		
//		this.paramsDinamicosBuscarProdutos(query, filtro);
//
//		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesProdutoDTO.class));
//
//		if (filtro != null){
//			configurarPaginacao(filtro, query);
//		}
//		
//		return query.list();
	
	private String paramsDinamicosBuscarProdutos (Query query, FiltroInformacoesProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		if(filtro.getIdTipoClassificacaoProd() !=null && filtro.getIdTipoClassificacaoProd() > 0){
			query.setParameter("ID_CLASSIFICACAO", filtro.getIdTipoClassificacaoProd());
		}
		
		return hql.toString();
	}
	
	private String getSqlWhereBuscarProdutos (FiltroInformacoesProdutoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		if(filtro.getIdTipoClassificacaoProd() !=null && filtro.getIdTipoClassificacaoProd() > 0){
			hql.append(" AND produto.tipoClassificacaoProduto.id = :ID_CLASSIFICACAO ");
		}

		return hql.toString();
	}
	
	
	@Override
	public InformacoesCaracteristicasProdDTO buscarCaracteristicas(String codProduto, Long numEdicao) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" prodEdicao.precoVenda AS precoVenda, ");
		hql.append(" prodEdicao.pacotePadrao AS pacotePadrao, ");
		hql.append(" prodEdicao.chamadaCapa AS chamadaCapa, ");
		hql.append(" prodEdicao.nomeComercial AS nomeComercial, ");
		hql.append(" prodEdicao.boletimInformativo AS boletimInformativo ");
		
		
		hql.append(" FROM ProdutoEdicao AS prodEdicao ");
		hql.append(" WHERE produto.codigo = :COD_PRODUTO AND ");
		hql.append(" prodEdicao.numeroEdicao = :NUM_EDICAO ");
		
		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("COD_PRODUTO", codProduto);
		query.setParameter("NUM_EDICAO", numEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesCaracteristicasProdDTO.class));		

		return (InformacoesCaracteristicasProdDTO) query.uniqueResult();
	}
	
	@Override
	public InformacoesAbrangenciaEMinimoProdDTO buscarAbrangenciaEMinimo(Long estudoId) {
		/*
		 * select 
	est.reparte_minimo as minimoSugerido, est.ABRANGENCIA as abrangSugerido, estudo.ID as estudoId 
	from estrategia est
	inner join produto_edicao prodEdic 
	  ON est.PRODUTO_EDICAO_ID = prodEdic.ID
	inner join estudo 
	  ON estudo.PRODUTO_EDICAO_ID = prodEdic.ID
	where estudo.id = 27161;
		 */

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("     distinct ");
		sql.append("         est.reparte_minimo as minimoSugerido, ");
		sql.append("         est.ABRANGENCIA as abrangenciaSugerida, ");
		sql.append("         estudo.ID as minimoEstudoId ");
		sql.append("     FROM ");
		sql.append("         estrategia est ");
		sql.append("     INNER JOIN ");
		sql.append("         produto_edicao prodEdic  ");
		sql.append("             ON est.PRODUTO_EDICAO_ID = prodEdic.ID ");
		sql.append("     INNER JOIN  ");
		sql.append("         estudo  ");
		sql.append("             ON estudo.PRODUTO_EDICAO_ID = prodEdic.ID ");
		sql.append("     where ");
		sql.append("         estudo.id = ");
		sql.append(estudoId);

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesAbrangenciaEMinimoProdDTO.class));
		 
		return (InformacoesAbrangenciaEMinimoProdDTO) query.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InfoProdutosItemRegiaoEspecificaDTO> buscarItensRegiao() {

			StringBuilder hql = new StringBuilder();
			
			hql.append(" SELECT ");
			hql.append(" regiao.nomeRegiao as nomeItemRegiao");
			hql.append(" FROM Regiao AS regiao ");
			
			Query query = super.getSession().createQuery(hql.toString());

			query.setResultTransformer(new AliasToBeanResultTransformer(InfoProdutosItemRegiaoEspecificaDTO.class));
			
			return query.list();
	}
	
	private void configurarPaginacao(FiltroInformacoesProdutoDTO filtro, Query query) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		
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

}
