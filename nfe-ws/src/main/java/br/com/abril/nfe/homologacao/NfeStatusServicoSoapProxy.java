package br.com.abril.nfe.homologacao;

public class NfeStatusServicoSoapProxy implements br.com.abril.nfe.homologacao.NfeStatusServicoSoap {
  private String _endpoint = null;
  private br.com.abril.nfe.homologacao.NfeStatusServicoSoap nfeStatusServicoSoap = null;
  
  public NfeStatusServicoSoapProxy() {
    _initNfeStatusServicoSoapProxy();
  }
  
  public NfeStatusServicoSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initNfeStatusServicoSoapProxy();
  }
  
  private void _initNfeStatusServicoSoapProxy() {
    try {
      nfeStatusServicoSoap = (new br.com.abril.nfe.homologacao.NfeStatusServicoLocator()).getNfeStatusServicoSoap();
      if (nfeStatusServicoSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)nfeStatusServicoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)nfeStatusServicoSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (nfeStatusServicoSoap != null)
      ((javax.xml.rpc.Stub)nfeStatusServicoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.abril.nfe.homologacao.NfeStatusServicoSoap getNfeStatusServicoSoap() {
    if (nfeStatusServicoSoap == null)
      _initNfeStatusServicoSoapProxy();
    return nfeStatusServicoSoap;
  }
  
  public java.lang.String nfeStatusServicoNF(java.lang.String nfeCabecMsg, java.lang.String nfeDadosMsg) throws java.rmi.RemoteException{
    if (nfeStatusServicoSoap == null)
      _initNfeStatusServicoSoapProxy();
    return nfeStatusServicoSoap.nfeStatusServicoNF(nfeCabecMsg, nfeDadosMsg);
  }
  
  
}