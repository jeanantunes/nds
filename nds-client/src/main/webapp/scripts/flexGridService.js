/**
 * @author Samuel Mendes
 */

function FlexGridService() {
	
	var flexGridService = this,
		loadingGrid;
	
	flexGridService.addDataToGrid = function addDataToGrid(row){
		loadingGrid = this;
		
		if (row) {
			loadingGrid.tableModel.rows.push(row);

			if (loadingGrid.tableModel.page == 0) {
				loadingGrid.tableModel.page = 1;
			}
			
			loadingGrid.tableModel.total = loadingGrid.tableModel.rows.length;
			
			$('.' + this.gridName).flexAddData(loadingGrid.tableModel);
			
			// limpando o cache caso não esteja configurado 
			if (!this.cached) {
				this.tableModel = null;
			}
		}
	},
	
	flexGridService.removeRowFromGrid = function removeDataFromGrid(rowId){
		
		$("." + this.gridName + " #row"+rowId).remove();
		
		this.tableModel.rows = $.grep(this.tableModel.rows, function(val, index) {
			return val.id != rowId;
		});
	},
	
	this.createInputOnPreProcess = function createInputOnPreProcess(result){
		var input,
			inputModel;
		
		if (loadingGrid) {
			inputModel = loadingGrid.inputModel;
		}
		
		if (result) {
		
			for ( var i in inputModel) {
				$.each(result.rows, function(index, row) {

					model = inputModel[i];
					
					input = "<"+model.element; //fileReference
					if (model.inputType) {
						input += " type='" + model.inputType + "'";
					}
					
					if (model.attributeName) {
						input += " name='" + model.attributeName + "'";
					}
					
					if(model.cellValue){
						input += " value='" + row.cell[model.cellValue] + "'";
					}
					
					if (model.width) {
						input += " width='" + model.width + "'";
						
					}
					if (model.event) {
						event = model.event;
						if (event.parameter == "rowId") {
							input += " " + event.type+"='" + event.functionName + "(" + row.id + ", event)'";
						}else{
							parameter = event.parameter.
							
							input += " " + event.type+"='" + event.functionName + "(" + row.cell[event.parameter] + ", event)'";
						}
					}
					
					if (model.style) {
						input += " style='"+ model.style + "'";
					}

					if (model.fileReference) {
						input += " src='" + model.fileReference + "'";
					}
					
					if(model.checked){
						input += " checked";
					}
					input += " />";
					
					row.cell[model.columnName] = input;
				});
			}
		};
		
	},
	
	/**
	* @author InfoA2 - Samuel Mendes
	* 
	* @method reload
	* 
	* @param {String} url para executar a requisição
	* <p> Ex.: url : contextPath + 'distribuicao/segmentoNaoRecebido/pesquisarSegmentos' </p>
	*  
	* @param {String} dataType : tipo da requisição  
	* <p> Ex.: dataType : 'json'</p>
	* 
	* @param {Array} params : parametros para enviar com a requisição 
	* <p> Ex.: params : '[{name : "numeroCota", value : "123"}]' </p>
	* 
	* @param {String} workspace : "workspace"
	* <p> Ex.: workspace : this.workspace;
	* 
	* @param {function} preProcess : função para executar antes da construção do grid 
	* <p> Ex.: preProcess : function(){ 
	* 				// código para construção de ELEMENTOS CHECKBOX ou IMG
	* 		   } 
	* </p>
	* 
	*/
	flexGridService.reload = function reload(options) {

		if (this.cached) {
			loadingGrid = this;
		}
		
		// Inicializa o objeto caso não exista
		options = options || {};
		
		// obtendo a url padrão caso o usuário não tenha informado
		options.url = this.Url.urlDefault || options.url;
		
		// Caso o grid não possua um preProcess, inicializa um vazio
//		this.inputModel = this.inputModel || [];
		
		options.preProcess = options.preProcess || this.preProcess; 
		
		// GUARDA O ULTIMO PARÂMETRO UTILIZADO
		this.lastParams = options.params;
		
		if (options !== undefined) {
			if (options.workspace === undefined) {
				$("." + this.gridName).flexOptions(options);
				$("." + this.gridName).flexReload();
			}else {
				$("." + this.gridName, options.workspace).flexOptions(options);
				$("." + this.gridName, options.workspace).flexReload();
			}
		}
	},
	
	flexGridService.defaultPreProcess = function defaultGridPreProcess(result){
		if (result.mensagens) {

			exibirMensagem(result.mensagens.tipoMensagem,
					result.mensagens.listaMensagens);

			//$(".grids").hide();

			return result;
		}
		
		flexGridService.createInputOnPreProcess(result);

		if (result.rows && loadingGrid) {
			loadingGrid.tableModel = result;
		}
		
		$(".grids").show();

		return result;
	},
	
	this.GridFactory = {
			grids : [],
			createGrid : function(options){
				
				options.gridConfiguration.preProcess = options.preProcess || flexGridService.defaultPreProcess;
				
				grid = {
						gridName : options.gridName,
						cached : options.cached,
						Url : {
							urlDefault : options.url,
						},
						addData : flexGridService.addDataToGrid,
						removeRow : flexGridService.removeRowFromGrid,
						reload : flexGridService.reload,
						tableModel : {
							rows : [],
							page : 0,
							total : 0
						},
						preProcess : options.preProcess || flexGridService.defaultPreProcess,
						inputModel : options.inputModel,
						init : $("."+options.gridName).flexigrid(options.gridConfiguration),
				};
				
				this.grids.push(grid);
				
				return grid; 
			},
	};
}

//@ sourceURL=flexGridService.js
