package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.fechamentoencalhe.GridFechamentoEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.ValidacaoVO;

public interface FechamentoEncalheService {

	void verificarEstoqueProdutoNaoAtualizado();
	
	List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	void salvarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento);
	
	List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp);

	Integer buscarTotalCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao, Integer numeroCota);

	void postergarCotas(Date dataEncalhe, Date dataPostergacao, List<Long> idsCotas);
	
	void postergarTodasCotas(Date dataEncalhe, Date dataPostergacao, List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe);

	void cobrarCota(Date dataOperacao, Usuario usuario, Long idCota) throws GerarCobrancaValidacaoException;

	BigDecimal buscarValorTotalEncalhe(Date dataEncalhe, Long idCota);
	
	void salvarFechamentoEncalheBox(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento);
	
	Set<String> encerrarOperacaoEncalhe(Date dataEncalhe, Usuario usuario, FiltroFechamentoEncalheDTO filtroSessao, 
			List<FechamentoFisicoLogicoDTO> listaEncalheSessao, boolean cobrarCotas);
	
	/**
	 * Verifica se existe pesquisa de fechamento de encalhe feita
	 * por box, ou seja, detalhada.
	 * 
	 * @param filtro
	 * 
	 * @return Boolean
	 */
	Boolean existeFechamentoEncalheDetalhado(FiltroFechamentoEncalheDTO filtro);
	
	/**
	 * Verifica se existe pesquisa de fechamento de encalhe feita
	 * de modo consolidado, ou seja, independente de box.
	 * 
	 * @param filtro
	 * @return
	 */
	Boolean existeFechamentoEncalheConsolidado(FiltroFechamentoEncalheDTO filtro);
	
	void converteFechamentoDetalhadoEmConsolidado(FiltroFechamentoEncalheDTO filtro);
	
	ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(Date dataFechamentoEncalhe);

	Date buscaDataUltimoControleFechamentoEncalhe();
	
	Date buscarUltimoFechamentoEncalheDia(Date dataFechamentoEncalhe);

	List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	Integer buscarTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro);
	
	Date buscarUtimoDiaDaSemanaRecolhimento();

	List<CotaDTO> obterListaCotaConferenciaNaoFinalizada(Date dataOperacao);
	
	int buscarQuantidadeConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro);
	
	BigDecimal obterValorTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro);

	void realizarCobrancaCotas(Date dataOperacao, 
									Usuario usuario,
									List<CotaAusenteEncalheDTO> listaCotasAusentes, 
									Cota cotaAusente) throws GerarCobrancaValidacaoException;

	List<GridFechamentoEncalheDTO> listaEncalheTotalParaGrid(List<FechamentoFisicoLogicoDTO> listaEncalheSessao);

	Integer buscarTotalCotasAusentesSemPostergado(Date dataEncalhe, boolean isSomenteCotasSemAcao, boolean ignorarUnificacao);
	
	void realizarCobrancaCota(Date dataOperacao,
			                  Usuario usuario, 
			                  Long idCota,
			                  ValidacaoVO validacaoVO);
	
	Boolean validarEncerramentoOperacaoEncalhe(Date data);

	List<CotaDTO> obterListaCotaConferenciaPendenciaErro(Date dataOperacao);
	
	boolean existeFechamentoEncalhePorDataOperacao(Date dataOperacao);
	
	boolean existeFechamentoEncalhePorCota(Date dataOperacao, Integer numeroCota);
}
