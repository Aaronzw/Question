$(document).ready(function () {

    $('#page').jqPaginator({
        totalPages: 100,
        visiblePages: 10,
        currentPage: 1,

        first: '<li class="first"><a href="javascript:void(0);">First</a></li>',
        prev: '<li class="prev"><a href="javascript:void(0);">Previous</a></li>',
        next: '<li class="next"><a href="javascript:void(0);">Next</a></li>',
        last: '<li class="last"><a href="javascript:void(0);">Last</a></li>',
        page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
        onPageChange: function (num) {
            $('#text').html('当前第' + num + '页');
        }
    });
});


