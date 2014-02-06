package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CobrancaImpressaoDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.ItemSlipVendaEncalheDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoArquivo;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ControleNumeracaoSlipService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ImpressaoMatricialUtil;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.StringUtil;

@Service
public class DocumentoCobrancaServiceImpl implements DocumentoCobrancaService {
    
    private static final Logger LOGGER = Logger.getLogger(DocumentoCobrancaServiceImpl.class);


	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Autowired
	private ControleNumeracaoSlipService controleNumeracaoSlipService;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Autowired
	private ConferenciaEncalheRepository conferenciaEncalheRepository;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * BOLETO/COBRANCA
	 * 
	 * Envia Cobranca por Email
	 * @param nossoNumero
	 */
	@Transactional
	@Override
	public void enviarDocumentoCobrancaPorEmail(String nossoNumero){
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		try {
			switch (cobranca.getTipoCobranca()) {
				case BOLETO:

					boletoService.enviarBoletoEmail(nossoNumero);
					break;

				default:
					enviarDocumentoPorEmail(cobranca);
				
			}
			
		} catch (Exception e) {
			
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Erro ao enviar e-mail de arquivo de cobrança para nosso número: " + nossoNumero + " - "
                            + e.getMessage());
		}
		
		this.cobrancaRepository.incrementarVia(nossoNumero);
	}
	
	/**
	 * BOLETO/COBRANCA
	 * 
	 * Gerar documento de Cobranca
	 * @param dividas
	 * @param tipoCobranca
	 */
	@Transactional
	@Override
	public byte[] gerarDocumentoCobranca(List<GeraDividaDTO> dividas, TipoCobranca tipoCobranca) {
		
		List<String> listNossoNumero = getNossoNumeros(dividas); 
		
		byte[] arquivo = null;
		
		try {
		
			if(TipoCobranca.BOLETO.equals(tipoCobranca)){
	
				arquivo =boletoService.gerarImpressaoBoletos(listNossoNumero);
			}
			else{
				
				arquivo = gerarDocumentoCobrancas(listNossoNumero);
			}
			
			if(arquivo!= null){
				this.cobrancaRepository.incrementarVia( listNossoNumero.toArray(new String[] {}) );
			}
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
		}
		
		return arquivo;
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Retorna um grupo de documentos de cobrança.
     * 
     * @param listNossoNumero
     * @return byte[]
     * @throws JRException
     */
	private byte[] gerarDocumentoCobrancas(List<String> listNossoNumero) throws Exception {
		
		List<CobrancaImpressaoDTO> cobrancas = new ArrayList<CobrancaImpressaoDTO>();
		
		Cobranca cobranca = null;
		
		String razaoSocial = this.distribuidorService.obterRazaoSocialDistribuidor();
		
		for(String nossoNumero : listNossoNumero) {
			
			cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
			
			cobrancas.add(obterCobrancaImpressaoDTO(cobranca, razaoSocial));
		}
		
		byte[] arquivo = gerarDocumentoIreport(cobrancas);
		
		return arquivo ;
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Retorna uma lista com os nosso número referente as cobranças.
     * 
     * @param dividas
     * @return List<String>
     */
	private List<String> getNossoNumeros(List<GeraDividaDTO> dividas){
		
		List<String> list = new ArrayList<String>();
		
		for(GeraDividaDTO dto : dividas){
			list.add(dto.getNossoNumero());
		}
		
		return list;
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Envia um tipo de cobrança por email.
     * 
     * @param cobranca
     * @throws AutenticacaoEmailException
     * @throws JRException
     */
	private void enviarDocumentoPorEmail(Cobranca cobranca) throws AutenticacaoEmailException, Exception {
		
		String assunto = this.distribuidorService.assuntoEmailCobranca();
		
		String mensagem = this.distribuidorService.mensagemEmailCobranca();
		
		String emailCota = cobranca.getCota().getPessoa().getEmail();
		String[] destinatarios = new String[]{emailCota};
		
		byte[] arquivo = getDocumentoCobranca(cobranca);
		
		AnexoEmail anexoEmail = new AnexoEmail();
		anexoEmail.setNome(cobranca.getNossoNumero());
		anexoEmail.setAnexo(arquivo);
		anexoEmail.setTipoAnexo(AnexoEmail.TipoAnexo.PDF);
		
		emailService.enviar(assunto,mensagem,destinatarios,anexoEmail);
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Retorna o documento de cobrança gerado pelo Ireport
     * 
     * @param cobrancas
     * @return byte[]
     * @throws JRException
     */
	private byte[] getDocumentoCobranca(Cobranca... cobrancas) throws Exception{
		
		String razaoSocial = this.distribuidorService.obterRazaoSocialDistribuidor();
		
		List<CobrancaImpressaoDTO> list = new ArrayList<CobrancaImpressaoDTO>();
		
		for(Cobranca cobranca : cobrancas){
			list.add(obterCobrancaImpressaoDTO(cobranca, razaoSocial));
		}
		
		return gerarDocumentoIreport(list);
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Retorna um grupo de documentos de cobrança gerado pelo Ireport
     * 
     * @param list
     * @return byte[]
     * @throws JRException
     */
	private byte[] gerarDocumentoIreport(List<CobrancaImpressaoDTO> list) throws Exception{
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		 Map<String, Object> map = getInformacoesDistribuido();
		 URL url = Thread.currentThread().getContextClassLoader()
				 	.getResource("/reports/cobranca.jasper");
		 //Executa o decode do path do arquivo
		 String path = url.toURI().getPath();
		 
		 return  JasperRunManager.runReportToPdf(path, map, jrDataSource);
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Retorna as informações do distribuidor para montagem dos parâmetros do
     * Ireport
     * 
     * @return Map<String, Object>
     */
	private Map<String, Object> getInformacoesDistribuido(){
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		EnderecoDistribuidor enderecoDistribuidor = distribuidor.getEnderecoDistribuidor();
		
		map.put("cidade",(enderecoDistribuidor.getEndereco()==null)?"": enderecoDistribuidor.getEndereco().getCidade());
		map.put("enderecoDistribuidor", this.obterDescricaoEnderecoDistribuidor(enderecoDistribuidor) );
		
		for(TelefoneDistribuidor telefone : distribuidor.getTelefones()){
			
			if(telefone.isPrincipal()){
				map.put("telefoneDistribuidor", (telefone.getTelefone()== null)?""
						:telefone.getTelefone().getDdd() + " - " +
							telefone.getTelefone().getNumero());
				break;
			}
		}
		
		map.put("data", new Date());
		map.put("nomeDistribuidor",distribuidor.getJuridica().getRazaoSocial());
		
		InputStream logoDistribuidor = parametrosDistribuidorService.getLogotipoDistribuidor();

		if(logoDistribuidor == null){
			logoDistribuidor = new ByteArrayInputStream(new byte[0]);;
		}
		
		map.put("imagem",JasperUtil.getImagemRelatorio(logoDistribuidor));
		
		return map;
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Obtem descrição concatenada do endereço do distribuidor
     * 
     * @param enderecoDistribuidor
     * @return String
     */
	private String obterDescricaoEnderecoDistribuidor(EnderecoDistribuidor enderecoDistribuidor){
		
		Endereco endereco  = enderecoDistribuidor.getEndereco();
		
		if(endereco == null){
			return null;
		}
		
		StringBuilder descricao = new StringBuilder();
		
		descricao.append(endereco.getTipoLogradouro()).append(" ")
					.append(endereco.getLogradouro()).append(", ")
					.append(endereco.getNumero()).append(" - ")
					.append(endereco.getBairro()).append(" - ")
					.append("CEP ").append(endereco.getCep()).append(" ")
					.append(endereco.getCidade()).append(" - ")
					.append(endereco.getUf()); 
		
		return descricao.toString();
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Monta a estrutura do objeto para impressão no Ireport
     * 
     * @param cobranca
     * @param distribuidor
     * @return CobrancaImpressaoDTO
     */
	private CobrancaImpressaoDTO obterCobrancaImpressaoDTO(Cobranca cobranca, String razaoSocialDistribuidor){
		
		CobrancaImpressaoDTO impressaoDTO = new CobrancaImpressaoDTO();
		
		Banco banco  = cobranca.getBanco();
		
		this.atribuirDadosBanco(impressaoDTO, banco);
		
		Cota cota  = cobranca.getCota();
		
		this.atribuirDadosCota(razaoSocialDistribuidor, impressaoDTO, cota);
		
		BigDecimal valor  = cobranca.getValor();
		valor = valor.setScale(2,RoundingMode.HALF_EVEN);
		
		impressaoDTO.setTipoCobranca(cobranca.getTipoCobranca());
		impressaoDTO.setValor(valor);
		impressaoDTO.setVencimento(cobranca.getDataVencimento());
		
		return impressaoDTO;
	}

	    /**
     * BOLETO/COBRANCA
     * 
     * Atribui os dados da cota para a impressão
     * 
     * @param razaoSocialDistribuidor
     * @param impressaoDTO
     * @param cota
     */
	private void atribuirDadosCota(String razaoSocialDistribuidor,CobrancaImpressaoDTO impressaoDTO, Cota cota) {
		
		if(cota!= null){
			
			Pessoa pessoa  = cota.getPessoa();
			
			if (pessoa instanceof PessoaFisica){
				impressaoDTO.setNomeCota(((PessoaFisica) pessoa).getNome());
			}
			else if(pessoa instanceof PessoaJuridica){
				impressaoDTO.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial()); 
			}
			
			impressaoDTO.setNomeFavorecido(razaoSocialDistribuidor);
			impressaoDTO.setNumeroCota(cota.getNumeroCota());
			
			impressaoDTO.setBox((cota.getBox()!= null)?cota.getBox().getCodigo() + " - " + cota.getBox().getNome():"");
			
			Roteirizacao roteirizacao = roteirizacaoService.buscarRoteirizacaoDeCota(cota.getNumeroCota());
			
			if(roteirizacao!= null){
				
				if(roteirizacao.getBox()!=null){
					
					Roteiro roteiro = roteirizacaoService.obterRoteiroDeBoxPorOrdem(roteirizacao.getBox().getId());
					
					if (roteiro!=null){
						
						impressaoDTO.setRoteiro(roteiro.getDescricaoRoteiro());
						
						Rota rota = roteirizacaoService.obterRotaDeRoteiroPorOrdem(roteiro.getId());
					
					    impressaoDTO.setRota( (rota!= null)? rota.getDescricaoRota():"");
					}
				}
			}
			else{
		
				impressaoDTO.setRota("");
				impressaoDTO.setRoteiro("");
			}
		}
	}

	    /**
     * BOLETO/COBRANCA
     * 
     * Atribui os dados do Banco para a impressão
     * 
     * @param impressaoDTO
     * @param banco
     */
	private void atribuirDadosBanco(CobrancaImpressaoDTO impressaoDTO, Banco banco) {
		
		if(banco!= null){
			
			String agencia = (banco.getAgencia() == null) ? "" : banco.getAgencia().toString();
			
			String conta = (banco.getConta() == null) ? "" : banco.getConta().toString();
			
			if(banco.getDvAgencia()!= null && !banco.getDvAgencia().isEmpty()){
				agencia += " - " + banco.getDvAgencia();
			}
			
			if(banco.getDvConta()!= null && !banco.getDvConta().isEmpty()){
				conta += " - " + banco.getDvConta();
			}
			
			impressaoDTO.setAgencia(agencia);
			impressaoDTO.setConta(conta);
			impressaoDTO.setNomeBanco(banco.getNome());
		}
	}
	
	    /**
     * BOLETO/COBRANCA
     * 
     * Gera documento de cobrança
     * 
     * @param nossoNumero
     */
	@Override
	@Transactional
	public byte[] gerarDocumentoCobranca(String nossoNumero) {
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		byte[] retorno = null;
	
		try {
			
			switch (cobranca.getTipoCobranca()) {
			
				case BOLETO:
					
					retorno = boletoService.gerarImpressaoBoleto(nossoNumero);
					
					break;
					
				case BOLETO_EM_BRANCO:
					
					return null;
					
				default:
					
					retorno = getDocumentoCobranca(cobranca);
			}
			
		}
		catch (Exception e) {
			
            throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar arquivo de cobrança para nosso número: "
                    + nossoNumero + " - " + e.getMessage());
		}
		
		Integer vias = (cobranca.getVias() == null) ? 1 : (cobranca.getVias()+1);
		
		cobranca.setVias(vias);
		
		this.cobrancaRepository.merge(cobranca);
		
		return retorno;
	}

	
	
	/**
	 * SLIP
	 * 
	 * Obtem Ordinal
	 * @param x
	 * @return String
	 */
	private String getDiaMesOrdinal(Integer x){
		String ord="";
		String aux="";
		aux = Long.toString(x);
		
		int i=1;
		
		if (aux.length()>1){
			aux = aux.substring(i-1, i);
			switch (Integer.parseInt(aux)){ 
			    case 1:
                ord += "DÉCIMO";
			        break;
			    case 2:
                ord += "VIGÉSIMO";
			        break;
			    case 3:
                ord += "TRIGÉSIMO";
			        break;
			    default:
			    	ord+="";
			}
			i++;
		}
		
		aux = Long.toString(x);
		
		aux = aux.substring(i-1,i);
		
		switch (Integer.parseInt(aux)){ 
		    case 1:
		    	ord+=" PRIMEIRO";
		    	break;
		    case 2:
		    	ord+=" SEGUNDO";
		    	break;
		    case 3:
		    	ord+=" TERCEIRO";
		    	break;
		    case 4:
		    	ord+=" QUARTO";
		    	break;
		    case 5:
		    	ord+=" QUINTO";
		    	break;
		    case 6:
		    	ord+="SEXTO";
		    	break;
		    case 7:
            ord += " SÉTIMO";
		    	break;
		    case 8:
		    	ord+=" OITAVO";
		        break;
		    case 9:
		    	ord+=" NONO";
		        break;
		    default:
		    	ord+="";
	    }

		return ord;
	}
	
	    /**
     * SLIP
     * 
     * Ordena lista de produtos da impressão por dia
     * 
     * @param listaProdutoEdicaoSlip
     */
	@SuppressWarnings("unchecked")
	private void ordenarListaPorDia(List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip) {

		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("dia", new NullComparator()));
		
		Collections.sort(listaProdutoEdicaoSlip, comparatorChain);
	}
	
	private boolean exibirSubtotal(List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip, int i,
			   ProdutoEdicaoSlipDTO itemLista) {

		boolean exibirSubtotal =
		(i == listaProdutoEdicaoSlip.size() - 1)
		|| (itemLista.getDia() != listaProdutoEdicaoSlip.get(i + 1).getDia());
		
		return exibirSubtotal;
	}
	
	/**
	 * SLIP
	 * 
	 * Calcula totais dos produtos do Slip
	 * @param listaProdutoEdicaoSlip
	 */
    private void calcularTotaisListaSlip(List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip) {
		
		boolean exiberSubtotal;
		BigInteger qtdeTotalProdutosDia = null;
		BigDecimal valorTotalEncalheDia = null;
		
		for(int i = 0; i < listaProdutoEdicaoSlip.size(); i++) {
		
			ProdutoEdicaoSlipDTO produtoSlip = listaProdutoEdicaoSlip.get(i);
			
			qtdeTotalProdutosDia =
				BigIntegerUtil.soma(qtdeTotalProdutosDia, produtoSlip.getEncalhe());   
			
			valorTotalEncalheDia =
				BigDecimalUtil.soma(valorTotalEncalheDia, produtoSlip.getValorTotal());
			
			exiberSubtotal = this.exibirSubtotal(listaProdutoEdicaoSlip, i, produtoSlip);
            
			if (exiberSubtotal) {
			
				produtoSlip.setQtdeTotalProdutos(String.valueOf(qtdeTotalProdutosDia.intValue()));
				
				produtoSlip.setValorTotalEncalhe(CurrencyUtil.formatarValor(valorTotalEncalheDia));
				
				qtdeTotalProdutosDia = BigInteger.ZERO;
				valorTotalEncalheDia = BigDecimal.ZERO;
			}
		}
	}

    /**
     * SLIP
     * 
     * Obtem logo do Distribuidor
     * @return InputStream
     */
    protected InputStream getLogoDistribuidor(){
		
		InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null){
		  
			return new ByteArrayInputStream(new byte[0]);
		}
		
		return inputStream;
	}

    /**
     * SLIP
     * 
     * Retorna a data de operação do controle confernecia encalhe junto com o
     * horario da data de finalização de controle conferencia encalhe.
     * 
     * @param dataFimConferencia - data de finalização de controle conferencia
     *            encalhe cota
     * @param dataOperacaoConferenia - data de operação de controle conferencia
     *            encalhe cota
     * @return Date - data de operação de controle conferencia encalhe cota com
     *         horario de finalização da conferencia
     */
	private Date obterDataOperacaoConferencia(Date dataFimConferencia, Date dataOperacaoConferenia){
		
		Calendar dataFinalConferencia =  Calendar.getInstance();
		dataFinalConferencia.setTime(dataFimConferencia);
		
		Calendar  dataOperacaoConferencia  = Calendar.getInstance();
		dataOperacaoConferencia.setTime(dataOperacaoConferenia);
		dataOperacaoConferencia.add(Calendar.HOUR, dataFinalConferencia.get(Calendar.HOUR_OF_DAY));
		dataOperacaoConferencia.add(Calendar.MINUTE, dataFinalConferencia.get(Calendar.MINUTE));
		dataOperacaoConferencia.add(Calendar.SECOND, dataFinalConferencia.get(Calendar.SECOND));
		
		return dataOperacaoConferencia.getTime();
	}
	
	    /**
     * SLIP
     * 
     * Obtem todos os dados da impressão do Slip
     * 
     * @param idControleConferenciaEncalheCota
     * @param incluirNumeroSlip
     * @return SlipDTO
     */
    private SlipDTO setParamsSlip(Long idControleConferenciaEncalheCota, boolean incluirNumeroSlip) {
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);
		
		if(incluirNumeroSlip || controleConferenciaEncalheCota.getNumeroSlip() == null || controleConferenciaEncalheCota.getNumeroSlip() < 1) {
			controleConferenciaEncalheCota.setNumeroSlip(controleNumeracaoSlipService.obterProximoNumeroSlip(TipoSlip.SLIP_CONFERENCIA_ENCALHE));
			controleConferenciaEncalheCotaRepository.alterar(controleConferenciaEncalheCota);
		}
		
		Date dataOperacao = controleConferenciaEncalheCota.getDataOperacao();
		
		List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip = conferenciaEncalheRepository.obterDadosSlipConferenciaEncalhe(idControleConferenciaEncalheCota);
		
		Integer numeroCota 		= controleConferenciaEncalheCota.getCota().getNumeroCota();
	
		String nomeCota 		= controleConferenciaEncalheCota.getCota().getPessoa().getNome();
		Date dataConferencia 	= this.obterDataOperacaoConferencia(controleConferenciaEncalheCota.getDataFim(), controleConferenciaEncalheCota.getDataOperacao());
		Integer codigoBox 		= controleConferenciaEncalheCota.getBox().getCodigo();
		Long numeroSlip 		= controleConferenciaEncalheCota.getNumeroSlip();
		
		BigInteger qtdeTotalProdutos 	   = null;
		BigDecimal valorTotalEncalhe 	   = null;
		BigDecimal valorTotalPagar 		   = BigDecimal.ZERO;
		
		BigDecimal valorTotalReparte = this.conferenciaEncalheService.obterValorTotalReparte(numeroCota, dataOperacao);
		BigDecimal valorTotalDesconto = this.conferenciaEncalheService.obterValorTotalDesconto(numeroCota, dataOperacao);
		BigDecimal valorTotalSemDesconto = this.conferenciaEncalheService.obterValorTotalReparteSemDesconto(numeroCota, dataOperacao);
		
		Integer dia=0;
		
		for(ProdutoEdicaoSlipDTO produtoEdicaoSlip : listaProdutoEdicaoSlip) {
				
 			qtdeTotalProdutos = BigIntegerUtil.soma(qtdeTotalProdutos, produtoEdicaoSlip.getEncalhe());
			
			valorTotalEncalhe = BigDecimalUtil.soma(valorTotalEncalhe, produtoEdicaoSlip.getValorTotal());
			
			if(produtoEdicaoSlip.getReparte() == null) {
				
				produtoEdicaoSlip.setReparte(BigInteger.ZERO);
				
			}
			
			dia = produtoEdicaoSlip.getDia();
			
			String ordinal = ((dia != null) ? this.getDiaMesOrdinal(dia) + " DIA" : "PRODUTOS ANTECIPADOS");
		
			produtoEdicaoSlip.setOrdinalDiaConferenciaEncalhe(ordinal);	
		}
		
		this.ordenarListaPorDia(listaProdutoEdicaoSlip);
		
		this.calcularTotaisListaSlip(listaProdutoEdicaoSlip);
		
		valorTotalReparte = (valorTotalReparte == null) ? BigDecimal.ZERO : valorTotalReparte;
		valorTotalEncalhe = (valorTotalEncalhe == null) ? BigDecimal.ZERO : valorTotalEncalhe;
		qtdeTotalProdutos = (qtdeTotalProdutos == null) ? BigInteger.ZERO : qtdeTotalProdutos;
		
		BigDecimal valorVenda = (valorTotalReparte.subtract(valorTotalEncalhe));
		
		SlipDTO slipDTO = new SlipDTO();
		
		slipDTO.setNumeroCota(numeroCota);
		slipDTO.setNomeCota(nomeCota);
		slipDTO.setDataConferencia(dataConferencia);           
		slipDTO.setCodigoBox(codigoBox.toString());                   
		slipDTO.setTotalProdutoDia(qtdeTotalProdutos);  
		slipDTO.setTotalProdutos(qtdeTotalProdutos);    
		slipDTO.setValorEncalheDia(valorTotalEncalhe);    
		slipDTO.setValorTotalEncalhe(valorTotalEncalhe);
		
		slipDTO.setValorTotalDesconto(valorTotalDesconto);
		slipDTO.setValorTotalSemDesconto(valorTotalSemDesconto);
		
		slipDTO.setValorDevido(valorTotalReparte);        
		slipDTO.setValorSlip(valorTotalReparte.subtract(valorTotalEncalhe));           
		
		slipDTO.setValorTotalPagar(valorTotalPagar); 
		slipDTO.setNumSlip(numeroSlip);                 
		slipDTO.setListaProdutoEdicaoSlipDTO(listaProdutoEdicaoSlip);
		
		BigDecimal pagamentoPendente = slipDTO.getValorTotalPagar().compareTo(valorVenda)>0?slipDTO.getValorTotalPagar().subtract(valorVenda):BigDecimal.ZERO;
		
		BigDecimal valorCreditoDif = valorVenda.compareTo(slipDTO.getValorTotalPagar())>0?valorVenda.subtract(slipDTO.getValorTotalPagar()):BigDecimal.ZERO;
		
		Map<String, Object> parametersSlip = new HashMap<String, Object>();
		slipDTO.setParametersSlip(parametersSlip);
		
		parametersSlip.put("NUMERO_COTA", slipDTO.getNumeroCota());
		parametersSlip.put("NOME_COTA", slipDTO.getNomeCota());
		parametersSlip.put("NUM_SLIP", numeroSlip.toString());
		parametersSlip.put("CODIGO_BOX", slipDTO.getCodigoBox());
		parametersSlip.put("DATA_CONFERENCIA", slipDTO.getDataConferencia());
		parametersSlip.put("CE_JORNALEIRO", slipDTO.getCeJornaleiro());
		parametersSlip.put("TOTAL_PRODUTOS", slipDTO.getTotalProdutos());
		parametersSlip.put("VALOR_TOTAL_ENCA", slipDTO.getValorTotalEncalhe() );
		parametersSlip.put("VALOR_PAGAMENTO_POSTERGADO", slipDTO.getValorTotalPagar());
		parametersSlip.put("VALOR_PAGAMENTO_PENDENTE", pagamentoPendente);
		parametersSlip.put("VALOR_MULTA_MORA", slipDTO.getValorTotalPagar());
		parametersSlip.put("VALOR_CREDITO_DIF", valorCreditoDif);
		slipDTO.setCeJornaleiro(null);
		
		try {
			
			parametersSlip.put("LOGOTIPO", JasperUtil.getImagemRelatorio(getLogoDistribuidor()));
		
		} catch(Exception e) {
		
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Erro ao carregar logotipo do distribuidor no documento de cobrança");
		
		}
		
		List<DebitoCreditoCotaDTO> listaComposicaoCobranca = 
				this.debitoCreditoCotaService.obterListaDebitoCreditoCotaDTO(controleConferenciaEncalheCota.getCota(), dataOperacao);
		
		slipDTO.setListaComposicaoCobrancaDTO(listaComposicaoCobranca);
		
		parametersSlip.put("LISTA_COMPOSICAO_COBRANCA",listaComposicaoCobranca);
		
		BigDecimal totalComposicao = BigDecimal.ZERO;
		
		for(DebitoCreditoCotaDTO item : listaComposicaoCobranca){
			
            // TOTALIZAÇÃO DO SLIP CONSIDERANDO COMPOSIÇÃO DE COBRANÇA
            // débito para o distribuidor, não para a cota
		    if (OperacaoFinaceira.DEBITO.equals(item.getTipoLancamento())) {
		    	
				totalComposicao = totalComposicao.add(item.getValor());
				
			} else {
				
				totalComposicao = totalComposicao.subtract(item.getValor());
			}
		}
		
		totalComposicao = slipDTO.getValorSlip().add(totalComposicao).abs();
		
		BigDecimal totalPagar = totalComposicao;
		
		slipDTO.setValorTotalPagar(totalPagar);

		slipDTO.setValorLiquidoDevido(slipDTO.getValorTotalSemDesconto().subtract(slipDTO.getValorTotalDesconto()));
		
		parametersSlip.put("VALOR_LIQUIDO_DEVIDO", slipDTO.getValorLiquidoDevido());
		
		parametersSlip.put("VALOR_DEVIDO", valorTotalReparte);
		
		parametersSlip.put("VALOR_SLIP", slipDTO.getValorSlip());
		
		parametersSlip.put("VALOR_TOTAL_SEM_DESCONTO", slipDTO.getValorTotalSemDesconto());
		
		parametersSlip.put("VALOR_TOTAL_DESCONTO", slipDTO.getValorTotalDesconto());
		
		parametersSlip.put("VALOR_TOTAL_PAGAR", totalPagar);
		
		parametersSlip.put("RAZAO_SOCIAL_DISTRIBUIDOR", this.distribuidorService.obterRazaoSocialDistribuidor());
		
		return slipDTO;
	}
    
    /**
     * SLIP
     * 
     * Gera impressão de Slip para Impressoras Matriciais
     * 
     * @param slipDTO
     * @return byte[]
     */
    @Transactional
	public byte[] gerarSlipTxtMatricial(SlipDTO slipDTO){
		
		StringBuffer sb = new StringBuffer();
		ImpressaoMatricialUtil e = new ImpressaoMatricialUtil(sb);
		Integer ultimoDia = null;
		
		e.darEspaco(1);
		e.adicionar(this.distribuidorService.obterRazaoSocialDistribuidor());
		e.quebrarLinhaEscape();
		e.darEspaco(3);
		e.adicionar("SLIP DE RECOLHIMENTO DE ENCALHE");
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
		e.adicionar("Cota: "+slipDTO.getNumeroCota()+" - "+slipDTO.getNomeCota());
		e.quebrarLinhaEscape();
//		e.adicionar("Data: "+new SimpleDateFormat("dd/MM/yyyy").format(slipDTO.getDataConferencia()));
//		e.quebrarLinhaEscape();
//		e.adicionar("Hora: "+new SimpleDateFormat("HH:mm:ss").format(slipDTO.getDataConferencia()));
//		e.quebrarLinhaEscape();
		e.adicionar("BOX:  "+slipDTO.getCodigoBox());e.darEspaco(2);e.adicionar("Num. Slip: "+(slipDTO.getNumSlip() == null ? 0 : slipDTO.getNumSlip()));
		e.quebrarLinhaEscape();
		e.adicionar("----------------------------------------");
		e.quebrarLinhaEscape();
		
		boolean exibirSubtotal = false;
		
		List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip = slipDTO.getListaProdutoEdicaoSlipDTO();
		
		for(int i = 0; i < listaProdutoEdicaoSlip.size(); i++) {
			
			
			ProdutoEdicaoSlipDTO itemLista = listaProdutoEdicaoSlip.get(i);
			
			//Deve imprimir linha apenas caso haja ENCALHE
			if(itemLista.getEncalhe() == null || itemLista.getEncalhe().intValue() < 1)
				continue;
			
			if (!itemLista.getDia().equals(ultimoDia)){
			
				this.inserirCabecalho(e, itemLista.getOrdinalDiaConferenciaEncalhe());
				ultimoDia = itemLista.getDia();
			}
			
			e.adicionar(itemLista.getNomeProduto() == null ? "": itemLista.getNomeProduto(), 9);e.darEspaco(1);
			e.adicionar(itemLista.getNumeroEdicao() == null ? "" : itemLista.getNumeroEdicao().toString(), 6);e.darEspaco(1);
			e.adicionar(itemLista.getReparte() == null ? "" : itemLista.getReparte().toString(), 3);e.darEspaco(1);
			e.adicionar(itemLista.getEncalhe() == null ? "" : itemLista.getEncalhe().toString(), 3);e.darEspaco(1);
			e.adicionar(itemLista.getPrecoVenda() == null ? "0,00" : itemLista.getPrecoVenda().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString(), 7);e.darEspaco(1);
			e.adicionar(itemLista.getValorTotal() == null ? "0,00" : itemLista.getValorTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString(), 8);
			e.quebrarLinhaEscape();
			
			exibirSubtotal = this.exibirSubtotal(listaProdutoEdicaoSlip, i, itemLista);
			
			if(exibirSubtotal){
				
				e.quebrarLinhaEscape();
				
				String dataRecolhimentoStr = "";
				if(itemLista.getDataRecolhimento()!=null ){
					dataRecolhimentoStr = new SimpleDateFormat("dd/MM/yy").format(itemLista.getDataRecolhimento());
				}

				/*
				 * 
				 */
				
				String qtdeTotalProdutos =  itemLista.getQtdeTotalProdutos() == null ? "0" : itemLista.getQtdeTotalProdutos();
				e.adicionarCompleteEspaco("Total Exemps. do dia "+ dataRecolhimentoStr+":", qtdeTotalProdutos);
				
				e.quebrarLinhaEscape();

				String valorTotalEncalhe = itemLista.getValorTotalEncalhe() == null ? "0" : itemLista.getValorTotalEncalhe();
				e.adicionarCompleteEspaco("Total Encalhe do dia "+ dataRecolhimentoStr +":", valorTotalEncalhe);
				e.quebrarLinhaEscape();
				/*
				 * 
				 */
				
				e.adicionar("----------------------------------------");
				e.quebrarLinhaEscape();
			}
		}

		e.adicionarCompleteEspaco("Reparte Capa", 
			slipDTO.getValorTotalSemDesconto().setScale(2, RoundingMode.HALF_EVEN).toString());
		e.quebrarLinhaEscape();
		
		e.adicionarCompleteEspaco("Desconto Reparte", 
			slipDTO.getValorTotalDesconto().setScale(2, RoundingMode.HALF_EVEN).toString());
		e.quebrarLinhaEscape();
		
		e.adicionarCompleteEspaco("Valor Liquido Devido (B) ", slipDTO.getValorTotalSemDesconto().subtract(slipDTO.getValorTotalDesconto())
				.setScale(2, RoundingMode.HALF_EVEN).toString());
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
		
		e.adicionar("SUB-TOTAL-------------------------------");
		e.quebrarLinhaEscape();
		
		String totalProdutos = slipDTO.getTotalProdutos() == null ? "0" : slipDTO.getTotalProdutos().toString();
		e.adicionarCompleteEspaco("Total de Exemps. :", totalProdutos);
		e.quebrarLinhaEscape();
		
		String valorTotalEncalhe = slipDTO.getValorTotalEncalhe() == null ? "0,00" : slipDTO.getValorTotalEncalhe().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
		e.adicionarCompleteEspaco("Valor total de Encalhe: ( A )", valorTotalEncalhe);
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
	
		this.adicionarComposicaoCobranca(e, slipDTO);
		
		String valorTotalPagar = slipDTO.getValorTotalPagar() == null ? "0,00" : slipDTO.getValorTotalPagar().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
		
		
		//e.adicionarCompleteEspaco("Outros valores", slipDTO.getOutrosValores().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
		e.adicionarCompleteEspaco("VALOR TOTAL A PAGAR", valorTotalPagar);
	
        e.quebrarLinhaEscape(9);// Espaços fim da impressao
		
		String saida = sb.toString();
		
		return saida.getBytes();
	}

    /**
     * SLIP
     * 
     * Insere cabeçalho do Slip
     * 
     * @param e
     * @param ordinalDiaConferenciaEncalhe
     */
	private void inserirCabecalho(ImpressaoMatricialUtil e, String ordinalDiaConferenciaEncalhe) {
		
		e.darEspaco((38 - ordinalDiaConferenciaEncalhe.length()) / 2);
		e.adicionar(ordinalDiaConferenciaEncalhe);
		e.quebrarLinhaEscape();
		
		e.adicionar("DESCRICAO", 9);e.darEspaco(1);
		e.adicionar("EDICAO", 6);e.darEspaco(1);
		e.adicionar("REP", 3);e.darEspaco(1);
		e.adicionar("ENC", 3);e.darEspaco(1);
		e.adicionar("PRECO", 7);e.darEspaco(1);
		e.adicionar("TOTAL", 8);
		e.quebrarLinhaEscape();
	}
	
	    /**
     * SLIP
     * 
     * Adiciona informações da composição da cobrança no Slip
     * 
     * @param e
     * @param slipDTO
     */
	private void adicionarComposicaoCobranca(ImpressaoMatricialUtil e, SlipDTO slipDTO) {

		e.adicionar("COMPOSICAO COBRANCA---------------------");

		e.quebrarLinhaEscape();
		
		String valorSlip = slipDTO.getValorSlip() == null ? "0,00" : slipDTO.getValorSlip().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
		
		e.adicionarCompleteEspaco("Valor SLIP do dia: ( B - A ) ", valorSlip);

		e.quebrarLinhaEscape();
		
		if(slipDTO.getListaComposicaoCobrancaDTO() != null){
			
			for(DebitoCreditoCotaDTO composicao : slipDTO.getListaComposicaoCobrancaDTO()) 
			{
				String observacoes = StringUtil.limparString(composicao.getObservacoes());
				observacoes = observacoes == null ? "" : observacoes;
				
				String descricao = StringUtil.limparString(composicao.getDescricao());
				
				observacoes = (descricao == null) ? observacoes + ":" : observacoes;
				
				descricao = (descricao == null) ? "" : " - " + descricao + ":";
				observacoes = observacoes + descricao;
				
				String valor = (composicao.getValor() == null) ? "0,00" : composicao.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
				
				String operacaoFinanceira = (composicao.getTipoLancamento() == null) ? "" : composicao.getTipoLancamento().getSiglaOperacao();
				e.adicionarCompleteEspaco(observacoes + " " + operacaoFinanceira, valor);
				e.quebrarLinhaEscape();
			}
		}
		
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
	}
	
	/**
	 * SLIP
	 * 
	 * Gera Slip em formato PDF
	 * @param slipDTO
	 * @return byte[]
	 */
    private byte[] gerarSlipPDF(SlipDTO slipDTO) {
		
		URL subReportDir = Thread.currentThread().getContextClassLoader().getResource("/reports/");
		
		try{
			
		    slipDTO.getParametersSlip().put("SUBREPORT_DIR", subReportDir.toURI().getPath());
		}
		catch(Exception e){
			
			LOGGER.error(e.getMessage(), e);
		}

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(slipDTO.getListaProdutoEdicaoSlipDTO());

		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/slip_pdf.jasper");

		String path = null;

		try {
			
			path = url.toURI().getPath();
			
			return  JasperRunManager.runReportToPdf(path, slipDTO.getParametersSlip(), jrDataSource);
			
		} catch (JRException e) {
			
            throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
			
		}catch (URISyntaxException e) {
			
            throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
			
		}
	}
    
    /**
     * SLIP
     * 
     * Gera Slip da Cobrança para Impressão em Impressora Matricial
     * 
     * @param idControleConferenciaEncalheCota
     * @param incluirNumeroSlip
     * @return byte[]
     */
    @Override
    @Transactional
	public byte[] gerarSlipCobrancaMatricial(Long idControleConferenciaEncalheCota, boolean incluirNumeroSlip) {
		
		SlipDTO slipDTO = setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
		
		return gerarSlipTxtMatricial(slipDTO);
	}
    
	    /**
     * SLIP
     * 
     * Gera Slip da Cobrança
     * 
     * @param idControleConferenciaEncalheCota
     * @param incluirNumeroSlip
     * @param tpArquivo
     * @return byte[]
     */
	@Override
	@Transactional
	public byte[] gerarSlipCobranca(Long idControleConferenciaEncalheCota, 
			                        boolean incluirNumeroSlip, 
			                        TipoArquivo tpArquivo) {
		
        SlipDTO slipDTO = setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
		
		if(slipDTO.getListaComposicaoCobrancaDTO().isEmpty()){
			
			slipDTO.getListaComposicaoCobrancaDTO().add(new DebitoCreditoCotaDTO());
		}
		
		switch (tpArquivo) {
		
			case PDF:
				
				return gerarSlipPDF(slipDTO);
			default:
				
				return null;
		}
	}
	
	    /**
     * SLIP
     * 
     * Gera Slip da Cobrança
     * 
     * @param nossoNumero
     * @param incluirNumeroSlip
     * @param tpArquivo
     * @return byte[]
     */
	@Override
	@Transactional
	public byte[] gerarSlipCobranca(String nossoNumero, 
			                        boolean incluirNumeroSlip, 
                                    TipoArquivo tpArquivo) {
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		if (cobranca == null){
			
			return null;
		}
		
		ControleConferenciaEncalheCota controleConferenciaEncalheEncalheCota = this.controleConferenciaEncalheCotaRepository
				.obterControleConferenciaEncalheCotaPorIdCobranca(cobranca.getId());
		
		if (controleConferenciaEncalheEncalheCota==null){
			
			return null;
		}
		
		Long idControleConferenciaEncalheCota = controleConferenciaEncalheEncalheCota.getId();
		
        SlipDTO slipDTO = setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
		
		if(slipDTO.getListaComposicaoCobrancaDTO().isEmpty()){
			
			slipDTO.getListaComposicaoCobrancaDTO().add(new DebitoCreditoCotaDTO());
		}
		
		switch (tpArquivo) {
		
			case PDF:
				
				return gerarSlipPDF(slipDTO);
			default:
				
				return null;
		}
	}
	
	
	
	/**
	 * RECIBO
	 * 
	 * @param cobranca
	 * @param qtdeTotal
	 * @param valorTotal
	 * @return Map<String, Object>
	 */
    private Map<String, Object> obterParametrosRelatorioRecibo(Cobranca cobranca, 
    		                                                   String qtdeTotal, 
    		                                                   String valorTotal){
    	
    	Usuario usuario = this.usuarioService.getUsuarioLogado();

	    Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("NUMERO_COTA", cobranca.getCota().getNumeroCota().toString());
		
		parameters.put("NOME_COTA", cobranca.getCota().getPessoa().getNome());
		
		parameters.put("CODIGO_BOX", cobranca.getCota().getBox().getCodigo().toString());
		
		parameters.put("DESC_BOX", cobranca.getCota().getBox().getNome());
		
		parameters.put("DATA_VENDA", DateUtil.formatarData(cobranca.getDataEmissao(), "dd/MM/yyyy"));
		
		parameters.put("HORA_VENDA", DateUtil.formatarData(cobranca.getDataEmissao(), "hh:mm:ss"));
		
		parameters.put("USUARIO", usuario.getNome());
		
		parameters.put("QNT_TOTAL_A_VISTA", qtdeTotal.toString());
		
		parameters.put("VALOR_TOTAL_A_VISTA", CurrencyUtil.formatarValor(cobranca.getValor()));
		
		parameters.put("QNT_TOTAL_A_PRAZO", CurrencyUtil.formatarValor(BigDecimal.ZERO));
		
		parameters.put("VALOR_TOTAL_A_PRAZO", CurrencyUtil.formatarValor(BigDecimal.ZERO));
		
		parameters.put("QNT_TOTAL_GERAL", qtdeTotal.toString());
		
		parameters.put("VALOR_TOTAL_GERAL", CurrencyUtil.formatarValor(cobranca.getValor()));
		
		String nomeDistribuidor = this.distribuidorService.obterRazaoSocialDistribuidor();
	
		parameters.put("NOME_DISTRIBUIDOR",nomeDistribuidor);
		
		parameters.put("LOGO_DISTRIBUIDOR", getLogoDistribuidor());
			
		parameters.put("TITULO_RELATORIO","Comprovante de Pagamento");
		
		parameters.put("TITULO_RELATORIO_COMPROVANTE","Comprovante de Pagamento");

		parameters.put("NUM_SLIP", cobranca.getId());
		
		return parameters;
	}
    
    /**
     * RECIBO
     * 
     * Obtem lista de itens do recibo
     * @param cobranca
     * @return List<ItemSlipVendaEncalheDTO>
     */
    private List<ItemSlipVendaEncalheDTO> obterListaItensRecibo(Cobranca cobranca){
    	
    	ConsolidadoFinanceiroCota c = null;
		
		for (ConsolidadoFinanceiroCota cc : cobranca.getDivida().getConsolidados()){
			
			if (cc.getCota().equals(cobranca.getCota())){
				
				c = cc;
				break;
			}
		}
    	
    	List<MovimentoFinanceiroCota> mfcs = c.getMovimentos();
    	
    	List<MovimentoEstoqueCota> mecs = new ArrayList<MovimentoEstoqueCota>();
    	
    	if (mfcs!=null){
    		
    		for (MovimentoFinanceiroCota mfc : mfcs){
    			
    		    mecs.addAll(mfc.getMovimentos());	
    		}
    	}
    	
    	List<ItemSlipVendaEncalheDTO> listaItensRecibo = new ArrayList<ItemSlipVendaEncalheDTO>();
    	
    	for (MovimentoEstoqueCota mec : mecs){
    	
	    	ItemSlipVendaEncalheDTO itemRecibo = new ItemSlipVendaEncalheDTO();
	    	
	    	itemRecibo.setCodigo(mec.getProdutoEdicao().getProduto().getCodigo());
	    	
	    	itemRecibo.setEdicao(mec.getProdutoEdicao().getNumeroEdicao().toString());
	    	
	    	itemRecibo.setPreco(CurrencyUtil.formatarValor(mec.getValoresAplicados().getPrecoComDesconto()));
	    	
	    	itemRecibo.setProduto(mec.getProdutoEdicao().getProduto().getNome());
	    	
	    	itemRecibo.setQuantidade(mec.getQtde().toString());
	    	
	    	itemRecibo.setTotal(CurrencyUtil.formatarValor(mec.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(mec.getQtde()))));
	    	
	    	listaItensRecibo.add(itemRecibo);
    	}
    	
    	return listaItensRecibo;
    }
    
    /**
     * RECIBO
     * 
     * Obtem valor total do Recibo formatado
     * @param itensRecibo
     * @return String
     */
    private String calculaValorTotalRecibo(List<ItemSlipVendaEncalheDTO> itensRecibo){
    	
    	BigDecimal valorTotal = BigDecimal.ZERO;
    	
    	for (ItemSlipVendaEncalheDTO item : itensRecibo){
    	
    		valorTotal = valorTotal.add(CurrencyUtil.converterValor(item.getTotal()));
    	}
    	
    	return CurrencyUtil.formatarValor(valorTotal);
    }
    
    /**
     * RECIBO
     * 
     * Obtem quantidade total do Recibo formatado
     * @param itensRecibo
     * @return String
     */
    private String calculaQuantidadeTotalRecibo(List<ItemSlipVendaEncalheDTO> itensRecibo){
    	
        BigInteger qtdeTotal = BigInteger.ZERO;
    	
        for (ItemSlipVendaEncalheDTO item : itensRecibo){
        	
        	qtdeTotal = qtdeTotal.add(new BigInteger(item.getQuantidade()));
    	}
        
    	return qtdeTotal.toString();
    }
	
	    /**
     * RECIBO
     * 
     * Gera um relatório à partir de um Objeto com atributos e listas definidas
     * 
     * @param list
     * @param cobranca
     * @param pathJasper
     * @return Array de bytes do relatório gerado
     * @throws JRException
     * @throws URISyntaxException
     */
	private byte[] gerarDocumentoReciboIreport(Cobranca cobranca,
										       String pathJasper) throws JRException, URISyntaxException {
		
		List<ItemSlipVendaEncalheDTO> itensRecibo = this.obterListaItensRecibo(cobranca);
		
		String qtdeTotal = this.calculaQuantidadeTotalRecibo(itensRecibo);
		
		String valorTotal = this.calculaValorTotalRecibo(itensRecibo);
		
		Map<String, Object> parameters = obterParametrosRelatorioRecibo(cobranca,
				                                                        qtdeTotal,
				                                                        valorTotal);
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(itensRecibo);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(pathJasper);

		String path = url.toURI().getPath();

		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	    /**
     * RECIBO
     * 
     * Gera Recibo da Cobrança
     * 
     * @param nossoNumero
     * @return byte[]
     */
	@Override
	@Transactional
	public byte[] gerarReciboCobranca(String nossoNumero) {
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		byte[] retorno;
		
		try {

			retorno = this.gerarDocumentoReciboIreport(cobranca,"/reports/slipComprovanteVendaEncalheSuplementar.jasper");
			
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Erro ao gerar Recibo.");
		}

		return retorno;
	}
}
