package elastictransfor.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ScriptEntity {
	@JsonProperty("source_cluster")
	private String source_cluster = "elasticsearch";
	@JsonProperty("source_host")
	private String source_host = "127.0.0.1";
	@JsonProperty("source_port")
	private int source_port = 9300;
	@JsonProperty("target_cluster")
	private String target_cluster = "elasticsearch";
	@JsonProperty("target_host")
	private String target_host = "127.0.0.1";
	@JsonProperty("target_port")
	private int target_port = 9300;
	@JsonProperty("indexes")
	private List<IndexesRelationEntity> indexesrelation;
	
	public String getSource_cluster() {
		return source_cluster;
	}
	public void setSource_cluster(String source_cluster) {
		this.source_cluster = source_cluster;
	}
	public String getSource_host() {
		return source_host;
	}
	public void setSource_host(String source_host) {
		this.source_host = source_host;
	}
	public int getSource_port() {
		return source_port;
	}
	public void setSource_port(int source_port) {
		this.source_port = source_port;
	}
	public String getTarget_cluster() {
		return target_cluster;
	}
	public void setTarget_cluster(String target_cluster) {
		this.target_cluster = target_cluster;
	}
	public String getTarget_host() {
		return target_host;
	}
	public void setTarget_host(String target_host) {
		this.target_host = target_host;
	}
	public int getTarget_port() {
		return target_port;
	}
	public void setTarget_port(int target_port) {
		this.target_port = target_port;
	}
	public List<IndexesRelationEntity> getIndexesrelation() {
		return indexesrelation;
	}
	public void setIndexesrelation(List<IndexesRelationEntity> indexesrelation) {
		this.indexesrelation = indexesrelation;
	}

}
