package edu.monash.humanise.smartcity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payload {

    @Id
    @SequenceGenerator(
            name = "wqm101_id_sequence",
            sequenceName = "wqm101_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "wqm101_id_sequence"
    )
    private Integer id;
    private String deviceName;
    private String data;
    private String devEUI;
    private double temperature;
    private double tds;


    private LocalDateTime createdAt;

//    @UpdateTimestamp
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

}
