package elastictransfor.utilities;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * Created by Jia Shiwen.
 */

public class ElasticsearchConnector {

	private Client client;

	private String clustername;
	private String host;
	private int port;

	private PreBuiltTransportClient preBuiltTransportClient;

	public String getClustername() {
		return clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ElasticsearchConnector(String clustername, String host, int port) throws Exception {
		this.setClustername(clustername);
		this.setHost(host);
		this.setPort(port);
		initClient();

	}

	public void initClient() throws UnknownHostException {
		Settings setting = Settings.builder().put("cluster.name", clustername).build();
		preBuiltTransportClient = new PreBuiltTransportClient(setting);
		client = preBuiltTransportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
	

	}

	public Client getClient() {
		return client;
	}

}
