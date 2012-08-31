package br.com.abril.nds.client.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaEndereco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaFisica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaJuridica;


/**
 * Classe utilitária para auxílio na 
 * criação dos DTO's com informações da Cota
 * 
 * @author francisco.garcia
 *
 */
public class CotaDTOAssembler {
    
    private CotaDTOAssembler() {
    }
    
    
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
    
    public static Collection<EnderecoAssociacaoDTO> toEnderecoAssociacaoDTOCollcetion(Collection<HistoricoTitularidadeCotaEndereco> enderecos) {
        List<EnderecoAssociacaoDTO> dtos = new ArrayList<EnderecoAssociacaoDTO>(enderecos.size());
        for (HistoricoTitularidadeCotaEndereco endereco : enderecos) {
            EnderecoAssociacaoDTO dto = new EnderecoAssociacaoDTO();
            dto.setEnderecoPrincipal(endereco.isPrincipal());
            dto.setTipoEndereco(endereco.getTipoEndereco());
            dto.setEndereco(toEnderecoDTO(endereco));
        }
        return dtos;
    }
    
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
        return dto;
    }

}
