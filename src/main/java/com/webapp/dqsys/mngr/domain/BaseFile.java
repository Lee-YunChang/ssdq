package com.webapp.dqsys.mngr.domain;

import java.io.Serializable;

public class BaseFile implements Serializable{
	/**넘겨주는  html tag name */
	private String htmlName="";
    /** 파일명 */
    private String fileName = "";
    /** ContextType */
    private String contentType = "";
    /** 하위 디렉토리 지정 */
    private String serverSubPath = "";
    /** 물리적 파일명 */
    private String physicalName = "";
    /** 파일 사이즈 */
    private long size = 0L;
    
    /**
     * fileName attribute를 리턴한다.
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * fileName attribute 값을 설정한다.
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * contentType attribute를 리턴한다.
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }
    /**
     * contentType attribute 값을 설정한다.
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    /**
     * serverSubPath attribute를 리턴한다.
     * @return the serverSubPath
     */
    public String getServerSubPath() {
        return serverSubPath;
    }
    /**
     * serverSubPath attribute 값을 설정한다.
     * @param serverSubPath the serverSubPath to set
     */
    public void setServerSubPath(String serverSubPath) {
        this.serverSubPath = serverSubPath;
    }
    /**
     * physicalName attribute를 리턴한다.
     * @return the physicalName
     */
    public String getPhysicalName() {
        return physicalName;
    }
    /**
     * physicalName attribute 값을 설정한다.
     * @param physicalName the physicalName to set
     */
    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }
    /**
     * size attribute를 리턴한다.
     * @return the size
     */
    public long getSize() {
        return size;
    }
    /**
     * size attribute 값을 설정한다.
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }
	public String getHtmlName() {
		return htmlName;
	}
	public void setHtmlName(String htmlName) {
		this.htmlName = htmlName;
	}
}
