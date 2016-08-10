package elastictransfor.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexesRelationEntity {
	@JsonProperty("source_index")
	private String source_index;	
	@JsonProperty("target_index")
	private String target_index;
	@JsonProperty("dsl")
	private Object dsl;
	public Object getDsl() {
		return dsl;
	}
	public void setDsl(Object dsl) {
		this.dsl = dsl;
	}
	@JsonProperty("type")
	private String type="meta";
	
	
	public String getSource_index() {
		return source_index;
	}
	public void setSource_index(String source_index) {
		this.source_index = source_index;
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
