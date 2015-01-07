var fornecedorController = $.extend(true,{
		fecharModalCadastroFornecedor : false,
		init:function(){
			$('#fornecedorController-filtroConsultaFornecedorRazaoSocial,#fornecedorController-filtroConsultaFornecedorCnpj,#fornecedorController-filtroConsultaFornecedorNomeFantasia', fornecedorController.workspace).bind('keypress', function(e) {
				if(e.keyCode == 13) {
					fornecedorController.pesquisarFornecedores();
				}
			});
			
			$('#fornecedorController-filtroConsultaFornecedorRazaoSocial', fornecedorController.workspace).focus();
			$("#fornecedorController-fornecedoresGrid", fornecedorController.workspace).flexigrid({
				dataType : 'json',
				colModel : [  {
					display : 'C&oacute;digo',
					name : 'codigoInterface',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Raz&atilde;o Social',
					name : 'razaoSocial',
					width : 160,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNPJ',
					name : 'cnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Contato',
					name : 'responsavel',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-Mail',
					name : 'email',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : 'A&ccedil;&atilde;o',
					name : 'acao',
					width : 50,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				singleSelect: true,
				rp : 15,
				showTableToggleBtn : true,
				width : 950,
				height : 'auto'
			});
			
			$("#fornecedorController-validadeContrato", fornecedorController.workspace).datepicker({
				showOn : "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy'
			});
			
			$( "#fornecedorController-validadeContrato", fornecedorController.workspace ).mask("99/99/9999");
			
			$( "#fornecedorController-cnpj", fornecedorController.workspace ).mask("99.999.999/9999-99",{completed:function(){
				fornecedorController.obterPessoaJuridica();
			}});
					
			$( "#fornecedorController-validadeContrato", fornecedorController.workspace ).mask("99/99/9999");
			
			$( "#fornecedorController-tabFornecedores", fornecedorController.workspace ).tabs();
		
			jQuery(function($){
			   $.mask.definitions['#']='[\-\.0-9]';
			   $("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).mask("?##################",{placeholder:" "});
			});
			
			$("#fornecedorController-filtroConsultaFornecedorRazaoSocial", fornecedorController.workspace).autocomplete({source: ""});
			$("#fornecedorController-filtroConsultaFornecedorNomeFantasia", fornecedorController.workspace).autocomplete({source: ""});
			
		},
		
		
		getActionParaGridEnderecoFornecedor : function (idEndereco) {
			
			if(ENDERECO_FORNECEDOR.indBloqueiaCamposEdicaoFornecedor) {
				
				return  '<a href="javascript:;" isEdicao="true" onclick="ENDERECO_FORNECEDOR.editarEndereco(' 		 
				+ idEndereco 
				+ ')" ' 															
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' 	
				+ '<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' 			
				+ '</a>' 
				+ '<img src="'+contextPath+'/images/ico_excluir.gif" style="opacity:0.4" border="0px"/>'; 
				
			} else {
				
				return '<a href="javascript:;" isEdicao="true" onclick="ENDERECO_FORNECEDOR.editarEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' +
				'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" isEdicao="true" onclick="ENDERECO_FORNECEDOR.confirmarExclusaoEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
				'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
				
			}
		
		},		
		
		bloquearCamposEdicaoFornecedor : function(indBloqueiaCampo){
			
			$("#fornecedorController-codigoInterface", 		fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-razaoSocial", 			fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-nomeFantasia", 		fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-cnpj", 				fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-inscricaoEstadual", 	fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-responsavel", 			fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-email", 				fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-tipoFornecedor", 		fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-validadeContrato", 	fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-emailNfe", 			fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-possuiContrato", 		fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$("#fornecedorController-integraGFS", 		    fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			$( '#fornecedorController-validade', 			fornecedorController.workspace).prop('disabled', indBloqueiaCampo);
			
		},
		
		cancelarCadastro : function() {
			$("#dialog-cancelar-cadastro-fornecedor", fornecedorController.workspace).dialog({
				resizable: false,
				height:150,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON(contextPath + "/cadastro/fornecedor/cancelarCadastro", null, 
							function(result){
								
								fecharModalCadastroFornecedor = true;
								
								$("#fornecedorController-dialogNovoFornecedor", fornecedorController.workspace ).dialog( "close" );								
								$("#dialog-cancelar-cadastro-fornecedor", fornecedorController.workspace).dialog("close");
							}
						);
					},
					"Cancelar": function() {
						
						$(this).dialog("close");
						
						fecharModalCadastroFornecedor = false;
					}
				},
				form: $("#dialog-cancelar-cadastro-fornecedor", this.workspace).parents("form")
			});
		},
		
		novoFornecedor:	function (isEdicao, indBloqueiaCamposEdicaoFornecedor) {
				
				$("#fornecedorController-cnpj").attr('disabled', false);
			
				$("#fornecedorController-codigoInterface", fornecedorController.workspace).enable();
				
				if (!isEdicao) {
					
					fornecedorController.limparCamposModal();
					
					$.postJSON(
						 contextPath +"/cadastro/fornecedor/novoCadastro",
						null,
						function(result) {
							$("#fornecedorController-inicioAtividade", fornecedorController.workspace).html(result.data);
							$("#fornecedorController-codigoInterface").numeric();
							$("#fornecedorController-codigoInterface", fornecedorController.workspace).val(result.nextCodigo);
							
							fornecedorController.showPopupFornecedor();
							fornecedorController.bloquearCamposEdicaoFornecedor(false);
							ENDERECO_FORNECEDOR.bloquearCamposFormEndereco(false);
							FORNECEDOR.bloquearCamposFormTelefone(false);
						},
						null,
						true
					);
				
				} else {
					
					fornecedorController.showPopupFornecedor();
				}

				$( "#fornecedorController-tabFornecedores", fornecedorController.workspace ).tabs( "select" , 0 );
			},

			showPopupFornecedor: function () {
				
				fecharModalCadastroFornecedor = false;
				
				var dialog_novo_fornecedor = {
						resizable: false,
						height:570,
						width:840,
						modal: true,
						form: $("#fornecedorController-dialogNovoFornecedor", this.workspace).parents("form"),
						buttons : {},
						beforeClose: function(event, ui) {
							
							if (!fecharModalCadastroFornecedor){
								
								fornecedorController.cancelarCadastro();
								
								return fecharModalCadastroFornecedor;
							}
							
							return fecharModalCadastroFornecedor;
						},
						
				};
				

				dialog_novo_fornecedor.buttons = {
						
						"Confirmar": function() {
							fecharModalCadastroFornecedor = true;
							fornecedorController.cadastrarFornecedor();
						},
						"Cancelar": function() {
							$( this ).dialog( "close" );
						}
						
				};
				
				
				$( "#fornecedorController-dialogNovoFornecedor", fornecedorController.workspace ).dialog(dialog_novo_fornecedor);
			},
			
			limparCamposModal:	function () {
				$("#fornecedorController-idFornecedor", fornecedorController.workspace).val("");
				$("#fornecedorController-origem", fornecedorController.workspace).val("");
				$("#fornecedorController-codigoInterface", fornecedorController.workspace).val("");
				$("#fornecedorController-razaoSocial", fornecedorController.workspace).val("");
				$("#fornecedorController-nomeFantasia", fornecedorController.workspace).val("");
				$("#fornecedorController-cnpj", fornecedorController.workspace).val("");
				$("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).val("");
				$("#fornecedorController-responsavel", fornecedorController.workspace).val("");
				$("#fornecedorController-email", fornecedorController.workspace).val("");
				$("#fornecedorController-tipoFornecedor", fornecedorController.workspace).val("");
				$("#fornecedorController-banco", fornecedorController.workspace).val("");
				$("#fornecedorController-validadeContrato", fornecedorController.workspace).val("");
				$("#fornecedorController-emailNfe", fornecedorController.workspace).val("");
				$("#fornecedorController-possuiContrato", fornecedorController.workspace).uncheck();
				$( '#fornecedorController-validade', fornecedorController.workspace ).hide();
				FORNECEDOR.limparCamposTelefone();
				ENDERECO_FORNECEDOR.limparFormEndereco();
			},
			
			cadastrarFornecedor:function () {
				
				var manterDesabilitado = false;
				if ($("#fornecedorController-codigoInterface").attr('disabled')  == 'disabled' || $("#fornecedorController-cnpj").attr('disabled') == 'disabled') {
					manterDesabilitado = true;
					$("#fornecedorController-codigoInterface").attr('disabled', false);
					$("#fornecedorController-cnpj").attr('disabled', false);
				}
				
				var formData = $("#fornecedorController-formNovoFornecedor", fornecedorController.workspace).serializeArray();

				if (manterDesabilitado) {
					$("#fornecedorController-cnpj").attr('disabled', true);
				} else {
					$("#fornecedorController-codigoInterface").attr('disabled', false);
					$("#fornecedorController-cnpj").attr('disabled', false);
				}
				
				$.postJSON(
					 contextPath +"/cadastro/fornecedor/cadastrarFornecedor",
					formData,
					function(result) {
						
						$( "#fornecedorController-dialogNovoFornecedor", fornecedorController.workspace ).dialog( "close" );
						
						exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
						);
						fornecedorController.pesquisarFornecedores();
					},
					function(result) {
						exibirMensagemDialog(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens, "fornecedorController-dialogNovoFornecedor"
						);
					},
					true
				);
			},
			
			pesquisarFornecedores:function () {
				
				var formData = $("#fornecedorController-formularioPesquisaFornecedores", fornecedorController.workspace).serializeArray();

				$("#fornecedorController-fornecedoresGrid", fornecedorController.workspace).flexOptions({
					url :  contextPath +"/cadastro/fornecedor/pesquisarFornecedores",
					preProcess : fornecedorController.processarResultadoFornecedores,
					params : formData
				});
				
				$("#fornecedorController-fornecedoresGrid", fornecedorController.workspace).flexReload();

				$(".fornecedorController-grids", fornecedorController.workspace).show();
			},

			processarResultadoFornecedores:function (data) {
				
				if (data.mensagens) {

					exibirMensagem(
						data.mensagens.tipoMensagem,
						data.mensagens.listaMensagens
					);

					$(".fornecedorController-grids", fornecedorController.workspace).hide();

					return;
				}

				data = data.tableModel;

				var i;

				for (i = 0; i < data.rows.length; i++) {

					if (!data.rows[i].cell.codigoInterface) {
						
						data.rows[i].cell.codigoInterface = "";
					}
					
					if (!data.rows[i].cell.razaoSocial) {
						
						data.rows[i].cell.razaoSocial = "";
					}
								
					if (!data.rows[i].cell.cnpj) {
						
						data.rows[i].cell.cnpj = "";
					}else{
						data.rows[i].cell.cnpj = Cnpj(data.rows[i].cell.cnpj);
					}
					
					if (!data.rows[i].cell.responsavel) {
						
						data.rows[i].cell.responsavel = "";
					}
					
					if (!data.rows[i].cell.telefone) {
						
						data.rows[i].cell.telefone = "";
					}

					if (!data.rows[i].cell.email) {
						
						data.rows[i].cell.email = "";
					}
					
					if (!data.rows[i].cell.emailNfe) {
						
						data.rows[i].cell.emailNfe = "";
					}
					
					var isHabilitado = data.rows[i].cell.origem != 'INTERFACE';
					
					data.rows[i].cell.acao = fornecedorController.getActionFornecedor(data.rows[i].id, isHabilitado);
				}

				if (data.rows.length < 0) {

					$(".fornecedorController-grids", fornecedorController.workspace).hide();

					return;
				} 

				$(".fornecedorController-grids", fornecedorController.workspace).show();

				return data;
			},

			editarFornecedor:function (idFornecedor, indBloqueiaCamposEdicaoFornecedor) {
				
				$.postJSON(
					 contextPath +"/cadastro/fornecedor/editarFornecedor",
					{'idFornecedor': idFornecedor},
					function(result) {
						
						$("#fornecedorController-inicioAtividade", fornecedorController.workspace).html(result.inicioAtividade);
						
						$("#fornecedorController-idFornecedor", fornecedorController.workspace).val(result.idFornecedor);
						$("#fornecedorController-origem", fornecedorController.workspace).val(result.origem);
						$("#fornecedorController-codigoInterface", fornecedorController.workspace).val(result.codigoInterface);
						$("#fornecedorController-razaoSocial", fornecedorController.workspace).val(result.razaoSocial);
						$("#fornecedorController-nomeFantasia", fornecedorController.workspace).val(result.nomeFantasia);
						$("#fornecedorController-cnpj", fornecedorController.workspace).val(Cnpj(result.cnpj));
						$("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).val(result.inscricaoEstadual);
						$("#fornecedorController-responsavel", fornecedorController.workspace).val(result.responsavel);
						$("#fornecedorController-email", fornecedorController.workspace).val(result.email);
						$("#fornecedorController-emailNfe", fornecedorController.workspace).val(result.emailNfe);
						$("#fornecedorController-tipoFornecedor", fornecedorController.workspace).val(result.tipoFornecedor);
						$("#fornecedorController-banco", fornecedorController.workspace).val(result.idBanco);
						$("#fornecedorController-canalDistribuicao", fornecedorController.workspace).val(result.canalDistribuicao);
						
						if (result.integraGFS) {
							$("#fornecedorController-integraGFS", fornecedorController.workspace).check();
						} else {
							
							$("#fornecedorController-integraGFS", fornecedorController.workspace).uncheck();
						}
						
						if (result.possuiContrato) {

							$("#fornecedorController-possuiContrato", fornecedorController.workspace).check();

							$("#fornecedorController-validadeContrato", fornecedorController.workspace).val(result.validadeContrato);	
						
							$( '.fornecedorController-validade', fornecedorController.workspace ).show();
							
						} else {
							
							$("#fornecedorController-possuiContrato", fornecedorController.workspace).uncheck();
							
							$("#fornecedorController-validadeContrato", fornecedorController.workspace).val("");
							
							$( '.fornecedorController-validade', fornecedorController.workspace ).hide();
						}
						fornecedorController.novoFornecedor(true, indBloqueiaCamposEdicaoFornecedor);
						
						ENDERECO_FORNECEDOR.getAction = fornecedorController.getActionParaGridEnderecoFornecedor;
						ENDERECO_FORNECEDOR.indBloqueiaCamposEdicaoFornecedor = indBloqueiaCamposEdicaoFornecedor;
						ENDERECO_FORNECEDOR.popularGridEnderecos();

						FORNECEDOR.getActions = fornecedorController.getActionParaGridTelefoneFornecedor;
						FORNECEDOR.indBloqueiaCamposEdicaoFornecedor = indBloqueiaCamposEdicaoFornecedor;
						FORNECEDOR.carregarTelefones();
						
						fornecedorController.bloquearCamposEdicaoFornecedor(indBloqueiaCamposEdicaoFornecedor);
						
						ENDERECO_FORNECEDOR.bloquearCamposFormEndereco(indBloqueiaCamposEdicaoFornecedor);
						
						FORNECEDOR.bloquearCamposFormTelefone(indBloqueiaCamposEdicaoFornecedor);

						$("#fornecedorController-cnpj", fornecedorController.workspace).prop('disabled', true);
						
						$("#fornecedorController-codigoInterface", fornecedorController.workspace).disable();
						
					},
					function(result) {
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
					}
				);
			},
			
			getActionParaGridTelefoneFornecedor : function(idTelefone) {

				if(FORNECEDOR.indBloqueiaCamposEdicaoFornecedor) {

					return '<a href="javascript:;" isEdicao="true" onclick="FORNECEDOR.editarTelefone('
					+ idTelefone
					+ ')" '
					+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar telefone">'
					+ '<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>'
					+ '</a>'
					+ '<img src="'+contextPath+'/images/ico_excluir.gif" style="opacity:0.4" border="0px"/>';
					
					
				} else {
					
					return '<a href="javascript:;" isEdicao="true" onclick="FORNECEDOR.editarTelefone('
					+ idTelefone
					+ ')" '
					+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar telefone">'
					+ '<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>'
					+ '</a>'
					+ '<a href="javascript:;" isEdicao="true" onclick="FORNECEDOR.removerTelefone('
					+ idTelefone
					+ ')" '
					+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir telefone">'
					+ '<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>'
					+ '</a>';
					
				}
				
			},
			
			getActionFornecedor : function(idFornecedor, isHabilitado) {

				if (isHabilitado) {
				
					return '<a href="javascript:;"  onclick="fornecedorController.editarFornecedor('
							+ idFornecedor 
							+ ','
							+ false
							+ ')" '
							+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar fornecedor">'
							+ '<img src="'+contextPath +'/images/ico_editar.gif" border="0px"/>'
							+ '</a>'
							+ '<a href="javascript:;" onclick="fornecedorController.confirmarExclusaoFornecedor('
							+ idFornecedor
							+ ')" '
							+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir fornecedor">'
							+ '<img src="'+contextPath + '/images/ico_excluir.gif" border="0px"/>'
							+ '</a>';
				
				} else {
					
					return 	'<a href="javascript:;" onclick="fornecedorController.editarFornecedor('
							+ idFornecedor 
							+ ','
							+ true
							+ ')" '
							+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar fornecedor">'
							+ '<img src="'+contextPath +'/images/ico_editar.gif" border="0px"/>'
							+ '</a>'
						 	+ '<img src="'+contextPath + '/images/ico_excluir.gif" border="0px"'
						 	+ ' style="opacity:0.4"/>';

				}
			},

			confirmarExclusaoFornecedor:function (idFornecedor) {
				
				if(!verificarPermissaoAcesso(fornecedorController.workspace)){
					return;
				}
				
				
				$( "#fornecedorController-dialog-excluir", fornecedorController.workspace ).dialog({
					resizable: false,
					height:170,
					width:380,
					modal: true,
					buttons: {
						"Confirmar": function() {
							fornecedorController.excluirFornecedor(idFornecedor);
							$( this ).dialog( "close" );
						},
						"Cancelar": function() {
							$( this ).dialog( "close" );
						}
					},
					form: $("#fornecedorController-dialog-excluir", this.workspace).parents("form")
				});
			},
			
			excluirFornecedor:function (idFornecedor) {
			
				$.postJSON(
					 contextPath +"/cadastro/fornecedor/excluirFornecedor",
					{'idFornecedor': idFornecedor},
					function(result) {
						exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
						);
						
						fornecedorController.pesquisarFornecedores();
					},
					function(result) {
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
					}
				);
			},
			
			obterPessoaJuridica:function () {				
				$.postJSON(
						 contextPath +"/cadastro/fornecedor/obterPessoaJuridica",
						{'cnpj': $("#fornecedorController-cnpj", fornecedorController.workspace).val()},
						function(result) {
							
							if (result.razaoSocial) 
								$("#fornecedorController-razaoSocial", fornecedorController.workspace).val(result.razaoSocial);
							if (result.nomeFantasia)
								$("#fornecedorController-nomeFantasia", fornecedorController.workspace).val(result.nomeFantasia);
							if (result.inscricaoEstadual)
								$("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).val(result.inscricaoEstadual);
							if (result.email)
								$("#fornecedorController-email", fornecedorController.workspace).val(result.email);
						},
						null,
						true
					);
			},
			

			mostraValidade:function () {
				
				if ($(".fornecedorController-validade", fornecedorController.workspace).css("display") == "none") {

					$( '.fornecedorController-validade', fornecedorController.workspace ).fadeIn( "slow" );
				
				} else {

					$( '.fornecedorController-validade', fornecedorController.workspace ).val("");

					$( '.fornecedorController-validade', fornecedorController.workspace ).fadeOut( "slow" );
				}

			}		
}, BaseController);

//@ sourceURL=fornecedor.js
