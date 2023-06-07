/*
 * Copyright (C) 2022 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.rollw.machlearn.data.dto;

import tech.rollw.machlearn.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * Http响应实体。携带Http状态码及Header。
 *
 * @author RollW
 */
public class HttpResponseEntity<D> extends ResponseEntity<HttpResponseBody<D>> {
    public HttpResponseEntity(HttpStatus status) {
        super(status);
    }

    public HttpResponseEntity(HttpResponseBody<D> body) {
        super(body, null, body.getStatus());
    }

    public HttpResponseEntity(HttpResponseBody<D> body, MultiValueMap<String, String> headers) {
        super(body, headers, body.getStatus());
    }

    public static <D> HttpResponseEntity<D> create(HttpResponseBody<D> body) {
        return new HttpResponseEntity<>(body);
    }

    public static <D> HttpResponseEntity<D> create(HttpResponseBody<D> body,
                                                   MultiValueMap<String, String> headers) {
        return new HttpResponseEntity<>(body, headers);
    }

    public static <D> HttpResponseEntity<D> success() {
        return HttpResponseEntity.create(
                HttpResponseBody.success()
        );
    }

    public static <D> HttpResponseEntity<D> success(String message) {
        return HttpResponseEntity.create(
                HttpResponseBody.success(message)
        );
    }

    public static <D> HttpResponseEntity<D> success(String message, MultiValueMap<String, String> headers) {
        return HttpResponseEntity.create(
                HttpResponseBody.success(message), headers
        );
    }

    public static <D> HttpResponseEntity<D> success(String message, D data) {
        return HttpResponseEntity.create(
                HttpResponseBody.success(message, data)
        );
    }

    public static <D> HttpResponseEntity<D> success(String message, D data, MultiValueMap<String, String> headers) {
        return HttpResponseEntity.create(
                HttpResponseBody.success(message, data), headers
        );
    }

    public static <D> HttpResponseEntity<D> success(D data) {
        return HttpResponseEntity.create(
                HttpResponseBody.success(data)
        );
    }

    public static <D> HttpResponseEntity<D> success(D data, MultiValueMap<String, String> headers) {
        return HttpResponseEntity.create(
                HttpResponseBody.success(data), headers
        );
    }

    // for semantic control

    public static <D> HttpResponseEntity<D> failure(HttpStatus status,
                                                    String message,
                                                    ErrorCode errorCode,
                                                    D data) {
        return HttpResponseEntity.create(
                HttpResponseBody.failure(status, message, errorCode, data)
        );
    }

    public static <D> HttpResponseEntity<D> failure(HttpStatus status,
                                                    String message,
                                                    ErrorCode errorCode,
                                                    D data,
                                                    MultiValueMap<String, String> headers) {
        return HttpResponseEntity.create(
                HttpResponseBody.failure(status, message, errorCode, data), headers
        );
    }

    public static <D> HttpResponseEntity<D> failure(String message,
                                                    ErrorCode errorCode) {
        return HttpResponseEntity.create(
                HttpResponseBody.failure(message, errorCode)
        );
    }

    public static <D> HttpResponseEntity<D> failure(String message,
                                                    ErrorCode errorCode,
                                                    MultiValueMap<String, String> headers) {
        return HttpResponseEntity.create(
                HttpResponseBody.failure(message, errorCode), headers
        );
    }

    public static <D> HttpResponseEntity<D> failure(String message,
                                                    ErrorCode errorCode,
                                                    D data) {
        return HttpResponseEntity.create(
                HttpResponseBody.failure(message, errorCode, data)
        );
    }

    public static <D> HttpResponseEntity<D> failure(String message,
                                                    ErrorCode errorCode,
                                                    D data, MultiValueMap<String, String> headers) {
        return HttpResponseEntity.create(
                HttpResponseBody.failure(message, errorCode, data), headers
        );
    }
}
