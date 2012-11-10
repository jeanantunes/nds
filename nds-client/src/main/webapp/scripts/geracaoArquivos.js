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

};


GeracaoArquivos.prototype.getParams = function() {
	var params = {
		"dataLctoPrevisto" : $("#datepickerDe", this.workspace).val(),
		"operacao" : $("#tipoArquivo", this.workspace).val()
	};
	return params;
};

GeracaoArquivos.prototype.btnGerarOnClick = function() {

	var _this = this;
	var params = this.getParams();
	
	$.postJSON(this.path + 'gerar',
			params, 
			function(result) {
				$("#qtdArquivosGerados", this.workspace).html(result);
			},
			function(result) {
				
			}
	);
};
