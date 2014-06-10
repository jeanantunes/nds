package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Condicao;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.util.Intervalo;

@Service
public class GeracaoNFeServiceImpl implements GeracaoNFeService {
	
	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	@Transactional
	public List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, Long idTipoNotaFiscal, Long idRoteiro, Long idRota,
			String sortname, String sortorder, Integer resultsPage, Integer page, SituacaoCadastro situacaoCadastro) {
		
		Set<TipoNotaFiscal> tiposNota = new HashSet<TipoNotaFiscal>();
		
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
		
		List<CotaExemplaresDTO> listaCotaExemplares = new ArrayList<CotaExemplaresDTO>();
		
		for (Entry<Cota, QuantidadePrecoItemNotaDTO> entry : cotasTotalItens.entrySet()) {
			
			CotaExemplaresDTO cotaExemplares = new CotaExemplaresDTO();
			
			cotaExemplares.setIdCota(entry.getKey().getId());
			cotaExemplares.setExemplares(entry.getValue().getQuantidade().longValue());
			cotaExemplares.setNomeCota(entry.getKey().getPessoa().getNome());
			cotaExemplares.setNumeroCota(entry.getKey().getNumeroCota());
			cotaExemplares.setTotal(entry.getValue().getPreco());
			cotaExemplares.setTotalDesconto(entry.getValue().getPrecoComDesconto());
			
			listaCotaExemplares.add(cotaExemplares);
			
		}
		
		return listaCotaExemplares;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.GeracaoNFeService#gerarNotaFiscal(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, java.util.List, java.util.List, java.lang.Long)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void gerarNotaFiscal(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, List<Long> listIdProduto,
			Long idTipoNotaFiscal, Date dataEmissao, List<Long> idCotasSuspensas, Condicao condicao) throws FileNotFoundException, IOException {
		
		List<Long> listaIdCota = this.cotaRepository.obterIdCotasEntre(intervalorCota, intervaloBox, null, null, null, null, null, null, null);
		
		List<TipoNotaFiscal> tiposNotaFiscal = new ArrayList<TipoNotaFiscal>();
		
		if (idTipoNotaFiscal == null){
			
			tiposNotaFiscal.addAll(
					this.tipoNotaFiscalRepository.obterTiposNotasFiscaisCotasNaoContribuintesPor(
							this.distribuidorRepository.tipoAtividade()));
		} else {
			
			tiposNotaFiscal.add(this.tipoNotaFiscalRepository.buscarPorId(idTipoNotaFiscal));
		}
		
		List<NotaFiscal> listaNotaFiscal = new ArrayList<NotaFiscal>();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = 
				this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
		
		for (Long idCota : listaIdCota) {
			//TRY adicionado para em caso de erro em alguma nota, não parar o fluxo das demais nos testes.
			//Remove-lo ou trata-lo com Logs
//			try {
				
				Cota cota = this.cotaRepository.buscarPorId(idCota);
				
				if (SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())) {
					
					if (idCotasSuspensas != null && !idCotasSuspensas.isEmpty()) {
						if (!idCotasSuspensas.contains(cota.getId())) {
							continue;
						}
					} else {
						continue;
					}
				}
				
				for (TipoNotaFiscal tipoNota : tiposNotaFiscal){
					
					List<ItemNotaFiscalSaida> listItemNotaFiscal = this.notaFiscalService.obterItensNotaFiscalPor(
							parametrosRecolhimentoDistribuidor, 
							cota, intervaloDateMovimento, listIdFornecedor, listIdProduto, tipoNota);
					
					if (listItemNotaFiscal == null || listItemNotaFiscal.isEmpty()) 
						continue;
					
					List<NotaFiscalReferenciada> listaNotasFiscaisReferenciadas = this.notaFiscalService.obterNotasReferenciadas(listItemNotaFiscal);
					
					InformacaoTransporte transporte = this.notaFiscalService.obterTransporte(idCota);
					
					Set<Processo> processos = new HashSet<Processo>();
					processos.add(Processo.GERACAO_NF_E);
					
					Long idNotaFiscal = this.notaFiscalService.emitiNotaFiscal(tipoNota.getId(), dataEmissao, cota, 
							listItemNotaFiscal, transporte, null, listaNotasFiscaisReferenciadas, processos, condicao);
					
					NotaFiscal notaFiscal = this.notaFiscalRepository.buscarPorId(idNotaFiscal);
					
					this.produtoServicoRepository.atualizarProdutosQuePossuemNota(notaFiscal.getProdutosServicos(), listItemNotaFiscal);
					
					listaNotaFiscal.add(notaFiscal);
				}
//			} catch (Exception exception) {
//				LOGGER.warn(exception.getLocalizedMessage(), exception);
//				continue;
//			}
		}
		
		if(listaNotaFiscal == null || listaNotaFiscal.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados itens para gerar nota.");
		
		this.notaFiscalService.exportarNotasFiscais(listaNotaFiscal);
	}
	
}
