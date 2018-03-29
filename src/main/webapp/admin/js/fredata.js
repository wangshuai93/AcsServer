//任务选择
var taskId = $("#all_task").val();

if(taskId == 13){
	$("#fre_data").show();

}else{
	$("#fre_data").hide();
}

function getFreData(){
	//任务选择
	var taskId = $("#all_task").val();

	if(taskId == 13){
		$("#fre_data").show();

	}else{
		$("#fre_data").hide();
	}
}