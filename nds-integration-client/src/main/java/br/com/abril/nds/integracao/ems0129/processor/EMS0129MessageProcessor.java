package br.com.abril.nds.integracao.ems0129.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.ems0129.data.SomaRegistro;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Detalhe;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Header;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Trailer;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Detalhe;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Header;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Trailer;
import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoImpressaoInterfaceLED;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.util.MathUtil;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class EMS0129MessageProcessor extends AbstractRepository implements MessageProcessor  {
	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0129Route.class);

	private static final String NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT = "PICKING.NEP";
	
	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private DescontoService descontoService;

	private String mensagemValidacao;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {
		
		this.setMensagemValidacao(null);
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_1)) {
			
			processarArquivoPickingModelo1(message, distribuidor);
			
		} else if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_2)) {
			
			processarArquivoPickingModelo2(message, distribuidor);

		} else if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_3)) {

			if (LOGGER.isWarnEnabled()) {

				LOGGER.warn(String.format("Leiaute Picking Distribuidor %s nao implementado !",
						distribuidor.getTipoImpressaoInterfaceLED()));
			}
			
		} else {
			
			String mensagemValidacao = "Leiaute Picking Invalido.";
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}

	
	/**
	 * Processa geração do arquivo de Picking do Modelo1
	 * 
	 * @param message
	 * @param distribuidor
	 */
	private void processarArquivoPickingModelo2(Message message,
			Distribuidor distribuidor) {
		String nomeArquivoPickingInterfaceLED;
		nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking2();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT;
		}
		
		geraArquivoPicking2(message, nomeArquivoPickingInterfaceLED);
	}

	
	/**
	 * Processa geração do arquivo de Picking do Modelo1
	 * 
	 * @param message
	 * @param distribuidor
	 */
	private void processarArquivoPickingModelo1(Message message,
			Distribuidor distribuidor) {
		
		String nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking1();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT;
		}
		
		geraArquivoPicking1(message, nomeArquivoPickingInterfaceLED);
	}

	
	/**
	 * Gera o arquivo de acordo com o modelo de pickin 1
	 * 
	 * @param message
	 * @param nomeArquivoPickingInterfaceLED
	 */
	private void geraArquivoPicking1(Message message, String nomeArquivoPickingInterfaceLED) {

		try {

			PrintWriter print = new PrintWriter(
					new FileWriter(
							message.getHeader().get(
									MessageHeaderProperties.OUTBOUND_FOLDER.getValue()) 
									+ File.pathSeparator + nomeArquivoPickingInterfaceLED));
			
			List<PDV> pdvs = findListPDV(message);
			
			
			for (PDV pdv : pdvs) {

				int numeroCota = pdv.getCota().getNumeroCota();

				criaHeader(print, numeroCota, pdv.getNome());

				int qtdeRegistros = criarDetalhes(print, numeroCota, pdv.getCota().getMovimentoEstoqueCotas());

				criaTrailer(print, numeroCota, qtdeRegistros);

			}

			print.flush();
			print.close();

		} catch (IOException e) {
			
			String mensagemValidacao = "Erro ao gerar o arquivo. " + e.getMessage();
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}

	
	/**
	 * Gera o arquivo de acordo com o modelo de picking 2
	 * 
	 * @param message
	 * @param nomeArquivoPickingInterfaceLED
	 */
	private void geraArquivoPicking2(Message message, String nomeArquivoPickingInterfaceLED) {

		List<PDV> pdvs = findListPDV(message);

		Date data = getDataLancDistrib(message);
		
		SomaRegistro somaRegistros = null;

		try {

			PrintWriter print = new PrintWriter(
					new FileWriter(message.getHeader().get(
							MessageHeaderProperties.OUTBOUND_FOLDER.getValue())	+ 
							File.pathSeparator  + nomeArquivoPickingInterfaceLED));

			for (PDV pdv : pdvs) {
				
				somaRegistros = new SomaRegistro();
				
				int numeroCota = pdv.getCota().getNumeroCota();
				
				criaHeader(print, pdv, data);
				
				somaRegistros = criarDetalhes(print, numeroCota, pdv.getCota().getMovimentoEstoqueCotas(), somaRegistros);
				
				criaTrailer(print, numeroCota, somaRegistros.getValorTotalBruto(), somaRegistros.getValorTotalDesconto());
			}

			print.flush();
			print.close();

		} catch (IOException e) {

			String mensagemValidacao = "Erro ao gerar o arquivo. " + e.getMessage();
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}

	
	/**
	 * Obtém a data de Lançamento do Distribuidor
	 * 
	 * @param message
	 * @return
	 */
	private Date getDataLancDistrib(Message message) {
		
		Date data = (Date) message.getHeader().get("DATA_LCTO_DISTRIB");
		
		if (data == null) {
			
			String mensagemValidacao = "Data nao informada. Execute a interface EMS0129 passando a dataLancamentoDistribuidor!";
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}
		
		return data;
	}
	
	
	/**
	 * Obtém listas de PDVs principais com base na data de lançamento.
	 * 
	 * @param message
	 * @return
	 */
	private List<PDV> findListPDV(Message message) {
		
		Date data = getDataLancDistrib(message);
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select pdv from PDV pdv ");
		sql.append(" join fetch pdv.cota co ");
		sql.append(" join fetch co.pessoa p ");
		sql.append(" left join fetch co.movimentoEstoqueCotas mov ");
		sql.append(" left join fetch mov.produtoEdicao pe ");
		sql.append(" left join fetch pe.lancamentos lan ");
		sql.append(" left join fetch pe.produto pd ");
		sql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
		sql.append(" and lan.dataLancamentoDistribuidor = :dataLancDistrib ");

		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("dataLancDistrib", data);

		@SuppressWarnings("unchecked")
		List<PDV> pdvs = (List<PDV>) query.list();

		if (pdvs.isEmpty()) {

			String mensagemValidacao = "Nenhum registro encontrado!";
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

		return pdvs;
	}

	
	/**
	 * Cria o Header para o arquivo a ser gerado
	 * 
	 * @param print
	 * @param numeroCota
	 * @param nome
	 */
	private void criaHeader(PrintWriter print, Integer numeroCota, String nome) {

		EMS0129Picking1Header outheader = new EMS0129Picking1Header();

		outheader.setCodigoCota(numeroCota);
		outheader.setNomeCota(nome);

		print.println(fixedFormatManager.export(outheader));

	}

	
	/**
	 * Cria o Header para o arquivo a ser gerado
	 * 
	 * @param print
	 * @param pdv
	 * @param data
	 */
	private void criaHeader(PrintWriter print, PDV pdv, Date data) {

		EMS0129Picking2Header outheader = new EMS0129Picking2Header();

		Pessoa pessoa = pdv.getCota().getPessoa();

		String nomeFantasia = pessoa.getNome();
		String cpfCnpj = null;

		if (pessoa instanceof PessoaFisica) {

			cpfCnpj = ((PessoaFisica) pessoa).getCpf();

		} else if (pessoa instanceof PessoaJuridica) {

			cpfCnpj = ((PessoaJuridica) pessoa).getCnpj();
		}

		String nomeCotaComCnpj = pdv.getNome() + "||" + cpfCnpj;
	
		outheader.setCodigoCota(pdv.getCota().getNumeroCota());
		outheader.setNomeCotaComCpfCnpj(nomeCotaComCnpj);
		outheader.setData(data);
		outheader.setNomeFantasia(nomeFantasia);

		print.println(fixedFormatManager.export(outheader));

	}

	
	/**
	 * Cria trailer do arquivo a ser gerado
	 * @param print
	 * @param numeroCota
	 * @param quantidadeRegistros
	 */
	private void criaTrailer(PrintWriter print, Integer numeroCota,
			int quantidadeRegistros) {

		EMS0129Picking1Trailer outtrailer = new EMS0129Picking1Trailer();

		outtrailer.setCodigoCota(numeroCota);
		outtrailer.setQuantidadeRegistros(quantidadeRegistros);

		print.println(fixedFormatManager.export(outtrailer));

	}

	
	/**
	 * Cria Trailer do arquivo a ser gerado
	 * 
	 * @param print
	 * @param numeroCota
	 * @param valorTotalBruto
	 * @param valorTotalDesconto
	 */
	private void criaTrailer(PrintWriter print, Integer numeroCota,	BigDecimal valorTotalBruto, BigDecimal valorTotalDesconto) {

		EMS0129Picking2Trailer outtrailer = new EMS0129Picking2Trailer();

		outtrailer.setCodigoCota(numeroCota);
		outtrailer.setValorTotalBruto(valorTotalBruto);
		outtrailer.setValorTotalDesconto(valorTotalDesconto);

		print.println(fixedFormatManager.export(outtrailer));

	}

	
	/**
	 * Cria os detalhes do arquivo a ser gerado
	 * 
	 * @param print
	 * @param numeroCota
	 * @param movimentoEstoqueCotas
	 * @return
	 */
	private int criarDetalhes(PrintWriter print, Integer numeroCota, Set<MovimentoEstoqueCota> movimentoEstoqueCotas) {

		EMS0129Picking1Detalhe outdetalhe = new EMS0129Picking1Detalhe();
		int qtdeRegistros = 0;

		for (MovimentoEstoqueCota moviEstCota : movimentoEstoqueCotas) {

			outdetalhe.setCodigoCota(numeroCota);
			outdetalhe.setQuantidade(moviEstCota.getQtde().longValue());
			ProdutoEdicao produtoEdicao = moviEstCota.getProdutoEdicao();
            outdetalhe.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			outdetalhe.setEdicao(produtoEdicao.getNumeroEdicao());
			outdetalhe.setNomePublicacao(produtoEdicao.getProduto().getNome());
			outdetalhe.setPrecoCusto(produtoEdicao.getPrecoCusto());
			
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			outdetalhe.setPrecoVenda(precoVenda);
			BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, moviEstCota.getCota(), produtoEdicao);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			outdetalhe.setDesconto(valorDesconto);

			if (!produtoEdicao.getLancamentos().isEmpty()) {

				for (Lancamento lanc : produtoEdicao.getLancamentos()) {

					outdetalhe.setSequenciaNotaEnvio(lanc.getSequenciaMatriz());

					print.println(fixedFormatManager.export(outdetalhe));

					qtdeRegistros++;
				}

			} else {

				print.println(fixedFormatManager.export(outdetalhe));

				qtdeRegistros++;
			}

		}

		return qtdeRegistros;
	}

	
	/**
	 * Cria os detalhes do arquivo a ser gerado
	 * 
	 * @param print
	 * @param numeroCota
	 * @param movimentoEstoqueCotas
	 * @param somaRegistros
	 * @return
	 */
	private SomaRegistro criarDetalhes(PrintWriter print,Integer numeroCota, 
					Set<MovimentoEstoqueCota> movimentoEstoqueCotas, SomaRegistro somaRegistros) {

		EMS0129Picking2Detalhe outdetalhe = new EMS0129Picking2Detalhe();
		int qtdeRegistros = 0;

		for (MovimentoEstoqueCota moviEstCota : movimentoEstoqueCotas) {
			
			outdetalhe.setCodigoCota(numeroCota);
			outdetalhe.setQuantidade(Long.valueOf(moviEstCota.getQtde().toString()));
			ProdutoEdicao produtoEdicao = moviEstCota.getProdutoEdicao();
            outdetalhe.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			outdetalhe.setEdicao(produtoEdicao.getNumeroEdicao());
			outdetalhe.setNomePublicacao(produtoEdicao.getProduto().getNome());
			outdetalhe.setPrecoCusto(produtoEdicao.getPrecoCusto());
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            outdetalhe.setPrecoVenda(precoVenda);
            
            BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, moviEstCota.getCota(), produtoEdicao);
            BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			outdetalhe.setDesconto(valorDesconto);
			
			if (!produtoEdicao.getLancamentos().isEmpty()) {

				for (Lancamento lanc : produtoEdicao.getLancamentos()) {

					outdetalhe.setSequenciaNotaEnvio(lanc.getSequenciaMatriz());

					print.println(fixedFormatManager.export(outdetalhe));

					qtdeRegistros++;
					
				}

			} else {

				print.println(fixedFormatManager.export(outdetalhe));

				qtdeRegistros++;

			}
			
			BigDecimal valorTotalBruto =
				produtoEdicao.getPrecoVenda().multiply(new BigDecimal(moviEstCota.getQtde()));
			
			BigDecimal valorTotalVenda =
				produtoEdicao.getPrecoCusto().multiply(new BigDecimal(moviEstCota.getQtde()));
			
			somaRegistros.addValorTotalBruto(valorTotalBruto);
			somaRegistros.addValorTotalDesconto(valorTotalVenda);
		}
		
		somaRegistros.addQtdeRegistros(qtdeRegistros);
	
		return somaRegistros;

	}


	/**
	 * Lança Exceção com Mensagem de Validação
	 * 
	 * @param mensagemValidacao
	 * @param message
	 * @throws ValidacaoException
	 */
	private void lancarMensagemValidacao(String mensagemValidacao, Message message) throws ValidacaoException {
		
		this.ndsiLoggerFactory.getLogger().logWarning(message,
				EventoExecucaoEnum.GERACAO_DE_ARQUIVO, mensagemValidacao);
		
		this.setMensagemValidacao(mensagemValidacao);
		
		throw new ValidacaoException(TipoMensagem.WARNING, mensagemValidacao);
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the mensagemValidacao
	 */
	public String getMensagemValidacao() {
		return mensagemValidacao;
	}

	/**
	 * @param mensagemValidacao the mensagemValidacao to set
	 */
	public void setMensagemValidacao(String mensagemValidacao) {
		this.mensagemValidacao = mensagemValidacao;
	}
	
}