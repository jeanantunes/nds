package br.com.abril.nds.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.service.BancoService;

public class BancoServiceImpl implements BancoService {

	@Autowired
	private BancoRepository bancoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<Banco> obterBancos(FiltroConsultaBancosDTO filtro) {
		return bancoRepository.obterBancos(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro) {
		return 0;
	}

}
