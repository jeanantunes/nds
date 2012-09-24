package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NegociacaoDividaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class NegociacaoDividaServiceImpl implements NegociacaoDividaService{
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Override
	@Transactional
	public List<NegociacaoDividaVO> obterDividasPorCota(FiltroConsultaNegociacaoDivida filtro) {
		
		List<Cobranca> list = cobrancaRepository.obterCobrancasPorCota(new FiltroConsultaDividasCotaDTO(filtro.getNumeroCota()));
		List<NegociacaoDividaVO> listDividas = new ArrayList<NegociacaoDividaVO>();
		
		for (Cobranca c : list){
			if(filtro.isLancamento()){
				listDividas.add(new NegociacaoDividaVO(c));
			}else if(DateUtil.obterDiferencaDias(new Date(), c.getDataVencimento()) <= 0){
				listDividas.add(new NegociacaoDividaVO(c));
			}
		}
		return listDividas;
	}
	
	
}
