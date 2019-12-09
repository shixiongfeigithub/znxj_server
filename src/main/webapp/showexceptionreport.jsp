<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <script type="text/javascript" src="js/formatdate.js"></script>
    <script type="text/javascript">
        $(function () {
            sitechange();
        })

        function sitechange(){
            $("#areainfo").empty();
            var siteId= $("#siteid option:selected")[0].value;
            var areaId = '${areaid}';
            $("#areainfo").append("<option  value='' >所有区域</option>");
            if(siteId != null && siteId !=''){
                $.ajax({
                    url:"queryareabysiteid",
                    type:"post",
                    data:{
                        siteid:siteId,
                    },
                    dataType:"json",
                    success:function(data){
                        if(data!=null){
                            for(var i=0;i<data.length;i++){
                                if(data[i].id==areaId){
                                    $("#areainfo").append("<option  value='"+data[i].id+"' selected>"+data[i].customid+"</option>");
                                }else{
                                    $("#areainfo").append("<option  value='"+data[i].id+"' >"+data[i].customid+"</option>");
                                }
                            }
                        }
                    }
                });
            }
            areachange();
        }


        /*根据区域编号显示所有的设备*/
        function areachange() {
            $("#equipment").empty();
            var areaid = $("#areainfo option:selected")[0].value;
            $("#equipment").append("<option  value='' >所有设备</option>");
            var equipmentid = '${equipmentid}';
            if(areaid != null && areaid != '') {
                $.ajax({
                    url: "showequipment?areaid=" + areaid,
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            if (data[i].id == equipmentid) {
                                $("#equipment").append("<option  value='" + data[i].id + "' selected>" + data[i].name + "</option>");
                            } else {
                                $("#equipment").append("<option  value='" + data[i].id + "' >" + data[i].name + "</option>");
                            }
                        }
                    }
                })
            }
        }

        function closetaskreport(id) {
            window.location="/toclosetaskreport?id="+id;
        }

        function assignprincipal(id) {
            window.location="/toassignprincipal?id="+id;
        }
        function handlerdetail(id){
            window.location="/handlerexceptiondetail?id="+id;
        }
    </script>
</head>
<body>
<input type="hidden" id="page" value="${pageBean.currentPage}">
<div class="ch-container">
    <div class="row">
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <input type="hidden" value="${roleid}" id="roleid">
                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="report">
                            <div class="box-inner">
                                <div class="box-header well" data-original-title="">
                                    <h2>
                                        <i class="glyphicon glyphicon-globe"></i>
                                        巡检异常 - 列表
                                    </h2>
                                </div>
                                <div class="box-content">
                                    <div class="form-inline" style="margin-bottom: 20px;">
                                        <form action="showexceptionreport?page=1" method="post">
                                            <label class="control-label" for="siteid">厂区：</label>
                                            <select id="siteid" class="form-control" name="siteid" onchange="sitechange()">
                                                <c:if test="${siteid==null}">
                                                    <option value="">所有厂区</option>
                                                </c:if>
                                                <c:forEach items="${sites}" var="site">
                                                    <option  value="${site.id}" ${siteid eq site.id ?'selected':''}>${site.customid}</option>
                                                </c:forEach>
                                            </select>

                                            <label class="control-label" for="areainfo">区域：</label>
                                            <select class="form-control" id="areainfo" name="areaid" onchange="areachange()" >
                                                <option value="" selected>所有区域</option>
                                            </select>

                                            <label class="control-label" for="equipment">设备：</label>
                                            <select class="form-control" id="equipment" name="equipmentid">
                                                <option value="" selected>所有设备</option>
                                            </select>
                                            <label class="control-label" for="exceptiontype">异常分类：</label>
                                            <select class="form-control" id="exceptiontype" name="exceptiontype">
                                                <option value="" >所有分类</option>
                                                <c:forEach items="${exceptiontypeList}" var="type">
                                                    <option ${exceptiontype eq type.name ?'selected':''} value="${type.name}">${type.name}</option>
                                                </c:forEach>
                                            </select>
                                            <label class="control-label" for="exceptionlever">异常分级：</label>
                                            <select class="form-control" id="exceptionlever" name="exceptionlever">
                                                <option value="" >所有分级</option>
                                                <c:forEach items="${levertype1List}" var="lever">
                                                    <option ${exceptionlever eq lever.name ?'selected':''} value="${lever.name}" >${lever.name}</option>
                                                </c:forEach>
                                            </select>
                                            <br>
                                            <div style="line-height: 10px;">&nbsp;</div>
                                            <label class="control-label" for="exceptionstate">处理状态：</label>
                                            <select class="form-control" id="exceptionstate" name="exceptionstate" style="width: 90px;">
                                                <option ${exceptionstate eq '' ? 'selected' : ''} value="">所有</option>
                                                <option ${exceptionstate eq '0' ? 'selected' : ''} value="0">待处理</option>
                                                <option ${exceptionstate eq '1' ? 'selected' : ''} value="1">已关闭</option>
                                                <option ${exceptionstate eq '2' ? 'selected' : ''} value="2">处理中</option>
                                            </select>
                                            <label class="control-label" for="operatorname">负责人：</label>
                                            <input type="text" style="width: 80px;" id="operatorname" name="operatorname" value="${operatorname}">
                                            <label class="control-label">执行时间：</label>
                                            <input type="text" name="time1" onClick="WdatePicker()" value="${param.time1}"style="margin-top: 10px; width: 90px;" id="time1">--<input type="text" name="time2" onClick="WdatePicker()" value="${param.time2}"style="margin-top: 10px;width: 90px;" id="time2">
                                            <input type="submit" class="btn btn-primary" value="搜索" style="margin-left: 20px;margin-top: 10px"> <span style="margin: 10px;">总数：${totalnum}</span><span style="margin: 10px;">已关闭：${closenum}</span><span style="margin: 10px;">剩余：${surplusnum}</span>
                                            <script language="JavaScript">
                                                Date.prototype.format = function (format) {
                                                    var args = {
                                                        "M+": this.getMonth() + 1,
                                                        "d+": this.getDate(),
                                                        "h+": this.getHours(),
                                                        "m+": this.getMinutes(),
                                                        "s+": this.getSeconds(),
                                                        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter

                                                        "S": this.getMilliseconds()
                                                    };
                                                    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                                                    for (var i in args) {
                                                        var n = args[i];
                                                        if (new RegExp("(" + i + ")").test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
                                                    }
                                                    return format;
                                                };
                                                $(function () {
                                                    if($("#time1").val() == ""){
                                                        $("#time1").val(new Date().format("yyyy-MM-dd"));
                                                    }
                                                    if($("#time2").val() == ""){
                                                        $("#time2").val(new Date().format("yyyy-MM-dd"));
                                                    }
                                                });
                                            </script>

                                        </form>
                                    </div>

                                    <table id="table" class="table table-striped table-bordered table-hover ">
                                        <tr>
                                            <th>操作</th>
                                            <th>厂区</th>
                                            <th class="fontcenter">区域</th>
                                            <th class="fontcenter">设备</th>
                                            <th class="fontcenter">巡检项</th>
                                            <th class="fontcenter">异常描述</th>
                                            <th class="fontcenter">问题上报时间</th>
                                            <th class="fontcenter">上报人</th>
                                            <th class="fontcenter">处理状态</th>
                                            <th class="fontcenter">责任人</th>
                                            <th class="fontcenter">关闭时间</th>
                                            <th class="fontcenter">分类</th>
                                            <th class="fontcenter">分级</th>
                                            <th class="fontcenter">异常上报状态</th>
                                            <th class="fontcenter">上报时间</th>
                                        </tr>
                                        <c:forEach items="${pageBean.list}" var="item" varStatus="status">
                                        <input type="hidden" id="img${status.index}" value='${item.img}'>
                                        <input type="hidden" id="audio${status.index}" value='${item.audio}'>
                                        <input type="hidden" id="video${status.index}" value='${item.video}'>
                                        <tr>
                                            <td>
                                                <c:if test="${item.exceptionstate != 1}">
                                                    <shiro:hasPermission name="item:closereport">
                                                        <a href="javascript:;" onclick="closetaskreport('${item.id==null?'':item.id}')">
                                                            <i class="glyphicon glyphicon-lock"></i></a>&nbsp;
                                                    </shiro:hasPermission>
                                                    <shiro:hasPermission name="item:assignprincipal">
                                                        <a href="javascript:;" onclick="assignprincipal('${item.id==null?'':item.id}')">
                                                            <i class="glyphicon glyphicon-share"></i></a>
                                                    </shiro:hasPermission>
                                                </c:if>
                                            </td>
                                            <td>${item.sitename}</td>
                                            <td class="fontcenter">${item.areaname}</td>
                                            <td class="fontcenter">${item.equipname}</td>
                                            <td class="fontcenter">${item.checkname}</td>
                                            <td class="fontcenter">
                                                <c:if test="${item.checktype == '枚举项'}">
                                                    <c:if test="${item.enumitem == ''}">-</c:if>
                                                    <c:if test="${item.enumitem != ''}">${reports.enumitem}</c:if>
                                                </c:if>
                                                <c:if test="${item.checktype == '记录项'}">
                                                    <c:if test="${item.checkvalue == '' or item.checkvalue==null}">
                                                        <c:if test="${item.numvalue!='' and item.numvalue !=null}">
                                                            <c:if test="${item.normalmin !='-' and item.normalmax!='-' and item.lowerwarning !='-' and item.upperwarning!='-'}">
                                                                <c:if test="${Double.parseDouble(item.numvalue) < Double.parseDouble(item.normalmin) and Double.parseDouble(item.numvalue) > Double.parseDouble(item.lowerwarning)}">${item.numvalue}<span style="color: red;">↓</span></c:if>
                                                                <c:if test="${Double.parseDouble(item.numvalue) > Double.parseDouble(item.normalmax) and Double.parseDouble(item.numvalue) < Double.parseDouble(item.upperwarning)}">${item.numvalue}<span style="color: red;">↑</span></c:if>
                                                                <c:if test="${ Double.parseDouble(item.normalmin)<=Double.parseDouble(item.numvalue) and Double.parseDouble(item.numvalue) <= Double.parseDouble(item.normalmax)}"><span>${item.numvalue}</span></c:if>
                                                                <c:if test="${Double.parseDouble(item.numvalue) <= Double.parseDouble(item.lowerwarning)}">${item.numvalue}<span style="color: red;">↓↓</span></c:if>
                                                                <c:if test="${Double.parseDouble(item.numvalue) >= Double.parseDouble(item.upperwarning)}">${item.numvalue}<span style="color: red;">↑↑</span></c:if>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${item.checkvalue!='' and item.checkvalue !=null}">
                                                        ${item.errcontent}
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${item.checktype == '状态项'}">
                                                        <c:if test="${item.reportstate == ''}">-</c:if>
                                                        <c:if test="${item.reportstate != ''}">
                                                            <c:if test="${item.reportstate == 1}">
                                                                <c:if test="${item.img != 'null' or item.audio != 'null' or item.video != 'null'}">
                                                                    <a href='http://${ip}/toException?img=${item.img}&audio=${item.audio}&video=${item.video}'  target="_Blank">${item.errcontent}</a>
                                                                </c:if>
                                                                <c:if test="${item.img == 'null' and item.audio == 'null' and item.video == 'null'}">
                                                                    ${item.errcontent}
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${item.reportstate == 0}">
                                                                正常
                                                            </c:if>
                                                        </c:if>
                                                </c:if>
                                            </td>
                                            <td class="fontcenter">${item.operationtime}</td>
                                            <td class="fontcenter">${item.report.worker}</td>
                                            <td class="fontcenter">
                                                <c:if test="${item.exceptionstate == 0}">待处理</c:if>
                                                <c:if test="${item.exceptionstate == 1}"><a href="javascript:;" onclick="handlerdetail('${item.id==null?'':item.id}')">已关闭</a></c:if>
                                                <c:if test="${item.exceptionstate == 2}">处理中</c:if>
                                            </td>
                                            <td class="fontcenter">${item.operatorname}</td>
                                            <td class="fontcenter"><sdf:formatDate value="${item.exceptionclosetime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
                                            <td class="fontcenter">${item.exceptiontype}</td>
                                            <td class="fontcenter">${item.exceptionlever}</td>
                                            <td class="fontcenter">
                                                <c:if test="${item.uploadstate == 0}">未上报</c:if>
                                                <c:if test="${item.uploadstate == 1}">上报成功</c:if>
                                                <c:if test="${item.uploadstate == 2}">上报失败</c:if>
                                            </td>
                                            <td class="fontcenter"><sdf:formatDate value="${item.uploadtime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
                                        </tr>
                                        </c:forEach>
                                    </table>
                                    <div style="height: 50px;width: 500px;text-align: center;margin-left: 300px;">
                                        <a href="showexceptionreport?page=1&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&exceptionstate=${exceptionstate}&exceptiontype=${exceptiontype}&exceptionlever=${exceptionlever}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">第一页</a>
                                        <c:if test="${pageBean.currentPage>1}">
                                            <a href="showexceptionreport?page=${pageBean.currentPage-1}&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&exceptionstate=${exceptionstate}&exceptiontype=${exceptiontype}&exceptionlever=${exceptionlever}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">上一页</a>
                                        </c:if>
                                        <c:if test="${pageBean.currentPage<pageBean.totalPage}">
                                            <a href="showexceptionreport?page=${pageBean.currentPage+1}&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&exceptionstate=${exceptionstate}&exceptiontype=${exceptiontype}&exceptionlever=${exceptionlever}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">下一页</a>
                                        </c:if>
                                        <a href="showexceptionreport?page=${pageBean.totalPage}&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&exceptionstate=${exceptionstate}&exceptiontype=${exceptiontype}&exceptionlever=${exceptionlever}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">最后一页</a>
                                        第${pageBean.currentPage}页/共${pageBean.totalPage}页
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div aria-hidden="false" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade in" style="z-index: 0">
    <div class="modal-dialog" style="z-index: 9999">
        <div class="modal-content" style="overflow: auto;">
            <div id="arealist">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>显示异常</h3>
                </div>
                <div class="modal-body">
                    <div id="showstop"style="height: 30%;">

                    </div>
                </div>
                <div class="modal-footer">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
