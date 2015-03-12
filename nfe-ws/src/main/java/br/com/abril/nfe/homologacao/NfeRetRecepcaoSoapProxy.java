package br.com.abril.nfe.homologacao;

public class NfeRetRecepcaoSoapProxy implements br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap {
  private String _endpoint = null;
  private br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap nfeRetRecepcaoSoap = null;
  
  public NfeRetRecepcaoSoapProxy() {
    _initNfeRetRecepcaoSoapProxy();
  }
  
  public NfeRetRecepcaoSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initNfeRetRecepcaoSoapProxy();
  }
  
  private void _initNfeRetRecepcaoSoapProxy() {
    try {
      nfeRetRecepcaoSoap = (new br.com.abril.nfe.homologacao.NfeRetRecepcaoLocator()).getNfeRetRecepcaoSoap();
      if (nfeRetRecepcaoSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)nfeRetRecepcaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)nfeRetRecepcaoSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (nfeRetRecepcaoSoap != null)
      ((javax.xml.rpc.Stub)nfeRetRecepcaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap getNfeRetRecepcaoSoap() {
    if (nfeRetRecepcaoSoap == null)
      _initNfeRetRecepcaoSoapProxy();
    return nfeRetRecepcaoSoap;
  }
  
  public java.lang.String nfeRetRecepcao(java.lang.String nfeCabecMsg, java.lang.String nfeDadosMsg) throws java.rmi.RemoteException{
    if (nfeRetRecepcaoSoap == null)
      _initNfeRetRecepcaoSoapProxy();
    return nfeRetRecepcaoSoap.nfeRetRecepcao(nfeCabecMsg, nfeDadosMsg);
  }
  
  
}