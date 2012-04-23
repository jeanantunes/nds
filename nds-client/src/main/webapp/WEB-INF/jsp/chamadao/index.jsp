<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<script>

	$(function() {
		
		inicializar();
	});
	
	function iniciarGrid() {
		
		$(".chamadaoGrid").flexigrid({
			preProcess: executarPreProcessamento,
			dataType : 'json',
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
				name : 'valorDesconto',
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
				name : 'dataRecolhimento',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valorTotal',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : ' ',
				name : 'sel',
				width : 40,
				sortable : false,
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
		
		$("#dataChamadao").datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	}
	
	function inicializar() {
		
		iniciarGrid();
		
		iniciarData();
		
		$("#tipoMovimento").focus();
		
		$("#descricaoCota").autocomplete({source: ""});
	}
		
	function popup() {
	
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
		
		var numeroCota = $("#numeroCota").val();
		var dataChamadaoFormatada = $("#dataChamadao").val();
		var idFornecedor = $("#idFornecedor").val();
		
		$(".chamadaoGrid").flexOptions({
			url: "<c:url value='/devolucao/chamadao/pesquisarConsignados' />",
			onSuccess: function() {
				
				var checkAllSelected = verifyCheckAll();
				
				if (checkAllSelected) {
					
					$("input[name='checkConsignado']").each(function() {
						
						this.checked = true;
					});
				}
			},
			params: [
		         {name:'numeroCota', value: numeroCota},
		         {name:'dataChamadaoFormatada', value: dataChamadaoFormatada},
		         {name:'idFornecedor', value: idFornecedor}
		    ],
		    newp: 1,
		});
		
		$(".chamadaoGrid").flexReload();
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
		
		$.each(resultado.tableModel.rows, function(index, row) {
			
			var spanReparte = "<span id='reparte" + index + "'>"
						+ row.cell.reparte + "</span>";
			
			var spanValorTotal = "<span id='valorTotal" + index + "'>"
						+ row.cell.valorTotal + "</span>";
			
			var inputCheck = '<input type="checkbox" id="ch' + index + '"'
						   + ' name="checkConsignado"'
						   + ' value="' + index + '"'
						   + ' onclick="calcularParcial()" />';
						   
			row.cell.reparte = spanReparte;
			row.cell.valorTotal = spanValorTotal;
			row.cell.sel = inputCheck;
		});
		
		$("#qtdProdutosTotal").val(resultado.qtdProdutosTotal);
		$("#qtdExemplaresTotal").val(resultado.qtdExemplaresTotal);
		$("#valorTotal").val(resultado.valorTotal);
		
		var checkAllSelected = verifyCheckAll();
		
		if (checkAllSelected) {
			
			duplicarCamposParciais();
			
		} else {
			
			zerarCamposParciais();
		}
		
		$(".grids").show();
		
		return resultado.tableModel;
	}
	
	function selecionarTodos(input) {
		
		checkAll(input, "checkConsignado");
		
		if (input.checked) {
			
			duplicarCamposParciais();
			
		} else {
			
			zerarCamposParciais();
		}
	}
	
	function calcularParcial() {

		var qtdProdutosParcial = 0;
		var qtdExemplaresParcial = 0;
		var valorParcial = 0;
		
		$("input[name='checkConsignado']").each(function() {
		
			var checado = this.checked;
			
			if (checado) {
				
				qtdProdutosParcial = qtdProdutosParcial + 1;
				
				var reparte = $("#reparte" + this.value).html();
				reparte = removeMascaraPriceFormat(reparte);
				qtdExemplaresParcial = qtdExemplaresParcial + intValue(reparte);
				
				var valor = $("#valorTotal" + this.value).html();
				valor = removeMascaraPriceFormat(valor);
				valorParcial = valorParcial + intValue(valor);
			
			} else {
				
				$("#checkAll").attr("checked", false);
			}
		});
		
		$("#qtdProdutosParcial").val(qtdProdutosParcial);
		$("#qtdExemplaresParcial").val(qtdExemplaresParcial);
		$("#valorParcial").val(valorParcial);
		
		aplicarMascaraCampos();
	}
	
	function verifyCheckAll() {
		return ($("#checkAll").attr("checked") == "checked");
	}
	
	function duplicarCamposParciais() {
			
		$("#qtdProdutosParcial").val($("#qtdProdutosTotal").val());
		$("#qtdExemplaresParcial").val($("#qtdExemplaresTotal").val());
		$("#valorParcial").val($("#valorTotal").val());
	}
	
	function zerarCamposParciais() {
		
		$("#qtdProdutosParcial").val(0);
		$("#qtdExemplaresParcial").val(0);
		$("#valorParcial").val(0);
		
		aplicarMascaraCampos();
	}
	
	function aplicarMascaraCampos() {
		
		$("#qtdExemplaresParcial").priceFormat({
			allowNegative: true,
			centsSeparator: '.',
		    thousandsSeparator: ''
		});
		
		$("#valorParcial").priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
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
			    <td width="98">
			    	<input type="text" name="numeroCota" id="numeroCota" style="width:70px; float:left;
						   margin-right:5px;" onchange="cota.pesquisarPorNumeroCota('#numeroCota', '#descricaoCota');" />
			    </td>
			    <td width="38">Nome:</td>
			    <td width="178">
			    	<input name="descricaoCota" id="descricaoCota" type="text"
		      		 	   class="nome_jornaleiro" maxlength="255" style="width:130px;"
		      		 	   onkeyup="cota.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="cota.pesquisarPorNomeCota('#numeroCota', '#descricaoCota');" />
		      	</td>
			    <td width="96">Data Chamadão:</td>
			    <td width="102">
			    	<input type="text" name="dataChamadao" id="dataChamadao" style="width:70px; float:left; margin-right:5px;" />
			    </td>
			    <td width="68">Fornecedor:</td>
			    <td width="191">
			   		<select name="idFornecedor" id="idFornecedor" style="width:190px;">
      					<option selected="selected" value="">Todos</option>
						<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
    				</select>
    			</td>
    			<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>
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
	
	      				<span class="bt_novos" title="Gerar Arquivo">
	      					<a href="${pageContext.request.contextPath}/devolucao/chamadao/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
								Arquivo
							</a>
	      				</span>
	
						<span class="bt_novos" title="Imprimir">
							<a href="${pageContext.request.contextPath}/devolucao/chamadao/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
								Imprimir
							</a>
						</span>
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
				                      	<td width="72" align="center">
				                      		<input id="qtdProdutosParcial" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td width="82" align="center">
				                      		<input id="qtdExemplaresParcial" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td width="90" align="center">
				                      		<input id="valorParcial" type="text" style="width:80px; text-align:right;" disabled="disabled"/>
				                      	</td>
				                    </tr>
				                    <tr class="class_linha_2">
				                      	<td height="23"><strong>Total:</strong></td>
				                      	<td align="center">
				                      		<input id="qtdProdutosTotal" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td align="center">
				                      		<input id="qtdExemplaresTotal" type="text" style="width:60px; text-align:center;" disabled="disabled"/>
				                      	</td>
				                      	<td align="center">
				                      		<input id="valorTotal" type="text" style="width:80px; text-align:right;" disabled="disabled"/>
				                      	</td>
				                    </tr>
				          		</table>
				          	</div>
				      	</fieldset>
	       			</td>
	       			<td width="163" valign="top">
	       				<span class="bt_sellAll">
	       					<label for="sel" style="float:left;">Selecionar Todos</label>
	       					
	       					<input type="checkbox" name="checkAll" id="checkAll"
	       						   onclick="selecionarTodos(this);" style="float:left;"/>
	       				</span>
	       			</td>
	      		</tr>
	 		</table>
		</div>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

</body>