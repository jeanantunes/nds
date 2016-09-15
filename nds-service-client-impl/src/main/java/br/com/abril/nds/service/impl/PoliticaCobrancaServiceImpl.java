package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PoliticaCobrancaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class PoliticaCobrancaServiceImpl implements PoliticaCobrancaService {
    
    @Autowired
    private PoliticaCobrancaRepository politicaCobrancaRepository;
    
    @Autowired
    private FormaCobrancaRepository formaCobrancaRepository;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private ConcentracaoCobrancaCotaRepository concentracaoCobrancaRepository;
    
    @Autowired
    private FornecedorService fornecedorService;
    
    @Autowired
    private DistribuidorRepository distribuidorRepository;
    
    @Autowired
    private ParametroCobrancaCotaRepository parametroCobrancaCotaRepository;
    
    
	                            /**
     * Método responsável por obter combo de Formas de Emissão
     * 
     * @return comboFormasEmissao: Formas de emissão
     */
    @Override
    public List<ItemDTO<FormaEmissao, String>> getComboFormasEmissao() {
        final List<ItemDTO<FormaEmissao,String>> comboFormasEmissao =  new ArrayList<ItemDTO<FormaEmissao,String>>();
        for (final FormaEmissao itemFormaEmissao: FormaEmissao.values()){
            comboFormasEmissao.add(new ItemDTO<FormaEmissao,String>(itemFormaEmissao, itemFormaEmissao.getDescFormaEmissao()));
        }
        return comboFormasEmissao;
    }
    
    
	                            /**
     * Método responsável por obter combo de Formas de Emissão dependendo do
     * Tipo de Cobrança
     * 
     * @return comboFormasEmissao: Formas de emissão
     */
    @Override
    public List<ItemDTO<FormaEmissao, String>> getComboFormasEmissaoTipoCobranca(final TipoCobranca tipoCobranca) {
        final List<ItemDTO<FormaEmissao,String>> comboFormasEmissao =  new ArrayList<ItemDTO<FormaEmissao,String>>();
        for (final FormaEmissao itemFormaEmissao: FormaEmissao.values()){
            if (itemFormaEmissao == FormaEmissao.INDIVIDUAL_AGREGADA){
                if (tipoCobranca == TipoCobranca.BOLETO_EM_BRANCO){
                    comboFormasEmissao.add(new ItemDTO<FormaEmissao,String>(itemFormaEmissao, itemFormaEmissao.getDescFormaEmissao()));
                }
            }
            else{
                comboFormasEmissao.add(new ItemDTO<FormaEmissao,String>(itemFormaEmissao, itemFormaEmissao.getDescFormaEmissao()));
            }
        }
        return comboFormasEmissao;
    }
    
    @Override
    public List<PoliticaCobranca> obterPoliticasCobranca(){
        
        final List<PoliticaCobranca> pc = politicaCobrancaRepository.buscarTodos();
        
        return pc;
    }
    
    @Override
    @Transactional(readOnly=true)
    public List<ParametroCobrancaVO> obterDadosPoliticasCobranca(
            final FiltroParametrosCobrancaDTO filtro) {
        
        final List<PoliticaCobranca> politicasCobranca = politicaCobrancaRepository.obterPoliticasCobranca(filtro);
        ParametroCobrancaVO parametroCobranca = null;
        List<ParametroCobrancaVO> parametrosCobranca = null;
        FormaCobranca forma = null;
        if ((politicasCobranca != null) && (!politicasCobranca.isEmpty())) {
            parametrosCobranca = new ArrayList<ParametroCobrancaVO>();
            for(final PoliticaCobranca itemPolitica:politicasCobranca){
                parametroCobranca = new ParametroCobrancaVO();
                
                parametroCobranca.setIdPolitica(itemPolitica.getId());
                parametroCobranca.setAcumulaDivida(itemPolitica.isAcumulaDivida() ? "Sim" : "Não");
                parametroCobranca.setCobrancaUnificada(itemPolitica.isUnificaCobranca() ? "Sim" : "Não");
                parametroCobranca.setFormaEmissao(itemPolitica.getFormaEmissao()!=null?itemPolitica.getFormaEmissao().getDescFormaEmissao():"");
                
                forma = itemPolitica.getFormaCobranca();
                if (forma!=null) {
                    parametroCobranca.setForma(forma.getTipoCobranca() != null ? forma.getTipoCobranca().getDescTipoCobranca() : "");
                    parametroCobranca.setBanco(forma.getBanco() != null ? forma.getBanco().getNome() : "");                   
                    parametroCobranca.setEnvioEmail(forma.isRecebeCobrancaEmail() ? "Sim" : "Não");
                    final StringBuilder fornecedores = new StringBuilder();
                    int i = 0;
                    for (final Fornecedor fornecedor: forma.getFornecedores()) {
                        fornecedores.append(fornecedor.getJuridica().getNome());
                        i++;
                        if(i < forma.getFornecedores().size()) {
                            fornecedores.append(", ");
                        }
                        
                    }
                    parametroCobranca.setFornecedores(fornecedores.toString());
                    final StringBuilder concentracaoPagamentos= new StringBuilder();
                    switch (forma.getTipoFormaCobranca()) {
                    case DIARIA:
                        concentracaoPagamentos.append(forma.getTipoFormaCobranca().getDescricao());
                        break;
                        
                    case MENSAL:
                        if (!forma.getDiasDoMes().isEmpty()) {
                            concentracaoPagamentos.append("Dia ");
                            concentracaoPagamentos.append(forma.getDiasDoMes()
                                    .get(0));
                        }
                        break;
                    case QUINZENAL:
                        if (!forma.getDiasDoMes().isEmpty()) {
                            concentracaoPagamentos.append("Dias ");
                            i = 0;
                            for (final Integer dia: forma.getDiasDoMes()) {
                                if(i > 0){
                                    concentracaoPagamentos.append(" e ");
                                }
                                concentracaoPagamentos.append(dia);
                                i++;
                            }
                        }
                        
                        break;
                    case SEMANAL:
                        i = 0;
                        for (final ConcentracaoCobrancaCota cobranca: forma.getConcentracaoCobrancaCota()) {
                            concentracaoPagamentos.append(cobranca.getDiaSemana().getDescricaoDiaSemana().substring(0, 3));
                            i++;
                            if(i < forma.getConcentracaoCobrancaCota().size()){
                                concentracaoPagamentos.append(", ");
                            }
                            
                        }
                        break;
                        
                    default:
                        break;
                    }
                    
                    
                    parametroCobranca.setConcentracaoPagamentos(concentracaoPagamentos.toString());
                }
                
                parametroCobranca.setPrincipal(itemPolitica.isPrincipal());
                
                
                parametrosCobranca.add(parametroCobranca);
            }
        }
        
        return parametrosCobranca;
    }
    
    @Override
    @Transactional(readOnly=true)
    public int obterQuantidadePoliticasCobranca(
            final FiltroParametrosCobrancaDTO filtro) {
        return politicaCobrancaRepository.obterQuantidadePoliticasCobranca(filtro);
    }
    
    @Override
    @Transactional(readOnly=true)
    public PoliticaCobranca obterPoliticaCobrancaPrincipal() {
        return politicaCobrancaRepository.buscarPoliticaCobrancaPrincipal();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PoliticaCobranca> obterDadosPoliticaCobranca(final TipoCobranca tipoCobranca) {
        
        final List<TipoCobranca> tipos = new ArrayList<>();
        tipos.add(tipoCobranca);
        
        return politicaCobrancaRepository.obterPoliticasCobranca(tipos);
    }
    
	                            /**
     * Método responsável por obter os dados da politica de cobranca
     * 
     * @param idPolitica: ID da politica de cobranca
     * @return Data Transfer Object com os dados da politica de cobranca
     */
    @Override
    @Transactional(readOnly = true)
    public ParametroCobrancaDTO obterDadosPoliticaCobranca(final Long idPolitica) {
        
        PoliticaCobranca politica = null;
        ParametroCobrancaDTO parametroCobrancaDTO = null;
        politica = politicaCobrancaRepository.buscarPorId(idPolitica);
        FormaCobranca formaCobranca = null;
        formaCobranca = politica.getFormaCobranca();
        
        if (politica!=null){
            
            parametroCobrancaDTO = new ParametroCobrancaDTO();
            
            parametroCobrancaDTO.setIdPolitica(politica.getId());
            parametroCobrancaDTO.setPrincipal(politica.isPrincipal()?true:false);
            parametroCobrancaDTO.setAcumulaDivida(politica.isAcumulaDivida()?true:false);
            parametroCobrancaDTO.setFormaEmissao(politica.getFormaEmissao());
            parametroCobrancaDTO.setUnificada(politica.isUnificaCobranca()?true:false);
            
            if(politica.getFormaCobranca() != null
                    && politica.getFornecedorPadrao() != null) {
                
                parametroCobrancaDTO.setIdFornecedorPadrao(politica.getFornecedorPadrao().getId());
            }
            
            if(politica.getFormaCobranca() != null && politica.getFatorVencimento() != null) {
                
                parametroCobrancaDTO.setFatorVencimento(politica.getFatorVencimento());
            }
            
            Set<ConcentracaoCobrancaCota> concentracoesCobranca=null;
            
            if (formaCobranca!=null){
                
                if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.SEMANAL) {
                    concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
                }
                
                parametroCobrancaDTO.setDiasDoMes(new ArrayList<Integer>(formaCobranca.getDiasDoMes()));
                
                parametroCobrancaDTO.setTipoFormaCobranca(formaCobranca.getTipoFormaCobranca());
                parametroCobrancaDTO.setEnvioEmail(formaCobranca.isRecebeCobrancaEmail() ? true : false);
                parametroCobrancaDTO.setVencimentoDiaUtil(formaCobranca.isVencimentoDiaUtil() ? true : false);           
                parametroCobrancaDTO.setFormaCobrancaBoleto(formaCobranca.getFormaCobrancaBoleto());
                parametroCobrancaDTO.setTaxaMulta(formaCobranca.getTaxaMulta());
                parametroCobrancaDTO.setValorMulta(formaCobranca.getValorMulta());
                parametroCobrancaDTO.setTaxaJuros(formaCobranca.getTaxaJurosMensal());
                
                if ((concentracoesCobranca != null) && (!concentracoesCobranca.isEmpty())) {
                    for (final ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca) {
                        
                        final DiaSemana dia = itemConcentracaoCobranca.getDiaSemana();
                        if (dia==DiaSemana.DOMINGO) {
                            parametroCobrancaDTO.setDomingo(true);
                        }
                        
                        if (dia==DiaSemana.SEGUNDA_FEIRA) {
                            parametroCobrancaDTO.setSegunda(true);
                        }
                        
                        if (dia==DiaSemana.TERCA_FEIRA) {
                            parametroCobrancaDTO.setTerca(true);
                        }
                        
                        if (dia==DiaSemana.QUARTA_FEIRA) {
                            parametroCobrancaDTO.setQuarta(true);
                        }
                        
                        if (dia==DiaSemana.QUINTA_FEIRA) {
                            parametroCobrancaDTO.setQuinta(true);
                        }
                        
                        if (dia==DiaSemana.SEXTA_FEIRA) {
                            parametroCobrancaDTO.setSexta(true);
                        }
                        
                        if (dia==DiaSemana.SABADO) {
                            parametroCobrancaDTO.setSabado(true);
                        }
                        
                    }
                }
                
                final Banco banco = formaCobranca.getBanco();
                if (banco != null) {
                    parametroCobrancaDTO.setIdBanco(banco.getId());
                    parametroCobrancaDTO.setTaxaMulta(banco.getMulta());
                    parametroCobrancaDTO.setValorMulta(banco.getVrMulta());
                    parametroCobrancaDTO.setTaxaJuros(banco.getJuros());
                    parametroCobrancaDTO.setInstrucoes1(banco.getInstrucoes1());
                    parametroCobrancaDTO.setInstrucoes2(banco.getInstrucoes2());
                    parametroCobrancaDTO.setInstrucoes3(banco.getInstrucoes3());
                    parametroCobrancaDTO.setInstrucoes4(banco.getInstrucoes4());
                }
                parametroCobrancaDTO.setTipoCobranca(formaCobranca.getTipoCobranca());
                
                
                final Set<Fornecedor> fornecedores = formaCobranca.getFornecedores();
                final List<Long> fornecedoresID = new ArrayList<Long>();
                if (fornecedores!=null) {
                    for(final Fornecedor itemFornecedor:fornecedores) {
                        fornecedoresID.add(itemFornecedor.getId());
                    }
                    parametroCobrancaDTO.setFornecedoresId(fornecedoresID);
                }
            }
        }
        
        return parametroCobrancaDTO;
    }
    
    
    @Override
    @Transactional
    public void postarPoliticaCobranca(final ParametroCobrancaDTO parametroCobrancaDTO) {
        
        PoliticaCobranca politica = null;
        FormaCobranca formaCobranca = null;
        Set<ConcentracaoCobrancaCota> concentracoesCobranca = null;
        Banco banco = null;
        boolean novaPolitica = false;
        boolean novaForma = false;
        
        if (parametroCobrancaDTO.getIdBanco() != null) {
            banco=bancoRepository.buscarPorId(parametroCobrancaDTO.getIdBanco());
        }
        
        
        if (parametroCobrancaDTO.getIdPolitica() != null) {
            politica = politicaCobrancaRepository.buscarPorId(parametroCobrancaDTO.getIdPolitica());
        }
        
        
        if(politica==null) {
            
            if (politicaCobrancaRepository.verificarPorTipoCobrancaPor(parametroCobrancaDTO.getTipoCobranca())) {
                
                throw new ValidacaoException(
                        TipoMensagem.WARNING,
                        "Já existe parâmetro cadastrado para o Tipo de Pagamento " +
                                parametroCobrancaDTO.getTipoCobranca().getDescricao());
            }
            
            novaPolitica = true;
            novaForma = true;
            politica = new PoliticaCobranca();
            formaCobranca = new FormaCobranca();
        } else {
            novaPolitica = false;
            
            formaCobranca = politica.getFormaCobranca();
            
            if (formaCobranca!=null) {
                novaForma=false;
                
                concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
                
                //APAGA CONCENTRACOES COBRANCA DA FORMA DE COBRANCA
                if ((concentracoesCobranca != null) && (!concentracoesCobranca.isEmpty())) {
                    formaCobranca.setConcentracaoCobrancaCota(null);
                    for(final ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
                        concentracaoCobrancaRepository.remover(itemConcentracaoCobranca);
                    }
                }
            } else {
                novaForma=true;
                formaCobranca = new FormaCobranca();
            }
            
        }
        
        
        final PoliticaCobranca parametroCobrancaPrincipal = this.obterPoliticaCobrancaPrincipal();
        
        if(parametroCobrancaPrincipal != null && parametroCobrancaDTO.isPrincipal()) {
            parametroCobrancaPrincipal.setPrincipal(false);
            politicaCobrancaRepository.alterar(parametroCobrancaPrincipal);
        }
        politica.setPrincipal(parametroCobrancaDTO.isPrincipal());
        //politica.setCobradoPeloBackoffice(parametroCobrancaDTO.isCobradoPeloBackoffice());
        politica.setFormaEmissao(parametroCobrancaDTO.getFormaEmissao());
        politica.setUnificaCobranca(parametroCobrancaDTO.isUnificada());
        
        politica.setAtivo(true);
        politica.setDistribuidor(distribuidorRepository.obter());
        politica.setFatorVencimento(Integer.valueOf(parametroCobrancaDTO.getFatorVencimento().toString()));
        politica.setFornecedorPadrao(fornecedorService.obterFornecedorPorId(parametroCobrancaDTO.getIdFornecedorPadrao()));
        
        if(parametroCobrancaDTO.isPrincipal()) {
        	 politica.setAcumulaDivida(parametroCobrancaDTO.isAcumulaDivida());
        	 this.atualizarAcumuloDeDividas(parametroCobrancaDTO.isAcumulaDivida());
        }
        
        formaCobranca.setDiasDoMes(parametroCobrancaDTO.getDiasDoMes());
        formaCobranca.setTipoFormaCobranca(parametroCobrancaDTO.getTipoFormaCobranca());
        formaCobranca.setTipoCobranca(parametroCobrancaDTO.getTipoCobranca());
        formaCobranca.setBanco(banco);
        
        formaCobranca.setRecebeCobrancaEmail(parametroCobrancaDTO.isEnvioEmail());
        formaCobranca.setAtiva(true);
        
        if (parametroCobrancaDTO.getIdBanco() == null) {
            
            formaCobranca.setTaxaJurosMensal(parametroCobrancaDTO.getTaxaJuros());
            formaCobranca.setTaxaMulta(parametroCobrancaDTO.getTaxaMulta());
            formaCobranca.setValorMulta(parametroCobrancaDTO.getValorMulta());
        }
        
        formaCobranca.setVencimentoDiaUtil(parametroCobrancaDTO.isVencimentoDiaUtil());
        
        if(TipoCobranca.BOLETO.equals(parametroCobrancaDTO.getTipoCobranca()) || TipoCobranca.BOLETO_EM_BRANCO.equals(parametroCobrancaDTO.getTipoCobranca())) {
            formaCobranca.setFormaCobrancaBoleto(parametroCobrancaDTO.getFormaCobrancaBoleto());
        } else {
            formaCobranca.setFormaCobrancaBoleto(null);
        }
        
        
        //CONCENTRACAO COBRANCA (DIAS DA SEMANA)
        concentracoesCobranca = new HashSet<ConcentracaoCobrancaCota>();
        ConcentracaoCobrancaCota concentracaoCobranca;
        if (parametroCobrancaDTO.isDomingo()) {
            
            concentracaoCobranca = new ConcentracaoCobrancaCota();
            concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
            concentracaoCobranca.setFormaCobranca(formaCobranca);
            
            concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
            concentracoesCobranca.add(concentracaoCobranca);
        }
        if (parametroCobrancaDTO.isSegunda()) {
            
            concentracaoCobranca = new ConcentracaoCobrancaCota();
            concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
            concentracaoCobranca.setFormaCobranca(formaCobranca);
            
            concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
            concentracoesCobranca.add(concentracaoCobranca);
        }
        if (parametroCobrancaDTO.isTerca()) {
            
            concentracaoCobranca = new ConcentracaoCobrancaCota();
            concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
            concentracaoCobranca.setFormaCobranca(formaCobranca);
            
            concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
            concentracoesCobranca.add(concentracaoCobranca);
        }
        if (parametroCobrancaDTO.isQuarta()) {
            
            concentracaoCobranca = new ConcentracaoCobrancaCota();
            concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
            concentracaoCobranca.setFormaCobranca(formaCobranca);
            
            concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
            concentracoesCobranca.add(concentracaoCobranca);
        }
        if (parametroCobrancaDTO.isQuinta()) {
            
            concentracaoCobranca = new ConcentracaoCobrancaCota();
            concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
            concentracaoCobranca.setFormaCobranca(formaCobranca);
            
            concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
            concentracoesCobranca.add(concentracaoCobranca);
        }
        if (parametroCobrancaDTO.isSexta()) {
            
            concentracaoCobranca = new ConcentracaoCobrancaCota();
            concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
            concentracaoCobranca.setFormaCobranca(formaCobranca);
            
            concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
            concentracoesCobranca.add(concentracaoCobranca);
        }
        if (parametroCobrancaDTO.isSabado()) {
            
            concentracaoCobranca = new ConcentracaoCobrancaCota();
            concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
            concentracaoCobranca.setFormaCobranca(formaCobranca);
            
            concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
            concentracoesCobranca.add(concentracaoCobranca);
        }
        
        if (!concentracoesCobranca.isEmpty()) {
            formaCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
        }
        
        
        formaCobranca.setFornecedores(null);
        if ((parametroCobrancaDTO.getFornecedoresId() != null) && (parametroCobrancaDTO.getFornecedoresId().size() > 0)) {
            
            final List<Long> idsFornecedor = parametroCobrancaDTO.getFornecedoresId();
            Fornecedor fornecedor;
            final Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
            for (final Long idFornecedor : idsFornecedor) {
                
                fornecedor = fornecedorService.obterFornecedorPorId(idFornecedor);
                if (fornecedor != null) {
                    fornecedores.add(fornecedor);
                }
            }
            if (!fornecedores.isEmpty()) {
                
                formaCobranca.setFornecedores(fornecedores);
            }
        }
        
        
        if(novaPolitica) {
            
            formaCobrancaRepository.adicionar(formaCobranca);
            
            politica.setFormaCobranca(formaCobranca);
            
            politicaCobrancaRepository.adicionar(politica);
            
        } else {
            
            if(novaForma) {
                formaCobrancaRepository.adicionar(formaCobranca);
            } else {
                formaCobrancaRepository.merge(formaCobranca);
            }
            
            politica.setFormaCobranca(formaCobranca);
            
            politicaCobrancaRepository.merge(politica);
        }
    }
    
    private void atualizarAcumuloDeDividas(boolean acumulaDivida) {
    	
    	List<PoliticaCobranca> politicasCobranca = politicaCobrancaRepository.buscarTodos();
		
    	for(PoliticaCobranca item : politicasCobranca){
    		item.setAcumulaDivida(acumulaDivida);
    		politicaCobrancaRepository.merge(item);
    	}
	}


	@Override
    @Transactional
    public void dasativarPoliticaCobranca(final Long idPolitica) {
        
        final PoliticaCobranca pc = politicaCobrancaRepository.buscarPorId(idPolitica);
        final FormaCobranca fcDistrib = pc.getFormaCobranca();
        
        boolean desativar = true;
        
        //caso existam cotas que usam o parametro de cobranca do distribuidor
        if (pc.isPrincipal() || formaCobrancaRepository.obterFormasCobrancaAtivaCotas(true, fcDistrib.getId())){
            desativar = parametroCobrancaCotaRepository.verificarCotaSemParametroCobrancaPorFormaCobranca(fcDistrib.getId());
        }
        
        if(fcDistrib.getTipoCobranca().equals(TipoCobranca.BOLETO_AVULSO)) {
        	desativar = parametroCobrancaCotaRepository.verificarCotaSemParametroCobrancaPorFormaCobranca(fcDistrib.getId());
        }
        
        if(desativar) {
            politicaCobrancaRepository.desativarPoliticaCobranca(idPolitica);
        } else {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não foi possível desativar. \nExistem cotas utilizando esta forma de cobrança!");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TipoCobranca> obterTiposCobrancaDistribuidor() {
        
        final List<TipoCobranca> tiposCobranca =
                politicaCobrancaRepository.obterTiposCobrancaDistribuidor();
        
        return tiposCobranca;
    }
    
    @Override
    @Transactional
    public Fornecedor obterFornecedorPadrao() {
    	
    	return politicaCobrancaRepository.obterFornecedorPadrao();
    }
}
