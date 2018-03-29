package com.yinhe.server.AcsServer.tcpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.yinhe.server.AcsServer.enums.DownloadEnum;
import com.yinhe.server.AcsServer.util.Resources;


public class TcpClient implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7639723509403511430L;
	private static final String UPLOAD_FILE_TMP = System.getProperty("jboss.home.dir") + "/welcome-content/record";
	private static final int DEFAULT_SERVER_PORT = 9000;
	private static final String CLIENT_FILE_DOWNLOAD = "download";
	private static DownloadEnum downloadResult;
	
	//add
	private static Long totalSize;
	private static Integer current;
	private static java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");//格式化数字

	public synchronized void download(String host, String path, String fileName) {
        System.out.println("TcpClient, start to download file!" + "host = " + host + ",path = " + path + ",fileName = " + fileName);
        Thread downloadThread = new DownloadFileHandler(host, path, fileName);
        
        try {
        	downloadThread.start();    //从机顶盒tcp_server下载文件，启动该操作的线程
			downloadThread.join();
			System.out.println("TcpClient, download file finished!");
			
		} catch (InterruptedException e) {
			downloadResult = DownloadEnum.DOWNLOAD_FAILED;
			System.out.println("TcpClient, download error !" + e.toString());
		}
	}

	private static class DownloadFileHandler extends Thread {
        private Socket client;
        private String fileName;
        private String server_host;
        private String path;

        public DownloadFileHandler(String host, String path, String fileName) {
            	this.server_host = host;
                this.fileName = fileName;
                this.path = path;
        }

        @Override
        public void run() {
        	//建立连接
        	try {
                client = new Socket();
                SocketAddress address = new InetSocketAddress(server_host, TcpClient.DEFAULT_SERVER_PORT);
                client.connect(address, 3000);
            } catch (IOException e) {
            	downloadResult = DownloadEnum.DOWNLOAD_CONNECT_FAILED;
                System.out.println("DownloadFileHandler, new tcpClient error!" + e.toString());
                return;
            }
        	
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            BufferedOutputStream sbos = null;

            byte[] buf = new byte[1024];
            int len = -1;
            try {
                File file = null;
                File tmpDirectory = new File(TcpClient.UPLOAD_FILE_TMP);
                if (!tmpDirectory.exists()) {   // 新建暂存目录
                    tmpDirectory.mkdirs();
                }
                file = new File(tmpDirectory, fileName);
                            
                
                bis = new BufferedInputStream(client.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(file));   //将数据写到服务器指定路径中的输出流
                sbos = new BufferedOutputStream(client.getOutputStream());
                
                String uploadstring = path;
                //String uploadstring = TcpClient.CLIENT_FILE_DOWNLOAD + "," + fileName;
                byte[] uploadbyte = uploadstring.getBytes();
                sbos.write(uploadbyte, 0, uploadbyte.length);
                sbos.flush();
                
                //文件大小
                len = bis.read(buf);
                int i = 0;
                for (; i < buf.length ; i++) {
                	if (buf[i] == '\0') {
                		break;
                	}
                }
                System.out.println("i = " + i);
                System.out.println("len = " + len);
                String result = new String(buf, 0, i);
                String file_length = new String(result.getBytes("ISO-8859-1"),"UTF-8");
                if (!Resources.isNullOrEmpty(result)) {
                	 System.out.println("DownloadFileHandler,file_length = " + file_length);
                	 try {
                 		 totalSize = Long.parseLong(file_length);
                 	 } catch (Exception e) {
                 		 downloadResult = DownloadEnum.DOWNLOAD_FAILED;
                 	 	 System.out.println("DownloadFileHandler, get file length error!" + e.toString());
                 		 return ;
                 	 }	
                } else {
                	 System.out.println("DownloadFileHandler,file length == null ");
                	 downloadResult = DownloadEnum.DOWNLOAD_FAILED;
             		 return ;
                }
                
                current = 0;
                int downloadSize = 0;
                while ((len = bis.read(buf)) > 0) {
                	downloadSize += len;
                    bos.write(buf, 0, len);
                    bos.flush();
                    
                    String temp = df.format((int)(((float)downloadSize/totalSize)*100));
                    temp = temp.substring(0, temp.indexOf("."));
                    if (!Resources.isNullOrEmpty(temp)) {
                    	current = Integer.parseInt(temp);
                    }
                }
                
                downloadResult = DownloadEnum.DOWNLOAD_SUCCESS;
            } catch (IOException e) {
            	downloadResult = DownloadEnum.DOWNLOAD_FAILED;
            	System.out.println("DownloadFileHandler, download error!" + e.toString());
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

	public DownloadEnum getDownloadResult() {
		return downloadResult;
	}

	@SuppressWarnings("static-access")
	public  void setDownloadResult(DownloadEnum downloadResult) {
		this.downloadResult = downloadResult;
	}

	public Integer getCurrent() {
		return current;
	}

	@SuppressWarnings("static-access")
	public void setCurrent(Integer current) {
		this.current = current;
	}
}
