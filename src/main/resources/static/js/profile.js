/**

 @Name：wenda首页
 @Author：庄巍

 */
layui.define(['element', 'form','laypage','jquery','laytpl','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common;
    //statr 分页

    $(function () {
        laypage.render({
            elem: 'page-pro-ques',
            count: 100,
            curr: 1,
            limit: 6,
            jump: function(obj, first){
                console.log(obj.curr+""+obj.limit);
            }
        });
        laypage.render({
            elem: 'page-pro-ans',
            count: 100,
            curr: 1,
            limit: 6,
            jump: function(obj, first){
                console.log(obj.curr+""+obj.limit);
            }
        });
    })



    //输出index接口
    exports('profile', {});
});
