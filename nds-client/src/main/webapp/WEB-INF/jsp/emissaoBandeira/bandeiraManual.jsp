<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<style type="text/css">
		#dialog-pallets-bandeira-manual{display:none;}
		
		.bordaSecao{
			height:100px;
			color:#000;
			border-left: 1px solid #000000;
			border-right: 1px solid #000000;
			border-bottom:1px solid #000000;
		}
		
		.inputCanalFornec{
			width:440px;
			font-size:50px !important;
			height:60px;
			font-weight:bold;
			text-align: left;
		}
		
		.inputLinhaToda{
			width:870px;
			font-size:50px !important;
			height:60px;
			font-weight:bold;
			text-align: center !important;
		}
	</style>
</head>

<body>
	<table width="100%" border="0" cellpadding="2" cellspacing="0" style="height:75px; color:#000; border: 1px solid #000000;">
		<tr>
			<td width="881" align="center">
		  		<input id="titulo" type="text" 
		  			class="inputLinhaToda"
		  			value="DEVOLU&Ccedil;&Atilde;O TREELOG"/>
		  	</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="2" cellspacing="0" style="height:75px;" class="bordaSecao">
		<tr>
			<td width="430" align="center">
		  		<input id="canal" type="text" class="inputCanalFornec"/>
		  	</td>
		  	<td width="1">&nbsp;</td>
		  	<td width="430" align="center">
		  		<input id="inputfornecedor" type="text" class="inputCanalFornec"/>
		  	</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="2" cellspacing="0" class="bordaSecao">
		<tr>
			<td>PRA&Ccedil;A:</td>
		</tr>
		<tr>
			<td width="881" align="center">
		  		<input id="praca" type="text" class="inputLinhaToda"/>
		  	</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="2" cellspacing="0" class="bordaSecao">
		<tr>
			<td>SEMANA:</td>
		</tr>
		<tr>
			<td width="881" align="center">
		  		<input id="semana" type="text" class="inputLinhaToda" value="SEMANA " />
		  	</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="2" cellspacing="0" class="bordaSecao">
		<tr>
			<td>VOLUMES:</td>
		</tr>
		<tr>
			<td width="881" align="center">
		  		<input id="destinoBandeiraManual" type="text" style="width:870px; font-size:50px; height:60px; font-weight:bold;" readonly="readonly" />
		  	</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="2" cellspacing="0" class="bordaSecao">
		<tr>
			<td>DATA DE ENVIO:</td>
		</tr>
		<tr>
			<td width="881" align="center">
		  		<input id="dataEnvioManual" type="text" class="inputLinhaToda"/>
		  	</td>
		</tr>
	</table>
	
	<table width="920" border="0" align="center" cellpadding="3" cellspacing="0" style="margin-top:5px;">
		<span class="bt_novos" title="Imprimir Bandeira">
			<a href="javascript:;" onclick="emissaoBandeiraController.imprimirBandeiraManual();" rel="bandeira">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Bandeira
			</a>
		</span>
	</table>

<form id="form-pallets-bandeira-manual">
	<div id="dialog-pallets-bandeira-manual" title="Pallets">
		<fieldset>
			<legend>Informe a quantidade de pallets</legend>
		    N&uacute;mero de Pallets: <input id="numeroPalletsBandeiraManual" type="text" style="width:40px; margin-left:10px; text-align:center;" />
		
		</fieldset>
	</div>
</form>
<script type="text/javascript">
	$(function(){
		emissaoBandeiraController.initBandeiraManual();
	});
</script>
</body>
