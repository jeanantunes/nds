package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.TipoNotaFiscalService;
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
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository conferenciaEncalheCotaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Override
	@Transactional
	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
		return notaFiscalDAO.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);
	}

	@Autowired
	private NotaFiscalEntradaRepository notaFiscalRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void inserirNotaFiscal(NotaFiscalEntradaCota notaFiscal, Integer numeroCota, Long idControleConferenciaEncalheCota) {

		if (notaFiscal == null) {

			throw new IllegalArgumentException("Erro inesperado. Nota Fiscal não definida.");
		}

		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		TipoAtividade tipoAtividade = this.distribuidorService.obter().getTipoAtividade();

		GrupoNotaFiscal grupoNotaFiscal = GrupoNotaFiscal.NF_TERCEIRO;
		
		boolean isContribuinte = true;

		notaFiscal.setCota(cota);
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setOrigem(Origem.MANUAL);
		notaFiscal.setValorBruto(BigDecimal.ZERO);
		notaFiscal.setValorLiquido(BigDecimal.ZERO);
		notaFiscal.setValorDesconto(BigDecimal.ZERO);
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);

		if (idControleConferenciaEncalheCota != null) {

			ControleConferenciaEncalheCota conferenciaEncalheCota = this.conferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);

			if (conferenciaEncalheCota != null 
					&& conferenciaEncalheCota.getNotaFiscalEntradaCota() != null 
					&& !conferenciaEncalheCota.getNotaFiscalEntradaCota().isEmpty()) {
				
				grupoNotaFiscal = GrupoNotaFiscal.NF_TERCEIRO_COMPLEMENTAR;
			}

			notaFiscal.setControleConferenciaEncalheCota(conferenciaEncalheCota);
		}
		
		TipoNotaFiscal tipoNotaFiscal = this.tipoNotaFiscalRepository.obterTipoNotaFiscal(grupoNotaFiscal, tipoAtividade, isContribuinte);

		if (tipoNotaFiscal == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo da nota fiscal não foi encontrado.");
		}
		
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);

		this.notaFiscalRepository.adicionar(notaFiscal); 
	}

	@Transactional
	public Cota obterPorNumerDaCota(Integer numeroCota) {
		return this.cotaRepository.obterPorNumerDaCota(numeroCota); 
	}

	@Transactional
	public Fornecedor obterFornecedorPorID(Long idFornecedor) {
		return this.fornecedorRepository.buscarPorId(idFornecedor); 
	}

	private void validarPeriodo(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
	
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

		
	}

	@Override
	@Transactional
	public List<ItemDTO<Long, String>> obterFornecedorNotaFiscal(List<Long> listaIdNotaFiscal) {

		return notaFiscalDAO.obterListaFornecedorNotaFiscal(listaIdNotaFiscal);

	}
	
	
	@Override
	@Transactional
	public List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
		
		validarPeriodo(filtroConsultaNotaFiscal);

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
		
		BigInteger totalExemplares = BigInteger.ZERO;
		BigDecimal totalSumarizado = BigDecimal.ZERO;
		
		totalSumarizado = totalSumarizado.setScale(2, BigDecimal.ROUND_DOWN);
		
		for (DetalheItemNotaFiscalDTO item : itensDetalhados) {
			
			totalExemplares = totalExemplares.add(item.getQuantidadeExemplares() == null ? BigInteger.ZERO : item.getQuantidadeExemplares());
			totalSumarizado = totalSumarizado.add(item.getPrecoVenda() == null ? BigDecimal.ZERO : item.getValorTotal().setScale(2, BigDecimal.ROUND_DOWN));
			/*if (item.getPrecoVenda() != null && item.getDesconto() != null) {
				totalSumarizado = totalSumarizado.subtract(item.getValorTotal().multiply(item.getDesconto()));
			}*/
		}

		detalheNotaFiscalDTO.setTotalExemplares(totalExemplares);
		detalheNotaFiscalDTO.setValorTotalSumarizado(totalSumarizado);
		
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



