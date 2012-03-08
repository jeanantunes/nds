package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ExpedicaoRepository;
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
	
	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
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

	private LancamentoNaoExpedidoDTO montarDTOExpedicao(Lancamento lancamento) {
		
		String fornecedor;
		
		if(lancamento.getProdutoEdicao().getProduto().getFornecedores().size()>1) {
			fornecedor = "Diversos";
		} else {
			fornecedor = lancamento.getProdutoEdicao().getProduto().getFornecedor().getJuridica().getRazaoSocial();			
		}
		
		if(lancamento.getRecebimentos().size()>1) {
			
			Date maisRecente = lancamento.getRecebimentos().iterator().next().getRecebimentoFisico().getDataRecebimento();
			
			Iterator<ItemRecebimentoFisico> itemFisico = lancamento.getRecebimentos().iterator();
			
			while(itemFisico.next()) {
				//if(maisRecente.getTime()<itemFisico.)
			}
		}
		
		LancamentoNaoExpedidoDTO dto = new LancamentoNaoExpedidoDTO(
				lancamento.getId(), 
				lancamento.getRecebimentos().iterator().next().getRecebimentoFisico().getDataRecebimento(), 
				lancamento.getProdutoEdicao().getProduto().getId(), 
				lancamento.getProdutoEdicao().getProduto().getNome(), 
				lancamento.getProdutoEdicao().getNumeroEdicao(), 
				lancamento.getProdutoEdicao().getProduto().getTipoProduto().getDescricao(), 
				lancamento.getProdutoEdicao().getPrecoVenda(), 
				lancamento.getProdutoEdicao().getPacotePadrao(), 
				(lancamento.getEstudo()==null)? null : lancamento.getEstudo().getQtdeReparte(), 
				lancamento.getDataRecolhimentoPrevista(), 
				fornecedor, 
				lancamento.getEstudo().getQtdeReparte());
		
		return dto;
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
		historico.setData(new Date());
		historico.setLancamento(lancamento);
		
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
