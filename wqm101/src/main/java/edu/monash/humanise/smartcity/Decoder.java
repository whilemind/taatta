package edu.monash.humanise.smartcity;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@Getter
public class Decoder {
    private final String encoded;

    private double temperature;
    private double tds;
    private LocalDateTime generate_at;
    public Decoder(String encoded) {
        this.encoded = encoded;
    }

    public void decode() {
        int tmp;
        byte[] decoded;
        DecimalFormat decFormatTwoDigit = new DecimalFormat("0.00");

        decoded = Base64.getDecoder().decode(this.encoded);
        log.info("In the decoder class with encoded value {}", this.encoded);
        log.info("decoded value {}", decoded);
        // Channel 1 is for temperature sensor.
        if((decoded[0] & 0xFF) == 0x1) {
            // temperature code is 0x67 provided by Cayenne LPP (Low Power Payload)
            if((decoded[1] & 0xFF) == 0x67) {
                tmp = decoded[3] & 0xFF;
                tmp = (tmp << 8);
                tmp = tmp | (decoded[2] & 0xFF);

                log.info("The temperature bytes is {} and {} value is {}", decoded[2], decoded[3], (tmp / 16));

                // using two's compliment than divided by 16
                this.temperature = Double.valueOf(decFormatTwoDigit.format(tmp / 16));
            } else {
                log.warn("First sensor code {} is not valid.", decoded[1]);
            }
        } else {
            log.error("First channel number {} is not valid", decoded[0]);
        }

        // Channel 2 is for temperature sensor.
        if((decoded[4] & 0xFF) == 0x2) {
            // temperature code is 0x67 provided by Cayenne LPP (Low Power Payload)
            if((decoded[5] & 0xFF) == 0x68) {
                // using two's compliment than divided by 16
                tmp = (decoded[6] & 0xFF);
                tmp = (tmp << 8);
                tmp = tmp | (decoded[7] & 0xFF);
                log.info("The TDS bytes is {} and {} value is {}", decoded[6], decoded[7], tmp);
                this.tds = Double.valueOf(decFormatTwoDigit.format(tmp));
            } else {
                log.warn("Second sensor code {} is not valid.", decoded[1]);
            }
        } else {
            log.error("Second channel number {} is not valid", decoded[0]);
        }
        // channel 3 is for the generation time.
        int year, month, day, hour, min, sec, milsec;
        if((decoded[8] & 0xFF) == 3) {
            year = ((decoded[10] & 0xFF) << 8) | (decoded[11] & 0xFF);
            month = (decoded[12] & 0xFF);
            day = (decoded[13] & 0xFF);

            hour = (decoded[14] & 0xFF);
            min = (decoded[15] & 0xFF);
            sec = (decoded[16] & 0xFF);
            milsec = ((decoded[17] & 0xFF) << 8) | (decoded[18] & 0xFF);
//            String strDt = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec + "." + milsec;
            String strDt = year + "-" +
                    ((month < 10)? "0"+month : month) + "-" +
                    ((day < 10)? "0"+day: day) + " " +
                    ((hour < 10)? "0"+hour : hour) + ":" +
                    ((min < 10)? "0"+min: min) + ":" +
                    ((sec < 10)? "0"+sec: sec) + "." +
                    ((milsec < 10)? "00"+milsec : (milsec < 100)? "0"+milsec: milsec);
            log.info("The generation time stamp: {}", strDt);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            this.generate_at = LocalDateTime.parse(strDt, formatter);
        } else {
            log.error("Time stamp is missing.");
        }
    }
}
