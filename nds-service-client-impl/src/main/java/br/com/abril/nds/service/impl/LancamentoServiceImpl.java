package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoLancamentoVO;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.integracao.DistribuidorService;
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
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Value("${data_cabalistica}")
    private String dataCabalistica;
	
	LinkedList<Date> dinap = new LinkedList();
	LinkedList<Date> fc = new LinkedList();
	LinkedList<Date> dinapFC = new LinkedList();
    
    private static final List<StatusLancamento> STATUS_LANCAMENTOS_REMOVIVEL = Arrays.asList(
            StatusLancamento.PLANEJADO, StatusLancamento.CONFIRMADO, StatusLancamento.EM_BALANCEAMENTO,
            StatusLancamento.BALANCEADO);
	
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
		
        // Conforme solicitado pelo Cesar, caso não encontre preço de venda
        // utiliza o preço previsto
		String preco = "";
		if (lancamento.getProdutoEdicao().getPrecoVenda() != null) {
			preco = lancamento.getProdutoEdicao().getPrecoVenda().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",");
		} else if (lancamento.getProdutoEdicao().getPrecoPrevisto() != null) {
			preco = lancamento.getProdutoEdicao().getPrecoPrevisto().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",");
		} else {
            throw new ValidacaoException(TipoMensagem.ERROR, "Produto sem preço de venda e preco previsto. Codigo: "
                    + lancamento.getProdutoEdicao().getProduto().getCodigo() + ", Edicao: "
                    + lancamento.getProdutoEdicao().getProduto().getNome());
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
	public String confirmarExpedicao(Long idLancamento, Long idUsuario, Date dataOperacao, 
									 TipoMovimentoEstoque tipoMovimento, TipoMovimentoEstoque tipoMovimentoCota,
									 TipoMovimentoEstoque tipoMovimentoJuramentado) {
		
		LancamentoDTO lancamento = lancamentoRepository.obterLancamentoPorID(idLancamento);
		
		if (TipoLancamento.REDISTRIBUICAO.equals(lancamento.getTipoLancamento())) {
            
		    String msgRetorno = this.tratarRedistribuicao(lancamento);
            
            if (msgRetorno != null) {
                
                return msgRetorno;
            }
        }
		
		Expedicao expedicao = new Expedicao();
		expedicao.setDataExpedicao(dataOperacao);
		expedicao.setResponsavel(new Usuario(idUsuario));
		Long idExpedicao = expedicaoRepository.adicionar(expedicao);
		
		expedicao.setId(idExpedicao);
		
		lancamentoRepository.alterarLancamento(idLancamento, dataOperacao, StatusLancamento.EXPEDIDO, expedicao);
		
//		HistoricoLancamento historico = new HistoricoLancamento();
//		historico.setDataEdicao(dataOperacao);
//		historico.setLancamento(new Lancamento(idLancamento));
//		
//		historico.setResponsavel(new Usuario(idUsuario));
//		historico.setStatusNovo(StatusLancamento.EXPEDIDO);
//		historico.setTipoEdicao(TipoEdicao.ALTERACAO);
		
        // TODO: geração de historico desativada devido a criação de trigger
        // para realizar essa geração.
		//historicoLancamentoRepository.adicionar(historico);
		
		
		movimentoEstoqueService.gerarMovimentoEstoqueDeExpedicao(lancamento.getDataPrevista(), lancamento.getDataDistribuidor(), 
				lancamento.getIdProduto(), lancamento.getIdProdutoEdicao(), idLancamento, idUsuario, dataOperacao, tipoMovimento, tipoMovimentoCota, tipoMovimentoJuramentado);
		
		return null;
	}

	private String tratarRedistribuicao(LancamentoDTO lancamento) {
	    
	    Long idProdutoEdicao = lancamento.getIdProdutoEdicao();
        Integer numeroPeriodo = lancamento.getNumeroPeriodo();
	    
        Lancamento lancamentoOriginal =
            this.lancamentoRepository.obterLancamentoOriginalDaRedistribuicao(idProdutoEdicao, numeroPeriodo);
        
        if (lancamentoOriginal.getStatus().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO)
                || lancamentoOriginal.getStatus().equals(StatusLancamento.EM_RECOLHIMENTO)
                || lancamentoOriginal.getStatus().equals(StatusLancamento.RECOLHIDO)) {
         
            return "Produto: " + lancamentoOriginal.getProdutoEdicao().getProduto().getNome()
                + " - Edição: " + lancamentoOriginal.getProdutoEdicao().getNumeroEdicao()
                + " - Data Recolhimento: " + DateUtil.formatarDataPTBR(lancamentoOriginal.getDataRecolhimentoDistribuidor());
        }
        
        return null;
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
	public List<Lancamento> obterLancamentosEdicao(Long idProdutoEdicao) {
		
		return lancamentoRepository.obterLancamentosEdicao(idProdutoEdicao);
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

	@Override
	@Transactional(readOnly=true)
	public Lancamento obterLancamentoNaMesmaSessao(Long id) {
		return this.lancamentoRepository.buscarPorIdSemEstudo(id);
	}
    
    @Override
    @Transactional(readOnly = false)
    public void removerLancamento(Long id) {
        if (id == null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Id do lançamento deve ser especificado.");
        }
        
        Lancamento lancamento = lancamentoRepository.buscarPorId(id);
        if (lancamento == null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Lançamento não existe ou ja foi excluido.");
        }
        
        if (lancamento.getNumeroLancamento() == 1) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Não é perdido excluir o lançamento número 1.");
        }
        
        if (lancamento.getPeriodoLancamentoParcial() != null
                && TipoLancamentoParcial.PARCIAL.equals(lancamento.getPeriodoLancamentoParcial().getTipo())) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Este Lançamento é parcial e não pode ser excluido. Consulte a tela de parciais.");
        }
        if (!STATUS_LANCAMENTOS_REMOVIVEL.contains(lancamento.getStatus())) {
            throw new ValidacaoException(TipoMensagem.WARNING, "O status deste Lançamento é "
                    + lancamento.getStatus().getDescricao() + " e não pode ser removido.");
        }
        this.lancamentoRepository.remover(lancamento);
    }
    
    @Override
    @Transactional
    public void atualizarReparteLancamento(Long idLancamento, BigInteger reparte, BigInteger repartePromocional) {
    	
    	Lancamento lancamento = this.obterPorId(idLancamento);
    	
    	if (reparte != null ) {
    	
    		lancamento.setReparte(reparte);
    	}
    	
    	if (repartePromocional != null) {
    		
    		lancamento.setRepartePromocional(repartePromocional);
    	}
    	
    	this.lancamentoRepository.merge(lancamento);
    }
    
    public HashMap<String, Set<Date>> obterDiasMatrizLancamentoAbertos(){
    	List<Object[]> lista = lancamentoRepository.buscarDiasMatrizLancamentoAbertos();
    	

    	
    	Set <Date> diasConfirmados = new TreeSet<Date>();
    	Set <Date> diasNaoBalanceaveis = new TreeSet<Date>();
    	Date diaOperacaoDistribuidor = distribuidorService.obterDataOperacaoDistribuidor();

    	HashMap<String, Set<Date>> listaBalanceavelNaoBalanceavel = new HashMap<>();
    	
    	for(Object[] lancamento : lista){
    		
    		if((lancamento[1].equals(StatusLancamento.CONFIRMADO)
    		 || lancamento[1].equals(StatusLancamento.PLANEJADO)
    		 || lancamento[1].equals(StatusLancamento.FURO)) 
    	  && !((Date)lancamento[0]).before(diaOperacaoDistribuidor)
    	  && ! calendarioService.isFeriadoSemOperacao((Date)lancamento[0])
    	  && ! calendarioService.isFeriadoMunicipalSemOperacao((Date)lancamento[0])){
    			
    			if(!diasConfirmados.contains((Date)lancamento[0])){
    			  diasConfirmados.add((Date)lancamento[0]);
    			}
    		}else{
    			if(!diasNaoBalanceaveis.contains((Date)lancamento[0])){
    			  diasNaoBalanceaveis.add((Date)lancamento[0]);
      			}
    		}

    	}
    	
    	diasConfirmados.removeAll(diasNaoBalanceaveis);
    	
    	listaBalanceavelNaoBalanceavel.put("diasBalanceaveis", diasConfirmados);
    	listaBalanceavelNaoBalanceavel.put("diasNaoBalanceaveis", diasNaoBalanceaveis);
    	
    	return listaBalanceavelNaoBalanceavel;
    }
    
    @Override
    @Transactional
    public void excluirLancamento(final ProdutoLancamentoVO produtoLancamento) {
    	
    	  final Date data = DateUtil.parseDataPTBR(dataCabalistica);
          
          final Lancamento lancamento = buscarPorId(produtoLancamento.getId());
          
          lancamento.setDataLancamentoDistribuidor(data);
          lancamento.voltarStatusOriginal();
          // atualizarLancamento(produtoLancamento.getId(),data);
          
          this.lancamentoRepository.merge(lancamento);
    }

	@Override
	@Transactional
	public boolean existeProdutoEdicaoParaDia(
			ProdutoLancamentoDTO produtoLancamentoDTO, Date novaData) {
		
		return this.lancamentoRepository.existeProdutoEdicaoParaDia(produtoLancamentoDTO, novaData);
	}

	@Override
	@Transactional
	public Lancamento buscarPorId(Long id) {
		return this.lancamentoRepository.buscarPorId(id);
	}
	
	@Override
	@Transactional
	public LinkedList<Lancamento> obterLancamentosRedistribuicoes() {
		return this.lancamentoRepository.obterLancamentosRedistribuicoes();
	}
	
	@Override
    @Transactional
    public void atualizarRedistribuicoes(Lancamento lancamento, Date dataRecolhimento) {
        
        Long idProdutoEdicao = lancamento.getProdutoEdicao().getId();
        
        Integer numeroPeriodo = (lancamento.getPeriodoLancamentoParcial() != null)
                                    ? lancamento.getPeriodoLancamentoParcial().getNumeroPeriodo() : null;
        
        List<Lancamento> redistribuicoes =
            this.lancamentoRepository.obterRedistribuicoes(idProdutoEdicao, numeroPeriodo);
        
        for (Lancamento redistribuicao : redistribuicoes) {
            
            redistribuicao.setDataRecolhimentoDistribuidor(dataRecolhimento);
            
            this.lancamentoRepository.merge(redistribuicao);
        }
    }
	
	@Override
    @Transactional
    public void reajustarNumerosLancamento(PeriodoLancamentoParcial periodo) {
        
        List<Lancamento> lancamentos = 
            this.lancamentoRepository.obterLancamentosDoPeriodoParcial(periodo.getId());
        
        if(!lancamentos.isEmpty()){
        
            int numeroLancamento = 1;
            
            for(Lancamento item : lancamentos) {
                
                item.setNumeroLancamento(numeroLancamento++);
                
                lancamentoRepository.merge(item);
            }
        }
        
    }
	
	public Date obterDataLancamentoValido(Date dataLancamento,Long idFornecedor){
		
	
		List<Long> lista = new ArrayList<Long>();
		
		//Fix
		if(idFornecedor==1){	
		 
		 lista.add(new Long(1));
		 
		 if(dinap.isEmpty()){
		  dinap.addAll(lancamentoRepository.obterDatasLancamentoValido(lista));
		 }
		 
		 dataLancamento = this.obterDataLancamentoValida(dataLancamento, dinap);
		 
		}else if (idFornecedor==2){
		
		 lista.add(new Long(2));
		 
		 if(fc.isEmpty()){
		  fc.addAll(lancamentoRepository.obterDatasLancamentoValido(lista));
		 }
		 
		 dataLancamento = this.obterDataLancamentoValida(dataLancamento, fc);
		 
		}else {
			
		 lista.add(new Long(1));
		 lista.add(new Long(2));
			 
		 if(dinapFC.isEmpty()){
			dinapFC.addAll(lancamentoRepository.obterDatasLancamentoValido(lista));
		 }
			 
		 dataLancamento = this.obterDataLancamentoValida(dataLancamento, dinapFC);
			 
		}
		
		return dataLancamento;
	}
	
	private Date obterDataLancamentoValida(Date dataLancamento,LinkedList<Date> listaDatas){
		
		Date anterior;
		Date posterior;
		Date operacao;
		long qtAnterior;
		long qtPosterior;
		
		if(listaDatas==null || listaDatas.isEmpty()){
		 
		  operacao = distribuidorService.obterDataOperacaoDistribuidor();
		  
		  if(dataLancamento.before(operacao)){
			return operacao;
		  }else{
			return dataLancamento;
		  }
		}else if(listaDatas.contains(dataLancamento)){
		  return dataLancamento;
		}else if(dataLancamento.before(listaDatas.getFirst())){
		  return listaDatas.getFirst();
		}else{
			
		  for(int i =0;i<listaDatas.size();i++){
			  if(i>0 && dataLancamento.after(listaDatas.get(i-1)) 
			   && dataLancamento.before(listaDatas.get(i+1))){
				  
				  anterior   = listaDatas.get(i-1);
				  posterior  = listaDatas.get(i+1);
				  
				  qtAnterior = dataLancamento.getTime() - anterior.getTime();
				  qtPosterior = posterior.getTime() - dataLancamento.getTime();
				  
				  if(qtAnterior<qtPosterior){
					  return anterior;  
				  }else{
					  return posterior;
				  }
				  
			  }else {
				  dataLancamento =  listaDatas.get(i);
			  }
		  }
		  return dataLancamento;
		}
		
	}


}
