import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.surftools.BeanstalkClient.Client;
import com.surftools.BeanstalkClientImpl.ClientImpl;

public class Producer {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Producer started");
		
		Client client = new ClientImpl();
		client.useTube("demo");
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			for(;;) {
				System.out.print("> ");
				System.out.flush();
				
				String command = br.readLine().trim();
				
				if(command.equalsIgnoreCase("exit")) {
					break;
				}
				
				if(command.toLowerCase().startsWith("put")) {
					String message = command.substring(4).trim();
					
					client.put(0, 0, 1, message.getBytes("utf-8"));
					
					System.out.println("put message: '" + message + "'");
					continue;
				}
				
				System.out.println("unknown command: '" + command + "'");
			}
		}
		
		System.out.println("Producer terminated");
	}
}