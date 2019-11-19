<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <style>
        .fileinput-button {
            position: relative;
            display: inline-block;
            overflow: hidden;
        }

        .fileinput-button input{
            position:absolute;
            right: 0px;
            top: 0px;
            opacity: 0;
            -ms-filter: 'alpha(opacity=0)';
            font-size: 200px;
        }
        .selected{background: #35A7E7;
            color: white;}
    </style>
    <script type="text/javascript">
        var photo = new Array();

        function upload(){
            var desc=$("#descontent").val();
            if(desc==""){
                alert("详情不能为空");
                return false;
            }
            var pic="";
            for(var i=0;i<photo.length;i++){
                pic+=photo[i]+",";
            }
            $("#attachvalue").val(pic.substring(0,pic.length-1));
                $.ajax({
                    url:"closetaskreport",
                    type:"post",
                    data:$("#form").serialize(),
                    success: function(data){
                        if(data>0){
                            alert("任务关闭成功");
                            showexceptionreport();
                        }else if(data==0){
                            alert("任务已关闭，不能重复关闭");
                            showexceptionreport();
                            return false;
                        }else {
                            alert("任务关闭失败");
                            return false;
                        }
                    },
                });

        }

        function showexceptionreport(){
            window.location="showexceptionreport?page=1";
        }

        function getFileName(o){
            var pos=o.lastIndexOf("\\");
            return o.substring(pos+1);
        }
        function delfile(){
            var $li = $("#file").find("li:selected");
            var a=$("#file").find("li:selected").attr("num");
            for(var i=0;i<photo.length;i++){
                if(a==photo[i]){
                    photo.splice(i,1);
                }
            }
            if ($li.length <= 0) {
                alert("请选择要删除的选项");
                return false;
            }
            $($li).remove();
        }
        function selectfile(obj){
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);
        }

        function uploadFile(obj,type) {
            var file = obj.files[0];//获取file文件对象
            if (file.size > (3 * 1024 * 1024)) {
                alert("图片大小不能超过" + 1 + "MB");
                $(this).val("");
                return;
            }
            var  backpic="";
            var formData = new FormData();
            formData.append("img",file);
            $.ajax({
                url:"imgupload",
                type: "POST",
                data:formData,
                async: false,
                processData: false,
                contentType: false,
                dataType: "json",
                success: function (data) {
                    photo.push(data.message);
                    backpic=data.message;
                }
            });
            $("#file").append("<li class='list-group-item' onclick='selectfile(this)' num='"+backpic+"'>"+getFileName(file.name)+"</li>");
            return false;
        }
    </script>
</head>
<body>
<div class="ch-container">
    <div class="row">

        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2>
                                <i class="glyphicon glyphicon-globe"></i> 关闭巡检异常
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="" enctype="multipart/form-data" method="post" id="form">
                                <input type="hidden"  name="attachment" id="attachvalue">
                                <input type="hidden"  name="reportid" id="reportid" value="${reportcontent.reportid}">
                                <input type="hidden"  name="reportcontentid" id="reportcontentid" value="${reportcontent.id}">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >巡检项名称:</label>
                                            ${reportcontent.checkname}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <span>描述:</span>
                                            <textarea name="descontent" id="descontent" cols="20" rows="5" style="width: 300px;" required></textarea>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="form-inline">
                                            <span>图片:<span style="color: red;">(只支持图片文件)</span></span>
                                            <div id="attachment">
                                                <div style="width: 400px;height:150px;border: 1px solid darkgray;background:white;position:relative;float:left;overflow: auto">
                                                     <ul class="list-group" id="file">

                                                     </ul>
                                                 </div>
                                                <div>
                                                     <span class="btn btn-inverse btn-default btn-lg fileinput-button" style="margin-left: 25px;margin-top: 33px;">
                                                         <span>添加</span>
                                                         <input type="file"  id="addfile" multiple="true" name="photos" onchange ="uploadFile(this,1)" >
                                                     </span>

                                                     <input type="button" class="btn btn-inverse btn-default btn-lg" value="删除" onclick="delfile()" style="margin-top: 33px;">
                                                </div>
                                                 <div class="clearfix"></div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><input type="button" class="btn btn-primary" value="保存" onclick="upload()">
                                            <input type="reset" class="btn btn-primary white" value="取消" onclick="javascript:history.go(-1);"></td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
