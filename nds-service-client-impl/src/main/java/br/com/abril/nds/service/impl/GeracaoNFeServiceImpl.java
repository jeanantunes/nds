package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalBase;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaFiscalNdsRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.builders.EmitenteDestinatarioBuilder;
import br.com.abril.nds.service.builders.FaturaBuilder;
import br.com.abril.nds.service.builders.FaturaEstoqueProdutoNotaFiscalBuilder;
import br.com.abril.nds.service.builders.ItemNotaFiscalBuilder;
import br.com.abril.nds.service.builders.ItemNotaFiscalEstoqueProdutoBuilder;
import br.com.abril.nds.service.builders.NaturezaOperacaoBuilder;
import br.com.abril.nds.service.builders.NotaFiscalBuilder;
import br.com.abril.nds.service.builders.NotaFiscalEstoqueProdutoBuilder;
import br.com.abril.nds.service.builders.NotaFiscalValoresCalculadosBuilder;
import br.com.abril.nds.util.Intervalo;

@Service
public class GeracaoNFeServiceImpl implements GeracaoNFeService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(GeracaoNFeServiceImpl.class);
	
	@Autowired
	private NotaFiscalService notaFiscalService;

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
	
	@Autowired
	private UsuarioService usuarioService;
	
	// Trava para evitar duplicidade ao gerar notas de envio por mais de um usuario simultaneamente
    // O HashMap suporta os mais detalhes e pode ser usado futuramente para restricoes mais finas
    private static final Map<String, Object> TRAVA_GERACAO_NFe = new HashMap<>();
	
	@Override
	@Transactional
	public List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, Long idTipoNotaFiscal, Long idRoteiro, Long idRota,
			String sortname, String sortorder, Integer resultsPage, Integer page, SituacaoCadastro situacaoCadastro) {
		
		if (TRAVA_GERACAO_NFe.get("NFesSendoGeradas") != null) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Notas de envio sendo geradas por outro usuário, tente novamente mais tarde.");
        }
        
        TRAVA_GERACAO_NFe.put("NFesSendoGeradas", true);
        
        List<CotaExemplaresDTO> listaCotaExemplares = null; 
        		
        try {
        	
        
			Set<NaturezaOperacao> tiposNota = new HashSet<NaturezaOperacao>();
			
			if (idTipoNotaFiscal == null){
				
				tiposNota.addAll(this.naturezaOperacaoRepository.obterTiposNotasFiscaisCotasNaoContribuintesPor(
						this.distribuidorRepository.tipoAtividade()));
			} else {
				
				tiposNota.add(this.naturezaOperacaoRepository.buscarPorId(idTipoNotaFiscal));
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
			
			listaCotaExemplares = new ArrayList<CotaExemplaresDTO>();
			
			for (Entry<Cota, QuantidadePrecoItemNotaDTO> entry : cotasTotalItens.entrySet()) {
				
				CotaExemplaresDTO cotaExemplares = new CotaExemplaresDTO();
				
				cotaExemplares.setIdCota(entry.getKey().getId());
				cotaExemplares.setExemplares(entry.getValue().getQuantidade());
				cotaExemplares.setNomeCota(entry.getKey().getPessoa().getNome());
				cotaExemplares.setNumeroCota(entry.getKey().getNumeroCota());
				cotaExemplares.setTotal(entry.getValue().getPreco());
				cotaExemplares.setTotalDesconto(entry.getValue().getPrecoComDesconto());
				
				listaCotaExemplares.add(cotaExemplares);
				
			}
			
        } catch (Exception e) {
        	LOGGER.error("", e);
        	throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar NF-e.");
        	
        } finally {
            TRAVA_GERACAO_NFe.remove("NFesSendoGeradas");
        }
		
        return listaCotaExemplares;
        
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.GeracaoNFeService#gerarNotaFiscal(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, java.util.List, java.util.List, java.lang.Long)
	 */
	@Override
	@Transactional(rollbackFor=Throwable.class)
	public List<NotaFiscal> gerarNotaFiscal(FiltroNFeDTO filtro) throws FileNotFoundException, IOException {
		
		this.validarFiltroNFe(filtro);
		/**
		 * metodo para gerar nota.
		 */
		List<NotaFiscal> notas = new ArrayList<NotaFiscal>();
		Distribuidor distribuidor = this.obterInformacaoDistribuidor();
		
		if(distribuidor.isPossuiRegimeEspecialDispensaInterna()){
			if(new Date().getTime() < distribuidor.getDataLimiteVigenciaRegimeEspecial().getTime()){
				throw new ValidacaoException(TipoMensagem.WARNING, "A data limite de vigincia do regime especial expirou!" );
			}
		}
		
		NaturezaOperacao naturezaOperacao = this.naturezaOperacaoRepository.obterNaturezaOperacao(filtro.getIdNaturezaOperacao());
		
		switch (naturezaOperacao.getTipoDestinatario()) {
		
			case COTA:
			case DISTRIBUIDOR:
				
				if(!distribuidor.isPossuiRegimeEspecialDispensaInterna()){
					this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao);
				}else{
					
					

				}
				
				break;
				
			case FORNECEDOR:			
				this.gerarNotasFiscaisFornecedor(filtro, distribuidor, naturezaOperacao);
				break;
	
			default:
				throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Destinatário não especificado");
				
		}
		
		if(notas == null || notas.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados itens para gerar nota.");
		
		for (NotaFiscal notaFiscal : notas) {
			notaFiscalRepository.adicionar(notaFiscal);
		}
		
		this.notaFiscalService.exportarNotasFiscais(notas);
		
		return notas;
	}

	private void validarFiltroNFe(FiltroNFeDTO filtro) {
		
		if(filtro.getDataInicial() == null || filtro.getDataFinal() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "As datas inicial e final não podem ser nulas.");
		} 
		
		if(filtro.getDataFinal().getTime() < filtro.getDataInicial().getTime()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "A data inicial não pode ser maior que a da final.");
		}
	}

	private void gerarNotasFiscaisCotas(FiltroNFeDTO filtro,
			List<NotaFiscal> notasFiscais, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao) {
		
		// obter as cotas que estão na tela pelo id das cotas
		List<Cota> cotas = this.notaFiscalNdsRepository.obterConjuntoCotasNotafiscal(filtro);
		
		for (Cota cota : cotas) {
			
			NotaFiscal notaFiscal = new NotaFiscal();
			
			notaFiscal.setUsuario(usuarioService.getUsuarioLogado());
			
			NotaFiscalBuilder.popularDadosDistribuidor(notaFiscal, distribuidor, filtro);
			
			NotaFiscalBuilder.popularDadosTransportadora(notaFiscal, distribuidor, filtro);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, cota);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, cota);
			
			// obter os movimentos de cada cota
			filtro.setIdCota(cota.getId());
			List<MovimentoEstoqueCota> movimentosEstoqueCota = this.notaFiscalNdsRepository.obterMovimentosEstoqueCota(filtro);
			for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota);
			}
			
			//FIXME: Ajustar o valor do campo para valores parametrizados
			notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais("XXXXX");
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoqueCota);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal, cota);
			notasFiscais.add(notaFiscal);
		}
	}
	
	private void gerarNotasFiscaisFornecedor(FiltroNFeDTO filtro, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao) {

		// obter as cotas que estão na tela pelo id das cotas
		List<EstoqueProduto> estoques = this.notaFiscalNdsRepository.obterConjuntoFornecedorNotafiscal(filtro);
		
		for (EstoqueProduto estoque : estoques) {
			NotaFiscal notaFiscal = new NotaFiscal();
			
			// popular distribuidor
			NotaFiscalEstoqueProdutoBuilder.popularDadosDistribuidor(notaFiscal, distribuidor, filtro);
			
			// popular header
			NotaFiscalEstoqueProdutoBuilder.montarHeaderNotaFiscal(notaFiscal, estoque);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, estoque);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os estoques
			filtro.setIdCota(estoque.getId());
			List<EstoqueProduto> estoqueProdutos = this.notaFiscalNdsRepository.obterEstoques(filtro);
			for (EstoqueProduto estoqueProduto : estoqueProdutos) {
				
				ItemNotaFiscalEstoqueProdutoBuilder.montaItemNotaFiscal(notaFiscal, estoqueProduto);
			}
			
			FaturaEstoqueProdutoNotaFiscalBuilder.montarFaturaEstoqueProdutoNotaFiscal(notaFiscal, estoqueProdutos);
			
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculadosEstoqueProduto(notaFiscal, estoque);
			
			//FIXME: Ajustar o valor do campo para valores parametrizados 
			notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais("ssss");
		}	
	}
	
	@Override
	@Transactional
	public List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaCotaExemplareSumarizado(filtro);
	}

	@Override
	@Transactional
	public Long consultaCotaExemplareSumarizadoQtd(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaCotaExemplareSumarizadoQtd(filtro);
	}
	
	// metodo responsavel pelo dados do distribuidor da nota
	public Distribuidor obterInformacaoDistribuidor(){
		return distribuidorRepository.obter();
	}
	
	public Long serieNotaFiscal (NotaFiscalBase notaFiscal){
		return serieRepository.obterNumeroSerieNota();
	}

	@Override
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaFornecedorExemplarSumarizado(filtro);
	}

	@Override
	public Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaFornecedorExemplaresSumarizadosQtd(filtro);
	}
	
}
