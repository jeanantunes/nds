function RetornoNFE(){
	
	this.retornoNFE = {};
	
};

RetornoNFE.prototype.path ="/nfe/retornoNFe";

RetornoNFE.prototype.pesquisarArquivos = function() {
	
	var dataReferencia = $("#retornoNFEDataReferencia").val();
	
	var _this = this;
	
	$.postJSON(this.path + 'pesquisarArquivos.json', {"dataReferencia":dataReferencia}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens, "");

		} else {
			_this.retornoNFE = data;
			_this.gerarTabela();
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

RetornoNFE.prototype.bindEvents = function() {};

RetornoNFE.prototype.dataBind = function() {};

RetornoNFE.prototype.dataUnBind = function() {};

RetornoNFE.prototype.gerarTabela = function() {
	
};

RetornoNFE.prototype.limparTabela = function() {};







