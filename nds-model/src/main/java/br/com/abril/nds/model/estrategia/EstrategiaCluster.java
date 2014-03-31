package br.com.abril.nds.model.estrategia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.integracao.StatusIntegracaoNFE;

/**
 * Chamada de Encalhe do Fornecedor para retorno
 * dos produtos recolhidos pelo Distribuidor
 * 
 */

public class EstrategiaCluster implements Serializable {

    private static final long serialVersionUID = 1L;
 
}
