package configuration;

import java.io.File;

public class Config implements Cloneable{

	private int timeout = 10_1000;
	
	private int delay = 1000;
	
	private int maxDownloaderThreads = Runtime.getRuntime().availableProcessors() * 2;
	
	private String userAgent = UserAgent.IE_9_FOR_WIN;
	
	private int queueSize;
	
	private String filePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"downloadFile";
	public static Config newConfig() {
		return new Config();
	}
	
	public Config timeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public int timeout() {
        return this.timeout;
    }
	
	public Config delay(int delay) {
		this.delay = delay;
		return this;
	}
	
	public int delay() {
		return this.delay;
	}
	
	public Config maxDownloaderThreads(int maxDownloaderThreads) {
		this.maxDownloaderThreads = maxDownloaderThreads;
		return this;
	}
	
	public int maxDownloaderThreads() {
		return this.maxDownloaderThreads;
	}
	
	public Config userAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}
	
	public String userAgent() {
		return this.userAgent;
	}
	
	public Config queueSize(int queueSize) {
		this.queueSize = queueSize;
		return this;
	}
	
	public int queueSize() {
		return this.queueSize;
	}
	
	public String getFilePath() {
	    return this.filePath;
	}
	
	@Override
	public Config clone() {
		try {
			return (Config)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
