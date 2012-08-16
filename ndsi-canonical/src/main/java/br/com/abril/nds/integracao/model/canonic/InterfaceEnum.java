package br.com.abril.nds.integracao.model.canonic;

public enum InterfaceEnum {

	EMS0109(109L, null, EMS0109Input.class),
	EMS0110(110L, null, EMS0110Input.class),
	EMS0111(111L, null, EMS0111Input.class),
	EMS0113(113L, null, EMS0113Input.class),
	EMS0114(114L, null, EMS0114Input.class),
	EMS0119(119L, null, EMS0119Input.class),
	EMS0125(125L, null, EMS0125Input.class),
	EMS0126(126L, null, EMS0126Input.class),
	EMS0112(112L, null, EMS0112Input.class),
	EMS0134(134L, null, null),
	EMS0185(185L, null, null),
	EMS0135(135L, null, EMS0135Input.class);
	
	
	private Long codigoInterface;
	private Integer tamanhoLinha;
	private Class<? extends IntegracaoDocument> classeLinha;
	private Class<? extends IntegracaoDocument> classeHeader;
	private Class<? extends IntegracaoDocument> classeTrailer;
	
	
	private InterfaceEnum(Long codigoInterface, Integer tamanhoLinha, Class<? extends IntegracaoDocument> classeLinha) {
		this.codigoInterface = codigoInterface;
		this.classeLinha = classeLinha;
	}
	
	private InterfaceEnum(Long codigoInterface, Integer tamanhoLinha, Class<? extends IntegracaoDocument> classeLinha, Class<? extends IntegracaoDocument> classeHeader, Class<? extends IntegracaoDocument> classeTrailer) {
		this.codigoInterface = codigoInterface;
		this.classeLinha = classeLinha;
		this.classeHeader = classeHeader;
		this.classeTrailer = classeTrailer;
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

	public Class<?> getClasseHeader() {
		return classeHeader;
	}

	public Class<?> getClasseTrailer() {
		return classeTrailer;
	}
	
	public Integer getTamanhoLinha() {
		return this.tamanhoLinha;
	}
}
