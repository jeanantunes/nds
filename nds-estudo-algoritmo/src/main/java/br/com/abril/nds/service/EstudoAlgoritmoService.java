package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dao.DefinicaoBasesDAO;
import br.com.abril.nds.dao.EstudoDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enumerators.DataReferencia;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.process.ajustefinalreparte.AjusteFinalReparte;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;
import br.com.abril.nds.process.verificartotalfixacoes.VerificarTotalFixacoes;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link DefinicaoBases} - {@link SomarFixacoes} -
 * {@link VerificarTotalFixacoes} - {@link MontaTabelaEstudos} -
 * {@link CorrecaoVendas} - {@link Medias} - {@link Bonificacoes} -
 * {@link AjusteCota} - {@link JornaleirosNovos} - {@link VendaMediaFinal} -
 * {@link AjusteReparte} - {@link RedutorAutomatico} - {@link ReparteMinimo} -
 * {@link ReparteProporcional} - {@link EncalheMaximo} -
 * {@link ComplementarAutomatico} - {@link CalcularReparte} Processo Pai: - N/A
 * 
 * Processo Anterior: N/A Próximo Processo: N/A
 * </p>
 */
@Service
public class EstudoAlgoritmoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EstudoAlgoritmoService.class);
    
    @Autowired
    private EstudoDAO estudoDAO;
    
    @Autowired
    private DefinicaoBasesDAO definicaoBasesDAO;
    
    @Autowired
    private ProdutoEdicaoDAO produtoEdicaoDAO;
    
    @Autowired
    private DefinicaoBases definicaoBases;
    
    @Autowired
    private VerificarTotalFixacoes verificarTotalFixacoes;
    
    @Autowired
    private AjusteReparte ajusteReparte;
    
    @Autowired
    private RedutorAutomatico redutorAutomatico;
    
    @Autowired
    private ReparteMinimo reparteMinimo;
    
    @Autowired
    private ReparteProporcional reparteProporcional;
    
    @Autowired
    private EncalheMaximo encalheMaximo;
    
    @Autowired
    private ComplementarAutomatico complementarAutomatico;
    
    @Autowired
    private CalcularReparte calcularReparte;
    
    @Autowired
    private AjusteFinalReparte ajusteFinalReparte;
    
    @Autowired
    private CorrecaoVendas correcaoVendas;
    
    @Autowired
    private Medias medias;
    
    @Autowired
    private VendaMediaFinal vendaMediaFinal;
    
    @Autowired
    private Bonificacoes bonificacoes;
    
    @Autowired
    private JornaleirosNovos jornaleirosNovos;
    
    public static void calculate(final EstudoTransient estudo) {
        // Somatória da venda média de todas as cotas e
        // Somatória de reparte das edições abertas de todas as cotas
        estudo.setSomatoriaVendaMedia(BigDecimal.ZERO);
        estudo.setSomatoriaReparteEdicoesAbertas(BigDecimal.ZERO);
        estudo.setTotalPDVs(BigDecimal.ZERO);
        for (final CotaEstudo cota : estudo.getCotas()) {
            if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado,
                    ClassificacaoCota.BancaSoComEdicaoBaseAberta, ClassificacaoCota.RedutorAutomatico)) {
                estudo.setSomatoriaVendaMedia(estudo.getSomatoriaVendaMedia().add(cota.getVendaMedia()));
            }
            estudo.setSomatoriaReparteEdicoesAbertas(estudo.getSomatoriaReparteEdicoesAbertas().add(
                    cota.getSomaReparteEdicoesAbertas()));
            if (cota.getQuantidadePDVs() != null) {
                estudo.setTotalPDVs(estudo.getTotalPDVs().add(cota.getQuantidadePDVs()));
            }
        }
    }
    
    public static void somarVendaMedia(final EstudoTransient estudo) {
        estudo.setSomatoriaVendaMedia(BigDecimal.ZERO);
        for (final CotaEstudo cota : estudo.getCotas()) {
            if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado,
                    ClassificacaoCota.BancaSoComEdicaoBaseAberta, ClassificacaoCota.RedutorAutomatico)) {
                if (cota.getVendaMedia() != null) {
                    estudo.setSomatoriaVendaMedia(estudo.getSomatoriaVendaMedia().add(cota.getVendaMedia()));
                }
            }
        }
    }
    
    public void carregarParametros(final EstudoTransient estudo) {
        estudo.setProdutoEdicaoEstudo(produtoEdicaoDAO.getProdutoEdicaoEstudo(estudo.getProdutoEdicaoEstudo()
                .getProduto().getCodigo(), estudo.getProdutoEdicaoEstudo().getNumeroEdicao(), estudo
                .getProdutoEdicaoEstudo().getIdLancamento()));
        if (estudo.getPacotePadrao() == null) {
            estudo.setPacotePadrao(BigInteger.valueOf(estudo.getProdutoEdicaoEstudo().getPacotePadrao()));
        }
        estudo.getProdutoEdicaoEstudo().setPacotePadrao(0);
        estudoDAO.carregarParametrosDistribuidor(estudo);
        estudoDAO.carregarPercentuaisExcedente(estudo);
    }
    
    public LinkedList<ProdutoEdicaoEstudo> getEdicoesBases(final ProdutoEdicaoEstudo edicao) {
        LOGGER.info("Buscando edições para estudo.");
        return definicaoBasesDAO.getEdicoesBases(edicao);
    }
    
    public List<ProdutoEdicaoEstudo> buscaEdicoesAnosAnterioresVeraneio(final ProdutoEdicaoEstudo edicao) {
        List<ProdutoEdicaoEstudo> listaEdicoesBase = definicaoBasesDAO.listaEdicoesAnosAnterioresMesmoMes(edicao);
        
        if (!listaEdicoesBase.isEmpty()) {
            return listaEdicoesBase;
        }
        
        listaEdicoesBase = definicaoBasesDAO
                .listaEdicoesAnosAnterioresVeraneio(edicao, getDatasPeriodoVeraneio(edicao));
        if (listaEdicoesBase.isEmpty()) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
                    "Não foram encontradas edições de veraneio, favor inserir as bases manualmente."));
        }
        return listaEdicoesBase;
    }
    
    public List<ProdutoEdicaoEstudo> buscaEdicoesAnosAnterioresSaidaVeraneio(final ProdutoEdicaoEstudo edicao) {
        return definicaoBasesDAO.listaEdicoesAnosAnterioresVeraneio(edicao, getDatasPeriodoSaidaVeraneio(edicao));
    }
    
    private List<LocalDate> getDatasPeriodoVeraneio(final ProdutoEdicaoEstudo edicao) {
        final List<LocalDate> periodoVeraneio = new ArrayList<LocalDate>();
        final Date dataLancamento = edicao.getDataLancamento();
        periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.DEZEMBRO_20));
        periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ZERO, DataReferencia.FEVEREIRO_15));
        periodoVeraneio.add(parseLocalDate(dataLancamento, Years.TWO, DataReferencia.DEZEMBRO_20));
        periodoVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.FEVEREIRO_15));
        return periodoVeraneio;
    }
    
    private List<LocalDate> getDatasPeriodoSaidaVeraneio(final ProdutoEdicaoEstudo edicao) {
        final List<LocalDate> periodoSaidaVeraneio = new ArrayList<LocalDate>();
        final Date dataLancamento = edicao.getDataLancamento();
        periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.FEVEREIRO_16));
        periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.ONE, DataReferencia.DEZEMBRO_19));
        periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.TWO, DataReferencia.FEVEREIRO_16));
        periodoSaidaVeraneio.add(parseLocalDate(dataLancamento, Years.TWO, DataReferencia.DEZEMBRO_19));
        return periodoSaidaVeraneio;
    }
    
    private LocalDate parseLocalDate(final Date dataLancamento, final Years anosSubtrair, final DataReferencia dataReferencia) {
        return MonthDay.parse(dataReferencia.getData()).toLocalDate(
                LocalDate.fromDateFields(dataLancamento).minus(anosSubtrair).getYear());
    }
    
    public static BigInteger arredondarPacotePadrao(final EstudoTransient estudo, final BigDecimal reparte) {
        if (reparte == null) {
            return BigInteger.ZERO;
        } else if (estudo.isDistribuicaoPorMultiplos() && estudo.getPacotePadrao() != null
                && estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0) {
            return reparte.divide(new BigDecimal(estudo.getPacotePadrao()), 0, BigDecimal.ROUND_HALF_UP).toBigInteger()
                    .multiply(estudo.getPacotePadrao());
        } else {
            return reparte.setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
        }
    }
    
    public void gravarEstudo(final EstudoTransient estudo) {
        estudoDAO.gravarEstudo(estudo);
    }
    
    public EstudoTransient gerarEstudoAutomatico(final ProdutoEdicaoEstudo produto, final BigInteger reparte,
            final Usuario usuario) throws Exception {
        
        return gerarEstudoAutomatico(null, produto, reparte, usuario);
    }
    
    public EstudoTransient gerarEstudoAutomatico(final DistribuicaoVendaMediaDTO distribuicaoVendaMedia,
            final ProdutoEdicaoEstudo produto, final BigInteger reparte, final Usuario usuario) throws Exception {
        
        LOGGER.debug("Iniciando execução do estudo.");
        final EstudoTransient estudo = new EstudoTransient();
        estudo.setDataCadastro(new Date());
        estudo.setDataLancamento(produto.getDataLancamento());
        estudo.setStatusEstudo("ESTUDO_FECHADO");
        estudo.setUsuario(usuario);
        estudo.setProdutoEdicaoEstudo(produto);
        estudo.setReparteDistribuir(reparte);
        estudo.setReparteDistribuirInicial(reparte);
        
        estudo.setDistribuicaoPorMultiplos(0); // valor default
        estudo.setPacotePadrao(new BigDecimal(produto.getPacotePadrao()).toBigInteger()); // valor
        // default
        
        if (distribuicaoVendaMedia != null) {
            estudo.setBonificacoes(distribuicaoVendaMedia.getBonificacoes());
            estudo.setReparteDistribuir(distribuicaoVendaMedia.getReparteDistribuir());
            estudo.setDistribuicaoPorMultiplos(distribuicaoVendaMedia.isDistribuicaoPorMultiplo() ? 1 : 0);
            estudo.setReparteMinimo(distribuicaoVendaMedia.getReparteMinimo());
            estudo.setComplementarAutomatico(distribuicaoVendaMedia.getComplementarAutomatico());
            estudo.setUsarFixacao(distribuicaoVendaMedia.isUsarFixacao());
            estudo.setDistribuicaoVendaMediaDTO(distribuicaoVendaMedia);
            final LinkedList<ProdutoEdicaoEstudo> edicoesBase = new LinkedList<>();
            
            // ordenando cotas pela data de lancamento
            Collections.sort(distribuicaoVendaMedia.getBases(), new Comparator<ProdutoEdicaoDTO>() {
                
                @Override
                public int compare(final ProdutoEdicaoDTO o1, final ProdutoEdicaoDTO o2) {
                    return o2.getDataLancamento().compareTo(o1.getDataLancamento());
                }
            });
            
            for (final ProdutoEdicaoDTO base : distribuicaoVendaMedia.getBases()) {
                final ProdutoEdicaoEstudo ed = new ProdutoEdicaoEstudo();
                ed.setProduto(new Produto());
                ed.setId(base.getId());
                ed.getProduto().setCodigo(base.getCodigoProduto());
                ed.getProduto().setId(base.getIdProduto());
                ed.setNumeroEdicao(base.getNumeroEdicao());
                ed.setIndicePeso(new BigDecimal(base.getPeso()));
                ed.setPeriodo(base.getPeriodo());
                ed.setParcial(base.isParcial());
                ed.setEdicaoAberta(definicaoBasesDAO.traduzStatus(base.getStatus()));
                edicoesBase.add(ed);
            }
            estudo.setEdicoesBase(edicoesBase);
            if (distribuicaoVendaMedia.isDistribuicaoPorMultiplo() && distribuicaoVendaMedia.getMultiplo() != null) {
                estudo.setPacotePadrao(distribuicaoVendaMedia.getMultiplo());
            }
            
            // verificacao se o reparte minimo e multiplo do pacote padrao
            // TODO: melhorar logica ou encontrar alguma funcao da api mais
            // simples
            if (estudo.getPacotePadrao() != null && estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0
                    && estudo.getReparteMinimo() != null && estudo.getReparteMinimo().compareTo(BigInteger.ZERO) > 0) {
                final BigDecimal quebrado = new BigDecimal(estudo.getReparteMinimo()).divide(new BigDecimal(estudo
                        .getPacotePadrao()), 4, BigDecimal.ROUND_HALF_UP);
                final BigDecimal inteiro = new BigDecimal(estudo.getReparteMinimo()).divide(new BigDecimal(estudo
                        .getPacotePadrao()), 0, BigDecimal.ROUND_HALF_UP);
                if (quebrado.compareTo(inteiro) != 0) {
                    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, String.format(
                            "O reparte mínimo deve ser múltiplo de %s.", estudo.getPacotePadrao())));
                }
            }
        }
        
        // carregando parâmetros do banco de dados
        carregarParametros(estudo);
        
        definicaoBases.executar(estudo);
        
        verificarTotalFixacoes.executar(estudo);
        
        calculate(estudo);
        
        for (final CotaEstudo cota : estudo.getCotas()) {
            correcaoVendas.executar(cota, estudo);
            
            medias.executar(cota);
        }
        bonificacoes.executar(estudo);
        
        jornaleirosNovos.executar(estudo);
        
        vendaMediaFinal.executar(estudo);
        
        somarVendaMedia(estudo);
        
        ajusteReparte.executar(estudo);
        
        redutorAutomatico.executar(estudo);
        
        reparteMinimo.executar(estudo);
        
        reparteProporcional.executar(estudo);
        
        encalheMaximo.executar(estudo);
        
        complementarAutomatico.executar(estudo);
        
        calcularReparte.executar(estudo);
        
        // processo que faz os ajustes finais e grava as informações no banco de
        // dados
        ajusteFinalReparte.executar(estudo);
        
        LOGGER.debug("Execução do estudo concluída");
        return estudo;
    }
    
    public boolean isCotaDentroDoComponenteElemento(final ComponentesPDV componente, final String[] elementos, final CotaEstudo cota) {
        
        if (componente != null && elementos != null) {
            if (componente.equals(ComponentesPDV.AREA_DE_INFLUENCIA)) {
                for (final String elemento : elementos) {
                    if (cota.getAreasInfluenciaPdv().contains(Integer.parseInt(elemento))) {
                        return true;
                    }
                }
            } else if (componente.equals(ComponentesPDV.BAIRRO)) {
                for (final String elemento : elementos) {
                    if (cota.getBairros().contains(elemento)) {
                    	return true;
                    }
                }
            } else if (componente.equals(ComponentesPDV.COTAS_A_VISTA)) {
                for (final String elemento : elementos) {
                    if (cota.getTiposCota().contains(elemento)) {
                    	return true;
                    }
                }
            } else if (componente.equals(ComponentesPDV.COTAS_NOVAS_RETIVADAS)) {
                for (final String elemento : elementos) {
                    if ((cota.isNova() && "1".equals(elemento)) || (!cota.isNova() && "0".equals(elemento))) {
                    	return true;
                    }
                }
            } else if (componente.equals(ComponentesPDV.DISTRITO)) {
                for (final String elemento : elementos) {
                    if (cota.getEstados().contains(elemento)) {
                    	return true;
                    }
                }
            } else if (componente.equals(ComponentesPDV.GERADOR_DE_FLUXO)) {
                for (final String elemento : elementos) {
                    if (cota.getTiposGeradorFluxo().contains(Integer.parseInt(elemento))) {
                    	return true;
                    }
                }
            } else if (componente.equals(ComponentesPDV.REGIAO)) {
                for (final String elemento : elementos) {
                    if (cota.getRegioes().contains(Integer.parseInt(elemento))) {
                    	return true;
                    }
                }
            } else if (componente.equals(ComponentesPDV.TIPO_PONTO_DE_VENDA)) {
                for (final String elemento : elementos) {
                    if (cota.getTiposPontoPdv().contains(Integer.parseInt(elemento))) {
                    	return true;
                    }
                }
            }
        }
        return false;
    }
}
