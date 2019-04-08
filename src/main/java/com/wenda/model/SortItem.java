package com.wenda.model;

public class SortItem<T> implements Comparable<SortItem> {
    private T item;//比较的对象
    private int sortNum;//用于比较的数值
    private long sortSecond;//通常导入时间

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortItem(int sortNum) {
        this.sortNum = sortNum;
    }
    public long getSortSecond() {
        return sortSecond;
    }

    public void setSortSecond(long sortSecond) {
        this.sortSecond = sortSecond;
    }
    @Override
    /*重载排序，默认降序*/
    public int compareTo(SortItem s) {
        // TODO Auto-generated method stub
        if(s.sortNum-this.sortNum!=0)
            return s.sortNum-this.sortNum;
        else {
            return (s.sortSecond-this.sortSecond)>0?1:-1;
        }
    }

}
