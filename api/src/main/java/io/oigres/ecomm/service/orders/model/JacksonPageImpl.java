package io.oigres.ecomm.service.orders.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JacksonPageImpl<T> extends PageResponseImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public JacksonPageImpl(
            @JsonProperty("content") List<T> content,
            @JsonProperty("pageable") io.oigres.ecomm.service.orders.model.JacksonPageableRequestImpl pageable,
            @JsonProperty("total") long total) {
        super(content, pageable, total);
    }
}
