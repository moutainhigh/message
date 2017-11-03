package com.zhongan.icare.message.push.dao.dataobject;

public class SmsApplyPageDO extends SmsApplyDO {
    /**
     * 
     */
    private static final long serialVersionUID = 8774553201862556938L;

    private int               pageSize         = 20;

    /**
     * 起始条数
     */
    private int               startRow         = 0;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

}
