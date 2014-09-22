package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.InfoProdutosBonificacaoDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.model.planejamento.StatusLancamento;
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
		hql.append(" produto.codigoICD AS codigoICD, ");
		hql.append(" prodEdicao.numeroEdicao AS numeroEdicao, ");
		hql.append(" produto.nome AS nomeProduto, ");
		hql.append(" periodoLancamentoParcial.numeroPeriodo AS periodo, ");
		hql.append(" prodEdicao.precoVenda AS preco, ");
		hql.append(" lancamento.status AS status, ");
		hql.append(" estudoG.qtdeReparte AS reparteDistribuido, ");
		hql.append(" estudoG.abrangencia AS percentualAbrangencia, ");
		hql.append(" (select t.descricao from TipoClassificacaoProduto t where t.id=prodEdicao.tipoClassificacaoProduto.id) as tipoClassificacaoProdutoDescricao, ");
		hql.append(" lancamento.dataLancamentoPrevista AS datalanc, ");
		hql.append(" lancamento.dataLancamentoDistribuidor AS dataLcto, ");
		hql.append(" lancamento.dataRecolhimentoPrevista AS dataRcto, ");
		hql.append(" CASE ");
		hql.append(" WHEN estudoG.dataAlteracao = null THEN estudoG.dataCadastro ");
		hql.append(" ELSE estudoG.dataAlteracao  end AS dataAlteracao, ");
		hql.append(" estudoG.id AS estudo, ");
		hql.append(" estudoG.liberado AS estudoLiberado, ");
		hql.append(" estudoG.qtdeReparte AS qtdeReparteEstudo, ");

		hql.append(" coalesce(estudoG.reparteMinimo, 0) as reparteMinimo, ");
		
		hql.append(" estudoG.tipoGeracaoEstudo AS algoritmo, ");
		hql.append(" usuarioEstudo.nome AS nomeUsuario, ");
		
		hql.append(" (select sum(coalesce(cec.qtdePrevista, 0) - coalesce(coe.qtde, 0)) ");
		hql.append(" from ChamadaEncalhe ce ");
		hql.append(" join ce.lancamentos l ");
		hql.append(" join ce.chamadaEncalheCotas cec ");
		hql.append(" left outer join cec.conferenciasEncalhe coe ");
		hql.append(" where ce.produtoEdicao = prodEdicao ");
		hql.append(" group by ce.produtoEdicao) AS venda ");
        
		hql.append(" FROM Lancamento AS lancamento ");
		
		hql.append(" join lancamento.produtoEdicao AS prodEdicao ");
		hql.append(" join prodEdicao.produto AS produto ");
		hql.append(" left join lancamento.periodoLancamentoParcial AS periodoLancamentoParcial, ");
		hql.append(" EstudoGerado as estudoG ");
		hql.append(" left join estudoG.usuario as usuarioEstudo ");
		hql.append(" WHERE ");
		hql.append(" estudoG.lancamentoID = lancamento.id ");
		
		List<String> whereClauseList = new ArrayList<>();
		
		if(StringUtils.isNotEmpty(filtro.getCodProduto())){
			whereClauseList.add(" produto.codigoICD = :COD_PRODUTO ");
		}
		
		if(filtro.getNumeroEdicao()!=null){
			whereClauseList.add(" prodEdicao.numeroEdicao = :NUMERO_EDICAO ");
		}
		
		if(filtro.getNumeroEstudo()!=null){
			whereClauseList.add(" estudoG.id = :numero_estudo ");
		}
		
		if(filtro.getIdTipoClassificacaoProd() !=null && filtro.getIdTipoClassificacaoProd() > 0){
			whereClauseList.add(" prodEdicao.tipoClassificacaoProduto.id = :ID_CLASSIFICACAO ");
		}
		
		if(!whereClauseList.isEmpty()){
			hql.append(" AND ");
		}
		
		hql.append(StringUtils.join(whereClauseList, " AND "));
		
		hql.append(" group BY estudoG.id ");
		
		hql.append(this.ordenarConsultaBuscarProdutos(filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		setParameterBuscarProduto(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesProdutoDTO.class));
		
		if (filtro != null){
			configurarPaginacao(filtro, query);
		}
		return query.list();
	}

	
	private String ordenarConsultaBuscarProdutos(FiltroInformacoesProdutoDTO filtro) {

		StringBuilder hql = new StringBuilder();

		if (filtro.getOrdemColuna() != null) {

		    switch (filtro.getOrdemColuna()) {

		    case CODIGO:
		    	hql.append(" ORDER BY codigoICD ");
			break;

		    case EDICAO:
		    	hql.append(" ORDER BY numeroEdicao ");
			break;

		    case PRODUTO:
		    	hql.append(" ORDER BY nomeProduto ");
			break;

		    case CLASSIFICACAO:
		    	hql.append(" ORDER BY tipoClassificacaoProdutoDescricao ");
			break;

		    case PERIODO:
		    	hql.append(" ORDER BY periodo ");
			break;

		    case PRECO:
		    	hql.append(" ORDER BY preco ");
			break;

		    case STATUS:
		    	hql.append(" ORDER BY status ");
			break;
			
		    case REPARTE:
	    		hql.append("ORDER BY reparteDistribuido ");
			break;
			
		    case VENDA:
		    	hql.append("ORDER BY venda ");
			break;
				
		    case ABRANGENCIA:
	    		hql.append("ORDER BY percentualAbrangencia ");
			break;
				
		    case DATA_LANCAMENTO:
				hql.append("ORDER BY dataLcto ");
			break;
				
		    case DATA_RELANCAMENTO:
				hql.append("ORDER BY dataRcto ");
			break;
				
		    case ALGORITMO:
				hql.append("ORDER BY algoritmo ");
			break;
				
		    case REPARTE_MINIMO:
				hql.append("ORDER BY reparteMinimo ");
			break;
				
		    case ESTUDO:
				hql.append("ORDER BY estudo ");
			break;
				
		    case USUARIO:
				hql.append("ORDER BY nomeUsuario ");
			break;
				
		    case DATA:
				hql.append("ORDER BY dataAlteracao ");
			break;
				
		    case HORA:
				hql.append("ORDER BY hora ");
			break;
				
		    default:
				hql.append(" ORDER BY estudo desc ");
		    }

		    if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
		    	hql.append(filtro.getPaginacao().getOrdenacao().toString());
		    }

		}else{
			hql.append(" ORDER BY estudo desc ");
		}
		return hql.toString();
	}
	
	private void setParameterBuscarProduto(FiltroInformacoesProdutoDTO filtro,
			Query query) {
		if(filtro.getCodProduto()!=null){
			query.setParameter("COD_PRODUTO", filtro.getCodProduto());
		}
		
		if(filtro.getNumeroEdicao()!=null){
			query.setParameter("NUMERO_EDICAO", filtro.getNumeroEdicao());
		}
		if(filtro.getNumeroEstudo()!=null){
			query.setParameter("numero_estudo", filtro.getNumeroEstudo());
		}
		
		if(filtro.getIdTipoClassificacaoProd() !=null && filtro.getIdTipoClassificacaoProd() > 0){
			query.setParameter("ID_CLASSIFICACAO", filtro.getIdTipoClassificacaoProd());
		}
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
		hql.append(" group by nomeComercial ");
		
		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("COD_PRODUTO", codProduto);
		query.setParameter("NUM_EDICAO", numEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesCaracteristicasProdDTO.class));		

		query.setMaxResults(1);
		
		return (InformacoesCaracteristicasProdDTO) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InfoProdutosBonificacaoDTO> buscarItensRegiao(Long idEstudo) {
		
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
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InfoProdutosBonificacaoDTO.class));
		
		return query.list();
		
	}
	
	private void configurarPaginacao(FiltroInformacoesProdutoDTO filtro, Query query) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		
		if(paginacao==null)return;
		
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
	public InformacoesVendaEPerceDeVendaDTO buscarVendas(String codProduto, Long numEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append(" sum(if(tipoMovimento.OPERACAO_ESTOQUE ='ENTRADA',movCota.QTDE,movCota.QTDE*-1)) as totalVenda, ");
		sql.append(" (	sum(if(tipoMovimento.OPERACAO_ESTOQUE ='ENTRADA',movCota.QTDE,movCota.QTDE*-1)) ");
		sql.append(" 	/	 ");
		sql.append(" 	sum(if(tipoMovimento.OPERACAO_ESTOQUE ='ENTRADA',movCota.QTDE,0)) ");
		sql.append(" )	* 100 ");
		sql.append(" as porcentagemDeVenda ");
		sql.append(" from movimento_estoque_cota movCota ");
		sql.append(" join tipo_movimento tipoMovimento on tipoMovimento.ID = movCota.TIPO_MOVIMENTO_ID ");
		sql.append(" join lancamento lancamento on lancamento.ID = movCota.LANCAMENTO_ID ");
		sql.append(" join produto_edicao produtoEdicao on produtoEdicao.ID = lancamento.PRODUTO_EDICAO_ID ");
		sql.append(" join produto produto on produto.ID = produtoEdicao.PRODUTO_ID ");
		sql.append(" where lancamento.STATUS = :statusFechado ");
		sql.append(" and produtoEdicao.NUMERO_EDICAO =:NUM_EDICAO  ");
		sql.append(" and produto.CODIGO =:COD_PRODUTO  ");
				
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("COD_PRODUTO", codProduto);
		query.setParameter("NUM_EDICAO", numEdicao);
		query.setParameter("statusFechado", StatusLancamento.FECHADO.name());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesVendaEPerceDeVendaDTO.class));
		 
		return (InformacoesVendaEPerceDeVendaDTO) query.uniqueResult();
		
	}

}
