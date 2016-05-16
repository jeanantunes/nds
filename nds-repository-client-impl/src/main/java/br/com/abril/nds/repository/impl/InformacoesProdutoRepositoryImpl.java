package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
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

		StringBuilder sql = new StringBuilder();
		
		
		sql.append("  SELECT ");
		sql.append("         pd.codigo as codProduto, ");
		sql.append("         pd.codigo_icd as codigoICD, ");
		sql.append("         pe.numero_edicao as numeroEdicao, ");
		sql.append("         pe.id as idProdutoEdicao, ");
		sql.append("         eg.QTDE_REPARTE AS qtdeReparteEstudo, ");
		sql.append("         pd.NOME as nomeProduto, ");
		sql.append("         plp.NUMERO_PERIODO as periodo, ");
		sql.append("         pe.PRECO_VENDA as preco, ");
		sql.append("         lanc.STATUS as status, ");
		
		sql.append("         cast(sum(case ");
		sql.append("             when tipo.OPERACAO_ESTOQUE = 'ENTRADA' ");
		sql.append("                     THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) ");
		sql.append("                	 ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) ");
		sql.append("             	  end) as unsigned int) AS reparteDistribuido, ");
		
		sql.append("         eg.abrangencia as percentualAbrangencia, ");
		sql.append("         tcp.DESCRICAO as tipoClassificacaoProdutoDescricao, ");
		sql.append("         lanc.DATA_LCTO_PREVISTA as datalanc, ");
		sql.append("         lanc.DATA_LCTO_DISTRIBUIDOR as dataLcto, ");
		sql.append("         lanc.DATA_REC_PREVISTA as dataRcto, ");
		sql.append("         case ");
		sql.append("             when eg.DATA_ALTERACAO is null then eg.DATA_CADASTRO ");
		sql.append("             else eg.DATA_ALTERACAO ");
		sql.append("         end as dataAlteracao, ");
		sql.append("         eg.ID as estudo, ");
		sql.append("         eg.LIBERADO as estudoLiberado, ");
		sql.append("         coalesce(eg.REPARTE_MINIMO,0) as reparteMinimo, ");
		sql.append("         eg.TIPO_GERACAO_ESTUDO as algoritmo, ");
		sql.append("         usu.NOME as nomeUsuario, ");
		
		sql.append("         case ");
		sql.append("             when lanc.STATUS IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO') ");
		sql.append("                 then cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA'  ");
		sql.append("                               THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) ");
		sql.append("                			   ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) ");
		sql.append("             end) - ");
		sql.append("             (select sum(mecEncalhe.qtde) ");
		sql.append("                 from ");
		sql.append("                     lancamento lancto ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     chamada_encalhe_lancamento cel ");
		sql.append("                         on cel.LANCAMENTO_ID = lancto.ID ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     chamada_encalhe ce ");
		sql.append("                         on ce.id = cel.CHAMADA_ENCALHE_ID ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     chamada_encalhe_cota cec ");
		sql.append("                         on cec.CHAMADA_ENCALHE_ID = ce.ID ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     conferencia_encalhe confEnc ");
		sql.append("                         on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID ");
		sql.append("                 LEFT JOIN ");
		sql.append("                     movimento_estoque_cota mecEncalhe ");
		sql.append("                         on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
		sql.append("                 WHERE ");
		sql.append("                     lancto.id = lanc.id) as unsigned int) ");
		sql.append("                 else null ");
		sql.append("         end as venda ");
		sql.append("     from ");
		sql.append("         LANCAMENTO lanc ");
		sql.append("     inner join ");
		sql.append("         PRODUTO_EDICAO pe ");
		sql.append("             on lanc.PRODUTO_EDICAO_ID=pe.ID ");
		sql.append("     inner join ");
		sql.append("         PRODUTO pd ");
		sql.append("             on pe.PRODUTO_ID=pd.ID ");
		sql.append("     left outer join ");
		sql.append("         PERIODO_LANCAMENTO_PARCIAL plp ");
		sql.append("             on lanc.PERIODO_LANCAMENTO_PARCIAL_ID=plp.ID ");
		sql.append("     join ");
		sql.append("         ESTUDO_GERADO eg ");
		sql.append("           on eg.LANCAMENTO_ID = lanc.id ");
		sql.append("     left join ");
		sql.append("         USUARIO usu ");
		sql.append("             on eg.USUARIO_ID=usu.ID ");
		sql.append("     join ");
		sql.append("         tipo_classificacao_produto tcp ");
		sql.append("             On tcp.ID = pe.TIPO_CLASSIFICACAO_PRODUTO_ID ");
		sql.append("     LEFT JOIN ");
		sql.append("         movimento_estoque_cota mecReparte ");
		sql.append("             on mecReparte.LANCAMENTO_ID = lanc.id ");
		sql.append("     LEFT JOIN ");
		// alteracao por motivo de performance 
		// sql.append("         tipo_movimento tipo ");
		// sql.append("             ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
		sql.append(" (select * from  tipo_movimento  ) tipo on  tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
		
		sql.append("     WHERE (tipo.GRUPO_MOVIMENTO_ESTOQUE is null or ");
		sql.append("         tipo.GRUPO_MOVIMENTO_ESTOQUE not in ('ENVIO_ENCALHE')) ");
		
		addWhereClauseList(filtro, sql);
		
		sql.append(" group BY eg.id ");
		
		sql.append(this.ordenarConsultaBuscarProdutos(filtro));
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesProdutoDTO.class));
		
		setParameterBuscarProduto(filtro, query);
		
		query.addScalar("codProduto", StandardBasicTypes.STRING);
		query.addScalar("codigoICD", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("qtdeReparteEstudo", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("periodo", StandardBasicTypes.INTEGER);
		query.addScalar("preco", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("reparteDistribuido", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("percentualAbrangencia", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("tipoClassificacaoProdutoDescricao", StandardBasicTypes.STRING);
		query.addScalar("datalanc", StandardBasicTypes.DATE);
		query.addScalar("dataLcto", StandardBasicTypes.DATE);
		query.addScalar("dataRcto", StandardBasicTypes.DATE);
		query.addScalar("dataAlteracao", StandardBasicTypes.DATE);
		query.addScalar("estudo", StandardBasicTypes.LONG);
		query.addScalar("estudoLiberado", StandardBasicTypes.BOOLEAN);
		query.addScalar("reparteMinimo", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("algoritmo", StandardBasicTypes.STRING);
		query.addScalar("nomeUsuario", StandardBasicTypes.STRING);
		query.addScalar("venda", StandardBasicTypes.BIG_INTEGER);
		
		if (filtro != null){
			configurarPaginacao(filtro, query);
		}

		List<InformacoesProdutoDTO> resultado = query.list();
		
		return resultado;
		
	}

	private void addWhereClauseList(FiltroInformacoesProdutoDTO filtro, StringBuilder sql) {
		
		List<String> whereClauseList = new ArrayList<>();
		
		if(StringUtils.isNotEmpty(filtro.getCodProduto())){
			whereClauseList.add(" pd.codigo_icd = :COD_PRODUTO ");
		}
		
		if(filtro.getNumeroEdicao()!=null){
			whereClauseList.add(" pe.numero_Edicao = :NUMERO_EDICAO ");
		}
		
		if(filtro.getNumeroEstudo()!=null){
			whereClauseList.add(" eg.id = :numero_estudo ");
		}
		
		if(filtro.getIdTipoClassificacaoProd() !=null && filtro.getIdTipoClassificacaoProd() > 0){
			whereClauseList.add(" pe.tipo_classificacao_produto_id = :ID_CLASSIFICACAO ");
		}
		
		if(!whereClauseList.isEmpty()){
			sql.append(" AND ");
		}
		
		sql.append(StringUtils.join(whereClauseList, " AND "));
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
	
	private void setParameterBuscarProduto(FiltroInformacoesProdutoDTO filtro, Query query) {
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
			if(paginacao.getPosicaoInicial() > paginacao.getQtdResultadosTotal()){
				query.setFirstResult(1);
				paginacao.setPaginaAtual(1);
			}else{
				query.setFirstResult(paginacao.getPosicaoInicial());
			}
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
