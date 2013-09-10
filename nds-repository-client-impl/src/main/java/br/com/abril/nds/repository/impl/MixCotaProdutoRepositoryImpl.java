
package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
public class MixCotaProdutoRepositoryImpl extends
		AbstractRepositoryModel<MixCotaProduto, Long> implements
		MixCotaProdutoRepository {

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
		
		sql.append(" select ") 
		.append(" mix_cota_produto.ID id,  ")
		.append(" produto.codigo as codigoProduto, ")
		.append(" produto.CODIGO_ICD as codigoICD, ")
		.append(" produto.nome as nomeProduto, ")
		.append(" mix_cota_produto.DATAHORA as dataHora, ")
		.append(" mix_cota_produto.REPARTE_MAX as reparteMaximo, ")
		.append(" mix_cota_produto.REPARTE_MIN as reparteMinimo, ")  
		.append(" mix_cota_produto.ID_COTA as idCota, ")
		.append(" mix_cota_produto.ID_PRODUTO as idProduto, ")
		.append(" usuario.login as usuario, ")
		.append(" tipo_classificacao_produto.descricao as classificacaoProduto ")
//		.append(" round(avg(epc.qtde_recebida), 0) as reparteMedio, ")
//		.append(" round(avg(epc.qtde_recebida - epc.qtde_devolvida), 0) as vendaMedia, ")
//		.append(" coalesce((select round(lc.reparte,0) from lancamento lc where lc.produto_edicao_id=produto_edicao.id and lancamento.status in ('LANÇADA','CALCULADA') limit 1),0) as ultimoReparte ")
		.append(" FROM mix_cota_produto ") 
		.append(" LEFT join produto on mix_cota_produto.ID_PRODUTO = produto.ID ")
//		.append(" LEFT join produto_edicao on produto_edicao.PRODUTO_ID = produto.ID ") 
//		.append(" LEFT join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID")
//		.append(" LEFT join estoque_produto_cota epc on epc.cota_id = mix_cota_produto.ID_COTA ")
		.append(" LEFT join tipo_classificacao_produto ON tipo_classificacao_produto.ID = produto.TIPO_CLASSIFICACAO_PRODUTO_ID ")
		.append(" LEFT join usuario on usuario.ID = mix_cota_produto.ID_USUARIO ")

		.append(" where ");
		if(filtroConsultaMixCotaDTO.getProdutoId()!=null ){
			sql.append(" produto.CODIGO = :idProduto ");
			
		}else{
			
			sql.append(" mix_cota_produto.ID_COTA = :cota ");
		}
		
//		sql.append(" and lancamento.status='FECHADO'");
//		   .append(" and epc.produto_edicao_id in (")
//		   .append("      select produto_edicao.id from produto_edicao") 
//		   .append(" 		join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID")
//		   .append(" 		where lancamento.status='FECHADO'")
//		   .append(" 		and produto_id = (produto.id)")
//		   .append(" )")
		
		   sql.append(" group by produto.codigo ")
		   .append(" order by produto.codigo ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		//query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO.toString());
		query.setParameter("cota", filtroConsultaMixCotaDTO.getCotaId());
		query.setResultTransformer(new AliasToBeanResultTransformer(MixCotaDTO.class));
		
		configurarPaginacao(filtroConsultaMixCotaDTO, query);
		List<MixCotaDTO> list = (List<MixCotaDTO>)query.list();
		for (MixCotaDTO mixCotaDTO : list) {
			
			List<Long> obterNumeroDas6UltimasEdicoesFechadas = this.produtoEdicaoRepository.obterNumeroDas6UltimasEdicoesFechadas(mixCotaDTO.getIdProduto().longValue());
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
			.append(" coalesce((select round(lc.reparte,0) from lancamento lc where lc.produto_edicao_id=pe.id and lancamento.status in ('LANÇADA','CALCULADA') limit 1),0) as ultimoReparte ")
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

		/*if (paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}*/

		/*if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}*/
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO) {
		boolean isClassificacaoPreenchida = filtroConsultaMixProdutoDTO.getClassificacaoProduto()!=null && filtroConsultaMixProdutoDTO.getClassificacaoProduto()!="";
		StringBuilder sql = new StringBuilder("");

	sql.append(" select ") 
		.append(" mix_cota_produto.ID id,  ")
		.append(" produto.codigo as codigoProduto, ")
		.append(" cota.numero_cota as numeroCota, ")
		.append(" produto.nome as nomeProduto, ")
		.append(" coalesce(pessoa.nome_fantasia, pessoa.razao_social, pessoa.nome, '')  as nomeCota, ")
		.append(" mix_cota_produto.DATAHORA as dataHora, ")
		.append(" mix_cota_produto.REPARTE_MAX as reparteMaximo, ")
		.append(" mix_cota_produto.REPARTE_MIN as reparteMinimo, ")  
		.append(" mix_cota_produto.ID_COTA as idCota, ")
		.append(" mix_cota_produto.ID_PRODUTO as idProduto, ")
		.append(" (select count(pdv.id) from pdv where cota.id = pdv.cota_id) as qtdPdv, ") 
		.append(" usuario.login as usuario, ")
		.append(" tipo_classificacao_produto.descricao as classificacaoProduto, ")
		.append(" round(coalesce(avg(epc.qtde_recebida),0), 0) as reparteMedio, ")
		.append(" round(coalesce(avg(epc.qtde_recebida - epc.qtde_devolvida),0), 0) as vendaMedia, ")
		.append(" coalesce((select round(lc.reparte,0) from lancamento lc where lc.produto_edicao_id=produto_edicao.id and lancamento.status in ('LAN�ADA','CALCULADA') limit 1),0) as ultimoReparte ")
		.append(" FROM mix_cota_produto ") 
		.append(" LEFT join produto on mix_cota_produto.ID_PRODUTO = produto.ID ")
		.append(" LEFT join produto_edicao on produto_edicao.PRODUTO_ID = produto.ID ") 
		.append(" LEFT join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID")
		.append(" LEFT join cota on mix_cota_produto.ID_COTA = cota.ID ")
		.append(" LEFT join estoque_produto_cota epc on epc.cota_id = cota.id ")
		.append(" and epc.produto_edicao_id in (select id from produto_edicao where produto_id = (produto.id)) ")
		.append(" LEFT join tipo_classificacao_produto ON tipo_classificacao_produto.ID = produto.TIPO_CLASSIFICACAO_PRODUTO_ID ")
		.append(" LEFT join usuario on usuario.ID = mix_cota_produto.ID_USUARIO ")
		.append(" LEFT join pessoa on cota.pessoa_id = pessoa.id ")
		
		.append(" where ")
		.append(" lancamento.status='FECHADO' ");
		if(filtroConsultaMixProdutoDTO.getCodigoProduto()!=null ){
			sql.append(" and produto.CODIGO = :codigoProduto ");
		}
		if(isClassificacaoPreenchida){
			sql.append(" and upper(tipo_classificacao_produto.descricao) = upper(:classificacaoProduto)");
		}
		sql.append(" and cota.tipo_distribuicao_cota = :tipoCota")
		.append(" group by cota.numero_cota ")
		.append(" order by cota.numero_cota ");
	
		
		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO.toString());
		if(filtroConsultaMixProdutoDTO.getCodigoProduto() !=null && filtroConsultaMixProdutoDTO.getCodigoProduto()!=""){
			query.setParameter("codigoProduto", filtroConsultaMixProdutoDTO.getCodigoProduto());
		}
		if(isClassificacaoPreenchida){
			query.setParameter("classificacaoProduto", filtroConsultaMixProdutoDTO.getClassificacaoProduto());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MixProdutoDTO.class));
		configurarPaginacao(filtroConsultaMixProdutoDTO, query);
		
		return query.list();
	}

	public boolean existeMixCotaProdutoCadastrado(Long idProduto, Long idCota){
		StringBuilder hql = new StringBuilder("");

		hql.append("")
				.append(" from MixCotaProduto m  ")
				.append(" where m.produto.id = :idProduto ")
				.append(" and m.cota.id = :idCota ");
				
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProduto", idProduto);
		query.setParameter("idCota", idCota);
		return query.list().size() >0;
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
		
	}
	
	@Override
	public void execucaoQuartz() {
		StringBuilder hql = new StringBuilder("");

		hql.append("")
				.append(" delete from mix_cota_produto   ")
				.append(" where mix_cota_produto.ID_PRODUTO in")
				.append(" (select  id_produto from mix_cota_produto ")
				.append(" join produto on produto.ID = mix_cota_produto.ID_PRODUTO  ")
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
		.append(" (DATAHORA, REPARTE_MAX, REPARTE_MED, REPARTE_MIN, ULTIMO_REPARTE, VENDA_MED, ID_COTA, ID_PRODUTO, ID_USUARIO) VALUES "); 
		
		List<String> insertsList = new ArrayList<String>();
		
		for (MixCotaDTO mixCotaDTO : mixCotaOrigem) {
//			insertsList.add(" (now(), REPARTE_MAX, REPARTE_MED, REPARTE_MIN, ULTIMO_REPARTE, VENDA_MED, ID_COTA, ID_PRODUTO, :idUsuario) ");
			insertsList.add(" (now(), "+mixCotaDTO.getReparteMaximo()+", "+mixCotaDTO.getReparteMedio()+", "+mixCotaDTO.getReparteMinimo()+"," +
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
			mcp.setProduto(produto);
			mcp.setReparteMaximo(mixProdutoDTO.getReparteMaximo().longValue());
			mcp.setReparteMedio(mixProdutoDTO.getReparteMedio().longValue());
			mcp.setReparteMinimo(mixProdutoDTO.getReparteMinimo().longValue());
			mcp.setUltimoReparte(mixProdutoDTO.getUltimoReparte().longValue());
			mcp.setVendaMedia(mixProdutoDTO.getVendaMedia().longValue());
			mcp.setUsuario(usuarioLogado);
			
			List<RepartePDV> repartePdvFixacaoList = repartePDVRepository.buscarPorIdMix(mixProdutoDTO.getId().longValue());
			for (RepartePDV repartePDV : repartePdvFixacaoList) {
				RepartePDV newReparte = new RepartePDV();
				newReparte.setMixCotaProduto(mcp);
				
				newReparte.setPdv(repartePDV.getPdv());
				newReparte.setProduto(produto);
				newReparte.setReparte(repartePDV.getReparte());
				mcp.getRepartesPDV().add(newReparte);
			}
			adicionar(mcp);
		}
	}
		
	
		@Override
		public MixCotaProduto obterMixPorCotaProduto(Long cotaId, Long produtoId) {
			return (MixCotaProduto) getSession().createCriteria(MixCotaProduto.class)
			.add(Restrictions.eq("cota.id", cotaId))
			.add(Restrictions.eq("produto.id", produtoId))
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
}
