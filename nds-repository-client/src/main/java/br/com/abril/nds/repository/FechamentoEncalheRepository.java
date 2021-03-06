package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.ExtracaoContaCorrenteDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtracaoContaCorrenteDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

public interface FechamentoEncalheRepository extends Repository<FechamentoEncalhe, FechamentoEncalhePK> {
	
	List<FechamentoFisicoLogicoDTO> buscarConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp);
	
	List<FechamentoEncalhe> buscarFechamentoEncalhe(Date dataEncalhe);
	
	List<Date> obterDatasControleFechamentoEncalheRealizado(Date dataDe, Date dataAte);
	
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
	
    Integer obterTotalCotasAusentes(Date dataEncalhe, DiaSemana diaRecolhimento, boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp, Integer numeroCota);

    List<CotaAusenteEncalheDTO> obterCotasAusentes(Date dataEncalhe, DiaSemana diaRecolhimento, boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp);
    
    int buscarQuantidadeConferenciaEncalheNovo(FiltroFechamentoEncalheDTO filtro);
    
    BigDecimal obterValorTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro);
    
    public List<FechamentoFisicoLogicoDTO> buscarConferenciaEncalheNovo(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp);

	public FechamentoFisicoLogicoDTO buscarDescontosLogistica(FechamentoFisicoLogicoDTO fechamento);

	public FechamentoFisicoLogicoDTO buscarDescontosProduto(
			FechamentoFisicoLogicoDTO fechamento);

	public FechamentoFisicoLogicoDTO buscarDescontosProdutoEdicao(
			FechamentoFisicoLogicoDTO fechamento);

	public List<FechamentoFisicoLogicoDTO> buscarMovimentoEstoqueCota(
			FiltroFechamentoEncalheDTO filtro, ArrayList<Long> listaDeIdsProdutoEdicao);

	List<FechamentoFisicoLogicoDTO> buscarMovimentoEstoqueCotaVendaProduto(
			FiltroFechamentoEncalheDTO filtro, ArrayList<Long> listaDeCodigosProduto);

	Integer obterTotalCotasAusentesSemPostergado(Date dataEncalhe, DiaSemana diaRecolhimento, boolean isSomenteCotasSemAcao,
			String sortorder, String sortname, int page, int rp, boolean ignorarUnificacao);
	
	boolean verificarExistenciaFechamentoEncalheConsolidado(Date dataEncalhe);
	
	Boolean buscaControleConferenciaEncalhe(Date dataEncalhe);

	Boolean validarEncerramentoOperacaoEncalhe(Date data);
	
    Integer obterDiaRecolhimento(Long produtoEdicao,Date dataRecolhimento);

	BigInteger buscarQtdeFechamentoEncalhe(Date dataEncalhe, Long produtoEdicaoId);
	
	boolean existeFechamentoEncalhePorData(Date dataOperacao);
	
	boolean existeFechamentoEncalhePorCota(Date dataOperacao, Integer numeroCota);

	List<ExtracaoContaCorrenteDTO> extracaoContaCorrente(FiltroExtracaoContaCorrenteDTO filtro);

	List<Integer> extracaoContaCorrente_BuscarCotasObservacoes(FiltroExtracaoContaCorrenteDTO filtro);
}
