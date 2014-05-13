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
	
	    popupConfirmaSenha : function(usuarioSupervisorCallback, usuarioNaoSupervisorCallback) {
	    	var _this = this;
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

						_this.validarUsuarioSupervisor({
							usuarioSupervisorCallback: function(result) {
								usuarioSupervisorCallback(result);
								$dialog.dialog("close");
							},
							
							usuarioNaoSupervisorCallback: function(result){
								if(usuarioNaoSupervisorCallback){
									usuarioNaoSupervisorCallback(result);
									$dialog.dialog("close");
								}else{
									var msg = result.mensagens && result.mensagens.listaMensagens[0] ? result.mensagens.listaMensagens[0] : 'Usuário não é supervisor.';
									_this.setErrorMessages(msg);
								}
							}
						});
	                },
	                "Cancelar": function() {
	                	if(usuarioNaoSupervisorCallback){
							usuarioNaoSupervisorCallback();
							$dialog.dialog("close");
						}else{
							$(this).dialog("close");
						}
	                }
				},
				form: $("#dialog-autenticar-supervisor").parents("form"),
				close: function() {
					_this.cleanDialogData();
				}
			});
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
