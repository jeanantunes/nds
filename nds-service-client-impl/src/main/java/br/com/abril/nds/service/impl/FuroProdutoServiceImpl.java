package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaFuroDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.repository.FuroProdutoRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FuroProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class FuroProdutoServiceImpl implements FuroProdutoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FuroProdutoRepository furoProdutoRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private ItemNotaEnvioRepository itemNovaEnvioRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository; 

	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Transactional
	@Override
	public void validarFuroProduto(String codigoProduto, Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario) {
	
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (codigoProduto == null || codigoProduto.isEmpty()){
			mensagensValidacao.add("Código do produto é obrigatório.");
		}
		
		if (idProdutoEdicao == null){
			mensagensValidacao.add("Id produto edição é obrigatório.");
		}
		
		if (idLancamento == null){
			mensagensValidacao.add("Lançamento é obrigatório.");
		}
		
		if (novaData == null){
			mensagensValidacao.add("Data Lançamento é obrigatório.");
		}
		
		if (idUsuario == null){
			mensagensValidacao.add("Id usuário é obrigatório.");
		}
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);
		
		if (lancamento == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Lançamento não encontrado.");
		}
		
		if (novaData.equals(lancamento.getDataLancamentoDistribuidor()) 
				|| novaData.before(lancamento.getDataLancamentoDistribuidor())){
			mensagensValidacao.add("Nova data deve ser maior que a data de lançamento atual.");
		}

		if(StatusLancamento.RECOLHIDO.equals(lancamento.getStatus())){
			
			if(!this.produtoContaFirmeEmProcessoDeExpedicao(lancamento)){
				
				mensagensValidacao.add("Produto com forma de comercialização Conta Firme, só pode ser furado com a data de lançamento igual ou inferior a data de operação ");
			}
			
		}else{

			if (novaData.after(lancamento.getDataRecolhimentoDistribuidor())){
				mensagensValidacao.add("Nova data não deve ser maior que data de recolhimento.");
			}		
		}

		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}

		//verificar se existe distribuição nesse dia da semana
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(novaData);
		
		boolean diaOperante = this.isDiaOperante(codigoProduto, idProdutoEdicao, calendar);
		
		if (!diaOperante) {
			 
		    throw new ValidacaoException(TipoMensagem.WARNING, DateUtil.formatarDataPTBR(novaData)+" não é uma data em que o distribuidor realiza operação! ");
		}
		
		if (!this.distribuidorService.regimeEspecial()) {

			boolean produtoExpedido = this.verificarProdutoExpedido(idLancamento);
			
			boolean cobrancaGerada =
				this.lancamentoRepository.existeCobrancaParaLancamento(idLancamento);
			
			if (produtoExpedido && cobrancaGerada) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,
					"Produto não pode sofrer furo! Já foi realizada expedição física e gerada a cobrança!");
			}
		}
	}
	

	@Override
	@Transactional(readOnly=true)
	public boolean verificarProdutoExpedido(Long idLancamento) {
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);
		
		if (lancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Lançamento não encontrado.");
		}
		
		if(StatusLancamento.RECOLHIDO.equals(lancamento.getStatus())){
			return this.produtoContaFirmeEmProcessoDeExpedicao(lancamento);
		}
	
		return lancamento.getStatus().equals(StatusLancamento.EXPEDIDO);
	}

	private boolean produtoContaFirmeEmProcessoDeExpedicao(Lancamento lancamento) {
			
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		final boolean lancamentoExpedidoParaProdutoContaFitem = (lancamento.getDataLancamentoDistribuidor().compareTo(dataOperacao)<1);
		
		final FormaComercializacao produtoContaFirme = lancamento.getProdutoEdicao().getProduto().getFormaComercializacao();
		
		return (lancamentoExpedidoParaProdutoContaFitem 
				&& FormaComercializacao.CONTA_FIRME.equals(produtoContaFirme)); 
	}
	
	@Transactional
	@Override
	public void efetuarFuroProduto(String codigoProduto, Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario) {		
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);
	
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		FuroProduto furoProduto = criarRegistroFuroProduto(lancamento, idProdutoEdicao, usuario);
		
		final FormaComercializacao produtoContaFirme = lancamento.getProdutoEdicao().getProduto().getFormaComercializacao();
		
		if (this.verificarProdutoExpedido(idLancamento)) {
			
			List<MovimentoEstoqueCota> movimentos = movimentoEstoqueCotaRepository.obterPorLancamento(idLancamento);
			for (MovimentoEstoqueCota movimento : movimentos) {
				movimento.setEstudoCota(null);
				movimentoEstoqueCotaRepository.alterar(movimento);
			}
			
			if(FormaComercializacao.CONTA_FIRME.equals(produtoContaFirme)){
				
				movimentoFinanceiroCotaService
						.processarCreditosParaCotasNoProcessoDeFuroDeProdutoContaFirme(idLancamento, idUsuario);
			}
			
			// Geração de movimentação de estoque por cota / movimentação de estoque / estoque / estoque cota
			movimentoEstoqueService.gerarMovimentoEstoqueFuroPublicacao(lancamento, furoProduto, idUsuario);			
		}
			
		lancamento.setStatus(StatusLancamento.FURO);
		lancamento.setSequenciaMatriz(null);
		
		if (lancamento.getEstudo() != null) {
			
			for(EstudoCota ec : lancamento.getEstudo().getEstudoCotas()) {
				
				if (lancamento.getEstudo().getEstudoCotas() != null && !lancamento.getEstudo().getEstudoCotas().isEmpty()) {
					
					for(ItemNotaEnvio item : ec.getItemNotaEnvios()) {
						
						item.setFuroProduto(furoProduto.getId());
						item.setEstudoCota(null);
						
						itemNovaEnvioRepository.merge(item);
					}
				}
			}
		}
		
		if(FormaComercializacao.CONTA_FIRME.equals(produtoContaFirme)){
		
			lancamento.setDataRecolhimentoDistribuidor(novaData);
			lancamento.setDataRecolhimentoPrevista(novaData);
		}
		
		lancamento.setDataLancamentoDistribuidor(novaData);
		lancamento.setUsuario(usuario);
		lancamento.setExpedicao(null);
		
		this.lancamentoRepository.alterar(lancamento);
	}

	private FuroProduto criarRegistroFuroProduto(
			Lancamento lancamento, 
			Long idProdutoEdicao, 
			Usuario usuario){
		
		/**
		 * Alterado data do furo p/ inserir com data do sistema
		 * 
		 * A data do furo deve estar de acordo com a expedição devido a possibilidade de data do sistema defasada.
		 */
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		FuroProduto furoProduto = new FuroProduto();
		furoProduto.setData(dataOperacao);
		furoProduto.setLancamento(lancamento);
		furoProduto.setDataLancamentoDistribuidor(lancamento.getDataLancamentoDistribuidor());
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(idProdutoEdicao);
		furoProduto.setProdutoEdicao(produtoEdicao);
		furoProduto.setUsuario(usuario);
		
		this.furoProdutoRepository.adicionar(furoProduto);
		
		return furoProduto;
		
	}
	
	/**
	 * Verifica se Data não é feriado e é dia de operação do Distribuidor
	 * 
	 * @param codigoProduto
	 * @param idProdutoEdicao
	 * @param c
	 * @return boolean
	 */
	@Override
	@Transactional 
	public boolean isDiaOperante(String codigoProduto, Long idProdutoEdicao, Calendar c){
		
		boolean diaSemanaOperante = this.distribuicaoFornecedorRepository.verificarDistribuicaoDiaSemana(codigoProduto, 
                                                                                                         idProdutoEdicao, 
                                                                                                         DiaSemana.getByCodigoDiaSemana(c.get(Calendar.DAY_OF_WEEK)));

        boolean feriadoSemOperacao = (calendarioService.isFeriadoSemOperacao(c.getTime()) || calendarioService.isFeriadoMunicipalSemOperacao(c.getTime()));
        
        return (diaSemanaOperante && !feriadoSemOperacao);
	}
	
	/**
     * Obtem a proxima data, considerando Feriados e Dia de Operação do Distribuidor
     * 
     * @param codigoProduto
     * @param idProdutoEdicao
     * @param data 
     * @return Date
     */
    @Override
    @Transactional
    public Date obterProximaDataDiaOperante(String codigoProduto, Long idProdutoEdicao, Date data) {
        
        Calendar c = Calendar.getInstance();
        
        c.setTime(data);
        
        boolean diaOperante = this.isDiaOperante(codigoProduto, idProdutoEdicao, c);
        
        if (!diaOperante) {
            
            data = this.obterProximaDataDiaOperante(codigoProduto,idProdutoEdicao,DateUtil.adicionarDias(data, 1));
        }
        
        return data;
    }


	@Override
	@Transactional
	public List<CotaFuroDTO> obterCobrancaRealizadaParaCotaVista(Long idProdutoEdicao, Date dataFuro, Long idLancamento) {
		return this.furoProdutoRepository.obterCobrancaRealizadaParaCotaVista(idProdutoEdicao, dataFuro, idLancamento); 
	}
}