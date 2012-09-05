package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.GeradorFluxoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;



/**
 * 
 *  Interface que define as regras de implementação referentes a PDVs
 * 
 * @author Discover Technology
 *
 */
public interface PdvService {
	
	
	/**
	 * Retorna uma lista de PDVs referente a uma cota informada.
	 * 
	 * @param filtro - filtro com opçãoe de consulta, ordenação e paginação
	 * 
	 * @return List<PdvDTO>
	 */
	List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro);
	
	void salvar(PdvDTO pdvDTO);
	
	List<TipoPontoPDV> obterTiposPontoPDV();
	
	List<AreaInfluenciaPDV> obterAreasInfluenciaPDV();
		
	List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(Long... codigos);
	
	List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoNotIn(Long... codigos);
	
	List<MaterialPromocional> obterMateriaisPromocionalPDV(Long... codigos);
	
	List<MaterialPromocional> obterMateriaisPromocionalPDVNotIn(Long... codigos);
	
	List<TipoEstabelecimentoAssociacaoPDV> obterTipoEstabelecimentoAssociacaoPDV();
	
	List<TipoLicencaMunicipal> obterTipoLicencaMunicipal();
	
	PdvDTO obterPDV(Long idCota, Long idPdv);
	
	List<EnderecoAssociacaoDTO> buscarEnderecosPDV(Long idPDV,Long idCota);
	
	void excluirPDV(Long idPdv);

	/**
	 * Obtém lista com os possíveis peridos a serem selecionados
	 * 
	 * @param selecionados - Periodos já selecionados
	 * @return - períodos que ainda podem ser selecionados
	 */
	List<TipoPeriodoFuncionamentoPDV> getPeriodosPossiveis(List<PeriodoFuncionamentoDTO> selecionados);
	
	/**
	 * Valida se uma lista de períodos é valida, de acordo com as regras definidas na EMS 0159
	 * 
	 * @param listaTipos
	 * @throws Exception
	 */
	void validarPeriodos(List<PeriodoFuncionamentoDTO> periodos) ;

	List<TelefoneAssociacaoDTO> buscarTelefonesPdv(Long idPdv, Long idCota);
	
	boolean existePDVPrincipal(Long idCota, Long idPdv);
	
	List<Endereco> buscarMunicipiosPdvPrincipal();
	
	Endereco buscarMunicipioPdvPrincipal(Integer codigoCidadeIBGE);
	
	List<TipoPontoPDV> obterTiposPontoPDVPrincipal();
	
	TipoPontoPDV obterTipoPontoPDVPrincipal(Long codigoPontoPDV);
	
    /**
     * Obtém a lista de PDV's associados ao histórico de titularidade da cota
     * 
     * @param filtro
     *            filtro com os parâmetros para consulta dos PDV's
     * @return {@link List<PdvDTO>} com os PDV's associados ao histórico de
     *         titularidade da cota
     */
    List<PdvDTO> obterPdvsHistoricoTitularidade(FiltroPdvDTO filtro);
    
    /**
     * Obtém o PDV associado ao histórico de titularidade da cota
     * @param idPdv
     *            identificador do PDV associado ao histórico de titularidade da
     *            cota
     * @return {@link PdvDTO} com as informações do PDV associado ao histórico
     *         de titularidade da cota
     */
    PdvDTO obterPdvHistoricoTitularidade(Long idPdv);
    
    /**
     * Obtém os endereços do histórico de titularidade do PDV
     * 
     * @param idPdv
     *            identificador do PDV associado ao histórico de titularidade
     * @return Lista de {@link EnderecoAssociacaoDTO} com as informações dos
     *         endereços associados ao histórico de titularidade do PDV
     */
    List<EnderecoAssociacaoDTO> obterEnderecosHistoricoTitularidadePDV(Long idPdv);
    
    
    /**
     * Obtém os telefones do histórico de titularidade do PDV
     * 
     * @param idPdv
     *            identificador do PDV associado ao histórico de titularidade
     * @return Lista de {@link TelefoneAssociacaoDTO} com as informações dos
     *         telefones associados ao histórico de titularidade do PDV
     */
    List<TelefoneAssociacaoDTO> obterTelefonesHistoricoTitularidadePDV(Long idPdv);
    
    /**
     * Obtém os geradores de fluxo do PDV do histórico de titularidade da cota
     * 
     * @param idPdv
     *            identificador do PDV do histórico de titularidade
     * @param codigos
     *            códigos dos geradores de fluxo
     * 
     * @return lista de {@link GeradorFluxoDTO} com as informações de geradores
     *         de fluxo associados ao PDV do histórico de titularidade da cota
     */
    List<GeradorFluxoDTO> obterGeradoresFluxoHistoricoTitularidadePDV(Long idPdv, Set<Long> codigos);
    
}

