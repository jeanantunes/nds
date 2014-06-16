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
import java.util.Arrays;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CobrancaImpressaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.ItemSlipVendaEncalheDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
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
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ControleNumeracaoSlipService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.RoteiroService;
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
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.StringUtil;

@Service
public class DocumentoCobrancaServiceImpl implements DocumentoCobrancaService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentoCobrancaServiceImpl.class);
    
    
    @Autowired
    private CobrancaRepository cobrancaRepository;
    
    @Autowired
    private RotaRepository rotaRepository;
    
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
    
    @Autowired
    private RotaService rotaService;
    
    @Autowired
    private RoteiroService roteiroService;
    
    @Autowired
    private RoteirizacaoRepository roteirizacaoRepository;
    
    @Autowired
    private BoletoRepository boletoRepository;
    
    /**
     * BOLETO/COBRANCA
     * 
     * Envia Cobranca por Email
     * @param nossoNumero
     */
    @Transactional
    @Override
    public void enviarDocumentoCobrancaPorEmail(final String nossoNumero){
        
        final Cobranca cobranca = cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
        
        try {
            switch (cobranca.getTipoCobranca()) {
            case BOLETO:
                
                boletoService.enviarBoletoEmail(nossoNumero);
                break;
                
            default:
                enviarDocumentoPorEmail(cobranca);
                break;
                
            }
            
        } catch (final Exception e) {
            final String msg = "Erro ao enviar e-mail de arquivo de cobrança para nosso número: " + nossoNumero + " - "
                    + e.getMessage();
            LOGGER.error(msg, e);
            throw new ValidacaoException(TipoMensagem.ERROR,
                    msg );
        }
        
        cobrancaRepository.incrementarVia(nossoNumero);
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
    public byte[] gerarDocumentoCobranca(final List<GeraDividaDTO> dividas, final TipoCobranca tipoCobranca) {
        
        final List<String> listNossoNumero = getNossoNumeros(dividas);
        
        byte[] arquivo = null;
        
        try {
            
            if(TipoCobranca.BOLETO.equals(tipoCobranca)){
                
                arquivo =boletoService.gerarImpressaoBoletos(listNossoNumero);
            }
            else{
                
                arquivo = gerarDocumentoCobrancas(listNossoNumero);
            }
            
            if(arquivo!= null){
                cobrancaRepository.incrementarVia( listNossoNumero.toArray(new String[] {}) );
            }
        } catch (final Exception e) {
            throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
        }
        
        return arquivo;
    }
    
    @Transactional
    @Override
    public byte[] gerarDocumentoCobrancaComSlip(final List<GeraDividaDTO> dividas, final TipoCobranca tipoCobranca) {
        
        final List<String> listNossoNumero = getNossoNumeros(dividas);
        final List<byte[]> arquivos = new ArrayList<byte[]>();
        
        try {
            for (String nossoNumero : listNossoNumero){
                
                final Boleto boleto = this.boletoRepository.obterPorNossoNumero(nossoNumero,null,false);
                
                arquivos.add(this.boletoService.gerarImpressaoBoleto(boleto));
                arquivos.add(this.gerarSlipCobranca(boleto, true, TipoArquivo.PDF));
            }
        } catch (Exception e) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage() + " ao gerar arquivo de cobrança + Slip");
        }
        
        return PDFUtil.mergePDFs(arquivos);
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
    private byte[] gerarDocumentoCobrancas(final List<String> listNossoNumero) throws Exception {
        
        final List<CobrancaImpressaoDTO> cobrancas = new ArrayList<CobrancaImpressaoDTO>();
        
        Cobranca cobranca = null;
        
        final String razaoSocial = distribuidorService.obterRazaoSocialDistribuidor();
        
        for(final String nossoNumero : listNossoNumero) {
            
            cobranca = cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
            
            cobrancas.add(obterCobrancaImpressaoDTO(cobranca, razaoSocial));
        }
        
        final byte[] arquivo = gerarDocumentoIreport(cobrancas);
        
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
    private List<String> getNossoNumeros(final List<GeraDividaDTO> dividas){
        
        final List<String> list = new ArrayList<String>();
        
        for(final GeraDividaDTO dto : dividas){
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
    private void enviarDocumentoPorEmail(final Cobranca cobranca) throws AutenticacaoEmailException, Exception {
        
        final String assunto = distribuidorService.assuntoEmailCobranca();
        
        final String mensagem = distribuidorService.mensagemEmailCobranca();
        
        final String emailCota = cobranca.getCota().getPessoa().getEmail();
        final String[] destinatarios = new String[]{emailCota};
        
        final byte[] arquivo = getDocumentoCobranca(cobranca);
        
        final AnexoEmail anexoEmail = new AnexoEmail();
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
    private byte[] getDocumentoCobranca(final Cobranca... cobrancas) throws Exception{
        
        final String razaoSocial = distribuidorService.obterRazaoSocialDistribuidor();
        
        final List<CobrancaImpressaoDTO> list = new ArrayList<CobrancaImpressaoDTO>();
        
        for(final Cobranca cobranca : cobrancas){
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
    private byte[] gerarDocumentoIreport(final List<CobrancaImpressaoDTO> list) throws Exception{
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        
        final Map<String, Object> map = getInformacoesDistribuido();
        final URL url = Thread.currentThread().getContextClassLoader()
                .getResource("/reports/cobranca.jasper");
        //Executa o decode do path do arquivo
        final String path = url.toURI().getPath();
        
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
        
        final Distribuidor distribuidor = distribuidorService.obter();
        
        final Map<String, Object> map = new HashMap<String, Object>();
        
        final EnderecoDistribuidor enderecoDistribuidor = distribuidor.getEnderecoDistribuidor();
        
        map.put("cidade",(enderecoDistribuidor.getEndereco()==null)?"": enderecoDistribuidor.getEndereco().getCidade());
        map.put("enderecoDistribuidor", this.obterDescricaoEnderecoDistribuidor(enderecoDistribuidor) );
        
        for(final TelefoneDistribuidor telefone : distribuidor.getTelefones()){
            
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
    private String obterDescricaoEnderecoDistribuidor(final EnderecoDistribuidor enderecoDistribuidor){
        
        final Endereco endereco  = enderecoDistribuidor.getEndereco();
        
        if(endereco == null){
            return null;
        }
        
        final StringBuilder descricao = new StringBuilder();
        
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
    private CobrancaImpressaoDTO obterCobrancaImpressaoDTO(final Cobranca cobranca, final String razaoSocialDistribuidor){
        
        final CobrancaImpressaoDTO impressaoDTO = new CobrancaImpressaoDTO();
        
        final Banco banco  = cobranca.getBanco();
        
        this.atribuirDadosBanco(impressaoDTO, banco);
        
        final Cota cota  = cobranca.getCota();
        
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
    private void atribuirDadosCota(final String razaoSocialDistribuidor,final CobrancaImpressaoDTO impressaoDTO, final Cota cota) {
        
        if(cota!= null){
            
            final Pessoa pessoa  = cota.getPessoa();
            
            if (pessoa instanceof PessoaFisica){
                impressaoDTO.setNomeCota(((PessoaFisica) pessoa).getNome());
            }
            else if(pessoa instanceof PessoaJuridica){
                impressaoDTO.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial());
            }
            
            impressaoDTO.setNomeFavorecido(razaoSocialDistribuidor);
            impressaoDTO.setNumeroCota(cota.getNumeroCota());
            
            impressaoDTO.setBox((cota.getBox()!= null)?cota.getBox().getCodigo() + " - " + cota.getBox().getNome():"");
            
            final Roteirizacao roteirizacao = roteirizacaoService.buscarRoteirizacaoDeCota(cota.getNumeroCota());
            
            if(roteirizacao!= null){
                
                if(roteirizacao.getBox()!=null){
                    
                    final Roteiro roteiro = roteirizacaoService.obterRoteiroDeBoxPorOrdem(roteirizacao.getBox().getId());
                    
                    if (roteiro!=null){
                        
                        impressaoDTO.setRoteiro(roteiro.getDescricaoRoteiro());
                        
                        final Rota rota = roteirizacaoService.obterRotaDeRoteiroPorOrdem(roteiro.getId());
                        
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
    private void atribuirDadosBanco(final CobrancaImpressaoDTO impressaoDTO, final Banco banco) {
        
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
    public byte[] gerarDocumentoCobranca(final String nossoNumero) {
        
        return this.gerarDocumentoCobranca(nossoNumero, true);
    }
    
    @Override
    @Transactional
    public byte[] gerarDocumentoCobranca(String nossoNumero, boolean incrementarVias){
        
        final Cobranca cobranca = cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
        
        byte[] retorno = null;
        
        try {
            
            switch (cobranca.getTipoCobranca()) {
            
            case BOLETO:
                
                retorno = boletoService.gerarImpressaoBoleto(nossoNumero);
                
                break;
                
            case BOLETO_EM_BRANCO:
                
                retorno = boletoService.gerarImpressaoBoleto(nossoNumero);
                
                if (retorno == null){
                    
                    retorno = this.boletoService.gerarImpressaoBoletoEmBranco(nossoNumero);
                }
                
                break;
                
            default:
                
                retorno = getDocumentoCobranca(cobranca);
                break;
            }
            
        }catch (final Exception e) {
            final String msg = "Erro ao gerar arquivo de cobrança para nosso número: " + nossoNumero + " - "
                    + e.getMessage();
            LOGGER.error(msg, e);
            throw new ValidacaoException(TipoMensagem.ERROR, msg);
        }
        
        if (incrementarVias){
            
            final Integer vias = (cobranca.getVias() == null) ? 1 : (cobranca.getVias()+1);
            
            cobranca.setVias(vias);
            
            cobrancaRepository.merge(cobranca);
        }
        
        return retorno;
    }
    
    /**
     * SLIP
     * 
     * Obtem Ordinal
     * @param x
     * @return String
     */
    private String getDiaMesOrdinal(final Integer x){
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
    private void ordenarListaPorDia(final List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip) {
        
        final ComparatorChain comparatorChain = new ComparatorChain();
        
        comparatorChain.addComparator(new BeanComparator("dia", new NullComparator()));
        
        Collections.sort(listaProdutoEdicaoSlip, comparatorChain);
    }
    
    private boolean exibirSubtotal(final List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip, final int i,
            final ProdutoEdicaoSlipDTO itemLista) {
        
        final boolean exibirSubtotal =
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
    private void calcularTotaisListaSlip(final List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip) {
        
        boolean exiberSubtotal;
        BigInteger qtdeTotalProdutosDia = null;
        BigDecimal valorTotalEncalheDia = null;
        
        for(int i = 0; i < listaProdutoEdicaoSlip.size(); i++) {
            
            final ProdutoEdicaoSlipDTO produtoSlip = listaProdutoEdicaoSlip.get(i);
            
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
        
        final InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
        
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
    private Date obterDataOperacaoConferencia(final Date dataFimConferencia, final Date dataOperacaoConferenia){
        
        final Calendar dataFinalConferencia =  Calendar.getInstance();
        dataFinalConferencia.setTime(dataFimConferencia);
        
        final Calendar  dataOperacaoConferencia  = Calendar.getInstance();
        dataOperacaoConferencia.setTime(dataOperacaoConferenia);
        dataOperacaoConferencia.add(Calendar.HOUR, dataFinalConferencia.get(Calendar.HOUR_OF_DAY));
        dataOperacaoConferencia.add(Calendar.MINUTE, dataFinalConferencia.get(Calendar.MINUTE));
        dataOperacaoConferencia.add(Calendar.SECOND, dataFinalConferencia.get(Calendar.SECOND));
        
        return dataOperacaoConferencia.getTime();
    }
    
    /**
     * Obtem Informações de Roteirizacao da cota diferende de Box Especial
     * 
     * @param numeroCota
     * @return ConsultaRoteirizacaoDTO
     */
    private ConsultaRoteirizacaoDTO getConsultaRoteirizacaoDTO(Integer numeroCota){
    	
        FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
        
        filtro.setNumeroCota(numeroCota);
        
        List<ConsultaRoteirizacaoDTO> listaRoteirizacaoDTO = this.roteirizacaoRepository.buscarRoteirizacao(filtro); 
        
        ConsultaRoteirizacaoDTO roteirizacaoDTO = null;
        
        for (ConsultaRoteirizacaoDTO item : listaRoteirizacaoDTO){
        	
        	if (!item.getNomeBox().equals("Especial")){
        		
        		roteirizacaoDTO = item;
        		
        		return roteirizacaoDTO;
        	}
        }
        
        return roteirizacaoDTO;
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
    private SlipDTO setParamsSlip(final Long idControleConferenciaEncalheCota, final boolean incluirNumeroSlip) {
        
        final ControleConferenciaEncalheCota controleConferenciaEncalheCota = controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);
        
        if(incluirNumeroSlip || controleConferenciaEncalheCota.getNumeroSlip() == null || controleConferenciaEncalheCota.getNumeroSlip() < 1) {
            controleConferenciaEncalheCota.setNumeroSlip(controleNumeracaoSlipService.obterProximoNumeroSlip(TipoSlip.SLIP_CONFERENCIA_ENCALHE));
            controleConferenciaEncalheCotaRepository.alterar(controleConferenciaEncalheCota);
        }
        
        final Date dataOperacao = controleConferenciaEncalheCota.getDataOperacao();
        
        final List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip = conferenciaEncalheRepository.obterDadosSlipConferenciaEncalhe(idControleConferenciaEncalheCota);
        
        final Integer numeroCota 		= controleConferenciaEncalheCota.getCota().getNumeroCota();
        
        final String nomeCota 		= controleConferenciaEncalheCota.getCota().getPessoa().getNome();
        
        final Date dataConferencia 	= this.obterDataOperacaoConferencia(controleConferenciaEncalheCota.getDataFim(), controleConferenciaEncalheCota.getDataOperacao());

        ConsultaRoteirizacaoDTO rDto = this.getConsultaRoteirizacaoDTO(numeroCota);
        
        final String descricaoRota = rDto!=null?rDto.getDescricaoRota():"";
        
        final String descricaoRoteiro = rDto!=null?rDto.getDescricaoRoteiro():"";

        final String descricaoBox = rDto!=null?rDto.getNomeBox():"";

        final Long numeroSlip 		= controleConferenciaEncalheCota.getNumeroSlip();

        BigInteger qtdeTotalProdutos 	   = null;
        BigDecimal valorTotalEncalhe 	   = null;
        final BigDecimal valorTotalPagar   = BigDecimal.ZERO;
        
        BigDecimal valorTotalReparte = conferenciaEncalheService.obterValorTotalReparte(numeroCota, Arrays.asList(dataOperacao));
        final BigDecimal valorTotalDesconto = conferenciaEncalheService.obterValorTotalDesconto(numeroCota, dataOperacao);
        final BigDecimal valorTotalSemDesconto = conferenciaEncalheService.obterValorTotalReparteSemDesconto(numeroCota, dataOperacao);
        
        Integer dia=0;
        
        for(final ProdutoEdicaoSlipDTO produtoEdicaoSlip : listaProdutoEdicaoSlip) {
            
            qtdeTotalProdutos = BigIntegerUtil.soma(qtdeTotalProdutos, produtoEdicaoSlip.getEncalhe());
            
            valorTotalEncalhe = BigDecimalUtil.soma(valorTotalEncalhe, produtoEdicaoSlip.getValorTotal());
            
            if(produtoEdicaoSlip.getReparte() == null) {
                
                produtoEdicaoSlip.setReparte(BigInteger.ZERO);
                
            }
            
            dia = produtoEdicaoSlip.getDia();
            
            final String ordinal = ((dia != null) ? this.getDiaMesOrdinal(dia) + " DIA" : "PRODUTOS ANTECIPADOS");
            
            produtoEdicaoSlip.setOrdinalDiaConferenciaEncalhe(ordinal);
        }
        
        this.ordenarListaPorDia(listaProdutoEdicaoSlip);
        
        this.calcularTotaisListaSlip(listaProdutoEdicaoSlip);
        
        valorTotalReparte = (valorTotalReparte == null) ? BigDecimal.ZERO : valorTotalReparte;
        valorTotalEncalhe = (valorTotalEncalhe == null) ? BigDecimal.ZERO : valorTotalEncalhe;
        qtdeTotalProdutos = (qtdeTotalProdutos == null) ? BigInteger.ZERO : qtdeTotalProdutos;
        
        final BigDecimal valorVenda = (valorTotalReparte.subtract(valorTotalEncalhe));
        
        final SlipDTO slipDTO = new SlipDTO();
        
        slipDTO.setNumeroCota(numeroCota);
        slipDTO.setNomeCota(nomeCota);
        slipDTO.setDataConferencia(dataConferencia);
        slipDTO.setCodigoBox(descricaoBox);
        slipDTO.setDescricaoRoteiro(descricaoRoteiro);
        slipDTO.setDescricaoRota(descricaoRota);
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
        
        final BigDecimal pagamentoPendente = slipDTO.getValorTotalPagar().compareTo(valorVenda)>0?slipDTO.getValorTotalPagar().subtract(valorVenda):BigDecimal.ZERO;
        
        final BigDecimal valorCreditoDif = valorVenda.compareTo(slipDTO.getValorTotalPagar())>0?valorVenda.subtract(slipDTO.getValorTotalPagar()):BigDecimal.ZERO;
        
        final Map<String, Object> parametersSlip = new HashMap<String, Object>();
        slipDTO.setParametersSlip(parametersSlip);
        
        parametersSlip.put("NUMERO_COTA", slipDTO.getNumeroCota());
        parametersSlip.put("NOME_COTA", slipDTO.getNomeCota());
        parametersSlip.put("NUM_SLIP", numeroSlip.toString());
        parametersSlip.put("CODIGO_BOX", slipDTO.getCodigoBox());
        parametersSlip.put("CODIGO_ROTEIRO", slipDTO.getDescricaoRoteiro());
        parametersSlip.put("CODIGO_ROTA", slipDTO.getDescricaoRota());
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
            
        } catch(final Exception e) {
            
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Erro ao carregar logotipo do distribuidor no documento de cobrança");
            
        }
        
        final List<DebitoCreditoCotaDTO> listaComposicaoCobranca =
                debitoCreditoCotaService.obterListaDebitoCreditoCotaDTO(controleConferenciaEncalheCota.getCota(), Arrays.asList(dataOperacao), null);

        slipDTO.setListaComposicaoCobrancaDTO(listaComposicaoCobranca);
        parametersSlip.put("LISTA_COMPOSICAO_COBRANCA",listaComposicaoCobranca);
        
        final List<DebitoCreditoCotaDTO> listaResumoCobranca = 
        		debitoCreditoCotaService.obterListaResumoCobranca(controleConferenciaEncalheCota.getCota(), dataOperacao);
        slipDTO.setListaResumoCobrancaDTO(listaResumoCobranca);
        parametersSlip.put("LISTA_RESUMO_COBRANCA",listaResumoCobranca);
      
        
        BigDecimal totalComposicao = BigDecimal.ZERO;
        
        for(final DebitoCreditoCotaDTO item : listaComposicaoCobranca){
            
            // TOTALIZAÇÃO DO SLIP CONSIDERANDO COMPOSIÇÃO DE COBRANÇA
            // débito para o distribuidor, não para a cota
            if (OperacaoFinaceira.DEBITO.equals(item.getTipoLancamento())) {
                
                totalComposicao = totalComposicao.add(item.getValor());
                
            } else {
                
                totalComposicao = totalComposicao.subtract(item.getValor());
            }
        }
        
        totalComposicao = slipDTO.getValorSlip().add(totalComposicao).abs();
        
        final BigDecimal totalPagar = totalComposicao;
        
        slipDTO.setValorTotalPagar(totalPagar);
        
        slipDTO.setValorLiquidoDevido(slipDTO.getValorTotalSemDesconto().subtract(slipDTO.getValorTotalDesconto()));
        
        parametersSlip.put("VALOR_LIQUIDO_DEVIDO", slipDTO.getValorLiquidoDevido());
        
        parametersSlip.put("VALOR_DEVIDO", valorTotalReparte);
        
        parametersSlip.put("VALOR_SLIP", slipDTO.getValorSlip());
        
        parametersSlip.put("VALOR_TOTAL_SEM_DESCONTO", slipDTO.getValorTotalSemDesconto());
        
        parametersSlip.put("VALOR_TOTAL_DESCONTO", slipDTO.getValorTotalDesconto());
        
        parametersSlip.put("VALOR_TOTAL_PAGAR", totalPagar);
        
        parametersSlip.put("RAZAO_SOCIAL_DISTRIBUIDOR", distribuidorService.obterRazaoSocialDistribuidor());
        
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
    public byte[] gerarSlipTxtMatricial(final SlipDTO slipDTO){
        
        final StringBuilder sb = new StringBuilder();
        final ImpressaoMatricialUtil e = new ImpressaoMatricialUtil(sb);
        Integer ultimoDia = null;
        
        e.darEspaco(1);
        e.adicionar(distribuidorService.obterRazaoSocialDistribuidor());
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
        
        final List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip = slipDTO.getListaProdutoEdicaoSlipDTO();
        
        for(int i = 0; i < listaProdutoEdicaoSlip.size(); i++) {
            
            
            final ProdutoEdicaoSlipDTO itemLista = listaProdutoEdicaoSlip.get(i);
            
            //Deve imprimir linha apenas caso haja ENCALHE
            if(itemLista.getEncalhe() == null || itemLista.getEncalhe().intValue() < 1) {
                continue;
            }
            
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
                
                final String qtdeTotalProdutos =  itemLista.getQtdeTotalProdutos() == null ? "0" : itemLista.getQtdeTotalProdutos();
                e.adicionarCompleteEspaco("Total Exemps. do dia "+ dataRecolhimentoStr+":", qtdeTotalProdutos);
                
                e.quebrarLinhaEscape();
                
                final String valorTotalEncalhe = itemLista.getValorTotalEncalhe() == null ? "0" : itemLista.getValorTotalEncalhe();
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
        
        final String totalProdutos = slipDTO.getTotalProdutos() == null ? "0" : slipDTO.getTotalProdutos().toString();
        e.adicionarCompleteEspaco("Total de Exemps. :", totalProdutos);
        e.quebrarLinhaEscape();
        
        final String valorTotalEncalhe = slipDTO.getValorTotalEncalhe() == null ? "0,00" : slipDTO.getValorTotalEncalhe().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
        e.adicionarCompleteEspaco("Valor total de Encalhe: ( A )", valorTotalEncalhe);
        e.quebrarLinhaEscape();
        e.quebrarLinhaEscape();
        
        this.adicionarComposicaoCobranca(e, slipDTO);
        
        final String valorTotalPagar = slipDTO.getValorTotalPagar() == null ? "0,00" : slipDTO.getValorTotalPagar().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
        
        
        //e.adicionarCompleteEspaco("Outros valores", slipDTO.getOutrosValores().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        e.adicionarCompleteEspaco("VALOR TOTAL A PAGAR", valorTotalPagar);
        
        e.quebrarLinhaEscape(9);// Espaços fim da impressao
        
        final String saida = sb.toString();
        
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
    private void inserirCabecalho(final ImpressaoMatricialUtil e, final String ordinalDiaConferenciaEncalhe) {
        
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
    private void adicionarComposicaoCobranca(final ImpressaoMatricialUtil e, final SlipDTO slipDTO) {
        
        e.adicionar("COMPOSICAO COBRANCA---------------------");
        
        e.quebrarLinhaEscape();
        
        final String valorSlip = slipDTO.getValorSlip() == null ? "0,00" : slipDTO.getValorSlip().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
        
        e.adicionarCompleteEspaco("Valor SLIP do dia: ( B - A ) ", valorSlip);
        
        e.quebrarLinhaEscape();
        
        if(slipDTO.getListaComposicaoCobrancaDTO() != null){
            
            for(final DebitoCreditoCotaDTO composicao : slipDTO.getListaComposicaoCobrancaDTO())
            {
                String observacoes = StringUtil.limparString(composicao.getObservacoes());
                observacoes = observacoes == null ? "" : observacoes;
                
                String descricao = StringUtil.limparString(composicao.getDescricao());
                
                observacoes = (descricao == null) ? observacoes + ":" : observacoes;
                
                descricao = (descricao == null) ? "" : " - " + descricao + ":";
                observacoes = observacoes + descricao;
                
                final String valor = (composicao.getValor() == null) ? "0,00" : composicao.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
                
                final String operacaoFinanceira = (composicao.getTipoLancamento() == null) ? "" : composicao.getTipoLancamento().getSiglaOperacao();
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
    private byte[] gerarSlipPDF(final SlipDTO slipDTO) {
        
        final URL subReportDir = Thread.currentThread().getContextClassLoader().getResource("/reports/");
        
        try{
            
            slipDTO.getParametersSlip().put("SUBREPORT_DIR", subReportDir.toURI().getPath());
        }
        catch(final Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(slipDTO.getListaProdutoEdicaoSlipDTO());
        
        final URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/slip_pdf.jasper");
        
        String path = null;
        
        try {
            
            path = url.toURI().getPath();
            
            return  JasperRunManager.runReportToPdf(path, slipDTO.getParametersSlip(), jrDataSource);
            
        } catch (final JRException e) {
            LOGGER.error("Não foi possível gerar relatório Slip", e);
            throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
            
        }catch (final URISyntaxException e) {
            LOGGER.error("Não foi possível gerar relatório Slip", e);
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
    public byte[] gerarSlipCobrancaMatricial(final Long idControleConferenciaEncalheCota, final boolean incluirNumeroSlip) {
        
        final SlipDTO slipDTO = setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
        
        return gerarSlipTxtMatricial(slipDTO);
    }
    
    /**
     * SLIP DTO
     * 
     * Gera Slip da Cobrança
     * 
     * @param nossoNumero
     * @param incluirNumeroSlip
     * @return SlipDTO
     */
    @Override
    @Transactional
    public SlipDTO gerarSlipDTOCobranca(final Long idControleConferenciaEncalheCota,
						                final boolean incluirNumeroSlip) {
        
    	 final SlipDTO slipDTO = setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
         
         if(slipDTO.getListaComposicaoCobrancaDTO().isEmpty()){
             
             slipDTO.getListaComposicaoCobrancaDTO().add(new DebitoCreditoCotaDTO());
         }
        
        return slipDTO;
    }    
    
    /**
     * SLIP DTO
     * 
     * Gera lista de SlipDTO por lista de controle de conferencia de encalhe
     * 
     * @param idsControleConferenciaEncalheCota
     * @param incluirNumeroSlip
     * @return SlipDTO
     */
    @Override
    @Transactional
    public List<SlipDTO> gerarListaSlipDTOCobranca(final List<Long> idsControleConferenciaEncalheCota,
						                           final boolean incluirNumeroSlip) {
        
    	List<SlipDTO> listaSlipDTO = new ArrayList<SlipDTO>();
    	
    	for (Long idContrlConf : idsControleConferenciaEncalheCota){
    	     
    		final SlipDTO slipDTO = this.gerarSlipDTOCobranca(idContrlConf, incluirNumeroSlip);
    		
    		listaSlipDTO.add(slipDTO);	
    	}

        return listaSlipDTO;
    }    
    
    /**
     * SLIP
     * 
     * Gera lista de Slip da Cobranças
     * 
     * @param slipDTO
     * @param tpArquivo
     * @return List<byte[]>
     */
    @Override
    @Transactional
    public List<byte[]> gerarListaSlipCobranca(final List<SlipDTO> listaSlipDTO, final TipoArquivo tpArquivo) {
        
    	List<byte[]> arquivos = new ArrayList<byte[]>();
    	
        for (SlipDTO sDTO : listaSlipDTO){

        	switch (tpArquivo) {
            
	            case PDF:
	                
	            	byte[] arq = this.gerarSlipPDF(sDTO);
	            	
	                arquivos.add(arq);
	            default:
	                
	            	continue;
            }
        }
        
        return arquivos;
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
    public byte[] gerarSlipCobranca(final Long idControleConferenciaEncalheCota,
						            final boolean incluirNumeroSlip,
						            final TipoArquivo tpArquivo) {
        
        final SlipDTO slipDTO = this.gerarSlipDTOCobranca(idControleConferenciaEncalheCota, incluirNumeroSlip);
        
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
    public byte[] gerarSlipCobranca(final String nossoNumero,
            final boolean incluirNumeroSlip,
            final TipoArquivo tpArquivo) {
        
        return this.gerarSlipCobranca(
                this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero), 
                incluirNumeroSlip, tpArquivo);
    }
    
    private byte[] gerarSlipCobranca(final Cobranca cobranca,
            final boolean incluirNumeroSlip,
            final TipoArquivo tpArquivo) {
        
        if (cobranca == null){
            
            return null;
        }
        
        final ControleConferenciaEncalheCota controleConferenciaEncalheEncalheCota = controleConferenciaEncalheCotaRepository
                .obterControleConferenciaEncalheCotaPorIdCobranca(cobranca.getId());
        
        if (controleConferenciaEncalheEncalheCota==null){
            
            return null;
        }
        
        final Long idControleConferenciaEncalheCota = controleConferenciaEncalheEncalheCota.getId();
        
        final SlipDTO slipDTO = setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
        
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
    private Map<String, Object> obterParametrosRelatorioRecibo(final Cobranca cobranca,
            final String qtdeTotal,
            final String valorTotal){
        
        final Usuario usuario = usuarioService.getUsuarioLogado();
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
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
        
        final String nomeDistribuidor = distribuidorService.obterRazaoSocialDistribuidor();
        
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
    private List<ItemSlipVendaEncalheDTO> obterListaItensRecibo(final Cobranca cobranca){
        
        ConsolidadoFinanceiroCota c = null;
        
        for (final ConsolidadoFinanceiroCota cc : cobranca.getDivida().getConsolidados()){
            
            if (cc.getCota().equals(cobranca.getCota())){
                
                c = cc;
                break;
            }
        }
        
        final List<MovimentoEstoqueCota> mecs = new ArrayList<MovimentoEstoqueCota>();
        if (c != null) {
            final List<MovimentoFinanceiroCota> mfcs = c.getMovimentos();
            for (final MovimentoFinanceiroCota mfc : mfcs){
                
                mecs.addAll(mfc.getMovimentos());
            }
        }
        
        final List<ItemSlipVendaEncalheDTO> listaItensRecibo = new ArrayList<ItemSlipVendaEncalheDTO>();
        
        for (final MovimentoEstoqueCota mec : mecs){
            
            final ItemSlipVendaEncalheDTO itemRecibo = new ItemSlipVendaEncalheDTO();
            
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
    private String calculaValorTotalRecibo(final List<ItemSlipVendaEncalheDTO> itensRecibo){
        
        BigDecimal valorTotal = BigDecimal.ZERO;
        
        for (final ItemSlipVendaEncalheDTO item : itensRecibo){
            
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
    private String calculaQuantidadeTotalRecibo(final List<ItemSlipVendaEncalheDTO> itensRecibo){
        
        BigInteger qtdeTotal = BigInteger.ZERO;
        
        for (final ItemSlipVendaEncalheDTO item : itensRecibo){
            
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
    private byte[] gerarDocumentoReciboIreport(final Cobranca cobranca,
            final String pathJasper) throws JRException, URISyntaxException {
        
        final List<ItemSlipVendaEncalheDTO> itensRecibo = this.obterListaItensRecibo(cobranca);
        
        final String qtdeTotal = this.calculaQuantidadeTotalRecibo(itensRecibo);
        
        final String valorTotal = this.calculaValorTotalRecibo(itensRecibo);
        
        final Map<String, Object> parameters = obterParametrosRelatorioRecibo(cobranca,
                qtdeTotal,
                valorTotal);
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(itensRecibo);
        
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathJasper);
        
        final String path = url.toURI().getPath();
        
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
    public byte[] gerarReciboCobranca(final String nossoNumero) {
        
        final Cobranca cobranca = cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
        
        byte[] retorno;
        
        try {
            
            retorno = this.gerarDocumentoReciboIreport(cobranca,"/reports/slipComprovanteVendaEncalheSuplementar.jasper");
            
        } catch (final Exception e) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,"Erro ao gerar Recibo.");
        }
        
        return retorno;
    }
}
