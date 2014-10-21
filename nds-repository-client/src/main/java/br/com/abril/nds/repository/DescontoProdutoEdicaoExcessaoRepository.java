package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TipoDescontoEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoEditorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */
public interface DescontoProdutoEdicaoExcessaoRepository extends Repository<DescontoCotaProdutoExcessao, Long>{

	/**
	 * Obtém descontos de produto edição diferentes do tipo de desconto informado.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @return {@link List} {@link DescontoCotaProdutoExcessao}
	 */
	List<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor);
	
	/**
	 * Obtém descontos de produto edição diferentes do tipo de desconto informado.
	 * 
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @return {@link List} {@link DescontoCotaProdutoExcessao}
	 */
	List<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota);
	
	/**
	 * Obtém o desconto de um produto edição.
	 * @param desconto TODO
	 * @param fornecedor - fornecedor
	 * @param editor TODO
	 * @param cota - cota
	 * @param produto TODO
	 * @param produtoEdicao - produto
	 * @return {@link DescontoCotaProdutoExcessao}
	 */
	DescontoCotaProdutoExcessao buscarDescontoCotaProdutoExcessao(TipoDesconto tipoDesconto, Desconto desconto, Fornecedor fornecedor, Editor editor, Cota cota, Long idProduto, Long idProdutoEdicao);
	
	DescontoCotaProdutoExcessao buscarDescontoCotaEditorExcessao(TipoDesconto tipoDesconto, Desconto desconto, Cota cota, Editor editor);
	
	/**
	 * Obtém descontos de produtos edição de um fornecedor.
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @return {@link Set} de {@link DescontoCotaProdutoExcessao}
	 */
	Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor);
	
	/**
	 * Obtém descontos de produtos edição de um fornecedor e uma cota.
	 * 
	 * @param fornecedor - fornecedor
	 * @param cota - cota
	 * 
	 * @return {@link Set} de {@link DescontoCotaProdutoExcessao}
	 */
	Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota);
	
	/**
	 * Obtém descontos de produtos edição de uma cota.
	 * 
	 * @param cota - cota
	 * 
	 * @return {@link Set} de {@link DescontoCotaProdutoExcessao}
	 */
	Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Cota cota);
	
	/**
	 * Obtém descontos do produto edição.
	 * 
	 * @param produtoEdicao - produto edição
	 * 
	 * @return {@link Set} de {@link DescontoCotaProdutoExcessao}
	 */
	Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao);
	
	/**
	 * 
	 * Obtém descontos de produtos edição de fornecedor, cota e tipo de desconto.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @return {@link Set} {@link DescontoCotaProdutoExcessao}
	 */
	Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota);
	
	/**
	 * 
	 * Obtém descontos de produtos edição de fornecedor, cota e tipo de desconto.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param desconto - desconto
	 * 
	 * @param fornecedor - fornecedor
	 * 
	 * @param cota - cota
	 * 
	 * @param produtoEdicao - produto edição
	 * 
	 * @return {@link Set} {@link DescontoCotaProdutoExcessao}
	 */
	Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Desconto desconto, Fornecedor fornecedor, Cota cota, ProdutoEdicao produtoEdicao);
		
	/**
	 * 
	 * Obtém descontos de produtos edição de editor, cota e tipo de desconto.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * 
	 * @param desconto - desconto
	 * 
	 * @param editor - editor
	 * 
	 * @param cota - cota
	 * 
	 * @param produtoEdicao - produto edição
	 * 
	 * @return {@link Set} {@link DescontoCotaProdutoExcessao}
	 */
	Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Desconto desconto, Editor editor, Cota cota);
	
	/**
	 * Salva uma lista de descontosProdutoEdicao
	 * @param lista
	 */
	public void salvarListaDescontoProdutoEdicaoExcessao(List<DescontoCotaProdutoExcessao> lista);

	/**
	 * @param filtro
	 * @return
	 */
	List<TipoDescontoEditorDTO> buscarTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro);

	Integer buscarQuantidadeTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro);

}