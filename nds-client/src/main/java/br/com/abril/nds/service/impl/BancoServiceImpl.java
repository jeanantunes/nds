package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.service.BancoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class BancoServiceImpl implements BancoService {
	
	@Autowired
	private BancoRepository bancoRepository;
	
	/**
	 * Método responsavel por obter bancos cadastrados
	 * @param filtro: parametros de busca
	 */
	@Override
	public List<Banco> obterBancos(FiltroConsultaBancosDTO filtro) {
		return bancoRepository.obterBancos(filtro);
	}

	/**
	 * Método responsavel por obter a quantidade bancos cadastrados
	 * @param filtro: parametros de busca
	 */
	@Override
	public long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro) {
		return bancoRepository.obterQuantidadeBancos(filtro);
	}

}
