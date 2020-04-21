package response;

import java.util.ArrayList;
import java.util.List;

import request.Request;

public class Result<T> {
    private T item;
    private List<Request> requests = new ArrayList<>();
    public Result() {}
    public Result(T item) {
        this.item = item;
    }
    
    public Result addRequest(Request request) {
        this.requests.add(request);
        return this;
    }
    
    public Result addRequest(List<Request> requests) {
        if(this.requests.isEmpty()) {
            this.requests.addAll(requests);
        }
        return this;
    }
    
    public void setItem(T item) {
        this.item = item;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public T getItem() {
        return item;
    }

}
