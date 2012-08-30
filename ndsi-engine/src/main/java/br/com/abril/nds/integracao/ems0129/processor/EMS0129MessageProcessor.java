package br.com.abril.nds.integracao.ems0129.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0129.data.SomaRegistro;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Detalhe;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Header;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Trailer;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Detalhe;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Header;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Trailer;
import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.LeiautePicking;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.util.MathUtil;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class EMS0129MessageProcessor extends AbstractRepository implements MessageProcessor  {
	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0129Route.class);

	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DescontoService descontoService;

	
	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor.getLeiautePicking().equals(LeiautePicking.UM)) {

			geraArquivoPicking1(message);

		} else if (distribuidor.getLeiautePicking().equals(LeiautePicking.DOIS)) {

			geraArquivoPicking2(message);

		} else if (distribuidor.getLeiautePicking().equals(LeiautePicking.TRES)) {

			if (LOGGER.isWarnEnabled()) {

				LOGGER.warn(String.format("Leiaute Picking Distribuidor %s nao implementado !",
						distribuidor.getLeiautePicking()));
			}
			
		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Leiaute Picking Invalido.");

			throw new RuntimeException("Leiaute Picking Invalido.");
		}

	}

	private void geraArquivoPicking1(Message message) {

		List<PDV> pdvs = findListPDV(message);

		try {

			PrintWriter print = new PrintWriter(
					new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue()) + "/PICKING1.NEW"));

			for (PDV pdv : pdvs) {

				int numeroCota = pdv.getCota().getNumeroCota();

				criaHeader(print, numeroCota, pdv.getNome());

				int qtdeRegistros = criarDetalhes(print, numeroCota, pdv.getCota().getMovimentoEstoqueCotas());

				criaTrailer(print, numeroCota, qtdeRegistros);

			}

			print.flush();
			print.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void geraArquivoPicking2(Message message) {

		List<PDV> pdvs = findListPDV(message);

		Date data = getDataLancDistrib(message);
		
		SomaRegistro somaRegistros = null;

		try {

			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())	+ "/PICKING2.NEW"));

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

			e.printStackTrace();
		}

	}

	private Date getDataLancDistrib(Message message) {
		
		Date data = (Date) message.getHeader().get("DATA_LCTO_DISTRIB");
		
		if (data == null) {
			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Data nao informada!");

			throw new RuntimeException("Execute a interface EMS0129 passando a dataLancamentoDistribuidor !");
		}
		
		return data;
	}
	
	
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

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Nenhum registro encontrado!");

			throw new RuntimeException("Nenhum registro encontrado!");

		} else {

			return pdvs;
		}
	}

	private void criaHeader(PrintWriter print, Integer numeroCota, String nome) {

		EMS0129Picking1Header outheader = new EMS0129Picking1Header();

		outheader.setCodigoCota(numeroCota);
		outheader.setNomeCota(nome);

		print.println(fixedFormatManager.export(outheader));

	}

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

		String nomeCotaComCnpj = pdv.getNome() + "-" + cpfCnpj;
	
		outheader.setCodigoCota(pdv.getCota().getNumeroCota());
		outheader.setNomeCotaComCpfCnpj(nomeCotaComCnpj.replaceAll(" ", ""));
		outheader.setData(data);
		outheader.setNomeFantasia(nomeFantasia);

		print.println(fixedFormatManager.export(outheader));

	}

	private void criaTrailer(PrintWriter print, Integer numeroCota,
			int quantidadeRegistros) {

		EMS0129Picking1Trailer outtrailer = new EMS0129Picking1Trailer();

		outtrailer.setCodigoCota(numeroCota);
		outtrailer.setQuantidadeRegistros(quantidadeRegistros);

		print.println(fixedFormatManager.export(outtrailer));

	}

	private void criaTrailer(PrintWriter print, Integer numeroCota,	BigDecimal valorTotalBruto, BigDecimal valorTotalDesconto) {

		EMS0129Picking2Trailer outtrailer = new EMS0129Picking2Trailer();

		outtrailer.setCodigoCota(numeroCota);
		outtrailer.setValorTotalBruto(valorTotalBruto);
		outtrailer.setValorTotalDesconto(valorTotalDesconto);

		print.println(fixedFormatManager.export(outtrailer));

	}

	private int criarDetalhes(PrintWriter print, Integer numeroCota, Set<MovimentoEstoqueCota> movimentoEstoqueCotas) {

		EMS0129Picking1Detalhe outdetalhe = new EMS0129Picking1Detalhe();
		int qtdeRegistros = 0;

		for (MovimentoEstoqueCota moviEstCota : movimentoEstoqueCotas) {

			outdetalhe.setCodigoCota(numeroCota);
			outdetalhe.setQuantidade(moviEstCota.getQtde());
			ProdutoEdicao produtoEdicao = moviEstCota.getProdutoEdicao();
            outdetalhe.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			outdetalhe.setEdicao(produtoEdicao.getNumeroEdicao());
			outdetalhe.setNomePublicacao(produtoEdicao.getProduto().getNome());
			outdetalhe.setPrecoCusto(produtoEdicao.getPrecoCusto());
			
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			outdetalhe.setPrecoVenda(precoVenda);
			BigDecimal percentualDesconto = descontoService.obterDescontoPorCotaProdutoEdicao(moviEstCota.getCota(), produtoEdicao);
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

	private SomaRegistro criarDetalhes(PrintWriter print,Integer numeroCota, 
					Set<MovimentoEstoqueCota> movimentoEstoqueCotas, SomaRegistro somaRegistros) {

		EMS0129Picking2Detalhe outdetalhe = new EMS0129Picking2Detalhe();
		int qtdeRegistros = 0;

		for (MovimentoEstoqueCota moviEstCota : movimentoEstoqueCotas) {
			
			outdetalhe.setCodigoCota(numeroCota);
			outdetalhe.setQuantidade(moviEstCota.getQtde());
			ProdutoEdicao produtoEdicao = moviEstCota.getProdutoEdicao();
            outdetalhe.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			outdetalhe.setEdicao(produtoEdicao.getNumeroEdicao());
			outdetalhe.setNomePublicacao(produtoEdicao.getProduto().getNome());
			outdetalhe.setPrecoCusto(produtoEdicao.getPrecoCusto());
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            outdetalhe.setPrecoVenda(precoVenda);
            
            BigDecimal percentualDesconto = descontoService.obterDescontoPorCotaProdutoEdicao(moviEstCota.getCota(), produtoEdicao);
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
			
			somaRegistros.addValorTotalBruto(produtoEdicao.getPrecoCusto());
			somaRegistros.addValorTotalDesconto(precoVenda);
			
		}
		
		somaRegistros.addQtdeRegistros(qtdeRegistros);
	
		return somaRegistros;

	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}