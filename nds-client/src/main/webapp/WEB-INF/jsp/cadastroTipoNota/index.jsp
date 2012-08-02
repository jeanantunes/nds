<head>
<title>Cadastro de Tipos de Notas</title>
<script language="javascript" type="text/javascript">

function pesquisar() {

	var operacao = $("#operacaoID").val();
	var tipoNota = $("#tipoNota").val();

	$(".tiposNotasGrid").flexOptions({
		url: "<c:url value='/administracao/cadastroTipoNota/pesquisar' />",
		params: [
	         {name:'operacao', value: operacao},
	         {name:'tipoNota', value: tipoNota}
	    ],
	    newp: 1,
	});
	
	$(".tiposNotasGrid").flexReload();
}


$(function() {
	definirAcaoPesquisaTeclaEnter();	
});

</script>
<style type="text/css">
	#dialog-box{display:none;}
	#dialog-box fieldset{width:570px!important;}
</style>
</head>

<body>
	
	<div class="corpo">
    	<div class="container">
      		<fieldset class="classFieldset">
   	    		<legend> Pesquisar Tipo de Nota</legend>
        		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            		<tr>
              			<td width="47">Operação:</td>
                		<td width="114">
                			 <select name="operacao" id="operacaoID" style="width:150px;" class="campoDePesquisa">
						      <option selected="selected">Selecione...</option>
						      <c:forEach var="operacao" items="${listaAtividades}">
										<option value="${operacao.key}">${operacao.value}</option>
							  </c:forEach>
						    </select>
                		</td>
		                <td width="81">Tipo de Nota:</td>
		                <td width="576"><input type="text" name="tipoNota" id="tipoNota" style="width:200px;" class="campoDePesquisa" /></td>
		              	<td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();" class="botaoPesquisar" >Pesquisar</a></span></td>
            		</tr>
          		</table>
      		</fieldset>
      		<div class="linha_separa_fields">&nbsp;</div>
	      	<fieldset class="classFieldset">
	       	  	<legend>Tipos de Notas Cadastradas</legend>
	        	<div class="grids" style="display:none;">
	        		<table class="tiposNotasGrid"></table>
	            	<div class="linha_separa_fields">&nbsp;</div>
	       			<span class="bt_novos" title="Gerar Arquivo"><a href="${pageContext.request.contextPath}/administracao/cadastroTipoNota/exportar?fileType=XLS"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
	             	<span class="bt_novos" title="Imprimir"><a href="${pageContext.request.contextPath}/administracao/cadastroTipoNota/exportar?fileType=PDF"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
	        	</div>
	      	</fieldset>
    	</div>
	</div> 
	<script>
		$(".tiposNotasGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'Operacao',
					name : 'tipoAtividade',
					width : 140,
					sortable : true,
					align : 'left'
				},{
					display : 'Processo',
					name : 'processo',
					width : 140,
					sortable : true,
					align : 'left'
				},{
					display : 'Tipo de Nota',
					name : 'nopDescricao',
					width : 400,
					sortable : true,
					align : 'left'
				},{
					display : 'CFOP Dentro UF',
					name : 'cfopEstado',
					width : 100,
					sortable : true,
					align : 'left'
				},{
					display : 'CFOP Fora UF',
					name : 'cfopOutrosEstados',
					width : 100,
					sortable : true,
					align : 'left'
				}],
				sortname : "tipoAtividade",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
		});

		function executarPreProcessamento(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}

			mostrar();
			return resultado;
		}
		
	</script>
</body>
</html>
