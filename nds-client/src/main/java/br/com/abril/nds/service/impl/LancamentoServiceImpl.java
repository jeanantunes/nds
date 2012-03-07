package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoService;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MovimentoService movimentoService;
	
	@Override
	@Transactional
	public List<LancamentoNaoExpedidoDTO> obterLancamentosNaoExpedidos(PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo) {
		return lancamentoRepository.obterLancamentosNaoExpedidos(paginacaoVO, data, idFornecedor, estudo);
	}

	@Override
	@Transactional
	public void confirmarExpedicao(Long idLancamento, Long idUsuario) {
		
		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		lancamento.setDataStatus(new Date());
		lancamento.setStatus(StatusLancamento.EXPEDIDO);
		
		lancamentoRepository.alterar(lancamento);
		
		HistoricoLancamento historico = new HistoricoLancamento();
		historico.setData(new Date());
		historico.setLancamento(lancamento);
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		historico.setResponsavel(usuario);
		historico.setStatus(lancamento.getStatus());
		historicoLancamentoRepository.adicionar(historico);
		
		movimentoService.gerarMovimentoEstoqueDeExpedicao(
				lancamento.getDataLancamentoPrevista(), 
				lancamento.getProdutoEdicao().getId(),
				idUsuario);
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
}
