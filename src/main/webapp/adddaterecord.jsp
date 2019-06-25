<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>

</head>
<body>
<%--<%@ include file="/WEB-INF/pages/common/navigation.jsp"%>--%>
<!-- topbar ends -->
<div class="ch-container">
    <div class="row">

        <%--<%@ include file="/WEB-INF/pages/common/menu.jsp"%>--%>
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2>
                                <i class="glyphicon glyphicon-globe"></i> 可读数据项--添加
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="addrecord" method="post">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="datename">名称:</label>
                                           <input type="text" name="name" class="form-control" id="datename" required>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="name2">计量单位:</label>
                                            <input type="text" class="form-control" id="name2" name="unitname" required>
                                        </td>
                                    </tr>
                                    <%--<tr>--%>
                                        <%--<td class="form-inline">--%>
                                            <%--<label class="control-label" for="pwd">默认值:</label>--%>
                                            <%--<input type="text" class="form-control" id="pwd" name="defaultvalue" required>--%>
                                        <%--</td>--%>
                                    <%--</tr>--%>
                                    <tr>
                                        <td><input type="submit" class="btn btn-primary" value="保存">
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
