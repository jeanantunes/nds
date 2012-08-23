
var tipoFormaCobranca = null;
var idPolitica = null;

var parametroCobrancaController = $.extend(true,
	{
		

		init : function() {
			parametroCobrancaController.formatarCampos();
	    	parametroCobrancaController.criarParametrosGrid();
		},
		
	   
		formatarCampos : function() {
			$("#valorMinimo", this.workspace).numeric();
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
				colModel :[ {
					display : 'Forma Pagto',
					name : 'forma',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Banco',
					name : 'banco',
					width : 50,
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
					width : 60,
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
				
				var linkEditar = '<a href="javascript:;" id="bt_alterar" onclick="parametroCobrancaController.popup_alterar(' + row.cell.idPolitica + ');" style="cursor:pointer">' +
						     	  	'<img title="Aprovar" src="'+contextPath+'/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" id="bt_excluir" onclick="parametroCobrancaController.popup_excluir(' + row.cell.idPolitica + ');" style="cursor:pointer">' +
								   	 '<img title="Rejeitar" src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkEditar + linkExcluir;
				
				
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
	
			$( "#dialog-novo", this.workspace).dialog({
				resizable: false,
				height:560,
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
		    }
			
			if((op=='BOLETO') || (op=='BOLETO_EM_BRANCO')){
				$('.formPgto', this.workspace).show();
			}else{
				$('.formPgto', this.workspace).hide();
			}
			
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
			
			$("#tipoCobranca", this.workspace).val(resultado.tipoCobranca);
			
			$("#formaCobranca", this.workspace).val(resultado.formaCobranca);
			$("#banco", this.workspace).val(resultado.idBanco);
			
			$("#valorMinimo", this.workspace).val(resultado.valorMinimo);
			$("#taxaMulta", this.workspace).val(resultado.taxaMulta);
			$("#valorMulta", this.workspace).val(resultado.valorMulta);
			$("#taxaJuros", this.workspace).val(resultado.taxaJuros);
			
			$("#instrucoes", this.workspace).val(resultado.instrucoes);
	
			$("#acumulaDivida", this.workspace).val(resultado.envioEmail?'S':'N');
			$("#vencimentoDiaUtil", this.workspace).val(resultado.envioEmail?'S':'N');
			$("#unificada", this.workspace).val(resultado.envioEmail?'S':'N');
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
			$("#PS", this.workspace).attr('checked',resultado.segunda);
			$("#PT", this.workspace).attr('checked',resultado.terca);
			$("#PQ", this.workspace).attr('checked',resultado.quarta);
			$("#PQu", this.workspace).attr('checked',resultado.quinta);
			$("#PSex", this.workspace).attr('checked',resultado.sexta);
			$("#PSab", this.workspace).attr('checked',resultado.sabado);
			$("#PDom", this.workspace).attr('checked',resultado.domingo);
	
	
			parametroCobrancaController.opcaoPagto(resultado.tipoCobranca);
			parametroCobrancaController.opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
			
			$("input[name='checkGroupFornecedores']", this.workspace).each(function(i) {			
				$(this, this.workspace).attr('checked',false);
			});	
			
			$.each(resultado.fornecedoresId, function(index, value) { 
				 $("#fornecedor_" + value, this.workspace).attr('checked', true);
			});
			
			
			$("input[name='radioFormaCobrancaBoleto']", this.workspace).each(function(i) {			
				if($(this, this.workspace).val() == resultado.formaCobrancaBoleto){
					$(this, this.workspace).attr('checked', true);
				}
			});
			
			parametroCobrancaController.carregarFormasEmissao(resultado.tipoCobranca,formaEmissao);
		},	
		
		
		//INCLUSÃO DE NOVO PARAMETRO
		postarParametro : function(novo) {
			if(novo){
				idPolitica = null;
			}
			var parametroCobranca = {
					idPolitica: idPolitica,
					tipoFormaCobranca: tipoFormaCobranca,
					tipoCobranca : $("#tipoCobranca", this.worspace).val(),
					formaEmissao : $("#formaEmissao", this.worspace).val(),
					idBanco : $("#banco", this.worspace).val(),
					valorMinimo : $("#valorMinimo", this.worspace).val(),
					taxaMulta : $("#taxaMulta", this.worspace).val(),
					valorMulta : $("#valorMulta", this.worspace).val(),
					taxaJuros : $("#taxaJuros", this.worspace).val(),
					instrucoes : $("#instrucoes", this.worspace).val(),
					acumulaDivida : $("#acumulaDivida", this.workspace).val() == 'S',
					vencimentoDiaUtil : $("#vencimentoDiaUtil", this.workspace).val() == 'S',
					unificada : $("#unificada", this.workspace).val() == 'S',
					envioEmail : $("#envioEmail", this.workspace).val() == 'S',
					principal : $("#principal", this.workspace).attr('checked')  == 'checked',
					segunda : $("#PS", this.workspace).attr('checked')  == 'checked',		
					terca : $("#PT", this.workspace).attr('checked')  == 'checked',
					quarta : $("#PQ", this.workspace).attr('checked')  == 'checked',		
					quinta : $("#PQu", this.workspace).attr('checked')  == 'checked',	
					sexta : $("#PSex", this.workspace).attr('checked')  == 'checked',		
					sabado : $("#PSab", this.workspace).attr('checked')  == 'checked',		
					domingo : $("#PDom", this.workspace).attr('checked')  == 'checked'
			};
			
			$("input[name='radioFormaCobrancaBoleto']:checked", this.workspace).each(function(i) {			
				parametroCobranca.formaCobrancaBoleto =  $(this, this.workspace).val();
			});	
			
			
			var postObject = serializeObjectToPost("parametros", parametroCobranca);
			if(tipoFormaCobranca == 'MENSAL'){
				postObject = serializeArrayToPost("parametros.diasDoMes",[$('#diaDoMes', this.workspace).val()] ,postObject);
			}else if(tipoFormaCobranca == 'QUINZENAL'){
				postObject = serializeArrayToPost("parametros.diasDoMes",[$('#diaDoMes1', this.workspace).val(),$('#diaDoMes2', this.workspace).val()] ,postObject);
			}
			var listaIdsFornecedores = new Array();
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
			
			$("#tipoCobranca", this.workspace).val('');
			
			$("#formaCobranca", this.workspace).val('');
			$("#banco", this.workspace).val('');
			
			$("#valorMinimo", this.workspace).val('');
			$("#taxaMulta", this.workspace).val('');
			$("#valorMulta", this.workspace).val('');
			$("#taxaJuros", this.workspace).val('');
			
			$("#instrucoes", this.workspace).val('');
	
			$("#acumulaDivida", this.workspace).val('S');
			$("#vencimentoDiaUtil", this.workspace).val('S');
			$("#unificada", this.workspace).val('S');
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
	
			
			parametroCobrancaController.carregarFormasEmissao(null,"");
			$("input[name='checkGroupFornecedores']", this.workspace).each(function(i) {			
				$(this, this.workspace).attr('checked',false);
			});	
	
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
				           parametroCobrancaController.criaComponenteFormasEmissao(resultado,selected)
			           }
					   ,
					   null,
					   true);
		},
		
		
		criaComponenteFormasEmissao : function(result,selected) {
		    var comboFormasEmissao =  parametroCobrancaController.montarComboBox(result,"formaEmissao","",selected);
			$("#formasEmissao", this.workspace).html(comboFormasEmissao);
		},
	
		
		obterDadosBancarios : function(idBanco){
			var data = [{name: 'idBanco', value: idBanco}];
			$.postJSON(contextPath+"/distribuidor/parametroCobranca/obterDadosBancarios",
					   data,
					   parametroCobrancaController.sucessCallbackCarregarDadosBancarios,
					   null,
					   true);
		},
		
		
		sucessCallbackCarregarDadosBancarios : function(result) {
			$("#taxaMulta", this.workspace).val(result.multa);
			$("#valorMulta", this.workspace).val(result.vrMulta);
			$("#taxaJuros", this.workspace).val(result.juros);
			$("#instrucoes", this.workspace).val(result.instrucoes);
		}
		
	}	
	, 
	BaseController
);
