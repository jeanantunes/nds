package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheExcedeReparteException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;

public interface ConferenciaEncalheService {
	
	/**
	 * Retorna uma lista de box de recolhimento.
	 * 
	 * @return List - Box
	 */
	public List<Box> obterListaBoxEncalhe(Long idUsuario);

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
	 * Método que irá validar se o encalhe não ultrapassa o valor de reparte.
	 * Caso isso ocorra ira lançar EncalheExcedeReparteException 
	 * 
	 * @param idProdutoEdicao
	 * @param numeroCota
	 * 
	 * @throws EncalheExcedeReparteException
	 */
	public void validarQtdeEncalheExcedeQtdeReparte(Integer numeroCota, Long idProdutoEdicao, BigDecimal qtdeExemplarEncalhe) throws EncalheExcedeReparteException;

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

	/**
	 * Obtém dados do produtoEdicao através do id do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param id
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long id) throws ChamadaEncalheCotaInexistenteException;
	
	/**
	 * Obtém dados do produtoEdicao através do código de barras do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param codigoDeBarras
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException;
	
	/**
	 * Obtém dados do produtoEdicao através do código SM do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param sm
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorSM(Integer numeroCota, Integer sm) throws ChamadaEncalheCotaInexistenteException;
	
	/**
	 * Obtém detalhes do item de conferencia de encalhe.
	 * 
	 * @param numeroCota
	 * @param idConferenciaEncalhe
	 * @param idProdutoEdicao
	 * 
	 * @return ConferenciaEncalheDTO
	 */
	ConferenciaEncalheDTO obterDetalheConferenciaEncalhe(Integer numeroCota, Long idConferenciaEncalhe, Long idProdutoEdicao);
	
	/**
	 * Salvas os dados de uma operação de conferência de encalhe.
	 * 
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalhe
	 * @param listaIdConferenciaEncalheParaExclusao
	 * @param usuario
	 * 
	 * @throws EncalheSemPermissaoSalvarException
	 * @throws ConferenciaEncalheFinalizadaException
	 */
	public void salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException;
	
	
	/**
	 * Finaliza uma conferência de encalhe gerando os movimentos financeiros 
	 * relativos a mesma, faz chamada também ao rotinas relativas a cobrança.
	 * 
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalhe
	 * @param listaIdConferenciaEncalheParaExclusao
	 * @param usuario
	 */
	public void finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario);
}
