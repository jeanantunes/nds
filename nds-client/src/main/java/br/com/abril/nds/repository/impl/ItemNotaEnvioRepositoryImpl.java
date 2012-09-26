package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;

@Repository
public class ItemNotaEnvioRepositoryImpl extends AbstractRepositoryModel<ItemNotaEnvio, ItemNotaEnvioPK> implements ItemNotaEnvioRepository {

	public ItemNotaEnvioRepositoryImpl() {
		super(ItemNotaEnvio.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheItemNotaFiscalDTO> obterItensNotaEnvio(Date dataEmissao, Integer numeroCota) {
		
		String hql = "select produto.codigo as codigoProduto, produto.nome as nomeProduto, "
				   + " produtoEdicao.numeroEdicao as numeroEdicao, produtoEdicao.precoVenda as precoVenda, "
				   + " sum(itemNotaEnvio.reparte) as quantidadeExemplares, produtoEdicao.id as idProdutoEdicao "
				   + " from ItemNotaEnvio itemNotaEnvio "
				   + " join itemNotaEnvio.itemNotaEnvioPK.notaEnvio notaEnvio "
				   + " join itemNotaEnvio.listaMovimentoEstoqueCota movimentoEstoqueCota "
				   + " join itemNotaEnvio.produtoEdicao produtoEdicao "
				   + " join produtoEdicao.produto produto "
				   + " where notaEnvio.dataEmissao = :dataEmissao "
				   + " and movimentoEstoqueCota.cota.numeroCota = :numeroCota "
				   + " group by produtoEdicao.id ";
		
		Query query = super.getSession().createQuery(hql);
		
		ResultTransformer resultTransformer =
			new AliasToBeanResultTransformer(DetalheItemNotaFiscalDTO.class); 

		query.setParameter("dataEmissao", dataEmissao);
		query.setParameter("numeroCota", numeroCota);
		
		query.setResultTransformer(resultTransformer);
		
		return query.list();
	}
	
}
