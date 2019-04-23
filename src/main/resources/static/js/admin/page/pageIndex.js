/**

 @Name：管理员界面首页
 @Author：庄巍
 */
layui.define(['element', 'form','laypage','jquery','laytpl'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl

        ,layer=layui.layer;

    $(function () {
        initList();
    });
    function initList(){
        var that=this;
        that.cur=1;
        that.pageSize=5;
        that.totals=16;

        laypage.render({
            elem: 'page',
            count: that.totals,
            curr: that.cur,
            limit: that.pageSize,
            jump: function(obj, first){

            }
        });
    }

    //输出answer_接口
    exports('pageIndex', {});
});
