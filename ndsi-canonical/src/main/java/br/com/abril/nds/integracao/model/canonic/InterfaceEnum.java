package br.com.abril.nds.integracao.model.canonic;

public enum InterfaceEnum {

	EMS0111(111L, EMS0111Input.class),
	EMS0110(110L, EMS0110Input.class),
	EMS0114(114L, EMS0114Input.class);
	
	private Long codigoInterface;
	private Class<? extends IntegracaoDocument> classeLinha;
	private Class<? extends IntegracaoDocument> classeHeader;
	private Class<? extends IntegracaoDocument> classeTrailer;
	
	
	private InterfaceEnum(Long codigoInterface, Class<? extends IntegracaoDocument> classeLinha) {
		this.codigoInterface = codigoInterface;
		this.classeLinha = classeLinha;
	}
	
	private InterfaceEnum(Long codigoInterface, Class<? extends IntegracaoDocument> classeLinha, Class<? extends IntegracaoDocument> classeHeader, Class<? extends IntegracaoDocument> classeTrailer) {
		this.codigoInterface = codigoInterface;
		this.classeLinha = classeLinha;
		this.classeHeader = classeHeader;
		this.classeTrailer = classeTrailer;
	}
	
	public InterfaceEnum getByCodigo(Long codigoInterface) {
		
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

	public Class<?> getClasseHeader() {
		return classeHeader;
	}

	public Class<?> getClasseTrailer() {
		return classeTrailer;
	}
}
