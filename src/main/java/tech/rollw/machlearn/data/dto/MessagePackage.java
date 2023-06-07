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

import java.util.function.Function;

/**
 * @author RollW
 */
public record MessagePackage<D>(
        ErrorCode errorCode,
        String message,
        D data
) {
    public MessagePackage(ErrorCode code, D data) {
        this(code, code.toString(), data);
    }

    public HttpResponseBody<D> toResponseBody() {
        if (errorCode.getState()) {
            return HttpResponseBody.success(message(), data());
        }
        return HttpResponseBody.failure(message(), errorCode(), data());
    }

    public <T> HttpResponseBody<T> toResponseBody(Function<D, T> typeTrans) {
        if (errorCode.getState()) {
            return HttpResponseBody.success(message(), typeTrans.apply(data()));
        }
        return HttpResponseBody.failure(message(), errorCode(), typeTrans.apply(data()));
    }
}
