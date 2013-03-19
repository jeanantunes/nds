package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.InformacoesBaseProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
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

		hql.append(" prodEdicao.reparteDistribuido AS venda, "); // DADO INCONSISTENTE...
		hql.append(" prodEdicao.reparteDistribuido AS reparteMinimoGhoma, "); // DADO INCONSISTENTE...
		
		hql.append(" algortm.descricao AS algoritmo, "); 
		hql.append(" estudoG.id AS estudo "); 

		hql.append(" FROM ProdutoEdicao AS prodEdicao ");
		
		hql.append(" left join prodEdicao.produto AS produto ");
		hql.append(" left join prodEdicao.lancamentos AS lancamento ");
		hql.append(" left join produto.algoritmo AS algortm ");
		hql.append(" INNER join lancamento.estudo AS estudoG ");
		
//		hql.append(" group by prodEdicao.numeroEdicao ");

		hql.append(" WHERE produto.codigo = :COD_PRODUTO ");
		hql.append(" OR produto.nome = :NOME_PRODUTO ");
		
		hql.append(" ORDER BY numeroEdicao ");
		
		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("COD_PRODUTO", filtro.getCodProduto());
		query.setParameter("NOME_PRODUTO", filtro.getNomeProduto());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesProdutoDTO.class));
		
		if (filtro != null){
			configurarPaginacao(filtro, query);
		}
		
		return query.list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<InformacoesBaseProdDTO> buscarBase(String codProduto) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" produto.codigo AS codProduto, ");
		hql.append(" produto.nome AS nomeProduto, ");
		hql.append(" prodEdicao.numeroEdicao AS numeroEdicao, ");
		hql.append(" produto.peso AS peso ");

		hql.append(" FROM ProdutoEdicao AS prodEdicao ");
		hql.append(" left join prodEdicao.produto AS produto ");
		hql.append(" WHERE produto.codigo = :COD_PRODUTO ");
		
		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("COD_PRODUTO", codProduto);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesBaseProdDTO.class));
		
		return query.list();
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
