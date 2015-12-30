package com.nsc.dem.util.xml;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.sun.org.apache.xml.internal.security.utils.XMLUtils;

/**
 * 实现ping命令
 * @author Administrator
 *
 */
public class Ping {
	
	static class Target {
		InetSocketAddress address;
		SocketChannel channel;
		Exception failure;
		long connectStart;
		long connectFinish = 0;

		Target(String host,int port) {
			try {
				address = new InetSocketAddress(InetAddress.getByName(host),
						port);
			} catch (IOException x) {
				failure = x;
			}
		}
		
		String show() {
			String result;
			if (connectFinish != 0)
				result = Long.toString(connectFinish - connectStart);//ms
			else if (failure != null)
				result = "-2";//连接失败
			else
				result = "-1"; //连接超时
			return result;
		}
		
	}

	static class Printer extends Thread {
		LinkedList pending = new LinkedList();
		Printer() {
			setName("Printer");
			setDaemon(true);
		}
		void add(Target t) {
			synchronized (pending) {
				pending.add(t);
				pending.notify();
			}
		}
		public void run() {
			try {
				for (;;) {
					Target t = null;
					synchronized (pending) {
						while (pending.size() == 0)
							pending.wait();
						t = (Target) pending.removeFirst();
					}
					t.show();
				}
			} catch (InterruptedException x) {
				return;
			}
		}
	}
	static class Connector extends Thread {
		Selector sel;
		Printer printer;
		LinkedList pending = new LinkedList();
		Connector(Printer pr) throws IOException {
			printer = pr;
			sel = Selector.open();
			setName("Connector");
		}

		void add(Target t) {
			SocketChannel sc = null;
			try {
				sc = SocketChannel.open();
				sc.configureBlocking(false);
				boolean connected = sc.connect(t.address);
				t.channel = sc;
				t.connectStart = System.currentTimeMillis();
				if (connected) {
					t.connectFinish = t.connectStart;
					sc.close();
					printer.add(t);
				} else {
					synchronized (pending) {
						pending.add(t);
					}
					sel.wakeup();
				}
			} catch (IOException x) {
				if (sc != null) {
					try {
						sc.close();
					} catch (IOException xx) {
					}
				}
				t.failure = x;
				printer.add(t);
			}
		}

		void processPendingTargets() throws IOException {
			synchronized (pending) {
				while (pending.size() > 0) {
					Target t = (Target) pending.removeFirst();
					try {
						t.channel.register(sel, SelectionKey.OP_CONNECT, t);
					} catch (IOException x) {
						t.channel.close();
						t.failure = x;
						printer.add(t);
					}
				}
			}
		}

		void processSelectedKeys() throws IOException {
			for (Iterator i = sel.selectedKeys().iterator(); i.hasNext();) {
				SelectionKey sk = (SelectionKey) i.next();
				i.remove();
				Target t = (Target) sk.attachment();
				SocketChannel sc = (SocketChannel) sk.channel();
				try {
					if (sc.finishConnect()) {
						sk.cancel();
						t.connectFinish = System.currentTimeMillis();
						sc.close();
						printer.add(t);
					}
				} catch (IOException x) {
					sc.close();
					t.failure = x;
					printer.add(t);
				}
			}
		}

		volatile boolean shutdown = false;

		void shutdown() {
			shutdown = true;
			sel.wakeup();
		}

		public void run() {
			for (;;) {
				try {
					int n = sel.select();
					if (n > 0)
						processSelectedKeys();
					processPendingTargets();
					if (shutdown) {
						sel.close();
						return;
					}
				} catch (IOException x) {
					Logger.getLogger(Ping.class).warn(x);
				}
			}
		}
	}

	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @return -1：连接超时，-2：其他问题
	 */
	public static String getTime(String ip, int port){
		if(null == ip || "".equals(ip)){
			return "-2";
		}
		try {
			
			Printer printer = new Printer();
			printer.start();
			Connector connector = new Connector(printer);
			connector.start();
			Target t = new Target(ip,port);
			connector.add(t);
			Thread.sleep(2000);
			connector.shutdown();
			connector.join();
			return t.show();
		} catch (Exception e) {
			Logger.getLogger(Ping.class).warn(e);
		} 
		return "-2";
	}
	
	
}