/**
 @Name：wenda首页
 @Author：庄巍
 */
layui.define(['element', 'form','laypage','jquery','upload','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,upload = layui.upload
        ,common=layui.common;
    //statr 分页
    $(function () {
        //执行实例
        var uploadInst = upload.render({
            elem: '#upload_head' //绑定元素
            ,url: '/uploadHeadPic/' //上传接口
            ,done: function(res){
                console.log(res);//上传完毕回调
                if (res.code=='0'){
                    layer.msg("上传头像成功！");
                    window.location.reload()
                }else if(res.code=='1'){
                    layer.msg("上传头像失败！");
                }else if(res.code=="-1"){
                    layer.msg("请登录后修改头像");
                }
            },error: function(){
                //请求异常回调
                layer.msg("上传失败！");
            }
        });
    });
    //输出index接口
    exports('profile', {});
});
