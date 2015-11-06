package br.com.abril.nds.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.repository.EstornoNFERepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.EstornoNFEService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;

@Service
public class EstornoNFEServiceImpl implements EstornoNFEService {
	
    @Autowired
    private ParametroSistemaService parametroSistemaService;
    
    @Autowired
    private DistribuidorService distribuidorService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstornoNFERepository estornoNFERepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EstornoNFEServiceImpl.class);
	
	@Override
	@Transactional
	public void estornoNotaFiscal(Long id) {
		
		
	}
	
	@Override
	@Transactional
	public Long quantidade(FiltroMonitorNfeDTO filtro) {
		
		return estornoNFERepository.quantidade(filtro);
	}
	
	@Override
	@Transactional
	public List<NotaFiscalDTO> pesquisar(FiltroMonitorNfeDTO filtro) {
		
		return estornoNFERepository.obterListaNotasFiscaisEstorno(filtro);
		
	}
}