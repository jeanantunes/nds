

function NotaPromissoria(idCota){	
	this.idCota = idCota;
	
	this.path = contextPath + "/cadastro/garantia/";
	
	this.notaPromissoria = {id:null,vencimento:null,valor:null,valorExtenso:null};
	this.salva = function(){
		this.dataUnBind();
		var postData = this.processPost('notaPromissoria', this.notaPromissoria);		
		postData['idCota'] = this.idCota;
		
		$.postJSON(this.path + 'salvaNotaPromissoria.json', postData, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if(tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}

		}, null, true);
	};	
	
	
	this.processPost = function(objectName, object){
		var obj = {};
		for(var propriedade in object) {
			obj[objectName + '.' + propriedade] = object[propriedade];
		}
		return obj;
	};
	
	this.get = function(){
		
		var _this = this;
		$.getJSON(this.path + 'getByCota.json', {'idCota':this.idCota}, function(data) {
			
			console.log(data);
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if(tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}else{
				_this.notaPromissoria = data.cotaGarantia.notaPromissoria;
				_this.dataBind();
			}
			

		});
	};
	
	this.toggle = function(){
		$('#cotaGarantiaNotaPromissoriaPanel').toggle();
	};
	
	this.dataBind =  function(){
		$("#cotaGarantiaNotaPromissoriaId").val(this.notaPromissoria.id);
		$("#cotaGarantiaNotaPromissoriaVencimento").val(this.notaPromissoria.vencimento.$);
		$("#cotaGarantiaNotaPromissoriaValor").val(this.notaPromissoria.valor);
		$("#cotaGarantiaNotaPromissoriavalorExtenso").val(this.notaPromissoria.valorExtenso);
	};
	
	this.dataUnBind = function(){		
		this.notaPromissoria.vencimento = $("#cotaGarantiaNotaPromissoriaVencimento").val();
		this.notaPromissoria.valor = $("#cotaGarantiaNotaPromissoriaValor").val();
		this.notaPromissoria.valorExtenso = $("#cotaGarantiaNotaPromissoriavalorExtenso").val();
	};
	
}