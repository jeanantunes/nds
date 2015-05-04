<head>
<title>Edições Fechadas com Saldo</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/painelProcessamento.js"></script>
<script type="text/javascript">
	$(function() {
		$("#toggleFornecedores").buttonset();
		$("input[name='radioToggle']").click(function() {
			painelProcessamentoController.alterarProcessamento();
		});
		painelProcessamentoController.init("${pageContext.request.contextPath}");
	});
</script>
<style type="text/css">
#dialog-novo fieldset, #dialog-detalhes fieldset{ width: 700px!important;}
#conteudo{width: 950px !important;}
.fieldFiltro {width: 960px !important;}
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
	    <p><strong>Interface:</strong> <span id="nomeInterface">&nbsp;</span> <strong style="margin-right:5px; margin-left:10px;">Data: </strong><span id="dataProcessamento">&nbsp;</span> <strong style="margin-right:5px; margin-left:10px;">Hora:</strong> <span id="horaProcessamento">&nbsp;</span></p>
	    <br clear="all" />
	    <table class="detalheProcessamentoGrid"></table>
    </fieldset>
</div>
</form>

<div id="dialog-excutarInterfacesEmOrdem" title="Processar todas as Interfaces em Ordem" style="display:none;">
    <fieldset style="width:350px!important;">
    <legend>Reprocessar Interfaces:</legend>
    <p>Deseja reprocessar todas as interfaces do Prodin e MDC?</p><br />
    <p>* As interfaces serão executadas em ordem de prioridade</p><br />
    </fieldset>
</div>

<div id="dialog-excutarInterface" title="Processar Interface" style="display:none;">
	<br />
    <p>Deseja reprocessar interface?</p>
</div>

<form id="detalhesInterface">
<div id="dialog-detalhes" title="Detalhes da Interface">
    <fieldset>
    <legend>Detalhe da Interface</legend>
    <p><%--strong>Interface:</strong> <span id="nomeInterface">&nbsp;</span> - --%><strong style="margin-right:5px; margin-left:10px;">Data: </strong>27/04/2012 <strong style="margin-right:5px; margin-left:10px;">Hora:</strong> 13:59h</p>
    <br clear="all" />
    <table class="detalheInterfaceGrid"></table>
    </fieldset>
</div>
</form>

	  <div class="areaBts">
	  	<div class="area">
	  		<span class="bt_novos"><a href="javascript:;" id="btnReprocessarTodas" rel="tipsy" title="Reprocessar todas as interfaces em ordem"><img src="${pageContext.request.contextPath}/images/bt_devolucao.png" hspace="5" border="0" /></a></span>
	  		<span class="bt_arq"><a href="javascript:;" id="btnGerarXLS" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
			<span class="bt_arq"><a href="javascript:;" id="btnGerarPDF" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /></a></span>
	  	</div>
	  </div>
	  <div class="linha_separa_fields">&nbsp;</div>

      <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
       	  <legend>Painel de Processamentos</legend>
		<div id="toggleFornecedores">

		  <input type="radio" id="visao-dinap" name="radioToggle" value="${codigoDistribuidor.dinap}" checked="checked">
		  <label for="visao-dinap" style="width:100px">DINAP</label>
		
		  <input type="radio" id="visao-fc" name="radioToggle" value="${codigoDistribuidor.fc}">
		  <label for="visao-fc" style="width:100px">FC</label>
		  
		  <input type="radio" id="visao-processos" name="radioToggle" value="">
		  <label for="visao-processos" style="width:100px">Processos</label>
		
		</div>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<div id="divProcessamento" style="display: none;">
			
			<span class="bt_confirmar_novo" title="Gerar Ranking Segmento">
           			<a onclick="painelProcessamentoController.gerarRankingSegmento();" href="javascript:;">
           			<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">&nbsp;Gerar Ranking Segmento</a>
           	</span>
			
			<span class="bt_confirmar_novo" title="Gerar Ranking Faturamento">
           			<a onclick="painelProcessamentoController.gerarRankingFaturamento();" href="javascript:;">
           			<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">&nbsp;Gerar Ranking Faturamento</a>
           	</span>
           	
           	<span class="bt_confirmar_novo" title="Exportar Cobran&ccedil;a">
           			<a onclick="painelProcessamentoController.exportarCobranca();" href="javascript:;">
           			<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_boletos.gif">&nbsp;Exportar Cobran&ccedil;a</a>
           	</span>
           	
           	<span class="bt_confirmar_novo" title="Processar Cobran&ccedil;a Consolidada">
           			<a onclick="painelProcessamentoController.processarCobrancaConsolidada();" href="javascript:;">
           			<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_boletos.gif">&nbsp;Processar Cobran&ccedil;a Consolidada</a>
           	</span>
			
		</div>
		
          <div class="grids" style="display:block;">
        	<div id="tabPainel">
                <div id="tabPainel-Interfaces">
                    <table width="600" border="0" align="right" cellpadding="2" cellspacing="1" >
                      <tr>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_operando.png" alt="Operando" /></td>
                        <td width="143">Realizado com Sucesso</td>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_semdados.png" alt="Off-line" /></td>
                        <td width="143">Sem dados a processar</td>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_offline.png" alt="Off-line" /></td>
                        <td width="142">Processado com erro</td>
                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_encerrado.png" alt="Off-line" /></td>
                        <td width="123">Não Processado</td>
                      </tr>
                      </table>
                      <br clear="all"/>
                  <table class="painelInterfaceGrid"></table>
              	</div>  
            </div>
        </div>
        
      </fieldset>

</body>
</html>