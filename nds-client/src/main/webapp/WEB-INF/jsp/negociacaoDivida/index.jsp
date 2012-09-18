<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/negociacaoDivida.js"></script>
	
	<style>
		#dadosArquivo, .comissaoAtual, .pgtos{display:none;}
		#dialog-formaPgto{display:none;}
		.semanal, .mensal, .quinzenal{display:none;}
	</style>
</head>

<body>
	<form id="negocicaoDividaForm">
		<fieldset class="classFieldset">
	   	    <legend> Negociar D&iacute;vidas</legend>
	   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              	<td width="28">Cota:</td>
	              	<td colspan="3"><input type="text" id="negociacaoDivida_numCota" onblur="negociacaoDividaController.pesquisarCota(this.value)" style="width:60px; float:left; margin-right:5px;" /></td>
	              	<td width="39">Nome:</td>
	              	<td width="207"><span id="negociacaoDivida_nomeCota"></span></td>
	              	<td width="41">Status:</td>
	              	<td width="157"><span id="negociacaoDivida_statusCota"></span></td>
	              	<td width="33" align="right"><input type="checkbox" name="checkbox" id="checkbox" /></td>
	              	<td width="201">Lan&ccedil;amentos Futuros</td>
	              	<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="negociacaoDividaController.pesquisar()"></a></span></td>
	        	</tr>
	    	</table>
		</fieldset>
		
	    <div class="linha_separa_fields">&nbsp;</div>
	    
	    <fieldset class="grids classFieldset" style="display:none;">
	      	<legend>D&iacute;vida Negociada - Cota: <span id="negociacaoDivida_numEnomeCota"></span></legend>
	        <br />
	      	<table class="negociarGrid"></table>
	        <table width="100%" border="0" cellspacing="2" cellpadding="2">
		        <tr>
	            	<td width="19%">
	            		<span class="bt_arquivo"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportar?fileType=XLS">Arquivo</a></span>
						<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportar?fileType=PDF">Imprimir</a></span>
	                </td>
	                <td width="35%">   
	                    <span class="bt_confirmar_novo" title="Formas de Pagamento"><a href="javascript:;" onclick="popup_formaPgto();"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Negociar</a></span>
	                </td>
	                <td width="13%"><strong>Total Selecionado R$:</strong></td>
	                <td width="6%">10.567,00</td>
	                <td width="6%"><strong>Total R$:</strong></td>
	                <td width="6%">10.567,00</td>
	                <td width="15%"><span class="bt_sellAll"><label for="sel">Selecionar Todos</label><input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:left;"/></span></td>
	            </tr>
	        </table>
		</fieldset>
	</form>

	<script type="text/javascript">
		$(function(){
			negociacaoDividaController.init();
		});
	</script>
</body>