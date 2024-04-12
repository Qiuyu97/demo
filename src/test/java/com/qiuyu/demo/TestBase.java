package com.qiuyu.demo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Junit5
 * {@link org.junit.jupiter.api.Test}
 * {@link BeforeEach}
 * {@link AfterEach}
 *
 * Junit4
 * {@link org.junit.Test}
 * {@link org.junit.Before}
 * {@link org.junit.After}
 *
 * {@link <a>https://junit.org/junit5/docs/5.0.0/user-guide/#migrating-from-junit4-tips</a>}
 */
@SpringBootTest(classes = {DemoApplication.class})
@AutoConfigureMockMvc
public abstract class TestBase {

    private HttpHeaders httpHeaders;

    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    public void init() throws UnsupportedEncodingException {
        System.out.println("-----初始化-----");
    }

    @AfterEach
    public void after() {
        System.out.println("-----测试调用完成-----");
    }

    private MultiValueMap<String, String> convert(Object params) {
        ObjectMapper objectMapper = new ObjectMapper();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.setAll(objectMapper.convertValue(params, new TypeReference<Map<String, String>>() {
        }));
        return parameters;
    }

    protected String get(String url) {
        return this.get(url, null);
    }

    protected String get(String url, Object params) {
        return this.get(url, this.convert(params));
    }

    protected String get(String url, MultiValueMap<String, String> params) {
        try {
            MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(url).headers(httpHeaders);
            if (!CollectionUtils.isEmpty(params)) {
                get.params(params);
            }
            MvcResult mvcResult = mockMvc.perform(get)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            return mvcResult.getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StrUtil.EMPTY_JSON;
    }

    protected String post(String url, Object body) {
        return this.post(url, null, JSON.toJSONString(body), MediaType.APPLICATION_JSON);
    }

    protected String post(String url, MultiValueMap<String, String> params) {
        return this.post(url, params, null, null);
    }

    protected String post(String url, MultiValueMap<String, String> params, String body, MediaType contentType) {
        try {
            MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(url).headers(httpHeaders);
            if (!CollectionUtils.isEmpty(params)) {
                post.params(params);
            }
            if (body != null) {
                post.contentType(contentType).content(body);
            }
            MvcResult mvcResult = mockMvc.perform(post)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            return mvcResult.getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StrUtil.EMPTY_JSON;
    }

    protected String delete(String url, MultiValueMap<String, String> params) {
        try {
            MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete(url).headers(httpHeaders);
            if (!CollectionUtils.isEmpty(params)) {
                delete.params(params);
            }
            MvcResult mvcResult = mockMvc.perform(delete)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            return mvcResult.getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StrUtil.EMPTY_JSON;
    }




}