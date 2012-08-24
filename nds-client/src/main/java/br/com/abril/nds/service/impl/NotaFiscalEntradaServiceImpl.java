package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class NotaFiscalEntradaServiceImpl implements NotaFiscalEntradaService {

	@Autowired
	private NotaFiscalEntradaRepository notaFiscalDAO;
	
	@Autowired
	private CFOPRepository cfopRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Override
	@Transactional
	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
		return notaFiscalDAO.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);
	}
	

	@Autowired
	private NotaFiscalEntradaRepository notaFiscalRepository;

		
	@Transactional
	public void inserirNotaFiscal(NotaFiscalEntrada notaFiscal){
		this.notaFiscalRepository.inserirNotaFiscal(notaFiscal); 
	}

	@Override
	@Transactional
	public List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(
			FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		PeriodoVO periodo = filtroConsultaNotaFiscal.getPeriodo(); 

		if (periodo == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do período é obrigatório.");
		} 
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.WARNING);
		
		List<String> mensagens = new ArrayList<String>();
		
		if (!mensagens.isEmpty()) {

			validacao.setListaMensagens(mensagens);
			
			throw new ValidacaoException(validacao);
		}
		
		Calendar data = Calendar.getInstance();
		
		if (periodo.getDataInicial() != null && periodo.getDataFinal() != null) {
			
			if (DateUtil.isDataInicialMaiorDataFinal(periodo.getDataInicial(), periodo.getDataFinal())) {

				throw new ValidacaoException(TipoMensagem.WARNING, "A data inicial deve anteceder a data final.");
			}
			
			data.setTime(periodo.getDataFinal());
			
			data.add(Calendar.DAY_OF_MONTH, 1);
			
			periodo.setDataFinal(DateUtil.removerTimestamp(data.getTime()));

		} else {

			periodo.setDataInicial(DateUtil.removerTimestamp(data.getTime()));

			data.add(Calendar.DAY_OF_MONTH, 1);

			periodo.setDataFinal(DateUtil.removerTimestamp(data.getTime()));
		}

		return notaFiscalDAO.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
	}


	@Override
	@Transactional
	public DetalheNotaFiscalDTO obterDetalhesNotaFical(Long idNotaFiscal) {
		
		if (idNotaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro inesperado. ID da nota fiscal não pode ser nulo.");
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
	public NotaFiscalEntrada obterNotaFiscalPorNumero(String numero){
		return notaFiscalRepository.obterNotaFiscalPorNumero(numero);

	}
	
	@Override
	@Transactional
	public List<NotaFiscalEntrada> obterNotaFiscalPorNumeroSerieCnpj(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal){
		if(filtroConsultaNotaFiscal.getNumeroNota() == null || filtroConsultaNotaFiscal.getSerie() == null || filtroConsultaNotaFiscal.getCnpj() == null){
			throw new IllegalArgumentException("Todos os dados são obrigatórios");
		}
		return notaFiscalRepository.obterNotaFiscalPorNumeroSerieCnpj(filtroConsultaNotaFiscal);
	}

	
}



