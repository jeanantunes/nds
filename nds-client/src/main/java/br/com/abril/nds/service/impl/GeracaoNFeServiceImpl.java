package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.repository.CotaRepository;
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
	
	@Override
	@Transactional
	public List<CotaExemplaresDTO> busca(Intervalo<String> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, List<Long> listIdProduto, Long idTipoNotaFiscal, String sortname,
			String sortorder, Integer resultsPage, Integer page) {
		
		TipoNotaFiscal tipoNotaFiscal = this.tipoNotaFiscalRepository.buscarPorId(idTipoNotaFiscal);
		
		ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal = new ConsultaLoteNotaFiscalDTO();
		
		dadosConsultaLoteNotaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		dadosConsultaLoteNotaFiscal.setPeriodoMovimento(intervaloDateMovimento);
		dadosConsultaLoteNotaFiscal.setIdsCotasDestinatarias(this.cotaRepository.obterIdCotasEntre(intervalorCota, intervaloBox, SituacaoCadastro.ATIVO));
		dadosConsultaLoteNotaFiscal.setListaIdProdutos(listIdProduto);
		dadosConsultaLoteNotaFiscal.setListaIdFornecedores(listIdFornecedor);
		
		Map<Long, Integer> idCotaTotalItens = this.notaFiscalService.obterTotalItensNotaFiscalPorCotaEmLote(dadosConsultaLoteNotaFiscal);
		
		Set<Long> idCotas = idCotaTotalItens.keySet();
		
		List<CotaExemplaresDTO> listaCotaExemplares = new ArrayList<CotaExemplaresDTO>();
		
		for (Long idCota : idCotas) {
			
			Cota cota = this.cotaRepository.buscarPorId(idCota);
			
			CotaExemplaresDTO cotaExemplares = new CotaExemplaresDTO();
			
			cotaExemplares.setIdCota(idCota);
			cotaExemplares.setExemplares((long)idCotaTotalItens.get(idCota));
			cotaExemplares.setNomeCota(cota.getPessoa().getNome());
			cotaExemplares.setNumeroCota(cota.getNumeroCota());
			
			listaCotaExemplares.add(cotaExemplares);
		}
		
		return listaCotaExemplares;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.GeracaoNFeService#gerarNotaFiscal(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, java.util.List, java.util.List, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void gerarNotaFiscal(Intervalo<String> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, List<Long> listIdProduto,
			Long idTipoNotaFiscal, Date dataEmissao) {
		
		Set<Long> listaIdCota = this.cotaRepository.obterIdCotasEntre(intervalorCota,intervaloBox,SituacaoCadastro.ATIVO);
		
		TipoNotaFiscal tipoNotaFiscal = this.tipoNotaFiscalRepository.buscarPorId(idTipoNotaFiscal);
		
		GrupoNotaFiscal grupoNotaFiscal = tipoNotaFiscal.getGrupoNotaFiscal();
		
		List<NotaFiscal> listaNotaFiscal = new ArrayList<NotaFiscal>();
		
		for (Long idCota : listaIdCota) {
		
			List<ItemNotaFiscal> listItemNotaFiscal = this.notaFiscalService.obterItensNotaFiscalPor(
					grupoNotaFiscal, idCota, intervaloDateMovimento, listIdFornecedor, listIdProduto);
			
			//FIXME: obter informacaoTransporte
			InformacaoTransporte transporte = null;
			
			this.notaFiscalService.emitiNotaFiscal(idTipoNotaFiscal, dataEmissao, idCota, 
					listItemNotaFiscal, transporte, null);
		}
		
	}
	
	
}
