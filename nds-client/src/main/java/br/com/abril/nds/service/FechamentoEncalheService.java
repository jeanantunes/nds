package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;

public interface FechamentoEncalheService {

	List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	void salvarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento);
	
	List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe, String sortorder, String sortname, int page, int rp);

	Integer buscarTotalCotasAusentes(Date dataEncalhe);

	void postergarCotas(Date dataEncalhe, Date dataPostergacao, List<Long> idsCotas);
	
	void cobrarCotas(Date dataOperacao, Usuario usuario, List<Long> idsCotas);
	
	BigDecimal buscarValorTotalEncalhe(Date dataEncalhe, Long idCota);
	
	void salvarFechamentoEncalheBox(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento);
	
	void encerrarOperacaoEncalhe(Date dataEncalhe);
	
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
}
