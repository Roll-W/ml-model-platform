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

package tech.rollw.machlearn.controller;

import tech.rollw.machlearn.common.ErrorCode;
import tech.rollw.machlearn.data.dto.HttpResponseBody;
import tech.rollw.machlearn.data.dto.HttpResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import space.lingu.light.LightRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Handle {@link LightRuntimeException}
 *
 * @author RollW
 */
@ControllerAdvice
public class SystemExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(SystemExceptionHandler.class);

    @ExceptionHandler(LightRuntimeException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(LightRuntimeException e) {
        logger.error("[SQL] Suspected SQL error. %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                e.getMessage(),
                ErrorCode.getErrorFromThrowable(e),
                e.toString())
        );
    }


    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(NullPointerException e) {
        logger.error("Null exception : %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                e.getMessage(),
                ErrorCode.ERROR_NULL,
                e.toString())
        );
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(FileNotFoundException e) {
        return HttpResponseEntity.create(HttpResponseBody.failure(
                HttpStatus.NOT_FOUND,
                "404 Not found.",
                ErrorCode.ERROR_FILE_NOT_FOUND,
                "404 Not found.")
        );
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(IOException e) {
        logger.error("Error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                HttpStatus.SERVICE_UNAVAILABLE,
                e.getMessage(),
                ErrorCode.getErrorFromThrowable(e),
                e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(Exception e) {
        logger.error("Error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.create(HttpResponseBody.failure(
                HttpStatus.SERVICE_UNAVAILABLE,
                e.getMessage(),
                ErrorCode.getErrorFromThrowable(e),
                e.toString())
        );
    }
}
