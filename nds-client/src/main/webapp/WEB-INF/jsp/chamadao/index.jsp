<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<script>

	var pesquisaCotaChamadao = new PesquisaCota();

	$(function() {
		var followUp = $('#numeroCotaFollowUp').val();
		
		inicializar();
		if(followUp != ''){			
			pesquisar();
		}
	});

	function getQueryString() {
		var result = {}; 
		var queryString = location.search.substring(1);
		var re = /([^&=]+)=([^&]*)/g;
		var m;

		while (m = re.exec(queryString)) {
			result[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
		}

		return result;
	}

	function popularGridPeloFollowUp(numeroCota, dataChamadaoFormatada){
				
		
		$(".chamadaoGrid").flexOptions({
			url: "${pageContext.request.contextPath}/devolucao/chamadao/pesquisarConsignados",
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
				display : 'Preço Venda R$',
				name : 'precoVenda',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desconto R$',
				name : 'precoDesconto',
				width : 110,
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
				width : 130,
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
				width : 70,
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
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("#dataChamadao").mask("99/99/9999");
	}
	
	function inicializar() {
		
		iniciarGrid();
		
		iniciarData();
		
		$("#numeroCota").focus();
		
		$("#descricaoCota").autocomplete({source: ""});

		
		if(getQueryString()["carregarGrid"] == true){
			var numeroCota = getQueryString()["numeroCota"];
			var dataChamadaoFormatada = getQueryString()["data"];
			popularGridPeloFollowUp(numeroCota,dataChamadaoFormatada);

			}
	}
		
	function pesquisar() {
		var followUp = $('#numeroCotaFollowUp').val();
		
		var numeroCota;
		var dataChamadaoFormatada;
		
		if(followUp != ''){
			numeroCota = $("#numeroCotaFollowUp").val();
			dataChamadaoFormatada = $("#dataCotaFollowUp").val();
		}else{
			numeroCota = $("#numeroCota").val();
			dataChamadaoFormatada = $("#dataChamadao").val();
			var idFornecedor = $("#idFornecedor").val();
		}
		
		$(".chamadaoGrid").flexOptions({
			url: "${pageContext.request.contextPath}/devolucao/chamadao/pesquisarConsignados",
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
			
			var spanReparte = "<span id='reparte" + row.id + "'>"
						+ row.cell.reparte + "</span>";
			
			var spanValorTotal = "<span id='valorTotal" + row.id + "'>"
						+ row.cell.valorTotal + "</span>";
			
			var inputCheck = '<input type="checkbox" id="ch' + row.id + '"'
						   + ' name="checkConsignado"'
						   + ' value="' + row.id + '"'
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
		
		$("input[name='checkConsignado']").each(function() {
		
			var checado = this.checked;
			
			clickLineFlexigrid(this, checado);
		});
		
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
			
			clickLineFlexigrid(this, checado);
			
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
			centsSeparator: '',
		    thousandsSeparator: '.'
		});
		
		$("#valorParcial").priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
	}
	
	function confirmar() {
		
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:'auto',
			width:320,
			modal: true,
			buttons: {
				"Confirmar": function() {

					realizarChamadao();
				},
				"Cancelar": function() {
					
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
			
				clearMessageDialogTimeout();
			}
		});
	}
	
	function realizarChamadao() {
		
		var linhasDaGrid = $('.chamadaoGrid tr');
		
		var listaChamadao = "";
		
		var checkAllSelected = verifyCheckAll();
		
		if (!checkAllSelected) {
			
			$.each(linhasDaGrid, function(index, value) {
				
				var linha = $(value);
				
				var colunaCheck = linha.find("td")[9];
				
				var inputCheck = $(colunaCheck).find("div").find('input[name="checkConsignado"]');
				
				var checked = inputCheck.attr("checked") == "checked";
				
				if (checked) {
				
					var colunaCodProduto = linha.find("td")[0];
					var colunaNumEdicao = linha.find("td")[2];
					
					var codProduto = $(colunaCodProduto).find("div").html();
					var numEdicao = $(colunaNumEdicao).find("div").html();
					
					var linhaSelecionada = 'listaChamadao[' + index + '].codigoProduto=' + codProduto + '&';
					linhaSelecionada += 'listaChamadao[' + index + '].numeroEdicao=' + numEdicao + '&';
					
					listaChamadao = (listaChamadao + linhaSelecionada);
				}
			});
		}
		
		$.postJSON("${pageContext.request.contextPath}/devolucao/chamadao/confirmarChamadao",
				   listaChamadao + "&chamarTodos=" + checkAllSelected,
				   function(result) {
						
						$("#dialog-confirm").dialog("close");
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagem(tipoMensagem, listaMensagens);
						}
						
						$(".chamadaoGrid").flexReload();
						
						$("#checkAll").attr("checked", false);
					},
				   null,
				   true
		);
	}
		
</script>

</head>

<body>
<input type="hidden" value="${numeroCotaFollowUp}" id="numeroCotaFollowUp" name="numeroCotaFollowUp">
<input type="hidden" value="${dataCotaFollowUp}" id="dataCotaFollowUp" name="dataCotaFollowUp">
	<div id="dialog-confirm" title="Chamadão">
		
		<jsp:include page="../messagesDialog.jsp" />
		
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
						   margin-right:5px;" onchange="pesquisaCotaChamadao.pesquisarPorNumeroCota('#numeroCota', '#descricaoCota');" />
			    </td>
			    <td width="38">Nome:</td>
			    <td width="178">
			    	<input name="descricaoCota" id="descricaoCota" type="text"
		      		 	   class="nome_jornaleiro" maxlength="255" style="width:130px;"
		      		 	   onkeyup="pesquisaCotaChamadao.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="pesquisaCotaChamadao.pesquisarPorNomeCota('#numeroCota', '#descricaoCota');" />
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
    			<td width="104">
    				<span class="bt_pesquisar" title="Pesquisar">
    					<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
    				</span>
    			</td>
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
	    				<span class="bt_confirmar_novo" title="Confirmar"><a onclick="confirmar();" href="javascript:;"><img
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
				        	<div class="box_resumo" style="width:308px;">
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