//@ sourceURL=fiadorjs
var fiadorController = {
		fecharModalCadastroFiador: false,
		addConjuge : false,
		
		popupCadastroFiadorCPF:function () {
			
			$("#fiadorController-tabSocio").hide();
			$('#fiadorController-tabs').tabs('select', 0);
			
			$("#fiadorController-cadastroCnpj").hide();
			$("#fiadorController-cadastroCpf").show();
			
			this.modalCadastroFiador("CPF");
		},
	
		popupCadastroFiadorCNPJ:function () {
			
			$("#fiadorController-tabSocio").show();
			$('#fiadorController-tabs').tabs('select', 0);
			
			$("#fiadorController-cadastroCnpj").show();
			$("#fiadorController-cadastroCpf").hide();
			
			this.modalCadastroFiador("CNPJ");
		},
		
		modalCadastroFiador: function (paramCpfCnpj){
			var _this = this;
			this.fecharModalCadastroFiador = false;
			
			this.limparCamposCadastroFiador();
			
			$("#fiadorController-dialog-fiador").dialog({
				resizable: false,
				height:610,
				width:840,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						if (paramCpfCnpj == "CPF") {
							_this.cadastrarFiadorCpf(this);
						} else {
							_this.cadastrarFiadorCnpj(this);
						}
					},
					"Cancelar": function() {
						
						$(this).dialog("close");
					}
				},
				beforeClose: function(event, ui) {
					
					if (!_this.fecharModalCadastroFiador){
						_this.cancelarCadastro();
					}
					
					return _this.fecharModalCadastroFiador;
				}
			});
		
			$(".fiadorController-trSocioPrincipal").hide();
		},
		
		cancelarCadastro:function (){
			var _this =  this;
			$("#fiadorController-dialog-cancelar-cadastro-fiador").dialog({
				resizable: false,
				height:150,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON(contextPath + '/cadastro/fiador/cancelarCadastro', null, 
							function(result){
								_this.fecharModalCadastroFiador = true;
								$("#fiadorController-dialog-close").dialog("close");
								$("#fiadorController-dialog-fiador").dialog("close");
								$("#fiadorController-dialog-cancelar-cadastro-fiador").dialog("close");					
								
								_this.limparCamposCadastroFiador();
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
						_this.fecharModalCadastroFiador = false;
					}
				}
			});
		},
	
		popup_excluir:function () {
			var _this = this;
			$( "#fiadorController-dialog-excluir" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						_this.cadastrarFiadorCnpj(this);
					},
					"Cancelar": function() {
						$(this).dialog( "close" );
					}
				}
			});
		},
		
		exibirGridFiadoresCadastrados:function (){
			var data = "filtro.nome=" + $("#fiadorController-nomeFiadorPesquisa").val() + "&filtro.cpfCnpj=" + $("#fiadorController-cpfCnpjFiadorPesquisa").val();
			$.postJSON(contextPath + '/cadastro/fiador/pesquisarFiador', data, 
				function(result){
					
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados,.fiadorController-gridFiadoresCadastrados").show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados,.fiadorController-gridFiadoresCadastrados").hide();
					}
				}
			);
		},
		
		init : function() {
			var _this  = this;
			$("#fiadorController-tabs").tabs();
			
			$(".fiadorController-pessoasGrid").flexigrid({
				preProcess: function(data){return _this.processarResultadoConsultaFiadores(data);},
				dataType : 'json',
				colModel : [  {
					display : 'Código',
					name : 'codigo',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nome',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'CPF / CNPJ',
					name : 'cpfCnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'RG / Inscrição Estadual',
					name : 'rg',
					width : 110,
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
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "desc",
				disableSelect: true,
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			});
			
			$(".fiadorController-pessoasGrid").flexOptions({url:contextPath + '/cadastro/fiador/pesquisarFiador'});
			
			
			$("#fiadorController-cnpjFiador").mask("99.999.999/9999-99",{completed:function(){_this.buscarPessoaCNPJ(this.val()); }});
			jQuery(function($){
				   $.mask.definitions['#']='[\-\.0-9]';
				   $("#fiadorController-inscricaoEstadualFiador").mask("?##################",{placeholder:" "});
			});
			
			$('#fiadorController-cpfFiador').mask("999.999.999-99",{completed:function(){
				_this.buscarPessoaCPF(this.value, true);
			}});	
			$('#fiadorController-socio-cpfFiador').mask("999.999.999-99",{completed:function(){
				_this.buscarPessoaCPF(this.value, true);
			}});
			$('#fiadorController-cpfConjuge, #fiadorController-socio-cpfFiador').mask("999.999.999-99");
			$('#fiadorController-dataNascimentoFiadorCpf,#fiadorController-socio-dataNascimentoFiadorCpf').mask("99/99/9999");
			$('#fiadorController-dataNascimentoConjugeCpf,#fiadorController-socio-dataNascimentoConjugeCpf').mask("99/99/9999");
			$('#fiadorController-selectUfOrgaoEmiCpf,#fiadorController-socio-selectUfOrgaoEmiConjugeCpf').mask("aa");
			
			$('.justLetter').justLetter();
			
			$(".fiadorController-sociosGrid").flexigrid({
				preProcess:function(data){return _this.processarResultadoSocios(data);},
				dataType : 'json',
				colModel : [  {
					display : 'Nome',
					name : 'nome',
					width : 580,
					sortable : true,
					align : 'left'
				}, {
					display : 'Principal',
					name : 'principal',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				width : 770,
				height : 160
			});
			
			$(".fiadorController-cotasAssociadasGrid").flexOptions({url:contextPath + '/cadastro/fiador/pesquisarSociosFiador'});
			
			
			
			$(".fiadorController-imoveisGrid").flexigrid({
				preProcess: function(data){return _this.processarResultadoGarantias(data);},
				dataType : 'json',
				colModel : [ {
					display : 'Descrição',
					name : 'garantia.descricao',
					width : 510,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor R$',
					name : 'garantia.valor',
					width : 130,
					sortable : true,
					align : 'right'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				} ],
				width : 770,
				height : 150,
				sortname : "garantia.descricao",
				sortorder : "asc",
				disableSelect: true
			});
			
			$(".fiadorController-imoveisGrid").flexOptions({url:contextPath + '/cadastro/fiador/obterGarantiasFiador'});
			
			$("#fiadorController-garantia-valorGarantia").numeric();
			$("#fiadorController-garantia-valorGarantia").priceFormat({
			    centsSeparator: ',',
			    thousandsSeparator: '.',
			    centsLimit: 4
			});
			
			$(".fiadorController-cotasAssociadasGrid").flexigrid({
				preProcess: function(data){return _this.processarResultadoCotasAssociadas(data);},
				url: contextPath + '/cadastro/fiador/obterAssociacoesCotaFiador',
				dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 620,
					sortable : true,
					align : 'left'
				}, {
					display : '',
					name : 'sel',
					width : 30,
					sortable : false,
					align : 'center'
				} ],
				width : 770,
				height : 150,
				sortname : "numeroCota",
				sortorder : "asc",
				disableSelect: true
			});
			
			$("#numeroCota").numeric();
		
			
			
		},
		
		processarResultadoConsultaFiadores:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result[1].rows;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;

				data.rows[i].cell[lastIndex] = this.getActionsConsultaFiadores(data.rows[i].id);
			}

			$('.fiadorController-socio-imoveisGrid').show();
			
			if (data.result){
				return data.result[1];
			}
			return data;
		},
		
		getActionsConsultaFiadores: function (idFiador){
			return '<a href="javascript:;" onclick="fiadorController.editarFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Fiador">' +
					'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="fiadorController.excluirFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fiador">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		},
		
		editarFiador:function (idFiador){
			var _this = this;
			$.postJSON(contextPath +'/cadastro/fiador/editarFiador', "idFiador=" + idFiador, 
				function(result) {
					
					$(".fiadorController-inicioAtividadeNovo").hide();
					$(".fiadorController-inicioAtividadeEdicao").show();
					
					_this.limparCamposCadastroFiador();
				
					if (result[0] == "CPF"){
						
						_this.popupCadastroFiadorCPF();
						
						$("#fiadorController-nomeFiadorCpf").val(result[1]);
						$("#fiadorController-emailFiadorCpf").val(result[2]);
						$("#fiadorController-cpfFiador").val(result[3]).attr("disabled", true);
						$("#fiadorController-rgFiador").val(result[4]);
						$("#fiadorController-dataNascimentoFiadorCpf").val(result[5]);
						$("#fiadorController-orgaoEmissorFiadorCpf").val(result[6]);
						$("#fiadorController-selectUfOrgaoEmiCpf").val(result[7]);
						$("#fiadorController-estadoCivilFiadorCpf").val(result[8]);
						$("#fiadorController-selectSexoFiador").val(result[9]);
						$("#fiadorController-nacionalidadeFiadorCpf").val(result[10]);
						$("#fiadorController-naturalFiadorCpf").val(result[11]);
						
						if (result[8] == "CASADO"){
							
							_this.opcaoCivilPf("CASADO",'fiadorController-');
							
							$("#fiadorController-nomeConjugeCpf").val(result[12]);
					        $("#fiadorController-emailConjugeCpf").val(result[13]);
					        $("#fiadorController-cpfConjuge").val(result[14]).attr("disabled", true);
					        $("#fiadorController-rgConjuge").val(result[15]);
					        $("#fiadorController-dataNascimentoConjugeCpf").val(result[16]);
					        $("#fiadorController-orgaoEmissorConjugeCpf").val(result[17]);
					        $("#fiadorController-selectUfOrgaoEmiConjugeCpf").val(result[18]);
					        $("#fiadorController-selectSexoConjuge").val(result[19]);
					        $("#fiadorController-nacionalidadeConjugeCpf").val(result[20]);
					        $("#fiadorController-naturalConjugeCpf").val(result[21]);
					        
					        $(".fiadorController-inicioAtividadeEdicao").text(result[22]);
					        
						} else {
							$(".fiadorController-inicioAtividadeEdicao").text(result[12]);
						}
						
					} else {
						
						_this.popupCadastroFiadorCNPJ();
						
						$("#fiadorController-razaoSocialFiador").val(result[1]);
						$("#fiadorController-nomeFantasiaFiador").val(result[2]);
						$("#fiadorController-inscricaoEstadualFiador").val(result[3]);
						$("#fiadorController-cnpjFiador").val(result[4]);
						$("#fiadorController-emailFiadorCnpj").val(result[5]);
						$(".fiadorController-inicioAtividadeEdicao").text(result[6]);
						
						$("#fiadorController-cnpjFiador").attr("disabled", true);
					}
				}
			);
		},
		
		excluirFiador:function (idFiador){
			$(".fiadorController-dialog-excluir-fiador").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath + '/cadastro/fiador/excluirFiador', "idFiador=" + idFiador, 
							function(result) {
								
								if (result[0].tipoMensagem){
									exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
								}
								
								if (result[1] != ""){
									$(".fiadorController-pessoasGrid").flexAddData({
										page: result[1].page, total: result[1].total, rows: result[1].rows
									});
									
									$("#fiadorController-gridFiadoresCadastrados").show();
								} else {
									$("#fiadorController-gridFiadoresCadastrados").hide();
								}
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$(".fiadorController-dialog-excluir-fiador").show();
		},
		
		
		limparCamposCadastroFiador:function (){
			//dados cadastrais cpf
			this.limparDadosCadastraisCPFFiador();
	        
	        //dados cadastrais cnpj
			this.limparDadosCadastraisCNPJ();
	        
			//dados cadastrais socios
			this.limparDadosCadastraisCPFSocio();
		    
		    //endereÃ§os
			ENDERECO_FIADOR.limparFormEndereco();
		    
		    //telefones
		    FIADOR.limparCamposTelefone();
		    
		    //garantias
		    this.limparCamposGarantias();
		    
		    //cotas associadas
		    this.limparCamposCotasAssociadas();
		    
		    this.opcaoCivilPf("",'fiadorController-socio-');
		    this.opcaoCivilPf("",'fiadorController-');
		},
		cadastrarFiadorCnpj :function (janela){
			var _this = this;
			this.fecharModalCadastroFiador = true;
			
			var data = "fiador.razaoSocial=" + $("#fiadorController-razaoSocialFiador").val() + "&" +
			           "fiador.nomeFantasia=" + $("#fiadorController-nomeFantasiaFiador").val() + "&" +
			           "fiador.inscricaoEstadual=" + $("#fiadorController-inscricaoEstadualFiador").val() + "&" +
			           "fiador.cnpj=" + $("#fiadorController-cnpjFiador").val() + "&" +
			           "fiador.email=" + $("#fiadorController-emailFiadorCnpj").val();
			
			$.postJSON(contextPath + '/cadastro/fiador/cadastrarFiadorCnpj', data, 
				function(result){
			
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados").show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados").hide();
					}
					
					if (result[0].tipoMensagem == "SUCCESS"){
						$(janela).dialog("close");
						$("#fiadorController-cnpjFiador").removeAttr("disabled");
					}
				},
				function(){
					_this.fecharModalCadastroFiador = false;
				},
				true
			);
		},
		
		limparDadosCadastraisCNPJ:function (){
			$("#fiadorController-razaoSocialFiador").val("");
			$("#fiadorController-nomeFantasiaFiador").val("");
			$("#fiadorController-inscricaoEstadualFiador").val("");
			$("#fiadorController-cnpjFiador").val("");
			$("#fiadorController-emailFiadorCnpj").val("");
		},
		
		buscarPessoaCNPJ:function (cnpj){
			
			if (cnpj != "__.___.___/____-__" && cnpj != ""){
				
				$.postJSON(contextPath + '/cadastro/fiador/buscarPessoaCNPJ', "cnpj=" + cnpj, 
					function(result) {
						
						if (result[0] != undefined){
							$("#fiadorController-razaoSocialFiador").val(result[0]);
							$("#fiadorController-nomeFantasiaFiador").val(result[1]);
							$("#fiadorController-inscricaoEstadualFiador").val(result[2]);
							$("#fiadorController-cnpjFiador").val(result[3]);
							$("#fiadorController-emailFiadorCnpj").val(result[4]);
						}
					},
					null,
					true
				);
			}
		},
		
		opcaoCivilPf:function (valor,prefix){
			if (valor == "CASADO"){
				$("."+prefix+"divConjuge").show();
				this.addConjuge = true;
			} else {
				$("."+prefix+"divConjuge").hide();
				this.addConjuge = false;
			}
		},
		
		cadastrarFiadorCpf:function (janela){
			var _this = this;
			this.fecharModalCadastroFiador = true;
			
			var data = "pessoa.nome=" + $("#fiadorController-nomeFiadorCpf").val() + "&" +
			           "pessoa.email=" + $("#fiadorController-emailFiadorCpf").val() + "&" +
			           "pessoa.cpf=" + $("#fiadorController-cpfFiador").val() + "&" +
			           "pessoa.rg=" + $("#fiadorController-rgFiador").val() + "&" +
			           "pessoa.dataNascimento=" + $("#fiadorController-dataNascimentoFiadorCpf").val() + "&" +
			           "pessoa.orgaoEmissor=" + $("#fiadorController-orgaoEmissorFiadorCpf").val() + "&" +
			           "pessoa.ufOrgaoEmissor=" + $("#fiadorController-selectUfOrgaoEmiCpf").val() + "&" +
			           "pessoa.estadoCivil=" + $("#fiadorController-estadoCivilFiadorCpf").val() + "&" +
			           "pessoa.sexo=" + $("#fiadorController-selectSexoFiador").val() + "&" +
			           "pessoa.nacionalidade=" + $("#fiadorController-nacionalidadeFiadorCpf").val() + "&" +
			           "pessoa.natural=" + $("#fiadorController-naturalFiadorCpf").val();
			
			if (this.addConjuge){ 
			           data = data + "&pessoa.conjuge.nome=" + $("#fiadorController-nomeConjugeCpf").val() + "&" +
			           "pessoa.conjuge.email=" + $("#fiadorController-emailConjugeCpf").val() + "&" +
			           "pessoa.conjuge.cpf=" + $("#fiadorController-cpfConjuge").val() + "&" +
			           "pessoa.conjuge.rg=" + $("#fiadorController-rgConjuge").val() + "&" +
			           "pessoa.conjuge.dataNascimento=" + $("#fiadorController-dataNascimentoConjugeCpf").val() + "&" +
			           "pessoa.conjuge.orgaoEmissor=" + $("#fiadorController-orgaoEmissorConjugeCpf").val() + "&" +
			           "pessoa.conjuge.ufOrgaoEmissor=" + $("#fiadorController-selectUfOrgaoEmiConjugeCpf").val() + "&" +
			           "pessoa.conjuge.sexo=" + $("#fiadorController-selectSexoConjuge").val() + "&" +
			           "pessoa.conjuge.nacionalidade=" + $("#fiadorController-nacionalidadeConjugeCpf").val() + "&" +
			           "pessoa.conjuge.natural=" + $("#fiadorController-naturalConjugeCpf").val();
			}
			
			$.postJSON(contextPath + '/cadastro/fiador/cadastrarFiadorCpf', data, 
				function(result){
						
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados").show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados").hide();
					}
					
					if (result[0].tipoMensagem == "SUCCESS"){
						$(janela).dialog("close");
						$("#fiadorController-cpfFiador").removeAttr("disabled");
						$("#fiadorController-cpfConjuge").removeAttr("disabled");
					}
				},
				function (){
					_this.fecharModalCadastroFiador = false;
				},
				true
			);
		},
		limparDadosCadastraisCPFFiador:function(){
			$('input','#fiadorController-cadastroCpf').attr('disabled', false).val("").uncheck();
		},
		limparDadosCadastraisCPFSocio:function(){
			$('input','#fiadorController-tab-socios').attr('disabled', false).val("").uncheck();
		},
		buscarPessoaCPF:function (cpf, fiador){
			var _this = this;
			
			if (cpf != "___.___.___-__" && cpf != ""){
				
				var data = "cpf=" + cpf + "&isFiador=" + fiador + "&cpfConjuge=" + $('#fiadorController-cpfConjuge').val() +
					"&socio=" + (!fiador);
				
				$.postJSON(contextPath +  '/cadastro/fiador/buscarPessoaCPF', data, 
					function(result) {
					
						if (result[0] != undefined){
							
							if (fiador){
								$('#fiadorController-nomeFiadorCpf').val(result[0]);
								$('#fiadorController-emailFiadorCpf').val(result[1]);
								$('#fiadorController-cpfFiador').val(result[2]);
								$('#fiadorController-rgFiador').val(result[3]);
								$('#fiadorController-dataNascimentoFiadorCpf').val(result[4]);
								$('#fiadorController-orgaoEmissorFiadorCpf').val(result[5]);
								$('#fiadorController-selectUfOrgaoEmiCpf').val(result[6]);
								$('#fiadorController-estadoCivilFiadorCpf').val(result[7]);
								$('#fiadorController-selectSexoFiador').val(result[8]);
								$('#fiadorController-nacionalidadeFiadorCpf').val(result[9]);
								$('#fiadorController-naturalFiadorCpf').val(result[10]);
								
								if (result[7] == "CASADO"){
									
									_this.opcaoCivilPf(result[7],'fiadorController-');
									
									$('#fiadorController-nomeConjugeCpf').val(result[11]);
									$('#fiadorController-emailConjugeCpf').val(result[12]);
									$('#fiadorController-cpfConjuge').val(result[13]);
									$('#fiadorController-rgConjuge').val(result[14]);
									$('#fiadorController-dataNascimentoConjugeCpf').val(result[15]);
									$('#fiadorController-orgaoEmissorConjugeCpf').val(result[16]);
									$('#fiadorController-selectUfOrgaoEmiConjugeCpf').val(result[17]);
									$('#fiadorController-selectSexoConjuge').val(result[18]);
									$('#fiadorController-nacionalidadeConjugeCpf').val(result[19]);
									$('#fiadorController-naturalConjugeCpf').val(result[20]);
								}
							} else {
								
								$('#fiadorController-socio-nomeConjugeCpf').val(result[0]);
								$('#fiadorController-socio-emailConjugeCpf').val(result[1]);
								$('#fiadorController-socio-cpfConjuge').val(result[2]);
								$('#fiadorController-socio-rgConjuge').val(result[3]);
								$('#fiadorController-socio-dataNascimentoConjugeCpf').val(result[4]);
								$('#fiadorController-socio-orgaoEmissorConjugeCpf').val(result[5]);
								$('#fiadorController-socio-selectUfOrgaoEmiConjugeCpf').val(result[6]);
								$('#fiadorController-socio-selectSexoConjuge').val(result[8]);
								$('#fiadorController-socio-nacionalidadeConjugeCpf').val(result[9]);
								$('#fiadorController-socio-naturalConjugeCpf').val(result[10]);
							}
						}
					},
					null,
					true
				);
			}
		},
		
		carregarSocios:function (){
			$.postJSON(contextPath + '/cadastro/fiador/pesquisarSociosFiador', null, 
				function(result) {
					$(".fiadorController-sociosGrid").flexAddData({
						page: 1, total: 1, rows: result.rows
					});	
					
					$("#fiadorController-btnAddEditarSocio").text("Incluir Novo");
				},
				null,
				true
			);
		},
		
		processarResultadoSocios:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result.rows;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;

				data.rows[i].cell[lastIndex] = fiadorController.getActionsSocios(data.rows[i].id);
				
				data.rows[i].cell[1] = 
					data.rows[i].cell[1] == "true" ? '<img src="'+contextPath+'/images/ico_check.gif" border="0px"/>'
						                           : '&nbsp;';
			}

			$('.fiadorController-sociosGrid').show();
			
			if (data.result){
				return data.result;
			}
			return data;
		},
		
		getActionsSocios:function (idSocio){
			return '<a href="javascript:;" onclick="fiadorController.editarSocio(' + idSocio + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Socio">' +
					'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="fiadorController.removerSocio(' + idSocio + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Socio">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		},
		
		adicionarSocio: function (){
			var data = "pessoa.nome=" + $('#fiadorController-socio-nomeFiadorCpf').val() + "&" +
				"pessoa.email=" + $('#fiadorController-socio-emailFiadorCpf').val() + "&" +
		        "pessoa.cpf=" + $('#fiadorController-socio-cpfFiador').val() + "&" +
		        "pessoa.rg=" + $('#fiadorController-socio-rgFiador').val() + "&" +
		        "pessoa.dataNascimento=" + $('#fiadorController-socio-dataNascimentoFiadorCpf').val() + "&" +
		        "pessoa.orgaoEmissor=" + $('#fiadorController-socio-orgaoEmissorFiadorCpf').val() + "&" +
		        "pessoa.ufOrgaoEmissor=" + $('#fiadorController-socio-selectUfOrgaoEmiCpf').val() + "&" +
		        "pessoa.estadoCivil=" + $('#fiadorController-socio-estadoCivilFiadorCpf').val() + "&" +
		        "pessoa.sexo=" + $('#fiadorController-socio-selectSexoFiador').val() + "&" +
		        "pessoa.nacionalidade=" + $('#fiadorController-socio-nacionalidadeFiadorCpf').val() + "&" +
		        "pessoa.natural=" + $('#fiadorController-socio-naturalFiadorCpf').val() + "&" +
		        "pessoa.socioPrincipal=" + ("" + $('#fiadorController-socio-checkboxSocioPrincipal').attr("checked") == 'checked');
		    
		    if (this.addConjuge){ 
		        data = data + "&pessoa.conjuge.nome=" + $('#fiadorController-socio-nomeConjugeCpf').val() + "&" +
		        "pessoa.conjuge.email=" + $('#fiadorController-socio-emailConjugeCpf').val() + "&" +
		        "pessoa.conjuge.cpf=" + $('#fiadorController-socio-cpfConjuge').val() + "&" +
		        "pessoa.conjuge.rg=" + $('#fiadorController-socio-rgConjuge').val() + "&" +
		        "pessoa.conjuge.dataNascimento=" + $('#fiadorController-socio-dataNascimentoConjugeCpf').val() + "&" +
		        "pessoa.conjuge.orgaoEmissor=" + $('#fiadorController-socio-orgaoEmissorConjugeCpf').val() + "&" +
		        "pessoa.conjuge.ufOrgaoEmissor=" + $('#fiadorController-socio-selectUfOrgaoEmiConjugeCpf').val() + "&" +
		        "pessoa.conjuge.sexo=" + $('#fiadorController-socio-selectSexoConjuge').val() + "&" +
		        "pessoa.conjuge.nacionalidade=" + $('#fiadorController-socio-nacionalidadeConjugeCpf').val() + "&" +
		        "pessoa.conjuge.natural=" + $('#fiadorController-socio-naturalConjugeCpf').val();
		    }
		    
		    data = data + "&referencia=" + $("#fiadorController-socio-idSocioEdicao").val();
		    var _this =this;
			$.postJSON(contextPath +'/cadastro/fiador/adicionarSocio', data, 
				function(result){
			
					if (result != ""){
						$(".fiadorController-sociosGrid").flexAddData({
							page: result.page, total: result.total, rows: result.rows
						});
						
						_this.limparDadosCadastraisCPFSocio();
						
						_this.opcaoCivilPf("",'fiadorController-socio-');
						
						$("#fiadorController-btnAddEditarSocio").text("Incluir Novo");
						
						$('#fiadorController-socio-cpfFiador').removeAttr("disabled");
						$('#fiadorController-socio-cpfConjuge').removeAttr("disabled");
						
						$("#fiadorController-socio-idSocioEdicao").val("");
					}
				},
				null,
				true
			);
		},
		
		editarSocio : function (referencia){
			var _this = this;
			$.postJSON(contextPath + '/cadastro/fiador/editarSocio', "referencia=" + referencia, 
				function(result) {
					
				_this.limparDadosCadastraisCPFSocio();
				
					if (result){
						
						$('#fiadorController-socio-nomeFiadorCpf').val(result[0]);
						$('#fiadorController-socio-emailFiadorCpf').val(result[1]);
						$('#fiadorController-socio-cpfFiador').val(result[2]);
						$('#fiadorController-socio-rgFiador').val(result[3]);
						$('#fiadorController-socio-dataNascimentoFiadorCpf').val(result[4]);
						$('#fiadorController-socio-orgaoEmissorFiadorCpf').val(result[5]);
						$('#fiadorController-socio-selectUfOrgaoEmiCpf').val(result[6]);
						$('#fiadorController-socio-estadoCivilFiadorCpf').val(result[7]);
						$('#fiadorController-socio-selectSexoFiador').val(result[8]);
						$('#fiadorController-socio-nacionalidadeFiadorCpf').val(result[9]);
						$('#fiadorController-socio-naturalFiadorCpf').val(result[10]);
						
						if (result[7] == "CASADO"){
							
							_this.opcaoCivilPf(result[7],'fiadorController-socio-');
							
							$('#fiadorController-socio-nomeConjugeCpf').val(result[11]);
							$('#fiadorController-socio-emailConjugeCpf').val(result[12]);
							$('#fiadorController-socio-cpfConjuge').val(result[13]);
							$('#fiadorController-socio-rgConjuge').val(result[14]);
							$('#fiadorController-socio-dataNascimentoConjugeCpf').val(result[15]);
							$('#fiadorController-socio-orgaoEmissorConjugeCpf').val(result[16]);
							$('#fiadorController-socio-selectUfOrgaoEmiConjugeCpf').val(result[17]);
							$('#fiadorController-socio-selectSexoConjuge').val(result[18]);
							$('#fiadorController-socio-nacionalidadeConjugeCpf').val(result[19]);
							$('#fiadorController-socio-naturalConjugeCpf').val(result[20]);
							
							if (result[21] == "true"){
								$('#fiadorController-socio-checkboxSocioPrincipal').check();
							} else {
								$('#fiadorController-socio-checkboxSocioPrincipal').uncheck();
							}
						} else {
							
							if (result[11] == "true"){
								$('#fiadorController-socio-checkboxSocioPrincipal').check();
							} else {
								$('#fiadorController-socio-checkboxSocioPrincipal').uncheck();
							}
						}
						
						
						
						$("#fiadorController-idSocioEdicao").val(referencia);
					}
					
					$('#fiadorController-socio-cpfFiador').attr("disabled", true);
					$('#fiadorController-socio-cpfConjuge').attr("disabled", true);
					
					$("#fiadorController-btnAddEditarSocio").text("Editar");
				}
			);
		},
		
		removerSocio:function (referencia){
			var _this = this;
			$("#fiadorController-dialog-excluir-socio").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath + '/cadastro/fiador/removerSocio', "referencia=" + referencia, 
							function(result) {
								$(".sociosGrid").flexAddData({
									page: 1, total: 1, rows: result.rows
								});
								
								$("#fiadorController-btnAddEditarSocio").text("Incluir Novo");
								_this.limparDadosCadastraisCPFSocio();
							},
							null,
							true
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$("#fiadorController-dialog-excluir-socio").show();
		},
		
		carregarGarantias:function (){
			$.postJSON(contextPath + '/cadastro/fiador/obterGarantiasFiador', null, 
				function(result) {
					$(".fiadorController-imoveisGrid").flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});	
					
					$("#fiadorController-botaoAddEditarGarantia").text("Incluir Novo");
					
					$("#fiadorController-garantia-valorGarantia").val("");
					$("#fiadorController-garantia-descricaoGarantia").val("");
				},
				null,
				true
			);
		},
		
		processarResultadoGarantias:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result.rows;
			}
			
			var i;
			
			for (i = 0 ; i < data.rows.length; i++) {
				
				var lastIndex = data.rows[i].cell.length;
				
				data.rows[i].cell[lastIndex] = fiadorController.getActionsGarantia(data.rows[i].id);
			}

			$('.fiadorController-imoveisGrid').show();
			
			if (data.result){
				return data.result;
			}
			return data;
		},
		
		getActionsGarantia:function (idGarantia) {

			return '<a href="javascript:;" onclick="fiadorController.editarGarantia(' + idGarantia + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Garantia">' +
					'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/></a>' +
					'<a href="javascript:;" onclick="fiadorController.removerGarantia(' + idGarantia + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Garantia">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/></a>';
		},
		
		adicionarEditarGarantia:function (){
			var data = "garantia.valor=" + $("#fiadorController-garantia-valorGarantia").floatValue() + "&" +
	        		   "garantia.descricao=" + $("#fiadorController-garantia-descricaoGarantia").val() + "&" +
	        		   "referencia=" + $("#fiadorController-garantia-referenciaGarantia").val();
			
			$.postJSON(contextPath + '/cadastro/fiador/adicionarGarantia', data, 
				function(result) {
					$('.fiadorController-imoveisGrid').flexAddData({
						page: 1, total: 1, rows: result.rows
					});	
					
					$("#fiadorController-botaoAddEditarGarantia").text("Incluir Novo");
					
					$("#fiadorController-garantia-valorGarantia").val("");
					$("#fiadorController-garantia-descricaoGarantia").val("");
					$("#fiadorController-referenciaGarantia").val("");
				},
				null,
				true
			);
		},
		
		editarGarantia:function (referencia){
			$.postJSON(contextPath + '/cadastro/fiador/editarGarantia', "referencia=" + referencia, 
				function(result) {
					
					$("#fiadorController-botaoAddEditarGarantia").text("Editar");
					
					$("#fiadorController-garantia-valorGarantia").val(result.valor);
					$("#fiadorController-garantia-descricaoGarantia").val(result.descricao);
					$("#fiadorController-referenciaGarantia").val(referencia);
				},
				null,
				true
			);
		}, 
		
		removerGarantia:function (referencia){
			$("#fiadorController-dialog-excluir-garantia").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath + '/cadastro/fiador/excluirGarantia', "referencia=" + referencia, 
							function(result) {
								
								$(".fiadorController-imoveisGrid").flexAddData({
									page: 1, total: 1, rows: result.rows
								});
								
								$("#fiadorController-botaoAddEditarGarantia").text("Incluir Novo");
								
								$("#fiadorController-garantia-valorGarantia").val("");
								$("#fiadorController-garantia-descricaoGarantia").val("");
								$("#fiadorController-referenciaGarantia").val("");
							},
							null,
							true
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$("#fiadorController-dialog-excluir-garantia").show();
		},
		
		limparCamposGarantias:function (){
			$("#fiadorController-garantia-valorGarantia").val("");
			$("#fiadorController-garantia-descricaoGarantia").val("");
		},
		
		processarResultadoCotasAssociadas:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result.rows;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;

				data.rows[i].cell[lastIndex] = fiadorController.getActionCotaCadastrada(data.rows[i].id);
			}

			$('.fiadorController-cotasAssociadasGrid').show();
			
			if (data.result){
				return data.result;
			}
			return data;
		},
		
		getActionCotaCadastrada:function (referencia){
			return '<a href="javascript:;" onclick="fiadorController.removerAssociacaoCota(' + referencia + ')" ' +
			' style="cursor:pointer;border:0px;margin:5px" title="Excluir AssociaÃ§Ã£o">' +
			'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
			'</a>';
		},
		
		carregarCotasAssociadas:function (){
			$.postJSON(contextPath+'/cadastro/fiador/obterAssociacoesCotaFiador', null, 
				function(result) {
					$(".fiadorController-cotasAssociadasGrid").flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});
					
					$("#fiadorController-cotasAssociadas-numeroCota").val("");
					$("#fiadorController-cotasAssociadas-nomeCota").val("");
				},
				null,
				true
			);
		},
		
		adicionarAssociacaoCota:	function (){
			var data = "numeroCota=" + $("#fiadorController-cotasAssociadas-numeroCota").val() + "&nomeCota=" + $("#fiadorController-cotasAssociadas-nomeCota").val();
			
			$.postJSON(contextPath + '/cadastro/fiador/adicionarAssociacaoCota', data, 
				function(result) {
					$(".fiadorController-cotasAssociadasGrid").flexAddData({
						page: 1, total: 1, rows: result.rows
					});
					
					$("#fiadorController-cotasAssociadas-numeroCota").val("");
					$("#fiadorController-cotasAssociadas-nomeCota").val("");
				},
				null,
				true
			);
		},
		
		removerAssociacaoCota:function (referencia){
			
			$(".fiadorController-cotasAssociadas-dialog-excluir").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath +'/cadastro/fiador/removerAssociacaoCota', "referencia=" + referencia, 
							function(result) {
								$(".fiadorController-cotasAssociadasGrid").flexAddData({
									page: 1, total: 1, rows: result.rows
								});
								
								$("#fiadorController-cotasAssociadas-numeroCota").val("");
								$("#fiadorController-cotasAssociadas-nomeCota").val("");
							},
							null,
							true
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$(".fiadorController-cotasAssociadas-dialog-excluir").show();
		},
		
		limparCamposCotasAssociadas:function (){
			$("#fiadorController-cotasAssociadas-numeroCota").val("");
			$("#fiadorController-cotasAssociadas-nomeCota").val("");
		},
		
		buscarNomeCota:function (){
			
			var numeroCota = $("#fiadorController-cotasAssociadas-numeroCota").val();
			
			if (numeroCota.length > 0){
			
				$.postJSON(contextPath + '/cadastro/fiador/pesquisarNomeCotaPorNumeroCota', "numeroCota=" + numeroCota, 
					function(result) {
						if (result != ""){
							
							$("#fiadorController-cotasAssociadas-nomeCota").val(result);
							$("#fiadorController-cotasAssociadas-adicionarCotaAssociacao").attr("href", "javascript:fiadorController.adicionarAssociacaoCota();");
						} else {
							
							$("#fiadorController-cotasAssociadas-nomeCota").val("");
							$("#fiadorController-cotasAssociadas-adicionarCotaAssociacao").removeAttr("href");
						}
					},
					null,
					true
				);
			} else {
				$("#fiadorController-cotasAssociadas-nomeCota").val("");
			}
		}	
};