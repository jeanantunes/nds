var CadastroCalendario = $.extend(true, {

	mesPesquisaFeriados : null,
	
	datasDestacar :  {},

	init : function() {

		$("#dtFeriadoNovo", CadastroCalendario.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: 'dd/mm/yy',
		});
		$("#dtFeriadoNovo", CadastroCalendario.workspace).mask("99/99/9999");	
		
		CadastroCalendario.recarregarPainelCalendarioFeriado();
		
		$(".diaFeriadoGrid", CadastroCalendario.workspace).flexigrid({
			colModel : [ {
				display : 'Dia',
				name : 'dataFeriado',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Feriado',
				name : 'tipoFeriado',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'nomeCidade',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Descrição',
				name : 'descricaoFeriado',
				width : 180,
				sortable : true,
				align : 'left',
			}, {
				display : 'Opera',
				name : 'indOpera',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cobrança',
				name : 'indEfetuaCobranca',
				width : 45,
				sortable : true,
				align : 'center'
			}, {
				display : 'Anual',
				name : 'indRepeteAnualmente',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 650,
			height : 120
		});
		
		
		$(".mesFeriadoGrid", CadastroCalendario.workspace).flexigrid({
			colModel : [ {
				display : 'Dia',
				name : 'dataFeriado',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Feriado',
				name : 'tipoFeriado',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'nomeCidade',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Descrição',
				name : 'descricaoFeriado',
				width : 220,
				sortable : true,
				align : 'left',
			}, {
				display : 'Opera',
				name : 'indOpera',
				width : 35,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cobrança',
				name : 'indEfetuaCobranca',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Anual',
				name : 'indRepeteAnualmente',
				width : 35,
				sortable : true,
				align : 'center'
			}],
			width : 650,
			height : 120
		});
		
	$("#anoVigenciaPesquisa", CadastroCalendario.workspace).keyup(function (e){
			
			if (e.keyCode == 13) {
				CadastroCalendario.recarregarPainelCalendarioFeriado();
			}
		});
		
		
		
	},	

		recarregarDiaFeriadoGrid : function(dataFeriado) {
			
			$(".diaFeriadoGrid", CadastroCalendario.workspace).flexOptions({
				preProcess: CadastroCalendario.preProcessarFeriados,
				url: contextPath + '/administracao/cadastroCalendario/obterListaCalendarioFeriado',
				dataType : 'json',
				params:[{name:'data', value: dataFeriado}]
			});
			
			$(".diaFeriadoGrid", CadastroCalendario.workspace).flexReload();			
		},
		
		carregarPopUpFeriadosMes : function(mes) {
			
			if (CadastroCalendario.mesPesquisaFeriados == null) {
				CadastroCalendario.mesPesquisaFeriados = mes;
			}
			CadastroCalendario.recarregarMesFeriadoGrid();
			
			$( "#dialog-feriado-mes", CadastroCalendario.workspace ).dialog({
				
				resizable: false,
				
				width:700,
				
				modal: true,
				
				buttons: {
					
					"Fechar": function() {
						CadastroCalendario.mesPesquisaFeriados = null;
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-feriado-mes", this.workspace).parents("form")
			});
		},
		
		carregarDadosFeriadoDefault: function(feriadoDefault) {
			
			if(typeof feriadoDefault == 'undefined') {
				return;
			}
			
			var dtFeriado = feriadoDefault.dataFeriado;
			var tipoFeriado = feriadoDefault.tipoFeriado;
			var descricaoFeriado = feriadoDefault.descricaoFeriado;
			var idLocalidade = feriadoDefault.idLocalidade;

			var indOpera = feriadoDefault.indOpera;
			var indRepeteAnualmente = feriadoDefault.indRepeteAnualmente;
			var indEfetuaCobranca = feriadoDefault.indEfetuaCobranca;
			
			$("#dtFeriado", CadastroCalendario.workspace).val(dtFeriado);
			$("#tipos_feriado_dialog_editar", CadastroCalendario.workspace).val(tipoFeriado);
			$("#descricao", CadastroCalendario.workspace).val(descricaoFeriado);
			
			$("#indOpera", CadastroCalendario.workspace).attr('checked', indOpera);
			$("#indRepeteAnualmente", CadastroCalendario.workspace).attr('checked', indRepeteAnualmente);
			$("#indEfetuaCobranca", CadastroCalendario.workspace).attr('checked', indEfetuaCobranca);
			
			if(tipoFeriado == 'MUNICIPAL') {
				$("#cidades_dialog_editar", CadastroCalendario.workspace).removeAttr('disabled');
				$("#cidades_dialog_editar", CadastroCalendario.workspace).val(idLocalidade);
			} else {
				$("#cidades_dialog_editar", CadastroCalendario.workspace).val('');
				$("#cidades_dialog_editar", CadastroCalendario.workspace).attr('disabled', 'disabled');
			}
			
			
		},
		
		editarFeriado : function(
				dtFeriado, 
				tipoFeriado, 
				idLocalidade, 
				indRepeteAnualmente, 
				indOpera, 
				indEfetuaCobranca, 
				descricaoFeriado) {
						
			$("#dtFeriado", CadastroCalendario.workspace).val(dtFeriado);
			$("#tipos_feriado_dialog_editar", CadastroCalendario.workspace).val(tipoFeriado);
			$("#cidades_dialog_editar", CadastroCalendario.workspace).val(idLocalidade);
			
			$("#descricao", CadastroCalendario.workspace).val(descricaoFeriado);
			
			$("#indOpera", CadastroCalendario.workspace).attr("checked", indOpera );
			$("#indEfetuaCobranca", CadastroCalendario.workspace).attr("checked", indEfetuaCobranca);
			$("#indRepeteAnualmente", CadastroCalendario.workspace).attr("checked", indRepeteAnualmente);
			
		},
		
		excluirFeriado : function(
				dtFeriado, 
				tipoFeriado, 
				idLocalidade, 
				indRepeteAnualmente, 
				indOpera, 
				indEfetuaCobranca, 
				descricaoFeriado) {
			
					var parametrosPesquisa = 
						[ {name : "dtFeriado", value : dtFeriado},
						  {name : "descTipoFeriado", value : tipoFeriado},
						  {name : "idLocalidade", value : idLocalidade},
						  {name : "indRepeteAnualmente", value : indRepeteAnualmente}];
					
					$.postJSON(
							
							contextPath + '/administracao/cadastroCalendario/excluirCadastroFeriado', 
							
							parametrosPesquisa,
							
							function(result) {
								
								CadastroCalendario.limparCamposEdicaoCadastroFeriado();
								
								CadastroCalendario.recarregarDiaFeriadoGrid(dtFeriado);
								
								CadastroCalendario.recarregarPainelCalendarioFeriado();
								
							}, null, true, 'dialog-editar');
					
				},
		
		concatenarChar : function(valor){
			
			if(typeof valor == 'undefined') {
				return "\'\'";
			}
			
			if(valor == null) {
				return "\'\'";
			}
			
			return "\'" + valor + "\'";
			
		},
		
		
		preProcessarMesFeriado : function(result) {
			
			$.each(result.rows, function(index, value) {
				
				var cell = value.cell;

				if(index == 0) {
					CadastroCalendario.carregarDadosFeriadoDefault(cell);
				}

				cell.indOpera = (cell.indOpera == false) ?  'Não' : 'Sim';
				cell.indRepeteAnualmente = (cell.indRepeteAnualmente  == false) ?  'Não' : 'Sim';
				cell.indEfetuaCobranca = (cell.indEfetuaCobranca) == false ?  'Não' : 'Sim';
				cell.nomeCidade = (typeof cell.nomeCidade == 'undefined') ? '-' : cell.nomeCidade;

				
			});
			
			return result;
			
		},
		
		
		preProcessarFeriados : function(result) {
			
			$.each(result.rows, function(index, value) {
				
				var cell = value.cell;

				if(index == 0) {
					CadastroCalendario.carregarDadosFeriadoDefault(cell);
				}

				var dtFeriado 	 			= CadastroCalendario.concatenarChar(cell.dataFeriado);
				var tipoFeriado  			= CadastroCalendario.concatenarChar(cell.tipoFeriado);
				var idLocalidade 			= CadastroCalendario.concatenarChar(cell.idLocalidade);
				var indRepeteAnualmente  	= cell.indRepeteAnualmente;
				var indOpera 				= cell.indOpera;
				var indEfetuaCobranca 		= cell.indEfetuaCobranca;
				var descricaoFeriado		= CadastroCalendario.concatenarChar(cell.descricaoFeriado);

				var parametros = [ dtFeriado, tipoFeriado, idLocalidade, indRepeteAnualmente, indOpera, indEfetuaCobranca, descricaoFeriado ];
				
				var imgDetalhar = '<img src="' + contextPath + '/images/ico_detalhes.png" border="0" hspace="3"/>';
				cell.acao = '<a href="javascript:;" onclick="CadastroCalendario.editarFeriado('+ parametros +');">' + imgDetalhar + '</a>';
				
				var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
				cell.acao += '<a href="javascript:;" onclick="CadastroCalendario.excluirFeriado(' + parametros + ');">' + imgExclusao + '</a>';

				cell.indOpera = (cell.indOpera == false) ?  'N' : 'S';
				cell.indRepeteAnualmente = (cell.indRepeteAnualmente  == false) ?  'N' : 'S';
				cell.indEfetuaCobranca = (cell.indEfetuaCobranca) == false ?  'N' : 'S';
				cell.nomeCidade = (typeof cell.nomeCidade == 'undefined') ? '-' : cell.nomeCidade;

				
			});
			
			return result;
			
		},
		
		limparCamposEdicaoCadastroFeriado : function() {
			
			$("#tipos_feriado_dialog_editar", CadastroCalendario.workspace).val('');
			$("#descricao", CadastroCalendario.workspace).val('');
			
			$("#indOpera", CadastroCalendario.workspace).attr('checked', false);
			$("#indRepeteAnualmente", CadastroCalendario.workspace).attr('checked', false);
			$("#indEfetuaCobranca", CadastroCalendario.workspace).attr('checked', false);
			
		},
		
		popupEdicaoCadastroFeriado : function(date, dates) {	
			
			CadastroCalendario.limparCamposEdicaoCadastroFeriado();
			
			CadastroCalendario.recarregarDiaFeriadoGrid(date);
			
			$( "#dialog-editar", CadastroCalendario.workspace ).dialog({
				
				resizable: false,
				
				height:580,
				
				width:700,
				
				modal: true,
				
				buttons: {
					
					"Confirmar": function() {
						
						CadastroCalendario.cadastrarFeriado(false);
						
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-editar", this.workspace).parents("form")
			});
			
			$( "#dtFeriado" ).val(date);
			
		},
		
		bloquearComboMunicipio : function() {
			
			var tipoFeriado = $("#tipos_feriado_dialog_editar", CadastroCalendario.workspace).val(); 
			
			if(tipoFeriado == 'MUNICIPAL') {
				$("#cidades_dialog_editar", CadastroCalendario.workspace).removeAttr('disabled');
			} else {
				$("#cidades_dialog_editar", CadastroCalendario.workspace).val('');
				$("#cidades_dialog_editar", CadastroCalendario.workspace).attr('disabled', 'disabled');
			}

			tipoFeriado = $("#tipos_feriado_dialog_novo", CadastroCalendario.workspace).val(); 
			
			if(tipoFeriado == 'MUNICIPAL') {
				$("#cidades_dialog_novo", CadastroCalendario.workspace).removeAttr('disabled');
			} else {
				$("#cidades_dialog_novo", CadastroCalendario.workspace).val('');
				$("#cidades_dialog_novo", CadastroCalendario.workspace).attr('disabled', 'disabled');
			}
			
		},
		
		cadastrarFeriado : function(fromPopUpNovoFeriado){
			
			var dtFeriado = null;
			var tipoFeriado = null;
			var idLocalidade = null;
			var descricao = null;
			var indOpera = null;
			var indEfetuaCobranca = null;
			var indRepeteAnualmente = null;

			
			if(fromPopUpNovoFeriado) {
				
				dtFeriado = $("#dtFeriadoNovo", CadastroCalendario.workspace).val();
				tipoFeriado = $("#tipos_feriado_dialog_novo", CadastroCalendario.workspace).val();
				idLocalidade = $("#cidades_dialog_novo", CadastroCalendario.workspace).val();
				descricao = $("#descricaoNovo", CadastroCalendario.workspace).val();
				indOpera = $("#indOperaNovo", CadastroCalendario.workspace).is(":checked");
				indEfetuaCobranca = $("#indEfetuaCobrancaNovo", CadastroCalendario.workspace).is(":checked");
				indRepeteAnualmente = $("#indRepeteAnualmenteNovo", CadastroCalendario.workspace).is(":checked");
				
			
				
			} else {

				dtFeriado = $("#dtFeriado", CadastroCalendario.workspace).val();
				tipoFeriado = $("#tipos_feriado_dialog_editar", CadastroCalendario.workspace).val();
				idLocalidade = $("#cidades_dialog_editar", CadastroCalendario.workspace).val();
				descricao = $("#descricao", CadastroCalendario.workspace).val();
				indOpera = $("#indOpera", CadastroCalendario.workspace).is(":checked");
				indEfetuaCobranca = $("#indEfetuaCobranca", CadastroCalendario.workspace).is(":checked");
				indRepeteAnualmente = $("#indRepeteAnualmente", CadastroCalendario.workspace).is(":checked");
				
			}
			
			var parametrosCadastro = [
			                     {name: 'dtFeriado', 			value: dtFeriado},
			                     {name: 'descTipoFeriado', 		value: tipoFeriado},
			                     {name: 'idLocalidade', 		value: idLocalidade},
			                     {name: 'descricao', 			value: descricao},
			                     {name: 'indOpera', 			value: indOpera},
			                     {name: 'indEfetuaCobranca', 	value: indEfetuaCobranca},
			                     {name: 'indRepeteAnualmente', 	value: indRepeteAnualmente}];
			
			
			var idDialog = fromPopUpNovoFeriado ? 'dialog-novo' : 'dialog-editar';
			
			$.postJSON(
					
					contextPath + '/administracao/cadastroCalendario/cadastrarFeriado', 
					
					parametrosCadastro,
					
					function(result) {
						
						if(fromPopUpNovoFeriado) {
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
							$( "#dialog-novo", CadastroCalendario.workspace ).dialog( "close" );
							CadastroCalendario.recarregarMesFeriadoGrid();
						} else {
							CadastroCalendario.recarregarDiaFeriadoGrid(dtFeriado);
							exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, 'dialog-editar');
						}

						CadastroCalendario.recarregarPainelCalendarioFeriado();
						
					}, null, true, idDialog);
			
		},
		
		recarregarMesFeriadoGrid: function() {
			$(".mesFeriadoGrid", CadastroCalendario.workspace).flexOptions({
				preProcess: CadastroCalendario.preProcessarMesFeriado,
				url: contextPath + '/administracao/cadastroCalendario/obterFeriadosDoMes',
				dataType : 'json',
				params:[{name:'mes', value: CadastroCalendario.mesPesquisaFeriados}]
			
			});
			
			$(".mesFeriadoGrid", CadastroCalendario.workspace).flexReload();
		},
		
		popupNovoCadastroFeriado: function() {
		
			$( "#dialog-novo", CadastroCalendario.workspace ).dialog({
				
				resizable: false,
				height:370,
				width:430,
				modal: true,
				buttons: {
					
					"Confirmar": function() {
						
						CadastroCalendario.cadastrarFeriado(true);
						
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-novo", this.workspace).parents("form")
			});
		},
		
		
		popup_excluir : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-excluir", CadastroCalendario.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-excluir", this.workspace).parents("form")
			});
		},
		
		highlightDays : function(date) {
			
			var diaDoMes = date.getDate();
			
			if(diaDoMes < 10) {
				diaDoMes = ('0'+ diaDoMes);
			}
			
			
			var mesDoAno = (date.getMonth() + 1);
			
			if(mesDoAno < 10) {
				mesDoAno = ('0' + mesDoAno);
			}
			
			var search = diaDoMes   + "/" + mesDoAno + "/" + date.getFullYear();
			
			jQuery.inArray(search, CadastroCalendario.datasDestacar);
			
			if (CadastroCalendario.datasDestacar[search]) {
		           return [true, 'highlight', CadastroCalendario.datasDestacar[search] || ''];
		    }
	        
	        return [true, ''];
	        
	     },
	     
	     recarregarPainelCalendarioFeriado : function() {
	    	 
	    	 $("#feriadosWrapper", CadastroCalendario.workspace).empty();
	 		
	 		 $("#feriadosWrapper", CadastroCalendario.workspace).append($("<div>").attr("id", "feriados"));
	    	 
	    	 var anoVigencia = $('#anoVigenciaPesquisa', CadastroCalendario.workspace).val();
	    	 
	    	 parametroPesquisa = [{name: "anoVigencia", value: anoVigencia}];
	    	 
	    	 $.postJSON(contextPath + '/administracao/cadastroCalendario/obterFeriados', parametroPesquisa, 
	    				
	    				function(conteudo) {
	    					
	    					CadastroCalendario.datasDestacar = conteudo.datasDestacar;
	    					
	    					var dataInicialCalendario = new Date(conteudo.anoVigencia, 0, 1);
	    					
	    					var dataFinalCalendario = new Date(conteudo.anoVigencia, 11, 31);
	    					
	    					$( "#feriados", CadastroCalendario.workspace ).datepicker({
	    						
	    						numberOfMonths: 12,
	    						minDate: dataInicialCalendario, 
	    						maxDate: dataFinalCalendario, 
	    						showButtonPanel: false,
	    						showMonthAfterYear : false,
	    						altField: "#alternate",
	    						dateFormat: "dd/mm/yy",
	    						dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado','Domingo'],
	    				        dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
	    				        dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
	    				        
	    				        monthNames: ['<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(1)">Janeiro</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(2)">Fevereiro</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(3)">Março</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(4)">Abril</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(5)">Maio</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(6)">Junho</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(7)">Julho</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(8)">Agosto</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(9)">Setembro</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(10)">Outubro</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(11)">Novembro</a>',
	    				                     '<a href="javascript:;" onclick="CadastroCalendario.carregarPopUpFeriadosMes(12)">Dezembro</a>'
	    				                     ],
	    				        monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set', 'Out','Nov','Dez'],

	    				        beforeShowDay: CadastroCalendario.highlightDays,
	    				        
	    						onSelect: CadastroCalendario.popupEdicaoCadastroFeriado
	    						
	    						
	    					});
	    					
	    				
	    				});
	    	 
	     }
		
}, BaseController);
