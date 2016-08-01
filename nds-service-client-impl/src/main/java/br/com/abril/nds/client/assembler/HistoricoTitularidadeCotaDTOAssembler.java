package br.com.abril.nds.client.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.dto.CaracteristicaDTO;
import br.com.abril.nds.dto.ChequeCaucaoDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.FiadorDTO;
import br.com.abril.nds.dto.FormaCobrancaCaucaoLiquidaDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.GarantiaNotaPromissoriaDTO;
import br.com.abril.nds.dto.GarantiaOutrosDTO;
import br.com.abril.nds.dto.ImovelDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.TipoEstabelecimentoAssociacaoPDVDTO;
import br.com.abril.nds.dto.TipoLicencaMunicipalDTO;
import br.com.abril.nds.dto.TipoPontoPDVDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaBanco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCaucaoLiquida;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaChequeCaucao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCodigoDescricao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaConcentracaoCobranca;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDistribuicao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaEndereco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFiador;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFiadorGarantia;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFornecedor;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFuncionamentoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaImovel;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaNotaPromissoria;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaOutros;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPagamentoCaucaoLiquida;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaFisica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaJuridica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaReferenciaCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaTelefone;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;


/**
 * Classe utilitária para auxílio na 
 * criação dos DTO's com informações do 
 * histórico de titularidade da cota
 * 
 * @author francisco.garcia
 *
 */
public final class HistoricoTitularidadeCotaDTOAssembler {
    
    private static final String FORMATO_ENDERECO = "%s,%s-%s-%s";
    private static final String FORMATO_TELEFONE = "%s-%s";
    
    private HistoricoTitularidadeCotaDTOAssembler() {
    }
    
    
    /**
     * Cria a instâcia de {@link CotaDTO} com as informações
     * da instância de {@link HistoricoTitularidadeCota}
     * @param cota instância de {@link HistoricoTitularidadeCota} para a criação
     * da instância de {@link CotaDTO}
     * @return instância de {@link CotaDTO}
     */
    public static CotaDTO toCotaDTO(HistoricoTitularidadeCota cota) {
        CotaDTO dto = new CotaDTO();
        
        dto.setNumeroCota(cota.getNumeroCota());
        dto.setDataInclusao(cota.getDataInclusao());
        dto.setEmail(cota.getEmail());
        dto.setStatus(cota.getSituacaoCadastro());
        dto.setClassificacaoSelecionada(cota.getClassificacaoExpectativaFaturamento());
        dto.addItemClassificacaoFaturamento(cota.getClassificacaoExpectativaFaturamento());
        dto.setExigeNFE(cota.isExigeNfe());
        dto.setContribuinteICMS(cota.isContribuinteICMS());
        dto.setEmailNF(cota.getEmailNfe());
       
        dto.setInicioPeriodo(cota.getInicioPeriodoCotaBase());
        dto.setFimPeriodo(cota.getFimPeriodoCotaBase());
        
        int i = 0;
        for (HistoricoTitularidadeCotaReferenciaCota referencia : cota.getReferencias()) {
            if (i == 0) {
                dto.setHistoricoPrimeiraCota(referencia.getNumeroCota());
                dto.setHistoricoPrimeiraPorcentagem(referencia.getPercentual());
            } else if (i == 1) {
                dto.setHistoricoSegundaCota(referencia.getNumeroCota());
                dto.setHistoricoSegundaPorcentagem(referencia.getPercentual());
            } else if (i == 2) {
                dto.setHistoricoTerceiraCota(referencia.getNumeroCota());
                dto.setHistoricoTerceiraPorcentagem(referencia.getPercentual());
            }
            i++;
        }
        
        if (cota.isPessoaFisica()) {
            HistoricoTitularidadeCotaPessoaFisica pf = cota.getPessoaFisica();
            dto.setNomePessoa(pf.getNome());
            dto.setNomeFantasia(null);
            dto.setNumeroCPF(pf.getCpf());
            dto.setNumeroRG(pf.getRg());
            dto.setDataNascimento(pf.getDataNascimento());
            dto.setOrgaoEmissor(pf.getOrgaoEmissor());
            dto.setEstadoSelecionado(pf.getUfOrgaoEmissor());
            dto.setEstadoCivilSelecionado(pf.getEstadoCivil());
            dto.setSexoSelecionado(pf.getSexo());
            dto.setNacionalidade(pf.getNacionalidade());
            dto.setNatural(pf.getNatural());
            dto.setTipoPessoa(TipoPessoa.FISICA);
        } else {
            HistoricoTitularidadeCotaPessoaJuridica pj = cota.getPessoaJuridica();
            dto.setRazaoSocial(pj.getRazaoSocial());
            dto.setNomeFantasia(pj.getNomeFantasia());
            dto.setNumeroCnpj(pj.getCnpj());
            dto.setInscricaoEstadual(pj.getInscricaoEstadual());
            dto.setInscricaoMunicipal(pj.getInscricaoMunicipal());
            dto.setTipoPessoa(TipoPessoa.JURIDICA);
        }
        return dto;
    }
    
    /**
     * Cria a coleção de {@link EnderecoAssociacaoDTO} com informações
     * da coleção de {@link HistoricoTitularidadeCotaEndereco}
     * @param enderecos coleção de {@link HistoricoTitularidadeCotaEndereco} para criação
     * da coleção de {@link EnderecoAssociacaoDTO}
     * @return coleção de {@link EnderecoAssociacaoDTO}
     */
    public static Collection<EnderecoAssociacaoDTO> toEnderecoAssociacaoDTOCollection(Collection<HistoricoTitularidadeCotaEndereco> enderecos) {
        List<EnderecoAssociacaoDTO> dtos = new ArrayList<EnderecoAssociacaoDTO>(enderecos.size());
        for (HistoricoTitularidadeCotaEndereco endereco : enderecos) {
            EnderecoAssociacaoDTO dto = new EnderecoAssociacaoDTO();
            dto.setEnderecoPrincipal(endereco.isPrincipal());
            dto.setTipoEndereco(endereco.getTipoEndereco());
            dto.setEndereco(toEnderecoDTO(endereco));
            dto.setId(Util.generateObjectId(dto));
            dtos.add(dto);
        }
        return dtos;
    }
    
    /**
     * Cria a coleção de {@link TelefoneAssociacaoDTO} com as informações do
     * {@link HistoricoTitularidadeCotaTelefone} 
     * @param telefones coleção de {@link HistoricoTitularidadeCotaTelefone} para criação
     * da coleção {@link TelefoneAssociacaoDTO}
     * @return coleção de {@link TelefoneAssociacaoDTO}
     */
    public static Collection<TelefoneAssociacaoDTO> toTelefoneAssociacaoDTOCollection(Collection<HistoricoTitularidadeCotaTelefone> telefones) {
        List<TelefoneAssociacaoDTO> dtos = new ArrayList<TelefoneAssociacaoDTO>(telefones.size());
        for (HistoricoTitularidadeCotaTelefone telefone : telefones) {
            TelefoneAssociacaoDTO dto = new TelefoneAssociacaoDTO();
            dto.setPrincipal(telefone.isPrincipal());
            dto.setTipoTelefone(telefone.getTipoTelefone());
            dto.setTelefone(toTelefoneDTO(telefone));
            Long id = Util.generateObjectId(dto);
            dto.setId(id);
            dto.setReferencia(id.intValue());
            dtos.add(dto);
        }
        return dtos;
    }
    
    /**
     * Cria a instância de {@link EnderecoDTO} com as informações
     * da instância de {@link HistoricoTitularidadeCotaEndereco} 
     * @param endereco {@link HistoricoTitularidadeCotaEndereco} com as informações
     * do endereço para a criação do {@link EnderecoDTO}
     * @return Instância criada de {@link EnderecoDTO}
     */
    public static EnderecoDTO toEnderecoDTO(HistoricoTitularidadeCotaEndereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        if(endereco != null) {
        	dto.setTipoLogradouro(endereco.getTipoLogradouro());
            dto.setLogradouro(endereco.getLogradouro());
            dto.setNumero(endereco.getNumero());
            dto.setComplemento(endereco.getComplemento());
            dto.setBairro(endereco.getBairro());
            dto.setCep(endereco.getCep());
            dto.setCidade(endereco.getCidade());
            dto.setCodigoCidadeIBGE(endereco.getCodigoCidadeIBGE());
            dto.setCodigoUf(endereco.getCodigoUf());
            dto.setUf(endereco.getUf());
            dto.setId(Util.generateObjectId(dto));
        }
        return dto;
    }
    
    /**
     * Cria a instância de {@link TelefoneDTO} com as informações
     * da instância de {@link HistoricoTitularidadeCotaTelefone} 
     * @param telefone {@link HistoricoTitularidadeCotaTelefone} com as informações
     * do telefone para a criação do {@link TelefoneDTO}
     * @return Instância criada de {@link TelefoneDTO}
     */
    public static TelefoneDTO toTelefoneDTO(HistoricoTitularidadeCotaTelefone telefone) {
        TelefoneDTO dto = new TelefoneDTO();
        if(telefone != null) {
        	dto.setDdd(telefone.getDdd());
            dto.setNumero(telefone.getNumero());
            dto.setRamal(telefone.getRamal());
            dto.setId(Util.generateObjectId(dto));
        }
        return dto;
    }

    /**
     * Cria a coleção de {@link PdvDTO} com as informações da coleção de
     * {@link HistoricoTitularidadeCotaPDV} 
     * @param pdvs coleção de {@link HistoricoTitularidadeCotaPDV} para criação
     * da coleção de {@link PdvDTO}
     * @return coleção de {@link PdvDTO}
     */
    public static Collection<PdvDTO> toPdvDTOCollection(Collection<HistoricoTitularidadeCotaPDV> pdvs) {
        List<PdvDTO> dtos = new ArrayList<PdvDTO>(pdvs.size());
        for (HistoricoTitularidadeCotaPDV pdv : pdvs) {
            dtos.add(toPdvDTO(pdv));
        }
        return dtos;
    }


    /**
     * Cria o {@link PdvDTO} com as informações da instância de
     * {@link HistoricoTitularidadeCotaPDV}
     * 
     * @param pdv
     *            {@link HistoricoTitularidadeCotaPDV} para a criação do DTO
     * @return {@link PdvDTO} com as informações
     */
    public static PdvDTO toPdvDTO(HistoricoTitularidadeCotaPDV pdv) {
        PdvDTO dto = new PdvDTO();
        dto.setIdCota(pdv.getHistoricoTitularidadeCota().getId());
        dto.setId(pdv.getId());
        dto.setStatusPDV(pdv.getStatus());
        dto.setDataInicio(pdv.getDataInclusao());
        dto.setNomePDV(pdv.getNome());
        dto.setContato(pdv.getContato());
        dto.setSite(pdv.getSite());
        dto.setEmail(pdv.getEmail());
        dto.setPontoReferencia(pdv.getPontoReferencia());
        
        dto.setDentroOutroEstabelecimento(pdv.isDentroOutroEstabelecimento());
        HistoricoTitularidadeCotaCodigoDescricao tipoEstabelecimento = pdv.getTipoEstabelecimentoPDV();
        if (tipoEstabelecimento != null) {
            TipoEstabelecimentoAssociacaoPDVDTO tipoEstabelecimentoDTO = new TipoEstabelecimentoAssociacaoPDVDTO(
                    tipoEstabelecimento.getCodigo(),
                    tipoEstabelecimento.getDescricao());
            dto.setTipoEstabelecimentoAssociacaoPDV(tipoEstabelecimentoDTO);
        }
        
        dto.setArrendatario(pdv.isArrendatario());
        dto.setTamanhoPDV(pdv.getTamanhoPDV());
        dto.setSistemaIPV(pdv.isPossuiSistemaIPV());
        dto.setQtdeFuncionarios(pdv.getQtdeFuncionarios());
        dto.setPorcentagemFaturamento(pdv.getPorcentagemFaturamento());
        HistoricoTitularidadeCotaCodigoDescricao tipoLicencaMunicipal = pdv
                .getTipoLicencaMunicipal();
        if (tipoLicencaMunicipal != null) {
            dto.setTipoLicencaMunicipal(new TipoLicencaMunicipalDTO(
                    tipoLicencaMunicipal.getCodigo(), tipoLicencaMunicipal
                            .getDescricao()));
            dto.setNomeLicenca(pdv.getNomeLicencaMunicipal());
            dto.setNumeroLicenca(pdv.getNumeroLicencaMunicipal());
        }
        
        HistoricoTitularidadeCotaCodigoDescricao tipoPonto = pdv.getTipoPonto();
        if (tipoPonto != null) {
            TipoPontoPDVDTO tipoPontoDTO = new TipoPontoPDVDTO(
                    tipoPonto.getCodigo(), tipoPonto.getDescricao());
            dto.setTipoPontoPDV(tipoPontoDTO);
            dto.setDescricaoTipoPontoPDV(tipoPonto.getDescricao());
        }
        
        CaracteristicasPDV caracteristicas = pdv.getCaracteristicas();
        if (caracteristicas != null) {
            CaracteristicaDTO caracteristicaDTO = new CaracteristicaDTO();
            dto.setPrincipal(caracteristicas.isPontoPrincipal());
            caracteristicaDTO.setPontoPrincipal(caracteristicas.isPontoPrincipal());
            caracteristicaDTO.setBalcaoCentral(caracteristicas
                    .isBalcaoCentral());
            caracteristicaDTO.setTemComputador(caracteristicas
                    .isPossuiComputador());
            caracteristicaDTO.setLuminoso(caracteristicas.isPossuiLuminoso());
            caracteristicaDTO.setTextoLuminoso(caracteristicas
                    .getTextoLuminoso());
            caracteristicaDTO.setPossuiCartao(caracteristicas
                    .isPossuiCartaoCredito());
            
            HistoricoTitularidadeCotaCodigoDescricao areaInfluencia = pdv.getAreaInfluencia();
            if (areaInfluencia != null) {
                caracteristicaDTO.setAreaInfluencia(areaInfluencia.getCodigo());
                caracteristicaDTO.setDescricaoAreaInfluencia(areaInfluencia.getDescricao());
            }
            if (tipoPonto != null) {
                caracteristicaDTO.setTipoPonto(tipoPonto.getCodigo());
            }
            caracteristicaDTO.setTipoCaracteristicaSegmentacaoPDV(pdv.getTipoCaracteristica());
            dto.setCaracteristicaDTO(caracteristicaDTO);
        }

        Collection<HistoricoTitularidadeCotaFuncionamentoPDV> periodos = pdv
                .getPeriodos();
        List<PeriodoFuncionamentoDTO> periodosDTO = new ArrayList<PeriodoFuncionamentoDTO>(
                periodos.size());
        for (HistoricoTitularidadeCotaFuncionamentoPDV periodo : periodos) {
            String inicio = DateUtil.formatarHoraMinuto(periodo
                    .getHorarioInicio());
            String fim = DateUtil.formatarHoraMinuto(periodo.getHorarioFim());
            TipoPeriodoFuncionamentoPDV tipo = periodo
                    .getTipoPeriodoFuncionamentoPDV();
            PeriodoFuncionamentoDTO periodoDTO = new PeriodoFuncionamentoDTO(
                    tipo, inicio, fim);
            periodosDTO.add(periodoDTO);
        }
        dto.setPeriodosFuncionamentoDTO(periodosDTO);

        HistoricoTitularidadeCotaEndereco enderecoPrincipal = pdv
                .getEnderecoPrincipal();
        if (enderecoPrincipal != null) {
            dto.setEndereco(String.format(FORMATO_ENDERECO,
                    enderecoPrincipal.getLogradouro(),
                    enderecoPrincipal.getNumero(),
                    enderecoPrincipal.getBairro(),
                    enderecoPrincipal.getCidade()));
        }

        HistoricoTitularidadeCotaTelefone telefonePrincipal = pdv
                .getTelefonePrincipal();
        if (telefonePrincipal != null) {
            dto.setTelefone(String.format(FORMATO_TELEFONE,
                    telefonePrincipal.getDdd(), telefonePrincipal.getNumero()));
        }
        
        HistoricoTitularidadeCotaCodigoDescricao geradorFluxoPrincipal = pdv.getGeradorFluxoPrincipal();
        if (geradorFluxoPrincipal != null) {
            dto.setGeradorFluxoPrincipal(geradorFluxoPrincipal.getCodigo());
        }
        for (HistoricoTitularidadeCotaCodigoDescricao gfs : pdv.getGeradoresFluxoSecundarios()) {
            dto.addGeradorFluxoSecundario(gfs.getCodigo());
        }
        
        for (HistoricoTitularidadeCotaCodigoDescricao mp : pdv.getMateriais()) {
            dto.addMaterialPromocional(mp.getCodigo());
        }
        
        dto.setExpositor(Util.nvl(pdv.getExpositor(), Boolean.FALSE));
        dto.setTipoExpositor(pdv.getTipoExpositor());
        boolean possuiImagem = pdv.getImagem() != null;
        dto.setPossuiImagem(possuiImagem);
        
        return dto;
    }


    /**
     * Cria a coleção de {@link FornecedorDTO} com as informações da coleção de
     * {@link HistoricoTitularidadeCotaFornecedor} associados ao histórico de
     * titularidade da cota
     * 
     * @param fornecedores
     *            coleção de {@link HistoricoTitularidadeCotaFornecedor} para
     *            criação da coleção de {@link FornecedorDTO}
     * @return coleção de {@link FornecedorDTO}
     */
    public static Collection<FornecedorDTO> toFornecedorDTOCollection(Collection<HistoricoTitularidadeCotaFornecedor> fornecedores) {
        List<FornecedorDTO> dtos = new ArrayList<FornecedorDTO>(fornecedores.size());
        for (HistoricoTitularidadeCotaFornecedor fornecedor : fornecedores) {
            FornecedorDTO dto = new FornecedorDTO();
            dto.setCnpj(fornecedor.getPessoaJuridica().getCnpj());
            dto.setRazaoSocial(fornecedor.getPessoaJuridica().getRazaoSocial());
            dto.setNomeFantasia(fornecedor.getPessoaJuridica().getNomeFantasia());
            dto.setInscricaoEstadual(fornecedor.getPessoaJuridica().getInscricaoEstadual());
            dto.setIdFornecedor(fornecedor.getId());
            dtos.add(dto);
        }
        return dtos;
    }


    /**
     * Cria o {@link ParametroCobrancaCotaDTO} com as informações da 
     * instância de {@link HistoricoTitularidadeCotaFinanceiro}
     * 
     * @param financeiro
     *            {@link HistoricoTitularidadeCotaFinanceiro} para a criação do DTO
     * @return {@link ParametroCobrancaCotaDTO} com as informações
     */
    public static ParametroCobrancaCotaDTO toParametroCobrancaCotaDTO(HistoricoTitularidadeCotaFinanceiro financeiro) {
        ParametroCobrancaCotaDTO dto = new ParametroCobrancaCotaDTO();
        if (financeiro != null) {
            dto.setFatorVencimento(financeiro.getFatorVencimento());
            dto.setValorMinimo(CurrencyUtil.formatarValor(financeiro.getValorMininoCobranca()));
            PoliticaSuspensao politicaSuspensao = financeiro.getPoliticaSuspensao();
            if (politicaSuspensao != null) {
                dto.setQtdDividasAberto(politicaSuspensao.getNumeroAcumuloDivida());
                dto.setVrDividasAberto(CurrencyUtil.formatarValor(politicaSuspensao.getValor()));
                dto.setSugereSuspensao(true);
            }
            dto.setContrato(financeiro.isPossuiContrato() != null ? financeiro.isPossuiContrato() : false);
            dto.setInicioContrato(financeiro.getDataInicioContrato());
            dto.setTerminoContrato(financeiro.getDataTerminoContrato());
            dto.setContratoRecebido(financeiro.isContratoRecebido() != null ? financeiro.isContratoRecebido() : false);
            dto.setTipoCota(financeiro.getTipoCota());
        }
        return dto;
    }

    /**
     * Cria a coleção de {@link FormaCobrancaDTO} com as informações da coleção de
     * {@link HistoricoTitularidadeCotaFormaPagamento} associados ao histórico de
     * titularidade da cota
     * 
     * @param formasPagamento
     *            coleção de {@link HistoricoTitularidadeCotaFormaPagamento} para
     *            criação da coleção de {@link FormaCobrancaDTO}
     * @return coleção de {@link FormaCobrancaDTO} com as informações
     */
    public static Collection<FormaCobrancaDTO> toFormaCobrancaDTOCollection(
            Collection<HistoricoTitularidadeCotaFormaPagamento> formasPagamento) {
        List<FormaCobrancaDTO> dtos = new ArrayList<FormaCobrancaDTO>(formasPagamento.size());
        for (HistoricoTitularidadeCotaFormaPagamento formaPagto : formasPagamento) {
            FormaCobrancaDTO dto = toFormaCobrancaDTO(formaPagto);
            dtos.add(dto);
        }
        return dtos;
    }


    /**
     * Cria o {@link FormaCobrancaDTO} com as informações da 
     * instância de {@link HistoricoTitularidadeCotaFormaPagamento}
     * 
     * @param formaPagto instância de
     *            {@link HistoricoTitularidadeCotaFormaPagamento} para a criação do DTO
     * @return {@link FormaCobrancaDTO} com as informações
     */
    public static FormaCobrancaDTO toFormaCobrancaDTO(HistoricoTitularidadeCotaFormaPagamento formaPagto) {
        FormaCobrancaDTO dto = new FormaCobrancaDTO();
        dto.setTipoCobranca(formaPagto.getTipoCobranca());
        dto.setIdFormaCobranca(formaPagto.getId());
        HistoricoTitularidadeCotaBanco banco = formaPagto.getBanco();
        if (banco != null) {
            dto.setAgencia(banco.getAgencia());
            dto.setAgenciaDigito(banco.getDvAgencia());
            dto.setConta(banco.getConta());
            dto.setContaDigito(banco.getDvConta());
            dto.setNomeBanco(banco.getNome());
            dto.setNumBanco(banco.getNumeroBanco());
            dto.setDetalhesTipoPagto(String.format("%s : %s : %s - %s",
                    banco.getNome(), banco.getAgencia(), banco.getConta(),
                    Util.nvl(banco.getDvConta(), "")));
        }
        HistoricoTitularidadeCotaConcentracaoCobranca concentracao = formaPagto.getConcentracaoCobranca();
        if (concentracao != null) {
            dto.setTipoFormaCobranca(concentracao.getTipoFormaCobranca());
            Collection<Integer> diasMes = concentracao.getDiasMes();
            List<DiaSemana> diasSemana = new ArrayList<DiaSemana>(concentracao.getDiasSemana());
            Collections.sort(diasSemana);
            if (TipoFormaCobranca.SEMANAL == dto.getTipoFormaCobranca()) {
                List<String> concentracaoes = new ArrayList<String>();
                for (DiaSemana diaSemana : diasSemana) {
                    if (DiaSemana.DOMINGO == diaSemana) {
                        dto.setDomingo(true);
                    } else if (DiaSemana.SEGUNDA_FEIRA == diaSemana) {
                        dto.setSegunda(true);
                    } else if (DiaSemana.TERCA_FEIRA == diaSemana) {
                        dto.setTerca(true);
                    } else if (DiaSemana.QUARTA_FEIRA == diaSemana) {
                        dto.setQuarta(true);
                    } else if (DiaSemana.QUINTA_FEIRA == diaSemana) {
                        dto.setQuinta(true);
                    } else if (DiaSemana.SEXTA_FEIRA == diaSemana) {
                        dto.setSexta(true);
                    } else if (DiaSemana.SABADO == diaSemana) {
                        dto.setSabado(true);
                    }
                    concentracaoes.add(diaSemana.getDescricaoDiaSemana());
                    dto.setConcentracaoPagto(StringUtils.join(concentracaoes, " / "));
                }
            } else if (TipoFormaCobranca.QUINZENAL == dto.getTipoFormaCobranca()) {
                if (diasMes != null && !diasMes.isEmpty()) {
                    Iterator<Integer> iterator = diasMes.iterator();
                    Integer dia1 = iterator.next();
                    Integer dia2 = iterator.hasNext() ? iterator.next() : null;
                    dto.setPrimeiroDiaQuinzenal(dia1);
                    dto.setSegundoDiaQuinzenal(dia2);
                    dto.setConcentracaoPagto(String.format("Todo dia %s e %s", dia1, dia2));
                }
            } else if (TipoFormaCobranca.MENSAL == dto.getTipoFormaCobranca()) {
                if (diasMes != null && !diasMes.isEmpty()) {
                    Integer dia = diasMes.iterator().next();
                    dto.setDiaDoMes(dia);
                    dto.setConcentracaoPagto(String.format("Todo dia %s", dia));
                }
            } else if (TipoFormaCobranca.DIARIA == dto.getTipoFormaCobranca()) {
                dto.setConcentracaoPagto("Diariamente");
            }
        }
        List<String> fornecedores = new ArrayList<String>();
        for (HistoricoTitularidadeCotaFornecedor fornecedor : formaPagto.getFornecedores()) {
            dto.addIdFornecedor(fornecedor.getId());
            fornecedores.add(fornecedor.getPessoaJuridica().getRazaoSocial());
        }
        dto.setFornecedor(StringUtils.join(fornecedores, " / "));
        return dto;
    }


    /**
     * Cria o {@link DistribuicaoDTO} com as informações da 
     * instância de {@link HistoricoTitularidadeCotaDistribuicao}
     * 
     * @param distribuicao instância de
     *            {@link HistoricoTitularidadeCotaDistribuicao} para a criação do DTO
     * @return {@link DistribuicaoDTO} com as informações
     */
    public static DistribuicaoDTO toDistribuicaoDTO(HistoricoTitularidadeCotaDistribuicao distribuicao) {
        DistribuicaoDTO dto = new DistribuicaoDTO();
        
        dto.setNumCota(distribuicao.getHistoricoTitularidadeCota().getNumeroCota());
        dto.setBox(distribuicao.getHistoricoTitularidadeCota().getBox());
        dto.setQtdePDV(distribuicao.getQtdePDV());
        dto.setAssistComercial(distribuicao.getAssistenteComercial());
        dto.setGerenteComercial(distribuicao.getGerenteComercial());
        dto.setObservacao(distribuicao.getObservacao());
        dto.setRepPorPontoVenda(distribuicao.getEntregaReparteVenda());
        dto.setSolNumAtras(distribuicao.getSolicitaNumAtrasados());
        dto.setRecebeRecolhe(distribuicao.getRecebeRecolheParcias());
        
        dto.setSlipEmail(distribuicao.getSlipEmail());
        dto.setSlipImpresso(distribuicao.getSlipImpresso());
        
        dto.setBoletoImpresso(distribuicao.getBoletoImpresso());
        dto.setBoletoEmail(distribuicao.getBoletoEmail());
        
        dto.setBoletoSlipEmail(distribuicao.getBoletoSlipEmail());
        dto.setBoletoSlipImpresso(distribuicao.getBoletoSlipImpresso());
        
        dto.setReciboEmail(distribuicao.getReciboEmail());
        dto.setReciboImpresso(distribuicao.getReciboImpresso());
        
        dto.setNeEmail(distribuicao.getNotaEnvioEmail());
        dto.setNeImpresso(distribuicao.getNotaEnvioImpresso());
        
        dto.setCeEmail(distribuicao.getChamadaEncalheEmail());
        dto.setCeImpresso(distribuicao.getChamadaEncalheImpresso());
        
        DescricaoTipoEntrega tipoEntrega = distribuicao.getTipoEntrega();
        dto.setDescricaoTipoEntrega(tipoEntrega);
        dto.setPercentualFaturamento(distribuicao.getPercentualFaturamentoEntrega());
        dto.setTaxaFixa(distribuicao.getTaxaFixaEntrega());
        
        dto.setInicioPeriodoCarencia(DateUtil.formatarDataPTBR(distribuicao.getInicioPeriodoCarencia()));
        dto.setFimPeriodoCarencia(DateUtil.formatarDataPTBR(distribuicao.getFimPeriodoCarencia()));
        
        dto.setUtilizaProcuracao(distribuicao.getPossuiProcuracao());
        dto.setProcuracaoRecebida(distribuicao.getProcuracaoAssinada());
        dto.setUtilizaTermoAdesao(distribuicao.getPossuiTermoAdesao());
        dto.setTermoAdesaoRecebido(distribuicao.getTermoAdesaoAssinado());
        
        if (tipoEntrega != null) {
            dto.addTipoEntrega(tipoEntrega);
        }
        
        return dto;
    }


    /**
     * Cria o {@link FiadorDTO} com as informações da 
     * instância de {@link HistoricoTitularidadeCotaFiador}
     * 
     * @param fiador instância de
     *            {@link HistoricoTitularidadeCotaFiador} para a criação do DTO
     * @return {@link FiadorDTO} com as informações
     */
    public static FiadorDTO toFiadorDTO(HistoricoTitularidadeCotaFiador fiador) {
        FiadorDTO dto = new FiadorDTO();
        dto.setDocumento(fiador.getCpfCnpj());
        dto.setNome(fiador.getNome());
        dto.setEnderecoPrincipal(toEnderecoDTO(fiador.getHistoricoTitularidadeCotaEndereco()));
        dto.setTelefonePrincipal(toTelefoneDTO(fiador.getHistoricoTitularidadeCotaTelefone()));
        for (HistoricoTitularidadeCotaFiadorGarantia garantia : fiador.getGarantias()) {
            dto.addGarantia(garantia.getDescricao(), garantia.getValor());
        }
        return dto;
    }

    /**
     * Retorna o DTO com as informações de garantia para o tipo de garantia
     * fiador do histórico de titularidade da cota
     * 
     * @param garantiaFiador
     *            garantia fiador do histórico de titularidade da cota
     * @return DTO com as informações da garantia do tipo fiador do histórico de
     *         titularidade da cota
     */
    public static CotaGarantiaDTO<FiadorDTO> toCotaGarantiaDTO(HistoricoTitularidadeCotaFiador garantiaFiador) {
        CotaGarantiaDTO<FiadorDTO> dto = new CotaGarantiaDTO<FiadorDTO>();
        dto.setTipo(garantiaFiador.getTipoGarantia());
        dto.setCotaGarantia(toFiadorDTO(garantiaFiador));
        return dto;
    }


    /**
     * Cria o {@link ChequeCaucaoDTO} com as informações da 
     * instância de {@link HistoricoTitularidadeCotaChequeCaucao}
     * 
     * @param garantiaChequeCaucao instância de
     *            {@link HistoricoTitularidadeCotaChequeCaucao} para a criação do DTO
     * @return {@link ChequeCaucaoDTO} com as informações
     */
    public static ChequeCaucaoDTO toChequeCaucaoDTO(HistoricoTitularidadeCotaChequeCaucao garantiaChequeCaucao) {
        ChequeCaucaoDTO dto = new ChequeCaucaoDTO();
        dto.setAgencia(garantiaChequeCaucao.getAgencia());
        dto.setConta(garantiaChequeCaucao.getConta());
        dto.setCorrentista(garantiaChequeCaucao.getCorrentista());
        dto.setDvAgencia(garantiaChequeCaucao.getDvAgencia());
        dto.setDvConta(garantiaChequeCaucao.getDvConta());
        dto.setEmissao(garantiaChequeCaucao.getEmissao());
        dto.setNomeBanco(garantiaChequeCaucao.getNomeBanco());
        dto.setNumeroBanco(garantiaChequeCaucao.getNumeroBanco());
        dto.setNumeroCheque(garantiaChequeCaucao.getNumeroCheque());
        dto.setValidade(garantiaChequeCaucao.getValidade());
        dto.setValor(garantiaChequeCaucao.getValor());
        return dto;
    }

    /**
     * Retorna o DTO com as informações de garantia para o tipo de garantia
     * cheque caução do histórico de titularidade da cota
     * 
     * @param garantiaChequeCaucao
     *            garantia cheque caução do histórico de titularidade da cota
     * @return DTO com as informações da garantia do tipo cheque caução do histórico de
     *         titularidade da cota
     */
    public static CotaGarantiaDTO<ChequeCaucaoDTO> toCotaGarantiaDTO(HistoricoTitularidadeCotaChequeCaucao garantiaChequeCaucao) {
        CotaGarantiaDTO<ChequeCaucaoDTO> dto = new CotaGarantiaDTO<ChequeCaucaoDTO>();
        dto.setTipo(garantiaChequeCaucao.getTipoGarantia());
        dto.setCotaGarantia(toChequeCaucaoDTO(garantiaChequeCaucao));
        return dto;
    }

    /**
     * Cria o DTO {@link CotaGarantiaDTO} com as informações da coleção de
     * {@link HistoricoTitularidadeCotaImovel} associados ao histórico de
     * titularidade da cota
     * 
     * @param garantiasImovel
     *            coleção de {@link HistoricoTitularidadeCotaImovel} para
     *            criação do DTO {@link CotaGarantiaDTO}
     * @return coleção de {@link CotaGarantiaDTO} com as informações
     */
    public static CotaGarantiaDTO<Collection<ImovelDTO>> toCotaGarantiaDTOImovel(
            Collection<HistoricoTitularidadeCotaImovel> garantiasImovel) {
        CotaGarantiaDTO<Collection<ImovelDTO>> dto = new CotaGarantiaDTO<Collection<ImovelDTO>>();
        dto.setTipo(TipoGarantia.IMOVEL);
        List<ImovelDTO> imoveis = new ArrayList<ImovelDTO>(
                garantiasImovel.size());
        for (HistoricoTitularidadeCotaImovel imovel : garantiasImovel) {
            imoveis.add(new ImovelDTO(imovel.getProprietario(), imovel
                    .getEndereco(), imovel.getNumeroRegistro(), imovel
                    .getValor(), imovel.getObservacao()));
        }
        dto.setCotaGarantia(imoveis);
        return dto;
    }


    /**
     * Retorna o DTO com as informações de garantia para o tipo de garantia
     * nota promissória do histórico de titularidade da cota
     * 
     * @param garantiaNotaPromissoria
     *            garantia nota promissória do histórico de titularidade da cota
     * @return DTO com as informações da garantia do tipo nota promissória do histórico de
     *         titularidade da cota
     */
    public static CotaGarantiaDTO<GarantiaNotaPromissoriaDTO> toCotaGarantiaDTO(
            HistoricoTitularidadeCotaNotaPromissoria garantiaNotaPromissoria) {
        CotaGarantiaDTO<GarantiaNotaPromissoriaDTO> dto = new CotaGarantiaDTO<GarantiaNotaPromissoriaDTO>();
        dto.setTipo(garantiaNotaPromissoria.getTipoGarantia());
        dto.setCotaGarantia(new GarantiaNotaPromissoriaDTO(
                garantiaNotaPromissoria.getVencimento(),
                garantiaNotaPromissoria.getValor(), garantiaNotaPromissoria
                        .getValorExtenso()));
        return dto;
    }


    /**
     * Retorna o DTO com as informações de garantia para o tipo de garantia
     * caução líquida do histórico de titularidade da cota
     * 
     * @param garantiaCaucaoLiquida
     *            garantia caução líquida do histórico de titularidade da cota
     * @return DTO com as informações da garantia do tipo caução líquida do histórico de
     *         titularidade da cota
     */
    public static CotaGarantiaDTO<FormaCobrancaCaucaoLiquidaDTO> toCotaGarantiaDTO(HistoricoTitularidadeCotaCaucaoLiquida garantiaCaucaoLiquida) {
        CotaGarantiaDTO<FormaCobrancaCaucaoLiquidaDTO> dto = new CotaGarantiaDTO<FormaCobrancaCaucaoLiquidaDTO>();
        dto.setTipo(garantiaCaucaoLiquida.getTipoGarantia());
        dto.setCotaGarantia(toFormaCobrancaCaucaoLiquidaDTO(garantiaCaucaoLiquida));       
        return dto;
    }


    /**
     * Retorna o DTO com as informações de garantia para o tipo de garantia
     * caução líquida do histórico de titularidade da cota
     * 
     * @param garantiaCaucaoLiquida
     *            garantia caução líquida do histórico de titularidade da cota
     * @return DTO com as informações da garantia do tipo caução líquida do
     *         histórico de titularidade da cota
     */
    public static FormaCobrancaCaucaoLiquidaDTO toFormaCobrancaCaucaoLiquidaDTO(HistoricoTitularidadeCotaCaucaoLiquida garantiaCaucaoLiquida) {
        FormaCobrancaCaucaoLiquidaDTO dto = new FormaCobrancaCaucaoLiquidaDTO();
        dto.setValor(garantiaCaucaoLiquida.getValor());

        ContaBancariaDeposito contaDeposito = garantiaCaucaoLiquida.getContaBancariaDeposito();
        if (contaDeposito != null) {
            dto.setAgencia(contaDeposito.getAgencia());
            dto.setAgenciaDigito(contaDeposito.getDvAgencia());
            dto.setConta(contaDeposito.getConta());
            dto.setContaDigito(contaDeposito.getDvConta());
            dto.setNomeBanco(contaDeposito.getNomeBanco());
            dto.setNumBanco(contaDeposito.getNumeroBanco());
            dto.setNomeCorrentista(contaDeposito.getNomeCorrentista());
        }
        
        for (HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida atualizacao : garantiaCaucaoLiquida.getAtualizacoes()) {
            dto.addCaucaoLiquida(atualizacao.getAtualizacao(), atualizacao.getValor());
        }
        
        HistoricoTitularidadeCotaPagamentoCaucaoLiquida pagamento = garantiaCaucaoLiquida.getPagamento();
        if (pagamento != null) {
            dto.setTipoCobranca(pagamento.getTipoCobranca());
            if (TipoCobrancaCotaGarantia.BOLETO == pagamento.getTipoCobranca()) {
                dto.setQtdeParcelas(pagamento.getQtdeParcelasBoleto());
                dto.setValorParcela(pagamento.getValorParcelasBoleto());
                dto.setTipoFormaCobranca(pagamento.getPeriodicidadeBoleto());
                Collection<Integer> diasMes = pagamento.getDiasMesBoleto();
                if (TipoFormaCobranca.SEMANAL == pagamento.getPeriodicidadeBoleto()) {
                    for (DiaSemana diaSemana : pagamento.getDiasSemanaBoleto()) {
                        if (DiaSemana.DOMINGO == diaSemana) {
                            dto.setDomingo(true);
                        } else if (DiaSemana.SEGUNDA_FEIRA == diaSemana) {
                            dto.setSegunda(true);   
                        } else if (DiaSemana.TERCA_FEIRA == diaSemana) {
                            dto.setTerca(true);   
                        } else if (DiaSemana.QUARTA_FEIRA == diaSemana) {
                            dto.setQuarta(true);   
                        } else if (DiaSemana.QUINTA_FEIRA == diaSemana) {
                            dto.setQuinta(true);   
                        } else if (DiaSemana.SEXTA_FEIRA == diaSemana) {
                            dto.setSexta(true);   
                        } else if (DiaSemana.SABADO == diaSemana) {
                            dto.setSabado(true);   
                        }
                    }
                } else if (TipoFormaCobranca.QUINZENAL == pagamento.getPeriodicidadeBoleto()) {
                    if (diasMes != null && !diasMes.isEmpty()) {
                        Iterator<Integer> iterator = diasMes.iterator();
                        Integer dia1 = iterator.next();
                        Integer dia2 = iterator.hasNext() ? iterator.next() : null;
                        dto.setPrimeiroDiaQuinzenal(dia1);
                        dto.setSegundoDiaQuinzenal(dia2);
                    }
                } else if (TipoFormaCobranca.MENSAL == pagamento.getPeriodicidadeBoleto()) {
                    if (diasMes != null && !diasMes.isEmpty()) {
                        Integer dia = diasMes.iterator().next();
                        dto.setDiaDoMes(dia);
                    }
                }
            } else if (TipoCobrancaCotaGarantia.DESCONTO_COTA == pagamento.getTipoCobranca()) {
                dto.setValorDescontoAtual(pagamento.getDescontoNormal());
                dto.setUtilizarDesconto(pagamento.getPorcentagemUtilizada());
                dto.setDescontoCotaDesconto(pagamento.getDescontoReduzido());
            }
        }
        return dto;
    }

    /**
     * Cria o DTO {@link CotaGarantiaDTO} com as informações da coleção de
     * {@link HistoricoTitularidadeCotaOutros} associados ao histórico de
     * titularidade da cota
     * 
     * @param garantiasOutros
     *            coleção de {@link HistoricoTitularidadeCotaOutros} para
     *            criação do DTO {@link CotaGarantiaDTO}
     * @return DTO {@link CotaGarantiaDTO} com as informações
     */
    public static CotaGarantiaDTO<Collection<GarantiaOutrosDTO>> toCotaGarantiaDTOOutros(
            Collection<HistoricoTitularidadeCotaOutros> garantiasOutros) {
        CotaGarantiaDTO<Collection<GarantiaOutrosDTO>> dto = new CotaGarantiaDTO<Collection<GarantiaOutrosDTO>>();
        dto.setTipo(TipoGarantia.OUTROS);
        List<GarantiaOutrosDTO> outrosDTO = new ArrayList<GarantiaOutrosDTO>(
                garantiasOutros.size());
        for (HistoricoTitularidadeCotaOutros outros : garantiasOutros) {
            outrosDTO.add(new GarantiaOutrosDTO(outros.getDescricao(), outros
                    .getValor(), outros.getValidade()));
        }
        dto.setCotaGarantia(outrosDTO);
        return dto;
    }
 
}
