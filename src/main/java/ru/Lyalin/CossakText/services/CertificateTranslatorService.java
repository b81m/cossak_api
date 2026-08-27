package ru.Lyalin.CossakText.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.Lyalin.CossakText.dto.ApiResponseDto;
import ru.Lyalin.CossakText.exceptions.ExternalServiceException;

@Service
public class CertificateTranslatorService {
    private final String cossakApiUrl;
    private final RestClient restClient;

    public CertificateTranslatorService(@Value("${app.lyalin.cossak-api-url}") String cossakApiUrl) {
        this.cossakApiUrl = cossakApiUrl;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(60000);
        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .build();
    }

    public ApiResponseDto getTranscript(byte[] originalImage) {
        try {
            ApiResponseDto response = restClient.post()
                    .uri(cossakApiUrl)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(originalImage)
                    .retrieve()
                    .body(ApiResponseDto.class);

            if (response == null || response.transcribedImage() == null) {
                throw new ExternalServiceException("Сервер вернул плохой ответ");
            }
            return response;
        } catch (RestClientException e) {
            throw new ExternalServiceException("Сервис недоступен");
        }
    }
}
