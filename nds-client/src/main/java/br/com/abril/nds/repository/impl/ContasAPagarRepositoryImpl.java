package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.repository.ContasAPagarRepository;

@Repository
public class ContasAPagarRepositoryImpl extends AbstractRepository implements ContasAPagarRepository{

	@SuppressWarnings("unchecked")
	@Override
	public List<ContasAPagarConsultaProdutoDTO> pesquisaProdutoContasAPagar(
			String codigoProduto, Long edicao) {
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT pe.precoVenda as precoCapa");
		sql.append("      ,pe.numeroEdicao as edicao ");
		sql.append("      ,pe.id as produtoEdicaoID ");
		sql.append("      ,p.codigo as codigo");
		sql.append("      ,p.nome as produto");
		sql.append("      ,pj.nomeFantasia as editor");
		sql.append("      ,j.nomeFantasia as fornecedor");

		sql.append("  FROM ProdutoEdicao as pe");
		sql.append("  		JOIN pe.produto as p");
		sql.append(" 	    JOIN p.fornecedores as f");
		sql.append("  		JOIN f.juridica  as j" );
		sql.append("  		JOIN p.editor  as e" );
		sql.append("  		JOIN e.pessoaJuridica  as pj" );
		sql.append("  WHERE p.codigo = :codigoProduto");
		sql.append("  AND   pe.produto = p");
		if(edicao!=null)
		sql.append("  AND   pe.numeroEdicao = :edicao");
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("codigoProduto", codigoProduto);
		if(edicao!=null)
			query.setParameter("edicao", edicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContasAPagarConsultaProdutoDTO.class));
		
		return query.list();
	}


	@Override
	public List<ContasApagarConsultaPorProdutoDTO> pesquisaContasAPagarPorProduto(
			FiltroContasAPagarDTO dto) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT l.dataRecolhimentoPrevista as Rctl");
		sql.append("	   p.codigo as Codigo");
		sql.append("	   p.nome as Produto");
		sql.append("	   pe.numeroEdicao as Edicao");
		sql.append("	   pe.parcial as Tipo");
		sql.append("	   l.reparte as Reparte");
		sql.append("	   ep.qtdeSuplementar as Suplementacao");
		sql.append("	   ep.qtdeDevolucaoEncalhe as Encalhe");
		sql.append("	   SUM(d.qtde) as FaltasSobras");
		sql.append("	   null as DebitosCreditos");
		
		sql.append("FROM	");
		sql.append("	   MovimentoEstoque me, lancamento l, Diferenca d ");
		sql.append("	   JOIN me.estoqueProduto as ep");
		sql.append("	   JOIN me.produtoEdicao as me_pe");
		sql.append("	   JOIN pe.produto as p");
		sql.append("	   JOIN l.produtoEdicao as l_pe");
		sql.append("	   JOIN d.produtoEdicao as d_pe");
		
		sql.append("WHERE 	");
		sql.append("	   p.codigo as Codigo");

			return null;
	}
}