/**

 @Name：收藏列表界面详情
 @Author：庄巍

 */
layui.define(['element', 'form','laypage','jquery','laytpl','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common
        ,layer=layui.layer;
    //statr 分页

    $(function () {
        initCollectList();
    })
    function initCollectList(){
        var that=this;
        that.totals=$("input[name='totals']").val();
        that.curPage=1;
        that.pageSize=20;
        laypage.render({
            elem: 'collect-page',
            count: that.totals,
            curr: that.curPage,
            limit: that.pageSize,
            jump: function(obj, first){
                common.ajax("/request/followQuestionList",{
                    "limit":that.pageSize,
                    "offset":obj.curr
                },function (result) {
                    $("#collect-question-list").html("");
                    $.each(result.data,function (index, item) {
                        console.log(item)
                        var html='<li >\n' +
                            '                        <a href="/question/'+item.question.id+'" class="jie-title"> '+item.question.title+'</a>\n' +
                            '                        <i class="pull-right">'+item.browserTime+'</i>\n' +
                            '                    </li>'
                        $("#collect-question-list").append(html);
                    })
                })
            }
        });
    }
    //输出接口
    exports('follow_question_list', {});
});
