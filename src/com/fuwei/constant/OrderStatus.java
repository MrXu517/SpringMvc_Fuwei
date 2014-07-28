package com.fuwei.constant;

public enum OrderStatus {
	/*已创建*/
	CREATE {public String getName(){return "已创建";}},
	/*待确认样确认*/
	CONFIRMSAMPLE {public String getName(){return "待确认样确认";}},
	/*待产前样确认*/
	PRODUCESAMPLE{public String getName(){return "待产前样确认";}},
	/*已生成生产通知单*/
	PRODUCING{public String getName(){return "已生成生产通知单";}},
	/*在染色*/
	COLORING{public String getName(){return "在染色";}},
	/*在备纱*/
	YARNING{public String getName(){return "在备纱";}},
	/*在机织*/
	MACHINING{public String getName(){return "在机织";}},
	/*完成生产*/
	COMPLETE_PRODUCING{public String getName(){return "已完成生产，待发货";}},
	/*已发货*/
	DELIVERED{public String getName(){return "已发货";}},
	/* 已收货 */
    RECEIVED {public String getName(){return "对方已收货";}},
    /* 交易已完成 */
    COMPLETED {public String getName(){return "交易已完成";}},
	 /* 已取消 */
    CANCEL {public String getName(){return "已取消";}};
   
    public abstract String getName();
}
