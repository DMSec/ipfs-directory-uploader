package br.com.dmsec.idup.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ipfs")
public class IpfsProperties {
    private String multiAddr;
    private String port;
    private String host;
    
	public String getMultiAddr() {
		return multiAddr;
	}
	public void setMultiAddr(String multiAddr) {
		this.multiAddr = multiAddr;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

    
}
