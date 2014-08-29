var dataHolder = {
		
	hold : function(actionKey, dataKey, fieldKey, fieldValue) {
		
		var data = [
			{
				name: 'actionKey', value: actionKey
			},
			{
				name: 'dataKey', value: dataKey
			},
			{
				name: 'fieldKey', value: fieldKey
			},
			{
				name: 'fieldValue', value: fieldValue
			}
		];
		
		$.postJSON(
			contextPath + '/dataholder/hold',
			data
		);
	},
	
	clearAction : function(actionKey, callback) {
		
		var data = [
			{
				name: 'actionKey', value: actionKey
			}
		];
		
		$.postJSON(
			contextPath + '/dataholder/clearAction', 
			data,
			function(){
				if (callback){
					callback();
				}
			}
		);
	},
	
	clearData : function(actionKey, dataKey) {
		
		var data = [
			{
				name: 'actionKey', value: actionKey
			},
			{
				name: 'dataKey', value: dataKey
			}
		];
		
		$.postJSON(
			contextPath + '/dataholder/clearData', 
			data
		);
	},
	
	clearAll : function() {

		$.postJSON(
			contextPath + '/dataholder/clearAll'
		);
	}
	
}

//@ sourceURL=data.holder.js