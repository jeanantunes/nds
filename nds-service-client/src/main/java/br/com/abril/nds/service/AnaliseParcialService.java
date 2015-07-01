package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.DataLancamentoPeriodoEdicoesBasesDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ReparteFixacaoMixWrapper;
import br.com.abril.nds.dto.DetalhesEdicoesBasesAnaliseEstudoDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.estudo.CotaLiberacaoEstudo;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;

public interface AnaliseParcialService {

	EstudoCotaGerado buscarPorId(Long id);
	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO);
	void atualizaClassificacaoCota(Long estudoId, Integer numeroCota, String classificacaoCota);
	void atualizaReparte(Long estudoId, Integer numeroCota, Long reparte, Long reparteDigitado);
	void liberar(Long id, List<CotaLiberacaoEstudo> cotas);
	List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo);
	
	List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);

    BigDecimal calcularPercentualAbrangencia(Long estudoId);

    void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap, String legenda, boolean manterFixa);

    CotaDTO buscarDetalhesCota(Integer numeroCota, String codigoProduto, Long idClassifProdEdicao);

    Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo);

    BigInteger atualizaReparteTotalESaldo(Long idEstudo, Integer reparteTotal);
    
    List<EstudoCotaGerado> obterEstudosCotaGerado(Long id);
    
    BigDecimal reparteFisicoOuPrevistoLancamento (Long idEstudo);
    
	void atualizarFixacaoOuMix(ReparteFixacaoMixWrapper wrapper);
	
	ValidacaoException validarLiberacaoDeEstudo(Long estudoId);
	
	DetalhesEdicoesBasesAnaliseEstudoDTO obterReparteEVendaTotal(String codigoProduto, Long edicao, Long idTipoClassificacao, Integer numPeriodoParcial);
	List<DataLancamentoPeriodoEdicoesBasesDTO> obterDataLacmtoPeridoEdicoesBaseParciais(Long idEstudo, Long idProdutoEdicao);
	List<EdicoesProdutosDTO> carregarPeriodosAnterioresParcial(Long idEstudo, Boolean isTrazerEdicoesAbertas);

}
