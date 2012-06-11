function RetornoNFE(){
	
	this.sumarizacaoRetornoNFE = {
		numeroNotasAprovadas:null,
		numeroNotasRejeitadas:null,
		numeroTotalArquivos:null
	};
	
};

RetornoNFE.prototype.path = contextPath +"/nfe/retornoNFe/";

RetornoNFE.prototype.pesquisarArquivos = function() {
	
	var dataReferencia = $("#retornoNFEDataReferencia").val();
	
	var _this = this;
	
	$.postJSON(this.path + 'pesquisarArquivos.json', {"dataReferencia":dataReferencia}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens, "");

		} else {
			_this.sumarizacaoRetornoNFE = data.sumarizacao;
			_this.dataBind();
		}

	}, null, true);
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
		
	}, null, true);
};

RetornoNFE.prototype.bindEvents = function() {
	
	var _this = this;
	
	$("#retornoNFEPesquisar").click(function() {
		_this.pesquisarArquivos();
	});
};

RetornoNFE.prototype.dataBind = function() {
	
	$("#numeroArquivos").val(this.sumarizacaoRetornoNFE.numeroTotalArquivos);
	$("#notasAprovadas").val(this.sumarizacaoRetornoNFE.numeroNotasAprovadas);
	$("#notasRejeitadas").val(this.sumarizacaoRetornoNFE.numeroNotasRejeitadas);
	
};

RetornoNFE.prototype.dataUnBind = function() {};

RetornoNFE.prototype.limparTabela = function() {};







