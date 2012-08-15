package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */
public interface DescontoProdutoEdicaoRepository extends Repository<DescontoProdutoEdicao, Long>{

	DescontoProdutoEdicao buscarDescontoProdutoEdicao(Fornecedor fornecedor,Cota cota, ProdutoEdicao produto);

	List<DescontoProdutoEdicao> buscarDescontoProdutoEdicaoNotInTipoDesconto(
			TipoDesconto tipoDesconto, Fornecedor fornecedor);

	List<DescontoProdutoEdicao> buscarDescontoProdutoEdicaoNotInTipoDesconto(
			TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota);
}
