package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

public interface FechamentoEncalheRepository extends Repository<FechamentoEncalhe, FechamentoEncalhePK> {
	
	List<FechamentoFisicoLogicoDTO> buscarConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	List<FechamentoEncalhe> buscarFechamentoEncalhe(Date dataEncalhe);
	
	Boolean buscaControleFechamentoEncalhe(Date dataEncalhe);
	
	List<FechamentoFisicoLogicoDTO> buscarValorTotalEncalhe(Date dataEncalhe, Long idCota);
	
	void salvarControleFechamentoEncalhe(ControleFechamentoEncalhe controleFechamentoEncalhe);
	
	List<ChamadaEncalheCota> buscarChamadaEncalheCota(Date dataEncalhe, Long idCota);
	
	int buscaQuantidadeConferencia(Date dataEncalhe, boolean porBox);
	
	Date obterChamdasEncalhePostergadas(Long idCota, Date dataEncalhe);

	ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(Date dataFechamentoEncalhe);

	Date buscaDataUltimoControleFechamentoEncalhe();

	Date buscarUltimoFechamentoEncalheDia(Date dataFechamentoEncalhe);
	
	List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	Integer buscarTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro);
	
	List<Cota> buscarCotaChamadaEncalhe(Date dataEncalhe);
	
    List<Cota> buscarCotaFechamentoChamadaEncalhe(Date dataEncalhe);
    
    Integer obterTotalCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp);

    List<CotaAusenteEncalheDTO> obterCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp);
    
    int buscarQuantidadeConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro);
    
    BigDecimal obterValorTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro, Integer page, Integer rp );
}
