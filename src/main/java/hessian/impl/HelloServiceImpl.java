package hessian.impl;

import hessian.share.HelloService;
import org.springframework.stereotype.Service;

/**
 * Created by root on 15-3-11.
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello "+name;
    }
}
