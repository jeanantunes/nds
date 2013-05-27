function GeracaoArquivos() {
	this.init();
}

GeracaoArquivos.prototype.path = contextPath + '/administracao/geracaoArquivos/';
GeracaoArquivos.prototype.init = function() {
	var _this = this;

	$("#datepickerDe")
			.datepicker(
					{
						showOn : "button",
						buttonImage : contextPath
								+ "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
						buttonImageOnly : true
					});


	$("#btnGerar", this.workspace).click(function() {
		_this.btnGerarOnClick();
	});

	$("#tipoArquivo", this.workspace).change(function() {
		_this.tipoArquivoGerarOnChange();
	});

};


GeracaoArquivos.prototype.getParams = function() {
	var params = {
		"dataLctoPrevisto" : $("#datepickerDe", this.workspace).val(),
		"operacao" : $("#tipoArquivo", this.workspace).val()
	};
	return params;
};

GeracaoArquivos.prototype.btnGerarOnClick = function() {

	var params = this.getParams();
	
	$.postJSON(this.path + 'gerar',
			params, 
			function(data) {
				$("#qtdArquivosGerados", this.workspace).html(data.int);
			},
			function(result) {
				$("#resultado").show();
				$("#qtdArquivosGerados").text(result);
			}
	);
};

GeracaoArquivos.prototype.tipoArquivoGerarOnChange = function() {

	var reparte = $("#dtLancto");
	var encalhe = $("#dtRecolhimento");
	var tipoArquivo = $("#tipoArquivo").val();

    switch (tipoArquivo) {  
            case 'REPARTE':    
        		reparte.show();
                encalhe.hide();
            break;  
            case 'PICKING':
        		reparte.show();
                encalhe.hide();
            break;  
            default:
                reparte.hide();
            	encalhe.show();
            break;  
    }

};
//@ sourceURL=geracaoArquivo.js
