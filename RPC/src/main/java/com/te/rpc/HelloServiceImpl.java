package com.te.rpc;

public class HelloServiceImpl  implements HelloService{
	public String hello(String name) {
        return "Hello  Te " + name;
    }
}
