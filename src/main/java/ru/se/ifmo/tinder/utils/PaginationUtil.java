package ru.se.ifmo.tinder.utils;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public class PaginationUtil {

    // Метод для создания заголовков с информацией о пагинации
    public static HttpHeaders createPaginationHeaders(Page<?> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements())); // Общее количество элементов
        headers.add("X-Total-Pages", String.valueOf(page.getTotalPages()));    // Общее количество страниц
        headers.add("X-Current-Page", String.valueOf(page.getNumber()));       // Текущая страница
        return headers;
    }

    public static <T> HttpHeaders endlessSwipeHeadersCreate(Page<T> entityList) {
        HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.set("app-current-page-num", String.valueOf(entityList.getNumber()));
        responseHeaders.set("app-page-has-next", String.valueOf(entityList.hasNext()));
        return responseHeaders;
    }
}
