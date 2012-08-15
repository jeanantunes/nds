function RetornoNFE(){
	
	this.bindEvents();
	
	this.sumarizacaoRetornoNFE = {
		numeroNotasAprovadas:null,
		numeroNotasRejeitadas:null,
		numeroTotalArquivos:null
	};
	
};

RetornoNFE.prototype.path = contextPath +"/nfe/retornoNFe/";

RetornoNFE.prototype.pesquisarArquivos = function() {
	
	this.limparTabela();
	
	var dataReferencia = $("#retornoNFEDataReferencia").val();
	
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

RetornoNFE.prototype.confirmar = function() {
	
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

RetornoNFE.prototype.bindEvents = function() {
	
	var _this = this;
	
	$("#retornoNFEPesquisar").click(function() {
		_this.pesquisarArquivos();
	});
	
	$("#retornoNFEConfirmar").click(function() {
		_this.confirmar();
	});
};

RetornoNFE.prototype.dataBind = function() {
	$("#numeroArquivos").html(this.sumarizacaoRetornoNFE.numeroTotalArquivos);
	$("#notasAprovadas").html(this.sumarizacaoRetornoNFE.numeroNotasAprovadas);
	$("#notasRejeitadas").html(this.sumarizacaoRetornoNFE.numeroNotasRejeitadas);
	
};

RetornoNFE.prototype.dataUnBind = function() {
	this.sumarizacaoRetornoNFE.numeroTotalArquivos = $("#numeroArquivos").html();
	this.sumarizacaoRetornoNFE.numeroNotasAprovadas = $("#notasAprovadas").html();
	this.sumarizacaoRetornoNFE.numeroNotasRejeitadas = $("#notasRejeitadas").html();
};

RetornoNFE.prototype.limparTabela = function() {
	
	$("#numeroArquivos").html(0);
	$("#notasAprovadas").html(0);
	$("#notasRejeitadas").html(0);
	this.dataUnBind();
};







