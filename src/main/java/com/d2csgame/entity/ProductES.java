package com.d2csgame.entity;

import com.d2csgame.server.image.model.response.ImageRes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products")  // The index name should match the one in your Logstash config
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductES {

    @Id
    private Long id;

    private String name;

    @Field(type = FieldType.Text)
    private String description;

    private Double price;

    @Field(type = FieldType.Keyword)
    private String productType;

    private String demo;

    private ImageRes imageRes;

    // Add other necessary fields

    // Getters and Setters
}
