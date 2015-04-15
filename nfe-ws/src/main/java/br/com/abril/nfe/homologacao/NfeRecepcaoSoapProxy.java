package br.com.abril.nfe.homologacao;

public class NfeRecepcaoSoapProxy implements br.com.abril.nfe.homologacao.NfeRecepcaoSoap {
  private String _endpoint = null;
  private br.com.abril.nfe.homologacao.NfeRecepcaoSoap nfeRecepcaoSoap = null;
  
  public NfeRecepcaoSoapProxy() {
    _initNfeRecepcaoSoapProxy();
  }
  
  public NfeRecepcaoSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initNfeRecepcaoSoapProxy();
  }
  
  private void _initNfeRecepcaoSoapProxy() {
    try {
      nfeRecepcaoSoap = (new br.com.abril.nfe.homologacao.NfeRecepcaoLocator()).getNfeRecepcaoSoap();
      if (nfeRecepcaoSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)nfeRecepcaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)nfeRecepcaoSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (nfeRecepcaoSoap != null)
      ((javax.xml.rpc.Stub)nfeRecepcaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.abril.nfe.homologacao.NfeRecepcaoSoap getNfeRecepcaoSoap() {
    if (nfeRecepcaoSoap == null)
      _initNfeRecepcaoSoapProxy();
    return nfeRecepcaoSoap;
  }
  
  public java.lang.String nfeRecepcaoLote(java.lang.String nfeCabecMsg, java.lang.String nfeDadosMsg) throws java.rmi.RemoteException{
    if (nfeRecepcaoSoap == null)
      _initNfeRecepcaoSoapProxy();
    return nfeRecepcaoSoap.nfeRecepcaoLote(nfeCabecMsg, nfeDadosMsg);
  }
  
  
}