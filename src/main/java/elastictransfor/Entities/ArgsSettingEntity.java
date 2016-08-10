package elastictransfor.Entities;

import com.beust.jcommander.Parameter;

public class ArgsSettingEntity {

	@Parameter(names = "--help", help = true)
	private boolean help = false;

	@Parameter(names = "--source_cluster", description = "Source elasticsearch cluster name,default is 'elasticsearch")
	private String source_cluster = "elasticsearch";

	@Parameter(names = "--source_host", description = "Source elasticsearch cluster one of master ip address,default is '127.0.0.1'.")
	private String source_host = "127.0.0.1";

	@Parameter(names = "--source_port", description = "Source port")
	private int source_port = 9300;

	@Parameter(names = "--source_index", required = false, description = "Source index name")
	private String source_index;

	@Parameter(names = "--target_cluster", description = "Target elasticsearch cluster name,default is 'elasticsearch")
	private String target_cluster = "elasticsearch";

	@Parameter(names = "--target_host", description = "Target elasticsearch cluster one of master ip address,default is '127.0.0.1'.")
	private String target_host = "127.0.0.1";

	@Parameter(names = "--target_port", description = "Target port")
	private int target_port = 9300;

	@Parameter(names = "--target_index", required = false, description = "Target index name")
	private String target_index;

	@Parameter(names = "--type", description = "Transfor type value is [data,meta,force] and default value is 'meta'.If value is 'metadata' try to create a new empty target index as source;'data' copy source index documents to target index; 'force' delete target index if exists and copy source index to target index.")
	private String type = "meta";

	@Parameter(names = "--dsl", description = "elasticsearch query dsl for Preform a partial transfor based on search results.you must make content of this variable between '',just like  '{\"query\":{\"term\":{\"word.primitive\":{\"value\":\"keywork\"}}}}' ")
	private String dsl;

	@Parameter(names = "--script_file", description = "execute script file write by json ")
	private String script_file;

	public String getScript_file() {
		return script_file;
	}

	public void setScript_file(String script_file) {
		this.script_file = script_file;
	}

	public String getDsl() {
		return dsl;
	}

	public void setDsl(String dsl) {
		this.dsl = dsl;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}

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

	public String getSource_index() {
		return source_index;
	}

	public void setSource_index(String source_index) {
		this.source_index = source_index;
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

	public String getTarget_index() {
		return target_index;
	}

	public void setTarget_index(String target_index) {
		this.target_index = target_index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
