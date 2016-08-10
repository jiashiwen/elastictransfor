package elastictransfor.utilities;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchIndexTools {
	private Logger logger;
	private ImmutableOpenMap<String, MappingMetaData> mappingmetadata;
	private Map<String, String> settings;
	
	public ElasticsearchIndexTools(){
		logger = LoggerFactory.getLogger(this.getClass());		
	}

	// 获取索引setting
	public Map<String, String> GetIndexSettings(Client client, String indexname) throws UnknownHostException {
		ClusterState cs = client.admin().cluster().prepareState().setIndices(indexname).execute().actionGet()
				.getState();
		IndexMetaData imd = cs.getMetaData().index(indexname);
		settings = imd.getSettings().getAsMap();

		return settings;

	}

	// 获取索引mapping
	public ImmutableOpenMap<String, MappingMetaData> GetIndexMappings(Client client, String indexname)
			throws UnknownHostException, InterruptedException, ExecutionException {
		GetMappingsResponse res = client.admin().indices().getMappings(new GetMappingsRequest().indices(indexname))
				.get();
		mappingmetadata = res.mappings().get(indexname);
		return mappingmetadata;
	}

	// 判断索引是否存在
	public boolean IndexExistes(Client client, String indexname) {
		boolean exists = false;
		try {
			exists = client.admin().indices().prepareExists(indexname).execute().get().isExists();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return exists;
	}

	public boolean DeleteIndex(Client client, String indexname) {
		boolean isAcknowledged = false;
		try {
			isAcknowledged = client.admin().indices().prepareDelete(indexname).execute().get().isAcknowledged();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(indexname+" deleted!");
		return isAcknowledged;
	}

}