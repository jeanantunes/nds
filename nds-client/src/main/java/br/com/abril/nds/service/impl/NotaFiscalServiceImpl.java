package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PeriodoVO;

@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {

	@Autowired
	private NotaFiscalRepository notaFiscalDAO;
	
	@Override
	@Transactional
	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
		return notaFiscalDAO.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);
	}
	

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

		
	@Transactional
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		
	}

	@Override
	@Transactional
	public List<NotaFiscal> obterNotasFiscaisCadastradas(
			FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		PeriodoVO periodo = filtroConsultaNotaFiscal.getPeriodo(); 

		if (periodo == null || periodo.getDataInicial() == null || periodo.getDataFinal() == null) {
			throw new IllegalArgumentException("O período deve ser especificado.");
		}

		if (DateUtil.isDataFinalMaiorDataInicial(periodo)) {
			throw new IllegalArgumentException("A data inicial deve anteceder a data final.");
		}

		return notaFiscalDAO.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
	}


	@Override
	@Transactional
	public DetalheNotaFiscalDTO obterDetalhesNotaFical(Long idNotaFiscal) {
		
		if (idNotaFiscal == null) {
			throw new IllegalArgumentException("Erro inesperado. ID da nota fiscal não pode ser nulo.");
		}

		List<DetalheItemNotaFiscalDTO> itensDetalhados = notaFiscalDAO.obterDetalhesNotaFical(idNotaFiscal);

		DetalheNotaFiscalDTO detalheNotaFiscalDTO = new DetalheNotaFiscalDTO();
		
		detalheNotaFiscalDTO.setItensDetalhados(itensDetalhados);
		
		double totalExemplares = 0.0;
		double totalSumarizado = 0.0;
		
		for (DetalheItemNotaFiscalDTO item : itensDetalhados) {
			
			totalExemplares += 
					item.getQuantidadeExemplares() == null ? 0.0 : item.getQuantidadeExemplares().doubleValue();
			totalSumarizado += 
					item.getPrecoVenda() == null ? 0.0 : item.getPrecoVenda().doubleValue();
		}

		detalheNotaFiscalDTO.setTotalExemplares(new BigDecimal(totalExemplares));
		detalheNotaFiscalDTO.setValorTotalSumarizado(new BigDecimal(totalSumarizado));
		
		return detalheNotaFiscalDTO;
	}
	
	@Override
	@Transactional
	public NotaFiscal obterNotaFiscalPorNumero(String numero){
		return notaFiscalRepository.obterNotaFiscalPorNumero(numero);

	}
}



