<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>

</head>
<body>
<%--<%@ include file="/WEB-INF/pages/common/navigation.jsp"%>--%>
<style>
    .selected{background: #35A7E7;
        color: white;}
</style>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    var  res = {areas:new Array()};
    function sitechange(){
        var siteid = $("#siteid option:selected")[0].value;
        $.ajax({
            url:"queryclassbysiteid",
            type:"post",
            data:{
                siteid:siteid,
            },
            dataType:"json",
            success:function(data){
                if(data!=null){
                    var classselect =  document.getElementById("classid");
                    classselect.innerHTML = "";
                    var classop = document.createElement("option");
                    classop.innerHTML = '所有班组';
                    classop.value ="";
                    classop.selected ="selected";
                    classselect.appendChild(classop);
                    for(var i=0;i<data.length;i++){
                        var classop = document.createElement("option");
                        classop.innerHTML = data[i].customid;
                        classop.value = data[i].id;
                        classselect.appendChild(classop);
                    }
                    return true;
                }else{
                    alert("该厂区暂时没有班组");
                    return false;
                }
            }
        });
        $("#directorid").empty();
    }
    function change(){
        var classid = $("#classid option:selected")[0].value;
        $.ajax({
            url:"querydirectorid",
            type:"post",
            data:{
                classid:classid,
            },
            dataType:"json",
            success:function(data){
                if(data>0){
                    directorname(data);
                    return true;
                }else{
                    alert("该区域暂时没有负责人");
                    return false;
                }
            }
        });
//        directorname(classid);
    }
    function directorname(id){
        $.ajax({
            url:"querydirectorname",
            type:"post",
            data:{
                uid:id,
            },
            dataType:"json",
            success:function(data){
                if(data!=null){
                    var parent =  document.getElementById("directorid");
                    parent.innerHTML = "";
                    var op = document.createElement("option");
                    op.innerHTML = data.realname;
                    op.value = data.id;
                    parent.appendChild(op);
                    return true;
                }else{
                    alert("出错了");
                    return false;
                }
            }
        });
    }
    window.onload=change;
    function first(){
        $("#first").css('display','block');
        $("#second").css('display','none');
        $("#third").css('display','none');
    }
    function second(){
        /*验证任务号*/
        var idtype=$("#idtype").val();
        if(idtype==""){
            alert("亲，任务号必须要填写的哟");
            return false;
        }
        if(idtype.length>6){
            alert("亲，你输入的任务号太长了哟");
            return false;
        }
        /*验证任务名*/
        var taskname=$("#taskname").val();
        if(taskname==""){
            alert("亲，任务名必须要填写的哟");
            return false;
        }
        if(taskname.length>10){
            alert("亲，你输入的任务名太长了哟");
            return false;
        }
        /*验证任务描述*/
        var taskcontent=$("#username").val();
        if(taskcontent==""){
            alert("亲，任务描述必须要填写的哟");
            return false;
        }
        if(taskcontent.length>15){
            alert("亲，你输入的任务描述太长了哟");
            return false;
        }
        /*验证创建人*/
        var createname=$("#pwd2").val();
        if(createname==""){
            alert("亲，创建人必须要填写的哟");
            return false;
        }
        if(createname.length>4){
            alert("亲，请输入正确的创建人格式哟");
            return false;
        }
        /*创建时间*/
        var createtime=$("#pwd3").val();
        if(createtime==""){
            alert("亲，创建时间必须的填写哟");
            return false;
        }
        /*验证审核人和审核时间*/
        var reviewuser=$("#pwd4").val();
        if(reviewuser==""){
            alert("亲，审核人必须要填写的哟");
            return false;
        }
        if(reviewuser.length>4){
            alert("亲，请输入正确的审核人格式哟");
            return false;
        }
        var reviewtime=$("#pwd7").val();
        if(reviewtime==""){
            alert("亲，审核时间必须的填写哟");
            return false;
        }
        /*验证批准人和批准时间*/
        var approveuser=$("#pwd5").val();
        if(approveuser==""){
            alert("亲，批准人必须要填写的哟");
            return false;
        }
        if(approveuser.length>4){
            alert("亲，请输入正确的批准人格式哟");
            return false;
        }
        var approvetime=$("#pwd6").val();
        if(approvetime==""){
            alert("亲，批准时间必须的填写哟");
            return false;
        }
        /*验证版本号*/
        var banben=$("#banben").val();
        var reg1 = /^[\d]+$/g;
        if(banben==""){
            alert("版本号不能为空");
            return false;
        }
        if (!reg1.test(banben)) {
            alert("版本号只能输入整数哟");
            return false;
        }
        /*验证最长执行时间*/
        var maxtime=$("#pwd8").val();
        var reg2 = /^[\d]+$/g;
        if(maxtime==""){
            alert("最长执行时间不能为空");
            return false;
        }
        if (!reg2.test(parseInt(maxtime))) {
            alert("最长执行时间只能输入整数哟");
            return false;
        }

        $("#first").css('display','none');
        $("#second").css('display','block');
        $("#third").css('display','none');
        return true;
    }
    function third(){
        var taskcontent=JSON.stringify(res);
        $("#taskcontent").val(taskcontent);
        var taskcon=$("#taskcontent").val();
        if(taskcon=='{"areas":[]}'){
            alert("任务内容不能为空");
            return false;
        }
        /*changeradio();*/
        $("#first").css('display','none');
        $("#second").css('display','none');
        $("#third").css('display','block');
    }
   /* function changeradio(){
        if(document.getElementById("radion1").checked){
            $("#name1").attr("disabled",true);
            $("#name3").attr("disabled",true);
            $("#name4").attr("disabled",true);

            $("#addtimee").attr("disabled",true);
            $("#deltime").attr("disabled",true);

            $("#name2").attr("disabled",false);
            $("#radion2").removeClass('checked');
            $("#radion1").addClass("checked",true);
        }else if(document.getElementById("radion2").checked){
            $("#name2").attr("disabled",true);
            $("#name1").attr("disabled",false);
            $("#name3").attr("disabled",false);
            $("#name4").attr("disabled",false);
            $("#addtimee").attr("disabled",false);
            $("#deltime").attr("disabled",false);
            $("#radion1").removeClass('checked');
            $("#radion2").addClass("checked",true);
        }
    }*/
</script>
<script type="text/javascript">
    var areasdata; //区域数据源
    var equipmentdata; //设备数据源
    var checkitemdata;

    var datarecorddata;

    var res = {areas:new Array()};
    var selectedareaindex;
    var selectedequipmentindex;
    var selecteditemindex;
    /*显示所有的区域*/
    function model(){
        $("#myModal").modal("show");
        $("#equipmentlist")[0].style.display="none";
        $("#checklist")[0].style.display="none";
        $("#dotime")[0].style.display="none";
        $("#arealist")[0].style.display="block";
        $("#loading")[0].style.display="none";
        $.ajax({
            url:"showarea",
            type:"post",
            dataType:"json",
            success:function(data) {
                areasdata = data;
                var first = $("#area tr:first");
                $("#area").empty();
                $("#area").append(first);
                for (var i = 0; i < data.length; i++) {/*name='data["+i+"].id'*/
                    $("#area").append("<tr><td><input type='checkbox' name='allarea'></td><td>" + data[i].id +
                            "</td><td>" + data[i].customid + "</td><td>"+data[i].desccontent+"</td></tr>");
                }
            }
        });
        $("#selectallarea").prop("checked",false);
    }
    /*根据区域编号显示所有的设备*/
    function showequip(){

        var $li = $("#areaul .selected");
        var areaid = $li.attr("areaid");
        if (areaid==undefined) {
            alert("请选择设备所属区域");
            return false;
        }else{
            $.ajax({
                url:"showequipment?areaid="+areaid,
                type:"post",
                dataType:"json",
                success:function(data) {
                    if(data==""){
                        alert("该区域暂时没有设备");
                        return false;
                    }else{
                        equipmentdata = data ;
                        var first = $("#equipment tr:first");
                        $("#equipment").empty();
                        $("#equipment").append(first);

                        $("#myModal").modal("show");
                        $("#equipmentlist")[0].style.display="block";
                        $("#arealist")[0].style.display="none";
                        $("#checklist")[0].style.display="none";
                        $("#dotime")[0].style.display="none";
                        $("#loading")[0].style.display="none";
                        for (var i = 0; i < data.length; i++) {
                            $("#equipment").append("<tr><td><input type='checkbox' name='allequip'></td><td>" +
                                    data[i].customid + "</td><td>" + data[i].name +
                                    "</td><td>"+data[i].desccontent+"</td><td style='display:none;'>"+data[i].id+"</td></tr>");
                        }
                    }
                }
            });
            $("#selectsllequip").prop("checked",false);
        }
    }
    /*根据设备编号显示所有的巡检项*/
    function showcheck(){

        var $li = $("#equipul .selected");
        var equipid = $li.attr("equipid");
        if (equipid==undefined) {
            alert("请选择巡检项所属设备");
            return false;
        }else{
            $("#myModal").modal("show");
            $("#equipmentlist")[0].style.display = "none";
            $("#arealist")[0].style.display = "none";
            $("#checklist")[0].style.display = "block";
            $("#dotime")[0].style.display="none";
            $("#loading")[0].style.display="none";
            $.ajax({
                url: "showcheck",
                type: "post",
                dataType: "json",
                success: function (data) {
                    checkitemdata = data;
                    var first = $("#check tr:first");
                    $("#check").empty();
                    $("#check").append(first);
                    var dty;
                    var html = "";
                    for (var i = 0; i < data.length; i++) {
                        dty=data[i].type;
                        html += "<tr><td><input type='checkbox' name='allcheckitem'></td>" +
                                "<td>" + data[i].customid + "</td><td>"+(data[i].type==1?'状态项':'记录项')+
                                "</td><td>" + data[i].itemname + "</td><td style='display: none;'>" + data[i].id + "</td></tr>";

                    }
                $("#check").append(html);
                }
            });
            $("#selectallcheck").prop("checked",false);
        }
    }
    /*将选择好的区域追加到指定位置*/
    function addarea(){
        var area= $("#area").find("input[type='checkbox']:checked").parent().parent();
        for(var i= $("#selectallarea")[0].checked?1:0;i<area.length;i++){
            for(var j =0 ; j < res.areas.length ; j++ ){
                if(area[i].cells[1].innerHTML==res.areas[j].area.id){
                    alert("区域"+area[i].cells[2].innerHTML+"已存在");
                    return;
                }
            }
            res.areas.push({area:areasdata[area[i].rowIndex-1], equipments:new Array()});
            $('#myModal').modal('hide');
        }
//        for(var i= $("#selectallarea")[0].checked?1:0;i<area.length;i++){
//            res.areas.push({area:areasdata[area[i].rowIndex-1], equipments:new Array()});
//            $('#myModal').modal('hide');
//        }
        refresharea();
    }

    /*填充区域*/
    function refresharea(selectIndex){
        var data = res.areas;
        var changearea=$("#areaul");
        changearea.empty();
        for(var i=0;i<data.length;i++){
            changearea.append("<li class='list-group-item' " + " rel="+i+"   " + "areaindex="+i+" areaid="+data[i].area.id+" onclick='selectarea(this)' id='area"+i+"'>"+data[i].area.customid+"</li>");
        }
        $('#myModal').modal('hide');
       /* var aa=$("#areaul li:eq(0)").attr("areaindex");
        if(aa==0){
            $("#areaul li:eq(0)").siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $("#areaul li:eq(0)").siblings('li').prop("selected", false);
            $("#areaul li:eq(0)").addClass('selected');
            $("#areaul li:eq(0)").prop("selected", true);
        }*/
       if(selectIndex==undefined){
           selectarea($("#areaul li:eq(0)"));
       }else{
           var aa=parseInt(selectIndex);
           selectarea($("#areaul li:eq('"+aa+"')"));
       }
    }

    /*将选择好的设备追加到指定位置*/
    function addequip(){
        var equipment= $("#equipment").find("input[type='checkbox']:checked").parent().parent();
        /* equipul.empty();*/
        for(var i=$("#selectsllequip")[0].checked?1:0;i<equipment.length;i++){
            for(var j =0 ; j < res.areas[selectedareaindex].equipments.length ; j++ ){
                if(equipment[i].cells[4].innerHTML==res.areas[selectedareaindex].equipments[j].equipment.id){
                    alert("设备"+equipment[i].cells[1].innerHTML+" "+equipment[i].cells[2].innerHTML+"已存在");
                    return;
                }
            }
            //添加设备
            res.areas[selectedareaindex].equipments.push({equipment:equipmentdata[equipment[i].rowIndex-1],checkItems:new Array()});
            $('#myModal').modal('hide');
        }
        refreshEq();
    }

    /*填充设备*/
    function refreshEq(index){
        var data = res.areas[selectedareaindex].equipments;
        var equipul=$("#equipul");
        equipul.empty();
        for(var i=0;i<data.length;i++){
            equipul.append("<li class='list-group-item' " + "equipindex="+i+" equipid="+data[i].equipment.id+" onclick='selectequipment(this)'>"+data[i].equipment.name+"</li>");
            $('#myModal').modal('hide');
        }
       /* var bb=$("#equipul li:eq(0)").attr("equipindex");
        if(bb==0){
            $("#equipul li:eq(0)").siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $("#equipul li:eq(0)").siblings('li').prop("selected", false);
            $("#equipul li:eq(0)").addClass('selected');
            $("#equipul li:eq(0)").prop("selected", true);
        }*/
        if(index==undefined){
            selectequipment($("#equipul li:eq(0)"));
        }else{
            selectequipment($("#equipul li:eq('"+index+"')"));
        }

    }

    /*将选择好的巡检项追加到指定位置*/
    function addcheck(){
        var check= $("#check").find("input[type='checkbox']:checked").parent().parent();
        for(var i=$("#selectallcheck")[0].checked?1:0;i<check.length;i++){
            for(var j =0 ; j < res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems.length ; j++ ){
                if(check[i].cells[4].innerHTML==res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems[j].item.id){
                    alert("巡检项"+check[i].cells[1].innerHTML+" "+check[i].cells[3].innerHTML+"已存在");
                    return;
                }
            }
            //添加巡检项
            var checkitem = checkitemdata[check[i].rowIndex-1];
            res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems.push({item:checkitem});
          /* console.log(res);*/
            $('#myModal').modal('hide');
        }
        refreshCheck();
    }
    /*将选择好的巡检项填充*/
    function refreshCheck(index){
        data = res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems;
        var checkul=$("#checkul");
        checkul.empty();
        for(var i=0;i<data.length;i++){
            checkul.append("<li class='list-group-item' "+" checkitemindex ="+ i +" recordid= "+data[i].item.recordid+" onclick='showdata(this)'>"+data[i].item.itemname+"</li>");
            $('#myModal').modal('hide');
        }
       /* var cc=$("#checkul li:eq(0)").attr("checkitemindex");
        if(cc==0){
            $("#checkul li:eq(0)").siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $("#checkul li:eq(0)").siblings('li').prop("selected", false);
            $("#checkul li:eq(0)").addClass('selected');
            $("#checkul li:eq(0)").prop("selected", true);
        }*/
       if(index==undefined){
           showdata($("#checkul li:eq(0)"));
       }else{
           showdata($("#checkul li:eq('"+index+"')"));
       }

    }
    /*控制选定area的li的样式*/
    function selectarea(obj){
        $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
        $(obj).siblings('li').prop("selected", false);
        $(obj).addClass('selected');
        $(obj).prop("selected", true);

        var $li = $("#areaul .selected");
        selectedareaindex =$li.attr("areaindex");
        //关联设备列表切换
        $("#checkul").empty();
        $("#datarecord").empty();
        refreshEq();
    }

    /*控制选定equipment的li的样式*/
    function selectequipment(obj){
        $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
        $(obj).siblings('li').prop("selected", false);
        $(obj).addClass('selected');
        $(obj).prop("selected", true);
        var $li = $("#equipul .selected");
        selectedequipmentindex =$li.attr("equipindex");
        //关联巡检项列表切换
        $("#datarecord").empty();
        refreshCheck();
    }

    /*对选中区域项进行删除*/
    function delarea(id){
        var $li = $(id).find("li:selected");
        if ($li.length <= 0) {
            alert("请选择要删除的选项");
            return false;
        }
        res.areas.splice(selectedareaindex,1);
        var equipul=$("#equipul");
        equipul.empty();
        var checkul=$("#checkul");
        checkul.empty();
        refresharea();
    }

    /*对选中设备项进行删除*/
    function delequip(id){
        var $li = $(id).find("li:selected");
        if ($li.length <= 0) {
            alert("请选择要删除的选项");
            return false;
        }
        res.areas[selectedareaindex].equipments.splice(selectedequipmentindex,1);
        refreshEq();
        var checkul=$("#checkul");
        checkul.empty();

    }

    /*对选中巡检想项进行删除*/
    function delitem(id){
        var $li = $(id).find("li:selected");
        if ($li.length <= 0) {
            alert("请选择要删除的选项");
            return false;
        }
        res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems.splice(selecteditemindex,1);
        refreshCheck();
    }

    function  showdata(obj) {
        $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
        $(obj).siblings('li').prop("selected", false);
        $(obj).addClass('selected');
        $(obj).prop("selected", true);

        var $li = $("#checkul .selected");
        var recordid = $li.attr("recordid");

        selecteditemindex = $li.attr("checkitemindex");
        if (recordid==undefined) {
            /*alert("请选择巡检项");*/
            return false;
        }else{
            var data = res.areas[selectedareaindex].equipments[selectedequipmentindex].
                    checkItems[selecteditemindex].item;
            var url=JSON.stringify(data);
            /* var check= $("#check").find("input[type='checkbox']:checked").parent().parent();*/
            var datarecord=$("#datarecord");
            datarecord.empty();
            if(data.recordid==null){
                datarecord.append("<p>类型："+(data.type==1?'状态项':'记录项')+"</p>");
            }else{
                var min;var max;
                if(data.normalmin==null){
                    min="";
                }else{
                    min=data.normalmin;
                }
                if(data.normalmax==null){
                    max="";
                }else{
                    max=data.normalmax;
                }
                if(data.lowerwarning==null){
                    lower="";
                }else{
                    lower=data.lowerwarning;
                }
                if(data.upperwarning==null){
                    upper="";
                }else{
                    max=data.upperwarning;
                }
                datarecord.append("<p>类型："+(data.type==1?'状态项':'记录项')+"</p>" +
                        "<p>数据名称："+data.daterecord.name+"</p>" +
                        "<p>单位："+data.daterecord.unitname+"</p>" +
                        "<p>最低值：<span><input type='text' value='"+min+
                        "' style='width: 50px;' onblur='edit(this)' id='input1'></span></p>" +
                        "<p>最高值：<span><input type='text' value='"+max+
                        "' style='width: 50px;'onblur='edit(this)' id='input2'></span></p>" +
                        "<p>下限警告值：<span><input type='text' value='"+lower+
                        "' style='width: 50px;'onblur='edit(this)' id='input3'></span></p>" +
                        "<p>上限警告值：<span><input type='text' value='"+upper+
                        "' style='width: 50px;'onblur='edit(this)' id='input4'></span></p>");
            }
        }
    }
    function edit(obj){
        var normalmin=$("#input1").val();
        var normalmax=$("#input2").val();
        var lowerwarning=$("#input3").val();
        var upperwarning=$("#input4").val();
        selecteditemindex = $("#checkul .selected").attr("checkitemindex");
        var item = res.areas[selectedareaindex].equipments[selectedequipmentindex].
                checkItems[selecteditemindex].item;
        item.normalmin=normalmin;
        item.normalmax=normalmax;
        item.lowerwarning=lowerwarning;
        item.upperwarning=upperwarning;
    }
</script>
<script type="text/javascript">
    $(function(){
        var hour=$("#hour");
        var minute=$("#minute");
        var name2=$("#name2");
        for(var i=0;i<24;i++){
            if(i<=9){
                hour.append("<option>0"+i+"</option>");
            }else{
                hour.append("<option>"+i+"</option>");
            }
        }
        for(var j=0;j<60;j++){
            if(j<=9){
                minute.append("<option>0"+j+"</option>");
            }else{
                minute.append("<option>"+j+"</option>");
            }
        }
    })
    function addtime(){
        $("#myModal").modal("show");
        $("#equipmentlist")[0].style.display = "none";
        $("#arealist")[0].style.display = "none";
        $("#checklist")[0].style.display = "none";
        $("#dotime")[0].style.display="block";
        $("#loading")[0].style.display="none";
    }
    function addtime2(){
        var selecthour=$("#hour").find("option:selected").text();
        var selectminute=$("#minute").find("option:selected").text();
        $("#name4").append("<p onclick='selectp(this)'>"+selecthour+":"+selectminute+"</p>");
        $("#myModal").modal("hide");
    }
    function selectp(obj){
        $(obj).siblings('p').removeClass('selected');  // 删除其他兄弟元素的样式
        $(obj).siblings('p').prop("selected", false);
        $(obj).addClass('selected');
        $(obj).prop("selected", true);
    }
    function deltime1(){
        var $li = $("#name4").find("p:selected");
        if ($li.length <= 0) {
            alert("请选择要删除的选项");
            return false;
        }
        $($li).remove();
    }
</script>
<script type="text/javascript">
function submitarea(){
    /*将日期转换成jsonarray格式*/
    var timeslist= new Array();

    var list=$("#name4").find("p");
    for(var i=0;i<list.length;i++){
        timeslist.push($(list[i]).text());
    }
    $("#listtime").val(JSON.stringify(timeslist));
    /*验证任务执行时间*/
    var listtime=$("#listtime").val();
    if(listtime=="[]"){
        alert("执行时间不能为空哟");
        return false;
    }
    /*将周循环转换成jsonarray格式*/
    var chk_value=new Array();
    $('input[name="week1"]:checked').each(function(){
        chk_value.push($(this).val());
    })
    $("#weeklytime").val(JSON.stringify(chk_value));
    var weeklytime=$("#weeklytime").val();
    if(weeklytime=="[]"){
        alert("请选择周循环");
        return false;
    }
    var val1=$("#zhixing option:selected").val();
    var val2=$("#nfc option:selected").val();
    if(val1==0&&val2==0){
        $("#issequentially").val(0);
    }else if(val1==0&&val2==1){
        $("#issequentially").val(2);
    }else if(val1==1&&val2==0){
        $("#issequentially").val(1);
    }else if(val1==1&&val2==1){
        $("#issequentially").val(3);
    }
    /*表单提交*/
    $("#loading").modal("show");
  $.ajax({
        url:"addtaskplan",
        type:"post",
        data:$('#tasksubmit').serialize(),
        async: false,
       /* timeout:5000,
        beforeSend:function(XMLHttpRequest){
            $("#loading").modal("show");
        },*/
        success: function(data) {
            if(data.success){
                $("#loading").modal("hide");
               alert("添加成功");
               showtask();
            }else{
                alert("添加失败");
               return false;
            }
       }
     /* complete:function(XMLHttpRequest){
          $("#myModal").modal("hide");
          $("#equipmentlist")[0].style.display = "none";
          $("#arealist")[0].style.display = "none";
          $("#checklist")[0].style.display = "none";
          $("#dotime")[0].style.display="none";
          $("#loading")[0].style.display="none";
      }*/
   })
}
function showtask(){
    var tasktype=$("#tasktype").val();
    window.location="showtaskplan?page=1&type="+tasktype;
}
function selectallarea(obj,checkboxname){
    if($(obj).prop("checked")){
        $("input:checkbox[name='"+checkboxname+"']").each(function() {
            $(this).prop("checked",true);
          /*  $(".showinput").css("display", "block");*/
        });
    }else{
        $("input:checkbox[name='"+checkboxname+"']").each(function() {
            $(this).prop("checked",false);
           /* $(".showinput").css("display", "none");*/
        });
    }
}
function areadataupper(){
    var selectindex = $("#areaul").find(".selected").attr("areaindex");
    if(selectindex==0) {
        alert("不能继续上移了");
        return false;
    }
    var areauldata = res.areas;
    var area;
    area = res.areas[selectindex];
    res.areas.splice(selectindex,1);
    res.areas.splice(--selectindex, 0, area);
    refresharea(selectindex);
}
function areadatalower(){
    var selectindex = $("#areaul").find(".selected").attr("areaindex");
    var areauldata = res.areas;
    var lastindex=areauldata.length-1;
    if(selectindex==lastindex) {
        alert("不能继续下移了");
        return false;
    }
    var area;
    area = res.areas[selectindex];
    res.areas.splice(selectindex,1);
    res.areas.splice(++selectindex, 0, area);
    refresharea(selectindex);
}
function equipdataupper(){
    var selectindex = $("#equipul").find(".selected").attr("equipindex");
    if(selectindex==0) {
        alert("不能继续上移了");
        return false;
    }
    var equipdata = res.areas[selectedareaindex].equipments;
    var equip;
    equip = res.areas[selectedareaindex].equipments[selectindex];
    res.areas[selectedareaindex].equipments.splice(selectindex,1);
    res.areas[selectedareaindex].equipments.splice(--selectindex, 0, equip);
    refreshEq(selectindex);
}
function equipdatalower(){
    var selectindex = $("#equipul").find(".selected").attr("equipindex");
    var equipdata = res.areas[selectedareaindex].equipments;
    var lastindex=equipdata.length-1;
    if(selectindex==lastindex) {
        alert("不能继续下移了");
        return false;
    }
    var equip;
    equip = res.areas[selectedareaindex].equipments[selectindex];
    res.areas[selectedareaindex].equipments.splice(selectindex,1);
    res.areas[selectedareaindex].equipments.splice(++selectindex, 0, equip);
    refreshEq(selectindex);
}
function checkdataupper(){
    var selectindex = $("#checkul").find(".selected").attr("checkitemindex");
    if(selectindex==0) {
        alert("不能继续上移了");
        return false;
    }
    var checkdata = res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems;
    var check = res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems[selectindex];
    checkdata.splice(selectindex,1);
    checkdata.splice(--selectindex, 0, check);
    refreshCheck(selectindex);
}
function checkdatalower(){
    var selectindex = $("#checkul").find(".selected").attr("checkitemindex");
    var checkdata = res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems;
    var lastindex=checkdata.length-1;
    if(selectindex==lastindex) {
        alert("不能继续下移了");
        return false;
    }
    var check= res.areas[selectedareaindex].equipments[selectedequipmentindex].checkItems[selectindex];
    checkdata.splice(selectindex,1);
    checkdata.splice(++selectindex, 0, check);
    refreshCheck(selectindex);
}
</script>
<!-- topbar ends -->
<div class="ch-container">
    <div class="row">

       <%-- <%@ include file="/WEB-INF/pages/common/menu.jsp"%>--%>
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2>
                                <i class="glyphicon glyphicon-globe"></i>
                                <c:if test="${type==0}">日常巡检任务--添加</c:if>
                                <c:if test="${type==1}">计划巡检任务--添加</c:if>
                                <c:if test="${type==2}">HSE隐患排查--添加</c:if>
                                <c:if test="${type==3}">视频巡检任务--添加</c:if>
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="" method="post" id="tasksubmit">
                                <div id="first">
                                    <input type="hidden" name="type" id="tasktype" value="${type}">
                                    <input type="hidden" name="state" value="1">
                                    <input type="hidden" name="auditstatus" value="1">
                                    <input type="hidden" name="issequentially" id="issequentially">
                                    <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                        <tr>
                                            <td class="form-inline" colspan="3">
                                                <label class="control-label" for="siteid">厂区:</label>
                                                <select class="form-control" id="siteid" name="siteid" onchange="sitechange()">
                                                    <option value="">--选择厂区--</option>
                                                    <c:forEach items="${siteareainfos}" var="site">
                                                        <option value="${site.id}">${site.customid}</option>
                                                    </c:forEach>
                                                </select>
                                                <label class="control-label" for="classid" style="margin-left: 50px;">班组:</label>
                                                <select class="form-control" id="classid" name="classid" onchange="change()">
                                                    <%--<c:forEach items="${classinfos}" var="classinfos">--%>
                                                        <%--<option value="${classinfos.id}">${classinfos.customid}</option>--%>
                                                    <%--</c:forEach>--%>
                                                </select>

                                                <label class="control-label" for="directorid" style="margin-left: 50px;">负责人:</label>
                                                <select class="form-control" id="directorid" name="directorid">

                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="form-inline">
                                                <label class="control-label" for="idtype">任务号:</label>
                                                <input type="text" class="form-control" id="idtype" name="identifyingid" required placeholder="任务号不能为空" >
                                            </td>
                                            <td class="form-inline">
                                                <label class="control-label" for="taskname">任务名:</label>
                                                <input type="text" name="customid" class="form-control" id="taskname" placeholder="任务名不能为空">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="form-inline" colspan="2">
                                               <%-- <label class="control-label" for="username">任务描述:</label>--%>
                                                <span>任务描述:</span>
                                                <textarea  class="form-control" id="username" name="taskdesc" rows="3" cols="150" required placeholder="只能输入0-15个字符"></textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="form-inline">
                                                <label class="control-label" for="pwd2">创建人:</label>
                                                <input type="text" class="form-control" style="width: 300px;" id="pwd2" name="createuser" required placeholder="创建人不能为空">
                                            </td>
                                            <td class="form-inline">
                                                 <label class="control-label" for="pwd3">创建时间:</label>
                                                <input type="text"  id="pwd3" name="createtime" class="form-control" onClick="WdatePicker()">
                                            </td>
                                        </tr>

                                        <tr>
                                            <td class="form-inline">
                                                <label class="control-label" for="pwd4">审核人:</label>
                                                <input type="text" class="form-control" style="width: 300px;" id="pwd4" name="revieweduser" required placeholder="审核人不能为空">
                                            </td>
                                            <td class="form-inline">
                                                <label class="control-label" for="pwd7">审核时间:</label>
                                                <input type="text" class="form-control" style="width: 300px;" id="pwd7"  onClick="WdatePicker()" name="reviewedtime" required>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="form-inline">
                                             <label class="control-label" for="pwd5">批准人:</label>
                                                <input type="text" class="form-control" style="width: 300px;" id="pwd5" name="approveuser" required placeholder="批准人不能为空">
                                            </td>
                                            <td class="form-inline">
                                                <label class="control-label" for="pwd6">批准时间:</label>
                                                <input type="text" class="form-control" style="width: 300px;" id="pwd6"  onClick="WdatePicker()" name="approvetime" required>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="form-inline">
                                                <label class="control-label" for="banben">任务版本号:</label>
                                                <input type="text" class="form-control" style="width: 300px;" value="1" id="banben" name="taskversion" required placeholder="版本号不能为空">
                                            </td>
                                            <td class="form-inline">
                                                <label class="control-label" for="pwd8">最长执行时间(单位：分钟):</label>
                                                <input type="text" class="form-control" style="width: 300px;" id="pwd8" name="maxduration" required placeholder="请输入整数">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="form-inline">
                                                <label class="control-label" for="zhixing">顺序执行:</label>
                                                <select name="order" class="form-control"  id="zhixing">
                                                    <option value="0">否</option>
                                                    <option value="1">是</option>
                                                </select>
                                            </td>
                                            <td class="form-inline">
                                                <label class="control-label" for="nfc">扫描nfc:</label>
                                                <select name="nfc" class="form-control"  id="nfc">
                                                    <option value="0">否</option>
                                                    <option value="1" selected>是</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><input type="button" class="btn btn-primary" value="下一步 区域设置" onclick="second()">
                                                <input type="reset" class="btn btn-primary white" value="取消" onclick="javascript:history.go(-1);"></td>
                                        </tr>
                                    </table>
                                </div>
                                <div id="second" style="display: none">
                                    <input type="hidden" name="taskcontent" id="taskcontent">
                                    <div style="width: 230px;height: 420px;position:relative;float: left;">
                                        <h4>区域列表</h4>
                                        <div style="width: 200px;height: 300px;border: 1px solid gray;overflow:auto;margin-bottom: 20px;">
                                            <ul class="list-group" id="areaul">
                                               <%-- <li class="list-group-item">区域一</li>
                                                 <li class="list-group-item">区域二</li>
                                                 <li class="list-group-item">区域三</li>--%>
                                            </ul>
                                        </div>
                                        <a href="javascript:void(0)" onclick="model()">
                                            <input type="button" class="btn btn-inverse btn-default btn-lg" value="添加区域">
                                        </a>
                                        <a href="javascript:void(0)" onclick="delarea('#areaul')">
                                            <input type="button" class="btn btn-inverse btn-default btn-lg" value="删除区域">
                                        </a>
                                    </div>
                                    <div style="width: 30px;height: 400px;position:relative;float: left;padding-top: 130px;padding-left: 3px;">
                                        <a href="javascript:void(0);" onclick="areadataupper()"><i class="glyphicon glyphicon-arrow-up black" style="font-size: 30px;"></i></a><br/><br/><br/><br/>
                                        <a href="javascript:void(0);" onclick="areadatalower()"><i class="glyphicon glyphicon-arrow-down black" style="font-size: 30px;"></i></a>
                                    </div>
                                    <div style="width: 230px;height: 420px;position:relative;float: left;margin-left:30px;">
                                        <h4>设备列表</h4>
                                        <div style="width: 200px;height: 300px;border: 1px solid gray;overflow:auto;margin-bottom: 20px;">
                                            <ul class="list-group" id="equipul">
                                                <%--<li class="list-group-item">设备一</li>
                                                <li class="list-group-item">设备二</li>
                                                <li class="list-group-item">设备三</li>--%>
                                            </ul>
                                        </div>
                                        <input type="button" class="btn btn-inverse btn-default btn-lg" value="添加设备" onclick="showequip()">
                                        <input type="button" class="btn btn-inverse btn-default btn-lg" value="删除设备" onclick="delequip('#equipul')">
                                    </div>
                                    <div style="width: 30px;height: 400px;position:relative;float: left;padding-top: 130px;padding-left: 3px;">
                                        <a href="javascript:void(0);" onclick="equipdataupper()"><i class="glyphicon glyphicon-arrow-up black" style="font-size: 30px;"></i></a><br/><br/><br/><br/>
                                        <a href="javascript:void(0);" onclick="equipdatalower()"><i class="glyphicon glyphicon-arrow-down black" style="font-size: 30px;"></i></a>
                                    </div>
                                    <c:if test="${type==0 || type==1}">
                                        <div style="width: 270px;height: 420px;position:relative;float: left;margin-left:30px;">
                                            <h4>巡检项列表</h4>
                                            <div style="width: 220px;height: 300px;border: 1px solid gray;overflow:auto;margin-bottom: 20px;">
                                                <ul class="list-group" id="checkul">
                                                        <%--<li class="list-group-item">巡检项一</li>
                                                        <li class="list-group-item">巡检项二</li>
                                                        <li class="list-group-item">巡检项三</li>--%>
                                                </ul>
                                            </div>
                                            <input type="button" class="btn btn-inverse btn-default btn-lg" value="添加巡检项" onclick="showcheck()">
                                            <input type="button" class="btn btn-inverse btn-default btn-lg" value="删除巡检项" onclick="delitem('#checkul')">
                                        </div>
                                        <div style="width: 30px;height: 400px;position:relative;float: left;padding-top: 130px;margin-left: -3px;">
                                            <a href="javascript:void(0);" onclick="checkdataupper()"><i class="glyphicon glyphicon-arrow-up black" style="font-size: 30px;"></i></a><br/><br/><br/><br/>
                                            <a href="javascript:void(0);" onclick="checkdatalower()"><i class="glyphicon glyphicon-arrow-down black" style="font-size: 30px;"></i></a>
                                        </div>
                                        <div style="width: 200px;height: 420px;position:relative;float: left;margin-left: 30px;">
                                            <h4>巡检项内容</h4>
                                            <div style="width: 200px;height: 300px;border: 1px solid gray;overflow:auto;margin-bottom: 20px;" id="datarecord">
                                                    <%--类型：记录项
                                                    压力：0.9 ~2.5
                                                    单位：MPa--%>
                                            </div>
                                        </div>
                                    </c:if>
                                    <%--<div style="display:${type eq 2 ? 'none' : 'block'};">--%>
                                        <%--<div style="width: 270px;height: 420px;position:relative;float: left;margin-left:30px;">--%>
                                            <%--<h4>巡检项列表</h4>--%>
                                            <%--<div style="width: 220px;height: 300px;border: 1px solid gray;overflow:auto;margin-bottom: 20px;">--%>
                                                <%--<ul class="list-group" id="checkul">--%>
                                                    <%--&lt;%&ndash;<li class="list-group-item">巡检项一</li>--%>
                                                    <%--<li class="list-group-item">巡检项二</li>--%>
                                                    <%--<li class="list-group-item">巡检项三</li>&ndash;%&gt;--%>
                                                <%--</ul>--%>
                                            <%--</div>--%>
                                            <%--<input type="button" class="btn btn-inverse btn-default btn-lg" value="添加巡检项" onclick="showcheck()">--%>
                                            <%--<input type="button" class="btn btn-inverse btn-default btn-lg" value="删除巡检项" onclick="delitem('#checkul')">--%>
                                        <%--</div>--%>
                                        <%--<div style="width: 30px;height: 400px;position:relative;float: left;padding-top: 130px;margin-left: -3px;">--%>
                                            <%--<a href="#"><i class="glyphicon glyphicon-arrow-up black" style="font-size: 30px;"></i></a><br/><br/><br/><br/>--%>
                                            <%--<a href="#"><i class="glyphicon glyphicon-arrow-down black" style="font-size: 30px;"></i></a>--%>
                                        <%--</div>--%>
                                        <%--<div style="width: 200px;height: 420px;position:relative;float: left;margin-left: 30px;">--%>
                                            <%--<h4>巡检项内容</h4>--%>
                                            <%--<div style="width: 200px;height: 300px;border: 1px solid gray;overflow:auto;margin-bottom: 20px;" id="datarecord">--%>
                                                <%--&lt;%&ndash;类型：记录项--%>
                                                <%--压力：0.9 ~2.5--%>
                                                <%--单位：MPa&ndash;%&gt;--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <div class="clearfix"></div>
                                    <div style="margin-top: 35px;">
                                        <a href="#"><input type="button" class="btn btn-inverse btn-default btn-lg" value="上一步 基本设置" onclick="first()"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <a href="#"><input type="button" class="btn btn-inverse btn-default btn-lg" value="下一步 循环执行设置" onclick="third()"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <a href="showtaskplan?page=1&type=${type}"><input type="button" class="btn btn-inverse btn-default btn-lg" value="取消"></a>
                                    </div>
                                </div>
                                <div id="third" style="display: none">
                                    <div style="border: 1px solid gray;width: 800px;" id="checkedradio">
                                        <%--<div class="form-inline" >
                                            <input id="radion1" type="radio" name="issingletime" checked value="1" onclick="changeradio()" style="margin-top:15px;margin-left: 15px;margin-bottom: 25px;">单日单次执行<br/>
                                            <label class="control-label" for="name2" style="margin-left: 25px;">执行时间:</label>
                                            <input type="text" class="form-control" id="name2" placeholder="格式为：00:00 小时：分钟">
                                        </div>--%>
                                        <input type="hidden"  id="listtime" name="implementtime">
                                        <div class="form-inline"style="margin-top: 25px;margin-bottom: 25px;">
                                           <%-- <input id="radion2" type="radio" name="issingletime" value="0" onclick="changeradio()" style="margin-top:15px;margin-left: 15px;margin-bottom: 25px;">单日多次执行<br/>

                                            <label class="control-label" for="name1"style="margin-left: 25px;" >起始时间：</label>
                                            <input type="text" name="starttime" id="name1" disabled class="form-control" placeholder="格式为：00:00 小时：分钟">

                                            <label class="control-label" for="name3">终止时间:</label>
                                            <input type="text" id="name3" name="endtime" class="form-control" placeholder="格式为：00:00 小时：分钟"><br>--%>


                                            <div style="margin-left: 25px;font-size:15px;position:relative;float: left;margin-top: 60px;">执行列表：</div>
                                            <div id="name4" style="position:relative;float: left;margin-top: 25px;width:350px;height:150px;border: 1px solid gray;overflow: auto"></div>

                                            <div style="position:relative;float: left;margin-top: 40px;margin-left: 25px;">
                                                <input type="button" id="addtimee" value="添加" class="btn btn-inverse btn-default btn-lg" onclick="addtime()"><br>
                                                <input type="button" id="deltime" value="删除" class="btn btn-inverse btn-default btn-lg" style="margin-top:15px;" onclick="deltime1()">
                                            </div>
                                            <div class="clearfix"></div>
                                        </div>
                                    </div>
                                    <div style="margin-top:35px;margin-left: 15px;">
                                        周循环：<input type="checkbox" name="week1" value="周一">周一
                                        <input type="checkbox" name="week1" value="周二">周二
                                        <input type="checkbox" name="week1" value="周三">周三
                                        <input type="checkbox" name="week1" value="周四">周四
                                        <input type="checkbox" name="week1" value="周五">周五
                                        <input type="checkbox" name="week1" value="周六">周六
                                        <input type="checkbox" name="week1" value="周日">周日
                                        <input type="hidden" name="weeklytime" value="" id="weeklytime">
                                    </div>
                                    <div style="margin-top: 25px;">
                                        <a href="#" style="margin-left: 35px;">
                                            <input type="button" class="btn btn-inverse btn-default btn-lg" value="上一步 区域设置" onclick="second()">
                                        </a>
                                        <a href="#" style="margin-left: 35px;">
                                            <input type="button" class="btn btn-inverse btn-default btn-lg" value="保存" onclick="submitarea()">
                                        </a>
                                        <a href="showtaskplan?page=1&type=${type}" style="margin-left: 35px;">
                                            <input type="button" class="btn btn-inverse btn-default btn-lg" value="取消">
                                        </a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div aria-hidden="false" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade in" style="z-index: 0">
    <div class="modal-dialog" style="z-index: 9999;">
        <div class="modal-content" >
            <div id="arealist"  style="height:700px;overflow: auto">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>区域列表</h3>
                </div>
                <div class="modal-body">
                    <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable" id="area">
                        <tr>
                            <th><input type="checkbox" name="selectallarea" id="selectallarea" onclick="selectallarea(this,'allarea')"> 全选/全不选</th>
                            <th>区域编号</th>
                            <th>区域名称</th>
                            <th>区域描述</th>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <input type="button" id="btn" value="添加" class="btn btn-primary" onclick="addarea()">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
            <div id="equipmentlist" style="height:700px;overflow: auto;">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>设备列表</h3>
                </div>
                <div class="modal-body">
                    <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable" id="equipment">
                        <tr>
                            <th><input type="checkbox" name="allequip" id="selectsllequip" onclick="selectallarea(this,'allequip')">全选/全不选</th>
                            <th>设备编号</th>
                            <th>设备名称</th>
                            <th>设备描述</th>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <input type="button" value="添加" class="btn btn-primary" onclick="addequip()">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
            <div id="checklist"  style="height:700px;overflow: auto;">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>巡检项列表<span>(选中行灰色区域可编辑)</span></h3>
                </div>
                <div class="modal-body">
                    <table class="table table-bordered " id="check">
                        <tr>
                            <th><input type="checkbox" name="allcheckitem" id="selectallcheck" onclick="selectallarea(this,'allcheckitem')">全选/全不选</th>
                            <th>编号</th>
                            <th>类型</th>
                            <th>名称</th>
                          <%--  <th>最低值</th>
                            <th>最高值</th>
                            <th>下限警告值</th>
                            <th>上限警告值</th>--%>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <input type="button" value="添加" class="btn btn-primary" onclick="addcheck()">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
            <div id="dotime">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>执行列表</h3>
                </div>
                <div class="modal-body">
                    选择执行时间：<select id="hour"></select>:<select id="minute"></select>
                </div>
                <div class="modal-footer">
                    <input type="button" id="btn2" value="添加" class="btn btn-primary" onclick="addtime2()">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
            <div id="loading">
                <div class="modal-header">
                    <h3>提示</h3>
                </div>
                <div class="modal-body">
                   请稍后。。。。。
                </div>
                <%--<div class="modal-footer">
                    <input type="button" id="btn2" value="添加" class="btn btn-primary" onclick="addtime2()">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>--%>
            </div>
        </div>
    </div>
</div>
<%--<script type="text/javascript">
    $(function () {
        $("#check").delegate(".showCheckbox", "click", function () {
            var $inputChk = $(this).parent().parent().find($(".showinput"));
            var css = $inputChk.css("display");
            if (css != 'none') {
                $inputChk.css("display", "none");
            } else {
                $inputChk.css("display", "block");
            }
        });
    });
</script>--%>
</body>
</html>
