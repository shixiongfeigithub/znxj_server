<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <script type="text/javascript">
        function validateForm() {
            var chk_value =[];
            $('input[name="exceptiontype"]:checked').each(function(){ //遍历，将所有选中的值放到数组中
                chk_value.push($(this).val());
            });
            if(chk_value.length==0){
                alert("请选择需要上传的异常类型！");
                return false;
            };

            var chk2_value =[];
            $('input[name="exceptionlever"]:checked').each(function(){ //遍历，将所有选中的值放到数组中
                chk2_value.push($(this).val());
            });
            if(chk2_value.length==0){
                alert("请选择需要上传的异常等级！");
                return false;
            };

            var state = $("#uploadstate").val();
            if (state == null || state == "") {
                alert("请选择是否需要上传！");
                return false;
            }

            var contactinfo = $("#contactinfo").val();
            if(contactinfo==null || contactinfo == ""){
                alert("请选择联系人说！");
                return false;
            }
        }

        $(function(){
            var exceptiontypes = '${taskuploadconfig.exceptiontype}';
            var array = exceptiontypes.split(",");
            if(array != null) {
                $.each(array, function(key, value) {
                    $("input[name='exceptiontype'][value='"+value+"']").attr("checked", true);
                });
            }

            var exceptionlevers = '${taskuploadconfig.exceptionlever}';
            var array2 = exceptionlevers.split(",");
            if(array2 != null) {
                $.each(array2, function(key, value) {
                    $("input[name='exceptionlever'][value='"+value+"']").attr("checked", true);
                });
            }
        })
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
                                <i class="glyphicon glyphicon-globe"></i> 任务上传--编辑
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="updtaskupload" method="post" id="form2" onsubmit="return validateForm()">
                                <input type="hidden" name="id" value="${taskuploadconfig.id}">
                                <input type="hidden" name="page" value="${page}">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label">厂区:</label>
                                            ${taskuploadconfig.sitename }
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >任务:</label>
                                            ${taskuploadconfig.taskname }
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >上传异常类型:</label>
                                            <c:forEach items="${exceptiontypeList}" var="type">
                                                <input class="form-control" name="exceptiontype" type="checkbox" value="${type.name}">${type.name}
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label">上传异常等级:</label>
                                            <c:forEach items="${levertype1List}" var="lever">
                                                <input class="form-control" name="exceptionlever" type="checkbox" ${taskuploadconfig.exceptionlever eq lever.name ?'checked':''} value="${lever.name}">${lever.name}
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="uploadstate">是否上传:</label>
                                            <select class="form-control" id="uploadstate" name="uploadstate">
                                                <option ${taskuploadconfig.uploadstate eq '1'?'selected':''} value="1">是</option>
                                                <option ${taskuploadconfig.uploadstate eq '0'?'selected':''}value="0">否</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="contactinfo">联系人:</label>
                                            <select class="form-control" name="contactid" id="contactinfo" required="required">
                                                <c:forEach items="${contactinfoList}" var="info">
                                                    <option ${taskuploadconfig.contactid eq info.id ?'selected':''} value="${info.id}">${info.name}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><input type="submit" class="btn btn-primary" value="修改">
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
