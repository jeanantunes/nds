package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
public interface DescontoRepository extends Repository<Desconto, Long> {

	List<Fornecedor> buscarFornecedoresQueUsamDescontoGeral(Desconto desconto);
	
	List<Cota> buscarCotasQueUsamDescontoEspecifico(Desconto desconto);
	
	List<Fornecedor> buscarFornecedoresQueUsamDescontoEspecifico(Desconto desconto);
	
	List<Long> buscarProdutosQueUsamDescontoProduto(Desconto desconto);
	
	List<Long> buscarProdutosEdicoesQueUsamDescontoProduto(Desconto desconto);
	
	List<Long> buscarProximosLancamentosQueUsamDescontoProduto(Desconto desconto);
	
	Desconto buscarUltimoDescontoValido(Long idDesconto, Fornecedor fornecedor);

	List<TipoDescontoDTO> obterMergeDescontosEspecificosEGerais(Cota cota,String sortorder, String sortname);

	BigDecimal obterMediaDescontosFornecedoresCota(Integer numeroCota);

	List<Cota> buscarCotasQueUsamDescontoEditor(Desconto desconto);

	Editor buscarEditorUsaDescontoEditor(Desconto desconto);

	List<DescontoDTO> buscarDescontosAssociadosACota(Integer numeroCota);
	
}
