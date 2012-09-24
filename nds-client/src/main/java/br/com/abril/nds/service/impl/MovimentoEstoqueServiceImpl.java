package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.ControleAprovacaoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MovimentoEstoqueServiceImpl implements MovimentoEstoqueService {

	@Autowired
	EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	EstudoCotaRepository estudoCotaRepository; 
	
	@Autowired
	CotaRepository cotaRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	ControleAprovacaoService controleAprovacaoService;

	@Autowired
	LancamentoRepository lancamentoRepository;

	@Override
	@Transactional
	public void gerarMovimentoEstoqueDeExpedicao(Date dataLancamento, Long idProdutoEdicao, Long idUsuario) {
		
		TipoMovimentoEstoque tipoMovimento = 
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		
		TipoMovimentoEstoque tipoMovimentoCota =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		List<EstudoCota> listaEstudoCota = estudoCotaRepository.
				obterEstudoCotaPorDataProdutoEdicao(dataLancamento, idProdutoEdicao);
		
		BigInteger total = BigInteger.ZERO;
		
		MovimentoEstoqueCota movimentoEstoqueCota;
		
		for( EstudoCota estudoCota:listaEstudoCota ) {
			
			movimentoEstoqueCota = 
					gerarMovimentoCota(dataLancamento,idProdutoEdicao,estudoCota.getCota().getId(),	
					idUsuario, estudoCota.getQtdeEfetiva(),tipoMovimentoCota);
			
			movimentoEstoqueCota.setEstudoCota(estudoCota);
			
			movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);
			
			total = total.add(estudoCota.getQtdeEfetiva());
		}
		
		gerarMovimentoEstoque(dataLancamento, idProdutoEdicao, idUsuario, total, tipoMovimento);
		
	}
	
	@Override
	@Transactional
	public void enviarSuplementarCotaAusente(Date data, Long idCota,List<MovimentoEstoqueCota> listaMovimentoCota) throws TipoMovimentoEstoqueInexistenteException{
		
		if(listaMovimentoCota==null || listaMovimentoCota.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não possui reparte na data.");
		}
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
			
		TipoMovimentoEstoque tipoMovimentoCota =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		
		if ( tipoMovimento == null ) {
		 	throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);		
		} 
		
		if ( tipoMovimentoCota == null ) {
			throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		}
		
			
		for(MovimentoEstoqueCota movimentoCota : listaMovimentoCota){
			
			if(movimentoCota.getData() != null &&  movimentoCota.getProdutoEdicao()!=null
					&& movimentoCota.getUsuario() != null
					&&  movimentoCota.getQtde() != null ){
				
				gerarMovimentoEstoque(movimentoCota.getData(), movimentoCota.getProdutoEdicao().getId(), movimentoCota.getUsuario().getId(), movimentoCota.getQtde(), tipoMovimento);
			
				gerarMovimentoCota(movimentoCota.getData(), movimentoCota.getProdutoEdicao().getId(), movimentoCota.getCota().getId(),movimentoCota.getUsuario().getId(), movimentoCota.getQtde(), tipoMovimentoCota);
		
			}
		}
	
	
	}
	
	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = criarMovimentoEstoque(dataLancamento, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque);
		
		return movimentoEstoque;
	}
	
	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque);
		
		return movimentoEstoque;
	}
	
	private MovimentoEstoque criarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque){
		
		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		
		if(dataLancamento!= null){	
			ItemRecebimentoFisico itemRecebimentoFisico = 
				itemRecebimentoFisicoRepository.obterItemPorDataLancamentoIdProdutoEdicao(dataLancamento, idProdutoEdicao);
			
			movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
		
		if (estoqueProduto == null) {
			
			estoqueProduto = new EstoqueProduto();
			
			estoqueProduto.setProdutoEdicao(produtoEdicao);
			
			estoqueProduto.setQtde(BigInteger.ZERO);
			
			this.estoqueProdutoRespository.adicionar(estoqueProduto);
		}
		
		movimentoEstoque.setEstoqueProduto(estoqueProduto);
		movimentoEstoque.setProdutoEdicao(produtoEdicao);		
		movimentoEstoque.setData(new Date());
		movimentoEstoque.setUsuario(usuario);
		movimentoEstoque.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoque.setQtde(quantidade);
		
		movimentoEstoqueRepository.adicionar(movimentoEstoque);
		
		if (tipoMovimentoEstoque.isAprovacaoAutomatica()) {		
			
			controleAprovacaoService.realizarAprovacaoMovimento(movimentoEstoque, usuario);
		}
		
		return movimentoEstoque;
	}
	
	@Override
	@Transactional
	public void atualizarEstoqueProduto(TipoMovimentoEstoque tipoMovimentoEstoque,
			 							MovimentoEstoque movimentoEstoque) {
		
		if (StatusAprovacao.APROVADO.equals(movimentoEstoque.getStatus())) {
			
			EstoqueProduto estoqueProduto = movimentoEstoque.getEstoqueProduto();
			
			BigInteger novaQuantidade;
			
			if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {
				
				 if( GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque()) ||
					 GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())||
					 GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())) {
					 
					 BigInteger qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
					 
					 novaQuantidade = qtdeSuplementar.add(movimentoEstoque.getQtde());
					 
					 estoqueProduto.setQtdeSuplementar(novaQuantidade);
					 
				 } else if(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())){
					
					 BigInteger qtdeEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
						
					 novaQuantidade = qtdeEncalhe.add(movimentoEstoque.getQtde());
						
					 estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
				 } 				 
				 else {
					 
					 novaQuantidade = estoqueProduto.getQtde().add(movimentoEstoque.getQtde());
					 
					 estoqueProduto.setQtde(novaQuantidade);
				 }
				
			} else {
				
				if(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())
						|| GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())) {
					
					BigInteger qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
					
					novaQuantidade = qtdeSuplementar.subtract(movimentoEstoque.getQtde());
					
					estoqueProduto.setQtdeSuplementar(novaQuantidade);
					 
				 }else if (GrupoMovimentoEstoque.VENDA_ENCALHE.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())){
					
					 BigInteger qtdeEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
						
					 novaQuantidade = qtdeEncalhe.subtract(movimentoEstoque.getQtde());
						
					 estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
				 }
				 else {
					 
					 novaQuantidade = estoqueProduto.getQtde().subtract(movimentoEstoque.getQtde());
						
					 estoqueProduto.setQtde(novaQuantidade);
				 }
			}
		
			estoqueProdutoRespository.alterar(estoqueProduto);
		}
		
	}
	
	@Override
	@Transactional
	public MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque) {
						
		EstoqueProdutoCota estoqueProdutoCota = 
			estoqueProdutoCotaRepository.buscarEstoquePorProdutoECota(idProdutoEdicao, idCota);
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		if (estoqueProdutoCota == null) {
			
			ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
			
			Cota cota = cotaRepository.buscarPorId(idCota);
			
			estoqueProdutoCota = new EstoqueProdutoCota();
			estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
			estoqueProdutoCota.setQtdeDevolvida(BigInteger.ZERO);
			estoqueProdutoCota.setQtdeRecebida(BigInteger.ZERO);
			estoqueProdutoCota.setCota(cota);
			
			estoqueProdutoCotaRepository.adicionar(estoqueProdutoCota);
		}
				
		
		MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
		
		movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoqueCota.setCota(estoqueProdutoCota.getCota());
		movimentoEstoqueCota.setData(new Date());
		movimentoEstoqueCota.setDataCriacao(new Date());
		movimentoEstoqueCota.setEstoqueProdutoCota(estoqueProdutoCota);
		movimentoEstoqueCota.setProdutoEdicao(estoqueProdutoCota.getProdutoEdicao());
		movimentoEstoqueCota.setQtde(quantidade);
		movimentoEstoqueCota.setUsuario(usuario);
		
		if (dataLancamento != null && idProdutoEdicao != null) {
			Lancamento lancamento = lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(estoqueProdutoCota.getProdutoEdicao(), null, dataLancamento);
			if (lancamento != null) {
				movimentoEstoqueCota.setLancamento(lancamento);
			}
		}

		
		movimentoEstoqueCotaRepository.adicionar(movimentoEstoqueCota);
		
		if (tipoMovimentoEstoque.isAprovacaoAutomatica()) {
			
			controleAprovacaoService.realizarAprovacaoMovimento(movimentoEstoqueCota,
																usuario);
		}
		
		return movimentoEstoqueCota;
	}
	
	@Override
	@Transactional
	public void atualizarEstoqueProdutoCota(TipoMovimentoEstoque tipoMovimentoEstoque,
											MovimentoEstoqueCota movimentoEstoqueCota) {
		
		if (StatusAprovacao.APROVADO.equals(movimentoEstoqueCota.getStatus())) {
			
			EstoqueProdutoCota estoqueProdutoCota = movimentoEstoqueCota.getEstoqueProdutoCota();
			
			BigInteger novaQuantidade;
			
			BigInteger quantidadeRecebida;
			
			BigInteger quantidadeDevolvida;
			
			if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {
				
				quantidadeRecebida = (estoqueProdutoCota.getQtdeRecebida() != null) 
									  ? estoqueProdutoCota.getQtdeRecebida() : BigInteger.ZERO;
				
				novaQuantidade = quantidadeRecebida.add(movimentoEstoqueCota.getQtde());
				estoqueProdutoCota.setQtdeRecebida(novaQuantidade);
				 
			} else {
				
				quantidadeDevolvida = (estoqueProdutoCota.getQtdeDevolvida() != null) 
						 			  ? estoqueProdutoCota.getQtdeDevolvida() : BigInteger.ZERO;
				
				novaQuantidade = quantidadeDevolvida.add(movimentoEstoqueCota.getQtde());
				estoqueProdutoCota.setQtdeDevolvida(novaQuantidade);
			}
			
			estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
		}
	}

	@Override
	public void processarRegistroHistoricoVenda(HistoricoVendaInput vendaInput) {
		
		Integer reparte = vendaInput.getQuantidadeRecebidaProduto();
		Integer encalhe = vendaInput.getQuantidadeDevolvidaProduto();
		
		ProdutoEdicao edicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				vendaInput.getCodigoProduto().toString(), vendaInput.getNumeroEdicao().longValue());
		
		if(edicao == null)
			throw new ImportacaoException("Edição inexistente.");
			
		Cota cota = cotaRepository.obterPorNumerDaCota(vendaInput.getNumeroCota());
		
		if(cota == null)
			throw new ImportacaoException("Cota inexistente.");
		
		Long idUsuario = usuarioRepository.getUsuarioImportacao().getId();
		
		persistirRegistroVendaHistoricoReparte(idUsuario, reparte, edicao, cota);
		
		persistirRegistroVendaHistoricoEncalhe(idUsuario, encalhe, edicao, cota);
	
	}
	
	/**
	 * Persistem os dados de reparte de histórico de vendas 
	 * 
	 * @param idUsuario
	 * @param reparte
	 * @param edicao
	 * @param cota
	 */
	private void persistirRegistroVendaHistoricoReparte(Long idUsuario, Integer reparte, ProdutoEdicao edicao, Cota cota){
		
		if(reparte != null && reparte>0) {
			
			TipoMovimentoEstoque tipoMovimentoEnvioReparte = 
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
			
			if(tipoMovimentoEnvioReparte == null){
				
				tipoMovimentoEnvioReparte = new TipoMovimentoEstoque();
				tipoMovimentoEnvioReparte.setAprovacaoAutomatica(true);
				tipoMovimentoEnvioReparte.setDescricao("Envio a Jornaleiro");
				tipoMovimentoEnvioReparte.setIncideDivida(true);
				tipoMovimentoEnvioReparte.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
				
				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoEnvioReparte);
			}
			
			TipoMovimentoEstoque tipoMovimentoRecebimentoReparte = 
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
			if(tipoMovimentoRecebimentoReparte == null){
				
				tipoMovimentoRecebimentoReparte  = new TipoMovimentoEstoque();
				tipoMovimentoRecebimentoReparte.setAprovacaoAutomatica(true);
				tipoMovimentoRecebimentoReparte.setDescricao("Recebimento Reparte");
				tipoMovimentoRecebimentoReparte.setIncideDivida(true);
				tipoMovimentoRecebimentoReparte.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
				
				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoRecebimentoReparte);
			}
			
			gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoEnvioReparte);
			
			gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoRecebimentoReparte);
		}
	}
	
	/**
	 * Persistem os dados de encalhe de histórico de vendas 
	 * 
	 * @param idUsuario
	 * @param encalhe
	 * @param edicao
	 * @param cota
	 */
	private void persistirRegistroVendaHistoricoEncalhe(Long idUsuario, Integer encalhe, ProdutoEdicao edicao, Cota cota){
		
		if(encalhe != null && encalhe>0) {
			
			TipoMovimentoEstoque tipoMovimentoEnvioEncalhe =
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);
			
			if(tipoMovimentoEnvioEncalhe == null){
				
				tipoMovimentoEnvioEncalhe = new TipoMovimentoEstoque();
				tipoMovimentoEnvioEncalhe.setAprovacaoAutomatica(true);
				tipoMovimentoEnvioEncalhe.setDescricao("Envio Encalhe - Estoque");
				tipoMovimentoEnvioEncalhe.setIncideDivida(true);
				tipoMovimentoEnvioEncalhe.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);
				
				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoEnvioEncalhe);
			}
			
			TipoMovimentoEstoque tipoMovimentoRecebimentoEncalhe =
						tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
			
			if(tipoMovimentoRecebimentoEncalhe  == null){
				
				tipoMovimentoRecebimentoEncalhe = new TipoMovimentoEstoque();
				tipoMovimentoRecebimentoEncalhe.setAprovacaoAutomatica(true);
				tipoMovimentoRecebimentoEncalhe.setDescricao("Recebimento Encalhe");
				tipoMovimentoRecebimentoEncalhe.setIncideDivida(true);
				tipoMovimentoRecebimentoEncalhe.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
				
				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoRecebimentoEncalhe);
			}
			
			gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoRecebimentoEncalhe);
			
			gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoEnvioEncalhe);
		}
	}

}
