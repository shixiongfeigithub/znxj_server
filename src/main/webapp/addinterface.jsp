<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>


    <script type="text/javascript">
        function validateForm(){
            var siteid = $("#siteid").val();
            if(siteid == null || siteid == undefined || siteid == ''){
                alert("您还没有选择厂区哟！！！");
                return false;
            }else{
                return true;
            }
            var ip = $("#id").val();
            if(ip==null || ip == ""){
                alert("IP地址不能为空！");
                return false;
            }

            var port = $("#port").val();
            if(port==null || port == ""){
                alert("端口号不能为空！");
                return false;
            }
            var enginetype = $("#enginetype").val();
            if(enginetype==null || enginetype == ""){
                alert("接口类型不能为空！");
                return false;
            }

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
                                <i class="glyphicon glyphicon-globe"></i> 接口--添加
                            </h2>
                        </div>
                        <div class="box-content">
                            <span style="color: #9a161a">${message}</span>
                            <form action="addinterface" method="post" id="form2" onsubmit="return validateForm()">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="siteid">厂区:</label>
                                           <select class="form-control" name="siteid" id="siteid" required="required">
                                               <option value="">--请选择--</option>
                                               <c:forEach items="${siteareainfos}" var="site">
                                                   <option value="${site.id}">${site.customid}</option>
                                               </c:forEach>
                                           </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="ip">ip地址:</label>
                                            <input type="text" class="form-control" style="width: 300px;" id="ip" name="ip" required>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="port">端口号:</label>
                                            <input type="text" class="form-control" style="width: 300px;" id="port" name="port" required>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="enginetype">接口类型:</label>
                                            <select class="form-control" id="enginetype" name="enginetype">
                                                <option selected="selected"value="1">苏州公司接口</option>
                                            </select>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="email">邮箱:</label>
                                            <input type="email" class="form-control" style="width: 300px;" id="email" name="email" required>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td><input type="submit" class="btn btn-primary" value="保存">
                                            <input type="button" class="btn btn-primary white" value="取消" onclick="javascript:history.go(-1);"></td>
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
