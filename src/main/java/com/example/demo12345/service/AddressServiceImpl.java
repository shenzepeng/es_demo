package com.example.demo12345.service;

import com.example.demo12345.model.AddressIfo;
import com.example.demo12345.utils.ObjectToJson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    /**
     * document相当于mysql的数据库
     * index相当于mysql中的表
     * type 数据类型
     * field 字段
     */
    @Autowired
    private RestHighLevelClient client;  //记得注入client哦~


    @Override
    public void addOneAddress(AddressIfo addressIfo, TransportClient client, String indexName, String typeName) {
        //将对象转换为json
        String sourse = ObjectToJson.toJson(addressIfo);
        //设置index，type，id用uuid代替。
        IndexResponse response = client.prepareIndex(indexName, indexName)
                //必须为对象单独指定ID
                .setId(addressIfo.getId().toString())
                .setSource(sourse)
                .execute()
                .actionGet();
        //多次index这个版本号会变
        //ConEsUtil.closeConEs();
    }
}
