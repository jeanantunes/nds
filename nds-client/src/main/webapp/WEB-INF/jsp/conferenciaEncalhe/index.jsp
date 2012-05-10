<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Conferencia Encalhe</title>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>scripts/produto.js'></script>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/shortcut.js'></script>


<script type="text/javascript">

	var ConferenciaEncalhe = {
			
			pesquisarCota : function() {
				
				var numeroCota = jQuery("#numeroCota").val();
				
				$(".conferenciaEncalheGrid").flexOptions({
					url: '<c:url value="/devolucao/conferenciaEncalhe/pesquisarCota"/>',
					preProcess: ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe,
					dataType : 'json',
					params:[
				        {name:'numeroCota', value: numeroCota}
					]
				});
				
				$(".conferenciaEncalheGrid").flexReload();

			},
			
			preProcessarConsultaConferenciaEncalhe : function(result) {
				
				var modeloConferenciaEncalhe = result.tableModelConferenciaEncalhe;
				
				$.each(modeloConferenciaEncalhe.rows, function(index, value){
					
					var valorExemplares = value.cell.qtdExemplar;
					var inputExemplares = '<input style="width:50px;" value="'+valorExemplares+'"/>';
					value.cell.qtdExemplar = inputExemplares;
					
					
					var valorCodigoDeBarras = value.cell.codigoDeBarras;
					var inputCodigoDeBarras = '<input style="width:150px;" disabled="disabled" value="'+valorCodigoDeBarras+'"/>';
					value.cell.codigoDeBarras = inputCodigoDeBarras;
					
					
					var valorCodigoSM = value.cell.codigoSM;
					var inputCodigoSM = '<input style="width:40px;" disabled="disabled" value="'+valorCodigoSM+'"/>';
					value.cell.codigoSM = inputCodigoSM;

					
					var valorCodigoProduto = value.cell.codigo;
					var inputCodigoProduto = '<input style="width:50px;" disabled="disabled" value="'+valorCodigoProduto+'"/>';
					value.cell.codigo = inputCodigoProduto;


					var valorJuramentada = value.cell.juramentada;

					var inputCheckBoxJuramentada = '';
					
					if(valorJuramentada) {
						inputCheckBoxJuramentada = '<input type="checkbox" checked="checked" name="checkgroup" style="float: left; margin-right: 25px;"/>';
					} else {
						inputCheckBoxJuramentada = '<input type="checkbox" name="checkgroup" style="float: left; margin-right: 25px;"/>';
					}
					
					value.cell.juramentada = inputCheckBoxJuramentada;
					
					
					var imgDetalhar = '<img src="'+contextPath+'/images/ico_detalhes.png" border="0" hspace="3"/>';
					value.cell.detalhes = '<a href="javascript:;" onclick="alert('+index+');">' + imgDetalhar + '</a>';
					

					var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
					value.cell.acao = '<a href="javascript:;" onclick="alert('+index+');">' + imgExclusao + '</a>';
					
				});
				
				return result.tableModelConferenciaEncalhe;
				
			},
			
			pesqProdutosGridModel :  
				[ {
					display : 'Código',
					name : 'codigo',
					width : 50,
					sortable : true,
					align : 'left'
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
					display : 'Chamada Capa',
					name : 'chamadaCapa',
					width : 160,
					sortable : true,
					align : 'left'
				}],
				
				conferenciaEncalheGridModel : [{
					display : 'Exemplares',
					name : 'qtdExemplar',
					width : 65,
					sortable : true,
					align : 'left'
				},{
					display : 'Código de Barras',
					name : 'codigoDeBarras',
					width : 165,
					sortable : true,
					align : 'left'
				}, {
					display : 'SM',
					name : 'codigoSM',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Código',
					name : 'codigo',
					width : 65,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Preço Capa R$',
					name : 'precoCapa',
					width : 70,
					sortable : true,
					align : 'right'
				}, {
					display : 'Desconto R$',
					name : 'desconto',
					width : 50,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total R$',
					name : 'valorTotal',
					width : 50,
					sortable : true,
					align : 'right'
				}, {
					display : 'Dia',
					name : 'dia',
					width : 20,
					sortable : true,
					align : 'center'
				}, {
					display : 'Juramentada',
					name : 'juramentada',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Detalhe',
					name : 'detalhes',
					width : 45,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 30,
					sortable : true,
					align : 'center'
				}], 
				
				pesqProdutosNotaGridModel : 
					
					[ {
						display : 'Código',
						name : 'codigo',
						width : 50,
						sortable : true,
						align : 'left'
					}, {
						display : 'Produto',
						name : 'produto',
						width : 100,
						sortable : true,
						align : 'left'
					}, {
						display : 'Edição',
						name : 'edicao',
						width : 50,
						sortable : true,
						align : 'center'
					}, {
						display : 'Dia',
						name : 'dia',
						width : 70,
						sortable : true,
						align : 'center'
					}, {
						display : 'Qtde. Info',
						name : 'qtdeInformada',
						width : 60,
						sortable : true,
						align : 'center'
					}, {
						display : 'Qtde. Recebida',
						name : 'qtdeRecebida',
						width : 90,
						sortable : true,
						align : 'center'
					}, {
						display : 'Preço Capa R$',
						name : 'precoCapa',
						width : 80,
						sortable : true,
						align : 'right'
					}, {
						display : 'Preço Desc R$',
						name : 'precoDesc',
						width : 80,
						sortable : true,
						align : 'right'
					}, {
						display : 'Total R$',
						name : 'vlrTotal',
						width : 60,
						sortable : true,
						align : 'right'
					}, {
						display : 'Ação',
						name : 'acao',
						width : 30,
						sortable : true,
						align : 'center'
					}],
				
				outrosVlrsGridModel : [{
					display : 'Data',
					name : 'data',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo de Lançamento',
					name : 'tipoLancto',
					width : 140,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor R$',
					name : 'valor',
					width : 100,
					sortable : true,
					align : 'right'
				}]
			
			
	};

	$(function() {
		
		$('#qtdeExemplar').focus();
		
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

		$( "#datepickerDe1" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$(".conferenciaEncalheGrid").flexigrid({
			dataType : 'json',
			colModel : ConferenciaEncalhe.conferenciaEncalheGridModel,
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 250
		});
		
		$(".pesqProdutosGrid").flexigrid({
			dataType : 'json',
			colModel : ConferenciaEncalhe.pesqProdutosGridModel,
			width : 500,
			height : 200
		});
		
		$(".pesqProdutosNotaGrid").flexigrid({
			dataType : 'json',
			colModel : ConferenciaEncalhe.pesqProdutosNotaGridModel,
			width : 810,
			height : 250
		});
		
		$(".outrosVlrsGrid").flexigrid({
			dataType : 'json',
			colModel : ConferenciaEncalhe.outrosVlrsGridModel,
			width : 400,
			height : 250
		});

		var availableTags = [
		         			"4234 - Casa Cláudia - 44344",
		         			"4234 - Casa Cláudia - 43324",
		         			"4234 - Casa Cláudia - 32122",
		         			"2323 - Veja - 3232",
		         			"2323 - Veja - 4431",
		         			"2323 - Veja - 2345",
		         			"2323 - Veja - 5678",
		         			"5665 - Turma da Mônica - 9876",
		         			"5665 - Turma da Mônica - 7767",
		         			"5665 - Turma da Mônica - 5567",
		         			"5665 - Turma da Mônica - 7744",
		         			"5665 - Turma da Mônica - 8765"
		         		];
		         		
   		$( "#pesq_prod" ).autocomplete({
   			source: availableTags
   		});
		
		
	});
	
	function popup_alterar() {
		
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:350,
			width:450,
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

	function popup_alert() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
		$( "#dialog-alert" ).dialog({
			resizable: false,
			height:190,
			width:460,
			modal: true,
			buttons: {
				"Sim": function() {
					$( this ).dialog( "close" );
					popup_notaFiscal();
					$("#vlrCE").focus();
					
				},
				"Não": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};


	function popup_notaFiscal() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-notaFiscal" ).dialog({
			resizable: false,
			height:360,
			width:750,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};


	function popup_dadosNotaFiscal() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-dadosNotaFiscal" ).dialog({
			resizable: false,
			height:'auto',
			width:860,
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

	function popup_pesquisar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-pesquisar" ).dialog({
			resizable: false,
			height:470,
			width:560,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};


	function popup_logado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-logado" ).dialog({
			resizable: false,
			height:180,
			width:460,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$('#pesq_cota').focus();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
					$('#pesq_cota').focus();
				}
			}
		});	
		      
	};


	function popup_editar_produto() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-editar-produto" ).dialog({
			resizable: false,
			height:370,
			width:750,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$('#pesq_cota').focus();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
					$('#pesq_cota').focus();
				}
			}
		});	
		      
	};



	function popup_detalhe_publicacao() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$('#observacao').focus();
		$( "#dialog-detalhe-publicacao" ).dialog({
			resizable: false,
			height:430,
			width:780,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			}
		});	
		      
	};



	function popup_outros_valores() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$('#observacao').focus();
		$( "#dialog-outros-valores" ).dialog({
			resizable: false,
			height:430,
			width:460,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			}
		});	
		      
	};

	function popup_salvarInfos() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-salvar" ).dialog({
			resizable: false,
			height:190,
			width:460,
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
	
	function confirmar(){
		$(".dados").show();
	}
	
	function pesqEncalhe(){
		$(".dadosFiltro").show();
		$(".grids").show();
	}
	
	function pesquisar_cota(){
		$('#pesq_cota').keypress(function(e) { 
		if(e.keyCode == 13) { 
			$('.dadosFiltro').fadeIn('fast');
		} 
	}); 
	}
	function incluir_cod_barras(){
			$('#cod_barras').keypress(function(e) { 
			if(e.keyCode == 13) { 
				$('.dadosIncluir').fadeIn('fast');
				$('#juramentada').focus();
				
			} 
		}); 
	}
	
	function incluir_SM(){
			$('#sm').keypress(function(e) { 
			if(e.keyCode == 13) { 
				$('.dadosIncluir').fadeIn('fast');
				$('#qtdeExemplar').focus();				
			} 
		}); 
	}
	
	function incluir_qtde(){
			$('#qtdeExemplar').keypress(function(e) { 
			if(e.keyCode == 13) { 
				
			} 
		}); 
	}
	
	function pesquisar_produtos(){
			$('#codProduto').keypress(function(e) { 
			if(e.keyCode == 13) { 
				popup_pesquisar();
			} 
		}); 
	}
	
	
	function incluir_grid(){
		pesqEncalhe();
		$(".conferenciaEncalheGrid #row1").show();
		}
	function excluir_grid(){
		$(".conferenciaEncalheGrid #row1").hide();
		}
		
		function excluir_grid_1(){
		$(".conferenciaEncalheGrid #row4").hide();
		}
	
	function mostrar_produtos(){
			$('#pesq_prod').keypress(function(e) { 
			if(e.keyCode == 13) { 
				$(".conferenciaEncalheGrid #row1").show();
				$("#dialog-pesquisar" ).dialog( "destroy" );
				$('.dadosIncluir').fadeIn('fast');
				$('#codProduto').focus();				
			} 
			
		}); 
	}
	
	function fechar_produtos(){
			$('.pesqProdutosGrid').keypress(function(e) { 
			if(e.keyCode == 13) { 
				$(".itensPesquisados").show();
			} 
		}); 
	}
	
	
	
	
	function pesqMostraCota(){
		$('#pesq_cota').keypress(function(e) { 
				if(e.keyCode == 13) { 
					$('.dadosFiltro').fadeIn('fast');
					popup_alert();
				} 
			}); 
		}
		
		function vaiVlr(){
		$('#vlrCE').keypress(function(e) { 
				if(e.keyCode == 13) { 
					$("#qtdeExemplar").focus();
				} 
			}); 
		}
		
		function gravaObs(){
		$('#observacao').keypress(function(e) { 
				if(e.keyCode == 13) { 
					$("#observacao").fadeOut();
					$(".obs").fadeIn("slow");
					$(".tit").fadeOut("slow");
					$("#btObs").fadeOut("slow");
				} 
			}); 
		}
		
	function validarVlr(){
		$("#effect_1").fadeIn("slow");
		}
	function incluirObs(){
		$(".obs").fadeIn("slow");
		$(".tit").fadeOut("slow");
		$("#btObs").fadeOut("slow");
		
		}
	shortcut.add("F2",function() 
	{
	$(".conferenciaEncalheGrid #row4").show();
	$('#qtdeExemplar4').focus();
	
	});
	shortcut.add("F6",function() 
	{
	popup_notaFiscal();
	});
	
	shortcut.add("F8",function() 
	{
	popup_dadosNotaFiscal();
	});
	shortcut.add("F9",function() 
	{
	popup_salvarInfos();
	});
	

	


</script>

</head>

<body>

	<jsp:include page="dialog.jsp" />

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">

			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info"> </span> <b>Conferência de Encalhe
					< evento > com < status >.</b>

			</p>

		</div>

		<fieldset class="classFieldset">

			<legend> Pesquisar Encalhe</legend>

			<table width="950" border="0" cellspacing="2" cellpadding="2"
				class="filtro">
				<tr>
					<td width="40">Cota:</td>
					<td width="121">
						<input 
							type="text"
							id="numeroCota"
							style="width: 80px; 
							float: left; 
							margin-right: 5px;"/> 
						
						<span class="classPesquisar">
							<a href="javascript:;" onclick="ConferenciaEncalhe.pesquisarCota();">&nbsp;</a> 
						</span>
					
					</td>
					
					<td colspan="2">
						<span class="dadosFiltro">
							CGB Distribuidora de Jorn e Rev
						</span>
					</td>
					<td width="44"><span class="dadosFiltro">Status:</span></td>
					<td width="91"><span class="dadosFiltro">Ativo</span></td>
					<td width="144"><span class="dadosFiltro">Valor CE
							Jornaleiro R$:</span></td>
					<td width="100"><span class="dadosFiltro"><input
							type="text" onkeypress="vaiVlr();" name="vlrCE" id="vlrCE"
							style="width: 100px; text-align: right;" /> </span></td>
				</tr>
			</table>

			<table width="949" border="0" cellpadding="0" cellspacing="1"
				class="filtro nfes">

				<tr>
					<td width="88">Campo Chave:</td>
					<td width="118" align="left"><input type="text"
						name="textfield11" id="textfield11" style="width: 80px;" /></td>
					<td width="118" align="left">Data da Emissão:</td>
					<td width="94" align="left"><input name="textfield9"
						type="text" id="textfield9" style="width: 80px;" value="" /></td>
					<td width="105" align="left">Valor da Nota R$:</td>
					<td width="309" align="left"><input name="textfield15"
						type="text" id="textfield15"
						style="width: 80px; text-align: right;" value="" /></td>
					<td width="109"><span class="bt_pesquisar"><a
							href="javascript:;" onclick="mostrar();pesqEncalhe();">Pesquisar</a>
					</span></td>
				</tr>

			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">

			<table width="950" border="0" cellspacing="1" cellpadding="1">

				<tr>
					<td width="126"><span class="bt_novos" title="Contingência"><a
							href="../Recolhimento/conferencia_encalhe_jornaleiro_contingencia.htm"><img
								border="0" hspace="5" src="../images/ico_expedicao_box.gif" />Contingência</a>
					</span></td>
					<td width="314">&nbsp;</td>
					<td width="60" align="center" bgcolor="#F4F4F4"><strong>Atalhos:</strong>
					</td>
					<td width="102" bgcolor="#F8F8F8"><strong>F2</strong>-Novo
						Produto</td>
					<td width="119" bgcolor="#F8F8F8"><strong>F6</strong>-Nova
						Nota Fiscal</td>
					<td width="145" bgcolor="#F8F8F8"><strong>F8</strong>-Finalizar
						Conferência</td>
					<td width="62" bgcolor="#F8F8F8"><strong>F9-</strong>Salvar</td>
				</tr>

			</table>

		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">

			<legend>Encalhe</legend>

			<table width="950" border="0" cellspacing="1" cellpadding="2">
				<tr class="header_table">
					<td width="65" align="center"
						style="border-left: 1px solid #666; border-top: 1px solid #666;">Qtde</td>
					<td width="168" align="center" style="border-top: 1px solid #666;">Código
						de Barras</td>
					<td width="42" align="center" style="border-top: 1px solid #666;">SM</td>
					<td width="107" align="center"
						style="border-top: 1px solid #666; border-right: 1px solid #666;">Código</td>
					<td width="158">Produto</td>
					<td width="69" align="center">Edição</td>
					<td width="93">Preço Capa R$</td>
					<td width="79" align="center">Desc. R$</td>
					<td width="87" align="center">Valor Total R$</td>
					<td width="31">&nbsp;</td>
				</tr>
				<tr>
					<td class="class_linha_1" align="center"
						style="border-left: 1px solid #666; border-bottom: 1px solid #666;"><input
						name="qtdeExemplar" type="text" id="qtdeExemplar"
						style="width: 60px; text-align: center;"
						onkeypress="incluir_qtde();" /></td>
					<td class="class_linha_1" align="center"
						style="border-bottom: 1px solid #666;"><input
						name="cod_barras" type="text" id="cod_barras"
						style="width: 160px;" value="" onkeypress="incluir_cod_barras();" />
					</td>
					<td class="class_linha_1" align="center"
						style="border-bottom: 1px solid #666;"><input name="sm"
						type="text" id="sm" style="width: 40px;"
						onkeypress="incluir_SM();" /></td>
					<td class="class_linha_1" align="center"
						style="border-bottom: 1px solid #666; border-right: 1px solid #666;"><input
						name="codProduto" type="text" id="codProduto"
						style="width: 100px;" onkeypress="popup_pesquisar();" /></td>
					<td class="class_linha_2"><span class="dadosIncluir">Veja</span>
					</td>
					<td class="class_linha_2" align="center"><span
						class="dadosIncluir">98989</span></td>
					<td class="class_linha_2" align="center"><span
						class="dadosIncluir">12,95</span></td>
					<td class="class_linha_2" align="center"><span
						class="dadosIncluir">2,50</span></td>
					<td class="class_linha_2" align="center"><span
						class="dadosIncluir">129,50</span></td>
					<td align="center"><a href="javascript:;" class="ok_filtro"
						onclick="incluir_grid();"><img src="../images/bt_check.gif"
							alt="Incluir" width="22" height="22" border="0" /> </a></td>
				</tr>
			</table>



			<div class="grids"
				style="display: block; clear: left; margin-top: 10px;">

				<table class="conferenciaEncalheGrid"></table>

				<br clear="all" />

				<table width="950" border="0" cellspacing="1" cellpadding="1">

					<tr>
						<td width="51"><strong>Reparte:</strong></td>

						<td width="85">91.169,21</td>

						<td width="83"><strong> ( - ) Encalhe:</strong></td>

						<td width="87">91.209,07</td>

						<td width="126" align="center" bgcolor="#EFEFEF"
							style="border: 1px solid #000;"><strong>( = )Valor
								Venda Dia:</strong></td>

						<td width="80" align="center" bgcolor="#EFEFEF"
							style="border: 1px solid #000;">99.155,28</td>

						<td width="120">&nbsp;&nbsp; <strong> <a
								href="javascript:;" onclick="popup_outros_valores();"> ( +
									)Outros valores </a>: </strong></td>

						<td width="68">99.209,07</td>

						<td width="122"><strong>( = )Valor a pagar R$:</strong></td>

						<td width="77">99.364,35</td>

						<td width="17">&nbsp;</td>

					</tr>

				</table>

			</div>

		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>

</body>