/* JS para a tela de Parâmetros do Sistema. */

function ParametroSistema() {
	
	this.bindEvents();
	this.initUploadForm();

}

ParametroSistema.prototype.path = contextPath + "/administracao/parametrosSistema/";

ParametroSistema.prototype.initUploadForm = function() {
	var _this = this;
	
	$('#formUpload').bind('submit', function(e) {
		
		e.preventDefault(); // <-- important
		$(this).ajaxSubmit({
			beforeSubmit: function(arr, formData, options) {
				
				// Validar seleção de imagem:
				/*
				if (!$('#imgLogoSistema').val()) { 
					exibirMensagem('WARNING', ['Escolha um arquivo para ser enviado!'], "");
					return false; 
				}
				*/
				
				// Valida o formato do email:
				if (!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{1,3})+$/.test($('#email').val())) {
					exibirMensagem('WARNING', ['Por favor, preencha com um email válido!'], "");
					return false; 
				}
			},
			success: function(responseText, statusText, xhr, $form)  { 
				var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
				var tipoMensagem = mensagens.tipoMensagem;
				var listaMensagens = mensagens.listaMensagens;
				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens, "");
				}    	    	    
				
				// carregar imagem:
				_this.loadImage();
				
			} ,
			url: _this.path + 'salvar',
			type: 'POST',
			dataType: 'json'
		});
	});
};


// Traz a imagem do logotipo para a tela:
ParametroSistema.prototype.loadImage = function(){
	var imgPath = this.path + 'getImageLogotipo';
	var img = null;
	img = $("<img />").attr('src', imgPath).attr('width', '110').attr('height', '70').attr('alt', 'Logo Sistema')
   .load(function() {
      if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
          $("#div_imagem_logotipo").append(img);
      }
   });
};


ParametroSistema.prototype.bindEvents = function() {
	$("#cnpj").mask("99.999.999/9999-99");
	$("#dtOperacaoCorrente").mask("99/99/9999");
};
