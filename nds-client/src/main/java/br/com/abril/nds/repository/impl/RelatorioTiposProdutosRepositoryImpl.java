package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.repository.RelatorioTiposProdutosRepository;

@Repository
public class RelatorioTiposProdutosRepositoryImpl extends AbstractRepository implements RelatorioTiposProdutosRepository {

	@Override
	public List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT p.id AS codigo, " +
				"p.descricao AS produto, " +
				"pe.numeroEdicao AS edicao, " +
				"pe.precoVenda AS precoCapa, " +
				"t.descricao AS tipoProduto, " +
				"l.dataRecolhimentoDistribuidor AS recolhimento, " +
				"l.dataLancamentoDistribuidor AS lancamento " +
				"FROM ProdutoEdicao pe " +
				"JOIN pe.produto p " +
				"JOIN p.tipoProduto t " +
				"JOIN pe.lancamentos l");
		
		Query query = getSession().createQuery(sb.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioTiposProdutosDTO.class));
		
		List result = query.list();
		
		return result;
	}
}
