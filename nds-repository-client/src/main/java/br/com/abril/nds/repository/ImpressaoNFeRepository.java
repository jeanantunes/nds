package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public interface ImpressaoNFeRepository extends Repository<NotaFiscal, Long>  {

	/**
	 * Retorna uma lista de NF-e's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<NotasCotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna a quantidade de NF-e's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public Integer buscarCotasParaImpressaoNFeQtd(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna uma lista de NE's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<NotasCotasImpressaoNfeDTO> buscarCotasParaImpressaoNotaEnvio(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna a quantidade de NE's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public Integer buscarCotasParaImpressaoNotaEnvioQtd(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna uma lista de NF's baseado no filtro informado
	 * @param filtro
	 * 
	 * @return
	 */
	public List<NotaFiscal> buscarNotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna uma lista de NE's baseado no filtro informado
	 * @param filtro
	 * @return
	 */
	public List<NotaEnvio> buscarNotasEnvioParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna uma lista de Produtos baseado no Intervalo de Datas de Movimento baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Produto> buscarProdutosParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);

	public List<NotasCotasImpressaoNfeDTO> obterNotafiscalImpressao(FiltroImpressaoNFEDTO filtro);

	List<BandeirasDTO> obterNotafiscalImpressaoBandeira(FiltroImpressaoNFEDTO filtro);

	public List<FornecedorDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana, Long forncedorId);
	
}
