package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.DetalhesDividaVO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.util.TipoBaixaCobranca;

@Service
public class DividaServiceImpl implements DividaService{

	@Autowired
	private DividaRepository dividaRepository;
	
	@Override
	@Transactional
	public List<StatusDividaDTO> obterInadimplenciasCota(
			FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterInadimplenciasCota(filtro);
	}

	@Override
	@Transactional
	public Long obterTotalInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterTotalInadimplenciasCota(filtro);
	}

	@Override
	@Transactional
	public Long obterTotalCotasInadimplencias(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterTotalCotasInadimplencias(filtro);
	}

	@Override
	@Transactional
	public Double obterSomaDividas(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterSomaDividas(filtro);
	}

	@Override
	@Transactional
	public List<Divida> getDividasAcumulo(Long idDivida) {
		
		List<Divida> dividas = new ArrayList<Divida>(dividaRepository.buscarPorId(idDivida).getAcumulado());
		
		for(Divida divida:dividas) {
			divida.getCobranca();
		}
		return dividas; 
	}

	@Override
	@Transactional
	public Divida obterDividaPorId(Long idDivida) {
		return dividaRepository.buscarPorId(idDivida);
	}
	
	@Override
	@Transactional
	public List<DetalhesDividaVO> obterDetalhesDivida(Long idDivida){
		
		BaixaCobranca baixaCobranca;
		Cobranca cobranca;
		DetalhesDividaVO detalhe;
		
		List<DetalhesDividaVO> detalhes = new ArrayList<DetalhesDividaVO>();
		Divida divida = this.obterDividaPorId(idDivida);
		
		//Detalhes Acumuladas
	    Set<Divida> dividasAcumuladas = divida.getAcumulado();
        for (Divida itemDivida:dividasAcumuladas){
    	   
    	    cobranca = itemDivida.getCobranca();
	   	    detalhe = new DetalhesDividaVO();
	   		
	   	    detalhe.setValor(cobranca.getValor());
	   	    detalhe.setData(cobranca.getDataEmissao());
	   	    detalhe.setTipo("Acumulado");
	   	    detalhe.setObservacao("");
	   	    
	        detalhes.add(detalhe);
        }
		
       
       
		
        //Historico !!!!
		cobranca = divida.getCobranca();
		baixaCobranca = cobranca.getBaixaCobranca();
		detalhe = new DetalhesDividaVO();
		detalhe.setValor(baixaCobranca.getValorPago());
		detalhe.setData(baixaCobranca.getDataBaixa());
	    detalhe.setTipo("Pagamento");
	    detalhe.setObservacao("");
	    
		detalhes.add(detalhe);
		//------------------
		
		
		
		
	    return detalhes;
	}

	@Override
	public void postergarCobrancaCota(List<Long> listaCodigosCotas,
			Date dataPostergacao) {
		// TODO Auto-generated method stub
		
	}

}
