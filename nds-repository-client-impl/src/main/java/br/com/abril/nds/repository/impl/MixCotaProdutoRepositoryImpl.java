package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RepartePDVRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * 
 * MixCotaProduto
 */

@Repository
public class MixCotaProdutoRepositoryImpl extends AbstractRepositoryModel<MixCotaProduto, Long> implements MixCotaProdutoRepository {

	@Autowired
	private RepartePDVRepository repartePDVRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	public MixCotaProdutoRepositoryImpl() {
		super(MixCotaProduto.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MixCotaDTO> pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		StringBuilder sql = new StringBuilder("");
		
		sql.append("select * from ( ")
        .append(" select ")
		.append(" mix_cota_produto.ID id,  ")
		.append(" mix_cota_produto.usar_icd_estudo usarICDEstudo,  ")
		.append(" produto.codigo as codigoProduto, ")
		.append(" produto.CODIGO_ICD as codigoICD, ")
//		.append(" produto.nome as nomeProduto, ")
		.append(" (select pd.NOME from produto pd where pd.codigo_icd = produto.CODIGO_ICD and pd.NOME is not null order by pd.CODIGO asc limit 1) as nomeProduto, ")
		.append(" DATE_FORMAT(mix_cota_produto.DATAHORA, '%d/%c/%Y') as data, ")
		.append(" DATE_FORMAT(mix_cota_produto.DATAHORA, '%H:%i') as hora, ")
		.append(" mix_cota_produto.DATAHORA as dataHora, ")
		.append(" mix_cota_produto.REPARTE_MAX as reparteMaximo, ")
		.append(" mix_cota_produto.REPARTE_MIN as reparteMinimo, ")  
		.append(" mix_cota_produto.ID_COTA as idCota, ")
		.append(" produto.id as idProduto, ")
		.append(" usuario.login as usuario, ")
		.append(" tipo_classificacao_produto.descricao as classificacaoProduto, ")
		.append(" tipo_classificacao_produto.id as tipoClassificacaoProdutoID ")
		.append(" FROM mix_cota_produto ") 
		.append(" LEFT join tipo_classificacao_produto ON tipo_classificacao_produto.ID = mix_cota_produto.TIPO_CLASSIFICACAO_PRODUTO_ID ")
		.append(" LEFT join usuario on usuario.ID = mix_cota_produto.ID_USUARIO ")
		.append(" , produto ")

		.append(" where ")
		.append(" produto.codigo_icd = mix_cota_produto.CODIGO_ICD and ");
	
		sql.append(" mix_cota_produto.ID_COTA = :cota ");
		
		sql.append(" order by CHAR_LENGTH(produto.nome) asc) temp_mix ");
		
		sql.append(" group by id ");
		
		if (filtroConsultaMixCotaDTO.getPaginacao() != null ){
			
			if (filtroConsultaMixCotaDTO.getPaginacao().getSortColumn().equals("data")) {
				
				sql.append(" order by temp_mix.dataHora");
				
			} else {
				
				if(filtroConsultaMixCotaDTO.getPaginacao().getSortColumn().equalsIgnoreCase("codigoICD")){
					sql.append(" order by lpad(temp_mix." + filtroConsultaMixCotaDTO.getPaginacao().getSortColumn() + ", 6, '0')");    
				}else{
					sql.append(" order by temp_mix." + filtroConsultaMixCotaDTO.getPaginacao().getSortColumn());
				}
				
			}
			
			sql.append(" " + filtroConsultaMixCotaDTO.getPaginacao().getSortOrder());
		} else {
			sql.append(" order by codigoProduto");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		//query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO.toString());
		query.setParameter("cota", filtroConsultaMixCotaDTO.getCotaId());
		query.setResultTransformer(new AliasToBeanResultTransformer(MixCotaDTO.class));
		
		if(filtroConsultaMixCotaDTO.getPaginacao() != null ){			
			configurarPaginacao(filtroConsultaMixCotaDTO, query);
		}
		List<MixCotaDTO> list = (List<MixCotaDTO>)query.list();
		for (MixCotaDTO mixCotaDTO : list) {
			
			List<Long> obterNumeroDas6UltimasEdicoesFechadas = this.produtoEdicaoRepository.obterNumeroDas6UltimasEdicoesFechadasPorICD(mixCotaDTO.getCodigoICD());
			if(obterNumeroDas6UltimasEdicoesFechadas.isEmpty()){
				mixCotaDTO.setReparteMedio(BigDecimal.ZERO);
				mixCotaDTO.setVendaMedia(BigDecimal.ZERO);
				mixCotaDTO.setUltimoReparte(BigDecimal.ZERO);
				continue;
			}
			sql = new StringBuilder("");
			
			sql .append(" select ") 
			.append(" coalesce(round(avg(epc.qtde_recebida), 0),0) as reparteMedio, ")
			.append(" coalesce(round(avg(epc.qtde_recebida - epc.qtde_devolvida), 0),0) as vendaMedia, ")
			.append(" coalesce((select ec.reparte from estudo_cota ec  join estudo et on ec.estudo_id = et.id ")
			.append("  join produto_edicao pe on et.produto_edicao_id = pe.id where ec.cota_id = :idCota ")
			.append("  and pe.produto_id = :idProduto order by ec.id desc limit 1), 0) as ultimoReparte ")
			.append(" from estoque_produto_cota epc ")
			.append(" left join produto_edicao pe ON pe.ID = epc.PRODUTO_EDICAO_ID ")
			.append(" left join lancamento on lancamento.PRODUTO_EDICAO_ID = pe.ID ")
			.append(" where epc.COTA_ID = :idCota  and pe.PRODUTO_ID = :idProduto ")
			.append(" and pe.NUMERO_EDICAO in :numeroEdicaoList ");
			
			
			query = getSession().createSQLQuery(sql.toString());
			query.setParameter("idCota", mixCotaDTO.getIdCota().longValue());
			query.setParameter("idProduto", mixCotaDTO.getIdProduto());
			query.setParameterList("numeroEdicaoList", obterNumeroDas6UltimasEdicoesFechadas.toArray());
			List<Map> list2 = query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).list();
			
			for (Map object : list2) {
				mixCotaDTO.setReparteMedio((BigDecimal)object.get("reparteMedio"));
				mixCotaDTO.setVendaMedia((BigDecimal)object.get("vendaMedia"));
				mixCotaDTO.setUltimoReparte((BigDecimal)object.get("ultimoReparte"));
				
			}
		}
		return list;
	}
	
	
	
	private void configurarPaginacao(FiltroDTO dto,Query query) {

		PaginacaoVO paginacao = dto.getPaginacao();

		if (paginacao!=null && paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if (paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO) {
		boolean isClassificacaoPreenchida = filtroConsultaMixProdutoDTO.getClassificacaoProduto()!=null && filtroConsultaMixProdutoDTO.getClassificacaoProduto()!="";
		StringBuilder sql = new StringBuilder("");

	sql.append(" select ") 
		.append(" mix_cota_produto.ID id,  ")
		.append(" mix_cota_produto.usar_icd_estudo usarICDEstudo,  ")
		.append(" produto.codigo_icd as codigoProduto, ")
		.append(" cota.numero_cota as numeroCota, ")
		.append(" produto.nome as nomeProduto, ")
		.append(" coalesce(if(pessoa.tipo='F',pessoa.nome, pessoa.razao_social), pessoa.nome_fantasia, '')  as nomeCota, ")
		.append(" mix_cota_produto.DATAHORA as dataHora, ")
		.append(" DATE_FORMAT(mix_cota_produto.DATAHORA, '%d/%c/%Y') as data, ")
        .append(" DATE_FORMAT(mix_cota_produto.DATAHORA, '%H:%i') as hora, ")
		.append(" mix_cota_produto.REPARTE_MAX as reparteMaximo, ")
		.append(" mix_cota_produto.REPARTE_MIN as reparteMinimo, ")  
		.append(" mix_cota_produto.ID_COTA as idCota, ")
		.append(" produto.id as idProduto, ")
//		.append(" mix_cota_produto.ID_PRODUTO as idProduto, ")
		.append(" (select count(pdv.id) from pdv where cota.id = pdv.cota_id) as qtdPdv, ") 
		.append(" usuario.login as usuario, ")
		.append(" tipo_classificacao_produto.descricao as classificacaoProduto, ")
		.append(" tipo_classificacao_produto.id as classificacaoProdutoID, ")
		
		.append(" coalesce((select sum(repartepdv0_.REPARTE) as REPARTE604_") 
		.append(" from REPARTE_PDV repartepdv0_  where repartepdv0_.PRODUTO_ID=produto.ID ") 
		.append(" and repartepdv0_.MIX_COTA_PRODUTO_ID = mix_cota_produto.ID ")
		.append(" group by repartepdv0_.MIX_COTA_PRODUTO_ID), 0) as somaPdv ")         
		
		// .append(" sum(reparte_pdv.reparte) as somaPdv ")
//		.append(" round(coalesce(avg(epc.qtde_recebida),0), 0) as reparteMedio, ")
//		.append(" round(coalesce(avg(epc.qtde_recebida - epc.qtde_devolvida),0), 0) as vendaMedia, ")
//		.append(" coalesce((select round(lc.reparte,0) from lancamento lc where lc.produto_edicao_id=produto_edicao.id and lancamento.status in ('LAN�ADA','CALCULADA') limit 1),0) as ultimoReparte ")
		.append(" FROM mix_cota_produto ") 
//		.append(" LEFT join produto on mix_cota_produto.ID_PRODUTO = produto.ID ")
		.append(" LEFT join cota on mix_cota_produto.ID_COTA = cota.ID ")
		
		.append(" LEFT JOIN reparte_pdv on reparte_pdv.MIX_COTA_PRODUTO_ID =  mix_cota_produto.ID_PRODUTO ")
//		.append(" LEFT join produto_edicao on produto_edicao.PRODUTO_ID = produto.ID ")
//		.append(" LEFT join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID")
//		.append(" LEFT join estoque_produto_cota epc on epc.cota_id = cota.id ")
//		.append(" and epc.produto_edicao_id in (select id from produto_edicao where produto_id = (produto.id)) ")
		.append(" LEFT join tipo_classificacao_produto ON tipo_classificacao_produto.ID = mix_cota_produto.TIPO_CLASSIFICACAO_PRODUTO_ID ")
		.append(" LEFT join usuario on usuario.ID = mix_cota_produto.ID_USUARIO ")
		.append(" LEFT join pessoa on cota.pessoa_id = pessoa.id ")
		.append(" , produto ")
		
		.append(" where ");
	
		List<String> l = new ArrayList<String>();
		
		l.add(" produto.codigo_icd=mix_cota_produto.codigo_icd ");
		
		if(filtroConsultaMixProdutoDTO.getCodigoProduto()!=null ){
			l.add(" produto.CODIGO_ICD = :codigoProduto ");
		}
		if(isClassificacaoPreenchida){
			l.add(" upper(tipo_classificacao_produto.descricao) = upper(:classificacaoProduto)");
		}
		
		l.add(" cota.tipo_distribuicao_cota = :tipoCota");
		
		sql.append(StringUtils.join(l," and "))
		.append("  group by cota.numero_cota,mix_cota_produto.codigo_icd,mix_cota_produto.TIPO_CLASSIFICACAO_PRODUTO_ID ");
	
		if (filtroConsultaMixProdutoDTO.getPaginacao() != null) {
			sql.append(" order by ");
		
			if (filtroConsultaMixProdutoDTO.getPaginacao().getSortColumn().equals("data")) {
	            
	            sql.append(" dataHora ");
	            
	        } else {
	            
	            sql.append(filtroConsultaMixProdutoDTO.getPaginacao().getSortColumn());
	        }
			
			sql.append(" " + filtroConsultaMixProdutoDTO.getPaginacao().getSortOrder());
		} else {
			sql.append(" order by ");
			sql.append(" cota.numero_cota ");
		}
		
		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO.toString());
		if(filtroConsultaMixProdutoDTO.getCodigoProduto() !=null && filtroConsultaMixProdutoDTO.getCodigoProduto()!=""){
			query.setParameter("codigoProduto", filtroConsultaMixProdutoDTO.getCodigoProduto());
		}
		if(isClassificacaoPreenchida){
			query.setParameter("classificacaoProduto", filtroConsultaMixProdutoDTO.getClassificacaoProduto());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MixProdutoDTO.class));
		
		if (filtroConsultaMixProdutoDTO.getPaginacao() != null) {
			configurarPaginacao(filtroConsultaMixProdutoDTO, query);
		}
		
		//tratamento para popular reparteMedio, vendaMedia, ultimoReparte
		List<MixProdutoDTO> list = (List<MixProdutoDTO>)query.list();
		for (MixProdutoDTO mixCotaDTO : list) {
			
			List<Long> obterNumeroDas6UltimasEdicoesFechadas = this.produtoEdicaoRepository.obterNumeroDas6UltimasEdicoesFechadasPorICD(mixCotaDTO.getCodigoProduto());
			if(obterNumeroDas6UltimasEdicoesFechadas.isEmpty()){
				mixCotaDTO.setReparteMedio(BigDecimal.ZERO);
				mixCotaDTO.setVendaMedia(BigDecimal.ZERO);
				mixCotaDTO.setUltimoReparte(BigDecimal.ZERO);
				continue;
			}
			sql = new StringBuilder("");
			
			sql.append(" select ") 
			.append(" 	coalesce(round(avg(t.reparte), 0),0) as reparteMedio, ")
			.append(" 	coalesce(round(avg(t.venda), 0),0) as vendaMedia, ")
			.append(" 	t.ultimoReparte ")
			.append("from (")
			.append(" 	select   ")
			.append("	 	cast(sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else ")
			.append("	 		- mecReparte.QTDE end ) as signed int) as reparte,")
			.append(" 	 	(case  when l.status in('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO') then ")
			.append(" 			cast(sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else - mecReparte.QTDE end ) ")
			.append(" 				- if(mecReparte.TIPO_MOVIMENTO_ID = 9, 0, ")
			.append(" 						(select 	  ")
			.append("	 					  	sum( mecEncalhe.qtde )  ")
			.append("	 					  from   ")
			.append("	 					  	lancamento lanc    ")
			.append("	 					  	left join movimento_estoque_cota mecEncalhe   ")
			.append("	 					  		on mecEncalhe.lancamento_id = lanc.id   ")
			.append("	 					  	left join cota cota   ")
			.append("	 					  		on cota.id = mecEncalhe.cota_id   ")
			.append("	 					  	join tipo_movimento tm   ")
			.append("	 					  		on mecEncalhe.tipo_movimento_id = tm.id   ")
			.append("	 					  where   ")
			.append("	 					  	lanc.id = l.id    ")
			.append("	 					   	and cota.id = c.id ")
			.append("	 					  	and tm.tipo_movimento_encalhe is true)  ")
			.append("	 				) as SIGNED int  ")
			.append("	 			)  else null  ")
			.append("	 		end  ")
			.append("	 	) as venda,")
			.append("	 	coalesce((select")
			.append("				cast(sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE ")
			.append("					else - mecReparte.QTDE end ) as unsigned int) as reparte")
			.append("			from")
			.append("				movimento_estoque_cota mecReparte ")
			.append("				join tipo_movimento tipo ")
			.append("					on tipo.id = mecReparte.tipo_movimento_id ")
			.append("				join lancamento l ")
			.append("					on l.id = mecReparte.lancamento_id ")
			.append("				join produto_edicao pe ")
			.append("					on l.PRODUTO_EDICAO_ID = pe.ID join produto p on")
			.append("				pe.PRODUTO_ID = p.id join cota c on")
			.append("				c.id = mecReparte.cota_id")
			.append("			where")
			.append("				tipo.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE'")
			.append("				and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null")
			.append("				and pe.id =(")
			.append("					select")
			.append("						mec.produto_edicao_id")
			.append("					from")
			.append("						movimento_estoque_cota mec join produto_edicao pe on")
			.append("						mec.produto_edicao_id = pe.id join produto pd on")
			.append("						pe.produto_id = :idProduto join tipo_movimento tm on")
			.append("						mec.tipo_movimento_id = tm.id join lancamento lc on")
			.append("						lc.produto_edicao_id = pe.id")
			.append("					where")
			.append("						mec.cota_id = :idCota")
			.append("						and lc.status in(")
			.append("							'EXPEDIDO', 'EM_BALANCEAMENTO_RECOLHIMENTO', ")
			.append("							'BALANCEADO_RECOLHIMENTO', 'EM_RECOLHIMENTO',")
			.append("							'RECOLHIDO', 'FECHADO'")
			.append("							)")
			.append("					group by")
			.append("						pe.numero_edicao,")
			.append("						pe.id,")
			.append("						mec.cota_id")
			.append("					order by")
			.append("						lc.data_lcto_distribuidor desc limit 1")
			.append("				)")
			.append("				and mecReparte.cota_id = :idCota")
			.append("			group by")
			.append("				pe.numero_edicao,")
			.append("				pe.id,")
			.append("				mecReparte.cota_id")
			.append("			order by")
			.append("				l.ID desc")
			.append("		),")
			.append("		0")
			.append("	) as ultimoReparte")
			.append("	 	")
			.append("	 from  ")
			.append("	  	movimento_estoque_cota mecReparte ")
			.append("	  	join tipo_movimento tipo ")
			.append("	  		on tipo.id = mecReparte.tipo_movimento_id ")
			.append("  		join lancamento l ")
			.append("  			on l.id = mecReparte.lancamento_id ")
			.append("		join produto_edicao pe")
			.append("			on l.PRODUTO_EDICAO_ID = pe.ID ")
			.append("		join produto p ")
			.append("			on pe.PRODUTO_ID = p.id")
			.append("		join cota c ")
			.append("			on c.id = mecReparte.cota_id  ")
			.append("	 where   ")
			.append("	 	tipo.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE'  ")
			.append("	 	and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null  ")
			.append("	 	and pe.id in (select id from produto_edicao ped where ped.produto_id = :idProduto and ped.numero_edicao in (:numeroEdicaoList))		")
			.append("	 	and mecReparte.cota_id = :idCota  ")
			.append("	 group by  ")
			.append("	 	pe.numero_edicao,  ")
			.append("	 	pe.id,  ")
			.append("	 	mecReparte.cota_id  ")
			.append("         	 order by  ")
			.append("         	 	l.ID desc) t where t.reparte <> 0 ");

			
//			sql.append(" select ") 
//			.append(" coalesce(round(avg(epc.qtde_recebida), 0),0) as reparteMedio, ")
//			.append(" coalesce(round(avg(epc.qtde_recebida - epc.qtde_devolvida), 0),0) as vendaMedia, ")
//			.append("coalesce((select  cast(sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE ") 
//			.append("			else - mecReparte.QTDE end ) as unsigned int ) as reparte ")
//			.append("	 from  movimento_estoque_cota mecReparte join tipo_movimento tipo on tipo.id = ") 
//			.append("	 	mecReparte.tipo_movimento_id join lancamento l on l.id = mecReparte.lancamento_id ") 
//			.append("		join produto_edicao pe on l.PRODUTO_EDICAO_ID = pe.ID join produto p ")
//			.append("		on pe.PRODUTO_ID = p.id join cota c on c.id = mecReparte.cota_id  ")
//			.append("	 where  tipo.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE'  ")
//			.append("	 	and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null  ")
//			.append("	 	and pe.id = (select mec.produto_edicao_id from movimento_estoque_cota mec ")
//			.append("				 	join produto_edicao pe on mec.produto_edicao_id = pe.id ")
//			.append("			 		join produto pd on pe.produto_id = :idProduto ")
//			.append("					join tipo_movimento tm on mec.tipo_movimento_id = tm.id ")
//			.append("					join lancamento lc on lc.produto_edicao_id = pe.id ")
//			.append("				 	where mec.cota_id = :idCota and lc.status in ('EXPEDIDO', 'EM_BALANCEAMENTO_RECOLHIMENTO', ")
//			.append("					'BALANCEADO_RECOLHIMENTO', 'EM_RECOLHIMENTO', 'RECOLHIDO','FECHADO')  group by  pe.numero_edicao, pe.id, ")  
//			.append("				 	mec.cota_id  order by lc.data_lcto_distribuidor desc limit 1) ")	
//			.append("	 	and mecReparte.cota_id = :idCota  group by  pe.numero_edicao,  pe.id,  mecReparte.cota_id ")  
//			.append("	 order by  l.ID desc), 0 ) as ultimoReparte ") 
//			
////			.append(" coalesce((select ec.reparte from estudo_cota ec  join estudo et on ec.estudo_id = et.id ")
////			.append("  join produto_edicao pe on et.produto_edicao_id = pe.id where ec.cota_id = :idCota ")
////			.append("  and pe.produto_id = :idProduto order by ec.id desc limit 1), 0) as ultimoReparte ")
//			.append(" from estoque_produto_cota epc ")
//			.append(" left join produto_edicao pe ON pe.ID = epc.PRODUTO_EDICAO_ID ")
//			.append(" left join lancamento on lancamento.PRODUTO_EDICAO_ID = pe.ID ")
//			.append(" where epc.COTA_ID = :idCota  and pe.PRODUTO_ID = :idProduto ")
//			.append(" and pe.NUMERO_EDICAO in :numeroEdicaoList ");
			
			query = getSession().createSQLQuery(sql.toString());
			query.setParameter("idCota", mixCotaDTO.getIdCota().longValue());
			query.setParameter("idProduto", mixCotaDTO.getIdProduto());
			query.setParameterList("numeroEdicaoList", obterNumeroDas6UltimasEdicoesFechadas.toArray());
			
			List<Map> list2 = query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).list();
			
			for (Map object : list2) {
				mixCotaDTO.setReparteMedio((BigDecimal)object.get("reparteMedio"));
				mixCotaDTO.setVendaMedia((BigDecimal)object.get("vendaMedia"));
				mixCotaDTO.setUltimoReparte((BigDecimal)object.get("ultimoReparte"));
				
			}
		}
		
		return list;
	}

	@Override
	public boolean existeMixCotaProdutoCadastrado(Long idProduto, Long idCota){
		
		StringBuilder hql = new StringBuilder("select count (m.id) ");

		hql.append(" from MixCotaProduto m, Produto p ")
		   .append(" where m.codigoICD = p.codigoICD  ");
		
		if (idProduto != null){
			
			hql.append(" and m.produto.id = :idProduto ");
		}
		
		if (idCota != null){
			
			hql.append(" and m.cota.id = :idCota ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		if (idProduto != null){
			
			query.setParameter("idProduto", idProduto);
		}
		
		if (idCota != null){
			
			query.setParameter("idCota", idCota);
		}
		
		return (Long) query.uniqueResult() > 0;
	}

	@Override
	public void excluirTodos() {
		List<MixCotaProduto> lista = this.buscarTodos();
		for (MixCotaProduto mixCotaProduto : lista) {
			this.remover(mixCotaProduto);
		}
	}

	public void removerPorIdCota(Long idCota){

		StringBuilder hql = new StringBuilder("");

		hql.append("")
				.append("delete from MixCotaProduto m  ")
				.append(" where  m.cota.id = :idCota ");
				
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		query.executeUpdate();
	}
	
	public void removerProdutoPorCodigoICD(String codigoICD) {

		StringBuilder hql = new StringBuilder("");

		hql.append("delete from MixCotaProduto m where m.codigoICD = :codigoICD ");
				
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("codigoICD", codigoICD);
		
		query.executeUpdate();
	}
	
	@Override
	public void execucaoQuartz() {
		StringBuilder hql = new StringBuilder("");

		hql.append("")
				.append(" delete from mix_cota_produto   ")
				.append(" where mix_cota_produto.codigo_icd in")
				.append(" (select codigo_icd from mix_cota_produto ")
				.append(" join produto on produto.codigo_icd = mix_cota_produto.codigo_icd  ")
				.append(" join produto_edicao on produto_edicao.PRODUTO_ID = produto.ID ")
				.append(" join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID ")
				.append(" where lancamento.DATA_CRIACAO between date_format(DATE_SUB(CURDATE(),INTERVAL 180 DAY),'%d/%m/%Y')  and CURDATE() ");
				
		Query query = getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
		
	}



	@Override
	public void gerarCopiaMixCota(List<MixCotaDTO> mixCotaOrigem,Usuario usuario) {

		
		StringBuilder hql = new StringBuilder("");

		hql.append(" INSERT INTO mix_cota_produto ")
		.append(" (CODIGO_ICD,TIPO_CLASSIFICACAO_PRODUTO_ID,DATAHORA, REPARTE_MAX, REPARTE_MED, REPARTE_MIN, ULTIMO_REPARTE, VENDA_MED, ID_COTA, ID_PRODUTO, ID_USUARIO) VALUES "); 
		
		List<String> insertsList = new ArrayList<String>();
		
		for (MixCotaDTO mixCotaDTO : mixCotaOrigem) {
//			insertsList.add(" (now(), REPARTE_MAX, REPARTE_MED, REPARTE_MIN, ULTIMO_REPARTE, VENDA_MED, ID_COTA, ID_PRODUTO, :idUsuario) ");
			insertsList.add(" ("+mixCotaDTO.getCodigoICD()+","+mixCotaDTO.getTipoClassificacaoProdutoID()+",now(), "+mixCotaDTO.getReparteMaximo()+", "+mixCotaDTO.getReparteMedio()+", "+mixCotaDTO.getReparteMinimo()+"," +
					mixCotaDTO.getUltimoReparte()+", "+mixCotaDTO.getVendaMedia()+", "+mixCotaDTO.getIdCota()+", "+mixCotaDTO.getIdProduto()+", "+usuario.getId()+") ");
		}
				
		hql.append(StringUtils.join(insertsList, ","));
		Query query = getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
		
	}

	@Override
	public void gerarCopiaMixProduto(List<MixProdutoDTO> mixProdutoOrigem,Usuario usuarioLogado) {
		
		for (MixProdutoDTO mixProdutoDTO : mixProdutoOrigem) {
			MixCotaProduto mcp = new MixCotaProduto();
			mcp.setRepartesPDV(new ArrayList<RepartePDV>());
			
			Cota cota= new Cota();
			cota.setId(mixProdutoDTO.getIdCota().longValue());
			mcp.setCota(cota);
			mcp.setDataHora(GregorianCalendar.getInstance().getTime());
			
			Produto produto = new Produto();
			produto.setId(mixProdutoDTO.getIdProduto().longValue());
			
			mcp.setCodigoICD(mixProdutoDTO.getCodigoICD());
			mcp.setReparteMaximo(mixProdutoDTO.getReparteMaximo().longValue());
			mcp.setReparteMedio(mixProdutoDTO.getReparteMedio().longValue());
			mcp.setReparteMinimo(mixProdutoDTO.getReparteMinimo().longValue());
			mcp.setUltimoReparte(mixProdutoDTO.getUltimoReparte().longValue());
			mcp.setVendaMedia(mixProdutoDTO.getVendaMedia().longValue());
			mcp.setUsuario(usuarioLogado);
			TipoClassificacaoProduto tcp = new TipoClassificacaoProduto();
			tcp.setId(mixProdutoDTO.getClassificacaoProdutoID().longValue());
			mcp.setTipoClassificacaoProduto(tcp);
			
			List<RepartePDV> repartePdvFixacaoList = repartePDVRepository.buscarPorIdMix(mixProdutoDTO.getId().longValue());
			if(repartePdvFixacaoList!=null &&  repartePdvFixacaoList.isEmpty()){
				for (RepartePDV repartePDV : repartePdvFixacaoList) {
					RepartePDV newReparte = new RepartePDV();
					newReparte.setMixCotaProduto(mcp);
					
					newReparte.setPdv(repartePDV.getPdv());
					newReparte.setProduto(produto);
					newReparte.setReparte(repartePDV.getReparte());
					mcp.getRepartesPDV().add(newReparte);
				}
				
			}
			adicionar(mcp);
		}
	}
		
	
		@Override
		public MixCotaProduto obterMixPorCotaProduto(Long cotaId, Long classificaoProdutoId, String codICD) {
			return (MixCotaProduto) getSession().createCriteria(MixCotaProduto.class)
			.add(Restrictions.eq("cota.id", cotaId))
			.add(Restrictions.eq("tipoClassificacaoProduto.id", classificaoProdutoId))
			.add(Restrictions.eq("codigoICD", codICD))
			.addOrder(Order.desc("dataHora")) // odemir -- pegar o mais recente
			.setMaxResults(1) 				  //  para evitar problema quando ha duplicacao de informacao
			.uniqueResult();
		}
		
		
		@Override
		public BigInteger obterSomaReparteMinimoPorProdutoUsuario(Long produtoId, Long idUsuario) {
			
			StringBuilder hql = new StringBuilder();
			
			hql.append(" select sum(reparteMinimo) from MixCotaProduto");
			hql.append(" where produto.id = :produtoId");
			hql.append(" and usuario.id = :idUsuario");
			
			Query query = getSession().createQuery(hql.toString());
			
			query.setParameter("produtoId", produtoId);
			query.setParameter("idUsuario", idUsuario);
			
			return (BigInteger)query.uniqueResult();
		}

		@Override
		public MixCotaProduto obterMixPorCotaICDCLassificacao(Long cotaid,	String codigoICD, String classificacaoProduto) {
			StringBuilder hql = new StringBuilder();
			
			hql.append("select mcp from MixCotaProduto mcp left join mcp.cota left join mcp.tipoClassificacaoProduto ")
			.append(" where mcp.cota.id = :cotaid and mcp.codigoICD= :codigoICD and mcp.tipoClassificacaoProduto.descricao= :classificacaoProduto order by dataHora desc ");
			
			Query query = getSession().createQuery(hql.toString());
			
			query.setParameter("cotaid", cotaid);
			query.setParameter("codigoICD", codigoICD);
			query.setParameter("classificacaoProduto", classificacaoProduto);
			query.setMaxResults(1);
	
			
			MixCotaProduto uniqueResult = (MixCotaProduto)query.uniqueResult();
			return uniqueResult;
		}
		
	@Override
	public boolean verificarReparteMinMaxCotaProdutoMix(Integer numeroCota, String codigoProduto, 
	        Long qtd, Long tipoClassificacaoProduto){
	    
	    final String hql = "select case when :qtd between m.reparteMinimo and m.reparteMaximo then true else false end " +
	    		" from MixCotaProduto m, Produto p " +
	    		" join m.cota cota " +
	    		" join m.tipoClassificacaoProduto t " +
	    		" where m.codigoICD = p.codigoICD " +
	    		" and cota.numeroCota = :numeroCota " +
	    		" and p.codigo = :codigoProduto " +
	    		" and t.id = :tipoClassificacaoProduto order by dataHora desc ";
	    
	    final Query query = this.getSession().createQuery(hql);
	    query.setParameter("numeroCota", numeroCota);
	    query.setParameter("codigoProduto", codigoProduto);
	    query.setParameter("qtd", qtd);
		query.setMaxResults(1) 	;			  //  para evitar problema quando ha duplicacao de informacao
	    
	    query.setParameter("tipoClassificacaoProduto", tipoClassificacaoProduto);
	    
	    return query.list().isEmpty() ? false : (boolean) query.uniqueResult();
	}

	@Override
	public void atualizarReparte(boolean isPDVUnico, Long mixID, Long reparte, Usuario usuario, Date data) {

		StringBuilder statement = new StringBuilder();

		statement.append(" update mix_cota_produto ")
				 .append(" set datahora = :data, ");
		
		if (isPDVUnico) {

			statement.append(" reparte_min = :reparte, ")
					 .append(" reparte_max = :reparte, ");
		
		} else {
			
			statement.append(" reparte_min = (case when :reparte < reparte_min then :reparte else reparte_min end), ")
					 .append(" reparte_max = (case when :reparte > reparte_max then :reparte else reparte_max end), ");

		}

		statement.append(" id_usuario = :idUsuario ")
				 .append(" where id = :mixID ");

		SQLQuery query = this.getSession().createSQLQuery(statement.toString());
		
		query.setParameter("reparte", reparte);
		query.setParameter("idUsuario", usuario.getId());
		query.setParameter("mixID", mixID);
		query.setParameter("data", data);
		
		query.executeUpdate();
	}
	
	@Override
	public boolean verificarMixDefinidoPorPDV(Long idMix){
	    
	    final String Sql = " SELECT count(*) FROM reparte_pdv WHERE MIX_COTA_PRODUTO_ID = :id ";
	    
	    final Query query = this.getSession().createSQLQuery(Sql);
	    query.setParameter("id", idMix);
	    
	    final int qtde = ((BigInteger) query.uniqueResult()).intValue();
	    
	    return qtde > 0 ? true : false;
	}
	
	@Override
	public void atualizarFlagUsarICDEstudo(Long idClassificacaoPraAtualizar, String icdProdutoPraAtualizar, String codProdutoPraAtualizar, boolean isUsarICDEstudo, Long idProduto){
		StringBuilder sql = new StringBuilder();
		
		sql.append(" update mix_cota_produto set   ");
		
		if(isUsarICDEstudo){
			sql.append(" codigo_icd =  :icdProdutoPraAtualizar, ");
			sql.append(" codigo_produto =  null, ");
			sql.append(" ID_PRODUTO =  null, ");
	    }else{
	    	sql.append(" codigo_produto =  :codProdutoPraAtualizar, ");
	    	sql.append(" ID_PRODUTO =  :idProdutoPraAtualizar, ");
	    }
		
		sql.append(" usar_icd_estudo = :isUsarICDEstudo ");
		sql.append(" where codigo_icd = :icdProdutoPraAtualizar and tipo_classificacao_produto_id = :idClassificacao ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
	    query.setParameter("idClassificacao", idClassificacaoPraAtualizar);
	    query.setParameter("isUsarICDEstudo", isUsarICDEstudo);
    	query.setParameter("icdProdutoPraAtualizar", icdProdutoPraAtualizar);

	    if(!isUsarICDEstudo){
	    	query.setParameter("codProdutoPraAtualizar", codProdutoPraAtualizar);
	    	query.setParameter("idProdutoPraAtualizar", idProduto);
	    }
	    
	    query.executeUpdate();
		
	}
	
	
}