package ru.Lyalin.CossakText.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.Lyalin.CossakText.dto.ApiResponseDto;

@Service
public class CertificateTranslatorService {
    @Value("${app.lyalin.cossak-api-url}")
    private String cossakApiUrl;

    public ApiResponseDto getTranslation(byte[] originalImage) {
        RestClient restClient = RestClient.create();

        return restClient.post().
                uri(cossakApiUrl).
                contentType(MediaType.APPLICATION_OCTET_STREAM).
                body(originalImage).
                retrieve()
                .body(ApiResponseDto.class);
    }

}
