package elastictransfor;

import java.util.List;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import elastictransfor.Entities.ArgsSettingEntity;
import elastictransfor.Entities.IndexesRelationEntity;
import elastictransfor.Entities.ScriptEntity;
import elastictransfor.utilities.ElasticsearchConnector;
import elastictransfor.utilities.ElasticsearchCopyIndex;
import elastictransfor.utilities.ElasticsearchIndexTools;
import elastictransfor.utilities.JsonUtilities;

public class ElasticTransforMain {

	private static final Logger logger = LoggerFactory.getLogger(ElasticTransforMain.class);

	public static void main(String[] args) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ArgsSettingEntity argssetting = new ArgsSettingEntity();
		ElasticsearchCopyIndex cpidx = new ElasticsearchCopyIndex();
		ElasticsearchIndexTools esidxtools = new ElasticsearchIndexTools();
		Client sourceclient;
		Client targetclient;

		JCommander jc = new JCommander();
		jc.setProgramName("java -jar elastictransfor.jar");
		jc.addObject(argssetting);
		
		/*
		 * 解析输入参数
		 */
		try {
			jc.parse(args);
		} catch (Exception e) {
			jc.usage();
			System.exit(0);
		}

		if (argssetting.isHelp()) {
			jc.usage();
			System.exit(0);
		}

		//解析脚本文件并执行相关操作
		if (argssetting.getScript_file() != null) {
			String scriptstring = new JsonUtilities().ReadJsonFile(argssetting.getScript_file());
			ScriptEntity script=(ScriptEntity) objectMapper.readValue(scriptstring, ScriptEntity.class);
			String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(script);
			logger.info("Your Script setting is: \n\t" + json);
			JsonNode node= objectMapper.readTree(scriptstring);
						
			if(node.get("indexes")==null){				
				logger.info("Script 'indexes' must be set!");
				return;				
			}
			
			List<IndexesRelationEntity> indexrelation=script.getIndexesrelation();
			
			for(IndexesRelationEntity idx:indexrelation){
				sourceclient = new ElasticsearchConnector(script.getSource_cluster(), script.getSource_host(),
						script.getSource_port()).getClient();
				targetclient = new ElasticsearchConnector(script.getTarget_cluster(), script.getTarget_host(),
						script.getTarget_port()).getClient();
				String idxjson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(idx);
				logger.info("\n\t" + idxjson);
				if (idx.getSource_index() == null || idx.getTarget_index() == null) {
					logger.info("In script 'indexes.source_index' and 'indexes.target_index' must been set!");	
					continue;
				}
				
				switch (idx.getType().toUpperCase()) {
				case "DATA":
					System.out.println("TYPE IS DATA OK!");
					if (idx.getDsl() != null) {
						cpidx.CopyIndexByQueryDsl(sourceclient, idx.getSource_index(), targetclient,
								idx.getTarget_index(), idx.getDsl().toString());

					} else {
						cpidx.CopyIndex(sourceclient, idx.getSource_index(), targetclient,
								idx.getTarget_index());
					}
					break;
				case "META":
					System.out.println("TYPE IS META OK!");
					cpidx.CopyIndexMetadata(sourceclient, idx.getSource_index(), targetclient,
							idx.getTarget_index());
					break;
				case "FORCE":
					System.out.println("FORCE OK!");
					if (esidxtools.IndexExistes(targetclient, idx.getTarget_index())) {
						esidxtools.DeleteIndex(targetclient, idx.getTarget_index());
					}
								
					cpidx.CopyIndexMetadata(sourceclient, idx.getSource_index(), targetclient,
							idx.getTarget_index());

					if (idx.getDsl() != null) {
						cpidx.CopyIndexByQueryDsl(sourceclient, idx.getSource_index(), targetclient,
								idx.getTarget_index(), idx.getDsl().toString());

					} else {
						cpidx.CopyIndex(sourceclient, idx.getSource_index(), targetclient,
								idx.getTarget_index());
					}
					break;
				default:
					System.out.println("type must be set [data,meta,force]");
					break;
				}

				sourceclient.close();
				targetclient.close();
								
			}
			
			System.exit(0);
		}

		if (argssetting.getSource_index() == null || argssetting.getTarget_index() == null) {
			logger.info("index name must been set!");
			jc.usage();
			System.exit(0);
		}

		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(argssetting);
		logger.info("Your command line setting is: \n\t" + json);

		sourceclient = new ElasticsearchConnector(argssetting.getSource_cluster(), argssetting.getSource_host(),
				argssetting.getSource_port()).getClient();
		targetclient = new ElasticsearchConnector(argssetting.getTarget_cluster(), argssetting.getTarget_host(),
				argssetting.getTarget_port()).getClient();

		System.out.println(argssetting.getDsl());

		switch (argssetting.getType().toUpperCase()) {
		case "DATA":
			System.out.println("TYPE IS DATA OK!");
			if (argssetting.getDsl() != null) {
				cpidx.CopyIndexByQueryDsl(sourceclient, argssetting.getSource_index(), targetclient,
						argssetting.getTarget_index(), argssetting.getDsl());

			} else {
				cpidx.CopyIndex(sourceclient, argssetting.getSource_index(), targetclient,
						argssetting.getTarget_index());
			}
			break;
		case "META":
			System.out.println("TYPE IS META OK!");
			cpidx.CopyIndexMetadata(sourceclient, argssetting.getSource_index(), targetclient,
					argssetting.getTarget_index());
			break;
		case "FORCE":
			System.out.println("FORCE OK!");
			if (esidxtools.IndexExistes(targetclient, argssetting.getTarget_index())) {
				esidxtools.DeleteIndex(targetclient, argssetting.getTarget_index());
			}

			cpidx.CopyIndexMetadata(sourceclient, argssetting.getSource_index(), targetclient,
					argssetting.getTarget_index());

			if (argssetting.getDsl() != null) {
				cpidx.CopyIndexByQueryDsl(sourceclient, argssetting.getSource_index(), targetclient,
						argssetting.getTarget_index(), argssetting.getDsl());

			} else {
				cpidx.CopyIndex(sourceclient, argssetting.getSource_index(), targetclient,
						argssetting.getTarget_index());
			}
			break;
		default:
			System.out.println("type must be set [data,meta,force]");
			break;
		}

		sourceclient.close();
		targetclient.close();

	}

}
