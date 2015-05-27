package br.com.abril.nds.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;

/**
 * Interface de serviços do parametrosDistribuidorVO
 * @author InfoA2
 */
public interface ParametrosDistribuidorService {

	/**
	 * Retorna o ParametrosDistribuidorVO utilizando os dados do Distribuidor e ParametrosSistema
	 * @return ParametrosDistribuidorVO
	 */
	public ParametrosDistribuidorVO getParametrosDistribuidor();

	/**
	 * Salva a distribuidor com todos os parâmetros informados
	 * 
	 * @param ParametrosDistribuidorVO
	 * @param imgLogotipo
	 * @param imgContentType
	 */
	public void salvarDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor,
								   InputStream imgLogotipo,
								   String imgContentType);
	
	/**
	 * Retorna o logotipo do distribuidor, caso exista.
	 * 
	 * @return InputStream do logotipo
	 */
	public InputStream getLogotipoDistribuidor();
	
	public ControleConferenciaEncalhe obterControleConferenciaEncalhe(Date dataOperacao);
	
	/**
	 * @return
	 */
	public List<DistribuidorTipoNotaFiscal> obterTiposNotaFiscalDistribuidor();

	/**
	 * @return
	 */
	public List<NotaFiscalTipoEmissao> obterTiposEmissoesNotaFiscalDistribuidor();

	/**
	 * @return
	 */
	public List<String> obterEstadosAtendidosPeloDistribuidor();
	
}