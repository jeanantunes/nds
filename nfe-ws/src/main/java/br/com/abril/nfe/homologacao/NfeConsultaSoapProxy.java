package br.com.abril.nfe.homologacao;

public class NfeConsultaSoapProxy implements br.com.abril.nfe.homologacao.NfeConsultaSoap {
  private String _endpoint = null;
  private br.com.abril.nfe.homologacao.NfeConsultaSoap nfeConsultaSoap = null;
  
  public NfeConsultaSoapProxy() {
    _initNfeConsultaSoapProxy();
  }
  
  public NfeConsultaSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initNfeConsultaSoapProxy();
  }
  
  private void _initNfeConsultaSoapProxy() {
    try {
      nfeConsultaSoap = (new br.com.abril.nfe.homologacao.NfeConsultaLocator()).getNfeConsultaSoap();
      if (nfeConsultaSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)nfeConsultaSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)nfeConsultaSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (nfeConsultaSoap != null)
      ((javax.xml.rpc.Stub)nfeConsultaSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.abril.nfe.homologacao.NfeConsultaSoap getNfeConsultaSoap() {
    if (nfeConsultaSoap == null)
      _initNfeConsultaSoapProxy();
    return nfeConsultaSoap;
  }
  
  public java.lang.String nfeConsultaNF(java.lang.String nfeCabecMsg, java.lang.String nfeDadosMsg) throws java.rmi.RemoteException{
    if (nfeConsultaSoap == null)
      _initNfeConsultaSoapProxy();
    return nfeConsultaSoap.nfeConsultaNF(nfeCabecMsg, nfeDadosMsg);
  }
  
  
}