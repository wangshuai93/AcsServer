//var ip =  "http://localhost:8080/AcsServer";
//var ip =  "http://172.16.250.27:8080/AcsServer";
//var ip = "http://" + window.location.host + "/AcsServer";
var ip = "http://" + window.location.host;

function getCPEBeanList(succeccCallback) {
	$.ajax({
		type: "get",
    	url: ip + "/rest/device/getCPEBeanList",
    	dataType: "json",
    	headers: {
    		"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
        },     
    	contentType: "application/json; charset=utf-8",
    	success:function(data){
    		succeccCallback(data);
    	},
    	error:function(error){
        	alert("调用出错" + error.responseText);
	    }
	}); 	
}

function updateCPEPosition(device_id, x, y, succeccCallback) {
	$.ajax({
		type: "get",
    	url: ip + "/rest/device/updateCPEPosition",
    	dataType: "text",
    	data: {
    		device_id: device_id,
			x: x,
			y: y
		},
    	headers: {
    		"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
        },     
    	contentType: "application/json; charset=utf-8",
    	success:function(data){
    		succeccCallback(data);
    	},
    	error:function(error){
        	alert("调用出错" + error.responseText);
	    }
	}); 	
}


function getPingTaskResultList(device_id, successCallback){
	$.ajax({
		type: "get",
		url: ip + "/rest/device/getPingTaskResultList",
		dataType: "json",
		data: {
			device_id: device_id
		},
		headers: {
			"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
		},
		contentType: "application/json; charset=utf-8",
		success: function(dataList){
			successCallback(dataList);
		},
		error: function(error){
			alert("调用出错" + error.responseText);
		}
	});
}

function getTaskResultList(m_serialNumber, taskId, successCallback){
	$.ajax({
		type: "get",
		url: ip + "/rest/device/getTaskResultList",
		dataType: "json",
		data: {
//			device_id: device_id
			m_serialNumber: m_serialNumber,
			taskId: taskId
		},
		headers: {
			"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
		},
		contentType: "application/json; charset=utf-8",
		success: function(dataList){
			successCallback(dataList);
		},
		error: function(error){
			alert("调用出错" + error.responseText);
		}
	});
}

function getTaskResult(m_countResultId, taskId, callback){
	$.ajax({
		type: "get",
		url: ip + "/rest/device/getResult",
		dataType: "json",
		data: {
			m_countResultId: m_countResultId,
			taskId: taskId
		},
		headers: {
			"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
		},
		contentType: "application/json; charset=utf-8",
		success: function(dataList){
			callback(dataList);
		},
		error: function(error){
			alert("调用出错" + error.responseText);
		}
	});
}

function getBroadcastTaskResultList(device_id, successCallback){
	$.ajax({
		type: "get",
		url: ip + "/rest/device/getBroadcastTaskResultList",
		dataType: "json",
		data: {
			device_id: device_id
		},
		headers: {
			"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
		},
		contentType: "application/json; charset=utf-8",
		success: function(dataList){
			successCallback(dataList);
		},
		error: function(error){
			alert("调用出错" + error.responseText);
		}
	});
}

function getHttpTaskResultList(device_id, successCallback){
	$.ajax({
		type: "get",
		url: ip + "/rest/device/getHttpTaskResultList",
		dataType: "json",
		data: {
			device_id: device_id
		},
		headers: {
			"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
		},
		contentType: "application/json; charset=utf-8",
		success: function(dataList){
			successCallback(dataList);
		},
		error: function(error){
			alert("调用出错" + error.responseText);
		}
	});
}

function getTcpUdpTaskResultList(device_id, successCallback){
	$.ajax({
		type: "get",
		url: ip + "/rest/device/getTcpUdpTaskResultList",
		dataType: "json",
		data: {
			device_id: device_id
		},
		headers: {
			"Access-Control-Allow-Origin":"*",
    		"Access-Control-Allow-Headers":"x-requested-with,content-type",
    		"Authorization": "Basic " 
		},
		contentType: "application/json; charset=utf-8",
		success: function(dataList){
			successCallback(dataList);
		},
		error: function(error){
			alert("调用出错" + error.responseText);
		}
	});
}
