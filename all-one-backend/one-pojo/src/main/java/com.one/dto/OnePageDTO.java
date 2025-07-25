package com.one.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OnePageDTO implements Serializable {

    private List<OneImageDTO> images;

    private List<OneContentDTO> contents;
}
