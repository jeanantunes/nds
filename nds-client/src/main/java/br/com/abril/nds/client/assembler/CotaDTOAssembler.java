package br.com.abril.nds.client.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.abril.nds.dto.CaracteristicaDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.TipoEstabelecimentoAssociacaoPDVDTO;
import br.com.abril.nds.dto.TipoLicencaMunicipalDTO;
import br.com.abril.nds.dto.TipoPontoPDVDTO;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCodigoDescricao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaEndereco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFuncionamentoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaFisica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaJuridica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaTelefone;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;


/**
 * Classe utilitária para auxílio na 
 * criação dos DTO's com informações da Cota
 * 
 * @author francisco.garcia
 *
 */
public class CotaDTOAssembler {
    
    private static final String FORMATO_ENDERECO = "%s,%s-%s-%s";
    private static final String FORMATO_TELEFONE = "%s-%s";
    
    private CotaDTOAssembler() {
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
        dto.setEmiteNFE(cota.isEmiteNfe());
        dto.setEmailNF(cota.getEmailNfe());
        
        if (cota.isPessoaFisica()) {
            HistoricoTitularidadeCotaPessoaFisica pf = cota.getPessoaFisica();
            dto.setNomePessoa(pf.getNome());
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
        dto.setTipoLogradouro(endereco.getTipoLogradouro());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCodigoBairro(endereco.getCodigoBairro());
        dto.setCep(endereco.getCep());
        dto.setCidade(endereco.getCidade());
        dto.setCodigoCidadeIBGE(endereco.getCodigoCidadeIBGE());
        dto.setCodigoUf(endereco.getCodigoUf());
        dto.setUf(endereco.getUf());
        dto.setId(Util.generateObjectId(dto));
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
        dto.setDdd(telefone.getDdd());
        dto.setNumero(telefone.getNumero());
        dto.setRamal(telefone.getRamal());
        dto.setId(Util.generateObjectId(dto));
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
        
        return dto;
    }
 
}
