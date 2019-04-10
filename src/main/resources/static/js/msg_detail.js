/**

 @Name：站内信详情
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
        var that=this;
        that.toUserId=$("input[name='toUserId']").val();
        that.conversationId=$("input[name='conversationId']").val();
        if(that.toUserId==undefined||that.conversationId==undefined){
            layer.msg("加载出错，请重新登录");
            return
        }
        laypage.render({
            elem: 'msg-detail-page',
            count: 1,
            curr: 1,
            limit: 10,
            jump: function(obj, first){
                console.log(obj)
            }
        });
    })


    //输出msg_list接口
    exports('msg_detail', {});
});
