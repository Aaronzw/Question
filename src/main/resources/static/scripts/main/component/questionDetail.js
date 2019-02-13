$(document).ready(function () {

    $("#paginate").pagination({
        pageSize: 10,
        total: 1000,
        pageIndex:1,
    });
    $("#page").on("pageClicked", function (event, data) {
        //分页按钮点击事件
        console.log(data)
    });
});


