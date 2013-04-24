
package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
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

	public MixCotaProdutoRepositoryImpl() {
		super(MixCotaProduto.class);
	}

		

	@SuppressWarnings("unchecked")
	@Override
	public List<MixCotaDTO> pesquisarPorCota(
			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		StringBuilder sql = new StringBuilder("");
		
		
		sql.append(" select ") 
		.append(" mix_cota_produto.ID id,  ")
		.append(" produto.codigo as codigoProduto, ")
		.append(" produto.nome as nomeProduto, ")
		.append(" mix_cota_produto.DATAHORA as dataHora, ")
		.append(" mix_cota_produto.REPARTE_MAX as reparteMaximo, ")
		.append(" mix_cota_produto.REPARTE_MIN as reparteMinimo, ")  
		.append(" mix_cota_produto.ID_COTA as idCota, ")
		.append(" mix_cota_produto.ID_PRODUTO as idProduto, ")
		.append(" (select count(pdv.id) from pdv where cota.id = pdv.cota_id) as qtdPdv, ") 
		.append(" usuario.login as usuario, ")
		.append(" tipo_classificacao_produto.descricao as classificacaoProduto, ")
		.append(" avg(lancamento.reparte) as reparteMedio, avg(venda_produto.valor_total_venda) as vendaMedia, ")
		.append(" coalesce((select lc.reparte from lancamento lc where lc.produto_edicao_id=produto_edicao.id and lancamento.status in ('LANÇADA','CALCULADA') limit 1),0) as ultimoReparte ")
		
		.append(" FROM mix_cota_produto ") 
		.append(" LEFT join produto on mix_cota_produto.ID_PRODUTO = produto.ID ")
		.append(" LEFT join produto_edicao on produto_edicao.PRODUTO_ID = produto.ID ") 
		.append(" LEFT join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID")
		.append(" LEFT join venda_produto on venda_produto.ID_PRODUTO_EDICAO = produto_edicao.ID ")
		.append(" LEFT join cota on mix_cota_produto.ID_COTA = cota.ID ")
		.append(" LEFT join tipo_classificacao_produto ON tipo_classificacao_produto.ID = produto.TIPO_CLASSIFICACAO_PRODUTO_ID ")
		.append(" LEFT join usuario on usuario.ID = mix_cota_produto.ID_USUARIO ")

		.append("where");
		if(filtroConsultaMixCotaDTO.getProdutoId()!=null ){
			sql.append("produto.CODIGO = :idProduto ");
			
		}else{
			
			sql.append(" cota.numero_cota= :cota ");
		}
		sql.append(" and lancamento.status='FECHADO'")
		.append(" and cota.tipo_distribuicao_cota = :tipoCota")
		.append(" group by produto.codigo ")
		.append(" order by lancamento.DATA_LCTO_DISTRIBUIDOR DESC limit 6");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO.toString());
		query.setParameter("cota", filtroConsultaMixCotaDTO.getCota());
		query.setResultTransformer(new AliasToBeanResultTransformer(MixCotaDTO.class));
		
		configurarPaginacaoCota(filtroConsultaMixCotaDTO, query);
		return query.list();
	}
	
	
	
	private void configurarPaginacaoCota(FiltroConsultaMixPorCotaDTO dto,
			Query query) {

		PaginacaoVO paginacao = dto.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
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
		.append(" coalesce(avg(lancamento.reparte),0) as reparteMedio, coalesce(avg(venda_produto.valor_total_venda),0) as vendaMedia, ")
		.append(" coalesce((select lc.reparte from lancamento lc where lc.produto_edicao_id=produto_edicao.id and lancamento.status in ('LAN�ADA','CALCULADA') limit 1),0) as ultimoReparte ")
		.append(" FROM lancamento ") 
		.append(" LEFT join produto_edicao on produto_edicao.ID = lancamento.PRODUTO_EDICAO_ID ")
		.append(" LEFT join produto on produto_edicao.ID = produto.ID ") 
		.append(" LEFT join venda_produto on venda_produto.ID_PRODUTO_EDICAO = produto_edicao.ID ")
		.append(" LEFT join mix_cota_produto on mix_cota_produto.ID_PRODUTO = produto.ID ")
		.append(" LEFT join cota on mix_cota_produto.ID_COTA = cota.ID ")
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
		.append(" order by lancamento.DATA_LCTO_DISTRIBUIDOR DESC limit 6");
	
		
		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO.toString());
		if(filtroConsultaMixProdutoDTO.getCodigoProduto() !=null && filtroConsultaMixProdutoDTO.getCodigoProduto()!=""){
			query.setParameter("codigoProduto", filtroConsultaMixProdutoDTO.getCodigoProduto());
		}
		if(isClassificacaoPreenchida){
			query.setParameter("classificacaoProduto", filtroConsultaMixProdutoDTO.getClassificacaoProduto());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MixProdutoDTO.class));
		configurarPaginacaoProduto(filtroConsultaMixProdutoDTO, query);
		
		return query.list();
	}

	private void configurarPaginacaoProduto(FiltroConsultaMixPorProdutoDTO dto,
			Query query) {

		PaginacaoVO paginacao = dto.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		/*if (paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}*/

		/*if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}*/
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

}
