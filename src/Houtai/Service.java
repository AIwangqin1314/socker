package Houtai;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
 
/** 
 * @author ĳ��: 
 * @version ����ʱ�䣺2015��8��17�� ����3:04:14 
 * ��˵�� 
 */
public class Service {
    private static final ThreadLocal<Socket> threadConnect = new ThreadLocal<Socket>(); 
    
    private static final String HOST = "localhost";
 
    private static final int PORT = 8888;
    
    private static Socket client;
    
    private static OutputStream outStr = null;
    
    private static InputStream inStr = null;
    
    private static Thread tRecv = new Thread(new RecvThread());
    private static Thread tKeep = new Thread(new KeepThread());
    public  boolean flg_timer=false;
   
 
    public static void connect() throws UnknownHostException, IOException  {
        client = threadConnect.get();
        if(client == null){
            client = new Socket(HOST, PORT);
            threadConnect.set(client);
            tKeep.start();
            System.out.println("========���ӿ�ʼ��========");
        }
        outStr = client.getOutputStream();
        inStr = client.getInputStream();
        tRecv.start();
    }
    
    public static void disconnect() {
        try {
            outStr.close();
            inStr.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
    private static class KeepThread implements Runnable {
        public boolean flg_timer=false;
		
		public void run() {
            try {
                System.out.println("=====================��ʼ����������==============");
                while (true) {
                    try {
                    	if(flg_timer) {
                        Thread.sleep(10000);
                        System.out.println("�����������ݰ�10");
                        flg_timer=false;
                        }else
                        {
                        	Thread.sleep(3000);
                        	
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                   int mad=client.getPort();
                   
                    outStr.write("w01".getBytes());
                    //outStr.write(("duankou"+mad).getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
		@SuppressWarnings("unused")
		public void setName(String name) 
		{ 
		this.flg_timer=true; 
		System.out.println("flg_timer id true");
		} 
    }
    
    private static class RecvThread implements Runnable {
        public void run() {
            try {
                System.out.println("==============��ʼ��������===============");
                while (true) {
                    byte[] b = new byte[1024];
                    int r = inStr.read(b);
                    if(r>-1){
                        String str = new String(b,0,r);
                        //System.out.println(str);
                        //System.out.println(str.equals("QuitClient")+"000"+str.length()+"QuitClient".length());
                        if (str.equals("QuitClient")){   //服务器要求客户端结束
                        	 //tKeep.setName("ss");
                        	 //System.out.println("dda���");
                        	// System.out.println("dda���==============��ʼ��������===============");
                    }
                     
                       // System.out.println( str );
                }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
 
        }
    }
    
}
