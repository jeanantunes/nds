package br.com.abril.nds.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
	@Autowired
	private DescontoProximosLancamentosRepository descontoProximosLancamentosRepository;
	
	@Autowired
	private DescontoProdutoRepository descontoProdutoRepository;
	
	@Override
	@Transactional
	public List<LancamentoNaoExpedidoDTO> obterLancamentosNaoExpedidos(PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo) {
		
		List<Lancamento> lancametos =lancamentoRepository.obterLancamentosNaoExpedidos(
				paginacaoVO, data, idFornecedor, estudo);
		
		 List<LancamentoNaoExpedidoDTO> dtos = new ArrayList<LancamentoNaoExpedidoDTO>();
		
		for(Lancamento lancamento:lancametos) {
			dtos.add(montarDTOExpedicao(lancamento));
		}
		return dtos;
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
		
		Date maisRecente = lancamento.getRecebimentos().iterator().next().getRecebimentoFisico().getDataRecebimento();
		
		if(lancamento.getRecebimentos().size()>1) {
			
			Iterator<ItemRecebimentoFisico> itemFisico = lancamento.getRecebimentos().iterator();
			
			while(itemFisico.hasNext()) {
				
				ItemRecebimentoFisico item = itemFisico.next();
			
				if(maisRecente.getTime()<item.getRecebimentoFisico().getDataRecebimento().getTime()) {
					maisRecente = item.getRecebimentoFisico().getDataRecebimento();
				}
			}
		} 
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		LancamentoNaoExpedidoDTO dto = new LancamentoNaoExpedidoDTO(
				lancamento.getId(), 
				sdf.format(maisRecente), 
				lancamento.getProdutoEdicao().getProduto().getId(), 
				lancamento.getProdutoEdicao().getProduto().getNome(), 
				lancamento.getProdutoEdicao().getNumeroEdicao(), 
				lancamento.getProdutoEdicao().getProduto().getTipoProduto().getDescricao(), 
				lancamento.getProdutoEdicao().getPrecoVenda().toString().replace(".", ","), 
				lancamento.getProdutoEdicao().getPacotePadrao(), 
				lancamento.getReparte().intValue(), 
				sdf.format(lancamento.getDataRecolhimentoPrevista()), 
				fornecedor, 
				(lancamento.getEstudo()==null) ? null : lancamento.getEstudo().getQtdeReparte().intValue(),
				false);
		
		return dto;
	}

	@Transactional
	public void confirmarExpedicoes(List<Long> idLancamentos,Long idUsuario) {
		
		for( Long idLancamento:idLancamentos ) {		
			this.confirmarExpedicao(idLancamento, idUsuario);
		}
	}
	
	@Override
	@Transactional
	public void confirmarExpedicao(Long idLancamento, Long idUsuario) {
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		Expedicao expedicao = new Expedicao();
		expedicao.setDataExpedicao(new Date());
		expedicao.setResponsavel(usuario);
		expedicaoRepository.adicionar(expedicao);
		
		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		lancamento.setDataStatus(new Date());
		lancamento.setStatus(StatusLancamento.EXPEDIDO);
		lancamento.setExpedicao(expedicao);
		
		lancamentoRepository.alterar(lancamento);
		
		HistoricoLancamento historico = new HistoricoLancamento();
		historico.setDataEdicao(new Date());
		historico.setLancamento(lancamento);
		
		historico.setResponsavel(usuario);
		historico.setStatus(lancamento.getStatus());
		historico.setTipoEdicao(TipoEdicao.ALTERACAO);
		historicoLancamentoRepository.adicionar(historico);
		
		movimentoEstoqueService.gerarMovimentoEstoqueDeExpedicao(
				lancamento.getDataLancamentoPrevista(), 
				lancamento.getProdutoEdicao().getId(),
				idUsuario);
		
		
		DescontoProximosLancamentos desconto = this.descontoProximosLancamentosRepository.
		obterDescontoProximosLancamentosPor(lancamento.getProdutoEdicao().getProduto().getId(), 
				lancamento.getDataLancamentoPrevista());
				
		if (desconto != null) {	
		
			Integer quantidade = desconto.getQuantidadeProximosLancamaentos();
						
			DescontoProduto descontoProduto = new DescontoProduto();
			
			descontoProduto.setCotas(desconto.getCotas());
			descontoProduto.setDataAlteracao(new Date());
			descontoProduto.setDesconto(desconto.getValorDesconto());
			descontoProduto.setProdutoEdicao(lancamento.getProdutoEdicao());
			descontoProduto.setDistribuidor(desconto.getDistribuidor());
			descontoProduto.setUsuario(usuario);
			
			this.descontoProdutoRepository.adicionar(descontoProduto);
			
			desconto.setQuantidadeProximosLancamaentos(--quantidade);
			this.descontoProximosLancamentosRepository.alterar(desconto);
		}
	}

	@Override
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
	public Date buscarDiaUltimoBalanceamentoRecolhimentoRealizado() {
		return lancamentoRepository.buscarDiaUltimoBalanceamentoRecolhimentoRealizado();
	}
	
}
