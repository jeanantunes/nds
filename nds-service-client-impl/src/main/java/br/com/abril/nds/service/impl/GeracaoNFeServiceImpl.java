package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.Condicao;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaFiscalNdsRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.builders.EmitenteDestinatarioBuilder;
import br.com.abril.nds.service.builders.FaturaBuilder;
import br.com.abril.nds.service.builders.ItemNotaFiscalBuilder;
import br.com.abril.nds.service.builders.NaturezaOperacaoBuilder;
import br.com.abril.nds.service.builders.NotaFiscalBuilder;
import br.com.abril.nds.service.builders.NotaFiscalValoresCalculadosBuilder;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nfe.model.NotaFiscalBase;

@Service
public class GeracaoNFeServiceImpl implements GeracaoNFeService {
	
	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private NaturezaOperacaoRepository tipoNotaFiscalRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired 
	private SerieRepository serieRepository;
	
	@Autowired 
	private NaturezaOperacaoRepository naturezaOperacaoRepository;
	
	@Autowired 
	private NotaFiscalNdsRepository notaFiscalNdsRepository; 
	
	@Override
	@Transactional
	public List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, Long idTipoNotaFiscal, Long idRoteiro, Long idRota,
			String sortname, String sortorder, Integer resultsPage, Integer page, SituacaoCadastro situacaoCadastro) {
		
		Set<NaturezaOperacao> tiposNota = new HashSet<NaturezaOperacao>();
		
		if (idTipoNotaFiscal == null){
			
			tiposNota.addAll(this.tipoNotaFiscalRepository.obterTiposNotasFiscaisCotasNaoContribuintesPor(
					this.distribuidorRepository.tipoAtividade()));
		} else {
			
			tiposNota.add(this.tipoNotaFiscalRepository.buscarPorId(idTipoNotaFiscal));
		}
		
		List<SituacaoCadastro> situacoesCadastro = null;
		
		if (situacaoCadastro != null){
			situacoesCadastro = new ArrayList<SituacaoCadastro>();
			situacoesCadastro.add(situacaoCadastro);
		}
		
		Set<Long> idsCotasDestinatarias = new HashSet<>();
		idsCotasDestinatarias.addAll(this.cotaRepository.obterIdCotasEntre(intervalorCota, intervaloBox, situacoesCadastro, idRoteiro, idRota, null, null, null, null));
		
		ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal = new ConsultaLoteNotaFiscalDTO();
		
		dadosConsultaLoteNotaFiscal.setTipoNotaFiscal(tiposNota);
		dadosConsultaLoteNotaFiscal.setPeriodoMovimento(intervaloDateMovimento);
		dadosConsultaLoteNotaFiscal.setIdsCotasDestinatarias(idsCotasDestinatarias);
		dadosConsultaLoteNotaFiscal.setListaIdFornecedores(listIdFornecedor);
		
		Map<Cota, QuantidadePrecoItemNotaDTO> cotasTotalItens = this.notaFiscalService.obterTotalItensNotaFiscalPorCotaEmLote(dadosConsultaLoteNotaFiscal);
		
		Set<Cota> cotas = cotasTotalItens.keySet();
		
		List<CotaExemplaresDTO> listaCotaExemplares = new ArrayList<CotaExemplaresDTO>();
		
		for (Cota cota : cotas) {
			
			CotaExemplaresDTO cotaExemplares = new CotaExemplaresDTO();
			
			cotaExemplares.setIdCota(cota.getId());
			cotaExemplares.setExemplares(cotasTotalItens.get(cota).getQuantidade());
			cotaExemplares.setNomeCota(cota.getPessoa().getNome());
			cotaExemplares.setNumeroCota(cota.getNumeroCota());
			cotaExemplares.setTotal(cotasTotalItens.get(cota).getPreco());
			cotaExemplares.setTotalDesconto(cotasTotalItens.get(cota).getPrecoComDesconto());
			
			listaCotaExemplares.add(cotaExemplares);
			
		}
		
		return listaCotaExemplares;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.GeracaoNFeService#gerarNotaFiscal(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, java.util.List, java.util.List, java.lang.Long)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void gerarNotaFiscal(FiltroViewNotaFiscalDTO filtro, List<Long> idCotasSuspensas, Condicao condicao) throws FileNotFoundException, IOException {
		
		List<NotaFiscalNds> listaNotaFiscal = new ArrayList<NotaFiscalNds>();
		Distribuidor distribuidor = this.obterInformacaoDistribuidor();
		NaturezaOperacao naturezaOperacao = this.naturezaOperacaoRepository.obterNaturezaOperacao(filtro.getIdNaturezaOperacao());
		
		switch (naturezaOperacao.getTipoDestinatario()) {
		case COTA:
			gerarNotasFiscaisCotas(filtro, listaNotaFiscal, distribuidor, naturezaOperacao);
			break;
			
		case DISTRIBUIDOR:
			
			break;
			
		case FORNECEDOR:
			
			break;

		default:
			break;
		}
		
		if(listaNotaFiscal == null || listaNotaFiscal.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados itens para gerar nota.");
		
		this.notaFiscalNdsRepository.salvarNotasFiscais(listaNotaFiscal);
		
		// this.notaFiscalService.exportarNotasFiscais(listaNotaFiscal);
	}

	private void gerarNotasFiscaisCotas(FiltroViewNotaFiscalDTO filtro,
			List<NotaFiscalNds> listaNotaFiscal, Distribuidor distribuidor,
			NaturezaOperacao naturezaOperacao) {
		NotaFiscalNds notaFiscal;
		// obter as cotas que estão na tela pelo id das cotas
		List<Cota> cotas = this.notaFiscalNdsRepository.obterConjuntoCotasNotafiscal(filtro);
		
		for (Cota cota : cotas) {
			notaFiscal = new NotaFiscalNds();
			NotaFiscalBuilder.popularDadosDistribuidor(notaFiscal, distribuidor, filtro);
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, cota);
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, cota);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os movimentos de cada cota
			filtro.setIdCota(cota.getId());
			List<MovimentoEstoqueCota> movimentosEstoquesCotas = this.notaFiscalNdsRepository.obterMovimentoEstoqueCota(filtro);
			for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoquesCotas) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota);
			}
			
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoquesCotas);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal, cota);
			notaFiscal.setInformacoesComplementares("ssss");
			listaNotaFiscal.add(notaFiscal);
		}
		
		
	}

	private void gerarNotasFiscaisDistribuidor(FiltroViewNotaFiscalDTO filtro, List<NotaFiscalNds> listaNotaFiscal, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao) {
		NotaFiscalNds notaFiscal;
		// obter as cotas que estão na tela pelo id das cotas
		List<MovimentoEstoque> movimentoEstoques = this.notaFiscalNdsRepository.obterConjuntoDistribuidorNotafiscal(filtro);
		
		
	}
	
	private void gerarNotasFiscaisFornecedor(FiltroViewNotaFiscalDTO filtro, List<NotaFiscalNds> listaNotaFiscal, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao) {
		NotaFiscalNds notaFiscal;
		// obter as cotas que estão na tela pelo id das cotas
		List<Cota> cotas = this.notaFiscalNdsRepository.obterConjuntoCotasNotafiscal(filtro);
		
		for (Cota cota : cotas) {
			notaFiscal = new NotaFiscalNds();
			NotaFiscalBuilder.popularDadosDistribuidor(notaFiscal, distribuidor, filtro);
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, cota);
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, cota);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os movimentos de cada cota
			filtro.setIdCota(cota.getId());
			List<MovimentoEstoqueCota> movimentosEstoquesCotas = this.notaFiscalNdsRepository.obterMovimentoEstoqueCota(filtro);
			for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoquesCotas) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota);
			}
			
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoquesCotas);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal, cota);
			notaFiscal.setInformacoesComplementares("ssss");
			listaNotaFiscal.add(notaFiscal);
		}	
	}
	
	@Override
	@Transactional
	public List<CotaExemplaresDTO> consultaCotaExemplareSumarizado(FiltroViewNotaFiscalDTO filtro) {
		return notaFiscalService.consultaCotaExemplareSumarizado(filtro);
	}

	@Override
	@Transactional
	public Long consultaCotaExemplareSumarizadoQtd(FiltroViewNotaFiscalDTO filtro) {
		return notaFiscalService.consultaCotaExemplareSumarizadoQtd(filtro);
	}
	
	// metodo responsavel pelo dados do distribuidor da nota
	public Distribuidor obterInformacaoDistribuidor(){
		return distribuidorRepository.obter();
	}
	
	public Long serieNotaFiscal (br.com.abril.nfe.model.NotaFiscalBase notaFiscal){
		return serieRepository.obterNumeroSerieNota();
	}
	
	
	private void obterNotaFiscal(FiltroViewNotaFiscalDTO filtro) {
		NotaFiscalNds notaFiscal;
		// obter as cotas que estão na tela pelo id das cotas
		List<NotaFiscalBase> movimentoEstoques = this.notaFiscalNdsRepository.obterNotafiscal(filtro);
		
		
	}
}
