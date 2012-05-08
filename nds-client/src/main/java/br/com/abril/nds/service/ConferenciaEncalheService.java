package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface ConferenciaEncalheService {

	public List<Box> obterListaBoxEncalhe();
	
	public String obterBoxPadraoUsuario(Long idUsuario);

   /*
	* Verifica se o encalhe para esta cota ja foi conferido,
	* caso positivo retorna true.
	* 
	* Lancara uma exception caso: 
	* 	Não haja chamada de encalhe prevista (or) 
	*   Não possa reabrir conferencia devido a data de operacao. 
	*/
	public boolean verificarCotaProcessada(Integer numeroCota);

   /*
	* Verifica cota emite NFe.  
	* Caso positivo retorna true
	*/
	public boolean verificarCotaEmiteNFe();

	public void inserirDadosNotaFiscalCota();

	/*
	* Obtem os dados sumarizados de encalhe da cota, e se esta estiver
	* com sua conferencia reaberta retorna tambem a lista do que ja foi
	* conferido.
	*
	*/
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota();
	
	
	public ProdutoEdicao pesquisarProdutoEdicaoPorCodigoDeBarras(String codigoDeBarras);
	
	public ProdutoEdicao pesquisarProdutoEdicaoPorSM();
	
	/*
	 * Traz uma lista de codigoProduto - nomeProduto -  numeroEdicao
	 */
	public Object obterListaDadosProdutoEdicao(String codigoOuNome);
	
   /*
	* Cada produto que é adicionado na conferencia de encalhe dever ser                 
	* verificado se existe uma chamada de encalhe para o mesmo.                         
	*                                                                                
	* Retorna dados do produto caso o mesmo esteja na chamada de encalhe em andamento.  
	*/
	public void verificarProdutoExistenciaChamadaEncalhe(Long idProdutoEdicao);
	
	
	public void salvarDadosConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe);
	
	// (caso valor nota esteja diferente do encalhe requisitar correcao)
	public void finalizarConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe);
	
}
