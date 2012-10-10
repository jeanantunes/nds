package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.Fornecedor;

/**
 * CE de Devolução ao fornecedor
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "CE_DEVOLUCAO_FORNECEDOR")
@SequenceGenerator(name="CE_DEVOLUCAO_FORNECEDOR_SEQ", initialValue = 1, allocationSize = 1)
public class CEDevolucaoFornecedor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "CE_DEVOLUCAO_FORNECEDOR_SEQ")
    @Column(name = "ID")
    private Long id;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_DEVOLUCAO")
    private Date dataDevolucao;
    
    @ManyToOne
    @JoinColumn(name = "FORNECEDOR_ID")
    private Fornecedor fornecedor;
    
    @OneToMany(mappedBy = "ceDevolucao")
    @Cascade(value = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE})
    private List<ConferenciaEncalheParcial> conferencias = new ArrayList<ConferenciaEncalheParcial>();

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the dataDevolucao
     */
    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    /**
     * @param dataDevolucao the dataDevolucao to set
     */
    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    /**
     * @return the fornecedor
     */
    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @return the conferencias
     */
    public List<ConferenciaEncalheParcial> getConferencias() {
        return conferencias;
    }

    /**
     * @param conferencias the conferencias to set
     */
    public void setConferencias(List<ConferenciaEncalheParcial> conferencias) {
        this.conferencias = conferencias;
    }
    
    /**
     *  Adiciona uma nova conferência à CE de Devolucao
     */
    public void addConferencia(ConferenciaEncalheParcial conferencia) {
        if (this.conferencias == null) {
            this.conferencias = new ArrayList<ConferenciaEncalheParcial>();
        }
        conferencia.setCeDevolucao(this);
        this.conferencias.add(conferencia);
    }
    

}
