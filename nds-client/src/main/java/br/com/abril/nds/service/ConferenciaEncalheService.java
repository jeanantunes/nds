package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
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
	 * Valida a existência de chamada de encalhe de acordo com a
	 * cota e produtoEdicao cuja dataRecolhimento esteja dentro da 
	 * faixa aceitavel (de acordo com  parâmetro do Distribuidor e dataOperacao atual).
	 * 
	 * Se encontrada, será retornada esta chamadaEncalhe para o produtoEdicao em questão.
	 *  
	 * @param numeroCota
	 * @param idProdutoEdicao
	 * 
	 * @return ChamadaEncalhe
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 */
	public ChamadaEncalhe validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException;

	
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

	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long id) throws ChamadaEncalheCotaInexistenteException;
	
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException;
	
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
	
	public void salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) throws EncalheSemPermissaoSalvarException;
	
	
	public void finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario);
}
