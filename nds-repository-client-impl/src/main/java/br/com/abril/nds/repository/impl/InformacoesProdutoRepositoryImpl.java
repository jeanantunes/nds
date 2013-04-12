package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.InformacoesAbrangenciaEMinimoProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesReparteTotalEPromocionalDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
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
		hql.append(" estudoG.dataAlteracao AS dataAlteracao, ");
		hql.append(" estudoG.id AS estudo, "); 

		hql.append(" (select sum(estCota.reparteMinimo)    					" + 
				   "	from EstudoCota estCota  							" +
				   "  	inner join estCota.estudo as estRM                  " + 
				   "	where estRM.id in (estudoG.id)) as reparteMinimo,   ");
		
		hql.append(" algortm.descricao AS algoritmo, "); 
		hql.append(" usuarioEstudo.nome AS nomeUsuario, ");
		
		hql.append(" (select sum(estqPC.qtdeRecebida - estqPC.qtdeDevolvida) as totalVenda           " + 
				"			  from MovimentoEstoqueCota movEC                                        " + 
				"		      inner join movEC.estoqueProdutoCota AS estqPC                          " + 
				"				  where estqPC.produtoEdicao = prodEdicao.id) AS venda               ");                                                                

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

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("     distinct ");
		sql.append("         est.reparte_minimo as minimoSugerido, ");
		sql.append("         est.ABRANGENCIA as abrangenciaSugerida ");
		sql.append("     FROM ");
		sql.append("         estrategia est ");
		sql.append("     INNER JOIN ");
		sql.append("         produto_edicao prodEdic  ");
		sql.append("             ON est.PRODUTO_EDICAO_ID = prodEdic.ID ");
		sql.append("     INNER JOIN  ");
		sql.append("         estudo estud ");
		sql.append("             ON estud.PRODUTO_EDICAO_ID = prodEdic.ID ");
		sql.append("     where ");
		sql.append("         estud.id = ");
		sql.append(estudoId);

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesAbrangenciaEMinimoProdDTO.class));
		 
		return (InformacoesAbrangenciaEMinimoProdDTO) query.uniqueResult();
		
	}
	
	@Override
	public BigDecimal buscarAbrangenciaApurada(String codProduto, Long numEdicao) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("   sum( ");
		sql.append("   		(select sum(reparte) from lancamento ");
		sql.append("       		JOIN produto_edicao on produto_edicao.ID = lancamento.PRODUTO_EDICAO_ID ");
		sql.append("        	JOIN PRODUTO ON produto_edicao.PRODUTO_ID=PRODUTO.ID ");
		sql.append("        	where produto.CODIGO = :COD_PRODUTO ");
		sql.append("         	and produto_Edicao.NUMERO_EDICAO in (:NUM_EDICAO)) ");
		sql.append("         /estd.REPARTE_DISTRIBUIR)");
		sql.append("         from estudo estd");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("COD_PRODUTO", codProduto);
		query.setParameter("NUM_EDICAO", numEdicao);
		
		return (BigDecimal)query.uniqueResult();	
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<InfoProdutosItemRegiaoEspecificaDTO> buscarItensRegiao(Long idEstudo) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" estBoni.ELEMENTO AS nomeItemRegiao, ");
		sql.append(" estBoni.REPARTE_MINIMO AS qtdReparteMin, ");
		sql.append(" estBoni.BONIFICACAO AS bonificacao ");
		sql.append(" from ");
		sql.append(" estudo_bonificacoes as estBoni ");
		sql.append(" where ");
		sql.append(" ESTUDO_ID in (:ESTUDO_ID) and estBoni.COMPONENTE in (:COMPONENTE) ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("ESTUDO_ID", idEstudo);
		query.setParameter("COMPONENTE", "REGIAO");
		
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

	@Override
	public InformacoesReparteTotalEPromocionalDTO buscarRepartes(String codProduto, Long numEdicao) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("         sum(lanc.reparte) as reparteTotal, ");
		sql.append("         lanc.REPARTE_PROMOCIONAL as repartePromocional ");
		sql.append("     FROM ");
		sql.append("         lancamento lanc ");
		sql.append("     JOIN ");
		sql.append("         produto_edicao prodEdic  ");
		sql.append("             ON prodEdic.ID=lanc.PRODUTO_EDICAO_ID ");
		sql.append("     JOIN  ");
		sql.append("         PRODUTO prod ");
		sql.append("             ON prodEdic.PRODUTO_ID=prod.ID ");
		sql.append("     where ");
		sql.append("         prod.CODIGO = :COD_PRODUTO ");
		sql.append("	 and prodEdic.NUMERO_EDICAO = :NUM_EDICAO ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("COD_PRODUTO", codProduto);
		query.setParameter("NUM_EDICAO", numEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesReparteTotalEPromocionalDTO.class));
		 
		return (InformacoesReparteTotalEPromocionalDTO) query.uniqueResult();
	}

	@Override
	public BigInteger buscarReparteDaEdica_Sobra(Long estudoID) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT ");
		
		hql.append(" (lanc.reparte - lanc.repartePromocional - estud.reparteDistribuir) as sobra ");

		hql.append(" FROM Lancamento AS lanc ");
		
		hql.append(" inner join lanc.estudo AS estud ");
		
		hql.append(" WHERE estud.id = :estudo_id ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("estudo_id", estudoID);
		
		return (BigInteger)query.uniqueResult();

	}

	@Override
	public BigInteger buscarVendaTotal(String codProduto, Long numEdicao) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("         sum(estqPC.QTDE_RECEBIDA - estqPC.QTDE_DEVOLVIDA) as totalVenda ");
		sql.append("     FROM ");
		sql.append("         movimento_estoque_cota movEC ");
		sql.append("     JOIN ");
		sql.append("         estoque_produto_cota estqPC  ");
		sql.append("             ON movEC.ESTOQUE_PROD_COTA_ID = estqPC.ID ");
		sql.append("     where ");
		sql.append("         estqPC.PRODUTO_EDICAO_ID in ( ");
		sql.append("         	select prodEdic.ID from produto_edicao prodEdic ");
		sql.append("         		inner join produto prod ");
		sql.append("        			ON prodEdic.PRODUTO_ID = prod.ID ");
		sql.append("				where prod.CODIGO = :COD_PRODUTO and prodEdic.NUMERO_EDICAO = :NUM_EDICAO ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("COD_PRODUTO", codProduto);
		query.setParameter("NUM_EDICAO", numEdicao);
		
		return (BigInteger) query.uniqueResult();
		
	}

	@Override
	public InformacoesVendaEPerceDeVendaDTO buscarVendas(String codProduto, Long numEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("     sum(estqPC.QTDE_RECEBIDA - estqPC.QTDE_DEVOLVIDA) as totalVenda, ");
		sql.append("     ((sum(estqPC.QTDE_RECEBIDA - estqPC.QTDE_DEVOLVIDA)/ ");
		sql.append("     (select ");
		sql.append("      	sum(reparte) from lancamento ");
		sql.append("      	JOIN produto_edicao prodEdic on prodEdic.ID = lancamento.PRODUTO_EDICAO_ID ");
		sql.append("      	JOIN PRODUTO ON prodEdic.PRODUTO_ID=PRODUTO.ID ");
		sql.append("      where produto.CODIGO = :COD_PRODUTO and ");
		sql.append("      	prodEdic.NUMERO_EDICAO in (:NUM_EDICAO) ");
		sql.append("      )))*100 as porcentagemDeVenda ");
		sql.append("   from movimento_estoque_cota");
		sql.append("   	  inner join estoque_produto_cota estqPC ");
		sql.append("      	ON movimento_estoque_cota.ESTOQUE_PROD_COTA_ID = estqPC.ID ");
		sql.append("where estqPC.PRODUTO_EDICAO_ID in ( ");
		sql.append("   	    select prodEdic.ID from produto_edicao prodEdic ");
		sql.append("      		inner join produto prod ON prodEdic.PRODUTO_ID = prod.ID ");
		sql.append("     	where prod.CODIGO = ':COD_PRODUTO' and prodEdic.NUMERO_EDICAO = :NUM_EDICAO )");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("COD_PRODUTO", codProduto);
		query.setParameter("NUM_EDICAO", numEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesVendaEPerceDeVendaDTO.class));
		 
		return (InformacoesVendaEPerceDeVendaDTO) query.uniqueResult();
		
	}

}
