package elastictransfor.utilities;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by Jia Shiwen.
 */

public class ElasticsearchConnector {

	private Client client;

	private String clustername;
	private String host;
	private int port;

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
		Settings setting = Settings.settingsBuilder().put("cluster.name", clustername).build();
		client = TransportClient.builder().settings(setting).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
	}

	public Client getClient() {
		return client;
	}

}
