package testing;

import java.io.Serializable;

public class Product implements Serializable {

    private String id;
    private String name;

    // TODO. 需要默认构造器，在POST请求时自动构建model object
    public Product() {
    }

    // public Product(String id, String name) {
    //    this.id = id;
    //    this.name = name;
    // }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
