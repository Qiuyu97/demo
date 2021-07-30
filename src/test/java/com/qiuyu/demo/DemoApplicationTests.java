package com.qiuyu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class DemoApplicationTests {


	@Test
	public void getJson() throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://47.115.30.87:9521/get-with-sku?no=20210320112037634WM6110", String.class);
		System.out.println(responseEntity.getBody());
	}


	private int sortBranch(double d1, double d2) {
		int a= Double.compare(d1, d2);
		System.out.println("a:"+a+" d1:"+d1+" d2:"+d2);
		return a;
	}


	@Test
	public void testCase() throws IOException {
		// List<Double> list = Arrays.asList(23.31,313.123,42.0,41.0,1.0,451.12,43.21);
	}

	@Test
	public void testStreamSorted()  {
		List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
		numbers.stream().distinct().map(i->i*i).sorted().forEach(System.out::println);
		System.out.println();
		numbers.stream().distinct().map(i->i*i).sorted((x,y)->y-x).forEach(System.out::println);
		System.out.println();
		System.out.println(numbers.parallelStream().distinct().mapToInt(i->i*i).sum());
	}




}
