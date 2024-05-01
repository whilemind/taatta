package edu.monash.humanise.smartcity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PayloadService {
    private final PayloadRepository payloadRepository;
    public void decodeUplinkPayload(PayloadUplinkRequest payloadRequest) {
        Decoder decoder = new Decoder(payloadRequest.data());
        decoder.decode();

        Payload payload = Payload.builder()
                .deviceName(payloadRequest.deviceName())
                .data(payloadRequest.data())
                .devEUI(payloadRequest.devEUI())
                .temperature(decoder.getTemperature())
                .tds(decoder.getTds())
                .createdAt(decoder.getGenerate_at())
                .build();
        payloadRepository.save(payload);
    }
}
