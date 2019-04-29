package com.example.demo12345;

import com.alibaba.fastjson.JSON;
import com.example.demo12345.model.AddressIfo;
import com.example.demo12345.service.AddressService;
import com.example.demo12345.utils.DateUtils;
import com.example.demo12345.utils.ObjectToJson;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo12345ApplicationTests {
    /**
     * document相当于mysql的数据库
     * index相当于mysql中的表
     * type 数据类型
     * field 字段
     */
    @Autowired
    private RestHighLevelClient client;  //记得注入client哦~

    //1.创建索引

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void testAdd() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("index");
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("createIndex: " + JSON.toJSONString(createIndexResponse));
    }

    //2.判断索引是否存在
    @Test
    public void existsIndex() throws IOException {
        String index = "index";//需要判断的索引
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("existsIndex: " + exists);
    }

    //3.判断记录是否存在
    @Test
    public void exists() throws IOException {
        String index = "";
        String type = "";
        GetRequest getRequest = new GetRequest(index, type, "数据的主键");
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println("exists: " + exists);
    }

    //4.更新记录信息
    @Test
    public void update() throws IOException {
        String index = "";
        String type = "";
        AddressIfo addressIfo=new AddressIfo();
        //MajorInfo majorInfo = new MajorInfo();
        UpdateRequest request = new UpdateRequest(index, type, addressIfo.getId().toString());
        request.doc(JSON.toJSONString(addressIfo), XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        System.out.println("update: " + JSON.toJSONString(updateResponse));
    }

    //5.删除记录
    public void delete(String index, String type, Long id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id.toString());
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("delete: " + JSON.toJSONString(response));
    }

    //6.查询
    @Test
    public void search() throws IOException {
        String index = "你的index";
        String type = "你的type";
        String name = "搜索的value"; //搜索的value
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchQuery("title", name)); // 这里可以根据字段进行搜索，must表示符合条件的，相反的mustnot表示不符合条件的
        // boolBuilder.should(QueryBuilders.fuzzyQuery("字段", 值)); //模糊搜索
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(100); // 获取记录数，默认10
        sourceBuilder.fetchSource(new String[]{"id", "name"}, new String[]{}); // 第一个是获取字段，第二个是过滤的字段，默认获取全部
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("search: " + JSON.toJSONString(response));
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            System.out.println("search -> " + hit.getSourceAsString());
        }
    }

    @Autowired
    private AddressService addressService;
    //添加记录
    @Test
    public void addValue(){
        while (true) {
            AddressIfo addressIfo = new AddressIfo();
            addressIfo.setId((long)(Math.random()*1000));
            addressIfo.setCreateTime(DateUtils.stringToDate(DateUtils.getCurrTimeStamp()));
            addressIfo.setUpdateTime(DateUtils.stringToDate(DateUtils.getCurrTimeStamp()));
            addressIfo.setAddress(UUID.randomUUID().toString());
        }
    }
}
