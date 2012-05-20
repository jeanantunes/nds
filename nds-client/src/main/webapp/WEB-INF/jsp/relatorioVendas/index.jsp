<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>NDS - Novo Distrib</title>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script language="javascript" type="text/javascript">
	$(function() {
		$("#datepickerDe")
				.datepicker(
						{
							showOn : "button",
							buttonImage : "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
							buttonImageOnly : true
						});
		$("#datepickerAte")
				.datepicker(
						{
							showOn : "button",
							buttonImage : "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
							buttonImageOnly : true
						});
	});

	function pesquisar() {
		if ($('#filtro_produto').attr("checked") == "checked") {
			
		} else if ($('#filtro_editor').attr("checked") == "checked") {
			
		} else if ($('#filtro_produto').attr("checked") == "checked") {
			
		} else if ($('#filtro_cota').attr("checked") == "checked") {
			
		}
	}
	
	function mostra_pesq_avancada() {
		$('#pesquisaAvancada').fadeIn("slow");
	}
	function esconde_pesq_avancada() {
		$('#pesquisaAvancada').fadeOut("slow");
	}

	function mostra_distrib() {
		$('#relatorioDistribuidor').show();
		$('#relatorioEditor').hide();
		$('#relatorioProduto').hide();
		$('#relatorioCota').hide();
		$('.linhaCota').hide();
		$('.linhaProduto').hide();
	}
	function mostra_editor() {
		$('#relatorioDistribuidor').hide();
		$('#relatorioEditor').show();
		$('#relatorioProduto').hide();
		$('#relatorioCota').hide();
		$('.linhaCota').hide();
		$('.linhaProduto').hide();
	}
	function mostra_produto() {
		$('#relatorioDistribuidor').hide();
		$('#relatorioEditor').hide();
		$('#relatorioProduto').show();
		$('#relatorioCota').hide();
		$('.linhaCota').hide();
		$('.linhaProduto').show();

	}
	function mostra_cota() {
		$('#relatorioDistribuidor').hide();
		$('#relatorioEditor').hide();
		$('#relatorioProduto').hide();
		$('#relatorioCota').show();
		$('.linhaCota').show();
		$('.linhaProduto').hide();
	}

	function popup_editor() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-editor").dialog({
			resizable : false,
			height : 450,
			width : 600,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");

				},

			}
		});
	};

	$(function() {
		var availableTags = [ "4455", "4566", "4567", "5678", "1223", "1234" ];
		function split(val) {
			return val.split(/,\s*/);
		}
		function extractLast(term) {
			return split(term).pop();
		}

		$("#edicoesCamp")
		// don't navigate away from the field on tab when selecting an item
		.bind(
				"keydown",
				function(event) {
					if (event.keyCode === $.ui.keyCode.TAB
							&& $(this).data("autocomplete").menu.active) {
						event.preventDefault();
					}
				}).autocomplete(
				{
					minLength : 0,
					source : function(request, response) {
						// delegate back to autocomplete, but extract the last term
						response($.ui.autocomplete.filter(availableTags,
								extractLast(request.term)));
					},
					focus : function() {
						// prevent value inserted on focus
						return false;
					},
					select : function(event, ui) {
						var terms = split(this.value);
						// remove the current input
						terms.pop();
						// add the selected item
						terms.push(ui.item.value);
						// add placeholder to get the comma-and-space at the end
						terms.push("");
						this.value = terms.join(", ");
						return false;
					}
				});
	});
</script>
</head>
<body>

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info"></span> <b>Parcial < evento > com
					< status >.</b>
			</p>
		</div>

		<fieldset class="classFieldset">
			<legend> Relat�rio de Vendas</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="20"><input type="radio" name="filtro"
						id="filtro_distrib" onclick="mostra_distrib();" value="radio" /></td>
					<td colspan="3"><label for="filtro_distrib">Curva ABC
							Distribuidor</label></td>
					<td width="20"><input type="radio" name="filtro"
						id="filtro_editor" value="radio" onclick="mostra_editor();" /></td>
					<td width="94"><label for="filtro_editor">Curva ABC
							Editor</label></td>
					<td width="20"><input type="radio" name="filtro"
						id="filtro_produto" onclick="mostra_produto();" value="radio" /></td>
					<td width="114"><label for="filtro_produto">Curva ABC
							Produto</label></td>
					<td width="21" align="right"><input type="radio" name="filtro"
						id="filtro_cota" value="radio" onclick="mostra_cota();" /></td>
					<td width="90"><label for="filtro_cota">Curva ABC Cota</label></td>
					<td width="47">Per�odo:</td>
					<td width="86"><input type="text" name="datepickerDe"
						id="datepickerDe" style="width: 60px;" /></td>
					<td width="24">At�:</td>
					<td width="87"><input type="text" name="datepickerAte"
						id="datepickerAte" style="width: 60px;" /></td>
					<td width="104" rowspan="3" valign="top"><span
						class="bt_pesquisar"><a href="javascript:;"
							onclick="mostrar();">Pesquisar</a></span></td>
					<td width="20" rowspan="3" align="center" valign="top"><a
						href="javascript:;" onclick="mostra_pesq_avancada();"><img
							src="../images/ico_pesq_avancada.jpg" alt="Pesquisa Avan�ada"
							width="20" height="20" vspace="10" border="0" /></a></td>
				</tr>
				<tr class="linhaCota">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="6"><label>Cota:</label> <input type="text"
						style="width: 80px; float: left; margin: 5px;" /> <label>Nome:</label>
						<input type="text" style="width: 200px; float: left; margin: 5px;" />
					</td>
				</tr>
				<tr class="linhaProduto">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="8"><label>C�digo:</label> <input type="text"
						style="width: 80px; float: left; margin: 5px;" /> <label>Produto:</label>
						<input type="text" style="width: 200px; float: left; margin: 5px;" />
					</td>
				</tr>
			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset" id="pesquisaAvancada"
			style="display: none;">
			<legend>Busca Avan�ada</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="69">Fornecedor:</td>
					<td width="255"><select name="select" id="select3"
						style="width: 240px;">
							<option>Todos</option>
							<option>Dinap</option>
							<option>FC</option>
					</select></td>
					<td width="47" colspan="-1">C�digo:</td>
					<td width="108"><input type="text" style="width: 80px;" /></td>
					<td width="52">Produto:</td>
					<td width="213"><input type="text" style="width: 200px;" /></td>
					<td width="41">Edi��o:</td>
					<td><input type="text" style="width: 100px;" id="edicoesCamp" /></td>
					<td><a href="javascript:;" onclick="esconde_pesq_avancada();"><img
							src="../images/ico_excluir.gif" alt="Fechar" width="15"
							height="15" border="0" /></a></td>
				</tr>
				<tr>
					<td>Editor:</td>
					<td><select name="select2" id="select" style="width: 240px;">
							<option>Todos</option>
					</select></td>
					<td colspan="-1">Cota:</td>
					<td><input type="text" name="datepickerAte2"
						id="datepickerAte2" style="width: 80px;" /></td>
					<td>Nome:</td>
					<td><input type="text" style="width: 200px;" /></td>
					<td>&nbsp;</td>
					<td width="104">&nbsp;</td>
					<td width="15">&nbsp;</td>
				</tr>
				<tr>
					<td>Municipio:</td>
					<td><select name="select3" id="select4" style="width: 240px;">
							<option selected="selected">Todos</option>
					</select></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;"
							onclick="pesquisar();">Pesquisar</a></span></td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<div class="grids" style="display: none;">
			<fieldset class="classFieldset" id="relatorioDistribuidor"
				style="display: none;">
				<legend>Curva ABC Distribuidor</legend>
				<table class="abcDistribuidorGrid"></table>

				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441"><span class="bt_novos" title="Gerar Arquivo"><a
								href="javascript:;"><img src="../images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="javascript:;"><img
									src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="121"><strong>Total:</strong></td>
						<td width="130">1.320</td>
						<td width="258">R$ 52.550,00</td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioEditor"
				style="display: none;">
				<legend>Curva ABC Editor</legend>
				<table class="abcEditorGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="329"><span class="bt_novos" title="Gerar Arquivo"><a
								href="javascript:;"><img src="../images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="javascript:;"><img
									src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="80"><strong>Total:</strong></td>
						<td width="215">1.320</td>
						<td width="326">R$ 52.550,00</td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioProduto"
				style="display: none;">
				<legend>Curva ABC Produto</legend>

				<table class="abcProdutoGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441"><span class="bt_novos" title="Gerar Arquivo"><a
								href="javascript:;"><img src="../images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="javascript:;"><img
									src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="151"><strong>Total:</strong></td>
						<td width="114">R$ 52.550,00</td>
						<td width="244">R$ 52.550,00</td>
					</tr>
				</table>

			</fieldset>

			<fieldset class="classFieldset" id="relatorioCota"
				style="display: none;">
				<legend>Curva ABC Cota: 4444 - Jos� da Silva</legend>

				<table class="abcCotaGrid"></table>

				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="432"><span class="bt_novos" title="Gerar Arquivo"><a
								href="javascript:;"><img src="../images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="javascript:;"><img
									src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="73"><strong>Total:</strong></td>
						<td width="205">1.320</td>
						<td width="240">R$ 52.550,00</td>
					</tr>
				</table>
			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>

			<div class="linha_separa_fields">&nbsp;</div>
		</div>

	</div>

	<script>
		$(".popEditorGrid").flexigrid({
			url : '../xml/pop-editor-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'C�digo',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi��o',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vdaExempl',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percVenda',
				width : 50,
				sortable : true,
				align : 'right'
			} ],

			width : 560,
			height : 255
		});

		$(".abcEditorGrid").flexigrid({
			url : '../xml/abc-editor-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'C�digo',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'editor',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExempl',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda Exs.',
				name : 'percExempl',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapa',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 72,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'partAcumulada',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Hist.',
				name : 'hist',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcDistribuidorGrid").flexigrid({
			url : '../xml/abc-distribuidor-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Munic�pio',
				name : 'municipio',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde PDV�s',
				name : 'qtdePdv',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vdaExempl',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapa',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 52,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'partAcumulada',
				width : 90,
				sortable : true,
				align : 'right'
			} ],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcProdutoGrid").flexigrid({
			url : '../xml/abc-produtos-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Munic�pio',
				name : 'municipio',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde PDV�s',
				name : 'qtdePdv',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exempl. R$',
				name : 'vdaExempl',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'partAcumulada',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcCotaGrid").flexigrid({
			url : '../xml/abc-cotas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'C�digo',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi��o',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vdaExempl',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda %',
				name : 'percVda',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'partAcumulada',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "produto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
	</script>
</body>
</html>
