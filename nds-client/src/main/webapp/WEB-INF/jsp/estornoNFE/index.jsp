<input id="permissaoAlteracao" type="hidden"
	value="${permissaoAlteracao}">

<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/estornoNFE.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>

<title>Estorno Nota Fiscal</title>

<style type="text/css">
fieldset label {
	width: auto;
	margin-bottom: 0px !important;
}

.gridLinhaDestacada {
	background: #BEBEBE;
	font-weight: bold;
	color: #fff;
}

.gridLinhaDestacada:hover {
	color: #000;
}

.gridLinhaDestacada a {
	color: #fff;
}

.gridLinhaDestacada a:hover {
	color: #000;
}
</style>
<style type="text/css">
#dialog-box {
	display: none;
}

#dialog-box fieldset {
	width: 570px !important;
}
</style>

<script language="javascript" type="text/javascript">
	$(function() {
		estornoNFEController.init();
	});
</script>

</head>

<body>
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
     			<span class="bt_novos">
      				<a isEdicao="true" name="btnPesquisar" id="btnPesquisar" onclick="estornoNFEController.buscar();">
      					<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
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