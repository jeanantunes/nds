<head>

<script>

	$(function() {
		
		inicializar();
	});
	
	function iniciarGrid() {
		
		$(".chamadaoGrid").flexigrid({
			url : '../xml/chamadao-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Desconto R$',
				name : 'desconto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Recolhimento',
				name : 'recolhimento',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : ' ',
				name : 'sel',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	}
	
	function iniciarData() {
		
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	}
	
	function inicializar() {
		
		iniciarGrid();
		
		iniciarData();
		
		$("#tipoMovimento").focus();
	}
		
	function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:'auto',
			width:320,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

	function pesquisar() {
		
		var idTipoMovimento = $("#tipoMovimento").val();
		var dataMovimento = $("#dataMovimento").val();
		
		$(".solicitacoesAprovacao").flexOptions({
			url: "<c:url value='/administracao/controleAprovacao/pesquisarAprovacoes' />",
			onSuccess: executarAposProcessamento,
			params: [
		         {name:'idTipoMovimento', value: idTipoMovimento},
		         {name:'dataMovimentoFormatada', value: dataMovimento}
		    ],
		    newp: 1,
		});
		
		$(".solicitacoesAprovacao").flexReload();
	}
	
	function executarPreProcessamento(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids").hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkAprovar = '<a href="javascript:;" onclick="aprovarMovimento(' + row.cell.id + ');" style="cursor:pointer">' +
					     	  	'<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkRejeitar = '<a href="javascript:;" onclick="rejeitarMovimento(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Rejeitar" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkAprovar + linkRejeitar;
		});
			
		$(".grids").show();
		
		return resultado;
	}
		
</script>

</head>

<body>

	<div id="dialog-novo" title="Chamadão">
		<br />
		<strong>Confirma a Programação do Chamadão?</strong>
		<br />   
	</div>
	
	<fieldset class="classFieldset">
   	    <legend> Pesquisar        </legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  			<tr>
			    <td width="29">Cota:</td>
			    <td width="98"><input type="text" name="cota" id="cota" style="width:70px; float:left; margin-right:5px;" /><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
			    <td width="38">Nome:</td>
			    <td width="178">Antonio José da Silva</td>
			    <td width="96">Data Chamadão:</td>
			    <td width="102"><input type="text" name="datepickerDe" id="datepickerDe" style="width:70px; float:left; margin-right:5px;" /></td>
			    <td width="68">Fornecedor:</td>
			    <td width="191">
			   		<select name="select" id="select" style="width:190px;">
      					<option selected="selected"> </option>
						<option>Todos</option>
					    <option>Dinap</option>
					    <option>FC</option>
    				</select>
    			</td>
    			<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
  			</tr>
		</table>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	      
	<fieldset class="classFieldset">
		<legend>Chamadão</legend>
	    
	    <div class="grids" style="display:none;">
			
			<table class="chamadaoGrid"></table>
	        
	        <table width="949" border="0" cellspacing="1" cellpadding="1">
	   			<tr>
	   				<td width="318" valign="top">
	    				<span class="bt_confirmar_novo" title="Gravar"><a onclick="popup();" href="javascript:;"><img
							  border="0" hspace="5"
							  src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar</a>
						</span>
	
	      				<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
	
						<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
					</td>
	      				
	      			<td width="458">
				        <fieldset class="box_field" style="width:320px;">
				        	<legend>Chamadão</legend>
				        	<div class="box_resumo">
				            	<table width="309" border="0" cellspacing="1" cellpadding="1">
				                	<tr class="header_table">
				                    	<td height="23" align="right">&nbsp;</td>
				                      	<td align="center"><strong>Produtos</strong></td>
				                      	<td align="center"><strong>Exemplares</strong></td>
				                      	<td align="center"><strong>Total R$</strong></td>
				                    </tr>
				                    <tr class="class_linha_1">
				                    	<td width="52" height="23"><strong>Parcial:</strong></td>
				                      	<td width="72" align="center"><input type="text" style="width:60px; text-align:center;" value="2" disabled="disabled"/></td>
				                      	<td width="82" align="center"><input type="text" style="width:60px; text-align:center;" value="250" disabled="disabled"/></td>
				                      	<td width="90" align="center"><input type="text" style="width:80px; text-align:right;" value="999.999,99" disabled="disabled"/></td>
				                    </tr>
				                    <tr class="class_linha_2">
				                      	<td height="23"><strong>Total:</strong></td>
				                      	<td align="center"><input type="text" style="width:60px; text-align:center;" value="2" disabled="disabled"/></td>
				                      	<td align="center"><input type="text" style="width:60px; text-align:center;" value="250" disabled="disabled"/></td>
				                      	<td align="center"><input type="text" style="width:80px; text-align:right;" value="999.999,99" disabled="disabled"/></td>
				                    </tr>
				          		</table>
				          	</div>
				      	</fieldset>
	       			</td>
	       			<td width="163" valign="top"><span class="bt_sellAll"><label for="sel" style="float:left;">Selecionar Todos</label><input type="checkbox" name="Todos" id="sel" onclick="checkAll();" style="float:left;"/></span></td>
	      		</tr>
	 		</table>
		</div>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

</body>