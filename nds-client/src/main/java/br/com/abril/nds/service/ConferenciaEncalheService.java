package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheExcedeReparteException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl.TipoDocumentoConferenciaEncalhe;

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
	 * Valida se o produto edicao em questão é relalivo a lancamento parcial.
	 * 
	 * @param codigo
	 * @param numeroEdicao
	 * 
	 * @return boolean
	 */
	public boolean isLancamentoParcialProdutoEdicao(String codigo, Long numeroEdicao);

	
	/**
	 * Obtém o TipoContabilizacaoCE.
	 * 
	 * @return TipoContabilizacaoCE
	 */
	public TipoContabilizacaoCE obterTipoContabilizacaoCE();

	
	/**
	 * Obtém os dados sumarizados de encalhe da cota, e se esta estiver
	 * com sua conferencia sendo reaberta retorna tambem a lista do que ja foi
	 * conferido.
	 * 
	 * @param numeroCota
	 * @param indConferenciaContingencia
	 * 
	 * @return InfoConferenciaEncalheCota
	 */
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota, boolean indConferenciaContingencia);
	
	/**
	 * Gera documentação referente a conferência de encalhe.
	 * 
	 * @param dadosDocumentacaoConfEncalheCotaDTO
	 * @param tipoDocumentoConferenciaEncalhe
	 * 
	 * @return byte
	 */
	public byte[] gerarDocumentosConferenciaEncalhe(
			DadosDocumentacaoConfEncalheCotaDTO dadosDocumentacaoConfEncalheCotaDTO, 
			TipoDocumentoConferenciaEncalhe tipoDocumentoConferenciaEncalhe);

	
	/**
	 * Obtém dados do produtoEdicao através do id do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param id
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 * @throws EncalheRecolhimentoParcialException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long id) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException;
	
	/**
	 * Obtém dados do produtoEdicao através do código de barras do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param codigoDeBarras
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 * @throws EncalheRecolhimentoParcialException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException;
	
	/**
	 * Obtém dados do produtoEdicao através do código SM do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param sm
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 * @throws EncalheRecolhimentoParcialException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorSM(Integer numeroCota, Integer sm) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException;
	
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
	 * @throws EncalheExcedeReparteException
	 * 
	 * @return Long
	 */
	public Long salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException, EncalheExcedeReparteException;
	
	
	/**
	 * Finaliza uma conferência de encalhe gerando os movimentos financeiros 
	 * relativos a mesma, faz chamada também ao rotinas relativas a cobrança.
	 * 
	 * Retorna objeto com dados da conferencia finalizada para a geração de
	 * documentos relativos a mesma.
	 * 
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalhe
	 * @param listaIdConferenciaEncalheParaExclusao
	 * @param usuario
	 * 
	 * @throws EncalheExcedeReparteException
	 */
	public DadosDocumentacaoConfEncalheCotaDTO finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) throws EncalheExcedeReparteException;
	
	/**
	 * Gera arquivo de slip a partir do ControleConferenciaEncalheCota
	 * 
	 * @param idControleConferenciaEncalheCota
	 * @param incluirNumeroSlip
	 * @return
	 */
	public byte[] gerarSlip(Long idControleConferenciaEncalheCota, boolean incluirNumeroSlip);
	
	/**
	 * Obtem valor total para geração de crédito na C.E.
	 * @param idControleConferenciaEncalheCota
	 * @return BigDecimal
	 */
	BigDecimal obterValorTotalConferenciaEncalhe(Long idControleConferenciaEncalheCota);
		
}