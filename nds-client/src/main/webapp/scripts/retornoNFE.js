function RetornoNFEController(){
	
	this.bindEvents();
	
	this.sumarizacaoRetornoNFE = {
		numeroNotasAprovadas:null,
		numeroNotasRejeitadas:null,
		numeroTotalArquivos:null
	};
	
};

RetornoNFEController.prototype.path = contextPath +"/nfe/retornoNFe/";

RetornoNFEController.prototype.pesquisarArquivos = function() {
	
	this.limparTabela();
	
	var dataReferencia = $("#retornoNFEDataReferencia", this.workspace).val();
	
	if(!dataReferencia) {
		exibirMensagem("WARNING", ["O campo [Date de Referência] é obrigatório"], "");
	} else {
	
		var _this = this;
	
		$.postJSON(this.path + 'pesquisarArquivos.json', {"dataReferencia":dataReferencia}, function(data) {

			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;
			
			if (tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
				_this.limparTabela();
			} else {
				_this.sumarizacaoRetornoNFE = data.sumarizacao;
				_this.dataBind();
			}

		});
	}
};

RetornoNFEController.prototype.confirmar = function() {
	
	var _this = this;
	
	$.postJSON(this.path + 'confirmar.json', null, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens, "");
		} 

		_this.limparTabela();
		
	});
};

RetornoNFEController.prototype.bindEvents = function() {
	
	var _this = this;
	
	$("#retornoNFEPesquisar", this.workspace).click(function() {
		_this.pesquisarArquivos();
	});
	
	$("#retornoNFEConfirmar", this.workspace).click(function() {
		_this.confirmar();
	});
};

RetornoNFEController.prototype.dataBind = function() {
	$("#numeroArquivos", this.workspace).html(this.sumarizacaoRetornoNFE.numeroTotalArquivos);
	$("#notasAprovadas", this.workspace).html(this.sumarizacaoRetornoNFE.numeroNotasAprovadas);
	$("#notasRejeitadas", this.workspace).html(this.sumarizacaoRetornoNFE.numeroNotasRejeitadas);
	
};

RetornoNFEController.prototype.dataUnBind = function() {
	this.sumarizacaoRetornoNFE.numeroTotalArquivos = $("#numeroArquivos", this.workspace).html();
	this.sumarizacaoRetornoNFE.numeroNotasAprovadas = $("#notasAprovadas", this.workspace).html();
	this.sumarizacaoRetornoNFE.numeroNotasRejeitadas = $("#notasRejeitadas", this.workspace).html();
};

RetornoNFEController.prototype.limparTabela = function() {
	
	$("#numeroArquivos", this.workspace).html(0);
	$("#notasAprovadas", this.workspace).html(0);
	$("#notasRejeitadas", this.workspace).html(0);
	this.dataUnBind();
};







