
var tipoFormaCobranca = null;
var idPolitica = null;

var parametroCobrancaController = $.extend(true,
	{
		
		idsFornecedoresDisponiveis : [],
	
		init : function() {
			parametroCobrancaController.formatarCampos();
	    	parametroCobrancaController.criarParametrosGrid();
	    	parametroCobrancaController.criarCotasCentralizadasGrid();
	    	$(".numCota", parametroCobrancaController.workspace).numeric();
		},
		
	   
		formatarCampos : function() {
			$("#valorMinimo", this.workspace).priceFormat({
				centsSeparator: ',',
			    thousandsSeparator: '.',
			    centsLimit:2	
			});
			$("#diasDoMes", this.workspace).numeric();
			$("#diasDoMes1", this.workspace).numeric();
			$("#diasDoMes2", this.workspace).numeric();
			$("#taxaMulta", this.workspace).numeric();
			$("#valorMulta", this.workspace).numeric();
			$("#taxaJuros", this.workspace).numeric();
			parametroCobrancaController.carregarFormasEmissao(null,"");
		},
		
	
		criarParametrosGrid : function() {
			$(".parametrosGrid", this.workspace).flexigrid({
				preProcess: parametroCobrancaController.getDataFromResult,
				dataType : 'json',
				onSuccess: function() { bloquearItensEdicao(parametroCobrancaController.workspace);},
				colModel :[ {
					display : 'Forma Pagto',
					name : 'forma',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Banco',
					name : 'banco',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Vlr. M&iacute;n. Emiss&atilde;o R$',
					name : 'valorMinimoEmissao',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Acumula Divida',
					name : 'acumulaDivida',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Cobran&ccedil;a Unif.',
					name : 'cobrancaUnificada',
					width : 80,
					sortable : true,
					align : 'center',
				}, {
					display : 'Forma Emiss&atilde;o',
					name : 'formaEmissao',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Envia E-Mail',
					name : 'envioEmail',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Fornecedores',
					name : 'fornecedores',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Concentra&ccedil;&atilde;o Pgtos',
					name : 'concentracaoPagamentos',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Principal',
					name : 'principal',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'A&ccedil;&atilde;o',
					name : 'acao',
					width : 45,
					sortable : true,
					align : 'center'
				}],
				sortname : "forma",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : '255'
			});
		},
		
		criarCotasCentralizadasGrid : function() {
			$(".cotasCentralizadas", this.workspace).flexigrid({
				preProcess: parametroCobrancaController.getDataCotasCentralizadas,
				dataType : 'json',
				colModel :[ {
					display : 'Cota Centralizadora',
					name : 'cota',
					width : 105,
					sortable : false,
					align : 'center'
				}, {
					display : 'Cotas Centralizadas',
					name : 'cotas',
					width : 200,
					sortable : false,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 50,
					sortable : false,
					align : 'center'
				}],
				width : 420,
				height : 255
			});
		},
		
	    mostrarGridConsulta : function() {
	    	
			/*PASSAGEM DE PARAMETROS*/
			$(".parametrosGrid", this.workspace).flexOptions({
				/*METODO QUE RECEBERA OS PARAMETROS*/
				url: contextPath+'/distribuidor/parametroCobranca/consultaParametrosCobranca',
				params: [
				         {name:'idBanco', value:$("#filtroBanco", this.worspace).val()},
				         {name:'tipoCobranca', value:$("#filtroTipoCobranca", this.worspace).val()}
				        ] ,
			    newp: 1
			    
			});
			
			/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
			$(".parametrosGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
		},	
	    
	    
		getDataFromResult : function(resultado) {
			
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens) {
				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids", this.workspace).hide();
				return resultado;
			}	
			
			$.each(resultado.rows, function(index, row) {
				
				var linkEditar = '<a isEdicao="true" href="javascript:;" id="bt_alterar" onclick="parametroCobrancaController.popup_alterar(' + row.cell.idPolitica + ');" style="cursor:pointer; margin-right:10px;">' +
						     	  	'<img title="Aprovar" src="'+contextPath+'/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a isEdicao="true" href="javascript:;" id="bt_excluir" onclick="parametroCobrancaController.popup_excluir(' + row.cell.idPolitica + ');" style="cursor:pointer">' +
								   	 '<img title="Rejeitar" src="'+contextPath+'/images/ico_excluir.gif" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkEditar + linkExcluir;
				
				
				row.cell.valorMinimoEmissao = $.formatNumber(row.cell.valorMinimoEmissao, {format:"#,##0.00", locale:"br"});
				
				
				if(row.cell.principal){
					row.cell.principal = '<img src="'+contextPath+'/images/ico_check.gif" border="0px"/>';
				}else{
					row.cell.principal = '';
				}
				
				
			});
				
			$(".grids", this.workspace).show();
			
			return resultado;
		},
		
		
		//VERIFICA A EXISTENCIA DE UMA POLITICA DE COBRANCA PRINCIPAL
		existPrincipal : function(){
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/existPrincipal",
					   null,
					   function(mensagens) {
			        	   if (mensagens){
							   var tipoMensagem = mensagens.tipoMensagem;
							   var listaMensagens = mensagens.listaMensagens;
							   if (tipoMensagem && listaMensagens) {
							       exibirMensagem(tipoMensagem, listaMensagens);
						       }
							   return false;
			        	   }
			        	   else{
			        		   return true;
			        	   }
			        	   
			           },
			           null,
					   true,
					   "idModal");
		},
	
		
		popup : function() {
			
			parametroCobrancaController.preparaCadastroParametro();
			
			parametroCobrancaController.botaoUnificaPorCotas('N');
			
			$("#unificadaCota", parametroCobrancaController.workspace).val("N");
			
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/resetCotaUnificacoes", null, null, null, true);
	
			$( "#dialog-novo", this.workspace).dialog({
				resizable: false,
				height:530,
				width:890,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   parametroCobrancaController.postarParametro();
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this, this.workspace).dialog( "close" );
					           }
				           }
		        ],
				beforeClose: function() {
					clearMessageDialogTimeout("idModal");
					/*
					if (!existPrincipal()){
						return false;
					}
					*/
					
			    },
				form: $("#dialog-novo", this.workspace).parents("form")
			});
		},
		
		
		popup_alterar : function(idPolitica) {
			
			parametroCobrancaController.obterParametro(idPolitica);
			
			$( "#dialog-novo", this.workspace).dialog({
				resizable: false,
				height:580,
				width:890,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   parametroCobrancaController.postarParametro();
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this, this.workspace).dialog( "close" );
					           }
				           }
		        ],
				beforeClose: function() {
					clearMessageDialogTimeout("idModal");
	                /*
					if (!existPrincipal()){
						return false;
					}
	                */
					
			    },
				form: $("#dialog-novo", this.workspace).parents("form")
			});	
			      
		},
		
		
		popup_excluir : function(idPolitica) {
			$( "#dialog-excluir", this.workspace).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   parametroCobrancaController.desativarParametro(idPolitica);
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this, this.workspace).dialog( "close" );
					           }
				           }
		        ],
				beforeClose: function() {
					clearMessageDialogTimeout("idModal");
			    },
				form: $("#dialog-excluir", this.workspace).parents("form")
			});
		},
		
	
		//MODOS DE EXIBIÇÃO
		opcaoPagto : function(op){
			if ((op=='BOLETO')||(op=='DEPOSITO')||(op=='TRANSFERENCIA_BANCARIA')){
				$('.tdComboBanco', this.workspace).show();
				$('.tdValorMinimo', this.workspace).show();			
				$('.tdMulta', this.workspace).show();			
				$('.tdJuros', this.workspace).show();
				
		    }
			
			else if ((op=='CHEQUE')||(op=='DINHEIRO')){
				$('.tdComboBanco', this.workspace).hide();			
				$('.tdValorMinimo', this.workspace).show();
				$('.tdMulta', this.workspace).show();			
				$('.tdJuros', this.workspace).show();
	
				$("#dBanco", this.workspace).val("");
		    }
			
			else if (op=='BOLETO_EM_BRANCO'){
				$('.tdComboBanco', this.workspace).show();			
				$('.tdValorMinimo', this.workspace).hide();			
				$('.tdMulta', this.workspace).hide();			
				$('.tdJuros', this.workspace).hide();
				$('.formPgto', this.workspace).show();
			}    
			
			else if ((op=='OUTROS')){
				$('.tdComboBanco', this.workspace).hide();			
				$('.tdValorMinimo', this.workspace).hide();			
				$('.tdMulta', this.workspace).show();			
				$('.tdJuros', this.workspace).show();
				
				$("#dBanco", this.workspace).val("");
		    }
			
			if((op=='BOLETO') || (op=='BOLETO_EM_BRANCO')){
				$('.formPgto', this.workspace).show();
			}else{
				$('.formPgto', this.workspace).hide();
			}
			
			parametroCobrancaController.obterDadosBancarios($("#dBanco", this.workspace).val());
		},
		
		
		mudaConcentracaoPagamento : function(op){
			tipoFormaCobranca = (op.valueOf().toUpperCase());
			$( ".mensal", this.workspace).hide();
			$( ".semanal", this.workspace).hide();
			$( ".quinzenal", this.workspace).hide();
			$( "." + op, this.workspace).show();
			
		},
		
		
		opcaoTipoFormaCobranca : function(op){
			op = op.toLowerCase();		
			
			$("#radio_"+op, this.workspace).attr("checked", true);
			//document.parametro_form[op].checked = true;
			parametroCobrancaController.mudaConcentracaoPagamento(op);
		},
		
		
		fecharDialogs : function() {
			$( "#dialog-novo", this.workspace).dialog( "close" );
		    $( "#dialog-excluir", this.workspace).dialog( "close" );
		},
		
		
		//OBTEM UM PARÂMETRO PARA ALTERAÇÃO
		obterParametro : function(idPolitica){
			var data = [{name: 'idPolitica', value: idPolitica}];
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/obterParametroCobranca",
					   data,
					   parametroCobrancaController.sucessCallbackObterParametro, 
					   null,
					   true);
		},

		sucessCallbackObterParametro : function(resultado) {
			
			var formaEmissao = resultado.formaEmissao;
			
			idPolitica  = resultado.idPolitica;
			
			$("#dTipoCobranca", this.workspace).val(resultado.tipoCobranca);
			
			$("#formaCobranca", this.workspace).val(resultado.formaCobranca);
			$("#dBanco", this.workspace).val(resultado.idBanco);
			
			$("#valorMinimo", this.workspace).val(resultado.valorMinimo);
			$("#taxaMulta", this.workspace).val(resultado.taxaMulta);
			$("#valorMulta", this.workspace).val(resultado.valorMulta);
			$("#taxaJuros", this.workspace).val(resultado.taxaJuros);
			
			$("#instrucoes", this.workspace).val(resultado.instrucoes);
	
			$("#acumulaDivida", this.workspace).val(resultado.acumulaDivida?'S':'N');
			$("#vencimentoDiaUtil", this.workspace).val(resultado.vencimentoDiaUtil?'S':'N');
			$("#unificada", this.workspace).val(resultado.unificada?'S':'N');
			$("#unificadaCota", this.workspace).val(resultado.unificadaPorCota?'S':'N');
			parametroCobrancaController.botaoUnificaPorCotas(
					resultado.unificadaPorCota?'S':'N',
					resultado.tipoCobranca);
			
			$("#envioEmail", this.workspace).val(resultado.envioEmail?'S':'N');
			
			if(resultado.tipoFormaCobranca == 'MENSAL'){
				if(resultado.diasDoMes[0]){
					$('#diaDoMes', this.workspace).val(resultado.diasDoMes[0]);
				}else{
					$('#diaDoMes', this.workspace).val("");
				}
			}else if(resultado.tipoFormaCobranca == 'QUINZENAL'){			
				if(resultado.diasDoMes[0]){
					$('#diaDoMes1', this.workspace).val(resultado.diasDoMes[0]);
				}else{
					$('#diaDoMes1', this.workspace).val("");
				}
				if(resultado.diasDoMes[1]){
					$('#diaDoMes2', this.workspace).val(resultado.diasDoMes[1]);
				}else{
					$('#diaDoMes2', this.workspace).val("");
				}
			}		
			$("#principal", this.workspace).attr('checked',resultado.principal);		
			$("#dPS", this.workspace).attr('checked',resultado.segunda);
			$("#dPT", this.workspace).attr('checked',resultado.terca);
			$("#dPQ", this.workspace).attr('checked',resultado.quarta);
			$("#dPQu", this.workspace).attr('checked',resultado.quinta);
			$("#dPSex", this.workspace).attr('checked',resultado.sexta);
			$("#dPSab", this.workspace).attr('checked',resultado.sabado);
			$("#dPDom", this.workspace).attr('checked',resultado.domingo);
	
	
			parametroCobrancaController.opcaoPagto(resultado.tipoCobranca);
			parametroCobrancaController.opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
			parametroCobrancaController.tratarFornecedoresCobrancaUnificada();
			
			$("input[name='checkGroupFornecedores']", this.workspace).each(function(i) {			
				$(this, this.workspace).attr('checked',false);
			});	
			
			parametroCobrancaController.idsFornecedoresDisponiveis = resultado.fornecedoresId;
			
			$.each(resultado.fornecedoresId, function(index, value) { 
				 $("#ParamCob-fornecedor_" + value, this.workspace).attr('checked', true);
			});
			
			$.each($("#comboFatorVencimento option", this.workspace), function(index, value) { 
				 if(this.value == resultado.fatorVencimento) {
					this.selected = true;
				 }
			});

			parametroCobrancaController.tratarSelecaoFornecedorPadrao();
			
			$("input[name='radioFormaCobrancaBoleto']", this.workspace).each(function(i) {			
				if($(this, this.workspace).val() == resultado.formaCobrancaBoleto){
					$(this, this.workspace).attr('checked', true);
				}
			});
			
			parametroCobrancaController.carregarFormasEmissao(resultado.tipoCobranca,formaEmissao);
			
			$("#comboFornecedorPadrao", parametroCobrancaController.workspace).val(resultado.idFornecedorPadrao);
			
			$("#valorMinimo", this.workspace).priceFormat({
				centsSeparator: ',',
			    thousandsSeparator: '.',
			    centsLimit:2	
			});
			
			$("#valorMulta", this.workspace).priceFormat({
				centsSeparator: ',',
			    thousandsSeparator: '.',
			    centsLimit:2	
			});
			
			$('#taxaMulta', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#taxaJuros', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
		},	
		
		
		//INCLUSÃO DE NOVO PARAMETRO
		postarParametro : function(novo) {
			if(novo){
				idPolitica = null;
			}
			var parametroCobranca = {
					idPolitica: idPolitica,
					tipoFormaCobranca: tipoFormaCobranca,
					tipoCobranca : $("#dTipoCobranca", this.worspace).val(),
					formaEmissao : $("#formaEmissao", this.worspace).val(),
					idBanco : $("#dBanco", this.workspace).val(),
					idFornecedorPadrao : $("#comboFornecedorPadrao option:selected", this.workspace).val(),
					fatorVencimento : $("#comboFatorVencimento option:selected", this.workspace).val(),
					valorMinimo : floatValue($("#valorMinimo", this.workspace).val()),
					taxaMulta : floatValue($("#taxaMulta", this.worspace).val()),
					valorMulta : floatValue($("#valorMulta", this.worspace).val()),
					taxaJuros : floatValue($("#taxaJuros", this.worspace).val()),
					instrucoes : $("#instrucoes", this.worspace).val(),
					acumulaDivida : $("#acumulaDivida", this.workspace).val() == 'S',
					vencimentoDiaUtil : $("#vencimentoDiaUtil", this.workspace).val() == 'S',
					unificada : $("#unificada", this.workspace).val() == 'S',
					unificadaPorCota : $("#unificadaCota", this.workspace).val() == 'S',
					envioEmail : $("#envioEmail", this.workspace).val() == 'S',
					principal : $("#principal", this.workspace).attr('checked')  == 'checked',
					segunda : $("#dPS", this.workspace).attr('checked')  == 'checked',		
					terca : $("#dPT", this.workspace).attr('checked')  == 'checked',
					quarta : $("#dPQ", this.workspace).attr('checked')  == 'checked',		
					quinta : $("#dPQu", this.workspace).attr('checked')  == 'checked',	
					sexta : $("#dPSex", this.workspace).attr('checked')  == 'checked',		
					sabado : $("#dPSab", this.workspace).attr('checked')  == 'checked',		
					domingo : $("#dPDom", this.workspace).attr('checked')  == 'checked'
			};
			
			$("input[name='radioFormaCobrancaBoleto']:checked", this.workspace).each(function(i) {			
				parametroCobranca.formaCobrancaBoleto =  $(this, this.workspace).val();
			});	
			
			var postObject = serializeObjectToPost("parametros", parametroCobranca);
			
			postObject = serializeArrayToPost("parametros.diasDoMes",[0] ,postObject);
			
			if(tipoFormaCobranca == 'MENSAL'){
				postObject = serializeArrayToPost("parametros.diasDoMes",[$('#diaDoMes', this.workspace).val()] ,postObject);
			}else if(tipoFormaCobranca == 'QUINZENAL'){
				postObject = serializeArrayToPost("parametros.diasDoMes",[$('#diaDoMes1', this.workspace).val(),$('#diaDoMes2', this.workspace).val()] ,postObject);
			}
			
			var listaIdsFornecedores = new Array();
			
			listaIdsFornecedores.push(0);
			
			$("input[name='checkGroupFornecedores']:checked", this.workspace).each(function(i) {			
				listaIdsFornecedores.push($(this, this.workspace).val());
			});	
			
			postObject = serializeArrayToPost("parametros.fornecedoresId",listaIdsFornecedores ,postObject);
			
			var telaMensagem = "";
			
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/postarParametroCobranca",postObject,
					   function(mensagens) {
				           parametroCobrancaController.fecharDialogs();
			        	   telaMensagem=null;
			        	   if (mensagens){
							   var tipoMensagem = mensagens.tipoMensagem;
							   var listaMensagens = mensagens.listaMensagens;
							   if (tipoMensagem && listaMensagens) {
							       exibirMensagem(tipoMensagem, listaMensagens);
						       }
			        	   }
			        	   parametroCobrancaController.mostrarGridConsulta();
			            },
			            null,
			   			true,
			   			telaMensagem==null?"":"idModal");
			
		},
	
		
		//EXCLUI (DESATIVA) UM PARÂMETRO
		desativarParametro : function(idPolitica) {
	    	var data = [{name: 'idPolitica', value: idPolitica}];
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/desativaParametroCobranca",
					   data,
					   function(result) {
				           parametroCobrancaController.fecharDialogs();
						   var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   parametroCobrancaController.mostrarGridConsulta();
			           },
					   function(result) {
			        	   parametroCobrancaController.fecharDialogs();
						   var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   parametroCobrancaController.mostrarGridConsulta();
			           },
			           true,
			           ""
				       );
		},
		
		
		//LIMPA TODOS OS DADOS PARA INCLUSÃO DE NOVO REGISTRO
		preparaCadastroParametro : function() {
			
	    	$("input[name='checkGroupFornecedores']:checked", this.workspace).each(function(i) {
				$(this, this.workspace).attr('checked',false);
			});
			
			$( ".semanal,.mensal,.quinzenal", this.workspace).hide();
			
			idPolitica  = null;
			
			$("#dTipoCobranca", this.workspace).val('');
			
			$("#formaCobranca", this.workspace).val('');
			$("#dBanco", this.workspace).val('');
			
			$("#valorMinimo", this.workspace).val('');
			$("#taxaMulta", this.workspace).val('');
			$("#valorMulta", this.workspace).val('');
			$("#taxaJuros", this.workspace).val('');
			
			$("#instrucoes", this.workspace).val('');
	
			$("#acumulaDivida", this.workspace).val('S');
			$("#vencimentoDiaUtil", this.workspace).val('S');
			$("#unificada", this.workspace).val('S');
			$("#unificadaCota", this.workspace).val('N');
			$("#envioEmail", this.workspace).val('S');		
			$('#diaDoMes', this.workspace).val("");
			$('#diaDoMes1', this.workspace).val("");	
			$('#diaDoMes2', this.workspace).val("");			
			
			$("#principal", this.workspace).attr('checked',false);		
			$("#PS", this.workspace).attr('checked',false);
			$("#PT", this.workspace).attr('checked',false);
			$("#PQ", this.workspace).attr('checked',false);
			$("#PQu", this.workspace).attr('checked',false);
			$("#PSex", this.workspace).attr('checked',false);
			$("#PSab", this.workspace).attr('checked',false);
			$("#PDom", this.workspace).attr('checked',false);
	
			$("#comboFatorVencimento option:first", this.workspace).attr("selected", "selected");
			
			$("#comboFornecedorPadrao option:first", this.workspace).attr("selected", "selected");
			$.each($("#comboFornecedorPadrao option", this.workspace), function(index, value) { 
				$("#ParamCob-fornecedor_"+ value.value, this.workspace).attr('disabled', false);				
			});
			
			$.each($('.semanal :input[type="checkbox"]', this.workspace), function(index, value) {
				$(value).attr('checked', false);
			});
			
			parametroCobrancaController.carregarFormasEmissao(null,"");
			$("input[name='checkGroupFornecedores']", this.workspace).each(function(i) {			
				$(this, this.workspace).attr('checked',false);
			});	
			$('[name=concentracaoPagamento]', this.workspace).attr('checked',false);
			
			parametroCobrancaController.tratarSelecaoFornecedorPadrao();	
		},	
		
		
		montarComboBox : function(result,name,onChange,selected) {
	
			var options = "";
			
			options += "<select name='"+name+"' id='"+name+"' style='width:220px;' onchange='"+onChange+"'>";
			options += "<option value=''>Selecione</option>";
			$.each(result, function(index, row) {
				if (selected == row.key.$){
				    options += "<option selected='true' value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
				}
				else{
					options += "<option value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
				}
			});
			options += "</select>";
			
			return options;
		},
		
		
		carregarFormasEmissao : function(tipoCobranca,selected){
			var data = [{name: 'tipoCobranca', value: tipoCobranca}];
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/obterFormasEmissao",
					   data,
					   function(resultado){
				           parametroCobrancaController.criaComponenteFormasEmissao(resultado,selected);
			           }
					   ,
					   null,
					   true);
		},
		
		
		criaComponenteFormasEmissao : function(result,selected) {
		    var comboFormasEmissao =  parametroCobrancaController.montarComboBox(result,"formaEmissao","",selected);
			$("#formasEmissao", this.workspace).html(comboFormasEmissao);
		},
		
		tratarFornecedoresCobrancaUnificada: function() {
		
			var isUnificada = $("#unificada").val();
			
			if (isUnificada == 'N') {
			
				$("input[name='checkGroupFornecedores']").prop("checked", false);
				
				parametroCobrancaController.tratarSelecaoFornecedorPadrao();
				
				$("input[name='checkGroupFornecedores']").prop("disabled", true);
				
			} else {
				
				parametroCobrancaController.habilitarTodosFornecedores();
				
				parametroCobrancaController.tratarSelecaoFornecedorPadrao();
			}
		},
		
		tratarSelecaoFornecedorPadrao: function() {
		
			parametroCobrancaController.desabilitarFornecedorPadrao(true);
		},

		desabilitarFornecedorPadrao: function(check) {
		
			$("#comboFornecedorPadrao", this.workspace).focus(function () {
				previous = this.value;
			}).on('change', function() {
				var isUnificada = $("#unificada").val();
				if (isUnificada == 'S') 
					parametroCobrancaController.habilitarTodosFornecedores();
				else
					$("input[name='checkGroupFornecedores']").prop("checked", false);
				$("#ParamCob-fornecedor_" + this.value, this.workspace).attr('disabled', true);
				$("#ParamCob-fornecedor_" + this.value, this.workspace).attr('checked', check);
			});
			
			$("#comboFornecedorPadrao", this.workspace).change();
		},
		
		habilitarTodosFornecedores: function() {
		
			$("input[name='checkGroupFornecedores']", this.workspace).attr('disabled', false);
		},
		
		obterDadosBancarios : function(idBanco){
			
			if (idBanco == ""){
				
				parametroCobrancaController.bloquearCampos(false);
				
				return;
				
		    } else {
		    	
		    	parametroCobrancaController.bloquearCampos(true);
		    }
			
			var data = [{name: 'idBanco', value: idBanco}];
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/obterDadosBancarios",
					   data,
					   parametroCobrancaController.sucessCallbackCarregarDadosBancarios,
					   null,
					   true);
		},
		
		bloquearCampos : function(bloquear) {
			
			$("#taxaMulta", this.workspace).attr('readonly', bloquear);
			$("#valorMulta", this.workspace).attr('readonly', bloquear);
			$("#taxaJuros", this.workspace).attr('readonly', bloquear);
		},
		
		sucessCallbackCarregarDadosBancarios : function(result) {
			$("#taxaMulta", this.workspace).val(result.multa);
			$("#valorMulta", this.workspace).val(result.vrMulta);
			$("#taxaJuros", this.workspace).val(result.juros);
			$("#instrucoes", this.workspace).val(result.instrucoes);
			
			$('#taxaMulta', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#taxaJuros', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
		},
		
		botaoUnificaPorCotas : function(value, tipoPagamento){
			
			if (tipoPagamento && tipoPagamento == 'BOLETO_EM_BRANCO'){
				
				$("#unificadaCota", parametroCobrancaController.workspace).val("N");
				$("#unificadaCota", parametroCobrancaController.workspace).attr("disabled", "disabled");
				$("#botaoTelaUnificacao", parametroCobrancaController.workspace).hide();
				return;
			} else {
				
				$("#unificadaCota", parametroCobrancaController.workspace).removeAttr("disabled");
			}
			
			if (value == 'S'){
				
				$("#botaoTelaUnificacao", parametroCobrancaController.workspace).show();
			} else {
				
				$("#botaoTelaUnificacao", parametroCobrancaController.workspace).hide();
			}
		},
		
		mostrarUnificacaoCotas : function(){
			
			$(".cotasCentralizadas", parametroCobrancaController.workspace).flexOptions({
				url: contextPath+'/cadastro/cotaUnificacao/consultarCotasUnificadas',
				newp : 1
			});
			
			$(".cotasCentralizadas", parametroCobrancaController.workspace).flexReload();
		},
		
		getDataCotasCentralizadas : function(data){
			
			$.each(data.rows, function(index, row){
				
				row.cell.cota = row.cell.numeroCota;
				
				var centralizadas = '';
				$.each(row.cell.cotas, function(idx, r){
					
					if (idx > 0){
						if (idx % 10 == 0){
							
							centralizadas += "</br>";
						} else {
							
							centralizadas += " / ";
						}
					}
					
					centralizadas += r.numero;
				});
				
				row.cell.cotas = centralizadas;
				
				var linkAEditar = '<a href="javascript:;" onclick="parametroCobrancaController.editarUnificacao(' + 
				row.cell.cota + ');"' +
				'style="cursor:pointer; margin-right:10px;">' +
				'<img title="Editar" src="' + contextPath + '/images/ico_editar.gif" border="0px" />' +
				'</a>';

				var linkExcluir = '<a href="javascript:;" onclick="parametroCobrancaController.excluirUnificacao(' + 
				row.cell.cota + ');" style="cursor:pointer">' +
				'<img title="Excluir" src="' + contextPath + '/images/ico_excluir.gif" border="0px" />' +
				'</a>';
				
				row.cell.acao = linkAEditar + linkExcluir;
			});
			
			$("#dialog_cota_unificacao", parametroCobrancaController.workspace).dialog({
				resizable: false,
				height:390,
				width:450,
				modal: true,
				buttons:[{
					id:"bt_inc_nova",
			        text:"Incluir Nova", 
			        click: function() {
			        	$("#numeroCota_", parametroCobrancaController.workspace).val("");
			        	$("#nomeCota_", parametroCobrancaController.workspace).val("");
			        	parametroCobrancaController.exibirModalNovaUnificacao();
			        	parametroCobrancaController.adicionarLinhaCota();
			        }
				}],
				form: $("#dialog_cota_unificacao", this.workspace).parent("form")
			});
			
			return data;
		},
		
		exibirModalNovaUnificacao : function(){
			
			$("#numeroCota_", parametroCobrancaController.workspace).removeAttr("readonly");
			$("#nomeCota_", parametroCobrancaController.workspace).removeAttr("readonly");
			parametroCobrancaController.limparCamposCentralizacaoCotas();
			
			$("#dialog_nova_cota_unificacao", parametroCobrancaController.workspace).dialog({
				resizable: false,
				height:390,
				width:600,
				modal: true,
				buttons:[
				{
					id:"bt_confirmar",
					text:"Confirmar",
					click: function(){
						
						var data = [
						            {name:'numeroCotaCentralizadora', value:$("#numeroCota_", parametroCobrancaController.workspace).val()}
						            ];
						
						$.each($("#cotasCentralizadas [class=numCota]", parametroCobrancaController.workspace),
							function(index, item){
								
								if (item.value && item.value != ""){
									
									data.push({name:'numeroCotasCentralizadas['+index+']', value:item.value});
								}
							}
						);
						
						$.postJSON(
							contextPath+"/cadastro/cotaUnificacao/cadastrarCotaUnificacao",
							data,
							function(result) {
								if (result){
									
									if (result.tipoMensagem){
										
										exibirMensagem(result.tipoMensagem, result.listaMensagens);
									}
									
									parametroCobrancaController.limparCamposCentralizacaoCotas();
						        	$("#dialog_nova_cota_unificacao", parametroCobrancaController.workspace).dialog("close");
						        	
						        	parametroCobrancaController.mostrarUnificacaoCotas();
								}
							},
							null,
							true
						);
					}
				}, {
					id:"bt_cancelar",
			        text:"Cancelar", 
			        click: function() {
			        	
			        	parametroCobrancaController.limparCamposCentralizacaoCotas();
			        	$("#dialog_nova_cota_unificacao", parametroCobrancaController.workspace).dialog("close");
			        }
				}],
				form: $("#dialog_nova_cota_unificacao", this.workspace).parent("form")
			});
		},
		
		buscarCotaPorNumero : function(index){
			
			var numeroCota = $("#numeroCota_" + index, parametroCobrancaController.workspace).val();
			
			if (!numeroCota || numeroCota == ""){
				
				$("#numeroCota_" + index, parametroCobrancaController.workspace).val("");
     		   	$("#nomeCota_" + index, parametroCobrancaController.workspace).val("");
				return;
			}
			
			var cotaValida = true;
			$.each($(".numCota", parametroCobrancaController.workspace), function(i, campo){
				
				if (campo.value == numeroCota && campo.id != "numeroCota_" + index){
					
					exibirMensagem('WARNING', ['Cota '+ campo.value +' já escolhida.']);
					cotaValida = false;
					$("#numeroCota_" + index, parametroCobrancaController.workspace).val("");
					return false;
				}
			});
			
			if (!cotaValida){
				return;
			}
			
			var data = [
	            {name:'numeroCota', value: numeroCota},
	            {name:'edicao', value: $("#numeroCota_", parametroCobrancaController.workspace).attr("readonly") != undefined}
			];
			
			$.postJSON(contextPath+"/cadastro/cotaUnificacao/buscarCota",
			   data,
			   function(result) {
	        	   if (result.tipoMensagem){
					   
	        		   exibirMensagem(result.tipoMensagem, result.listaMensagens);
	        		   $("#numeroCota_" + index, parametroCobrancaController.workspace).val("");
	        		   $("#nomeCota_" + index, parametroCobrancaController.workspace).val("");
	        		   
				       return;
	        	   } else {
	        		   
	        		   $("#numeroCota_" + index, parametroCobrancaController.workspace).val(result.numero);
	        		   $("#nomeCota_" + index, parametroCobrancaController.workspace).val(result.nome);
	        	   }
	           },
	           function(){
	        	   
	        	   $("#numeroCota_" + index, parametroCobrancaController.workspace).val("");
	        	   $("#nomeCota_" + index, parametroCobrancaController.workspace).val("");
	           },
			   true
			);
		},
		
		adicionarLinhaCota : function(indexAnterior){
			
			if ($("#numeroCota_"+indexAnterior, parametroCobrancaController.workspace).val() == "" ||
					$("#nomeCota_"+indexAnterior, parametroCobrancaController.workspace).val() == ""){
				
				return;
			}
			
			if (!indexAnterior && indexAnterior != 0){
				indexAnterior = 0;
			} else {
				indexAnterior += 1;
			}
			
			if ($("#numeroCota_"+indexAnterior, parametroCobrancaController.workspace)[0]){
				return;
			}
			
			var template = '<tr class="addCota"><td style="width: 10%;">'+
				'<input type="text" class="numCota" id="numeroCota_'+ indexAnterior +'" style="width: 40px;"'+
				'onchange="parametroCobrancaController.buscarCotaPorNumero('+ indexAnterior +')"/>'+
				'</td><td>'+
				'<input type="text" id="nomeCota_'+ indexAnterior +'" style="width: 475px;"'+
				'onblur="parametroCobrancaController.adicionarLinhaCota('+ indexAnterior +')"/></td></tr>';
			
			$("#cotasCentralizadas", parametroCobrancaController.workspace).append(template);
			
			$(".numCota", parametroCobrancaController.workspace).numeric();
			
			$("#numeroCota_"+ indexAnterior, parametroCobrancaController.workspace).focus();
		},
		
		limparCamposCentralizacaoCotas : function(){
			
			$("#numeroCota_", parametroCobrancaController.workspace).val("");
        	$("#nomeCota_", parametroCobrancaController.workspace).val("");
        	
        	$("#cotasCentralizadas tr:[class=addCota]", parametroCobrancaController.workspace).remove();
		},
		
		excluirUnificacao : function(cota){
			
			$.postJSON(
				contextPath+"/cadastro/cotaUnificacao/excluirCotaUnificacao",
				[{name:'cotaUnificadora', value:cota}],
				function(result){
					parametroCobrancaController.mostrarUnificacaoCotas();
				},
				null,
				true
			);
		},
		
		editarUnificacao : function(cota){
			
			$.postJSON(
				contextPath+"/cadastro/cotaUnificacao/editarCotaUnificacao",
				[{name:'cotaUnificadora', value:cota}],
				function(result){
					
					parametroCobrancaController.exibirModalNovaUnificacao();
					
					$.each(result, function(index, row){
						
						if (!index){
							
							$("#numeroCota_", parametroCobrancaController.workspace).val(row.numero);
							$("#nomeCota_", parametroCobrancaController.workspace).val(row.nome);
							
							$("#numeroCota_", parametroCobrancaController.workspace).attr("readonly", "readonly");
							$("#nomeCota_", parametroCobrancaController.workspace).attr("readonly", "readonly");
						} else {
							
							parametroCobrancaController.adicionarLinhaCota(index-1);
							$("#numeroCota_" + index, parametroCobrancaController.workspace).val(row.numero);
							$("#nomeCota_" + index, parametroCobrancaController.workspace).val(row.nome);
						}
					});
					
					
				},
				null,
				true
			);
		}
	}
	, 
	BaseController
);

//@ sourceURL=parametroCobranca.js
