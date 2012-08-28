<head>
<title>Edições Fechadas com Saldo</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/painelProcessamento.js"></script>
<script type="text/javascript">
	$(function() {
		painelProcessamentoController.init("${pageContext.request.contextPath}");
	});
</script>
<style type="text/css">
#dialog-novo fieldset, #dialog-detalhes fieldset{ width: 700px!important;}
</style>
</head>
<body>
<form id="sistemaOperacional">
<div id="dialog-operacional" title="Sistema Operacional" style="display:none;">
    <fieldset style="width:350px!important;">
    <legend>Status do Sistema Operacional:</legend>
    <p>Sistema Operacional esta: <strong><span id="statusSistemaOperacional"></span></strong></p><br />
    </fieldset>
</div>
</form>
<form id="detalhesProcessamento">
<div id="dialog-novo" title="Detalhes do Processamento">
    <fieldset>
	    <legend>Detalhe do Processamento</legend>
	    <p><strong>Interface:</strong> Lançamento - <strong>Data: </strong>27/04/2012 <strong>Hora:</strong> 13:59h</label>
	    <br />
	    <table class="detalheProcessamentoGrid"></table>
    </fieldset>
</div>
</form>
<form id="detalhesInterface">
<div id="dialog-detalhes" title="Detalhes da Interface">
    <fieldset>
    <legend>Detalhe da Interface</legend>
    <p><strong>Interface:</strong> Lançamento - <strong>Data: </strong>27/04/2012 <strong>Hora:</strong> 13:59h</label>
    <br />
    <table class="detalheInterfaceGrid"></table>
    </fieldset>
</div>

	  <div class="areaBts">
	  	<div class="area">
	  		<span class="bt_arq" title="Gerar Arquivo"><a href="javascript:;" id="btnGerarXLS" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
			<span class="bt_arq" title="Imprimir"><a href="javascript:;" id="btnGerarPDF" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /></a></span>
	  	</div>
	  </div>
	        <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro">
       	  <legend>Painel de Processamentos</legend>
          <div class="grids" style="display:block;">
        	<div id="tabPainel">
                <ul>
                    <li><a href="#tabPainel-Interfaces" onclick="painelProcessamentoController.pesquisarInterfaces()">Interfaces</a></li>
                    <li><a href="#tabPainel-Processos" onclick="painelProcessamentoController.pesquisarProcessos()">Processos</a></li>
                </ul>
                <div id="tabPainel-Interfaces">
                    <table width="500" border="0" align="right" cellpadding="2" cellspacing="1" class="filtro">
                      <tr>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_operando.png" alt="Operando" /></td>
                        <td width="143">Realizado com Sucesso</td>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_offline.png" alt="Off-line" /></td>
                        <td width="142">Processado com erro</td>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_encerrado.png" alt="Off-line" /></td>
                        <td width="123">Não Processado</td>
                      </tr>
                      </table>
                  <table class="painelInterfaceGrid"></table>
              </div>
                <div id="tabPainel-Processos">
                	<table width="500" border="0" align="right" cellpadding="2" cellspacing="1" class="filtro">
                      <tr>
                      	<td width="167">&nbsp;</td>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_operando.png" alt="Operando" /></td>
                        <td width="146">Realizado com Sucesso</td>
                        <td width="19"><img src="${pageContext.request.contextPath}/images/ico_encerrado.png" alt="Off-line" /></td>
                        <td width="125">Não Processado</td>
                      </tr>
                      </table>
                	<table class="painelProcessamentoGrid"></table>
                </div>                
            </div>
        </div>
        
      </fieldset>

</body>
</html>
