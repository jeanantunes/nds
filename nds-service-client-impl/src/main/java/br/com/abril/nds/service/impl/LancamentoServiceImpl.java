package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private CotaService cotaService;
	
	@Override
	@Transactional
	public List<LancamentoNaoExpedidoDTO> obterLancamentosNaoExpedidos(PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo) {
		
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(data, data);
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		List<Lancamento> lancamentos = lancamentoRepository.obterLancamentosNaoExpedidos(
				paginacaoVO, data, idFornecedor, estudo);
		
		List<LancamentoNaoExpedidoDTO> dtos = new ArrayList<LancamentoNaoExpedidoDTO>();
		
		for(Lancamento lancamento:lancamentos) {
			dtos.add(montarDTOExpedicao(lancamento));
		}
		return dtos;
	}
	
	@Override
	@Transactional
	public List<Long> obterIdsLancamentosNaoExpedidos(PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean isSaldoInsuficiente) {
		
		return lancamentoRepository.obterIdsLancamentosNaoExpedidos(paginacaoVO, data, idFornecedor, isSaldoInsuficiente);
	}
	
	@Transactional
	public Boolean existeMatrizBalanceamentoConfirmado(Date data) {
		return lancamentoRepository.existeMatrizBalanceamentoConfirmado(data);
	}
	
	private LancamentoNaoExpedidoDTO montarDTOExpedicao(Lancamento lancamento) {
	
		String fornecedor;
		
		if(lancamento.getProdutoEdicao().getProduto().getFornecedores().size()>1) {
			fornecedor = "Diversos";
		} else {
			fornecedor = lancamento.getProdutoEdicao().getProduto().getFornecedor().getJuridica().getRazaoSocial();			
		}
		
		// Conforme solicitado pelo Cesar, caso não encontre preço de venda utiliza o preço previsto
		String preco = "";
		if (lancamento.getProdutoEdicao().getPrecoVenda() != null) {
			preco = lancamento.getProdutoEdicao().getPrecoVenda().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",");
		} else if (lancamento.getProdutoEdicao().getPrecoPrevisto() != null) {
			preco = lancamento.getProdutoEdicao().getPrecoPrevisto().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",");
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto sem preço de venda e preco previsto. Codigo: " + lancamento.getProdutoEdicao().getProduto().getCodigo() + ", Edicao: " + lancamento.getProdutoEdicao().getProduto().getNome());
		}
		
		LancamentoNaoExpedidoDTO dto = 
			new LancamentoNaoExpedidoDTO(
				lancamento.getId(), 
				lancamento.getProdutoEdicao().getProduto().getCodigo(), 
				lancamento.getProdutoEdicao().getProduto().getNome(), 
				lancamento.getProdutoEdicao().getNumeroEdicao(), 
				lancamento.getProdutoEdicao().getProduto().getTipoProduto().getDescricao(), 
				preco, 
				lancamento.getProdutoEdicao().getPacotePadrao(), 
				lancamento.getReparte() != null ? lancamento.getReparte().intValue() : 0,
				DateUtil.formatarDataPTBR(lancamento.getDataRecolhimentoPrevista()), 
				fornecedor, 
				(lancamento.getEstudo()==null) ? null : lancamento.getEstudo().getQtdeReparte().intValue(),
				false,
				lancamento.getProdutoEdicao().getEstoqueProduto()!=null?lancamento.getProdutoEdicao().getEstoqueProduto().getQtde():BigInteger.ZERO);
		
		return dto;
	}

	@Override
	@Transactional
	public boolean confirmarExpedicao(Long idLancamento, Long idUsuario, Date dataOperacao, TipoMovimentoEstoque tipoMovimento, TipoMovimentoEstoque tipoMovimentoCota) {
		
		LancamentoDTO lancamento = lancamentoRepository.obterLancamentoPorID(idLancamento);
		
		Expedicao expedicao = new Expedicao();
		expedicao.setDataExpedicao(dataOperacao);
		expedicao.setResponsavel(new Usuario(idUsuario));
		Long idExpedicao = expedicaoRepository.adicionar(expedicao);
		
		expedicao.setId(idExpedicao);
		
		lancamentoRepository.alterarLancamento(idLancamento, dataOperacao, StatusLancamento.EXPEDIDO, expedicao);
				
		HistoricoLancamento historico = new HistoricoLancamento();
		historico.setDataEdicao(dataOperacao);
		historico.setLancamento(new Lancamento(idLancamento));
		
		historico.setResponsavel(new Usuario(idUsuario));
		historico.setStatusNovo(StatusLancamento.EXPEDIDO);
		historico.setTipoEdicao(TipoEdicao.ALTERACAO);
		
		//TODO: geração de historico desativada devido a criação de trigger para realizar essa geração.
		//historicoLancamentoRepository.adicionar(historico);
		
		
		movimentoEstoqueService.gerarMovimentoEstoqueDeExpedicao(lancamento.getDataPrevista(), lancamento.getDataDistribuidor(), 
				lancamento.getIdProduto(), lancamento.getIdProdutoEdicao(), idLancamento, idUsuario, dataOperacao, tipoMovimento, tipoMovimentoCota);
		
				
		
		return true;
	}

	@Override
	@Transactional
	public Lancamento obterPorId(Long idLancamento) {
		return lancamentoRepository.buscarPorId(idLancamento);
	}

	@Override
	@Transactional
	public Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor, Boolean estudo) {
		return lancamentoRepository.obterTotalLancamentosNaoExpedidos(data, idFornecedor, estudo);
	}

	@Override
	@Transactional(readOnly=true)
	public Long quantidadeLancamentoInformeRecolhimento(Long idFornecedor,
			Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento) {
		return lancamentoRepository.quantidadeLancamentoInformeRecolhimento(
				idFornecedor, dataInicioRecolhimento, dataFimRecolhimento);
	}

	@Override
	@Transactional(readOnly=true)
	public List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento, String orderBy, Ordenacao ordenacao,
			Integer initialResult, Integer maxResults) {
		return lancamentoRepository.obterLancamentoInformeRecolhimento(
				idFornecedor, dataInicioRecolhimento, dataFimRecolhimento,
				orderBy, ordenacao, initialResult, maxResults);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Lancamento obterUltimoLancamentoDaEdicao(Long idProdutoEdicao) {
		
		if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da Edição é inválido!");
		}
		
		return lancamentoRepository.obterUltimoLancamentoDaEdicao(idProdutoEdicao);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Lancamento obterPrimeiroLancamentoDaEdicao(Long idProdutoEdicao) {
		
		if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da Edição é inválido!");
		}
		
		return lancamentoRepository.obterPrimeiroLancamentoDaEdicao(idProdutoEdicao);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Lancamento obterUltimoLancamentoDaEdicaoParaCota(Long idProdutoEdicao, Long idCota) {
		
		if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da Edição é inválido!");
		}
		
		if (idCota == null || Long.valueOf(0).equals(idCota)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da Cota é inválido!");
		}
		
		return lancamentoRepository.obterUltimoLancamentoDaEdicaoParaCota(idProdutoEdicao, idCota);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento) {
		return lancamentoRepository.obterLancamentoInformeRecolhimento(
				idFornecedor, dataInicioRecolhimento, dataFimRecolhimento);
	}

	@Override
	@Transactional(readOnly=true)
	public Date buscarUltimoBalanceamentoLancamentoRealizadoDia(Date dataOperacao) {
		return lancamentoRepository.buscarUltimoBalanceamentoLancamentoRealizadoDia(dataOperacao);
	}

	@Override
	@Transactional(readOnly=true)
	public Date buscarDiaUltimoBalanceamentoLancamentoRealizado() {
		return lancamentoRepository.buscarDiaUltimoBalanceamentoLancamentoRealizado();
	}

	@Override
	@Transactional(readOnly=true)
	public Date buscarUltimoBalanceamentoRecolhimentoRealizadoDia(Date dataOperacao) {
		return lancamentoRepository.buscarUltimoBalanceamentoRecolhimentoRealizadoDia(dataOperacao);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Lancamento> obterLancamentoDataDistribuidorInStatus(Date dataRecebimentoDistribuidor, List<StatusLancamento> status){
		return lancamentoRepository.obterLancamentoDataDistribuidorInStatus(dataRecebimentoDistribuidor, status);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public Date buscarDiaUltimoBalanceamentoRecolhimentoRealizado() {
		return lancamentoRepository.buscarDiaUltimoBalanceamentoRecolhimentoRealizado();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Lancamento> obterLancamentosEdicao(Long idProdutoEdicao, String sortorder, String sortname) {
		
		Boolean isEdicaoParcial = produtoEdicaoRepository.isEdicaoParcial(idProdutoEdicao);
		
		if(isEdicaoParcial){
			
			return lancamentoRepository.obterLancamentosParcialEdicao(idProdutoEdicao, sortorder, sortname);
		}
		
		return lancamentoRepository.obterLancamentosEdicao(idProdutoEdicao, sortorder, sortname);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existeMatrizRecolhimentoConfirmado(Date dataChamadao) {
		
		return this.lancamentoRepository.existeMatrizRecolhimentoConfirmado(dataChamadao);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Date getMaiorDataLancamento(Long idProdutoEdicao) {
		
		Date maiorDataLancamentoPrevisto = 
			this.lancamentoRepository.getMaiorDataLancamentoPrevisto(idProdutoEdicao);
		
		Date maiorDataLancamentoDistribuidor =
			this.lancamentoRepository.getMaiorDataLancamentoDistribuidor(idProdutoEdicao);
		
		if (maiorDataLancamentoPrevisto == null || maiorDataLancamentoDistribuidor == null) {
		
			return null;
		}
		
		if (maiorDataLancamentoPrevisto.after(maiorDataLancamentoDistribuidor)) {
			
			return maiorDataLancamentoPrevisto;
		} else {
			
			return maiorDataLancamentoDistribuidor;
		}
	}
	
}