var usuarioController = $.extend(true, {
	
	data: {
		
		username: '',
		senha: '',
		
		set: function(username, senha) {
			this.username = username;
			this.senha = senha;
		},

		toJSON: function() {
			return {
				username: this.username,
				senha: this.senha
			};
		},

		reset: function() {
			this.username = '';
			this.senha = '';
		}		
	},
	
	supervisor: {

		verificarRoleSupervisao: function(params) {
			
			this.setOptionalMessages(params.optionalDialogMessage);
			
			this.validarUsuarioSupervisor(params.callbacks);
		},
		
	    validarUsuarioSupervisor: function(callbacks) {

	    	var _this = this;
	    	
	    	$.postJSON(
				contextPath + '/administracao/usuario/validarUsuarioSupervisor',
				usuarioController.data.toJSON(), function (result) {

					if (callbacks.usuarioSupervisorCallback) {
						callbacks.usuarioSupervisorCallback(result);
	            	}
					usuarioController.data.reset();
	            
				}, function(result) {
					_this.popupConfirmaSenha(callbacks.usuarioSupervisorCallback, callbacks.usuarioNaoSupervisorCallback);
					usuarioController.data.reset();
	            }
			);    	
	    },
	    
	    validarUsuarioSupervisorSemConfirmacaoDeSenha: function(callbacks) {

	    	var _this = this;
	    	
	    	$.postJSON(
				contextPath + '/administracao/usuario/validarUsuarioSupervisor',
				usuarioController.data.toJSON(), function (result) {

					if (callbacks.callbacks.usuarioSupervisorCallback) {
						callbacks.callbacks.usuarioSupervisorCallback();
	            	}
					usuarioController.data.reset();
	            
				}, function(result) {
					if (callbacks.callbacks.usuarioNaoSupervisorCallback) {
						callbacks.callbacks.usuarioNaoSupervisorCallback();
	            	}
					usuarioController.data.reset();
	            }
			);    	
	    },
	
	    popupConfirmaSenha : function(usuarioSupervisorCallback, usuarioNaoSupervisorCallback) {
	    	var _this = this;
	    	var validarClose = false;
	    	
	    	var $dialog = $('#dialog-confirmacao-senha');
	        $dialog.dialog({
				resizable: false,
				height:'auto',
				width:400,
				modal: true,
				buttons: {
					"Confirmar": function() {

						usuarioController.data.set(
							$.trim($dialog.find('#usuario-confirmar').val()), 
							$.trim($dialog.find('#senha-confirmar').val())
						);

						_this.verificarSenhaUsuarioSupervisor({
							usuarioSupervisorCallback: function(result) {
								usuarioSupervisorCallback(result);
								validarClose = true;
								$dialog.dialog("close");
							},
							
							usuarioNaoSupervisorCallback: function(result){
								if(usuarioNaoSupervisorCallback){
									usuarioNaoSupervisorCallback(result);
									exibirMensagemDialog('WARNING', ['Usuário não é supervisor.'], '');
									$dialog.dialog("close");
								}else{
									exibirMensagemDialog('WARNING', ['Usuário não é supervisor.'], '');
								}
							}
						});
	                },
	                "Cancelar": function() {
	                	if(usuarioNaoSupervisorCallback){
							usuarioNaoSupervisorCallback();
							$dialog.dialog("close");
						}else{
							$dialog.dialog("close");
						}
	                }
				},
				form: $("#dialog-autenticar-supervisor").parents("form"),
				close: function() {
					_this.cleanDialogData();
				},
				
				beforeClose: function(){
					if(!validarClose){
						if(usuarioNaoSupervisorCallback){
							usuarioNaoSupervisorCallback();
							validarClose = true;
							$dialog.dialog("close");
						}else{
							$dialog.dialog("close");
						}
					}
				}
			});
	    },
	    
	    verificarSenhaUsuarioSupervisor: function(callbacks) {

	    	var _this = this;
	    	
	    	$.postJSON(contextPath + '/administracao/usuario/validarUsuarioSupervisor',
				usuarioController.data.toJSON(), function (result) {

					if (callbacks.usuarioSupervisorCallback) {
						callbacks.usuarioSupervisorCallback(result);
	            	}
					usuarioController.data.reset();
	            
				}, function(result) {
					if (callbacks.usuarioNaoSupervisorCallback) {
						callbacks.usuarioNaoSupervisorCallback(result);
	            	}
					usuarioController.data.reset();
	            }
			);    	
	    },
	    
	    
	    cleanDialogData: function() {
	    	
	    	usuarioController.data.reset();
	    	
	    	this.setErrorMessages('');
	    	
	    	var $dialog = $('#dialog-confirmacao-senha');
	    	
	    	$dialog.find('input').val('');
	    },
	    
	    setOptionalMessages: function(optionalDialogMessage) {
	    	this.setMessage('optional-message', optionalDialogMessage);
	    },
	    
	    setErrorMessages: function(erroMessage) {
	    	this.setMessage('msg-usuario-invalido', erroMessage);
	    },
	    
	    setMessage: function(spanClass, message) {
	    	var $dialog = $('#dialog-confirmacao-senha');
	    	
	    	message = $.trim(message);
	    	
	    	$dialog.find("." + spanClass).html(message);
	    }
	}

}, BaseController);

//@ sourceURL=usuario.js
