package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalService;

@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
		
	@Transactional
	public List<NotaFiscal> buscarNotaFiscal(Date emissao, Date entradaRecebimento,Long valorBruto, Long valorLiquido, Long valorDescontado, String tipoNota, String cfop) {
		return notaFiscalRepository.buscarNotaFiscal(emissao, entradaRecebimento, valorBruto, valorLiquido, valorDescontado, tipoNota, cfop);
				
	}
	
	@Transactional
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		
	}
}
	

