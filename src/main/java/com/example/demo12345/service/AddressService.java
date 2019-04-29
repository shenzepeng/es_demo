package com.example.demo12345.service;

import com.example.demo12345.model.AddressIfo;
import org.elasticsearch.client.transport.TransportClient;

public interface AddressService {
    /**
     * 插入一条数据
     * @param addressIfo 对象
     * @param client  连接
     * @param indexName 索引名称
     * @param typeName  type名称
     */
    public void addOneAddress(AddressIfo addressIfo, TransportClient client, String indexName, String typeName);
}
