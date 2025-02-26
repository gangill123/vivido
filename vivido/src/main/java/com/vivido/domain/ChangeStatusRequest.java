package com.vivido.domain;

import lombok.Data;
import java.util.List;

@Data
public class ChangeStatusRequest {
	
    private List<String> productIds;
    private int status;
}
