package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;

public interface FechamentoEncalheService {

	List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	void salvarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento);
	
	List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp);

	Integer buscarTotalCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao);

	void postergarCotas(Date dataEncalhe, Date dataPostergacao, List<Long> idsCotas);
	
	void postergarTodasCotas(Date dataEncalhe, Date dataPostergacao);

	void cobrarCotas(Date dataOperacao, Usuario usuario, List<Long> idsCotas) throws GerarCobrancaValidacaoException;
	

	BigDecimal buscarValorTotalEncalhe(Date dataEncalhe, Long idCota);
	
	void salvarFechamentoEncalheBox(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento);
	
	void encerrarOperacaoEncalhe(Date dataEncalhe, Usuario usuario, FiltroFechamentoEncalheDTO filtroSessao);
	
	Boolean existeFechamentoEncalheDetalhado(FiltroFechamentoEncalheDTO filtro);
	
	Boolean existeFechamentoEncalheConsolidado(FiltroFechamentoEncalheDTO filtro);
	
	void converteFechamentoDetalhadoEmConsolidado(FiltroFechamentoEncalheDTO filtro);
	
	void removeFechamentoDetalhado(FiltroFechamentoEncalheDTO filtro);
	
	int buscarQuantidadeCotasAusentes(Date dataEncalhe);
	
	ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(Date dataFechamentoEncalhe);

	Date buscaDataUltimoControleFechamentoEncalhe();
	
	Date buscarUltimoFechamentoEncalheDia(Date dataFechamentoEncalhe);

	List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	Integer buscarTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro);
	
	Date buscarUtimoDiaDaSemanaRecolhimento();
	
	void gerarNotaFiscal(Date dataEncalhe);

	List<CotaDTO> obterListaCotaConferenciaNaoFinalizada(Date dataOperacao);
	
	int buscarQuantidadeConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro);

	Boolean buscaControleFechamentoEncalhe(Date data);
	
	BigDecimal obterValorTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro);

	public abstract void realizarCobrancaCotas(Date dataOperacao, Usuario usuario,
			List<CotaAusenteEncalheDTO> listaCotasAusentes, Cota cotaAusente) throws GerarCobrancaValidacaoException;
}
