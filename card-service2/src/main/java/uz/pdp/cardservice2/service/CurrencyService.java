package uz.pdp.cardservice2.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import uz.pdp.cardservice2.exceptions.RestException;
import uz.pdp.cardservice2.utils.MessageConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class CurrencyService {

    private final WebClient webClient;

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://cbu.uz/ru/arkhiv-kursov-valyut/json/all/").build();
    }

    public Mono<Double> getRateById(int id) {

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = currentDate.format(formatter);


        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("date", date).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getInt("id") == id) {
                            return jsonObject.getDouble("Rate");
                        }
                    }
                    throw new RestException(MessageConstants.ID_NOT_FOUNDS, HttpStatus.NOT_FOUND);
                })
                .onErrorResume(WebClientResponseException.class, e -> Mono.error(new RuntimeException("Failed to fetch data", e)));
    }


}
