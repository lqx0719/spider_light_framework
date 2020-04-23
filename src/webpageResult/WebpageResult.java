package webpageResult;

import request.Request;

public interface WebpageResult<T> {
    void process(T item, Request request);
}
