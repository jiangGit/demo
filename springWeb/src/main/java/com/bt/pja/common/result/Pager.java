package com.bt.pja.common.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenjiapeng on 2015/6/30 0030.
 */
public class Pager<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int offset;
    private int limit;
    private int totalCount;
    private List<T> datas;
    public Pager() {
	}
    public Pager(int offset, int limit, int totalCount, List<T> datas) {
        this.offset = offset;
        this.limit = limit;
        this.totalCount = totalCount;
        this.datas = datas;
    }

	public static <T> Pager<T> fromPager(Pager<?> pager, List<T> datas){
		if (pager == null) {
			return null;
		}

		return new Pager<>(pager.getOffset(), pager.getLimit(), pager.getTotalCount(), datas);
	}

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotalPage() {
	    if (limit == 0) {
		    return 0;
	    }
        return totalCount / limit + (totalCount % limit > 0 ? 1 : 0);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<T> getDatas() {
        return datas;
    }
}
