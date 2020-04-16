爬虫流程：
	爬虫任务 -> 爬虫框架 -> 框架引擎 -> 启动爬虫进程 -> 
	爬虫进程加载配置 -> 分析爬虫任务URLs -> URLS转为Request对象 -> 
	通过网页下载器得到Responses对象 -> 将Responses对象加入至 webpageResult对象集合中 -> 
	分析处理webpageResult对象集合
	
框架组件：	
	configuration:爬虫相关配置用户代理配置
	event: 事件驱动
	framework: 爬虫框架
	processorQueue: 爬虫处理队列，用来存放Request对象的队列和Response对象的队列
	request: Request对象相关
	response: Response对象相关
	spider:爬虫任务
	utils:爬虫工具类
	webpageDownloader: 用Request对象得到Response对象
	webpageResult: 处理Response对象解析的结果