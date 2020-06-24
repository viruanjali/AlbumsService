1) Run Elasticsearch Docker Container
docker network create --driver bridge api_network
docker run -d -v esdata1:/usr/share/elasticsearch/data --network api_network --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.7.1


#docker run -d --name elasticsearch --network api_network -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.3.0




2) Run Kibana Docker Container

docker run -d --network api_network -p 5601:5601 kibana:7.7.1
docker run -d -e "ELASTICSEARCH_HOSTS=http://172.31.14.197:9200" -p 5601:5601 kibana:7.7.1

Portal URI http://PublicIP:5601/



3) Run Albums Microservice Docker Container
#############AlbumsService dockerfile#################
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/PhotoAppApiAlbums-1.0-SNAPSHOT.jar AlbumsService.jar
ENTRYPOINT ["java", "-jar", "AlbumsService.jar"]
#################End AlbumsService #dockerfile########################

docker build --tag=albumsservice --force-rm=true .
docker tag 7af6fa79ddc8 amsidhlokhande/albumsservice
docker push amsidhlokhande/albumsservice

#docker run -d --network host -e "spring.cloud.config.uri=http://172.31.38.45:8012" -v /home/ec2-user/photoapp-logs/:$HOME/PhotoApp/logs/ amsidhlokhande/albumsservice
docker run -d --network host -e "spring.cloud.config.uri=http://172.31.4.122:8012" -v /home/ec2-user/photoapp-logs:/api-logs amsidhlokhande/albumsservice
--Here --network host is port dynamic port enabling in docker container





4) Run Logstash for Albums Microservice Docker Container

Create three files dockerfile, logstash.conf and logstash.yml
#####################dockerfile##############
FROM logstash:7.7.1
RUN rm -f /usr/share/logstash/pipeline/logstash.conf
COPY logstash.conf /usr/share/logstash/pipeline/logstash.conf
COPY logstash.yml config/logstash.yml


Environment Variable to be set for logstash.conf 
LOGFILE_TYPE  for example users-ws-log
LOGFILE_PATH  for example /api-logs/users-ws.log
ELASTIC_SEARCH_HOST_AND_PORT for example 172.31.7.205:9200 
ELASTIC_SEARCH_INDEX for example users-ws
######################logstash.conf####################
input { 
		file { 
		   type => "${SERVICE_NAME}-log"
		   path => "/api-logs/albums-ws.log"
		}
		
    }
	  
output {
  
   elasticsearch { 
	   hosts => ["172.31.7.205:9200"]
	   index => "albums-ws-%{+YYYY.MM.dd}"
   }
   
  stdout { codec => rubydebug }
}

Environment Variables to be set for logstahs.yml file
ELASTIC_SEARCH_HOST_URI for example http://172.31.7.205:9200
####################logstash.yml##########################

http.host: "0.0.0.0"
xpack.monitoring.elasticsearch.hosts: ["http://172.31.7.205:9200"]
######################################
Keep all files in one local folder and build the images

docker build --tag=logstash --force-rm=true .
docker tag 4a0676c36c6b amsidhlokhande/common-logstash
docker push amsidhlokhande/common-logstash

#docker run -d --name albumsservice-logstash -v /home/ec2-user/photoapp-logs:/api-logs amsidhlokhande/albumsservice-logstash

docker run -d --name albumsservice-logstash -e "LOGFILE_TYPE=albums-ws-log" -e "LOGFILE_PATH=/api-logs/albums-ws.log"  -e "ELASTIC_SEARCH_HOST_AND_PORT=172.31.14.197:9200" -e "ELASTIC_SEARCH_INDEX=albums-ws" -e "ELASTIC_SEARCH_HOST_URI=http://172.31.14.197:9200"  -v /home/ec2-user/photoapp-logs:/api-logs amsidhlokhande/common-logstash

You can also try with ELASTICSEARCH_HOSTS='["http://10.45.3.2:9220","http://10.45.3.1:9230"]'


