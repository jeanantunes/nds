package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class ParciaisServiceImpl implements ParciaisService{
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	
	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Override
	@Transactional
	public void gerarPeriodosParcias(ProdutoEdicao produtoEdicao, Integer qtdePeriodos, Long idUsuario) {
		
		LancamentoParcial lancamentoParcial = obterLancamentoParcialValidado(produtoEdicao, qtdePeriodos);		
		
		Lancamento ultimoLancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
		
		Integer fatorRelancamentoParcial = distribuidorService.obter().getFatorRelancamentoParcial();
		
		Integer peb = produtoEdicao.getPeb(); 
		
		Date dtLancamento = null;
		Date dtRecolhimento = null;
		
		for(int i=0; i<qtdePeriodos; i++) {
		
			if(ultimoLancamento == null) {
				dtLancamento = lancamentoParcial.getLancamentoInicial();			
			} else {
				dtLancamento = calendarioService.adicionarDiasRetornarDiaUtil(ultimoLancamento.getDataRecolhimentoDistribuidor(), fatorRelancamentoParcial) ;
			}
			
			if(DateUtil.obterDiferencaDias(dtLancamento, lancamentoParcial.getRecolhimentoFinal()) > 0) {
				break;
			}			
			
			dtRecolhimento =  calendarioService.adicionarDiasRetornarDiaUtil(dtLancamento,peb); 
			
			if(DateUtil.obterDiferencaDias(dtRecolhimento, lancamentoParcial.getRecolhimentoFinal()) > 0) {
				i = qtdePeriodos;
				dtRecolhimento = lancamentoParcial.getRecolhimentoFinal();
			}
			
			Lancamento novoLancamento =  gerarLancamento(produtoEdicao, dtLancamento, dtRecolhimento);
			
			HistoricoLancamento novoHistorico = gerarHistoricoLancamento(novoLancamento, idUsuario);
			
			PeriodoLancamentoParcial novoPeriodo = gerarPeriodoParcial(dtLancamento, dtRecolhimento, lancamentoParcial);
			
			lancamentoRepository.merge(novoLancamento);
			historicoLancamentoRepository.merge(novoHistorico);
			periodoLancamentoParcialRepository.merge(novoPeriodo);
			
			ultimoLancamento = novoLancamento;
		}
	}

	private PeriodoLancamentoParcial gerarPeriodoParcial(Date dtLancamento, Date dtRecolhimento, LancamentoParcial lancamentoParcial) {
		
		PeriodoLancamentoParcial periodo = new PeriodoLancamentoParcial();
		periodo.setLancamento(dtLancamento);
		periodo.setRecolhimento(dtRecolhimento);
		periodo.setLancamentoParcial(lancamentoParcial);
		periodo.setTipo(TipoLancamentoParcial.PARCIAL);
		periodo.setStatus(StatusLancamentoParcial.PROJETADO);
		
		return periodo;
	}

	private HistoricoLancamento gerarHistoricoLancamento(Lancamento lancamento, Long idUsuario) {

		HistoricoLancamento historico = new HistoricoLancamento();
		historico.setLancamento(lancamento);
		historico.setTipoEdicao(TipoEdicao.INCLUSAO);
		historico.setDataEdicao(new Date());
		historico.setResponsavel(usuarioRepository.buscarPorId(idUsuario));
		
		return historico;		
	}

	private Lancamento gerarLancamento(ProdutoEdicao produtoEdicao, Date dtLancamento, Date dtRecolhimento) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setTipoLancamento(TipoLancamento.PARCIAL);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dtLancamento);
		lancamento.setDataRecolhimentoPrevista(dtRecolhimento);
		lancamento.setDataCriacao(new Date());
		lancamento.setDataStatus(new Date());
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		
		return lancamento;		
	}

	private LancamentoParcial obterLancamentoParcialValidado(ProdutoEdicao produtoEdicao, Integer qtdePeriodos) {
		
		if(qtdePeriodos == null || qtdePeriodos<=0)
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de periodos informada deve ser maior que zero.");
		
		
		
		if(!produtoEdicao.getProduto().isParcial())
			throw new ValidacaoException(TipoMensagem.WARNING, "ProdutoEdicao não é parcial.");
		
		LancamentoParcial lancamentoParcial = lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(produtoEdicao.getId());
		
		if(lancamentoParcial != null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há LancamentoParcial para o ProdutoEdicao " + produtoEdicao.getId() +".");
				
		return lancamentoParcial;		
	}

}
