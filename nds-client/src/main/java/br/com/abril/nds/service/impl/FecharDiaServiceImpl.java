package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoGeracaoCobrancaFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FecharDiaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class FecharDiaServiceImpl implements FecharDiaService {
	
	@Autowired
	private FecharDiaRepository fecharDiaRepository;
	
	@Autowired
	private ImpressaoDividaService impressaoDividaService;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
	@Autowired
	private DividaService dividaService;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private MovimentoRepository movimentoRepository;
	
	@Override
	@Transactional
	public boolean existeCobrancaParaFecharDia(Date dataOperacaoDistribuidor) {
		Date diaDeOperaoMenosUm = DateUtil.subtrairDias(dataOperacaoDistribuidor, 1);
		return this.fecharDiaRepository.existeCobrancaParaFecharDia(diaDeOperaoMenosUm);
	}

	@Override
	@Transactional
	public boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor) {		 
		return this.fecharDiaRepository.existeNotaFiscalSemRecebimentoFisico(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor) {
		
		return this.fecharDiaRepository.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public Boolean existeConfirmacaoDeExpedicao(Date dataOperacao) {		 
		return this.fecharDiaRepository.existeConfirmacaoDeExpedicao(dataOperacao);
	}

	@Override
	@Transactional
	public List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {		 
		return this.fecharDiaRepository.obterConfirmacaoDeExpedicao(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public Boolean existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor) {		
		return this.fecharDiaRepository.existeLancamentoFaltasESobrasPendentes(dataOperacaoDistribuidor).isEmpty() ? true : false;
	}

	@Override
	@Transactional
	public List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> obterLancamentoFaltasESobras(Date dataOperacaoDistribuidor) {
		return this.fecharDiaRepository.existeLancamentoFaltasESobrasPendentes(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public List<ValidacaoControleDeAprovacaoFecharDiaDTO> obterPendenciasDeAprovacao(Date dataOperacao, StatusAprovacao pendente) {
		 
		return this.fecharDiaRepository.obterPendenciasDeAprovacao(dataOperacao,pendente);
	}

	@Override
	@Transactional
	public Boolean existeGeracaoDeCobranca(Date dataOperacao) {
		
		Calendar dataBase = Calendar.getInstance();
		dataBase.setTime(dataOperacao);
		int diaDaSemanaDaDataDeOperacao = dataBase.get(Calendar.DAY_OF_WEEK);
		int diaDaMesDaDataDeOperacao = dataBase.get(Calendar.DAY_OF_MONTH);
		
		List<ValidacaoGeracaoCobrancaFecharDiaDTO> listaDePoliticaCobranca = this.fecharDiaRepository.obterFormasDeCobranca();
		
		for(ValidacaoGeracaoCobrancaFecharDiaDTO dto: listaDePoliticaCobranca){
			FormaCobranca fc = this.formaCobrancaRepository.buscarPorId(dto.getFormaCobrancaId());
			if(dto.getTipoFormaCobranca().equals("Diária")){	
				return impressaoDividaService.validarDividaGerada(dataOperacao);				
			}
			if(dto.getTipoFormaCobranca().equals("Semanal")){
				List<ValidacaoGeracaoCobrancaFecharDiaDTO> lista = this.fecharDiaRepository.obterDiasDaConcentracao(fc);
				for(ValidacaoGeracaoCobrancaFecharDiaDTO con: lista){					
					if(con.getDiaDoMes() == diaDaSemanaDaDataDeOperacao){
						return impressaoDividaService.validarDividaGerada(dataOperacao);
					}					
				}
				
			}
			if(fc.getTipoFormaCobranca().getDescricao().equals("Mensal") || fc.getTipoFormaCobranca().getDescricao().equals("Quinzenal") ){
				for(Integer diaDeCobranca: fc.getDiasDoMes()){
					if(diaDeCobranca ==  diaDaMesDaDataDeOperacao){
						return impressaoDividaService.validarDividaGerada(dataOperacao);
					}
				}
			}
		}
		 
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ResumoFechamentoDiarioCotasDTO obterResumoCotas(Date dataFechamento) {

		Long quantidadeTotal = this.cotaRepository.obterQuantidadeCotas(null);
		
		Long quantidadeAtivas = this.cotaRepository.obterQuantidadeCotas(SituacaoCadastro.ATIVO);
		
		List<Cota> ausentesExpedicaoReparte = 
			this.cotaRepository.obterCotasAusentesNaExpedicaoDoReparteEm(dataFechamento);
		
		List<Cota> ausentesRecolhimentoEncalhe = 
			this.cotaRepository.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataFechamento);
		
		List<Cota> novas = this.cotaRepository.obterCotasComInicioAtividadeEm(dataFechamento);
		
		List<Cota> inativas = this.cotaRepository.obterCotas(SituacaoCadastro.INATIVO);
		
		return new ResumoFechamentoDiarioCotasDTO(
			quantidadeTotal, quantidadeAtivas, ausentesExpedicaoReparte, 
				ausentesRecolhimentoEncalhe, novas, inativas);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data) {
        return dividaService.sumarizacaoDividasReceberEm(data);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data) {
        return dividaService.sumarizacaoDividasVencerApos(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Divida> obterDividasReceberEm(Date data, PaginacaoVO paginacao) {
        return obterDividasReceberEm(data, paginacao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Divida> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
        return dividaService.obterDividasVencerApos(data, paginacao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int contarDividasReceberEm(Date data) {
        return dividaService.contarDividasReceberEm(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int contarDividasVencerApos(Date data) {
        return dividaService.contarDividasVencerApos(data);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void processarControleDeAprovacao() {

		Distribuidor distribuidor = this.distribuidorRepository.obter();

		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = obterGruposMovimentoFinaceiro();

		List<Movimento> movimentosPendentes = this.fecharDiaRepository.obterMovimentosPorStatusData(
				null, gruposMovimentoFinanceiro, distribuidor.getDataOperacao(), StatusAprovacao.PENDENTE);

		for (Movimento movimento : movimentosPendentes) {

			movimento.setData(DateUtil.adicionarDias(movimento.getData(), 1));

			this.movimentoRepository.merge(movimento);
		}
	}

	private List<GrupoMovimentoFinaceiro> obterGruposMovimentoFinaceiro() {
		
		List<GrupoMovimentoFinaceiro> grupos = new ArrayList<GrupoMovimentoFinaceiro>();
		
		grupos.add(GrupoMovimentoFinaceiro.CREDITO);
		grupos.add(GrupoMovimentoFinaceiro.DEBITO);
		grupos.add(GrupoMovimentoFinaceiro.POSTERGADO);
		grupos.add(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
		
		return grupos;
	}
}
