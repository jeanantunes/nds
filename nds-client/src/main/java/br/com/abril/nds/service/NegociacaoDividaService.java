package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.NegociacaoDividaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;

public interface NegociacaoDividaService {
	
	List<NegociacaoDividaVO> obterDividasPorCota(FiltroConsultaNegociacaoDivida filtro);
}
