package br.com.abril.nds.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.FuroProdutoRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.FuroProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Constantes;
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
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private ItemNotaEnvioRepository itemNovaEnvioRepository;

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

		if (novaData.after(lancamento.getDataRecolhimentoDistribuidor())){
			mensagensValidacao.add("Nova data não deve ser maior que data de recolhimento.");
		}

		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
		
		boolean ultrapassouLimiteReprogramacoes =
			lancamento.getNumeroReprogramacoes() != null
				&& lancamento.getNumeroReprogramacoes() >= Constantes.NUMERO_REPROGRAMACOES_LIMITE;
		
		boolean possuiRecebimento =
			lancamento.getRecebimentos() != null && !lancamento.getRecebimentos().isEmpty();
		
		if (ultrapassouLimiteReprogramacoes && possuiRecebimento) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "Produto não pode sofrer furo! Já ultrapassou o limite de reprogramações!");
		}
		
		//verificar se existe distribuição nesse dia da semana
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(novaData);
		
		if (!this.distribuicaoFornecedorRepository.verificarDistribuicaoDiaSemana(
				codigoProduto, idProdutoEdicao, DiaSemana.getByCodigoDiaSemana(calendar.get(Calendar.DAY_OF_WEEK)))){
			throw new ValidacaoException(TipoMensagem.ERROR, "Não existe distribuição para esse produto no dia " + 
				new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).format(novaData));
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
		
		return lancamento.getStatus().equals(StatusLancamento.EXPEDIDO);
	}
	
	@Transactional
	@Override
	public void efetuarFuroProduto(String codigoProduto, Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario) {		
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);
		
		if (this.distribuidorService.regimeEspecial()) {
			
			List<ItemNotaEnvio> itensNotaEnvio = 
				this.itemNovaEnvioRepository.obterItemNotaEnvio(idLancamento);
			
			for (ItemNotaEnvio itemNotaEnvio : itensNotaEnvio) {
				
				this.itemNovaEnvioRepository.remover(itemNotaEnvio);
			}
		}
		
		if (this.verificarProdutoExpedido(idLancamento)) {
			
			List<MovimentoEstoqueCota> movimentos = movimentoEstoqueCotaRepository.obterPorLancamento(idLancamento);
			for (MovimentoEstoqueCota movimento : movimentos) {
				movimento.setEstudoCota(null);
				movimentoEstoqueCotaRepository.alterar(movimento);
			}
			
			// Geração de movimentação de estoque por cota / movimentação de estoque / estoque / estoque cota
			movimentoEstoqueService.gerarMovimentoEstoqueFuroPublicacao(lancamento, idUsuario);
			
		}
		
		// Ao furar um produto com nota de envio emitida, o item da nota eh removido
		for(EstudoCota ec : lancamento.getEstudo().getEstudoCotas()) {
			for(ItemNotaEnvio item : ec.getItemNotaEnvios()){
				item.setEstudoCota(null);
				itemNovaEnvioRepository.alterar(item);
			}
		}
		
		lancamento.setDataLancamentoDistribuidor(novaData);
		lancamento.setStatus(StatusLancamento.FURO);
		lancamento.setNumeroReprogramacoes(this.atualizarNumeroReprogramacoes(lancamento));
		
		FuroProduto furoProduto = new FuroProduto();
		furoProduto.setData(new Date());
		furoProduto.setLancamento(lancamento);
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(idProdutoEdicao);
		furoProduto.setProdutoEdicao(produtoEdicao);
		Usuario usuario = new Usuario();
		usuario.setId(idUsuario);
		furoProduto.setUsuario(usuario);
		
		HistoricoLancamento historicoLancamento = new HistoricoLancamento();
		historicoLancamento.setDataEdicao(new Date());
		historicoLancamento.setLancamento(lancamento);
		historicoLancamento.setResponsavel(usuario);
		historicoLancamento.setStatus(lancamento.getStatus());
		historicoLancamento.setTipoEdicao(TipoEdicao.ALTERACAO);
		
		this.furoProdutoRepository.adicionar(furoProduto);
		
		this.lancamentoRepository.alterar(lancamento);
		
		this.historicoLancamentoRepository.adicionar(historicoLancamento);
	}
	
	private Integer atualizarNumeroReprogramacoes(Lancamento lancamento) {
		
		Integer numeroReprogramacoes = lancamento.getNumeroReprogramacoes();
		
		if (numeroReprogramacoes == null) {
			
			numeroReprogramacoes = 0;
		}
		
		numeroReprogramacoes++;
		
		return numeroReprogramacoes;
	}
	
}
