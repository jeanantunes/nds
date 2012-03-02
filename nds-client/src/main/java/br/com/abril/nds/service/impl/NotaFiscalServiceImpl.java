package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
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
	public List<NotaFiscalFornecedor> obterNotasFiscaisCadastradas(
			FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		PeriodoVO periodo = filtroConsultaNotaFiscal.getPeriodo(); 

		if (periodo == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "O preenchimento do período é obrigatório.");
		} 
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.ERROR);
		
		List<String> mensagens = new ArrayList<String>();
		
		if (periodo.getDataInicial() == null) {
		
			mensagens.add("O preenchimento do campo \"Data Inicial\" é obrigatório");
		} 
		
		if (periodo.getDataFinal() == null) {
		
			mensagens.add("O preenchimento do campo \"Data Final\" é obrigatório");
		}

		if (!mensagens.isEmpty()) {

			validacao.setListaMensagens(mensagens);
			
			throw new ValidacaoException(validacao);
		}
		
		if (DateUtil.isDataFinalMaiorDataInicial(periodo)) {

			throw new ValidacaoException(TipoMensagem.ERROR, "A data inicial deve anteceder a data final.");
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
					item.getPrecoVenda() == null ? 0.0 : item.getValorTotal().doubleValue();
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
	
	@Override
	@Transactional
	public List<NotaFiscal> obterNotaFiscalPorNumeroSerieCnpj(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal){
		if(filtroConsultaNotaFiscal.getNumeroNota() == null || filtroConsultaNotaFiscal.getSerie() == null || filtroConsultaNotaFiscal.getCnpj() == null){
			throw new IllegalArgumentException("Todos os dados são obrigatórios");
		}
		return notaFiscalRepository.obterNotaFiscalPorNumeroSerieCnpj(filtroConsultaNotaFiscal);
	}
}



