package elastictransfor.utilities;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;

public class ElasticsearchCopyIndex {

	private Logger logger;

	private long count = 0;

	public ElasticsearchCopyIndex() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public void CopyIndexMetadata(Client sourceclient, String sourceindex, Client targetclient, String targetindex)
			throws UnknownHostException {
		// TODO Auto-generated method stub
		// 获取source settings
		Settings.Builder settingbuilder = Settings.builder();
		ClusterState cs = sourceclient.admin().cluster().prepareState().setIndices(sourceindex).execute().actionGet()
				.getState();
		IndexMetaData imd = cs.getMetaData().index(sourceindex);
		Settings settings = imd.getSettings();

		for (Map.Entry<String, String> m : settings.getAsMap().entrySet()) {

			if (m.getKey().equals("index.uuid") || m.getKey().equals("index.version.created")
					|| m.getKey().equals("index.creation_date")) {
				continue;
			} else {
				settingbuilder.put(m.getKey(), m.getValue());
			}
		}

		// 创建索引
		targetclient.admin().indices().prepareCreate(targetindex).setSettings(settingbuilder).get();

		// 获取source mappings并添加到target
		try {
			GetMappingsResponse res = sourceclient.admin().indices()
					.getMappings(new GetMappingsRequest().indices(sourceindex)).get();
			ImmutableOpenMap<String, MappingMetaData> mapping = res.mappings().get(sourceindex);
			for (ObjectObjectCursor<String, MappingMetaData> c : mapping) {
				targetclient.admin().indices().preparePutMapping(targetindex).setType(c.key)
						.setSource(c.value.getSourceAsMap()).get();
			}
		} catch (InterruptedException | ExecutionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Target index " + targetindex + " create complete!");

	}

	public void CopyIndex(Client sourceclient, String sourceindex, Client targetclient, String targetindex) {
		// TODO Auto-generated method stub
		count = 0;
		QueryBuilder qb = QueryBuilders.matchAllQuery();

		try {

			SearchResponse scrollResp = sourceclient.prepareSearch(sourceindex)
					.addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC).setScroll(new TimeValue(60000))
					.setQuery(qb).setSize(500).execute().actionGet();

			while (true) {
				final BulkRequestBuilder bulkRequest = targetclient.prepareBulk();
				for (SearchHit hit : scrollResp.getHits().getHits()) {
					bulkRequest.add(targetclient.prepareIndex(targetindex, hit.getType().toString(), hit.getId())
							.setSource(hit.getSourceAsString(),XContentType.JSON));
					count++;
				}

				bulkRequest.execute();
				scrollResp = sourceclient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000))
						.execute().actionGet();

				logger.info(count + " documents putted!!");

				if (scrollResp.getHits().getHits().length == 0) {
					break;
				}
			}

			logger.info("copy index " + sourceindex + " " + count + " documents" + " to " + targetindex + " complete");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CopyIndexByQueryDsl(Client sourceclient, String sourceindex, Client targetclient, String targetindex,
			String DSL) {
		count = 0;
		// 单线程scroll，bulk写入
		WrapperQueryBuilder wrapper = new WrapperQueryBuilder(DSL);

		try {
			SearchResponse scrollResp = sourceclient.prepareSearch(sourceindex)
					.addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC).setScroll(new TimeValue(60000))
					.setQuery(wrapper).setSize(500).execute().actionGet();

			while (true) {
				final BulkRequestBuilder bulkRequest = targetclient.prepareBulk();
				for (SearchHit hit : scrollResp.getHits().getHits()) {
					bulkRequest.add(targetclient.prepareIndex(targetindex, hit.getType().toString(), hit.getId())
							.setSource(hit.getSourceAsString(),XContentType.JSON));
					count++;
				}

				bulkRequest.execute();

				scrollResp = sourceclient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000))
						.execute().actionGet();
				logger.info(count + " documents putted!!");

				if (scrollResp.getHits().getHits().length == 0) {
					break;
				}
			}
			
			logger.info("copy index " + sourceindex + " " + count + " documents" + " to " + targetindex + " complete");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
