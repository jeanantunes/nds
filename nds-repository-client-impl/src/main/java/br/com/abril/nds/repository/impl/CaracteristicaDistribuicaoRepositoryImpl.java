package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDetalheDTO;
import br.com.abril.nds.repository.CaracteristicaDistribuicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class CaracteristicaDistribuicaoRepositoryImpl   implements
		CaracteristicaDistribuicaoRepository {
	
	@Autowired
	private SessionFactory sessionFactory;

	
	protected Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			
		}
		
		return sessionFactory.openSession();
	}
	
	private void configurarPaginacaoPesquisaSimples(FiltroConsultaCaracteristicaDistribuicaoDTO dto, Query query) {

		  PaginacaoVO paginacao = dto.getPaginacao();
		  
		  if(paginacao!=null){
			  if (paginacao.getQtdResultadosTotal().equals(0)) {
			   paginacao.setQtdResultadosTotal(query.list().size());
			  }
		  }

		  if(paginacao.getQtdResultadosPorPagina() != null) {
		   query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		  }

		  if (paginacao.getPosicaoInicial() != null) {
		   query.setFirstResult(paginacao.getPosicaoInicial());
		  }
	 }

	@Override
	public List<CaracteristicaDistribuicaoDTO> obterCaracteristicaDistribuicaoDetalhe(
			FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro) {
		
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct ") 
		.append(" pro.codigo as 'codigoProduto', ")
		.append(" pro.nome as 'nomeProduto', ")
		.append(" pes2.NOME_FANTASIA as 'nomeEditor', ")
		.append(" ped.CHAMADA_CAPA as 'chamadaCapa', ")
		.append(" ped.NUMERO_EDICAO as 'numeroEdicao', ")
		.append(" tipoclas.descricao as 'classificacao', ")
		.append(" ped.PRECO_VENDA as 'precoCapa', ")
		.append(" est.QTDE as 'venda', ")
		.append(" lan.REPARTE as 'reparte', ")
		.append(" lan.DATA_LCTO_PREVISTA as 'dataLancamento', ")
		.append(" lan.DATA_REC_PREVISTA as 'dataRecolhimento', ")
		.append(" tiposeg.DESCRICAO as 'segmento' ")

		.append(" from produto pro ")
		.append(" left join produto_edicao ped on pro.ID = ped.PRODUTO_ID ")
		.append(" left join tipo_segmento_produto tiposeg ON tiposeg.ID = pro.TIPO_SEGMENTO_PRODUTO_ID ")
		.append(" left join tipo_classificacao_produto tipoclas ON tipoclas.ID = pro.tipo_classificacao_produto_id ")
		.append(" left join brinde bri ON bri.ID = ped.BRINDE_ID  ")
		.append(" left join editor edi on edi.id = pro.editor_id ")
		.append(" left join pessoa pes2 on pes2.id = edi.JURIDICA_ID ")
		.append(" left join lancamento lan on lan.ID = ped.ID  ")
		.append(" left join estoque_produto est on est.PRODUTO_EDICAO_ID = ped.ID  ")
		.append(" where  1=1 ");
		if(filtro.getCodigoProduto() !=null && filtro.getCodigoProduto() != ""){
			sql.append(" and pro.codigo = " ).append(filtro.getCodigoProduto());
		}
		if(filtro.getClassificacaoProduto()!=null && filtro.getClassificacaoProduto()!=""){
			sql.append(" and upper(tipoclas.descricao) = upper('").append(filtro.getClassificacaoProduto()).append("')");
		}
		if(filtro.getSegmento()!=null && filtro.getSegmento()!=""){
	    sql.append(" and upper(tiposeg.DESCRICAO) = upper('").append(filtro.getSegmento()).append("')");
		}
		if(filtro.getBrinde()!=null && filtro.getBrinde()!=""){
			sql.append(" and upper(bri.DESCRICAO_BRINDE) = upper('").append(filtro.getBrinde()).append("')");
		}
		if(filtro.getFaixaPrecoDe()!=null && filtro.getFaixaPrecoDe()!=""){
			sql.append(" and ped.PRECO_VENDA >=" ).append(filtro.getFaixaPrecoDe()).append("");
		}
		if(filtro.getFaixaPrecoAte()!=null && filtro.getFaixaPrecoAte()!=""){
			sql.append(" and ped.PRECO_VENDA <=" ).append(filtro.getFaixaPrecoAte()).append("");
		}
		
		
		//tipo pesquisa publicacao
		if(filtro.getNomeProduto() !=null && filtro.getNomeProduto()!=""){
				if(filtro.getOpcaoFiltroPublicacao()){
					//exato
					sql.append(" and upper(pro.nome) = ").append(" upper ('").append(filtro.getNomeProduto()).append("')");//exato
				}else{
					//contem
					sql.append(" and upper(pro.nome) like ").append(" upper ('%").append(filtro.getNomeProduto()).append("%')");//contem
				}
				
		}	
		//tipo pesquisa editor
		if(filtro.getNomeEditor()!=null && filtro.getNomeEditor()!="")	{	
				if(filtro.getOpcaoFiltroEditor()){
					//exato
					sql.append(" and upper(pes2.NOME_FANTASIA) =").append(" upper('").append(filtro.getNomeEditor()).append("')");
				}else{
					//contem
					sql.append(" and upper(pes2.NOME_FANTASIA) like").append(" upper('%").append(filtro.getNomeEditor()).append("%')");
				}
			
		}		
		
		//chamada de capa
		if(filtro.getChamadaCapa()!=null && filtro.getChamadaCapa()!=""){
				if(filtro.getOpcaoFiltroChamadaCapa()){
					//exato
					sql.append(" and upper(ped.CHAMADA_CAPA) =").append(" upper('").append(filtro.getChamadaCapa()).append("')");
				}else{
					//contem
					sql.append(" and upper(ped.CHAMADA_CAPA) like").append(" upper('%").append(filtro.getChamadaCapa()).append("%')");
				}
				
		}
				sql.append(" order by pro.nome ASC, ped.NUMERO_EDICAO DESC ");
				Query  query = getSession().createSQLQuery(sql.toString()); 
				 query.setResultTransformer(new AliasToBeanResultTransformer(CaracteristicaDistribuicaoDTO.class));
				 configurarPaginacaoPesquisaDetalhe(filtro,query);
				return query.list();
	}

	private void configurarPaginacaoPesquisaDetalhe(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO dto,Query query) {
		 PaginacaoVO paginacao = dto.getPaginacao();
		  
		  if(paginacao!=null){
			  if (paginacao.getQtdResultadosTotal().equals(0)) {
			   paginacao.setQtdResultadosTotal(query.list().size());
			  }
		  }

		  if(paginacao.getQtdResultadosPorPagina() != null) {
		   query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		  }

		  if (paginacao.getPosicaoInicial() != null) {
		   query.setFirstResult(paginacao.getPosicaoInicial());
		  }
		
	}

}
