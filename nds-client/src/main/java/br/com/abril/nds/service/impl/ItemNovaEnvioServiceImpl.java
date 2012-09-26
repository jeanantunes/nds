package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.service.ItemNotaEnvioService;

@Service
public class ItemNovaEnvioServiceImpl implements ItemNotaEnvioService {

	@Autowired
	protected ItemNotaEnvioRepository itemNotaEnvioRepository;

	@Override
	@Transactional(readOnly = true)
	public List<DetalheItemNotaFiscalDTO> obterItensNotaEnvio(Date dataEmissao, Integer numeroCota) {
		
		return itemNotaEnvioRepository.obterItensNotaEnvio(dataEmissao, numeroCota);
	}
	
}
