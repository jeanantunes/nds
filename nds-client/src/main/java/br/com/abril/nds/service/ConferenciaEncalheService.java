package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;

public interface ConferenciaEncalheService {
	
	/**
	 * Retorna uma lista de box de recolhimento.
	 * 
	 * @return List - Box
	 */
	public List<Box> obterListaBoxEncalhe(Long idUsuario);
	
	/**
	 * Retorna o código do box de recolhimento relacionado 
	 * ao usuário em questão (caso exista).
	 * 
	 * @param idUsuario
	 * 
	 * @return String
	 */
	public String obterBoxPadraoUsuario(Long idUsuario);

	/**
	 * Método faz seguintes verificações:
	 * 
	 * Se a cota ja possui uma conferencia de encalhe 
	 * para a data de operação atual, caso positivo, será lancada 
	 * uma exception para informando que é necessaria a reabertura
	 * desta conferência.
	 * 
	 * Senão, é verificado se existe alguma chamada de encalhe para
	 * a cota em questão. Se nenhuma chamada de encalhe atual ou 
	 * futura for encontrada, é lançada exception informando que não
	 * existe uma chamada de encalhe prevista para esta cota.
	 * 
	 * @param numeroCota
	 * 
	 * @throws ConferenciaEncalheExistenteException
	 * @throws ChamadaEncalheCotaInexistenteException
	 */
	public void verificarChamadaEncalheCota(Integer numeroCota) throws ConferenciaEncalheExistenteException, ChamadaEncalheCotaInexistenteException;

	/**
	 * Valida a existência de chamada de encalhe de acordo com a
	 * cota e produtoEdicao cuja dataRecolhimento esteja dentro da 
	 * faixa aceitavel (de acordo com  parâmetro do Distribuidor e dataOperacao atual). 
	 * 
	 * @param numeroCota
	 * @param idProdutoEdicao
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 */
	public void validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException;

	
   /*
	* Verifica cota emite NFe.  
	* Caso positivo retorna true
	*/
	public boolean verificarCotaEmiteNFe();

	public void inserirDadosNotaFiscalCota();

	/**
	 * Obtém os dados sumarizados de encalhe da cota, e se esta estiver
	 * com sua conferencia sendo reaberta retorna tambem a lista do que ja foi
	 * conferido.
	 * 
	 * @param numeroCota
	 * 
	 * @return InfoConferenciaEncalheCota
	 */
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota);

	ProdutoEdicao pesquisarProdutoEdicaoPorId(Integer numeroCota, Long id) throws ChamadaEncalheCotaInexistenteException;
	
	ProdutoEdicao pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException;
	
	ProdutoEdicao pesquisarProdutoEdicaoPorSM(Integer numeroCota, Long sm) throws ChamadaEncalheCotaInexistenteException;
	
	/*
	 * Traz uma lista de codigoProduto - nomeProduto -  numeroEdicao
	 */
	public Object obterListaDadosProdutoEdicao(String codigoOuNome);
	
	
	public void salvarDadosConferenciaEncalhe(ControleConferenciaEncalheCota controleConfEncalheCota, List<ConferenciaEncalheDTO> listaConferenciaEncalhe);
	
	// (caso valor nota esteja diferente do encalhe requisitar correcao)
	public void finalizarConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe);
}
