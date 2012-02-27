package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalDAO;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalVO;

@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {

	@Autowired
	private NotaFiscalDAO notaFiscalDAO;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

		
	@Transactional
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		
	}

	
	@Override
	@Transactional
	public List<NotaFiscal> obterNotasFiscaisCadastradas(
			FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal) {
		return notaFiscalDAO.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
	}
	
	@Override
	@Transactional
	public NotaFiscal obterNotaFiscalPorNumero(String numero){
		return notaFiscalRepository.obterNotaFiscalPorNumero(numero);
	}
}


