package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.export.cnab.cobranca.DetalheSegmentoP;
import br.com.abril.nds.export.cnab.cobranca.Header;
import br.com.abril.nds.export.cnab.cobranca.Trailer;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.GeradorArquivoCobrancaBancoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

/**
 * Classe de implementação de serviços referentes 
 * a geração de arquivo de cobrança para o banco.
 * 
 * @author Discover Technology
 */
@Service
public class GeradorArquivoCobrancaBancoServiceImpl implements GeradorArquivoCobrancaBancoService {
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private BoletoRepository boletoRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Override
	public void prepararGerarArquivoCobrancaCnab() throws IOException {
		
		Map<Banco, List<DetalheSegmentoP>> mapaDadosArquivoCobranca =
			this.prepararDadosArquivoCobranca();
		
		this.gerarArquivo(mapaDadosArquivoCobranca);
	}
	
	/**
	 * Gera arquivo de cobrança de acordo com os dados informados.
	 * 
	 * @param mapaArquivoCobranca - mapa contendo os dados para geração do arquivo.
	 * 
	 * @throws IOException
	 */
	private void gerarArquivo(Map<Banco, List<DetalheSegmentoP>> mapaArquivoCobranca) throws IOException {
		
		if (mapaArquivoCobranca == null || mapaArquivoCobranca.isEmpty()) {
			return;
		}
		
		File diretorioArquivoCobranca = this.getFilePathParametroSistema();
		
		FixedFormatManager manager = new FixedFormatManagerImpl();
		
		List<String> conteudoLinhas = null;
		Header header = null;
		Trailer trailer = null;
		
		Distribuidor distribuidor = this.getDistribuidor();
		
		for (Map.Entry<Banco, List<DetalheSegmentoP>> entry : mapaArquivoCobranca.entrySet()) {
		
			conteudoLinhas = new ArrayList<String>();
		
			Banco banco = entry.getKey();
			
			header = this.getHeader(banco, distribuidor);
			
			conteudoLinhas.add(manager.export(header));
			
			List<DetalheSegmentoP> listaDetalheSegmentoP = entry.getValue();
			
			Long quantidadeRegistros = 0L;
			
			for (DetalheSegmentoP detalheSegmentoP : listaDetalheSegmentoP) {
				
				conteudoLinhas.add(manager.export(detalheSegmentoP));
				
				quantidadeRegistros ++;
			}
		
			trailer = this.getTrailer(banco, quantidadeRegistros);
			
			conteudoLinhas.add(manager.export(trailer));
			
			this.criarArquivo(conteudoLinhas, diretorioArquivoCobranca);
		}
	}

	/**
	 * Obtém o distribuidor.
	 */
	protected Distribuidor getDistribuidor() {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		return distribuidor;
	}

	/**
	 * Cria o arquivo informado de acordo com o conteúdo informado.
	 */
	private void criarArquivo(List<String> conteudoLinhas, File diretorioArquivoCobranca) throws IOException {
		
		File file =
			new File(diretorioArquivoCobranca, this.getNomeArquivoCobranca());
		
		if (file != null) {
		
			FileUtils.writeLines(file, "UTF8", conteudoLinhas);
		}
	}
	
	/**
	 * Obtém o nome do arquivo de cobrança.
	 */
	private String getNomeArquivoCobranca() {
		
		return "cnab_" + new Date().getTime() + ".dat";
	}

	/**
	 * Obtém o file contendo o diretório parametrizada para geração do arquivo de cobrança.
	 */
	protected File getFilePathParametroSistema() {
		
		ParametroSistema parametroSistema =
			this.parametroSistemaRepository.buscarParametroPorTipoParametro(
				TipoParametroSistema.PATH_GERACAO_ARQUIVO_COBRANCA);

		if (parametroSistema != null) {
			
			return new File(parametroSistema.getValor());
		}
		
		return null;
	}
	
	private Header getHeader(Banco banco, Distribuidor distribuidor) {
		
		Header header = new Header();

		//Controle
		header.setCodigoBanco(Long.parseLong(banco.getNumeroBanco()));
		header.setLote(0000L);
	
		//Serviço
		header.setOperacao("R"); //Opreação do tipo de remessa de arquivo
		
		//Empresa
		header.setTipoInscicao(2L);  //Opção CNPJ
		header.setNumeroInscricao(Long.parseLong(distribuidor.getJuridica().getCnpj()));
		header.setConvenio(null);//Código que identifica o contato entre o distribuidor e o banco
		
		header.setCodigoAgencia(banco.getAgencia());
		header.setNumeroConta(banco.getConta());
		header.setDvAgencia(banco.getDvAgencia());
		header.setDvAgenciaConta(banco.getDvConta());
		header.setNomeEmpresa(distribuidor.getJuridica().getRazaoSocial());	
		header.setDataGravacaoRemessaRetorno(this.getDataFormatoCNAB(distribuidor.getDataOperacao()));
	
		return header;
	}
	
	private Trailer getTrailer(Banco banco,Long quantidadeRegistros) {
		
		Trailer trailer = new Trailer();
		
		// Controle
		trailer.setCodigoBanco(Long.parseLong(banco.getNumeroBanco()));
		trailer.setLote(9999L);
		
		trailer.setQuantidadeRegistros(quantidadeRegistros);
		
		// Totalização da Cobrança Simples
		trailer.setQtdTitulosCobrancaSimples(null);
		trailer.setValorTitulosCobrancaSimples(null);
		
		//Totalização da Cobrança Vinculada
		trailer.setQtdTitulosCobrancaVinculada(null);
		trailer.setValorTitulosCobrancaVinculada(null);
		
		//Totalização da Cobrança Caucionada
		trailer.setQtdTitulosCobrancaCaucionada(null);
		trailer.setValorTitulosCobrancaCaucionada(null);
		
		// Totalização da Cobrança Descontada
		trailer.setQtdTitulosCobrancaDescontada(null);
		trailer.setValorTitulosCobrancaDescontada(null);
		
		trailer.setNumeroAviso(null);
		
		return trailer;
	}
	
	/**
	 * 
	 * Retorna um map com a estrutura do arquivo preenchida para geração.
	 * 
	 * @return Map<Banco, List<DetalheSegmentoP>> 
	 */
	protected Map<Banco, List<DetalheSegmentoP>> prepararDadosArquivoCobranca() {
		
		Map<Banco, List<DetalheSegmentoP>> inputDados = new HashMap<Banco, List<DetalheSegmentoP>>();
		
		Distribuidor distribuidor = getDistribuidor();
		
		List<Boleto> cobrancas = boletoRepository.obterBoletosGeradosNaDataOperacaoDistribuidor(distribuidor.getDataOperacao());
		
		Long numeroLote = 1L;
		
		for(Boleto cobranca : cobrancas){
			
			DetalheSegmentoP detalhe = obterDetalheSegmentoP(cobranca, numeroLote++);
			
			Banco banco = cobranca.getBanco();
			
			List<DetalheSegmentoP> listaDetalheP = 
										((inputDados.containsKey(banco)))
											?  inputDados.get(banco) 
													: new ArrayList<DetalheSegmentoP>(); 
			listaDetalheP.add(detalhe);
			
			inputDados.put(banco, listaDetalheP);
		}
		
		return inputDados;
	}
	
	/**
	 * Retorna um objeto com o detalhamento da segmentação "P" para geração de arquivo de remessa.
	 * 
	 * @param cobranca - cobrança a ser processada
	 * 
	 * @return DetalheSegmentoP
	 */
	private DetalheSegmentoP obterDetalheSegmentoP(Boleto cobranca,Long numeroLote) {
		
		DetalheSegmentoP detalheSegmentoP = new DetalheSegmentoP();
		
		Banco banco = cobranca.getBanco();
		
		//Controle
		detalheSegmentoP.setCodigoBanco(Long.parseLong(banco.getNumeroBanco()));
		
		detalheSegmentoP.setLote(numeroLote);
		
		//Serviço
		detalheSegmentoP.setNumeroRegistro(null); //TODO Sera o mesmo numero do lote
		detalheSegmentoP.setSegmento(null);//TODO
		detalheSegmentoP.setCodigoMovimento(null);//TODO;
		
		//Conta Corrente
		detalheSegmentoP.setCodigoAgencia(banco.getAgencia());
		detalheSegmentoP.setDvAgencia(banco.getDvAgencia());
		detalheSegmentoP.setNumeroConta(Long.parseLong(banco.getNumeroBanco()));
		detalheSegmentoP.setDvConta(banco.getDvConta());
		detalheSegmentoP.setNossoNumero(cobranca.getNossoNumeroCompleto());
		
		//Caracteristica Cobrança
		detalheSegmentoP.setCodigoCarteira(banco.getCarteira().longValue());
		detalheSegmentoP.setCadastramento(null);//TODO
		detalheSegmentoP.setTipoDocumento(null);//TODO
		detalheSegmentoP.setEmissaoBloqueto(null);//TODO
		detalheSegmentoP.setDistribuicaoBloqueto(null);//TODO;
		detalheSegmentoP.setNumeroDocumento(cobranca.getNossoNumeroCompleto());
		detalheSegmentoP.setDataVencimento(this.getDataFormatoCNAB(cobranca.getDataVencimento()));
		detalheSegmentoP.setValorTitulo(this.getValorFormatoCNAB(cobranca.getValor()));
		detalheSegmentoP.setAgenciaCobradora(banco.getAgencia());
		detalheSegmentoP.setEspecieTitulo(null);//TODO
		detalheSegmentoP.setAceite(null);//TODO
		detalheSegmentoP.setDataEmissaoTitulo(this.getDataFormatoCNAB(cobranca.getDataEmissao()));
		
		//Juros
		detalheSegmentoP.setCodigoJurosMora(null);//TODO
		detalheSegmentoP.setDataJurosMora(null);//TODO
		detalheSegmentoP.setJurosMora(null);//TODO
		
		//Desconto
		detalheSegmentoP.setCodigoDesconto(null);//TODO;
		detalheSegmentoP.setDataDesconto(null);//TODO
		detalheSegmentoP.setDesconto(null);//TODO
		detalheSegmentoP.setValorIOF(null);//TODO
		detalheSegmentoP.setValorAbatimento(null);//TODO
		detalheSegmentoP.setIdentificacaoTituloEmpresa(null);//TODO
		detalheSegmentoP.setCodigoProtesto(null);//TODO
		detalheSegmentoP.setPrazoProtesto(null);//TODO
		detalheSegmentoP.setCodigoBaixaDevolucao(null);//TODO
		detalheSegmentoP.setPrazoBaixaDevolucao(null);//TODO
		detalheSegmentoP.setCodigoMoeda(Moeda.REAL.getCodigo());
		detalheSegmentoP.setNumeroContrato(null);//TODO
		
		return detalheSegmentoP;
	}
	
	private Long getValorFormatoCNAB(BigDecimal valor){
		
		return Long.parseLong( valor.toString().replace(".","").trim());
	}
	
	private Long getDataFormatoCNAB(Date data ){
		
		return Long.parseLong(DateUtil.formatarData(data, Constantes.FORMATO_DATA_ARQUIVO_CNAB));
	}
}
