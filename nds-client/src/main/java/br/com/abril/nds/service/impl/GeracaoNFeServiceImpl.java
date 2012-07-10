package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ProdutoService;
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
	private FornecedorService fornecedorService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Override
	public List<CotaExemplaresDTO> busca(Intervalo<String> intervaloBox,
			Intervalo<Long> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, List<Long> listIdProduto, Long idTipoNotaFiscal, String sortname,
			String sortorder, Integer resultsPage, Integer page) {
		
		TipoNotaFiscal tipoNotaFiscal = this.tipoNotaFiscalRepository.buscarPorId(idTipoNotaFiscal);
		
		ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal = new ConsultaLoteNotaFiscalDTO();
		
		dadosConsultaLoteNotaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		dadosConsultaLoteNotaFiscal.setPeriodoMovimento(intervaloDateMovimento);
		dadosConsultaLoteNotaFiscal.setIdsCotasDestinatarias(this.cotaRepository.obterIdCotasEntre(intervalorCota));
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
	
	
}
