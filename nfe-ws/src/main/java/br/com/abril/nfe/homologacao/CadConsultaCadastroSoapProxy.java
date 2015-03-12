package br.com.abril.nfe.homologacao;

public class CadConsultaCadastroSoapProxy implements br.com.abril.nfe.homologacao.CadConsultaCadastroSoap {
  private String _endpoint = null;
  private br.com.abril.nfe.homologacao.CadConsultaCadastroSoap cadConsultaCadastroSoap = null;
  
  public CadConsultaCadastroSoapProxy() {
    _initCadConsultaCadastroSoapProxy();
  }
  
  public CadConsultaCadastroSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initCadConsultaCadastroSoapProxy();
  }
  
  private void _initCadConsultaCadastroSoapProxy() {
    try {
      cadConsultaCadastroSoap = (new br.com.abril.nfe.homologacao.CadConsultaCadastroLocator()).getCadConsultaCadastroSoap();
      if (cadConsultaCadastroSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cadConsultaCadastroSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cadConsultaCadastroSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cadConsultaCadastroSoap != null)
      ((javax.xml.rpc.Stub)cadConsultaCadastroSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.abril.nfe.homologacao.CadConsultaCadastroSoap getCadConsultaCadastroSoap() {
    if (cadConsultaCadastroSoap == null)
      _initCadConsultaCadastroSoapProxy();
    return cadConsultaCadastroSoap;
  }
  
  public java.lang.String consultaCadastro(java.lang.String nfeCabecMsg, java.lang.String nfeDadosMsg) throws java.rmi.RemoteException{
    if (cadConsultaCadastroSoap == null)
      _initCadConsultaCadastroSoapProxy();
    return cadConsultaCadastroSoap.consultaCadastro(nfeCabecMsg, nfeDadosMsg);
  }
  
  
}