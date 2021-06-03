import java.io.*;
import java.net.*;

class OneClientModul extends Thread 
{
	LiarServer ls;
	Socket s;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;
	String chatId;

	OneClientModul(LiarServer ls){
		this.ls = ls;
		this.s = ls.s;
		try{
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		}catch(IOException ie){}
	}
	public void run(){
		listen();
	}
    void listen(){  
		String msg = "";
		try{
			chatId = dis.readUTF();
            broadcast(chatId + " ���� �����Ͽ����ϴ�. (���� ������: "+ls.v.size()+"��)");
			ls.pln(chatId + " ���� �����Ͽ����ϴ�. (���� ������: "+ls.v.size()+"��)");
			while(true){
				msg = dis.readUTF();
				broadcast(msg);
				ls.pln(msg);
			}
		}catch(IOException ie){
			ls.v.remove(this);
			broadcast(chatId + " ���� �����Ͽ����ϴ�. (�����ο�: "+ls.v.size()+"��)");
			ls.pln(chatId + " ���� �����Ͽ����ϴ�. (�����ο�: "+ls.v.size()+"��)");	
		}finally{
			closeAll();
		}
	}
	void broadcast(String msg){
		try{
			for(OneClientModul ocm: ls.v){
				ocm.dos.writeUTF(msg);
				ocm.dos.flush();
			}
		}catch(IOException ie){
		}
	}
	void cut() {
	String banId= "��";
		for(OneClientModul ocm : ls.v) {
		if(ocm.chatId.equals(banId)) {
			ls.v.remove(ocm);
			broadcast(ocm.chatId+"���� ������߽��ϴ�.");
			break;
			}
		}
	}
	
	void closeAll(){
		try{
			if(dis != null) dis.close();
			if(dos != null) dos.close();
			if(is != null) is.close();
			if(os != null) os.close();
			if(s != null) s.close();
		}catch(IOException ie){}
	}
}
