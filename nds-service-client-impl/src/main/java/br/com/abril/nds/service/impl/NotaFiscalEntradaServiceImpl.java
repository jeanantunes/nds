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
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaFiscalEntradaFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class NotaFiscalEntradaServiceImpl implements NotaFiscalEntradaService {

	@Autowired
	private NotaFiscalEntradaRepository notaFiscalEntradaRepository;
	
	@Autowired
	private CFOPRepository cfopRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private NaturezaOperacaoRepository naturezaOperacaoRepository;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private NaturezaOperacaoService naturezaOperacaoService;
	
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
		return notaFiscalEntradaRepository.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);
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
		
		TipoAtividade tipoAtividade = this.distribuidorService.tipoAtividade();

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

			notaFiscal.setControleConferenciaEncalheCota(conferenciaEncalheCota);
			
		}
		
		//FIXME: Ajustar a funcionalidade de NF-e de Terceiros 
		NaturezaOperacao naturezaOperacao = this.naturezaOperacaoService.obterNaturezaOperacao(tipoAtividade, TipoEmitente.COTA, TipoDestinatario.DISTRIBUIDOR, TipoOperacao.ENTRADA);

		if (naturezaOperacao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de nota fiscal não foi encontrado.");
		}
		
		notaFiscal.setNaturezaOperacao(naturezaOperacao);

		this.notaFiscalRepository.adicionar(notaFiscal); 
	}

	@Transactional
	public Cota obterPorNumerDaCota(Integer numeroCota) {
		return this.cotaRepository.obterPorNumeroDaCota(numeroCota); 
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

		return notaFiscalEntradaRepository.obterListaFornecedorNotaFiscal(listaIdNotaFiscal);

	}
	
	
	@Override
	@Transactional
	public List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
		
		validarPeriodo(filtroConsultaNotaFiscal);

		return notaFiscalEntradaRepository.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
	}

	@Override
	@Transactional(readOnly=true)
	public List<NotaFiscalEntradaFornecedorDTO> obterNotasFiscaisCadastradasDTO(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
		
		validarPeriodo(filtroConsultaNotaFiscal);

		return notaFiscalEntradaRepository.obterNotasFiscaisCadastradasDTO(filtroConsultaNotaFiscal);
	}
	

	@Override
	@Transactional
	public DetalheNotaFiscalDTO obterDetalhesNotaFical(Long idNotaFiscal, PaginacaoVO paginacao) {
		
		if (idNotaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro inesperado. ID da nota fiscal não pode ser nulo.");
		}

		List<DetalheItemNotaFiscalDTO> itensDetalhados = notaFiscalEntradaRepository.obterDetalhesNotaFical(idNotaFiscal, paginacao);

		DetalheNotaFiscalDTO detalheNotaFiscalDTO = new DetalheNotaFiscalDTO();
		
		detalheNotaFiscalDTO.setItensDetalhados(itensDetalhados);
		
		BigInteger totalExemplares = BigInteger.ZERO;
		BigDecimal totalSumarizado = BigDecimal.ZERO;
		BigDecimal totalSumarizadoComDesconto = BigDecimal.ZERO;
		
		for (DetalheItemNotaFiscalDTO item : itensDetalhados) {
			
			BigDecimal valorTotal =
				(item.getValorTotal() == null) ? BigDecimal.ZERO : item.getValorTotal();

			totalExemplares = totalExemplares.add(item.getQuantidadeExemplares() == null ? BigInteger.ZERO : item.getQuantidadeExemplares());
			totalSumarizado = totalSumarizado.add(item.getPrecoVenda() == null ? BigDecimal.ZERO : MathUtil.round(valorTotal, 2));
			totalSumarizadoComDesconto = totalSumarizadoComDesconto.add(item.getValorTotalComDesconto());
		}

		detalheNotaFiscalDTO.setTotalExemplares(totalExemplares);
		detalheNotaFiscalDTO.setValorTotalSumarizado(MathUtil.round(totalSumarizado, 2));
		detalheNotaFiscalDTO.setValorTotalSumarizadoComDesconto(MathUtil.round(totalSumarizadoComDesconto, 4));
		
		return detalheNotaFiscalDTO;
	}
	
	
	@Override
	@Transactional
	public List<NotaFiscalEntrada> obterNotaFiscalEntrada(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal){
		
		return notaFiscalRepository.obterNotaFiscalEntrada(filtroConsultaNotaFiscal);
	
	}

	@Override
	@Transactional
	public boolean existeNotaFiscalEntradaFornecedor(Long numeroNotaEnvio, Long idPessoaJuridica, Date dataEmissao) {
		
		return this.notaFiscalEntradaRepository.existeNotaFiscalEntradaFornecedor(numeroNotaEnvio, idPessoaJuridica, dataEmissao);
	}
	
	@Override
	@Transactional
	public void excluirNotasFiscaisPorReabertura(final InfoConferenciaEncalheCota infoConfereciaEncalheCota) {
		if(infoConfereciaEncalheCota.getNotaFiscalEntradaCota() != null) {
			this.notaFiscalEntradaRepository.remover(infoConfereciaEncalheCota.getNotaFiscalEntradaCota());			
		}
		
	}
}