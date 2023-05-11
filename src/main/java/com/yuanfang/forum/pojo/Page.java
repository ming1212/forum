package com.yuanfang.forum.pojo;

public class Page {

    private int currentPage = 1;  //当前页码

    private int limit = 10;  //每页显示的帖子的数量

    private int rows;  //总的帖子的数量，用于计算总的页码

    private String path; //页码的路径（链接），便于路径的复用

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage >= 1){  //当前页码必须是大于1的
            this.currentPage = currentPage;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100){  //每页显示的数量不能少于一条，也尽量不要多于100条，不然查询的压力很大并且大量的数据给浏览器会导致浏览器卡死
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset(){
        return (currentPage - 1) * limit;
    }

    /**
     * 获取总的页数
     * @return
     */
    public int getPageTotal(){
        if(rows % limit == 0){
            return rows / limit;
        }else{
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     * @return
     */
    public int from(){
        int from = currentPage - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取终止页码
     * @return
     */
    public int to(){
        int to = currentPage + 2;
        int total = getPageTotal();
        return to > total ? total : to;
    }
}
