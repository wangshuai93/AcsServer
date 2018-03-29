package com.yinhe.server.AcsServer.backbean;

public class BaseBean {
	private int min = 1;
	private int first = 0;
	private int second = 0;
	private int third = 0;
	private int fourth = 0;
	private int current = 1;
	private int size = 5;    //页面总数，按照宇通要求修改
	private int count = 5;
	private boolean pageApply = true;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		setMaxByCount(count);
	}

	public int getMax() {
		if (count == 0) {
			return 1;
		}
		return count / size + (count % size == 0 ? 0 : 1);
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getThird() {
		return third;
	}

	public void setThird(int third) {
		this.third = third;
	}

	public int getFourth() {
		return fourth;
	}

	public void setFourth(int fourth) {
		this.fourth = fourth;
	}

	/**
	 * page current
	 * @return
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * page current
	 * @return
	 */public void setCurrent(int current) {
		current = current >= getMin() ? current : getMax();
		current = current <= getMax() ? current : getMin();
		this.current = current;
	}

	/**
	 * 获取分页查询的maxResult 获取分页的页面的容量
	 * 
	 * @return maxResult or pageSize
	 */

	public int getSize() {
		return size;
	}

	/**
	 * 设置页面容量，容量改变是改变查询页码为1 同时更新max的值
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		if (this.size != size) {
			size = size == 0 ? 10 : size;
			size = size < 0 ? -size : size;
			this.size = size;
			this.setCurrent(1);
		}

	}

	/**
	 * 获取分页查询起始位置
	 * 
	 * @return
	 */
	public int getStartPosition() {
		return current * size - size;
	}

	/**
	 * 设置current为前一个页面
	 */
	public void previous() {
		this.setCurrent(current - 1);
	}

	/**
	 * 设置current为下一个页面
	 */
	public void next() {
		this.setCurrent(current + 1);
	}

	/**
	 * 通过传入表单的记录的条数来设置页面的最大数量 同时设置当前页码，防止页码因为最大页码改变而越界
	 * 
	 * @param count
	 */
	public void setMaxByCount(int count) {
		this.count = count;
		setCurrent(current);
	}

	public boolean isPageApply() {
		return pageApply;
	}

	public void setPageApply(boolean pageApply) {
		this.pageApply = pageApply;
	}
}
