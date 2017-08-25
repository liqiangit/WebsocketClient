import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

public class WebsocketPushClient extends WebSocketClient {
	FileWriter fw;
	BufferedWriter bw;
	public WebsocketPushClient( URI serverUri , Draft draft ) {
		super( serverUri, draft );
		try {
			fw = new FileWriter("E:/websocket/c1.txt");
			bw= new BufferedWriter(fw);      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
	}

	public WebsocketPushClient( URI serverURI ) {
		super( serverURI );
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		System.out.println( "opened connection" );
		// if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
		this.send("1000");
	}

	@Override
	public void onMessage( String message ) {
		try {
			bw.write(new SimpleDateFormat("yyyyMMddHHmmss.SSS").format(new Date()));      
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+ "received: " + message );
	}

	@Override
	public void onFragment( Framedata fragment ) {
		System.out.println( "received fragment: " + new String( fragment.getPayloadData().array() ) );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
		
		this.close();
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	public static void main( String[] args ) throws URISyntaxException { 
		WebsocketPushClient client = new WebsocketPushClient( new URI( "ws://192.168.50.133:8080/webSocketServer" ), new Draft_6455() ); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
		
		long ticketCon = 0;
		while(true) {
			
			if (System.currentTimeMillis() - ticketCon > 3000) {
				ticketCon = System.currentTimeMillis();
				
				if (! client.isOpen())
					client.connect();
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println("main end");
	}

}