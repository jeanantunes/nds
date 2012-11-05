package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO com as informações dos vários documentos "páginas" que compõe
 * a chamade de encalhe do fornecedor
 * 
 * @author francisco.garcia
 *
 */
public class ChamadasEncalheFornecedorDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private PessoaJuridicaChamadaEncalheFornecedorDTO fornecedor;
    
    private PessoaJuridicaChamadaEncalheFornecedorDTO distribuidor;
    
    private IdentificacaoChamadaEncalheFornecedorDTO identificacao;
    
    private List<ChamadaEncalheFornecedorDTO> documentos = new ArrayList<ChamadaEncalheFornecedorDTO>();
    
    private ResumoChamadaEncalheFornecedorDTO resumo;

    public ChamadasEncalheFornecedorDTO(
            PessoaJuridicaChamadaEncalheFornecedorDTO fornecedor,
            PessoaJuridicaChamadaEncalheFornecedorDTO distribuidor, IdentificacaoChamadaEncalheFornecedorDTO identificacao) {
        this.fornecedor = fornecedor;
        this.distribuidor = distribuidor;
        this.identificacao = identificacao;
        this.resumo = newResumo();
    }

    /**
     * @return the fornecedor
     */
    public PessoaJuridicaChamadaEncalheFornecedorDTO getFornecedor() {
        return fornecedor;
    }

    /**
     * @return the distribuidor
     */
    public PessoaJuridicaChamadaEncalheFornecedorDTO getDistribuidor() {
        return distribuidor;
    }

    /**
     * @return the identificacao
     */
    public IdentificacaoChamadaEncalheFornecedorDTO getIdentificacao() {
        return identificacao;
    }

    /**
     * @return the documentos
     */
    public List<ChamadaEncalheFornecedorDTO> getDocumentos() {
        return documentos;
    }

    /**
     * @return the resumo
     */
    public ResumoChamadaEncalheFornecedorDTO getResumo() {
        return resumo;
    }
    
    public void addDocumento(ChamadaEncalheFornecedorDTO chamadaEncalhe) {
        if (documentos == null) {
            documentos = new ArrayList<ChamadaEncalheFornecedorDTO>();
        }
        documentos.add(chamadaEncalhe);
    }
    
    public ChamadaEncalheFornecedorDTO newDocumento() {
        ChamadaEncalheFornecedorDTO documento = new ChamadaEncalheFornecedorDTO(fornecedor, distribuidor, identificacao);
        addDocumento(documento);
        return documento;
    }
    
    public ResumoChamadaEncalheFornecedorDTO newResumo() {
        ResumoChamadaEncalheFornecedorDTO resumo = new ResumoChamadaEncalheFornecedorDTO(fornecedor, distribuidor, identificacao);
        this.resumo = resumo;
        return resumo;
    }
    
    public ChamadaEncalheFornecedorDTO getDocumento(Long numeroDocumento) {
        for (ChamadaEncalheFornecedorDTO documento : documentos) {
            if (documento.getNumeroDocumento().equals(numeroDocumento)) {
                return documento;
            }
        }
        return null;
    }

}
