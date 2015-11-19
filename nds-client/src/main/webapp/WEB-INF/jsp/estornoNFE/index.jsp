<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="scripts/estornoNFE.js"></script>

<script language="javascript" type="text/javascript">
	$(function() {
		estornoNFEController.init();
	});
</script>

</head>

<body>

<div class="areaBts">
	<div class="area">

        <span class="bt_arq"><a href="javascript:;" id="impressaoNfe-btnImprimirXLS" title="Gerar Arquivo" onclick="estornoNFEController.imprimir('XLS');" rel="bandeira"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>

        <span class="bt_arq"><a href="javascript:;" id="impressaoNfe-btnImprimirPDF" title="Imprimir" onclick="estornoNFEController.imprimir('PDF');"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>	
	</div>
</div>
<div class="linha_separa_fields">&nbsp; </div>

<form id="excluir_certificado_form">
	<div id="dialog-certificado-excluir" title="Excluir Certificado">		
		<p>Confirma a exclus&atilde;o deste Certificado?</p>
	</div>
</form>

<div id="preparing-file-modal" title="Preparando para gera&ccedil;&atilde;o do report..." style="display: none;">
    Por Favor Aguarde...
    <div class="ui-progressbar-value ui-corner-left ui-corner-right" style="width: 100%; height:22px; margin-top: 20px;"></div>
</div>

<div id="error-modal" title="Error" style="display: none;">
    Problema ao gerar arquivo solicitado...
</div>

<div class="container">
<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
	<legend>Pesquisar nota fiscal</legend>
   	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
       	<tr>
       		<td width="80">Numero nota:</td>
        	<td width="152"><input type="text" name="estorno-numero" id="estorno-numero" style="width:100px;"/></td>
         
         	<td width="50">Serie:</td>
         	<td width="152"><input type="text" name="estorno-serie" id="estorno-serie" style="width:100px;"/></td>
         
         	<td width="80">Chave de acesso:</td>
         	<td width="152"><input type="text" name="estorno-chaveAcesso" id="estorno-chaveAcesso" style="width:230px;"/></td>
     	</tr>
     	<tr>    
        	<td width="80">Data Emiss&atilde;o:</td>
         	<td width="152"><input type="text" name="estorno-dataEmissao" id="estorno-dataEmissao" class="input-date" style="width:100px;"/></td>
         
     		<td width="474"  colspan="4">
     			<span class="bt_pesquisar">
      				<a isEdicao="true" name="btnPesquisar" id="btnPesquisar" href="javascript:;"  onclick="estornoNFEController.buscar();">
            		</a>
           		</span>
           	</td>
       	</tr>
   	</table>
</fieldset>

<div class="linha_separa_fields">&nbsp;</div>

<div class="grids" style="display:none;">
	<fieldset class="fieldGrid">
		<legend>Nota Fiscal</legend>
       	<table class="estornoGrid"></table>	
    </fieldset>
</div>
</body>