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

public class ElasticsearchGetMetaData {
	
	public Map<String, String> GetIndexSettings(Client client, String indexname) throws UnknownHostException {
		ClusterState cs = client.admin().cluster().prepareState().setIndices(indexname).execute().actionGet()
				.getState();
		IndexMetaData imd = cs.getMetaData().index(indexname);
		return imd.getSettings().getAsMap();
	}

	public ImmutableOpenMap<String, MappingMetaData> GetIndexMappings(Client client, String indexname) {
		ImmutableOpenMap<String, MappingMetaData> mappingmetadata = null;
		try {
			GetMappingsResponse res = client.admin().indices().getMappings(new GetMappingsRequest().indices(indexname))
					.get();

			mappingmetadata = res.mappings().get(indexname);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mappingmetadata;

	}

}