# elastictransfor


## Purpose

This programe is a command line tools for copy elasticsearch index.

# License

Apache License 2.0


## How Install


```
git clone https://github.com/jiashiwen/elastictransfor 
```
```
cd elastictransfor
```
```
gradle clean build
```

you should see 'build/libs/elastictransfor-1.0.jar‘




## Usage

### command:
```
java -jar build/libs/elastictransfor-1.0.jar --help
```
### out put:
```
Usage: java -jar elastictransfor.jar [options] 
  Options:
    --dsl
      elasticsearch query dsl for Preform a partial transfor based on search 
      results.you must make content of this variable between '',just like  
      '{"query":{"term":{"word.primitive":{"value":"keywork"}}}}' 
    --help

      Default: false
    --script_file
      execute script file write by json 
    --source_cluster
      Source elasticsearch cluster name,default is 'elasticsearch 
      Default: elasticsearch
    --source_host
      Source elasticsearch cluster one of master ip address,default is 
      '127.0.0.1'. 
      Default: 127.0.0.1
    --source_index
      Source index name 
    --source_port
      Source port 
      Default: 9300
    --target_cluster
      Target elasticsearch cluster name,default is 'elasticsearch 
      Default: elasticsearch
    --target_host
      Target elasticsearch cluster one of master ip address,default is 
      '127.0.0.1'. 
      Default: 127.0.0.1
    --target_index
      Target index name 
    --target_port
      Target port 
      Default: 9300
    --type
      Transfor type value is [data,meta,force] and default value is 'meta'.If 
      value is 'metadata' try to create a new empty target index as 
      source;'data' copy source index documents to target index; 'force' 
      delete target index if exists and copy source index to target index. 
      Default: meta

```

## Example 
### command
```
java -jar build/libs/elastictransfor-1.0.jar --source_cluster sourceclustername \
 --source_host 10.0.0.1 \
 --source_index sourceindex \
 --target_cluster targetclustername \
 --target_host 10.1.0.1 \
 --target_index targetindex \
 --type force
 
```

### out put:
```
2016-08-02 15:31:59 [INFO] - {
  "help" : false,
  "source_cluster" : "sourceclustername",
  "source_host" : "10.0.0.1",
  "source_port" : 9300,
  "source_index" : "sourceindex",
  "target_cluster" : "targetclustername",
  "target_host" : "10.1.0.1",
  "target_port" : 9300,
  "target_index" : "targetindex",
  "type" : "force"
}
2016-08-02 15:31:59 [INFO] - [Calvin Rankin] modules [], plugins [], sites []
2016-08-02 15:31:59 [INFO] - [Scarlet Spider] modules [], plugins [], sites []
FORCE OK!
2016-08-02 15:31:59 [INFO] - targetindex deleted!
2016-08-02 15:32:00 [INFO] - Target index targetindex create complete!
2016-08-02 15:32:00 [INFO] - 129 documents putted!!
2016-08-02 15:32:00 [INFO] - copy index sourceindex targetindex complete
```

## Example by query
### command
```
java -jar elastictransfor-1.0.jar \
 --source_cluster sourceclustername \
 --source_host 10.0.0.1 \
 --source_index sourceindex \
 --target_cluster targetclustername \
 --target_host 10.1.0.1 \
 --target_index targetindex \
 --type data \
 --dsl '{"query":{"term":{"word.primitive":{"value":"keyword"}}}}'
```
### out put:
```
2016-08-03 12:35:26 [INFO] - Your setting is: 
	{
  "help" : false,
  "source_cluster" : "sourceclustername",
  "source_host" : "10.0.0.1",
  "source_port" : 9300,
  "source_index" : "sourceindex",
  "target_cluster" : "targetclustername",
  "target_host" : "10.1.0.1",
  "target_port" : 9300,
  "target_index" : "targetindex",
  "type" : "data",
  "dsl" : "{\"query\":{\"term\":{\"word.primitive\":{\"value\":\"keyword\"}}}}"
}
2016-08-03 12:35:26 [INFO] - [Jean Grey] modules [], plugins [], sites []
2016-08-03 12:35:27 [INFO] - [Man-Spider] modules [], plugins [], sites []
{"query":{"term":{"word.primitive":{"value":"keyword"}}}}
TYPE IS DATA OK!
2016-08-03 12:35:27 [INFO] - 128 documents putted!!
2016-08-03 12:35:27 [INFO] - copy index sourceindex 1 documents to targetindex complete
```

## Example by use json execute script
### command
```
java -jar elastictransfor-1.0.jar --script_file script.json
```
### script.json 
```
{
    "source_cluster": "es-source",
    "source_host": "10.0.0.22",
    "source_port": 9300,
    "target_cluster": "es-target",
    "target_host": "10.10.10.32",
    "target_port": 9300,
    "indexes": [
        {
            "source_index": "index1",
            "target_index": "index1",
            "type": "force"
        },
        {
            "source_index": "index2",
            "target_index": "index2-copy",
            "type": "meta"
        }
    ]
}

```


## Use Docker

### command

```
sudo docker run --rm jiashiwen/elastictransfor --source_cluster sourceclustername \
 --source_host 10.0.0.1 \
 --source_index sourceindex \
 --target_cluster targetclustername \
 --target_host 10.1.0.1 \
 --target_index targetindex \
 --type force
```
### out put:
```
2016-08-02 15:31:59 [INFO] - {
  "help" : false,
  "source_cluster" : "sourceclustername",
  "source_host" : "10.0.0.1",
  "source_port" : 9300,
  "source_index" : "sourceindex",
  "target_cluster" : "targetclustername",
  "target_host" : "10.1.0.1",
  "target_port" : 9300,
  "target_index" : "targetindex",
  "type" : "force"
}
2016-08-02 15:31:59 [INFO] - [Calvin Rankin] modules [], plugins [], sites []
2016-08-02 15:31:59 [INFO] - [Scarlet Spider] modules [], plugins [], sites []
FORCE OK!
2016-08-02 15:31:59 [INFO] - targetindex deleted!
2016-08-02 15:32:00 [INFO] - Target index targetindex create complete!
2016-08-02 15:32:00 [INFO] - 129 documents putted!!
2016-08-02 15:32:00 [INFO] - copy index sourceindex targetindex complete
```
## Use Docker with script
```
sudo docker run  -v ${PWD}:/script --rm jiashiwen/elastictransfor  --script_file /script/script.json
```
