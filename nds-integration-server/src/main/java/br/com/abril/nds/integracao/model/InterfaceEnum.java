package br.com.abril.nds.integracao.model;

import br.com.abril.nds.integracao.model.canonic.EMS0109Input;
import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.integracao.model.canonic.EMS0111Input;
import br.com.abril.nds.integracao.model.canonic.EMS0113Input;
import br.com.abril.nds.integracao.model.canonic.EMS0114Input;
import br.com.abril.nds.integracao.model.canonic.EMS0125Input;
import br.com.abril.nds.integracao.model.canonic.EMS0126Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocumentDetail;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocumentMaster;
import br.com.abril.nds.integracao.model.canonic.TipoInterfaceEnum;


public enum InterfaceEnum {

	EMS0109(109L, null, EMS0109Input.class),
	EMS0110(110L, null, EMS0110Input.class),
	EMS0111(111L, null, EMS0111Input.class),
	EMS0113(113L, null, EMS0113Input.class),
	EMS0114(114L, null, EMS0114Input.class),
	EMS0125(125L, null, EMS0125Input.class),
	EMS0126(126L, null, EMS0126Input.class),
	EMS0127(127L, null, null, TipoInterfaceEnum.DB),
	EMS0128(128L, null, EMS0128Input.class, TipoInterfaceEnum.DB),
	EMS0134(134L, null, null),
	EMS0138(128L, null, null, TipoInterfaceEnum.DB),
	EMS0185(185L, null, null);

	private Long codigoInterface;
	private Integer tamanhoLinha;
	private Class<? extends IntegracaoDocument> classeLinha;
	private Class<? extends IntegracaoDocument> classeHeader;
	private Class<? extends IntegracaoDocument> classeTrailer;

	private Class<? extends IntegracaoDocumentDetail> classeDetail;
	private Class<? extends IntegracaoDocumentMaster<? extends IntegracaoDocumentDetail>> classeMaster;

	private TipoInterfaceEnum tipoInterfaceEnum;
	
	private InterfaceEnum(Long codigoInterface, Integer tamanhoLinha, Class<? extends IntegracaoDocument> classeLinha, TipoInterfaceEnum tipoInterfaceEnum) {
		this.codigoInterface = codigoInterface;
		this.classeLinha = classeLinha;
		this.setTipoInterfaceEnum(tipoInterfaceEnum); 		
	}
	
	private InterfaceEnum(Long codigoInterface, Integer tamanhoLinha, Class<? extends IntegracaoDocument> classeLinha) {
		this.codigoInterface = codigoInterface;
		this.classeLinha = classeLinha;
		this.setTipoInterfaceEnum(TipoInterfaceEnum.SIMPLES); 
	}

	private InterfaceEnum(Long codigoInterface, Integer tamanhoLinha, Class<? extends IntegracaoDocumentDetail> classeDetail, Class<? extends IntegracaoDocumentMaster<? extends IntegracaoDocumentDetail>> classeMaster , TipoInterfaceEnum tipoInterfaceEnum ) {
		this.codigoInterface = codigoInterface;
		this.classeDetail = classeDetail;
		this.classeMaster = classeMaster;
		this.setTipoInterfaceEnum(tipoInterfaceEnum); 
	}

	
	private InterfaceEnum(Long codigoInterface, Integer tamanhoLinha, Class<? extends IntegracaoDocument> classeLinha, Class<? extends IntegracaoDocument> classeHeader, Class<? extends IntegracaoDocument> classeTrailer) {
		this.codigoInterface = codigoInterface;
		this.classeLinha = classeLinha;
		this.classeHeader = classeHeader;
		this.classeTrailer = classeTrailer;
		this.setTipoInterfaceEnum(TipoInterfaceEnum.HEADER_LINHA_TRAILER); 

	}
	
	public static InterfaceEnum getByCodigo(Long codigoInterface) {
		
		for (InterfaceEnum ems: InterfaceEnum.values()) {
			if (ems.codigoInterface.longValue() == codigoInterface.longValue()) {
				return ems;
			}
		}
		
		return null;
	}

	public Long getCodigoInterface() {
		return codigoInterface;
	}

	public Class<?> getClasseLinha() {
		return classeLinha;
	}

	public Class<?> getClasseDetail() {
		return classeDetail;
	}
	
	public Class<?> getClasseMaster() {
		return classeMaster;
	}
	
	public Class<?> getClasseHeader() {
		return classeHeader;
	}

	public Class<?> getClasseTrailer() {
		return classeTrailer;
	}
	
	public Integer getTamanhoLinha() {
		return this.tamanhoLinha;
	}

	public TipoInterfaceEnum getTipoInterfaceEnum() {
		return tipoInterfaceEnum;
	}

	public void setTipoInterfaceEnum(TipoInterfaceEnum tipoInterfaceEnum) {
		this.tipoInterfaceEnum = tipoInterfaceEnum;
	}
}